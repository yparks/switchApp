package edu.mills.cs115;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * BaseFragment extension from fragment
 * @author Ying Parks
 */
public abstract class BaseFragment extends Fragment {

  protected Context mContext;

  /**
   * Created class by Callback
   */
  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mContext = getActivity();
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return initView();
  }

  /**
   * Abstract class is declared, may not be instantiated, and requires a  subclass to provide implementation
   * for abstract methods.
   */
  public abstract View initView();

  /**
   * Created Activity by  Callback this method
   */
  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    initData();
  }

  /**
   * When subclass needs to connect with internet, rewrite this method to connect with internet
   */
  public void initData() {

  }
}
