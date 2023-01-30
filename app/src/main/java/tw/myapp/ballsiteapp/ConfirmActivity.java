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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tw.myapp.ballsiteapp.databinding.ActivityConfirmBinding;

public class ConfirmActivity extends AppCompatActivity {

    ActivityConfirmBinding binding;
    ExecutorService executor;
    SharedPreferences userData;

    Handler RegisterHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            if (bundle.getInt("status") == 11) {

                // 記錄使用者相關資訊 到 context SharedPreferences 分享給其他 Activities 查詢
                SharedPreferences.Editor contextEditor = ConfirmActivity.this.getSharedPreferences("user_info", MODE_PRIVATE).edit();
                contextEditor.putString("siteID", binding.txtName2.getText().toString());
                contextEditor.putBoolean("register", true);
                contextEditor.apply();
                Toast.makeText(ConfirmActivity.this, "註冊成功", Toast.LENGTH_SHORT).show();
                Intent intentToMeetingRoom = new Intent(ConfirmActivity.this, MainActivity.class);
                startActivity(intentToMeetingRoom);
            } else {
                Toast.makeText(ConfirmActivity.this, "註冊失敗", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfirmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userData =getSharedPreferences("userData",MODE_PRIVATE);

        String name = userData.getString("name","");
        String tel = userData.getString("mobile","");
        String time = userData.getString("time","");
        String ymd = userData.getString("ymd","");

        binding.txtName2.setText(name);
        binding.txtTime2.setText(time);
        binding.txtTel2.setText(tel);
        binding.txtymd.setText(ymd);

        String siteID = userData.getString("site_id","");
        binding.textView35.setText(siteID);




        binding.OKBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject packet = new JSONObject();
                try {
                    JSONObject data = new JSONObject();
                    data.put("member_id",userData.getString("member_id","").toString());
                    data.put("site_id",binding.textView35.getText().toString());
                    data.put("day",binding.txtymd.getText().toString());
                    data.put("period_id",binding.txtTime2.getText().toString());
                    packet.put("date", data);
                    Log.w("API格式", packet.toString(5));
                } catch (JSONException e) {
                    Toast.makeText(ConfirmActivity.this, "資料格式異常,請重新輸入", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 使用網路通訊 Header + Body
                MediaType mType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(packet.toString(), mType);

                Request request = new Request.Builder()
                        .url("http://192.168.255.56:8123/api/site/rentSite")
                        .post(body)
                        .build();
                ConfirmActivity.SimpaleAPIWorker apiCaller = new ConfirmActivity.SimpaleAPIWorker(request);
                executor.execute(apiCaller);

                Intent intentMaintain = new Intent(ConfirmActivity.this, SettlementActivity.class);
                startActivity(intentMaintain);
            }
        });
        binding.BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfirmActivity.this, VenueRentalActivity.class);
                startActivity(intent);
            }
        });
    }

    public class SimpaleAPIWorker implements Runnable {
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
                if( result.getInt("status")== 11) {
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
