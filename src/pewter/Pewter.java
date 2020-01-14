package pewter;


//import java.rmi.activation.ActivationException;
//import java.lang.*;
import processing.core.*;

/**
 * (the tag example followed by the name of an example included in folder 'examples' will
 * automatically include the example in the javadoc.)
 *
 * @example Hello 
 */

public class Pewter {
	
	// myParent is a reference to the parent sketch
	PApplet POwner;
	private static Pewter single_instance = null;
	private static gameInstance game = null;
	//private static physicsManager physicsM = null;
	private long prevMills = System.currentTimeMillis();
	private static double deltaSeconds = -1;
	static float scaleFactor = 100; //number of pixels per world unit
	public static boolean enableDebugLoging = false;
	public static boolean enableErrorLoging = true;
	public static boolean enableStandardLoging = false;
	public static boolean enableDebugDrawing = false;
	private Pewter(PApplet t) {
		POwner = t;
		log("Pre create Game Instance");
		game = new gameInstance(POwner);
		//physicsM = physicsManager.getSingleton();
		prevMills = System.currentTimeMillis();
	}
	public static Pewter getSingleton(){
		if (single_instance != null) {
			return single_instance;
		}
		return null;
	}
	public static void init(PApplet t){
		if (single_instance == null) {
			single_instance = new Pewter(t);
			log("Initialized");
		}
	}
	public static gameInstance get_gameInstance() {
		return game;
	}
	/*
	 * This is the main update function of the library.
	 * this should be called as the second line of draw() in Processing (directly after a background() call)
	 */
	void updateInteranl() {
		log("update began");
		if (deltaSeconds != -1) {
			long currentMills = System.currentTimeMillis();
			deltaSeconds = (double)(currentMills-prevMills)/1000.0;
			prevMills = currentMills;
		} else {
			deltaSeconds = 0;
		}
		//log("Update calculated deltaSeconds");
		if (game != null) {
			
			game.update();
			
			//log("update finished");
		}else {
			err("Game Instance not initialized!");
		}
		
	}
	public static void update() {
		getSingleton().updateInteranl();
	}
	public static double getWorldDeltaSeconds() { //this is a SUPER IMPORTANT function, as it lets us dynamically alter the phsycis step based on the FPS of the app
		return deltaSeconds;
	}
	public static void setPixelsPerUnit(float p) {
		scaleFactor = p;
	}
	public static float getPixelsPerUnit() {
		return scaleFactor;
	}
	public static float getPixelOne() {
		return 1/scaleFactor;
	}
	public static void log(String s) {
		if(enableStandardLoging) {
			PApplet.println("LOG: Pewter: "+s);
		}
	}
	public static void err(String s) {
		if(enableErrorLoging) {
		//PApplet.println("ERROR: Pewtrt: "+s);
			System.err.println("ERROR: Pewter: "+s);
		}
	}
	public static void debug(String s) {
		if (enableDebugLoging) {
			PApplet.println("DEBUG: Pewter: "+s);
		}
	}
	public static Vec2 toWorld(Vec2 o) {
		Pewter p = Pewter.getSingleton();
		return o.mult(1, -1).sub(p.POwner.width/2, -p.POwner.height/2).div(scaleFactor);
	}
	public static Vec2 toScreen(Vec2 o) {
		Pewter p = Pewter.getSingleton();
		return o.mult(1, -1).add(p.POwner.width/2, -p.POwner.height/2).mult(scaleFactor);
	}
}

