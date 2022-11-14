package ke.co.shardx.mvote;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ResultsActivity extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        WebView webView = (WebView) findViewById(R.id.webview);
        webView.loadUrl("https://student.ktvc.ac.ke/Voting-App-Server-Side-master/mvote/candid.php");
webView.setOnTouchListener(new View.OnTouchListener() {
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }
});

    }

}
