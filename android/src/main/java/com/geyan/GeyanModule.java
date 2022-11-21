package com.geyan;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.module.annotations.ReactModule;
import com.geyan.activity.ELoginActivity;

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
            String token = intent.getStringExtra("token");
            myPromise.resolve(token);
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

  // Example method
  // See https://reactnative.dev/docs/native-modules-android
  @ReactMethod
  public void multiply(double a, double b, Promise promise) {
    promise.resolve(a * b);
  }

  @ReactMethod
  public void showActivity(ReadableMap config, Promise promise) {
    Activity activity = getCurrentActivity();
    if (activity == null) {
      Log.d("Activity not exist", "Activity not exist");
      return;
    }
    myPromise = promise;
    try {
      Intent intent = new Intent(activity, ELoginActivity.class);
      intent.putExtra(ELoginActivity.PASSED_INFO, config.getString("logo"));
      activity.startActivityForResult(intent, REQUEST_CODE);
    } catch (Exception e) {
        myPromise.reject(OPEN_ACTIVITY_ERROR, e);
        myPromise = null;
    }
  }
}

