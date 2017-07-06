package atef.twitterapitesting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration
                .createDefault(getBaseContext()));
        getSupportFragmentManager().beginTransaction().add(R.id.fl_main_container, new FollowersFragment()).addToBackStack("followers_fragment").commit();
    }
}
