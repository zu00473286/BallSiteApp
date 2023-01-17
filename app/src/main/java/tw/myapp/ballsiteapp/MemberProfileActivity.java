package tw.myapp.ballsiteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.concurrent.ExecutorService;

import tw.myapp.ballsiteapp.databinding.ActivityMemberProfileBinding;

public class MemberProfileActivity extends AppCompatActivity {
    ActivityMemberProfileBinding binding;
    SQLiteDatabase db;
    SharedPreferences activityPreference;
    ExecutorService executor ;
    final static String createTable =
            "create table if not exists restaurant(" +
                    "member_id text," +
                    "name text," +
                    "mobile text," +
                    "email text," +
                    "asswd text);";
    Handler dataHandler = new Handler(Looper.getMainLooper()) {
        // 當 網路下載的執行緒 從觀光局網站下載 JSON後會傳到這個 Handler 進行處理
        // 1. 可以選擇在 Handler 進行轉換
        // 2. 可在 Thread 當下就進行寫入
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            String jsonString;
            Bundle bundle = msg.getData();
            int status = bundle.getInt("status");
            if (status == 200) {
                db.execSQL("drop table if exists restaurant;");
                db.execSQL(createTable);
                jsonString = bundle.getString("data");
            }

        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMemberProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}