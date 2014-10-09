package distanceFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;



public class DistanceAnalysis {
	private static double getCentroid(double[][] distanceMatrix, ArrayList<Integer> category) {
		double average = 0;
		int count = 0;
		for (int i = 0; i < category.size()-1; ++i) {
			//System.out.println(distanceMatrix[category.get(i)].length);
			for (int j = i+1; j < category.size(); ++j) {
				average += distanceMatrix[category.get(i)][category.get(j)];
				count += 1;
			}
		}
		average = average/count;
		return average; 
	}
	
	private static double interCatDistance(double[][] distanceMatrix, ArrayList<Integer> category) {
		
		double average = getCentroid(distanceMatrix, category);
		
		double averageDistance = 0;
		double count = 0;
		for (int i = 0; i < category.size()-1; ++i) {
			for (int j = i+1; j < category.size(); ++j) {
				averageDistance += Math.abs(average - distanceMatrix[category.get(i)][category.get(j)]);
				count += 1;
			}
		}
		averageDistance = averageDistance/count;
		
		return averageDistance;
	}
	
	private static double intraCatDistance(double[][] distanceMatrix, ArrayList<Integer> categoryA, ArrayList<Integer> categoryB) {
		double centroidA = getCentroid(distanceMatrix, categoryA);
		double centroidB = getCentroid(distanceMatrix, categoryB);
		return Math.abs(centroidA-centroidB);
	}
	
	public static ArrayList<ArrayList<Integer>> getCategoriesList(String categoriesString) {
		HashMap<String, ArrayList<Integer>> catMap = new HashMap<String, ArrayList<Integer>>(); 
		for (int i = 0; i < categoriesString.length(); ++i) {
			String key = String.valueOf(categoriesString.charAt(i));
			if (!catMap.containsKey(key)) {
				catMap.put(key, new ArrayList<Integer>()); 
			} 
			catMap.get(key).add(i);	
		}
		return new ArrayList<ArrayList<Integer>>(catMap.values()); 
	}
	
	
	public static double dunnIndex(double[][] distanceMatrix, String categoriesString) {
		ArrayList<ArrayList<Integer>> categories = getCategoriesList(categoriesString);
		//for (int i = 0; i< categories.size(); ++i) {
		//	System.out.println(categories.get(i).toString());
		//}
		
		return dunnIndex(distanceMatrix, categories);
	}
		
	public static double dunnIndex(double[][] distanceMatrix, ArrayList<ArrayList<Integer>> categories) {
		
		
		
		double maxInterCatDistance = 0; 
        for (int i = 0; i < categories.size(); ++i) {
			double currInterCatDistance = interCatDistance(distanceMatrix, categories.get(i));
			if (currInterCatDistance > maxInterCatDistance) {
				maxInterCatDistance = currInterCatDistance;
			}
		}
        
		double minIntraCatDistance = 10000; 
		for (int i = 0; i < categories.size()-1; ++i) {
			for (int j = i+1; j < categories.size(); ++j) {
				double currIntraCatDistance  = intraCatDistance(distanceMatrix, categories.get(i), categories.get(j) );
				if (currIntraCatDistance < minIntraCatDistance) {
					minIntraCatDistance = currIntraCatDistance;
				}
			}
		}
		//System.out.println(minIntraCatDistance/maxInterCatDistance);
		return minIntraCatDistance/maxInterCatDistance;
	}
}
