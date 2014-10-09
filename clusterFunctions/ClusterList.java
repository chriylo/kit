package clusterFunctions;

import java.io.File;
import java.util.*;

import main.Test3;
import distanceFunction.EntryWeights;
import distanceFunction.HammingClusterBarcodeDistance;


public class ClusterList {
	private ArrayList<Cluster> clusters; 
	private int[][] templateBarcodes;
	
	protected ClusterList() {
		this.clusters = new ArrayList<Cluster>();
	}
	
	protected ClusterList(int[][] templateBarcodes) {
		this.clusters = new ArrayList<Cluster>();
		this.templateBarcodes = templateBarcodes;
	}
	
	public int[][] getTemplateBarcodes() {
		return this.templateBarcodes;
	}
	
	
	protected void add(Cluster c) {
		clusters.add(c);
	}
	
	public int size() {
		return clusters.size();
	}
	
	public Cluster get(int i) {
		return clusters.get(i);
	}
	/*
	public void clusterStats(){
		System.out.println("Number of clusters: " + clusters.size());
			
		//NUMBER OF NEW CATEGORIES
		ArrayList<String>[] categories = this.getTemplateCategories();
		HashMap<ArrayList<String>, Integer> templateCategoriesMap = new HashMap<ArrayList<String>, Integer>();
		for (int i = 0; i < categories.length; ++i) {
			if (!templateCategoriesMap.containsKey(categories[i])) {
				templateCategoriesMap.put(categories[i], 1);
			}
		}
		System.out.println("Number of categories: " + templateCategoriesMap.size());
	
		HammingClusterBarcodeDistance hamming = new HammingClusterBarcodeDistance();
		EntryWeights myEntryWeights = new EntryWeights(categories[0].size(), 1);
		hamming.setWeights(myEntryWeights);
		ArrayList<Double> scores = new ArrayList<Double>();
		for (int i = 0; i < categories.length-1; ++i) {
			for (int j = i+1; j < categories.length; ++j) {
				scores.add(hamming.distance(categories[i], categories[j]));
			}
		}
		Test3.getCounts(scores);
	}
	*/
	
	
	public ArrayList<String>[] getTemplateCategories() {
		int numTemplates = clusters.get(0).getPartition().length;
		//int numTemplates = clusters.get(0).getRepresentativePartition().length();
		ArrayList<String>[] templateCategories = new ArrayList[numTemplates];
		for (int i = 0; i < numTemplates; ++i) {
			templateCategories[i] = new ArrayList<String>();
		}
		for (int i = 0; i < clusters.size(); ++i) {
        	Cluster c = clusters.get(i);
        	//String representativePartition = c.getRepresentativePartition();
        	String[] partition = c.getPartition();
        	for (int j = 0; j < numTemplates; ++j) {
        		//templateCategories[j].add(String.valueOf(representativePartition.charAt(j)));
        		templateCategories[j].add(String.valueOf(partition[j]));
        	}
		}
		return templateCategories;

	}
	
	public int getTotalNumKmers() {
		int totalNumKmers = 0;
		for (int i = 0; i < clusters.size(); ++i) {
			totalNumKmers += clusters.get(i).getBarcodesOfThisCluster().size();
		}
		return totalNumKmers;
	}
}
