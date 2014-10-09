package allele;

import generator.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import net.sf.samtools.SAMFileReader;
import net.sf.samtools.SAMRecord;
import main.BarcodeBuilder;
import typing.TypePredictor;
import typing.Typing;

public class Preprocessor {
	
	public static final Pattern seqSpliter = Pattern.compile("[acgtuACGTU]+");

	public static Iterator<Record> fastaSeqIterator(String path) throws IOException{
		InputStream in;

		if (path.endsWith(".gz")){
			in = new GZIPInputStream(new FileInputStream(path));
		}
		else{
			in = new FileInputStream(path);
		}

		@SuppressWarnings("resource")
		final BufferedReader br = new BufferedReader(new InputStreamReader(in));

		return new Iterator<Record>() {
			String line = br.readLine();
			String read;
			String name; 
				
			@Override
			public void remove() {
				throw new UnsupportedOperationException(); 
			}

			@Override
			public Record next() {
				read = ""; 
				//sb.setLength(0);
				try {
					name = line.trim();
					line = br.readLine();
					while ((line != null) && !line.startsWith("@") && !line.startsWith("@>") && (!line.startsWith(">"))){
						read = read + line.trim();
						line = br.readLine();
					} 
				} catch (IOException e) {
					throw new RuntimeException(e.getMessage());
				}
				return new Record(name, read);
			}

			@Override
			public boolean hasNext() {
				return line != null;
			}
		};
	}
	
	public static Iterator<PairedRecord> fastaPairedSeqIterator(String path, String path2) throws IOException{
		InputStream in;
		InputStream in2;

		if (path.endsWith(".gz")){
			in = new GZIPInputStream(new FileInputStream(path));
		}
		else{
			in = new FileInputStream(path);
		}
		
		if (path2.endsWith(".gz")){
			in2 = new GZIPInputStream(new FileInputStream(path2));
		}
		else{
			in2 = new FileInputStream(path2);
		}

		@SuppressWarnings("resource")
		final BufferedReader br = new BufferedReader(new InputStreamReader(in));
		final BufferedReader br2 = new BufferedReader(new InputStreamReader(in2));

		return new Iterator<PairedRecord>() {
			String line = br.readLine();
			String read;
			String name; 
			
			String line2 = br2.readLine();
			String read2;
			String name2; 
				
			@Override
			public void remove() {
				throw new UnsupportedOperationException(); 
			}

			@Override
			public PairedRecord next() {
				read = "";
				read2 = "";
				try {
					name = line.trim();
					line = br.readLine();
					while ((line != null) && !line.startsWith("@") && !line.startsWith("@>") && (!line.startsWith(">"))){
						read = read + line.trim();
						line = br.readLine();
					} 
					name2 = line2.trim();
					line2 = br2.readLine();
					while ((line2 != null) && !line2.startsWith("@") && !line2.startsWith("@>") && (!line2.startsWith(">"))){
						read2 = read2 + line2.trim();
						line2 = br2.readLine();
					} 
				} catch (IOException e) {
					throw new RuntimeException(e.getMessage());
				}
				return new PairedRecord(name, read, name2, read2);
			}

			@Override
			public boolean hasNext() {
				return line != null;
			}
		};
	}
	


	public static Iterator<Record> fastqSeqIterator(String path) throws IOException{
		InputStream in;

		if (path.endsWith(".gz")){
			in = new GZIPInputStream(new FileInputStream(path));
		}
		else{
			in = new FileInputStream(path);
		}

		@SuppressWarnings("resource")
		final BufferedReader br = new BufferedReader(new InputStreamReader(in));

		return new Iterator<Record>() {
			int count = 1;
			String line = br.readLine();
			String read;
			String name; 
				
			@Override
			public void remove() {
				throw new UnsupportedOperationException(); 
			}

			@Override
			public Record next() {
				//sb.setLength(0);
				try {
					name = line.trim();
					line = br.readLine();
					count += 1;
					//while ((line != null) && !line.startsWith("@") && !line.startsWith("@>") && (!line.startsWith(">"))){
						read = line.trim();
						line = br.readLine();
						count += 1;
						line = br.readLine();
						count += 1;
						line = br.readLine();
						count += 1;
					//} 
				} catch (IOException e) {
					throw new RuntimeException(e.getMessage());
				}
				return new Record(name, read);
			}

			@Override
			public boolean hasNext() {
				return line != null;
			}
		};

	}
	
	public static Iterator<PairedRecord> fastqPairedSeqIterator(String path, String path2) throws IOException{
		InputStream in;
		InputStream in2;

		if (path.endsWith(".gz")){
			in = new GZIPInputStream(new FileInputStream(path));
		}
		else{
			in = new FileInputStream(path);
		}
		
		if (path2.endsWith(".gz")){
			in2 = new GZIPInputStream(new FileInputStream(path2));
		}
		else{
			in2 = new FileInputStream(path2);
		}

		@SuppressWarnings("resource")
		final BufferedReader br = new BufferedReader(new InputStreamReader(in));
		final BufferedReader br2 = new BufferedReader(new InputStreamReader(in2));

		return new Iterator<PairedRecord>() {
			String line = br.readLine();
			String read;
			String name; 
			
			String line2 = br2.readLine();
			String read2;
			String name2; 
				
			@Override
			public void remove() {
				throw new UnsupportedOperationException(); 
			}

			@Override
			public PairedRecord next() {
				try {
					name = line.trim();
					line = br.readLine();
					//while ((line != null) && !line.startsWith("@") && !line.startsWith("@>") && (!line.startsWith(">"))){
						read = line.trim();
						line = br.readLine();
						line = br.readLine();
						line = br.readLine();
					//}  
					name2 = line2.trim();
					line2 = br2.readLine();
					//while ((line2 != null) && !line2.startsWith("@") && !line2.startsWith("@>") && (!line.startsWith(">"))){
						read2 = line2.trim();
						line2 = br2.readLine();
						line2 = br2.readLine();
						line2 = br2.readLine();
					//} 
				} catch (IOException e) {
					throw new RuntimeException(e.getMessage());
				}
				return new PairedRecord(name, read, name2, read2);
			}

			@Override
			public boolean hasNext() {
				return line != null;
			}
		};
	}
	
	/*
	 * Get Unique K-mers
	 */
	public static Trie getUniqueKmers(TypePredictor predictor, int k, int geneTestIndex, String pathToReferenceBarcode) throws IOException {
		
		PrintStream ps;
		ArrayList<ArrayList<String>> temp = Typing.getGeneTests();
		ps = new PrintStream(temp.get(geneTestIndex).get(0)+"_unique_targets.txt");
		
		//get indices of gene kmer
		int[] reference = KMerFileReader.getBarcodeFromFile(pathToReferenceBarcode);
		//Trie t = BarcodeBuilder.getTrie(pathToTemplates, k);
		Trie t = Typing.t;
		ArrayList<Integer> all = new ArrayList<Integer>();
		ArrayList<ArrayList<String>> geneTests = Typing.getGeneTests();
		ArrayList<Integer> indices = Typing.geneIndicesMap.get(geneTests.get(geneTestIndex));
		for (int i = 0; i < indices.size(); ++i) {
			if (!all.contains(indices.get(i))) {
				all.add(indices.get(i));
			}
		}
		//System.out.println(all.size());
		indices = Typing.geneIndicesMap.get(geneTests.get(geneTestIndex));
		for (int i = 0; i < indices.size(); ++i) {
			if (all.contains(indices.get(i))) {
				all.remove(all.indexOf(indices.get(i)));
			}
		}
			
		//System.out.println(all.size());
		for (int i = 0; i < reference.length; ++i) {
			if (reference[i]>0){
				if (all.contains(i)) {
					all.remove(all.indexOf(i));
				}
			}
		}
		//System.out.println(all.size());

		Trie3 newt = new Trie3();		
		String[] words = t.words();
		for (int a = 0 ; a < all.size(); ++a) {
			newt.addWord(words[all.get(a)]);
			//ps.println(">" + all.get(a));
			//System.out.println(">" + all.get(a));
			//ps.println(words[all.get(a)]);
			//System.out.println(words[all.get(a)]);
			ps.println(all.get(a));
		}
		//ps.close();
		newt.finalize();
		return newt;
		
	}
	

	public static void getReferenceStats(String path, String pathVariants, Trie t, int k) throws IOException {
		Iterator<Record> iterator = fastaSeqIterator(path);
		if ((path.endsWith("q")) || (path.endsWith("q.gz"))) {
			iterator = fastqSeqIterator(path);
		} else if ((path.endsWith("a")) || (path.endsWith("a.gz"))) {
			iterator = fastaSeqIterator(path);
		} 
		//else {
		//	iterator = generator.KMerFileReader.samSeqIterator(path);
		//}
		int start, end, currEnd;
		
		ArrayList<Integer> ref_variants = new ArrayList<Integer>();
		InputStream in = new FileInputStream(pathVariants);
		final BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String line;
		while ((line = br.readLine()) != null) {
			ref_variants.add(Integer.valueOf(line.trim()));
		}
		
		int numNotCovered = 0;
		int total = 0;
		while (iterator.hasNext()){
			Record r = iterator.next();
			String read = r.seq();
			
			for (int v = 0; v < ref_variants.size(); ++v) {
				Boolean covered = Boolean.FALSE;
				start = ref_variants.get(v)-99; if (start<0) { start = 0; }
				end = ref_variants.get(v)+99; if (end>=read.length()-k) { end = read.length()-k-1; }
				currEnd = end - k;
				for (int i = start; i <= currEnd; ++i) {
					if (t.getWordIx(read.subSequence(i, i+k)) != -1) {
						covered = Boolean.TRUE;
					}	
				}
				if (!covered) { System.out.println(ref_variants.get(v)); numNotCovered+= 1; }
				total += 1;
			}
			
//			String name = r.name();
//			Matcher matcher = seqSpliter.matcher(read);
//			while(matcher.find()){
//				start = matcher.start();
//				end = matcher.end();
//				currEnd = 0;
//
//				currEnd = end - k;
//				System.out.println(name);
//				for (int i = start; i <= currEnd; ++i){
//					if (t.getWordIx(read.subSequence(i, i+k)) != -1) {
//						System.out.println(i);
//					}			
//				}
//			}
		}
		System.out.println("Total: " + total + "\tNot Covered: " + numNotCovered);
	}
	
	
	/*
	 * Input: SAM file
	 * Output: Fasta file of filtered reads
	 * TODO
	 */
	public static void filterSAMReads(String path, String indexPath, Trie t, ArrayList<Integer> ks, String outpath, String outpath2, String outpath3) throws IOException {
		File f = new File(path);
		File f2 = new File(indexPath);
		SAMFileReader sam = new SAMFileReader(f, f2);
		SAMFileReader sam2 = new SAMFileReader(f,f2);
		sam.setValidationStringency(SAMFileReader.ValidationStringency.LENIENT);
		sam2.setValidationStringency(SAMFileReader.ValidationStringency.LENIENT);
		
		Iterator iterator = sam.iterator();
		
		int start, end, currEnd;
		
		PrintStream ps, ps2, ps3;
		ps = new PrintStream(outpath);
		ps2 = new PrintStream(outpath2);
		ps3 = new PrintStream(outpath3);
		
		ArrayList<String> filteredReadNames = new ArrayList<String>();
		
		while (iterator.hasNext()){
			SAMRecord rec = (SAMRecord) iterator.next();
			String read = rec.getReadString();
			String readNameString = rec.getReadName();
			int tempIndx = readNameString.indexOf('_');
			readNameString = readNameString.substring(tempIndx+1, readNameString.length());
			if (!filteredReadNames.contains(readNameString)) {
				for (int kindex = 0; kindex < ks.size(); ++kindex) {
					int k = ks.get(kindex);
					for (int i = 0; i < read.length()-k; ++i) {
						if (t.getWordIx(read.subSequence(i, i+k)) != -1) {					
							SAMRecord mate = sam2.queryMate(rec);
							filteredReadNames.add(readNameString);
							if (mate != null) {
								String mateNameString = mate.getReadName();
								int tempIndx2 = mateNameString.indexOf('_');
								mateNameString = mateNameString.substring(tempIndx+1, mateNameString.length());
								filteredReadNames.add(mateNameString);
								ps.println(">" + readNameString + "/1");
								ps.println(read);
								ps2.println(">" + mateNameString + "/2");
								ps2.println(mate.getReadString());
							}
							else {
								ps3.println(">" + readNameString);
							}
							break;
						}	
					}
				}
			}
		}
		
		sam.close();
		
		ps.close();
		ps2.close();
		ps3.close();
		
	}
	
	public static void SAMtoFasta(String path, String indexPath, String fasta) throws FileNotFoundException {
		File f = new File(path);
		File f2 = new File(indexPath);
		SAMFileReader sam = new SAMFileReader(f, f2);
		sam.setValidationStringency(SAMFileReader.ValidationStringency.LENIENT);

		PrintStream ps = new PrintStream(fasta);

		Iterator iterator = sam.iterator();
		while (iterator.hasNext()){
			SAMRecord rec = (SAMRecord) iterator.next();
			String read = rec.getReadString();
			String readNameString = rec.getReadName();
			ps.println(">" + readNameString);
			ps.println(read);
		}

	}
	
	public static void filterSAMReadsFastNoMate(String path, String indexPath, Trie t, int k , String outdir, String name) throws IOException {
		File f = new File(path);
		File f2 = new File(indexPath);
		SAMFileReader sam = new SAMFileReader(f, f2);
		SAMFileReader sam2 = new SAMFileReader(f,f2);
		sam.setValidationStringency(SAMFileReader.ValidationStringency.LENIENT);
		sam2.setValidationStringency(SAMFileReader.ValidationStringency.LENIENT);
		
		Iterator iterator = sam.iterator();
		
		int start, end, currEnd;
		
		HashMap<Integer, ArrayList<PrintStream>> psMap = new HashMap<Integer, ArrayList<PrintStream>>();

		
		ArrayList<String> filteredReadNames = new ArrayList<String>();
		
		final int[] good = new int[1];
		final ArrayList<Integer> sources = new ArrayList<Integer>();
		IntFunction myFunction = myFunction(good, sources); 
		
		while (iterator.hasNext()){
			SAMRecord rec = (SAMRecord) iterator.next();
			String read = rec.getReadString();
			String readNameString = rec.getReadName();
			//int tempIndx = readNameString.indexOf('_');
			//readNameString = readNameString.substring(tempIndx+1, readNameString.length());
			
//			if (!filteredReadNames.contains(readNameString)) {
				Matcher matcher = seqSpliter.matcher(read);
				good[0] = 0;
				sources.clear();
				while(matcher.find()){
					t.forEach(read, matcher.start(), matcher.end(), myFunction);
				}
				if (good[0] == 1) {
//					filteredReadNames.add(readNameString);
//					SAMRecord mate = sam2.queryMate(rec);
//					if (mate != null) {
//						String mateRead = mate.getReadString();
//						String mateNameString = mate.getReadName();
//						filteredReadNames.add(mateNameString);
//						matcher = seqSpliter.matcher(mateRead);
//						while(matcher.find()){
//							t.forEach(mateRead, matcher.start(), matcher.end(), myFunction);
//						}
//						
//						for (int i = 0; i < sources.size(); ++i) {
//							Integer s  = sources.get(i);
//							if (!psMap.containsKey(s)) { 
//								psMap.put(s, new ArrayList<PrintStream>()); 
//								psMap.get(s).add(new PrintStream(outdir + s + "." + name + "_fir.fa"));
//								psMap.get(s).add(new PrintStream(outdir + s + "." + name + "_sec.fa"));
//								psMap.get(s).add(new PrintStream(outdir + s + "." + name + ".fa"));
//							}
//							psMap.get(s).get(0).println(">" + readNameString + "/1");
//							psMap.get(s).get(0).println(read);
//							psMap.get(s).get(1).println(">" + mateNameString + "/2");
//							psMap.get(s).get(1).println(mateRead);
//						}
//					} else {
						for (int w = 0; w < sources.size(); ++w) {
							Integer s  = sources.get(w);
							if (!psMap.containsKey(s)) { 
								psMap.put(s, new ArrayList<PrintStream>()); 
								psMap.get(s).add(new PrintStream(outdir + s + "." + name + "_fir.fa"));
								psMap.get(s).add(new PrintStream(outdir + s + "." + name + "_sec.fa"));
								psMap.get(s).add(new PrintStream(outdir + s + "." + name + ".fa"));
							}
							psMap.get(s).get(2).println(">" + readNameString);
							psMap.get(s).get(2).println(read);
						}
//					}
				}
//			}
		}
		
		sam.close();
		
		
	}
	
	/*
	 * Input: SAM file
	 * Output: Fasta file of filtered reads
	 * TODO
	 */
	public static void filterSAMReadsFastMate(String path, String indexPath, Trie t, int k , String outdir, String name) throws IOException {
		File f = new File(path);
		File f2 = new File(indexPath);
		SAMFileReader sam = new SAMFileReader(f, f2);
		SAMFileReader sam2 = new SAMFileReader(f,f2);
		sam.setValidationStringency(SAMFileReader.ValidationStringency.LENIENT);
		sam2.setValidationStringency(SAMFileReader.ValidationStringency.LENIENT);
		
		Iterator iterator = sam.iterator();
		
		int start, end, currEnd;
		
		HashMap<Integer, ArrayList<PrintStream>> psMap = new HashMap<Integer, ArrayList<PrintStream>>();

		
//		ArrayList<String> filteredReadNames = new ArrayList<String>();
		
		final int[] good = new int[1];
		final ArrayList<Integer> sources = new ArrayList<Integer>();
		IntFunction myFunction = myFunction(good, sources); 
		
		while (iterator.hasNext()){
			SAMRecord rec = (SAMRecord) iterator.next();
			String read = rec.getReadString();
			String readNameString = rec.getReadName();
			
//			if (!filteredReadNames.contains(readNameString)) {
				Matcher matcher = seqSpliter.matcher(read);
				good[0] = 0;
				sources.clear();
				while(matcher.find()){
					t.forEach(read, matcher.start(), matcher.end(), myFunction);
				}
				if (good[0] == 1) {
//					filteredReadNames.add(readNameString);
					SAMRecord mate = sam2.queryMate(rec);
					if (mate != null) {
						String mateRead = mate.getReadString();
						String mateNameString = mate.getReadName();
//						filteredReadNames.add(mateNameString);
						matcher = seqSpliter.matcher(mateRead);
						while(matcher.find()){
							t.forEach(mateRead, matcher.start(), matcher.end(), myFunction);
						}
						
						for (int i = 0; i < sources.size(); ++i) {
							Integer s  = sources.get(i);
							if (!psMap.containsKey(s)) { 
								psMap.put(s, new ArrayList<PrintStream>()); 
								psMap.get(s).add(new PrintStream(outdir + s + "." + name + "_fir.fa"));
								psMap.get(s).add(new PrintStream(outdir + s + "." + name + "_sec.fa"));
								psMap.get(s).add(new PrintStream(outdir + s + "." + name + ".fa"));
							}
							psMap.get(s).get(0).println(">" + readNameString + "/1");
							psMap.get(s).get(0).println(read);
							psMap.get(s).get(1).println(">" + mateNameString + "/2");
							psMap.get(s).get(1).println(mateRead);
						}
					} else {
						for (int w = 0; w < sources.size(); ++w) {
							Integer s  = sources.get(w);
							if (!psMap.containsKey(s)) { 
								psMap.put(s, new ArrayList<PrintStream>()); 
								psMap.get(s).add(new PrintStream(outdir + s + "." + name + "_fir.fa"));
								psMap.get(s).add(new PrintStream(outdir + s + "." + name + "_sec.fa"));
								psMap.get(s).add(new PrintStream(outdir + s + "." + name + ".fa"));
							}
							psMap.get(s).get(2).println(">" + readNameString);
							psMap.get(s).get(2).println(read);
						}
					}
//				}
			}
		}
			

		sam.close();
		
	}
	
	/*
	 * Input: SAM file
	 * Output: Fasta file of filtered reads
	 * TODO
	 */
	public static void filterSAMReadsFast(String path, String indexPath, Trie t, int k , String outdir, String name) throws IOException {
		File f = new File(path);
		File f2 = new File(indexPath);
		SAMFileReader sam = new SAMFileReader(f, f2);
		SAMFileReader sam2 = new SAMFileReader(f,f2);
		sam.setValidationStringency(SAMFileReader.ValidationStringency.LENIENT);
		sam2.setValidationStringency(SAMFileReader.ValidationStringency.LENIENT);
		
		Iterator iterator = sam.iterator();
		Iterator iterator2 = sam2.iterator();
		
		int start, end, currEnd;
		
		HashMap<Integer, ArrayList<PrintStream>> psMap = new HashMap<Integer, ArrayList<PrintStream>>();

		HashMap<String, SAMRecordWithSource> readsToBePaired = new HashMap<String, SAMRecordWithSource>();
		
		final int[] good = new int[1];
		final ArrayList<Integer> sources = new ArrayList<Integer>();
		IntFunction myFunction = myFunction(good, sources); 
		
		//go through bam first time
		while (iterator.hasNext()){
			SAMRecord rec = (SAMRecord) iterator.next();
			String read = rec.getReadString();
			String readNameString = rec.getReadName();
			
			if (!readsToBePaired.containsKey(readNameString)) {  //mate either isn't good or hasn't been seen 
				Matcher matcher = seqSpliter.matcher(read);
				good[0] = 0;
				sources.clear();
				while(matcher.find()){
					t.forEach(read, matcher.start(), matcher.end(), myFunction);
				}
				if (good[0] == 1) {
					ArrayList<Integer> copyOfSources = new ArrayList<Integer>();
					for (int i = 0; i < sources.size(); ++i) { copyOfSources.add(sources.get(i)); }
					readsToBePaired.put(readNameString, new SAMRecordWithSource(rec, copyOfSources));
				}
			} else { //mate is good, so add
				Matcher matcher = seqSpliter.matcher(read); 
				good[0] = 0;
				sources.clear();
				while(matcher.find()){
					t.forEach(read, matcher.start(), matcher.end(), myFunction);
				}
				
				SAMRecord mate = readsToBePaired.get(readNameString).rec;
				ArrayList<Integer> mateSources = readsToBePaired.get(readNameString).sources;
				for (int i = 0; i < mateSources.size(); ++i) { if (!sources.contains(mateSources.get(i))) {sources.add(mateSources.get(i)); }} //need to get sources of mate
				
				for (int i = 0; i < sources.size(); ++i) {
					Integer s  = sources.get(i);
					if (!psMap.containsKey(s)) { 
						psMap.put(s, new ArrayList<PrintStream>()); 
						psMap.get(s).add(new PrintStream(outdir + s + "." + name + "_fir.fa"));
						psMap.get(s).add(new PrintStream(outdir + s + "." + name + "_sec.fa"));
						psMap.get(s).add(new PrintStream(outdir + s + "." + name + ".fa"));
					}
					psMap.get(s).get(0).println(">" + readNameString + "/1");
					psMap.get(s).get(0).println(read);
					psMap.get(s).get(1).println(">" + readNameString + "/2");
					psMap.get(s).get(1).println(mate.getReadString());
				}
				readsToBePaired.remove(readNameString); 
			}
			
		}
		
		//go through bam one other time to get mates
		if (readsToBePaired.size() > 0) {
			while (iterator2.hasNext()) {
				SAMRecord rec = (SAMRecord) iterator2.next();
				String read = rec.getReadString();
				String readNameString = rec.getReadName();
				if (readsToBePaired.containsKey(readNameString)) { //mate is good, so add
					Matcher matcher = seqSpliter.matcher(read); 
					good[0] = 0;
					sources.clear();
					while(matcher.find()){
						t.forEach(read, matcher.start(), matcher.end(), myFunction);
					}
					
					SAMRecord mate = readsToBePaired.get(readNameString).rec;
					ArrayList<Integer> mateSources = readsToBePaired.get(readNameString).sources;
					for (int i = 0; i < mateSources.size(); ++i) { if (!sources.contains(mateSources.get(i))) {sources.add(mateSources.get(i)); }} //need to get sources of mate
					
					for (int i = 0; i < sources.size(); ++i) {
						Integer s  = sources.get(i);
						if (!psMap.containsKey(s)) { 
							psMap.put(s, new ArrayList<PrintStream>()); 
							psMap.get(s).add(new PrintStream(outdir + s + "." + name + "_fir.fa"));
							psMap.get(s).add(new PrintStream(outdir + s + "." + name + "_sec.fa"));
							psMap.get(s).add(new PrintStream(outdir + s + "." + name + ".fa"));
						}
						psMap.get(s).get(0).println(">" + readNameString + "/1");
						psMap.get(s).get(0).println(read);
						psMap.get(s).get(1).println(">" + readNameString + "/2");
						psMap.get(s).get(1).println(mate.getReadString());
					}
					readsToBePaired.remove(readNameString); 
				}
			}
			
			if (readsToBePaired.size() > 0) { 
				for (Iterator it = readsToBePaired.keySet().iterator(); it.hasNext(); ) {
					SAMRecordWithSource recWithSource = ((SAMRecordWithSource) it.next());
					SAMRecord rec = recWithSource.rec;
					ArrayList<Integer> recSources = recWithSource.sources;
					for (int i = 0; i < sources.size(); ++i) {
						Integer s  = sources.get(i);
						if (!psMap.containsKey(s)) { 
							psMap.put(s, new ArrayList<PrintStream>()); 
							psMap.get(s).add(new PrintStream(outdir + s + "." + name + "_fir.fa"));
							psMap.get(s).add(new PrintStream(outdir + s + "." + name + "_sec.fa"));
							psMap.get(s).add(new PrintStream(outdir + s + "." + name + ".fa"));
						}
						psMap.get(s).get(2).println(">" + rec.getReadName() + "/1");
						psMap.get(s).get(2).println(rec.getReadString());
					}
				}
				//System.out.println("Unpaired reads exist"); 
			}
			
		}
		
	
		sam.close();
	}

	
	/*
	 * Input: Fasta, or Fastq file
	 * Output: Fasta file of filtered reads
	 */
	public static void filterPairedReads(String path, String path2, Trie t, ArrayList<Integer> ks, String outpath, String outpath2) throws IOException {
		Iterator<PairedRecord> iterator = fastaPairedSeqIterator(path,path2);
		if ((path.endsWith("q")) || (path.endsWith("q.gz"))) {
			iterator = fastqPairedSeqIterator(path, path2);
		} else if ((path.endsWith("a")) || (path.endsWith("a.gz"))) {
			iterator = fastaPairedSeqIterator(path, path2);
		} 

		
		PrintStream ps, ps2;
		ps = new PrintStream(outpath);
		ps2 = new PrintStream(outpath2);

		while (iterator.hasNext()){
			boolean goodRead = Boolean.FALSE;
			ArrayList<Integer> sources = new ArrayList<Integer>();
			PairedRecord pr = iterator.next();
			String read1 = pr.seq1();
			String read2 = pr.seq2();
			for (int kindex = 0; kindex < ks.size(); ++kindex) {
			int k = ks.get(kindex);
			for (int i = 0; i < read1.length()-k; ++i) {
				if (t.getWordIx(read1.subSequence(i, i+k)) != -1) {
					goodRead = Boolean.TRUE;
				}	
			}
			for (int i = 0; i < read2.length()-k; ++i) {
				if (t.getWordIx(read2.subSequence(i, i+k)) != -1) {
					goodRead = Boolean.TRUE;
				}	
			}
			}
	
			if (goodRead) {
				//System.out.println(pr.name1());
				ps.println(pr.name1());
				ps.println(pr.seq1());
				ps2.println(pr.name2());
				ps2.println(pr.seq2());
			}
			
		}
		
//		ps.close();
//		ps2.close();
		
	}
	
	public static void filterPairedReadsFastNoMate(String path, String path2, Trie t, int k, String outdir, String name) throws IOException {
		HashMap<Integer, ArrayList<PrintStream>> psMap = new HashMap<Integer, ArrayList<PrintStream>>();
		
		Iterator<Record> iterator = fastaSeqIterator(path);
		if ((path.endsWith("q")) || (path.endsWith("q.gz"))) {
			iterator = fastqSeqIterator(path);
		} else if ((path.endsWith("a")) || (path.endsWith("a.gz"))) {
			iterator = fastaSeqIterator(path);
		} 
		
		final int[] good = new int[1];
		final ArrayList<Integer> sources = new ArrayList<Integer>();
		IntFunction myFunction = myFunction(good, sources); 
		
		while (iterator.hasNext()){
			Record rec = iterator.next();
			Matcher matcher = seqSpliter.matcher(rec.seq);
			good[0] = 0;
			sources.clear();
			while(matcher.find()){
				t.forEach(rec.seq, matcher.start(), matcher.end(), myFunction);
			}
			
			if (good[0] == 1) {
				for (int i = 0; i < sources.size(); ++i) {
					Integer s  = sources.get(i);
					if (!psMap.containsKey(s)) { 
						psMap.put(s, new ArrayList<PrintStream>()); 
						psMap.get(s).add(new PrintStream(outdir + s + "." + name + ".fa"));
					}
					psMap.get(s).get(0).println(rec.name());
					psMap.get(s).get(0).println(rec.seq());
				}
			}
		}
		
		Iterator<Record> iterator2 = fastaSeqIterator(path2);
		if ((path.endsWith("q")) || (path.endsWith("q.gz"))) {
			iterator2 = fastqSeqIterator(path2);
		} else if ((path.endsWith("a")) || (path.endsWith("a.gz"))) {
			iterator2 = fastaSeqIterator(path2);
		} 
		
		
		while (iterator2.hasNext()){
			Record rec = iterator2.next();
			Matcher matcher = seqSpliter.matcher(rec.seq);
			good[0] = 0;
			sources.clear();
			while(matcher.find()){
				t.forEach(rec.seq, matcher.start(), matcher.end(), myFunction);
			}
			
			if (good[0] == 1) {
				for (int i = 0; i < sources.size(); ++i) {
					Integer s  = sources.get(i);
					if (!psMap.containsKey(s)) { 
						psMap.put(s, new ArrayList<PrintStream>()); 
						psMap.get(s).add(new PrintStream(outdir + s + "." + name + ".fa"));
					}
					psMap.get(s).get(0).println(rec.name());
					psMap.get(s).get(0).println(rec.seq());
				}
			}
		}
	}
	
	/*
	 * Input: Fasta, or Fastq file
	 * Output: Fasta file of filtered reads
	 */
	public static void filterPairedReadsFast(String path, String path2, Trie t, int k, String outdir, String name) throws IOException {
		Iterator<PairedRecord> iterator = fastaPairedSeqIterator(path,path2);
		if ((path.endsWith("q")) || (path.endsWith("q.gz"))) {
			iterator = fastqPairedSeqIterator(path, path2);
		} else if ((path.endsWith("a")) || (path.endsWith("a.gz"))) {
			iterator = fastaPairedSeqIterator(path, path2);
		} 

		
		HashMap<Integer, ArrayList<PrintStream>> psMap = new HashMap<Integer, ArrayList<PrintStream>>();

		final int[] good = new int[1];
		final ArrayList<Integer> sources = new ArrayList<Integer>();
		IntFunction myFunction = myFunction(good, sources); 
		
		while (iterator.hasNext()){
			PairedRecord pr = iterator.next();
			String read1 = pr.seq1();
			String read2 = pr.seq2();
			Matcher matcher = seqSpliter.matcher(read1);
			good[0] = 0;
			sources.clear();
			while(matcher.find()){
				t.forEach(read1, matcher.start(), matcher.end(), myFunction);
			}
		
			matcher = seqSpliter.matcher(read2);
			while(matcher.find()){
				t.forEach(read2, matcher.start(), matcher.end(), myFunction);
			}
			
			
			if (good[0] == 1) {
				for (int i = 0; i < sources.size(); ++i) {
					Integer s  = sources.get(i);
					if (!psMap.containsKey(s)) { 
						psMap.put(s, new ArrayList<PrintStream>()); 
						psMap.get(s).add(new PrintStream(outdir + s + "." + name + "_fir.fa"));
						psMap.get(s).add(new PrintStream(outdir + s + "." + name + "_sec.fa"));
					}
					psMap.get(s).get(0).println(pr.name1());
					psMap.get(s).get(0).println(pr.seq1());
					psMap.get(s).get(1).println(pr.name2());
					psMap.get(s).get(1).println(pr.seq2());
				}
			}
		}
		
//		while (iterator.hasNext()){
//			boolean goodRead = Boolean.FALSE;
//			ArrayList<Integer> sources = new ArrayList<Integer>();
//			PairedRecord pr = iterator.next();
//			String read1 = pr.seq1();
//			String read2 = pr.seq2();
//
//			for (int i = 0; i < read1.length()-k; ++i) {
//				if (t.getWordIx(read1.subSequence(i, i+k)) != -1) {
//					goodRead = Boolean.TRUE;
//					ArrayList<Integer> ss = t.getSource(read1.subSequence(i, i+k));
//					for (int s = 0; s < ss.size(); ++s) {
//						if (!sources.contains(ss.get(s))) { sources.add(ss.get(s)); }
//					}
//				}
//				
////				if (t.getWordIx(getReverse(read1.subSequence(i, i+k).toString())) != -1) {
////					goodRead = Boolean.TRUE;
////					ArrayList<Integer> ss = t.getSource(read1.subSequence(i, i+k));
////					for (int s = 0; s < ss.size(); ++s) {
////						if (!sources.contains(ss.get(s))) { sources.add(ss.get(s)); }
////					}
////				}	
//			}
//			for (int i = 0; i < read2.length()-k; ++i) {
//				if (t.getWordIx(read2.subSequence(i, i+k)) != -1) {
//					goodRead = Boolean.TRUE;
//					ArrayList<Integer> ss = t.getSource(read2.subSequence(i, i+k));
//					for (int s = 0; s < ss.size(); ++s) {
//						if (!sources.contains(ss.get(s))) { sources.add(ss.get(s)); }
//					}
//
//				}
//				
////				if (t.getWordIx(getReverse(read2.subSequence(i, i+k).toString())) != -1) {
////					goodRead = Boolean.TRUE;
////					ArrayList<Integer> ss = t.getSource(read1.subSequence(i, i+k));
////					for (int s = 0; s < ss.size(); ++s) {
////						if (!sources.contains(ss.get(s))) { sources.add(ss.get(s)); }
////					}
////				}
//			}
//	
//			if (goodRead) {
//				for (int i = 0; i < sources.size(); ++i) {
//					Integer s  = sources.get(i);
//					if (!psMap.containsKey(s)) { 
//						psMap.put(s, new ArrayList<PrintStream>()); 
//						psMap.get(s).add(new PrintStream(outdir + s + "." + name + "_fir.fa"));
//						psMap.get(s).add(new PrintStream(outdir + s + "." + name + "_sec.fa"));
//					}
//					psMap.get(s).get(0).println(pr.name1());
//					psMap.get(s).get(0).println(pr.seq1());
//					psMap.get(s).get(1).println(pr.name2());
//					psMap.get(s).get(1).println(pr.seq2());
//				}
//			}
//			
//		}
		
		//TODO: close all PrintStreams
		
	}
	
	/*
	 * Input: Fasta, or Fastq file
	 * Output: Fasta file of filtered reads
	 */
	public static void filterReadsFast(String path, Trie t, int k, String outdir, String name) throws IOException {
		
		HashMap<Integer, ArrayList<PrintStream>> psMap = new HashMap<Integer, ArrayList<PrintStream>>();
		
		Iterator<Record> iterator2 = fastaSeqIterator(path);
		if ((path.endsWith("q")) || (path.endsWith("q.gz"))) {
			iterator2 = fastqSeqIterator(path);
		} else if ((path.endsWith("a")) || (path.endsWith("a.gz"))) {
			iterator2 = fastaSeqIterator(path);
		} 
		
		final int[] good = new int[1];
		final ArrayList<Integer> sources = new ArrayList<Integer>();
		IntFunction myFunction = myFunction(good, sources); 
		
		while (iterator2.hasNext()){
			Record rec = iterator2.next();
			Matcher matcher = seqSpliter.matcher(rec.seq);
			good[0] = 0;
			sources.clear();
			while(matcher.find()){
				t.forEach(rec.seq, matcher.start(), matcher.end(), myFunction);
			}
			
			if (good[0] == 1) {
				for (int i = 0; i < sources.size(); ++i) {
					Integer s  = sources.get(i);
					if (!psMap.containsKey(s)) { 
						psMap.put(s, new ArrayList<PrintStream>()); 
						psMap.get(s).add(new PrintStream(outdir + s + "." + name + ".fa"));
					}
					psMap.get(s).get(0).println(rec.name());
					psMap.get(s).get(0).println(rec.seq());
				}
			}
			
//			boolean goodRead = Boolean.FALSE;
//			ArrayList<Integer> sources = new ArrayList<Integer>();
//			Record rec = iterator2.next();
//			String read = rec.seq();
//
//			for (int i = 0; i < read.length()-k; ++i) {
//				if (t.getWordIx(read.subSequence(i, i+k)) != -1) {
//					goodRead = Boolean.TRUE;
//					ArrayList<Integer> ss = t.getSource(read.subSequence(i, i+k));
//					for (int s = 0; s < ss.size(); ++s) {
//						if (!sources.contains(ss.get(s))) { sources.add(ss.get(s)); }
//					}
//				}	
//			}
//			
//			if (goodRead) {
//				for (int i = 0; i < sources.size(); ++i) {
//					Integer s  = sources.get(i);
//					if (!psMap.containsKey(s)) { 
//						psMap.put(s, new ArrayList<PrintStream>()); 
//						psMap.get(s).add(new PrintStream(outdir + s + "." + name + ".fa"));
//					}
//					psMap.get(s).get(0).println(rec.name());
//					psMap.get(s).get(0).println(rec.seq());
//				}
//			}
			
		}
		
		
		//TODO: close all PrintStreams
		
	}
	
	private static IntFunction myFunction(final int[] good, final ArrayList<Integer> sources) {
		
		
		IntFunction myFunction = new IntFunction() {

			@Override
			public void execute(int x) {
				if (x != -1) { good[0] = 1; }
			}
			
			@Override
			public void execute(int x, int y) {
				if (x != -1) { good[0] = 1; }
			}
			
			public void execute(int x, int y, ArrayList<Integer> z) {
				if (x != -1) { good[0] = 1; 
					for (int s = 0; s < z.size(); ++s) {
						if (!sources.contains(z.get(s))) { sources.add(z.get(s)); }
					}
				}

			}

		};
		return myFunction;
	}
	


}
