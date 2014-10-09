package distanceFunction;

public class RatioPowerVecDistance extends EntrywiseVecDistance {

    protected double exponent;
    private static final double epsilone = 1;

	public RatioPowerVecDistance(EntryWeights weights, double exponent) {
        super(weights);
        this.exponent = exponent;
    }

    public RatioPowerVecDistance(double exponent) {
        super();
        this.exponent = exponent;
    }

    public RatioPowerVecDistance(EntryWeights weights) {
        super(weights);
        this.exponent = 1;
    }

    public RatioPowerVecDistance() {
        super();
        this.exponent = 1;
    }

    @Override
    protected double entryDistance(int a, int b) {
        double ratio;
        if (a > b) {
            ratio = (a + epsilone) / (b + epsilone);
        } else {
            ratio = (b + epsilone) / (a + epsilone);
        }
        return Math.pow(ratio, exponent);
    }
    
    @Override
    protected double entryDistance(double a, double b) {
    	
        double ratio;
        if (a < b) {
            ratio = (a + epsilone) / (b + epsilone);
        } else {
            ratio = (b + epsilone) / (a + epsilone);
        }
        return 1-Math.pow(ratio, exponent);
    	/*
    	double ratio;
        if (a < b) {
            ratio = 1-((a + epsilone) / (b + epsilone));
        } else {
            ratio = 1-((b + epsilone) / (a + epsilone));
        }
        return Math.pow(ratio, exponent);
        */
    }
	
    
    /*
    @Override
    protected double entryDistanceDouble(double a, int b) {
        double ratio;
        if (a > b) {
            ratio = (a + epsilone) / (b + epsilone);
        } else {
            ratio = (b + epsilone) / (a + epsilone);
        }
        return Math.pow(ratio, exponent);
    }
	*/
    public void setExponent(double exponent) {
        this.exponent = exponent;
    }

    public double[] getEntrywiseDistanceVec(int[] vec0, int[] vec1) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
