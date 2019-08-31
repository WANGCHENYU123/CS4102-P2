
//import processing.core.*;
import processing.core.PApplet;
import processing.core.PVector;
import processing.opengl.PGraphics2D;
//import processing.opengl.*;

import java.io.IOException;
import java.util.HashMap;

/**
 * The class is for starting the project
 * 
 * @author 180007800
 *
 */
public class Main extends PApplet {
	private static final int WIDTH = 1000;
	private static final int HEIGHT = 800;
	private static Main instance = null;
	private String meshFile = "CS4102 2019 P2 data/mesh.csv";
	private String avgFaceShFile = "CS4102 2019 P2 data/sh_000.csv";
	private String avgFaceTxFile = "CS4102 2019 P2 data/tx_000.csv";
	private String shEVFile = "CS4102 2019 P2 data/sh_ev.csv";
	private String txEVFile = "CS4102 2019 P2 data/tx_ev.csv";
	private String face1ShFile = "CS4102 2019 P2 data/sh_001.csv";
	private String face1TxFile = "CS4102 2019 P2 data/tx_001.csv";
	private String face2ShFile = "CS4102 2019 P2 data/sh_100.csv";
	private String face2TxFile = "CS4102 2019 P2 data/tx_100.csv";
	private String face3ShFile = "CS4102 2019 P2 data/sh_199.csv";
	private String face3TxFile = "CS4102 2019 P2 data/tx_199.csv";
	private FaceConfig faceConfig;
	private Face face;
	private userClickTriangle clickTri;
	Weight posWeigths;

	public void setup() {
		try {
			// setting the size of the triangle, 300 is from experimenting
			clickTri = new userClickTriangle(WIDTH, HEIGHT, 300);
			faceConfig = new FaceConfig(meshFile, avgFaceShFile, avgFaceTxFile, shEVFile, txEVFile);
			faceConfig.comFace(face1ShFile, face1TxFile);
			faceConfig.comFace(face2ShFile, face2TxFile);
			faceConfig.comFace(face3ShFile, face3TxFile);
			posWeigths = new Weight(0.4, 0.3, 0.3);
			// the initial synthetic face
			face = faceConfig.synFace(posWeigths, posWeigths);
			face.mul();
			face.size(0.001f);
			// the position of initial face
			face.move(new PVector(WIDTH / 2f, HEIGHT / 3.5f));
			face.regetNor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Draw scene, the clickable triangle and the syntetic face
	public void draw() {
		background(255);
		clickTri.drawTriangle(this);
		face.drawFace(this);
		noLoop();
	}

	public static void main(String[] args) {
		String[] processingArgs = { "" };
		Main runClass = new Main();
		PApplet.runSketch(processingArgs, runClass);
	}

	// Set the size of the scene
	public void settings() {
		// size(WIDTH, HEIGHT);
		size(WIDTH, HEIGHT, P2D);
	}

	public static Main getInstance() {
		if (instance == null)
			instance = new Main();
		return instance;
	}

	@Override
	public void mousePressed() {
		Weight weight = clickTri.onClick(this);
		face = faceConfig.synFace(weight, weight);
		face.size(0.001f);
		face.mul();
		face.move(new PVector(WIDTH / 2f, HEIGHT / 3.5f));
		face.regetNor();
		redraw();
	}
}