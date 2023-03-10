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



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}