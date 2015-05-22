package com.e.collisioncore.pixelperfect.masks.factory;

import com.e.collisioncore.pixelperfect.masks.IPixelPerfectMask;
import com.e.collisioncore.pixelperfect.masks.implementations.CustomPixelPerfectMask;

import java.nio.ByteBuffer;

public class PixelPerfectMaskFactory implements IPixelPerfectMaskFactory {

	@Override
	public IPixelPerfectMask getIPixelPerfectMask(int pWidth, int pHeight,
			ByteBuffer pByteBuffer) {
		return new CustomPixelPerfectMask(pWidth, pHeight, pByteBuffer);
	}

}
