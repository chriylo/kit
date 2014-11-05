package distanceFunction;

import generator.KMerFileReader;
import distanceFunction.NormalVecDistance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PresenceVecDistance extends EntrywiseVecDistance {

	public PresenceVecDistance(EntryWeights weights){
		super(weights);
	}

	public PresenceVecDistance(){
		super();
	}

	@Override
	protected double entryDistance(int a, int b) {
		throw new UnsupportedOperationException();
	}

	protected double entryDistance(double a, double b) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public double[] getEntrywiseDistanceVec(int[] vec0, int[] vec1) {
		int[] newvec0 = new int[vec0.length];
		int[] newvec1 = new int[vec1.length];
		for (int i = 0; i < vec0.length; ++i) {
			if (vec0[i]!=0) { newvec0[i] = 10; }
			else { newvec0[i] = 0; } 
			if (vec1[i]!=0) { newvec1[i] = 10; }
			else { newvec1[i] = 0; } 
		} 
	
		return NormalVecDistance.getEntrywiseDistanceVec2(newvec0,newvec1,1);
	}

	public double distance(int[] x, int[] y) {
		int[] newvec0 = new int[x.length];
		int[] newvec1 = new int[y.length];
		for (int i = 0; i < x.length; ++i) {
			if (x[i]!=0) { newvec0[i] = 10; }
			else { newvec0[i] = 0; } 
			if (y[i]!=0) { newvec1[i] = 10; }
			else { newvec1[i] = 0; } 
		} 
		return NormalVecDistance.distance2(newvec0, newvec1, 1);
	}
	

	

	



	

}
