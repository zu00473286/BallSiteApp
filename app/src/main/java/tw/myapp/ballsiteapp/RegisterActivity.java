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
    Request request;

    private String username;
    Handler registerResultHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            if (bundle.getInt("status") == 000) {
                Toast.makeText(RegisterActivity.this, "註冊成功", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(RegisterActivity.this, "註冊失敗", Toast.LENGTH_SHORT).show();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject packet = new JSONObject();
                //if (binding.PassText == binding.PassCheckText) {
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
                        .url("http://192.168.255.58:8123/api/member/register")
                        .post(body)
                        .build();
                Toast.makeText(RegisterActivity.this, "註冊成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                //   } else {
                Toast.makeText(RegisterActivity.this, "密碼不一致", Toast.LENGTH_SHORT).show();
                // }
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
                Message m = registerResultHandler.obtainMessage();
                Bundle bundle = new Bundle();
                if (result.getInt("status") == 00) {
                    bundle.putString("mesg", result.getString("mesg"));
                    bundle.putInt("status", result.getInt("status"));
                } else {
                    bundle.putString("mesg", "登入失敗,請確認有無帳號,或密碼是否有誤");
                    bundle.putInt("status", result.getInt("status"));
                }
                m.setData(bundle);
                registerResultHandler.sendMessage(m);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

