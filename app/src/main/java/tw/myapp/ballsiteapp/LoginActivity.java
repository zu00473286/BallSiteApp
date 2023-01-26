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
import android.widget.CompoundButton;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tw.myapp.ballsiteapp.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    // activity 個別資料儲存區
    SharedPreferences userData;
    //執行緒負責進行網路通訊任務
    ExecutorService executor;
    // Handler 接收登入結果處理
    Handler loginResultHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            if( bundle.getInt("status")== 11) {

                // 記錄使用者相關資訊 到 context SharedPreferences 分享給其他 Activities 查詢
                SharedPreferences.Editor contextEditor = LoginActivity.this.getSharedPreferences("user_info",MODE_PRIVATE).edit();
                contextEditor.putString("username",binding.txtUsername.getText().toString());
                contextEditor.putBoolean("isLogin",true);
                contextEditor.apply();
                Toast.makeText(LoginActivity.this, "登入成功", Toast.LENGTH_SHORT).show();
                Intent intentSite = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intentSite);
            } else {
                SharedPreferences.Editor contextEditor = LoginActivity.this.getSharedPreferences("user_info",MODE_PRIVATE).edit();
                contextEditor.putString("username","");
                contextEditor.putBoolean("isLogin",false);
                contextEditor.apply();

                // 確認清除
                Toast.makeText(LoginActivity.this, bundle.getString("mesg"), Toast.LENGTH_LONG).show();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //
        executor = Executors.newSingleThreadExecutor();
        //
        binding.btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 請將 使用者資料封裝成 JSON Format 後傳送給 Spring-Boot Controller 進行驗證
                JSONObject packet = new JSONObject();
                try {
                    JSONObject data = new JSONObject();
                    data.put("user", binding.txtUsername.getText().toString());
                    data.put("pass", binding.pass1.getText().toString());
                    packet.put("data", data);


                    Log.w("API格式", packet.toString(4));
                }catch(Exception e) {
                    Toast.makeText(LoginActivity.this, "資料格式異常,請重新輸入", Toast.LENGTH_SHORT).show();
                }
                // 使用網路通訊 Header + Body
                MediaType mType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(packet.toString(), mType);

                Request request = new Request.Builder()
                        .url("http://192.168.0.15:8123/api/member/login")
                        .post(body)
                        .build();

                //產生 Task 準備給 Executor 執行
                SimpaleAPIWorker apiCaller = new SimpaleAPIWorker(request);
                executor.execute(apiCaller);
            }
        });
        // 執行 Checkbox Remember 行為 (無需跟其他activities共享
        userData = this.getPreferences(MODE_PRIVATE);
        // 此處要先判斷 是否有勾選 判斷是否應該載入設定值
        boolean isRemember = userData.getBoolean("rememberMe", false);
        if( isRemember ) {
            // 從 userData中取出資料 放入 user & passwd 中
            binding.txtUsername.setText(userData.getString("user",""));
            binding.pass1.setText(userData.getString("pass",""));
            binding.ckRemember.setChecked(true);
        } else {
            // 直接清除 user & passwd 中
            binding.txtUsername.setText("");
            binding.pass1.setText("");
            binding.ckRemember.setChecked(false);
        }

        //
        binding.ckRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if( isChecked ) {
                    SharedPreferences.Editor editor = userData.edit();
                    editor.putString("user",binding.txtUsername.getText().toString());
                    editor.putString("pass",binding.pass1.getText().toString());
                    editor.putBoolean("rememberMe", true);
                    editor.apply();

                } else {
                    SharedPreferences.Editor editor = userData.edit();
                    editor.putString("user","");
                    editor.putString("pass","");
                    editor.putBoolean("rememberMe", false);
                    editor.apply();
                }
            }
        });
    }
    class SimpaleAPIWorker implements  Runnable {
        OkHttpClient client;
        Request request ;

        public SimpaleAPIWorker(Request request) {
            client = new OkHttpClient();
            this.request = request;
        }

        @Override
        public void run() {
            try {
                Response response = client.newCall(request).execute();
                String responseString = response.body().string();
                Log.w("api回應", responseString);
                // Response 也應該是 JSON格式回傳後 由 app端進行分析 確認登入結果
                JSONObject result = new JSONObject(responseString);
                Message m = loginResultHandler.obtainMessage();
                Bundle bundle = new Bundle();
                if( result.getInt("status")== 11) {
                    bundle.putString("mesg", result.getString("mesg"));
                    bundle.putInt("status",result.getInt("status") );
                } else {
                    bundle.putString("mesg", "登入失敗,請確認有無帳號,或密碼是否有誤");
                    bundle.putInt("status",result.getInt("status") );
                }
                m.setData(bundle);
                loginResultHandler.sendMessage(m);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}