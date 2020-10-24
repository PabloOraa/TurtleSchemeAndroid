package com.example.turtlescheme.ui.notifications;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.webkit.WebSettingsCompat;
import androidx.webkit.WebViewFeature;

import com.example.turtlescheme.R;

import java.util.Locale;

public class NotificationsFragment extends Fragment
{
    @SuppressLint("SetJavaScriptEnabled")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        WebView mywebview = root.findViewById(R.id.webView);
        WebSettings webSettings = mywebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode)
        {
            case Configuration.UI_MODE_NIGHT_NO:
                if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK))
                    WebSettingsCompat.setForceDark(mywebview.getSettings(), WebSettingsCompat.FORCE_DARK_OFF);
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK))
                    WebSettingsCompat.setForceDark(mywebview.getSettings(), WebSettingsCompat.FORCE_DARK_ON);
                break;
        }
        if(Locale.getDefault().getLanguage().equals("en"))
            mywebview.loadUrl("https://turtlesketch.consulting/en-us/index.php");
        else
            mywebview.loadUrl("https://turtlesketch.consulting/es-es/index.php");
        mywebview.setWebViewClient(new WebViewClient());

        return root;
    }
}