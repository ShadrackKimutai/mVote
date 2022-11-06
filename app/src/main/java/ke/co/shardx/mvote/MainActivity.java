package ke.co.shardx.mvote;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
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
    List<DashBoardItem> lstItem;
    ProgressBar progressBar;
    private int progressBarStatus = 0;
    Boolean flag=false;
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
       // drawContents();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final EditText userName,userPassword;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        String getM;
        LayoutInflater layoutInflater=LayoutInflater.from(MainActivity.this);
        View view=layoutInflater.inflate(R.layout.dialog,null);
        AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(view);

        userName=(EditText)view.findViewById(R.id.editTextDialogUserName);
        userPassword=(EditText)view.findViewById(R.id.editTextDialogUserPassword);
        alertDialogBuilder.setIcon(android.R.drawable.ic_lock_lock);
        alertDialogBuilder.setTitle("Authentication");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Login",
                new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // testLogin
                login(userName.getText().toString(),userPassword.getText().toString());
                //  drawContents(); // draws the dashboard
            }
        });
        alertDialogBuilder.setNeutralButton("Register", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
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
        AlertDialog ald=alertDialogBuilder.create();
        ald.show();
    }

    private void login(String userName, String userPassword) {
                new Thread(new Runnable() {
                public void run() {
                    while (progressBarStatus < 100) {
                        progressBarStatus = 0;
                        Util voterUtilities = new Util();

                        String pass;
                        pass = voterUtilities.encryptPassword(userPassword);
                        String user = userName;
                        Log.i("Debuging Info","Pass:" + userPassword + "-->" + pass);

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
                                System.out.println("Result" + Result);
                                if (Result.equals("0")) {
                                    flag = false;
                                    progressBarStatus = 100;
                                } else {
                                    if (Result.equals("1")) {
                                        flag = true;
                                        progressBarStatus = 100;
                                    }
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            }).start();
        }


    private void drawContents(){
        int screen=0;


        lstItem=new ArrayList<>();
        lstItem.add(new DashBoardItem("Profile",R.drawable.profile));
        lstItem.add(new DashBoardItem("Tutorials",R.drawable.tut));
        lstItem.add(new DashBoardItem("Resources", R.drawable.git));
        lstItem.add(new DashBoardItem("Slack",R.drawable.slack));
        lstItem.add(new DashBoardItem("IRC",R.drawable.chat));
        lstItem.add(new DashBoardItem("Social",R.drawable.face));
        lstItem.add(new DashBoardItem("Sketches",R.drawable.sketchpad));
        lstItem.add(new DashBoardItem("Brought By",R.drawable.placeholder));

        screen=getOrientation();
        if(screen== Configuration.ORIENTATION_PORTRAIT) {
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.dashboardview_id);
            DashBoardItemAdapter dashBoardItemAdapter = new DashBoardItemAdapter(this, lstItem);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            recyclerView.setAdapter(dashBoardItemAdapter);
        }else {
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.dashboardview_id);
            DashBoardItemAdapter dashBoardItemAdapter = new DashBoardItemAdapter(this, lstItem);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            recyclerView.setAdapter(dashBoardItemAdapter);
        }


    }
    private int getOrientation(){
        int orientation=0;
        orientation=getResources().getConfiguration().orientation;

        return orientation;
    }



}
