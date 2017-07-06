package atef.twitterapitesting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;


public class LoginActivity extends AppCompatActivity {
    private TwitterLoginButton twitterLoginButton;
    SharedPreferences loginDetails;
    @BindView(R.id.btn_login_change_language)
    Button changeLanguage;
    SharedPreferences.Editor loginDetailsEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(MUT.TWITTER_KEY, MUT.TWITTER_SECRET);
        Fabric.with(this, new com.twitter.sdk.android.Twitter(authConfig));
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        loginDetails = MUT.getLoginDetails(LoginActivity.this);
        loginDetailsEditor = loginDetails.edit();
        setLanguage(loginDetails.getString("language", "en"), false);
        loginToTwitter();

        changeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //here fast if to check if he clicked on button and his language is arabic make it english and the opposite if english make it arabic
                String selectedLanguage = (loginDetails.getString("language", "en").equals("en") ? "ar" : "en");
                loginDetailsEditor.putString("language", selectedLanguage);
                loginDetailsEditor.commit();
                setLanguage(selectedLanguage, true);
            }
        });

    }

    public void loginToTwitter() {


        if (loginDetails.getBoolean("isLogin", false)) {
            startMainActivity();
        } else {
            twitterLoginButton = (TwitterLoginButton) findViewById(R.id.btn_main_twitter_login);
            twitterLoginButton.setCallback(new Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> result) {
                    final TwitterSession session = result.data;
                    TwitterAuthToken authToken = session.getAuthToken();
                    //let's save user details within sharedPreference for fast Login after application killed

                    loginDetailsEditor.putString("oAuthToken", authToken.token);
                    loginDetailsEditor.putString("oAuthSecret", authToken.secret);
                    loginDetailsEditor.putLong("userID", session.getUserId());
                    loginDetailsEditor.putBoolean("isLogin", true);
                    loginDetailsEditor.commit();
                    startMainActivity();

                }

                @Override
                public void failure(TwitterException e) {
                    e.getStackTrace();
                }
            });
        }
    }


    public void startMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void setLanguage(String languageCode, boolean restartActivity) {

        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getApplicationContext().getResources().updateConfiguration(config,
                getApplicationContext().getResources().getDisplayMetrics());
        if (restartActivity) {
            Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }

}
