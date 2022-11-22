package ke.co.shardx.mvote;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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

public class ResultsActivity extends AppCompatActivity {
    ListView list;
    public static String position = "";
    private String myJSON;
    private JSONArray peoples;
    private static final String TAG_RESULTS = "result";
    private static final String TAG_ID = "cid";
    private static final String TAG_NAME = "name";
    private static final String TAG_VOTES = "votes";
    private ArrayList<HashMap<String, String>> personList;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_results);
        list = (ListView) findViewById(R.id.listView);
        Bundle bundle = getIntent().getExtras();
        personList = new ArrayList<HashMap<String, String>>();

        position = bundle.getString("seat");
        getData();



        list.setAdapter(new ArrayAdapter<String>(this, R.layout.activity_candidates));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
/*
        WebView webView = (WebView) findViewById(R.id.webview);
        webView.loadUrl("https://student.ktvc.ac.ke/Voting-App-Server-Side-master/mvote/results.php");
webView.setOnTouchListener(new View.OnTouchListener() {
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }
});
*/
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
                Log.i("Trace Results",result);

                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                Log.i("Show Result",result);
                int x=result.length();//----> this is a Hack to extract empty Json Objects since non empty ones are longer than 20

                if (x<20) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (Looper.myLooper() == null) {
                                    Looper.prepare();
                                }
                                new AlertDialog.Builder(ResultsActivity.this)

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
                    ResultsActivity.this, personList, R.layout.list_row,
                    new String[]{TAG_NAME, TAG_VOTES},
                    new int[]{R.id.name, R.id.votes}
            );

            list.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
