package com.propertymanagment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

public class AddressDetails extends AppCompatActivity {

    Context context;
    SharedPreferences sharedPreferences;
    DatabaseHelper db;
    ArrayList<UserDetailModel> userDetailModelArrayList;
    JSONObject jsonObject, jsonObject1;
    JSONArray jsonArray = new JSONArray();
    String str_userName, str_email, str_phoneNo, str_address, str_landmark, str_flatNo;
    int user_id;
    @BindView(R.id.toolbar1)
    Toolbar toolbar;

    @BindView(R.id.texttoolbar)
    TextView toolbar_title;

    @BindView(R.id.tv_phoneno)
    TextView tv_phoneNo;
    @BindView(R.id.tv_address)
    TextView tv_address;
    @BindView(R.id.tv_landmark)
    TextView tv_landmark;
    @BindView(R.id.tv_flat_no)
    TextView tv_flatNo;

    @BindView(R.id.et_phoneNo)
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
        setContentView(R.layout.activity_address_details);
        context = AddressDetails.this;
        ButterKnife.bind(this);
        db = new DatabaseHelper(context);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_title.setText("Address Detail");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });
        getUserDetail();
    }

    private void getUserDetail() {
        sharedPreferences = getSharedPreferences("MyPrefe", context.MODE_PRIVATE);
        user_id = sharedPreferences.getInt("user_id", 0);
        UserDetailModel userDetailModel = db.getUserDetail(user_id);
        String json = userDetailModel.getUser_detail();
        try {
            jsonObject1 = new JSONObject(json);
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

    @OnClick(R.id.btn_submit)
    void btn_continue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your work requirement have sucessfully!!")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context, Dashboard.class);
                        startActivity(intent);
                    }
                }).show();
    }

    @OnClick(R.id.img_edit)
    void edit() {
        Intent intent = new Intent(context, Profile.class);
        intent.putExtra("Address Detail", true);
        startActivity(intent);
    }
}
