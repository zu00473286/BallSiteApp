package tw.myapp.ballsiteapp;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Menu;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Request;
import tw.myapp.ballsiteapp.databinding.ActivityMainBinding;
import tw.myapp.ballsiteapp.util.JSonToDB2;
import tw.myapp.ballsiteapp.util.SimpleAPIWorker;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    SharedPreferences activityPreference;
    Request request;
    SQLiteDatabase db;
    ExecutorService executor;

    String createTable =
            "create table if not exists Sites(" +
                    "site_id text," +
                    "no_id text," +
                    "category_id text);";

    Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            String jsonString;
            Bundle bundle = msg.getData();
            int status = bundle.getInt("status");
            if(status==200){
                db.execSQL(createTable);
                jsonString=bundle.getString("data");
                JSonToDB2 j2db = new JSonToDB2(db);
                j2db.writeToDatabase(jsonString);
            }else{
                Log.d("網路",bundle.getString("data"));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db=openOrCreateDatabase("Sites",MODE_PRIVATE,null);

        db.execSQL(createTable);

        request=new Request.Builder().url("http://192.168.0.15:8123/api/site/SiteAll").build();

        executor= Executors.newSingleThreadExecutor();
        SimpleAPIWorker downLoadData=new SimpleAPIWorker(request,handler);
        executor.execute(downLoadData);

        checkA();


        setSupportActionBar(binding.appBarMain.toolbar);
        /*binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }
    public void checkA(){

        db=openOrCreateDatabase("Sites",MODE_PRIVATE,null);
        db.execSQL(createTable);
        Cursor cursor=db.rawQuery("select * from Site",null);
        if(cursor==null || cursor.getCount()==0){
            Log.d("網路","沒有資料");
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}