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
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tw.myapp.ballsiteapp.databinding.ActivityLoginBinding;
import tw.myapp.ballsiteapp.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    ExecutorService executor;
    Handler RegisterHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            if (bundle.getInt("status") == 000) {

                // 記錄使用者相關資訊 到 context SharedPreferences 分享給其他 Activities 查詢
                SharedPreferences.Editor contextEditor = RegisterActivity.this.getSharedPreferences("user_info", MODE_PRIVATE).edit();
                contextEditor.putString("username", binding.NameText.getText().toString());
                contextEditor.putBoolean("register", true);
                contextEditor.apply();
                Toast.makeText(RegisterActivity.this, "註冊成功", Toast.LENGTH_SHORT).show();
                Intent intentToMeetingRoom = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intentToMeetingRoom);
            } else {
                Toast.makeText(RegisterActivity.this, "註冊失敗", Toast.LENGTH_SHORT).show();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        executor = Executors.newSingleThreadExecutor();
        binding.RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = binding.PassText.getText().toString();
                String pwd2 = binding.PassCheckText.getText().toString();
                if(pwd.equals(pwd2)){
                JSONObject packet = new JSONObject();
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

                Request request = new Request.Builder()
                        .url("http://20.2.70.0:8123/api/member/register")
                        .post(body)
                        .build();
                RegisterActivity.SimpaleAPIWorker apiCaller = new RegisterActivity.SimpaleAPIWorker(request);
                executor.execute(apiCaller);


                Toast.makeText(RegisterActivity.this, "註冊成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                } else {
                    Toast.makeText(RegisterActivity.this, "請確認輸入相同密碼,且欄位不可空白", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    class SimpaleAPIWorker implements Runnable {
        OkHttpClient client;
        Request request;

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
                Message m = RegisterHandler.obtainMessage();
                Bundle bundle = new Bundle();
                if( result.getInt("status")== 000) {
                    bundle.putString("mesg", result.getString("mesg"));
                    bundle.putInt("status",result.getInt("status") );
                } else {
                    bundle.putString("mesg", "登入失敗,請確認有無帳號,或密碼是否有誤");
                    bundle.putInt("status",result.getInt("status") );
                }
                m.setData(bundle);
                RegisterHandler.sendMessage(m);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}


