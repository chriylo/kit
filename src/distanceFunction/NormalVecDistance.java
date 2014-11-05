package distanceFunction;

import java.io.IOException;

import generator.KMerFileReader;


public class NormalVecDistance extends EntrywiseVecDistance {

	public static final double eps = 0.05; //0.1;

	
	public NormalVecDistance(EntryWeights weights){
		super(weights);
	}

	public NormalVecDistance(){
		super();
	}

	@Override
	public double entryDistance(int x, int y) {
		double score;
		if (y != 0){
			double diff = x - y;
			score = diff * diff / (2 * y);			
		}
		else{
			score = x * x / (2 * eps);
		}
		return score;	
	}
	
	public double entryDistance(double x, int y) {
		double score;
		if (y != 0){
			double diff = x - y;
			score = diff * diff / (2 * y);			
		}
		else{
			score = x * x / (2 * eps);
		}
		return score;	
	}
	
	public double entryDistance(int x, double y) {
		double score;
		if (y != 0){
			double diff = x - y;
			score = diff * diff / (2 * y);			
		}
		else{
			score = x * x / (2 * eps);
		}
		return score;	
	}

	public double entryDistance(double x, double y) {
//		double score;
//		if (y != 0) {
//			double diff = x - y;
//			score = diff * diff / y;
//			score += -Math.log(1/Math.sqrt(2 * Math.PI * y));
//		} else {
//			score = x * x / eps;
//		}
//		return score;
		double score;
		if (y != 0){
			double diff = x - y;
			score = diff * diff / (2 * y);			
		}
		else{
			score = x * x / (2 * eps);
		}
		return score;
	}
	
	

	public double[] getEntrywiseDistanceVec(int[] vec0, int[] vec1) {
		/*
		double c = scaleFactor0(vec0, vec1);
		double[] entryDistanceVec = new double[vec0.length];
		for (int i = 0; i < vec0.length; ++i) {
			double weight = weights.weight(i);
			double diff = c * vec0[i] - vec1[i];
			entryDistanceVec[i] = 0.5 * weight * (diff * diff / (vec1[i] + eps) + Math.log(2*Math.PI*(vec1[i] + eps)));
		}
		return entryDistanceVec;
		*/
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
	
	public double distance0(int[] x, int[] y) {
		double c = scaleFactor0(x, y);
		double score = 0;
		for (int i = 0; i < x.length; ++i) {
			double weight = weights.weight(i);
			double diff = c * x[i] - y[i];
			score += weight * (diff * diff / (y[i] + eps) + Math.log(2*Math.PI*(y[i] + eps)));
		}
		return 0.5 * score;
	}

	/**
	 * The weighted probability (normal distribution) for observing x given c*y  
	 * as means.
	 * 
	 * score(i) = 0.5w_i*((x[i] - c*y[i])^2 / s^2 + log(2pi*s^2)), where 
	 * s^2 = c*y[i] if y[i] != 0, and otherwise s^2 = eps.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public double distance1(int[] x, int[] y) {
		double c = scaleFactor1(x, y);
		double shift = Math.log(2*Math.PI*c);
		double zeroShift = Math.log(2*Math.PI*eps);
		double score = 0;
		for (int i = 0; i < x.length; ++i) {
			double weight = weights.weight(i);
			if (y[i] != 0){
				double diff = x[i] - c * y[i];
				score += weight * (diff * diff / (c * y[i]) + Math.log(y[i]) + shift);
			}
			else{
				score += weight * (x[i] * x[i] / eps + zeroShift);
			}
		}
		return 0.5 * score;
	}
	
	/**
	 * The weighted probability (normal distribution) for observing x given c*y  
	 * as means, divided by the weighted probability of the most likely vector 
	 * x^*.
	 * 
	 * score(i) = 0.5w_i*(x[i] - c*y[i])^2 / s^2, where s^2 = c*y[i] if 
	 * y[i] != 0, and otherwise s^2 = eps.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public double distance2(int[] x, int[] y) {
		double c = scaleFactor2(x, y);
		return distance2(x,y,c);
	}
	
	public double distance2(int[] x, double[] y) {
		double c = scaleFactor2(x, y);
		return distance2(x,y,c);
	}
	
	public double distance2(double[] x, int[] y) {
		double c = scaleFactor2(x, y);
		return distance2(x,y,c);
	}
	
	public double distance2(double[] x, double[] y) {
		double c = scaleFactor2(x,y);
		return distance2(x,y,c);
	}
	
	public double distance2(int[] x, int[] y, double c) {
		double score = 0;
		for (int i = 0; i < x.length; ++i) {
			double weight = weights.weight(i);
			if (y[i] != 0){
				double diff = x[i] - c * y[i];
				score += weight * diff * diff / (c * y[i]);
			}
			else{
				score += weight * x[i] * x[i] / (c*eps);
			}
		}
		return 0.5 * score;
	}
	
	public double distance2(int[] x, double[] y, double c) {
		double score = 0;
		for (int i = 0; i < x.length; ++i) {
			double weight = weights.weight(i);
			if (y[i] != 0){
				double diff = x[i] - c * y[i];
				score += weight * diff * diff / (c * y[i]);
			}
			else{
				score += weight * x[i] * x[i] / (c*eps);
			}
		}
		return 0.5 * score;
	}
	
	public double distance2(double[] x, int[] y, double c) {
		double score = 0;
		for (int i = 0; i < x.length; ++i) {
			double weight = weights.weight(i);
			if (y[i] != 0){
				double diff = x[i] - c * y[i];
				score += weight * diff * diff / (c * y[i]);
			}
			else{
				score += weight * x[i] * x[i] / (c*eps);
			}
		}
		return 0.5 * score;
	}
	
	public double distance2(double[] x, double[] y, double c) {
		double score = 0;
		for (int i = 0; i < x.length; ++i) {
			double weight = weights.weight(i);
			if (y[i] != 0){
				double diff = x[i] - c * y[i];
				score += weight * diff * diff / (c * y[i]);
			}
			else{
				score += weight * x[i] * x[i] / (c*eps);
			}
		}
		return 0.5 * score;
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
//		double score = 0;
//		for (int i = 0; i < x.length; ++i) {
//			double weight = weights.weight(i);
//			if (y[i] != 0){
//				double diff = x[i] - c * y[i];
//				score += weight * diff * diff / (2*c * y[i]);
//				score += -Math.log(1/Math.sqrt(2 * Math.PI * c* y[i]));
//			}
//			else{
//				score += weight * x[i] * x[i] / (c*eps);
//				score += -Math.log(1/Math.sqrt(2 * Math.PI * c * eps));
//			}
//		}
//		return score;
	}
	
	public double distance3(int[] x, double[] y, double c) {
		double[] entrywise = getEntrywiseDistanceVec3(x,y,c);
		double score = 0;
		for (int i = 0; i < entrywise.length; ++i) {
			score += entrywise[i];
		}
		return score;
//		double score = 0;
//		for (int i = 0; i < x.length; ++i) {
//			double weight = weights.weight(i);
//			if (y[i] != 0){
//				double diff = x[i] - c * y[i];
//				score += weight * diff * diff / (2*c * y[i]);
//				score += -Math.log(1/Math.sqrt(2 * Math.PI * c* y[i]));
//			}
//			else{
//				score += weight * x[i] * x[i] / (c*eps);
//				score += -Math.log(1/Math.sqrt(2 * Math.PI * c * eps));
//			}
//		}
//		return score;
	}
	
	public double distance3(double[] x, int[] y, double c) {
		double[] entrywise = getEntrywiseDistanceVec3(x,y,c);
		double score = 0;
		for (int i = 0; i < entrywise.length; ++i) {
			score += entrywise[i];
		}
		return score;
//		double score = 0;
//		for (int i = 0; i < x.length; ++i) {
//			double weight = weights.weight(i);
//			if (y[i] != 0){
//				double diff = x[i] - c * y[i];
//				score += weight * diff * diff / (2*c * y[i]);
//				score += -Math.log(1/Math.sqrt(2 * Math.PI * c* y[i]));
//			}
//			else{
//				score += weight * x[i] * x[i] / (c*eps);
//				score += -Math.log(1/Math.sqrt(2 * Math.PI * c * eps));
//			}
//		}
//		return score;
	}
	
	public double distance3(double[] x, double[] y, double c) {
		double[] entrywise = getEntrywiseDistanceVec3(x,y,c);
		double score = 0;
		for (int i = 0; i < entrywise.length; ++i) {
			score += entrywise[i];
		}
		return score;
//		double score = 0;
//		for (int i = 0; i < x.length; ++i) {
//			double weight = weights.weight(i);
//			if (y[i] != 0){
//				double diff = x[i] - c * y[i];
//				score += weight * diff * diff / (2*c * y[i]);
//				score += -Math.log(1/Math.sqrt(2 * Math.PI * c* y[i]));
//			}
//			else{
//				score += weight * x[i] * x[i] / (c*eps);
//				score += -Math.log(1/Math.sqrt(2 * Math.PI * c * eps));
//			}
//		}
//		return score;
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
			if (y[i] != 0){
				entryDistanceVec[i] = weight * entryDistance(x[i],c*y[i]);
				//double diff = x[i] - c * y[i];
				//entryDistanceVec[i] = weight * diff * diff / (2*c * y[i]);
				//entryDistanceVec[i] += -Math.log(1/Math.sqrt(2 * Math.PI * c* y[i]));
				
			}
			else{
				entryDistanceVec[i] = weight * entryDistance(x[i], c * eps);
				//entryDistanceVec[i] = weight * x[i] * x[i] / (2*c*eps);
				//entryDistanceVec[i] += -Math.log(1/Math.sqrt(2 * Math.PI * c*eps));
			}
		}
		return entryDistanceVec;
	}
	
	public double[] getEntrywiseDistanceVec3(int[] x, double[] y, double c) {
		double[] entryDistanceVec = new double[x.length];
		for (int i = 0; i < x.length; ++i) {
			double weight = weights.weight(i);
			if (y[i] != 0){
				entryDistanceVec[i] = weight * entryDistance(x[i],c*y[i]);
				//double diff = x[i] - c * y[i];
				//entryDistanceVec[i] = weight * diff * diff / (2*c * y[i]);
				//entryDistanceVec[i] += -Math.log(1/Math.sqrt(2 * Math.PI * c* y[i]));
				
			}
			else{
				entryDistanceVec[i] = weight * entryDistance(x[i], c * eps);
				//entryDistanceVec[i] = weight * x[i] * x[i] / (2*c*eps);
				//entryDistanceVec[i] += -Math.log(1/Math.sqrt(2 * Math.PI * c*eps));
			}
		}
		return entryDistanceVec;
	}
	
	public double[] getEntrywiseDistanceVec3(double[] x, int[] y, double c) {
		double[] entryDistanceVec = new double[x.length];
		for (int i = 0; i < x.length; ++i) {
			double weight = weights.weight(i);
			if (y[i] != 0){
				entryDistanceVec[i] = weight * entryDistance(x[i],c*y[i]);
				//double diff = x[i] - c * y[i];
				//entryDistanceVec[i] = weight * diff * diff / (2*c * y[i]);
				//entryDistanceVec[i] += -Math.log(1/Math.sqrt(2 * Math.PI * c* y[i]));
				
			}
			else{
				entryDistanceVec[i] = weight * entryDistance(x[i], c * eps);
				//entryDistanceVec[i] = weight * x[i] * x[i] / (2*c*eps);
				//entryDistanceVec[i] += -Math.log(1/Math.sqrt(2 * Math.PI * c*eps));
			}
		}
		return entryDistanceVec;
	}
	
	public double[] getEntrywiseDistanceVec3(double[] x, double[] y, double c) {
		double[] entryDistanceVec = new double[x.length];
		for (int i = 0; i < x.length; ++i) {
			double weight = weights.weight(i);
			if (y[i] != 0){
				entryDistanceVec[i] = weight * entryDistance(x[i],c*y[i]);
				//double diff = x[i] - c * y[i];
				//entryDistanceVec[i] = weight * diff * diff / (2*c * y[i]);
				//entryDistanceVec[i] += -Math.log(1/Math.sqrt(2 * Math.PI * c* y[i]));
				
			}
			else{
				entryDistanceVec[i] = weight * entryDistance(x[i], c * eps);
				//entryDistanceVec[i] = weight * x[i] * x[i] / (2*c*eps);
				//entryDistanceVec[i] += -Math.log(1/Math.sqrt(2 * Math.PI * c*eps));
			}
		}
		return entryDistanceVec;
	}
	
	public double[] getEntrywiseDistanceVec2(int[] x, int[] y) {
		double c = scaleFactor2(x, y);
		return getEntrywiseDistanceVec2(x,y,c);
	}
	
	public double[] getEntrywiseDistanceVec2(int[] x, int[] y, double c) {
		double[] entryDistanceVec = new double[x.length];
		for (int i = 0; i < x.length; ++i) {
			double weight = weights.weight(i);
			if (y[i] != 0){
				double diff = x[i] - c * y[i];
				entryDistanceVec[i] = (weight * diff * diff / (c * y[i]))/2;
			}
			else{
				entryDistanceVec[i] = (weight * x[i] * x[i] / (c*eps))/2;
			}
		}
		return entryDistanceVec;
	}

	/**
	 * @param x observed counts.
	 * @param y expected counts.
	 * @return an estimated optimal scaling factor for y.
	 */
	private double scaleFactor0(int[] x, int[] y) {
		double sumX = 0;
		double sumY = 0;
		double ratioSum = 0;
		double sumOfWeights = 0;
		for (int i = 0; i < x.length; ++i) {
			double weight = weights.weight(i);
			sumOfWeights += weight;
			sumX += weight * x[i];
			sumY += weight * (y[i] + eps);
			ratioSum += weight * x[i] * x[i] / (y[i] + eps);
		}
		double diff = sumOfWeights - sumX;
		return  diff + Math.sqrt(diff*diff + 4*sumY*ratioSum) / ratioSum/2;
	}

	/**
	 * @param x observed counts.
	 * @param y expected counts.
	 * @return an estimated optimal scaling factor for y.
	 */
	private double scaleFactor1(int[] x, int[] y) {
		double a = 0;
		double b = 0;
		double c = 0;
		for (int i = 0; i < x.length; ++i) {
			if (y[i] != 0){
				double weight = weights.weight(i);
				a += weight * y[i];
				b += weight;
				c += weight * x[i] * x[i] / y[i];
			}
		}
		return  (-b + Math.sqrt(b*b + 4*a*c)) / (2*c);
	}

	/**
	 * @param x observed counts.
	 * @param y expected counts.
	 * @return an estimated optimal scaling factor for y.
	 */
	public static double scaleFactor2(int[] x, int[] y) {		
		double a = 0;
		double b = 0;
		for (int i = 0; i < x.length; ++i) {
			double weight = scaleWeights.weight(i);
			//double weight = weights.weight(i);
			if (y[i] != 0){
				a += weight * y[i];
				b += weight * x[i] * x[i] / y[i];
				//a += y[i];
				//b += x[i] * x[i] / y[i];
			}
			else{
				b += weight * x[i] * x[i] / eps;
				//b += x[i] * x[i] / eps;
			}
		}
		return  Math.sqrt(b/a);
//			a += weight*y[i];
//			b += weight*x[i];
//		}
//		if (a==0) {return 0; } else { return b/a; }
	}
	
	
	
	public static double scaleFactor2(int[] x, double[] y) {
		double a = 0;
		double b = 0;
		for (int i = 0; i < x.length; ++i) {
			double weight = scaleWeights.weight(i);
			//double weight = weights.weight(i);
			if (y[i] != 0){
				a += weight * y[i];
				b += weight * x[i] * x[i] / y[i];
				//a += y[i];
				//b += x[i] * x[i] / y[i];
			}
			else{
				b += weight * x[i] * x[i] / eps;
				//b += x[i] * x[i] / eps;
			}
		}
		return  Math.sqrt(b/a);
//			a += weight*y[i];
//			b += weight*x[i];
//		}
//		if (a==0) {return 0; } else { return b/a; }
	}
	
	public static double scaleFactor2(double[] x, int[] y) {
		double a = 0;
		double b = 0;
		for (int i = 0; i < x.length; ++i) {
			double weight = scaleWeights.weight(i);
			//double weight = weights.weight(i);
			if (y[i] != 0){
				a += weight * y[i];
				b += weight * x[i] * x[i] / y[i];
				//a += y[i];
				//b += x[i] * x[i] / y[i];
			}
			else{
				b += weight * x[i] * x[i] / eps;
				//b += x[i] * x[i] / eps;
			}
		}
		return  Math.sqrt(b/a);
//			a += weight*y[i];
//			b += weight*x[i];
//		}
//		if (a==0) {return 0; } else { return b/a; }
	}
	
	public static double scaleFactor2(double[] x, double[] y) {
		double a = 0;
		double b = 0;
		for (int i = 0; i < x.length; ++i) {
			double weight = scaleWeights.weight(i);
			//double weight = weights.weight(i);
			if (y[i] != 0){
				a += weight * y[i];
				b += weight * x[i] * x[i] / y[i];
				//a += y[i];
				//b += x[i] * x[i] / y[i];
			}
			else{
				b += weight * x[i] * x[i] / eps;
				//b += x[i] * x[i] / eps;
			}
		}
		return  Math.sqrt(b/a);
//			a += weight*y[i];
//			b += weight*x[i];
//		}
//		if (a==0) {return 0; } else { return b/a; }
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
//		EntrywiseVecDistance dist = new NormalVecDistance();
		EntrywiseVecDistance dist = new DifferenceVecDistance();
		int[] vec0 = {1, 1, 2, 3, 4};
		int[] vec1 = {3, 3, 6, 9, 12};
		int[] vec2 = {4, 2, 7, 8, 12};
		int[] vec3 = {1, 2, 4, 6, 8};

		System.out.println(dist.distance(vec0, vec0));
		System.out.println(dist.distance(vec0, vec1));
		System.out.println(dist.distance(vec1, vec0));
		System.out.println(dist.distance(vec2, vec0));
		System.out.println(dist.distance(vec3, vec0));

		int[] sample = KMerFileReader.getBarcodeFromFile("G248BA2.masked.fa.Ref.50.barcod");
		int[] rightTemplate = KMerFileReader.getBarcodeFromFile("FH13BA2.masked.fa.Ref.50.barcod");
		int[] wrongTemplate = KMerFileReader.getBarcodeFromFile("FH15B.masked.fa.Ref.50.barcod");

		System.out.println();
		System.out.println("Sample sum: " + sum(sample));
		System.out.println("Right template sum: " + sum(rightTemplate));
		System.out.println("Wrong template sum: " + sum(wrongTemplate));
		System.out.println("Identity cost: " + dist.distance(sample, sample));
		System.out.println("Right template: " + dist.distance(sample, rightTemplate));
		System.out.println("Wrong template: " + dist.distance(sample, wrongTemplate));
		System.out.println("Right/wrong: " + dist.distance(rightTemplate, wrongTemplate));
		System.out.println("Wrong/right: " + dist.distance(wrongTemplate, rightTemplate));
	}

	private static int sum(int[] vec) {
		int sum = 0;
		for (int x : vec){
			sum += x;
		}
		return sum;
	}
}
