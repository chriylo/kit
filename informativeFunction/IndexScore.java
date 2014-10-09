/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package informativeFunction;

/**
 *
 * @author rachmani
 */
public class IndexScore implements Comparable{

    private int index;
    private double score;

    public IndexScore(int index, double score) {
        this.index = index;
        this.score = score;
    }

    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * @return the score
     */
    public double getScore() {
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public int compareTo(Object o) {
        IndexScore is = (IndexScore) o;
        if (this.score > is.getScore()) {
            return 1;
        } else if (this.score < is.getScore()) {
            return -1;
        }
        return 0;
    }
}
