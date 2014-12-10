package typing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import barcodeFunctions.*;
import distanceFunction.*;

public class TemplateScorer {
	
	private String[] templateTypes;
	private ArrayList<String> templateNames;
	private EntrywiseVecDistance distanceCalculator; 
	private int[][] templateBarcodes;
	private int numKmers;
	private int[] copyNumber;
	
	public TemplateScorer(int[][] templateBarcodes, String[] templateTypes, ArrayList<String> templateNames) {
		this.templateTypes = templateTypes;
		this.templateNames = templateNames;
		initialize(templateBarcodes);
	}

	
	public TemplateScorer(int[][] templateBarcodes, String[] templateTypes, ArrayList<String> templateNames, ArrayList<Integer> repetitiveKmers, ArrayList<Integer> exclude) {
		this.templateTypes = templateTypes;
		this.templateNames = templateNames;
		ArrayList<Integer> allKmers = new ArrayList<Integer>();
		for (int i = 0; i < exclude.size(); ++i) {
			if (exclude.get(i)>=0) {
			allKmers.add(exclude.get(i));
			}
		}
		for (int i = 0; i < repetitiveKmers.size(); ++i) {
			allKmers.add(repetitiveKmers.get(i));
		}
		initialize(templateBarcodes);
	}

	public TemplateScorer(int[][] templateBarcodes, String[] templateTypes, ArrayList<String> templateNames2, EntryWeights weights) {
		this.templateTypes = templateTypes;
		this.templateNames = templateNames2;
		this.templateBarcodes = templateBarcodes;
		this.distanceCalculator = new NormalCDFVecDistance();
		distanceCalculator.setWeights(weights);
		distanceCalculator.setScaleWeights(weights);
	}
	
	public TemplateScorer(int[][] templateBarcodes, String[] templateTypes, ArrayList<String> templateNames2, EntryWeights weights, EntryWeights scaleWeights) {
		this.templateTypes = templateTypes;
		this.templateNames = templateNames2;
		this.templateBarcodes = templateBarcodes;
		this.distanceCalculator = new NormalCDFVecDistance();
		distanceCalculator.setWeights(weights);
		distanceCalculator.setScaleWeights(scaleWeights);
	}
	
	
	private void initialize(int[][] templateBarcodes) {

		this.templateBarcodes = templateBarcodes;
		
		this.distanceCalculator = new ZVecDistance();
		
	}
	
	public int[] getCopyNumber() {
		return this.copyNumber;
	}
	
	public EntrywiseVecDistance getDistanceCalculator() {
		return this.distanceCalculator;
	}
	
	public int[][] getTemplateBarcodes() {
		return this.templateBarcodes;
	}
	
	public ArrayList<String> getTemplateNames() {
		return this.templateNames;
	}
	
	public String[] getTemplateTypes() {
		return this.templateTypes;
	}
	
	public int getNumKmers() {
		return this.numKmers;
	}
	
	public double[] getEntrywiseDistance(int[] sampleBarcode, int templateIndx) {
		return distanceCalculator.getEntrywiseDistanceVec(sampleBarcode, templateBarcodes[templateIndx]);
	}
	
	public double getDistance(int[] sampleBarcode, int templateIndx) {
		return distanceCalculator.distance(sampleBarcode, templateBarcodes[templateIndx]);
	}
	
	public double getDistance(int[] sampleBarcode, int[] templateBarcode) {
		return distanceCalculator.distance(sampleBarcode, templateBarcode);
	}
	
	public void printPresenceAbsenceAndSample(int[] sampleBarcode, ArrayList<Integer> exclude) {
		HashMap<String, Integer> numTypes = new HashMap<String, Integer>();
		HashMap<String, int[]> types = new HashMap<String, int[]>();
		for (int i = 0; i < templateTypes.length; ++i) {
			if (!exclude.contains(i)) {
			if (types.containsKey(templateTypes[i])) {
				numTypes.put(templateTypes[i],(numTypes.get(templateTypes[i])+1));
				types.put(templateTypes[i],BarcodeFunctions.addBarcodes(types.get(templateTypes[i]),templateBarcodes[i])); 
			} else {
				numTypes.put(templateTypes[i],1);
				types.put(templateTypes[i],templateBarcodes[i]);
			}
			}
		}
		
		String[] newTemplateTypes = new String[types.size()];
		String[] newTemplateNames = new String[types.size()];
		int indx = 0;
		for (Iterator it = types.keySet().iterator(); it.hasNext(); ) {
			String key = (String) it.next();
			newTemplateTypes[indx] = key;
			newTemplateNames[indx] = key;
			indx += 1;
		}
		
		double[][] scaledTemplateBarcodes = new double[newTemplateTypes.length][templateBarcodes[0].length];
		for (int i = 0; i < newTemplateTypes.length; ++i) {
			int[] typeBarcode = types.get(newTemplateTypes[i]);
			for (int j = 0; j < typeBarcode.length; ++j) {
				if (typeBarcode[j]==0) {
					scaledTemplateBarcodes[i][j] = 0;
				} else {
					scaledTemplateBarcodes[i][j] = 10;
				}
			}
		}
		
		System.out.print("X\t");
		int numkmers = 1;
		for (int i = 0; i < scaledTemplateBarcodes.length; ++i) {
			if (newTemplateTypes[i].equals("0")) {
				System.out.print("A" + numkmers + "\t");
			} else {
				System.out.print("P" + numkmers + "\t");
			}
				numkmers += 1;
		}
		System.out.println("Sample");
		
		EntryWeights weights = this.distanceCalculator.getWeights();
		for (int i = 0; i < scaledTemplateBarcodes[0].length; ++i) {
			if (weights.weight(i)!=0) {
				System.out.print(i + "\t");
				for (int j = 0; j < scaledTemplateBarcodes.length; ++j) {
					System.out.print(scaledTemplateBarcodes[j][i] + "\t");
				}
				if (sampleBarcode[i]!=0) { System.out.println("10.0"); } else { System.out.println("0.0"); }
			}
		}
	}
	
	public void printAggregateBarcodes(ArrayList<Integer> exclude) {
		HashMap<String, Integer> numTypes = new HashMap<String, Integer>();
		HashMap<String, int[]> types = new HashMap<String, int[]>();
		for (int i = 0; i < templateTypes.length; ++i) {
			if (!exclude.contains(i)) {
			if (types.containsKey(templateTypes[i])) {
				numTypes.put(templateTypes[i],(numTypes.get(templateTypes[i])+1));
				types.put(templateTypes[i],BarcodeFunctions.addBarcodes(types.get(templateTypes[i]),templateBarcodes[i])); 
			} else {
				numTypes.put(templateTypes[i],1);
				types.put(templateTypes[i],templateBarcodes[i]);
			}
			}
		}
		
		String[] newTemplateTypes = new String[types.size()];
		String[] newTemplateNames = new String[types.size()];
		int indx = 0;
		for (Iterator it = types.keySet().iterator(); it.hasNext(); ) {
			String key = (String) it.next();
			newTemplateTypes[indx] = key;
			newTemplateNames[indx] = key;
			indx += 1;
		}
		
		double[][] scaledTemplateBarcodes = new double[newTemplateTypes.length][templateBarcodes[0].length];
		for (int i = 0; i < newTemplateTypes.length; ++i) {
			int[] typeBarcode = types.get(newTemplateTypes[i]);
			//double c = this.distanceCalculator.getScaleFactor(sampleBarcode, typeBarcode);
			for (int j = 0; j < templateBarcodes[0].length; ++j) {
				scaledTemplateBarcodes[i][j] = typeBarcode[j];
			}
		}
		
		System.out.print("X\t");
		int numkmers = 1;
		for (int i = 0; i < scaledTemplateBarcodes.length; ++i) {
			if (newTemplateTypes[i].equals("0")) {
				System.out.print("A" + numkmers + "\t");
			} else if (newTemplateTypes[i].equals("1")) {
				System.out.print("Pa" + numkmers + "\t");
			} else {
				System.out.print("Pb" + numkmers + "\t");
			}
				numkmers += 1;
		}
		System.out.println();
		
		EntryWeights weights = this.distanceCalculator.getWeights();
		for (int i = 0; i < scaledTemplateBarcodes[0].length; ++i) {
			if (weights.weight(i)!=0) {
				System.out.print(i + "\t");
				for (int j = 0; j < scaledTemplateBarcodes.length; ++j) {
					System.out.print(scaledTemplateBarcodes[j][i] + "\t");
				}
				System.out.println();
			}
		}
	}
	
	public void printAggregateBarcodesAndSample(int[] sampleBarcode, ArrayList<Integer> exclude) {
		HashMap<String, Integer> numTypes = new HashMap<String, Integer>();
		HashMap<String, int[]> types = new HashMap<String, int[]>();
		for (int i = 0; i < templateTypes.length; ++i) {
			if (!exclude.contains(i)) {
			if (types.containsKey(templateTypes[i])) {
				numTypes.put(templateTypes[i],(numTypes.get(templateTypes[i])+1));
				types.put(templateTypes[i],BarcodeFunctions.addBarcodes(types.get(templateTypes[i]),templateBarcodes[i])); 
			} else {
				numTypes.put(templateTypes[i],1);
				types.put(templateTypes[i],templateBarcodes[i]);
			}
			}
		}
		
		String[] newTemplateTypes = new String[types.size()];
		String[] newTemplateNames = new String[types.size()];
		int indx = 0;
		for (Iterator it = types.keySet().iterator(); it.hasNext(); ) {
			String key = (String) it.next();
			newTemplateTypes[indx] = key;
			newTemplateNames[indx] = key;
			indx += 1;
		}
		
		double[][] scaledTemplateBarcodes = new double[newTemplateTypes.length][templateBarcodes[0].length];
		for (int i = 0; i < newTemplateTypes.length; ++i) {
			int[] typeBarcode = types.get(newTemplateTypes[i]);
			double c = this.distanceCalculator.getScaleFactor(sampleBarcode, typeBarcode);
			for (int j = 0; j < templateBarcodes[0].length; ++j) {
				scaledTemplateBarcodes[i][j] = typeBarcode[j]*c;
			}
		}
		
		System.out.print("X\t");
		int numkmers = 1;
		for (int i = 0; i < scaledTemplateBarcodes.length; ++i) {
			if (newTemplateTypes[i].equals("0")) {
				System.out.print("A" + numkmers + "\t");
			} else {
				System.out.print("P" + numkmers + "\t");
			}
				numkmers += 1;
		}
		System.out.println("Sample");
		
		EntryWeights weights = this.distanceCalculator.getWeights();
		for (int i = 0; i < scaledTemplateBarcodes[0].length; ++i) {
			if (weights.weight(i)!=0) {
				System.out.print(i + "\t");
				for (int j = 0; j < scaledTemplateBarcodes.length; ++j) {
					System.out.print(scaledTemplateBarcodes[j][i] + "\t");
				}
				System.out.println(sampleBarcode[i]);
			}
		}
	}
	

	
	public TemplateScores scoreSampleWithAggregate(int[] sampleBarcode) {
		return scoreSampleWithAggregate(sampleBarcode, new ArrayList<Integer>());
	}
	
	public TemplateScores scoreSampleWithAggregate(int[] sampleBarcode, ArrayList<Integer> exclude) {
		//int[][] templateBarcodes = getTemplateBarcodes();
		
		HashMap<String, Integer> numTypes = new HashMap<String, Integer>();
		HashMap<String, int[]> types = new HashMap<String, int[]>();
		for (int i = 0; i < templateTypes.length; ++i) {
			if (!exclude.contains(i)) {
			if (types.containsKey(templateTypes[i])) {
				numTypes.put(templateTypes[i],(numTypes.get(templateTypes[i])+1));
				types.put(templateTypes[i],BarcodeFunctions.addBarcodes(types.get(templateTypes[i]),templateBarcodes[i])); 
			} else {
				numTypes.put(templateTypes[i],1);
				types.put(templateTypes[i],templateBarcodes[i]);
			}
			}
		}
		
		double[] scores = new double[types.size()];
		String[] newTemplateTypes = new String[types.size()];
		ArrayList<String> newTemplateNames = new ArrayList<String>();
		int indx = 0;
		for (Iterator it = types.keySet().iterator(); it.hasNext(); ) {
			String key = (String) it.next();
			double score = distanceCalculator.distance(sampleBarcode, types.get(key));
			scores[indx] = score;
			newTemplateTypes[indx] = key;
			newTemplateNames.add(key);
			indx += 1;
		}
		return new TemplateScores(scores, newTemplateTypes, newTemplateNames, numKmers);
	}
	
	public void printBarcodesAndSample(int[] sampleBarcode, ArrayList<Integer> exclude) {
		int[][] templateBarcodes = getTemplateBarcodes();
		int[] newTemplateBarcodesIndx = new int[templateBarcodes.length-exclude.size()];
		String[] newTemplateTypes = new String[templateTypes.length-exclude.size()];
		String[] newTemplateNames = new String[templateNames.size()-exclude.size()];

		int currIndx = 0;
		for (int i = 0 ; i < templateBarcodes.length; ++i) {
			if (!exclude.contains(i)) {
			newTemplateBarcodesIndx[currIndx] = i;
			newTemplateTypes[currIndx] = templateTypes[i];
			newTemplateNames[currIndx] = templateNames.get(i);
			currIndx += 1;
			}
		}
					
		double[][] scaledTemplateBarcodes = new double[newTemplateBarcodesIndx.length][templateBarcodes[0].length];

		for (int i = 0; i < newTemplateBarcodesIndx.length; ++i) {
			double c = this.distanceCalculator.getScaleFactor(sampleBarcode, templateBarcodes[newTemplateBarcodesIndx[i]]);
			for (int j = 0; j < templateBarcodes[0].length; ++j) {
				scaledTemplateBarcodes[i][j] = templateBarcodes[i][j]*c;
			}
		}
		
		System.out.print("X\t");
		int numkmers = 1;
		for (int i = 0; i < scaledTemplateBarcodes.length; ++i) {
			if (templateTypes[i].equals("0")) {
				System.out.print("A" + numkmers + "\t");
			} else if (templateTypes[i].equals("1")) {
				System.out.print("Pa" + numkmers + "\t");
			} else if (templateTypes[i].equals("2")) {
				System.out.print("Pb" + numkmers + "\t");
			} else if (templateTypes[i].equals("3")) {
				System.out.print("Pc" + numkmers + "\t");
			} else if (templateTypes[i].equals("4")) {
				System.out.print("Pd" + numkmers + "\t");
			}
				
				numkmers += 1;
		}
		System.out.println("Sample");
		
		EntryWeights weights = this.distanceCalculator.getWeights();
		for (int i = 0; i < scaledTemplateBarcodes[0].length; ++i) {
			if (weights.weight(i)!=0) {
				System.out.print(i + "\t");
				for (int j = 0; j < scaledTemplateBarcodes.length; ++j) {
					System.out.print(scaledTemplateBarcodes[j][i] + "\t");
				}
				System.out.println(sampleBarcode[i]);
			}
		}
	}
	
	public void printBarcodes(ArrayList<Integer> exclude) {
		int[][] templateBarcodes = getTemplateBarcodes();
		int[] newTemplateBarcodesIndx = new int[templateBarcodes.length-exclude.size()];
		String[] newTemplateTypes = new String[templateTypes.length-exclude.size()];
		String[] newTemplateNames = new String[templateNames.size()-exclude.size()];

		int currIndx = 0;
		for (int i = 0 ; i < templateBarcodes.length; ++i) {
			if (!exclude.contains(i)) {
			newTemplateBarcodesIndx[currIndx] = i;
			newTemplateTypes[currIndx] = templateTypes[i];
			newTemplateNames[currIndx] = templateNames.get(i);
			currIndx += 1;
			}
		}
					
		double[][] scaledTemplateBarcodes = new double[newTemplateBarcodesIndx.length][templateBarcodes[0].length];

		for (int i = 0; i < newTemplateBarcodesIndx.length; ++i) {
			for (int j = 0; j < templateBarcodes[0].length; ++j) {
				scaledTemplateBarcodes[i][j] = templateBarcodes[i][j];
			}
		}
		
		System.out.print("X\t");
		int numkmers = 1;
		for (int i = 0; i < scaledTemplateBarcodes.length; ++i) {
			if (templateTypes[i].equals("0")) {
				System.out.print("A" + numkmers + "\t");
			} else if (templateTypes[i].equals("1")) {
				System.out.print("Pa" + numkmers + "\t");
			} else {
				System.out.print("Pb" + numkmers + "\t");
			}
				numkmers += 1;
		}
		System.out.println();
		
		EntryWeights weights = this.distanceCalculator.getWeights();
		for (int i = 0; i < scaledTemplateBarcodes[0].length; ++i) {
			if (weights.weight(i)!=0) {
				System.out.print(i + "\t");
				for (int j = 0; j < scaledTemplateBarcodes.length; ++j) {
					System.out.print(scaledTemplateBarcodes[j][i] + "\t");
				}
				System.out.println();
			}
		}
	}
	
	public TemplateScores scoreSample(int[] sampleBarcode, ArrayList<Integer> exclude) {
		int[][] templateBarcodes = getTemplateBarcodes();
		
		double[] scores = new double[templateBarcodes.length-exclude.size()];
		String[] newTemplateTypes = new String[templateTypes.length-exclude.size()];
		ArrayList<String> newTemplateNames = new ArrayList<String>();

		int currIndx = 0;
		for (int i = 0 ; i < templateBarcodes.length; ++i) {
			
			if (!exclude.contains(i)) {
			double score = distanceCalculator.distance(sampleBarcode, templateBarcodes[i]);
			scores[currIndx] = score;
			newTemplateTypes[currIndx] = templateTypes[i];
			newTemplateNames.add(templateNames.get(i));
			currIndx += 1;
			}
		}
		
		
		return new TemplateScores(scores, newTemplateTypes, newTemplateNames, numKmers);
	}
	
	public TemplateScores scoreSample(int[] sampleBarcode) {
		int[][] templateBarcodes = getTemplateBarcodes();
		
		double[] scores = new double[templateBarcodes.length];

		for (int i = 0 ; i < templateBarcodes.length; ++i) {
			double score = distanceCalculator.distance(sampleBarcode, templateBarcodes[i]);
			scores[i] = score;
		}
		return new TemplateScores(scores, templateTypes, templateNames, numKmers);
	}
	


	public double getCopyValueOfSampleWithAggregate(int[] sampleBarcode) {
		return getCopyValueOfSampleWithAggregate2(sampleBarcode);
	}
	
	private double getCopyValueOfSampleWithAggregate2(int[] sampleBarcode) {
		EntryWeights weights = this.distanceCalculator.getScaleWeights();

		int[][] templateBarcodes = getTemplateBarcodes();
		
		int numBarcodes = 0;
		for (int i = 0; i < copyNumber.length; ++i) {
			if (copyNumber[i]>0) { numBarcodes+=1; }
		}
				
		double[] scaledTemplateBarcode = new double[templateBarcodes[0].length];
//		double[] var = new double[templateBarcodes[0].length];
		Arrays.fill(scaledTemplateBarcode, 0);
		for (int i = 0; i < templateBarcodes.length; ++i) {
			if (copyNumber[i]>0) {
				for (int j = 0; j < templateBarcodes[0].length; ++j) {
					scaledTemplateBarcode[j] += (templateBarcodes[i][j])/copyNumber[i];
//					var[j] += Math.pow(templateBarcodes[i][j]/copyNumber[i],2);
				}
			}
		}
		for (int i = 0; i < scaledTemplateBarcode.length; ++i) {
			scaledTemplateBarcode[i] = scaledTemplateBarcode[i]/numBarcodes;
//			double blah = Math.abs(var[i]/numBarcodes - Math.pow(scaledTemplateBarcode[i],2));
//			if (blah>0.5) {
//				weights.setWeight(i,0);
//			}
		}
						
		double[] w = distanceCalculator.getWeights().getWeights();

		double c = this.distanceCalculator.getScaleFactor(sampleBarcode, scaledTemplateBarcode);
		
		return c;
	}
	

	
	public double getCopyNumberOfSample(int[] sampleBarcode) {
		return getCopyNumberOfSample1(sampleBarcode);
	}
	
	private double getCopyNumberOfSample1(int[] sampleBarcode) {
		
		int[][] templateBarcodes = getTemplateBarcodes();
		
		int numBarcodes = 0;
		for (int i = 0; i < copyNumber.length; ++i) {
			if (copyNumber[i]>0) { numBarcodes+=1; }
		}
		
		double[] sampleCopyNumbers = new double[numBarcodes];
		
		double[][] scaledTemplateBarcodes = new double[numBarcodes][templateBarcodes[0].length];
		int currIndx = 0;
		for (int i = 0; i < templateBarcodes.length; ++i) {
			if (copyNumber[i]>0) {
				double c = this.distanceCalculator.getScaleFactor(sampleBarcode, templateBarcodes[i]);
				for (int j = 0; j < templateBarcodes[0].length; ++j) {
					scaledTemplateBarcodes[currIndx][j] = (templateBarcodes[i][j]*c)/copyNumber[i];
				}
				currIndx+=1;
			}
		}
		
		
		
		for (int i = 0; i < scaledTemplateBarcodes.length; ++i) {
			double c = this.distanceCalculator.getScaleFactor(sampleBarcode, scaledTemplateBarcodes[i]);
			sampleCopyNumbers[i] = c;
		}
		
		
		if (sampleCopyNumbers.length==0) { return 0; }
		else {
		return BarcodeFunctions.getSum(sampleCopyNumbers)/sampleCopyNumbers.length;
		}
	}
	
	private double getCopyNumberOfSample2(int[] sampleBarcode) {
		
		int[][] templateBarcodes = getTemplateBarcodes();
		
		int numBarcodes = 0;
		for (int i = 0; i < copyNumber.length; ++i) {
			if (copyNumber[i]>0) { numBarcodes+=1; }
		}
		
		double[] sampleCopyNumbers = new double[numBarcodes];
		
		
		int currIndx = 0;
		for (int i = 0; i < templateBarcodes.length; ++i) {
			if (copyNumber[i]>0) {
			double c = this.distanceCalculator.getScaleFactor(sampleBarcode, templateBarcodes[i]);
			//System.out.print(c+"\t");
			sampleCopyNumbers[currIndx] = c;
			currIndx+=1;
			}
		}
		//System.out.println();
		
		
		if (sampleCopyNumbers.length==0) { return 0; }
		else {
		return BarcodeFunctions.getSum(sampleCopyNumbers)/sampleCopyNumbers.length;
		}
	}


	
	public void setCopyNumber(int[] copyNumber) {
		this.copyNumber = new int[copyNumber.length];
		for (int i = 0; i < copyNumber.length; ++i) {
			this.copyNumber[i] = copyNumber[i];
		}
	}

	public void setWeights(EntryWeights weights) {
		this.distanceCalculator.setWeights(weights);
	}
	
	public void setScaleWeights(EntryWeights weights) {
		this.distanceCalculator.setScaleWeights(weights);
	}
	

	
	public void printBarcode(int[] sample) {
		//Get weights
		double[] weights = distanceCalculator.getWeights().getWeights();
		
		//For each barcode
		for (int j = 0; j < weights.length; ++j) {
			if (weights[j] != 0) {
			for (int i = 0; i < templateBarcodes.length; ++i) {
				if (copyNumber[i]>0) {
					System.out.print((templateBarcodes[i][j])/copyNumber[i] + "\t");
				} else {
					System.out.print("-\t");
				}
			}
			System.out.println(sample[j]);
			}
		}
		 
	}
	

}
