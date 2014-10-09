/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package informativeFunction;

import java.util.HashMap;

/**
 *
 * @author rachmani
 */
public class SurprisalCalculator {

    private HashMap surprisalMap = new HashMap();

    public SurprisalCalculator(int[][] barcode) {
        System.out.println("Processing Surprisal Calculator...");
        int barlen = barcode[0].length;
        int temlen = barcode.length;
        for (int i = 0; i < barlen; i++) {
            int[] column = new int[temlen];
            String columnString = "";
            for (int j = 0; j < temlen; j++) {
                column[j] = barcode[j][i];
                columnString += String.valueOf(barcode[j][i]) + " ";
            }
            double[] surprisals = getSurprisal(column);
            this.surprisalMap.put(i, surprisals);
            if (i % 10000 == 0) {
                System.out.println("Processing\t" + i + "\t" + columnString.trim());
            }
//            System.out.print(i + "\t" + columnString.trim() + "\t");
//            for (int k = 0; k < temlen; k++) {
//                System.out.print(" " + String.valueOf(surprisals[k]));
//            }
//            System.out.println();
        }
    }
    
    public HashMap getSurprisalMap() {
        return this.surprisalMap;
    }

    public final double[] getSurprisal(int[] column) {
        HashMap freqMap = new HashMap();
        int length = column.length;
        double[] result = new double[length];
        for (int i = 0; i < length; i++) {
            int value = column[i];
            if (freqMap.containsKey(value)) {
            	int current = ((Integer) freqMap.get(value)).intValue();
                //int current = (int) freqMap.get(value);
                freqMap.put(value, current + 1);
            } else {
                freqMap.put(value, 1);
            }
        }
        for (int i = 0; i < length; i++) {
            int value = column[i];
            int freq = ((Integer) freqMap.get(value)).intValue();
            //int freq = (int) freqMap.get(value);
            double ratio = (double) freq / (double) length;
            double surprisal = -Math.log(ratio);
            result[i] = surprisal;
        }
        return result;
    }
}
