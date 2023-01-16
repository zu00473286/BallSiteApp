package tw.myapp.ballsiteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

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
        // 查詢 Preference 取得使用者身分
        SharedPreferences userInfo = this.getSharedPreferences("user_info",MODE_PRIVATE);
        username = userInfo.getString("username","尚未登入");
        Toast.makeText(this, "歡迎使用者:" + username, Toast.LENGTH_LONG).show();
    }
    }
