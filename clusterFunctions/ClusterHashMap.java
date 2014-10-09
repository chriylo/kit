/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clusterFunctions;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import typing.*;

/**
 *
 * @author rachmani
 */
public class ClusterHashMap {

    private HashMap clusterMap;
    private String[] templateTypes;
    private int[][] templateBarcodes;

    public ClusterHashMap() {
        this.clusterMap = new HashMap<String, Cluster>();
    }
    
    public ClusterHashMap(int[][] templateBarcodes) {
    	initialize(templateBarcodes);
    }
    
    public ClusterHashMap(int[][] templateBarcodes, File[] templateFiles) {
    	this.templateTypes = Typing.getTypes(templateFiles);
    	initialize(templateBarcodes);
        

    }
    
    //TODO: clean up
    public ClusterHashMap(int[][] templateBarcodes, boolean diploid) {
    	initializeDiploid(templateBarcodes, diploid);
      
	}
    
    public ClusterHashMap(int[][] templateBarcodes, boolean diploid, File[] templateFiles) {
    	this.templateTypes = Typing.getDiploidTypes(templateFiles);
    	initializeDiploid(templateBarcodes, diploid);
    	
    }
    
    
    private void initialize(int[][] templateBarcodes) {
    	this.clusterMap = new HashMap<String, Cluster>();
        int barlen = templateBarcodes[0].length;
        int temlen = templateBarcodes.length;
        for (int i = 0; i < barlen; i++) {
            int[] column = new int[temlen];
            String columnString = "";
            for (int j = 0; j < temlen; j++) {
                column[j] = templateBarcodes[j][i];
                columnString += String.valueOf(templateBarcodes[j][i]) + " ";
            }
            double iscore = 0;
            BarcodeInCluster bc = new BarcodeInCluster(column, i, iscore);
            this.addBarcode(bc);
            if (i % 10000 == 0) {
                System.out.println("Processing " + i + "\t" + columnString.trim());
            }
        }
        this.templateBarcodes = templateBarcodes;
        //return cMap;
    }
    
    //templateBarcodes
    private void initializeDiploid(int[][] templateBarcodes, boolean diploid) {
    	this.clusterMap = new HashMap<String, Cluster>();
        int barlen = templateBarcodes[0].length;
        int temlen = templateBarcodes.length;
        int numDiploidTemplates = (((temlen*temlen)-temlen)/2)+temlen;
        int[][] diploidTemplateBarcodes = new int[numDiploidTemplates][barlen];
        
        for (int i = 0; i < barlen; i++) {
            int[] column = new int[numDiploidTemplates];
            String columnString = "";
            int currIndx = 0;
            for (int j = 0; j < temlen; j++) {
            	for (int k = j; k < temlen; k++) {
                column[currIndx] = templateBarcodes[j][i]+templateBarcodes[k][i];
                diploidTemplateBarcodes[currIndx][i] = column[currIndx];
                currIndx += 1;
                columnString += String.valueOf(templateBarcodes[j][i]+templateBarcodes[k][i]) + " ";
                
            	}
            }
            double iscore = 0;
            BarcodeInCluster bc = new BarcodeInCluster(column, i, iscore);
            this.addBarcode(bc);
            if (i % 10000 == 0) {
                System.out.println("Processing " + i + "\t" + columnString.trim());
            }
        
        }
        this.templateBarcodes = diploidTemplateBarcodes;
    }
    
    
    
    
    public String[] getTemplateTypes() {
    	return this.templateTypes;
    }

    private void addBarcode(BarcodeInCluster barcode) {
        //String[] key = barcode.generateKey();
    	String key = barcode.generateKey();
        if (this.clusterMap.containsKey(key)) {
            Cluster c = (Cluster) this.clusterMap.get(key);
            c.addBarcode(barcode);
//            System.out.println("Found same cluster: " + c.getRepresentativePartition());
        } else {
            Cluster c = new Cluster(barcode);
            c.assignKey(key);
            c.setTypingScore(templateTypes);
            this.clusterMap.put(key, c);
//            System.out.println("Creating new cluster:" + key);
        }
    }
    
    
    
    public HashMap getMap() {
        return this.clusterMap;
    }
    
  
    
    private ClusterList getClusters(int maxNumCat, double maxCorrelation, int minNumKmers, int maxTypingScore) {
		//ArrayList<Cluster> clusters = new ArrayList<Cluster>();
    	ClusterList clusters = new ClusterList(this.templateBarcodes);
		for (Iterator it = this.getMap().keySet().iterator(); it.hasNext();) {
			Cluster myCluster = (Cluster) this.getMap().get(it.next());
			//for (int i = 0; i < cMap.getMap().keySet().size(); ++i) {
			//Cluster myCluster = (Cluster) cMap.getMap().get((String) cMap.getMap().keySet().toArray()[i]);
			if ((myCluster.getCategories().size()<=maxNumCat) && (myCluster.averageCorrBarcodes() <= maxCorrelation) && (myCluster.getBarcodesOfThisCluster().size() >= minNumKmers) && (myCluster.getTypingScore() <= maxTypingScore) ) {
				clusters.add(myCluster);
			}
		}
		return clusters;
	}
	
	public ClusterList getClusters() {		
		return getClusters(Integer.MAX_VALUE, Double.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}
    
	public int[][] getTemplateBarcodes() {
		return this.templateBarcodes;
	}
	
	
   
    
    
	
}
