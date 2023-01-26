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
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tw.myapp.ballsiteapp.databinding.ActivityMemberProfileBinding;

public class MemberProfileActivity extends AppCompatActivity {
    ActivityMemberProfileBinding binding;
    ExecutorService executorService;

    SharedPreferences sharedPreferences;
    Handler memberChangeHandler=new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle=msg.getData();
            if(bundle.getInt("status")==123){
                Toast.makeText(MemberProfileActivity.this, bundle.getString("mesg"), Toast.LENGTH_LONG).show();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMemberProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        executorService = Executors.newSingleThreadExecutor();
        sharedPreferences = getSharedPreferences("memberDataPre", MODE_PRIVATE);
        binding.txtEm.setText(sharedPreferences.getString("email", "查無資料"));
        binding.txtName.setText(sharedPreferences.getString("name", "查無資料"));
        binding.txtTel.setText(sharedPreferences.getString("phone", "查無資料"));
        binding.btnCh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.txtName.getText().toString();
                String phone = binding.txtTel.getText().toString();
                String money = binding.txtpn.getText().toString();
                String pwd = binding.txtPass.getText().toString();
                String pwd2 = binding.txtpassch.getText().toString();
                Boolean isName = binding.txtName.getText().toString().isEmpty();
                Boolean isPhone = binding.txtTel.getText().toString().isEmpty();
                Boolean ispn = binding.txtpn.getText().toString().isEmpty();
                Boolean isPwd = binding.txtPass.getText().toString().isEmpty();
                Boolean isPwd2 = binding.txtpassch.getText().toString().isEmpty();
                if (name != null && phone != null && !isName && !isPhone && (!pwd.isEmpty() && pwd != null && pwd.equals(pwd2))) {//pwd跟pwd2要一樣 就不用判斷pwd2是不是空白的了5
                    JSONObject packet = new JSONObject();
                    try {
                        JSONObject newMemberRegData = new JSONObject();
                        newMemberRegData.put("name", name);
                        newMemberRegData.put("pwd", pwd);
                        newMemberRegData.put("phone", phone);
                        newMemberRegData.put("email", sharedPreferences.getString("email", "查無資料"));
                        newMemberRegData.put("money", money);
                        packet.put("NewMemberData", newMemberRegData);
                        Log.e("JSON", packet.toString(4));
                        //把修改的會員姓名和電話寫入memberDataPre檔案
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("name", binding.txtName.getText().toString());
                        editor.putString("phone", binding.txtTel.getText().toString());
                        editor.apply();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    MediaType mType = MediaType.parse("application/json");
                    RequestBody body = RequestBody.create(packet.toString(), mType);
                    Request request = new Request.Builder()
                            .url("http://192.168.255.14:8123/api/member/memberAll")
                            .post(body)
                            .build();
                    Toast.makeText(MemberProfileActivity.this, "已送出修改的會員資料", Toast.LENGTH_LONG).show();
                    SimpaleAPIWorker apiCaller = new SimpaleAPIWorker(request);
                    //產生Task準備給executor執行
                    executorService.execute(apiCaller);

                } else {
                    Toast.makeText(MemberProfileActivity.this, "請確認輸入相同密碼,且欄位不可空白", Toast.LENGTH_LONG).show();
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
                Response response=client.newCall(request).execute();
                String responseString=response.body().string();
                Log.e("API回應",responseString);
                //Response也應該是JSON格式回傳 由APP端確認登入結果
                JSONObject result=new JSONObject(responseString);
                Message m=memberChangeHandler.obtainMessage();
                Bundle bundle=new Bundle();
                if (result.getInt("status")==123){//確認修改會員資料狀態
                    bundle.putString("mesg",result.getString("mesg"));
                    bundle.putInt("status",result.getInt("status"));
                } else {
                    bundle.putString("mesg","系統錯誤，請洽程式開發人員");
                    bundle.putInt("status",result.getInt("status"));
                }
                m.setData(bundle);
                memberChangeHandler.sendMessage(m);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}