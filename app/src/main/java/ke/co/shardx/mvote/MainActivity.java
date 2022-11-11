package ke.co.shardx.mvote;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

public class MainActivity extends AppCompatActivity {
    public Date[] theDays;
    List<DashBoardItem> lstItem;
    int progressBarStatus = 0;
    public String userID;
    Boolean flag = false;
    boolean checkDateFlag = false;
    int [] dateFlag=new int[6];
    private String regStart;
    private String regStop;
    private String voteStart;
    private String voteStop;
    Util util = new Util();
    String[] dates = util.getDates();


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final EditText userName, userPassword;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (checkDateFlag == false) {

            regStart = dates[0];
            regStop = util.getDates()[1];
            voteStart = util.getDates()[2];
            voteStop = util.getDates()[3];


            java.util.Date date1,date2,date3,date4,date5;
            String sahii=(String) android.text.format.DateFormat.format("yyyy-MM-dd kk:mm:ss", new java.util.Date());

            //Log.i("Dates",sahii+":"+regStart);
            try {
                date1 = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").parse(sahii);
                date2 = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").parse(regStart);
                date3 =new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").parse(regStop);
                date4 =new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").parse(voteStart);
                date5 =new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").parse(voteStop);
                dateFlag[0]= date1.compareTo(date2);

               Log.i("Flag Variables",String.valueOf(dateFlag[0]));

            } catch (ParseException e) {
                e.printStackTrace();
            }
            flag = true;
        }


        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View view = layoutInflater.inflate(R.layout.dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(view);

        userName = (EditText) view.findViewById(R.id.editTextDialogUserName);
        userPassword = (EditText) view.findViewById(R.id.editTextDialogUserPassword);
        alertDialogBuilder.setIcon(android.R.drawable.ic_lock_lock);
        alertDialogBuilder.setTitle("Authentication");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Login",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // testLogin
                        userID = userName.getText().toString();
                        login(userName.getText().toString(), userPassword.getText().toString());
                        if (!flag) {
                            drawContents();
                        }// draws the dashboard
                    }

                });
        alertDialogBuilder.setNeutralButton("Register", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                dialogInterface.dismiss();
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // dialog.cancel();
                finishAffinity();
                System.exit(0);
            }
        });
        AlertDialog ald = alertDialogBuilder.create();
        ald.show();
    }

    private void login(String userName, String userPassword) {



        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        progressBar.setMessage("Authenticating " + userName.toUpperCase() + "... ");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();

        new Thread(new Runnable() {
            private Handler progressBarHandler = new Handler();

            public void run() {
                while (progressBarStatus < 100) {
                    progressBarStatus = 0;
                    Util util = new Util();

                    String pass;
                    pass = util.encryptPassword(userPassword);
                    String user = userName;
                   // Log.i("Debuging Info", "Pass:" + userPassword + "-->" + pass);

                    try {
                        HttpClient client = new DefaultHttpClient();
                        String postURL = "http://student.ktvc.ac.ke/Voting-App-Server-Side-master/m-vote/index.php";
                        HttpPost post = new HttpPost(postURL);
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("ID", user));
                        params.add(new BasicNameValuePair("Pass", pass));

                        UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                        post.setEntity(ent);
                        HttpResponse responsePOST = client.execute(post);
                        HttpEntity resEntity = responsePOST.getEntity();
                        if (resEntity != null) {
                            String Result = EntityUtils.toString(resEntity);
                            Result = Result.trim();
                            Log.i("Login Result", Result);
                            if (Result.equals("F")) {
                                flag = false;
                                progressBarStatus = 100;
                            } else {
                                if (Result.equals("S")) {
                                    flag = true;
                                    progressBarStatus = 100;
                                }
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    progressBarHandler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressBarStatus);
                        }
                    });

                }

                if (progressBarStatus >= 100) {
                    try {
                        Thread.sleep(3000);


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    progressBar.dismiss();

                    if (flag.equals(false)) {
                        Intent i = new Intent(getApplicationContext(), SplashActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        //  finish();
                    }

                }
            }
        }).start();
    }

    private String formatDate(Date date) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return dateFormat.format(date);

    }


    private void drawContents() {
        int screen = 0;


        lstItem = new ArrayList<>();
        lstItem.add(new DashBoardItem("Chair Person", R.drawable.icon));
        lstItem.add(new DashBoardItem("Vice Chair Person", R.drawable.icon));
        lstItem.add(new DashBoardItem("Academic Rep", R.drawable.icon));
        lstItem.add(new DashBoardItem("Hospitality Rep", R.drawable.icon));
        lstItem.add(new DashBoardItem("Health Rep", R.drawable.icon));
        lstItem.add(new DashBoardItem("Sports Rep", R.drawable.icon));
        lstItem.add(new DashBoardItem("Departmental Rep", R.drawable.icon));
        lstItem.add(new DashBoardItem("Class Rep", R.drawable.icon));

        screen = getOrientation();
        if (screen == Configuration.ORIENTATION_PORTRAIT) {
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.dashboardview_id);
            DashBoardItemAdapter dashBoardItemAdapter = new DashBoardItemAdapter(this, lstItem, userID);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            recyclerView.setAdapter(dashBoardItemAdapter);
        } else {
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.dashboardview_id);
            DashBoardItemAdapter dashBoardItemAdapter = new DashBoardItemAdapter(this, lstItem, userID);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            recyclerView.setAdapter(dashBoardItemAdapter);
        }


    }

    private int getOrientation() {
        int orientation = 0;
        orientation = getResources().getConfiguration().orientation;

        return orientation;
    }


}
