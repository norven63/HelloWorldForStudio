package com.myAndroid.helloworld.activity.shareElement;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myAndroid.helloworld.R;
import com.myAndroid.helloworld.activity.recyclerView.BaseRecyclerViewAdapter;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SharedElementActivity extends Activity {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.bottom)
    View bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

                            //android:transitionName="share1"
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SharedElementActivity.this, Pair.create(view, "share1"), Pair.create(bottom, "share2"));

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
