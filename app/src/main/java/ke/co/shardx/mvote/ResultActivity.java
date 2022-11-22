package ke.co.shardx.mvote;

import android.content.res.Configuration;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ke.co.shardx.mvote.databinding.ActivityResultBinding;

public class ResultActivity extends AppCompatActivity {


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
        lstItem.add(new DashBoardItem("Chair Person", R.drawable.icon));
        lstItem.add(new DashBoardItem("Vice Chair Person", R.drawable.icon));
        lstItem.add(new DashBoardItem("Academic Rep", R.drawable.icon));
        lstItem.add(new DashBoardItem("Hospitality Rep", R.drawable.icon));
        lstItem.add(new DashBoardItem("Health Rep", R.drawable.icon));
        lstItem.add(new DashBoardItem("Sports Rep", R.drawable.icon));
        lstItem.add(new DashBoardItem("Departmental Rep", R.drawable.icon));
        lstItem.add(new DashBoardItem("Class Rep", R.drawable.icon));


        screen=getOrientation();
        if(screen== Configuration.ORIENTATION_PORTRAIT) {
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.dashboardview_id);
            DashBoardItemAdapter dashBoardItemAdapter = new DashBoardItemAdapter(this,lstItem,"");
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            recyclerView.setAdapter(dashBoardItemAdapter);
        }else {
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.dashboardview_id);
            DashBoardItemAdapter dashBoardItemAdapter = new DashBoardItemAdapter(this, lstItem,"");
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