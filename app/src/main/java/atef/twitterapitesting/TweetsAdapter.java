package atef.twitterapitesting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


@SuppressLint("InflateParams")
public class TweetsAdapter extends BaseAdapter {

    Context context;
    private ArrayList<String> tweets;
    private LayoutInflater inflater;

    public TweetsAdapter(Context context, ArrayList<String> tweets) {
        this.tweets = tweets;
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public int getCount() {
        return tweets.size();
    }

    public Object getItem(int position) {
        return tweets.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_tweet, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tweet.setText(tweets.get(position) + "");

        return convertView;
    }

    public class ViewHolder {
        @BindView(R.id.tv_item_tweet_tweet) TextView tweet;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
