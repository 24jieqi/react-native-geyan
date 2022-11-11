package com.geyan.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class ViewUtil {
  public static int px2dip(Context context, float pxValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (pxValue / scale + 0.5f);
  }
  public static int dp2px(Context context, float dp) {
    float density = context.getResources().getDisplayMetrics().density;
    return (int) (dp * density + 0.5f);
  }
  public static void setStatusBarTransparent(Integer statusBarColor, Integer navigationBarColor, Activity context) {
    try {
      if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
        return;
      }
      if (statusBarColor == null && navigationBarColor == null) {
        return;
      }
      Window window = context.getWindow();
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        int option = window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        if(statusBarColor == Color.TRANSPARENT) {
          option |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        }
        if (navigationBarColor == Color.TRANSPARENT) {
          option |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }
        window.getDecorView().setSystemUiVisibility(option);
        window.setStatusBarColor(statusBarColor);
        window.setNavigationBarColor(navigationBarColor);
      } else {
        if (statusBarColor != null && statusBarColor == Color.TRANSPARENT) {
          window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (navigationBarColor != null && navigationBarColor == Color.TRANSPARENT) {
          window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
      }
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }

  public static void setStatusBarLightMode(boolean isLightColor, Activity activity) {
    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        Window window = activity.getWindow();
        int option = window.getDecorView().getSystemUiVisibility();
        if (isLightColor) {
          option |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        } else {
          option &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }
        window.getDecorView().setSystemUiVisibility(option);
      }
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }
}
