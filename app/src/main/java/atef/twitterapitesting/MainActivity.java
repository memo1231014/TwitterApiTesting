package atef.twitterapitesting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.btn_main_logout)
    Button logOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration
                .createDefault(getBaseContext()));
        getSupportFragmentManager().beginTransaction().add(R.id.fl_main_container, new FollowersFragment()).addToBackStack("followers_fragment").commit();

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences loginDetails = MUT.getLoginDetails(MainActivity.this);
                SharedPreferences.Editor loginDetailsEditor = loginDetails.edit();
                loginDetailsEditor.putString("oAuthToken", "");
                loginDetailsEditor.putString("oAuthSecret", "");
                loginDetailsEditor.putLong("userID", 0);
                loginDetailsEditor.putBoolean("isLogin", false);
                loginDetailsEditor.commit();
                loginDetailsEditor.clear();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
            return;
        }
        super.onBackPressed();
    }


}
