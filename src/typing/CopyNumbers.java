package typing;

public class CopyNumbers {
	String copyNumbers;
	int[] intCopyNumbers; 
	double score;
	double scalingFactor;
	double[] entrywiseScores;
	
	public CopyNumbers(int[] copyNumbers, double score, double[] entrywiseScores, double scalingFactor) {
		intCopyNumbers = copyNumbers;

		String strSampleCopyNumbers = "";
		for (int i = 0; i < copyNumbers.length-1; ++i) {
			strSampleCopyNumbers = strSampleCopyNumbers + Integer.toString(copyNumbers[i]) + ",";
		}
		strSampleCopyNumbers = strSampleCopyNumbers + Integer.toString(copyNumbers[copyNumbers.length-1]);
		
		this.copyNumbers = strSampleCopyNumbers;
		this.score = score;
		this.scalingFactor = scalingFactor;
		this.entrywiseScores = entrywiseScores;

	}
	
	public CopyNumbers(int[] copyNumbers, double score, double scalingFactor) {
		intCopyNumbers = copyNumbers;
		String strSampleCopyNumbers = "";
		for (int i = 0; i < copyNumbers.length-1; ++i) {
			strSampleCopyNumbers = strSampleCopyNumbers + Integer.toString(copyNumbers[i]) + ",";
		}
		strSampleCopyNumbers = strSampleCopyNumbers + Integer.toString(copyNumbers[copyNumbers.length-1]);
		
		this.copyNumbers = strSampleCopyNumbers;
		this.score = score;
		this.scalingFactor = scalingFactor;

	}
	
//	public CopyNumbers(String copyNumbers, double score, double scalingFactor) {
//		this.copyNumbers = copyNumbers;
//		this.score = score;
//		this.scalingFactor = scalingFactor;
//
//	}
	
//	public CopyNumbers(String copyNumbers, double score, double[] entrywiseScores, double scalingFactor) {
//		this.copyNumbers = copyNumbers;
//		this.score = score;
//		this.scalingFactor = scalingFactor;
//		this.entrywiseScores = entrywiseScores;
//
//	}
	
	public int[] getIntCopyNumbers() {
		return 	intCopyNumbers;
	}
	
	public String getCopyNumbers() {
		return copyNumbers;
	}
	
	public double getScore() {
		return score;
	}
	
	public double getScalingFactor() {
		return scalingFactor;
	}
	
	public double[] getEntrywiseScores() {
		return entrywiseScores;
	}
}
