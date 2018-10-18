package com.cris.nvh.loadgallerytorecyclerview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
	private ArrayList<String> mPaths;

	public Adapter(ArrayList<String> paths) {
		mPaths = paths;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater
				.from(parent.getContext())
				.inflate(R.layout.viewholder, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		Bitmap bitmap = decodeFile(mPaths.get(position));
		holder.setImage(bitmap);
	}

	@Override
	public int getItemCount() {
		return mPaths != null ? mPaths.size() : 0;
	}

	public static Bitmap decodeFile(String pathName) {
		final String TAG_LOG = "LOG: ";
		Bitmap bitmap = null;
		int reduceImageSize = 32;
		BitmapFactory.Options options = new BitmapFactory.Options();
		for (options.inSampleSize = 1; options.inSampleSize <= reduceImageSize; options.inSampleSize++)
			try {
				bitmap = BitmapFactory.decodeFile(pathName, options);
				break;
			} catch (OutOfMemoryError outOfMemoryError) {
				// If an OutOfMemoryError occurred, we continue with for loop and next inSampleSize value
				Log.e(TAG_LOG, outOfMemoryError.toString());
			}
		return bitmap;
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		public ImageView mImageView;

		public ViewHolder(View v) {
			super(v);
			mImageView = (ImageView) v.findViewById(R.id.image_view);
		}

		public void setImage(Bitmap bitmap) {
			mImageView.setImageBitmap(bitmap);
		}
	}
}
