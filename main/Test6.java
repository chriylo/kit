
package main;

import generator.*;
import allele.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.zip.GZIPInputStream;
import java.text.DecimalFormat;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.util.CombinatoricsUtils;

import net.sf.samtools.SAMFileReader;
import net.sf.samtools.SAMRecord;
import typing.TemplateScorer;
import typing.TemplateScores;
import typing.TypePredictor;
import typing.Typing;
import distanceFunction.EntryWeights;
import typing.CopyNumberScorer;
import allele.AlleleTyping;

public class Test6 {



	
	
	public static void main(String[] args) throws IOException {
		//getTrieWithSource();
		
//		testHaploid("Scaled");
//		testHaploid("5x");
//		testHaploid("15x");
//		testHaploid("30x");
//		testHaploid("50x");
//		testDiploid("Scaled");
		testDiploid("5x");
		testDiploid("15x");
		testDiploid("30x");
		testDiploid("50x");
		
		//filterReference();
		
		//printAlleles(3);
		//printUniqueTrie();
		
		/**
		//print gene reference source
		String geneDir = "Data/Genes/";
		for (int geneIndex = 0; geneIndex < Typing.getGeneTests().size(); ++geneIndex) {
			String geneName = Typing.getGeneTests().get(geneIndex).get(0);
	        String geneRef = geneDir + geneName + "/" + geneName + ".fasta";
	        InputStream gris = new FileInputStream(geneRef);
	        BufferedReader gr = new BufferedReader(new InputStreamReader(gris));
	        System.out.println(gr.readLine());
		}
		**/
		
	}

	/*
	 * 
	 */
//	public static void filterReference() throws IOException {
//		ArrayList<Integer> ks = new ArrayList<Integer>();
//		ks.add(50);
//		
//
//		for (int geneIndex = 0; geneIndex < Typing.getGeneTests().size(); ++geneIndex) {
//			String geneName = Typing.getGeneTests().get(geneIndex).get(0);
//
//				
//			//Create trie
//			Trie3 t = new Trie3();
//			for (int ki = 0;  ki < ks.size(); ++ki) {
//				int k = ks.get(ki);
//				String pathToTemplates = "Data/TemplatesMasked";
//				String pathToTemplateBarcodes = "Data/Barcodes/Templates_Ref_"+k;
//				TypePredictor predictor = new TypePredictor();
//
//				String pathToRefBarcode = "Data/Barcodes/Reference/reference"+k+".barcod";
//				Trie tempt = Preprocessor.getUniqueKmers(predictor, k, geneIndex, pathToRefBarcode);
//				String[] uniqueKmers = tempt.words();
//				for (int ti = 0; ti < uniqueKmers.length; ++ti) { t.addWord(uniqueKmers[ti]); }
//			}
//			t.finalize();
//				
//			for (int i = 1; i < 23; ++i) {
//				System.out.println(geneIndex + "\t" + i );
//
//				String reads1 = "Data/ReferenceReads/chr" + i + "_fir.fastq";
//				String reads2 = "Data/ReferenceReads/chr" + i + "_sec.fastq";
//
//				String outpath = "Data/AlleleCalling/FilteredReferenceReads/" + geneName + ".chr" + i + "_fir.fa";
//				String outpath2 = "Data/AlleleCalling/FilteredReferenceReads/" + geneName + ".chr" + i + "_sec.fa";
//				Preprocessor.filterPairedReads(reads1, reads2, t, ks, outpath, outpath2);
//			}
//		}
//	}
	
public static void getTrieWithSource() throws IOException {
		
		int k = 50;
		
		TypePredictor predictor = new TypePredictor();
			
		/** To create trie of unique k-mers with source **/			
		Trie4 t = new Trie4();
		for (int geneIndex = 0; geneIndex < Typing.getGeneTests().size(); ++geneIndex) {
			String geneName = Typing.getGeneTests().get(geneIndex).get(0);
				
			//Create trie

			String pathToRefBarcode = "Data/Barcodes/Reference/reference"+k+".barcod";
			Trie tempt = Preprocessor.getUniqueKmers(predictor, k, geneIndex, pathToRefBarcode);
			String[] uniqueKmers = tempt.words();
			for (int ti = 0; ti < uniqueKmers.length; ++ti) { t.addWord(uniqueKmers[ti], geneIndex); }
		}
		t.finalize();
		KMerFileReader.printTrieToFile("trieWithSource.txt", t);	

	}
	
	public static void testHaploid(String cov) throws IOException {
//		String cov = "5x";
		Params2 params = new Params2();
		params.setk(50);
		for (int template1 = 0; template1 < Typing.templates.size(); ++template1) {
		String template1Name = Typing.templates.get(template1);
		params.setSample(template1Name);
		String type1 = Typing.templateTypes.get(template1Name);
		int[] c = Typing.getTypeCopyNumbers().get(type1);
		params.setCopyNumber(c);
		params.setTrie("Data/Tries/trieSource50");
		
		params.setGenes("Data/Genes/");
		params.setOutputDir("Data/AlleleCalling"+cov+"/TempMix/");
		
		if (cov.contains("Scaled")) {
//			for (int geneIndex = 0; geneIndex < Typing.getGeneTests().size(); ++geneIndex) {
//			String geneName = Typing.getGeneTests().get(geneIndex).get(0);
//		
//			Process p; String[] cmd = new String[3]; cmd[0] = "/bin/sh"; cmd[1] = "-c"; String s;
//
//			cmd[2] = "bwa mem "+ params.g + geneName+"/"+geneName+".fasta data/TemplatesMasked/"+template1Name+".masked.fa > data/AlleleCalling"+cov+"/TempMix2/"+geneIndex+"."+template1Name+".sam";
//					p = Runtime.getRuntime().exec(cmd);
//			try {
//				p.waitFor();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			//TODO: take only first alignment of sam file 
//			
//			String alignedunpaired = params.o + geneIndex + "." + template1Name + ".sam";
//			String alignedbamunpaired = params.o + geneIndex + "." + template1Name + ".bam";
//			
//			cmd[2] = "samtools view -Sb " + alignedunpaired + " > " + alignedbamunpaired ;
//			p = Runtime.getRuntime().exec(cmd);
//			try {
//				p.waitFor();
//				p.destroy();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			String alignedsortedbam = params.o + geneIndex + "." + template1Name + ".sorted";
//			cmd[2] = "samtools sort " + alignedbamunpaired + " " + alignedsortedbam;
//			p = Runtime.getRuntime().exec(cmd);
//			try {
//				p.waitFor();
//				p.destroy();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//
//			alignedsortedbam = alignedsortedbam + ".bam";
//			cmd[2] = "samtools index " + alignedsortedbam;
//			p = Runtime.getRuntime().exec(cmd);
//			try {
//				p.waitFor();
//				p.destroy();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}			
		}
		else {
			params.setReadsDir("Data/AlleleCalling"+cov+"/Temp/"); 
			String reads1 = "Data/TemplateReads"+cov+"/"+template1Name + "/" + template1Name + "_fir.fastq";
			params.setRead1(reads1);
			String reads2 = "Data/TemplateReads"+cov+"/" +template1Name + "/"+ template1Name + "_sec.fastq";
			params.setRead2(reads2);
			
//			AlleleCaller.filterReads(params);			
//			//TODO: Filtered reference reads
			
			AlleleCaller.preprocess(params);
			

		}
		
		AlleleCaller.callAllele(params);
		
	}
	}
	

	

	/*
	 * Test all diploid templates
	 */
	public static void testDiploid(String cov) throws IOException {
		//String cov = "50x";
		Params2 params = new Params2();
		params.setk(50);
		for (int template1 = 0; template1 < Typing.templates.size(); ++template1) {
//		int template1 = 0;
			for (int template2 = template1; template2 < Typing.templates.size(); ++template2) {
//				int template2 = 22;
				String template1Name = Typing.templates.get(template1);
				String template2Name = Typing.templates.get(template2);
				String sample = template1Name + "." + template2Name;
				params.setSample(sample);
				String type1 = Typing.templateTypes.get(template1Name);
				String type2 = Typing.templateTypes.get(template2Name);
				String[] temp = new String[2];
				temp[0] = type1;
				temp[1] = type2;
				Arrays.sort(temp);
				int[] c = Typing.getDiploidTypeCopyNumbers().get(temp[0]+"_"+temp[1]);
				params.setCopyNumber(c);
				//params.setTrie("Data/Tries/trieSource50");
				
				params.setGenes("Data/Genes/");
				params.setOutputDir("Data/AlleleCalling"+cov+"/TempMix/");
				
				
				//Filter reads first
				//for (int geneIndex = 0; geneIndex < Typing.getGeneTests().size(); ++geneIndex) {
					int geneIndex = 7;
					if (cov.contains("Scaled")) {
						String geneName = Typing.getGeneTests().get(geneIndex).get(0);
				        String geneRef = params.g + geneName + "/" + geneName + ".fasta";
				        
				        Process p; String[] cmd = new String[3]; cmd[0] = "/bin/sh"; cmd[1] = "-c"; String s;

						String bam1 = params.o + geneIndex + "." + template1Name + ".bam";
						String bam2 = params.o + geneIndex + "." + template2Name + ".bam";
						String dipbam = params.o + geneIndex + "." + sample + ".bam";
						cmd[2] = "samtools merge -f " + dipbam + " " + bam1 + " " + bam2;
						p = Runtime.getRuntime().exec(cmd);
						try {
							p.waitFor();
							p.destroy();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
						String alignedsortedbam = params.o + geneIndex + "." + sample + ".sorted";
						cmd[2] = "samtools sort " + dipbam + " " + alignedsortedbam;
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
				        
					} else {
					
					params.setReadsDir("Data/AlleleCalling"+cov+"/Temp/");
	
					
					String outpath = params.d + geneIndex + "."  + template1Name + "_fir.fa";
					String outpath2 = params.d + geneIndex + "."  + template1Name + "_sec.fa";
					
					String outpath3 = params.d  + geneIndex + "." + template2Name + "_fir.fa";
					String outpath4 = params.d  + geneIndex + "." + template2Name + "_sec.fa";
					
					String newoutpath = params.d 	 + geneIndex + "." + sample + "_fir.fa";
					String newoutpath2 = params.d  + geneIndex + "." + sample + "_sec.fa";
					
					if (!(new File(outpath)).exists()) { PrintStream tps = new PrintStream(outpath); tps.close(); }
					if (!(new File(outpath2)).exists()) { PrintStream tps = new PrintStream(outpath2); tps.close(); }
					if (!(new File(outpath3)).exists()) { PrintStream tps = new PrintStream(outpath3); tps.close(); }
					if (!(new File(outpath4)).exists()) { PrintStream tps = new PrintStream(outpath4); tps.close(); }

					
					//combine files
					PrintStream ps, ps2;
					ps = new PrintStream(newoutpath);
					ps2 = new PrintStream(newoutpath2);
					int counter = 0;
					Iterator<PairedRecord> iterator = Preprocessor.fastaPairedSeqIterator(outpath,outpath2);
					while (iterator.hasNext()){
						PairedRecord pr = iterator.next();
						ps.println(">" + counter + "_" + pr.name1().substring(1));
						ps.println(pr.seq1());
						ps2.println(">" + counter + "_" + pr.name2().substring(1));
						ps2.println(pr.seq2());
						counter += 1;
					}
					Iterator<PairedRecord> iterator2 = Preprocessor.fastaPairedSeqIterator(outpath3,outpath4);
					while (iterator2.hasNext()){
						PairedRecord pr = iterator2.next();
						ps.println(">" + counter + "_" + pr.name1().substring(1));
						ps.println(pr.seq1());
						ps2.println(">" + counter + "_" + pr.name2().substring(1));
						ps2.println(pr.seq2());
						counter += 1;
					}
					ps.close();
					ps2.close();
					
					//TODO: Filtered reference reads
					
					AlleleCaller.preprocess(params);			

					}
				//}
				
				
				AlleleCaller.callAllele(params);
							
			}
		}
			
	}
		
	

	
	
	/*
	 * printAllele() is used to generate AlleleTyping.java
	 */
	public static void printAlleles(int code) throws IOException {
		
		if (code == 0) {
		System.out.println("package allele;");
		

		System.out.println("import java.util.HashMap;");

		System.out.println("public class AlleleTyping {");
		
		System.out.println("public static HashMap<Integer, HashMap<String, Allele>> allGeneAlleles;");
		System.out.println("public static HashMap<Integer, Allele> allGeneReference;");
		for (int geneIndex = 0; geneIndex < Typing.getGeneTests().size(); ++geneIndex) {
			System.out.println("public static HashMap<String, Allele> alleles" + geneIndex + ";");
		}
		
		System.out.println("static {");
		System.out.println("allGeneAlleles = new HashMap<Integer, HashMap<String, Allele>>();");
		System.out.println("allGeneReference = new HashMap<Integer, Allele>();");
		
		
		for (int geneIndex = 0; geneIndex < Typing.getGeneTests().size(); ++geneIndex) {
		//	int geneIndex = 14;
			
			System.out.println("alleles" + geneIndex + " = new HashMap<String, Allele>();"); 
			AlleleScorer a = new AlleleScorer(geneIndex);
			File dir = new File("Data/Genes/"+Typing.getGeneTests().get(geneIndex).get(0)+"/Variants/");
			File[] dirFiles = dir.listFiles();
			ArrayList<String> vcfFiles = new ArrayList<String>();
			ArrayList<String> bamFiles = new ArrayList<String>();
			ArrayList<String> bamIndexFiles = new ArrayList<String>();
			for (int f = 0; f < dirFiles.length; ++f) {
				if (dirFiles[f].getName().contains("vcf")) {
					vcfFiles.add(dirFiles[f].getAbsolutePath());
					bamFiles.add(new File("Data/Genes/"+Typing.getGeneTests().get(geneIndex).get(0)+"/Aligned/"+dirFiles[f].getName().split(".v")[0]+".sorted.bam").getAbsolutePath());
					bamIndexFiles.add(new File("Data/Genes/"+Typing.getGeneTests().get(geneIndex).get(0)+"/Aligned/"+dirFiles[f].getName().split(".v")[0]+".sorted.bam.bai").getAbsolutePath());

				}
			}
			a.loadAllele(vcfFiles, bamFiles, bamIndexFiles);
			
			a.removeBadAlleles();
			
			
			for (Iterator alleleIt = a.getAlleles().keySet().iterator(); alleleIt.hasNext(); ) {
				String name = (String) alleleIt.next();
				String[] justname = name.split(".v");
				System.out.println("alleles"+geneIndex+".put(\""+name+"\", add"+geneIndex+"_" + justname[0] + "());");
			}
			
			System.out.println("allGeneAlleles.put(" + geneIndex + ", alleles" + geneIndex + ");");
			System.out.println("allGeneReference.put(" + geneIndex + ", add" + geneIndex + "a());");
			
		}
		System.out.println("}"); 
		}
		
		for (int geneIndex = 0; geneIndex < Typing.getGeneTests().size(); ++geneIndex) {
				if (code == 1) {System.out.println("Gene " + geneIndex + " Polymorphic Sites"); }
				//int geneIndex = 14;
				AlleleScorer a = new AlleleScorer(geneIndex);
				File dir = new File("Data/Genes/"+Typing.getGeneTests().get(geneIndex).get(0)+"/Variants/");
				File[] dirFiles = dir.listFiles();
				ArrayList<String> vcfFiles = new ArrayList<String>();
				ArrayList<String> bamFiles = new ArrayList<String>();
				ArrayList<String> bamIndexFiles = new ArrayList<String>();
				for (int f = 0; f < dirFiles.length; ++f) {
					if (dirFiles[f].getName().contains("vcf")) {
						vcfFiles.add(dirFiles[f].getAbsolutePath());
						bamFiles.add(new File("Data/Genes/"+Typing.getGeneTests().get(geneIndex).get(0)+"/Aligned/"+dirFiles[f].getName().split(".v")[0]+".sorted.bam").getAbsolutePath());
						bamIndexFiles.add(new File("Data/Genes/"+Typing.getGeneTests().get(geneIndex).get(0)+"/Aligned/"+dirFiles[f].getName().split(".v")[0]+".sorted.bam.bai").getAbsolutePath());
	
					}
				}
				a.loadAllele(vcfFiles, bamFiles, bamIndexFiles);
				if (code == 2) {System.out.print(Typing.getGeneTests().get(geneIndex).get(0) + "&" + a.getAlleles().size() + "&");}
				a.removeBadAlleles();
				if (code == 2) {System.out.println(a.getAlleles().size()+"\\\\");}

				
				if (code == 1) { System.out.print("\treference"); }
				for (Iterator alleleIt = a.getAlleles().keySet().iterator(); alleleIt.hasNext(); ) {
					String name = (String) alleleIt.next();
					
					String[] justname = name.split(".v");
					if (code == 0) {
					System.out.println("public static Allele add"+geneIndex+"_" + justname[0] + "() {");
					System.out.println("HashMap<Integer, Variant> variants = new HashMap<Integer, Variant>();");

					System.out.println("variants = new HashMap<Integer, Variant>();");
					
					for (Iterator varIt = a.getAlleles().get(name).getVariants().keySet().iterator(); varIt.hasNext(); ) {
						Integer pos = (Integer) varIt.next();
						System.out.println("variants.put("+pos+", new Variant("+pos+", \"" + a.getAlleles().get(name).getVariants().get(pos).getAllele() + "\", -1, \""+ a.getAlleles().get(name).getVariants().get(pos).getGenotype() +"\"));");
					}
					System.out.println("alleles"+geneIndex+".put(\"" + name + "\", new Allele( \"" + name + "\", variants));");
					System.out.println("return new Allele( \"" + name + "\", variants);");
					
					System.out.println("}");
					}
					if (code == 1) {
						System.out.print("\t" + justname[0]);
					}
				
				}
				if (code == 1) { System.out.println(); }
				
				if (code == 0) {
				System.out.println("public static Allele add"+geneIndex+"a() {");
				System.out.println("HashMap<Integer, Variant> variants = new HashMap<Integer, Variant>();");
				}
				
				if (code == 3) {System.out.println(a.getReferenceAllele().getVariants().size()); }

				for (Iterator varIt = a.getReferenceAllele().getVariants().keySet().iterator(); varIt.hasNext(); ) {
					Integer pos = (Integer) varIt.next();
					if (code == 0) {System.out.println("variants.put("+pos+", new Variant("+pos+", \"" + a.getReferenceAllele().getVariants().get(pos).getAllele() + "\", -1, \"1/1\"));");}
					if (code == 1) {
					System.out.print(pos + "\t" + a.getReferenceAllele().getVariants().get(pos).getAllele() );
					for (Iterator alleleIt = a.getAlleles().keySet().iterator(); alleleIt.hasNext(); ) {
						String name = (String) alleleIt.next();
						try {
						System.out.print("\t" + a.getAlleles().get(name).getVariants().get(pos).getAllele());
						} catch (Exception NullPointerException) {
							System.out.print("\t-");
						}
					}
					System.out.println();
					}
				}
				if (code == 0) {
				System.out.println("allGeneReference.put("+geneIndex+", new Allele(\"reference\", variants));");
				System.out.println("return new Allele(\"reference\", variants);");
				
				System.out.println("}");
				System.out.println();
				}
		}
		if (code == 0) { System.out.println("}"); }
	}
	
//	public static void printUniqueTrie() throws IOException{
////		ArrayList<Integer> ks = new ArrayList<Integer>();
////		ks.add(50);
//		
//		for (int geneIndex = 0; geneIndex < Typing.getGeneTests().size(); ++geneIndex) {
//		//int geneIndex = 14;
//			String geneName = Typing.getGeneTests().get(geneIndex).get(0);
//		
//			//Create trie
//			Trie3 t = new Trie3();
//			//for (int ki = 0;  ki < ks.size(); ++ki) {
//				//int k = ks.get(ki);
//				int k = Typing.k;
//				String pathToTemplates = "Data/TemplatesMasked";
//				String pathToTemplateBarcodes = "Data/Barcodes/Templates_Ref_"+k;
//				TypePredictor predictor = new TypePredictor();
//
//				String pathToRefBarcode = "Data/Barcodes/Reference/reference"+k+".barcod";
//				Trie tempt = Preprocessor.getUniqueKmers(predictor, k, geneIndex, pathToRefBarcode);
//				String[] uniqueKmers = tempt.words();
//				for (int ti = 0; ti < uniqueKmers.length; ++ti) { t.addWord(uniqueKmers[ti]); }
//			//}
//			t.finalize();
//			//System.out.println(t.size());
//			KMerFileReader.printTrieWordsToFile("Data/Tries/trie50"+geneName, t);
//		}
//	}
	
	
	
}
