package tw.myapp.ballsiteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import tw.myapp.ballsiteapp.databinding.ActivitySiteBinding;

public class SiteActivity extends AppCompatActivity {

    ActivitySiteBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySiteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.OkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMaintain = new Intent(SiteActivity.this, VenueRentalActivity.class);
                startActivity(intentMaintain);
            }
        });
    }
}