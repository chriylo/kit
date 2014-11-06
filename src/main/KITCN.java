package main;

import generator.KMerFileReader;
import generator.Trie;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import typing.TypePredictor;
import allele.Preprocessor;
import barcodeFunctions.BarcodeFunctions;

public class KITCN {
	public static void main(String[] args) throws IOException {
		Params3 p = parse_cmd(args);	

		String reads = p.b;
		String freads1 = p.r1;
		String freads2 = p.r2;
		String freads = p.r;
		String sample = p.sample;
		int k = p.k;
		
		long startTime = System.currentTimeMillis();
		int time;
		
		//get barcode
		Trie t = KMerFileReader.getTrieFromFile(p.t);
	
		int[] barcode = new int[t.size()];
		Arrays.fill(barcode, 0);
		int[] counts; 
		if (freads1 != null) {
			counts = KMerFileReader.countKMersFile(freads1, k, t);
			barcode = BarcodeFunctions.addBarcodes(barcode, counts);	
		} 
		if (freads2 != null) {
			counts = KMerFileReader.countKMersFile(freads2, k, t);
			barcode = BarcodeFunctions.addBarcodes(barcode, counts);
		}
		if (freads != null) { 
			counts = KMerFileReader.countKMersFile(freads, k, t);
			barcode = BarcodeFunctions.addBarcodes(barcode, counts); 	
		}
		if (reads != null){
			counts = KMerFileReader.countKMersFile(reads, k, t);
			barcode = BarcodeFunctions.addBarcodes(barcode, counts);
		}	
		
		System.out.print(sample);
		//predict cn
		TypePredictor predictor = new TypePredictor();
		predictor.typeSample(barcode);
		
		time = (int) ((System.currentTimeMillis()-startTime));
		System.out.println("Total time (ms): " + time);

	}
	
	public static Params3 parse_cmd(String[] args) throws IOException {
		Params3 p = new Params3();

		Options options = new Options();
		options.addOption("s", true, "sample name");
		options.addOption("b", true, "bam file");
		options.addOption("bai", true, "bam index file");
		options.addOption("r1", true, "fastq paired-end reads 1 file");
		options.addOption("r2", true, "fastq paired-end reads2 file");
		options.addOption("r", true, "fastq reads file");
		options.addOption("h", false, "Show help");
		
		try {
			CommandLine cmd = new PosixParser().parse(options, args);
			
			p.t = "Data/Tries/trie50";
			p.k = 50;

			if(cmd.hasOption("h"))
				usage_exit();
			
			if(cmd.hasOption("s")) {
				p.sample = cmd.getOptionValue("s"); 
			} 
			
			if(cmd.hasOption("b")) {
				String bam = cmd.getOptionValue("b"); 
				if (!bam.endsWith("bam")) { usage_exit(); }
				p.b = bam; 
			} else { 
				//usage_exit(); 
			}
			
			if(cmd.hasOption("bai")) {
				String bai = cmd.getOptionValue("bai"); 
				if (!bai.endsWith("bai")) { usage_exit(); }
				p.i = bai; 
			} else { 
				//usage_exit(); 
			}
			if(cmd.hasOption("r1")) {
				p.r1 = cmd.getOptionValue("r1"); 
			} else { 
				//usage_exit(); 
			}
			
			if(cmd.hasOption("r2")) {
				p.r2 = cmd.getOptionValue("r2"); 
			} else { 
				//usage_exit(); 
			}
			
			if(cmd.hasOption("r")) {
				p.r = cmd.getOptionValue("r"); 
			} else { 
				//usage_exit(); 
			}
			
			
			
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		return p;
	}
	
	public static void usage_exit(){
		String usage = 
				"\tUsage:\n"
						+ "\tjava -jar KITCN.jar [OPTIONS...] \n"
						+ "\n"
						+ "\tOptions:\n" 
						+ "\t[-h] help\n"
						+ "\n";
		System.err.println("\n" + usage);
		System.exit(1);
	
	}
}
