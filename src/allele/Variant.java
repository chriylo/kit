package allele;

import java.util.*;

public class Variant {

	protected int position;
	protected String allele;
	protected double qual;
	protected String genotype;
	protected HashMap<String, Double> genotypeLikelihood;
	
	public Variant(int position, String allele, double qual, String genotype) {
		this.position = position;
		this.allele = allele;
		this.qual = qual;
		this.genotype = genotype;
		this.genotypeLikelihood = new HashMap<String, Double>();
	}
	
	
	
	public HashMap<String, Double> getGenotypeLikelihood() {
		return this.genotypeLikelihood;
	}
	
	public void setGenotypeLiklihood(HashMap<String, Double> genotypeLikelihood) {
		this.genotypeLikelihood = genotypeLikelihood;
	}
	
	public int getPosition() {
		return this.position;
	}
	
	public double getQual() {
		return this.qual;
	}
	
	public String getAllele() {
		return this.allele;
	}
	
	public String getGenotype() {
		return this.genotype;
	}
	
	public void setAllele(String allele) {
		this.allele = allele;
	}
}
