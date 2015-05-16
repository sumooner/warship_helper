package com.warship.helper.manager.navigation;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.warship.helper.BuildConfig;
import com.warship.helper.app.WshApp;
import com.warship.helper.ui.fragment.BaseWshFragment;
import com.warship.helper.ui.fragment.HomeFragment;
import com.warship.helper.ui.fragment.LevelFragment;
import com.warship.helper.util.CollectionUtils;
import com.warship.helper.util.Const;

/**
 * Handle all the "go to xxx" command.
 *
 * @author qinbi@wandoujia.com (Bi Qin)
 */
public class NavigationManager {
  private static final String EXTRA = "navigation_extra";

  public static final String SCHEMA_WSH = "wsh";
  public static final String HOST_WSH = "ws_helper";

  private NavigationManager() {}

  public static void navigateTo(Context context, PageName pageName, NavigationParams params) {
    if (context instanceof Activity) {
      doNavigation((Activity) context, pageName, params);
    } else {
      Intent intent = buildNavigationIntent(context, pageName, params);
      context.startActivity(intent);
    }
  }

  public static Intent buildNavigationIntent(Context context, PageName pageName,
      NavigationParams params) {
    Uri uri = buildUri(params, HOST_WSH, pageName.name());
    return createNavigateIntent(context, uri, params);
  }

  public static boolean dispatchNavigation(Activity activity, Intent intent) {
    Uri uri = intent.getData();
    if (uri != null && !CollectionUtils.isEmpty(uri.getPathSegments())) {
      String pageNameStr = uri.getPathSegments().get(0);
      PageName pageName = asPageName(pageNameStr);
      if (pageName != null) {
        NavigationParams params = intent.getParcelableExtra(NavigationManager.EXTRA);
        if (params == null) {
          final NavigationParams.Builder builder = NavigationParams.newBuilder();
          final String add2BackStack =
              uri.getQueryParameter(Const.Navigation.KEY_ADD_TO_BACK_STACK);
          if (!TextUtils.isEmpty(add2BackStack)) {
            builder.setAddToBackStack(Boolean.valueOf(add2BackStack));
          }
          final String navMode = uri.getQueryParameter(Const.Navigation.KEY_NAV_MODE);
          if (!TextUtils.isEmpty(navMode)) {
            try {
              builder.setMode(NavigationMode.valueOf(navMode));
            } catch (IllegalArgumentException e) {
              e.printStackTrace();
            }
          }
          params = builder.build();
        }
        doNavigation(activity, pageName, params);
        clearNeedNavigation(intent);
        return true;
      }
    }
    clearNeedNavigation(intent);
    return false;
  }

  private static void doNavigation(Activity activity, PageName pageName, NavigationParams params) {
    FragmentManager fragmentManager = activity.getFragmentManager();
    if (params == null) {
      // a default param
      params = NavigationParams.newBuilder().build();
    }
    if (pageName == PageName.DIALOG) {
      try {
        Fragment fragment = generateFragment(pageName, params);
        ((DialogFragment) fragment).show(activity.getFragmentManager(), null);
      } catch (IllegalStateException e) {
        e.printStackTrace();
      }
      return;
    }
    switch (params.getMode()) {
      case CLEAR_TOP:
        int backStackEntryCount = fragmentManager.getBackStackEntryCount();
        boolean found = false;
        for (int i = 0; i < backStackEntryCount; i++) {
          if (found) {
            fragmentManager.popBackStack();
          } else {
            FragmentManager.BackStackEntry entry = fragmentManager.getBackStackEntryAt(i);
            if (TextUtils.equals(pageName.name(), entry.getName())) {
              found = true;
            }
          }
        }
        if (found) {
          Fragment curFragment = fragmentManager.findFragmentById(android.R.id.content);
          if (curFragment instanceof BaseWshFragment) {
            ((BaseWshFragment) curFragment).onNewArguments(params.getExtra());
          }
          break;
        }
      case STANDARD:
      default:
        // if we use FragmentTransaction.replace(), enter animator will behind exit animator.
        Fragment fragment = generateFragment(pageName, params);
        Fragment curFragment = fragmentManager.findFragmentById(android.R.id.content);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(params.getEnter(), params.getExit(), params.getPopEnter(),
            params.getPopExit());
        if (curFragment != null) {
          transaction.hide(curFragment);
        }
        transaction.add(android.R.id.content, fragment);
        if (params.needAddToBackStack()) {
          transaction.addToBackStack(pageName.name());
        }
        transaction.commitAllowingStateLoss();
    }
  }

  private static Fragment generateFragment(PageName pageName, NavigationParams params) {
    Fragment fragment = getFragmentByName(pageName);
    if (fragment == null) {
      if (BuildConfig.DEBUG) {
        throw new IllegalArgumentException("no such fragment for name: " + pageName.name());
      }
      fragment = getFragmentByName(PageName.HOME);
    }
    fragment.setArguments(params.getExtra());
    return fragment;
  }

  private static void clearNeedNavigation(Intent intent) {
    intent.setData(null);
  }

  private static Fragment getFragmentByName(PageName name) {
    Fragment fragment = null;
    // TODO add more fragment here.
    switch (name) {
      case HOME:
        fragment = new HomeFragment();
        break;
      case LEVEL:
        fragment = new LevelFragment();
        break;
      default:
    }
    return fragment;
  }

  private static Intent createNavigateIntent(Context context, Uri data, NavigationParams params) {
    Intent intent = new Intent(Intent.ACTION_VIEW);
    if (!(context instanceof Activity)) {
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }
    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    if (data != null) {
      intent.setData(data);
    }
    if (params != null) {
      intent.putExtra(EXTRA, params);
    }
    intent.setPackage(WshApp.getAppContext().getPackageName());
    return intent;
  }

  private static Uri buildUri(NavigationParams params, String host, String... paths) {
    Uri.Builder builder = new Uri.Builder();
    builder.scheme(SCHEMA_WSH);
    builder.authority(host);
    if (paths != null) {
      for (String path : paths) {
        if (!TextUtils.isEmpty(path)) {
          builder.appendPath(path);
        }
      }
    }
    builder.appendQueryParameter(Const.Navigation.KEY_ADD_TO_BACK_STACK,
        String.valueOf(params.needAddToBackStack()));
    if (params.getMode() != null) {
      builder.appendQueryParameter(Const.Navigation.KEY_NAV_MODE, params.getMode().name());
    }
    return builder.build();
  }

  public static PageName asPageName(String str) {
    for (PageName pageName : PageName.values()) {
      if (pageName.name().equalsIgnoreCase(str))
        return pageName;
    }
    return null;
  }

  public enum PageName {
    HOME, LEVEL, LEVEL_DETAIL, DIALOG
  }

}
