package com.adsg0186.shapemergency;

/*
import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
*/


import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.util.Log;

import com.adsg0186.shapemergency.testgame1.FiringGameTest;
import com.adsg0186.shapemergency.testgame1.config.GameConfig;
import com.adsg0186.shapemergency.testgame1.config.SavedGame;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.github.adsgray.gdxtry1.engine.*;
import com.github.adsgray.gdxtry1.engine.input.DefaultDirectionListener;
import com.github.adsgray.gdxtry1.engine.input.DragAndFlingDirectionListener;
import com.github.adsgray.gdxtry1.engine.input.SimpleDirectionGestureDetector;
import com.github.adsgray.gdxtry1.engine.input.SimpleDirectionGestureDetector.DirectionListener;
import com.github.adsgray.gdxtry1.engine.output.Renderer;
import com.github.adsgray.gdxtry1.engine.output.SoundIF;
import com.github.adsgray.gdxtry1.engine.output.SoundPoolPlayer;
import com.github.adsgray.gdxtry1.engine.util.Game;
import com.github.adsgray.gdxtry1.engine.util.GameCommand;
import com.github.adsgray.gdxtry1.engine.util.GameFactory;
import com.github.adsgray.gdxtry1.engine.util.HighScoreSaveIF;
import com.github.adsgray.gdxtry1.engine.util.LocalHighScore;
import com.github.adsgray.gdxtry1.engine.util.WorldTickTask;

public class GameScreen implements ApplicationListener {

    private static final int CAMERA_WIDTH = 800;
	private static final int CAMERA_HEIGHT = 1422;
	private static final int numBlobs = 10;

	private WorldIF world;
	private Renderer renderConfig;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private ShapeRenderer shapes;
	private Timer worldTimer;
	private TimerTask worldTick;
	private GameFinished gameFinished; // executed by the Game when it is complete
	private GameCommand exitGame; // called by this ApplicationListener to go back to menu screen
    private HighScoreSaveIF highScore;
	Game game;
	protected int difficultyLevel = 1;
	Context context;
	
	private class GameFinished implements GameCommand {
	    protected Game game;
	    
	    public void setGame(Game game) { this.game = game; }

        @Override public void execute(int score) {
            Log.d("trace", "Game finished");
            game.stop();
            SavedGame.get().clearSavedGame();

            // make a funciton that maps from difficulty level to score string
            // used here and in score display activity/view

            highScore.submitScore(String.format("high_score_%d", GameConfig.get().getDifficultyLevel()), score);
            exitGame.execute(score);
        }
	}
	

	public GameScreen(Context context, GameCommand exitGame, int difficultyLevel) {
	    super();
	    this.context = context;
	    this.exitGame = exitGame;
	    this.difficultyLevel = difficultyLevel;
	}
	
	private void populateWorld() {
	    //GameFactory.populateWorldWithBlobs(world, numBlobs, renderConfig);
	    //GameFactory.populateWorldNonRandom(world, renderConfig);
	    //GameFactory.populateWorldNonRandomBlobSet(world, renderConfig);
	    //GameFactory.populateWorldLaunchUp(world, renderConfig);
	    //GameFactory.populateWorldOoze(world, renderConfig);
	    //GameFactory.populateWorldCollisionTest(world, renderConfig);
	    //GameFactory.populateWorldTestTriggers(world, renderConfig);
	    //GameFactory.populateWorldTestTriggersAgain(world, renderConfig);
	    //GameFactory.populateWorldGameTestOne(world, renderConfig);
	    //GameFactory.populateWorldTestOffsetPosition(world, renderConfig);
	    //GameFactory.populateWorldTestBumpAccel(world, renderConfig);
	    //GameFactory.populateWorldTestNewBlobSet(world, renderConfig);
	    //GameFactory.populateWorldTestTriangle(world,  renderConfig);
	    //GameFactory.populateWorldTestMultiplyPosition(world,  renderConfig);
	    GameFactory.populateWorldTestText(world, renderConfig);
	}

	// make a DirectionListener that can affect the world
	private class TestDirectionListener extends DefaultDirectionListener {
	    protected WorldIF world;
	    public TestDirectionListener(WorldIF world) {
	        this.world = world;
	    }
	        
	    @Override
	    public void onUp(DirectionListener.FlingInfo f) {
	        super.onUp(f);
	        // velocities are "backwards"
	        if (f.startY < 200 && f.velocityY < -800) {
	            populateWorld();
	        }
	    }

	    @Override
	    public void onDown(DirectionListener.FlingInfo f) {
	        Log.d("input", String.format("screen swiped DOWN start(%f,%f) vel(%f,%f)", f.startX, f.startY, f.velocityX, f.velocityY));
	        if (f.startY > 1000) {
	            world.killAllBlobs();
	        }
	    }
	}	

	// amazingly, yes, in resume() we need an entirely new
	// Timer and Task. You can't just create a new timer and put
	// the old task on it. That's an exception.
	// 01-04 16:29:19.190: E/AndroidRuntime(14979): java.lang.IllegalStateException: TimerTask is scheduled already
	private void startWorldTicker() {
	    // create timer task that will call tick on world every 25 ms
	    worldTimer = new Timer("worldTickTimer");
	    worldTick = WorldTickTask.createInstance(world);
	    worldTimer.scheduleAtFixedRate(worldTick, 0, 25);
	}

	@Override
	public void create() {
	    shapes = new ShapeRenderer();
		batch = new SpriteBatch();
		gameFinished = new GameFinished();
        highScore = LocalHighScore.get();
		
		Log.d("trace", "in gamescreen create");

		Renderer.createRealInstance(shapes, batch);
	    renderConfig = Renderer.getRealInstance();
	    world = GameFactory.defaultWorld();
	    
	    Gdx.graphics.setContinuousRendering(false);
	    startWorldTicker();
	   
		camera = new OrthographicCamera();
		camera.setToOrtho(false, CAMERA_WIDTH, CAMERA_HEIGHT); // the camera is like a window into our game world
		
		// Setup swipe/touch handling:
		// The SimpleDirectionGestureDetector processes events, mangles the coordinates
		// so that they're in relation to the camera, and fires the events in
		// the DirecitonListener

		//DirectionListener dl = new TestDirectionListener(world);
	    //populateWorld();

		DirectionListener dl = new DragAndFlingDirectionListener();
		game = new FiringGameTest((DragAndFlingDirectionListener)dl, world, renderConfig, context, gameFinished);
		gameFinished.setGame(game);
		game.init();
		//GameCommand toggleSound = game.getSoundToggle(); // get sound toggler command
		GameCommand difficulty = game.getDifficultySetter();
		difficulty.execute(difficultyLevel); // 0 = easy, 1 = normal, 2 = insane
		//toggleSound.execute(1); // enable sound
		game.start();

		Gdx.input.setInputProcessor(new SimpleDirectionGestureDetector(camera, dl));
		//Gdx.input.setCatchBackKey(true);
	}
	

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
        Log.d("trace", "GameScreen dispose");
        shapes.dispose();
        batch.dispose();
        Renderer.get().dispose();
    }

    @Override
    public void pause() {
        worldTick.cancel();
        worldTimer.cancel();
        worldTimer.purge();
        worldTimer = null;
        Log.d("trace", "GameScreen pause");
        /*
         * stash game state somewhere
         * score
         * blobs
         * etc
         */
        game.save();
    }

	protected long millisOfLastTick = 0;
	//protected long milliDelta = 33; // about 30 ticks per second?
	protected long milliDelta = 25; // 40 ticks per second

	@Override
	public void render() {
	    
	    // TODO: config class for colours
	    //Gdx.gl.glClearColor(0.199f, 0.398f, 0.598f, 0.4f);	// OpenGL code to make the screen blue
	    Gdx.gl.glClearColor(0f, 0f, 0f, 0.4f);	
	    Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);			// OpenGL code to clear the screen
	    
	    // only tick 30 times per second
	    /*
	    long curMillis = System.currentTimeMillis();
	    if (curMillis - millisOfLastTick >= milliDelta) {
	        millisOfLastTick = curMillis;
	        world.tick();
	    }
	    */

	    // but we have to render all the time
	    camera.update();
	    batch.setProjectionMatrix(camera.combined);
	    shapes.setProjectionMatrix(camera.combined);
	    world.render();
	}

    @Override
    public void resize(int arg0, int arg1) {
        // TODO Auto-generated method stub
        Log.d("trace", "GameScreen resize");
        
    }

    @Override
    public void resume() {
        Log.d("trace", "GameScreen resume");
        if (worldTimer == null) {
            startWorldTicker();
        }
    }
    
}
