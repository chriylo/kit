package main;

import generator.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import typing.TypePredictor;
import typing.Typing;

public class Predictor {

	public static void main(String[] args) throws IOException {
//		int k = 50;
//		String pathToTemplates = "Data/TemplatesMasked";
//		String pathToTemplateBarcodes = "Data/Barcodes/Templates_Ref_50";
//		String barcodeFileName = "Data/Barcodes/NA12877_50.barcod";
		
		Params p = parse_cmd(args);
//		int k = 50;
//		Trie t = KMerFileReader.getTrieFromFile(p.pathToTrieFile);
//		String pathToTemplates = p.pathToTemplates;
//		String pathToGeneIndicesFile = p.pathToGeneIndicesFile;
//		String pathToTemplateBarcodes = p.pathToTemplateBarcodes;
		String barcodeFileName = p.barcodeFileName;

		long startTime = System.currentTimeMillis();

//		TypePredictor predictor = new TypePredictor(k, pathToTemplates, pathToTemplateBarcodes);
//		TypePredictor predictor = new TypePredictor(pathToGeneIndicesFile);
		TypePredictor predictor = new TypePredictor();
		
		System.out.print(barcodeFileName);
		predictor.typeSample(barcodeFileName);
		int time = (int) ((System.currentTimeMillis()-startTime));
		System.out.println("Total time (ms): " + time);
	} 

	
//	public static void main(String[] args) throws IOException {
//		Params p = parse_cmd(args);
//
//		PrintStream ps;
//		ps = new PrintStream("Data/indices.txt");
//		
//		int k = 50;
//
//		String pathToTemplates = p.pathToTemplates;
//		Trie t = KMerFileReader.getTrieFromFile(p.pathToTrieFile);
//		ArrayList<Integer> KIR3DL2KmerIndices = TypePredictor.getIndicesOfRegionKmers(new File(pathToTemplates), k, t, "KIR3DL2", "KIR3DL2");
//		ps.println("KIR3DL2");
//		for (int i = 0; i < KIR3DL2KmerIndices.size(); ++i) { ps.println(KIR3DL2KmerIndices.get(i)); }
//		ArrayList<ArrayList<String>> geneTests = Typing.getGeneTests();
//		for (int numGeneTest = 0; numGeneTest < geneTests.size(); numGeneTest+=1) {
//			ArrayList<String> genes = geneTests.get(numGeneTest);
//			ArrayList<Integer> geneKmerIndices = TypePredictor.getIndicesOfGenesKmers(new File(pathToTemplates), genes, k, t);
//			for (int i = 0; i < genes.size()-1; ++i) { ps.print(genes.get(i)+"\t"); }; ps.println(genes.get(genes.size()-1));
//			for (int i = 0; i < geneKmerIndices.size(); ++i) { ps.println(geneKmerIndices.get(i)); }
//		}
//	}
	
	public static Params parse_cmd(String[] args) {
		Params p = new Params();

		Options options = new Options();
		options.addOption("T", true, "directory of FASTA template files");
		options.addOption("t", true, "path to template .barcod files");
		options.addOption("i", true, "indicator file");
		options.addOption("g", true, "path to gene indices files");
		options.addOption("s", true, "sample .barcod file");
		options.addOption("h", false, "Show help");
		try {
			CommandLine cmd = new PosixParser().parse(options, args);
			// help
			if(cmd.hasOption("h"))
				usage_exit();
	
//			if(cmd.hasOption("T")) {
//				p.pathToTemplates = cmd.getOptionValue("T"); 
//			} else { usage_exit(); }
			
//			if(cmd.hasOption("t")) {
//				p.pathToTemplateBarcodes = cmd.getOptionValue("t"); 
//			} else { usage_exit(); }
//			
//			if(cmd.hasOption("i")) {
//				p.pathToTrieFile = cmd.getOptionValue("i"); 
//			} else { usage_exit(); }
//			
//			if(cmd.hasOption("g")) {
//				p.pathToGeneIndicesFile = cmd.getOptionValue("g"); 
//			} else { usage_exit(); }
			
			if(cmd.hasOption("s")) {
				p.barcodeFileName = cmd.getOptionValue("s"); 
			} else { usage_exit(); }
			
			
			
			
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		return p;
	}
	
	public static void usage_exit(){
		String usage = 
				"\tUsage:\n"
						+ "\tjava -jar typePredictor.jar [OPTIONS...] \n"
						+ "\n"
						+ "\tOptions:\n" 
						//+ "\t[-T] templates directory\n"
						+ "\t[-t] template barcode directory\n"
						+ "\t[-i] gene indices barcode file\n"
						+ "\t[-s] sample .barcod file \n"
						+ "\t[-h] help\n"
						+ "\n";
		System.err.println("\n" + usage);
		System.exit(1);
	
	}
}
