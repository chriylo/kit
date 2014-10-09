/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clusterFunctions;

import java.util.*;

import distanceFunction.EntryWeights;
import distanceFunction.EntrywiseVecDistance;
import distanceFunction.RatioPowerVecDistance;
import distanceFunction.RatioVecDistance;


import barcodeFunctions.BarcodeFunctions;


/**
 *
 * @author rachmani
 */
public class Cluster {

    private String representativePartition = "";
	//private String[] representativePartition;
    private String[] partition;
    private ArrayList<BarcodeInCluster> barcodes;
    private ArrayList<String> categories;
    //private HashMap<String, Integer> categoriesIndex;
    private HashMap<String, Integer> categoriesOrigIndex;
    private int typingScore;
    

    public Cluster() {
    	this.barcodes= new ArrayList<BarcodeInCluster>();
    }
    
    public Cluster(BarcodeInCluster barcode) {
        this.barcodes = new ArrayList<BarcodeInCluster>();
        this.barcodes.add(barcode);
    }

    public void addBarcode(BarcodeInCluster barcode) {
        this.barcodes.add(barcode);
    }

    public ArrayList<BarcodeInCluster> getBarcodesOfThisCluster() {
        return this.barcodes;
    }
    
    //public void assignKey(String[] key) {
    public void assignKey(String key) {
        this.setRepresentativePartition(key);
        setCategories();
    }
    
    public void assignKey(int length) {
    	int[] barcode = new int[length];
    	for (int i = 0; i < length; ++i) {
    		barcode[i] = i;
    	}
    	BarcodeInCluster bc = new BarcodeInCluster(barcode, -1, 0);
    	this.assignKey(bc.generateKey());
    }
    
    //TODO
    //get informative score for each k-mer of cluster 
  	public double[] getInformativeScore() {
  		//PoissonInfoScoreCalculator pCal = new PoissonInfoScoreCalculator();
      	//double [] clusterInfoScore = pCal.getInfoScoreForBarcodesInCluster(myCluster);
          //double [] normClusterInfoScore = new double[clusterInfoScore.length];
  		double [] clusterInfoScore = new double[this.getBarcodesOfThisCluster().size()];
      	for (int i = 0; i < clusterInfoScore.length; ++ i) {
      		clusterInfoScore[i] = 1;            
      		//normClusterInfoScore[i] = clusterInfoScore[i]*clusterInfoScore.length/BarcodeComparator.getSum(clusterInfoScore);
          }
      	return clusterInfoScore;
  	}
  	
  	
  //Get index of k-mers in cluster
  	public List<Integer> getClusterBarcodeIndices(){
  		ArrayList<BarcodeInCluster> myClusterBarcodes = this.getBarcodesOfThisCluster();
      	List<Integer> myClusterBarcodesIndices = new ArrayList<Integer>();
      	for (int i = 0; i < myClusterBarcodes.size(); ++i) {
      		myClusterBarcodesIndices.add(myClusterBarcodes.get(i).getKmerID());
      	}
      	return myClusterBarcodesIndices;
  	}
  	
  	
  	/*
  	public int getNumTemplates() {
  		if (barcodes.isEmpty()) {
  			return 0;
  		} else {
  			return barcodes.get(0).getBarcode().length;
  		}
  	}
	*/
  	
  	public HashMap<String, int[]> getCategoryBarcodes() {
  		HashMap<String, int[]> categoryBarcodes = new HashMap<String, int[]>();
  		int numKmers = this.barcodes.size();
  		for (int i = 0; i < categories.size(); ++i) {
  			String cat = categories.get(i);
  			int origCatIndex = categoriesOrigIndex.get(cat);
  			int[] categoryBarcode = new int[numKmers];
  			for (int j = 0; j < numKmers; ++j) {
  				categoryBarcode[j] = barcodes.get(j).getBarcode()[origCatIndex];
  			}
  			categoryBarcodes.put(cat, categoryBarcode);
  		}
  		return categoryBarcodes;
  	}
  	
  	
  	
  	public ClusterScore getClusterScore(int[] sample) {
  		ClusterScore clusterScore = new ClusterScore();
		//HashMap<String, Double> clusterScore = new HashMap<String, Double>();
		
		double sumSample = BarcodeFunctions.getSum(sample); // OLD
		double[] scaledSample = BarcodeFunctions.scaleBarcode(sample, 100000, sumSample); //OLD
		//double[] scaledSample = BarcodeFunctions.scaleBarcode(sample, 1,1);
		
		EntrywiseVecDistance ratioDistance = new RatioPowerVecDistance(); // OLD
  		//RatioVecDistance ratioDistance = new RatioVecDistance(); // NEW
		EntryWeights myEntryWeights = new EntryWeights(this.getInformativeScore());
		ratioDistance.setWeights(myEntryWeights);

		
		HashMap<String, int[]> categoryBarcodes = this.getCategoryBarcodes();
		for (Iterator<String> it = categoryBarcodes.keySet().iterator(); it.hasNext();) {
			String key = it.next();
			int[] categoryBarcode = categoryBarcodes.get(key);
			double sumCategoryBarcode = BarcodeFunctions.getSum(categoryBarcode); // OLD
			double[] scaledCategoryBarcode = BarcodeFunctions.scaleBarcode(categoryBarcode, 100000, sumCategoryBarcode); // OLD
			//double[] scaledCategoryBarcode = BarcodeFunctions.scaleBarcode(sample, 1,1);
			double score = ratioDistance.distance(scaledSample, scaledCategoryBarcode); // OLD
			//ratioDistance.setFactor(categoryBarcode, sample); // NEW
			//double score = ratioDistance.distance(categoryBarcode, sample); // NEW
			clusterScore.add(key, score);
		}
		return clusterScore;

    }
  	
  	/*
  	//Get barcodes of categories
  	public int[][] getCategoryBarcodes() {
  		//int numTemplates = getNumTemplates();
  		int[][] categoryBarcodesArray = new int[categories.size()][this.barcodes.size()];
  		
      	for (int i = 0; i < categories.size(); ++i) {
      		int origCatIndex = categoriesOrigIndex.get(categories.get(i));
      		for (int j = 0; j < this.barcodes.size(); ++j) {
      			categoryBarcodesArray[i][j] = barcodes.get(j).getBarcode()[origCatIndex];
      		}
      	}
      	
      	return categoryBarcodesArray;
  	}
    */
    
  	public int getTypingScore() {
  		return this.typingScore;
  	}
  	
  	public void setTypingScore(String[] templateTypes) {
  		if (templateTypes == null) {
  			this.typingScore = 0;
  		} else {
  		
  			//assert templateTypes.length == this.representativePartition.length() : "Inconsistent number of templates";
  			assert templateTypes.length == this.partition.length : "Inconsistent number of templates";
  		
  			HashMap<String, String> typeCategory = new HashMap<String, String>();
  			for (int i = 0; i < templateTypes.length; ++i) {
  				String type = templateTypes[i];
  				//String cat = String.valueOf(this.representativePartition.charAt(i));
  				String cat = String.valueOf(this.partition[i]);
  				if (typeCategory.containsKey(type)) {
  					if (!cat.equals(typeCategory.get(type))) {
  						typeCategory.put(type, "-1");
  					}
  				} else {
  					typeCategory.put(type, cat);
  				}
  			}
  			int score = 0;
  			for (Iterator it = typeCategory.keySet().iterator(); it.hasNext(); ) {
  				String key = (String) it.next();
  				if (typeCategory.get(key).equals("-1")) {
  					score += 1;
  				}
  			}
  			this.typingScore = score;
  		}
  	}
  	
  	public double averageCorrBarcodes() {
  		HashMap<String, int[]> categoryBarcodes = getCategoryBarcodes();
    	double sumCorrBarcodes = 0;
    	double numPairs = 0;
    	String[] keys = new String[categoryBarcodes.size()];
    	int currIndx = 0;
    	for (Iterator it = categoryBarcodes.keySet().iterator(); it.hasNext();) {
    		keys[currIndx] = (String) it.next();
    		currIndx += 1;
    	}
		for (int i = 0; i < keys.length-1; ++i) {
			//double[] subsetBarcodeA = BarcodeFunctions.toDoubleArray(BarcodeComparator.getSubsetBarcode(barcodes[i], myClusterBarcodesIndices));
			for (int j = i+1; j < keys.length; ++j) {
				//double[] subsetBarcodeB = BarcodeFunctions.toDoubleArray(BarcodeComparator.getSubsetBarcode(barcodes[j], myClusterBarcodesIndices));
				//System.out.println();
				//System.out.println("A: " + Arrays.toString(subsetBarcodeA));
				//System.out.println("B: " + Arrays.toString(subsetBarcodeB));
				//System.out.println(correlation(subsetBarcodeA, subsetBarcodeB));
				//if (correlation(subsetBarcodeA, subsetBarcodeB)>0.99) {
				//	perfCorr = Boolean.TRUE;
				//}
				double corr = BarcodeFunctions.correlation(BarcodeFunctions.toDoubleArray(categoryBarcodes.get(keys[i])), BarcodeFunctions.toDoubleArray(categoryBarcodes.get(keys[j])));
				if (!Double.isNaN(corr)) {
					sumCorrBarcodes += corr;
					numPairs += 1;
				}
			}
		}
		if (numPairs > 0) {
			return sumCorrBarcodes/numPairs;
		}
		else {
			return 1.1;
		}
    }
  	
  
  	
    /**
     * @return the representativePartition
     */
    public String getRepresentativePartition() {
    //public String[] getRepresentativePartition() { 
  		return representativePartition;
    }
    
    
    public ArrayList<String> getCategories() {
  		return categories;
  	}
  	
    
    /*
    public HashMap<String, Integer> getCategoriesIndex() {
    	return categoriesIndex;
    }
    */
    

    
    public HashMap<String, Integer> getCategoriesOrigIndex() {
    	return categoriesOrigIndex;
    }
    
    
    /**
     * @param representativePartition the representativePartition to set
     */
    
    private void setRepresentativePartition(String representativePartition) {
    //private void setRepresentativePartition(String[] representativePartition) {
        this.representativePartition = representativePartition;
        this.partition = representativePartition.split(",");
        
    }
    
    public String[] getPartition() {
    	return this.partition;
    }
    
    
    //Category's index to Category's ID
  	private void setCategories() {
  		//String clusterCategories = this.getRepresentativePartition();
  		String[] clusterCategories = this.getPartition();
  		
  		ArrayList<String> categories = new ArrayList<String>();
  		HashMap<String, Integer> categoriesOrigIndex = new HashMap<String, Integer>();
  		//HashMap catIndx = new HashMap<String, Integer>();
  		//int currIndx = 0;
  		//for (int i = 0; i < clusterCategories.length(); ++i) {
  		for (int i = 0; i < clusterCategories.length; ++i) {
  			String cat = String.valueOf(clusterCategories[i]); 
      		//String cat = String.valueOf(clusterCategories.charAt(i)); 
      		if (!categories.contains(cat)) {
      			categories.add(cat);
      			categoriesOrigIndex.put(cat, i);
      		}
      		/*
      		if (!catIndx.containsKey(cat)) {
      			catIndx.put(cat, currIndx);
      			currIndx += 1;
      		}
      		*/
      		
      	}
  		this.categories = categories;
  		this.categoriesOrigIndex = categoriesOrigIndex;
  		//this.categoriesIndex = catIndx; 

  		

  	}
  	
    
    
}
