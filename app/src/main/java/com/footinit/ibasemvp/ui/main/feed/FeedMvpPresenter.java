package com.footinit.ibasemvp.ui.main.feed;

import com.footinit.ibasemvp.di.PerActivity;
import com.footinit.ibasemvp.ui.base.MvpPresenter;

/**
 * Created by Abhijit on 23-11-2017.
 */

@PerActivity
public interface FeedMvpPresenter<V extends FeedMvpView> extends MvpPresenter<V> {
    void onViewPrepared();
}
