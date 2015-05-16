package com.warship.helper.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.warship.helper.app.WshApp;
import com.warship.helper.manager.navigation.NavigationManager;
import com.warship.helper.manager.navigation.NavigationParams;
import com.warship.helper.ui.fragment.BaseWshFragment;
import com.warship.helper.util.Const;

/**
 * @author qinbi@wandoujia.com (Bi Qin)
 */
public class WshActivity extends Activity {

  private BroadcastReceiver exitClickReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      if (TextUtils.equals(Const.Action.ACTION_APPLICATION_EXIT, intent.getAction())) {
        finish();
      }
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    boolean dispatchSuccess = NavigationManager.dispatchNavigation(this, getIntent());
    if (!dispatchSuccess) {
      navigateToPlayScene();
    }

    IntentFilter filter = new IntentFilter(Const.Action.ACTION_APPLICATION_EXIT);
    LocalBroadcastManager.getInstance(this).registerReceiver(exitClickReceiver, filter);

  }

  @Override
  protected void onNewIntent(Intent intent) {
    setIntent(intent);
    NavigationManager.dispatchNavigation(this, getIntent());
    super.onNewIntent(intent);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();

    LocalBroadcastManager.getInstance(WshApp.getAppContext()).unregisterReceiver(exitClickReceiver);
  }

  @Override
  protected void onUserLeaveHint() {
    super.onUserLeaveHint();
  }

  @Override
  public void onBackPressed() {
    boolean handled = false;
    final Fragment currentFragment = getFragmentManager().findFragmentById(android.R.id.content);
    if (currentFragment instanceof BaseWshFragment) {
      handled = ((BaseWshFragment) currentFragment).onBackPressed();
    }
    if (!handled) {
      if (getFragmentManager().getBackStackEntryCount() == 1) {
        finish();
      } else {
        super.onBackPressed();
      }
    }
  }

  @Override
  public void finish() {
    try {
      // TODO fix this crash later
      while (getFragmentManager().getBackStackEntryCount() > 0) {
        getFragmentManager().popBackStackImmediate();
      }
    } catch (IllegalStateException e) {
      e.printStackTrace();
    }
    super.finish();
  }

  private void navigateToPlayScene() {
    NavigationManager.navigateTo(this, NavigationManager.PageName.HOME,
        new NavigationParams.Builder().setAddToBackStack(true).setExtra(null)
            .build());
  }
}
