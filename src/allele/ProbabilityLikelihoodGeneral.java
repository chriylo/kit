package allele;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.math3.util.ArithmeticUtils;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.apache.commons.math3.util.MathUtils;


public class ProbabilityLikelihoodGeneral {
	int[] pl;
	String ref;
	ArrayList<String> alt;
	
	public ProbabilityLikelihoodGeneral(String ref, ArrayList<String> alt, ArrayList<String> pc, int cn) {
		this.ref = ref;
		this.alt = alt;
		
		pl = new int[(int) CombinatoricsUtils.binomialCoefficient(alt.size()+1,2)+alt.size()+1];
		AlleleScorer a = new AlleleScorer();
		
		if (cn == 0) {
			int counter = 0;
			for (int i = 0; i < (alt.size()+1); ++i ) {
				for (int j = 0; j < i+1; ++j) {
					if ((i == alt.size()) && (j == alt.size())) {
						pl[counter] = 0;
					} else{
						pl[counter] = 255;
					}
					counter += 1;
				}
			}
		} else if (cn == 1) {
			int counter = 0;
			for (int i = 0; i < (alt.size()+1); ++i ) {
				for (int j = 0; j < i+1; ++j) {
					if (i != alt.size()) { //at least one allele is "D"
						pl[counter] = 255;
					} else if (j == alt.size()) { //"D/D"
						pl[counter] = 255;
					} else {
						if (j == 0) {
							pl[counter] = (int) Math.round(a.calculateLikelihoodVariants(pc, ref, ref)*10); //get likelihood of ref/D
						}
						else {
							pl[counter] = (int) Math.round(a.calculateLikelihoodVariants(pc, alt.get(j-1), alt.get(j-1))*10); //get likelihood of alt/D
						}
					}
					counter += 1;
				}
			}
		} else if (cn == 2) { //We just assume that there is one copy on each haplotype
			int counter = 0;
			for (int i = 0; i < (alt.size()+1); ++i ) {
				for (int j = 0; j < i+1; ++j) {
					if (i == alt.size()) { //at least one allele is "D"
						pl[counter] = 255;
					} else if (j == alt.size()) { //"D/D"
						pl[counter] = 255;
					} else {
						if ((j == 0) && (i == 0)){
							pl[counter] = (int) Math.round(a.calculateLikelihoodVariants(pc, ref, ref)*10); //get likelihood of ref/ref
						} else if (j == 0) {
							pl[counter] = (int) Math.round(a.calculateLikelihoodVariants(pc, alt.get(i-1), ref)*10); //get likelihood of ref/alt
						} else if (i == 0) {
							pl[counter] = (int) Math.round(a.calculateLikelihoodVariants(pc, ref, alt.get(j-1))*10); //get likelihood of ref/alt
						} else {
							pl[counter] = (int) Math.round(a.calculateLikelihoodVariants(pc, alt.get(i-1), alt.get(j-1))*10); //get likelihood of alt/alt
						}
					}
					counter += 1;
				}
			}
		} else {
			int counter = 0;
			for (int i = 0; i < (alt.size()+1); ++i ) {
				for (int j = 0; j < i+1; ++j) {
					pl[counter] = 0;
					counter += 1;
				}
			}
		}
		
		
	}
		
	
	
	public ProbabilityLikelihoodGeneral(String ref, ArrayList<String> alt, HashMap<Integer, Integer> normalizedAlleleScores, int cn) {
		
		this.ref = ref;
		this.alt = alt;
		pl = new int[(int) CombinatoricsUtils.binomialCoefficient(alt.size()+1,2)+alt.size()+1];
		
		if (cn == 0) {
			int counter = 0;
			for (int i = 0; i < (alt.size()+1); ++i ) {
				for (int j = 0; j < i+1; ++j) {
					if ((i == alt.size()) && (j == alt.size())) {
						pl[counter] = 0;
					} else{
						pl[counter] = 255;
					}
					counter += 1;
				}
			}
		} else if (cn == 1) {
			int counter = 0;
			for (int i = 0; i < (alt.size()+1); ++i ) {
				for (int j = 0; j < i+1; ++j) {
					if (i != alt.size()) { //make sure at least one allele is "D"
						pl[counter] = 255;
					} else if (j == alt.size()) { //"D/D"
						pl[counter] = 255;
					} else {
						if (normalizedAlleleScores.containsKey(counter)) {
						pl[counter] = normalizedAlleleScores.get(counter); //normalizedAlleleScores should just have entries for haploid
						} else {
							pl[counter] = 255;
						}
					}
					counter += 1;
				}
			}
		} else if (cn == 2) { //We just assume that there is one copy on each haplotype
			int counter = 0;
			for (int i = 0; i < (alt.size()+1); ++i ) {
				for (int j = 0; j < i+1; ++j) {
					if (i == alt.size()) { //at least one allele is "D"
						pl[counter] = 255;
					} else if (j == alt.size()) { //"D/D"
						pl[counter] = 255;
					} else {
						//pl[counter] = normalizedAlleleScores.get(counter);
						if (normalizedAlleleScores.containsKey(counter)) {
							pl[counter] = normalizedAlleleScores.get(counter); //normalizedAlleleScores should just have entries for haploid
							} else {
								pl[counter] = 255;
							}
	
					}
					counter += 1;
				}
			}
		} else {
			int counter = 0;
			for (int i = 0; i < (alt.size()+1); ++i ) {
				for (int j = 0; j < i+1; ++j) {
					pl[counter] = 0;
					counter += 1;
				}
			}
		}
	}



	public void normalize() {
		//make minimum value = 0
		double min = pl[0];
		for (int i = 0; i < pl.length; ++i) {
			if (pl[i] < min) {
				min = pl[i];
			}
		}
		for (int i = 0; i < pl.length; ++i) {
			if (pl[i] == min) {
				//System.out.println(pl[i]);
				pl[i] = 0;
			}
		}
	}
	
	public String getGenotype(){
		int indexOfMin = 0;
		for (int i = 0; i < pl.length; ++i) {
			if (pl[i] < pl[indexOfMin]) {
				indexOfMin = i;
			}
		}
		int counter = 0; 
		for (int i = 0; i < (alt.size()+1); ++i ) {
			for (int j = 0; j < i+1; ++j) {
				if (indexOfMin == counter) { 
					//String g1 = ref;
					//String g2 = ref;
					//if (i > 0) { g1 = alt.get(i-1); }
					//if (j > 0) { g2 = alt.get(j-1); }
					//return g1 + "/" + g2;
					return String.valueOf(j)+"/"+String.valueOf(i);  
				}
				counter += 1;
			}
		}
		
		//return ref + "/" + ref;
		return String.valueOf(0)+"/"+String.valueOf(0);
	}
	
	public String toString(){
		String myStr = "";
		for (int i = 0; i < pl.length-1; ++i) {
			myStr = myStr + String.valueOf(pl[i]) + ",";
		} myStr = myStr + String.valueOf(pl[pl.length-1]); 
		return myStr;
	}
	
}
