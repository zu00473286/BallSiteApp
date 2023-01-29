package tw.myapp.ballsiteapp.site;

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
