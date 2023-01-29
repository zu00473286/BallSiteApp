package tw.myapp.ballsiteapp.site;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import tw.myapp.ballsiteapp.R;
import tw.myapp.ballsiteapp.model.SiteModel;

public class SiteAdapter extends RecyclerView.Adapter<SiteAdapter.ViewHolder> {

    private SQLiteDatabase db;

    private SiteItemClickListener listener;

    private ArrayList<SiteModel> siteAll;

    public SiteAdapter(SQLiteDatabase db, SiteItemClickListener listener, ArrayList<SiteModel> siteAll) {
        this.db = db;
        this.listener = listener;
        this.siteAll = siteAll;
    }

    public void getAllSiteData() {
        Cursor cursor = db.rawQuery("SELECT * FROM site;", null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                SiteModel siteModel = new SiteModel(
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3)
                );
                siteAll.add(siteModel);
            } while (cursor.moveToNext());
        }
        cursor.close();

    }

    public void setSites(String sites) {
        siteAll.clear();
        Cursor cursor = null;
        switch (sites) {
            case "all":
                cursor = db.rawQuery("SELECT * FROM site;", null);
                break;
            case "羽球場":
                cursor = db.rawQuery("SELECT * FROM site WHERE category_id=1;", null);
                break;
            case "排球場":
                cursor = db.rawQuery("SELECT * FROM site WHERE category_id=2;", null);
                break;
            case "桌球場":
                cursor = db.rawQuery("SELECT * FROM site WHERE category_id=3;", null);
                break;
        }
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                SiteModel siteModel = new SiteModel(
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3)
                );
                siteAll.add(siteModel);
            } while (cursor.moveToNext());
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

    }

    // 資料數量
    @Override
    public int getItemCount() {
        return siteAll.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
