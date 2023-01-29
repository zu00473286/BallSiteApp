package tw.myapp.ballsiteapp.ui.home;


import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import okhttp3.Request;
import tw.myapp.ballsiteapp.SiteActivity;
import tw.myapp.ballsiteapp.databinding.FragmentHomeBinding;
import tw.myapp.ballsiteapp.util.JSonToDB2;
import tw.myapp.ballsiteapp.util.SimpleAPIWorker;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    SharedPreferences activityPreference;
    Request request;
    SQLiteDatabase db;
    ExecutorService executor;

    final static String createTable =
            "create table if not exists sites(" +
                    "site_id text," +
                    "no_id text," +
                    "category_id text);";

    Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            String jsonString;
            Bundle bundle = msg.getData();

                db.execSQL("drop table if exists sites;");
                db.execSQL(createTable);
                jsonString=bundle.getString("data");
                JSonToDB2 j2db = new JSonToDB2(db);
                j2db.writeToDatabase(jsonString);

        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        db=openOrCreateDatabase("site",null);

        db.execSQL(createTable);

        request=new Request.Builder().url("http://192.168.0.15:8123/???").build();

        executor= Executors.newSingleThreadExecutor();
        SimpleAPIWorker downLoadData=new SimpleAPIWorker(request,handler);
        executor.execute(downLoadData);

        checkA();

        binding.GoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SiteActivity.class);
                startActivity(intent);
            }
        });

        //final TextView textView = binding.textHome;
        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }
    public void checkA(){
        db=openOrCreateDatabase("Sites",null);
        db.execSQL(createTable);
        Cursor cursor=db.rawQuery("select * from product",null);
        if(cursor==null || cursor.getCount()==0){
            Log.d("網路","沒有資料");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}