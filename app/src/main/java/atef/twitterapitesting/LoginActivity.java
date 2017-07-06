package atef.twitterapitesting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import io.fabric.sdk.android.Fabric;


public class LoginActivity extends AppCompatActivity {
    private TwitterLoginButton twitterLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(MUT.TWITTER_KEY, MUT.TWITTER_SECRET);
        Fabric.with(this, new com.twitter.sdk.android.Twitter(authConfig));
        setContentView(R.layout.activity_login);
        loginToTwitter();

    }

    public void loginToTwitter() {
        final SharedPreferences loginDetails = MUT.getLoginDetails(LoginActivity.this);

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
                    SharedPreferences.Editor loginDetailsEditor = loginDetails.edit();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }


    public void startMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}
