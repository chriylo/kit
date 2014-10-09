package generator;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.plaf.SliderUI;


/**
 * A class representing the trie data structure.
 * 
 * @author zakov
 *
 */
public class Trie2 implements Trie{

	protected static final byte NO_TRANSITION = (byte) -1;
	protected static final int NO_WORD = -1;

	protected static final byte[] alphabetIxs = new byte['z'];
	protected static final List<Character> alphabet = new ArrayList<Character>();

	static{
		Arrays.fill(alphabetIxs, NO_TRANSITION);
		addLetter('A');
		addLetter('C');
		addLetter('G');
		addLetter('T');
		addLetter('U', alphabetIxs['T']);
	}

	private static void addLetter(char letter) {
		addLetter(letter, (byte) alphabet.size());
		alphabet.add(letter);
	}

	private static void addLetter(char letter, byte ix) {
		alphabetIxs[(""+letter).toLowerCase().charAt(0)] = ix;
		alphabetIxs[(""+letter).toUpperCase().charAt(0)] = ix;
	}

	private static String getWord(byte[] label) {
		String word = "";
		for (int i=0; i<label.length; ++i){
			word += alphabet.get(label[i]);
		}
		return word;
	}


	protected byte[] tmpLabel;

	/**
	 * The number of words represented by the trie.
	 */
	protected int numOfWords;
	protected int numOfStates = 0;
	protected int totalEdgeLength = 0;
	

	protected State root;
	
	/**
	 * A constructor that generates an empty trie.
	 */
	public Trie2(){
		tmpLabel = new byte[0];
		root = new State((byte) -1, null);
		Arrays.fill(root.failTransitions, root);
	}

	/* (non-Javadoc)
	 * @see generator.Trie#size()
	 */
	@Override
	public int size() {
		return numOfWords;
	}
	
	/* (non-Javadoc)
	 * @see generator.Trie#numOfStates()
	 */
	@Override
	public int numOfStates(){
		return numOfStates;
	}
	
	/* (non-Javadoc)
	 * @see generator.Trie#totalEdgeLength()
	 */
	@Override
	public int totalEdgeLength(){
		return totalEdgeLength;
	}

	/* (non-Javadoc)
	 * @see generator.Trie#addWord(java.lang.CharSequence, int, int)
	 */

	@Override
	public int addWord(CharSequence word, int start, int end) {
		setTmpLabel(word, start, end);
		return addWord(root, end-start);
	}
	
	/* (non-Javadoc)
	 * @see generator.Trie#addWord(java.lang.CharSequence)
	 */
	@Override
	public int addWord(CharSequence word) {
		return addWord(word, 0, word.length());
	}

	private void setTmpLabel(CharSequence word, int start, int end) {
		if (tmpLabel.length < end - start + 1){
			tmpLabel = new byte[end - start + 1];
		}
		for (int i = end - start -1; i >=0; --i){
			tmpLabel[i] = alphabetIxs[word.charAt(start + i)];
		}
	}

	protected int addWord(State state, int wordLength) {
		if (state.wordLength == wordLength){
			//the entire word was read
			if (state.wordIx() == NO_WORD){
				// this word is a prefix of a previous word, but not yet 
				// included in the trie and should be added.
				state = new TerminalState(state, numOfWords);
				state.parent.transitions[state.inTransition] = state;
				++numOfWords;
			}
			return state.wordIx();
		}
		else{
			byte transitionIx = tmpLabel[state.wordLength];
			State child = state.transitions[transitionIx];
			if (child == null){
				// a branch leading to a new state should be added
				child = new State(transitionIx, 
						state);
				state.transitions[transitionIx] = child; 
			}
			return addWord(child, wordLength);
		}
	}

	/* (non-Javadoc)
	 * @see generator.Trie#getWordIx(java.lang.CharSequence)
	 */
	@Override
	public int getWordIx(CharSequence word){
		setTmpLabel(word, 0, word.length());
		return getWordIx(root, word.length());
	}


	protected int getWordIx(State state, int wordLength) {
		while (state != null && state.wordLength < wordLength){
			state = state.transitions[tmpLabel[state.wordLength]];
		}
		if (state == null) {
			return NO_WORD;
		}
		else{
			return state.wordIx();
		}
	}

	private int longestCommonPrefix(int start, int end, byte[] label){
		int lcp = 0;
		int maxLength = Math.min(label.length, end-start);
		for (; lcp < maxLength && tmpLabel[start + lcp] == label[lcp]; ++lcp);
		return lcp;
	}

	/* (non-Javadoc)
	 * @see generator.Trie#forEach(java.lang.CharSequence, int, int, generator.IntFunction)
	 */
	@Override
	public void forEach(CharSequence word, int start, int end, IntFunction f){
		setTmpLabel(word, start, end);
		end -= start;
		start = -1; // fake first step.
		byte transition = 0;
		int prevWordLength;
		State curr = root;
		
		while (start < end){
			curr = curr.failTransition(transition);
			++start;
			
			prevWordLength = curr.wordLength;
			curr = curr.progress(start, end);
			int wordIx = curr.wordIx();
			if (wordIx != NO_WORD){
				f.execute(wordIx);
			}
			start += curr.wordLength - prevWordLength;
			transition = tmpLabel[start];
		}
	}

//	private void forEach(State state, int start, int wordLength, IntFunction f) {
//		if (state.wordIx() != NO_WORD){
//			f.execute(state.wordIx());
//		}
//		if (start == wordLength){
//			return;
//		}
//		State next = state.neighbor(tmpLabel[start]);
//		if (next != null){
//			int lcp = next.longestCommonPrefix(tmpLabel, start, wordLength);
//			if (lcp < next.inLabel.length){
//				forEach(state.failTransition(tmpLabel[start]), start+1, wordLength, f);
//			}
//			else{
//				forEach(next, start+lcp, wordLength, f);
//			}
//		}
//		else{
//			forEach(state.failTransition(tmpLabel[start]), start+1, wordLength, f);
//		}
//	}

	//	public void finalize(){
	//		int[] box = new int[2];
	//		setLongestSuffix(0, states.get(0), 0, (byte) 0, box);
	//	}
	//
	//	private void setLongestSuffix(int wordLength, int parentStateIx, 
	//			byte parentTransitionIx, int[] suffixLengthBox) {
	//		State parentState = states.get(parentStateIx);
	//		State childState = states.get(parentState.transitions[parentTransitionIx]);
	//		State suffixState = states.get(parentState.failTransition[parentTransitionIx]);
	//		byte suffixShift = parentState.failTransitionShift[parentTransitionIx];
	//
	//		byte[] edgeLabel = parentState.inLabel[parentTransitionIx];
	//		for (int i=0; i<edgeLabel.length; ++i){
	//			wordLabel.add(edgeLabel[i]);
	//		}
	//		
	//		suffixLengthBox[BOX_STATE] = parentStateIx;
	//		suffixLengthBox[BOX_SHIFT] = edgeLabel.length+1;
	//		
	//		longestSuffix(wordLabel, suffixLengthBox);
	//		
	//		for (int i = alphabet.size()-1; i>=0; --i){
	//
	//		}
	//	}
	//
	//	private State longestSuffix(int wordLength, State state, int[] suffixLengthBox) {
	//		int suffixLength = suffixLengthBox[0];
	//
	//		//		Precondition: 
	//		//		1. The word corresponding to the state is a substring of tmpLabel that   
	//		//		ends at (wordLength - suffixLength). 
	//		//		2. suffixLength > 0. 
	//
	//		//		Postcondition: 
	//		//		1. Precondition 1 is maintained. 
	//		//		2. Either suffixLength = 0 or there is an edge from "state" such that    
	//		//		the remaining suffix is a prefix of the edge label.
	//
	//		byte transitionIx = tmpLabel[wordLength-suffixLength];
	//		byte[] label = state.inLabel[transitionIx];
	//		if (state.transitions[transitionIx] != NO_TRANSITION && 
	//				longestCommonPrefix(wordLength - suffixLength+1, wordLength, label) == label.length){
	//			suffixLengthBox[0] -= label.length+1;
	//			state = states.get(state.transitions[transitionIx]);
	//			if (suffixLengthBox[0] == 0){
	//				return state;
	//			}
	//		}
	//		else{
	//			int nextStateIx = state.failTransition[transitionIx];
	//			if (nextStateIx == NO_TRANSITION){
	//				memoizeFailTransitions(state);
	//				nextStateIx = state.failTransition[transitionIx];
	//			}
	//			suffixLengthBox[0] += state.failTransitionShift[transitionIx];
	//			state = states.get(nextStateIx);
	//		}
	//		return longestSuffix(wordLength, state, suffixLengthBox);
	//	}
	//
	//	private void memoizeFailTransition(State state, int start, int end){
	//		// Precondition: fail transitions are memoized for the parent state. The sub-label of tmpLabel between start and end corresponds to the word represented by the state.
	//		// Postcondition: all fail transitions and corresponding suffix length are memoized for the state.
	//
	//		int[] suffixLengthBox = new int[1];
	//		byte transitionIx = state.inLabel[0];
	//		State prevFailState = null, currFaleState = state.parent, suffixState = null;
	//		int suffixLength = end - state.inLabel.length;
	//
	//		while (suffixState == null){
	//			prevFailState = currFaleState;
	//			transitionIx = tmpLabel[end - suffixLength];
	//			currFaleState = prevFailState.failTransitions[transitionIx];
	//			suffixLength += prevFailState.failTransitionShifts[transitionIx];
	//			suffixState = read(currFaleState, end - suffixLength, end);
	//		}
	//
	//
	//
	//		currFaleState = prevFailState.failTransitions[transitionIx];
	//		suffixState = read(currFaleState, 
	//				end - state.inLabel.length - prevFailState.failTransitionShifts[transitionIx], end);
	//
	//		if (suffixState == null){
	//			suffixLength = end - state.inLabel.length;
	//			for (; suffixLength >= 0 && suffixState == null; --suffixLength){
	//				suffixState = read(root, end - suffixLength, end);
	//			}
	//		}
	//		else{
	//
	//		}
	//
	//		for (byte transitionIx = 0; transitionIx < alphabet.size(); ++transitionIx){
	//			tmpLabel[end] = transitionIx;
	//
	//
	//		}
	//	}
	//
	//	private State read(State state, int start, int end){
	//		if (start == end) {
	//			return state;
	//		}
	//		State childState = state.transitions[tmpLabel[start]];
	//		if (childState == null){
	//			return null;
	//		}
	//		else {
	//			int lcp = longestCommonPrefix(start, end, childState.inLabel);
	//			if (lcp < childState.inLabel.length){
	//				if (lcp < end - start){
	//					return null;
	//				}
	//				else{
	//					return state;
	//				}
	//			}
	//			else{
	//				return read(childState, start+childState.inLabel.length, end);
	//			}
	//		}
	//	}

	protected class State{

		protected State parent;
		protected byte inTransition;
		protected byte wordLength; // the length of the word from the root to this state.

		protected State[] transitions;
		protected State[] failTransitions;

		public State(byte inTransition, State parent){
			this.inTransition = inTransition;
			if (parent != null){
				this.parent = parent;
				wordLength = (byte) (parent.wordLength+1);
			}
			transitions = new State[alphabet.size()];
			failTransitions = new State[alphabet.size()];
			++numOfStates;
			++totalEdgeLength;
		}

		public State progress(int start, int end) {
			State curr = this, next = neighbor(tmpLabel[start]);
			while (curr.wordIx() == NO_WORD && start < end && next != null){
				curr = next;
				++start;
				next = curr.neighbor(tmpLabel[start]);
			}
			
			return curr;
		}

		public State neighbor(byte transition) {
			if (transition < 0){
				return null;
			}
			else {
				return transitions[transition];
			}
		}

		public int wordIx(){
			return NO_WORD;
		}

		public String toString(){
			if (parent == null) {
				return "";
			}
			else {
				return parent.toString() + alphabet.get(inTransition); 
			}
		}

//		private State progress(byte[] label, int start, byte lastTransition){
//			if (start == label.length+1){
//				return this;
//			}
//			
//			byte transition;
//			State next;
//			int lcp;
//			if (start < label.length){
//				transition = label[start];
//			}
//			else{
//				transition = lastTransition;
//			}
//			next = neighbor(transition);
//			
//			if (next != null){
//				lcp = next.longestCommonPrefix(label, start, label.length);
//				if (lcp == label.length - start && lcp < next.inTransition.length && 
//						next.inTransition[lcp] == lastTransition){
//					++lcp;
//				}
//
//				if (lcp == next.inTransition.length){
//					return next.progress(label, start + lcp, lastTransition);
//				}
//				else{
//					return subdivide(transition, lcp);
//				}
//			}
//			else{
//				return this;
//			}
//		}

//		private State subdivide(byte transition, int splitLength) {
//			State next = transitions[transition];
//			State newState = new State(Arrays.copyOfRange(next.inTransition, 0, splitLength), this, wordLength + splitLength);
//			totalEdgeLength -= splitLength;
//			transitions[transition] = newState;
//			next.parent = newState;
//			next.inTransition = Arrays.copyOfRange(next.inTransition, splitLength, next.inTransition.length);
//			newState.transitions[next.inTransition[0]] = next;
//			return newState;
//		}

		public State failTransition(byte transitionIx){
			if (transitionIx < 0){
				return root();
			}
			if (failTransitions[transitionIx] == null){
				State suffixState = parent.failTransition(inTransition);
				while (suffixState.transitions[transitionIx] == null){
					suffixState = suffixState.parent.failTransition(inTransition);
				}
				failTransitions[transitionIx] = suffixState.transitions[transitionIx];
			}
			return failTransitions[transitionIx];
		}

		private State root() {
			State curr = this;
			for (; curr.parent != null; curr = curr.parent);
			return curr;
		}

//		private State failTransition(byte firstTransition, byte lastTransition) {
//			State suffixState = failTransition(firstTransition);
//			
//			
//			if (start == firstTransition.length){
//				return failTransition(lastTransition);
//			}
//			else{
//				State failTransition = failTransition(firstTransition[start]);
//				State next = failTransition.progress(firstTransition, start+1, lastTransition);
//				int leftToRead = failTransition.wordLength + firstTransition.length-start - next.wordLength;
//				if (leftToRead == 0){
//					return next;
//				}
//				else{
//					return next.failTransition(firstTransition, lastTransition);
//				}
//			}
//		}
//
//		private int longestCommonPrefix(byte[] label, int start, int end) {
//			int lcp = 0;
//			int max = Math.min(end-start, inTransition.length);
//			for (; lcp<max && label[start+lcp] == inTransition[lcp]; ++lcp);
//			return lcp;
//		}

		public void setWords(String[] words) {
			if (wordIx() == NO_WORD){
				for (byte transition = (byte) (alphabet.size()-1); transition >= 0; --transition){
					if (transitions[transition] != null){
						transitions[transition].setWords(words);
					}
				}
			}
			else{
				words[wordIx()] = this.toString();
			}
		}
	}



	protected class TerminalState extends State{
		protected int wordIx; // the index of the word in the trie that is represented by this state (if there is such a word).

		public TerminalState(byte inTransition, State parent, int wordLength, int wordIx){
			super(inTransition, parent);
			this.wordIx = wordIx;
		}

		public TerminalState(State state, int wordIx){
			this(state.inTransition, state.parent, state.wordLength, wordIx);
			this.transitions = state.transitions;
		}

		public int wordIx(){
			return wordIx;
		}
	}
	
	/* (non-Javadoc)
	 * @see generator.Trie#words()
	 */
	@Override
	public String[] words(){
		String[] words = new String[numOfWords];
		root.setWords(words);
		return words;
	}

	public static void main(String[] args){
		// a simple test
		Trie t = new Trie2();

		//		t.addWord("acguuga");
		//		t.addWord("acgu");
		//		t.addWord("acguggg");
		//		t.addWord("acguuga");
		//		t.addWord("uu");
		//
		//		System.out.println(t.getWordIx("acguuga"));
		//		System.out.println(t.addWord("acguuga"));
		//		System.out.println(t.addWord("acgu"));
		//		System.out.println(t.getWordIx("acguggg"));
		//		System.out.println(t.getWordIx("uu"));
		//		System.out.println(t.getWordIx("aaa"));

		t.addWord("gtcc");
		t.addWord("acgt");
		t.addWord("tcca");

		String word = "aacgtccacgttgtccat";

//		t.forEach(word, 0, word.length(), new IntFunction() {
//
//			@Override
//			public void execute(int x) {
//				System.out.println(x);
//			}
//		});

	}


}
