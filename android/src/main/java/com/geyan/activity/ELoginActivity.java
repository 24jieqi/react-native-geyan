package com.geyan.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.geyan.R;
import com.geyan.util.ViewUtil;

public class ELoginActivity extends BaseActivity {
  public static final String PASSED_INFO = "com.reactnativegeyan.eloginactivity.passedinfo";
  private static String TAG = ELoginActivity.class.getSimpleName();

  private CheckBox checkBox;
  private Button loginBtn;
  private ImageView imageView;

  @Override
  public void onCreate(Bundle saveInstanceState) {
    super.onCreate(saveInstanceState);
    ViewUtil.setStatusBarTransparent(Color.TRANSPARENT, Color.TRANSPARENT, this);
    ViewUtil.setStatusBarLightMode(true, this);

    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
      findViewById(android.R.id.content).setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }
    setContentView(R.layout.activity_elogin);
    imageView = (ImageView) findViewById(R.id.logo_imageview);
    String logo = getIntent().getStringExtra(PASSED_INFO);
    Glide.with(this).load(logo).into(imageView);
    toolbarMoveDownward();

    // 设置事件
    loginBtn = (Button) findViewById(R.id.login_button);
    loginBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent();
        intent.putExtra("token", "this is token");
        setResult(Activity.RESULT_OK, intent);
        finish();
      }
    });
  }
}
