package allele;

import java.io.File;
import java.util.*;

import net.sf.samtools.SAMFileReader;

public class Allele {
	protected String name;
	protected HashMap<Integer, Variant> variants;
	//protected SAMFileReader sam;
	protected String bamFilename;
	protected String bamIndxFilename;
	
	public Allele(String name, HashMap<Integer, Variant> variants) {
		this.name = name;
		this.variants = variants;
	}
	
	public void removeVariants(int startPosition, int endPosition) {
		ArrayList<Integer> toRemove = new ArrayList<Integer>();
		for (Iterator it = variants.keySet().iterator(); it.hasNext(); ) {
			Integer pos = (Integer) it.next();
			if (pos >= startPosition && pos <= endPosition) {
				toRemove.add(pos);
			}
		}
		for (int i = 0; i < toRemove.size(); ++i) {
			this.variants.remove(toRemove.get(i));
		}
	}
	
	public void addVariant(Variant v) {
		if (this.variants.containsKey(v.getPosition())) {
			//duplicate
		}
		else {
			this.variants.put(v.getPosition(), v);
		}
	}
	
	public int getNumVariants() {
		return this.variants.size();
	}
	
	public void addVariants(HashMap<Integer, Variant> variants) {
		for (Iterator it = variants.keySet().iterator(); it.hasNext(); ) {
			Integer vpos = (Integer) it.next();
			if (this.variants.containsKey(vpos)) {
				//duplicate
			} else {
				this.variants.put(vpos, variants.get(vpos));
			}
		}
	}

	public HashMap<Integer, Variant> getVariants() {
		return this.variants;
	}
	
	public String getName() {
		return this.name;
	}
	
//	public void setSam(String samFilename, String samIndexFilename) {
//		File f =  new File(samFilename);
//		File f2 = new File(samIndexFilename);
//		this.sam = new SAMFileReader(f, f2);
//
//	}
	
	public void setSam(String f, String f2) {
//		File f =  new File(samFilename);
//		File f2 = new File(samIndexFilename);
//		this.sam = new SAMFileReader(f, f2);
		this.bamFilename = f;
		this.bamIndxFilename = f2;

	}
	
	public String getSam() {
//		return this.sam;
		return this.bamFilename;
	}
	
	public String getSamIndx() {
//		return this.sam;
		return this.bamIndxFilename;
	}
	
}
