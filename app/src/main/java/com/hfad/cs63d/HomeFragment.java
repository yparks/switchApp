package com.hfad.cs63d;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment {
  //
  private ConvenientBanner banner;
  //banneradapter
  private CBViewHolderCreator creator;
  //
  private int[] lists = new int[] {
      R.drawable.cs1, R.drawable.cs2, R.drawable.cs3, R.drawable.cs4, R.drawable.cs5,
      R.drawable.cs6, R.drawable.cs7, R.drawable.cs8, R.drawable.cs9, R.drawable.cs10,
      R.drawable.cs11, R.drawable.cs12, R.drawable.cs13
  };
  private List<Integer> curLists = new ArrayList<>();
  //
  private ImageView refresh;
  private SQLiteDatabase db;
  private TextView title_tv;
  //
  private WebView webView;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override public View initView() {
    View view = View.inflate(mContext, R.layout.fragment_home, null);
    banner = (ConvenientBanner) view.findViewById(R.id.convenientBanner);
    title_tv = (TextView) view.findViewById(R.id.title_tv);
    refresh = (ImageView) view.findViewById(R.id.refresh_iv);
    webView = (WebView) view.findViewById(R.id.webview);
    WebSettings mWebSettings = webView.getSettings();
    mWebSettings.setSupportZoom(true);
    mWebSettings.setLoadWithOverviewMode(true);
    mWebSettings.setUseWideViewPort(true);
    mWebSettings.setDefaultTextEncodingName("utf-8");
    mWebSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);//。
    mWebSettings.setJavaScriptEnabled(true);
    mWebSettings.setSupportMultipleWindows(true);

    //
    //
    mWebSettings.setSupportMultipleWindows(false);
    mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
    refresh.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        //
        setDictionary();
      }
    });
    creator = new CBViewHolderCreator<NetworkImageHolderView>() {
      @Override public NetworkImageHolderView createHolder() {
        return new NetworkImageHolderView();
      }
    };
    //
    for (int i = 0; i < lists.length; i++) {
      int random = (int) (Math.random() * lists.length);
      if (curLists.size() < 4) {
        if (!curLists.contains(lists[random])) {
          curLists.add(lists[random]);
        }
      }
    }
    banner.setPages(creator, curLists).setOnItemClickListener(new OnItemClickListener() {
      @Override public void onItemClick(int position) {

      }
    }).setPageIndicator(new int[] { R.drawable.banner_unselect, R.drawable.banner_select });

    return view;
  }

  @Override public void onStart() {
    super.onStart();
    setDictionary();
    banner.startTurning(2000);
  }

  @Override public void onStop() {
    super.onStop();
    if (banner.isTurning()) {
      banner.stopTurning();
    }
  }

  public void setDictionary() {
    //
    DictionaryBean db = queryRandom();
    title_tv.setText("Word of a day ： " + db.getTerm());
    webView.loadDataWithBaseURL(null, db.getDefinition(), "text/html", "utf-8", null);
  }

  /**
   *
   */
  public class NetworkImageHolderView implements Holder<Integer> {
    private ImageView imageView;

    @Override public View createView(Context context) {
      //
      imageView = new ImageView(context);
      imageView.setScaleType(ImageView.ScaleType.FIT_XY);
      imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
          ViewGroup.LayoutParams.MATCH_PARENT));
      return imageView;
    }

    @Override public void UpdateUI(Context context, int position, Integer id) {
      imageView.setBackgroundResource(id);
    }
  }

  /**
   *
   */
  public DictionaryBean queryRandom() {
    DictionaryBean dbean = new DictionaryBean();
    try {
      SQLiteOpenHelper dictionaryDatabaseHelper = new DictionaryDatabaseHelper(getActivity());
      db = dictionaryDatabaseHelper.getReadableDatabase();
      Cursor cursor =
          db.rawQuery("SELECT * FROM " + DictionaryDatabaseHelper.DICTIONARY_TABLE, null);
      Log.v("MainActivity.java", "in doTermSearch() " + cursor.getCount());
      //
      int random = (int) (Math.random() * cursor.getCount());
      if (cursor.moveToPosition(random)) {
        String term = cursor.getString(1);
        String definition = cursor.getString(2);
        dbean.setTerm(term);
        dbean.setDefinition(definition);
      }
      cursor.close();
    } catch (SQLiteException e) {
      Toast toast = Toast.makeText(getActivity(), "Database unavailable", Toast.LENGTH_SHORT);
      toast.show();
    } finally {
      db.close();
    }
    return dbean;
  }
}//Test