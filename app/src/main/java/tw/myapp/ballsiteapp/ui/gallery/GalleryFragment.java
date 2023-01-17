package tw.myapp.ballsiteapp.ui.gallery;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import tw.myapp.ballsiteapp.databinding.FragmentGalleryBinding;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.txtEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mailIntent = new Intent();
                mailIntent.setAction( Intent.ACTION_VIEW);
                mailIntent.setData(Uri.parse("mailto: service@gmail.com"));
                startActivity(mailIntent);
            }
        });
        binding.Acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent telIntent = new Intent();
                telIntent.setAction( Intent.ACTION_DIAL);
                telIntent.setData(Uri.parse("tel:037-123456"));
                startActivity(telIntent);
            }
        });
        binding.txtMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent geoIntent = new Intent();
                geoIntent.setAction( Intent.ACTION_VIEW);
                //geoIntent.setData(Uri.parse("geo:"+24.802957 +","+ 120.973136 ));
                geoIntent.setData( Uri.parse("https://www.google.com/maps/place/新竹市東區中華路二段377號"));
                startActivity(geoIntent);
            }
        });

       // final TextView textView = binding.textGallery;
        //galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}