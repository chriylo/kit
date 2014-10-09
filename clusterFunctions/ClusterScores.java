package clusterFunctions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import barcodeFunctions.BarcodeFunctions;

import clusterFunctions.*;


public class ClusterScores {
	private ArrayList<ClusterScore> scores;
	private double[] weights;
	
	public ClusterScores(ClusterList clusters, double[] weights, int[] sampleBarcode) {
		this.initialize(clusters, weights, sampleBarcode);
	}
	
	public ClusterScores(ClusterList clusters, int[] sampleBarcode) {
		
		double[] weights = new double[clusters.size()];
		Arrays.fill(weights, 1);
		
		initialize(clusters, weights, sampleBarcode);
		
	}
	
	private void initialize(ClusterList clusters, double[] weights, int[] sampleBarcode) {
		this.scores = new ArrayList<ClusterScore>();
		this.weights = weights;
		for (int i = 0; i < clusters.size(); ++i) {
        	Cluster c = clusters.get(i);       
        	int[] subsetSampleBarcode = BarcodeFunctions.getSubsetBarcode(sampleBarcode, c.getClusterBarcodeIndices());
        	ClusterScore clusterScore = c.getClusterScore(subsetSampleBarcode);
        	this.scores.add(clusterScore);
		}
	}
	
	
	
	
	
	public void setClusterWeights(double[] clusterWeights) {
		assert clusterWeights.length == scores.size() : "Trying to set weights of differet length.";
		this.weights = clusterWeights;
	}
	
	public ArrayList<ClusterScore> getScores() {
		return this.scores;
	}
	
	public int getNumClusters() {
		return this.scores.size();
	}
	
	public double[] getScoresForCategoryBarcode(ArrayList<String> categoryBarcode) {
		assert scores != null && categoryBarcode != null : "Trying to compute distance "
                + "between null vectors.";
		assert scores.size() == categoryBarcode.size() : "Trying to compute distance "
                + "between vectors of different lengths.";
		
		double[] catBarcodeScores = new double[scores.size()];
		for (int i = 0; i < scores.size(); ++i) {
			catBarcodeScores[i] = scores.get(i).get(categoryBarcode.get(i));
		}
		return catBarcodeScores;
	}
	
	public double[] getMinScores() {
		double[] minScores = new double[scores.size()];
		for (int i = 0; i < scores.size(); ++i) {
			minScores[i] = scores.get(i).getMinCategoryScore();
		}
		return minScores;
	}
	
	public double[] getMaxScores() {
		double[] maxScores = new double[scores.size()];
		for (int i = 0; i < scores.size(); ++i) {
			maxScores[i] = scores.get(i).getMinCategoryScore();
		}
		return maxScores;
	}
	
	public double[] getClusterWeights() {
		return this.weights;
	}
	
	
	
	
	
}
