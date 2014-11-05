package allele;

public class ProbabilityLikelihood {
	protected int homoref;
	protected int het;
	protected int homoalt;
	
	public ProbabilityLikelihood(int homoref, int het, int homoalt) {
		this.homoref = homoref;
		this.het = het;
		this.homoalt = homoalt;
	}
		
	public ProbabilityLikelihood(String ref, String alt, String myRef, String myAlt, String myPL) {
		String[] myAltArray = myAlt.split(",");
		int indxOfRef = -1;
		int indxOfAlt = -1;
		for (int i = 0; i < myAltArray.length; ++i) {
			if (myAltArray[i].equals(ref)) { indxOfRef = i; }
			if (myAltArray[i].equals(alt)) { indxOfAlt = i; }
		}
		
		String[] myPLArray = myPL.split(",");
		
		if ((indxOfRef == -1) && (indxOfAlt == -1)) {
			homoref = 255;
			homoalt = 255;
			het = 255;
		} else if (indxOfRef == -1) {
			homoref = 255;
			homoalt = 0;
			het = 255;
		} else if (indxOfAlt == -1) {
			homoref = 0;
			homoalt = 255;
			het = 255;
		} else {
			homoref = Integer.valueOf(myPLArray[getPLIndex(indxOfRef,indxOfRef)]);
			homoref = Integer.valueOf(myPLArray[getPLIndex(indxOfAlt,indxOfAlt)]);
			het = Integer.valueOf(myPLArray[getPLIndex(indxOfRef,indxOfAlt)]);
		}
		
	}
	
	public void normalize() {
		//make minimum value = 0
		double min = Math.min(homoref, Math.min(het, homoalt));
		if (homoref == min) { homoref = 0; }
		if (homoalt == min) { homoalt = 0; }
		if (het == min) { het = 0; } 
	}
	
	private int getPLIndex(int k, int j) {
		return (k*(k+1)/2)+j;
	}
	
	public String getGenotype(){
		if ((het < homoref) && (het < homoalt)) {
			return "0/1";
		} else if ((homoalt < het) && (homoalt < homoref)) {
			return "1/1";
		} else {
			return "0/0";
		}
	}
	
	public String toString(){
		return String.valueOf(homoref)+","+String.valueOf(het)+","+String.valueOf(homoalt);
	}
	
}
