package com.e.manager;

import android.graphics.Color;

import com.e.activities.GameActivity;
import com.e.extension.collisions.opengl.texture.region.PixelPerfectTextureRegion;
import com.e.extension.collisions.opengl.texture.region.PixelPerfectTextureRegionFactory;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by erichorvat on 5/11/15.
 */
public class ResourcesManager {

    public ITextureRegion splash_region;

    //Define texture atlases
    private BitmapTextureAtlas splashTextureAtlas;
    private BitmapTextureAtlas menuTextureAtlas;
    private BitmapTextureAtlas mBobTextureAtlas;
    private BitmapTextureAtlas mGrenadeTextureAtlas;
    private BitmapTextureAtlas mSpikesTextureAtlas;
    private BitmapTextureAtlas mFireButtonTextureAtlas;
    private BitmapTextureAtlas mBloodTextureAtlas;
    private BitmapTextureAtlas mFloatingMine;
    private BitmapTextureAtlas mVolcano;

    //Define Texture Regions
    public PixelPerfectTextureRegion bob;
    public PixelPerfectTextureRegion tank;
    public PixelPerfectTextureRegion barrel;
    public PixelPerfectTextureRegion grenade;
    public PixelPerfectTextureRegion spikes;
    public PixelPerfectTextureRegion fire;
    public PixelPerfectTextureRegion blood;
    public PixelPerfectTextureRegion mine;
    public PixelPerfectTextureRegion volcano;

    //Define Fonts
    public Font gameFont;
    public Font restartFont;
    public Font scoreFont;

        //---------------------------------------------
        // VARIABLES
        //---------------------------------------------

        private static final ResourcesManager INSTANCE = new ResourcesManager();

        public Engine engine;
        public GameActivity activity;
        public SmoothCamera camera;
        public VertexBufferObjectManager vbom;


        //---------------------------------------------
        // TEXTURES & TEXTURE REGIONS
        //---------------------------------------------

        //---------------------------------------------
        // CLASS LOGIC
        //---------------------------------------------

    public void loadGameResources()
    {
        loadGameGraphics();
        loadMenuAudio();
        loadGameFonts();
    }

    private void loadGameGraphics()
    {
        FontFactory.setAssetBasePath("font/");
        PixelPerfectTextureRegionFactory.setAssetBasePath("gfx/");
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

        final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        final ITexture mainFontTexture1 = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        final ITexture mainFontTexture2 = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

        gameFont = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "americanTypewriter.ttf", 50, true, Color.BLACK, 2, Color.BLACK);
        gameFont.load();
        restartFont = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture1, activity.getAssets(), "americanTypewriter.ttf", 30, true, Color.parseColor("#1874CD"), 2, Color.parseColor("#1874CD"));
        restartFont.load();
        scoreFont = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture2, activity.getAssets(), "americanTypewriter.ttf", 30, true, Color.parseColor("#1874CD"), 2, Color.parseColor("#1874CD"));
        scoreFont.load();

        menuTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.NEAREST_PREMULTIPLYALPHA);
        mBobTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.DEFAULT);
        mGrenadeTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.DEFAULT);
        mSpikesTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.DEFAULT);
        mFireButtonTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.DEFAULT);
        mBloodTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.DEFAULT);
        mFloatingMine = new BitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.DEFAULT);
        mVolcano = new BitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.DEFAULT);

        bob = PixelPerfectTextureRegionFactory.createFromAsset(mBobTextureAtlas, activity.getAssets(), "bob2.png", 1, 1, false, 0);
        tank =PixelPerfectTextureRegionFactory.createFromAsset(menuTextureAtlas, activity.getAssets(), "tank.png", 1, 1, false, 0);
        barrel = PixelPerfectTextureRegionFactory.createFromAsset(menuTextureAtlas, activity.getAssets(), "barrel.png", 1, 1, false, 0);
        spikes = PixelPerfectTextureRegionFactory.createFromAsset(mSpikesTextureAtlas, activity.getAssets(), "spikes.png", 1, 1, false, 0);
        fire = PixelPerfectTextureRegionFactory.createFromAsset(mFireButtonTextureAtlas, activity.getAssets(), "fire.png", 1, 1, false, 0);
        grenade = PixelPerfectTextureRegionFactory.createFromAsset(mGrenadeTextureAtlas, activity.getAssets(), "grenade1.png", 1, 1, false,0);
        blood = PixelPerfectTextureRegionFactory.createFromAsset(mBloodTextureAtlas, activity.getAssets(), "blood.png", 1, 1, false,0);
        mine = PixelPerfectTextureRegionFactory.createFromAsset(mFloatingMine, activity.getAssets(), "floatingMine.png", 1, 1, false,0);
        volcano = PixelPerfectTextureRegionFactory.createFromAsset(mVolcano, activity.getAssets(), "volcano.png", 1, 1, false,0);

        this.menuTextureAtlas.load();
        this.mBobTextureAtlas.load();
        this.mGrenadeTextureAtlas.load();
        this.mSpikesTextureAtlas.load();
        this.mFireButtonTextureAtlas.load();
        this.mBloodTextureAtlas.load();
        this.mFloatingMine.load();
        this.mVolcano.load();



        /*BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        menuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 512, 1024, TextureOptions.BILINEAR);
        game_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "parallax_background_layer_back.png");
        //this.menuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
        this.menuTextureAtlas.load();*/

    }


    private void loadMenuAudio()
    {

    }

    private void loadGameFonts()
    {

    }

    private void loadGameAudio()
    {

    }

    public void loadSplashScreen()
    {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
        splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity, "face_box.png", 0, 0);
        splashTextureAtlas.load();

    }

    public void unloadSplashScreen()
    {
        splashTextureAtlas.unload();
        splash_region = null;
    }

    public void unloadGameScreen()
    {
        this.menuTextureAtlas.unload();
        this.mBobTextureAtlas.unload();
        this.mGrenadeTextureAtlas.unload();
        this.mSpikesTextureAtlas.unload();
        this.mFireButtonTextureAtlas.unload();
        this.mBloodTextureAtlas.unload();
        this.mFloatingMine.unload();
        this.mVolcano.unload();

        gameFont.unload();
        restartFont.unload();
        scoreFont.unload();
    }

    /**
     * @param engine
     * @param activity
     * @param camera
     * @param vbom
     * <br><br>
     * We use this method at beginning of game loading, to prepare Resources Manager properly,
     * setting all needed parameters, so we can latter access them from different classes (eg. scenes)
     */
    public static void prepareManager(Engine engine, GameActivity activity, SmoothCamera camera, VertexBufferObjectManager vbom)
    {
        getInstance().engine = engine;
        getInstance().activity = activity;
        getInstance().camera = camera;
        getInstance().vbom = vbom;
    }

    //---------------------------------------------
    // GETTERS AND SETTERS
    //---------------------------------------------

    public static ResourcesManager getInstance()
    {
        return INSTANCE;
    }


}
