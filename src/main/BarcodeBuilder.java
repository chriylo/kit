package main;

import generator.*;
import barcodeFunctions.*;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import distanceFunction.EntrywiseVecDistance;
import distanceFunction.NormalCDFVecDistance;

import java.io.*;
import java.util.*;



public class BarcodeBuilder {
	protected static Params param;

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		//EntrywiseVecDistance distanceCalculator = new NormalCDFVecDistance();
		//System.out.println(distanceCalculator.distance(KMerFileReader.getBarcodeFromFile("Data/Barcodes/ReferenceReadsVariants/RefVar1Reads5x_50.barcod"), KMerFileReader.getBarcodeFromFile("Data/Barcodes/ReferenceVariants/reference_var1_50.barcod")));
		//System.out.println(distanceCalculator.distance(KMerFileReader.getBarcodeFromFile("Data/Barcodes/ReferenceReadsVariants/RefVar1Reads5x_50.barcod"), KMerFileReader.getBarcodeFromFile("Data/Barcodes/ReferenceVariants/reference_var2_50.barcod")));
		//System.out.println(distanceCalculator.distance(KMerFileReader.getBarcodeFromFile("Data/Barcodes/ReferenceReadsVariants/RefVar1Reads5x_50.barcod"), KMerFileReader.getBarcodeFromFile("Data/Barcodes/Reference/reference50.barcod")));

		long startTime = System.currentTimeMillis();

		
		int k = 50;

		Params p = parse_cmd(args);	
		
		Trie t = KMerFileReader.getTrieFromFile(p.pathToTrieFile);
		
//		//Building the barcode of reads
		String outputFileName = p.outputFileName;
		int[] barcode = getBarcode(p.pathToReads, t, k);
		KMerFileReader.printBarcodeToFile(outputFileName, barcode);

		int time = (int) ((System.currentTimeMillis()-startTime));
		System.out.println("Total time (ms): " + time);
		
//		//Build template barcodes
//		String pathTotemplatesDir = "Data/TemplatesMasked/";
//		String pathToTemplateBarcodesDir = "Data/Barcodes/Template_Ref_"+k;	
//		//String pathToReferenceDir = "Data/Reference/ReferenceMasked/;
//		//int[] referenceBarcode = getBarcode(pathToReferenceDir, t, k) ;
//		//KMerFileReader.printBarcodeToFile("Data/Barcodes/Reference/reference"+k, referenceBarcode);
//		int[] referenceBarcode = KMerFileReader.getBarcodeFromFile("Data/Barcodes/Reference/reference50.barcod");
//		getTemplateBarcodes(pathTotemplatesDir, pathToTemplateBarcodesDir, referenceBarcode, t, k);

//		String pathTotemplatesDir = "Data/TemplateReads30x/";
//		int[] referenceBarcodeTemp = KMerFileReader.getBarcodeFromFile("Data/Barcodes/ReferenceReadsVariants/RefVar1Reads5x_50.barcod");
//		int[] referenceBarcode = barcodeFunctions.BarcodeFunctions.multBarcode(referenceBarcodeTemp, 6);
//		getTemplateBarcodesDir(pathTotemplatesDir, "Data/Barcodes/TemplateReads30x_RefVar1Reads30x_50", referenceBarcode, t, k);
		
	}
	
	

	public static Params parse_cmd(String[] args) {
		Params p = new Params();

		Options options = new Options();
		options.addOption("i", true, "indicator file");
		options.addOption("r", true, "directory of FASTA read files");
		options.addOption("o", true, "output barcode file");
		options.addOption("h", false, "Show help");
		
		try {
			CommandLine cmd = new PosixParser().parse(options, args);
			// help
			if(cmd.hasOption("h"))
				usage_exit();
			
			if(cmd.hasOption("i")) {
			p.pathToTrieFile = cmd.getOptionValue("i"); 
			} else { usage_exit(); }
			
			
//			if(cmd.hasOption("r")) {
//				p.test = 1;
				if(cmd.hasOption("r")) {
				p.pathToReads = cmd.getOptionValue("r"); 
				} else { usage_exit(); }
				if(cmd.hasOption("o")) {
					p.outputFileName = cmd.getOptionValue("o"); 
				} else { usage_exit(); }
//			} 
//			else if(cmd.hasOption("Tb")) {
//				p.test = 2;
//				if (cmd.hasOption("Tb")) {
//				p.pathToTemplateBarcodesDir = cmd.getOptionValue("Tb"); 
//				} else { usage_exit(); }
//					
//				if(cmd.hasOption("R")) {
//					p.pathToReferenceDir = cmd.getOptionValue("R"); 
//				} else { usage_exit(); }
//			} 
//			else { usage_exit(); }	
			
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		return p;
	}
	
	public static void usage_exit(){
		String usage = 
				"\tUsage:\n"
						+ "\tjava -jar barcode.jar [OPTIONS...] \n"
						+ "\n"
						+ "\tOptions:\n" 
						+ "\t[-i] indicator file\n"
						//+ "\t[-t] trie directory\n"
						//+ "\t[-T] templates directory\n"
						//+ "\tConstruct barcode for reads: \n" 
						+ "\t[-r] reads directory\n"
						+ "\t[-o] output .barcod file\n"
						//+ "\tConstruct barcode for templates: \n"
						//+ "\t\t[-Tb] template barcode directory\n"
						//+ "\t\t[-R] reference genome directory\n"
						+ "\t[-h] help\n"
						+ "\n";
		System.err.println("\n" + usage);
		System.exit(1);
	
	}
	
	//for scaled template barcodes (just one file per template)
	public static void getTemplateBarcodes(String pathToTemplatesDir, String pathToTemplateBarcodesDir, int[] referenceBarcode, Trie t, int k) throws IOException {
		File templateBarcodesDir = new File(pathToTemplateBarcodesDir);
		
		File templateFiles = new File(pathToTemplatesDir);
		String[] templateFileNames = templateFiles.list();
		Arrays.sort(templateFileNames);
		for (int i=0; i<templateFileNames.length; ++i){
			if (templateFileNames[i].startsWith(".")) { continue; } 
			int[] templateBarcode = KMerFileReader.countKMersFile(templateFiles.getAbsolutePath() + File.separatorChar + templateFileNames[i], k,t);
			int[] templateRefBarcode = BarcodeFunctions.addBarcodes(templateBarcode, referenceBarcode); 
			KMerFileReader.printBarcodeToFile(templateBarcodesDir.getAbsolutePath() + File.separatorChar + templateFileNames[i] + ".barcod", templateRefBarcode);
		}
		
	}
	
	//for simulated template barcodes (reads are in a directory)
	public static void getTemplateBarcodesDir(String pathToTemplatesDir, String pathToTemplateBarcodesDir,  int[] referenceBarcode, Trie t, int k) throws IOException {
		File templateBarcodesDir = new File(pathToTemplateBarcodesDir);

		File templateFiles = new File(pathToTemplatesDir);
		String[] templateFileNames = templateFiles.list();
		Arrays.sort(templateFileNames);
		for (int i=0; i<templateFileNames.length; ++i){
			if (templateFileNames[i].startsWith(".")) { continue; } 
			int[] templateBarcode = getBarcode(templateFiles.getAbsolutePath() + File.separatorChar + templateFileNames[i], t, k);
			int[] templateRefBarcode = BarcodeFunctions.addBarcodes(templateBarcode, referenceBarcode); 
			KMerFileReader.printBarcodeToFile(templateBarcodesDir.getAbsolutePath() + File.separatorChar + templateFileNames[i] + ".barcod", templateRefBarcode);

		}

	}
	
	public static int[] getBarcode(String pathToFastas, Trie t, int k) throws IOException {
		long start = System.currentTimeMillis();

		int[] barcode = new int[t.size()];
		Arrays.fill(barcode, 0);
		
		//Generate template reads barcode
		File dir = new File(pathToFastas);
		String[] fileNames = dir.list();
		
		if (fileNames.length > 0) {
		Arrays.sort(fileNames);
		
		for (int i=0; i<fileNames.length; ++i) {
			
			String fileName = fileNames[i];
			if (fileName.startsWith(".")) { continue; }
			System.out.println(fileName);
			int[] counts = KMerFileReader.countKMersFile(dir.getAbsolutePath() + File.separatorChar + fileName, k, t);
			barcode = BarcodeFunctions.addBarcodes(barcode, counts);			
		}
		
		}
		long time = (System.currentTimeMillis()-start);
		System.out.println("Time to build barcode (ms): " + time);
		return barcode;
		
	}
	
	public static int[] getBarcodeFile(String fileName, Trie t, int k) throws IOException {
		long start = System.currentTimeMillis();

		int[] barcode = new int[t.size()];
		Arrays.fill(barcode, 0);
		
		//Generate template reads barcode
		//File dir = new File(pathToFastas);
		//String[] fileNames = dir.list();
		
		//if (fileNames.length > 0) {
		//Arrays.sort(fileNames);
		
		//for (int i=0; i<fileNames.length; ++i) {
			//System.out.println(fileName);
			File f = new File(fileName);
			int[] counts = KMerFileReader.countKMersFile(f.getAbsolutePath(), k, t);
			barcode = BarcodeFunctions.addBarcodes(barcode, counts);			
		//}
		
		//}
		long time = (System.currentTimeMillis()-start)/1000;
		System.out.println("Running time (sec): " + time);
		return barcode;
		
	}
	
	
	
	public static Trie getTrie(String pathToTemplates, int k) throws IOException {
		//Build trie
		File dir = new File(pathToTemplates);
		long start = System.currentTimeMillis();
		Trie t = KMerFileReader.extractKMersDir(dir, k);
		long time = (System.currentTimeMillis()-start)/1000;
		//System.out.println("Number of words in trie: " + t.size() + ", building time (sec): " + time);
		
				
		return t;
	}
	
	public static Trie getTrie(String pathToTemplates, int k, String pathToTries, boolean output) throws IOException {
		Trie t = getTrie(pathToTemplates, k);
		//Output trie
		if (output) {
			File triesDir = new File(pathToTries);
			String trieFile = triesDir.getAbsolutePath() + File.separatorChar + "customindicators" + k;
			KMerFileReader.printTrieWordsToFile(trieFile, t);
		}
				
		return t;
	}
	
	public static Trie getTrieFromFile(String filename) throws IOException  {
		return KMerFileReader.getTrieFromFile(filename);
	}
	
	
	
	
	
		

}
