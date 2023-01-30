package tw.myapp.ballsiteapp;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import tw.myapp.ballsiteapp.databinding.ActivitySiteNumberBinding;
import tw.myapp.ballsiteapp.site.SiteAdapter;
import tw.myapp.ballsiteapp.site.SiteItemClickListener;

public class SiteListActivity extends AppCompatActivity {

    ActivitySiteNumberBinding binding;

    SiteItemClickListener siteItemClickListener;

    SiteAdapter adapter;

    SharedPreferences userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySiteNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //每一個 UI元件都必須要與上層綁定　( Layout 則必須與有關的 Activity 綁定 )
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        // 資料的來源 ?  RecyclerView 指定需要與 RecyclerView.Adapter 物件綁定 提供資料與 細部的
        // ViewHolder Layout 一起呈現
        // Adapter 要負責 載入所有資料 並建立一個 UI View 容器 並把指定的資料填入 UI後 交給 RecyclerView顯示
        SQLiteDatabase db = openOrCreateDatabase("Sites",MODE_PRIVATE,null);
        userData = getSharedPreferences("userData", MODE_PRIVATE);
        binding.recyclerView.setAdapter( new SiteAdapter(db,siteItemClickListener, userData));

    }
}