package generator;

import java.util.ArrayList;

public interface Trie {

	/**
	 * 
	 * @return the number of words in the trie.
	 */
	public abstract int size();

	public abstract int numOfStates();

	public abstract int totalEdgeLength();

	/**
	 * Adds a new word to the trie.
	 * 
	 * @param word a word to add to the trie.
	 * @return the index of the word in the trie (if the word already exists in 
	 * the trie the procedure returns its previously given index).
	 */

	public abstract int addWord(CharSequence word, int start, int end);

	public abstract int addWord(CharSequence word);

	/**
	 * Gets the index of a word in the trie.
	 * 
	 * @param word a word to lookup in the trie.
	 * @return the index of the word in the trie, or -1 if the word is not in
	 * the trie.
	 */
	public abstract int getWordIx(CharSequence word);

	public abstract ArrayList<Integer> getSource(CharSequence word);
	
	public abstract void forEach(CharSequence word, int start, int end,
			IntFunction f);

	public abstract void forEach(CharSequence word, int start, int end,
			IntFunction f, boolean isReversed);

	public abstract String[] words();

}