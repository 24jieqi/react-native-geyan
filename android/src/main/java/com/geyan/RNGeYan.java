package com.geyan;

import android.app.Activity;

import androidx.annotation.Nullable;

import com.g.gysdk.GYManager;
import com.g.gysdk.GYResponse;
import com.g.gysdk.GyCallBack;
import com.g.gysdk.GyConfig;

public class RNGeYan {
  public static void init(Activity activity, @Nullable String channel) {
    GyConfig.Builder builder = GyConfig.with(activity.getApplicationContext()).preLoginUseCache(true);
    if (channel != null) {
      builder.channel(channel);
    }
    builder.callBack(new GyCallBack() {
      @Override
      public void onSuccess(GYResponse gyResponse) {

      }

      @Override
      public void onFailed(GYResponse gyResponse) {

      }
    });
    GYManager.getInstance().init(builder.build());
    GYManager.getInstance().ePreLogin(8000, new GyCallBack() {
      @Override
      public void onSuccess(GYResponse gyResponse) {

      }

      @Override
      public void onFailed(GYResponse gyResponse) {

      }
    });
  }
}
