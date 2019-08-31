
/**
 * The class is for colors and weights of vertices
 * 
 * @author 180007800
 *
 */
public class Weight {
	public double wei1;
	public double wei2;
	public double wei3;

	public Weight(double wei1, double wei2, double wei3) {
		this.wei1 = wei1;
		this.wei2 = wei2;
		this.wei3 = wei3;
	}

	// Do add, multiple, divide operations for weight

	public Weight multip(double scal) {
		this.wei1 *= scal;
		this.wei2 *= scal;
		this.wei3 *= scal;
		return this;
	}

	public Weight addition(Weight Wei) {
		this.wei1 += Wei.wei1;
		this.wei2 += Wei.wei2;
		this.wei3 += Wei.wei3;
		return this;
	}

	public Weight divide(double para) {
		this.wei1 /= para;
		this.wei2 /= para;
		this.wei3 /= para;
		return this;
	}

	public Weight cuplicate() {
		return new Weight(this.wei1, this.wei2, this.wei3);
	}
}