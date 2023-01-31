package tw.myapp.ballsiteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.concurrent.ExecutorService;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import tw.myapp.ballsiteapp.databinding.ActivitySettlementBinding;

public class SettlementActivity extends AppCompatActivity {

    ActivitySettlementBinding binding;

    ExecutorService executor;
    SharedPreferences userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettlementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        userData = getSharedPreferences("userData", MODE_PRIVATE);


        String name = userData.getString("name", "");
        String tel = userData.getString("mobile", "");
        String time = userData.getString("time", "");
        String ymd = userData.getString("ymd", "");
        String siteID = userData.getString("siteID", "");

        binding.txtName3.setText(name);
        binding.txtTime3.setText(time);
        binding.txtTel3.setText(tel);
        binding.txtDay3.setText(ymd);
        binding.txtSite3.setText(siteID);



        binding.btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMaintain = new Intent(SettlementActivity.this, MainActivity.class);
                startActivity(intentMaintain);
            }
        });
        binding.button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMaintain = new Intent(SettlementActivity.this, PayActivity.class);
                startActivity(intentMaintain);
            }
        });


        }

}