package distanceFunction;

import java.util.ArrayList;
import java.util.List;

public class StdVecDistance extends EntrywiseVecDistance {

	public static final double eps = 0.0001;
	private static final List<Integer> emptyList = new ArrayList<Integer>();

	@Override
	public double distance(int[] vec0, int[] vec1) {
		return distance(vec0, vec1, emptyList);
	}

	@Override
	public double distance(int[] vec0, int[] vec1, List<Integer> excluded) {
		int n = vec0.length;
		excluded.add(n);
		int m = excluded.size();
		int nextExcludedIx = 0;
		int nextExcluded = excluded.get(nextExcludedIx);

		double sumOfLogs = 0;
		double sumOfLogSquares = 0;
		double sumOfWeights = 0;

		for (int i=0; i<n; ++i){
			if (i == nextExcluded){
				++nextExcludedIx;
				nextExcluded = excluded.get(nextExcludedIx);
			}
			else{			
				double logRatio = Math.log((vec0[i] + eps) / (vec1[i] + eps));
				double weight = weights.weight(i);
				sumOfLogs += weight * logRatio;
				sumOfLogSquares += weight * logRatio * logRatio;
				sumOfWeights += weight;
			}
		}

		excluded.remove(m-1);
		return Math.exp(Math.sqrt((sumOfLogSquares - sumOfLogs*sumOfLogs/sumOfWeights)/(sumOfWeights)));
	}

	@Override
	protected double entryDistance(int a, int b) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected double entryDistance(double a, double b) {
		throw new UnsupportedOperationException();
	}
	
	public double[] getEntrywiseDistanceVec(int[] vec0, int[] vec1) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
