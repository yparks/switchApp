package edu.mills.cs115;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.Html;
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

/**
 * HomeFragment implements dozens of memes images  as a banner.
 * A TextView of word of a day that use random number generator to
 * select a random term form a complete list of terms.
 *
 * @author Ying Parks (yparks@mills.edu)
 */

public class HomeFragment extends BaseFragment {
  //
  private ConvenientBanner banner;
  //banneradapter
  private CBViewHolderCreator creator;
  //Memes picture source for random display
  private int[] lists = new int[] {
      R.drawable.cs1, R.drawable.cs2, R.drawable.cs3, R.drawable.cs4, R.drawable.cs5,
      R.drawable.cs6, R.drawable.cs7, R.drawable.cs8, R.drawable.cs9, R.drawable.cs10,
      R.drawable.cs11, R.drawable.cs12, R.drawable.cs13
  };
  private List<Integer> curLists = new ArrayList<>();
  //Refresh button for refresh content of "word of the day"
  private ImageView refresh;
  private SQLiteDatabase db;
  private TextView title_tv;
  //Add definition for word of "word of the day"

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


    //Repeat onCreateWindow method in WebChromeClient
    mWebSettings.setSupportMultipleWindows(false);
    mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
    refresh.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        setDictionary();
      }
    });
    creator = new CBViewHolderCreator<NetworkImageHolderView>() {
      @Override public NetworkImageHolderView createHolder() {
        return new NetworkImageHolderView();
      }
    };
    //Random generate 4 memes pictures for banner
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
    banner.startTurning(5000);
  }

  @Override public void onStop() {
    super.onStop();
    if (banner.isTurning()) {
      banner.stopTurning();
    }
  }
  //Random generate a word  as word of the day content
  public void setDictionary() {
    DictionaryBean db = queryRandom();
    String colorText = "<h1>Word of the Day:</h1>"
                      + "<font color=\"#FF717E\"><bold>"
                      + db.getTerm()
                      + "</bold></font>";
    title_tv.setText(Html.fromHtml(colorText));

    //Set the font and font-size of the web view text
    String htmlStringStart = "<html><head><style type=\"text/css\">@font-face " +
            "{font-family: MyFont;src: url(\"file:///android_asset/font/BMitra.ttf\")}body" +
            " {font-family: MyFont;font-size: 36px;}</style></head><body>";
    String htmlStringClose = "</body></html>";
    String myHtmlString = htmlStringStart + db.getDefinition() + htmlStringClose;

    webView.loadDataWithBaseURL(null, myHtmlString, "text/html", "utf-8", null);
  }

  /**
   * Memes pictures loading
   */
  public class NetworkImageHolderView implements Holder<Integer> {
    private ImageView imageView;

    @Override public View createView(Context context) {
      //Create pictures ImageView
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
   * Word of the day random generator
   */
  public DictionaryBean queryRandom() {
    DictionaryBean dbean = new DictionaryBean();
    try {
      SQLiteOpenHelper dictionaryDatabaseHelper = new DictionaryDatabaseHelper(getActivity());
      db = dictionaryDatabaseHelper.getReadableDatabase();
      Cursor cursor =
          db.rawQuery("SELECT * FROM " + DictionaryDatabaseHelper.DICTIONARY_TABLE, null);
      Log.v("MainActivity.java", "in doTermSearch() " + cursor.getCount());
      //Random generate a word form database
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