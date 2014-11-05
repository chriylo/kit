package typing;

import java.io.File;
import java.util.*;

import clusterFunctions.*;
import distanceFunction.EntryWeights;
import distanceFunction.EntrywiseVecDistance;
import distanceFunction.HammingClusterBarcodeDistance;
import distanceFunction.RatioPowerVecDistance;
import barcodeFunctions.BarcodeFunctions;

public class TemplateScores {
	
	private double[] scores;
	private String[] templateTypes;
	private ArrayList<String> templateNames;
	HashMap<String, Integer> templateIndx; 
	HashMap<Integer, String> indxToType;
	//private double[] probabilities; 
	private int numKmers;
	private double[] scales;
	private double[][] entrywiseScores;
	
	
	//private double[][] typeScores;
	private HashMap<String, ArrayList<Double>> typeScores;
	
	private double[] typeAvg;
	private double[] typeMin;
	private double[] typeMax;
	
	private double[] typeProbMax;
	private double[] typeProbAvg;
	private double[] typeProb;
	
	public TemplateScores(double[] scores, String[] templateTypes,ArrayList<String> templateNames) {
		this.scores = scores;
		this.templateTypes = templateTypes;
		this.templateNames = templateNames;
		this.numKmers = 1;
		
		//fillTypeScores();
		//fillTypeProb();
		
		
	}
	
//	public TemplateScores(double[] scores, String[] templateTypes2, ArrayList<String> templateNames2, int numKmers2) {
//		this.scores = scores;
//		this.templateTypes = templateNames2;
//		this.templateNames = numKmers2;
//		this.numKmers = 1;
//		this.scales = templateTypes2;
//		
//		//fillTypeScores();
//		//fillTypeProb();
//		
//		
//	}
	
	
	public TemplateScores(double[] scores, String[] templateTypes, ArrayList<String> templateNames2, int numKmers) {
		this.scores = scores;
		this.templateTypes = templateTypes;
		this.templateNames = templateNames2;
		this.numKmers = numKmers;
		
		//fillTypeScores();
		//fillTypeProb();
		//normalizeScores();
		
	}
	
	public TemplateScores(double[] scores, double[] scales, String[] templateTypes, ArrayList<String> templateNames, int numKmers) {
		this.scores = scores;
		this.templateTypes = templateTypes;
		this.templateNames = templateNames;
		this.numKmers = numKmers;
		this.scales = scales;
		
		
		//fillTypeScores();
		//fillTypeProb();
		//normalizeScores();
		
	}
	
	public TemplateScores(double[] scores, double[][] entrywiseScores, double[] scales, String[] templateTypes, ArrayList<String> templateNames, int numKmers) {
		this.scores = scores;
		this.templateTypes = templateTypes;
		this.templateNames = templateNames;
		this.numKmers = numKmers;
		this.scales = scales;
		this.entrywiseScores = entrywiseScores;
		
		
		//fillTypeScores();
		//fillTypeProb();
		//normalizeScores();
		
	}
	
	private void normalizeScores() {
		double[] tempScores = new double[scores.length];
		for (int i = 0; i < scores.length ; ++i ) {
			tempScores[i] = scores[i]/numKmers;
		}
		double sum = BarcodeFunctions.getSum(tempScores);
		
		for (int i = 0; i < tempScores.length; ++i) {
			scores[i] = (tempScores[i]/sum);
		}
		
	}
	
	private double min(ArrayList<Double> list) {
		double min = Double.MAX_VALUE;
		for (int i = 0; i < list.size(); ++i) {
			if (list.get(i) < min) {
				min = list.get(i);
			}
		}
		return min;
	}
	
	private double max(ArrayList<Double> list) {
		double max = Double.MIN_VALUE;
		for (int i = 0; i < list.size(); ++i) {
			if (list.get(i) > max) {
				max = list.get(i);
			}
		}
		return max;
	}
	
	public double[] getScores() {
		return this.scores;
	}
	
	public double[] getScales() {
		return this.scales;
	}
	
	public double getScoreOfTemplate(int i) {
		return this.scores[i];
	}
	
	public ArrayList<String> getBestTemplates() {
		ArrayList<String> bestTemplates = new ArrayList<String>();
		double bestScore = BarcodeFunctions.min(this.scores);
		for (int i = 0; i < this.scores.length; ++i) {
			if (this.scores[i] == bestScore) {
				bestTemplates.add(templateNames.get(i));
			}
		}
		return bestTemplates;
	}
	
	
	
	//public double[] getProbabilities() {
	//	return this.probabilities;
	//}
	
	public ArrayList<String> getBestTypesProb() {
		ArrayList<String> bestTypes = new ArrayList<String>();
		
		double bestProb = BarcodeFunctions.max(typeProb);
		for (int i = 0; i < typeProb.length; ++i) {
			if (typeProb[i]==bestProb) {
				bestTypes.add(indxToType.get(i));
			}
		}
		return bestTypes;
	}
	
	public ArrayList<String> getBestTypesMin() {
		ArrayList<String> bestTypes = new ArrayList<String>();
		
		double bestScore = BarcodeFunctions.min(this.scores);
		for (int i = 0; i < this.scores.length; ++i) {
			if (this.scores[i] == bestScore) { 
				bestTypes.add(templateTypes[i]) ; 
			}
		}
		return bestTypes;
	}
	
	public ArrayList<String> getBestTypes() {
		assert templateTypes != null : "Unknown template types";
		//return getBestTypesProb();
		return getBestTypesMin();
	}
	
	
	/*
	public ArrayList<String> getBestTypes(int exclude) {
		assert templateTypes != null : "Unknown template types";
		
		ArrayList<String> bestTypes = new ArrayList<String>();
		double bestScore = BarcodeFunctions.min(this.scores, exclude);
		for (int i = 0; i < this.scores.length; ++i) {
			if (this.scores[i] == bestScore) { 
				bestTypes.add(templateTypes[i]) ; 
			}
		}
		return bestTypes;
	}
	*/
	public double getBestScore() {
		return BarcodeFunctions.min(this.scores);
		
	}
	
	public void fillTypeScores() {
		assert templateTypes != null : "Unknown template types";
		templateIndx = new HashMap<String, Integer>();
		indxToType = new HashMap<Integer, String>();
		int currIndx = 0;
		for (int i = 0; i < templateTypes.length; ++i) {
			if (!templateIndx.containsKey(templateTypes[i])) {
				templateIndx.put(templateTypes[i], currIndx);
				indxToType.put(currIndx, templateTypes[i]);
				currIndx += 1;
			}
		}
		int[] typeCount = new int[templateIndx.size()];
		double[] typeSum = new double[templateIndx.size()];
		typeMin = new double[templateIndx.size()];
		typeMax = new double[templateIndx.size()];
		typeProb = new double[templateIndx.size()];
		
		typeScores = new HashMap<String, ArrayList<Double>>();
		for (Iterator it = templateIndx.keySet().iterator(); it.hasNext(); ) {
			String key = (String) it.next(); 
			typeScores.put(key, new ArrayList<Double>());
		}
		
		Arrays.fill(typeCount, 0);
		Arrays.fill(typeSum, 0);
		Arrays.fill(typeMin, Double.POSITIVE_INFINITY);
		Arrays.fill(typeMax, Double.NEGATIVE_INFINITY);
		for (int i = 0; i < scores.length; ++i) {
			int indx = templateIndx.get(templateTypes[i]);
			typeCount[indx]+=1;
			typeSum[indx]+=scores[i];
			if (typeMin[indx]>scores[i]) { typeMin[indx] = scores[i]; }
			if (typeMax[indx]<scores[i]) { typeMax[indx] = scores[i]; }
			
			typeScores.get(templateTypes[i]).add(scores[i]);
		}

		typeAvg = new double[templateIndx.size()];
		for (int i = 0; i < typeSum.length; ++i) {
			typeAvg[i] = typeSum[i]/typeCount[i];
		} 
	}
	
//	private void fillTypeProb2() {
//		int[] ranked = rankTemplates();
//		double[] pvalues = new double[templateIndx.size()];
//		for (Iterator it = templateIndx.keySet().iterator(); it.hasNext(); ) {
//			String type = (String) it.next(); 
//			int uType = 0;
//			int numType = 0;
//			int uOther = 0;
//			int numOther = 0;
//			for (int i = 0; i < ranked.length; ++i) {
//				int template = ranked[i];
//				if (templateTypes[template] == type) {
//					uType += i+1;
//					numType += 1;
//				} else {
//					//uOther += i;
//					numOther += 1;
//				}
//			}
//			uType = (numType*numOther)+ (numType)*(numType+1)/2 -uType;
//			uOther = (numType*numOther) + (numOther*numOther+1)/2 - uOther;
//			double temp1 = (numType*numOther)/2;
//			double temp2 = (Math.sqrt(numType*numOther*(numType+numOther+1.0))/12.0);
//			if (uType<=uOther) {
//				pvalues[templateIndx.get(type)] = (uType - temp1)/temp2;
//			} else {
//				pvalues[templateIndx.get(type)] = (uOther - temp1)/temp2;
//			}
//		}
//		
//		double minPvalue = BarcodeFunctions.min(pvalues);
//		for (int i = 0; i < pvalues.length; ++i) {
//			pvalues[i] = pvalues[i]-minPvalue;
//		}
//		double sumPvalues = BarcodeFunctions.getSum(pvalues);
//		for (int i = 0; i < pvalues.length; ++i) {
//			typeProb[i] = pvalues[i]/sumPvalues;
//		}
//	}
	
//	public void fillTypeProb() {
//		double[] negScores = new double[typeScores.size()];
//		for (Iterator it = typeScores.keySet().iterator(); it.hasNext(); ){
//			String key = (String) it.next();
//			double min = min(typeScores.get(key));
//			negScores[templateIndx.get(key)] = -1*min;
//		}
//		double max = BarcodeFunctions.max(negScores);
//    	double[] intScores = new double[negScores.length];
//    	for (int i = 0; i < negScores.length; ++i) {
//    		intScores[i] = Math.exp(negScores[i]-max);
//    	}
//    	double sum = BarcodeFunctions.getSum(intScores);
//    	typeProbMax = new double[negScores.length];
//    	for (int i = 0; i < negScores.length; ++i) {
//    		typeProbMax[i] += intScores[i]/sum;
//    		
//    	}
//    	
//    	
//    	double[] negSumScores = new double[typeScores.size()];
//		for (Iterator it = typeScores.keySet().iterator(); it.hasNext(); ){
//			String key = (String) it.next();
//			double typeSum = 0;
//			for (int i = 0; i < typeScores.get(key).size(); ++i) {
//				typeSum += typeScores.get(key).get(i);
//			}
//			negSumScores[templateIndx.get(key)] =  -1*typeSum/typeScores.get(key).size();
//		}
//		double maxSum = BarcodeFunctions.max(negSumScores);
//    	double[] intSumScores = new double[negScores.length];
//    	for (int i = 0; i < negSumScores.length; ++i) {
//    		intSumScores[i] = Math.exp(negSumScores[i]-maxSum);
//    	}
//    	typeProbAvg = new double[negSumScores.length];
//    	double sumAvg = BarcodeFunctions.getSum(intSumScores);
//    	typeProbMax = new double[negSumScores.length];
//    	for (int i = 0; i < negSumScores.length; ++i) {
//    		typeProbAvg[i] += intSumScores[i]/sumAvg;
//    		
//    	}
//		
//	}
	
	public HashMap<Integer, String> getIndxToType() {
		return indxToType;
	}
	
	
	
	/*
	public double[][] getTypeScores(){
		return typeScores;
	}
	*/
	public HashMap<String, ArrayList<Double>> getTypeScores() {
		return typeScores;
	}
	
	/*
	public void printTypeScores() {
		fillTypeScores();

		
		for (int i = 0; i < typeScores.length; ++i) {
			String type = indxToType.get(i);
			System.out.print(type + "=c(");
			for (int j = 0; j < typeScores[i].length-1; ++j) {
				System.out.print(typeScores[i][j] + ",");
			}
			
			System.out.println(typeScores[i][typeScores[i].length-1]+")");
		}
	}
	*/
	
	public void getStats() {
		fillTypeScores();
		
		ArrayList<String> bestTypes = getBestTypes();
		System.out.println( "Best Type: " + bestTypes.toString());
		for (int j = 0; j < typeAvg.length; ++j) {
			System.out.println("\t" + indxToType.get(j) + ")\tAvg: " + typeAvg[j] + "\tMin: " + typeMin[j] + "\tMax: " + typeMax[j]);
		}
		
	}
	
	public CopyNumbers[] rankTemplatesHaploid() {
		CopyNumbers[] ranked = new CopyNumbers[scores.length];
		HashMap<Double, ArrayList<Integer>> scoreMap = new HashMap();
		for (int i = 0; i < this.scores.length; ++i) {
			ArrayList<Integer> keyArrayList = new ArrayList<Integer>();
			if (scoreMap.containsKey(this.scores[i])) {
				keyArrayList = scoreMap.get(this.scores[i]);
			}
			keyArrayList.add(i);
			scoreMap.put(this.scores[i], keyArrayList);
		}
		Object[] myScores = scoreMap.keySet().toArray();
		//Double[] myScores = (Double[]) scoreMap.keySet().toArray();
		Arrays.sort(myScores);
		
		//HashMap<String, String> typeToCopyNumber = Typing.getTypeToCopyNumbers();
		HashMap<String, int[]> typeCopyNumber = Typing.getTypeCopyNumbers();

		
		int indx = 0;
		for (int i = 0; i < myScores.length; ++i) {
			ArrayList<Integer> temp = scoreMap.get(myScores[i]);
			for (int j = 0; j < temp.size(); ++j) {
				double score = (Double) myScores[i];
				
				CopyNumbers cn =  new CopyNumbers(typeCopyNumber.get(templateTypes[temp.get(j)]), score, entrywiseScores[temp.get(j)], scales[temp.get(j)]);
				ranked[indx] = cn; 
				indx += 1;
				
			}
		}

		return ranked;
	}
	
	public CopyNumbers[] rankTemplates() {
		CopyNumbers[] ranked = new CopyNumbers[scores.length];
		HashMap<Double, ArrayList<Integer>> scoreMap = new HashMap();
		for (int i = 0; i < this.scores.length; ++i) {
			ArrayList<Integer> keyArrayList = new ArrayList<Integer>();
			if (scoreMap.containsKey(this.scores[i])) {
				keyArrayList = scoreMap.get(this.scores[i]);
			}
			keyArrayList.add(i);
			scoreMap.put(this.scores[i], keyArrayList);
		}
		Object[] myScores = scoreMap.keySet().toArray();
		//Double[] myScores = (Double[]) scoreMap.keySet().toArray();
		Arrays.sort(myScores);
		
		HashMap<String, String> typeToCopyNumber = Typing.getDiploidTypeToCopyNumbers();
		HashMap<String, int[]> typeCopyNumber = Typing.getDiploidTypeCopyNumbers();
		
		int indx = 0;
		for (int i = 0; i < myScores.length; ++i) {
			ArrayList<Integer> temp = scoreMap.get(myScores[i]);
			for (int j = 0; j < temp.size(); ++j) {
				double score = (Double) myScores[i];
				CopyNumbers cn =  new CopyNumbers(typeCopyNumber.get(templateTypes[temp.get(j)]), score,  entrywiseScores[temp.get(j)], scales[temp.get(j)]);
				ranked[indx] = cn; 
				indx += 1;
				
			}
		}

		return ranked;
	}
	
//	public int[] rankTemplates() {
//		int[] ranked = new int[scores.length];
//		HashMap<Double, ArrayList<Integer>> scoreMap = new HashMap();
//		for (int i = 0; i < this.scores.length; ++i) {
//			ArrayList<Integer> keyArrayList = new ArrayList<Integer>();
//			if (scoreMap.containsKey(this.scores[i])) {
//				keyArrayList = scoreMap.get(this.scores[i]);
//			}
//			keyArrayList.add(i);
//			scoreMap.put(this.scores[i], keyArrayList);
//		}
//		Object[] myScores = scoreMap.keySet().toArray();
//		Arrays.sort(myScores);
//		//Arrays.sort(myScores, Collections.reverseOrder());
//		
//		int indx = 0;
//		for (int i = 0; i < myScores.length; ++i) {
//			ArrayList<Integer> temp = scoreMap.get(myScores[i]);
//			for (int j = 0; j < temp.size(); ++j) {
//				if ( (i == 0) && (j ==0) ) {
//				//double toPrint = (Double) myScores[i]/this.numKmers;
//				System.out.print("\t" + myScores[i] + "\t" + scales[temp.get(j)] + "\t" + templateTypes[temp.get(j)]);
//				//+"\t" + scales[temp.get(j)] 
//				//+ "\\\\\\hline");
//				}
//				ranked[indx] = temp.get(j); 
//				indx += 1;
//				
//			}
//		}
//
//		return ranked;
//	}
	
//	public int[] rankTemplates() {
//		int[] ranked = new int[scores.length];
//		HashMap<Double, ArrayList<Integer>> scoreMap = new HashMap();
//		for (int i = 0; i < this.scores.length; ++i) {
//			ArrayList<Integer> keyArrayList = new ArrayList<Integer>();
//			if (scoreMap.containsKey(this.scores[i])) {
//				keyArrayList = scoreMap.get(this.scores[i]);
//			}
//			keyArrayList.add(i);
//			scoreMap.put(this.scores[i], keyArrayList);
//		}
//		Object[] myScores = scoreMap.keySet().toArray();
//		Arrays.sort(myScores);
//		//Arrays.sort(myScores, Collections.reverseOrder());
//		
//		int indx = 0;
//		for (int i = 0; i < myScores.length; ++i) {
//			ArrayList<Integer> temp = scoreMap.get(myScores[i]);
//			for (int j = 0; j < temp.size(); ++j) {
//				if ( (i == 0) && (j ==0) ) {
//				double toPrint = (Double) myScores[i];
//				//double toPrint = (Double) myScores[i]/this.numKmers;
//				System.out.print("\t" + toPrint + "\t");
//				System.out.println(templateTypes[temp.get(j)]+"\t");
//				}
//				ranked[indx] = temp.get(j); 
//				indx += 1;
//				
//			}
//		}
//
//		return ranked;
//	}
	
	public void printTypeProbMax() {
		HashMap<Double, ArrayList<Integer>> scoreMap = new HashMap();
		for (int i = 0; i < typeProb.length; ++i) {
			ArrayList<Integer> keyArrayList = new ArrayList<Integer>();
			if (scoreMap.containsKey(typeProb[i])) {
				keyArrayList = scoreMap.get(typeProb[i]);
			}
			keyArrayList.add(i);
			scoreMap.put(typeProb[i], keyArrayList);
		}
		Object[] myScores = scoreMap.keySet().toArray();
		Arrays.sort(myScores, Collections.reverseOrder());
		
		
		for (int i = 0; i < myScores.length; ++i) {
			ArrayList<Integer> temp = scoreMap.get(myScores[i]);
			for (int j = 0; j < temp.size(); ++j) {
				System.out.println("\t" + indxToType.get(temp.get(j)) + ": " + myScores[i] + "\t" );
				//System.out.print(templateTypes[temp.get(j)]);
			}
		}
		System.out.println();
	}
	
	public void printTypeProbAvg() {
		HashMap<Double, ArrayList<Integer>> scoreMap = new HashMap();
		for (int i = 0; i < typeProbAvg.length; ++i) {
			ArrayList<Integer> keyArrayList = new ArrayList<Integer>();
			if (scoreMap.containsKey(typeProbAvg[i])) {
				keyArrayList = scoreMap.get(typeProbAvg[i]);
			}
			keyArrayList.add(i);
			scoreMap.put(typeProbAvg[i], keyArrayList);
		}
		Object[] myScores = scoreMap.keySet().toArray();
		Arrays.sort(myScores, Collections.reverseOrder());
		
		
		for (int i = 0; i < myScores.length; ++i) {
			ArrayList<Integer> temp = scoreMap.get(myScores[i]);
			for (int j = 0; j < temp.size(); ++j) {
				System.out.println("\t" + indxToType.get(temp.get(j)) + ": " + myScores[i] + "\t" );
				//System.out.print(templateTypes[temp.get(j)]);
			}
		}
		System.out.println();
	}
	
	public void printTypeProbAvg(String[] printOrder) {
		for (int i = 0; i < printOrder.length; ++i) {
			for (Iterator it = indxToType.keySet().iterator(); it.hasNext();) {
				Integer key = (Integer) it.next();
				String type = indxToType.get(key);
				String toPrint = printOrder[i];
				if (type.equals(toPrint)) {
					System.out.print(typeProbAvg[key] + "\t");
				}
			}
		}
		System.out.println();
	}
	
	
	/*
	private static String getTemplateName(int index, File[] templates) {
		
		String[] temp = templates[index].getAbsolutePath().split("/");
		String temp2 = temp[temp.length-1];
		//System.out.println(temp2);
		String[] temp3 = temp2.split(".");
		for (int i = 0 ;i < temp3.length; ++i) {
		System.out.println(temp3[i]);
		}
		return temp2;
		
	}
	
	private static String[] getTemplateNameDiploid(int index, File[] templates) {
		int currIndex = 0;
		for (int i = 0; i < templates.length-1; ++i) {
			for (int j = i; j < templates.length; ++j) {
				if (index == currIndex) {
					String[] temp = new String[2];
					temp[0] = getTemplateName(i, templates);
					temp[1] = getTemplateName(j, templates);
					return temp;
				}
				currIndex += 1;
			}
		}
		return new String[2];
	}
	*/

}