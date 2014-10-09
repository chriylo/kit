package clusterFunctions;

import java.util.*;

public class ClusterScore {
	public HashMap<String,Double> score;
	
	public ClusterScore() {
		score = new HashMap<String, Double>();
	}
	
	public void add(String category, Double categoryScore) {
		score.put(category, categoryScore);
	}
	
	public Double get(String category) {
		return score.get(category);
	}
	
	public Set<String> getCategorySet() {
		return score.keySet();
	}
	
	public ArrayList<String> getMinCategories() {
		Double min = this.getMinCategoryScore();
		ArrayList<String> minCategories = new ArrayList<String>();
		for (Iterator it = this.getCategorySet().iterator(); it.hasNext(); ) {
			String key = (String) it.next();
			if (min == score.get(key)) {
				minCategories.add(key);
			}
		}
		return minCategories;
	}
	
	public ArrayList<String> getMaxCategories() {
		Double max = this.getMaxCategoryScore();
		ArrayList<String> maxCategories = new ArrayList<String>();
		for (Iterator it = this.getCategorySet().iterator(); it.hasNext(); ) {
			String key = (String) it.next();
			if (max == score.get(key)) {
				maxCategories.add(key);
			}
		}
		return maxCategories;
	}
	
	public Double getMinCategoryScore() {
		Double min = Double.MAX_VALUE;
		for (Iterator it = this.getCategorySet().iterator(); it.hasNext(); ) {
			String key = (String) it.next();
			if (min > score.get(key)) {
				min = score.get(key);
			}
		}
		return min;
	}
	
	public Double getMaxCategoryScore() {
		Double max = Double.MIN_VALUE;
		for (Iterator it = this.getCategorySet().iterator(); it.hasNext(); ) {
			String key = (String) it.next();
			if (max < score.get(key)) {
				max = score.get(key);
			}
		}
		return max;
	}
}
