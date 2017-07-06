package atef.twitterapitesting;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.services.concurrency.AsyncTask;
import it.carlom.stikkyheader.core.StikkyHeaderBuilder;
import it.carlom.stikkyheader.core.animator.AnimatorBuilder;
import it.carlom.stikkyheader.core.animator.BaseStickyHeaderAnimator;
import it.carlom.stikkyheader.core.animator.HeaderStikkyAnimator;
import twitter4j.Paging;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;


public class UserInformationFragment extends Fragment {
    @BindView(R.id.lv_tweets_tweets)
    ListView tweetsList;
    @BindView(R.id.toolbar)
    View toolbar;
    @BindView(R.id.img_tweets_profile_image)
    ImageView profileImage;
    @BindView(R.id.img_tweets_background_image)
    ImageView backgroundImage;
    View rootView;
    java.util.List statuses = null;
    String userName = "";
    Dialog loadingBar;
    private ArrayList<String> tweets;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        BaseStickyHeaderAnimator animator = new HeaderStikkyAnimator() {

            @Override
            public AnimatorBuilder getAnimatorBuilder() {
                View viewToAnimate = getHeader().findViewById(R.id.img_tweets_profile_image);
                final Rect squareSizeToolbar = new Rect(0, 0, toolbar.getHeight(), toolbar.getHeight());
                return AnimatorBuilder.create()
                        .applyScale(viewToAnimate, squareSizeToolbar)
                        .applyTranslation(viewToAnimate, new Point(50, 0))
                        .applyFade(viewToAnimate, 1f);
            }

        };


        StikkyHeaderBuilder.stickTo(tweetsList)
                .setHeader(R.id.header, (ViewGroup) getView())
                .minHeightHeader(250)
                .animator(animator)
                .build();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_user_information, container, false);
        ButterKnife.bind(this, rootView);
        setUserData();
        getUserTweets();
        return rootView;
    }


    public void getUserTweets() {
        final SharedPreferences loginDetails = MUT.getLoginDetails(getActivity());
        loadingBar = MUT.appearLoadingBar(getActivity());
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                ConfigurationBuilder configurations = new ConfigurationBuilder();
                configurations.setOAuthAccessToken(loginDetails.getString("oAuthToken", ""))
                        .setOAuthAccessTokenSecret(loginDetails.getString("oAuthSecret", ""))
                        .setOAuthConsumerKey(MUT.TWITTER_KEY)
                        .setOAuthConsumerSecret(MUT.TWITTER_SECRET);
                Twitter twitter = new TwitterFactory(configurations.build()).getInstance();

                try {
                    statuses = twitter.getUserTimeline(userName, new Paging(1, 10));
                } catch (TwitterException e) {
                    e.getStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                loadingBar.cancel();
                if(statuses!=null&&statuses.size()>0) {
                    tweets = new ArrayList<>();
                    for (int i = 0; i < statuses.size(); i++) {
                        tweets.add(((twitter4j.Status) statuses.get(i)).getText());
                    }
                    setTweetsToList();
                }
            }
        }.execute();
    }


    public void setTweetsToList() {

        TweetsAdapter tweetsAdapter = new TweetsAdapter(
                getActivity(), tweets);
        tweetsList = (ListView) rootView.findViewById(R.id.lv_tweets_tweets);
        tweetsList.setAdapter(tweetsAdapter);

    }

    public void setUserData() {
        userName = getArguments().getString("user_name");
        MUT.loadImage(profileImage, getArguments().getString("profile_image"));
        MUT.loadImage(backgroundImage, getArguments().getString("background_image"));
    }

}
