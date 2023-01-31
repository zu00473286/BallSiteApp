package tw.myapp.ballsiteapp;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tw.myapp.ballsiteapp.databinding.ActivityMainBinding;
import tw.myapp.ballsiteapp.util.JSonToDB;
import tw.myapp.ballsiteapp.util.JSonToDB2;
import tw.myapp.ballsiteapp.util.SimpleAPIWorker;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;


    SQLiteDatabase db;
    ExecutorService executor;

    SharedPreferences memberDataPre;

    String createTable =
            "create table if not exists Sites(" +
                    "site_id text," +
                    "no_id text," +
                    "category_id text);";

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            String jsonString;
            Bundle bundle = msg.getData();

            db.execSQL(createTable);
            jsonString = bundle.getString("data");
            JSonToDB2 j2db = new JSonToDB2(db);
            j2db.writeToDatabase(jsonString);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = openOrCreateDatabase("Sites", MODE_PRIVATE, null);

        db.execSQL(createTable);
        JSONObject packet = new JSONObject();
        try {
            JSONObject data = new JSONObject();
            packet.put("data", data);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType mType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(packet.toString(), mType);
        Request request = new Request.Builder()
                .url("http://20.2.70.0:8123/api/site/SiteAll")
                .post(body)
                .build();

        executor = Executors.newSingleThreadExecutor();
        SimpleAPIWorker downLoadData = new SimpleAPIWorker(request, handler);
        executor.execute(downLoadData);

        //checkA();


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

    public void checkA() {

        db = openOrCreateDatabase("Sites", MODE_PRIVATE, null);
        db.execSQL(createTable);


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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                AlertDialog.Builder logoutbtn = new AlertDialog.Builder(this);
                logoutbtn.setTitle("登出");
                logoutbtn.setMessage("確定要登出嗎?");
                logoutbtn.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        memberDataPre = getSharedPreferences("memberDataPre", MODE_PRIVATE);
                        SharedPreferences.Editor editor = memberDataPre.edit();
                        editor.remove("name");
                        editor.remove("mobile");
                        editor.remove("email");
                        editor.apply();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });
                logoutbtn.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = logoutbtn.create();
                dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }
}
