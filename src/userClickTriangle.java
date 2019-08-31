import processing.core.PApplet;
import processing.core.PVector;

/**
 * The class is for mouse click and draw the triangle on the screen
 * 
 * @author 180007800
 *
 */
public class userClickTriangle {
	private Triangle tri;
	private Weight finalWei;

	public userClickTriangle(int width, int height, float scale) {
		this.tri = Triangle.createTriangle(width, height, scale);
		this.finalWei = new Weight(0.4, 0.3, 0.3);
	}

	public void drawTriangle(PApplet p) {
		tri.drawTriangle(p);
	}

	// calculate the weights of the clicked point
	public Weight onClick(PApplet p) {
		PVector mouse;
		mouse = new PVector(p.mouseX, p.mouseY);
		// if the clicked point is in the triangle
		if (tri.PointInTriangle(mouse)) {
			finalWei = tri.calPosWeight(mouse, 1, 1, 1);
		}
		return finalWei;
	}
}