package distanceFunction;

import java.util.List;

public class RatioVecDistance extends EntrywiseVecDistance {

	protected double factor;
	protected static final double eps0 = 1, eps1 = 1;

	public RatioVecDistance() {
		super();
		factor = 1;
	}

	public RatioVecDistance(EntryWeights weights) {
		super(weights);
		factor = 1;
	}

	@Override
	protected double entryDistance(int a, int b) {
		double c = factor * a + eps0;
		double d = b + eps1;
		return c / d + d / c - 2;
	}

	public void setFactor(int[] vec0, int[] vec1) {
		double ratioSum = 0;
		double invRatioSum = 0;

		for (int i = 0; i<vec0.length; ++i){
			double weight = weights.weight(i);
			ratioSum += (weight * vec0[i]) / vec1[i]; 
			invRatioSum += (weight * vec1[i]) / vec0[i];
		}
		factor = Math.sqrt(invRatioSum / ratioSum);
	}

	public void setFactor(int[] vec0, int[] vec1, List<Integer> excluded) {
		double ratioSum = 0;
		double invRatioSum = 0;

		int excludedIx = 0;
		for (int i = 0; i<vec0.length; ++i){
			if (excludedIx < excluded.size() && excluded.get(excludedIx) == i) {
				++excludedIx;
			} else {
				double weight = weights.weight(i);
				ratioSum += (weight * vec0[i]) / vec1[i]; 
				invRatioSum += (weight * vec1[i]) / vec0[i];
			}
		}
		factor = Math.sqrt(invRatioSum / ratioSum);
	}


	public void setFactor (double factor){
		this.factor = factor;
	}

	public double getFactor(){
		return factor;
	}
	
	protected double entryDistance(double a, double b) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
	public static void main(String[] args){
		RatioVecDistance dist= new RatioVecDistance();
		for (int i = 0; i < 8; ++i){
			for (int j = 0; j <= i; ++j){
				System.out.println(i + ", " + j + ": " + (((double) i) / j) + " - " + dist.entryDistance(i, j) + ", " + dist.entryDistance(j, i));
			}
		}
	}
	public double[] getEntrywiseDistanceVec(int[] vec0, int[] vec1) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
