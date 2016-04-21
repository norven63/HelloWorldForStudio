package com.myAndroid.helloworld.activity.shareElement;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.myAndroid.helloworld.R;
import com.myAndroid.helloworld.activity.recyclerView.BaseRecyclerViewAdapter;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SharedElementActivity extends Activity {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

//        getWindow().setEnterTransition(new Explode());
//        getWindow().setExitTransition(new Explode());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setAllowEnterTransitionOverlap(true);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_element);
        ButterKnife.bind(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new BaseRecyclerViewAdapter<String, ShareViewHolder>(Arrays.asList(new String[]{"item-1", "item-2", "item-3", "item-4", "item-5", "item-6"})) {
            @Override
            public ShareViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
                View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_view, viewGroup, false);
                return new ShareViewHolder(itemView);
            }

            @Override
            public void onBindViewHolder(final ShareViewHolder holder, final int position) {
                holder.textView.setText(getItem(position));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Intent intent = new Intent(SharedElementActivity.this, SharedElementActivity2.class);
                            intent.putExtra("item", getItem(position));

                            //android:transitionName="robot"
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SharedElementActivity.this, view, "share");

                            startActivity(intent, options.toBundle());
                        }
                    }
                });
            }
        });
    }

    class ShareViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ShareViewHolder(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.textViewRecyclerItem);
        }
    }
}
