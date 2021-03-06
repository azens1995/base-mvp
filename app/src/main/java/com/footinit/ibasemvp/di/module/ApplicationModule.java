package com.footinit.ibasemvp.di.module;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.facebook.CallbackManager;
import com.footinit.ibasemvp.R;
import com.footinit.ibasemvp.data.AppDataManager;
import com.footinit.ibasemvp.data.DataManager;
import com.footinit.ibasemvp.data.db.AppDatabase;
import com.footinit.ibasemvp.data.db.AppDbHelper;
import com.footinit.ibasemvp.data.db.DbHelper;
import com.footinit.ibasemvp.data.network.ApiCall;
import com.footinit.ibasemvp.data.network.ApiHelper;
import com.footinit.ibasemvp.data.network.AppApiHelper;
import com.footinit.ibasemvp.data.pref.AppPreferenceHelper;
import com.footinit.ibasemvp.data.pref.PreferenceHelper;
import com.footinit.ibasemvp.di.ApplicationContext;
import com.footinit.ibasemvp.di.DatabaseInfo;
import com.footinit.ibasemvp.di.PreferenceInfo;
import com.footinit.ibasemvp.utils.AppConstants;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Abhijit on 08-11-2017.
 */

@Module
public class ApplicationModule {

    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @ApplicationContext
    Context providesContext() {
        return application;
    }

    @Provides
    Application providesApplication() {
        return application;
    }

    @Provides
    @DatabaseInfo
    String providesDatabaseName() {
        return AppConstants.DB_NAME;
    }

    @Provides
    @DatabaseInfo
    Integer providesDatabaseVersion() {
        return AppConstants.DB_VERSION;
    }

    @Provides
    @PreferenceInfo
    String providesSharedPrefName() {
        return AppConstants.PREF_NAME;
    }

    @Provides
    @Singleton
    DataManager providesDataManager(AppDataManager appDataManager) {
        return appDataManager;
    }

    @Provides
    @Singleton
    DbHelper providesDbHelper(AppDbHelper appDbHelper) {
        return appDbHelper;
    }

    @Provides
    @Singleton
    ApiHelper providesApiHelper(AppApiHelper appApiHelper) {
        return appApiHelper;
    }

    @Provides
    @Singleton
    PreferenceHelper providesPreferenceHelper(AppPreferenceHelper appPreferenceHelper) {
        return appPreferenceHelper;
    }

    @Provides
    @Singleton
    ApiCall providesApiCall() {
        return ApiCall.Factory.create();
    }

    @Provides
    @Singleton
    AppDatabase providesAppDatabase(@ApplicationContext Context context,
                                    @DatabaseInfo String dbName) {
        return Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,
                dbName).build();
    }

    @Provides
    @Singleton
    CalligraphyConfig providesCalligraphyConfig() {
        return new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/source-sans-pro/SourceSansPro-Light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build();
    }

    @Provides
    @Singleton
    GoogleSignInOptions providesGoogleSignInOptions() {
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
    }

    @Provides
    @Singleton
    GoogleSignInClient providesGoogleSignInClient(GoogleSignInOptions googleSignInOptions,
                                                  @ApplicationContext Context context) {
        return GoogleSignIn.getClient(context, googleSignInOptions);
    }

    @Provides
    @Singleton
    CallbackManager providesCallbackManager() {
        return CallbackManager.Factory.create();
    }
}
