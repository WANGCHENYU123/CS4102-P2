import processing.core.PApplet;
import processing.core.PVector;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * The class is storing color, weight and vertics of triangles
 * 
 * @author 180007800
 *
 */
public class Triangle implements Comparable<Triangle> {
	public Weight col1;
	public Weight col2;
	public Weight col3;
	public PVector centerPoint;
	public PVector point1;
	public PVector point2;
	public PVector point3;

	public Triangle(PVector point1, PVector point2, PVector point3) {
		this.point1 = point1;
		this.point2 = point2;
		this.point3 = point3;
	}

	public Triangle(PVector point1, PVector point2, PVector point3, Weight col1, Weight col2, Weight col3) {
		this.point1 = point1;
		this.point2 = point2;
		this.point3 = point3;
		this.col1 = col1;
		this.col2 = col2;
		this.col3 = col3;
		// center point of the triangle
		this.centerPoint = point1.copy().add(point2).add(point3).mult(0.3333f);
	}

	@Override
	public int compareTo(Triangle tri) {
		float d = centerPoint.z - tri.centerPoint.z;
		if (d > 0) {
			return 1;
		} else if (d < 0) {
			return -1;
		} else
			return 0;
	}

	// create a triangle in the screen
	public static Triangle createTriangle(int width, int height, float para) {
		// TODO Auto-generated method stub
		PVector p1;
		PVector p2;
		PVector p3;
		Triangle tri;
		p1 = new PVector(-1, 1);
		p2 = new PVector(1, 1);
		p3 = new PVector(1, -1);
		tri = new Triangle(p1, p2, p3, new Weight(10, 0, 0), new Weight(0, 10, 0), new Weight(0, 0, 10));
		tri.mulPara(para);
		tri.move(new PVector(width / 2.0f, height / 2.0f));
		return tri;
	}

	// Add points and colors from the other trangles to the triangle for creating a
	// face
	public Triangle render(Triangle othertri, double shWeight, double txWeight) {
		col1.addition(othertri.col1.cuplicate().multip((float) txWeight));
		col2.addition(othertri.col2.cuplicate().multip((float) txWeight));
		col3.addition(othertri.col3.cuplicate().multip((float) txWeight));
		point1.add(othertri.point1.copy().mult((float) shWeight));
		point2.add(othertri.point2.copy().mult((float) shWeight));
		point3.add(othertri.point3.copy().mult((float) shWeight));
		return this;
	}

	// copy the triangle
	public Triangle cuplicate() {
		return new Triangle(point1.copy(), point2.copy(), point3.copy(), col1.cuplicate(), col2.cuplicate(),
				col3.cuplicate());
	}

	// Get triangle's normal vector
	public PVector getNorVector() {
		// TODO Auto-generated method stub
		PVector value = point1.copy().mult(-1);
		PVector normal = null;
		PVector v1;
		PVector v2;
		v1 = point2.copy().add(value);
		v2 = point3.copy().add(value);
		normal = v1.cross(v2);
		return normal.normalize();
	}

	public Weight getLamberColor(PVector inLight, double inLightIntensity, double diffCoef) {
		// Get the average color of the triangle
		Weight avgColor = col1.cuplicate().addition(col2).addition(col3).multip(0.3333);
		return getLamberColor(inLight, inLightIntensity, diffCoef, avgColor);
	}

	// Calculating the color of point with Lambert's illumination model
	public Weight getLamberColor(PVector inLight, double inLightIntensity, double diffCoef, Weight col) {
		PVector normal = getNorVector();
		double res;
		res = inLight.normalize().dot(normal) * inLightIntensity * diffCoef;
		return col.multip(res);
	}

	// Calculate three vertices'color with Lambert's illumination model.
	// Calculate the normals
	public Weight[] getLamberColorGradient(PVector inLight, double inLightIntensity, double diffCoef,
			HashMap<PVector, PVector> map) {
		Weight color1;
		Weight color2;
		Weight color3;
		double res1;
		double res2;
		double res3;
		color1 = col1.cuplicate();
		color2 = col2.cuplicate();
		color3 = col3.cuplicate();
		// Id =(l⋅n)IiK
		res1 = inLight.normalize().dot(map.get(point1)) * inLightIntensity * diffCoef;
		res2 = inLight.normalize().dot(map.get(point2)) * inLightIntensity * diffCoef;
		res3 = inLight.normalize().dot(map.get(point3)) * inLightIntensity * diffCoef;
		return new Weight[] { color1.multip(res1), color2.multip(res2), color3.multip(res3) };
	}

	// draw the triangle
	public void drawTriangle(PApplet p) {
		p.stroke(100);
		p.noFill();
		p.triangle(point1.x, point1.y, point2.x, point2.y, point3.x, point3.y);
	}

	// use gradient shading to draw triangle, insert colors among verties
	public void drawTriGradient(PApplet p, PVector inLight, double inLightIntensity, double diffCoef,
			HashMap<PVector, PVector> map) {
		Weight[] c = getLamberColorGradient(inLight, inLightIntensity, diffCoef, map);
		p.beginShape();
		// every vertex's normal is same with the triangle
		// draw
		p.stroke(((float) c[0].wei1), ((float) c[0].wei2), ((float) c[0].wei3));
		p.fill(((float) c[0].wei1), ((float) c[0].wei2), ((float) c[0].wei3));
		p.vertex(point1.x, point1.y);

		p.stroke(((float) c[1].wei1), ((float) c[1].wei2), ((float) c[1].wei3));
		p.fill(((float) c[1].wei1), ((float) c[1].wei2), ((float) c[1].wei3));
		p.vertex(point2.x, point2.y);

		p.stroke(((float) c[2].wei1), ((float) c[2].wei2), ((float) c[2].wei3));
		p.fill(((float) c[2].wei1), ((float) c[2].wei2), ((float) c[2].wei3));
		p.vertex(point3.x, point3.y);
		p.endShape();
	}

	// is the point in the triangle?
	public boolean PointInTriangle(PVector p) {
		float dis1, dis2, dis3;
		boolean no, yes;
		dis1 = (p.x - point2.x) * (point1.y - point2.y) - (point1.x - point2.x) * (p.y - point2.y);
		dis2 = (p.x - point3.x) * (point2.y - point3.y) - (point2.x - point3.x) * (p.y - point3.y);
		dis3 = (p.x - point1.x) * (point3.y - point1.y) - (point3.x - point1.x) * (p.y - point1.y);
		no = (dis1 < 0) || (dis2 < 0) || (dis3 < 0);
		yes = (dis1 > 0) || (dis2 > 0) || (dis3 > 0);
		return !(no && yes);
	}

	// calculate the weight of the clicked position(Barycentric coordinates)
	public Weight calPosWeight(PVector p, double wei1, double wei2, double wei3) {
		double[] res = calWeights(p);
		Weight result = new Weight(res[0] * wei1, res[1] * wei2, res[2] * wei3);
		return result.divide(res[0] + res[1] + res[2]);
	}

	private double[] calWeights(PVector p) {
		// TODO Auto-generated method stub
		double wei1;
		double wei2;
		double wei3;
		wei1 = ((point2.y - point3.y) * (p.x - point3.x) + (point3.x - point2.x) * (p.y - point3.y))
				/ ((point2.y - point3.y) * (point1.x - point3.x) + (point3.x - point2.x) * (point1.y - point3.y));
		wei2 = ((point3.y - point1.y) * (p.x - point3.x) + (point1.x - point3.x) * (p.y - point3.y))
				/ ((point2.y - point3.y) * (point1.x - point3.x) + (point3.x - point2.x) * (point1.y - point3.y));
		wei3 = (1 - wei1) - wei2;

		return new double[] { wei1, wei2, wei3 };
	}

	// calculate normalised normal for vertex
	public static PVector calNor(Set<Triangle> tri) {
		// TODO Auto-generated method stub
		PVector res;
		res = new PVector(0, 0, 0);
		for (Triangle tr : tri) {
			res.add(tr.getNorVector());
		}
		return res.normalize();
	}

	// scaling the triangle
	public Triangle mulPara(float para) {
		point1.mult(para);
		point2.mult(para);
		point3.mult(para);
		return this;
	}

	// moving the triangle
	public Triangle move(PVector para) {
		point1.add(para);
		point2.add(para);
		point3.add(para);
		return this;
	}

	public Triangle mul() {
		point1.y *= -1;
		point2.y *= -1;
		point3.y *= -1;
		return this;
	}

}