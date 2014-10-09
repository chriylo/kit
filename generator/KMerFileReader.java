package generator;

import generator.Trie4.State;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.NoSuchElementException;
import net.sf.samtools.SAMFileReader;
import net.sf.samtools.SAMRecord;

public class KMerFileReader {

	//	public static final Pattern FASTA_ITEM = Pattern.compile("(>[^\\r\\n]*[\\r\\n]+)+[nN]*([^>]*[^>nN\\s])");
	public static final Pattern FASTA_ITEM = Pattern.compile("(>[^\\r\\n]*[\\r\\n]+)+([^>]+)");
	public static final int FASTA_ITEM_ANNTOATION_GROUP = 1;
	public static final int FASTA_ITEM_SEQUENCE_GROUP = 2;

	//	public static final Pattern nTrimer = Pattern.compile("[nN]*(.*[^>nN])[nN]*");
	public static final Pattern seqSpliter = Pattern.compile("[acgtuACGTU]+");
	public static final int N_TRIMED_GROUP = 1;
	
	public static Iterator<CharSequence> samSeqIterator(String path) throws NoSuchElementException {
		
		File f = new File(path);
		
		SAMFileReader sam = new SAMFileReader(f);
		sam.setValidationStringency(SAMFileReader.ValidationStringency.LENIENT);
		
		final Iterator it = sam.iterator();
		final StringBuilder sb = new StringBuilder();
		
		return new Iterator<CharSequence>() {

			@Override
			public void remove() {
				throw new UnsupportedOperationException(); 
			}

			@Override
			public CharSequence next() {
				sb.setLength(0);
				try {
					SAMRecord rec = (SAMRecord) it.next();
					String rs = rec.getReadString();
					sb.append(rs);
				} //catch (IOException e) {
				catch (NoSuchElementException e) {
					throw new RuntimeException(e.getMessage());
				}
				//System.out.println(sb);
				return sb;
			}

			@Override
			public boolean hasNext() {
				return it.hasNext();
			}
		};
	}

	public static Iterator<CharSequence> fastaSeqIterator(String path) throws IOException{
		InputStream in;

		if (path.endsWith(".gz")){
			in = new GZIPInputStream(new FileInputStream(path));
		}
		else{
			in = new FileInputStream(path);
		}

		@SuppressWarnings("resource")
		final BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String line = br.readLine();

		//while (line != null && (!line.startsWith(">") || !line.startsWith("@"))){
		while (line != null && (!line.startsWith(">"))){
			line = br.readLine();

		}

		final boolean[] hasNext = {line != null};
		final StringBuilder sb = new StringBuilder();

		return new Iterator<CharSequence>() {

			@Override
			public void remove() {
				throw new UnsupportedOperationException(); 
			}

			@Override
			public CharSequence next() {
				sb.setLength(0);
				try {
					String line = br.readLine();
					while (line != null && (!line.startsWith(">"))){
					//while (line != null && (!line.startsWith(">") || !line.startsWith("@"))) {
						sb.append(line.trim());
						line = br.readLine();
					} 
					hasNext[0] = line != null;
				} catch (IOException e) {
					throw new RuntimeException(e.getMessage());
				}
				return sb;
			}

			@Override
			public boolean hasNext() {
				return hasNext[0];
			}
		};

	}

	public static Iterator<CharSequence> fastqSeqIterator(String path) throws IOException{
		InputStream in;

		if (path.endsWith(".gz")){
			in = new GZIPInputStream(new FileInputStream(path));
		}
		else{
			in = new FileInputStream(path);
		}

		@SuppressWarnings("resource")
		final BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String line = br.readLine();
		while (line != null && !line.startsWith("@")){
			line = br.readLine();
		}
		final boolean[] hasNext = {line != null};
		final StringBuilder sb = new StringBuilder();

		return new Iterator<CharSequence>() {

			@Override
			public void remove() {
				throw new UnsupportedOperationException(); 
			}

			@Override
			public CharSequence next() {
				sb.setLength(0);
				try {
					String line = br.readLine();
					while (line != null && !line.startsWith("@")) {
						sb.append(line.trim());
						line = br.readLine();
						line = br.readLine();
						line = br.readLine();
						
//						line = br.readLine();
//						line = br.readLine();
//						line = br.readLine();
//						line = br.readLine();
//						
//						line = br.readLine();
//						line = br.readLine();
//						line = br.readLine();
//						line = br.readLine();
//						
//						line = br.readLine();
//						line = br.readLine();
//						line = br.readLine();
//						line = br.readLine();
					} 
					hasNext[0] = line != null;
				} catch (IOException e) {
					throw new RuntimeException(e.getMessage());
				}
				return sb;
			}

			@Override
			public boolean hasNext() {
				return hasNext[0];
			}
		};

	}
	
	public static Iterator<CharSequence> fastqSeqIteratorDownsample(String path) throws IOException{
		InputStream in;

		if (path.endsWith(".gz")){
			in = new GZIPInputStream(new FileInputStream(path));
		}
		else{
			in = new FileInputStream(path);
		}

		@SuppressWarnings("resource")
		final BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String line = br.readLine();
		while (line != null && !line.startsWith("@")){
			line = br.readLine();
		}
		final boolean[] hasNext = {line != null};
		final StringBuilder sb = new StringBuilder();

		return new Iterator<CharSequence>() {

			@Override
			public void remove() {
				throw new UnsupportedOperationException(); 
			}

			@Override
			public CharSequence next() {
				sb.setLength(0);
				try {
					String line = br.readLine();
					while (line != null && !line.startsWith("@")) {
						sb.append(line.trim());
						line = br.readLine();
						line = br.readLine();
						line = br.readLine();
						
						line = br.readLine();
						line = br.readLine();
						line = br.readLine();
						line = br.readLine();
						
						line = br.readLine();
						line = br.readLine();
						line = br.readLine();
						line = br.readLine();
						
						line = br.readLine();
						line = br.readLine();
						line = br.readLine();
						line = br.readLine();
					} 
					hasNext[0] = line != null;
				} catch (IOException e) {
					throw new RuntimeException(e.getMessage());
				}
				return sb;
			}

			@Override
			public boolean hasNext() {
				return hasNext[0];
			}
		};

	}


	public static void extractKMersFile(String path, int k, Trie t) throws IOException{

		//		long start = System.currentTimeMillis();
		//		int time;
		//
		//		int words = 0;//, initialSize = tries.size();

		Iterator<CharSequence> iterator;
		if ((path.endsWith("q")) || (path.endsWith("q.gz"))) {
			iterator = fastqSeqIterator(path);
		} else {
			iterator = fastaSeqIterator(path);
		} 

		int start, end, currEnd;

		while (iterator.hasNext()){
			CharSequence cs = iterator.next();
			Matcher matcher = seqSpliter.matcher(cs);
			while(matcher.find()){
				start = matcher.start();
				end = matcher.end();
				currEnd = 0;

				currEnd = end - k;
				for (int i = start; i <= currEnd; ++i){
					t.addWord(cs, i, i + k); 
					//				++words;
				}
			}
		}
		//		System.err.println("words read: " + words + ", words added: " + (t.size() - initialSize) + ", trie size: " + t.size());
	}
	
	public static void printAllKMersFile(String path, int k, int start, int end) throws IOException{
		Iterator<CharSequence> iterator;
		if ((path.endsWith("q")) || (path.endsWith("q.gz"))) {
			iterator = fastqSeqIterator(path);
		} else {
			iterator = fastaSeqIterator(path);
		}
		
		CharSequence cs = iterator.next();
		Matcher matcher = seqSpliter.matcher(cs);

		matcher.find();
			
		int matcherStart = matcher.start();
		int matcherEnd = matcher.end();
		
		int counter = 0;
		for (int i = start; i <= end-k; i=i+k){
			System.out.println(">"+counter);
			System.out.println((cs.subSequence(i, i+k)).toString());
			counter+=1;
		}
		
//
//
//		t.forEach(cs, matcher.start()+start, matcher.start()+end, indexFunction, false);


//		//int start, end, 
//		int currEnd;
//		int counter = 0;
//		while (iterator.hasNext()){
//			System.out.print("1");
//			CharSequence cs = iterator.next();
//			Matcher matcher = seqSpliter.matcher(cs);
//			while(matcher.find()){
//				start = matcher.start();
//				end = matcher.end();
//				currEnd = 0;
//
//				currEnd = end - k;
//				for (int i = start; i <= currEnd; ++i){
//					//System.out.println(">"+counter);
//					//System.out.println((cs.subSequence(i, i+k)).toString());
//					counter+=1;
//				}
//			}
//		}
	}


	public static int[] countKMersFile(String path, int k, Trie t) throws IOException{

		//int printEvery = 10000000;
		int printEvery = 100;

		final int[] counts = new int[t.size()];
		final long[] found = new long[1];
		IntFunction countFunction = countFunction(counts, found); 

		//		long words = 0;
		int items = 0;

		long startTime = System.currentTimeMillis();
		int time;

		Iterator<CharSequence> iterator;
		
		if ((path.endsWith("q")) || (path.endsWith("q.gz"))) {
			//iterator = fastqSeqIteratorDownsample(path);
			iterator = fastqSeqIterator(path);
		} else if ((path.endsWith("a")) || (path.endsWith("a.gz"))) {
			iterator = fastaSeqIterator(path);
		} else {
			iterator = samSeqIterator(path);
		}

		while (iterator.hasNext()){
			CharSequence cs = iterator.next();
			Matcher matcher = seqSpliter.matcher(cs);
			while(matcher.find()){
				t.forEach(cs, matcher.start(), matcher.end(), countFunction, false);
				t.forEach(cs, matcher.start(), matcher.end(), countFunction, true); // this line counts k-mers on the complementary reversed sequence. 
			}

			
			++items;
			if ((items % printEvery) == 0){
				time = (int) ((System.currentTimeMillis()-startTime)/60000);
				System.err.print("Items read: " + (items/1000000)  
						+ "M, time (min): " + time);
				System.err.println(", " + k + "-mers: " + found[0]);
//				if (items % (printEvery << 6) == 0){
//					System.err.print("Performing garbage collection...");
//					long s = System.currentTimeMillis();
//					System.gc();
//					System.err.println(" (" + (System.currentTimeMillis()-s)/1000 + " seconds).");
//				}
			}
		}

		//		System.err.println("words read: " + words);
		return counts;
	}

	/**
	 * 
	 * @param path a path to a .fasta or .fastq file.
	 * @param t k-mer trie.
	 * @param individuals the number of barcodes to generate.
	 * @param samplingProb the probability of a given read to be sampled for an 
	 * individual. The coverage of each sampled individual is 
	 * {@code samplingProb *} the coverage of the sequenced individual. 
	 * @return A 2D array {@code barcodes}, where {@code barcodes[i]} is the 
	 * barcode of individual i.
	 * @throws IOException in case the procedure cannot read the file.
	 */
	public static int[][] sampleBarcodes(String path, Trie t, int individuals, double samplingProb) throws IOException{

		int printEvery = 10000000;

		int[][] barcodes = new int[individuals][t.size()];
		final List<Integer> currKMers = new ArrayList<Integer>();
		
		IntFunction addToList = new IntFunction() {
			@Override
			public void execute(int x) {
				currKMers.add(x);
			}

			@Override
			public void execute(int x, int y) {
				currKMers.add(x);
			}
			
			public void execute(int x, int y, ArrayList<Integer> z) {
				currKMers.add(x);
			}
		}; 

		int items = 0;

		long startTime = System.currentTimeMillis();
		int time;

		Iterator<CharSequence> iterator;
		if ((path.endsWith("q")) || (path.endsWith("q.gz"))) {
			iterator = fastqSeqIterator(path);
		} else {
			iterator = fastaSeqIterator(path);
		}

		while (iterator.hasNext()){
			CharSequence cs = iterator.next();
			Matcher matcher = seqSpliter.matcher(cs);
			while(matcher.find()){
				currKMers.clear();
				t.forEach(cs, matcher.start(), matcher.end(), addToList);
				for (int i=0; i<individuals; ++i){
					if (Math.random() < samplingProb){ 
						for (int j = currKMers.size()-1; j >=0; --j){
							++barcodes[i][j];
						}
					}
				}
			}


			++items;
			if ((items % printEvery) == 0){
				time = (int) ((System.currentTimeMillis()-startTime)/60000);
				System.err.print("Items read: " + (items/1000000)  
						+ "M, time (min): " + time);
//				if (items % (printEvery << 6) == 0){
//					System.err.print("Performing garbage collection...");
//					long s = System.currentTimeMillis();
//					System.gc();
//					System.err.println(" (" + (System.currentTimeMillis()-s)/1000 + " seconds).");
//				}
			}
		}
		return barcodes;
	}

	private static IntFunction countFunction(final int[] counts,
			final long[] found) {
		IntFunction countWords = new IntFunction() {

			@Override
			public void execute(int x) {
				++counts[x];
				++found[0];
			}
			
			@Override
			public void execute(int x, int y) {
				++counts[x];
				++found[0];
			}
			
			public void execute(int x, int y, ArrayList<Integer> z) {
				++counts[x];
				++found[0];
			}

		};
		return countWords;
	}

	public static int[] indexKMersFile(String path, int k, Trie t) throws IOException{


		Iterator<CharSequence> iterator;
		if ((path.endsWith("q")) || (path.endsWith("q.gz"))) {

			iterator = fastqSeqIterator(path);
		} else {
			iterator = fastaSeqIterator(path);
		}
		
		CharSequence cs = iterator.next();
		Matcher matcher = seqSpliter.matcher(cs);

		matcher.find();
		
		final int[] indices = new int[matcher.end()-matcher.start()];
		IntFunction indexFunction = indexFunction(indices);
			

		t.forEach(cs, matcher.start(), matcher.end(), indexFunction, false);

		//		System.err.println("words read: " + words);
		return indices;
	}
	
	public static int[] indexKMersFile(String path, int k, Trie t, int start, int end) throws IOException{


		Iterator<CharSequence> iterator;
		if ((path.endsWith("q")) || (path.endsWith("q.gz"))) {

			iterator = fastqSeqIterator(path);
		} else {
			iterator = fastaSeqIterator(path);
		}
		
		CharSequence cs = iterator.next();
		Matcher matcher = seqSpliter.matcher(cs);

		matcher.find();
		
		final int[] indices = new int[end-start];
		IntFunction indexFunction = indexFunction(indices);
			
		int matcherStart = matcher.start();
		t.forEach(cs, matcher.start()+start, matcher.start()+end, indexFunction, false);

		//		System.err.println("words read: " + words);
		return indices;
	}
	
	private static IntFunction indexFunction(final int[] indices) {
		Arrays.fill(indices, -1);
		IntFunction index = new IntFunction() {
			@Override
			public void execute(int x) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public void execute(int x, int y) {
				indices[y] = x;
			}
			
			public void execute(int x, int y, ArrayList<Integer> z) {
				indices[y] = x;
			}

		};
		return index;
	}

	public static Trie extractKMersDir(File dir, int k) throws IOException{
		Trie3 t = new Trie3();
		String[] fileNames = dir.list();
		Arrays.sort(fileNames);
		for (int i = 0; i < fileNames.length; ++i){
			String fileName = fileNames[i];
			extractKMersFile(dir.getAbsolutePath() + File.separatorChar + fileName, k, t);
		}
		t.finalize();
		return t;
	}

	public static int[] countKMersDir(String path, int k, Trie t) throws IOException{
		int[] counts = new int[t.size()];

		File dir = new File(path);
		String[] fileNames = dir.list();
		Arrays.sort(fileNames);
		for (int j = 0; j < fileNames.length; ++j){
			String fileName = fileNames[j];
			if (fileName.endsWith(".fa")) {
				System.out.println(fileName);
				int[] countsFile = countKMersFile(dir.getAbsolutePath() + File.separatorChar + fileName, k, t);

				for (int i=0; i<counts.length; ++i) {
					counts[i] += countsFile[i];
				}
			}
		}
		return counts;
	}


	public static int countItems(String path) throws IOException{
		int items = 0;
		int printEvery = 1000000;

		Scanner sc = new Scanner(new GZIPInputStream(new FileInputStream(path)));
		long start = System.currentTimeMillis();
		int time;

		while (sc.findWithinHorizon(FASTA_ITEM, 0) != null){
			++items;
			if ((items % printEvery) == 0){
				time = (int) ((System.currentTimeMillis()-start)/60000);
				System.err.println("Items read: " + (items/1000000)  +"M, time (min): " + time);
			}
		}

		sc.close();
		return items;
	}
	/*
	public static void removeAnnotations(String inputPath, String outputPath) throws IOException{
		int items = 0, megaItems;
		int printEvery = 1000000;

		Scanner sc = new Scanner(new GZIPInputStream(new FileInputStream(inputPath)));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
				new GZIPOutputStream(new FileOutputStream(outputPath))));
		Iterator<CharSequence> iterator = fastaSeqIterator(inputPath);

		long start = System.currentTimeMillis();
		int time;

		while (iterator.hasNext()){
			++items;
			bw.append(">\n");
			bw.append(iterator.next());
			//			bw.append(sc.match().group(FASTA_ITEM_SEQUENCE_GROUP).replaceAll("\\s*", "").toUpperCase());
			bw.append("\n");
			if ((items % printEvery) == 0){
				bw.flush();
				time = (int) ((System.currentTimeMillis()-start)/60000);
				megaItems = items/1000000;
				System.err.println("Items read: " + megaItems  +"M, time (min): " + time);
			}
		}

		sc.close();
		bw.close();

	}
	 */
	public static void printTrieWordsToFile(String fileName, Trie t) throws IOException {
		PrintStream ps;
		ps = new PrintStream(fileName);
		String[] words = t.words();
		for (int i = 0; i < words.length; ++i){
			ps.println(i + "\t" + words[i]);
		}
		ps.close();
	}
	
	public static void printTrieToFile(String fileName, Trie4 t) throws IOException {
		
		PrintStream ps;
		ps = new PrintStream(fileName);
		State[] states = t.states();
		String[] words = t.words();
		for (int i = 0; i < states.length; ++i){
			ps.print(i + "\t" + words[i] + "\t");
			if (states[i].sources.size() == 0) { ps.println(); }
			else {
				for (int s = 0; s < states[i].sources.size()-1; ++s) {
					ps.print(states[i].sources.get(s) + ",");
				} 
				ps.println(states[i].sources.get(states[i].sources.size()-1)); 
			}
		}
		ps.close();
	}

	public static void printBarcodeToFile(String fileName, int[] counts) throws IOException {
		PrintStream ps;
		ps = new PrintStream(fileName);
		//System.out.println(fileName);
		//System.out.println(Arrays.toString(counts));
		ps.println(Arrays.toString(counts));
		ps.close();
	}

	public static int[] getBarcodeFromFile(String fileName) throws IOException {
		//System.out.println(fileName);
		InputStream in = new FileInputStream(fileName);

		final BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String line = br.readLine();
		line = line.replaceAll("\\[", "");
		line = line.replaceAll("\\]", "");

		String[] lineSplit = line.split(", ");

		int[] barcode = new int[lineSplit.length];
		for (int i=0; i<lineSplit.length; ++i) {
			barcode[i]=Integer.parseInt(lineSplit[i]);
		}

		return barcode; 
	}

	//TODO: Validate
	public static Trie getTrieFromFile(String fileName) throws IOException {

		Trie3 t = new Trie3();
//		fileName = "data/Barcode/readTest.fa";
		InputStream in = new FileInputStream(fileName);

		final BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String line;

		while ((line = br.readLine()) != null) {
			String[] lineSplit = line.split("\\t");
			t.addWord(lineSplit[1]);

		}
		t.finalize();
		return t;


	}
	
	public static Trie4 getTrieFromFileWithSource(String fileName) throws IOException {

		Trie4 t = new Trie4();
//		fileName = "data/Barcode/readTest.fa";
		InputStream in = new FileInputStream(fileName);

		final BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String line;

		while ((line = br.readLine()) != null) {
			String[] lineSplit = line.split("\\t");
			String[] sourceSplit = lineSplit[2].split(",");
			for (int i = 0; i < sourceSplit.length; ++i ) {
				t.addWord(lineSplit[1], Integer.parseInt(sourceSplit[i]));

			}

		}
		t.finalize();
		return t;


	}



	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException{

		//		int[] barcode = getBarcodeFromFile("data/Barcodes/Ref.80.barcod");
		//		System.out.println(Arrays.toString(barcode));
		String dataPath = "data/";
		File templatesDir = new File(dataPath + "Masked/");
		File triesDir = new File(dataPath + "tries/");
		triesDir.mkdir();
		File barcodeDir = new File(dataPath + "barcodes/");
		barcodeDir.mkdir();

		//		String inputPath = "data/PlatinumGenome/NA12877_S1.fa.gz";
		//		String outputPath = "data/PlatinumGenome/NA12877_S1_clean2.fa.gz";
		//		removeAnnotations(inputPath, outputPath);
		//
		//		
		int k = 10;// {36, 50, 76, 100};

		PrintStream ps;

		System.out.print("Building " + k + "-mer trie from " + 
				templatesDir.list().length + " templates in " + templatesDir);
		long start = System.currentTimeMillis();
		Trie t = extractKMersDir(templatesDir, k);
		long time = (System.currentTimeMillis()-start)/1000;
		System.out.print(", time (sec): " + time);
		System.out.println(", words: " + t.size() +  
				", average label length: " + t.totalEdgeLength()/(t.numOfStates()-1.0) + 
				", states per word: " + (t.numOfStates()-0.0)/t.size());
		String trieWordsFile = triesDir.getAbsolutePath() + File.separatorChar + "trie" + k + ".words";
		printTrieWordsToFile(trieWordsFile, t);


		System.out.println("Generating template barcode files. Files are saved to " + barcodeDir.getAbsolutePath());
		for (String fileName : templatesDir.list()){
			System.out.print("Barcoding template " + fileName);
			start = System.currentTimeMillis();
			int[] counts = countKMersFile(templatesDir.getAbsolutePath() + File.separatorChar + fileName, k, t);
			printBarcodeToFile(barcodeDir.getAbsolutePath() + File.separatorChar + fileName +"." + k +".barcod", counts);
			time = (System.currentTimeMillis()-start)/1000;
			System.out.println(", time (sec): " + time);
		}

		String[] files = {"NA12877_S1", "NA12878_S1", "NA12882_S1"};
		for (String inputFile : files){
			String sequencingPath = dataPath + "PlatinumGenome/" + inputFile + ".fa.gz"; 
			String barcodeFile = barcodeDir.getAbsolutePath() + File.separatorChar + inputFile + "." + k + ".barcod";

			System.out.println("Barcoding " + sequencingPath + ". Barcode will be saved in " + barcodeFile);
			start = System.currentTimeMillis();
			int[] counts = countKMersFile(sequencingPath, k, t);
			time = (System.currentTimeMillis()-start)/60000;
			System.out.println("Counting time (min): " + time);

			System.out.println("Saving " + barcodeFile);
			ps = new PrintStream(barcodeFile);
			ps.println(Arrays.toString(counts));
		}
		System.out.println("Done!");
	}
}
