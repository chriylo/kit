package main;

import generator.KMerFileReader;
import generator.Trie;

import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

public class GenerateCandidateIndicators {
	public static void main(String[] args) throws IOException {
		long startTime = System.currentTimeMillis();
		int k = 50;
		Params p = parse_cmd(args);	
		
		Trie t = BarcodeBuilder.getTrie(p.pathToTemplates, k, p.pathToTries, Boolean.TRUE);
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
}
