package com.propertymanagment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.CoordinatorLayout;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
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

public class SignUp extends BaseActivity {
    DatabaseHelper db;
    Context context;
    UserDetailModel userDetailModel;
    ArrayList<UserDetailModel> userDetailModelArrayList;
    JSONObject jsonObject, jsonObject1;
    JSONArray jsonArray = new JSONArray();
    String str_userName, str_email, str_phoneNo, str_password;
    SharedPreferences sharedPreferences;
    long id;

    @BindView(R.id.toolbar1)
    Toolbar toolbar;

    @BindView(R.id.texttoolbar)
    TextView toolbar_title;

    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_email)
    EditText et_email;
    @BindView(R.id.et_phoneno)
    EditText et_phoneNo;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.SignUpContainer)
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        context = SignUp.this;
        db = new DatabaseHelper(context);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_title.setText("Create Account");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Login.class);
                startActivity(intent);
                finish();
            }
        });
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

    @OnClick(R.id.btn_signup)
    void signUp() {
        validateValue();
    }

    private void validateValue() {
        str_userName = et_name.getText().toString().trim();
        str_email = et_email.getText().toString().trim();
        str_phoneNo = et_phoneNo.getText().toString().trim();
        str_password = et_password.getText().toString().trim();

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
        } else if (str_password.isEmpty()) {
            et_password.setError("Enter Password");
            //snackbar_(coordinatorLayout,"Enter Password");
        } else if (str_password.length() < 6) {
            et_password.setError("Password should be of minimum 6 character or digit.");
            //snackbar_(coordinatorLayout,"Password should be of minimum 6 character or digit.");
        } else {
            addUserDetail();
            sharedPreferences = getSharedPreferences("MyPrefe", context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("has Logged In", true);
            editor.putInt("user_id", (int) id);
            editor.commit();
            Intent intent = new Intent(context, Dashboard.class);
            startActivity(intent);
        }
    }

    public static Boolean isValidEmail(CharSequence target) {
        return Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void addUserDetail() {
        boolean count = false;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("name", str_userName);
            jsonObject.put("email", str_email);
            jsonObject.put("phoneNo", str_phoneNo);
            jsonObject.put("password", str_password);
            //jsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        userDetailModelArrayList = new ArrayList<>();
        userDetailModelArrayList.addAll(db.getAllUsersDetails());
        if (userDetailModelArrayList.size() == 0) {
            id = db.insertProduct(jsonObject.toString());

        } else {
            for (int i = 0; i < userDetailModelArrayList.size(); i++) {
                String json = userDetailModelArrayList.get(i).getUser_detail();
                try {

                    jsonObject1 = new JSONObject(json);
                    if (jsonObject1.getString("email").equals(str_email)) {
                        snackbar_(coordinatorLayout, "Already Exist.");
                        count = true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (count == false) {
                id = db.insertProduct(jsonObject.toString());

            }

        }
    }
}
