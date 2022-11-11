package ke.co.shardx.mvote;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.HTTP;
import cz.msebera.android.httpclient.util.EntityUtils;

public class Util {
    static String[] date;
    public void voteForCandidate(final String Candidate,final String Chair,int CurrentVotes,final String userID) {
        final int Votes = CurrentVotes + 1;

        new Thread(new Runnable() {
            int progressBarStatus = 0;

            public void run() {
                while (progressBarStatus < 100) {
                    String Result;
                    /* Start Insert */
                    //   Log.i("Voting Activity",userID);
                    HttpClient client = new DefaultHttpClient();
                    String postURL = "http://student.ktvc.ac.ke/Voting-App-Server-Side-master/m-vote/vote.php";
                    HttpPost post = new HttpPost(postURL);
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("votes", String.valueOf(Votes)));
                    params.add(new BasicNameValuePair("ID", userID));
                    params.add(new BasicNameValuePair("Seat", Chair));
                    params.add(new BasicNameValuePair("Candidate", Candidate));


                    UrlEncodedFormEntity ent = null;
                    try {
                        ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    post.setEntity(ent);
                    HttpResponse responsePOST = null;
                    try {
                        responsePOST = client.execute(post);

                        HttpEntity resEntity = responsePOST.getEntity();
                        if (resEntity != null) {
                            Result = EntityUtils.toString(resEntity);
                            Result = Result.trim();


                            Log.i("Voting Blocked","Voted Attempt Blocked!");
                            progressBarStatus = 100;
                        }



                        /* End Insert  */

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (progressBarStatus >= 100) {
                    try {
                        Thread.sleep(3000);


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            }
        }).start();

    }
    public static String encryptPassword(String password)
    {
        String sha1 = "";
        try
        {
            MessageDigest crypt = MessageDigest.getInstance("MD5");
            crypt.reset();
            crypt.update(password.getBytes(StandardCharsets.UTF_8));
            sha1 = byteToHex(crypt.digest()).toUpperCase();
        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return sha1;
    }

    private static String byteToHex(final byte[] hash)
    {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    public String[] getDates() {
        date = new String[4];
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                HttpClient httpclient = new DefaultHttpClient();
                String postURL = "http://student.ktvc.ac.ke/Voting-App-Server-Side-master/m-vote/checkdates.php";
                HttpPost httppost = new HttpPost(postURL);
                List<NameValuePair> list = new ArrayList<NameValuePair>();

                InputStream inputStream = null;
                String result = null;
                try {
                    UrlEncodedFormEntity ent = new UrlEncodedFormEntity(list, HTTP.UTF_8);
                    httppost.setEntity(ent);

                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();

                    inputStream = entity.getContent();
                    // json is UTF-8 by default
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (Exception e) {
                    // Oops
                } finally {
                    try {
                        if (inputStream != null) inputStream.close();
                    } catch (Exception squish) {
                    }
                }
                return result;
            }

            //   System.out.println(result);
            @Override
            protected void onPostExecute(String result) {


            }




        }
        GetDataJSON g = new GetDataJSON();

        try {
                String myJSON = g.execute().get();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                JSONObject jsonObj = new JSONObject(myJSON);
                JSONArray dates = jsonObj.getJSONArray("dates");

                JSONObject c = dates.getJSONObject(0);
                String regStart = c.getString("RegStart");
                String regEnd = c.getString("RegStop");
                String voteStart = c.getString("VotingStart");
                String voteEnd = c.getString("VotingStop");
                Log.i("Dates", "Registration Dates:" + regStart + "-->" + regEnd + "Voting Dates:" + voteStart + "-->" + voteEnd);
                date[0] = regStart;
                date[1] = regEnd;
                date[2] = voteStart;
                date[3] = voteEnd;

        } catch (JSONException e) {
                e.printStackTrace();
        }
         catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return date;
    }




}
