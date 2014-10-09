/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package informativeFunction;

/**
 *
 * @author rachmani
 */
public class BarcodeReducer {

    public BarcodeReducer(int[][] barcode) {
        int temlen = barcode.length;
        int barlen = barcode[0].length;
        for (int i=0; i < barlen; i++) {
            int[] column = new int[temlen];
            for (int j=0; j<temlen; j++) {
                column[j] = barcode[j][i];
            }
        }
    }
}
