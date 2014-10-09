/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package informativeFunction;
import clusterFunctions.Cluster;
import java.util.*;
import org.apache.commons.math3.distribution.PoissonDistribution;

/**
 *
 * @author rachmani
 */
public class PoissonInfoScoreCalculator implements InfoScoreCalculator {

    private HashMap freq = new HashMap();
    private HashMap scoreMap = new HashMap();
    private boolean got01 = false;
    private boolean got12 = false;
    private boolean got23 = false;
    private double[] score01;
    private double[] score12;
    private double[] score23;
    
    public PoissonInfoScoreCalculator() {
    	
    }

    public PoissonInfoScoreCalculator(int[][] barcode, int d, int k, int l) {
        // expected number of k-mers in the read data (lambda in Poisson) for 1 copy

        System.out.println("Processing PoissonInfoScoreCalculator...");
        double lambda = (double) d * (double) (l - k + 1) / (double) l;
        int barlen = barcode[0].length;
        int temlen = barcode.length;
        for (int i = 0; i < barlen; i++) {
            int[] column = new int[temlen];
            String columnString = "";
            for (int j = 0; j < temlen; j++) {
                column[j] = barcode[j][i];
                columnString += String.valueOf(barcode[j][i]) + " ";
            }
            if (i % 10000 == 0) {
                System.out.println("Processing " + i + "\t" + columnString.trim());
//                double[] scores = getInfoVector(column, lambda);
//                System.out.println(i + "\t" + columnString.trim() + "\t" + scores[0]);
            }
//            System.out.print(i + "\t" + columnString.trim() + "\t");
//            double[] scores = getInfoVector(column, lambda);
//            this.scoreMap.put(i, scores);
            double score = getInfoScore(column, lambda);
            this.scoreMap.put(i, score);
//            System.out.println(i + "\t" + columnString + "\t" + score);
        }
    }

    public HashMap getScoreMap() {
        return this.scoreMap;
    }

    public PoissonInfoScoreCalculator(ArrayList<int[]> bararray, int d, int k, int l) {
        int[][] barcode = new int[bararray.size()][];
        for (int i = 0; i < bararray.size(); i++) {
            barcode[i] = bararray.get(i);
        }
        double lambda = (double) d * (double) (l - k + 1) / (double) l;
        int barlen = barcode[0].length;
        int temlen = barcode.length;
        for (int i = 0; i < barlen; i++) {
            int[] column = new int[temlen];
            for (int j = 0; j < temlen; j++) {
                column[j] = barcode[j][i];
            }
            double score = getInfoScore(column, lambda);
            System.out.println(i + "\t" + score);
        }
    }
    
    public double[] getInfoScoreForBarcodesInCluster(Cluster myCluster) {
    	double[] clusterInfoScore = new double[myCluster.getBarcodesOfThisCluster().size()];
        for (int i = 0; i < clusterInfoScore.length; ++ i) {
        	clusterInfoScore[i] = getInfoScore(myCluster.getBarcodesOfThisCluster().get(i).getBarcode(), 1);
        }
        
        return clusterInfoScore;
    }

    @Override
    public double getInfoScore(int[] vec, int d, int k, int l) {
        double lambda = (double) d * (double) (l - k + 1) / (double) l;
        return getInfoScore(vec, lambda);
    }

    public double getInfoScore(int[] vec, double lambda) {
        int veclen = vec.length;
        int mincopy = vec[0];
        int maxcopy = vec[0];
        for (int i = 0; i < veclen; i++) {
            if (vec[i] < mincopy) {
                mincopy = vec[i];
            }
            if (vec[i] > maxcopy) {
                maxcopy = vec[i];
            }
        }
        int kstart = getKstart(lambda, mincopy);
        int kend = getKend(lambda, maxcopy);
//        System.out.println(lambda + "\t" + kstart + "\t" + kend);
        double score = 0;

        for (int i = 0; i < veclen - 1; i++) {
            int ci = vec[i];
            double lambdai = ci * lambda;
            for (int j = i + 1; j < veclen; j++) {
                int cj = vec[j];
                double lambdaj = cj * lambda;
                if (ci == cj) {
                    continue;
                }
                int bigger = Math.max(ci, cj);
                int smaller = Math.min(ci, cj);
                int[] keys = {mincopy, maxcopy, bigger, smaller};
                if (freq.containsKey(keys)) {
                	score += ((Double) freq.get(keys)).doubleValue();
                    //score += (double) freq.get(keys);
                } else {
                    double value = 0;
                    for (int p = kstart; p <= kend; p++) {
                        double pi = getPoissonProbability(p, lambdai);
                        double pj = getPoissonProbability(p, lambdaj);
                        double Dij = Math.abs(pi - pj);
                        value += Dij;
                        score += Dij;
                    }
                    freq.put(keys, value);
                }
            }
        }
        return score;
    }

    public double[] getInfoScoreVector(int[] vec, double lambda) {
        int veclen = vec.length;
        int mincopy = vec[0];
        int maxcopy = vec[0];
        for (int i = 0; i < veclen; i++) {
            if (vec[i] < mincopy) {
                mincopy = vec[i];
            }
            if (vec[i] > maxcopy) {
                maxcopy = vec[i];
            }
        }
        int kstart = getKstart(lambda, mincopy);
        int kend = getKend(lambda, maxcopy);
//        System.out.println(lambda + "\t" + kstart + "\t" + kend);
        double[] scores = new double[vec.length];

        for (int i = 0; i < veclen; i++) {
            int ci = vec[i];
            double lambdai = ci * lambda;
            double score = 0;
            for (int j = 0; j < veclen; j++) {
                int cj = vec[j];
                double lambdaj = cj * lambda;
                if (i == j) {
                    continue;
                }
                for (int p = kstart; p <= kend; p++) {
                    double pi = getPoissonProbability(p, lambdai);
                    double pj = getPoissonProbability(p, lambdaj);
                    double Dij = Math.abs(pi - pj);
                    score += Dij;
                }
            }
            scores[i] = score;
        }
        return scores;
    }

    private double getPoissonProbability(int k, double lambda) {
        if (lambda == 0) {
            if (k == 0) {
                return 1;
            } else {
                return 0;
            }
        }
        PoissonDistribution p = new PoissonDistribution(lambda);
        return p.probability(k);
    }

    private int getKstart(double lambda, int mincopy) {
        int e = (int) lambda * mincopy;
        int kstart = 0;
        for (int i = 0; i < e; i++) {
            double p = getPoissonProbability(i, e);
            if (p > 0.0001) {
                kstart = i;
            }
        }
        return kstart;
    }

    private int getKend(double lambda, int maxcopy) {
        int e = (int) lambda * maxcopy;
        int kend = 3 * e;
        for (int i = e; i < 3 * e; i++) {
            double p = getPoissonProbability(i, e);
            if (p > 0.0001) {
                kend = i;
            }
        }
        return kend;
    }

    private double[] getInfoVector(int[] vec, double lambda) {
        double[] nullset = new double[vec.length];
        for (int k = 0; k < vec.length; k++) {
            nullset[k] = 0;
        }
        int[] vecset = getSet(vec);
        if (vecset.length == 1) {
            return nullset;
        }
        boolean this01 = (vecset.length == 2 && vecset[0] == 0 && vecset[1] == 1);
//        boolean this02 = (vecset.length == 2 && vecset[0] == 0 && vecset[1] == 2);
        boolean this12 = (vecset.length == 2 && vecset[0] == 1 && vecset[1] == 2);
        boolean this23 = (vecset.length == 2 && vecset[0] == 2 && vecset[1] == 3);

        if (this01) {
            if (this.got01) {
                return this.score01;
            }
        } else if (this12) {
            if (this.got12) {
                return this.score12;
            }
        } else if (this23) {
            if (this.got23) {
                return this.score23;
            }
        }
        int min = vecset[0];
        if (min > 2) {
            return nullset;
            
//            for (int vs : vecset) {
//                if (min > vs) {
//                    min = vs;
//                }
//            }
////      return 0's if all copy numbers are bigger than 2
//            if (min > 2) {
//                double[] alter = new double[vec.length];
//                for (int k = 0; k < vec.length; k++) {
//                    alter[k] = 0;
//                }
//                return alter;
//            }
        }
//        if (vecset.length < 3) {
//            System.out.println();
//            return null;
//        }
//        for (int i = 0; i < vecset.length; i++) {
//            System.out.print(vecset[i] + " ");
//        }
//        System.out.print("\t");
        double[] scores = getInfoScoreVector(vecset, lambda);
//        for (int i = 0; i < scores.length; i++) {
//            System.out.print(scores[i] + " ");
//        }
        double[] result = new double[vec.length];

        for (int i = 0; i < vec.length; i++) {
            int copy = vec[i];
            int index = -1;
            for (int j = 0; j < vecset.length; j++) {
                if (copy == vecset[j]) {
                    index = j;
                    break;
                }
            }
            result[i] = scores[index];
        }
//        for (double r : result) {
//            System.out.print(r + " ");
//        }
//        System.out.println();
        if (!this.got01 && this01) {
            this.score01 = result;
            this.got01 = true;
        } else if (!this.got12 && this12) {
            this.score12 = result;
            this.got12 = true;
        } else if (!this.got23 && this23) {
            this.score23 = result;
            this.got23 = true;
        }
        return result;
    }

    public int[] getSet(int[] vec) {
        Set barSet = new TreeSet();
        for (int v : vec) {
            barSet.add(v);
        }
        int[] result = new int[barSet.size()];
        Object[] o = barSet.toArray();
        for (int i = 0; i < barSet.size(); i++) {
        	result[i] = ((Integer) o[i]).intValue();
            //result[i] = (int) o[i];
        }
        return result;
    }
}
