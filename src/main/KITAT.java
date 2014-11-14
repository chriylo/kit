package main;

import generator.KMerFileReader;
import generator.Trie;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import net.sf.samtools.util.RuntimeIOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import typing.TypePredictor;
import allele.Allele;
import allele.AlleleScorer;
import allele.AlleleTypingMix;
import allele.Preprocessor;
import barcodeFunctions.BarcodeFunctions;

public class KITAT {
	public static void main(String[] args) throws IOException {
		Params3 p = parse_cmd(args);	

		
		
		long startTime = System.currentTimeMillis();
		int time;
		
		filterReads(p);
		preprocess(p); 
		callAllele(p);
		
		time = (int) ((System.currentTimeMillis()-startTime));
		System.out.println("Total time (ms): " + time);

	}
	
	public static Params3 parse_cmd(String[] args) {
		Params3 p = new Params3();

		Options options = new Options();
		options.addOption("s", true, "sample name");
		options.addOption("c", true, "copy numbers separated by ,");
		options.addOption("b", true, "bam file");
		options.addOption("bai", true, "bam index file");
		options.addOption("r1", true, "fastq paired-end reads 1 file");
		options.addOption("r2", true, "fastq paired-end reads2 file");
		options.addOption("r", true, "fastq reads file");
		options.addOption("d", true, "temp output directory");
		options.addOption("o", true, "output directory");
		options.addOption("h", false, "Show help");
		
		try {
			CommandLine cmd = new PosixParser().parse(options, args);

			if(cmd.hasOption("h"))
				usage_exit();
			
			p.t = "Data/Tries/trieSource50";
			p.g = "Data/Genes/";
			p.k = 50;
			
			if(cmd.hasOption("s")) {
				p.sample = cmd.getOptionValue("s"); 
			} 
			
			if(cmd.hasOption("c")) {
				String temp = cmd.getOptionValue("c"); 
				if (temp.contains(",")) {
					String[] tempArray = temp.split(",");
					if (tempArray.length != Genes.geneTests.size()) { usage_exit(); }
					p.c = new int[tempArray.length];
					for (int i = 0; i < tempArray.length; ++i) { p.c[i] = Integer.valueOf(tempArray[i]); }
				} else {
					if (temp.contains("_")) {
						HashMap<String, int[]> diploidTypeToCopyNumbers = Genes.getDiploidTypeCopyNumbers();
						if (!diploidTypeToCopyNumbers.containsKey(temp)) { usage_exit(); }
						p.c = diploidTypeToCopyNumbers.get(temp);
					} else {
						//usage_exit();
					}
				}
			} else { 
				//usage_exit(); 
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
			
			if (cmd.hasOption("d")) {
				p.d = cmd.getOptionValue("d");
			} else { 
				//usage_exit(); 
			}
			
			if (cmd.hasOption("o")) {
				p.o = cmd.getOptionValue("o");
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
	
	public static void filterReads(Params3 params) throws IOException {
		String template1Name = params.sample;
		String reads = params.b;
		String readsIndex = params.i;
		String freads1 = params.r1;
		String freads2 = params.r2;
		String freads = params.r;
		int k = params.k;
		String trie = params.t;
		String tempDir = params.d;

		
		Trie t = KMerFileReader.getTrieFromFileWithSource(trie);
		
		//long startTime = System.currentTimeMillis();
		
		if (freads1 != null && freads2 != null) {
			Preprocessor.filterPairedReadsFast(freads1, freads2, t, k, tempDir, template1Name);
			if (freads != null) { Preprocessor.filterReadsFast(freads, t, k, tempDir, template1Name); } 	
		} else if (freads != null) {
			Preprocessor.filterReadsFast(freads, t, k, tempDir, template1Name);
		} else {
			Preprocessor.filterSAMReadsFast(reads, readsIndex, t, k, tempDir, template1Name);
		}
		
		//int time = (int) ((System.currentTimeMillis()-startTime));
		//System.out.println("Time to filter reads (ms): " + time);

	}
	
	public static void preprocess(Params3 params) throws IOException {
		

		String template1Name = params.sample;
		String geneDir = params.g;
		String tempDir = params.d;
		
		//long startTime = System.currentTimeMillis();

		
		for (int geneIndex = 0; geneIndex < Genes.getGeneTests().size(); ++geneIndex) {
		String geneName = Genes.getGeneTests().get(geneIndex).get(0);
			

		
			String outpath = tempDir + geneIndex + "." + template1Name + "_fir.fa";
			String outpath2 = tempDir + geneIndex + "." + template1Name + "_sec.fa";
			String outpath3 = tempDir + geneIndex + "." + template1Name + ".fa";

			//2. Map filtered reads to gene reference
	        String geneRef = geneDir + geneName + "/" + geneName + ".fasta";
	        String aligned = tempDir + geneIndex + "." + template1Name + ".sam";
			String alignedunpaired = tempDir + geneIndex + "." + template1Name + ".unpaired.sam";
			String alignedbam = tempDir + geneIndex + "." + template1Name + ".bam";
			String alignedbamunpaired = tempDir + geneIndex + "." + template1Name + ".unpaired.bam";
			String finalalignedbam = alignedbam;
			
			Process p; String[] cmd = new String[3]; cmd[0] = "/bin/sh"; cmd[1] = "-c"; String s;
			
			File paired1f = new File(outpath); File paired2f = new File(outpath2);
			if (paired1f.exists() && paired2f.exists()) {
			cmd[2] = "bwa mem " + geneRef + " " + outpath + " " + outpath2 + " > " + aligned;
			p = Runtime.getRuntime().exec(cmd);
			try {
				p.waitFor();
				p.destroy();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			 			
			//3. Call variants 
			cmd[2] = "samtools view -Sb " + aligned + " > " + alignedbam ;
			p = Runtime.getRuntime().exec(cmd);
			try {
				p.waitFor();
				p.destroy();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			}
			
			//3.5 Align unpaired reads
			File unpairedf = new File(outpath3);
			if (unpairedf.exists()) {
				cmd[2] = "bwa mem " + geneRef + " " + outpath3 + " > " + alignedunpaired;
				p = Runtime.getRuntime().exec(cmd);
				try {
					p.waitFor();
					p.destroy();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
				//while ((s = stdError.readLine()) != null) { System.out.println(s); }
			
				cmd[2] = "samtools view -Sb " + alignedunpaired + " > " + alignedbamunpaired ;
				p = Runtime.getRuntime().exec(cmd);
				try {
					p.waitFor();
					p.destroy();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				finalalignedbam = alignedbamunpaired;
				
			}
			
			if (paired1f.exists() && paired2f.exists() && unpairedf.exists()) {
				//merge bam files
				finalalignedbam = tempDir + geneIndex + "." + template1Name + ".paired.unpaired.bam";
				cmd[2] = "samtools merge -f " + finalalignedbam + " " + alignedbam + " " + alignedbamunpaired;
				p = Runtime.getRuntime().exec(cmd);
				try {
					p.waitFor();
					p.destroy();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			String alignedsortedbam = tempDir + geneIndex + "." + template1Name + ".sorted";
			cmd[2] = "samtools sort " + finalalignedbam + " " + alignedsortedbam;
			p = Runtime.getRuntime().exec(cmd);
			try {
				p.waitFor();
				p.destroy();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			alignedsortedbam = alignedsortedbam + ".bam";
			cmd[2] = "samtools index " + alignedsortedbam;
			p = Runtime.getRuntime().exec(cmd);
			try {
				p.waitFor();
				p.destroy();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		//int time = (int) ((System.currentTimeMillis()-startTime));
		//System.out.println("Time to preprocess (ms): " + time);
	}
	
	public static void callAllele(Params3 params) throws IOException {
		
		int[] copyNumbers = params.c;
		String template1Name = params.sample;
		String tempDir = params.d;
		String outputDir = params.o;
		
		//long startTime = System.currentTimeMillis();

		for (int geneIndex = 0; geneIndex < Genes.getGeneTests().size(); ++geneIndex) {
			String geneName = Genes.getGeneTests().get(geneIndex).get(0);
			
			String alignedsortedbam = tempDir + geneIndex + "." + template1Name + ".sorted";
			alignedsortedbam = alignedsortedbam + ".bam";
			//String variants = outputDir + geneIndex + "." + template1Name + ".var.vcf";
			
			//4. Call allele
			AlleleScorer a = new AlleleScorer(geneIndex, template1Name, outputDir);
			a.loadSample(copyNumbers[geneIndex], alignedsortedbam, alignedsortedbam+".bai");

			HashMap<Integer, Allele> allGeneReference = AlleleTypingMix.allGeneReference;
			HashMap<Integer, HashMap<String, Allele>> allGeneAlleles = AlleleTypingMix.allGeneAlleles;
			allGeneReference.get(7).removeVariants(9181, 11384);	
			HashMap<String, Allele> tempAlleles = new HashMap<String, Allele>();
			for (Iterator it = allGeneAlleles.get(7).keySet().iterator(); it.hasNext(); ) {
				String alleleName = (String) it.next();
				allGeneAlleles.get(7).get(alleleName).removeVariants(9181, 9181);
			}
			a.loadAllele(allGeneReference.get(geneIndex), allGeneAlleles.get(geneIndex));


			//System.out.println(template1Name + "\t" + geneName);
		
			try{
				if (a.getSampleCopyNumber() > 2) {
					//throw new UnsupportedOperationException();
				} else if (a.getSampleCopyNumber() == 2) {
					a.calculateLikelihoodAllPairs();
				} else if (a.getSampleCopyNumber() == 1) {
					a.calculateLikelihoodHaploid();
				} else {
					//Sample copy number is 0 
				}
			} catch(RuntimeIOException e) {
				e.printStackTrace();
			}
		
		}
		//int time = (int) ((System.currentTimeMillis()-startTime));
		//System.out.println("Time to call alleles (ms): " + time);
	}

}
