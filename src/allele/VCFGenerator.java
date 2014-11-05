package allele;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import typing.Typing;

public abstract class VCFGenerator {
	
	public static ArrayList<String> getVCFSamplesFromOutFile(String pathToDir) throws IOException {
		ArrayList<String> samples = new ArrayList<String>();
		File dir = new File(pathToDir);
		String[] fileNames = dir.list();
		for (int f = 0; f < fileNames.length; ++f) {
			if (fileNames[f].startsWith("0.") && fileNames[f].endsWith(".out")) {
				String fileName = fileNames[f];
				String[] sampleArray = fileName.split("\\.");
				samples.add(sampleArray[1]);
			}
		}
		return samples;
	}
	
	public static ArrayList<String> getVCFSamplesFromDebugFile(String pathToDir) throws IOException {
		ArrayList<String> samples = new ArrayList<String>();
		File dir = new File(pathToDir);
		String[] fileNames = dir.list();
		for (int f = 0; f < fileNames.length; ++f) {
			if (fileNames[f].startsWith("0.") && fileNames[f].endsWith(".debug")) {
				String fileName = fileNames[f];
				String[] sampleArray = fileName.split("\\.");
				samples.add(sampleArray[1]);
			}
		}
		return samples;
//		HashMap<String, int[]> sampleCN = getSampleCN(pathToCN);
//		for (Iterator it = sampleCN.keySet().iterator(); it.hasNext(); ) {
//			String sample = (String) it.next();
//			samples.add(sample);
//			
//		}
//		return samples;
		
	}
	
	public static HashMap<String, int[]> getVCFSampleCNFromCNFile(String pathToCN) throws IOException {
		HashMap<String, int[]> sampleCN = new HashMap<String, int[]>();
	
		BufferedReader br = new BufferedReader(new FileReader(pathToCN));
		String line;
		while ((line = br.readLine()) != null) {
			String[] lineSplit = line.split("\t");
			String sample = lineSplit[0];
			String tempStringCN = lineSplit[1];
			int[] cn;
			if (tempStringCN.contains("_")) { 
				cn = Typing.getDiploidTypeCopyNumbers().get(tempStringCN);
			}
			else {
				String stringCN = tempStringCN;
				//String stringCN = (String) lineSplit[1].subSequence(1, lineSplit[1].length()-1);
				String[] stringCNArray; 
				stringCNArray = stringCN.split(",");
				cn = new int[stringCNArray.length];
				for (int i = 0; i < stringCNArray.length; ++i) {
					cn[i] = Integer.valueOf(stringCNArray[i]);
				}
			}
			
			sampleCN.put(sample, cn);
		}
		return sampleCN;
	}
		
	public static ArrayList<Integer> getVCFOffset(){
		ArrayList<Integer> offset = new ArrayList<Integer>();
		offset.add(59927494); //0
		offset.add(-59942000); //1 (not in b36)
		offset.add(-59945000); //2 (not in b36)
		offset.add(-59948000); //3 (not in b36)
		offset.add(-59951000); //4 (not in b36)
		offset.add(-59954000); //5 (not in b36)
		offset.add(59958020); //6 
		offset.add(59972847); //7
		offset.add(59989686); //8
		offset.add(60006652); //9
		offset.add(60022622); //10
		offset.add(60019501); //11
		offset.add(-60034501); //12 (not in b36)
		offset.add(60035765); //13
		offset.add(60053475); //14
		
		return offset;
	}

}
