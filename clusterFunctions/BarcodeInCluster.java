/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clusterFunctions;

import java.util.HashMap;

/**
 *
 * @author rachmani
 */
public class BarcodeInCluster {
	
    private static final HashMap<Integer, Integer> auxMap = new HashMap<Integer, Integer>();


    private int[] barcode;
    private int kmerID;
    private double informativeness;
    private int[] partition;

    public BarcodeInCluster(int[] barcode, int kmerID, double informativeness) {
        setBarcode(barcode);
        this.kmerID = kmerID;
        this.informativeness = informativeness;
    }

    private static int[] getPartition(int[] barcode) {
    	int[] partition = new int[barcode.length];
    	int numOfGroups = 0;
    	auxMap.clear();
        for (int i = 0; i < barcode.length; i++) {
            Integer groupIx = auxMap.get(barcode[i]);
            if (groupIx == null){
            	groupIx = numOfGroups;
            	auxMap.put(barcode[i], groupIx);
            	++numOfGroups;
            }
            partition[i] = groupIx;
        }
        return partition;
	}

	/**
     * @return the barcode
     */
    public int[] getBarcode() {
        return barcode;
    }
    
    

    /**
     * @param barcode the barcode to set
     */
    public void setBarcode(int[] barcode) {
        this.barcode = barcode;
        partition = getPartition(barcode);
    }

    /**
     * @return the kmerID
     */
    public int getKmerID() {
        return kmerID;
    }

    /**
     * @param kmerID the kmerID to set
     */
    public void setKmerID(int kmerID) {
        this.kmerID = kmerID;
    }

    /**
     * @return the informativeness
     */
    public double getInformativeness() {
        return informativeness;
    }

    /**
     * @param informativeness the informativeness to set
     */
    public void setInformativeness(double informativeness) {
        this.informativeness = informativeness;
    }

    
    /*
    public String generateKey() {
        int currentassign = 65;
        HashMap seen = new HashMap();
        int[] converted = new int[this.barcode.length];
        for (int i = 0; i < this.barcode.length; i++) {
            int x = this.barcode[i];
            if (seen.containsKey(x)) {
                int assigned = ((Integer) seen.get(x)).intValue();
            	//int assigned = (int) seen.get(x);
                converted[i] = assigned;
            } else {
                seen.put(x, currentassign);
                converted[i] = currentassign;
                currentassign += 1;
            }
        }
        StringBuilder s = new StringBuilder("");
        for (int i = 0; i < converted.length; i++) {
            s.append((char)converted[i]);            
        }
        return s.toString();
    }*/
    
    public String generateKey(int factor) {
    	int currentassign = 65; 
    	HashMap seen = new HashMap();
    	int[] converted = new int[this.barcode.length];
    	for (int i = 0; i < this.barcode.length; ++i) {
    		int x = this.barcode[i]/factor;
    		if (seen.containsKey(x)) {
                int assigned = ((Integer) seen.get(x)).intValue();
            	//int assigned = (int) seen.get(x);
                converted[i] = assigned;
            } else {
                seen.put(x, currentassign);
                converted[i] = currentassign;
                currentassign += 1;
            }
        }
       
        StringBuilder s = new StringBuilder("");
        for (int i = 0; i < converted.length; i++) {
            s.append((char)converted[i]);
            if (i < converted.length -1) {
            	s.append(",");
            }
        }
        return s.toString();
    		
   
    }
    
    public String generateKey() {
    //public String[] generateKey() {
        int currentassign = 65;
        HashMap seen = new HashMap();
        int[] converted = new int[this.barcode.length];
        for (int i = 0; i < this.barcode.length; i++) {
            int x = this.barcode[i];
            if (seen.containsKey(x)) {
                int assigned = ((Integer) seen.get(x)).intValue();
            	//int assigned = (int) seen.get(x);
                converted[i] = assigned;
            } else {
                seen.put(x, currentassign);
                converted[i] = currentassign;
                currentassign += 1;
            }
        }
        /*
        String[] s = new String[converted.length];
        for (int i = 0; i < converted.length; ++i) {
        	s[i] = convert(converted[i]);
        }
        return s;
        */
        
        StringBuilder s = new StringBuilder("");
        for (int i = 0; i < converted.length; i++) {
            s.append((char)converted[i]);
            if (i < converted.length -1) {
            	s.append(",");
            }
        }
        return s.toString();
        
    }
    
    private String convert(int code) {
    	StringBuilder s = new StringBuilder("");
    	if (code > 90) {
    		s.append(convert(code-26));
    	} else {
    		s.append((char)code);
    	}
    	return s.toString();
    }
}


