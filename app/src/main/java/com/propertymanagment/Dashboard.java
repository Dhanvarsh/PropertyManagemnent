package com.propertymanagment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.ganfra.materialspinner.MaterialSpinner;

public class Dashboard extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    Context context;
    String[] str_work = {"Plumbing", "Painting", "Electrical Work", "Carpentry", "Flat Rental"};
    SharedPreferences sharedPreferences;
    String str_description, str_work_requirement;

    @BindView(R.id.drawer_home_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view1)
    NavigationView navigationView;

    @BindView(R.id.toolbar1)
    Toolbar toolbar;

    @BindView(R.id.texttoolbar)
    TextView toolbar_title;

    @BindView(R.id.enquire_spinner)
    MaterialSpinner enquire_spinner;

    @BindView(R.id.et_description)
    EditText et_description;
    @BindView(R.id.Dashboard_Container)
    CoordinatorLayout coordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        context = Dashboard.this;
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_title.setText("Dashboard");


        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.open_navigation_drawer, R.string.close_navigation_drawer);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, str_work);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        enquire_spinner.setAdapter(adapter);
        enquire_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str_work_requirement = enquire_spinner.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @OnClick(R.id.btn_continue)
    void btn_continue() {
        validateValue();
    }

    private void validateValue() {
        str_description = et_description.getText().toString().trim();

        if (str_work_requirement.equals("Select Work")) {
            //enquire_spinner.setError("Select Work");
            snackbar_(coordinatorLayout, "Select Work");

        } else if (str_description.isEmpty()) {
            et_description.setError("Enter Description");
            //snackbar_(coordinatorLayout,"Enter Email");
        } else {
            Intent intent = new Intent(context, AddressDetails.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.nav_profile:
                Intent intent = new Intent(context, Profile.class);
                intent.putExtra("Dashboard", true);
                context.startActivity(intent);
                break;
            case R.id.nav_singout:
                signout();
                break;

        }
        drawer.closeDrawer(GravityCompat.START);

        return false;
    }

    private void signout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Leaving already ?")
                .setMessage("Are you sure you want to sign out ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(context, Login.class);
                        startActivity(intent);
                        sharedPreferences = getSharedPreferences("MyPrefe", context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("has Logged In", false);
                        editor.commit();
                        editor.apply();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            ActivityCompat.finishAffinity(this);
        }

    }
}
