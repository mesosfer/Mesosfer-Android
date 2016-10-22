package com.eyro.mesosfer.sample;

import android.app.ProgressDialog;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.eyro.mesosfer.FindCallback;
import com.eyro.mesosfer.MesosferException;
import com.eyro.mesosfer.MesosferQuery;
import com.eyro.mesosfer.MesosferUser;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class UserListActivity extends AppCompatActivity {

    private TextInputEditText textSearch;
    private ListView listview;
    private SimpleAdapter adapter;

    private ProgressDialog loading;
    private AlertDialog dialog;

    private final List<Map<String, String>> mapDataList = new ArrayList<>();
    private static final int[] to = new int[] { android.R.id.text1, android.R.id.text2 };
    private static final String[] from = new String[] { "id", "data" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("User List");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        textSearch = (TextInputEditText) findViewById(R.id.text_search);
        adapter = new SimpleAdapter(this, mapDataList, android.R.layout.simple_list_item_2, from, to);
        listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(adapter);

        loading = new ProgressDialog(this);
        loading.setIndeterminate(true);
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);

        updateAndShowUserList(null);
    }

    public void handleSearch(View view) {
        String searchString = textSearch.getText().toString();
        updateAndShowUserList(searchString);
    }

    private void updateAndShowUserList(@Nullable String firstname) {
        MesosferQuery<MesosferUser> query = MesosferUser.getQuery();
        if (!TextUtils.isEmpty(firstname)) {
            query.whereEqualTo("firstname", firstname);
        }

        // showing a progress dialog loading
        loading.setMessage("Querying user...");
        loading.show();

        query.findAsync(new FindCallback<MesosferUser>() {
            @Override
            public void done(List<MesosferUser> list, MesosferException e) {
                // hide progress dialog loading
                loading.dismiss();

                // check if there is an exception happen
                if (e != null) {
                    // setup alert dialog builder
                    AlertDialog.Builder builder = new AlertDialog.Builder(UserListActivity.this);
                    builder.setNegativeButton(android.R.string.ok, null);
                    builder.setTitle("Error Happen");
                    builder.setMessage(
                            String.format(Locale.getDefault(), "Error code: %d\nDescription: %s",
                                    e.getCode(), e.getMessage())
                    );
                    dialog = builder.show();
                    return;
                }

                // clear all user list
                mapDataList.clear();
                for (MesosferUser user : list) {
                    Map<String, String> map = new HashMap<>();
                    map.put("id", "ID : " + user.getObjectId());
                    try {
                        map.put("data", user.toJSON().toString(4));
                    } catch (JSONException e1) {
                        map.put("data", user.toJSON().toString());
                    }
                    mapDataList.add(map);
                }
                adapter.notifyDataSetChanged();
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
