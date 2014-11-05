package main;

import generator.*;
import allele.*;
import barcodeFunctions.BarcodeFunctions;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.text.DecimalFormat;

import net.sf.samtools.SAMFileReader;
import net.sf.samtools.SAMRecord;
import typing.TemplateScorer;
import typing.TemplateScores;
import typing.TypePredictor;
import typing.Typing;
import distanceFunction.EntryWeights;
import distanceFunction.EntrywiseVecDistance;
import distanceFunction.NormalCDFVecDistance;
import typing.CopyNumberScorer;

public class Test5 {
	private static Comparator<String[]> typeComparator = new Comparator<String[]>() { 
		public int compare(String[] a, String[] b) {return ((String) a[1]).compareTo(b[1]); } 
	};



	public static void main(String[] args) throws IOException {
		int c = 50;
		
		/*** Inputs ***/
		int k = 50;
		
		String pathToReads = "Data/Barcodes/TemplateReads"+c+"x_RefVar1Reads"+c+"x_"+k;
		String pathToReads2 = "Data/Barcodes/TemplateReads"+c+"x_RefVar2Reads"+c+"x_"+k;
		//String pathTo1k = "Data/Barcodes/deCODEtrios/";
		
		String pathTo1k = "Data/Barcodes/deCODEpop/";
		//String pathTo1k = "Data/Barcodes/1ktrios/";

		/*** Run Tests ***/
		TypePredictor predictor = new TypePredictor();
		
				
		testHaploidScaled(predictor);
		//testHaploidSimulated(predictor, pathToReads);
		//testHaploidSimulated(predictor, pathToReads2);
		//testDiploidScaled(predictor);
		//testDiploidSimulated(predictor, pathToReads, pathToReads2);
		//testTrios(predictor, pathToTrios);
		//test1k(predictor, pathTo1k);
		
		//mainHelper(5);
		//mainHelper(15);
		//mainHelper(30);
		//mainHelper(50);
		
	}
	
	public static void mainHelper(int c) throws IOException {
		/*** Inputs ***/
		int k = 50;

		String pathToReads = "Data/Barcodes/TemplateReads"+c+"x_RefVar1Reads"+c+"x_"+k;
		String pathToReads2 = "Data/Barcodes/TemplateReads"+c+"x_RefVar2Reads"+c+"x_"+k;
		
		/*** Run Tests ***/
		TypePredictor predictor = new TypePredictor();
		
		testDiploidSimulated(predictor, pathToReads, pathToReads2);

	}
	
	public static void test1k(TypePredictor predictor, String pathTo1k) throws IOException{
		//String[] thousand = new String[] {"NA06985" };
		
		File dir = new File(pathTo1k);
		String[] fileNames = dir.list();
		
		for (int i=0; i<fileNames.length; ++i) {
		//int i = 2;
			String fileName = fileNames[i];
			if (!fileName.endsWith("barcod")) { continue; }
			System.out.print(fileName);		
			int[] barcode = KMerFileReader.getBarcodeFromFile(dir.getAbsolutePath() + File.separatorChar + fileName);
			
			predictor.typeSample(barcode);
		}
	}
	
	public static void testTrios(TypePredictor predictor, String pathToTrios) throws IOException{
		String[] trios = new String[] {"NA12877","NA12878","NA12882","NA12889","NA12890","NA19238","NA19239","NA19240","HG00731","HG00732","HG00733","HG02024","HG02025","HG02026" };
		
		for (int r=0; r<trios.length;++r) {
			System.out.print(trios[r]);		
			File triopath = new File(pathToTrios+trios[r]+"_50.barcod");
			String tr = triopath.getAbsolutePath();
			int[] trioBarcode = KMerFileReader.getBarcodeFromFile(tr);
			
			predictor.typeSample(trioBarcode);
		}
	}
	
	public static void testHaploidScaled(TypePredictor predictor) throws IOException {
		
		int[][] templateBarcodes = Typing.templateBarcodes;
		ArrayList<String> templateNames = Typing.getNames();
		ArrayList<String> templateTypes = Typing.getTypes();
		HashMap<String, String> typeToCopyNumbers = Typing.getTypeToCopyNumbers();
		
		int[][] scaledTemplateBarcodes = new int[templateBarcodes.length][templateBarcodes[0].length];
		for (int i = 0; i < templateBarcodes.length; ++i) {
			for (int j = 0; j < templateBarcodes[i].length; ++j) {
				scaledTemplateBarcodes[i][j] = templateBarcodes[i][j]*15;
			}
		}
		
		for (int numSample = 0; numSample < scaledTemplateBarcodes.length; ++numSample) {
			System.out.print(templateNames.get(numSample));
			System.out.print("\t" + templateTypes.get(numSample));
			System.out.print("\t" + typeToCopyNumbers.get(templateTypes.get(numSample)));
			predictor.typeSampleHaploid(scaledTemplateBarcodes[numSample]);
		}

	}
	
	public static void testHaploidSimulated(TypePredictor predictor, String pathToReads) throws IOException {
		File readpath = new File(pathToReads);
		File[] readBarcodeFiles = readpath.listFiles();
		Arrays.sort(readBarcodeFiles);

		int[][] readBarcodes = new int[readBarcodeFiles.length][];
		for (int j = 0; j < readBarcodeFiles.length; j++) {
			String t1 = readBarcodeFiles[j].getAbsolutePath();
			readBarcodes[j] = KMerFileReader.getBarcodeFromFile(t1);
		}
		ArrayList<String> readNames = getNames(readBarcodeFiles);	
		ArrayList<String> readTypes = getTypes(readBarcodeFiles);
		HashMap<String, String> typeToCopyNumbers = Typing.getTypeToCopyNumbers();
		
		for (int numSample = 0; numSample < readBarcodes.length; ++numSample) {
		//int numSample = 23;
			System.out.print(readNames.get(numSample));
			System.out.print("\t" + readTypes.get(numSample));
			System.out.print("\t" + typeToCopyNumbers.get(readTypes.get(numSample)));
			predictor.typeSampleHaploid(readBarcodes[numSample]);
		
			
		}
		
		
	}
	
	public static void testDiploidSimulated(TypePredictor predictor, String pathToReads, String pathToReads2) throws IOException {
		File readpath = new File(pathToReads);
		File[] readBarcodeFiles = readpath.listFiles();
		Arrays.sort(readBarcodeFiles);

		int[][] readBarcodes = new int[readBarcodeFiles.length][];
		for (int j = 0; j < readBarcodeFiles.length; j++) {
			String t1 = readBarcodeFiles[j].getAbsolutePath();
			readBarcodes[j] = KMerFileReader.getBarcodeFromFile(t1);
		}
		
		File readpath2 = new File(pathToReads2);
		File[] readBarcodeFiles2 = readpath2.listFiles();
		Arrays.sort(readBarcodeFiles2);

		int[][] readBarcodes2 = new int[readBarcodeFiles2.length][];
		for (int j = 0; j < readBarcodeFiles2.length; j++) {
			String t1 = readBarcodeFiles2[j].getAbsolutePath();
			readBarcodes2[j] = KMerFileReader.getBarcodeFromFile(t1);
		}
		
		int[][] diploidReadBarcodes = getDiploidBarcodes(readBarcodes, readBarcodes2);
		ArrayList<String> diploidReadNames = getDiploidNames(readBarcodeFiles, readBarcodeFiles2);
		ArrayList<String> diploidReadTypes = getDiploidTypes(readBarcodeFiles, readBarcodeFiles2);
		HashMap<String, String> diploidTypeToCopyNumbers = Typing.getDiploidTypeToCopyNumbers();

		ArrayList<Integer> blah = new ArrayList<Integer>();
		blah.add(110);
		blah.add(122);
		
		
		
		for (int numSample = 0; numSample < diploidReadBarcodes.length; ++numSample) {
			
			//if (blah.contains(numSample)) {
			System.out.print(numSample + "\t");
			System.out.print(diploidReadNames.get(numSample));
			System.out.print("\t" + diploidReadTypes.get(numSample));
			System.out.print("\t" + diploidTypeToCopyNumbers.get(diploidReadTypes.get(numSample)));
			predictor.typeSample(diploidReadBarcodes[numSample]);
			//}
		}
	}
	
	public static void testDiploidScaled(TypePredictor predictor) throws IOException {
		
		int[][] diploidTemplateBarcodes = Typing.getDiploidBarcodes();
		ArrayList<String> diploidTemplateNames = Typing.getDiploidNames();
		ArrayList<String> diploidTemplateTypes = Typing.getDiploidTypes();
		HashMap<String, String> diploidTypeToCopyNumbers = Typing.getDiploidTypeToCopyNumbers();
		
		int[][] scaledDiploidTemplateBarcodes = new int[diploidTemplateBarcodes.length][diploidTemplateBarcodes[0].length];
		for (int i = 0; i < diploidTemplateBarcodes.length; ++i) {
			for (int j = 0; j < diploidTemplateBarcodes[i].length; ++j) {
				scaledDiploidTemplateBarcodes[i][j] = diploidTemplateBarcodes[i][j]*15;
			}
		}
		
		for (int numSample = 0; numSample < scaledDiploidTemplateBarcodes.length; ++numSample) {

			System.out.print(diploidTemplateNames.get(numSample));
			System.out.print("\t" + diploidTemplateTypes.get(numSample));
			System.out.print("\t" + diploidTypeToCopyNumbers.get(diploidTemplateTypes.get(numSample)));
			predictor.typeSample(scaledDiploidTemplateBarcodes[numSample]);
		}

	}
	
	public static ArrayList<String> getNames(File[] templateBarcodes) {
		ArrayList<String> names = new ArrayList<String>();
		for (int i = 0; i < templateBarcodes.length; ++i) {
			for (Iterator it = Typing.templateTypes.keySet().iterator(); it.hasNext(); ) {
				String key = (String) it.next();
				if (templateBarcodes[i].toString().contains(key)) {
					names.add(key);
				}
			}
		}
		return names;
	}
	
	public static ArrayList<String> getDiploidNames(File[] templateBarcodes1, File[] templateBarcodes2) {
		int barlen = templateBarcodes1.length;
		ArrayList<String> diploidTemplateNames = new ArrayList<String>();
		ArrayList<String> templateNames1 = getNames(templateBarcodes1);
		ArrayList<String> templateNames2 = getNames(templateBarcodes2);
		int currIndex = 0;
		for (int i = 0; i < templateNames1.size(); ++i) {
			for (int j = i; j < templateNames2.size(); ++j){
				String[] temp = new String[2];
				temp[0] = templateNames1.get(i);
				temp[1] = templateNames2.get(j);
				Arrays.sort(temp);
				diploidTemplateNames.add(temp[0] + "_" + temp[1]);
				currIndex+=1;
			}
		}
		
		return diploidTemplateNames;
	}
	
	public static ArrayList<String> getDiploidNames(File[] templateBarcodes) {
		int barlen = templateBarcodes.length;
		ArrayList<String> diploidTemplateNames = new ArrayList<String>();
		ArrayList<String> templateNames = getNames(templateBarcodes);
		int currIndex = 0;
		for (int i = 0; i < templateNames.size(); ++i) {
			for (int j = i; j < templateNames.size(); ++j){
				String[] temp = new String[2];
				temp[0] = templateNames.get(i);
				temp[1] = templateNames.get(j);
				Arrays.sort(temp);
				diploidTemplateNames.add(temp[0] + "_" + temp[1]);
				currIndex+=1;
			}
		}
		
		return diploidTemplateNames;
		//set types
	}
	
	public static ArrayList<String> getTypes(File[] templateBarcodes) {
		ArrayList<String> types = new ArrayList<String>();
		for (int i = 0; i < templateBarcodes.length; ++i) {
			for (Iterator it = Typing.templateTypes.keySet().iterator(); it.hasNext(); ) {
				String key = (String) it.next();
				if (templateBarcodes[i].toString().contains(key)) {
					types.add(Typing.templateTypes.get(key));
				}
			}
		}
		return types;
		
         
	}
	
	public static ArrayList<String> getDiploidTypes(File[] templateBarcodes1, File[] templateBarcodes2) {
		int barlen = templateBarcodes1.length;
		ArrayList<String> diploidTemplateTypes = new ArrayList<String>();
		ArrayList<String> templateTypes1 = getTypes(templateBarcodes1);
		ArrayList<String> templateTypes2 = getTypes(templateBarcodes2);
		int currIndex = 0;
		for (int i = 0; i < templateTypes1.size(); ++i) {
			for (int j = i; j < templateTypes2.size(); ++j){
				String[] temp = new String[2];
				temp[0] = templateTypes1.get(i);
				temp[1] = templateTypes2.get(j);
				Arrays.sort(temp);
				diploidTemplateTypes.add(temp[0] + "_" + temp[1]);
				currIndex+=1;
			}
		}
		
		return diploidTemplateTypes;
	}
	
	public static ArrayList<String> getDiploidTypes(File[] templateBarcodes) {
		int barlen = templateBarcodes.length;
		ArrayList<String> diploidTemplateTypes = new ArrayList<String>();
		ArrayList<String> templateTypes = getTypes(templateBarcodes);
		int currIndex = 0;
		for (int i = 0; i < templateTypes.size(); ++i) {
			for (int j = i; j < templateTypes.size(); ++j){
				String[] temp = new String[2];
				temp[0] = templateTypes.get(i);
				temp[1] = templateTypes.get(j);
				Arrays.sort(temp);
				diploidTemplateTypes.add(temp[0] + "_" + temp[1]);
				currIndex+=1;
			}
		}
		
		return diploidTemplateTypes;
		//set types
	}
	
	public static int[][] getDiploidBarcodes(int[][] barcodes) {
		int numBarcodes = barcodes.length;
		int[][] diploidBarcodes = new int[(((numBarcodes*numBarcodes)-numBarcodes)/2)+numBarcodes][];
		int currIndx = 0;
		for (int i = 0; i < numBarcodes; ++i) {
			for (int j = i; j < numBarcodes; ++j) {
				diploidBarcodes[currIndx]=BarcodeFunctions.addBarcodes(barcodes[i], barcodes[j]);
				currIndx += 1;
			}
		}
		return diploidBarcodes;
	}

	public static int[][] getDiploidBarcodes(int[][] barcodes, int[][] barcodes2) {
		//int[][] diploidBarcodes = new int[barcodes.length*barcodes2.length][];
		int numBarcodes = barcodes.length;
		int[][] diploidBarcodes = new int[(((numBarcodes*numBarcodes)-numBarcodes)/2)+numBarcodes][];

		int currIndx = 0;
		for (int i = 0; i < barcodes.length; ++i) {
			//for (int j = 0; j < barcodes2.length; ++j) {
			for (int j = i; j < barcodes2.length; ++j) {
				diploidBarcodes[currIndx]=BarcodeFunctions.addBarcodes(barcodes[i], barcodes[j]);
				currIndx += 1;
			}
		}
		return diploidBarcodes;
	}
	
}
