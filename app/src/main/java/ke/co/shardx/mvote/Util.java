package ke.co.shardx.mvote;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

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
                            System.out.println(Result);

                             Log.e("Voting Error","Attempt to vote twice");

                            System.out.println("Inserted" + responsePOST.toString());
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
    public static boolean  checkExist(final String userID,final String Password){
    final boolean[] registered = new boolean[1];


        new Thread(new Runnable() {
        int progressBarStatus = 0;

        public void run() {
            while (progressBarStatus < 100) {
                String Result;



                /* Start Insert */

                HttpClient client = new DefaultHttpClient();
                String postURL = "http://student.ktvc.ac.ke/Voting-App-Server-Side-master/m-vote/test.php";
                HttpPost post = new HttpPost(postURL);
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("password", Password.trim()));
                params.add(new BasicNameValuePair("id", userID.trim()));



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
                        Result=Result.trim();
                        System.out.println("Result From DB "+Result);
                        if (Result.equals("true")){
                            registered[0] =true;
                            System.out.println("Registered Now is " + registered[0]);
                        }
                        progressBarStatus=100;
                    }



                    /* End Insert  */

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (progressBarStatus >= 100) {

            }

        }
    }).start();
System.out.println("Registered is now is " + registered[0]);
        try {
        Thread.sleep(2000);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
        System.out.println("Registered  Finaly is " + registered[0]);
        return registered[0];

}
    public static String encryptPassword(String password)
    {
        String sha1 = "";
        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest()).toUpperCase();
        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch(UnsupportedEncodingException e)
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




}
