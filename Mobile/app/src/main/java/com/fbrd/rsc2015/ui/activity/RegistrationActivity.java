package com.fbrd.rsc2015.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.loginmodule.interactor.RegistrationInteractor;
import com.example.loginmodule.interactor.impl.RegistrationInteractorImpl;
import com.example.loginmodule.service.app.AppServiceImpl;
import com.fbrd.rsc2015.R;
import com.fbrd.rsc2015.ui.presenter.RegistrationPresenter;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistrationActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.etUsername)
    MaterialEditText etUsername;
    @Bind(R.id.etPassword)
    MaterialEditText etPassword;
    @Bind(R.id.etFirstName)
    MaterialEditText etFirstName;
    @Bind(R.id.etLastName)
    MaterialEditText etLastName;
    @Bind(R.id.etEmail)
    MaterialEditText etEmail;
    private ProgressDialog progressDialog;
    private RegistrationPresenter registrationPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        try{
            getSupportActionBar().setTitle("Register an account");
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }

        AppServiceImpl service = new AppServiceImpl("http://firebreathingrubberduckies.azurewebsites.net/");
        RegistrationInteractor interactor = new RegistrationInteractorImpl(service.getAppService());
        registrationPresenter = new RegistrationPresenter(this,interactor, this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }

    public String getUsername() {
        return etUsername.getText().toString();
    }

    public String getEmail() {
        return etEmail.getText().toString();
    }

    public String getPassword() {
        return etPassword.getText().toString();
    }

    public String getFirstName() {
        return etFirstName.getText().toString();
    }

    public String getLastName() {
        return etLastName.getText().toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @OnClick(R.id.btnRegister)
    public void registerClicked(){
        registrationPresenter.registerUser(etEmail.getText().toString(), etUsername.getText().toString(),
                etFirstName.getText().toString(), etLastName.getText().toString(),
                etPassword.getText().toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        registrationPresenter.onViewResume();
    }

    public void showLoading(String s) {
        progressDialog.setMessage(s);
        progressDialog.show();
    }

    public void dismissLoading() {
        progressDialog.dismiss();
    }

    public void proceed() {
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onPause() {
        registrationPresenter.onViewPause();
        super.onPause();

    }

    //    public Bitmap getImage(){
//    }

}
