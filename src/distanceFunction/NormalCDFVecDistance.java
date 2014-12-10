package distanceFunction;
import java.io.IOException;

import org.apache.commons.math3.distribution.NormalDistribution;

import generator.KMerFileReader;

public class NormalCDFVecDistance extends EntrywiseVecDistance {
	public static final double eps = 0.05; //0.1;

	public NormalCDFVecDistance(EntryWeights weights){
		super(weights);
	}

	public NormalCDFVecDistance(){
		super();
	}

	public double entryDistance(int x, int y) {
		NormalDistribution dist;
		if (y == 0) {
			dist = new NormalDistribution(0, Math.sqrt(eps));
		} else {
			dist = new NormalDistribution(y, Math.sqrt(y));
		}
		double p = 2 * dist.cumulativeProbability(y - Math.abs(x - y));
		return 1 - p; 
	}
	
	public double entryDistance(double x, int y) {
		NormalDistribution dist;
		if (y == 0) {
			dist = new NormalDistribution(0, Math.sqrt(eps));
		} else {
			dist = new NormalDistribution(y, Math.sqrt(y));
		}
		double p = 2 * dist.cumulativeProbability(y - Math.abs(x - y));
		return 1 - p; 
	}
	
	public double entryDistance(int x, double y) {
		NormalDistribution dist;
		if (y == 0) {
			dist = new NormalDistribution(0, Math.sqrt(eps));
		} else {
			dist = new NormalDistribution(y, Math.sqrt(y));
		}
		double p = 2 * dist.cumulativeProbability(y - Math.abs(x - y));
		return 1 - p; 
	}
		
	public double entryDistance(double x, double y) {
		NormalDistribution dist;
		if (y == 0) {
			dist = new NormalDistribution(0, Math.sqrt(eps));
		} else {
			dist = new NormalDistribution(y, Math.sqrt(y));
		}
		double p = 2 * dist.cumulativeProbability(y - Math.abs(x - y));
		return 1 - p; 
	}
	
	public double entryDistance(int x, int y, double c) {
		NormalDistribution dist;
		if (y == 0) {
			dist = new NormalDistribution(0, Math.sqrt(c*eps));
		} else {
			dist = new NormalDistribution(y, Math.sqrt(y));
		}
		double p = 2 * dist.cumulativeProbability(y - Math.abs(x - y));
		return 1 - p; 
	}
	
	public double entryDistance(double x, int y, double c) {
		NormalDistribution dist;
		if (y == 0) {
			dist = new NormalDistribution(0, Math.sqrt(c*eps));
		} else {
			dist = new NormalDistribution(y, Math.sqrt(y));
		}
		double p = 2 * dist.cumulativeProbability(y - Math.abs(x - y));
		return 1 - p; 
	}
	
	public double entryDistance(int x, double y, double c) {
		NormalDistribution dist;
		if (y == 0) {
			dist = new NormalDistribution(0, Math.sqrt(c*eps));
		} else {
			dist = new NormalDistribution(y, Math.sqrt(y));
		}
		double p = 2 * dist.cumulativeProbability(y - Math.abs(x - y));
		return 1 - p; 
	}
		
	public double entryDistance(double x, double y, double c) {
		NormalDistribution dist;
		if (y == 0) {
			dist = new NormalDistribution(0, Math.sqrt(c*eps));
		} else {
			dist = new NormalDistribution(y, Math.sqrt(y));
		}
		double p = 2 * dist.cumulativeProbability(y - Math.abs(x - y));
		return 1 - p; 
	}

	
		
	public double[] getEntrywiseDistanceVec(int[] vec0, int[] vec1) {
		return getEntrywiseDistanceVec3(vec0,vec1);
	}
		
	public double[] getEntrywiseDistanceVec(int[] vec0, double[] vec1) {
		return getEntrywiseDistanceVec3(vec0,vec1);
	}
		
	public double[] getEntrywiseDistanceVec(double[] vec0, int[] vec1) {
		return getEntrywiseDistanceVec3(vec0,vec1);
	}
		
	public double[] getEntrywiseDistanceVec(double[] vec0, double[] vec1) {
		return getEntrywiseDistanceVec3(vec0,vec1);
	}

	public double distance(int[] x, int[] y) {
		return distance3(x, y);// / distance1(x, x);
	}
		
	public double distance(int[] x, double[] y) {
		return distance3(x, y);// / distance1(x, x);
	}
		
	public double distance(double[] x, int[] y) {
		return distance3(x, y);// / distance1(x, x);
	}
		
	public double distance(int[] x, double[] y, double c) {
		return distance3(x, y, c);
	}
		
	public double distance(double[] x, double[] y, double c) {
		return distance3(x, y, c);
	}
		
	public double distance(int[] x, int[] y, double c) {
		return distance3(x, y, c);
	}
		
	public double distance(double[] x, int[] y, double c) {
		return distance3(x, y, c);
	}
		
	public double getScaleFactor(int[] x, int[] y) {
		return scaleFactor3(x,y);
	}
		
	public double getScaleFactor(double[] x, int[] y) {
		return scaleFactor3(x,y);
	}
		
	public double getScaleFactor(int[] x, double[] y) {
		return scaleFactor3(x,y);
	}
		
	public double distance3(int[] x, int[] y) {
		double c = scaleFactor3(x, y);
		return distance3(x,y,c);
	}
		
	public double distance3(int[] x, double[] y) {
		double c = scaleFactor3(x, y);
		return distance3(x,y,c);
	}
		
	public double distance3(double[] x, int[] y) {
		double c = scaleFactor3(x, y);
		return distance3(x,y,c);
	}
		
	public double distance3(double[] x, double[] y) {
		double c = scaleFactor3(x, y);
		return distance3(x,y,c);
	}
		
	public double distance3(int[] x, int[] y, double c) {
		double[] entrywise = getEntrywiseDistanceVec3(x,y,c);
		double score = 0;
		for (int i = 0; i < entrywise.length; ++i) {
			score += entrywise[i];
		}
		return score;
	}
		
	public double distance3(int[] x, double[] y, double c) {
		double[] entrywise = getEntrywiseDistanceVec3(x,y,c);
		double score = 0;
		for (int i = 0; i < entrywise.length; ++i) {
			score += entrywise[i];
		}
		return score;
	}
		
	public double distance3(double[] x, int[] y, double c) {
		double[] entrywise = getEntrywiseDistanceVec3(x,y,c);
		double score = 0;
		for (int i = 0; i < entrywise.length; ++i) {
			score += entrywise[i];
		}
		return score;
	}
		
	public double distance3(double[] x, double[] y, double c) {
		double[] entrywise = getEntrywiseDistanceVec3(x,y,c);
		double score = 0;
		for (int i = 0; i < entrywise.length; ++i) {
			score += entrywise[i];
		}
		return score;
	}
		
	public double[] getEntrywiseDistanceVec3(int[] x, int[] y) {
		double c = scaleFactor3(x, y);
		return getEntrywiseDistanceVec3(x,y,c);
	}
		
	public double[] getEntrywiseDistanceVec3(int[] x, double[] y) {
		double c = scaleFactor3(x, y);
		return getEntrywiseDistanceVec3(x,y,c);
	}
		
	public double[] getEntrywiseDistanceVec3(double[] x, int[] y) {
		double c = scaleFactor3(x, y);
		return getEntrywiseDistanceVec3(x,y,c);
	}
		
	public double[] getEntrywiseDistanceVec3(double[] x, double[] y) {
		double c = scaleFactor3(x, y);
		return getEntrywiseDistanceVec3(x,y,c);
	}
		
	public double[] getEntrywiseDistanceVec3(int[] x, int[] y, double c) {
		double[] entryDistanceVec = new double[x.length];
		for (int i = 0; i < x.length; ++i) {
			double weight = weights.weight(i);
			if (y[i] == 0) {
				entryDistanceVec[i] = weight * entryDistance(x[i], 0, c);
			} else {
				entryDistanceVec[i] = weight * entryDistance(x[i], c * y[i]);
			}
		}
		return entryDistanceVec;
	}
		
	public double[] getEntrywiseDistanceVec3(int[] x, double[] y, double c) {
		double[] entryDistanceVec = new double[x.length];
		for (int i = 0; i < x.length; ++i) {
			double weight = weights.weight(i);
			if (y[i] == 0) {
				entryDistanceVec[i] = weight * entryDistance(x[i], 0, c);
			} else {
				entryDistanceVec[i] = weight * entryDistance(x[i], c * y[i]);
			}
		}
		return entryDistanceVec;
	}
		
	public double[] getEntrywiseDistanceVec3(double[] x, int[] y, double c) {
		double[] entryDistanceVec = new double[x.length];
		for (int i = 0; i < x.length; ++i) {
			double weight = weights.weight(i);
			if (y[i] == 0) {
				entryDistanceVec[i] = weight * entryDistance(x[i], 0, c);
			} else {
				entryDistanceVec[i] = weight * entryDistance(x[i], c * y[i]);
			}
		}
		return entryDistanceVec;
	}
		
	public double[] getEntrywiseDistanceVec3(double[] x, double[] y, double c) {
		double[] entryDistanceVec = new double[x.length];
		for (int i = 0; i < x.length; ++i) {
			double weight = weights.weight(i);
			if (y[i] == 0) {
				entryDistanceVec[i] = weight * entryDistance(x[i], 0, c);
			} else {
				entryDistanceVec[i] = weight * entryDistance(x[i], c * y[i]);
			}
		}
		return entryDistanceVec;
	}
		
	public static double scaleFactor3(int[] vec0, int[] vec1){
		double sumX = 0;
		double sumY = 0;
		for (int i = 0; i < vec0.length; ++i) {
			double weight = weights.weight(i);
			sumX += weight*vec0[i];
			sumY += weight*vec1[i];
		}
		return sumX /sumY;
	}
		
	public static double scaleFactor3(int[] vec0, double[] vec1){
		double sumX = 0;
		double sumY = 0;
		for (int i = 0; i < vec0.length; ++i) {
			double weight = weights.weight(i);
			sumX += weight*vec0[i];
			sumY += weight*vec1[i];
//			if (weight != 0) {
//			System.out.println(i + "\t" + vec0[i] + "\t" + vec1[i]);
//			}
		}
		return sumX /sumY;
	}
		
	public static double scaleFactor3(double[] vec0, int[] vec1){
		double sumX = 0;
		double sumY = 0;
		for (int i = 0; i < vec0.length; ++i) {
			double weight = weights.weight(i);
			sumX += weight*vec0[i];
			sumY += weight*vec1[i];
		}
		return sumX /sumY;
	}
		
	public static double scaleFactor3(double[] vec0, double[] vec1){
		double sumX = 0;
		double sumY = 0;
		for (int i = 0; i < vec0.length; ++i) {
			double weight = weights.weight(i);
			sumX += weight*vec0[i];
			sumY += weight*vec1[i];
		}
		return sumX /sumY;
	}

	public static void main(String[] args) throws IOException{
//		NormalCDFVecDistance distCalc = new NormalCDFVecDistance();
////		NormalDistribution dist = new NormalDistribution(14.81481481, Math.sqrt(14.81481481));
////		System.out.println(dist.cumulativeProbability(14.81481481 - Math.abs(16.19 - 14.81481481)));
////		System.out.println(distCalc.entryDistance(16.19,14.81481481));
//		
//		double[] norm = {20.31,0.02,16.62,0.01,22.17,16.15,20.10,16.85,19.32,18.25,18.98,0.03,14.76,0.07,16.35};
//		int[] cn = {2,0,2,0,3,2,2,2,2,2,2,0,2,0,2};
//		System.out.println(distCalc.distance(norm, cn));
//		System.out.println(norm[4] + "\t" + cn[4] + '\t' + NormalCDFVecDistance.scaleFactor3(norm, cn) + '\t' + distCalc.entryDistance(norm[7], cn[7]* NormalCDFVecDistance.scaleFactor3(norm, cn)));
//		int[] cn2 = {2,0,2,0,2,2,2,2,2,2,2,0,2,0,2};
//		System.out.println(distCalc.distance(norm, cn2));
//		System.out.println(norm[4] + "\t" + cn2[4] + '\t' + NormalCDFVecDistance.scaleFactor3(norm, cn2) + '\t' + distCalc.entryDistance(norm[7], cn2[7]* NormalCDFVecDistance.scaleFactor3(norm, cn2)));

		NormalDistribution dist = new NormalDistribution(.58, Math.sqrt(.58));
		System.out.println(dist.cumulativeProbability(.55));
		System.out.println(1-dist.cumulativeProbability(.65));
		
		 dist = new NormalDistribution(58, Math.sqrt(58));
		System.out.println(dist.cumulativeProbability(55));
		System.out.println(1-dist.cumulativeProbability(65));
		
		 dist = new NormalDistribution(72.5, Math.sqrt(72.5));
			System.out.println(1-(dist.cumulativeProbability(68.75)+1-dist.cumulativeProbability(81.25)));

	}
	
}
