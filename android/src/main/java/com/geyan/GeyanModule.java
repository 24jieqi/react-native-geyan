package com.geyan;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.module.annotations.ReactModule;
import com.g.gysdk.GYManager;
import com.g.gysdk.GYResponse;
import com.g.gysdk.GyCallBack;
import com.g.gysdk.GyConfig;
import com.geyan.activity.ELoginActivity;
import com.geyan.util.Privacy;

import java.util.ArrayList;

@ReactModule(name = GeyanModule.NAME)
public class GeyanModule extends ReactContextBaseJavaModule {
  public static final String NAME = "Geyan";
  private static final int REQUEST_CODE = 100;
  private static final String OPEN_ACTIVITY_ERROR = "OPEN_ACTIVITY_ERROR";
  private Promise myPromise;
  final ReactApplicationContext reactContext;
  public GeyanModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
    this.reactContext.addActivityEventListener(myActivityListener);
  }
  private final ActivityEventListener myActivityListener = new BaseActivityEventListener() {
    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
      if (requestCode == REQUEST_CODE) {
        if (myPromise != null) {
          if (resultCode == Activity.RESULT_OK) {
            WritableMap payload = Arguments.createMap();
            payload.putString("token", intent.getStringExtra("token"));
            payload.putString("gyuid", intent.getStringExtra("gyuid"));
            payload.putInt("code", intent.getIntExtra("code", -1));
            payload.putDouble("expiredTime", (double) intent.getLongExtra("expiredTime", -1));
            payload.putString("operator", intent.getStringExtra("operator"));
            myPromise.resolve(payload);

            myPromise.resolve(payload);
          }
          // 确保只触发一次
          myPromise = null;
        }
      }
    }
  };
  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

  @ReactMethod
  public void open(ReadableMap config, Promise promise) {
    Activity activity = getCurrentActivity();
    if (activity == null) {
      Log.d("Activity not exist", "Activity not exist");
      return;
    }
    myPromise = promise;
    if (!isPreLoginResultValid()) {
      myPromise.reject("PRE_LOGIN_ERROR", "预登录失败！");
      return;
    }
    try {
      Intent intent = new Intent(activity, ELoginActivity.class);
      intent.putExtra(ELoginActivity.LOGO_INFO, config.getString("logo"));
      ReadableArray privacyRawList = config.getArray("privacy");
      ArrayList<Privacy> privacyList = new ArrayList<>();
      if (privacyRawList != null) {
        for (int i=0; i < privacyRawList.size(); i += 1) {
          ReadableMap current = privacyRawList.getMap(i);
          privacyList.add(new Privacy(current.getString("text"), current.getString("url")));
        }
      }
      intent.putExtra(ELoginActivity.PRIVACY_INFO, privacyList);
      activity.startActivityForResult(intent, REQUEST_CODE);
    } catch (Exception e) {
        myPromise.reject(OPEN_ACTIVITY_ERROR, e);
        myPromise = null;
    }
  }
  @ReactMethod
  public boolean isPreLoginResultValid() {
    return GYManager.getInstance().isPreLoginResultValid();
  }
  @ReactMethod
  protected void init(ReadableMap config, Promise promise) {
    GyConfig.Builder builder = GyConfig.with(this.getReactApplicationContext()).preLoginUseCache(true);
    String channel = config.getString("channel");
    if (channel != null) {
      builder.channel(channel);
    }
    builder.callBack(new GyCallBack() {
      @Override
      public void onSuccess(GYResponse gyResponse) {
        GYManager.getInstance().ePreLogin(10000, new GyCallBack() {
          @Override
          public void onSuccess(GYResponse gyResponse) {
            promise.resolve("预登录完成！");
          }

          @Override
          public void onFailed(GYResponse gyResponse) {
            promise.reject("PRE_LOGIN_FAILED", "预登录失败！" + gyResponse.getMsg());
          }
        });
      }

      @Override
      public void onFailed(GYResponse gyResponse) {
      promise.reject("INIT_FAILED", "初始化失败！" + gyResponse.getMsg());
      }
    });
    GYManager manager = GYManager.getInstance();
    manager.setMacEnable(reactContext, false);
    manager.init(builder.build());
  }
}

