package com.e.extension.collisions.bindings;

import com.e.collisioncore.pixelperfect.Transformation;

import org.andengine.entity.shape.IShape;



public class ShapeAdapter implements com.e.collisioncore.pixelperfect.IShape {

	private final IShape mShape;

	public ShapeAdapter(IShape pShape) {
		mShape = pShape;
	}

	@Override
	public Transformation getLocalToSceneTransformation() {
		return TransformationAdapter.adapt(mShape.getLocalToSceneTransformation());
	}

	@Override
	public Transformation getSceneToLocalTransformation() {
		return TransformationAdapter.adapt(mShape.getSceneToLocalTransformation());
	}

	@Override
	public float getWidth() {
		return mShape.getWidth();
	}

	@Override
	public float getHeight() {
		return mShape.getHeight();
	}

}
