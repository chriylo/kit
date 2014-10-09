package distanceFunction;

import java.util.*;

public class DifferenceVecDistance extends EntrywiseVecDistance {
	
	private static Comparator<double[]> ratioComparator = new Comparator<double[]>() { 
		public int compare(double[] a, double[] b) {return ((Double) a[0]).compareTo(b[0]); } 
	};
	
	private static double eps = 0.5;


	public DifferenceVecDistance(EntryWeights weights) {
		super(weights);
	}
	
	public DifferenceVecDistance() {
		super();
	}


	@Override
	public double entryDistance(int a, int b) {
		return Math.abs(a-b);

	}
		
	public double entryDistance(double a, double b) {
		return Math.abs(a-b);
	}
	
	private double scaleFactorSum(double[] vec0, double[] vec1){
		double sumX = 0;
		double sumY = 0;
		for (int i = 0; i < vec0.length; ++i) {
			double weight = weights.weight(i);
			sumX += weight*vec0[i];
			sumY += weight*vec1[i];
		}
		return sumX /sumY;
	}
	
	private double scaleFactorSum(int[] vec0, double[] vec1){
		double sumX = 0;
		double sumY = 0;
		for (int i = 0; i < vec0.length; ++i) {
			double weight = weights.weight(i);
			sumX += weight*vec0[i];
			sumY += weight*vec1[i];
		}
		return sumX /sumY;
	}
	
	private double scaleFactorSum(int[] vec0, int[] vec1){
		double sumX = 0;
		double sumY = 0;
		for (int i = 0; i < vec0.length; ++i) {
			double weight = weights.weight(i);
			sumX += weight*vec0[i];
			sumY += weight*vec1[i];
		}
		return sumX /sumY;
	}
	
	private double scaleFactorSum(double[] vec0, int[] vec1){
		double sumX = 0;
		double sumY = 0;
		for (int i = 0; i < vec0.length; ++i) {
			double weight = weights.weight(i);
			sumX += weight*vec0[i];
			sumY += weight*vec1[i];
		}
		return sumX /sumY;
	}
	
	private double scaleFactorAvg(int[] vec0, double[] vec1) {
		double sum = 0;
		double num = 0;
		for (int i = 0; i < vec0.length; ++i) {
			double weight = weights.weight(i);
			if (vec1[i]==0) {
				sum += weight*(vec0[i]*1.0)/eps;
			} else {
				sum += weight*(vec0[i]*1.0)/vec1[i];
			}
			num += weight;
		}
		return sum/num;
	}
	
	private double scaleFactorAvg(double[] vec0, int[] vec1) {
		double sum = 0;
		double num = 0;
		for (int i = 0; i < vec0.length; ++i) {
			double weight = weights.weight(i);
			if (vec1[i]==0) {
				sum += weight*(vec0[i]*1.0)/eps;
			} else {
				sum += weight*(vec0[i]*1.0)/vec1[i];
			}
			num += weight;
		}
		return sum/num;
	}
	
	private double scaleFactorAvg(int[] vec0, int[] vec1) {
		double sum = 0;
		double num = 0;
		for (int i = 0; i < vec0.length; ++i) {
			double weight = weights.weight(i);
			if (vec1[i]==0) {
				sum += weight*(vec0[i]*1.0)/eps;
			} else {
				sum += weight*(vec0[i]*1.0)/vec1[i];
			}
			num += weight;
		}
		return sum/num;
	}
	
	
	private double scaleFactor(int[] vec0, int[] vec1) {
		double[][] entryFactor = new double[vec0.length][3];
		double sum0 = 0;
		double sum1 = 0;
		for (int i = 0; i < entryFactor.length; ++i) {
			double weight = weights.weight(i);
			if ((weight==0) || (vec1[i]==0)){
				entryFactor[i] = new double[] { Double.MAX_VALUE, vec0[i], vec1[i] };
			} else {
				entryFactor[i] = new double[] { (vec0[i]*1.0)/vec1[i], vec0[i], vec1[i] };
			}
			sum0 += weight*vec0[i];
			sum1 += weight*vec1[i];
		}
		Arrays.sort(entryFactor, ratioComparator);
		double value = Double.MAX_VALUE;
		double scale = -1;
		double sumSoFar0 = 0;
		double sumSoFar1 = 0;
		for (int i = 0; i < entryFactor.length; ++i) {
			double newScale = entryFactor[i][1]/entryFactor[i][2];
			sumSoFar0 += entryFactor[i][1];
			sumSoFar1 += entryFactor[i][2];
			double newValue = sum0-2*sumSoFar0 + newScale*(2*sumSoFar1-sum1);
			if (newValue > value) { 
				return scale; 
			} 
			else { value = newValue; scale = newScale; }
		}
		return scale;
	}

	private double scaleFactor1(int[] x, int[] y) {
		double[][] entryFactor = new double[x.length][];
		double ratioSum = 0;
		double weightSum = 0;
		for (int i = 0; i < x.length; ++i) {
			double weight = weights.weight(i);
			double yi = y[i];
			if (yi == 0){
				if (x[i] == 0){
					entryFactor[i] = new double[] {Double.POSITIVE_INFINITY, 0, 0};
					continue;
				}
				else{
					yi = eps;
				}
			}
			entryFactor[i] = new double[] {x[i]/yi, x[i], yi};
			ratioSum += weight*x[i]/yi;
			weightSum += weight;
		}
		
		Arrays.sort(entryFactor, ratioComparator);
		double ratioSumSoFar = 0;
		double weightSumSoFar = 0;
		int i = 0; 
		
		for (; i < x.length && (2*ratioSumSoFar - ratioSum < 0); ++i) {
			double weight = weights.weight(i);
			ratioSumSoFar += weight * entryFactor[i][0];
			weightSumSoFar += weight;
		}
//		return 2*weightSumSoFar - weightSum + 1 / entryFactor[i][0] * (ratioSum - 2*ratioSumSoFar);
		return entryFactor[i][0];
	}
	
	public double[] getEntrywiseDistanceVec(int[] vec0, double[] vec1) {
		double[] entryDistanceVec = new double[vec0.length];
		double c = scaleFactorSum(vec0, vec1);
		//double c = scaleFactor(vec0, vec1);
		for (int i = 0; i < vec0.length; ++i) {
			double weight = weights.weight(i);
			double cY = c*vec1[i];
			entryDistanceVec[i] = weight*(Math.abs(vec0[i]-cY));
			//entryDistanceVec[i] = weight*(Math.abs(vec0[i]-vec1[i]));
		}
		return entryDistanceVec;
	}
	
	public double[] getEntrywiseDistanceVec(double[] vec0, double[] vec1) {
		double[] entryDistanceVec = new double[vec0.length];
		double c = scaleFactorSum(vec0, vec1);
		//double c = scaleFactor(vec0, vec1);
		for (int i = 0; i < vec0.length; ++i) {
			double weight = weights.weight(i);
			double cY = c*vec1[i];
			entryDistanceVec[i] = weight*(Math.abs(vec0[i]-cY));
			//entryDistanceVec[i] = weight*(Math.abs(vec0[i]-vec1[i]));
		}
		return entryDistanceVec;
	}
	
	public double[] getEntrywiseDistanceVec(double[] vec0, int[] vec1) {
		double[] entryDistanceVec = new double[vec0.length];
		double c = scaleFactorSum(vec0, vec1);
		//double c = scaleFactor(vec0, vec1);
		for (int i = 0; i < vec0.length; ++i) {
			double weight = weights.weight(i);
			double cY = c*vec1[i];
			entryDistanceVec[i] = weight*(Math.abs(vec0[i]-cY));
			//entryDistanceVec[i] = weight*(Math.abs(vec0[i]-vec1[i]));
		}
		return entryDistanceVec;
	}
	
	/* vec0 is sample, vec1 is template*/
	public double[] getEntrywiseDistanceVec(int[] vec0, int[] vec1) {
		double[] entryDistanceVec = new double[vec0.length];
		double c = scaleFactorSum(vec0, vec1);
		//double c = scaleFactor(vec0, vec1);
		for (int i = 0; i < vec0.length; ++i) {
			double weight = weights.weight(i);
			double cY = c*vec1[i];
			entryDistanceVec[i] = weight*(Math.abs(vec0[i]-cY));
		}
		return entryDistanceVec;
	}
		
	public double[] getEntrywiseDistanceVec1(int[] x, int[] y) {
		double[] entryDistanceVec = new double[x.length];
		double c = scaleFactor1(x, y);
		double cY;
		for (int i = 0; i < x.length; ++i) {
			double weight = weights.weight(i);
			if (y[i] == 0){
				if (x[i] == 0){
					continue;
				}
				else{
					cY = c*eps;
				}
			}
			else{
				cY = c*y[i];
			}
			entryDistanceVec[i] = weight*(Math.abs(x[i]-cY)/cY);
		}
		return entryDistanceVec;
	}
	
	
	
	public double distance(int[] vec0, int[] vec1) {
		double distance = 0;
		double[] entrywiseDistance = getEntrywiseDistanceVec(vec0, vec1);
		for (int i = 0; i < entrywiseDistance.length; i++) {
			distance += entrywiseDistance[i];
		}
		return distance;
	}
	
	public double distance(double[] vec0, int[] vec1) {
		double distance = 0;
		double[] entrywiseDistance = getEntrywiseDistanceVec(vec0, vec1);
		for (int i = 0; i < entrywiseDistance.length; i++) {
			distance += entrywiseDistance[i];
		}
		return distance;
	}
	
	public double getScaleFactor(int[] x, int[] y) {
		return scaleFactorAvg(x,y);
	}
	
	public double getScaleFactor(int[] x, double[] y) {
		return scaleFactorAvg(x,y);
	}
	
	
	public double getScaleFactor(double[] x, int[] y) {
		return scaleFactorAvg(x,y);
	}
		

}
