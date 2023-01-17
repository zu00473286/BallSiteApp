package tw.myapp.ballsiteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import tw.myapp.ballsiteapp.databinding.ActivityLoginBinding;
import tw.myapp.ballsiteapp.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    private  String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject packet = new JSONObject();
                if (binding.PassText == binding.PassCheckText) {
                    try {
                        JSONObject data = new JSONObject();
                        data.put("name", binding.NameText.getText().toString());
                        data.put("email", binding.EmailText.getText().toString());
                        data.put("mobile", binding.TelText.getText().toString());
                        data.put("passwd", binding.PassText.getText().toString());
                        packet.put("data", data);
                        Log.w("API格式", packet.toString(5));
                    } catch (Exception e) {
                        Toast.makeText(RegisterActivity.this, "資料格式異常,請重新輸入", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // 使用網路通訊 Header + Body
                    MediaType mType = MediaType.parse("application/json");
                    RequestBody body = RequestBody.create(packet.toString(), mType);
                    Toast.makeText(RegisterActivity.this, "註冊成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                } else {
                    Toast.makeText(RegisterActivity.this, "密碼不一致", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    }

