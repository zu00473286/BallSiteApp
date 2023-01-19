package tw.myapp.ballsiteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tw.myapp.ballsiteapp.databinding.ActivityMemberProfileBinding;
import tw.myapp.ballsiteapp.util.JSonToDB;
import tw.myapp.ballsiteapp.util.SimpleAPIWorker;

public class MemberProfileActivity extends AppCompatActivity {
    ActivityMemberProfileBinding binding;
    SharedPreferences activityPreference;
    ExecutorService executor;
    final static String createTable =
            "create table if not exists restaurant(" +
                    "member_id text," +
                    "name text," +
                    "email text," +
                    "tel text," +
                    "passwd text);";
    Handler dataHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            String jsonString;
            Bundle bundle = msg.getData();
            int status = bundle.getInt("status");
            if (status == 200) {

            jsonString = bundle.getString("data");
            JSonToDB j2db = new JSonToDB(openOrCreateDatabase("restaurants", MODE_PRIVATE, null));
            j2db.writeToDatabase(jsonString);   // 如果資料量巨大  寫入超過時間 , Android ANR 又發生, 選擇 方法一 有執行緒的優點
        }
            Date now = new Date();
            activityPreference.edit().putString("lastUpdate", now.toString());
        }
    };
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            binding = ActivityMemberProfileBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            activityPreference = this.getPreferences(MODE_PRIVATE);
            executor = Executors.newSingleThreadExecutor();

        }
    }