import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The class is for reading .csv file and transfer the format of the data
 * 
 * @author 180007800
 *
 */
public class ReadFile {
	// Read data with BufferedReader and put the data into matrixes
	public static double[][] getDoubleData(String fileName) throws IOException {
		BufferedReader file = new BufferedReader(new FileReader(fileName));
		List<String> line = new ArrayList<>();
		String row = file.readLine();
		while (row != null) {
			line.add(row);
			row = file.readLine();
		}
		file.close();
		int s = line.size();
		double[][] matrix = new double[s][];
		for (int i = 0; i < s; i++) {
			matrix[i] = doubleTransfer(line.get(i));
		}
		return matrix;
	}

	public static int[][] getIntData(String fileName) throws IOException {
		BufferedReader file = new BufferedReader(new FileReader(fileName));
		String row = file.readLine();
		List<String> line = new ArrayList<>();
		while (row != null) {
			line.add(row);
			row = file.readLine();
		}
		file.close();
		int s = line.size();
		int[][] matrix = new int[s][];
		for (int i = 0; i < s; i++) {
			matrix[i] = intTransfer(line.get(i));
		}
		return matrix;
	}

	// Transfer the format of the data
	private static int[] intTransfer(String line) {
		// TODO Auto-generated method stub
		String[] column = null;
		column = line.split(",");
		int len = column.length;
		int[] res = new int[len];
		for (int i = 0; i < len; i++) {
			try {
				res[i] = Integer.parseInt(column[i]);
			} catch (Exception e) {
				System.out.println("No int value");
				res[i] = 0;
			}
		}
		return res;
	}

	private static double[] doubleTransfer(String line) {
		// TODO Auto-generated method stub
		String[] column = null;
		column = line.split(",");
		int len = column.length;
		double[] res = new double[len];
		for (int i = 0; i < len; i++) {
			try {
				res[i] = Double.parseDouble(column[i]);
			} catch (Exception e) {
				System.out.println("No double value");
				res[i] = 0;
			}
		}
		return res;
	}
}