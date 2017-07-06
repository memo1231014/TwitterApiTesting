package atef.twitterapitesting;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;


/**
 * MUT mean Most Used Tools it will contains the data that will be used at many different position within the app
 */

public class MUT {
    private static DisplayImageOptions options = new DisplayImageOptions.Builder().showImageForEmptyUri(Color.TRANSPARENT).showImageOnFail(Color.TRANSPARENT).cacheInMemory(true).cacheOnDisk(true).build();
    public static final String TWITTER_KEY = "jHwNHgjrLFkx9fkyFgIpMDSOt";
    public static final String TWITTER_SECRET = "icFQ0RvaxOmXKeUhRkxZO8Yi3hSenATrAzYBKJnIua1GKAExeB";


    public static SharedPreferences getLoginDetails(Context context) {
        return context.getSharedPreferences("loginDetails", context.MODE_PRIVATE);
    }


    public static void loadImage(ImageView image, String imagePath) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(imagePath, image, options);
    }

    public static Dialog appearLoadingBar(Context context) {
        Dialog loadingBar = new Dialog(context);
        loadingBar.setCancelable(false);
        loadingBar.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingBar.setContentView(R.layout.loading_bar);
        loadingBar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loadingBar.show();
        return loadingBar;
    }


}
