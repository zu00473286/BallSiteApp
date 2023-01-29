package tw.myapp.ballsiteapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import tw.myapp.ballsiteapp.databinding.ActivitySiteNumberBinding;
import tw.myapp.ballsiteapp.site.SiteAdapter;
import tw.myapp.ballsiteapp.site.SiteItemClickListener;

public class SiteListActivity extends AppCompatActivity {

    ActivitySiteNumberBinding binding;

    SiteItemClickListener siteItemClickListener;

    SiteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySiteNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}