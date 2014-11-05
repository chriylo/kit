package distanceFunction;

import java.util.List;

public class RatioSquareVecDistance extends EntrywiseVecDistance {

	protected double factor;

	public RatioSquareVecDistance() {
		super();
	}

	public RatioSquareVecDistance(EntryWeights weights) {
		super(weights);
	}

	@Override
	protected double entryDistance(int a, int b) {
		double delta = 1 - (factor * a) / b;
		return delta * delta;
	}

	public void setFactor(int[] vec0, int[] vec1) {
		double ratioSum = 0;
		double ratioSquareSum = 0;

		for (int i = 0; i<vec0.length; ++i){
			double ratio = ((double) vec0[i]) / vec1[i];
			double weightedRatio = weights.weight(i) * ratio;
			ratioSum += weightedRatio; 
			ratioSquareSum += weightedRatio * ratio; 
		}

		factor = ratioSum / ratioSquareSum;
	}

	public void setFactor(int[] vec0, int[] vec1, List<Integer> excluded) {
		double ratioSum = 0;
		double ratioSquareSum = 0;

		int excludedIx = 0;
		for (int i = 0; i<vec0.length; ++i){
			if (excludedIx < excluded.size() && excluded.get(excludedIx) == i) {
				++excludedIx;
			} else {
				double ratio = ((double) vec0[i]) / vec1[i];
				double weightedRatio = weights.weight(i) * ratio;
				ratioSum += weightedRatio; 
				ratioSquareSum += weightedRatio * ratio; 
			}
		}
		factor = ratioSum / ratioSquareSum;
	}
	
	protected double entryDistance(double a, double b) {
		throw new UnsupportedOperationException("Not supported yet.");
	}


	public void setFactor (double factor){
		this.factor = factor;
	}

	public double getFactor(){
		return factor;
	}
	public double[] getEntrywiseDistanceVec(int[] vec0, int[] vec1) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
}
