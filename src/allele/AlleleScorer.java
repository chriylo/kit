package allele;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.*;
import java.util.zip.GZIPInputStream;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.util.CombinatoricsUtils;

import net.sf.samtools.*;



public class AlleleScorer {
	
	public static Iterator<String> vcfIterator(String path) throws IOException{
		
		//TODO: Check that file is .vcf
		InputStream in = new FileInputStream(path);; 

		@SuppressWarnings("resource")
		final BufferedReader br = new BufferedReader(new InputStreamReader(in));
		
		return new Iterator<String>() {
			String line = br.readLine();
				
			@Override
			public void remove() {
				throw new UnsupportedOperationException(); 
			}

			@Override
			public String next() {
				String var;
				try {
					var = line.trim();
					line = br.readLine();
				} catch (IOException e) {
					throw new RuntimeException(e.getMessage());
				}
				return var;
			}

			@Override
			public boolean hasNext() {
				while (line != null && line.startsWith("#") ){
					try {
						line = br.readLine();
					} catch (IOException e) {
						throw new RuntimeException(e.getMessage());
					}
				} 
				return line != null;
			}
		};
	}
	
	int sampleCopyNumber;
	//HashMap<Integer, Variant> sampleVariants;
	HashMap<String, Allele> alleles;
	HashMap<String, Double> likelihoodAllPairs;
	Allele referenceAllele;
	String sampleSAMFilename;
	String sampleSAMIndxFilename;
	double errorRate;
	Boolean debug = Boolean.FALSE;
	int geneIndex;
	String name;
	String outputdir;
	PrintStream ps;
	PrintStream dps;
	static double min = 25.5;
	

	public AlleleScorer() {
		debug = Boolean.FALSE;
	}
	
	public AlleleScorer(int geneIndex) throws FileNotFoundException {
		debug = Boolean.FALSE;
		this.geneIndex = geneIndex;
		sampleCopyNumber = 2;
		//sampleVariants = new HashMap<Integer, Variant>();
		alleles = new HashMap<String, Allele>();
		likelihoodAllPairs = new HashMap<String, Double>();
		referenceAllele = new Allele("reference", new HashMap<Integer, Variant>() );
		errorRate = 0.001;
		this.name = "sample";
		this.outputdir = "";
		this.ps = new PrintStream(this.outputdir + this.geneIndex + "." + this.name + ".out" );
		//this.dps = new PrintStream(this.outputdir + this.geneIndex + "." + this.name + ".debug" );

	}
	
	public AlleleScorer(int geneIndex, String name, String outputdir) throws FileNotFoundException {
		debug = Boolean.TRUE;
		this.geneIndex = geneIndex;
		sampleCopyNumber = 2;
		//sampleVariants = new HashMap<Integer, Variant>();
		alleles = new HashMap<String, Allele>();
		likelihoodAllPairs = new HashMap<String, Double>();
		referenceAllele = new Allele("reference", new HashMap<Integer, Variant>() );
		errorRate = 0.001;
		this.name = name;
		this.outputdir = outputdir;
		this.ps = new PrintStream(this.outputdir + this.geneIndex + "." + this.name + ".out3" );
		this.dps = new PrintStream(this.outputdir+ this.geneIndex + "."  + this.name + ".debug3" );

	}
	
	public HashMap<String, Allele> getAlleles() {
		return this.alleles;
	}
	
	public static double getMin() {
		return min;
	}
	
	public void setAlleles(HashMap<String, Allele> alleles) {
		this.alleles = alleles;
	}
	
	public int getSampleCopyNumber() {
		return this.sampleCopyNumber;
	}
	
	public Allele getReferenceAllele() {
		return this.referenceAllele;
	}
	
	public Allele getAlternateAllele() {
		HashMap<Integer, Variant> variants = new HashMap<Integer, Variant>();
		
		HashMap<Integer, Variant> refVariants = referenceAllele.getVariants();
		for (Iterator it = refVariants.keySet().iterator(); it.hasNext(); ) {
			Integer pos = (Integer) it.next();
			variants.put(pos, new Variant(pos, "N", -1, "1/1"));
		}
		for (Iterator it = alleles.keySet().iterator(); it.hasNext(); ) {
			String a = (String) it.next();
			HashMap<Integer, Variant> alleleVariants = alleles.get(a).getVariants();
			for (Iterator it2 = alleleVariants.keySet().iterator(); it2.hasNext(); ) {
				Integer pos = (Integer) it2.next();
				if (variants.get(pos).getAllele().equals("N")) { //still needs to be filled
					String refA = refVariants.get(pos).getAllele().toUpperCase();
					String altA = alleleVariants.get(pos).getAllele().toUpperCase();
					if (!refA.equals(altA)) { //not equal to the reference allele
						//System.out.println(pos + "\t" + refVariants.get(pos).getAllele() + "\t" + alleleVariants.get(pos).getAllele());
						variants.get(pos).setAllele(alleleVariants.get(pos).getAllele().toUpperCase());
					}
				}
			}
		}
		
		Allele alt = new Allele("alternate", variants);
		
		return alt;
	}
	
	public void setReferenceAllele(Allele ref) {
		this.referenceAllele = ref;
	}
	
	public void loadSample(int c) {
		if (c >= 0) { this.sampleCopyNumber = c; }

	}
	
	public void loadSample(int c, String sampleSAMPath, String sampleSAMIndexPath) throws IOException {
		if (c >= 0) { this.sampleCopyNumber = c; }
		loadSample( sampleSAMPath, sampleSAMIndexPath);
		
	}
	
	public void loadSample(String sampleSAMPath, String sampleSAMIndexPath) throws IOException {
		this.sampleSAMFilename = sampleSAMPath;
		this.sampleSAMIndxFilename = sampleSAMIndexPath;
		
//		for (Iterator it = vcfIterator(sampleVCFPath); it.hasNext(); ) {
//			String line = (String) it.next();
//			String[] line_split = line.split("\t");
//			int pos = Integer.valueOf(line_split[1]);
//			String allele = line_split[4];
//			if (allele.length() > 1) { continue; }
//			double qual = Double.valueOf(line_split[5]);
//			String genotype = line_split[9].split(":")[0];
//			//if (qual > 100) {
//				this.sampleVariants.put(pos, new Variant(pos, allele, qual, genotype));
//				this.sampleVariants.get(pos).setGenotypeLiklihood(getGenotypeLikelihoodFromVCF(line_split[8], line_split[9]));
//			//}		
//		}
	}
	
	public HashMap<String, Double> getGenotypeLikelihoodFromVCF(String format, String data) {
		HashMap<String, Double> genotypeLikelihood = new HashMap<String, Double>();
		int indexOfPL = -1;
		String[] formatSplit = format.split(":");
		for (int i = 0; i < formatSplit.length; ++i) {
			if (formatSplit[i].equals("PL")) {
				indexOfPL = i;
			}
		}
		String[] dataSplit = data.split(":");
		String pl = dataSplit[indexOfPL];
		String[] plSplit = pl.split(",");
		genotypeLikelihood.put("0/0", Double.valueOf(plSplit[0])/10);
		genotypeLikelihood.put("0/1", Double.valueOf(plSplit[1])/10);
		genotypeLikelihood.put("1/1", Double.valueOf(plSplit[2])/10);
		return genotypeLikelihood;
	}
	
	public void loadAllele(ArrayList<String> alleleVCFFiles, ArrayList<String> alleleSAMFiles, ArrayList<String> alleleSAMIndexFiles ) throws IOException {
		
		for (int i = 0; i < alleleVCFFiles.size(); ++i ) {
			File file = new File(alleleVCFFiles.get(i));
			String alleleName = file.getName();
			HashMap<Integer, Variant> alleleVariants = new HashMap<Integer, Variant>();
			for (Iterator it = vcfIterator(file.getAbsolutePath()); it.hasNext(); ) {
				String line = (String) it.next(); 
				String[] line_split = line.split("\t");
				int pos = Integer.valueOf(line_split[1]);
				String refAllele = line_split[3];
				if (refAllele.length() > 1) { refAllele = refAllele.substring(0,1); }
				String allele = line_split[4];
				if (allele.length() > 1) { allele = allele.substring(0,1); }
				double qual = Double.valueOf(line_split[5]);
				String genotype = line_split[9].split(":")[0];
				alleleVariants.put(pos, new Variant(pos, allele, -1, "1/1"));
				this.referenceAllele.addVariant(new Variant(pos, refAllele, -1, "1/1"));
			}
			this.alleles.put(alleleName, new Allele(alleleName, alleleVariants));
			this.alleles.get(alleleName).setSam(alleleSAMFiles.get(i), alleleSAMIndexFiles.get(i));
		}
	}
	
	public void removeBadAlleles() {
		ArrayList<String> toRemove = new ArrayList<String>();
		HashMap<String, Allele> goodAlleles = new HashMap<String, Allele>();
		for (Iterator it = this.alleles.keySet().iterator(); it.hasNext(); ) {
			String alleleName = (String) it.next();
			HashMap<Integer, Variant> alleleVariants = this.alleles.get(alleleName).getVariants();
			HashMap<Integer, Variant> newAlleleVariants = new HashMap<Integer, Variant>();

			int totalBad = 0;
			for (Iterator it2 = this.referenceAllele.getVariants().keySet().iterator(); it2.hasNext(); ) {
				Integer var = (Integer) it2.next(); 
				if (!alleleVariants.containsKey(var)) {
					ArrayList<String> pc = getPileupColumn(var, this.alleles.get(alleleName).getSam(), this.alleles.get(alleleName).getSamIndx());
					if (pc.size() == 0) { totalBad +=1; }
					else {
						newAlleleVariants.put(var, new Variant(var, this.referenceAllele.getVariants().get(var).getAllele(), -1, "0/0"));
					}
				} else {
					newAlleleVariants.put(var, alleleVariants.get(var));
				}
				
			}
			if (totalBad > this.referenceAllele.getNumVariants()*0.6) { 
				toRemove.add(alleleName);
			} else {
				goodAlleles.put(alleleName, new Allele(alleleName, newAlleleVariants));
			}
		}
		

		this.alleles = goodAlleles;
	}
	

	public double calculateLikelihoodVariants(ArrayList<String> pc, Variant v, Variant v2) {
		//check that variants are from the same position
		
		if ((v.getAllele() == "N") && (v2.getAllele() == "N")) {
			if (debug) {dps.println(v.getPosition() + "\t" + "NIA" + "\t" + 0 + "\t" + pc.toString() + "\t" + v.getAllele() + "\t" + v2.getAllele()) ; }
			return 0;
		} 
		if (pc.size() > 0) {
                double likelihood = calculateLikelihoodBinomial(pc, v.getAllele(), v2.getAllele());

				if (debug) { dps.println(v.getPosition()+ "\t" + "C" + "\t" + likelihood + "\t" + pc.toString() + "\t" + v.getAllele() + "\t" + v2.getAllele()); }
				return likelihood;
			} else {
				if (debug) {dps.println(v.getPosition() + "\t" + "NC" + "\t" + 0 + "\t" + pc.toString() + "\t" + v.getAllele() + "\t" + v2.getAllele()) ; }
				return 0;
			}
		
	}
	
	public double calculateLikelihoodVariants(ArrayList<String> pc, String v, String v2) {
		//check that variants are from the same position
		
		if ((v == "N") && (v2 == "N")) {
			return 0;
		} 
		if (pc.size() > 0) {
                double likelihood = calculateLikelihoodBinomial(pc, v, v2);
				return likelihood;
			} else {
				return 0;
			}
		
	}
	
	public double calculateLikelihoodBinomial(ArrayList<String> pileup, String a, String a2) {
		
		a = a.toUpperCase();
		a2 = a2.toUpperCase();
		
		if (a.equals(a2)) {
            int count1 = 0;
            int count2 = 0;
            for (int i = 0; i < pileup.size(); ++i) {
            	String p = pileup.get(i);
                if (p.equals(a)) { count1 += 1; }
                else { count2 += 1; }
            }
            
            try {
            	return Math.min(min, -Math.log10(Math.pow(this.errorRate,count2)*(CombinatoricsUtils.binomialCoefficient(count1+count2, count1)))); 
            } catch(MathArithmeticException mae) {
            	return min;
            }
            
		}
         else {
        	if (a == "N") { return Math.min(calculateLikelihoodBinomial(pileup, "A", a2), Math.min(calculateLikelihoodBinomial(pileup, "C", a2), Math.min(calculateLikelihoodBinomial(pileup, "G", a2), calculateLikelihoodBinomial(pileup, "T", a2)))) ; }
        	if (a2 == "N") { return Math.min(calculateLikelihoodBinomial(pileup, "A", a), Math.min(calculateLikelihoodBinomial(pileup, "C", a), Math.min(calculateLikelihoodBinomial(pileup, "G", a), calculateLikelihoodBinomial(pileup, "T", a)))); }

        	int count1 = 0;
            int count2 = 0;
            int count3 = 0;
            for (int i = 0; i < pileup.size(); ++i) {
                if (pileup.get(i).equals(a)) { count1 += 1; }
                else if (pileup.get(i).equals(a2)) { count2 += 1; }
                else { count3 += 1; }
            }
            
            try{
            return Math.min(min, -Math.log10(Math.pow(this.errorRate,count3)*(CombinatoricsUtils.binomialCoefficient(count1+count2+count3, count1+count2))*(1.0/Math.pow(2,count1+count2))*(CombinatoricsUtils.binomialCoefficient(count1+count2,count1))));
         } catch(MathArithmeticException mae) {
        	 if (count3/(count1+count2+count3) > 0.1) {
        		 return min;
        	 } else {
        		 if (count1 > count2) {
        			 if (count2/count1 < 0.5) { return 0; } else { return min; }
        		 } else {
        			 if (count1/count2 < 0.5) {	 return 0; } else { return min; }
        		 }
        		 
        	 }
         }
         }
	}
	
	public double calculateLikelihoodAlleles(HashMap<Integer, ArrayList<String>> pcs, Allele a, Allele a2) {
		//KNOWN VARIANTS
		double totalLikelihood = 0;
		HashMap<Integer, Variant> allVariants = this.referenceAllele.getVariants();
		HashMap<Integer, Variant> alleleVariants = a.getVariants(); 
		HashMap<Integer, Variant> allele2Variants = a2.getVariants(); 
		int notCovered = 0;
		int totalVariants = 0;
		for (Iterator it = allVariants.keySet().iterator(); it.hasNext(); ) {
			Integer vpos = (Integer) it.next();
			Variant v = allVariants.get(vpos);
			if (alleleVariants.containsKey(vpos)) {
				v = alleleVariants.get(vpos);
			} else {
				v = new Variant(vpos, "N", -1, "1/1"); 
			}
			Variant v2 = allVariants.get(vpos);
			if (allele2Variants.containsKey(vpos)) {
				v2 = allele2Variants.get(vpos);
			} else {
				v2 = new Variant(vpos, "N", -1, "1/1"); 
			}
			ArrayList<String> pc = pcs.get(vpos);
			if (pc.size()==0) { notCovered+=1; }
			double l;
			if ((v.getAllele() == "N") || (v2.getAllele() == "N")) {
				if (debug) {dps.println(v.getPosition() + "\t" + "NIA" + "\t" + 0 + "\t" + pc.toString() + "\t" + v.getAllele() + "\t" + v2.getAllele()) ; }
				l = 0;
			} else if (pc.size() <= 0) {
				if (debug) {dps.println(v.getPosition() + "\t" + "NC" + "\t" + 0 + "\t" + pc.toString() + "\t" + v.getAllele() + "\t" + v2.getAllele()) ; }
				l = 0;
			} else {
				l = calculateLikelihoodVariants(pcs.get(vpos), v, v2);
				totalVariants += 1;
			}
			totalLikelihood += l;
		}
		
//		//NOVEL SAMPLE VARIANTS
//		for (Iterator it = this.sampleVariants.keySet().iterator(); it.hasNext(); ) {
//			Integer spos = (Integer) it.next();
//			if (!allVariants.containsKey(spos)) {
//				double l = getProbWrong(this.sampleVariants.get(spos));
//				totalLikelihood += l;
//				if (debug) { dps.println(spos + "\t" + "NOVEL" + "\t" + l); };
//			}
//		}
		
		if (debug) { dps.println(a.getName() + "\t" + a2.getName() + "\t" + totalLikelihood); }
		//int totalVariants = this.referenceAllele.getVariants().size();
		//if (notCovered == totalVariants) {return 0;}
		return totalLikelihood/(totalVariants);
	
	}
	
	public double getProbWrong(Variant v) {
		return v.getQual()/10;
	}
	
	public ArrayList<String> getPileupColumn(int pos, String samFilename, String samIndxFilename) {
		SAMFileReader sam = new SAMFileReader(new File(samFilename), new File(samIndxFilename));
		ArrayList<String> pileup = new ArrayList<String>();
		String name = sam.getFileHeader().getSequence(0).getSequenceName();
		for (Iterator it = sam.queryOverlapping(name, pos, pos); it.hasNext(); ) {
			SAMRecord rec = (SAMRecord) it.next();
			String seq = rec.getReadString();
			for (int i = 1; i <= seq.length(); ++i) {
				if (rec.getReferencePositionAtReadPosition(i) == pos-1) {
					pileup.add(String.valueOf(seq.charAt(i)).toUpperCase());
					break;
				}
			}
		}
		sam.close();
		return pileup;
	}
	
	 
public void calculateLikelihoodHaploid(String filename) throws IOException {

		
		HashMap<Integer, ArrayList<String>> pcs = getAllPileups(filename);
		calculateLikelihoodHaploidHelper(pcs);
		
	}

	
	public void calculateLikelihoodHaploid() throws FileNotFoundException {

		
		HashMap<Integer, ArrayList<String>> pcs = getAllPileups();
		calculateLikelihoodHaploidHelper(pcs);
		
	}

	
	public void calculateLikelihoodHaploidHelper(HashMap<Integer, ArrayList<String>> pcs) throws FileNotFoundException {

		
		//HashMap<Integer, ArrayList<String>> pcs = getAllPileups();
		
		for (Iterator it = this.alleles.keySet().iterator(); it.hasNext(); ) {
			String alleleName = (String) it.next();
			Allele a = this.alleles.get(alleleName);
			double l = calculateLikelihoodAlleles(pcs, a, a);
			this.likelihoodAllPairs.put(a.getName(), l);
		}
		//print out best alleles in sorted order
		ArrayList<Map.Entry<String, Double>> scoresList = new ArrayList<Map.Entry<String, Double>>();
		for (Iterator it = this.likelihoodAllPairs.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry<String, Double> entry = (Map.Entry<String, Double>) it.next();
			scoresList.add(entry);
		} 
		Collections.sort(scoresList, new Comparator<Map.Entry<String, Double>>() { public int compare(Map.Entry<String, Double> e, Map.Entry<String, Double> e2 ) { if (e.getValue() > e2.getValue()) {return 1;} else if (e2.getValue() > e.getValue()) {return -1; } else {return 0;} } });
		for (Iterator it2 = scoresList.iterator(); it2.hasNext(); ) {
			Map.Entry<String, Double> score = (Map.Entry<String, Double>) it2.next();
			//System.out.println(score.getKey() + "\t" + score.getValue());
			ps.println(score.getKey() + "\t" + score.getValue());
		}
	}
	
	public HashMap<Integer, ArrayList<String>> getAllPileups() {
		HashMap<Integer, ArrayList<String>> pcs = new HashMap<Integer, ArrayList<String>>();
		HashMap<Integer, Variant> allVariants = this.referenceAllele.getVariants();
		for (Iterator it = allVariants.keySet().iterator(); it.hasNext(); ) {
			Integer vpos = (Integer) it.next();
			ArrayList<String> pc = getPileupColumn(vpos, sampleSAMFilename, sampleSAMIndxFilename);
			pcs.put(vpos, pc);
		}
		return pcs;
	}
	
	public HashMap<Integer, ArrayList<String>> getAllPileups(String filename) throws IOException {
		HashMap<Integer, ArrayList<String>> pcsInFile = new HashMap<Integer, ArrayList<String>>();
		InputStream in = new FileInputStream(filename);

		final BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String line = br.readLine();

		while (line != null){
			if (line.startsWith("KIR")) { break; }
			String[] linesplit = line.split("\t");
			if (linesplit[1].contains("NOVEL")) { break; }
			String pcsString = linesplit[3];
			pcsString = pcsString.substring(1, pcsString.length()-1);
			String[] pcsStringSplit = pcsString.split(", ");
			ArrayList<String> pcsArrayList = new ArrayList<String>();
			for (int i = 0; i < pcsStringSplit.length; ++i) {
				pcsArrayList.add(pcsStringSplit[i]);
			}
			int vpos = Integer.valueOf(linesplit[0]);
			pcsInFile.put(vpos, pcsArrayList);
			line = br.readLine();

		}
		
		
		HashMap<Integer, ArrayList<String>> pcs = new HashMap<Integer, ArrayList<String>>();
		HashMap<Integer, Variant> allVariants = this.referenceAllele.getVariants();
		for (Iterator it = allVariants.keySet().iterator(); it.hasNext(); ) {
			Integer vpos = (Integer) it.next();
			ArrayList<String> pc = new ArrayList<String>();
			if (pcsInFile.containsKey(vpos)) { pc = pcsInFile.get(vpos); } 
			pcs.put(vpos, pc);
		}
		return pcs;
	}
	
	public void calculateLikelihoodAllPairs() throws FileNotFoundException {	
		HashMap<Integer, ArrayList<String>> pcs = getAllPileups();
		calculateLikelihoodAllPairsHelper(pcs);
	}
	
	public void calculateLikelihoodAllPairs(String filename) throws IOException {
		HashMap<Integer, ArrayList<String>> pcs = getAllPileups(filename);
		calculateLikelihoodAllPairsHelper(pcs);
	}

	
	public void calculateLikelihoodAllPairsHelper(HashMap<Integer, ArrayList<String>> pcs) throws FileNotFoundException {	
		
		
		//HashMap<Integer, ArrayList<String>> pcs = getAllPileups();

		
		ArrayList<String> alleleNames = new ArrayList<String>();
		for (Iterator it = this.alleles.keySet().iterator(); it.hasNext(); ) {
			String n = (String) it.next();
			alleleNames.add(n);
 		}
		for (int i = 0; i < alleleNames.size(); ++i) {
			for (int j = i; j < alleleNames.size(); ++j) {
				Allele a1 = this.alleles.get(alleleNames.get(i));
				Allele a2 = this.alleles.get(alleleNames.get(j));
				double l = calculateLikelihoodAlleles(pcs, a1, a2);
				this.likelihoodAllPairs.put(a1.getName()+"_"+a2.getName(), l);
			}
		}
		//print out best alleles in sorted order
		ArrayList<Map.Entry<String, Double>> scoresList = new ArrayList<Map.Entry<String, Double>>();
		for (Iterator it = this.likelihoodAllPairs.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry<String, Double> entry = (Map.Entry<String, Double>) it.next();
			scoresList.add(entry);
		} 
		Collections.sort(scoresList, new Comparator<Map.Entry<String, Double>>() { public int compare(Map.Entry<String, Double> e, Map.Entry<String, Double> e2 ) { if (e.getValue() > e2.getValue()) {return 1;} else if (e2.getValue() > e.getValue()) {return -1; } else {return 0;} } });
		for (Iterator it2 = scoresList.iterator(); it2.hasNext(); ) {
			Map.Entry<String, Double> score = (Map.Entry<String, Double>) it2.next();
			//System.out.println(score.getKey() + "\t" + score.getValue());
			ps.println(score.getKey() + "\t" + score.getValue());
		}
	}

	public void loadAllele(Allele ref,
			HashMap<String, Allele> hashMap) {
		this.alleles = hashMap;
		this.referenceAllele = ref;
		
	}
	
}
