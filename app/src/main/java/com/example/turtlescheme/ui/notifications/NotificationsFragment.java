package com.example.turtlescheme.ui.notifications;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.turtlescheme.R;

import java.util.Locale;
import java.util.Objects;

public class NotificationsFragment extends Fragment
{

    @SuppressLint("SetJavaScriptEnabled")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        WebView mywebview = root.findViewById(R.id.webView);
        WebSettings webSettings = mywebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        Log.d("Language",Locale.getDefault().getDisplayLanguage());
        if(Locale.getDefault().getLanguage().equals("en"))
            mywebview.loadUrl("https://turtlesketch.consulting/en-us/index.php");
        else
            mywebview.loadUrl("https://turtlesketch.consulting/es-es/index.php");
        mywebview.setWebViewClient(new WebViewClient());
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((AppCompatActivity)requireActivity()).getSupportActionBar()).hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        Objects.requireNonNull(((AppCompatActivity)requireActivity()).getSupportActionBar()).show();
    }
}