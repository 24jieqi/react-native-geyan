package com.geyan.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.g.gysdk.EloginActivityParam;
import com.g.gysdk.GYManager;
import com.g.gysdk.GYResponse;
import com.g.gysdk.GyCallBack;
import com.g.gysdk.GyPreloginResult;
import com.geyan.R;
import com.geyan.util.Privacy;
import com.geyan.util.ViewUtil;

import org.json.JSONObject;

import java.util.ArrayList;

public class ELoginActivity extends AppCompatActivity {
  public static final String LOGO_INFO = "com.reactnativegeyan.eloginactivity.logo";
  public static final String PRIVACY_INFO = "com.reactnativegeyan.eloginactivity.privacy";
  private static final String TAG = ELoginActivity.class.getSimpleName();
  private CheckBox mCheckbox;
  private ProgressDialog dialog;
  @Override
  public void onCreate(Bundle saveInstanceState) {
    super.onCreate(saveInstanceState);
    ViewUtil.setStatusBarTransparent(Color.TRANSPARENT, Color.TRANSPARENT, this);
    ViewUtil.setStatusBarLightMode(true, this);
    setContentView(R.layout.activity_elogin);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setNavigationOnClickListener((v) -> { onBackPressed(); });
    ImageView imageView = (ImageView) findViewById(R.id.logo_imageview);
    String logo = getIntent().getStringExtra(LOGO_INFO);
    Glide.with(this).load(logo).into(imageView);
    elogin();
  }

  private void elogin() {
    TextView numberTv = findViewById(R.id.number_textview);
    TextView sloganTv = findViewById(R.id.slogan_textview);
    View loginBtn = findViewById(R.id.login_button);
    CheckBox checkBox = findViewById(R.id.privacy_checkbox);
    TextView privacyTv = findViewById(R.id.privacy_textview);
    initPrivacyText(privacyTv);
    mCheckbox = checkBox;
    EloginActivityParam eloginActivityParam = new EloginActivityParam()
      .setActivity(this)
      .setNumberTextview(numberTv)
      .setSloganTextview(sloganTv)
      .setLoginButton(loginBtn)
      .setPrivacyCheckbox(checkBox)
      .setPrivacyTextview(privacyTv)
      .setUiErrorListener((msg) -> {
          Log.d("Error", msg);
      })
      .setLoginOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (!mCheckbox.isChecked()) {
            showToast("请先仔细阅读协议并勾选，然后再点击登录");
            // 抛出错误，以免SDK登录调用
            throw new IllegalStateException("请先仔细阅读协议并勾选，然后再点击登录");
          }
          showDialog();
        }
      });
    GYManager.getInstance().eAccountLogin(eloginActivityParam, 5000, new GyCallBack() {
      @Override
      public void onSuccess(GYResponse gyResponse) {
        try {
          hideDialog();
          showToast("登录成功");
          JSONObject jsonObject = new JSONObject(gyResponse.getMsg());
          JSONObject data = jsonObject.getJSONObject("data");
          String token = data.getString("token");
          Intent intent = new Intent();
          intent.putExtra("token", token);
          setResult(Activity.RESULT_OK, intent);
          finish();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      @Override
      public void onFailed(GYResponse gyResponse) {
        showToast("一键登录失败：" + gyResponse);
        hideDialog();
        finish();
      }
    });
  }

  private void initPrivacyText(TextView textView) {
    textView.setLineSpacing(8.0F, 1.0F);
    textView.setMovementMethod(LinkMovementMethod.getInstance());
    GyPreloginResult preloginResult = GYManager.getInstance().getPreLoginResult();
    textView.setText("");
    textView.append("登录即认可");
    textView.append(generateSpan(preloginResult.getPrivacyName(), preloginResult.getPrivacyUrl()));
    textView.append("、");
    ArrayList<Privacy> privacyList = (ArrayList<Privacy>) getIntent().getSerializableExtra(PRIVACY_INFO);
    for (int i=0; i< privacyList.size(); i += 1) {
      Privacy config = privacyList.get(i);
      String name = config.text;
      String url = config.url;
      textView.append(generateSpan(name, url));
      if (i < privacyList.size() - 1) {
        textView.append("、");
      }
    }
    textView.append("并使⽤用本机号码登录");
  }

  private SpannableString generateSpan(final String name, final String url) {
    SpannableString spannableString = new SpannableString(name);
    spannableString.setSpan(new ClickableSpan() {
      public void onClick(View view) {
        ELoginWebActivity.start(ELoginActivity.this, url, name);
      }
      public void updateDrawState(TextPaint ds) {
        try {
          ds.setColor(0xFF3973FF);
          ds.setUnderlineText(false);
        } catch (Throwable t) {
          t.printStackTrace();
        }
      }
    }, 0, name.length(), 33);
    return spannableString;
  }

  /**
   * android show toast
   */
  private void showToast(final String message) {
    runOnUiThread(() -> {Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();});
  }

  /**
   * android show dialog
   */
  private void showDialog() {
    runOnUiThread(() -> {
      if (dialog == null) {
        dialog = new ProgressDialog(ELoginActivity.this);
      }
      dialog.setMessage("认证中...");
      dialog.setCancelable(false);
      dialog.show();
    });
  }

  /**
   * android hide dialog
   */
  private void hideDialog() {
    if (dialog != null) {
      dialog.hide();
    }
  }
}
