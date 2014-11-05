package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import net.sf.samtools.util.RuntimeIOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import generator.*;
import allele.*;

public class AlleleCaller {
	
	public static void main(String[] args) throws IOException {
		
		Params2 params = parse_cmd(args);

		
		
		long startTime = System.currentTimeMillis();
		//int time;
		
		//filterReads(params);
		//preprocess(params); 
		//callAllele(params);
		//callAlleleFromPileupFile(params);
		
		//getVCFGene(params);
		//getVCF(params);
		
		//printPileup(params);
				
		//time = (int) ((System.currentTimeMillis()-startTime));
		//System.out.println("Total time (ms): " + time);
					

}
	

	
//	private static ArrayList<String> getVCFSamplesFromOutFile(String pathToDir) throws IOException {
//		ArrayList<String> samples = new ArrayList<String>();
//		File dir = new File(pathToDir);
//		String[] fileNames = dir.list();
//		for (int f = 0; f < fileNames.length; ++f) {
//			if (fileNames[f].startsWith("0.") && fileNames[f].endsWith(".out")) {
//				String fileName = fileNames[f];
//				String[] sampleArray = fileName.split("\\.");
//				samples.add(sampleArray[1]);
//			}
//		}
//		return samples;
//	}
	
	
//	private static ArrayList<String> getVCFSamplesFromDebugFile(String pathToDir) throws IOException {
//		ArrayList<String> samples = new ArrayList<String>();
//		File dir = new File(pathToDir);
//		String[] fileNames = dir.list();
//		for (int f = 0; f < fileNames.length; ++f) {
//			if (fileNames[f].startsWith("0.") && fileNames[f].endsWith(".debug")) {
//				String fileName = fileNames[f];
//				String[] sampleArray = fileName.split("\\.");
//				samples.add(sampleArray[1]);
//			}
//		}
//		return samples;
////		HashMap<String, int[]> sampleCN = getSampleCN(pathToCN);
////		for (Iterator it = sampleCN.keySet().iterator(); it.hasNext(); ) {
////			String sample = (String) it.next();
////			samples.add(sample);
////			
////		}
////		return samples;
//		
//	}
	
	private static HashMap<String, int[]> getVCFSampleCNFromCNFile(String pathToCN) throws IOException {
		HashMap<String, int[]> sampleCN = new HashMap<String, int[]>();
	
		BufferedReader br = new BufferedReader(new FileReader(pathToCN));
		String line;
		while ((line = br.readLine()) != null) {
			String[] lineSplit = line.split("\t");
			String sample = lineSplit[0];
			String tempStringCN = lineSplit[1];
			int[] cn;
			if (tempStringCN.contains("_")) { 
				cn = Genes.getDiploidTypeCopyNumbers().get(tempStringCN);
			}
			else {
				String stringCN = tempStringCN;
				//String stringCN = (String) lineSplit[1].subSequence(1, lineSplit[1].length()-1);
				String[] stringCNArray; 
				stringCNArray = stringCN.split(",");
				cn = new int[stringCNArray.length];
				for (int i = 0; i < stringCNArray.length; ++i) {
					cn[i] = Integer.valueOf(stringCNArray[i]);
				}

			}
			
			sampleCN.put(sample, cn);
		}
		return sampleCN;
	}
		
	private static ArrayList<Integer> getVCFOffset(){
		ArrayList<Integer> offset = new ArrayList<Integer>();
		offset.add(59927494); //0
		offset.add(-59942000); //1 (not in b36)
		offset.add(-59945000); //2 (not in b36)
		offset.add(-59948000); //3 (not in b36)
		offset.add(-59951000); //4 (not in b36)
		offset.add(-59954000); //5 (not in b36)
		offset.add(59958020); //6 
		offset.add(59972847); //7
		offset.add(59989686); //8
		offset.add(60006652); //9
		offset.add(60022622); //10
		offset.add(60019501); //11
		offset.add(-60034501); //12 (not in b36)
		offset.add(60035765); //13
		offset.add(60053475); //14
		
//		offset.add(59927494); //0
//		offset.add(59941523); //1 (not in b36)
//		offset.add(59941523); //2 (not in b36)
//		offset.add(59941523); //3 (not in b36)
//		offset.add(59941523); //4 (not in b36)
//		offset.add(59941523); //5 (not in b36)
//		offset.add(59958020); //6 
//		offset.add(59972847); //7
//		offset.add(59989686); //8
//		offset.add(60006652); //9
//		offset.add(60022622); //10
//		offset.add(60019501); //11
//		offset.add(60034501); //12 (not in b36)
//		offset.add(60035765); //13
//		offset.add(60053475); //14
		
		return offset;
	}
	
	private static HashMap<Integer, Integer> getAlleleScores(int resolution, String pathToDir, int geneIndex, String refVariant, ArrayList<String> altVariants, String sample, int cn) throws NumberFormatException, IOException {

//		int numVariants = 0;
//		double totalNovelScore = 0;
//		BufferedReader br2 = new BufferedReader(new FileReader(pathToDir + geneIndex + "." + sample + ".debug"));
//		String vcfLine;
//		while (((vcfLine = br2.readLine()) != null) && (!vcfLine.startsWith("KIR"))) { //EOF or no more new pileups
//			String[] vcfLineSplit = vcfLine.split("\t");
//			if (vcfLineSplit[1].contains("NOVEL")) {
//				totalNovelScore += Double.valueOf(vcfLineSplit[2]);
//			} else {
//				numVariants += 1;
//			}
//		} 
		
		HashMap<String,Double> alleleNameScores = new HashMap<String, Double>();
		
		//HashMap<Integer, Double> alleleScores = new HashMap<Integer, Double>();
		BufferedReader br = new BufferedReader(new FileReader(pathToDir + geneIndex + "." + sample + ".out"));
		String line; // = br.readLine(); //discard first line
		while ((line = br.readLine()) != null) { 
			String[] lineSplit = line.split("\t");
			String[] allelePair = lineSplit[0].split("_");
			String name = getAlleleName(allelePair[0] + "_" + allelePair[1],resolution);
		
			if (allelePair.length == 4) {
				name = name + "," + getAlleleName(allelePair[2] + "_" + allelePair[3],resolution);
			}
			//Double score = (Double.valueOf(lineSplit[1])-totalNovelScore)/numVariants;
			Double score = Double.valueOf(lineSplit[1]);
			//alleleScores.put(indx, score);
			if (alleleNameScores.containsKey(name)) { if (score < alleleNameScores.get(name)) { alleleNameScores.put(name, score); }}
			else { alleleNameScores.put(name, score); }
		}
		br.close();
		
		HashMap<Integer, Double> alleleScores = new HashMap<Integer, Double>();
		int counter = 0; 
		for (int i = 0; i < (altVariants.size()+1); ++i ) {
			for (int j = 0; j < i+1; ++j) {
				String name1 = refVariant;
				if (i != 0) { name1 = altVariants.get(i-1); } 
				String name2 = refVariant;
				if (j != 0) { name2 = altVariants.get(j-1); } 
				
				String name = name1+","+name2;
				String altname = name2+","+name1;

				if (i != altVariants.size() || j != altVariants.size()) {
					if (i == altVariants.size()) {
						name = name2;
					} else if (j == altVariants.size()) {
						name = name1;
					} 
					
					if (alleleNameScores.containsKey(name)) {
						alleleScores.put(counter, alleleNameScores.get(name));
					} else if (alleleNameScores.containsKey(altname)) {
						alleleScores.put(counter,  alleleNameScores.get(altname));
					}
//					else {
//						System.out.println("Not in alleleNameScores: " + name);
//						alleleScores.put(counter, AlleleScorer.getMin());
//					}
				} 
				
				
				
				counter += 1; 
			}
		}
		
		
		//TODO: preprocess alleleScores
		HashMap<Integer, Integer> normalizedAlleleScores = new HashMap<Integer, Integer>();
		//find smallest score
		//Double smallestScore = Collections.min(alleleScores.values());
		for (Iterator it = (Iterator) alleleScores.keySet().iterator(); it.hasNext(); ) {
			Integer k = (Integer) it.next();
			int score = (int) Math.round(alleleScores.get(k)*100);
			normalizedAlleleScores.put(k, Math.min(255, score));		}
		return normalizedAlleleScores;

	}
	
	private static String getAlleleName(String idfile, int resolution) {
		String[] idSplit = idfile.split(".v");
		String id = idSplit[0];
		if (AlleleTypingMix.allele_id_to_name.containsKey(id)) { 
			String name = AlleleTypingMix.allele_id_to_name.get(id);
			int temp = name.indexOf("*");
			name = (String) name.subSequence(0, Math.min(temp+resolution+1, name.length()));
			return name; 
		}
		else { return id; }
	}
	
	public static void getVCFGene(Params2 params) throws IOException {
		
		//Input: .out file for each sample and for each gene
		String pathToDir = params.o;
		String pathToCN = params.cn;
		int resolution = 3;
		
		HashMap<String, int[]> samplesCN = getVCFSampleCNFromCNFile(pathToCN);
		ArrayList<String> samples = new ArrayList<String>();
		for (Iterator scnit = samplesCN.keySet().iterator(); scnit.hasNext(); ) {
			samples.add((String) scnit.next());
		}
		
		ArrayList<Integer> offset = getVCFOffset();
		
		HashMap<Integer, HashMap<String, Allele>> allGeneAlleles = AlleleTypingMix.allGeneAlleles;
		
		
		//Print header
		System.out.println("##fileformat=VCFv4.1");
		System.out.println("##reference=chr19.b36.fa");
		System.out.println("##contig=<ID=chr19,length=63811651,assembly=B36,species=\"Homo sapiens\">");
		System.out.println("##FORMAT=<ID=GT,Number=1,Type=String,Description=\"Genotype\">");
		System.out.println("##FORMAT=<ID=PL,Number=G,Type=Integer,Description=\"List of Phred-scaled genotype likelihoods\">");
		System.out.print("#CHROM\tPOS\tID\tREF\tALT\tQUAL\tFILTER\tINFO\tFORMAT");
		for (Iterator it2 = samplesCN.keySet().iterator(); it2.hasNext(); ) {
			String sample = (String) it2.next();				
			File f = new File(pathToDir +  "0." + sample + ".debug");
			if (!f.exists()) { continue; }
			System.out.print("\t"+sample);
		} System.out.println();
	
		//For each gene
		HashMap<Integer, VCFVariantGeneral> geneVariantsGeneral = new HashMap<Integer, VCFVariantGeneral>();

		for (int geneIndex = 0; geneIndex < Genes.getGeneTests().size(); ++geneIndex) {
			Integer pos = Math.abs(offset.get(geneIndex));
			ArrayList<String> altVariants = new ArrayList<String>();
			HashMap<String, Allele> geneAlleles = allGeneAlleles.get(geneIndex);
			for (Iterator it = geneAlleles.keySet().iterator(); it.hasNext(); ) {
				String alleleName = getAlleleName((String) it.next(), resolution);
				if (!altVariants.contains(alleleName)) { altVariants.add(alleleName); }
			}
			altVariants.add("D");
			String refVariant = altVariants.get(0);
			altVariants.remove(0);
			geneVariantsGeneral.put(pos, new VCFVariantGeneral("ref", pos, 0, refVariant, altVariants)); 
			
			//For all samples
			for (Iterator it2 = samplesCN.keySet().iterator(); it2.hasNext(); ) {
				String sample = (String) it2.next();				
				File f = new File(pathToDir +  "0." + sample + ".debug");
				if (!f.exists()) { continue; }
				
				int[] sampleCN = samplesCN.get(sample);
				
				//Read corresponding input file (Hashmap for allele pair and score)
				
				HashMap<Integer, Integer> alleleScores = getAlleleScores(resolution, pathToDir, geneIndex, refVariant,  altVariants, sample, sampleCN[geneIndex]);
				ProbabilityLikelihoodGeneral pl = new ProbabilityLikelihoodGeneral(geneVariantsGeneral.get(pos).getRef(), geneVariantsGeneral.get(pos).getAlt(), alleleScores, sampleCN[geneIndex]);
				pl.normalize();
				
				geneVariantsGeneral.get(pos).addSample(sample, pl);

			}
		}
		
		
		
		
		
		ArrayList<Map.Entry<Integer, VCFVariantGeneral>> scoresList = new ArrayList<Map.Entry<Integer, VCFVariantGeneral>>();
		for (Iterator it = geneVariantsGeneral.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry<Integer, VCFVariantGeneral> entry = (Map.Entry<Integer, VCFVariantGeneral>) it.next();
			scoresList.add(entry);
		}
		
		Collections.sort(scoresList, new Comparator<Map.Entry<Integer, VCFVariantGeneral>>() { public int compare(Map.Entry<Integer, VCFVariantGeneral> e, Map.Entry<Integer, VCFVariantGeneral> e2 ) { if (e.getKey() > e2.getKey()) {return 1;} else if (e2.getKey() > e.getKey()) {return -1; } else {return 0;} } });
		for (Iterator it2 = scoresList.iterator(); it2.hasNext(); ) {
			Map.Entry<Integer, VCFVariantGeneral> score = (Map.Entry<Integer, VCFVariantGeneral>) it2.next();
			Integer varPos = score.getKey();
			VCFVariantGeneral sampleVariant = score.getValue();
			HashMap<String, ProbabilityLikelihoodGeneral> samplesPl = sampleVariant.getPLSamples();
						
			
			if (sampleVariant.getOffset() < 0) { 
				System.out.print("chr19\t"+String.valueOf(-sampleVariant.getOffset()+varPos)+"\t"+String.valueOf(-sampleVariant.getOffset()+varPos)+"\t"+sampleVariant.getRef()+"\t"+sampleVariant.getAltStr()+"\t.\t.\t.\tGT:PL");
//				String alt = "";
//				for (int i = 1; i < sampleVariant.getAlt().size(); ++i ) { alt = alt  + Integer.toString(i) + ","; } alt = alt + sampleVariant.getAlt().size(); 
//				System.out.print("chr19\t"+String.valueOf(-sampleVariant.getOffset()+varPos)+"\t"+String.valueOf(-sampleVariant.getOffset()+varPos)+"\t0\t"+alt+"\t.\t.\t.\tGT:PL");
			} 
			else {
				System.out.print("chr19\t"+String.valueOf(sampleVariant.getOffset()+varPos)+"\t"+String.valueOf(sampleVariant.getOffset()+varPos)+"\t"+sampleVariant.getRef()+"\t"+sampleVariant.getAltStr()+"\t.\t.\t.\tGT:PL");

//				String alt = "";
//				for (int i = 1; i < sampleVariant.getAlt().size(); ++i ) { alt = alt  + Integer.toString(i) + ","; } alt = alt + sampleVariant.getAlt().size(); 
//				System.out.print("chr19\t"+String.valueOf(sampleVariant.getOffset()+varPos)+"\t"+String.valueOf(sampleVariant.getOffset()+varPos)+"\t0\t"+alt+"\t.\t.\t.\tGT:PL");
			}
			
			for (Iterator it3 = samplesCN.keySet().iterator(); it3.hasNext(); ) {
				String sample = (String) it3.next();				
				File f = new File(pathToDir +  "0." + sample + ".debug");
				if (!f.exists()) { continue; }

				ProbabilityLikelihoodGeneral pl = samplesPl.get(sample);
				System.out.print("\t" +  pl.getGenotype() + ":" + pl.toString());

			}
			System.out.println();
	
		
		}
		
	}
	
	
	public static void getVCF(Params2 params) throws IOException {
//		String pathToDir = params.o;
//		String pathToCN = params.cn;
		
		//get samples cn 
//		HashMap<String, int[]> samplesCN = getVCFSampleCNFromCNFile(pathToCN);
		
		ArrayList<String> samples = new ArrayList<String>();
//		for (Iterator scnit = samplesCN.keySet().iterator(); scnit.hasNext(); ) {
//			samples.add((String) scnit.next());
//		}
		
		//get offset
		ArrayList<Integer> offset = getVCFOffset();
		
	
		HashMap<Integer, Allele> allGeneReference = AlleleTypingMix.allGeneReference;
		HashMap<Integer, HashMap<String, Allele>> allGeneAlleles = AlleleTypingMix.allGeneAlleles;
		
		System.out.println("##fileformat=VCFv4.1");
		System.out.println("##reference=chr19.b36.fa");
		System.out.println("##contig=<ID=chr19,length=63811651,assembly=B36,species=\"Homo sapiens\">");
		System.out.println("##FORMAT=<ID=GT,Number=1,Type=String,Description=\"Genotype\">");
		System.out.println("##FORMAT=<ID=PL,Number=G,Type=Integer,Description=\"List of Phred-scaled genotype likelihoods\">");
		System.out.print("#CHROM\tPOS\tID\tREF\tALT\tQUAL\tFILTER\tINFO\tFORMAT");
//		for (Iterator it2 = samplesCN.keySet().iterator(); it2.hasNext(); ) {
//			String sample = (String) it2.next();				
//			File f = new File(pathToDir +  "0." + sample + ".debug");
//			if (!f.exists()) { continue; }
//			System.out.print("\t"+sample);
//		} System.out.println();
//		
		//for each gene
		for (int geneIndex = 0; geneIndex < Genes.getGeneTests().size(); ++geneIndex) {
//			System.out.println("*******\t" + geneIndex + "\t***********");
//			AlleleScorer a = new AlleleScorer(geneIndex, "sample", pathToDir);
			AlleleScorer a = new AlleleScorer(geneIndex); //remove
			a.loadAllele(allGeneReference.get(geneIndex), allGeneAlleles.get(geneIndex));

			HashMap<Integer, Variant> geneReferenceVariants = a.getReferenceAllele().getVariants();
			HashMap<Integer, Variant> geneAltVariants = a.getAlternateAllele().getVariants();
			
			HashMap<Integer, VCFVariantGeneral> geneVariantsGeneral = new HashMap<Integer, VCFVariantGeneral>();
			for (Iterator it = geneReferenceVariants.keySet().iterator(); it.hasNext(); ) {
				Integer pos = (Integer) it.next();
				ArrayList<String> altVariants = new ArrayList<String>();
				altVariants.add(geneAltVariants.get(pos).getAllele().toUpperCase());
				altVariants.add("D");
				geneVariantsGeneral.put(pos, new VCFVariantGeneral("ref", pos, offset.get(geneIndex), geneReferenceVariants.get(pos).getAllele().toUpperCase(), altVariants)); 
			}
			
			
			//for each sample
//			for (Iterator it2 = samplesCN.keySet().iterator(); it2.hasNext(); ) {
//				String sample = (String) it2.next();				
//				File f = new File(pathToDir +  "0." + sample + ".debug");
//				if (!f.exists()) { continue; }
//				
//				
//				int[] sampleCN = samplesCN.get(sample);
//				//System.out.println(sample);
//				
//				
//				//if cn == 0
//				if (sampleCN[geneIndex]==0 || sampleCN[geneIndex]>2) {
//					for (Iterator it = geneReferenceVariants.keySet().iterator(); it.hasNext(); ) {
//						Integer pos = (Integer) it.next();
//						
//						ProbabilityLikelihoodGeneral pl;
//						if (sampleCN[geneIndex]==0) {
//							pl = new ProbabilityLikelihoodGeneral(geneVariantsGeneral.get(pos).getRef(), geneVariantsGeneral.get(pos).getAlt(), new ArrayList<String>(), 0);
//						} else {
//							pl = new ProbabilityLikelihoodGeneral(geneVariantsGeneral.get(pos).getRef(), geneVariantsGeneral.get(pos).getAlt(), new ArrayList<String>(), 3);
//						}
//						pl.normalize();
//						
//						geneVariantsGeneral.get(pos).addSample(sample, pl);
//					}
//				} 
//				
//				else { 
//					BufferedReader br = new BufferedReader(new FileReader(pathToDir + geneIndex + "." + sample + ".debug"));
//					String vcfLine;
//					while (((vcfLine = br.readLine()) != null) && (!vcfLine.startsWith("KIR"))) { //EOF or no more new pileups
//						String[] vcfLineSplit = vcfLine.split("\t");
//						Integer vcfVarPosition = Integer.valueOf(vcfLineSplit[0]);
//						if (!geneVariantsGeneral.containsKey(vcfVarPosition)) { break; } //not novel
//	
//						String pileup = vcfLineSplit[3];
//						pileup = pileup.substring(1,pileup.length()-1);
//						String[] pileupArray = pileup.split(", ");
//						if (pileupArray.length == 1) { if (pileupArray[0].equals("")) {pileupArray = new String[0]; } }
//						ArrayList<String> pc = new ArrayList<String>();
//						for (int p = 0; p < pileupArray.length; ++p) { pc.add(pileupArray[p]); }
//						
//						ProbabilityLikelihoodGeneral pl;
//						String refTemp = geneVariantsGeneral.get(vcfVarPosition).getRef();
//						ArrayList<String> altTemp = geneVariantsGeneral.get(vcfVarPosition).getAlt();
//						if (sampleCN[geneIndex]==1) {
//						pl = new ProbabilityLikelihoodGeneral(refTemp, altTemp, pc, 1 ); //TODO: get pl
//						} else{
//						pl = new ProbabilityLikelihoodGeneral(refTemp, altTemp, pc, 2 ); //TODO: get pl
//						}
//						pl.normalize();
//						
//						geneVariantsGeneral.get(vcfVarPosition).addSample(sample, pl);
//					}
//					br.close();
//				}
//			}
			
			
			
			
			ArrayList<Map.Entry<Integer, VCFVariantGeneral>> scoresList = new ArrayList<Map.Entry<Integer, VCFVariantGeneral>>();
			for (Iterator it = geneVariantsGeneral.entrySet().iterator(); it.hasNext(); ) {
				Map.Entry<Integer, VCFVariantGeneral> entry = (Map.Entry<Integer, VCFVariantGeneral>) it.next();
				scoresList.add(entry);
			}
			
			int counter = 0;
			Collections.sort(scoresList, new Comparator<Map.Entry<Integer, VCFVariantGeneral>>() { public int compare(Map.Entry<Integer, VCFVariantGeneral> e, Map.Entry<Integer, VCFVariantGeneral> e2 ) { if (e.getKey() > e2.getKey()) {return 1;} else if (e2.getKey() > e.getKey()) {return -1; } else {return 0;} } });
			for (Iterator it2 = scoresList.iterator(); it2.hasNext(); ) {
				Map.Entry<Integer, VCFVariantGeneral> score = (Map.Entry<Integer, VCFVariantGeneral>) it2.next();
				Integer varPos = score.getKey();
				VCFVariantGeneral sampleVariant = score.getValue();
				HashMap<String, ProbabilityLikelihoodGeneral> samplesPl = sampleVariant.getPLSamples();
				
				if (sampleVariant.getAlt().contains(("N"))) { continue;}
				
				else {
				if (sampleVariant.getOffset() < 0) { 
					System.out.println(varPos + "\t" + -sampleVariant.getOffset() + "\t" +  (-sampleVariant.getOffset()+counter));
					//System.out.print("chr19\t"+String.valueOf(-sampleVariant.getOffset()+counter)+"\t"+String.valueOf(-sampleVariant.getOffset()+counter)+"\t"+sampleVariant.getRef()+"\t"+sampleVariant.getAltStr()+"\t.\t.\t.\tGT:PL");
					counter += 1;
				} 
				else {
					System.out.println(varPos + "\t" + sampleVariant.getOffset() + "\t" +  (sampleVariant.getOffset()+varPos));

					//System.out.print("chr19\t"+String.valueOf(sampleVariant.getOffset()+varPos)+"\t"+String.valueOf(sampleVariant.getOffset()+varPos)+"\t"+sampleVariant.getRef()+"\t"+sampleVariant.getAltStr()+"\t.\t.\t.\tGT:PL");
				}
				
//				for (Iterator it3 = samplesCN.keySet().iterator(); it3.hasNext(); ) {
//					String sample = (String) it3.next();				
//					File f = new File(pathToDir +  "0." + sample + ".debug");
//					if (!f.exists()) { continue; }
//					
//					ProbabilityLikelihoodGeneral pl = samplesPl.get(sample);
//					if (pl == null) {System.out.println(); System.out.println(sample); }
//					System.out.print("\t" +  pl.getGenotype() + ":" + pl.toString());
//
//				}
//				System.out.println();
				}

 			}

		
		}
		
	}
	
	public static void getVCFType(Params2 params) throws IOException {
//		//Input: .out file for each sample and for each gene
//		String pathToDir = params.o;
//		String pathToCN = params.cn;
//				
//		HashMap<String, int[]> samplesCN = getVCFSampleCNFromCNFile(pathToCN);
//		ArrayList<String> samples = new ArrayList<String>();
//		for (Iterator scnit = samplesCN.keySet().iterator(); scnit.hasNext(); ) {
//			samples.add((String) scnit.next());
//		}
//				
//		ArrayList<Integer> offset = getVCFOffset();
//				
//								
//		//Print header
//		System.out.println("##fileformat=VCFv4.1");
//		System.out.println("##reference=chr19.b36.fa");
//		System.out.println("##contig=<ID=chr19,length=63811651,assembly=B36,species=\"Homo sapiens\">");
//		System.out.println("##FORMAT=<ID=GT,Number=1,Type=String,Description=\"Genotype\">");
//		System.out.println("##FORMAT=<ID=PL,Number=G,Type=Integer,Description=\"List of Phred-scaled genotype likelihoods\">");
//		System.out.print("#CHROM\tPOS\tID\tREF\tALT\tQUAL\tFILTER\tINFO\tFORMAT");
//
//			
//		//For each gene
//		HashMap<Integer, VCFVariantGeneral> geneVariantsGeneral = new HashMap<Integer, VCFVariantGeneral>();
//
//		for (int geneIndex = 0; geneIndex < Genes.getGeneTests().size(); ++geneIndex) {
//			Integer pos = Math.abs(offset.get(geneIndex));
//			ArrayList<String> altVariants = new ArrayList<String>();
//			HashMap<String, Allele> geneAlleles = allGeneAlleles.get(geneIndex);
//			for (Iterator it = geneAlleles.keySet().iterator(); it.hasNext(); ) {
//				String alleleName = getAlleleName((String) it.next(), resolution);
//				if (!altVariants.contains(alleleName)) { altVariants.add(alleleName); }
//			}
//			altVariants.add("D");
//			String refVariant = altVariants.get(0);
//			altVariants.remove(0);
//			geneVariantsGeneral.put(pos, new VCFVariantGeneral("ref", pos, 0, refVariant, altVariants)); 
//					
//			//For all samples
//			for (Iterator it2 = samplesCN.keySet().iterator(); it2.hasNext(); ) {
//						String sample = (String) it2.next();				
//						File f = new File(pathToDir +  "0." + sample + ".debug");
//						if (!f.exists()) { continue; }
//						
//						int[] sampleCN = samplesCN.get(sample);
//						
//						//Read corresponding input file (Hashmap for allele pair and score)
//						
//						HashMap<Integer, Integer> alleleScores = getAlleleScores(resolution, pathToDir, geneIndex, refVariant,  altVariants, sample, sampleCN[geneIndex]);
//						ProbabilityLikelihoodGeneral pl = new ProbabilityLikelihoodGeneral(geneVariantsGeneral.get(pos).getRef(), geneVariantsGeneral.get(pos).getAlt(), alleleScores, sampleCN[geneIndex]);
//						pl.normalize();
//						
//						geneVariantsGeneral.get(pos).addSample(sample, pl);
//
//					}
//				}
//				
//				
//				
//				
//				
//				ArrayList<Map.Entry<Integer, VCFVariantGeneral>> scoresList = new ArrayList<Map.Entry<Integer, VCFVariantGeneral>>();
//				for (Iterator it = geneVariantsGeneral.entrySet().iterator(); it.hasNext(); ) {
//					Map.Entry<Integer, VCFVariantGeneral> entry = (Map.Entry<Integer, VCFVariantGeneral>) it.next();
//					scoresList.add(entry);
//				}
//				
//				Collections.sort(scoresList, new Comparator<Map.Entry<Integer, VCFVariantGeneral>>() { public int compare(Map.Entry<Integer, VCFVariantGeneral> e, Map.Entry<Integer, VCFVariantGeneral> e2 ) { if (e.getKey() > e2.getKey()) {return 1;} else if (e2.getKey() > e.getKey()) {return -1; } else {return 0;} } });
//				for (Iterator it2 = scoresList.iterator(); it2.hasNext(); ) {
//					Map.Entry<Integer, VCFVariantGeneral> score = (Map.Entry<Integer, VCFVariantGeneral>) it2.next();
//					Integer varPos = score.getKey();
//					VCFVariantGeneral sampleVariant = score.getValue();
//					HashMap<String, ProbabilityLikelihoodGeneral> samplesPl = sampleVariant.getPLSamples();
//								
//					
//					if (sampleVariant.getOffset() < 0) { 
//						System.out.print("chr19\t"+String.valueOf(-sampleVariant.getOffset()+varPos)+"\t"+String.valueOf(-sampleVariant.getOffset()+varPos)+"\t"+sampleVariant.getRef()+"\t"+sampleVariant.getAltStr()+"\t.\t.\t.\tGT:PL");
////						String alt = "";
////						for (int i = 1; i < sampleVariant.getAlt().size(); ++i ) { alt = alt  + Integer.toString(i) + ","; } alt = alt + sampleVariant.getAlt().size(); 
////						System.out.print("chr19\t"+String.valueOf(-sampleVariant.getOffset()+varPos)+"\t"+String.valueOf(-sampleVariant.getOffset()+varPos)+"\t0\t"+alt+"\t.\t.\t.\tGT:PL");
//					} 
//					else {
//						System.out.print("chr19\t"+String.valueOf(sampleVariant.getOffset()+varPos)+"\t"+String.valueOf(sampleVariant.getOffset()+varPos)+"\t"+sampleVariant.getRef()+"\t"+sampleVariant.getAltStr()+"\t.\t.\t.\tGT:PL");
//
////						String alt = "";
////						for (int i = 1; i < sampleVariant.getAlt().size(); ++i ) { alt = alt  + Integer.toString(i) + ","; } alt = alt + sampleVariant.getAlt().size(); 
////						System.out.print("chr19\t"+String.valueOf(sampleVariant.getOffset()+varPos)+"\t"+String.valueOf(sampleVariant.getOffset()+varPos)+"\t0\t"+alt+"\t.\t.\t.\tGT:PL");
//					}
//					
//					for (Iterator it3 = samplesCN.keySet().iterator(); it3.hasNext(); ) {
//						String sample = (String) it3.next();				
//						File f = new File(pathToDir +  "0." + sample + ".debug");
//						if (!f.exists()) { continue; }
//
//						ProbabilityLikelihoodGeneral pl = samplesPl.get(sample);
//						System.out.print("\t" +  pl.getGenotype() + ":" + pl.toString());
//
//					}
//					System.out.println();
//			
//				
//				}
				
	}
	

public static void preprocess(Params2 params) throws IOException {
	

	String template1Name = params.sample;
	String geneDir = params.g;
	String outputDir = params.o;
	String filteredReadsDir = params.d;
	
	long startTime = System.currentTimeMillis();

	
	for (int geneIndex = 0; geneIndex < Genes.getGeneTests().size(); ++geneIndex) {
	String geneName = Genes.getGeneTests().get(geneIndex).get(0);
		

	
		String outpath = filteredReadsDir + geneIndex + "." + template1Name + "_fir.fa";
		String outpath2 = filteredReadsDir + geneIndex + "." + template1Name + "_sec.fa";
		String outpath3 = filteredReadsDir + geneIndex + "." + template1Name + ".fa";

		//2. Map filtered reads to gene reference
        String geneRef = geneDir + geneName + "/" + geneName + ".fasta";
        String aligned = outputDir + geneIndex + "." + template1Name + ".sam";
		String alignedunpaired = outputDir + geneIndex + "." + template1Name + ".unpaired.sam";
		String alignedbam = outputDir + geneIndex + "." + template1Name + ".bam";
		String alignedbamunpaired = outputDir + geneIndex + "." + template1Name + ".unpaired.bam";
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
		//BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		//while ((s = stdError.readLine()) != null) { System.out.println(s); }	
		 			
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
			finalalignedbam = outputDir + geneIndex + "." + template1Name + ".paired.unpaired.bam";
			cmd[2] = "samtools merge -f " + finalalignedbam + " " + alignedbam + " " + alignedbamunpaired;
			p = Runtime.getRuntime().exec(cmd);
			try {
				p.waitFor();
				p.destroy();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		String alignedsortedbam = outputDir + geneIndex + "." + template1Name + ".sorted";
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

//		String binaryvariants = outputDir + geneIndex + "." + template1Name + ".var.raw.bcf";
//		cmd[2] = "samtools mpileup -ugf " + geneRef + " " + alignedsortedbam + " | bcftools view -bvcg - > " + binaryvariants;
//		p = Runtime.getRuntime().exec(cmd);
//		try {
//			p.waitFor();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		String variants = outputDir + geneIndex + "." + template1Name + ".var.vcf";
//		cmd[2] = "bcftools view " + binaryvariants + " > " + variants;
//		p = Runtime.getRuntime().exec(cmd);
//		try {
//			p.waitFor();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	int time = (int) ((System.currentTimeMillis()-startTime));
	System.out.println("Time to preprocess (ms): " + time);
}

public static void callAllele(Params2 params) throws IOException {
	
	int[] copyNumbers = params.c;
	String template1Name = params.sample;
	String outputDir = params.o;
	long startTime = System.currentTimeMillis();

	//for (int geneIndex = 0; geneIndex < Genes.getGeneTests().size(); ++geneIndex) {
	int geneIndex = 14;
		String geneName = Genes.getGeneTests().get(geneIndex).get(0);
		
		String alignedsortedbam = outputDir + geneIndex + "." + template1Name + ".sorted";
		alignedsortedbam = alignedsortedbam + ".bam";
		//String variants = outputDir + geneIndex + "." + template1Name + ".var.vcf";
		
		//4. Call allele
		AlleleScorer a = new AlleleScorer(geneIndex, template1Name, outputDir);
		a.loadSample(copyNumbers[geneIndex], alignedsortedbam, alignedsortedbam+".bai");

		HashMap<Integer, Allele> allGeneReference = AlleleTypingMix.allGeneReference;
		HashMap<Integer, HashMap<String, Allele>> allGeneAlleles = AlleleTypingMix.allGeneAlleles;
		allGeneReference.get(7).removeVariants(9181, 11384);
		//allGeneReference.get(14).removeVariants(2777, 16347);
		HashMap<String, Allele> tempAlleles = new HashMap<String, Allele>();
		for (Iterator it = allGeneAlleles.get(7).keySet().iterator(); it.hasNext(); ) {
			String alleleName = (String) it.next();
			allGeneAlleles.get(7).get(alleleName).removeVariants(9181, 9181);
		}
		a.loadAllele(allGeneReference.get(geneIndex), allGeneAlleles.get(geneIndex));


		System.out.println(template1Name + "\t" + geneName);
	
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
	
	//}
	int time = (int) ((System.currentTimeMillis()-startTime));
	System.out.println("Time to call alleles (ms): " + time);
}

public static void callAlleleFromPileupFile(Params2 params) throws IOException {
	
	int[] copyNumbers = params.c;
	String template1Name = params.sample;
	String outputDir = params.o;
	String pileupDir = params.d;
	long startTime = System.currentTimeMillis();

	for (int geneIndex = 0; geneIndex < Genes.getGeneTests().size(); ++geneIndex) {
	//int geneIndex = 5;
		String geneName = Genes.getGeneTests().get(geneIndex).get(0);
		
		//4. Call allele
		AlleleScorer a = new AlleleScorer(geneIndex, template1Name, outputDir);
		a.loadSample(copyNumbers[geneIndex]);

		HashMap<Integer, Allele> allGeneReference = AlleleTypingMix.allGeneReference;
		HashMap<Integer, HashMap<String, Allele>> allGeneAlleles = AlleleTypingMix.allGeneAlleles;
		a.loadAllele(allGeneReference.get(geneIndex), allGeneAlleles.get(geneIndex));
		
		System.out.println(template1Name + "\t" + geneName);
	
		try{
			if (a.getSampleCopyNumber() > 2) {
				//throw new UnsupportedOperationException();
			} else if (a.getSampleCopyNumber() == 2) {
				a.calculateLikelihoodAllPairs(params.d + geneIndex + "." + template1Name + ".debug");
			} else if (a.getSampleCopyNumber() == 1) {
				a.calculateLikelihoodHaploid(params.d + geneIndex + "." + template1Name + ".debug");
			} else {
				//Sample copy number is 0 
			}
		} catch(RuntimeIOException e) {
			e.printStackTrace();
		}
	
	}
	int time = (int) ((System.currentTimeMillis()-startTime));
	System.out.println("Time to call alleles (ms): " + time);
}

public static void printPileup(Params2 params) throws IOException {
	
	int[] copyNumbers = params.c;
	String template1Name = params.sample;
	String outputDir = params.o;
	String readDir = params.d;

	for (int geneIndex = 0; geneIndex < Genes.getGeneTests().size(); ++geneIndex) {
		String geneName = Genes.getGeneTests().get(geneIndex).get(0);
		
		String alignedsortedbam = readDir + geneIndex + "." + template1Name + ".sorted";
		alignedsortedbam = alignedsortedbam + ".bam";

		AlleleScorer a = new AlleleScorer(geneIndex, template1Name, outputDir);
		a.loadSample(copyNumbers[geneIndex], alignedsortedbam, alignedsortedbam+".bai");

		HashMap<Integer, Allele> allGeneReference = AlleleTypingMix.allGeneReference;
		HashMap<Integer, HashMap<String, Allele>> allGeneAlleles = AlleleTypingMix.allGeneAlleles;
		a.loadAllele(allGeneReference.get(geneIndex), allGeneAlleles.get(geneIndex));
		
		//a.printPileup();

	}

}
	

	
	public static Params2 parse_cmd(String[] args) {
		Params2 p = new Params2();

		Options options = new Options();
		
		options.addOption("sample", true, "sample name");
		
		options.addOption("c", true, "copy numbers separated by ,");
		options.addOption("b", true, "bam file");
		options.addOption("i", true, "bai file");
		options.addOption("r1", true, "fasta file");
		options.addOption("r2", true, "fasta file");
		options.addOption("r", true, "fasta file");
		//options.addOption("t", true, "directory/ containing the trie for each gene");
		options.addOption("t", true, "trie file");
		options.addOption("g", true, "directory/ containing the reference for each gene");
		options.addOption("o", true, "directory/ for generated files to be output");
		options.addOption("d", true, "directory/ containing the filtered reads"); 
		options.addOption("cn", true, "sample and cn file");

		options.addOption("h", false, "Show help");
		
		try {
			p.k = 50;
			
			CommandLine cmd = new PosixParser().parse(options, args);
			// help
			if(cmd.hasOption("h")) { usage_exit(); }
			
			if(cmd.hasOption("sample")) {
				p.sample = cmd.getOptionValue("sample"); 
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
			
			if(cmd.hasOption("i")) {
				String bai = cmd.getOptionValue("i"); 
				if (!bai.endsWith("bai")) { usage_exit(); }
				p.i = bai; 
			} else { 
				//usage_exit(); 
			}
			
			if (cmd.hasOption("cn")) {
				p.cn = cmd.getOptionValue("cn");
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
			
//			if(cmd.hasOption("t")) {
//				File triesDir = new File(cmd.getOptionValue("t")); 
//				File[] trieFiles = triesDir.listFiles();
//				HashMap<String, String> geneToTrieFile = new HashMap<String, String>();
//				for (int geneIndex = 0; geneIndex < Genes.getGeneTests().size(); ++geneIndex) {
//					String geneName = Genes.getGeneTests().get(geneIndex).get(0);
//					for (int f = 0; f < trieFiles.length; ++f) {
//						if (trieFiles[f].getName().contains(geneName)) {
//							geneToTrieFile.put(geneName, trieFiles[f].getAbsolutePath());
//						}
//					}
//				}
//				if (geneToTrieFile.size() != Genes.geneTests.size()) { usage_exit(); }
//				p.t = geneToTrieFile;
//			} else {usage_exit(); }
			if (cmd.hasOption("t")) {
				p.t = cmd.getOptionValue("t");
			} else { 
				//usage_exit(); 
				}
			
			if (cmd.hasOption("g")) {
				p.g = cmd.getOptionValue("g");
			} else { 
				//usage_exit(); 
				}
			
			
			
			if (cmd.hasOption("o")) {
				p.o = cmd.getOptionValue("o");
			} else { 
				//usage_exit();
				}
			
			if (cmd.hasOption("d")) {
				p.d = cmd.getOptionValue("d");
			} else { p.d = p.o; }
			
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		return p;
	}
	
	public static void usage_exit(){
		String usage = 
				"\tUsage:\n"
//						+ "\tjava -jar alleleCaller.jar -c copyNumbers -b reads -i readsIndex -t trieDir/ -g geneReferenceDir/ -o outputDir/ -sample sampleName [OPTIONS...] \n"
						+ "\tjava -jar alleleCaller.jar -c copyNumbers -b reads -i readsIndex -t trie -g geneReferenceDir/ -o outputDir/ -sample sampleName [OPTIONS...] \n"

						+ "\n"
						+ "\tOptions:\n" 
						+ "\t[-h] help\n"
						+ "\n";
		System.err.println("\n" + usage);
		System.exit(1);
	
	}
	
	
}
