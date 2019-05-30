package com.propertymanagment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.propertymanagment.database.DatabaseHelper;
import com.propertymanagment.database.model.UserDetailModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Profile extends AppCompatActivity {
    Context context;
    SharedPreferences sharedPreferences;
    DatabaseHelper db;
    ArrayList<UserDetailModel> userDetailModelArrayList;
    JSONObject jsonObject, jsonObject1;
    JSONArray jsonArray = new JSONArray();
    String str_userName, str_email, str_phoneNo, str_address, str_landmark, str_flatNo, json;
    Boolean address_detail_activity, dashboard_activity;
    int user_id;
    @BindView(R.id.btn_Update)
    Button btn_Update;
    @BindView(R.id.toolbar1)
    Toolbar toolbar;

    @BindView(R.id.texttoolbar)
    TextView toolbar_title;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_email)
    TextView tv_email;
    @BindView(R.id.tv_phoneno)
    TextView tv_phoneNo;
    @BindView(R.id.tv_address)
    TextView tv_address;
    @BindView(R.id.tv_landmark)
    TextView tv_landmark;
    @BindView(R.id.tv_flat_no)
    TextView tv_flatNo;

    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_email)
    EditText et_email;
    @BindView(R.id.et_phoneno)
    EditText et_phoneNo;
    @BindView(R.id.et_address)
    EditText et_address;
    @BindView(R.id.et_landmark)
    EditText et_landmark;
    @BindView(R.id.et_flat_no)
    EditText et_flatNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        context = Profile.this;
        ButterKnife.bind(this);
        db = new DatabaseHelper(context);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_title.setText("Profile");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });
        sharedPreferences = getSharedPreferences("MyPrefe", context.MODE_PRIVATE);
        user_id = sharedPreferences.getInt("user_id", 0);
        UserDetailModel userDetailModel = db.getUserDetail(user_id);
        json = userDetailModel.getUser_detail();
        final Intent intent = getIntent();
        address_detail_activity = intent.getBooleanExtra("Address Detail", false);
        dashboard_activity = intent.getBooleanExtra("Dashboard", false);
        if (address_detail_activity) {
            edit();
        } else if (dashboard_activity) {
            getUserDetail();
        }


    }

    private void getUserDetail() {
        try {
            jsonObject1 = new JSONObject(json);
            tv_name.setText(jsonObject1.getString("name"));
            tv_email.setText(jsonObject1.getString("email"));
            tv_phoneNo.setText(jsonObject1.getString("phoneNo"));
            if (!jsonObject1.getString("addess").isEmpty()) {
                tv_address.setText(jsonObject1.getString("addess"));
            }
            if (!jsonObject1.getString("landmark").isEmpty()) {
                tv_landmark.setText(jsonObject1.getString("landmark"));
            }
            if (!jsonObject1.getString("flatNo").isEmpty()) {
                tv_flatNo.setText(jsonObject1.getString("flatNo"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @OnClick(R.id.btn_Update)
    void submit() {
        validateValue();
        if (address_detail_activity) {
            Intent intent = new Intent(context, AddressDetails.class);
            startActivity(intent);
            finish();
        } else if (dashboard_activity) {
            Intent intent = new Intent(context, Dashboard.class);
            startActivity(intent);
            finish();
        }

    }

    private void validateValue() {
        str_userName = et_name.getText().toString().trim();
        str_email = et_email.getText().toString().trim();
        str_phoneNo = et_phoneNo.getText().toString().trim();
        str_address = et_address.getText().toString().trim();
        str_landmark = et_landmark.getText().toString().trim();
        str_flatNo = et_flatNo.getText().toString().trim();

        if (str_userName.isEmpty()) {
            et_name.setError("Enter UserName");
            //snackbar_(coordinatorLayout,"Enter UserName");

        } else if (str_email.isEmpty()) {
            et_email.setError("Enter Email");
            //snackbar_(coordinatorLayout,"Enter Email");
        } else if (!isValidEmail(str_email)) {
            et_email.setError("Enter Valid Email");
            //snackbar_(coordinatorLayout,"Enter Valid Email");
        } else if (str_phoneNo.isEmpty()) {
            et_phoneNo.setError("Enter PhoneNo");
            // snackbar_(coordinatorLayout,"Enter PhoneNo");
        } else if (str_phoneNo.length() < 10) {
            et_phoneNo.setError("PhoneNo Length should be 10 digit.");
            //snackbar_(coordinatorLayout,"PhoneNo Length should be 10 digit.");
        } else if (str_address.isEmpty()) {
            et_email.setError("Enter address");
            //snackbar_(coordinatorLayout,"Enter Email");
        } else if (str_landmark.isEmpty()) {
            et_email.setError("Enter landmark");
            //snackbar_(coordinatorLayout,"Enter Email");
        } else if (str_flatNo.isEmpty()) {
            et_email.setError("Enter FlatNo");
            //snackbar_(coordinatorLayout,"Enter Email");
        } else {
            updateUserDetail();

        }
    }

    public static Boolean isValidEmail(CharSequence target) {
        return Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void updateUserDetail() {
        try {
            jsonObject = new JSONObject();
            jsonObject.put("name", str_userName);
            jsonObject.put("email", str_email);
            jsonObject.put("phoneNo", str_phoneNo);
            jsonObject.put("addess", str_address);
            jsonObject.put("landmark", str_landmark);
            jsonObject.put("flatNo", str_flatNo);
            db.updateProduct(jsonObject.toString(), user_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @OnClick(R.id.img_edit)
    void edit() {

        tv_name.setVisibility(View.GONE);
        et_name.setVisibility(View.VISIBLE);

        tv_email.setVisibility(View.GONE);
        et_email.setVisibility(View.VISIBLE);

        tv_phoneNo.setVisibility(View.GONE);
        et_phoneNo.setVisibility(View.VISIBLE);

        tv_address.setVisibility(View.GONE);
        et_address.setVisibility(View.VISIBLE);

        tv_landmark.setVisibility(View.GONE);
        et_landmark.setVisibility(View.VISIBLE);

        tv_flatNo.setVisibility(View.GONE);
        et_flatNo.setVisibility(View.VISIBLE);
        btn_Update.setVisibility(View.VISIBLE);
        try {
            jsonObject1 = new JSONObject(json);
            et_name.setText(jsonObject1.getString("name"));
            et_email.setText(jsonObject1.getString("email"));
            et_phoneNo.setText(jsonObject1.getString("phoneNo"));
            if (!jsonObject1.getString("addess").isEmpty()) {
                et_address.setText(jsonObject1.getString("addess"));
            }
            if (!jsonObject1.getString("landmark").isEmpty()) {
                et_landmark.setText(jsonObject1.getString("landmark"));
            }
            if (!jsonObject1.getString("flatNo").isEmpty()) {
                et_flatNo.setText(jsonObject1.getString("flatNo"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
