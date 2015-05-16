package com.warship.helper.app;

import android.app.Application;
import android.content.Context;

/**
 * @author qinbi@wandoujia.com (Bi Qin)
 */
public class WshApp extends Application {

  private static Context sContext;


  @Override
  public void onCreate() {
    sContext = this;
    super.onCreate();
  }

  public static Context getAppContext() {
    return sContext;
  }
}
