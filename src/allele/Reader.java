package allele;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.zip.GZIPInputStream;

import net.sf.samtools.SAMFileReader;
import net.sf.samtools.SAMRecord;

public class Reader {
	
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
}
