package allele;

public class Record {
	
	protected String seq;
	protected String name;

	
	public Record(String name, String seq) {
		this.seq = seq;
		this.name = name;
	}
	
	public String seq() {
		return seq;
	}
	
	public String name() {
		return name;
	}
	
}
