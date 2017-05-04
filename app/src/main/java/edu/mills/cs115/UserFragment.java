package edu.mills.cs115;

import android.view.View;

import edu.mills.cs115.BaseFragment;

/**
 *
 */

public class UserFragment extends BaseFragment {
  @Override public View initView() {
    View view = View.inflate(mContext, R.layout.fragment_user, null);

    return view;
  }
}
