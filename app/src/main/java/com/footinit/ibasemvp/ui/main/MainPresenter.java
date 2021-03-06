package com.footinit.ibasemvp.ui.main;

import com.facebook.login.LoginManager;
import com.footinit.ibasemvp.R;
import com.footinit.ibasemvp.data.DataManager;
import com.footinit.ibasemvp.ui.base.BasePresenter;
import com.footinit.ibasemvp.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

/**
 * Created by Abhijit on 16-11-2017.
 */

public class MainPresenter<V extends MainMvpView> extends BasePresenter<V>
        implements MainMvpPresenter<V> {


    @Inject
    public MainPresenter(SchedulerProvider schedulerProvider,
                         CompositeDisposable compositeDisposable,
                         DataManager dataManager) {
        super(schedulerProvider, compositeDisposable, dataManager);
    }

    @Override
    public void onNavMenuCreated() {
        if (!isViewAttached())
            return;

        String stringHolder;

        stringHolder = getDataManager().getCurrentUserName();
        if (stringHolder != null && !stringHolder.isEmpty())
            getMvpView().updateUserName(stringHolder);

        stringHolder = getDataManager().getCurrentUserEmail();
        if (stringHolder != null && !stringHolder.isEmpty())
            getMvpView().updateUserEmail(stringHolder);
    }

    @Override
    public void onDrawerOptionFeedClicked() {
        getMvpView().closeNavigationDrawer();
        checkFeedAvailableInDb();
    }

    private void checkFeedAvailableInDb() {
        getMvpView().showLoading();

        getCompositeDisposable().add(
                Observable.zip(
                        getDataManager().getBlogRecordCount(),
                        getDataManager().getOpenSourceRecordCount(),
                        (aLong, aLong2) -> aLong > 0 || aLong2 > 0)
                        .subscribeOn(getSchedulerProvider().io())
                        .observeOn(getSchedulerProvider().ui())
                        .subscribe(aBoolean -> {
                            if (!isViewAttached())
                                return;

                            if (aBoolean) {
                                getMvpView().hideLoading();
                                getMvpView().openFeedActivity();
                            } else {
                                getMvpView().hideLoading();
                                if (getMvpView().isNetworkConnected())
                                    getMvpView().onError(R.string.something_went_wrong);
                            }
                        }, throwable -> {
                            getMvpView().hideLoading();
                            if (getMvpView().isNetworkConnected())
                                getMvpView().onError(R.string.something_went_wrong);
                        })
        );
    }

    @Override
    public void onDrawerOptionLogoutClicked() {
        getMvpView().showLoading();

    /*
    * Logout from the Facebook's LoginManager Instance
    * */
        LoginManager.getInstance().logOut();


    /*
    * Clearing Shared Preferences
    * */
        getDataManager().setCurrentUserLoggedOut();

    /*
    * Clearing/Wiping all data from the User Table
    * And if successful, logs User out
    * */

        getDataManager().wipeUserData()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        getMvpView().hideLoading();
                        getMvpView().showMessage(R.string.logging_you_out);
                        getMvpView().openLoginActivity();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideLoading();
                        getMvpView().showMessage(R.string.there_was_an_error_logout);
                    }
                });
    }

    @Override
    public void onViewInitialized() {

    }
}
