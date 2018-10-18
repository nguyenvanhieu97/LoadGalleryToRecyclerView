package com.cris.nvh.loadgallerytorecyclerview;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
	public static final int REQUEST_CODE = 283;
	public static final String PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE;
	public static final int PERMISSION_GRANTED = PackageManager.PERMISSION_GRANTED;
	public static final String PERMISSION_NOT_GRANTED = "Permission is not granted";
	public static final int SPAN_COUNT = 2;
	private ArrayList<String> mPaths;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		RecyclerView recyclerView;
		RecyclerView.Adapter adapter;
		RecyclerView.LayoutManager layoutManager;
		recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
		layoutManager = new GridLayoutManager(this, SPAN_COUNT);
		recyclerView.setLayoutManager(layoutManager);
		requestPermission();
		adapter = new Adapter(mPaths);
		recyclerView.setAdapter(adapter);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == REQUEST_CODE) {
			if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
				mPaths = getAllShownImagesPath();
			} else {
				Toast.makeText(this, PERMISSION_NOT_GRANTED, Toast.LENGTH_SHORT).show();
			}
		}
	}

	public void requestPermission() {
		if (ContextCompat.checkSelfPermission(this, PERMISSION) != PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, new String[]{PERMISSION}, REQUEST_CODE);
		} else {
			// get image paths
			mPaths = getAllShownImagesPath();
		}
	}

	public ArrayList<String> getAllShownImagesPath() {
		Cursor cursor;
		int column_index;
		String absolutePathOfImage;
		ArrayList<String> listOfAllImages = new ArrayList<>();

		//projection specifies which column is choose
		String[] projection = {MediaStore.MediaColumns.DATA};
		Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

		cursor = getContentResolver().query(uri, projection, null, null, null);
		if (cursor == null) {
			return listOfAllImages;
		}
		cursor.moveToFirst();
		column_index = cursor.getColumnIndex(projection[0]);
		while (!cursor.isAfterLast()) {
			absolutePathOfImage = cursor.getString(column_index);
			listOfAllImages.add(absolutePathOfImage);
			cursor.moveToNext();
		}
		cursor.close();
		return listOfAllImages;
	}
}
