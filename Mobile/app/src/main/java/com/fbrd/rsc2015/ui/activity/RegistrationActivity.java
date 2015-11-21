package com.fbrd.rsc2015.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.fbrd.rsc2015.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RegistrationActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.imgAvatar)
    ImageView imgAvatar;
    @Bind(R.id.etUsername)
    MaterialEditText etUsername;
    @Bind(R.id.etPassword)
    MaterialEditText etPassword;
    @Bind(R.id.etPasswordConfirm)
    MaterialEditText etPasswordConfirm;
    @Bind(R.id.etFirstName)
    MaterialEditText etFirstName;
    @Bind(R.id.etLastName)
    MaterialEditText etLastName;
    @Bind(R.id.etEmail)
    MaterialEditText etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
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

    public String getPasswordConfirm() {
        return etPasswordConfirm.getText().toString();
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

//    public Bitmap getImage(){
//    }

}
