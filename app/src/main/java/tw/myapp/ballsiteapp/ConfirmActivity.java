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
        String siteID = userData.getString("siteID","");

        binding.txtName2.setText(name);
        binding.txtTime2.setText(time);
        binding.txtTel2.setText(tel);
        binding.txtymd.setText(ymd);
        binding.textView35.setText(siteID);






        binding.OKBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject packet = new JSONObject();
                try {
                    JSONObject data = new JSONObject();
                    data.put("member_id",userData.getString("member_id",""));
                    data.put("siteID",binding.textView35.getText().toString());
                    data.put("ymd",binding.txtymd.getText().toString());
                    data.put("time",binding.txtTime2.getText().toString());
                    packet.put("data", data);
                    Log.w("API格式", packet.toString(5));
                } catch (JSONException e) {
                    Toast.makeText(ConfirmActivity.this, "資料格式異常,請重新輸入", Toast.LENGTH_SHORT).show();
                }
                // 使用網路通訊 Header + Body
                MediaType mType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(packet.toString(), mType);

                Request request = new Request.Builder()
                        .url("http://192.168.0.15:8123/api/site/rentSite")
                        .post(body)
                        .build();
                SimpaleAPIWorker apiCaller = new SimpaleAPIWorker(request);
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
    Handler Handler =new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();


            binding.txtName2.setText(bundle.getString("name"));
            binding.txtTel2.setText(bundle.getString("mobile"));
            binding.txtTime2.setText(bundle.getString("time"));
            binding.txtymd.setText(bundle.getString("ymd"));
            binding.textView35.setText(bundle.getString("siteID"));

            SharedPreferences.Editor editor = userData.edit();
            editor.putString("name", binding.txtName2.getText().toString());
            editor.putString("mobile", binding.txtTel2.getText().toString());
            editor.putString("time", binding.txtTime2.getText().toString());
            editor.putString("ymd", binding.txtymd.getText().toString());
            editor.putString("siteID",binding.textView35.getText().toString());
            editor.apply();
        }
    };

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
                Message m = Handler.obtainMessage();
                Bundle bundle = new Bundle();


                bundle.putString("name", result.getString("name"));
                bundle.putString("mobile", result.getString("mobile"));
                bundle.putString("time", result.getString("time"));
                bundle.putString("ymd", result.getString("ymd"));
                bundle.putString("site_id", result.getString("site_id"));

                m.setData(bundle);
                Handler.sendMessage(m);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
