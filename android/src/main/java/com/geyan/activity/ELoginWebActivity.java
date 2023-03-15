package com.geyan.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toolbar;

import com.geyan.R;
import com.geyan.util.ViewUtil;

/**
 * 一键登录的隐私协议页
 */
public class ELoginWebActivity extends Activity {
  private WebView webView;

  public static void start(Context context, String url, String title) {
    try {
      Intent intent = new Intent(context, ELoginWebActivity.class);
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      intent.putExtra("url", url);
      intent.putExtra("title", title);
      context.startActivity(intent);
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_elogin_web);
    ViewUtil.setStatusBarTransparent(Color.WHITE, Color.TRANSPARENT, this);
    ViewUtil.setStatusBarLightMode(true, this);
    Toolbar toolbar = findViewById(R.id.privacy_toolbar);
    toolbar.setTitle(getIntent().getStringExtra("title"));
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        try {
          if (webView.canGoBack()) {
            webView.goBack();
            return;
          }
        } catch (Throwable t) {
          t.printStackTrace();
        }
        finish();
      }
    });
    initWebView();
  }

  @Override
  protected void onStart() {
    super.onStart();
  }


  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  protected void onDestroy() {
    try {
      ViewGroup viewGroup = (ViewGroup) webView.getParent();
      if (viewGroup != null) {
        viewGroup.removeView(webView);
      }

      webView.removeAllViews();
      webView.destroy();
      webView = null;
    } catch (Throwable t) {
      t.printStackTrace();
    }
    super.onDestroy();
  }

  private void initWebView() {
    webView = findViewById(R.id.elogin_web_web);

    try {
      WebSettings settings = webView.getSettings();
      settings.setJavaScriptEnabled(true);
      webView.setWebViewClient(new WebViewClient());
      settings.setAllowFileAccess(false);
      settings.setAllowContentAccess(true);
      settings.setDatabaseEnabled(true);
      settings.setDomStorageEnabled(true);
      settings.setAppCacheEnabled(true);
      settings.setUseWideViewPort(true);
      String url = this.getIntent().getStringExtra("url");
      webView.removeJavascriptInterface("searchBoxJavaBridge_");
      webView.removeJavascriptInterface("accessibility");
      webView.removeJavascriptInterface("accessibilityTraversal");
      webView.loadUrl(url);
    } catch (Throwable t) {
      t.printStackTrace();
      finish();
    }
  }


}
