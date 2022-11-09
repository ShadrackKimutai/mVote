package ke.co.shardx.mvote;



import static ke.co.shardx.mvote.CandidatesActivity.userID;

import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class  DashBoardItemAdapter extends RecyclerView.Adapter<DashBoardItemAdapter.myViewHolder> {

    private Context context;
    private List<DashBoardItem> myItem;
    private String userID;
    static String UserID;

    public DashBoardItemAdapter(Context context, List<DashBoardItem> myItem,String userID) {
        this.context = context;
        this.myItem = myItem;
        this.userID=userID;
        UserID=userID;
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

                    /*
                    *
                    *
                    *   lstItem.add(new DashBoardItem("Chair Person",R.drawable.icon));
                    *   lstItem.add(new DashBoardItem("Vice Chair Person",R.drawable.icon));
                    *   lstItem.add(new DashBoardItem("Academic Rep",R.drawable.icon));
                    *   lstItem.add(new DashBoardItem("Hospitality Rep", R.drawable.icon));
                    *   lstItem.add(new DashBoardItem("Health Rep",R.drawable.icon));
                    *   lstItem.add(new DashBoardItem("Sports Rep",R.drawable.icon));
                    *   lstItem.add(new DashBoardItem("Departmental Rep",R.drawable.icon));
                    *   lstItem.add(new DashBoardItem("Class Rep",R.drawable.icon));
                    *
                    *
                    * */
                    String level =textView.getText().toString();
                    switch (level){
                        case "Chair Person" :

                            Log.i("Position","chairman");
                           // Log.i("UserID",UserID);

                            try{
                                Bundle bundle = new Bundle();
                                Intent intent = new Intent(view.getContext(), CandidatesActivity.class);
                                bundle.putString("seat","Chair Person");
                                bundle.putString("user",UserID);

                                intent.putExtras(bundle);
                                view.getContext().startActivity(intent);
                                // finish();
                            }catch (Exception x){
                                System.out.println(x.getMessage());
                            }

                            break;

                        case "Vice Chair Person" :
                            Log.i("Position","Vice chairman");
                            break;
                        case "Academic Rep"  :
                            Log.i("Position","Academic Rep");
                            break;
                        case "Hospitality Rep" :
                            Log.i("Position","Hospitality Rep");
                            break;

                        case "Health Rep":
                            Log.i("Position","Health Rep");
                            break;

                        case "Sports Rep":
                            Log.i("Position","Sports Rep");
                            break;

                        case "Departmental Rep":
                            Log.i("Position","Departmental Rep");
                            break;
                        case "Class Rep":
                            Log.i("Position","Class Rep");
                            break;

                    }
                }

            });
        }

    }

}