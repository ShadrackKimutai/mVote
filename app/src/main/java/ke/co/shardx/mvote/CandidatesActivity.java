package ke.co.shardx.mvote;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
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

/**
 * Created by Shady on 03-Mar-17.
 */

public class CandidatesActivity extends Activity {

    String myJSON;
    public static String position = "";
    public static String userID="";
    private static final String TAG_RESULTS = "result";
    private static final String TAG_ID = "cid";
    private static final String TAG_NAME = "name";
    private static final String TAG_VOTES = "votes";
    private Util util = new Util();
    private boolean voted=false;


    JSONArray peoples = null;

    ArrayList<HashMap<String, String>> personList;

    ListView list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidates);
        list = (ListView) findViewById(R.id.listView);
        personList = new ArrayList<HashMap<String, String>>();
        Bundle bundle = getIntent().getExtras();
        userID=bundle.getString("user");
        position = bundle.getString("seat");
        //System.out.println(position);

        checkExist();
        getData();



        list.setAdapter(new ArrayAdapter<String>(this, R.layout.activity_candidates));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {


                final int innerPos = pos;
                String candidate = personList.get(innerPos).get("name");
                int currentVotes = Integer.parseInt(personList.get(innerPos).get("votes"));
                voteForSelectedItem(candidate, currentVotes,userID);

            }
        });
    }

    private void voteForSelectedItem(final String candidate, final int currentVotes, String userID) {

        System.out.println(candidate + "Current Votes:" + currentVotes);
        final Handler handler = new Handler();
        final String user = Util.encryptPassword(userID); // encrypt fpor privacy
        new Thread() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        final Util util = new Util();

                        try {
                            if (Looper.myLooper() == null) {
                                Looper.prepare();
                            }
                            new AlertDialog.Builder(CandidatesActivity.this)
                                    .setTitle("Confirm Selection")
                                    .setMessage("Are you sure you wish to nominate " + candidate.toUpperCase() + " for the position of " + position.toUpperCase() + " \t If Yes your vote will be cast and cannot be reversed")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // System.out.println("Call your Function");
                                            //Log.i("ID in CandidateActivity",user);
                                            util.voteForCandidate(candidate, position, currentVotes, user);

                                            CandidatesActivity.this.finish();
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })

                                    .setIcon(R.drawable.icon)
                                    .show();
                            Thread.sleep(200);
                            Looper.loop();

                        } catch (Throwable throwable) {
                            Log.e("Error", "Halted due to thread Error", throwable);
                        }
                    }
                });

            }
        }.start();

    }

    protected void checkExist() {
       // final String imei = getImei();
        new Thread(new Runnable() {
            int progressBarStatus = 0;
            //Context context=CandidatesActivity.this.getApplicationContext();
            public void run() {
                while (progressBarStatus < 100) {
                    String Result;
                    /* Start Insert */
                    HttpClient client = new DefaultHttpClient();
                    String postURL = "http://student.ktvc.ac.ke/Voting-App-Server-Side-master/m-vote/check.php";
                    HttpPost post = new HttpPost(postURL);
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("seat", String.valueOf(position)));
                    params.add(new BasicNameValuePair("ID", Util.encryptPassword(userID)));

                    Log.e("Show Check Error","Seat:"+String.valueOf(position)+" UID:"+Util.encryptPassword(userID));

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
                          //  System.out.println(Result);
                            if (Result.equals("1")) {
                                if (Looper.myLooper() == null) {
                                    Looper.prepare();
                                }

                                   new AlertDialog.Builder(CandidatesActivity.this)

                                           .setTitle("Voting Attempt Blocked!")
                                           .setCancelable(false)
                                           .setMessage("You have already voted for a candidate vying for this seat. Attempt Blocked")
                                           .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                               @Override
                                               public void onClick(DialogInterface dialog, int which) {

                                                   finish();

                                               }
                                           })
                                           .setIcon(R.drawable.mvote)
                                           .show();
                                   Thread.sleep(200);
                                   Looper.loop();

                            }

                        }

                    } catch (IOException iox) {
                        Log.e("Error", iox.getMessage());

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (WindowManager.BadTokenException e){
                        e.printStackTrace();

                        finish();
                    }
                }
            }
        }).start();

    }

    protected void showList() {
        Log.i("JSON Dump", "[" + myJSON + "]");
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < peoples.length(); i++) {
                JSONObject c = peoples.getJSONObject(i);
                String id = c.getString(TAG_ID);
                String name = c.getString(TAG_NAME);
                String votes = c.getString(TAG_VOTES);

                HashMap<String, String> persons = new HashMap<String, String>();

                persons.put(TAG_ID, id);
                persons.put(TAG_NAME, name);
                persons.put(TAG_VOTES, votes);

                personList.add(persons);
            }

            ListAdapter adapter = new SimpleAdapter(
                    CandidatesActivity.this, personList, R.layout.list_row,
                    new String[]{TAG_NAME, TAG_VOTES},
                    new int[]{R.id.name, R.id.votes}
            );

            list.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getData() {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {


                HttpClient httpclient = new DefaultHttpClient();
                String postURL = "http://student.ktvc.ac.ke/Voting-App-Server-Side-master/m-vote/candidates.php";
                HttpPost httppost = new HttpPost(postURL);
                List<NameValuePair> list = new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("seat", position));
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

            @Override
            protected void onPostExecute(String result) {
               // System.out.println(result);
                int x=result.length();//----> this is a Hack to extract empty Json Objects since non empty ones are longer than 20

                if (x<20) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (Looper.myLooper() == null) {
                                    Looper.prepare();
                                }
                                new AlertDialog.Builder(CandidatesActivity.this)

                                        .setTitle("Candidates Missing")
                                        .setCancelable(false)
                                        .setMessage("There is no candidate vying for this seat!")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();

                                            }
                                        })
                                        .setIcon(android.R.drawable.stat_notify_error)
                                        .show();
                                Thread.sleep(200);
                                Looper.loop();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } else {
                    myJSON = result;
                    showList();
                }
            }

        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }

    public String getImei() {
        //TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return ""; //telephonyManager.getSimSerialNumber();
    }
}