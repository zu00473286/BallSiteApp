package tw.myapp.ballsiteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;

import tw.myapp.ballsiteapp.databinding.ActivityVenueRentalBinding;

public class VenueRentalActivity extends AppCompatActivity {

    ActivityVenueRentalBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVenueRentalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
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
                        binding.CalenderSelectBtn.setText("日期:" + year + "/" + (month+1)+ "/" + dayOfMonth );
                    }
                }, y,m,d);
                dialog.show();
            }
        });

        binding.OKBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMaintain = new Intent(VenueRentalActivity.this, ConfirmActivity.class);
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
}