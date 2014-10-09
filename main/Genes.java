package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class Genes {
	
	public static ArrayList<ArrayList<String>> geneTests = new ArrayList<ArrayList<String>>();
	public static HashMap<String, int[]> typeCopyNumbers;
	public static HashMap<String, int[]> diploidTypeCopyNumbers;
	public static HashMap<String, String> copyNumbersToType;
	public static HashMap<String, String> typeToCopyNumbers;
	public static HashMap<String, String> diploidCopyNumbersToType;
	public static HashMap<String, String> diploidTypeToCopyNumbers;
	
	static {
		typeCopyNumbers = new HashMap<String, int[]>();
		typeCopyNumbers.put("A", new int[] 	{1,0,1,0,0,0,1,1,1,1,0,1,0,1,1});
		typeCopyNumbers.put("AB",new int[] 	{1,0,1,0,1,1,1,1,1,1,1,0,1,0,1});
		typeCopyNumbers.put("BA1",new int[] {1,1,0,1,0,0,0,0,1,1,0,1,0,1,1});
		typeCopyNumbers.put("BA2X",new int[]{1,0,1,0,1,1,1,1,1,1,0,1,0,1,1});
		typeCopyNumbers.put("BA2",new int[] {1,1,0,1,1,1,1,1,1,1,0,1,0,1,1});
		typeCopyNumbers.put("Bdel",new int[]{1,1,0,1,1,1,0,0,1,1,1,0,1,0,1});
		typeCopyNumbers.put("B",new int[] 	{1,1,0,1,2,2,1,1,1,1,1,0,1,0,1});
//		typeCopyNumbers.put("A", new int[] {1,0,1,0,0,0,1,1,1,1,0,1,0,1});
//		typeCopyNumbers.put("AB",new int[] {1,0,1,0,1,1,1,1,1,1,1,0,1,0});
//		typeCopyNumbers.put("BA1",new int[] {1,1,0,1,0,0,0,0,1,1,0,1,0,1});
//		typeCopyNumbers.put("BA2X",new int[] {1,0,1,0,1,1,1,1,1,1,0,1,0,1});
//		typeCopyNumbers.put("BA2",new int[] {1,1,0,1,1,1,1,1,1,1,0,1,0,1});
//		typeCopyNumbers.put("Bdel",new int[] {1,1,0,1,1,1,0,0,1,1,1,0,1,0});
//		typeCopyNumbers.put("B",new int[] {1,1,0,1,2,2,1,1,1,1,1,0,1,0});
		
		ArrayList<String> geneNames = new ArrayList<String>(); 
		geneNames.add("KIR3DL3");
		geneTests.add(geneNames);

		geneNames = new ArrayList<String>(); 
		geneNames.add("KIR2DS2");
		geneTests.add(geneNames);

		geneNames = new ArrayList<String>(); 
		geneNames.add("KIR2DL3"); 	//geneNames.add("KIR2DL2"); 
		geneTests.add(geneNames);
		
		geneNames = new ArrayList<String>(); 
		geneNames.add("KIR2DL2"); 
		geneTests.add(geneNames);

		geneNames = new ArrayList<String>(); 
		geneNames.add("KIR2DL5B"); geneNames.add("KIR2DL5A");
		geneTests.add(geneNames);

		geneNames = new ArrayList<String>(); 
		geneNames.add("KIR2DS3B"); geneNames.add("KIR2DS3A");		geneNames.add("KIR2DS5A"); geneNames.add("KIR2DS5B");
		geneTests.add(geneNames);

		geneNames = new ArrayList<String>(); 
		geneNames.add("KIR2DP1");
		geneTests.add(geneNames);

		geneNames = new ArrayList<String>(); 
		geneNames.add("KIR2DL1");
		geneTests.add(geneNames);

		geneNames = new ArrayList<String>(); 
		geneNames.add("KIR3DP1");
		geneTests.add(geneNames);

		geneNames = new ArrayList<String>(); 
		geneNames.add("KIR2DL4");
		geneTests.add(geneNames);

		geneNames = new ArrayList<String>(); 
		geneNames.add("KIR3DS1"); 		//geneNames.add("KIR3DL1");
		geneTests.add(geneNames);
		
		geneNames = new ArrayList<String>(); 
		geneNames.add("KIR3DL1");
		geneTests.add(geneNames);

		geneNames = new ArrayList<String>(); 
		geneNames.add("KIR2DS1");
		geneTests.add(geneNames);

		geneNames = new ArrayList<String>(); 
		geneNames.add("KIR2DS4");
		geneTests.add(geneNames);

		geneNames = new ArrayList<String>(); 
		geneNames.add("KIR3DL2");
		geneTests.add(geneNames);
		
		generateDiploid();
	}
	private static void generateDiploid() {
		diploidTypeCopyNumbers = new HashMap<String, int[]>();
		for (Iterator typeIt = typeCopyNumbers.keySet().iterator(); typeIt.hasNext(); ) {
			String type1 = (String) typeIt.next();
			for (Iterator typeIt2 = typeCopyNumbers.keySet().iterator(); typeIt2.hasNext(); ) {
				String type2 = (String) typeIt2.next();
				String[] temp = new String[2];
				temp[0] = type1;
				temp[1] = type2;
				Arrays.sort(temp);
				int[] cn = new int[typeCopyNumbers.get(type1).length];
				for (int i = 0; i < cn.length; ++i) {
					cn[i] = typeCopyNumbers.get(type1)[i] + typeCopyNumbers.get(type2)[i];
				}
				diploidTypeCopyNumbers.put(temp[0] + "_" + temp[1], cn);
			}
		}
		
		
		typeToCopyNumbers = new HashMap<String, String>();
		for (Iterator typeIt = typeCopyNumbers.keySet().iterator(); typeIt.hasNext(); ) {
			String type = (String) typeIt.next();
			int[] cn = typeCopyNumbers.get(type);
			String temp = Integer.toString(cn[0]);
			for (int i = 1; i < cn.length; ++i) {
				temp = temp + "," + Integer.toString(cn[i]); 
			}
			typeToCopyNumbers.put(type, temp);
		}
		
		copyNumbersToType = new HashMap<String, String>();
		for (Iterator it = typeToCopyNumbers.keySet().iterator(); it.hasNext();) {
			String key = (String) it.next();
			copyNumbersToType.put(typeToCopyNumbers.get(key), key);
		}
		
		diploidTypeToCopyNumbers = new HashMap<String, String>();
		for (Iterator typeIt = diploidTypeCopyNumbers.keySet().iterator(); typeIt.hasNext(); ) {
			String type = (String) typeIt.next();
			int[] cn = diploidTypeCopyNumbers.get(type);
			String temp = Integer.toString(cn[0]);
			for (int i = 1; i < cn.length; ++i) {
				temp = temp + "," + Integer.toString(cn[i]); 
			}
			diploidTypeToCopyNumbers.put(type, temp);
		}
		
		
		diploidCopyNumbersToType = new HashMap<String, String>();
		for (Iterator it = diploidTypeToCopyNumbers.keySet().iterator(); it.hasNext();) {
			String key = (String) it.next();
			diploidCopyNumbersToType.put(diploidTypeToCopyNumbers.get(key), key);
		}
	}
	
	public static HashMap<String, String> getCopyNumbersToType() {
		return copyNumbersToType;
	}
	
	public static HashMap<String, int[]> getTypeCopyNumbers() {
		return typeCopyNumbers;
	}
	
	public static HashMap<String, int[]> getDiploidTypeCopyNumbers() {
		return diploidTypeCopyNumbers;
	}
	
	public static HashMap<String, String> getTypeToCopyNumbers() {
		return typeToCopyNumbers;
	}
	
	public static HashMap<String, String> getDiploidCopyNumbersToType() {
		return diploidCopyNumbersToType;
	}
	
	public static HashMap<String, String> getDiploidTypeToCopyNumbers() {
		return diploidTypeToCopyNumbers;
	}
	
	public static ArrayList<ArrayList<String>> getGeneTests() {
		return geneTests;
	}

}
