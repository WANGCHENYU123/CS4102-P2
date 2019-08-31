import processing.core.PApplet;
import processing.core.PVector;

import java.io.IOException;
import java.util.*;

/**
 * The class is for representing face
 * 
 * @author 180007800
 *
 */
public class Face {
	private List<Triangle> tri;
	private List<PVector> inPos = new ArrayList<>();
	private HashMap<PVector, Set<Triangle>> pointsTri = new HashMap<>();
	private HashMap<PVector, PVector> map;
	private double shWei = -1;
	private double txWei = -1;

	public Face(double shWei, double txWei) {
		tri = new ArrayList<>();
		this.shWei = shWei;
		this.txWei = txWei;
	}

	public Face(int[][] mesh, String shFile, String txFile, double shWei, double txWei) throws IOException {
		double[][] matrix1 = ReadFile.getDoubleData(shFile);
		double[][] matrix2 = ReadFile.getDoubleData(txFile);
		tri = new ArrayList<>();
		Triangle tr;
		this.shWei = shWei;
		this.txWei = txWei;
		int len = mesh.length;
		for (int i = 0; i < len; i++) {
			PVector[] p = new PVector[3];
			Weight[] col = new Weight[3];
			int mlen = mesh[0].length;
			for (int column = 0; column < mlen; column++) {
				int m = mesh[i][column];
				int row = m - 1;
				col[column] = new Weight(matrix2[row][0], matrix2[row][1], matrix2[row][2]).multip(txWei);
				p[column] = new PVector(((float) matrix1[row][0]), ((float) matrix1[row][1]), ((float) matrix1[row][2]))
						.mult((float) shWei);
			}
			tr = new Triangle(p[0], p[1], p[2], col[0], col[1], col[2]);
			tri.add(tr);
		}
	}

	public Face(int[][] mesh, String shFile, String txFile) throws IOException {
		tri = new ArrayList<>();
		Triangle tr;
		double[][] matrix1;
		double[][] matrix2;
		matrix1 = ReadFile.getDoubleData(shFile);
		matrix2 = ReadFile.getDoubleData(txFile);
		HashSet<PVector> poiset = new HashSet<>();
		// get points and clolors
		int len = mesh.length;
		for (int i = 0; i < len; i++) {
			PVector[] p = new PVector[3];
			Weight[] col = new Weight[3];
			int mlen = mesh[0].length;
			for (int column = 0; column < mlen; column++) {
				int row;
				int m = mesh[i][column];
				row = m - 1;
				col[column] = new Weight(matrix2[row][0], matrix2[row][1], matrix2[row][2]);
				p[column] = new PVector(((float) matrix1[row][0]), ((float) matrix1[row][1]),
						((float) matrix1[row][2]));
			}
			tr = new Triangle(p[0], p[1], p[2], col[0], col[1], col[2]);
			tri.add(tr);
		}
		inPos = new ArrayList<>(poiset);
	}

	public Face mix(Face other, double shWei, double txWei) {
		int s = tri.size();
		for (int i = 0; i < s; i++) {
			tri.get(i).render(other.getTri().get(i), shWei, txWei);
		}
		return this;
	}

	// mix a triangle with other triangles
	private void mixTriangles(PVector p, Triangle tri) {
		if (!pointsTri.containsKey(p)) {
			pointsTri.put(p, new HashSet<>());
		}
		pointsTri.get(p).add(tri);
	}

	public void drawFace(PApplet p) {
		for (Triangle tr : tri) {
			tr.drawTriGradient(p, new PVector(0, 0, 1), 3, 0.3, map);
		}
	}

	// map normals for vertices, put into hashtable
	private HashMap<PVector, PVector> norMap() {
		HashMap<PVector, PVector> normMap = new HashMap<>();
		for (PVector p : pointsTri.keySet()) {
			normMap.put(p, Triangle.calNor(pointsTri.get(p)));
		}

		return normMap;
	}

	// get the normal of vertices
	public void regetNor() {
		for (Triangle tr : tri) {
			mixTriangles(tr.point1, tr);
			mixTriangles(tr.point2, tr);
			mixTriangles(tr.point3, tr);
		}
		map = norMap();
	}

	public Face cuplicate() {
		Face cup = new Face(this.shWei, this.txWei);
		for (Triangle ti : this.tri) {
			cup.tri.add(ti.cuplicate());
		}
		cup.pointsTri = pointsTri;
		return cup;
	}

	public void size(float para) {
		for (Triangle tr : tri) {
			tr.mulPara(para);
		}
	}

	public void mul() {
		for (Triangle t : tri) {
			t.mul();
		}
	}

	public void move(PVector val) {
		for (Triangle tr : tri) {
			tr.move(val);
		}
	}

	public List<Triangle> getTri() {
		return tri;
	}

}