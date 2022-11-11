package com.geyan.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.geyan.R;
import com.geyan.util.ViewUtil;

public class ELoginActivity extends BaseActivity {
  private static String TAG = ELoginActivity.class.getSimpleName();

  private CheckBox checkBox;
  private View loginBtn;

  @Override
  public void onCreate(Bundle saveInstanceState) {
    super.onCreate(saveInstanceState);
    ViewUtil.setStatusBarTransparent(Color.TRANSPARENT, Color.TRANSPARENT, this);
    ViewUtil.setStatusBarLightMode(true, this);

    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
      findViewById(android.R.id.content).setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }
    setContentView(R.layout.activity_elogin);
    toolbarMoveDownward();
  }
}
