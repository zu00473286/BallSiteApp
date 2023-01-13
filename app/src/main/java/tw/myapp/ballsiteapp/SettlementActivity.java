package tw.myapp.ballsiteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import tw.myapp.ballsiteapp.databinding.ActivitySettlementBinding;

public class SettlementActivity extends AppCompatActivity {

    ActivitySettlementBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettlementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMaintain = new Intent(SettlementActivity.this, MainActivity.class);
                startActivity(intentMaintain);
            }
        });
    }
}