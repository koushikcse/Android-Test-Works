package com.kusu.test;

import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.kusu.test.databinding.ActivityMainBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int STORAGE_CODE = 1;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        List<String> data = new ArrayList<>();
        data.add("Test text line 1");
        data.add("Test text line 2");
        data.add("Test text line 3");
        data.add("Test text line 4");
        data.add("Test text line 5");
        data.add("Test text line 6");
        data.add("Test text line 7");
        data.add("Test text line 8");
        data.add("Test text line 9");
        data.add("Test text line 10");
        data.add("Test text line 11");
        data.add("Test text line 12");
        data.add("Test text line 13");
        data.add("Test text line 14");
        data.add("Test text line 15");
        data.add("Test text line 16");
        data.add("Test text line 17");
        data.add("Test text line 18");
        data.add("Test text line 19");
        data.add("Test text line 20");
        data.add("Test text line 21");
        data.add("Test text line 22");
        data.add("Test text line 1");
        data.add("Test text line 2");
        data.add("Test text line 3");
        data.add("Test text line 4");
        data.add("Test text line 5");
        data.add("Test text line 6");
        data.add("Test text line 7");
        data.add("Test text line 8");
        data.add("Test text line 9");
        data.add("Test text line 10");
        data.add("Test text line 11");
        data.add("Test text line 12");
        data.add("Test text line 13");
        data.add("Test text line 14");
        data.add("Test text line 15");
        data.add("Test text line 16");
        data.add("Test text line 17");
        data.add("Test text line 18");
        data.add("Test text line 19");
        data.add("Test text line 20");
        data.add("Test text line 21");
        data.add("Test text line 22");
        data.add("Test text line 1");
        data.add("Test text line 2");
        data.add("Test text line 3");
        data.add("Test text line 4");
        data.add("Test text line 5");
        data.add("Test text line 6");
        data.add("Test text line 7");
        data.add("Test text line 8");
        data.add("Test text line 9");
        data.add("Test text line 10");
        data.add("Test text line 11");
        data.add("Test text line 12");
        data.add("Test text line 13");
        data.add("Test text line 14");
        data.add("Test text line 15");
        data.add("Test text line 16");
        data.add("Test text line 17");
        data.add("Test text line 18");
        data.add("Test text line 19");
        data.add("Test text line 20");
        data.add("Test text line 21");
        data.add("Test text line 22");

        binding.rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        MyAdapter adapter = new MyAdapter(this, data);
        binding.rvList.setAdapter(adapter);

        List<String> test = new ArrayList<>();
        test.add("4");
        test.add("1");
        test.add("3");
        test.add("7");
        test.add("2");

        Collections.sort(test, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Integer.parseInt(o1) - Integer.parseInt(o2);
            }
        });

        for (int i = 0; i < test.size(); i++) {
            Log.e("Test => ", test.get(i));
        }


        binding.pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                STORAGE_CODE);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {
                    Bitmap bitmap = Utils.getRecyclerViewScreenshot(binding.rvList);
                    PDFHelper pdfHelper = new PDFHelper(createFile(), MainActivity.this);
                    pdfHelper.saveImageToPDF(binding.rvList, bitmap, "test");
                }

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case STORAGE_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Bitmap bitmap = Utils.getRecyclerViewScreenshot(binding.rvList);
                    PDFHelper pdfHelper = new PDFHelper(createFile(), this);
                    pdfHelper.saveImageToPDF(binding.rvList, bitmap, "test");
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "No permission", Toast.LENGTH_LONG);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    private File createFile() {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "PDF_test");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e("App", "failed to create directory");
                return null;
            }
        }
        return mediaStorageDir;
    }
}
