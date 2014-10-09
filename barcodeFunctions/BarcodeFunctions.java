package barcodeFunctions;

import generator.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BarcodeFunctions {
	

	public static double[] scaleBarcode(int[] barcode, double mult, double div) {
		double[] scaledBarcode = new double[barcode.length];
		double factor;
		if (div==0) {
			factor = 1;
		}
		else if (mult==0) {
			factor = 1/div;
		}
		else {
			factor = mult/div;
		}
		
		for (int m=0; m<barcode.length; ++m) {
			scaledBarcode[m] = barcode[m] * factor;
		}
		return scaledBarcode;
	}
	
	public static int[] multBarcode(int[] barcode, int mult) {
		int[] scaledBarcode = new int[barcode.length];

		for (int m=0; m<barcode.length; ++m) {
			scaledBarcode[m] = barcode[m] * mult;
		}
		
		return scaledBarcode;
	}
	
	public static int[] getSubsetBarcode(int[] fullBarcode, List<Integer> includeIndx) {
    	int[] subsetBarcode = new int[includeIndx.size()];
    	for (int i = 0; i < includeIndx.size(); ++i) {
    		subsetBarcode[i] = fullBarcode[includeIndx.get(i)];
    	}
    	return subsetBarcode;
    }
	
	public static double max(double[] doubleArray) {
		double max = doubleArray[0];
		
		for (int i=1; i<doubleArray.length; ++i) {
			if (doubleArray[i] > max) max = doubleArray[i];
		}
		return max;
	}
	
	public static int max(int[] intArray) {
		int max = intArray[0];
		
		for (int i=1; i<intArray.length; ++i) {
			if (intArray[i] > max) max = intArray[i];
		}
		return max;
	}
	
	public static int min(int[] intArray) {
		int min = intArray[0];
		
		for (int i=1; i<intArray.length; ++i) {
			if (intArray[i] < min) min = intArray[i];
		}
		return min;
	}
	
	public static double min(double[] doubleArray, int exclude) {
		double min = Double.MAX_VALUE;
		
		for (int i=0; i<doubleArray.length; ++i) {
			if (i!=exclude) {
			if (doubleArray[i] < min) min = doubleArray[i];
			}
		}
		return min;
	}
	
	public static double min(double[] doubleArray) {
		double min = doubleArray[0];
		
		for (int i=1; i<doubleArray.length; ++i) {
			if (doubleArray[i] < min) min = doubleArray[i];
		}
		return min;
	}
	
	private static double dotProduct(double[] a, double[] b) {
    	assert a != null && b != null : "Trying to add " +
				"null vectors.";
		assert a.length == b.length : "Trying to add " +
				"vectors of different lengths.";
    	double dot = 0;
    	for (int i = 0; i < a.length; ++i) {
    		dot += a[i]*b[i];
    	}
    	return dot;
    }
    
	private static double magnitude(double[] a) {
    	//.out.println("\t\t"+Arrays.toString(a));
    	double var = 0;
    	for (int i = 0; i < a.length; ++i) {
    		var += a[i]*a[i];
    	}
    	return Math.sqrt(var);
    }
	
    public static double correlation(double[] a, double[] b) { 
    	double dot = dotProduct(a,b);
    	double maga = magnitude(a);
    	double magb = magnitude(b);
    	/*
    	if (dot == 0) {
    		dot = 1*1;
    	}
    	if (vara == 0) {
    		maga = 1;
    	} 
    	if (varb==0) {
    		magb = 1;
    	}
    	*/
    	return dot/(maga*magb);
    }
    
    public static int[] addBarcodes(int[] vec0, int[] vec1) {
		assert vec0 != null && vec1 != null : "Trying to add " +
				"null vectors.";
		assert vec0.length == vec1.length : "Trying to add " +
				"vectors of different lengths.";
		
		int[] vec = new int[vec0.length];
		for (int i=0; i<vec0.length; ++i) {
			vec[i] = vec0[i] + vec1[i];
		}
		return vec;
	}
    
    public static int[] addBarcodesDir(String path) throws IOException {
		File dir = new File(path);
		String[] fileNames = dir.list();
		Arrays.sort(fileNames);


		
		int[] vec = KMerFileReader.getBarcodeFromFile(dir.getAbsolutePath() + File.separatorChar + fileNames[0]);
		
		for (int i=0; i<fileNames.length; ++i){
			String fileName = fileNames[i];
			System.out.println(dir.getAbsolutePath() + File.separatorChar + fileName);
			vec = addBarcodes(vec, KMerFileReader.getBarcodeFromFile(dir.getAbsolutePath() + File.separatorChar + fileName));
		}
		
		return vec;
		
	}
    
    public static int[] reorderBarcode(int[] barcode, Trie barcodeTrie, Trie newTrie) {
		
		int[] newBarcode = new int[newTrie.size()];
		
		String[] barcodeWords = barcodeTrie.words();
		for (int i = 0; i < barcodeWords.length; ++i){
			newBarcode[newTrie.getWordIx(barcodeWords[i])] = barcode[i];
		}
		//System.out.println(Arrays.toString(newBarcode));
		return newBarcode;
	}
    
    public static List<Integer> filterBarcodes(int[][] barcodes, double threshold) {

        List<Integer> exclude = new ArrayList<Integer>();
        int[] temp = new int[barcodes.length];
        for (int i = 0; i < barcodes[0].length; ++i) {
            for (int j = 0; j < barcodes.length; ++j) {

                temp[j] = barcodes[j][i];
            }
            int min = min(temp);
            int max = max(temp);
            if (max == min) {
                exclude.add(i);
            } else if (((double) min) / max > threshold) {
                exclude.add(i);
            }
        }



        return exclude;
    }
    
    public static double getSum(double[] barcode) {
		double sum = 0;
		for (int i=0; i<barcode.length; ++i) {
			sum += barcode[i];
		}
		return sum;
	}
	
	public static double getSum(int[] barcode) {
		int sum = 0;
		for (int i=0; i<barcode.length; ++i) {
			sum += barcode[i];
		}
		return sum;
	}
	
    
    public static double[] toDoubleArray(int[] a) {
        double[] d = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            d[i] = (double) a[i];
        }
        return d;
    }
}
