package distanceFunction;

import java.util.*;
import clusterFunctions.*;


public class HammingClusterBarcodeDistance extends
		EntrywiseClusterBarcodeDistance {
	
	public HammingClusterBarcodeDistance() {
		super();
	}
	
	public HammingClusterBarcodeDistance(EntryWeights weights) {
		super();
	}
	
	protected double entryDistance(String a, String b) {
		if (a.equals(b)) {
			return 0;
		} else {
			return 1;
		}
	}
	
	protected double entryDistance(ClusterScore sampleEntry, String templateEntry) {
		
		if (sampleEntry.getMinCategories().contains(templateEntry)) {
			return 0;
		} else {
			return 1;
		}
		
		//return sampleEntry.get(templateEntry);
	}

}
