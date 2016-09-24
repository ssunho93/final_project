package com.example.jeon.tabtest;


import android.os.Bundle;
import android.os.Handler;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.jeon.tabtest.R;
import com.example.jeon.tabtest.view.FeedContextMenu;
import com.example.jeon.tabtest.view.FeedContextMenuManager;

import butterknife.Bind;

public class TabFragment3 extends Fragment implements FeedAdapter.OnFeedItemClickListener,
        FeedContextMenu.OnFeedContextMenuItemClickListener{

    public static final String ACTION_SHOW_LOADING_ITEM = "action_show_loading_item";

    private static final int ANIM_DURATION_TOOLBAR = 300;
    private static final int ANIM_DURATION_FAB = 400;

    @Bind(R.id.rvFeed)
    RecyclerView rvFeed;

    private FeedAdapter feedAdapter;
    View rootView;
    private boolean pendingIntroAnimation;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView  = inflater.inflate(R.layout.tab_fragment_3, container, false);
        setupFeed();
        feedAdapter.updateItems(true);
        return rootView;
    }

    private void setupFeed() {
        RecyclerView.LayoutManager mLayoutManager;

//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()) {
//            @Override
//            protected int getExtraLayoutSpace(RecyclerView.State state) {
//                return 300;
//            }
//        };
        rvFeed = (RecyclerView) rootView.findViewById(R.id.rvFeed);
        rvFeed.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        rvFeed.setLayoutManager(mLayoutManager);

        feedAdapter = new FeedAdapter(getActivity().getApplicationContext());
        feedAdapter.setOnFeedItemClickListener(this);
        rvFeed.setAdapter(feedAdapter);
        rvFeed.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                FeedContextMenuManager.getInstance().onScrolled(recyclerView, dx, dy);
            }
        });
        rvFeed.setItemAnimator(new FeedItemAnimator());
    }

    @Override
    public void onReportClick(int feedItem) {

    }

    @Override
    public void onSharePhotoClick(int feedItem) {

    }

    @Override
    public void onCopyShareUrlClick(int feedItem) {

    }

    @Override
    public void onCancelClick(int feedItem) {

    }

    @Override
    public void onCommentsClick(View v, int position) {

    }

    @Override
    public void onMoreClick(View v, int position) {

    }

    @Override
    public void onProfileClick(View v) {

    }


    private void startContentAnimation() {
        feedAdapter.updateItems(true);
    }


    private void showFeedLoadingItemDelayed() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rvFeed.smoothScrollToPosition(0);
                feedAdapter.showLoadingView();
            }
        }, 500);
    }
}