package distanceFunction;

import generator.KMerFileReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import typing.Typing;

/**
 * An abstract VecDistance implementation for distance functions that sums
 * weighted distances for all pairs of corresponding entries.
 *
 * @author zakov
 *
 */
public abstract class EntrywiseVecDistance implements VecDistance {

    private static final List<Integer> emptyList = new ArrayList<Integer>(0);
    static EntryWeights weights;
    static EntryWeights scaleWeights;

    public EntrywiseVecDistance(EntryWeights weights) {
        this.weights = weights;
        this.scaleWeights = weights;
    }

    public EntrywiseVecDistance() {
        this(new EntryWeights());
    }

    @Override
    public double distance(int[] vec0, int[] vec1) {
        return distance(vec0, vec1, emptyList);
    }
    
    public double distance(int[] vec0, double[] vec1) {
    	return 0;
    }
    
    public double distance(double[] vec0, int[] vec1) {
    	return 0;
    }
    
    public double distance(int[] vec0, int[] vec1, double c) {
        return distance(vec0, vec1, emptyList);
    }
    
    public double distance(int[] vec0, double[] vec1, double c) {
    	return 0;
    }
    
    public double distance(double[] vec0, int[] vec1, double c) {
    	return 0;
    }
    
    public double distance(double[] vec0, double[] vec1, double c) {
    	return 0;
    }
    
    
    /*
    public double distance(double[] vec0, int[] vec1) {
        return distance(vec0, vec1, emptyList);
    }
	*/
    
    public double getScaleFactor(int[] vec0, int[] vec1) {
    	return 0;
    }
    
    public double getScaleFactor(int[] vec0, double[] vec1) {
    	return 0;
    }
    
    public double getScaleFactor(double[] vec0, int[] vec1) {
    	return 0;
    }
    
    
    
    @Override
    public double distance(int[] vec0, int[] vec1, List<Integer> excluded) {
        assert vec0 != null && vec1 != null : "Trying to compute distance "
                + "between null vectors.";
        assert vec0.length == vec1.length : "Trying to compute distance "
                + "between vectors of different lengths.";

        double sum = 0;
        int excludedIx = 0;
        for (int i = vec0.length - 1; i >= 0; --i) {
            if (excludedIx < excluded.size() && excluded.get(excludedIx) == i) {
                ++excludedIx;
            } else {
                sum += weights.weight(i) * entryDistance(vec0[i], vec1[i]);
            }
        }

        return sum;
    }
    
    public EntryWeights getWeights() {
    	return this.weights;
    }
    
    public EntryWeights getScaleWeights() {
    	return this.scaleWeights;
    }
    /*
    public double distance(double[] vec0, int[] vec1, List<Integer> excluded) {
        assert vec0 != null && vec1 != null : "Trying to compute distance "
                + "between null vectors.";
        assert vec0.length == vec1.length : "Trying to compute distance "
                + "between vectors of different lengths.";

        double sum = 0;
        int excludedIx = 0;
        for (int i = vec0.length - 1; i >= 0; --i) {
            if (excludedIx < excluded.size() && excluded.get(excludedIx) == i) {
                ++excludedIx;
            } else {
                sum += weights.weight(i) * entryDistanceDouble(vec0[i], vec1[i]);
            }
        }

        return sum;
    }
     */
    public double distanceIncl(int[] vec0, int[] vec1, List<Integer> included) {
        assert vec0 != null && vec1 != null : "Trying to compute distance "
                + "between null vectors.";
        assert vec0.length == vec1.length : "Trying to compute distance "
                + "between vectors of different lengths.";

        double sum = 0;
        for (int i = 0; i < included.size(); i++) {
            int index = included.get(i);
            sum += weights.weight(index) * entryDistance(vec0[index], vec1[index]);
        }
        return sum;
    }
    
    public double distanceIncl(double[] vec0, double[] vec1, List<Integer> included) {
        assert vec0 != null && vec1 != null : "Trying to compute distance "
                + "between null vectors.";
        assert vec0.length == vec1.length : "Trying to compute distance "
                + "between vectors of different lengths.";

        double sum = 0;
        for (int i = 0; i < included.size(); i++) {
            int index = included.get(i);
            sum += weights.weight(index) * entryDistance(vec0[index], vec1[index]);
        }
        return sum;
    }
        
    public abstract double[] getEntrywiseDistanceVec(int[] vec0, int[] vec1);
    
    public abstract double[] getEntrywiseDistanceVec(int[] vec0, double[] vec1);
    
    public abstract double[] getEntrywiseDistanceVec(double[] vec0, int[] vec1);
    
    public abstract double[] getEntrywiseDistanceVec(double[] vec0, double[] vec1);
    
    public double[] getEntrywiseDistance(int[] vec0, int[] vec1) {
    	assert vec0 != null && vec1 != null : "Trying to compute distance "
                + "between null vectors.";
        assert vec0.length == vec1.length : "Trying to compute distance "
                + "between vectors of different lengths.";

        double[] entrywiseDistance = new double[vec0.length];
        for (int index = 0; index < vec0.length; index++) {
            entrywiseDistance[index] = weights.weight(index)*entryDistance(vec0[index],vec1[index]);            
        }
        return entrywiseDistance;
    }
    
    public double distance(double[] vec0, double[] vec1) {
        assert vec0 != null && vec1 != null : "Trying to compute distance "
                + "between null vectors.";
        assert vec0.length == vec1.length : "Trying to compute distance "
                + "between vectors of different lengths.";

        double sum = 0;
        for (int index = 0; index < vec0.length; index++) {
            //int index = included.get(i);
        	if (index % 38665 ==0) {
        	//	System.out.println(index + "\t" + sum);
        	}
            sum += weights.weight(index) * entryDistance(vec0[index], vec1[index]);
            
        }
        return sum;
    }
    
    
    
    private static double getSum(double[] barcode) {
        double sum = 0;
        for (int i = 0; i < barcode.length; ++i) {
            sum += barcode[i];
        }
        return sum;
    }
    
    private static int getSum(int[] barcode) {
        int sum = 0;
        for (int i = 0; i < barcode.length; ++i) {
            sum += barcode[i];
        }
        return sum;
    }
    /*
    public double distanceIncl(double[] vec0, int[] vec1, List<Integer> included) {
        assert vec0 != null && vec1 != null : "Trying to compute distance "
                + "between null vectors.";
        assert vec0.length == vec1.length : "Trying to compute distance "
                + "between vectors of different lengths.";

        double sum = 0;
        for (int i = 0; i < included.size(); i++) {
            int index = included.get(i);
            sum += weights.weight(index) * entryDistanceDouble(vec0[index], vec1[index]);
        }
        return sum;
    }
    */
    /*
    public double distanceIncl(int[] vec0, int[] vec1, int depth, List<Integer> included) {
        assert vec0 != null && vec1 != null : "Trying to compute distance "
                + "between null vectors.";
        assert vec0.length == vec1.length : "Trying to compute distance "
                + "between vectors of different lengths.";

        double sum = 0;
        for (int i = 0; i < included.size(); i++) {
            int index = included.get(i);
            sum += entryDistanceDouble((double)vec0[index]/(double)depth, vec1[index]);
        }
        return sum;
    }
	*/
    /**
     * Computes the distance between two integer entries.
     *
     * @param a the value of the first entry.
     * @param b the value of the second entry.
     * @return the distance between the entries.
     */
    public abstract double entryDistance(int a, int b);
        
    //protected abstract double entryDistanceDouble(double a, int b);
    
    public abstract double entryDistance(double a, double b);

    public void setWeights(EntryWeights weights) {
        this.weights = weights;
    }
    
    public void setScaleWeights(EntryWeights weights) {
    	this.scaleWeights = weights;
    }
    
    public static double[] ratioAnalisis(int[] vec0, int[] vec1, int bins){
    	double epsilone = 0.0001;
    	int n = vec0.length;
		double[] ratios = new double[n];
    	double sum = 0;
    	double sumOfSquares = 0;
    	double logSum = 0;
    	double sumOfLogSquares = 0;
    	
    	int sum0 = 0, sum1 = 0;
    	
    	for (int i=0; i<n; ++i){
    		sum0 += vec0[i];
    		sum1 += vec1[i];
			double ratio = (vec0[i] + epsilone) / (vec1[i] + + epsilone);
			ratios[i] = ratio;
			sum += ratio;
			sumOfSquares += ratio * ratio;
			double logRatio = Math.log(ratio);
			logSum += logRatio;
			sumOfLogSquares += logRatio * logRatio;
    	}
    	
    	double average = sum/n;
    	double std = Math.sqrt(sumOfSquares/n - average*average);
    	double logAverage = logSum/n;
		double expLogAverage = Math.exp(logAverage);
    	double expLogStd = Math.exp(Math.sqrt(sumOfLogSquares/n - logAverage*logAverage));
    	Arrays.sort(ratios);
    	double median = ratios[n/2];
    	double sumRatio = ((double) sum0) / sum1;

//		System.out.println(
////				"Average: " + average + ", std: " + std + ", " + 
//    			"exp log average: " + expLogAverage + ", exp log std: " 
//    			+ expLogStd + ", median: " + median + ", sum ratio: " + sumRatio);
    	
    	return new double[] {average, std, expLogAverage, expLogStd, median, sumRatio};
    	
//    	int bin = 0;
//    	int start = 0;
//    	for (int )
    	
    }
    
    public static int[] scale(int[] vec, int factor, double std){
    	int n = vec.length;
		int[] scaled = new int[n];
		return scaled;
    }
    
    /* 
    public static void main(String[] args) throws IOException{
    	String templatePath = "Data/Barcodes/Templates50/";
    	String simPath = "Data/Barcodes/TemplateRefReads50/";
    	
    	int bins = 10;
    	
		File templateDir = new File(templatePath);
		String[] templateFileNames = templateDir.list();
		Arrays.sort(templateFileNames, Typing.templateComparator);

		File simDir = new File(simPath);
		String[] simFileNames = simDir.list();
		Arrays.sort(simFileNames);
		
		double[] ratioStat = new double[4];
		List<Double> stds = new ArrayList<Double>();

		//VecDistance dist = new StdVecDistance();
		//VecDistance dist = new PresenceVecDistance();
		
		VecDistance dist = new PoissonVecDistance();
		
		int[] typeElements = new int[7]; // {11, 1, 3, 3, 1, 3, 2};
		double[] typeMaxDistances = new double[7];
		double[] typeMinDistances = new double[7];
		double[] typeAveDistances = new double[7];
		
		List<String> simTemplates = new ArrayList<String>();
		List<int[]> simBarcodes = new ArrayList<int[]>();
		
		for (int j=0; j<simFileNames.length; ++j){
			String file = simFileNames[j];
			if (!file.endsWith(".barcod")){
				continue;
			}
			
			String template = file.substring(4, file.indexOf(".barcod"));
			simTemplates.add(template);
			simBarcodes.add(KMerFileReader.getBarcodeFromFile(simPath + File.separatorChar + simFileNames[j]));
			++typeElements[Typing.getTypeIx(template)];
		}

		
		for (int i=0; i<templateFileNames.length; ++i){
			String file = templateFileNames[i];
			if (!file.endsWith(".barcod")){
				continue;
			}
			String template = file.substring(0, file.indexOf('.'));
			System.out.println("Template: " + template + ", type: " + Typing.getType(template));
			
			int[] templateVec = KMerFileReader.getBarcodeFromFile(templatePath + File.separatorChar + templateFileNames[i]);
			
			int n = 0;
			Arrays.fill(ratioStat, 0);
			Arrays.fill(typeMaxDistances, Double.NEGATIVE_INFINITY);
			Arrays.fill(typeMinDistances, Double.POSITIVE_INFINITY);
			Arrays.fill(typeAveDistances, 0);
			stds.clear();
			
			for (int j=0; j<simTemplates.size(); ++j){
				int[] simVec = simBarcodes.get(j);
				double distance = dist.distance(simVec, templateVec);
				stds.add(distance);
				double[] ratioAnalisis = ratioAnalisis(simVec, templateVec, bins);
				for (int k = 0; k <4; ++k){
					ratioStat[k] += ratioAnalisis[k+2];
				}
				
				String simTemplate = simTemplates.get(j);
				if (template.equals(simTemplate)){
					System.out.println("Correct simulated read set score: " + distance);
//					System.out.println(template);
//					System.out.println(
////							"Average: " + average + ", std: " + std + ", " + 
//			    			"exp log average: " + ratioAnalisis[2] + ", exp log std: " 
//			    			+ ratioAnalisis[3] + ", median: " + ratioAnalisis[4] + 
//			    			", sum ratio: " + ratioAnalisis[5] + ", dist: " + distance);
////					break;
				}
				
				int simTypeIx = Typing.getTypeIx(simTemplate);
				typeAveDistances[simTypeIx] += distance;
				if (typeMaxDistances[simTypeIx] < distance){
					typeMaxDistances[simTypeIx] = distance;
				}
				if (typeMinDistances[simTypeIx] > distance){
					typeMinDistances[simTypeIx] = distance;
				}

			}
			
			
//			Collections.sort(stds);
//			System.out.println("Avarage results:\n" +
//					"exp log average: " + ratioStat[0]/n + ", exp log std: " 
//					+ ratioStat[1]/n + ", median: " + ratioStat[2]/n + 
//					", sum ratio: " + ratioStat[3]/n);
//			System.out.println(stds);
			
			for (int t=0; t<7; ++t){
				System.out.println(Typing.getTypeName(t) + ") ave: " + typeAveDistances[t] / typeElements[t]
						+ ", min: " + typeMinDistances[t] + ", max: " + typeMaxDistances[t]);
			}
			
			System.out.println();

		}
	
    }
    */
}
