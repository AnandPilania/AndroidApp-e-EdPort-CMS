package com.application.schooltime;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.SslError;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


public class SchoolActivity extends AppCompatActivity {

    private static final String PAGE_URL = "https://cms001.schooltimes.ca/";
    private WebView webView;
    LinearLayout layout_error  ;
    Button  btn_retry ;
    ProgressBar progress ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_times);

        layout_error = findViewById(R.id.error_layout);
        btn_retry = findViewById(R.id.btn_retry);
        progress = findViewById(R.id.progress);
        btn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadWebViewUrl(PAGE_URL);
            }
        });
        CookieSyncManager.createInstance(this);

        initWebView();

    }

    public class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;

        }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progress.setVisibility(View.VISIBLE);

        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            webView.setVisibility(View.VISIBLE);
            layout_error.setVisibility(View.GONE);
            progress.setVisibility(View.GONE);
        }
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            //Toast.makeText(SchoolActivity.this,"Something Wrong",Toast.LENGTH_SHORT).show();
            webView.setVisibility(View.VISIBLE);
            layout_error.setVisibility(View.GONE);
        }
        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);

            //Toast.makeText(SchoolActivity.this,"Something Wrong",Toast.LENGTH_SHORT).show();
            webView.setVisibility(View.VISIBLE);
            layout_error.setVisibility(View.GONE);
        }
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);

        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            isCanGoBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initWebView() {
        webView = (WebView) findViewById(R.id.web);
        webView.setWebViewClient(new MyWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        LoadWebViewUrl(PAGE_URL);
    }

    private void LoadWebViewUrl(String url) {
        if (isInternetConnected()) {
            webView.setVisibility(View.VISIBLE);
            layout_error.setVisibility(View.GONE);
            webView.loadUrl(url);
        } else {
            webView.setVisibility(View.GONE);
            layout_error.setVisibility(View.VISIBLE);
        }
    }
    private void isCanGoBack() {
        if (webView.canGoBack())
            webView.goBack();
        else
            finish();
    }
    public boolean isInternetConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;

    }
    @Override
    protected void onPause() {
        super.onPause();
        CookieSyncManager.getInstance().stopSync();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CookieSyncManager.getInstance().startSync();
    }
}
