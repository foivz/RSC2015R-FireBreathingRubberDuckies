package com.fbrd.rsc2015.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fbrd.rsc2015.R;
import com.fbrd.rsc2015.app.di.component.DaggerMainComponent;
import com.fbrd.rsc2015.app.di.module.MainModule;
import com.fbrd.rsc2015.domain.model.response.FeedItem;
import com.fbrd.rsc2015.ui.presenter.MainPresenter;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.squareup.picasso.Picasso;

import org.buraktamturk.loadingview.LoadingView;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.nlopez.smartadapters.adapters.RecyclerMultiAdapter;

public class MainActivity extends AppCompatActivity implements Drawer.OnDrawerItemClickListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    ImageView imgAvatar;
    TextView txtUsername;
    @Inject
    Drawer drawer;
    @Inject
    MainPresenter presenter;
    @Inject
    @Named("list_feed")
    RecyclerMultiAdapter adapter;
    @Bind(R.id.listFeed)
    RecyclerView listFeed;
    @Bind(R.id.loading)
    LoadingView loadingView;

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
        listFeed.setLayoutManager(new LinearLayoutManager(this));
        listFeed.setAdapter(adapter);
        adapter.setViewEventListener((i, o, i1, view) -> {

        });
    }

    @Override
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
        switch (position) {
            case 6:
                presenter.logout();
                break;
        }
        return false;
    }


    public void showAvatar(String image) {
        Picasso.with(this).load(image).into(imgAvatar);
    }

    public void showUsername(String username) {
        txtUsername.setText(username);
    }

    public void showFeed(List<FeedItem> items) {
        loadingView.setLoading(false);
        adapter.setItems(items);
    }

    public void showError(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    public void dismissLoading() {
        loadingView.setLoading(false);
    }

    public void showLoading(String message) {
        loadingView.setText(message);
        loadingView.setLoading(true);
    }


    public void showLogin() {
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onViewResume();
    }

    @Override
    protected void onPause() {
        presenter.onViewPause();
        super.onPause();
    }

    private void openUrl(String url){
        /*CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setShowTitle(true);
        CustomTabsIntent intent = builder.build();
        intent.launchUrl(this, Uri.parse(someUrl));*/
    }
}
