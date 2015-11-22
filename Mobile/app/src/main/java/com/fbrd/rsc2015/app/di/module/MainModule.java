package com.fbrd.rsc2015.app.di.module;

import android.support.v7.widget.Toolbar;

import com.fbrd.rsc2015.R;
import com.fbrd.rsc2015.domain.interactor.NotificationInteractor;
import com.fbrd.rsc2015.domain.model.response.FeedItem;
import com.fbrd.rsc2015.domain.repository.RSCPreferences;
import com.fbrd.rsc2015.domain.repository.RSCRepository;
import com.fbrd.rsc2015.ui.activity.MainActivity;
import com.fbrd.rsc2015.ui.presenter.MainPresenter;
import com.fbrd.rsc2015.ui.view.FeedItemView;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.SimpleLineIconsIcons;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.nlopez.smartadapters.SmartAdapter;
import io.nlopez.smartadapters.adapters.RecyclerMultiAdapter;

/**
 * Created by david on 21.11.2015..
 */
@Module(includes = {ApiModule.class})
public class MainModule {

    private MainActivity view;
    private Drawer.OnDrawerItemClickListener onDrawerItemClickListener;
    private Toolbar toolbar;

    public MainModule(MainActivity view, Drawer.OnDrawerItemClickListener onDrawerItemClickListener, Toolbar toolbar) {
        this.view = view;
        this.onDrawerItemClickListener = onDrawerItemClickListener;
        this.toolbar = toolbar;
    }

    @Provides
    MainActivity mainActivity() {
        return view;
    }

    @Provides
    MainPresenter mainPresenter(MainActivity activity, RSCPreferences preferences, NotificationInteractor interactor) {
        return new MainPresenter(activity, preferences, interactor);
    }

    @Provides
    NotificationInteractor interactor(RSCRepository.Api service) {
        return new NotificationInteractor(service);
    }

    @Provides
    Drawer mainDrawer() {
        return new DrawerBuilder()
                .withActivity(view)
                .withToolbar(toolbar)
                .withTranslucentNavigationBar(false)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .withHeader(R.layout.nav_header_main)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("What's new").withIcon(new IconDrawable(view, SimpleLineIconsIcons.icon_feed).actionBarSize()),
//                        new PrimaryDrawerItem().withName("My team"),
                        new PrimaryDrawerItem().withName("Game").withIcon(new IconDrawable(view, SimpleLineIconsIcons.icon_game_controller).actionBarSize()),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Settings").withIcon(new IconDrawable(view, SimpleLineIconsIcons.icon_settings).actionBarSize()),
                        new PrimaryDrawerItem().withName("Logout").withIcon(new IconDrawable(view, SimpleLineIconsIcons.icon_logout).actionBarSize())
                ).withOnDrawerItemClickListener(onDrawerItemClickListener)
                .build();
    }

    @Provides
    @Named("list_feed")
    RecyclerMultiAdapter feedAdapter() {
        return SmartAdapter.empty().map(FeedItem.class, FeedItemView.class).recyclerAdapter();
    }
}
