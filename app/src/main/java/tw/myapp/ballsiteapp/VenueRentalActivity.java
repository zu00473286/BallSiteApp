package tw.myapp.ballsiteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tw.myapp.ballsiteapp.databinding.ActivityVenueRentalBinding;

public class VenueRentalActivity extends AppCompatActivity {

    ActivityVenueRentalBinding binding;

    SharedPreferences userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVenueRentalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userData =getSharedPreferences("userData",MODE_PRIVATE);


        binding.CalenderSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Calendar calendar = Calendar.getInstance(); //取得java內建的 日曆物件
                int y = calendar.get(calendar.YEAR);
                int m = calendar.get(calendar.MONTH);
                int d = calendar.get(calendar.DATE);
                DatePickerDialog dialog = new DatePickerDialog(VenueRentalActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        binding.CalenderSelectBtn.setText(year + "/" + (month+1)+ "/" + dayOfMonth );
                        SharedPreferences.Editor editor = userData.edit();
                        editor.putString("ymd",binding.CalenderSelectBtn.getText().toString());
                        editor.apply();
                    }
                }, y,m,d);
                dialog.show();
            }
        });

        binding.TimeSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                periodSelectAlterDialog();  // 點擊後執行副程式

            }
        });

        binding.OKBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMaintain = new Intent(VenueRentalActivity.this, SiteListActivity.class);
                startActivity(intentMaintain);
            }
        });
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VenueRentalActivity.this, SiteActivity.class);
                startActivity(intent);
            }
        });
    }


    // 寫一個條列式選單給 TimeSelectBtn 使用
    public void periodSelectAlterDialog() {
        final String period[] = {
                "10:00~11:00", "11:00~12:00",
                "12:00~13:00", "13:00~14:00",
                "14:00~15:00", "15:00~16:00",
                "16:00~17:00", "17:00~18:00",
                "18:00~19:00", "19:00~20:00",
                "20:00~21:00", "21:00~22:00"}; //先建立個字串陣列
        AlertDialog.Builder builder = new AlertDialog.Builder(VenueRentalActivity.this);
        builder.setTitle("時間選項");


        builder.setItems(period, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                binding.TimeSelectBtn.setText(period[i]);

                SharedPreferences.Editor editor = userData.edit();
                editor.putString("time", period[i]);
                editor.putInt("period_id", i+1);
                editor.apply();
            }
        });
        builder.create().show();    //一定要創建並且展示才會成功
    }
}