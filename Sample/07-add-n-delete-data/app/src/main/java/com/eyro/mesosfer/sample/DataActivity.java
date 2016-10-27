package com.eyro.mesosfer.sample;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.eyro.mesosfer.DeleteCallback;
import com.eyro.mesosfer.FindCallback;
import com.eyro.mesosfer.MesosferData;
import com.eyro.mesosfer.MesosferException;
import com.eyro.mesosfer.MesosferQuery;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DataActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView listview;
    private SimpleAdapter adapter;
    private List<MesosferData> listData;

    private ProgressDialog loading;
    private AlertDialog dialog;

    private final List<Map<String, String>> mapDataList = new ArrayList<>();
    private static final int[] to = new int[] { android.R.id.text1, android.R.id.text2 };
    private static final String[] from = new String[] { "id", "data" };
    private static final String[] menu = new String[] { "Delete" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Data Beacon List");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        adapter = new SimpleAdapter(this, mapDataList, android.R.layout.simple_list_item_2, from, to);
        listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);

        loading = new ProgressDialog(this);
        loading.setIndeterminate(true);
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);

        updateAndShowDataList();
    }

    private void updateAndShowDataList() {
        MesosferQuery<MesosferData> query = MesosferData.getQuery("Beacon");

        // showing a progress dialog loading
        loading.setMessage("Querying beacon...");
        loading.show();

        query.findAsync(new FindCallback<MesosferData>() {
            @Override
            public void done(List<MesosferData> list, MesosferException e) {
                // hide progress dialog loading
                loading.dismiss();

                // check if there is an exception happen
                if (e != null) {
                    // setup alert dialog builder
                    AlertDialog.Builder builder = new AlertDialog.Builder(DataActivity.this);
                    builder.setNegativeButton(android.R.string.ok, null);
                    builder.setTitle("Error Happen");
                    builder.setMessage(
                            String.format(Locale.getDefault(), "Error code: %d\nDescription: %s",
                                    e.getCode(), e.getMessage())
                    );
                    dialog = builder.show();
                    return;
                }

                // clear all data list
                mapDataList.clear();
                // save list data
                listData = new ArrayList<>(list);
                for (MesosferData data : list) {
                    Map<String, String> map = new HashMap<>();
                    map.put("id", "ID : " + data.getObjectId());
                    try {
                        map.put("data", data.toJSON().toString(4));
                    } catch (JSONException e1) {
                        map.put("data", data.toJSON().toString());
                    }
                    mapDataList.add(map);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void handleAddBeacon(View view) {
        Intent intent = new Intent(this, DataFormActivity.class);
        intent.putExtra(DataFormActivity.INTENT_FORM_MODE, DataFormActivity.FORM_MODE_ADD);
        startActivityForResult(intent, DataFormActivity.INTENT_REQUEST_CODE);
    }

    private void deleteBeacon(final int position) {
        final MesosferData data = listData.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Confirmation");
        builder.setMessage("Do you want to delete beacon with id-" + data.getObjectId() + "?");
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int which) {
                loading.setMessage("Deleting beacon...");
                loading.show();

                data.deleteAsync(new DeleteCallback() {
                    @Override
                    public void done(MesosferException e) {
                        // hide progress dialog loading
                        loading.dismiss();

                        // check if there is an exception happen
                        if (e != null) {
                            // setup alert dialog builder
                            AlertDialog.Builder builder = new AlertDialog.Builder(DataActivity.this);
                            builder.setNegativeButton(android.R.string.ok, null);
                            builder.setTitle("Error Happen");
                            builder.setMessage(
                                    String.format(Locale.getDefault(), "Error code: %d\nDescription: %s",
                                            e.getCode(), e.getMessage())
                            );
                            dialog = builder.show();
                            return;
                        }

                        Toast.makeText(DataActivity.this, "Deleted successfully", Toast.LENGTH_SHORT).show();
                        updateAndShowDataList();
                    }
                });
            }
        });
        dialog = builder.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        if (listData != null && !listData.isEmpty()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Beacon Menu");
            builder.setItems(menu, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        deleteBeacon(position);
                    }
                }
            });
            dialog = builder.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DataFormActivity.INTENT_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                updateAndShowDataList();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
