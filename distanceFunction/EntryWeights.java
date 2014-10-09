package distanceFunction;


import java.util.Arrays;

public class EntryWeights {
	
	/**
	 * The maximum number of entries to which weights can be explicitly set.
	 */
	protected int size;
	
	/**
	 * The weight of entries exceeding {@code size}.
	 */
	protected double defaultValue;
	
	/**
	 * Entry-specific weights;
	 */
	protected double[] weights;
	
	public EntryWeights(int size, double defaultValue){
		this.defaultValue = defaultValue;
		this.size = size;
		weights = new double[size];
		Arrays.fill(weights, defaultValue);
	}
	
	public EntryWeights(int size){
		this(size, 1);
	}
	
	public EntryWeights(double[] myWeights) {
		this.weights = myWeights;
		this.size = myWeights.length;
		this.defaultValue = 1;
	}
	


	/**
	 * Generates a weight function that assign all entries the weight 1.
	 */
	public EntryWeights(){
		this(0, 1);
	}
	
	public double weight(int entryIx){
		if (entryIx < size) return weights[entryIx];
		else return defaultValue;
	}
	
	public double[] getWeights() {
		return weights;
	}

	public void setWeight(int entryIx, double weight){
		weights[entryIx] = weight;
	}
	
	public int count() {
		int count = 0;
		for (int i = 0; i < weights.length; ++i) {
			if (weights[i]>0) { count += 1; }
		}
		return count;
	}
	
	public int length() {
		return weights.length;
	}
	
	public int countOverlap(EntryWeights other) {
		int count = 0;
		if (weights.length != other.length()) {
			return -1;
		}
		double[] otherArray = other.weights;
		for (int i = 0; i < weights.length; ++i) {
			if (weights[i]>0 && otherArray[i]>0) {
				count += 1;
			}
		}
		return count;
	}
	
	public void printNonZeroIndices() {
		for (int i = 0; i < weights.length; ++i) {
			if (weights[i]>0) { System.out.println(i); }
		}
	}
	
	public int getNumWeightedEntries() {
		int c = 0;
		for (int i = 0; i < weights.length; ++i) {
			if (weights[i] > 0) {
				c += 1;
			}
		}
		return c;
	}
	
	
}
