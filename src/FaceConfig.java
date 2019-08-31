import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The class is for generating synthetic face
 * 
 * @author 180007800
 *
 */
public class FaceConfig {
	private Face avgFace;
	private int[][] mesh;
	private double[] shEv;
	private double[] txEv;
	private List<Face> face;

	public FaceConfig(String meshFile, String avgShFile, String avgTxFile, String shEvFile, String txEvFile)
			throws IOException {
		this.face = new ArrayList<>();
		this.shEv = transToArr(ReadFile.getDoubleData(shEvFile));
		this.txEv = transToArr(ReadFile.getDoubleData(txEvFile));
		this.mesh = ReadFile.getIntData(meshFile);
		this.avgFace = new Face(mesh, avgShFile, avgTxFile);
	}

	// combine faces
	public void comFace(String shFile, String txFile) throws IOException {
		double shWei;
		double txWei;
		shWei = getWeight(shFile, shEv) * 3;
		txWei = getWeight(txFile, txEv) * 5;
		this.face.add(new Face(mesh, shFile, txFile, shWei, txWei));
	}

	// 2d to 1d
	public double[] transToArr(double[][] item) {
		// TODO Auto-generated method stub
		int len = item.length;
		double[] arr;
		arr = new double[len];
		int len1 = item.length;
		for (int i = 0; i < len1; i++) {
			arr[i] = item[i][0];
		}
		return arr;
	}

	// get the synthetic face
	public Face synFace(Weight shWei, Weight txWei) {
		Face synFace = avgFace.cuplicate();
		synFace.mix(face.get(0), shWei.wei1, txWei.wei1);
		synFace.mix(face.get(1), shWei.wei2, txWei.wei2);
		synFace.mix(face.get(2), shWei.wei3, txWei.wei3);
		synFace.getTri().sort(null);
		return synFace;
	}

	// get the id of face from the file
	private double getWeight(String file, double[] item) {
		// TODO Auto-generated method stub
		String inx;
		String[] fs = file.split("_");
		inx = fs[1].split(".csv")[0];
		return item[Integer.parseInt(inx) - 1];
	}

}