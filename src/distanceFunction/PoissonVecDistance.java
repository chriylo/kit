package distanceFunction;

import generator.KMerFileReader;

import java.io.IOException;



public class PoissonVecDistance extends EntrywiseVecDistance {

	private static final double HALF_LOG_PI = Math.log(Math.PI) / 2.;
	private static final double[] factLans  = {0, 0, Math.log(2), Math.log(6), Math.log(24), Math.log(120), Math.log(720)};
	private static final int computedLans = factLans.length;
	public static final double eps = 0.1;

	
	public PoissonVecDistance(EntryWeights weights){
		super(weights);
	}
	
	public PoissonVecDistance(){
		super();
	}

	/**
	 * Calculates an approximation of the Poisson probability.
	 * @param mean - lambda, the average number of occurrences
	 * @param observed - the actual number of occurrences observed
	 * @return ln(Poisson probability) - the natural log of the Poisson probability.
	 */
	public static double poissonProbabilityApproximation (double mean, int observed) {
		return observed * Math.log(mean) - mean - factorialApproximation(observed);
	}

	/**
	 * Srinivasa Ramanujan ln(n!) factorial estimation.
	 * Good for larger values of n.
	 * @return ln(n!)
	 */
	public static double factorialApproximation(long n) {
		if (n < computedLans) return factLans[(int) n];
		double a = n * Math.log(n) - n;
		double b = Math.log(n * (1 + 4 * n * (1 + 2 * n))) / 6.;
		return a + b + HALF_LOG_PI;
	}
	
	/**
	 * Stirling's factorial approximation
	 */
	public static double stirlingApproximation(double n) {
		if (n==0) { return 0; }
		else { return -(n*Math.log(n)-n+0.5*Math.log(2*Math.PI*n)); }
	}

	@Override
	public double entryDistance(int a, int b) {
		throw new UnsupportedOperationException();
	}
	
	public double entryDistance(double a, double b) {
		double score = 0;
		if (b==0) {
			if (a==0) {
				score = 0;
			} else {
				score = -((a)*Math.log(b) - b -stirlingApproximation(a));
			}
		} else {
			score = -((a)*Math.log(b) - b -stirlingApproximation(a)); 
		}
		return score;
	}
	
	public double[] getEntrywiseDistanceVec(int[] vec0, int[] vec1) {	
		double[] entryDistanceVec = new double[vec0.length];
		double c = scaleFactor(vec0, vec1);
		for (int i = 0; i < vec0.length; ++i) {
			if ((vec1[i]==0)) {
				if (vec1[i]==0) {
					entryDistanceVec[i] = 0;
				} else {
				double weight = weights.weight(i);
				entryDistanceVec[i] = weight*((vec0[i])*Math.log(vec1[i]) - vec1[i] -stirlingApproximation(vec0[i]));
				}
				//double currY = (c*(vec1[i]+0.5)+0.001);
				//entryDistanceVec[i] = -weight*((vec0[i]+1)*Math.log(currY) - currY -factorialApproximation(vec0[i]+1))/10000; 
			} else {
				double weight = weights.weight(i);
				double currY = (c*vec1[i]);
				entryDistanceVec[i] = weight*((vec0[i])*Math.log(currY) - currY -stirlingApproximation(vec0[i])); 
			}
		}
		return entryDistanceVec;
		
	}
	
	public double[] getEntrywiseDistanceVec(int[] vec0, double[] vec1) {	
		double[] entryDistanceVec = new double[vec0.length];
		double c = scaleFactor(vec0, vec1);
		for (int i = 0; i < vec0.length; ++i) {
			if ((vec1[i]==0)) {
				if (vec1[i]==0) {
					entryDistanceVec[i] = 0;
				} else {
				double weight = weights.weight(i);
				entryDistanceVec[i] = -weight*((vec0[i])*Math.log(vec1[i]) - vec1[i] -stirlingApproximation(vec0[i]));
				}
				//double currY = (c*(vec1[i]+0.5)+0.001);
				//entryDistanceVec[i] = -weight*((vec0[i]+1)*Math.log(currY) - currY -factorialApproximation(vec0[i]+1))/10000; 
			} else {
				double weight = weights.weight(i);
				double currY = (c*vec1[i]);
				entryDistanceVec[i] = -weight*((vec0[i])*Math.log(currY) - currY -stirlingApproximation(vec0[i])); 
			}
		}
		return entryDistanceVec;	
	}
	
	public double[] getEntrywiseDistanceVec(double[] vec0, int[] vec1) {	
		double[] entryDistanceVec = new double[vec0.length];
		double c = scaleFactor(vec0, vec1);
		for (int i = 0; i < vec0.length; ++i) {
			if ((vec1[i]==0)) {
				if (vec1[i]==0) {
					entryDistanceVec[i] = 0;
				} else {
				double weight = weights.weight(i);
				entryDistanceVec[i] = -weight*((vec0[i])*Math.log(vec1[i]) - vec1[i] -stirlingApproximation(vec0[i]));
				}
				//double currY = (c*(vec1[i]+0.5)+0.001);
				//entryDistanceVec[i] = -weight*((vec0[i]+1)*Math.log(currY) - currY -factorialApproximation(vec0[i]+1))/10000; 
			} else {
				double weight = weights.weight(i);
				double currY = (c*vec1[i]);
				entryDistanceVec[i] = -weight*((vec0[i])*Math.log(currY) - currY -stirlingApproximation(vec0[i])); 
			}
		}
		return entryDistanceVec;
		
	}
	
	public double[] getEntrywiseDistanceVec(double[] vec0, double[] vec1) {	
		double[] entryDistanceVec = new double[vec0.length];
		double c = scaleFactor(vec0, vec1);
		for (int i = 0; i < vec0.length; ++i) {
			if ((vec1[i]==0)) {
				if (vec1[i]==0) {
					entryDistanceVec[i] = 0;
				} else {
				double weight = weights.weight(i);
				entryDistanceVec[i] = -weight*((vec0[i])*Math.log(vec1[i]) - vec1[i] -stirlingApproximation(vec0[i]));
				}
				//double currY = (c*(vec1[i]+0.5)+0.001);
				//entryDistanceVec[i] = -weight*((vec0[i]+1)*Math.log(currY) - currY -factorialApproximation(vec0[i]+1))/10000; 
			} else {
				double weight = weights.weight(i);
				double currY = (c*vec1[i]);
				entryDistanceVec[i] = -weight*((vec0[i])*Math.log(currY) - currY -stirlingApproximation(vec0[i])); 
			}
		}
		return entryDistanceVec;
	}
	
	public double getScaleFactor(int[] x, int[] y) {
		return scaleFactor(x,y);
	}
	
	public double getScaleFactor(double[] x, int[] y) {
		return scaleFactor(x,y);
	}
	
	public double getScaleFactor(int[] x, double[] y) {
		return scaleFactor(x,y);
	}
	
	public double scaleFactor(int[] vec0, int[] vec1){
		double sumX = 0;
		double sumY = 0;
		for (int i = 0; i < vec0.length; ++i) {
			double weight = weights.weight(i);
			sumX += weight*vec0[i];
			sumY += weight*vec1[i];
		}
		return sumX /sumY;
	}
	
	public double scaleFactor(int[] vec0, double[] vec1){
		double sumX = 0;
		double sumY = 0;
		for (int i = 0; i < vec0.length; ++i) {
			double weight = weights.weight(i);
			sumX += weight*vec0[i];
			sumY += weight*vec1[i];
		}
		return sumX /sumY;
	}
	
	public double scaleFactor(double[] vec0, int[] vec1){
		double sumX = 0;
		double sumY = 0;
		for (int i = 0; i < vec0.length; ++i) {
			double weight = weights.weight(i);
			sumX += weight*vec0[i];
			sumY += weight*vec1[i];
		}
		return sumX /sumY;
	}
	
	public double scaleFactor(double[] vec0, double[] vec1){
		double sumX = 0;
		double sumY = 0;
		for (int i = 0; i < vec0.length; ++i) {
			double weight = weights.weight(i);
			sumX += weight*vec0[i];
			sumY += weight*vec1[i];
		}
		return sumX /sumY;
	}
	
	
	

	
	public double distance(int[] vec0, int[] vec1) {
		double distance = 0;
		double[] entrywiseDistance = getEntrywiseDistanceVec(vec0, vec1);
		for (int i = 0; i < entrywiseDistance.length; i++) {
			distance += entrywiseDistance[i];
		}
		return distance;

	}
	
	public double distance(int[] vec0, double[] vec1) {
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
	
	public double distance(double[] vec0, double[] vec1) {
		double distance = 0;
		double[] entrywiseDistance = getEntrywiseDistanceVec(vec0, vec1);
		for (int i = 0; i < entrywiseDistance.length; i++) {
			distance += entrywiseDistance[i];
		}
		return distance;

	}
	
	

	
	public static void main(String[] args) throws IOException{
		
		int[] sample = KMerFileReader.getBarcodeFromFile("G248BA2.masked.fa.Ref.50.barcod");
		int[] rightTemplate = KMerFileReader.getBarcodeFromFile("FH13BA2.masked.fa.Ref.50.barcod");
		int[] wrongTemplate = KMerFileReader.getBarcodeFromFile("FH15B.masked.fa.Ref.50.barcod");
		
		EntrywiseVecDistance dist = new PoissonVecDistance();
		System.out.println(dist.distance(sample, sample));
		System.out.println(dist.distance(sample, rightTemplate));
		System.out.println(dist.distance(sample, wrongTemplate));
		
		
	}

}
