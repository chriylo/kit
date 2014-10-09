package distanceFunction;

import java.util.HashMap;
import java.util.ArrayList;

import clusterFunctions.ClusterScores;

import typing.*;


public interface clusterBarcodeDistance {
	//public double distance(ArrayList<HashMap<String, Double>> sample, ArrayList<String> template);
	public double distance(ClusterScores sample, ArrayList<String> templates);
	
	public double distance(ArrayList<String> list0, ArrayList<String> list1);
}
