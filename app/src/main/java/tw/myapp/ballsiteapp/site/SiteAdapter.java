package tw.myapp.ballsiteapp.site;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull SiteAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
