package com.e.collisioncore.pixelperfect.masks.factory;

import com.e.collisioncore.pixelperfect.masks.IPixelPerfectMask;

import java.nio.ByteBuffer;

public interface IPixelPerfectMaskFactory {

	public IPixelPerfectMask getIPixelPerfectMask(int pWidth, int pHeight, ByteBuffer pByteBuffer);
}
