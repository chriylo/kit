package allele;

public class PairedRecord {
	protected String seq1;
	protected String name1;
	protected String seq2;
	protected String name2;

	
	public PairedRecord(String name1, String seq1, String name2, String seq2) {
		this.seq1 = seq1;
		this.name1 = name1;
		this.seq2 = seq2;
		this.name2 = name2;
	}
	
	public String seq1() {
		return seq1;
	}
	
	public String seq2() {
		return seq2;
	}
	
	public String name1() {
		return name1;
	}
	
	public String name2() {
		return name2;
	}
}
