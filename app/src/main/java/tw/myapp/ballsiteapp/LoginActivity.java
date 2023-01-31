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

import org.json.JSONException;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userData =getSharedPreferences("User",MODE_PRIVATE);

        String account=userData.getString("account","null");
        if(userData.getBoolean("check",false) && !account.equals(null)){
            binding.ckRemember.setChecked(true);
            binding.txtUsername.setText(account);
        }
        executor= Executors.newSingleThreadExecutor();
        binding.btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                Bundle datas = new Bundle();
                startActivity(intent);
            }
        });
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.txtUsername.getText().toString();
                String pwd = binding.pass1.getText().toString();
                //判斷欄位不可空白才送出登入資料
                if (name != null && pwd != null && !name.isEmpty() && !pwd.isEmpty()) {
                    JSONObject packet = new JSONObject();
                    try {
                        //把使用者資料 封裝成JSON格式 回傳給SpringBoot Controller進行驗證
                        JSONObject memberLogData = new JSONObject();
                        memberLogData.put("user", binding.txtUsername.getText().toString());
                        memberLogData.put("pass", binding.pass1.getText().toString());
                        packet.put("data", memberLogData);
                        Log.e("JSON", packet.toString(4));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    MediaType mType = MediaType.parse("application/json");
                    RequestBody body = RequestBody.create(packet.toString(), mType);
                    Request request = new Request.Builder()
                            .url("http://20.2.70.0:8123/api/member/login")
                            .post(body)
                            .build();
                    SimpaleAPIWorker apiCaller = new SimpaleAPIWorker(request,loginResultHandler);
                    //產生Task準備給executor執行
                    executor.execute(apiCaller);

                } else {
                    Toast.makeText(LoginActivity.this, "欄位空白", Toast.LENGTH_SHORT).show();
                }
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

    Handler loginResultHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            if (bundle.getInt("status") == 11) {
                Toast.makeText(LoginActivity.this, "登入成功", Toast.LENGTH_SHORT).show();
                //接收後端回傳登入成功的訊息後 將Email存起來 供會員資料頁面使用
                userData = getSharedPreferences("userData", MODE_PRIVATE);
                SharedPreferences.Editor edit = userData.edit();
                String savedEmail = userData.getString("email", "查無資料");
                String loginEmail = binding.txtUsername.getText().toString();
                //確認登入成功的帳號與儲存在手機內的會員為同一人 不同則移除儲存的會員資料
                if (savedEmail.equals(loginEmail)) {
                    edit.putString("email", loginEmail).commit();//存入登入帳號到memberDataPre檔案
                } else {
                    edit.remove("email");
                    edit.apply();
                    edit.putString("email", loginEmail).commit();
                }
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(LoginActivity.this, bundle.getString("mesg"), Toast.LENGTH_LONG).show();
            }

        }
    };
    class SimpaleAPIWorker implements  Runnable {
        OkHttpClient client;
        Request request ;

        public SimpaleAPIWorker(Request request, Handler loginResultHandler) {
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