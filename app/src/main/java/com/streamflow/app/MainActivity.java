package com.streamflow.app;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private LinearLayout setupLayout;
    private EditText ipInput;
    private EditText portInput;
    private Button connectButton;
    private ProgressBar loadingProgress;
    private SharedPreferences prefs;

    private static final String PREFS_NAME = "StreamFlowPrefs";
    private static final String KEY_SERVER_IP = "server_ip";
    private static final String KEY_SERVER_PORT = "server_port";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Fullscreen mode
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        
        // Initialize views
        webView = findViewById(R.id.webView);
        setupLayout = findViewById(R.id.setupLayout);
        ipInput = findViewById(R.id.ipInput);
        portInput = findViewById(R.id.portInput);
        connectButton = findViewById(R.id.connectButton);
        loadingProgress = findViewById(R.id.loadingProgress);

        // Setup WebView
        setupWebView();

        // Check for saved server
        String savedIp = prefs.getString(KEY_SERVER_IP, "");
        String savedPort = prefs.getString(KEY_SERVER_PORT, "7575");
        
        if (!savedIp.isEmpty()) {
            // Auto-connect to saved server
            ipInput.setText(savedIp);
            portInput.setText(savedPort);
            connectToServer(savedIp, savedPort);
        } else {
            // Show setup screen
            showSetupScreen();
        }

        connectButton.setOnClickListener(v -> {
            String ip = ipInput.getText().toString().trim();
            String port = portInput.getText().toString().trim();
            
            if (ip.isEmpty()) {
                Toast.makeText(this, "Please enter server IP address", Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (port.isEmpty()) {
                port = "7575";
            }
            
            // Save settings
            prefs.edit()
                .putString(KEY_SERVER_IP, ip)
                .putString(KEY_SERVER_PORT, port)
                .apply();
            
            connectToServer(ip, port);
        });
    }

    private void setupWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                loadingProgress.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                loadingProgress.setVisibility(View.GONE);
                showConnectionError();
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress < 100) {
                    loadingProgress.setVisibility(View.VISIBLE);
                    loadingProgress.setProgress(newProgress);
                } else {
                    loadingProgress.setVisibility(View.GONE);
                }
            }
        });
    }

    private void showSetupScreen() {
        setupLayout.setVisibility(View.VISIBLE);
        webView.setVisibility(View.GONE);
        loadingProgress.setVisibility(View.GONE);
    }

    private void connectToServer(String ip, String port) {
        setupLayout.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);
        loadingProgress.setVisibility(View.VISIBLE);
        
        String url = "http://" + ip + ":" + port;
        webView.loadUrl(url);
    }

    private void showConnectionError() {
        new AlertDialog.Builder(this)
            .setTitle("Connection Failed")
            .setMessage("Cannot connect to StreamFlow server. Please check:\n\n" +
                "1. Server is running on your computer\n" +
                "2. Phone is on same WiFi network\n" +
                "3. IP address is correct")
            .setPositiveButton("Retry", (d, w) -> {
                String ip = prefs.getString(KEY_SERVER_IP, "");
                String port = prefs.getString(KEY_SERVER_PORT, "7575");
                connectToServer(ip, port);
            })
            .setNegativeButton("Change Server", (d, w) -> {
                showSetupScreen();
            })
            .show();
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else if (webView.getVisibility() == View.VISIBLE) {
            new AlertDialog.Builder(this)
                .setTitle("Exit StreamFlow?")
                .setMessage("Do you want to exit the app?")
                .setPositiveButton("Exit", (d, w) -> finish())
                .setNegativeButton("Cancel", null)
                .setNeutralButton("Change Server", (d, w) -> showSetupScreen())
                .show();
        } else {
            super.onBackPressed();
        }
    }
}
