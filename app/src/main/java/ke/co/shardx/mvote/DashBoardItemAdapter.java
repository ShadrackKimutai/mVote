package ke.co.shardx.mvote;


import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DashBoardItemAdapter extends RecyclerView.Adapter<DashBoardItemAdapter.myViewHolder> {

    private Context context;
    private List<DashBoardItem> myItem;

    public DashBoardItemAdapter(Context context, List<DashBoardItem> myItem) {
        this.context = context;
        this.myItem = myItem;
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater= LayoutInflater.from(context);
        view=layoutInflater.inflate(R.layout.dashboarditem,parent,false);

        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {
        holder.textView.setText(myItem.get(position).getItem());
        holder.imageView.setImageResource(myItem.get(position).getThumbnail());
    }

    @Override
    public int getItemCount() {
        return myItem.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder{
        private Context context;

        TextView textView;
        ImageView imageView;
        public myViewHolder(View view) {
            super(view);
            textView = (TextView) itemView.findViewById(R.id.dashboard_item_id);
            imageView = (ImageView) itemView.findViewById(R.id.dashboard_img_id);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.i("Pathfinder", "Where Am I " + textView.getText());
                    //Toast.makeText(context, "Where Am I " + textView.getText(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}