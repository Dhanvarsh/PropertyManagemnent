package com.propertymanagment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
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

public class Login extends BaseActivity {

    DatabaseHelper db;
    Context context;
    UserDetailModel userDetailModel;
    ArrayList<UserDetailModel> userDetailModelArrayList;
    JSONObject jsonObject, jsonObject1;
    JSONArray jsonArray = new JSONArray();
    String str_user_id, str_password;
    SharedPreferences sharedPreferences;

    @BindView(R.id.et_user_id)
    EditText et_user_id;

    @BindView(R.id.et_password)
    EditText et_password;

    @BindView(R.id.Login_container)
    CoordinatorLayout coordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        context = Login.this;
        db = new DatabaseHelper(context);
        onEnterPress();
    }

    private void onEnterPress() {
        et_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    validateValue();
                }
                return false;
            }
        });
    }

    @OnClick(R.id.btn_login)
    void login() {
        validateValue();


    }

    @OnClick(R.id.create_account)
    void signup() {
        Intent intent = new Intent(context, SignUp.class);
        startActivity(intent);

    }

    private void validateValue() {
        str_user_id = et_user_id.getText().toString().trim();
        str_password = et_password.getText().toString().trim();


        if (str_user_id.isEmpty()) {
            et_user_id.setError("Enter Email/PhoneNo");
            //snackbar_(coordinatorLayout,"Enter Email");
        }
        /*else if(!isValidEmail(str_email)){
            et_email.setError("Enter Valid Email");
            //snackbar_(coordinatorLayout,"Enter Valid Email");
        }*/
        else if (str_password.isEmpty()) {
            et_password.setError("Enter Password");
            //snackbar_(coordinatorLayout,"Enter Password");
        } else if (str_password.length() < 6) {
            et_password.setError("Password should be of minimum 6 character or digit.");
            //snackbar_(coordinatorLayout,"Password should be of minimum 6 character or digit.");
        } else {
            authentication();
        }
    }

    private void authentication() {
        boolean count = false;
        userDetailModelArrayList = new ArrayList<>();
        userDetailModelArrayList.addAll(db.getAllUsersDetails());
        if (userDetailModelArrayList.size() == 0) {
            snackbar_(coordinatorLayout, "Register yourself by clicking on NewUser");
        } else {
            for (int i = 0; i < userDetailModelArrayList.size(); i++) {
                String json = userDetailModelArrayList.get(i).getUser_detail();
                try {

                    jsonObject1 = new JSONObject(json);
                    if ((jsonObject1.getString("email").equals(str_user_id)
                            || jsonObject1.getString("phoneNo").equals(str_user_id))
                            && jsonObject1.getString("password").equals(str_password)) {
                        sharedPreferences = getSharedPreferences("MyPrefe", context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("has Logged In", true);
                        editor.putInt("user_id", userDetailModelArrayList.get(i).getId());
                        editor.putString("email", jsonObject1.getString("email"));
                        editor.commit();
                        count = true;
                        Intent intent = new Intent(context, Dashboard.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (count == false) {
                snackbar_(coordinatorLayout, "Id/Password is Invalid");
            }


        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityCompat.finishAffinity(this);
    }

}
