package tw.myapp.ballsiteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import tw.myapp.ballsiteapp.databinding.ActivitySiteBinding;

public class SiteActivity extends AppCompatActivity {

    SharedPreferences userData;
    ActivitySiteBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySiteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userData =getSharedPreferences("userData",MODE_PRIVATE);
        SharedPreferences.Editor editor = userData.edit();

        binding.imageBadminton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("category","1");
                editor.apply();
                Intent intent = new Intent(SiteActivity.this, VenueRentalActivity.class);
                startActivity(intent);
            }
        });
        binding.imageTennis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("category","2");
                editor.apply();
                Intent intent = new Intent(SiteActivity.this, VenueRentalActivity.class);
                startActivity(intent);
            }
        });
        binding.imageTabelTennis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("category","3");
                editor.apply();
                Intent intent = new Intent(SiteActivity.this, VenueRentalActivity.class);
                startActivity(intent);
            }
        });
    }
}