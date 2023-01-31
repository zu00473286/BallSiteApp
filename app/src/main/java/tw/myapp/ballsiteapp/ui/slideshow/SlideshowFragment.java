package tw.myapp.ballsiteapp.ui.slideshow;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import tw.myapp.ballsiteapp.MemberProfileActivity;

import tw.myapp.ballsiteapp.R;
import tw.myapp.ballsiteapp.databinding.AppBarMainBinding;
import tw.myapp.ballsiteapp.databinding.FragmentSlideshowBinding;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;

    SharedPreferences userData;

    ExecutorService executor;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        executor = Executors.newSingleThreadExecutor();

        userData = getActivity().getSharedPreferences("userData", MODE_PRIVATE);
        String memberEmailDataCheck = userData.getString("email", "查無資料");//取得登入後儲存的會員EMAIL
        Log.e("JSON", "會員email" + userData.getString("email", "查無資料"));
        String memberNameDataCheck = userData.getString("name", "查無資料");
        //如果SharedPreferance裡面的memberDataPre檔案裡的name沒有資料，就從網路下載會員資料


        JSONObject packet = new JSONObject();
        try {
            JSONObject data = new JSONObject();
            data.put("email", memberEmailDataCheck);//抓出登入時儲存在SharedPreferance的會員EMAIL
            packet.put("data", data);

            Log.e("JSON", "這裡是從網路下載的會員資料");
            Toast.makeText(getActivity(), "已送出EMAIL抓取會員資料", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //把email資料封裝成JSON格式 透過網路傳給Sever
        MediaType mType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(packet.toString(), mType);
        Request request = new Request.Builder()
                .url("http://20.2.70.0:8123/api/member/memberAll")
                .post(body)
                .build();
        SimpaleAPIWorker apiCaller = new SimpaleAPIWorker(request, memberDataHandler);
        //產生Task準備給executor執行
        executor.execute(apiCaller);


        binding.ReviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MemberProfileActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }

    Handler memberDataHandler =new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();

            binding.txtid.setText(bundle.getString("member_id"));
            binding.Name1.setText(bundle.getString("name"));
            binding.tel1.setText(bundle.getString("mobile"));
            binding.email1.setText(bundle.getString("email"));
            binding.pass1.setText(bundle.getString("passwd"));
            binding.txtpn2.setText(bundle.getString("money"));

            SharedPreferences.Editor editor = userData.edit();
            editor.putString("member_id", binding.txtid.getText().toString());
            editor.putString("name", binding.Name1.getText().toString());
            editor.putString("mobile", binding.tel1.getText().toString());
            editor.putString("email", binding.email1.getText().toString());
            editor.putString("passwd", binding.pass1.getText().toString());
            editor.putString("money", binding.txtpn2.getText().toString());
            editor.apply();
        }
    };
    class SimpaleAPIWorker implements  Runnable {
        OkHttpClient client;
        Request request;

        public SimpaleAPIWorker(Request request, Handler memberDataHandler) {
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
                Message m = memberDataHandler.obtainMessage();
                Bundle bundle = new Bundle();

                bundle.putString("member_id", result.getString("member_id"));
                bundle.putString("name", result.getString("name"));
                bundle.putString("mobile", result.getString("mobile"));
                bundle.putString("email", result.getString("email"));
                bundle.putString("passwd", result.getString("passwd"));
                bundle.putString("money", result.getString("money"));

                m.setData(bundle);
                memberDataHandler.sendMessage(m);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
