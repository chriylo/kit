package allele;

import java.util.HashMap;

public class VCFVariant {
	protected String type; //polymorphic or novel
	protected int position;
	protected int offset;
	protected String ref;
	protected String alt;
	HashMap<String, ProbabilityLikelihood> plSamples;

	public VCFVariant(String type, int position, int offset, String ref, String alt) {
		this.type = type;
		this.position = position;
		this.offset = offset;
		this.ref = ref; 
		this.alt = alt;
		plSamples = new HashMap<String, ProbabilityLikelihood>();
	}
	
	public VCFVariant(String type, int position, int offset, String ref) {
		this.type = type;
		this.position = position;
		this.offset = offset;
		this.ref = ref;		
		this.alt = "N";
		plSamples = new HashMap<String, ProbabilityLikelihood>();
	}
	
	public int getOffset() {
		return this.offset;
	}
	
	public void setAlt(String alt) {
		this.alt = alt;
	}
	
	public void addSample(String sample, ProbabilityLikelihood pl) {
		plSamples.put(sample, pl);
	}
	
	public HashMap<String, ProbabilityLikelihood> getPLSamples() {
		return this.plSamples;
	}
	
	public String getRef(){
		return this.ref;
	}
	
	public String getAlt() {
		return this.alt;
	}
	
	
}
