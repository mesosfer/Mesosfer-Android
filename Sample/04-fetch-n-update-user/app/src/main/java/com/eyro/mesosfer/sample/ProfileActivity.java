package com.eyro.mesosfer.sample;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.eyro.mesosfer.ChangePasswordCallback;
import com.eyro.mesosfer.GetCallback;
import com.eyro.mesosfer.MesosferDateFormat;
import com.eyro.mesosfer.MesosferException;
import com.eyro.mesosfer.MesosferObject;
import com.eyro.mesosfer.MesosferUser;
import com.eyro.mesosfer.SaveCallback;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {
    private TextInputEditText textOldPassword, textNewPassword, textConfirmPassword,
            textFirstname, textLastname, textDateOfBirth, textHeight, textWeight;
    private Switch switchIsMarried;

    private String oldPassword, newPassword, confirmPassword,
            firstname, lastname, dateOfBirthString, height, weight;
    private Date dateOfBirth;
    private boolean isMarried;

    private ProgressDialog loading;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Mesosfer Profile");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // initialize input form view
        textOldPassword = (TextInputEditText) findViewById(R.id.text_password_old);
        textNewPassword = (TextInputEditText) findViewById(R.id.text_password_new);
        textConfirmPassword = (TextInputEditText) findViewById(R.id.text_password_confirm);

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

        // fetch user and show profile data
        this.fetchUser();
    }

    private void fetchUser() {
        // showing a progress dialog loading
        loading.setMessage("Fetching user profile...");
        loading.show();

        final MesosferUser user = MesosferUser.getCurrentUser();
        if (user != null) {
            user.fetchAsync(new GetCallback<MesosferUser>() {
                @Override
                public void done(MesosferUser mesosferUser, MesosferException e) {
                    // hide progress dialog loading
                    loading.dismiss();

                    // check if there is an exception happen
                    if (e != null) {
                        // setup alert dialog builder
                        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                        builder.setNegativeButton(android.R.string.ok, null);
                        builder.setTitle("Error Happen");
                        builder.setMessage(
                                String.format(Locale.getDefault(), "Error code: %d\nDescription: %s",
                                        e.getCode(), e.getMessage())
                        );
                        dialog = builder.show();
                        return;
                    }

                    Toast.makeText(ProfileActivity.this, "Profile Fetched", Toast.LENGTH_SHORT).show();
                    updateView(user);
                }
            });
        }
    }

    private void updateView(MesosferUser user) {
        if (user != null) {
            textFirstname.setText(user.getFirstName());
            textLastname.setText(user.getLastName());

            MesosferObject data = user.getData();
            if (data != null) {
                Date date = data.optDate("dateOfBirth");
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                textDateOfBirth.setText(format.format(date));

                double height = data.optDouble("height");
                textHeight.setText(String.valueOf(height));

                int weight = data.optInt("weight");
                textWeight.setText(String.valueOf(weight));

                switchIsMarried.setChecked(data.optBoolean("isMarried"));
            }
        }
    }

    public void handleUpdateProfile(View view) {
        // get all value from input
        firstname = textFirstname.getText().toString();
        lastname = textLastname.getText().toString();
        dateOfBirthString = textDateOfBirth.getText().toString();
        height = textHeight.getText().toString();
        weight = textWeight.getText().toString();
        isMarried = switchIsMarried.isChecked();

        // validating input values
        if (!isInputProfileValid()) {
            // return if there is an invalid input
            return;
        }

        // execute update profile
        updateProfile();
    }

    private boolean isInputProfileValid() {
        // validating all input values if it is empty
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

    private void updateProfile() {
        // showing a progress dialog loading
        loading.setMessage("Updating user profile...");
        loading.show();

        MesosferUser user = MesosferUser.getCurrentUser();
        if (user != null) {
            user.setFirstName(firstname);
            user.setLastName(lastname);
            // set custom field
            user.setData("dateOfBirth", dateOfBirth);
            user.setData("height", height);
            user.setData("weight", weight);
            user.setData("isMarried", isMarried);
            // execute update user
            user.updateDataAsync(new SaveCallback() {
                @Override
                public void done(MesosferException e) {
                    // hide progress dialog loading
                    loading.dismiss();

                    // setup alert dialog builder
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
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

                    builder.setMessage("Update Profile Succeeded");
                    dialog = builder.show();
                }
            });
        }
    }

    public void handleChangePassword(View view) {
        oldPassword = textOldPassword.getText().toString();
        newPassword = textNewPassword.getText().toString();
        confirmPassword = textConfirmPassword.getText().toString();

        // validating input values
        if (!isInputPasswordValid()) {
            // return if there is an invalid input
            return;
        }

        // execute update password
        updatePassword();
    }

    private boolean isInputPasswordValid() {
        // validating all input values if it is empty
        if (TextUtils.isEmpty(oldPassword)) {
            Toast.makeText(this, "Old password is empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(newPassword)) {
            Toast.makeText(this, "New password is empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if (oldPassword.equals(newPassword)) {
            Toast.makeText(this, "Old and new password are equal", Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Confirmation password is empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Confirmation password is not match", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void updatePassword() {
        // showing a progress dialog loading
        loading.setMessage("Updating user profile...");
        loading.show();

        MesosferUser user = MesosferUser.getCurrentUser();
        if (user != null) {
            // execute update user
            user.changePasswordAsync(oldPassword, newPassword, new ChangePasswordCallback() {
                @Override
                public void done(MesosferException e) {
                    // hide progress dialog loading
                    loading.dismiss();

                    // setup alert dialog builder
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
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

                    builder.setTitle("Update Password Succeeded");
                    builder.setMessage("You need to re-login to use new password!");
                    dialog = builder.show();
                }
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
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
