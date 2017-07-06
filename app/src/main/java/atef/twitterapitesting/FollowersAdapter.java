package atef.twitterapitesting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


@SuppressLint("InflateParams")
public class FollowersAdapter extends BaseAdapter {

    Context context;
    private ArrayList<FollowerModel> followers;
    private LayoutInflater inflater;

    public FollowersAdapter(Context context, ArrayList<FollowerModel> followers) {
        this.followers = followers;
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public int getCount() {
        return followers.size();
    }

    public Object getItem(int position) {
        return followers.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_follower, null);
            holder = new ViewHolder(convertView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(followers.get(position).getName());
        holder.handle.setText(followers.get(position).getHandle());
        holder.bio.setText(followers.get(position).getBio());
        MUT.loadImage(holder.image, followers.get(position).getProfileImage());


        return convertView;
    }

    public class ViewHolder {
        @BindView(R.id.tv_item_follower_name) TextView name;
        @BindView(R.id.tv_item_follower_handle) TextView handle;
        @BindView(R.id.tv_item_follower_bio) TextView bio;
        @BindView(R.id.img_item_follower_center_photo) ImageView image;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
