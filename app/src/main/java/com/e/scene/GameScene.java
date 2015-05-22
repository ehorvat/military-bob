package com.e.scene;

import android.hardware.SensorManager;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.e.activities.PowerBar;
import com.e.extension.collisions.entity.sprite.PixelPerfectSprite;
import com.e.extension.collisions.opengl.texture.region.PixelPerfectTextureRegion;
import com.e.manager.SceneManager;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.color.Color;
import org.andengine.util.math.MathUtils;

import java.util.ArrayList;

/**
 * Created by erichorvat on 5/11/15.
 */
public class GameScene extends BaseScene implements MenuScene.IOnMenuItemClickListener, IOnSceneTouchListener {

    private final int MENU_PLAY = 0;
    private final int MENU_OPTIONS = 1;

    private PhysicsWorld world;

    private PixelPerfectSprite bob;
    private PixelPerfectSprite barrel;
    PixelPerfectSprite mFire;
    PixelPerfectSprite blood;

    private ArrayList<Rectangle> rectangles = null;

    float spawnPoint = 0;

    private float cameraBounds;

    float rotation;
    float mFirePositionX;
    float mFirePositionY;

    Body body;
    Body mTankBody;
    Body mBarrelBody;
    Body mGrenadeBody;
    Body mSpikesBody;

    Text gameOverText;
    Text restartText;
    Text scoreText;

    boolean hasFired;
    boolean isHoldingFire;
    boolean gameOver;
    boolean reset;
    boolean isMoving;

    PowerBar mPowerBar;
    float mPower = 0;
    float i = 4;
    float multiplier = 0;

    /* HUD Constants */
    private static final String GAME_OVER_TEXT = "Game Over";
    private static final String TAPE_TO_RESET_TEXT = "tap to reset";
    private static final String SCORE_TEXT = "0123456789";

    /* The categories. */
    public static final short CATEGORYBIT_WALL = 1;
    public static final short CATEGORYBIT_BOB = 2;
    public static final short CATEGORYBIT_SPIKES = 4;
    public static final short CATEGORYBIT_GRENADES = 8;
    public static final short CATEGORYBIT_TANK = 16;
    public static final short CATEGORYBIT_BARREL = 32;
    public static final short CATEGORYBIT_POWERBAR = 64;
    public static final short CATEGORYBIT_MINE = 128;


    /* And what should collide with what. */
    public static final short MASKBITS_WALL = CATEGORYBIT_WALL + CATEGORYBIT_BOB + CATEGORYBIT_SPIKES + CATEGORYBIT_GRENADES + CATEGORYBIT_TANK + CATEGORYBIT_BARREL;
    public static final short MASKBITS_BOB = CATEGORYBIT_WALL + CATEGORYBIT_MINE;
    public static final short MASKBITS_TANK = CATEGORYBIT_WALL;
    public static final short MASKBITS_POWERBAR = CATEGORYBIT_POWERBAR;
    public static final short MASKBITS_BARREL = CATEGORYBIT_BARREL;
    public static final short MASKBITS_MINE = CATEGORYBIT_MINE;

    public static final FixtureDef WALL_FIXTURE_DEF = PhysicsFactory.createFixtureDef(1f, 0.1f, .33f, false, CATEGORYBIT_WALL, MASKBITS_WALL, (short)0);
    public static final FixtureDef BOB_FIXTURE_DEF = PhysicsFactory.createFixtureDef(1.33f, 0.65f, 0.66f, false, CATEGORYBIT_BOB, MASKBITS_BOB, (short)0);
    public static final FixtureDef TANK_FIXTURE_DEF = PhysicsFactory.createFixtureDef(0f, 0.0f, 0.0f, false, CATEGORYBIT_TANK, MASKBITS_TANK, (short)0);
    public static final FixtureDef POWERBAR_FIXTURE_DEF = PhysicsFactory.createFixtureDef(0f, 0.0f, 0.0f, false, CATEGORYBIT_POWERBAR, MASKBITS_POWERBAR, (short)0);
    public static final FixtureDef BARREL_FIXTURE_DEF = PhysicsFactory.createFixtureDef(0f, 0.0f, 0.0f, false, CATEGORYBIT_BARREL, MASKBITS_BARREL, (short)0);
    public static final FixtureDef MINE_FIXTURE_DEF = PhysicsFactory.createFixtureDef(0f, 0.0f, 0.0f, false, CATEGORYBIT_MINE, MASKBITS_MINE, (short)0);

    @Override
    public void createScene() {
        hasFired = false;
        gameOver = false;
        reset = false;
        isMoving = false;
        cameraBounds = camera.getWidth() * 60;
        camera.setBounds(0,0, cameraBounds,480);
        camera.setBoundsEnabled(true);
        rotation = 0;
        createBackground();
        createPhysics();
        createFrameHandler();
        generateWalls();
        createBob();
        createTank();
        createPowerBar();
        createHUD();

        setOnSceneTouchListener(this);
    }



    private void createBob()
    {
        bob = new PixelPerfectSprite(0, 0, resourcesManager.bob, vbom);
        bob.setHeight(100);
        bob.setWidth(70);
        body = PhysicsFactory.createCircleBody(world, bob, BodyDef.BodyType.DynamicBody, BOB_FIXTURE_DEF);
        world.registerPhysicsConnector(new PhysicsConnector(bob, body, true, true));
        body.setUserData("bob");
        attachChild(bob);
        camera.setChaseEntity(bob);
        //bob.setVisible(false);


        blood = addSpriteToParent(bob, (int)bob.getHeight()/2, (int)bob.getWidth()/2, resourcesManager.blood);
        blood.setVisible(false);



    }

    private void createTank(){

        //Attach Tank to Scene
        final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0f, 0.0f);
        final PixelPerfectSprite tank = addSprite(this, 150, 125, resourcesManager.tank);
        tank.setHeight(400);
        tank.setWidth(500);

        mTankBody = PhysicsFactory.createBoxBody(world, tank, BodyDef.BodyType.KinematicBody, TANK_FIXTURE_DEF);

        tank.setCullingEnabled(true);
        tank.setZIndex(1);

        //Attach barrel to scene
        barrel = addSprite(this, 255, 140, resourcesManager.barrel);
        barrel.setHeight(250);
        barrel.setWidth(350);
        barrel.setCullingEnabled(true);

        mBarrelBody = PhysicsFactory.createBoxBody(world, barrel, BodyDef.BodyType.DynamicBody, BARREL_FIXTURE_DEF);
        barrel.setZIndex(0);

        //////////////////
        //
        // Fire Button
        //
        //////////////////

        mFire = new PixelPerfectSprite(0, 0, resourcesManager.fire, vbom){
           @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.isActionUp()){

                    setTouchAreaBindingOnActionDownEnabled(false);
                    isHoldingFire = false;
                    hasFired = true;
                }
                if(pSceneTouchEvent.isActionDown()){
                    if(!hasFired){
                        isHoldingFire = true;
                        Log.v("Holding","holding");
                    }

                }
                return true;
            }

            @Override
            protected void onManagedUpdate(float pSecondsElapsed) {
                super.onManagedUpdate(pSecondsElapsed);
                if(isHoldingFire){
                    if(!hasFired){
                        updatePowerBar();
                    }
                }
            }
        };

        PhysicsFactory.createBoxBody(world, mFire, BodyDef.BodyType.KinematicBody, FIXTURE_DEF);
        mFire.setZIndex(2);


        float [] tankCoords = tank.convertSceneCoordinatesToLocalCoordinates(tank.getX(), tank.getY());

        //float [] fireCoords = tank.convertLocalCoordinatesToSceneCoordinates(fire.getX())

        float midX = tankCoords[0]+tank.getWidth()/2;
        float midY = tankCoords[1]+tank.getHeight()/2;
        Log.v("mids", tank.getWidth() + " getX " + tank.getHeight() + " getY " + mFire.getY() + " tank coords " + tankCoords[0] + " " + tankCoords[1]);


        registerTouchArea(mFire);
        tank.attachChild(mFire);
        mFire.setPosition(tankCoords[0] + 20, tankCoords[1]);


        sortChildren();


    }
    private void updatePowerBar(){
        float prog = mPower * .01f;
        multiplier = prog;
        mPowerBar.setProgress(prog);
        mPower = mPower + i;
        if(mPower > 100){
            i = -4;
        }else if(mPower < 1){
            i = 4;
        }
    }

    @Override
    public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY) {
        switch(pMenuItem.getID())
        {
            case MENU_PLAY:
                return true;
            case MENU_OPTIONS:
                return true;
            default:
                return false;
        }
    }

    private HUD gameHUD;

    private void createHUD(){
        gameHUD = new HUD();
        createFont();
        gameHUD.attachChild(gameOverText);
        gameHUD.attachChild(restartText);
        gameHUD.attachChild(scoreText);
        camera.setHUD(gameHUD);
    }
    private void createPhysics()
    {
        //world = new PhysicsWorld(new Vector2(0, -SensorManager.GRAVITY_EARTH), false);
        world = new PhysicsWorld(new Vector2(0, 0), false);
        this.registerUpdateHandler(world);
        //world.setContactListener(createContactListener());
    }
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent)
    {
        Log.v("boolz", "Has Fired: " + hasFired + " gameOver : " + gameOver);
        if (pSceneTouchEvent.isActionMove()) {
            if(hasFired == false){
                if(gameOver == false){
                Log.v("moving ", "barrel");

                mFire.onAreaTouched(pSceneTouchEvent, pSceneTouchEvent.getX(), pSceneTouchEvent.getY());

                float positionbob[] = bob.convertLocalCoordinatesToSceneCoordinates(body.getPosition().x, body.getPosition().y);

                float position[] = barrel.convertLocalCoordinatesToSceneCoordinates(mBarrelBody.getPosition().x, mBarrelBody.getPosition().y);
                float pValueX = pSceneTouchEvent.getX();
                float pValueY = pSceneTouchEvent.getY();

                float directionX = pValueX - barrel.getX();
                float directionY = pValueY - barrel.getY();

                mFirePositionX = (barrel.getX() + barrel.getWidth()/2);
                mFirePositionY = (barrel.getY() + barrel.getHeight()/2);
                bob.setPosition(mFirePositionX, mFirePositionY );
                body.setTransform(mFirePositionX / 32, mFirePositionY / 32, 0);

                Log.v("LAUNCH VALS", barrel.getX() + " " + barrel.getY() + " " + mFirePositionX + " " + mFirePositionY
                +" "+ barrel.getWidth() + " " + barrel.getHeight() + " " + position[0] + " " + position[1] + " " + body.getPosition().x + " " + body.getPosition().y);


                float r = (float) Math.atan2(-directionY, directionX);
                if(r * -1 < .9 && r * -1 > 0 ){
                    rotation = r;
                    barrel.setRotation(MathUtils.radToDeg(rotation));
                }
                }

                }

            if(gameOver){
                //If game is over and scene is tapped, set reset flag to reset game on next frame
                resetGame();
            }


            return false;

        }

        return true;
    }

    private void generateWalls() {

        rectangles = new ArrayList<Rectangle>();

        for(int i = 0; i<60; i++){
            Rectangle rectangle = new Rectangle(spawnPoint, 0, camera.getWidth() * 2, 80f,vbom);
            rectangle.setColor(new Color(.27f, .63f, .12f));
            rectangle.setZIndex(0);
            rectangles.add(rectangle);
            spawnPoint = spawnPoint + camera.getWidth() * 2;
            PhysicsFactory.createBoxBody(world, rectangle, BodyDef.BodyType.StaticBody, WALL_FIXTURE_DEF);
            attachChild(rectangle);
            createObstacles(rectangle, i);
        }

    }


    private void createPowerBar(){
        mPowerBar = new PowerBar(340, 200, 250, 10, 1, 0, vbom);
        mPowerBar.setBackColor(0f, .0f, 0f, 0f);
        mPowerBar.setProgressColor(1f, .84f, 0f, 1f);
        mPowerBar.setFrameColor(0f, .0f, 0f, 0f);
        mPowerBar.setAnchorCenter(0, 0);
        PhysicsFactory.createBoxBody(world, mPowerBar, BodyDef.BodyType.KinematicBody, POWERBAR_FIXTURE_DEF);
        attachChild(mPowerBar);

    }


    private void destroyPrecedingWall(){
        if(rectangles != null){
            engine.runOnUpdateThread(new Runnable()
            {
                @Override
                public void run()
                {
                    rectangles.remove(0);
                    detachChild(rectangles.get(0));
                    // TODO: Append new wall with sprites
                }
            });
        }
    }

    private void destroyPowerBar(){
        if(mPowerBar != null){
            engine.runOnUpdateThread(new Runnable()
            {
                @Override
                public void run()
                {
                   detachChild(mPowerBar);
                }
            });
        }
    }

    private void attachNewWallWithSprites(){
        FixtureDef WALL_FIX = PhysicsFactory.createFixtureDef(0.0f, 0.0f, 20.0f);
        Rectangle rectangle = new Rectangle(camera.getWidth()/2, camera.getHeight()/2, camera.getWidth(), 100f,vbom);
        rectangle.setColor(Color.GREEN);

        rectangles.add(rectangle);
        spawnPoint += camera.getWidth();
        PhysicsFactory.createBoxBody(world, rectangle, BodyDef.BodyType.StaticBody, WALL_FIX);
    }

    private void createBackground()
    {
        setBackground(new Background(new Color(.54f,.78f,1f)));

        /*** FOR PARALLAX BACKGROUNDS ***/
        /*Background background = new Background(72, 118, 225);
        //background.attachParallaxEntity(new ParallaxBackground.ParallaxEntity(0, new Rectangle(0, 240, resourcesManager.game_background_region, vbom)));
        this.setBackground(background);*/


       /* final AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 1);
        autoParallaxBackground.attachParallaxEntity(new ParallaxBackground.ParallaxEntity(0.0f, new Sprite(200, 200, resourcesManager.game_background_region, vbom)));
        setBackground(autoParallaxBackground);

        autoParallaxBackground.attachParallaxEntity(new ParallaxBackground.ParallaxEntity(-20.0f, new Sprite(0, 480 - ResourcesManager.getInstance().game_background_region.getHeight(), ResourcesManager.getInstance().game_background_region, vbom)));
        setBackground(autoParallaxBackground);*/

    }

    private void createFrameHandler(){
        registerUpdateHandler(new IUpdateHandler() {

            @Override
            public void onUpdate(float pSecondsElapsed) {

                float position[] = bob.convertLocalCoordinatesToSceneCoordinates(body.getPosition().x, body.getPosition().y);
                if (position[0] >= spawnPoint - 50000) {
                    spawnPoint = spawnPoint + camera.getWidth() * 2;
                    cameraBounds = cameraBounds * 2;
                    camera.setBounds(0, 0, cameraBounds, 480);
                    generateWalls();
                }
                if (body.getPosition().x >= camera.getWidth()) {
                    // destroyPrecedingWall();
                    // attachNewWallWithSprites();
                }

                if(hasFired){
                    hasFired = false;
                    launchBob();
                }

                if(isMoving){
                    updateScore();
                }

                if(gameOver){
                    //Tap to reset
                }

                if(reset){
                    reset = false;

                }


            }

            @Override
            public void reset() {

            }
        });

    }

    private void launchBob(){
        isMoving = true;
        destroyPowerBar();
        bob.setVisible(true);
        //world.setGravity(new Vector2(0, -SensorManager.GRAVITY_EARTH));
        //body.setAngularVelocity(rotation);

        //body.setLinearVelocity(10f, 0f);
        //body.setType(BodyDef.BodyType.DynamicBody);
        //mBarrelBody.setType(BodyDef.BodyType.DynamicBody);
        //body.setAngularVelocity(1f);
        Log.v("Multiplier before ", multiplier+"");

        float multVal = 4 * multiplier * 10;
        Log.v("Multiplier after ", multVal+"");

        if (multVal < 1) multVal = 1;

        float xmult = (float)Math.cos(rotation * -1);
        float ymult = 1 - xmult;

        float xvelocity = (xmult * multVal) * 170;
        float yvelocity = (ymult * multVal) * 125;

        Log.v("Velocities", xvelocity + " " + yvelocity);

        Log.v("launch vals ", " Rotation " + rotation + " x: " + xmult + " y: " + ymult);
        world.setGravity(new Vector2(0, -SensorManager.GRAVITY_EARTH));

        //bobHandler.setAcceleration(xvelocity, yvelocity);
        body.applyLinearImpulse(new Vector2(xvelocity,yvelocity),new Vector2(body.getPosition().x,body.getPosition().y));
        body.applyAngularImpulse(20f);
        //body.applyLinearImpulse(4000f, 700f, 1f, 1f);
    }

    @Override
    public void onBackKeyPressed()
    {
        System.exit(0);
    }

    @Override
    public SceneManager.SceneType getSceneType() {
        return null;
    }

    @Override
    public void disposeScene() {

    }

    private PixelPerfectSprite addSprite(Scene scene, int x, int y, PixelPerfectTextureRegion region){
        PixelPerfectSprite sprite = new PixelPerfectSprite(x, y, region, vbom);
        scene.attachChild(sprite);
        return sprite;
    }

    private PixelPerfectSprite addSpriteToParent(Sprite parent, float x, float y, PixelPerfectTextureRegion region){
        PixelPerfectSprite sprite = new PixelPerfectSprite(x, y, region, vbom);
        parent.attachChild(sprite);
        return sprite;
    }

    private void createObstacles(Rectangle rectangle, int i){

        float grenadeSpawn = rectangle.getWidth()/2 -150;
        float spikeSpawn = rectangle.getWidth()/2 + 200;

        final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(0f, 0f, 0f);


        if(i + 1 % 3 != 0){
            //Spawn spike
            PixelPerfectSprite spikes = new PixelPerfectSprite(spikeSpawn, 100, resourcesManager.spikes, vbom){
                @Override
                protected void onManagedUpdate(float pSecondsElapsed) {
                    if(bob.collidesWith(this)){
                        killed(true);
                    }
                }
            };

            spikes.setHeight(45);
            spikes.setWidth(45);
            spikes.setCullingEnabled(true);

            mSpikesBody = PhysicsFactory.createBoxBody(world, spikes, BodyDef.BodyType.KinematicBody, FIXTURE_DEF);
            mSpikesBody.setTransform(spikeSpawn / 32, 100 / 32 - 30, 0);
            mSpikesBody.setUserData("spikes");
            rectangle.attachChild(spikes);
        }else{
            //Spawn rifle

        }
        float r = 4 % (i + 1) ;
        if((i + 1) % 4 != 0){
            //Spawn balloon
            PixelPerfectSprite mine = new PixelPerfectSprite(spikeSpawn+700, 200, resourcesManager.mine, vbom){
                @Override
                protected void onManagedUpdate(float pSecondsElapsed) {
                    if(bob.collidesWith(this)){
                        boost();
                       // body.applyForce(600,200,1f,1f);
                    }
                }
            };

            mine.setHeight(70);
            mine.setWidth(70);
            mine.setCullingEnabled(true);

            Body mMineBody = PhysicsFactory.createBoxBody(world, mine, BodyDef.BodyType.KinematicBody, MINE_FIXTURE_DEF);
            mMineBody.setTransform(spikeSpawn+700/32, 100/32, 0);
            mMineBody.setMassData(new MassData());
            mMineBody.setUserData("mine");
            rectangle.attachChild(mine);
        }else{
            //Spawn volcano
            PixelPerfectSprite volcano = new PixelPerfectSprite(spikeSpawn+300, 100, resourcesManager.volcano, vbom){
                @Override
                protected void onManagedUpdate(float pSecondsElapsed) {
                    if(bob.collidesWith(this)){
                       killed(false);
                    }
                }
            };

            volcano.setHeight(100);
            volcano.setWidth(90);
            volcano.setCullingEnabled(true);
            volcano.setZIndex(2);

            Body mVolcanoBody = PhysicsFactory.createBoxBody(world, volcano, BodyDef.BodyType.KinematicBody, MINE_FIXTURE_DEF);
            mVolcanoBody.setTransform(spikeSpawn+300/32, 100/32, 0);
            mVolcanoBody.setMassData(new MassData());
            mVolcanoBody.setUserData("volcano");
            rectangle.attachChild(volcano);
        }

        final PixelPerfectSprite grenade = new PixelPerfectSprite(grenadeSpawn, 100, resourcesManager.grenade, vbom){
            @Override
            protected void onManagedUpdate(float pSecondsElapsed) {
                if(bob.collidesWith(this)){
                    boost();
                }
            }
        };
        grenade.setHeight(45);
        grenade.setWidth(45);
        grenade.setCullingEnabled(true);

        mGrenadeBody = PhysicsFactory.createBoxBody(world, grenade, BodyDef.BodyType.StaticBody, FIXTURE_DEF);
        mGrenadeBody.setUserData("grenade");

        rectangle.attachChild(grenade);
        sortChildren();

    }

    ////////////////////////
    //
    // Mine/Grenade Boost
    //
    ////////////////////////
    private void boost(){

        Vector2 velocities = body.getLinearVelocity();

        float xvelocity = velocities.x;
        float yvelocity = velocities.y;

        // Make the vectoy
        float newx;
        float newy;

        // Set Y Velocity
        if (yvelocity < 100){
            newy = 100;
        }
        if (yvelocity < 200) {
            newy = 150;
        }else if (yvelocity < 300){
            newy = 300;
        } else if (yvelocity < 500){
            newy = 400;
        } else if (yvelocity < 700){
            newy = 400;
        }
        else if (yvelocity < 100){
            newy = 500;
        }
        else if (yvelocity < 1200){
            newy = 600;
        }
        else if (yvelocity < 1400){
            newy = 800;
        }
        else if (yvelocity < 2000){
            newy = 1000;
        }
        else if (yvelocity < 2500){
            newy = 1300;
        }
        else if (yvelocity < 3000){
            newy = 1600;
        }
        else if (yvelocity < 3600){
            newy = 2000;
        }
        else if (yvelocity < 4300){
            newy = 2100;
        }
        else if (yvelocity < 5000){
            newy = 2400;
        }
        else newy = 2500;

        // Set X Velocity
        if (xvelocity < 100){
            newx = 250;
        }
        if (xvelocity < 200) {
            newx = 300;
        }else if (xvelocity < 300){
            newx = 400;
        } else if (xvelocity < 500){
            newx = 600;
        } else if (xvelocity < 700){
            newx = 800;
        }
        else if (xvelocity < 1000){
            newx = 900;
        }
        else if (xvelocity < 1400){
            newx = 1350;
        }
        else if (xvelocity < 1800){
            newx = 1750;
        }
        else if (xvelocity < 2200){
            newx = 2200;
        }
        else if (xvelocity < 3000){
            newx = 3000;
        }
        else if (xvelocity < 4000){
            newx = 3750;
        }
        else if (xvelocity < 5000){
            newx = 4250;
        }
        else if (xvelocity < 6000){
            newx = 4750;
        }
        else if (xvelocity < 7000){
            newx = 5250;
        }
        else if (xvelocity < 8000){
            newx = 5500;
        }
        else newx = 6000;

        body.setLinearVelocity(newx, newy);
        bob.setRotation(10f);
    }

    ///////////////////
    //
    // Tap To Reset
    //
    ///////////////////

    private void resetGame(){
        //Reset player world position
        //Reset score
        //Remove game over text/buttons
        //Reset cannon position
        //Rest powerbar
        gameHUD.detachChild(gameOverText);
        gameHUD.detachChild(restartText);

        SceneManager.getInstance().resetScene();
        blood.setVisible(false);

        /*bob.setPosition(mFirePositionX, mFirePositionY );
        body.setTransform(mFirePositionX / 32, mFirePositionY / 32, 0);
        hasFired = false;

        mPowerBar.setProgress(0f);
        blood.setVisible(false);*/


    }


    ///////////////////
    //
    // Create Font
    //
    ///////////////////

    private void createFont(){
        // CREATE GAME OVER
        gameOverText = new Text(camera.getWidth()/2, camera.getHeight()/2 + 100, resourcesManager.gameFont, GAME_OVER_TEXT, new TextOptions(HorizontalAlign.LEFT), vbom);
        //gameOverText.setAnchorCenter(0, 0);
        gameOverText.setText(GAME_OVER_TEXT);
        gameOverText.setVisible(false);

        restartText = new Text(camera.getWidth()/2, camera.getHeight()/2 + 60, resourcesManager.restartFont, TAPE_TO_RESET_TEXT, new TextOptions(HorizontalAlign.LEFT), vbom);
        restartText.setText(TAPE_TO_RESET_TEXT);
        restartText.setVisible(false);

        scoreText = new Text(camera.getWidth()-50, camera.getHeight()-50, resourcesManager.scoreFont, SCORE_TEXT, new TextOptions(HorizontalAlign.LEFT), vbom);
        scoreText.setText("0");
    }

    ///////////////////
    //
    // Bob is killed
    //
    ///////////////////

    private void killed(boolean showBlood){
        isMoving = false;
        gameOver = true;

        gameOverText.setVisible(true);
        restartText.setVisible(true);

        body.setLinearVelocity(0, 0);
        PhysicsConnector connector = world.getPhysicsConnectorManager().findPhysicsConnectorByShape(bob);
        world.unregisterPhysicsConnector(connector);

        if(showBlood){
            blood.setVisible(true);
        }
    }

    /////////////////
    //
    // Update Score
    //
    /////////////////

    private void updateScore(){
        scoreText.setText(Math.round(body.getPosition().x)+"");
    }


}
