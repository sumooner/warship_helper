package com.warship.helper.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.warship.helper.R;

/**
 * @author qinbi@wandoujia.com (Bi Qin)
 */
public class LevelFragment extends BaseWshFragment {

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View contentView = inflater.inflate(R.layout.fragment_level, container, false);
    return contentView;
  }
}
