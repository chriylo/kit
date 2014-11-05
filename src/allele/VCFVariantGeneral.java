package allele;

import java.util.ArrayList;
import java.util.HashMap;

public class VCFVariantGeneral {
	protected String type; //polymorphic or novel
	protected int position;
	protected int offset;
	protected String ref;
	protected ArrayList<String> alt;
	HashMap<String, ProbabilityLikelihoodGeneral> plSamples;

	public VCFVariantGeneral(String type, int position, int offset, String ref, ArrayList<String> alt) {
		this.type = type;
		this.position = position;
		this.offset = offset;
		this.ref = ref; 
		this.alt = alt;
		plSamples = new HashMap<String, ProbabilityLikelihoodGeneral>();
	}
	
//	public VCFVariantGeneral(String type, int position, int offset, String ref) {
//		this.type = type;
//		this.position = position;
//		this.offset = offset;
//		this.ref = ref;		
//		this.alt = new ArrayList<String>();
//		this.alt.add("N");
//		plSamples = new HashMap<String, ProbabilityLikelihoodGeneral>();
//	}
	
	public int getOffset() {
		return this.offset;
	}
	
	public void setAlt(ArrayList<String> alt) {
		this.alt = alt;
	}
	
	public void addSample(String sample, ProbabilityLikelihoodGeneral pl) {
		plSamples.put(sample, pl);
	}
	
	public HashMap<String, ProbabilityLikelihoodGeneral> getPLSamples() {
		return this.plSamples;
	}
	
	public String getRef(){
		return this.ref;
	}
	
	public ArrayList<String> getAlt() {
		return this.alt;
	}
	
	public String getAltStr() {
		String altstr = "";
		for (int i = 0; i < this.alt.size()-1; ++i) {
			altstr = altstr + this.alt.get(i) + ",";
		} altstr = altstr + this.alt.get(this.alt.size()-1); 
		return altstr;
		//return this.alt;
	}
	
	
}
