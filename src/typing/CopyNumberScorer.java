package typing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import distanceFunction.*;

public class CopyNumberScorer {
//	private static HashMap<String, int[]> typeCopyNumbers;
//	private static HashMap<String, int[]> diploidTypeCopyNumbers;
//	private static HashMap<Integer, Double> copyNumberToValue;
//	private static int numGenes;

	private EntrywiseVecDistance distanceCalculator; 
	
	
	static{
//		numGenes = Typing.getGeneTests().size();
		
//		typeCopyNumbers = new HashMap<String, int[]>();
//		//-8,-15
//		typeCopyNumbers.put("A", new int[] {1,0,1,0,0,0,1,1,1,0,1,0,1});
//		typeCopyNumbers.put("AB",new int[] {1,0,1,0,1,1,1,1,1,1,0,1,0});
//		typeCopyNumbers.put("BA1",new int[] {1,1,0,1,0,0,0,1,1,0,1,0,1});
//		typeCopyNumbers.put("BA2X",new int[] {1,0,1,0,1,1,1,1,1,0,1,0,1});
//		typeCopyNumbers.put("BA2",new int[] {1,1,0,1,1,1,1,1,1,0,1,0,1});
//		typeCopyNumbers.put("Bdel",new int[] {1,1,0,1,1,1,0,1,1,1,0,1,0});
//		typeCopyNumbers.put("B",new int[] {1,1,0,1,2,2,1,1,1,1,0,1,0});
//		//-15
//		typeCopyNumbers.put("A", new int[] {1,0,1,0,0,0,1,1,1,1,0,1,0,1});
//		typeCopyNumbers.put("AB",new int[] {1,0,1,0,1,1,1,1,1,1,1,0,1,0});
//		typeCopyNumbers.put("BA1",new int[] {1,1,0,1,0,0,0,0,1,1,0,1,0,1});
//		typeCopyNumbers.put("BA2X",new int[] {1,0,1,0,1,1,1,1,1,1,0,1,0,1});
//		typeCopyNumbers.put("BA2",new int[] {1,1,0,1,1,1,1,1,1,1,0,1,0,1});
//		typeCopyNumbers.put("Bdel",new int[] {1,1,0,1,1,1,0,0,1,1,1,0,1,0});
//		typeCopyNumbers.put("B",new int[] {1,1,0,1,2,2,1,1,1,1,1,0,1,0});
//		//all included	
//		typeCopyNumbers.put("A", new int[] 	{1,0,1,0,0,0,1,1,1,1,0,1,0,1,1});
//		typeCopyNumbers.put("AB",new int[] 	{1,0,1,0,1,1,1,1,1,1,1,0,1,0,1});
//		typeCopyNumbers.put("BA1",new int[] {1,1,0,1,0,0,0,0,1,1,0,1,0,1,1});
//		typeCopyNumbers.put("BA2X",new int[]{1,0,1,0,1,1,1,1,1,1,0,1,0,1,1});
//		typeCopyNumbers.put("BA2",new int[] {1,1,0,1,1,1,1,1,1,1,0,1,0,1,1});
//		typeCopyNumbers.put("Bdel",new int[]{1,1,0,1,1,1,0,0,1,1,1,0,1,0,1});
//		typeCopyNumbers.put("B",new int[] 	{1,1,0,1,2,2,1,1,1,1,1,0,1,0,1});
////		//-8
//		typeCopyNumbers.put("A", new int[] 	{1,0,1,0,0,0,1,1,1,0,1,0,1,1});
//		typeCopyNumbers.put("AB",new int[] 	{1,0,1,0,1,1,1,1,1,1,0,1,0,1});
//		typeCopyNumbers.put("BA1",new int[] {1,1,0,1,0,0,0,1,1,0,1,0,1,1});
//		typeCopyNumbers.put("BA2X",new int[] {1,0,1,0,1,1,1,1,1,0,1,0,1,1});
//		typeCopyNumbers.put("BA2",new int[] {1,1,0,1,1,1,1,1,1,0,1,0,1,1});
//		typeCopyNumbers.put("Bdel",new int[] {1,1,0,1,1,1,0,1,1,1,0,1,0,1});
//		typeCopyNumbers.put("B",new int[] 	{1,1,0,1,2,2,1,1,1,1,0,1,0,1});
		

		
//		diploidTypeCopyNumbers = new HashMap<String, int[]>();
//		for (Iterator typeIt = typeCopyNumbers.keySet().iterator(); typeIt.hasNext(); ) {
//			String type1 = (String) typeIt.next();
//			for (Iterator typeIt2 = typeCopyNumbers.keySet().iterator(); typeIt2.hasNext(); ) {
//				String type2 = (String) typeIt2.next();
//				String[] temp = new String[2];
//				temp[0] = type1;
//				temp[1] = type2;
//				Arrays.sort(temp);
//				int[] cn = new int[typeCopyNumbers.get(type1).length];
//				for (int i = 0; i < cn.length; ++i) {
//					cn[i] = typeCopyNumbers.get(type1)[i] + typeCopyNumbers.get(type2)[i];
//				}
//				diploidTypeCopyNumbers.put(temp[0] + "_" + temp[1], cn);
//			}
//		}
		
		
//		copyNumberToValue = new HashMap<Integer, Double>();
//		copyNumberToValue.put(0, 0.0);
//		copyNumberToValue.put(1, 0.75);
//		copyNumberToValue.put(2, 1.5);
//		copyNumberToValue.put(3, 2.25);
//		copyNumberToValue.put(4, 3.0);
		

	}
	
	public CopyNumberScorer() {		
		//this.distanceCalculator = new NormalVecDistance();
		//this.distanceCalculator = new NormalCDFVecDistance();
		this.distanceCalculator = new ZVecDistance();
	}

	public double getDistance(double[] sampleBarcode, double[] templateBarcode) {
		return distanceCalculator.distance(sampleBarcode, templateBarcode, 1.0);
	}
	
	private HashMap<String, double[]> getTypeValues() {
		HashMap<String, double[]> typeValues = new HashMap<String, double[]>();
		//double[][] typeValues = new double[typeCopyNumbers.size()][];
		int currIndx = 0;
		for (Iterator it = Typing.typeCopyNumbers.keySet().iterator(); it.hasNext(); ) {
			String key = (String) it.next();
			//int[] copyNumber = (int[]) it.next();
			int[] copyNumber = Typing.typeCopyNumbers.get(key);
			//typeValues[currIndx] = new double[copyNumber.length];
			double[] temp = new double[copyNumber.length];
			for (int i = 0; i < copyNumber.length; ++i) {
				temp[i] = copyNumber[i]*1.0;
				//typeValues[currIndx][i] = copyNumber[i]*1.0;
			}
			typeValues.put(key, temp);
			//currIndx += 1;
		}
		return typeValues;
	}
	
	private HashMap<String, double[]> getDiploidTypeValues() {
		HashMap<String, double[]> typeValues = new HashMap<String, double[]>();
		//double[][] typeValues = new double[typeCopyNumbers.size()][];
		int currIndx = 0;
		for (Iterator it = Typing.diploidTypeCopyNumbers.keySet().iterator(); it.hasNext(); ) {
			String key = (String) it.next();
			//int[] copyNumber = (int[]) it.next();
			int[] copyNumber = Typing.diploidTypeCopyNumbers.get(key);
			//typeValues[currIndx] = new double[copyNumber.length];
			double[] temp = new double[copyNumber.length];
			for (int i = 0; i < copyNumber.length; ++i) {
				temp[i] = copyNumber[i]*1.0;
				//typeValues[currIndx][i] = copyNumber[i]*1.0;
			}
			typeValues.put(key, temp);
			//currIndx += 1;
		}
		return typeValues;
	}
	
	public TemplateScores scoreSampleHaploid(double[] sampleValues) {
		
		double[] scores = new double[Typing.typeCopyNumbers.size()];
		double[][] entrywiseScores = new double[Typing.typeCopyNumbers.size()][];
		double[] scales = new double[Typing.typeCopyNumbers.size()];
		String[] types = new String[Typing.typeCopyNumbers.size()];
		ArrayList<String> typesList = new ArrayList<String>();


		int counter = 0;
		for (Iterator it = Typing.typeCopyNumbers.keySet().iterator(); it.hasNext(); ) {
			String key = (String) it.next();
			double score = distanceCalculator.distance(sampleValues, Typing.typeCopyNumbers.get(key));
			scores[counter] = score;
			entrywiseScores[counter] = distanceCalculator.getEntrywiseDistanceVec(sampleValues, Typing.typeCopyNumbers.get(key));
			scales[counter] = distanceCalculator.getScaleFactor(sampleValues, Typing.typeCopyNumbers.get(key));
			types[counter] = key;
			typesList.add(key);
			counter += 1;
		}
		return new TemplateScores(scores, entrywiseScores, scales, types, typesList, Typing.geneTests.size());
	}
	
	public TemplateScores scoreSample(double[] sampleValues) {
		
		double[] scores = new double[Typing.diploidTypeCopyNumbers.size()];
		double[][] entrywiseScores = new double[Typing.diploidTypeCopyNumbers.size()][];
		double[] scales = new double[Typing.diploidTypeCopyNumbers.size()];
		String[] types = new String[Typing.diploidTypeCopyNumbers.size()];
		ArrayList<String> typesList = new ArrayList<String>();

		int counter = 0;
		for (Iterator it = Typing.diploidTypeCopyNumbers.keySet().iterator(); it.hasNext(); ) {
			String key = (String) it.next();
			double score = distanceCalculator.distance(sampleValues, Typing.diploidTypeCopyNumbers.get(key));
			scores[counter] = score;
			entrywiseScores[counter] = distanceCalculator.getEntrywiseDistanceVec(sampleValues, Typing.diploidTypeCopyNumbers.get(key));
			scales[counter] = distanceCalculator.getScaleFactor(sampleValues, Typing.diploidTypeCopyNumbers.get(key));
			types[counter] = key;
			typesList.add(key);
			counter += 1;
		}
		return new TemplateScores(scores, entrywiseScores, scales, types, typesList, Typing.geneTests.size());
	}
	
//	public ArrayList<String> rankTypesOfSample(double[] sampleValues) {
//		System.out.println();
//		double bestScore = Double.MAX_VALUE;
//		ArrayList<String> bestTypes = new ArrayList<String>();
//		for (Iterator it = diploidTypeCopyNumbers.keySet().iterator(); it.hasNext(); ) {
//			String key = (String) it.next();
//			double score = distanceCalculator.distance(sampleValues, diploidTypeCopyNumbers.get(key));
//			System.out.println("\t" + key + "\t" + score);
//			if (score < bestScore) {
//				bestScore = score;
//				bestTypes = new ArrayList<String>();
//				bestTypes.add(key);
//			} else if (score == bestScore) {
//				bestTypes.add(key);
//			} 
//
//		}
//		return bestTypes;
//		
//	}
	
	public CopyNumbers getCopyNumberOfSampleHaploid(double[] sampleValues) {
			return getCopyNumberOfSampleHaploid1(sampleValues);
		}
	
	public CopyNumbers getCopyNumberOfSample(double[] sampleValues) {
		return getCopyNumberOfSample1(sampleValues);
	}
	
	//get highest ranked template and use it as start for simulated annealing
	public CopyNumbers getCopyNumberOfSample1(double[] sampleValues) {
		TemplateScores tscores = scoreSample(sampleValues);
		CopyNumbers[] ranked = tscores.rankTemplates();
		return getCopyNumberOfSample2(sampleValues, ranked[0].getIntCopyNumbers());
	}
	
	public CopyNumbers getCopyNumberOfSampleHaploid1(double[] sampleValues) {
		TemplateScores tscores = scoreSampleHaploid(sampleValues);
		CopyNumbers[] ranked = tscores.rankTemplatesHaploid();
		return getCopyNumberOfSample2(sampleValues, ranked[0].getIntCopyNumbers());
	}
	
	//Simulated annealing s (starting with given copy number)
	public CopyNumbers getCopyNumberOfSample2(double[] sampleValues, int[] startCopyNumber ) {
		double distance = distanceCalculator.distance(sampleValues, startCopyNumber);
		double bestDistance = distance;

		double s = distanceCalculator.getScaleFactor(sampleValues, startCopyNumber);
		
		int[] sampleCopyNumber = new int[startCopyNumber.length];
		int[] bestSampleCopyNumber = new int[startCopyNumber.length];
		for (int i = 0; i < startCopyNumber.length; ++i) {
			sampleCopyNumber[i] = startCopyNumber[i];
			bestSampleCopyNumber[i] = startCopyNumber[i];
		}
		
		int t = 0; 
		int tmax = 50;
		while (t<tmax) { 
			//Get neighbor
			double news = getNeighbors(s); 
			int[] newSampleCopyNumber = getCopyNumberOfSample3(sampleValues, news);
			//Get distance of neighbor
			double newDistance = distanceCalculator.distance(sampleValues, newSampleCopyNumber);
			double temp = getTemp(t, tmax);
			if (probabilityOfMoving(distance, newDistance, temp) > Math.random() ) { 
				for (int i = 0; i < sampleCopyNumber.length; ++i) {
					sampleCopyNumber[i] = newSampleCopyNumber[i]; 
				}
				distance = newDistance;
				s = news;
			}
			if (newDistance < bestDistance) {
				for (int i = 0; i < bestSampleCopyNumber.length; ++i) {
					bestSampleCopyNumber[i] = newSampleCopyNumber[i];
				}
				bestDistance = newDistance;
					
			}
			t += 1;
		}
		double[] bestEntrywiseDistance = distanceCalculator.getEntrywiseDistanceVec(sampleValues, bestSampleCopyNumber);
		double finals = distanceCalculator.getScaleFactor(sampleValues, bestSampleCopyNumber);
		CopyNumbers bestCopyNumbers = new CopyNumbers(bestSampleCopyNumber, bestDistance, bestEntrywiseDistance, finals);
		return bestCopyNumbers;

	}
	
	//Get best copy number given s
	public int[] getCopyNumberOfSample3(double[] sampleValues, double s) {
		//double s = 0.75;
		int[] sampleCopyNumber = new int[sampleValues.length];
		
		for (int i = 0; i < sampleCopyNumber.length; ++i) {
			if (sampleValues[i]==0) {
				sampleCopyNumber[i] = 0;
			} else {
			int prevCopyNumber = -1;
			double prevScore = Double.POSITIVE_INFINITY;
			while (true) {
				int copyNumber = prevCopyNumber+1;
				double temp = distanceCalculator.entryDistance(sampleValues[i], copyNumber*s);
				if (temp <= prevScore) {
					prevCopyNumber = copyNumber;
					prevScore = temp;
				} else {
					break;
				}
			}
			sampleCopyNumber[i] = prevCopyNumber;
			}
		}
		return sampleCopyNumber;
	}
	
	//Simulated annealing s
	public CopyNumbers getCopyNumberOfSample4(double[] sampleValues, int copyNumFirstGene) {
		double s = sampleValues[0]/copyNumFirstGene;
		double bests = s;
		int[] sampleCopyNumber = getCopyNumberOfSample3(sampleValues, s);
		int[] bestSampleCopyNumber = getCopyNumberOfSample3(sampleValues, bests);
		double distance = distanceCalculator.distance(sampleValues, sampleCopyNumber);
		//double[] entrywiseDistance = distanceCalculator.getEntrywiseDistanceVec(sampleValues, sampleCopyNumber);
		double bestDistance = distance;
		//double[] bestEntrywiseDistance = entrywiseDistance;
		int t = 0; 
		int tmax = 50;
		while (t<tmax) { 
			//System.out.print("\t" + s);
			//System.out.println("\t" + distance);
			//Get neighbor
			double news = getNeighbors(s); 
			int[] newSampleCopyNumber = getCopyNumberOfSample3(sampleValues, news);
			//Get distance of neighbor
			double newDistance = distanceCalculator.distance(sampleValues, newSampleCopyNumber);
			//double[] newEntrywiseDistance = distanceCalculator.getEntrywiseDistanceVec(sampleValues, newSampleCopyNumber);
			double temp = getTemp(t, tmax);
			if (probabilityOfMoving(distance, newDistance, temp) > Math.random() ) { 
				for (int i = 0; i < sampleCopyNumber.length; ++i) {
					sampleCopyNumber[i] = newSampleCopyNumber[i]; 
				}
				distance = newDistance;
				//entrywiseDistance = newEntrywiseDistance;
				s = news;
			}
			if (newDistance < bestDistance) {
				for (int i = 0; i < bestSampleCopyNumber.length; ++i) {
					bestSampleCopyNumber[i] = newSampleCopyNumber[i];
				}
				bestDistance = newDistance;
				//bestEntrywiseDistance = newEntrywiseDistance;
				bests = news;
					
			}
			t += 1;
		}
		//System.out.print("\t"+bestDistance);
		double[] bestEntrywiseDistance = distanceCalculator.getEntrywiseDistanceVec(sampleValues, bestSampleCopyNumber);
		double finals = distanceCalculator.getScaleFactor(sampleValues, bestSampleCopyNumber);
		CopyNumbers bestCopyNumbers = new CopyNumbers(bestSampleCopyNumber, bestDistance, bestEntrywiseDistance, finals);
		//return bestSampleCopyNumber;
		return bestCopyNumbers;

	}
		

	
	private double getNeighbors(double s) {
		double r = Math.random();
		double neighbors;
		if (0.5 > r) {
			if (0.2 > r) {
				neighbors = s + 0.25;
			} else if (0.4 > r) {
				neighbors = s+ 0.125;
			} else {
				neighbors =  s + 0.05;
			}
		} else {
			if (0.6 > r) {
				neighbors =  s - 0.05;
			} else if (0.8 > r){
				neighbors = s - 0.125;
			} else {
				neighbors =  s - 0.25;
			}
		}
//		if ((0 < neighbors) && (neighbors < 1.1)) {
//			return neighbors;
//		} else {
//			return s;
//		}
		return neighbors;
	}
	
	private int[] getNeighbor(double[] sampleValues, int[] sampleCopyNumber, double s) {
		int[] neighborCopyNumber = new int[sampleValues.length];
		
		for (int i = 0; i < neighborCopyNumber.length; ++i) {
			if (sampleValues[i] < sampleCopyNumber[i]*s) {
				if (Math.exp(-(sampleCopyNumber[i]*s-sampleValues[i])) > Math.random()) {
					neighborCopyNumber[i] = sampleCopyNumber[i]-1;
				} else {
					neighborCopyNumber[i] = sampleCopyNumber[i];
				}
			} else {
				if ((Math.exp((sampleCopyNumber[i]*s-sampleValues[i])) > Math.random())) {
					neighborCopyNumber[i] = sampleCopyNumber[i]+1;
				} else {
					neighborCopyNumber[i] = sampleCopyNumber[i];
				}
			}
		}
		return neighborCopyNumber;
	}
	
	private double getTemp(int t, int tmax) {
		return (t+0.0)/tmax; 
	}
	
	private double probabilityOfMoving(double distance, double newDistance, double temp) {
		if (distance > newDistance) {
			return 1.0; 
		} else {
			return Math.exp(-(newDistance-distance)*temp*10);
		}
	}
	
	
	

	
}
