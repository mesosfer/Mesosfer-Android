package com.eyro.mesosfer.sample;

import android.app.ProgressDialog;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.eyro.mesosfer.MesosferException;
import com.eyro.mesosfer.MesosferUser;
import com.eyro.mesosfer.RegisterCallback;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText textEmail, textPassword, textFirstname, textLastname, textDateOfBirth,
            textHeight, textWeight;
    private Switch switchIsMarried;

    private String email, password, firstname, lastname, dateOfBirthString, height, weight;
    private Date dateOfBirth;
    private boolean isMarried;

    private ProgressDialog loading;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Mesosfer Registration");
        }

        // initialize input form view
        textEmail = (TextInputEditText) findViewById(R.id.text_email);
        textPassword = (TextInputEditText) findViewById(R.id.text_password);
        textFirstname = (TextInputEditText) findViewById(R.id.text_firstname);
        textLastname = (TextInputEditText) findViewById(R.id.text_lastname);
        textDateOfBirth = (TextInputEditText) findViewById(R.id.text_date_of_birth);
        textHeight = (TextInputEditText) findViewById(R.id.text_height);
        textWeight = (TextInputEditText) findViewById(R.id.text_weight);
        switchIsMarried = (Switch) findViewById(R.id.switch_is_married);

        loading = new ProgressDialog(this);
        loading.setIndeterminate(true);
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);
    }

    public void handleRegister(View view) {
        // get all value from input
        email = textEmail.getText().toString();
        password = textPassword.getText().toString();
        firstname = textFirstname.getText().toString();
        lastname = textLastname.getText().toString();
        dateOfBirthString = textDateOfBirth.getText().toString();
        height = textHeight.getText().toString();
        weight = textWeight.getText().toString();
        isMarried = switchIsMarried.isChecked();

        // validating input values
        if (!isInputValid()) {
            // return if there is an invalid input
            return;
        }

        registerUser();
    }

    private boolean isInputValid() {
        // validating all input values if it is empty
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Email is empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Password is empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(firstname)) {
            Toast.makeText(this, "First name is empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(lastname)) {
            Toast.makeText(this, "Last name is empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(dateOfBirthString)) {
            Toast.makeText(this, "Date of birth is empty", Toast.LENGTH_LONG).show();
            return false;
        } else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            try {
                dateOfBirth = format.parse(dateOfBirthString);
            } catch (ParseException e) {
                // show error message when user input invalid format of date
                Toast.makeText(this, "Invalid format of date of birth, use `yyyy-mm-dd`", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        if (TextUtils.isEmpty(height)) {
            Toast.makeText(this, "Height is empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(weight)) {
            Toast.makeText(this, "Weight is empty", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void registerUser() {
        // showing a progress dialog loading
        loading.setMessage("Registering new user...");
        loading.show();

        // create new instance of Mesosfer User
        MesosferUser newUser = MesosferUser.createUser();
        // set default field
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setFirstName(firstname);
        newUser.setLastName(lastname);
        // set custom field
        newUser.setData("dateOfBirth", dateOfBirth);
        newUser.setData("height", Double.parseDouble(height));
        newUser.setData("weight", Integer.parseInt(weight));
        newUser.setData("isMarried", isMarried);
        // execute register user asynchronous
        newUser.registerAsync(new RegisterCallback() {
            @Override
            public void done(MesosferException e) {
                // hide progress dialog loading
                loading.dismiss();

                // setup alert dialog builder
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setNegativeButton(android.R.string.ok, null);

                // check if there is an exception happen
                if (e != null) {
                    builder.setTitle("Error Happen");
                    builder.setMessage(
                            String.format(Locale.getDefault(), "Error code: %d\nDescription: %s",
                                    e.getCode(), e.getMessage())
                    );
                    dialog = builder.show();
                    return;
                }

                builder.setTitle("Register Succeeded");
                builder.setMessage("Thank you for registering.");
                dialog = builder.show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        // dismiss any resource showing
        if (loading != null && loading.isShowing()) {
            loading.dismiss();
        }

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        super.onDestroy();
    }
}
