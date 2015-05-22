package com.e.sprite;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.e.manager.ResourcesManager;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by erichorvat on 5/12/15.
 */
public abstract class Bob extends Sprite
{

    // ---------------------------------------------
    // VARIABLES
    // ---------------------------------------------
    private Body body;


    // ---------------------------------------------
    // CONSTRUCTOR
    // ---------------------------------------------

    public Bob(float pX, float pY, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld)
    {
        super(pX, pY, ResourcesManager.getInstance().bob, vbo);
        createPhysics(camera, physicsWorld);
        camera.setChaseEntity(this);
    }

    public abstract void onDie();

    private void createPhysics(final Camera camera, PhysicsWorld physicsWorld){

        FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0, 0);


        body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyDef.BodyType.DynamicBody, FIXTURE_DEF);

        body.setUserData("bob");

        physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false)
        {
            @Override
            public void onUpdate(float pSecondsElapsed)
            {
                super.onUpdate(pSecondsElapsed);
                camera.onUpdate(0.1f);

                if (getY() <= 0)
                {
                    onDie();
                }else{
                   // body.applyAngularImpulse(3.0f);
                }

             /** Stimulates X Axis Movement  WILL NEED LATER **/
             //body.setLinearVelocity(new Vector2(5, body.getLinearVelocity().y));
                //body.applyAngularImpulse(3.0f);
            }
        });
    }

    public void launchBob(){
        body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, 12));
    }

    /*public void setRunning()
    {
       // canRun = true;

        final long[] PLAYER_ANIMATE = new long[] { 60, 60, 60 };

        animate(PLAYER_ANIMATE, 0, 1, true);
    }*/

}