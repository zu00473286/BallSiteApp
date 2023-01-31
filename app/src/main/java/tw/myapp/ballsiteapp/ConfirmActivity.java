package tw.myapp.ballsiteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import java.util.concurrent.Executors;

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

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfirmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userData =getSharedPreferences("userData",MODE_PRIVATE);

        String name = userData.getString("name","");
        String tel = userData.getString("mobile","");
        String time = userData.getString("time","");
        int pos = userData.getInt("period_id",-1);
        String ymd = userData.getString("ymd","");
        String siteID = userData.getString("siteID","");
        String site_id = userData.getString("site_id","");
        String price = userData.getString("price", "");

        binding.txtName2.setText(name);
        binding.txtTime2.setText(time);
        binding.txtTel2.setText(tel);
        binding.txtymd.setText(ymd);
        binding.textView35.setText(siteID);
        binding.textView36.setText(price);
        
        binding.OKBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject packet = new JSONObject();
                try {
                    JSONObject data = new JSONObject();
                    //Cursor cursor;
                    //String noID = binding.textView35.getText().toString();
                    //cursor = db.rawQuery("SELECT site_id FROM Sites WHERE no_id=" + noID, null);
                    data.put("site_id",Integer.parseInt(site_id));

                    data.put("member_id",Integer.parseInt(userData.getString("member_id","")));
                    data.put("day",binding.txtymd.getText().toString());
                    data.put("period_id",pos);
                    packet.put("data", data);
                    Log.w("API格式", packet.toString(5));
                } catch (JSONException e) {
                    Toast.makeText(ConfirmActivity.this, "資料格式異常,請重新輸入", Toast.LENGTH_SHORT).show();
                }
                // 使用網路通訊 Header + Body
                MediaType mType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(packet.toString(), mType);

                executor= Executors.newSingleThreadExecutor();

                Request request = new Request.Builder()
                        .url("http://20.2.70.0:8123/api/site/rentSite")
                        .post(body)
                        .build();
                SimpaleAPIWorker apiCaller = new SimpaleAPIWorker(request);
                executor.execute(apiCaller);
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

            if (bundle.getInt("status") == 11) {
                binding.txtName2.setText(bundle.getString("name"));
                binding.txtTel2.setText(bundle.getString("mobile"));
                binding.txtTime2.setText(bundle.getString("time"));
                binding.txtymd.setText(bundle.getString("ymd"));
                binding.textView35.setText(bundle.getString("siteID"));

                Toast.makeText(ConfirmActivity.this, "租借成功", Toast.LENGTH_SHORT).show();
                Intent intentMaintain = new Intent(ConfirmActivity.this, SettlementActivity.class);
                startActivity(intentMaintain);

            } else {
                Toast.makeText(ConfirmActivity.this, "租借失敗", Toast.LENGTH_SHORT).show();
            }
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

                JSONObject result = new JSONObject(responseString);
                Message m = Handler.obtainMessage();
                Bundle bundle = new Bundle();
                if( result.getInt("status")== 11) {
                    bundle.putString("mesg", result.getString("mesg"));
                    bundle.putInt("status",result.getInt("status") );
                } else {
                    bundle.putString("mesg", "登入失敗,請確認有無帳號,或密碼是否有誤");
                    bundle.putInt("status",result.getInt("status") );
                }
                m.setData(bundle);
                Handler.sendMessage(m);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}