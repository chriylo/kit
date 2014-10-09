/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package informativeFunction;

/**
 *
 * @author rachmani
 */
public interface InfoScoreCalculator {

    /**
     * Compute an getInfoScore score of a given integer vector. Each integer
     * vector is assumed to be a collection of possible copy numbers of a k-mer.
     * The size of the vector is expected to be same with the number of
     * templates.
     *
     * @param vec copy number vector of a k-mer
     * @param d mean depth-of-coverage of a data
     * @param k length of the k-mer
     * @param l read length 
     * @return the informativeness of the given vector.
     */
    public double getInfoScore(int[] vec, int d, int k, int l);
}
