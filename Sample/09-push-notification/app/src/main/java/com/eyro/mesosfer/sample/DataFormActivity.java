package com.eyro.mesosfer.sample;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.eyro.mesosfer.GetCallback;
import com.eyro.mesosfer.MesosferData;
import com.eyro.mesosfer.MesosferException;
import com.eyro.mesosfer.MesosferObject;
import com.eyro.mesosfer.SaveCallback;

import java.util.Date;
import java.util.Locale;

public class DataFormActivity extends AppCompatActivity {

    public static final int INTENT_REQUEST_CODE = 5342;
    public static final int FORM_MODE_ADD = 0;
    public static final int FORM_MODE_EDIT = 1;
    public static final String INTENT_FORM_MODE = "IntentFormMode";
    public static final String INTENT_OBJECT_ID = "IntentObjectId";

    private int formMode;
    private String name, uuid, major, minor;
    private boolean isActive;
    private MesosferData data;

    private ProgressDialog loading;
    private AlertDialog dialog;

    private TextInputEditText textName, textUUID, textMajor, textMinor;
    private Switch switchIsActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_form);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Data Beacon Form");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        textName = (TextInputEditText) findViewById(R.id.text_name);
        textUUID = (TextInputEditText) findViewById(R.id.text_uuid);
        textMajor = (TextInputEditText) findViewById(R.id.text_major);
        textMinor = (TextInputEditText) findViewById(R.id.text_minor);
        switchIsActive = (Switch) findViewById(R.id.switch_is_active);

        loading = new ProgressDialog(this);
        loading.setIndeterminate(true);
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);

        formMode = getIntent().getIntExtra(INTENT_FORM_MODE, FORM_MODE_ADD);

        if (formMode == FORM_MODE_EDIT) {
            String objectId = getIntent().getStringExtra(INTENT_OBJECT_ID);
            fetchData(objectId);
        }
    }

    private void fetchData(String objectId) {
        loading.setMessage("Fetching data...");
        loading.show();
        MesosferData.createWithObjectId(objectId).fetchAsync(new GetCallback<MesosferData>() {
            @Override
            public void done(MesosferData data, MesosferException e) {
                loading.dismiss();

                if (e != null) {
                    dialog = new AlertDialog.Builder(DataFormActivity.this)
                            .setTitle("Error Happen")
                            .setMessage(
                                    String.format(Locale.getDefault(),
                                            "Error code: %d\nDescription: %s", e.getCode(), e.getMessage()))
                            .show();
                    return;
                }

                DataFormActivity.this.data = data;
                updateView();
            }
        });
    }

    private void updateView() {
        if (data != null) {
            MesosferObject object = data.getData();
            textName.setText(object.optString("name"));
            textUUID.setText(object.optString("proximityUUID"));
            textMajor.setText(String.valueOf(object.optInt("major")));
            textMinor.setText(String.valueOf(object.optInt("minor")));
            switchIsActive.setChecked(object.optBoolean("isActive"));
        }
    }

    public void handleSave(View view) {
        // get all values
        name = textName.getText().toString();
        uuid = textUUID.getText().toString();
        major = textMajor.getText().toString();
        minor = textMinor.getText().toString();
        isActive = switchIsActive.isChecked();

        // validating input
        if (!isInputValid()) {
            // return if invalid
            return;
        }

        // save beacon
        saveBeacon();
    }

    private boolean isInputValid() {
        // validating all input values if it is empty
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Beacon name is empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(uuid)) {
            Toast.makeText(this, "Proximity UUID is empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(major)) {
            Toast.makeText(this, "Beacon major is empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(minor)) {
            Toast.makeText(this, "Beacon minor is empty", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void saveBeacon() {
        // showing a progress dialog loading
        loading.setMessage("Saving beacon data..");
        loading.show();

        if (data == null) {
            data = MesosferData.createData("Beacon");
        }

        // set data
        data.setData("name", name);
        data.setData("proximityUUID", uuid);
        data.setData("major", Integer.parseInt(major));
        data.setData("minor", Integer.parseInt(minor));
        data.setData("isActive", isActive);
        data.setData("timestamp", new Date());
        // execute save data
        data.saveAsync(new SaveCallback() {
            @Override
            public void done(MesosferException e) {
                // hide progress dialog loading
                loading.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(DataFormActivity.this);
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

                Toast.makeText(DataFormActivity.this, "Beacon saved", Toast.LENGTH_SHORT).show();
                setResult(Activity.RESULT_OK);
                finish();
            }
        });
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
