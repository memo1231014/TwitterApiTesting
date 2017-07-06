package atef.twitterapitesting;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.services.concurrency.AsyncTask;
import twitter4j.PagableResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;


public class FollowersFragment extends Fragment {
    ArrayList<FollowerModel> followers = null;
    View rootView;
    @BindView(R.id.lv_followers_followers)
    ListView followersList;
    @BindView(R.id.sl_followers_refresher)
    SwipeRefreshLayout refresher;
    boolean isLoading = false;
    long courser = -1;
    SharedPreferences loginDetails;
    Dialog loadingBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_followers, container, false);
        ButterKnife.bind(this, rootView);
        loginDetails = MUT.getLoginDetails(getActivity());
        followers = new ArrayList<>();
        getFollowers();
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresher.setRefreshing(false);
                getFollowers();
            }
        });
        return rootView;
    }

    public void setFollowersToList() {
        FollowersAdapter followersAdapter = new FollowersAdapter(
                getActivity(), followers);

        followersList.setAdapter(followersAdapter);
        followersList.setOnScrollListener(new PaggingScrollerListner());
        //here i am substracting 20 because the twitter send me only 20 follower per time
        followersList.setSelection(followers.size() - 20);
        followersList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("user_name", followers.get(position).getHandle());
                bundle.putString("profile_image", followers.get(position).getProfileImage());
                bundle.putString("background_image", followers.get(position).getBackgroundImage());
                UserInformationFragment tweets = new UserInformationFragment();
                tweets.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack("user_information").add(R.id.fl_main_container, tweets)
                        .commit();
            }

        });
    }


    public void getFollowers() {

        loadingBar = MUT.appearLoadingBar(getActivity());

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                ConfigurationBuilder configurations = new ConfigurationBuilder();
                configurations.setOAuthAccessToken(loginDetails.getString("oAuthToken", ""))
                        .setOAuthAccessTokenSecret(loginDetails.getString("oAuthSecret", ""))
                        .setOAuthConsumerKey(MUT.TWITTER_KEY)
                        .setOAuthConsumerSecret(MUT.TWITTER_SECRET);
                Twitter mTwitter = new TwitterFactory(configurations.build()).getInstance();
                try {
                    PagableResponseList<User> responseList = mTwitter.getFollowersList(loginDetails.getLong("userID", 0), courser);


                    //I used other arrayList because PagableResponseList prevent me to set data
                    for (int i = 0; i < responseList.size(); i++) {
                        FollowerModel followerModel = new FollowerModel();
                        followerModel.setName(responseList.get(i).getName());
                        followerModel.setHandle(responseList.get(i).getScreenName());
                        followerModel.setBio(responseList.get(i).getDescription());
                        followerModel.setProfileImage(responseList.get(i).getBiggerProfileImageURLHttps());
                        followerModel.setBackgroundImage(responseList.get(i).getProfileBackgroundImageUrlHttps());
                        followers.add(followerModel);
                    }

                    if (followers != null && followers.size() > 0 && courser == -1) {
                            //just save 20 tweets it's better for mobile memory
                            saveDataOffline();
                    }
                    courser = responseList.getNextCursor();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                loadingBar.cancel();
                refresher.setRefreshing(false);
                //if there's no data go to check the offline
                if (followers != null && followers.size() > 0) {
                    setFollowersToList();
                } else {
                    getOffLineData();
                }
            }
        }.execute();
    }


    public void saveDataOffline() {
        SqlLiteHelper sqlLiteHelper = new SqlLiteHelper(getActivity());
        //delete the last tweets to prevent load on mobile memory
        sqlLiteHelper.deleteOrUpdate("delete from followers where 1");
        for (int i = 0; i < followers.size(); i++) {
            ContentValues cv = new ContentValues();
            cv.put("user_name", "" + followers.get(i).getName());
            cv.put("handle", "" + followers.get(i).getHandle());
            cv.put("bio", "" + followers.get(i).getBio());
            cv.put("profile_image", "" + followers.get(i).getProfileImage());
            cv.put("background_image", "" + followers.get(i).getBackgroundImage());
            sqlLiteHelper.insert("followers", cv);
        }
        sqlLiteHelper.closeConnection();
    }

    public void getOffLineData() {
        Cursor c;
        SqlLiteHelper sqlLiteHelper = new SqlLiteHelper(getActivity());
        try {
            c = sqlLiteHelper.select("select * from followers");
            while (c.moveToNext()) {
                FollowerModel followerModel = new FollowerModel();
                followerModel.setName(c.getString(c.getColumnIndex("user_name")));
                followerModel.setHandle(c.getString(c.getColumnIndex("handle")));
                followerModel.setBio(c.getString(c.getColumnIndex("bio")));
                followerModel.setProfileImage(c.getString(c.getColumnIndex("profile_image")));
                followerModel.setBackgroundImage(c.getString(c.getColumnIndex("background_image")));
                followers.add(followerModel);
            }
            if (followers != null && followers.size() > 0) {
                setFollowersToList();
            }
            c.close();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }


    public class PaggingScrollerListner implements AbsListView.OnScrollListener {
        private int visibleThreshold = 1;
        private int previousTotal = 0;
        private boolean loading = true;

        public PaggingScrollerListner() {
        }

        public PaggingScrollerListner(int visibleThreshold) {
            this.visibleThreshold = visibleThreshold;
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;

                }
            }
            if (!loading && (totalItemCount - visibleItemCount) < (firstVisibleItem + visibleThreshold)) {
                getFollowers();
                loading = true;
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }
    }

}
