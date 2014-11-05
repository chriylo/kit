package distanceFunction;

import java.util.List;


/**
 * An interface for a distance function between two integer vectors. 
 * 
 * @author zakov
 *
 */
public interface VecDistance {
	
	/**
	 * Computes the distance between two integer vectors. The input vectors are
	 * assumed to have the same length.
	 * 
	 * @param vec0 the first vector.
	 * @param vec1 the second vector.
	 * @return the distance between {@code vec0} and {@code vec1}. 
	 */
	public double distance(int[] vec0, int[] vec1);

	double distance(int[] vec0, int[] vec1, List<Integer> excluded);
}
