package pewter;
import processing.core.*;

public class gameInstance {
	protected PApplet POwner;
	protected PWorld loaded_world = null;
	gameInstance(PApplet o){
		if (Pewter.get_gameInstance() == null) {
			POwner = o;
		} else {
			Pewter.err("Gameinstance: Should not create a second gameInstance!. Let Pewter automatically create the gameinstance for you!");
		}
		load_world(new PWorld(POwner));
	}
	public PWorld get_loaded_world() {
		return loaded_world;
	}
	public boolean load_world(PWorld w) {
		if (w != null) {
			if (loaded_world != null){
				loaded_world.unload();
			}
			physicsManager.getSingleton().killAllBodies();
			loaded_world = w;
			loaded_world.load();
			return true;
		}else {return false;}
	}
	void update() {
		//Pewter.log("Gaminstance: update began");
		if (loaded_world != null) {
			POwner.pushMatrix();
			POwner.translate(POwner.width/2, POwner.height/2);
			loaded_world.update();
			POwner.popMatrix();
		}else {
			Pewter.err("Gaminstance: No world loaded");
		}
		//Pewter.log("Gaminstance: update ended");
	}
}
