package com.warship.helper.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.warship.helper.R;
import com.warship.helper.app.WshApp;
import com.warship.helper.manager.navigation.NavigationManager;
import com.warship.helper.ui.WshActivity;

/**
 * @author qinbi@wandoujia.com (Bi Qin)
 */
public class HomeFragment extends BaseWshFragment {

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View contentView = inflater.inflate(R.layout.fragment_home, container, false);
    initViews(contentView);
    return contentView;
  }

  private void initViews(View contentView) {
    View levelBtn = contentView.findViewById(R.id.btn_level);
    levelBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (isAdded()) {
          NavigationManager.navigateTo(getActivity(), NavigationManager.PageName.LEVEL,
              null);
        }
      }
    });
  }
}
