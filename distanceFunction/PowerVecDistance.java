package distanceFunction;

public class PowerVecDistance extends EntrywiseVecDistance {

    protected double exponent;

    public PowerVecDistance(EntryWeights weights, double exponent) {
        super(weights);
        this.exponent = exponent;
    }

    public PowerVecDistance(double exponent) {
        super();
        this.exponent = exponent;
    }

    public PowerVecDistance(EntryWeights weights) {
        super(weights);
        this.exponent = 1;
    }

    public PowerVecDistance() {
        super();
        this.exponent = 1;
    }

    @Override
    protected double entryDistance(int a, int b) {
        return Math.pow(Math.abs(a - b), exponent);
    }

    public void setExponent(double exponent) {
        this.exponent = exponent;
    }
    
    public double[] getEntrywiseDistanceVec(int[] vec0, int[] vec1) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
    
    
    @Override
    protected double entryDistance(double a, double b) {
        return Math.pow(Math.abs(a - b), exponent);
    }
    
}
