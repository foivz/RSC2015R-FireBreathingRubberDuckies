package com.fbrd.rsc2015.ui.activity;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fbrd.rsc2015.R;
import com.fbrd.rsc2015.app.di.component.DaggerMainComponent;
import com.fbrd.rsc2015.app.di.module.MainModule;
import com.fbrd.rsc2015.domain.repository.RSCPreferences;
import com.fbrd.rsc2015.ui.presenter.MainPresenter;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements Drawer.OnDrawerItemClickListener, AccountHeader.OnAccountHeaderListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    ImageView imgAvatar;
    TextView txtUsername;
    @Inject
    Drawer drawer;
    @Inject
    MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        DaggerMainComponent.builder().mainModule(new MainModule(this, this, toolbar)).build().inject(this);
        imgAvatar = (ImageView) drawer.getHeader().findViewById(R.id.imageView);
        txtUsername = (TextView) drawer.getHeader().findViewById(R.id.txtUserName);
        presenter.onViewCreate();
    }

    @Override
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
        return false;
    }

    @Override
    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
        return false;
    }

    public void showAvatar(String image) {
        Picasso.with(this).load("https://avatars3.githubusercontent.com/u/1476232").into(imgAvatar);
    }

    public void showUsername(String username) {
        txtUsername.setText(username);
    }
}
