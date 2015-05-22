package com.e.extension.collisions.bindings;


import android.graphics.Bitmap;

import com.e.collisioncore.pixelperfect.masks.implementations.IBitmap;


public class BitmapChunkAdapterGLES1 implements IBitmap {

	private final Bitmap mBitmap;

	public BitmapChunkAdapterGLES1(Bitmap pBitmap) {
		mBitmap = pBitmap;
	}

	@Override
	public int getPixel(int offsetX, int offsetY, int baseX, int baseY,
			int width, int height) {
		int y = baseY + offsetY;
		int x = baseX + offsetX;
		return mBitmap.getPixel(x, y);
	}

}
