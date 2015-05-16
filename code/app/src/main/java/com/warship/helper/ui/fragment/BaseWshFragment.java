package com.warship.helper.ui.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

/**
 * @author qinbi@wandoujia.com (Bi Qin)
 */
public class BaseWshFragment extends Fragment {

  private boolean hasBeenHidden = false;

  /**
   * @return whether this key event is handled.
   */
  public boolean onBackPressed() {
    return false;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    onPageShow();
  }

  @Override
  public void onHiddenChanged(boolean hidden) {
    super.onHiddenChanged(hidden);
    final View view = getView();
    if (view == null) {
      return;
    }
    if (!hidden && hasBeenHidden) {
      onPageShow();
    }
    hasBeenHidden = hidden;
  }

  /**
   * callback for getting new argument after navigation to active fragment with mode
   */
  public void onNewArguments(Bundle mArgument) {}

  /**
   * This method is called when {@link #onActivityCreated(android.os.Bundle)}, or
   * {@link #onHiddenChanged(boolean)} from hidden to unhidden.
   */
  public void onPageShow() {}
}
