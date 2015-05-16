package com.warship.helper.manager.navigation;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author qinbi@wandoujia.com (Bi Qin)
 */
public class NavigationParams implements Parcelable {

  private Bundle extra;
  private boolean addToBackStack = true;
  private NavigationMode mode = NavigationMode.STANDARD;
  private int enter;
  private int exit;
  private int popEnter;
  private int popExit;

  public static final Creator<NavigationParams> CREATOR =
      new Creator<NavigationParams>() {
        public NavigationParams createFromParcel(Parcel in) {
          return new NavigationParams(in);
        }

        public NavigationParams[] newArray(int size) {
          return new NavigationParams[size];
        }
      };

  private NavigationParams(Parcel in) {
    extra = in.readBundle();
    boolean[] booleans = new boolean[1];
    in.readBooleanArray(booleans);
    addToBackStack = booleans[0];
    mode = NavigationMode.valueOf(in.readString());
    enter = in.readInt();
    exit = in.readInt();
    popEnter = in.readInt();
    popExit = in.readInt();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeBundle(extra);
    dest.writeBooleanArray(new boolean[] {addToBackStack});
    dest.writeString(mode.name());
    dest.writeInt(enter);
    dest.writeInt(exit);
    dest.writeInt(popEnter);
    dest.writeInt(popExit);
  }

  private NavigationParams(Builder builder) {
    extra = builder.extra;
    addToBackStack = builder.addToBackStack;
    mode = builder.mode == null ? NavigationMode.STANDARD : builder.mode;
    enter = builder.enter;
    exit = builder.exit;
    popEnter = builder.popEnter;
    popExit = builder.popExit;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public Bundle getExtra() {
    return extra;
  }

  public boolean needAddToBackStack() {
    return addToBackStack;
  }

  public NavigationMode getMode() {
    return mode;
  }

  public int getEnter() {
    return enter;
  }

  public int getExit() {
    return exit;
  }

  public int getPopEnter() {
    return popEnter;
  }

  public int getPopExit() {
    return popExit;
  }


  public static class Builder {
    private Bundle extra;
    private boolean addToBackStack = true;
    private NavigationMode mode;
    private int enter;
    private int exit;
    private int popEnter;
    private int popExit;

    public Builder setCustomAnimator(int enter, int exit) {
      this.enter = enter;
      this.exit = exit;
      return this;
    }

    public Builder setCustomAnimator(int enter, int exit, int popEnter, int popExit) {
      this.enter = enter;
      this.exit = exit;
      this.popEnter = popEnter;
      this.popExit = popExit;
      return this;
    }

    public Builder setAddToBackStack(boolean addToBackStack) {
      this.addToBackStack = addToBackStack;
      return this;
    }

    public Builder setMode(NavigationMode mode) {
      this.mode = mode;
      return this;
    }

    public Builder setExtra(Bundle extra) {
      this.extra = extra;
      return this;
    }

    public NavigationParams build() {
      return new NavigationParams(this);
    }
  }
}
