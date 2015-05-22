package com.e.scene;

import android.app.Activity;

import com.e.manager.ResourcesManager;
import com.e.manager.SceneManager;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by erichorvat on 5/11/15.
 */
    public abstract class BaseScene extends Scene
    {
        //---------------------------------------------
        // VARIABLES
        //---------------------------------------------

        protected Engine engine;
        protected Activity activity;
        protected ResourcesManager resourcesManager;
        protected VertexBufferObjectManager vbom;
        protected SmoothCamera camera;

        //---------------------------------------------
        // CONSTRUCTOR
        //---------------------------------------------

        public BaseScene()
        {
            this.resourcesManager = ResourcesManager.getInstance();
            this.engine = resourcesManager.engine;
            this.activity = resourcesManager.activity;
            this.vbom = resourcesManager.vbom;
            this.camera = resourcesManager.camera;
            createScene();
        }

        //---------------------------------------------
        // ABSTRACTION
        //---------------------------------------------

        public abstract void createScene();

        public abstract void onBackKeyPressed();

        public abstract SceneManager.SceneType getSceneType();

        public abstract void disposeScene();


}
