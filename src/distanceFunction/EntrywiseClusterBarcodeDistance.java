package distanceFunction;

import java.util.HashMap;
import java.util.ArrayList;

import typing.*;
import clusterFunctions.*;


public abstract class EntrywiseClusterBarcodeDistance implements
		clusterBarcodeDistance {
	
	EntryWeights weights;

	public EntrywiseClusterBarcodeDistance(EntryWeights weights) {
		this.weights = weights;
	}
	
	public EntrywiseClusterBarcodeDistance() {
		this.weights = new EntryWeights();
		
	}
	
	/*
	@Override
    public double distance(ArrayList<HashMap<String, Double>> sample, ArrayList<String> template) {
		assert sample != null && template != null : "Trying to compute distance "
                + "between null vectors.";
        assert sample.size() == template.size() : "Trying to compute distance "
                + "between vectors of different lengths.";

        double sum = 0;
        for (int i = 0; i < sample.size(); i++) {
            sum += weights.weight(i) * entryDistance(sample.get(i), template.get(i));
        }
        return sum;
    }
    */
	
	public double distance(ClusterScores sampleClusterScores, ArrayList<String> template) {
		ArrayList<ClusterScore> sample = sampleClusterScores.getScores();
		
		assert sample != null && template != null : "Trying to compute distance "
                + "between null vectors.";
        assert sample.size() == template.size() : "Trying to compute distance "
                + "between vectors of different lengths.";

        double sum = 0;
        for (int i = 0; i < sample.size(); i++) {
            sum += weights.weight(i) * entryDistance(sample.get(i), template.get(i));
        }
        return sum;
    }
	
	public double distance(ArrayList<String> list0, ArrayList<String> list1) {
		assert list0 != null && list1 != null : "Trying to compute distance "
                + "between null vectors.";
		assert list0.size() == list1.size() : "Trying to compute distance "
                + "between vectors of different lengths.";
		
		double sum = 0;
		for (int i = 0; i < list0.size(); ++i) {
			sum += weights.weight(i) * entryDistance(list0.get(i), list1.get(i));
		}
		return sum;
	}
	
	protected abstract double entryDistance(ClusterScore sampleEntry, String templateEntry);
	protected abstract double entryDistance(String a, String b);
	
	public void setWeights(EntryWeights weights) {
        this.weights = weights;
    }
}
