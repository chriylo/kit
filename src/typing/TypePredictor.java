package typing;

import generator.KMerFileReader;
import generator.Trie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.text.DecimalFormat;

import main.BarcodeBuilder;
import main.Test5;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import barcodeFunctions.BarcodeFunctions;
import distanceFunction.EntryWeights;
import typing.*;

public class TypePredictor {

	ArrayList<ArrayList<String>> geneTests;
	HashMap<String, HashMap<String, int[]>> templateRegions;
	int[][] templateBarcodes;
	public static ArrayList<String> templateNames;
	public static ArrayList<String> templateDiploidNames;
	HashMap<Integer, ArrayList<Integer>> uniqueKmers;

	
	public TypePredictor() throws IOException  {
		geneTests = Typing.geneTests;
		templateRegions = Typing.templateRegions;
		templateNames = Typing.templateNames;
		templateDiploidNames = Typing.templateDiploidNames;
		templateBarcodes = Typing.getBarcodes();
		uniqueKmers = Typing.getUniqueKmers();
	}
	
	public void typeSample(String barcodeFileName) throws IOException {
		File barcodeFile = new File(barcodeFileName);
		int[] barcode = KMerFileReader.getBarcodeFromFile(barcodeFile.getAbsolutePath());
		typeSample(barcode);
	}
	
	public void typeSample(int[] barcode) throws IOException {	
		long startTime = System.currentTimeMillis();
		double[] sampleCopyValues = new double[geneTests.size()];
		
		ArrayList<Integer> badKmers = new ArrayList<Integer>();
		
		for (int numGeneTest = 0; numGeneTest < geneTests.size(); numGeneTest+=1) {
			ArrayList<String> genes = geneTests.get(numGeneTest);
		
			/*** Initialize Scorers ***/
			
				int[] diploidTemplateCopyNumber = Typing.getDiploidCopyNumber(templateNames, templateRegions, genes);
				String[] templateGeneTypes = Typing.getPresenceAbsenceTypes(templateNames, templateRegions, genes);
				String[] diploidTemplateGeneTypes = Typing.getDiploidPresenceAbsenceTypes(templateNames, templateRegions, genes);

				ArrayList<Integer> geneFilteredKmerIndices = getIndicesOfGenesFilteredKmers(uniqueKmers.get(numGeneTest), templateBarcodes, templateGeneTypes);

				EntryWeights filteredEntryWeights  = getEntryWeights(Test5.getDiploidBarcodes(templateBarcodes), geneFilteredKmerIndices, badKmers);
//				if (numGeneTest==8) {
//					filteredEntryWeights = getIntersectionEntryWeights(filteredEntryWeights, Typing.aggKmers.get(numGeneTest));
//				}
				//System.out.println("Gene:"+numGeneTest);
				//filteredEntryWeights.printNonZeroIndices();
				
				TemplateScorer diploidScorer = new TemplateScorer(Test5.getDiploidBarcodes(templateBarcodes), diploidTemplateGeneTypes, templateDiploidNames, filteredEntryWeights);
				diploidScorer.setCopyNumber(diploidTemplateCopyNumber);
				
				/*** Run All ***/
				sampleCopyValues[numGeneTest] = diploidScorer.getCopyValueOfSampleWithAggregate(barcode);

		}
		
		DecimalFormat df = new DecimalFormat("####0.00");
		System.out.print("\t");
		for (int i = 0; i < sampleCopyValues.length-1; ++i ) {
			System.out.print(df.format(sampleCopyValues[i]) + ",");
		}
		System.out.print(df.format(sampleCopyValues[sampleCopyValues.length-1]) + "");
		
		//normalize scaling factors
		double tempSum = 0; for (int i = 0; i < sampleCopyValues.length; ++i) { tempSum += sampleCopyValues[i]; }
		for (int i = 0; i < sampleCopyValues.length; ++i) { sampleCopyValues[i] = (200*sampleCopyValues[i])/tempSum; }
		
		System.out.print("\t");
		for (int i = 0; i < sampleCopyValues.length-1; ++i ) {
			System.out.print(df.format(sampleCopyValues[i]) + ",");
		}
		System.out.print(df.format(sampleCopyValues[sampleCopyValues.length-1]) + "");
		
		CopyNumberScorer cnScorer = new CopyNumberScorer();
		CopyNumbers sampleCN = cnScorer.getCopyNumberOfSample(sampleCopyValues);
		String strSampleCopyNumbers = sampleCN.getCopyNumbers();
		double[] entrywise = sampleCN.getEntrywiseScores(); System.out.print("\t" + df.format(entrywise[0])); for (int e = 1; e < entrywise.length; ++e) {	System.out.print("," + df.format(entrywise[e])); }
		System.out.print("\t" + sampleCN.getScore() + "\t" + sampleCN.getScalingFactor());
		if (Typing.diploidCopyNumbersToType.containsKey(strSampleCopyNumbers)) {
			System.out.print	("\t" + Typing.diploidCopyNumbersToType.get(strSampleCopyNumbers));
		} else { 
			System.out.print("\t" + strSampleCopyNumbers);
		}
	
		TemplateScores scores = cnScorer.scoreSample(sampleCopyValues);
		CopyNumbers[] ranked = scores.rankTemplates();
		for (int i = 0; i < ranked.length; ++i) {
			strSampleCopyNumbers = ranked[i].getCopyNumbers();
			entrywise = ranked[i].getEntrywiseScores(); System.out.print("\t" + df.format(entrywise[0])); for (int e = 1; e < entrywise.length; ++e) {	System.out.print("," + df.format(entrywise[e])); }
			System.out.print("\t" + ranked[i].getScore() + "\t" + ranked[i].getScalingFactor());
			if (Typing.diploidCopyNumbersToType.containsKey(strSampleCopyNumbers)) {
				System.out.print	("\t" + Typing.diploidCopyNumbersToType.get(strSampleCopyNumbers));
			} else { 
				System.out.print("\t" + strSampleCopyNumbers);
			}
		}
	
		System.out.println();
		
		int time = (int) ((System.currentTimeMillis()-startTime));
		//System.out.println("Time to type (ms): " + time);
	}
	

	
	
	public void typeSampleHaploid(int[] barcode) throws IOException {	
		double[] sampleCopyValues = new double[geneTests.size()];
		
		ArrayList<Integer> badKmers = new ArrayList<Integer>();

		for (int numGeneTest = 0; numGeneTest < geneTests.size(); numGeneTest+=1) {
			ArrayList<String> genes = geneTests.get(numGeneTest);
			
			/*** Initialize Scorers ***/
			int[] templateCopyNumber = Typing.getCopyNumber(templateNames, templateRegions, genes);
			String[] templateGeneTypes = Typing.getPresenceAbsenceTypes(templateNames, templateRegions, genes);
			
			ArrayList<Integer> geneFilteredKmerIndices = getIndicesOfGenesFilteredKmers(uniqueKmers.get(numGeneTest), templateBarcodes, templateGeneTypes);
			
			EntryWeights filteredEntryWeights  = getEntryWeights(templateBarcodes, geneFilteredKmerIndices, badKmers);
			//EntryWeights finalEntryWeights = getIntersectionEntryWeights(filteredEntryWeights, Typing.aggKmers.get(numGeneTest));
//			if (numGeneTest==8) {
//				filteredEntryWeights = getIntersectionEntryWeights(filteredEntryWeights, Typing.aggKmers.get(numGeneTest));
//			}

			TemplateScorer haploidScorer = new TemplateScorer(templateBarcodes, templateGeneTypes, templateNames, filteredEntryWeights);
			haploidScorer.setCopyNumber(templateCopyNumber);

			/*** Run All ***/
			sampleCopyValues[numGeneTest] = haploidScorer.getCopyValueOfSampleWithAggregate(barcode);
		}
		
		DecimalFormat df = new DecimalFormat("####0.00");
		System.out.print("\t");
		for (int i = 0; i < sampleCopyValues.length-1; ++i ) {
			System.out.print(df.format(sampleCopyValues[i]) + ",");
		}
		System.out.print(df.format(sampleCopyValues[sampleCopyValues.length-1]) + "");
		
		double tempSum = 0; for (int i = 0; i < sampleCopyValues.length; ++i) { tempSum += sampleCopyValues[i]; }
		for (int i = 0; i < sampleCopyValues.length; ++i) { sampleCopyValues[i] = (200*sampleCopyValues[i])/tempSum; }
		
		System.out.print("\t");
		for (int i = 0; i < sampleCopyValues.length-1; ++i ) {
			System.out.print(df.format(sampleCopyValues[i]) + ",");
		}
		System.out.print(df.format(sampleCopyValues[sampleCopyValues.length-1]) + "");
		
		CopyNumberScorer cnScorer = new CopyNumberScorer();
		CopyNumbers sampleCN = cnScorer.getCopyNumberOfSampleHaploid(sampleCopyValues);
		String strSampleCopyNumbers = sampleCN.getCopyNumbers();
		double[] entrywise = sampleCN.getEntrywiseScores(); System.out.print("\t" + df.format(entrywise[0])); for (int e = 1; e < entrywise.length; ++e) {	System.out.print("," + df.format(entrywise[e])); }
		System.out.print("\t" + sampleCN.getScore() + "\t" + sampleCN.getScalingFactor());
		if (Typing.copyNumbersToType.containsKey(strSampleCopyNumbers)) {
			System.out.print	("\t" + Typing.copyNumbersToType.get(strSampleCopyNumbers));
		} else { 
			System.out.print("\t" + strSampleCopyNumbers);
		}
	
		TemplateScores scores = cnScorer.scoreSampleHaploid(sampleCopyValues);
		CopyNumbers[] ranked = scores.rankTemplatesHaploid();
		strSampleCopyNumbers = ranked[0].getCopyNumbers();
		entrywise = ranked[0].getEntrywiseScores(); System.out.print("\t" + df.format(entrywise[0])); for (int e = 1; e < entrywise.length; ++e) {	System.out.print("," + df.format(entrywise[e])); }
		System.out.print("\t" + ranked[0].getScore() + "\t" + ranked[0].getScalingFactor());
		if (Typing.copyNumbersToType.containsKey(strSampleCopyNumbers)) {
			System.out.print("\t" + Typing.copyNumbersToType.get(strSampleCopyNumbers));
		} else { 
			System.out.print("\t" + strSampleCopyNumbers);
		}
		
		System.out.println();
	}
	
	

	
	
//	public static EntryWeights getEntryWeights(int[][] templateBarcodes, ArrayList<Integer> repetitiveKmers) {
//		double[] weights = new double[templateBarcodes[0].length];
//		Arrays.fill(weights, 1);
//		
//		
//		for (int i = 0; i < repetitiveKmers.size() ; ++i) {
//			if (repetitiveKmers.get(i)>-1) { weights[repetitiveKmers.get(i)] = 0; }
//		}
//		//int numKmers = 0;
//		//for (int i = 0; i < weights.length; ++i) { if (weights[i] == 1) {numKmers += 1;} }
//		//System.out.println("Number of k-mers: " + numKmers);
//		EntryWeights myEntryWeights = new EntryWeights(weights);
//		return myEntryWeights;
//	}
//	

	
	public static EntryWeights getEntryWeights(int[][] templateBarcodes, ArrayList<Integer> kmerIndices, ArrayList<Integer> repetitiveKmers) {
		double[] weights = new double[templateBarcodes[0].length];
		Arrays.fill(weights, 0);
		for (int i = 0; i < kmerIndices.size(); ++i) {
			if (kmerIndices.get(i)>-1) { weights[kmerIndices.get(i)] = 1; }
		}
		
		for (int i = 0; i < repetitiveKmers.size() ; ++i) {
			if (repetitiveKmers.get(i)>-1) { weights[repetitiveKmers.get(i)] = 0; }
		}
		int numKmers = 0;
		//for (int i = 0; i < weights.length; ++i) { if (weights[i] == 1) {numKmers += 1;} }
		//System.out.println("Number of k-mers: " + numKmers);
		EntryWeights myEntryWeights = new EntryWeights(weights);
		return myEntryWeights;
	}
	
	public static EntryWeights getIntersectionEntryWeights(EntryWeights weights1, EntryWeights weights2) {
		double[] array1 = weights1.getWeights(); 
		double[] array2 = weights2.getWeights();
		double[] newarray = new double[array1.length];
		for (int i = 0; i < array1.length; ++i) {
			if (array1[i]>0 && array2[i]>0) {
				newarray[i] = 1;
			} else { 
				newarray[i] = 0;
			}
		}
		EntryWeights myEntryWeights = new EntryWeights(newarray);
		return myEntryWeights;

	}
	
//	public static HashMap<String, ArrayList<Integer>> getGeneIndicesMapFromFile(String fileName) throws IOException {
//		HashMap<String, ArrayList<Integer>> geneIndicesMap = new HashMap<String, ArrayList<Integer>>();
//		
//		InputStream in = new FileInputStream(fileName);
//		final BufferedReader br = new BufferedReader(new InputStreamReader(in));
//		String line;
//		ArrayList<Integer> indices = new ArrayList<Integer>();
//		ArrayList<String> genes = new ArrayList<String>(); 
//		while ((line = br.readLine()) != null) {
//			if (line.startsWith("KIR")) {
//				if (genes.size()>0) { for (int g = 0; g < genes.size(); ++g) { geneIndicesMap.put(genes.get(g), indices); } }
//				genes = new ArrayList<String>();
//				indices = new ArrayList<Integer>();
//				String[] lineSplit = line.split("\\t");
//				for (int i = 0; i < lineSplit.length; ++i) { genes.add(lineSplit[i]); }
//				//geneIndicesMap.put(genes, new ArrayList<Integer> ());
//			} else {
//				String[] lineSplit = line.split("\\t");
//				indices.add(Integer.parseInt(lineSplit[0]));
//			}
//		}
//		if (genes.size()>0) { for (int g = 0; g < genes.size(); ++g) { geneIndicesMap.put(genes.get(g), indices); } }
//		return geneIndicesMap;
//	}
	
	private static boolean goodKmer(int[][] templateBarcodes, String[] templateTypes, int k) {
		boolean good = Boolean.TRUE;
		for (int i = 0; i < templateBarcodes.length; ++i) {
			if (templateTypes[i].equals("0")) {
				if (templateBarcodes[i][k] != 0) {
					good = Boolean.FALSE;
				}
			} 
			else 
			if (templateTypes[i].equals("1")) {
				if (templateBarcodes[i][k] == 0) {
					good = Boolean.FALSE;
				}
			}
		}
		return good;
	}
	
	public static ArrayList<Integer> getIndicesOfGenesFilteredKmers(ArrayList<Integer> indicesOfGenesKmers, int[][] templateBarcodes,String[] templateTypes) throws IOException {
		
		ArrayList<Integer> filteredIndices = new ArrayList<Integer>();
		for (int i = 0; i < indicesOfGenesKmers.size(); ++i) {
			if (indicesOfGenesKmers.get(i) > -1) {
			if (goodKmer(templateBarcodes, templateTypes, indicesOfGenesKmers.get(i))) {
				filteredIndices.add(indicesOfGenesKmers.get(i));
			}
			}
		}
		
		return filteredIndices;
	}
	
	

	
}
