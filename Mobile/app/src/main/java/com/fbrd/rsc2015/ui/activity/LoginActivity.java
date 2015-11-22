package com.fbrd.rsc2015.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.example.loginmodule.util.GoogleApiUtil;
import com.facebook.CallbackManager;
import com.fbrd.rsc2015.R;
import com.fbrd.rsc2015.app.di.component.DaggerLoginComponent;
import com.fbrd.rsc2015.app.di.module.LoginModule;
import com.fbrd.rsc2015.ui.presenter.LoginPresenter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.rengwuxian.materialedittext.MaterialEditText;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.etUsername)
    MaterialEditText etUsername;

    @Bind(R.id.etPassword)
    MaterialEditText etPassword;

    ProgressDialog progressDialog;

    @Inject
    LoginPresenter loginPresenter;

    @Inject
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        DaggerLoginComponent.builder().loginModule(new LoginModule(this, this)).build().inject(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        /*
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm:ss");
        DateTime dt = formatter.parseDateTime("22.11.2015 4:00:00");

        DateTimeHelper dateTimeHelper = new DateTimeHelper(dt, dt, 120);
        Log.e("TIME", "" + dateTimeHelper.calculateElapsedTime());
*/

    }

    @OnClick(R.id.btnSignIn)
    protected void onBtnSignInClick() {
        loginPresenter.attemptSignIn();
    }

    @OnClick(R.id.btnSignInFacebook)
    protected void onBtnSignInFacebookClick() {
        loginPresenter.attemptSignInFacebook();
    }

    @OnClick(R.id.btnSignInGoogle)
    protected void onBtnSignInGoogleClick() {
        loginPresenter.attemptSignInGoogle();
    }

    @OnClick(R.id.fab)
    protected void onBtnRegisterClick() {
        startActivity(new Intent(this, RegistrationActivity.class));
    }

    public String getUsername() {
        return etUsername.getText().toString();
    }

    public String getPassword() {
        return etPassword.getText().toString();
    }

    public void dismissLoading() {
        progressDialog.dismiss();
    }

    public void proceed() {
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    public void showError(String s) {
        Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
    }

    public void showLoading(String s) {
        progressDialog.setMessage(s);
        progressDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        String token = GoogleApiUtil.googleSignInToken(requestCode, resultCode, data);
        Log.i("DAM", "Token: " + token);
        loginPresenter.completeSignInGoogle(token);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loginPresenter.onViewResume();
    }

    @Override
    protected void onPause() {
        loginPresenter.onViewPause();
        super.onPause();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(LoginActivity.this, "Can't connect to Google", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.open_comms)
    public void onCommsClicked() {
        startActivity(new Intent(this, VoiceChatActivity.class));
    }
}
