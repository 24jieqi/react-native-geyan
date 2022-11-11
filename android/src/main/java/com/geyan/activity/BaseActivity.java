package com.geyan.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toolbar;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;

import com.geyan.R;

import javax.annotation.Nullable;

public class BaseActivity extends AppCompatActivity {
  private FrameLayout frameLayout = null;
  protected Toolbar toolbar;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    super.setContentView(R.layout.activity_base);
    frameLayout = (FrameLayout) findViewById(R.id.layout_content);

    toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle("返回");
    toolbar.setTitleTextColor(Color.BLACK);
//    toolbar.setNavigationIcon();
    toolbar.setNavigationOnClickListener((v) -> { onBackPressed(); });
  }
  @Override
  public void setContentView(@LayoutRes int layoutResID) {
    frameLayout.removeAllViews();
    View.inflate(this, layoutResID, frameLayout);
    onContentChanged();
  }

  @Override
  public void setContentView(View view) {
    frameLayout.removeAllViews();
    frameLayout.addView(view);
    onContentChanged();
  }
  @Override
  public void setContentView(View view, ViewGroup.LayoutParams params) {
    frameLayout.removeAllViews();
    frameLayout.addView(view, params);
    onContentChanged();
  }

  private int getStatusBarHeight() {
    int height = 0;
    int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
    if (resourceId > 0) {
      height = getResources().getDimensionPixelSize(resourceId);
    }
    return height;
  }
  protected void toolbarMoveDownward() {
    int px = getStatusBarHeight();
    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) toolbar.getLayoutParams();
    layoutParams.topMargin = layoutParams.topMargin + px;
    toolbar.setLayoutParams(layoutParams);
  }
}
