package ke.co.shardx.mvote;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

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


public class RegisterActivity extends Activity {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_SIGNUP = 0;
    private Boolean flag,success;
    final Context context=this;
    EditText FirstName,LastName,PasswordText,ID;
    AppCompatButton registerButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        EditText FirstName=(EditText)findViewById(R.id.input_fname);
        EditText LastName=(EditText)findViewById(R.id.input_lname);
        EditText PasswordText=(EditText)findViewById(R.id.input_password);
        EditText ID=(EditText)findViewById(R.id.input_id);
        registerButton = (AppCompatButton) findViewById(R.id.btn_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                register(FirstName.getText().toString(),LastName.getText().toString(),PasswordText.getText().toString(),ID.getText().toString());//}catch (Exception ex){Log.e("Exception","Exeption Encountered");}
            }
        });


    }

    public void register(String firstName,String lastName,String passWord,String ID) {


        Log.i(TAG, "Register");


        if (!validate(firstName,lastName,passWord,ID)) {
            onLoginFailed();
            return;
        }


        registerButton.setEnabled(false);

        final ProgressDialog progressBar=new ProgressDialog(this);
        progressBar.setCancelable(false);
        progressBar.setMessage("Registering "+firstName.toUpperCase()+" "+lastName.toUpperCase()+"...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();

        new Thread(new Runnable() {
            int progressBarStatus = 0;
            private Handler progressBarbHandler = new Handler();

            @Override
            protected void finalize() throws Throwable {
                super.finalize();
                Success();
            }

            public void run() {
                while (progressBarStatus < 100) {


                    String Result = null;
                    final String Result_ = null;


                    Log.i("Captured Details","names:"+firstName+" "+lastName+" "+"password:"+passWord+"ID:"+ID);
                    try {
                        HttpClient client;
                        client = new DefaultHttpClient();
                        String postURL = "http://student.ktvc.ac.ke/Voting-App-Server-Side-master/m-vote/register.php";
                        HttpPost post = new HttpPost(postURL);
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("ID", ID));
                        params.add(new BasicNameValuePair("FName", firstName));
                        params.add(new BasicNameValuePair("SName", lastName));
                        params.add(new BasicNameValuePair(" Pass", Util.encryptPassword(passWord)));


                        UrlEncodedFormEntity ent;
                        ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                        post.setEntity(ent);
                        HttpResponse responsePOST = client.execute(post);
                        HttpEntity resEntity = responsePOST.getEntity();
                        if (resEntity != null) {
                            Result = EntityUtils.toString(resEntity);
                            Result=Result.trim();
                            System.out.println(Result);
                            if (Result.equals("F")){
                                flag=false;
                                progressBarStatus=100;
                            }else if (Result.equals("e")) {
                                flag = true;
                                success=false;
                                progressBarStatus = 100;
                            }else if(Result.equals("S")) {
                                flag=true;
                                success=true;
                                progressBarStatus=100;

                            }

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    progressBarbHandler.post(new Runnable() {
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

                    if (flag.equals(false)){
                        System.out.println("INFO: Attempt to reach the server failed");
                        Success();
                    }else if(flag.equals(true)){
                        if(success.equals(true)){
                            System.out.println("INFO: You have been Registered");
                            Success();
                        }else{
                            System.out.println("INFO: Registration attempt failed at the server");
                            Success();
                        }
                    }
                }
            }
        }).start();


    }

    public boolean validate(String firstName,String lastName,String passWord,String id) {
        boolean valid = true;

        try{


            if (firstName==null ||lastName==null || passWord ==null || id==null){
                valid=false;
                Log.i("Empty Form","one of the fields is null");
            }else {


                if (firstName.isEmpty() && lastName.isEmpty() && passWord.isEmpty() & id.isEmpty()){
                    valid=false;
                    Log.i("Empty Form","User Didnt enter anything. Stopping");
                }
                if (firstName.isEmpty() || (firstName.matches(".*\\d+.*"))) {
                    //FirstName.setError("First Name cannot be empty or contain numbers");
                    Toast.makeText(getApplicationContext(),"First Name cannot be empty or contain numbers",Toast.LENGTH_SHORT).show();
                    FirstName.requestFocus();
                    valid = false;

                } else {
                    //FirstName.setError(null);
                }
                if (lastName.isEmpty() || (lastName.matches(".*\\d+.*"))) {
                    Toast.makeText(getApplicationContext(),"Last Name cannot be empty or contain numbers",Toast.LENGTH_SHORT).show();

                    //LastName.setError("Last Name cannot be empty or contain numbers");
                    LastName.requestFocus();
                    valid = false;
                } else {
                   // LastName.setError(null);
                }

                if (passWord.isEmpty() || passWord.length() < 8 || passWord.length() > 20) {
                    Toast.makeText(getApplicationContext(),"Password to be Between 8 and 20 alphanumeric characters",Toast.LENGTH_SHORT).show();
                    valid = false;
                    PasswordText.requestFocus();

                } else {
                    //PasswordText.setError(null);
                }
                if (id.isEmpty() || id.length() != 12 || !id.matches("20\\d\\d[a-zA-Z]+\\d\\d\\d\\d\\d\\d$")) {
                    Toast.makeText(getApplicationContext(),"ID cannot be empty and must be 12 characters have long. It must match College ID",Toast.LENGTH_SHORT).show();
                    valid = false;
                    ID.requestFocus();
                } else {
                    //ID.setError(null);
                }
            }
        } catch (Exception exception) { //|| NullPointerException nullPointerException)
            Log.e("Exception Caught",exception.getLocalizedMessage());
            System.out.println(exception.getMessage());

            valid=false;
        }
        return valid;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {


                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the RegisterActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        registerButton.setEnabled(false);
        finish();
    }

    public void onLoginFailed() {
    Log.e("Login Issues","Login Failed");
        registerButton.setEnabled(true);
    }

    public void Success(){
        Intent i = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(i);
        this.finish();

    }


}