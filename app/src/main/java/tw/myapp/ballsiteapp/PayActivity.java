package tw.myapp.ballsiteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import tw.myapp.ballsiteapp.databinding.ActivityPayBinding;

public class PayActivity extends AppCompatActivity {
ActivityPayBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PayActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}