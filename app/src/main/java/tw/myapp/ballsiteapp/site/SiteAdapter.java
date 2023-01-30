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
                        cursor.getString(2)
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
                Log.e("sitesize","有");
                break;
            case "羽球場":
                cursor = db.rawQuery("SELECT * FROM Sites WHERE category_id=1;", null);
                Log.e("sitesize","有1");
                break;
            case "排球場":
                cursor = db.rawQuery("SELECT * FROM Sites WHERE category_id=2;", null);
                Log.e("sitesize","有2");
                break;
            case "桌球場":
                cursor = db.rawQuery("SELECT * FROM Sites WHERE category_id=3;", null);
                Log.e("sitesize","有3");
                break;
        }
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                SiteModel siteModel = new SiteModel(
                        cursor.getString(1),
                        cursor.getString(2)
                );
                siteAll.add(siteModel);
            } while (cursor.moveToNext());
            this.notifyDataSetChanged();
        } else {
            //無資料
            siteAll.clear();
        }
        cursor.close();
    }

    // 從哪個 layout xml 建立 畫面UI ( 由 RecyclerView 呼叫(畫面捲動時)  開發者無法察覺)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    // 將上面產生好的 UI 綁定資料 (由 RecyclerView 呼叫 開發者無法察覺)
    @Override
    public void onBindViewHolder(@NonNull SiteAdapter.ViewHolder holder, int position) {
        String category = userData.getString("category","");
        switch (category) {
            case "1":
                holder.imageView.setImageResource(R.drawable.a1);
                break;
            case "2":
                holder.imageView.setImageResource(R.drawable.a2);
                break;
            case "3":
                holder.imageView.setImageResource(R.drawable.a3);
                break;
        }

        holder.txtSiteID.setText( siteAll.get(position).getSiteID() );

        holder.txtPrice.setText( siteAll.get(position).getPrice() + "元");
        holder.txtSiteID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int pos = holder.getAdapterPosition(); // 正確做法
                String siteID = siteAll.get(pos).getSiteID();
                String price = siteAll.get(pos).getPrice();
                // 將事件控制權交回給 RestaurantListActivity 負責 不應在此處處理(可以這樣 但觀念錯)
                listener.onClick( pos, siteID, price );

                SharedPreferences.Editor editor = userData.edit();
                editor.putString("siteID",siteID);
                editor.apply();

            }
        });
    }

    // 資料數量
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
