package tw.myapp.ballsiteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import tw.myapp.ballsiteapp.databinding.ActivityVenueRentalBinding;

public class VenueRentalActivity extends AppCompatActivity {

    ActivityVenueRentalBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVenueRentalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.OKBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMaintain = new Intent(VenueRentalActivity.this, ConfirmActivity.class);
                startActivity(intentMaintain);
            }
        });
    }
}