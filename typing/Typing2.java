package typing;

import barcodeFunctions.*;
import clusterFunctions.*;
import distanceFunction.*;
import typing.TemplateScores;

import java.io.File;
import java.util.*;


public class Typing2 {
	
	public static final HashMap<String, String> templateTypes = new HashMap<String, String> ();
	public static final HashMap<String, String> templateFastaFileNames = new HashMap<String, String>();
	private static HashMap<String, int[]> typeCopyNumbers;
	private static HashMap<String, int[]> diploidTypeCopyNumbers;
	private static HashMap<String, String> copyNumbersToType;
	private static HashMap<String, String> typeToCopyNumbers;
	private static HashMap<String, String> diploidCopyNumbersToType;
	private static HashMap<String, String> diploidTypeToCopyNumbers;
	private static ArrayList<Integer> geneIndexFramework = new ArrayList<Integer>();
	
	public static final ArrayList<ArrayList<String>> geneTests = new ArrayList<ArrayList<String>>();

	public static final HashMap<String, HashMap<String, int[]>> templateRegions = new HashMap<String, HashMap<String, int[]>>();

	public static final ArrayList<String> templates = new ArrayList<String>();
	
	
	static{
		
		templates.add("ABC08A");
		templates.add("FH05A");
		templates.add("FH06A");
		templates.add("FH08A");
		templates.add("FH13A");
		templates.add("FH13A");
		templates.add("FH15A");
		templates.add("G085A");
		templates.add("G248A");
		templates.add("LUCEA");
		templates.add("RSHA");
		templates.add("T7526A");
		templates.add("GRC212AB");
		templates.add("FH06BA1");
		templates.add("G085BA1");
		templates.add("GRC212BA1");
		templates.add("FH08BA2X");
		templates.add("FH13BA2");
		templates.add("G248BA2");
		templates.add("RSHBA2");
		templates.add("LUCEBdel");
		templates.add("T7526Bdel");
		templates.add("FH05B");
		templates.add("FH15B");
		templates.add("WT47B");
		
		
		templateTypes.put("ABC08A", "A");
		templateTypes.put("FH05A", "A");
		templateTypes.put("FH06A", "A");
		templateTypes.put("FH08A", "A");
		templateTypes.put("FH13A", "A");
		templateTypes.put("FH13A", "A");
		templateTypes.put("FH15A", "A");
		templateTypes.put("G085A", "A");
		templateTypes.put("G248A", "A");
		templateTypes.put("LUCEA", "A");
		templateTypes.put("RSHA", "A");
		templateTypes.put("T7526A", "A");
		templateTypes.put("GRC212AB", "AB");
		templateTypes.put("FH06BA1", "BA1");
		templateTypes.put("G085BA1", "BA1");
		templateTypes.put("GRC212BA1", "BA1");
		templateTypes.put("FH08BA2X", "BA2X");
		templateTypes.put("FH13BA2", "BA2");
		templateTypes.put("G248BA2", "BA2");
		templateTypes.put("RSHBA2", "BA2");
		templateTypes.put("LUCEBdel", "Bdel");
		templateTypes.put("T7526Bdel", "Bdel");
		templateTypes.put("FH05B", "B");
		templateTypes.put("FH15B", "B");
		templateTypes.put("WT47B", "B");
		
		templateFastaFileNames.put("ABC08A", "ABC08A.masked.fa");
		templateFastaFileNames.put("G085A", "G085A.masked.fa");
		templateFastaFileNames.put("RSHA", "RSHA.masked.fa");
		templateFastaFileNames.put("FH06A", "FH06A.masked.fa");
		templateFastaFileNames.put("FH08A", "FH08A.masked.fa");
		templateFastaFileNames.put("LUCEA", "LUCEA.masked.fa");
		templateFastaFileNames.put("FH13A", "FH13A.masked.fa");
		templateFastaFileNames.put("G248A", "G248A.masked.fa");
		templateFastaFileNames.put("T7526A", "T7526A.masked.fa");
		templateFastaFileNames.put("FH05A", "FH05A.masked.fa");
		templateFastaFileNames.put("FH15A", "FH15A.masked.fa");
		templateFastaFileNames.put("GRC212AB", "GRC212AB.masked.fa");
		templateFastaFileNames.put("G085BA1", "G085BA1.masked.fa");
		templateFastaFileNames.put("FH06BA1", "FH06BA1.masked.fa");
		templateFastaFileNames.put("GRC212BA1", "GRC212BA1.masked.fa");
		templateFastaFileNames.put("FH08BA2X", "FH08BA2X.masked.fa");
		templateFastaFileNames.put("FH13BA2", "FH13BA2.masked.fa");
		templateFastaFileNames.put("RSHBA2", "RSHBA2.masked.fa");
		templateFastaFileNames.put("G248BA2", "G248BA2.masked.fa");
		templateFastaFileNames.put("LUCEBdel", "LUCEBdel.masked.fa");
		templateFastaFileNames.put("T7526Bdel", "T7526Bdel.masked.fa");
		templateFastaFileNames.put("FH05B", "FH05B.masked.fa");
		templateFastaFileNames.put("FH15B", "FH15B.masked.fa");
		templateFastaFileNames.put("WT47B", "WT47B.masked.fa");
		
		typeCopyNumbers = new HashMap<String, int[]>();
//		//-8,15
//		typeCopyNumbers.put("A", new int[] {1,0,1,0,0,0,1,1,1,0,1,0,1});
//		typeCopyNumbers.put("AB",new int[] {1,0,1,0,1,1,1,1,1,1,0,1,0});
//		typeCopyNumbers.put("BA1",new int[] {1,1,0,1,0,0,0,1,1,0,1,0,1});
//		typeCopyNumbers.put("BA2X",new int[] {1,0,1,0,1,1,1,1,1,0,1,0,1});
//		typeCopyNumbers.put("BA2",new int[] {1,1,0,1,1,1,1,1,1,0,1,0,1});
//		typeCopyNumbers.put("Bdel",new int[] {1,1,0,1,1,1,0,1,1,1,0,1,0});
//		typeCopyNumbers.put("B",new int[] {1,1,0,1,2,2,1,1,1,1,0,1,0});
//		//-15
//		typeCopyNumbers.put("A", new int[] {1,0,1,0,0,0,1,1,1,1,0,1,0,1});
//		typeCopyNumbers.put("AB",new int[] {1,0,1,0,1,1,1,1,1,1,1,0,1,0});
//		typeCopyNumbers.put("BA1",new int[] {1,1,0,1,0,0,0,0,1,1,0,1,0,1});
//		typeCopyNumbers.put("BA2X",new int[] {1,0,1,0,1,1,1,1,1,1,0,1,0,1});
//		typeCopyNumbers.put("BA2",new int[] {1,1,0,1,1,1,1,1,1,1,0,1,0,1});
//		typeCopyNumbers.put("Bdel",new int[] {1,1,0,1,1,1,0,0,1,1,1,0,1,0});
//		typeCopyNumbers.put("B",new int[] {1,1,0,1,2,2,1,1,1,1,1,0,1,0});
		//all included
		typeCopyNumbers.put("A", new int[] 	{1,0,1,0,0,0,1,1,1,1,0,1,0,1,1});
		typeCopyNumbers.put("AB",new int[] 	{1,0,1,0,1,1,1,1,1,1,1,0,1,0,1});
		typeCopyNumbers.put("BA1",new int[] {1,1,0,1,0,0,0,0,1,1,0,1,0,1,1});
		typeCopyNumbers.put("BA2X",new int[]{1,0,1,0,1,1,1,1,1,1,0,1,0,1,1});
		typeCopyNumbers.put("BA2",new int[] {1,1,0,1,1,1,1,1,1,1,0,1,0,1,1});
		typeCopyNumbers.put("Bdel",new int[]{1,1,0,1,1,1,0,0,1,1,1,0,1,0,1});
		typeCopyNumbers.put("B",new int[] 	{1,1,0,1,2,2,1,1,1,1,1,0,1,0,1});
//		//-8
//		typeCopyNumbers.put("A", new int[] 	{1,0,1,0,0,0,1,1,1,0,1,0,1,1});
//		typeCopyNumbers.put("AB",new int[] 	{1,0,1,0,1,1,1,1,1,1,0,1,0,1});
//		typeCopyNumbers.put("BA1",new int[] {1,1,0,1,0,0,0,1,1,0,1,0,1,1});
//		typeCopyNumbers.put("BA2X",new int[]{1,0,1,0,1,1,1,1,1,0,1,0,1,1});
//		typeCopyNumbers.put("BA2",new int[] {1,1,0,1,1,1,1,1,1,0,1,0,1,1});
//		typeCopyNumbers.put("Bdel",new int[]{1,1,0,1,1,1,0,1,1,1,0,1,0,1});
//		typeCopyNumbers.put("B",new int[] 	{1,1,0,1,2,2,1,1,1,1,0,1,0,1});
		
		geneIndexFramework.add(0);
		geneIndexFramework.add(8);
		geneIndexFramework.add(9);
		geneIndexFramework.add(14);

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
		
		HashMap<String, int[]> ABC08A = new HashMap<String, int[]>();
		ABC08A.put("KIR3DL3", new int[] {0, 11557});
		ABC08A.put("1_intergenic", new int[] {11557, 14005});
		ABC08A.put("KIR2DL3", new int[] {14005, 27993});
		ABC08A.put("3_intergenic", new int[] {27993, 30473});
		ABC08A.put("KIR2DP1", new int[] {30473, 42821});
		ABC08A.put("5_intergenic", new int[] {42821, 45301});
		ABC08A.put("KIR2DL1", new int[] {45301, 59262});
		ABC08A.put("7_intergenic", new int[] {59262, 61804});
		ABC08A.put("KIR3DP1", new int[] {61804, 65767});
		ABC08A.put("9_intergenic", new int[] {65767, 79088});
		ABC08A.put("KIR2DL4", new int[] {79088, 89590});
		ABC08A.put("11_intergenic", new int[] {89590, 91981});
		ABC08A.put("KIR3DL1", new int[] {91981, 105758});
		ABC08A.put("13_intergenic", new int[] {105758, 108248});
		ABC08A.put("KIR2DS4", new int[] {108248, 123448});
		ABC08A.put("last_intergenic", new int[] {123448, 125979});
		ABC08A.put("KIR3DL2", new int[] {125979, 128047});
		templateRegions.put("ABC08A", ABC08A);

		HashMap<String, int[]> G085A = new HashMap<String, int[]>();
		G085A.put("KIR3DL3", new int[] {0, 11557});
		G085A.put("1_intergenic", new int[] {11557, 14005});
		G085A.put("KIR2DL3", new int[] {14005, 27983});
		G085A.put("3_intergenic", new int[] {27983, 30463});
		G085A.put("KIR2DP1", new int[] {30463, 42811});
		G085A.put("5_intergenic", new int[] {42811, 45291});
		G085A.put("KIR2DL1", new int[] {45291, 59253});
		G085A.put("7_intergenic", new int[] {59253, 61795});
		G085A.put("KIR3DP1", new int[] {61795, 65758});
		G085A.put("9_intergenic", new int[] {65758, 79081});
		G085A.put("KIR2DL4", new int[] {79081, 89581});
		G085A.put("11_intergenic", new int[] {89581, 91971});
		G085A.put("KIR3DL1", new int[] {91971, 105746});
		G085A.put("13_intergenic", new int[] {105746, 108236});
		G085A.put("KIR2DS4", new int[] {108236, 123413});
		G085A.put("last_intergenic", new int[] {123413, 125942});
		G085A.put("KIR3DL2", new int[] {125942, 142219});
		templateRegions.put("G085A", G085A);

		HashMap<String, int[]> RSHA = new HashMap<String, int[]>();
		RSHA.put("KIR3DL3", new int[] {0, 12895});
		RSHA.put("1_intergenic", new int[] {12895, 15342});
		RSHA.put("KIR2DL3", new int[] {15342, 29319});
		RSHA.put("3_intergenic", new int[] {29319, 31800});
		RSHA.put("KIR2DP1", new int[] {31800, 44148});
		RSHA.put("5_intergenic", new int[] {44148, 46628});
		RSHA.put("KIR2DL1", new int[] {46628, 60590});
		RSHA.put("7_intergenic", new int[] {60590, 63132});
		RSHA.put("KIR3DP1", new int[] {63132, 67095});
		RSHA.put("9_intergenic", new int[] {67095, 80418});
		RSHA.put("KIR2DL4", new int[] {80418, 90920});
		RSHA.put("11_intergenic", new int[] {90920, 93311});
		RSHA.put("KIR3DL1", new int[] {93311, 107088});
		RSHA.put("13_intergenic", new int[] {107088, 109578});
		RSHA.put("KIR2DS4", new int[] {109578, 124779});
		RSHA.put("last_intergenic", new int[] {124779, 127311});
		RSHA.put("KIR3DL2", new int[] {127311, 143566});
		templateRegions.put("RSHA", RSHA);

		HashMap<String, int[]> FH06A = new HashMap<String, int[]>();
		FH06A.put("KIR3DL3", new int[] {0, 11557});
		FH06A.put("1_intergenic", new int[] {11557, 14005});
		FH06A.put("KIR2DL3", new int[] {14005, 27981});
		FH06A.put("3_intergenic", new int[] {27981, 30461});
		FH06A.put("KIR2DP1", new int[] {30461, 42809});
		FH06A.put("5_intergenic", new int[] {42809, 45289});
		FH06A.put("KIR2DL1", new int[] {45289, 59251});
		FH06A.put("7_intergenic", new int[] {59251, 61793});
		FH06A.put("KIR3DP1", new int[] {61793, 65756});
		FH06A.put("9_intergenic", new int[] {65756, 79075});
		FH06A.put("KIR2DL4", new int[] {79075, 89577});
		FH06A.put("11_intergenic", new int[] {89577, 91968});
		FH06A.put("KIR3DL1", new int[] {91968, 105745});
		FH06A.put("13_intergenic", new int[] {105745, 108235});
		FH06A.put("KIR2DS4", new int[] {108235, 123410});
		FH06A.put("last_intergenic", new int[] {123410, 125944});
		FH06A.put("KIR3DL2", new int[] {125944, 142205});
		templateRegions.put("FH06A", FH06A);

		HashMap<String, int[]> FH08A = new HashMap<String, int[]>();
		FH08A.put("KIR3DL3", new int[] {0, 10272});
		FH08A.put("1_intergenic", new int[] {10272, 12720});
		FH08A.put("KIR2DL3", new int[] {12720, 26719});
		FH08A.put("3_intergenic", new int[] {26719, 29199});
		FH08A.put("KIR2DP1", new int[] {29199, 41544});
		FH08A.put("5_intergenic", new int[] {41544, 44027});
		FH08A.put("KIR2DL1", new int[] {44027, 57988});
		FH08A.put("7_intergenic", new int[] {57988, 60530});
		FH08A.put("KIR3DP1", new int[] {60530, 64493});
		FH08A.put("9_intergenic", new int[] {64493, 77815});
		FH08A.put("KIR2DL4", new int[] {77815, 88317});
		FH08A.put("11_intergenic", new int[] {88317, 90708});
		FH08A.put("KIR3DL1", new int[] {90708, 104486});
		FH08A.put("13_intergenic", new int[] {104486, 108314});
		FH08A.put("KIR2DS4", new int[] {108314, 123514});
		FH08A.put("last_intergenic", new int[] {123514, 126046});
		FH08A.put("KIR3DL2", new int[] {126046, 142287});
		templateRegions.put("FH08A", FH08A);

		HashMap<String, int[]> LUCEA = new HashMap<String, int[]>();
		LUCEA.put("KIR3DL3", new int[] {0, 11597});
		LUCEA.put("1_intergenic", new int[] {11597, 14046});
		LUCEA.put("KIR2DL3", new int[] {14046, 28022});
		LUCEA.put("3_intergenic", new int[] {28022, 30502});
		LUCEA.put("KIR2DP1", new int[] {30502, 42850});
		LUCEA.put("5_intergenic", new int[] {42850, 45330});
		LUCEA.put("KIR2DL1", new int[] {45330, 59292});
		LUCEA.put("7_intergenic", new int[] {59292, 61834});
		LUCEA.put("KIR3DP1", new int[] {61834, 65797});
		LUCEA.put("9_intergenic", new int[] {65797, 79119});
		LUCEA.put("KIR2DL4", new int[] {79119, 89621});
		LUCEA.put("11_intergenic", new int[] {89621, 92012});
		LUCEA.put("KIR3DL1", new int[] {92012, 105789});
		LUCEA.put("13_intergenic", new int[] {105789, 108279});
		LUCEA.put("KIR2DS4", new int[] {108279, 123479});
		LUCEA.put("last_intergenic", new int[] {123479, 126011});
		LUCEA.put("KIR3DL2", new int[] {126011, 142252});
		templateRegions.put("LUCEA", LUCEA);

		HashMap<String, int[]> FH13A = new HashMap<String, int[]>();
		FH13A.put("KIR3DL3", new int[] {0, 11561});
		FH13A.put("1_intergenic", new int[] {11561, 14009});
		FH13A.put("KIR2DL3", new int[] {14009, 27980});
		FH13A.put("3_intergenic", new int[] {27980, 30460});
		FH13A.put("KIR2DP1", new int[] {30460, 42808});
		FH13A.put("5_intergenic", new int[] {42808, 45288});
		FH13A.put("KIR2DL1", new int[] {45288, 59250});
		FH13A.put("7_intergenic", new int[] {59250, 61792});
		FH13A.put("KIR3DP1", new int[] {61792, 65755});
		FH13A.put("9_intergenic", new int[] {65755, 79077});
		FH13A.put("KIR2DL4", new int[] {79077, 89579});
		FH13A.put("11_intergenic", new int[] {89579, 91970});
		FH13A.put("KIR3DL1", new int[] {91970, 105745});
		FH13A.put("13_intergenic", new int[] {105745, 108235});
		FH13A.put("KIR2DS4", new int[] {108235, 122556});
		FH13A.put("last_intergenic", new int[] {122556, 125087});
		FH13A.put("KIR3DL2", new int[] {125087, 141345});
		templateRegions.put("FH13A", FH13A);

		HashMap<String, int[]> G248A = new HashMap<String, int[]>();
		G248A.put("KIR3DL3", new int[] {0, 11598});
		G248A.put("1_intergenic", new int[] {11598, 14048});
		G248A.put("KIR2DL3", new int[] {14048, 28037});
		G248A.put("3_intergenic", new int[] {28037, 30517});
		G248A.put("KIR2DP1", new int[] {30517, 42865});
		G248A.put("5_intergenic", new int[] {42865, 45345});
		G248A.put("KIR2DL1", new int[] {45345, 59307});
		G248A.put("7_intergenic", new int[] {59307, 61849});
		G248A.put("KIR3DP1", new int[] {61849, 65812});
		G248A.put("9_intergenic", new int[] {65812, 79134});
		G248A.put("KIR2DL4", new int[] {79134, 89636});
		G248A.put("11_intergenic", new int[] {89636, 92027});
		G248A.put("KIR3DL1", new int[] {92027, 105804});
		G248A.put("13_intergenic", new int[] {105804, 108294});
		G248A.put("KIR2DS4", new int[] {108294, 123494});
		G248A.put("last_intergenic", new int[] {123494, 126026});
		G248A.put("KIR3DL2", new int[] {126026, 142267});
		templateRegions.put("G248A", G248A);

		HashMap<String, int[]> T7526A = new HashMap<String, int[]>();
		T7526A.put("KIR3DL3", new int[] {0, 11566});
		T7526A.put("1_intergenic", new int[] {11566, 14014});
		T7526A.put("KIR2DL3", new int[] {14014, 27985});
		T7526A.put("3_intergenic", new int[] {27985, 30465});
		T7526A.put("KIR2DP1", new int[] {30465, 42813});
		T7526A.put("5_intergenic", new int[] {42813, 45293});
		T7526A.put("KIR2DL1", new int[] {45293, 59255});
		T7526A.put("7_intergenic", new int[] {59255, 61797});
		T7526A.put("KIR3DP1", new int[] {61797, 65760});
		T7526A.put("9_intergenic", new int[] {65760, 79082});
		T7526A.put("KIR2DL4", new int[] {79082, 89584});
		T7526A.put("11_intergenic", new int[] {89584, 91975});
		T7526A.put("KIR3DL1", new int[] {91975, 105752});
		T7526A.put("13_intergenic", new int[] {105752, 108242});
		T7526A.put("KIR2DS4", new int[] {108242, 123442});
		T7526A.put("last_intergenic", new int[] {123442, 125974});
		T7526A.put("KIR3DL2", new int[] {125974, 142215});
		templateRegions.put("T7526A", T7526A);

		HashMap<String, int[]> FH05A = new HashMap<String, int[]>();
		FH05A.put("KIR3DL3", new int[] {0, 11556});
		FH05A.put("1_intergenic", new int[] {11556, 14006});
		FH05A.put("KIR2DL3", new int[] {14006, 28013});
		FH05A.put("3_intergenic", new int[] {28013, 30517});
		FH05A.put("KIR2DP1", new int[] {30517, 42863});
		FH05A.put("5_intergenic", new int[] {42863, 45346});
		FH05A.put("KIR2DL1", new int[] {45346, 59306});
		FH05A.put("7_intergenic", new int[] {59306, 61848});
		FH05A.put("KIR3DP1", new int[] {61848, 65811});
		FH05A.put("9_intergenic", new int[] {65811, 79141});
		FH05A.put("KIR2DL4", new int[] {79141, 89637});
		FH05A.put("11_intergenic", new int[] {89637, 92028});
		FH05A.put("KIR3DL1", new int[] {92028, 105805});
		FH05A.put("13_intergenic", new int[] {105805, 108295});
		FH05A.put("KIR2DS4", new int[] {108295, 122616});
		FH05A.put("last_intergenic", new int[] {122616, 125149});
		FH05A.put("KIR3DL2", new int[] {125149, 141410});
		templateRegions.put("FH05A", FH05A);

		HashMap<String, int[]> FH15A = new HashMap<String, int[]>();
		FH15A.put("KIR3DL3", new int[] {0, 11604});
		FH15A.put("1_intergenic", new int[] {11604, 14054});
		FH15A.put("KIR2DL3", new int[] {14054, 28059});
		FH15A.put("3_intergenic", new int[] {28059, 30539});
		FH15A.put("KIR2DP1", new int[] {30539, 42885});
		FH15A.put("5_intergenic", new int[] {42885, 45368});
		FH15A.put("KIR2DL1", new int[] {45368, 59327});
		FH15A.put("7_intergenic", new int[] {59327, 61869});
		FH15A.put("KIR3DP1", new int[] {61869, 65832});
		FH15A.put("9_intergenic", new int[] {65832, 79164});
		FH15A.put("KIR2DL4", new int[] {79164, 89624});
		FH15A.put("11_intergenic", new int[] {89624, 92014});
		FH15A.put("KIR3DL1", new int[] {92014, 105808});
		FH15A.put("13_intergenic", new int[] {105808, 108298});
		FH15A.put("KIR2DS4", new int[] {108298, 123303});
		FH15A.put("last_intergenic", new int[] {123303, 125836});
		FH15A.put("KIR3DL2", new int[] {125836, 142091});
		templateRegions.put("FH15A", FH15A);

		HashMap<String, int[]> GRC212AB = new HashMap<String, int[]>();
		GRC212AB.put("KIR3DL3", new int[] {0, 11602});
		GRC212AB.put("1_intergenic", new int[] {11602, 14050});
		GRC212AB.put("KIR2DL3", new int[] {14050, 28026});
		GRC212AB.put("3_intergenic", new int[] {28026, 30506});
		GRC212AB.put("KIR2DP1", new int[] {30506, 42854});
		GRC212AB.put("5_intergenic", new int[] {42854, 45334});
		GRC212AB.put("KIR2DL1", new int[] {45334, 59296});
		GRC212AB.put("7_intergenic", new int[] {59296, 61838});
		GRC212AB.put("KIR3DP1", new int[] {61838, 65801});
		GRC212AB.put("9_intergenic", new int[] {65801, 79134});
		GRC212AB.put("KIR2DL4", new int[] {79134, 89602});
		GRC212AB.put("11_intergenic", new int[] {89602, 91993});
		GRC212AB.put("KIR3DS1", new int[] {91993, 105979});
		GRC212AB.put("13_intergenic", new int[] {105979, 108632});
		GRC212AB.put("KIR2DL5A", new int[] {108632, 117624});
		GRC212AB.put("15_intergenic", new int[] {117624, 120028});
		GRC212AB.put("KIR2DS3A", new int[] {120028, 134186});
		GRC212AB.put("17_intergenic", new int[] {134186, 136818});
		GRC212AB.put("KIR2DS1", new int[] {136818, 150646});
		GRC212AB.put("last_intergenic", new int[] {150646, 153175});
		GRC212AB.put("KIR3DL2", new int[] {153175, 169452});
		templateRegions.put("GRC212AB", GRC212AB);

		HashMap<String, int[]> G085BA1 = new HashMap<String, int[]>();
		G085BA1.put("KIR3DL3", new int[] {0, 11600});
		G085BA1.put("1_intergenic", new int[] {11600, 14067});
		G085BA1.put("KIR2DS2", new int[] {14067, 27720});
		G085BA1.put("3_intergenic", new int[] {27720, 30248});
		G085BA1.put("KIR2DL2", new int[] {30248, 44249});
		G085BA1.put("5_intergenic", new int[] {44249, 46791});
		G085BA1.put("KIR3DP1", new int[] {46791, 52227});
		G085BA1.put("7_intergenic", new int[] {52227, 65561});
		G085BA1.put("KIR2DL4", new int[] {65561, 76023});
		G085BA1.put("9_intergenic", new int[] {76023, 78414});
		G085BA1.put("KIR3DL1", new int[] {78414, 92192});
		G085BA1.put("11_intergenic", new int[] {92192, 94682});
		G085BA1.put("KIR2DS4", new int[] {94682, 109664});
		G085BA1.put("last_intergenic", new int[] {109664, 112194});
		G085BA1.put("KIR3DL2", new int[] {112194, 128417});
		templateRegions.put("G085BA1", G085BA1);

		HashMap<String, int[]> FH06BA1 = new HashMap<String, int[]>();
		FH06BA1.put("KIR3DL3", new int[] {0, 11567});
		FH06BA1.put("1_intergenic", new int[] {11567, 14034});
		FH06BA1.put("KIR2DS2", new int[] {14034, 27689});
		FH06BA1.put("3_intergenic", new int[] {27689, 30217});
		FH06BA1.put("KIR2DL2", new int[] {30217, 44213});
		FH06BA1.put("5_intergenic", new int[] {44213, 46755});
		FH06BA1.put("KIR3DP1", new int[] {46755, 52191});
		FH06BA1.put("7_intergenic", new int[] {52191, 65525});
		FH06BA1.put("KIR2DL4", new int[] {65525, 75984});
		FH06BA1.put("9_intergenic", new int[] {75984, 78374});
		FH06BA1.put("KIR3DL1", new int[] {78374, 92148});
		FH06BA1.put("11_intergenic", new int[] {92148, 94638});
		FH06BA1.put("KIR2DS4", new int[] {94638, 109816});
		FH06BA1.put("last_intergenic", new int[] {109816, 112349});
		FH06BA1.put("KIR3DL2", new int[] {112349, 128604});
		templateRegions.put("FH06BA1", FH06BA1);

		HashMap<String, int[]> GRC212BA1 = new HashMap<String, int[]>();
		GRC212BA1.put("KIR3DL3", new int[] {0, 11566});
		GRC212BA1.put("1_intergenic", new int[] {11566, 14033});
		GRC212BA1.put("KIR2DS2", new int[] {14033, 27687});
		GRC212BA1.put("3_intergenic", new int[] {27687, 30215});
		GRC212BA1.put("KIR2DL2", new int[] {30215, 44216});
		GRC212BA1.put("5_intergenic", new int[] {44216, 46758});
		GRC212BA1.put("KIR3DP1", new int[] {46758, 52194});
		GRC212BA1.put("7_intergenic", new int[] {52194, 65516});
		GRC212BA1.put("KIR2DL4", new int[] {65516, 76018});
		GRC212BA1.put("9_intergenic", new int[] {76018, 78409});
		GRC212BA1.put("KIR3DL1", new int[] {78409, 92186});
		GRC212BA1.put("11_intergenic", new int[] {92186, 94676});
		GRC212BA1.put("KIR2DS4", new int[] {94676, 108440});
		GRC212BA1.put("last_intergenic", new int[] {108440, 110972});
		GRC212BA1.put("KIR3DL2", new int[] {110972, 127214});
		templateRegions.put("GRC212BA1", GRC212BA1);

		HashMap<String, int[]> FH08BA2X = new HashMap<String, int[]>();
		FH08BA2X.put("KIR3DL3", new int[] {0, 8792});
		FH08BA2X.put("1_intergenic", new int[] {8792, 11255});
		FH08BA2X.put("KIR2DL3", new int[] {11255, 25255});
		//FH08BA2X.put("KIR2DL2", new int[] {11255, 25255});
		FH08BA2X.put("3_intergenic", new int[] {25255, 27792});
		FH08BA2X.put("KIR2DL5B", new int[] {27792, 36885});
		FH08BA2X.put("5_intergenic", new int[] {36885, 39289});
		FH08BA2X.put("KIR2DS5B", new int[] {39289, 53640});
		FH08BA2X.put("7_intergenic", new int[] {53640, 56272});
		FH08BA2X.put("KIR2DP1", new int[] {56272, 68621});
		FH08BA2X.put("9_intergenic", new int[] {68621, 71104});
		FH08BA2X.put("KIR2DL1", new int[] {71104, 85071});
		FH08BA2X.put("11_intergenic", new int[] {85071, 87613});
		FH08BA2X.put("KIR3DP1", new int[] {87613, 93278});
		FH08BA2X.put("13_intergenic", new int[] {93278, 106610});
		FH08BA2X.put("KIR2DL4", new int[] {106610, 117071});
		FH08BA2X.put("15_intergenic", new int[] {117071, 119461});
		FH08BA2X.put("KIR3DL1", new int[] {119461, 133255});
		FH08BA2X.put("17_intergenic", new int[] {133255, 135743});
		FH08BA2X.put("KIR2DS4", new int[] {135743, 150727});
		FH08BA2X.put("last_intergenic", new int[] {150727, 153259});
		FH08BA2X.put("KIR3DL2", new int[] {153259, 169518});
		templateRegions.put("FH08BA2X", FH08BA2X);

		HashMap<String, int[]> FH13BA2 = new HashMap<String, int[]>();
		FH13BA2.put("KIR3DL3", new int[] {0, 11605});
		FH13BA2.put("1_intergenic", new int[] {11605, 14072});
		FH13BA2.put("KIR2DS2", new int[] {14072, 27725});
		FH13BA2.put("3_intergenic", new int[] {27725, 30253});
		FH13BA2.put("KIR2DL2", new int[] {30253, 44256});
		FH13BA2.put("5_intergenic", new int[] {44256, 46798});
		FH13BA2.put("KIR2DL5B", new int[] {46798, 55814});
		FH13BA2.put("7_intergenic", new int[] {55814, 58218});
		FH13BA2.put("KIR2DS5B", new int[] {58218, 72550});
		FH13BA2.put("9_intergenic", new int[] {72550, 75182});
		FH13BA2.put("KIR2DP1", new int[] {75182, 87532});
		FH13BA2.put("11_intergenic", new int[] {87532, 90015});
		FH13BA2.put("KIR2DL1", new int[] {90015, 103984});
		FH13BA2.put("13_intergenic", new int[] {103984, 106526});
		FH13BA2.put("KIR3DP1", new int[] {106526, 110489});
		FH13BA2.put("15_intergenic", new int[] {110489, 123812});
		FH13BA2.put("KIR2DL4", new int[] {123812, 134312});
		FH13BA2.put("17_intergenic", new int[] {134312, 136702});
		FH13BA2.put("KIR3DL1", new int[] {136702, 150477});
		FH13BA2.put("19_intergenic", new int[] {150477, 152967});
		FH13BA2.put("KIR2DS4", new int[] {152967, 168144});
		FH13BA2.put("last_intergenic", new int[] {168144, 170673});
		FH13BA2.put("KIR3DL2", new int[] {170673, 186950});
		templateRegions.put("FH13BA2", FH13BA2);

		HashMap<String, int[]> RSHBA2 = new HashMap<String, int[]>();
		RSHBA2.put("KIR3DL3", new int[] {0, 11561});
		RSHBA2.put("1_intergenic", new int[] {11561, 14029});
		RSHBA2.put("KIR2DS2", new int[] {14029, 27682});
		RSHBA2.put("3_intergenic", new int[] {27682, 30210});
		RSHBA2.put("KIR2DL2", new int[] {30210, 44211});
		RSHBA2.put("5_intergenic", new int[] {44211, 46753});
		RSHBA2.put("KIR2DL5B", new int[] {46753, 55769});
		RSHBA2.put("7_intergenic", new int[] {55769, 58173});
		RSHBA2.put("KIR2DS5B", new int[] {58173, 72463});
		RSHBA2.put("9_intergenic", new int[] {72463, 75095});
		RSHBA2.put("KIR2DP1", new int[] {75095, 87442});
		RSHBA2.put("11_intergenic", new int[] {87442, 89925});
		RSHBA2.put("KIR2DL1", new int[] {89925, 103883});
		RSHBA2.put("13_intergenic", new int[] {103883, 106425});
		RSHBA2.put("KIR3DP1", new int[] {106425, 110388});
		RSHBA2.put("15_intergenic", new int[] {110388, 123711});
		RSHBA2.put("KIR2DL4", new int[] {123711, 134211});
		RSHBA2.put("17_intergenic", new int[] {134211, 136601});
		RSHBA2.put("KIR3DL1", new int[] {136601, 150376});
		RSHBA2.put("19_intergenic", new int[] {150376, 152866});
		RSHBA2.put("KIR2DS4", new int[] {152866, 168043});
		RSHBA2.put("last_intergenic", new int[] {168043, 170572});
		RSHBA2.put("KIR3DL2", new int[] {170572, 179070});
		templateRegions.put("RSHBA2", RSHBA2);

		HashMap<String, int[]> G248BA2 = new HashMap<String, int[]>();
		G248BA2.put("KIR3DL3", new int[] {0, 11556});
		G248BA2.put("1_intergenic", new int[] {11556, 14023});
		G248BA2.put("KIR2DS2", new int[] {14023, 27676});
		G248BA2.put("3_intergenic", new int[] {27676, 30204});
		G248BA2.put("KIR2DL2", new int[] {30204, 44173});
		G248BA2.put("5_intergenic", new int[] {44173, 46715});
		G248BA2.put("KIR2DL5B", new int[] {46715, 55727});
		G248BA2.put("7_intergenic", new int[] {55727, 58131});
		G248BA2.put("KIR2DS3B", new int[] {58131, 72288});
		G248BA2.put("9_intergenic", new int[] {72288, 74919});
		G248BA2.put("KIR2DP1", new int[] {74919, 87269});
		G248BA2.put("11_intergenic", new int[] {87269, 89752});
		G248BA2.put("KIR2DL1", new int[] {89752, 103722});
		G248BA2.put("13_intergenic", new int[] {103722, 106264});
		G248BA2.put("KIR3DP1", new int[] {106264, 110223});
		G248BA2.put("15_intergenic", new int[] {110223, 123557});
		G248BA2.put("KIR2DL4", new int[] {123557, 134019});
		G248BA2.put("17_intergenic", new int[] {134019, 136410});
		G248BA2.put("KIR3DL1", new int[] {136410, 150188});
		G248BA2.put("19_intergenic", new int[] {150188, 152678});
		G248BA2.put("KIR2DS4", new int[] {152678, 167224});
		G248BA2.put("last_intergenic", new int[] {167224, 169754});
		G248BA2.put("KIR3DL2", new int[] {169754, 178112});
		templateRegions.put("G248BA2", G248BA2);

		HashMap<String, int[]> LUCEBdel = new HashMap<String, int[]>();
		LUCEBdel.put("KIR3DL3", new int[] {0, 11600});
		LUCEBdel.put("1_intergenic", new int[] {11600, 14067});
		LUCEBdel.put("KIR2DS2", new int[] {14067, 27721});
		LUCEBdel.put("3_intergenic", new int[] {27721, 30249});
		LUCEBdel.put("KIR2DL2", new int[] {30249, 44242});
		LUCEBdel.put("5_intergenic", new int[] {44242, 46784});
		LUCEBdel.put("KIR3DP1", new int[] {46784, 52220});
		LUCEBdel.put("7_intergenic", new int[] {52220, 65553});
		LUCEBdel.put("KIR2DL4", new int[] {65553, 76021});
		LUCEBdel.put("9_intergenic", new int[] {76021, 78412});
		LUCEBdel.put("KIR3DS1", new int[] {78412, 92397});
		LUCEBdel.put("11_intergenic", new int[] {92397, 95050});
		LUCEBdel.put("KIR2DL5A", new int[] {95050, 104046});
		LUCEBdel.put("13_intergenic", new int[] {104046, 106450});
		LUCEBdel.put("KIR2DS5A", new int[] {106450, 120784});
		LUCEBdel.put("15_intergenic", new int[] {120784, 123416});
		LUCEBdel.put("KIR2DS1", new int[] {123416, 137244});
		LUCEBdel.put("last_intergenic", new int[] {137244, 139773});
		LUCEBdel.put("KIR3DL2", new int[] {139773, 156050});
		templateRegions.put("LUCEBdel", LUCEBdel);

		HashMap<String, int[]> T7526Bdel = new HashMap<String, int[]>();
		T7526Bdel.put("KIR3DL3", new int[] {0, 11561});
		T7526Bdel.put("1_intergenic", new int[] {11561, 14004});
		T7526Bdel.put("KIR2DS2", new int[] {14004, 27658});
		T7526Bdel.put("3_intergenic", new int[] {27658, 30186});
		T7526Bdel.put("KIR2DL2", new int[] {30186, 44175});
		T7526Bdel.put("5_intergenic", new int[] {44175, 46717});
		T7526Bdel.put("KIR3DP1", new int[] {46717, 52172});
		T7526Bdel.put("7_intergenic", new int[] {52172, 65505});
		T7526Bdel.put("KIR2DL4", new int[] {65505, 75973});
		T7526Bdel.put("9_intergenic", new int[] {75973, 78364});
		T7526Bdel.put("KIR3DS1", new int[] {78364, 92349});
		T7526Bdel.put("11_intergenic", new int[] {92349, 95002});
		T7526Bdel.put("KIR2DL5A", new int[] {95002, 103994});
		T7526Bdel.put("13_intergenic", new int[] {103994, 106398});
		T7526Bdel.put("KIR2DS3A", new int[] {106398, 120556});
		T7526Bdel.put("15_intergenic", new int[] {120556, 123188});
		T7526Bdel.put("KIR2DS1", new int[] {123188, 137016});
		T7526Bdel.put("last_intergenic", new int[] {137016, 139545});
		T7526Bdel.put("KIR3DL2", new int[] {139545, 155822});
		templateRegions.put("T7526Bdel", T7526Bdel);

		HashMap<String, int[]> FH05B = new HashMap<String, int[]>();
		FH05B.put("KIR3DL3", new int[] {0, 11557});
		FH05B.put("1_intergenic", new int[] {11557, 14024});
		FH05B.put("KIR2DS2", new int[] {14024, 27677});
		FH05B.put("3_intergenic", new int[] {27677, 30205});
		FH05B.put("KIR2DL2", new int[] {30205, 44211});
		FH05B.put("5_intergenic", new int[] {44211, 46753});
		FH05B.put("KIR2DL5B", new int[] {46753, 55765});
		FH05B.put("7_intergenic", new int[] {55765, 58169});
		FH05B.put("KIR2DS3B", new int[] {58169, 72326});
		FH05B.put("9_intergenic", new int[] {72326, 74957});
		FH05B.put("KIR2DP1", new int[] {74957, 87307});
		FH05B.put("11_intergenic", new int[] {87307, 89790});
		FH05B.put("KIR2DL1", new int[] {89790, 103759});
		FH05B.put("13_intergenic", new int[] {103759, 106301});
		FH05B.put("KIR3DP1", new int[] {106301, 110260});
		FH05B.put("15_intergenic", new int[] {110260, 123593});
		FH05B.put("KIR2DL4", new int[] {123593, 134061});
		FH05B.put("17_intergenic", new int[] {134061, 136452});
		FH05B.put("KIR3DS1", new int[] {136452, 150437});
		FH05B.put("19_intergenic", new int[] {150437, 153090});
		FH05B.put("KIR2DL5A", new int[] {153090, 162082});
		FH05B.put("21_intergenic", new int[] {162082, 164486});
		FH05B.put("KIR2DS3A", new int[] {164486, 178644});
		FH05B.put("23_intergenic", new int[] {178644, 181276});
		FH05B.put("KIR2DS1", new int[] {181276, 195104});
		FH05B.put("last_intergenic", new int[] {195104, 197633});
		FH05B.put("KIR3DL2", new int[] {197633, 213910});
		templateRegions.put("FH05B", FH05B);

		HashMap<String, int[]> FH15B = new HashMap<String, int[]>();
		FH15B.put("KIR3DL3", new int[] {0, 11557});
		FH15B.put("1_intergenic", new int[] {11557, 14024});
		FH15B.put("KIR2DS2", new int[] {14024, 27677});
		FH15B.put("3_intergenic", new int[] {27677, 30205});
		FH15B.put("KIR2DL2", new int[] {30205, 44208});
		FH15B.put("5_intergenic", new int[] {44208, 46750});
		FH15B.put("KIR2DL5B", new int[] {46750, 55762});
		FH15B.put("7_intergenic", new int[] {55762, 58166});
		FH15B.put("KIR2DS3B", new int[] {58166, 72323});
		FH15B.put("9_intergenic", new int[] {72323, 74954});
		FH15B.put("KIR2DP1", new int[] {74954, 87304});
		FH15B.put("11_intergenic", new int[] {87304, 89787});
		FH15B.put("KIR2DL1", new int[] {89787, 103756});
		FH15B.put("13_intergenic", new int[] {103756, 106298});
		FH15B.put("KIR3DP1", new int[] {106298, 110257});
		FH15B.put("15_intergenic", new int[] {110257, 123589});
		FH15B.put("KIR2DL4", new int[] {123589, 134057});
		FH15B.put("17_intergenic", new int[] {134057, 136448});
		FH15B.put("KIR3DS1", new int[] {136448, 150433});
		FH15B.put("19_intergenic", new int[] {150433, 153086});
		FH15B.put("KIR2DL5A", new int[] {153086, 162078});
		FH15B.put("21_intergenic", new int[] {162078, 164482});
		FH15B.put("KIR2DS3A", new int[] {164482, 178640});
		FH15B.put("23_intergenic", new int[] {178640, 181272});
		FH15B.put("KIR2DS1", new int[] {181272, 195100});
		FH15B.put("last_intergenic", new int[] {195100, 197629});
		FH15B.put("KIR3DL2", new int[] {197629, 213906});
		templateRegions.put("FH15B", FH15B);

		HashMap<String, int[]> WT47B = new HashMap<String, int[]>();
		WT47B.put("KIR3DL3", new int[] {0, 11707});
		WT47B.put("1_intergenic", new int[] {11707, 14025});
		WT47B.put("KIR2DS2", new int[] {14025, 28176});
		WT47B.put("3_intergenic", new int[] {28176, 30206});
		WT47B.put("KIR2DL2", new int[] {30206, 44604});
		WT47B.put("5_intergenic", new int[] {44604, 46749});
		WT47B.put("KIR2DL5B", new int[] {46749, 55762});
		WT47B.put("7_intergenic", new int[] {55762, 58165});
		WT47B.put("KIR2DS3B", new int[] {58165, 72510});
		WT47B.put("9_intergenic", new int[] {72510, 74953});
		WT47B.put("KIR2DP1", new int[] {74953, 87401});
		WT47B.put("11_intergenic", new int[] {87401, 89786});
		WT47B.put("KIR2DL1", new int[] {89786, 103794});
		WT47B.put("13_intergenic", new int[] {103794, 106297});
		WT47B.put("KIR3DP1", new int[] {106297, 110257});
		WT47B.put("15_intergenic", new int[] {110257, 123595});
		WT47B.put("KIR2DL4", new int[] {123595, 134096});
		WT47B.put("17_intergenic", new int[] {134096, 136448});
		WT47B.put("KIR3DS1", new int[] {136448, 150641});
		WT47B.put("19_intergenic", new int[] {150641, 153086});
		WT47B.put("KIR2DL5A", new int[] {153086, 162083});
		WT47B.put("21_intergenic", new int[] {162083, 164485});
		WT47B.put("KIR2DS5A", new int[] {164485, 179351});
		WT47B.put("23_intergenic", new int[] {179351, 181450});
		WT47B.put("KIR2DS1", new int[] {181450, 195452});
		WT47B.put("last_intergenic", new int[] {195452, 197808});
		WT47B.put("KIR3DL2", new int[] {197808, 214569});
		templateRegions.put("WT47B", WT47B);

	}
	
	public static HashMap<String, String> getTemplateFastaFileNames() {
		return templateFastaFileNames;
	}
	
	public static HashMap<String, HashMap<String, int[]>> getTemplateRegions() {
		return templateRegions;
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
	
	public static ArrayList<String> getDiploidNames(File[] templateBarcodes1, File[] templateBarcodes2) {
		int barlen = templateBarcodes1.length;
		ArrayList<String> diploidTemplateNames = new ArrayList<String>();
		ArrayList<String> templateNames1 = getNames(templateBarcodes1);
		ArrayList<String> templateNames2 = getNames(templateBarcodes2);
		int currIndex = 0;
		for (int i = 0; i < templateNames1.size(); ++i) {
			for (int j = i; j < templateNames2.size(); ++j){
				String[] temp = new String[2];
				temp[0] = templateNames1.get(i);
				temp[1] = templateNames2.get(j);
				Arrays.sort(temp);
				diploidTemplateNames.add(temp[0] + "_" + temp[1]);
				currIndex+=1;
			}
		}
		
		return diploidTemplateNames;
	}
	
	public static ArrayList<String> getDiploidNames(File[] templateBarcodes) {
		int barlen = templateBarcodes.length;
		ArrayList<String> diploidTemplateNames = new ArrayList<String>();
		ArrayList<String> templateNames = getNames(templateBarcodes);
		int currIndex = 0;
		for (int i = 0; i < templateNames.size(); ++i) {
			for (int j = i; j < templateNames.size(); ++j){
				String[] temp = new String[2];
				temp[0] = templateNames.get(i);
				temp[1] = templateNames.get(j);
				Arrays.sort(temp);
				diploidTemplateNames.add(temp[0] + "_" + temp[1]);
				currIndex+=1;
			}
		}
		
		return diploidTemplateNames;
		//set types
	}
	
	public static ArrayList<String> getDiploidTypes(File[] templateBarcodes1, File[] templateBarcodes2) {
		int barlen = templateBarcodes1.length;
		ArrayList<String> diploidTemplateTypes = new ArrayList<String>();
		ArrayList<String> templateTypes1 = getTypes(templateBarcodes1);
		ArrayList<String> templateTypes2 = getTypes(templateBarcodes2);
		int currIndex = 0;
		for (int i = 0; i < templateTypes1.size(); ++i) {
			for (int j = i; j < templateTypes2.size(); ++j){
				String[] temp = new String[2];
				temp[0] = templateTypes1.get(i);
				temp[1] = templateTypes2.get(j);
				Arrays.sort(temp);
				diploidTemplateTypes.add(temp[0] + "_" + temp[1]);
				currIndex+=1;
			}
		}
		
		return diploidTemplateTypes;
	}
	
	public static ArrayList<String> getDiploidTypes(File[] templateBarcodes) {
		int barlen = templateBarcodes.length;
		ArrayList<String> diploidTemplateTypes = new ArrayList<String>();
		ArrayList<String> templateTypes = getTypes(templateBarcodes);
		int currIndex = 0;
		for (int i = 0; i < templateTypes.size(); ++i) {
			for (int j = i; j < templateTypes.size(); ++j){
				String[] temp = new String[2];
				temp[0] = templateTypes.get(i);
				temp[1] = templateTypes.get(j);
				Arrays.sort(temp);
				diploidTemplateTypes.add(temp[0] + "_" + temp[1]);
				currIndex+=1;
			}
		}
		
		return diploidTemplateTypes;
		//set types
	}

	public static ArrayList<String> getNames(File[] templateBarcodes) {
		ArrayList<String> names = new ArrayList<String>();
		for (int i = 0; i < templateBarcodes.length; ++i) {
			for (Iterator it = templateTypes.keySet().iterator(); it.hasNext(); ) {
				String key = (String) it.next();
				if (templateBarcodes[i].toString().contains(key)) {
					names.add(key);
				}
			}
		}
		return names;
	}
	
	
	
	public static String[] getDiploidPresenceAbsenceTypes(ArrayList<String> tempTemplateNames, HashMap<String, HashMap<String, int[]>> templateRegions, ArrayList<String> genes) {
		int barlen = tempTemplateNames.size();
		String[] diploidTypes = new String[(((barlen*barlen)-barlen)/2)+barlen];
		String[] types = getPresenceAbsenceTypes(tempTemplateNames, templateRegions, genes);
		int currIndx = 0;
		for (int i = 0; i < tempTemplateNames.size(); ++i) {
			for (int j = i; j < tempTemplateNames.size(); ++j) {
				//Integer temp = Integer.parseInt(types[i]) + Integer.parseInt(types[j]);
				//String tempString = Integer.toString(temp);
				//diploidTypes[currIndx] = tempString;

				if ((types[i]=="1") || (types[j]=="1")) {
					diploidTypes[currIndx] = "1";
				} 
				else { diploidTypes[currIndx] = "0"; }
				currIndx+=1;
			}
		}
		return diploidTypes;
	}
	
	public static ArrayList<Integer> getFrameowkrGenes() {
		return geneIndexFramework;
	}
	
	public static int[] getDiploidCopyNumber(ArrayList<String> tempTemplateNames, HashMap<String, HashMap<String, int[]>> templateRegions, ArrayList<String> genes) {
		int barlen = tempTemplateNames.size();
		int[] diploidCopyNumber = new int[(((barlen*barlen)-barlen)/2)+barlen];
		int[] copyNumber = getCopyNumber(tempTemplateNames, templateRegions, genes);
		int currIndx = 0;
		for (int i = 0; i < tempTemplateNames.size(); ++i) {
			for (int j = i; j < tempTemplateNames.size(); ++j) {
				diploidCopyNumber[currIndx] = copyNumber[i] + copyNumber[j];
				currIndx+=1;
			}
		}
		return diploidCopyNumber;
	}
	
	public static int[] getCopyNumber(ArrayList<String> tempTemplateNames, HashMap<String, HashMap<String, int[]>> templateRegions, ArrayList<String> genes) {
		int[] copyNumber = new int[tempTemplateNames.size()];
		for (int i = 0; i < tempTemplateNames.size(); ++i) {
			copyNumber[i] = 0;
			String templateName = tempTemplateNames.get(i);
			int num = 0;
			for (int j = 0; j < genes.size(); ++j) {
				if (templateRegions.get(templateName).containsKey(genes.get(j))) {
					num += 1;
				}
			}
			copyNumber[i] = num;	
			
		}
		return copyNumber;
	}
	
	public static String[] getPresenceAbsenceTypes(ArrayList<String> tempTemplateNames, HashMap<String, HashMap<String, int[]>> templateRegions, ArrayList<String> genes) {
		String[] types = new String[tempTemplateNames.size()];
		for (int i = 0; i < tempTemplateNames.size(); ++i) {
			types[i] = "0";
			String templateName = tempTemplateNames.get(i);
			int num = 0;
			for (int j = 0; j < genes.size(); ++j) {
				if (templateRegions.get(templateName).containsKey(genes.get(j))) {
					//num += 1;
					types[i] = "1";
				}
			}
			//types[i] = Integer.toString(num);	
			
		}
		return types;

	}
	

	
	public static ArrayList<String> getTypes(File[] templateBarcodes) {
		ArrayList<String> types = new ArrayList<String>();
		for (int i = 0; i < templateBarcodes.length; ++i) {
			for (Iterator it = templateTypes.keySet().iterator(); it.hasNext(); ) {
				String key = (String) it.next();
				if (templateBarcodes[i].toString().contains(key)) {
					types.add(templateTypes.get(key));
				}
			}
		}
		return types;
		
         
	}
	
	
	

	
	
	

}
