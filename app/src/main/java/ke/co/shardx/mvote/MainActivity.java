package ke.co.shardx.mvote;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
public class MainActivity extends AppCompatActivity {
    List<DashBoardItem> lstItem;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawContents();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawContents();
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
