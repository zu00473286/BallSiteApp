package tw.myapp.ballsiteapp.site;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ComponentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tw.myapp.ballsiteapp.R;
import tw.myapp.ballsiteapp.SiteListActivity;
import tw.myapp.ballsiteapp.model.SiteModel;

public class SiteAdapter extends RecyclerView.Adapter<SiteAdapter.ViewHolder> {

    private SQLiteDatabase db;

    private SiteItemClickListener listener;

    private ArrayList<SiteModel> siteAll;

    SharedPreferences userData;


    public SiteAdapter(SQLiteDatabase db, SiteItemClickListener listener, SharedPreferences userdata) {
        this.userData = userdata;
        this.db = db;
        this.listener = listener;
        this.siteAll = new ArrayList<>();

        Cursor cursor = null;
        String category = userData.getString("category","");
        switch (category) {
            case "1":
                cursor = db.rawQuery("SELECT * FROM Sites WHERE category_id=1;", null);
                break;
            case "2":
                cursor = db.rawQuery("SELECT * FROM Sites WHERE category_id=2;", null);
                break;
            case "3":
                cursor = db.rawQuery("SELECT * FROM Sites WHERE category_id=3;", null);
                break;
        }
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                SiteModel siteModel = new SiteModel(
                        cursor.getString(1),
//                        cursor.getString(2),
                        cursor.getString(0)
                );
                siteAll.add(siteModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }

    public void setSites(String sites) {
        siteAll.clear();
        Cursor cursor = null;
        switch (sites) {
            case "all":
                cursor = db.rawQuery("SELECT * FROM Sites;", null);
                Log.e("sitesize","???");
                break;
            case "?????????":
                cursor = db.rawQuery("SELECT * FROM Sites WHERE category_id=1;", null);
                Log.e("sitesize","???1");
                break;
            case "?????????":
                cursor = db.rawQuery("SELECT * FROM Sites WHERE category_id=2;", null);
                Log.e("sitesize","???2");
                break;
            case "?????????":
                cursor = db.rawQuery("SELECT * FROM Sites WHERE category_id=3;", null);
                Log.e("sitesize","???3");
                break;
        }
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                SiteModel siteModel = new SiteModel(
                        cursor.getString(1),
//                        cursor.getString(2),
                        cursor.getString(0)
                );
                siteAll.add(siteModel);
            } while (cursor.moveToNext());
            this.notifyDataSetChanged();
        } else {
            //?????????
            siteAll.clear();
        }
        cursor.close();
    }

    // ????????? layout xml ?????? ??????UI ( ??? RecyclerView ??????(???????????????)  ?????????????????????)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    // ????????????????????? UI ???????????? (??? RecyclerView ?????? ?????????????????????)
    @Override
    public void onBindViewHolder(@NonNull SiteAdapter.ViewHolder holder, int position) {
        String category = userData.getString("category","");
        switch (category) {
            case "1":
                holder.imageView.setImageResource(R.drawable.a1);
                holder.txtPrice.setText("45???");
                break;
            case "2":
                holder.imageView.setImageResource(R.drawable.a2);
                holder.txtPrice.setText("70???");
                break;
            case "3":
                holder.imageView.setImageResource(R.drawable.a3);
                holder.txtPrice.setText("35???");
                break;
        }

        holder.txtSiteID.setText( siteAll.get(position).getSiteID() );

//        holder.txtPrice.setText( siteAll.get(position).getPrice() + "???");
        holder.txtSiteID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int pos = holder.getAdapterPosition(); // ????????????
                String siteID = siteAll.get(pos).getSiteID();
//                String price = siteAll.get(pos).getPrice();
                String site_id = siteAll.get(pos).getSite_id();
                // ??????????????????????????? RestaurantListActivity ?????? ?????????????????????(???????????? ????????????)
//                listener.onClick( pos, siteID, price );
                listener.onClick(pos, siteID);
                SharedPreferences.Editor editor = userData.edit();
                editor.putString("siteID",siteID);      //???????????? eg.A1111
                editor.putString("site_id",site_id);    //??????id  eg.1
                editor.putString("price",holder.txtPrice.getText().toString());
                editor.apply();

            }
        });
    }

    // ????????????
    @Override
    public int getItemCount() {
        return siteAll.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txtSiteID;
        TextView txtPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.site_icon);
            this.txtSiteID = itemView.findViewById(R.id.site_name);
            this.txtPrice = itemView.findViewById(R.id.site_price);
        }
    }
}
