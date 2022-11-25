package com.geyan.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.geyan.R;


/**
 * 一键登录的隐私协议页
 */
public class ELoginWebActivity extends BaseActivity {
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

    // 协议页面没设置沉浸式

    setContentView(R.layout.activity_elogin_web);

    toolbar.setTitleTextColor(Color.BLACK);
    toolbar.setNavigationIcon(R.drawable.ic_back);
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
    // 协议页面没设置沉浸式，不需要调整
    // toolbarMoveDownward();

    initWebview();
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

  private void initWebview() {
    webView = (WebView) findViewById(R.id.elogin_web_web);

    try {
      WebSettings settings = webView.getSettings();
      settings.setJavaScriptEnabled(true);
      settings.setSavePassword(false);
//            webView.setWebChromeClient(new WebChromeClient());
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
