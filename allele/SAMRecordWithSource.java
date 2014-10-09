package allele;

import java.util.ArrayList;

import net.sf.samtools.SAMRecord;

public class SAMRecordWithSource {
	public SAMRecord rec;
	public ArrayList<Integer> sources;
	
	public SAMRecordWithSource(SAMRecord rec, ArrayList<Integer> sources) {
		this.rec = rec;
		this.sources = sources;
	}
	
	public SAMRecordWithSource(SAMRecord rec) {
		this.rec = rec;
		this.sources = new ArrayList<Integer>();
	}
	
}
