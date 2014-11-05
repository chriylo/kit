package generator;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


/**
 * A class representing the trie data structure.
 * 
 * @author zakov
 *
 */
public class Trie3 implements Trie{

	protected static final byte NO_TRANSITION = (byte) -1;
	public static final int NO_WORD = -1;

	protected static final byte[] alphabetIxs = new byte['z'];
	protected static final List<Character> alphabet = new ArrayList<Character>();
	protected static final byte[] complementary;

	static{
		Arrays.fill(alphabetIxs, NO_TRANSITION);
		addLetter('A');
		addLetter('C');
		addLetter('G');
		addLetter('T');
		addLetter('U', alphabetIxs['T']);
		complementary = new byte[alphabet.size()];
		complementary[alphabetIxs['A']] = alphabetIxs['T'];
		complementary[alphabetIxs['C']] = alphabetIxs['G'];
		complementary[alphabetIxs['G']] = alphabetIxs['C'];
		complementary[alphabetIxs['T']] = alphabetIxs['A'];
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

	private int[] shiftBox = {0};

	/**
	 * A constructor that generates an empty trie.
	 */
	public Trie3(){
		tmpLabel = new byte[0];
		root = new State(new byte[0], null, 0);
		root.parent = root;
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

	private void setTmpLabelReversed(CharSequence word, int start, int end) {
		if (tmpLabel.length < end - start + 1){
			tmpLabel = new byte[end - start + 1];
		}
		for (int i = end - start -1; i >=0; --i){
			tmpLabel[i] = complementary[alphabetIxs[word.charAt(end - i - 1)]];
		}
	}


	protected int addWord(State state, int wordLength) {
		if (state.wordLength == wordLength){
			//the entire word was read
			if (state.wordIx == NO_WORD){
				// this word is a prefix of a previous word, but not yet 
				// included in the trie and should be added.
				state.wordIx = numOfWords;
				++numOfWords;
			}
			return state.wordIx;
		}
		else{
			int transitionIx = tmpLabel[state.wordLength];
			if (state.transitions[transitionIx] == null){
				// a branch leading to a new terminal state should be added
				state.transitions[transitionIx] = new State(
						Arrays.copyOfRange(tmpLabel, state.wordLength, wordLength), 
						state, wordLength);
				state.transitions[transitionIx].wordIx = numOfWords;
				++numOfWords; 
				return numOfWords-1;
			}
			else{
				byte[] label = state.transitions[transitionIx].inLabel;
				int lcp = longestCommonPrefix(state.wordLength, wordLength, label);
				if (lcp == label.length){
					// the entire branch label can be read
					return addWord(state.transitions[transitionIx], wordLength);
				}
				else{
					// the transition branch should be split
					State prevChild = state.transitions[transitionIx];
					State newChild = new State(Arrays.copyOfRange(label, 0, lcp), state, state.wordLength+lcp);
					state.transitions[transitionIx] = newChild;
					byte newTransitionIx = label[lcp];
					newChild.transitions[newTransitionIx] = prevChild;
					prevChild.parent = newChild;
					prevChild.inLabel = Arrays.copyOfRange(label, lcp, label.length);
					return addWord(newChild, wordLength);
				}
			}
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
	
	public ArrayList<Integer> getSource(CharSequence word) {
		throw new UnsupportedOperationException();
	}

	protected int getWordIx(State state, int wordLength) {
		if (state.wordLength == wordLength){
			return state.wordIx;
		}
		else{
			byte transitionIx = tmpLabel[state.wordLength];
			if (transitionIx == NO_TRANSITION) return NO_WORD;		
			State childState = state.neighbor(transitionIx);
			if (childState == null || 
					longestCommonPrefix(state.wordLength, wordLength, childState.inLabel) 
					!= childState.inLabel.length){
				return NO_WORD;
			}
			else{
				return getWordIx(childState, wordLength);
			}
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
	public void forEach(CharSequence word, int start, int end, IntFunction f, boolean isReversed){
		if (isReversed){
			setTmpLabelReversed(word, start, end);
		}
		else{
			setTmpLabel(word, start, end);
		}
		forEachDirected(word, start, end, f);
	}

	private void forEachDirected(CharSequence word, int start, int end, IntFunction f){
		end -= start;
		start = -1; //fake first step 
		byte transition = 0, shift;
		int prevWordLength;
		State currState = root;
		shiftBox[0] = 0;

		while (start < end){
			++start;
			shift = currState.failShifts[shiftBox[0]][transition];
			currState = currState.failTransitions[shiftBox[0]][transition];
			shiftBox[0] = shift;

			prevWordLength = currState.wordLength - shiftBox[0];
			currState = currState.progress(start, end, shiftBox);

			if (shiftBox[0] == 0 && currState.wordIx != NO_WORD){
				//System.out.println(currState.wordIx + "\t" + (start - prevWordLength));
				f.execute(currState.wordIx, start - prevWordLength);
			}

			start += currState.wordLength - shiftBox[0] - prevWordLength;
			transition = tmpLabel[start];
		}
	}

	public void finalize(){
		root.failTransitions = new State[1][alphabet.size()];
		root.failShifts = new byte[1][alphabet.size()];
		Arrays.fill(root.failTransitions[0], root);
		Arrays.fill(root.failShifts[0], (byte) 0);

		List<State> stateQueue = new LinkedList<State>(); 
		List<Integer> shiftQueue = new LinkedList<Integer>(); 

		for (int i= alphabet.size()-1; i>=0; --i){
			if (root.transitions[i] != null){
				stateQueue.add(root.transitions[i]);
				shiftQueue.add(0);
			}
		}

		while (!stateQueue.isEmpty()){
			State state = stateQueue.remove(0);
			int shift = shiftQueue.remove(0);
			state.memoizeFailTransitions(shift);
			if (shift < state.inLabel.length-1){
				stateQueue.add(state);
				shiftQueue.add(shift+1);
			}
			else{
				for (int i= alphabet.size()-1; i>=0; --i){
					if (state.transitions[i] != null){
						stateQueue.add(state.transitions[i]);
						shiftQueue.add(0);
					}
				}
			}
		}
	}


	protected class State{

		protected State parent;
		protected byte[] inLabel;
		protected byte wordLength; // the length of the word from the root to this state.
		protected int wordIx; // the index of the word in the trie that is represented by this state (if there is such a word).

		protected State[] transitions;
		protected State[][] failTransitions;
		protected byte[][] failShifts;

		public State(byte[] inLabel, State parent, int wordLength){
			this.inLabel = inLabel;
			this.parent = parent;
			this.wordLength = (byte) wordLength;
			transitions = new State[alphabet.size()];
			++numOfStates;
			totalEdgeLength += inLabel.length;
			wordIx = NO_WORD;
		}

		public State progress(int start, int end, int[] shiftBox) {
			if (start < end){
				if (shiftBox[0] == 0){
					if (wordIx == NO_WORD){
						byte transition = tmpLabel[start];
						State next = neighbor(transition);
						if (next != null){
							shiftBox[0] = next.inLabel.length-1;
							return next.progress(start+1, end, shiftBox);
						}
					}
				}
				else{
					int lcp = longestCommonPrefix(tmpLabel, start, end, shiftBox[0]);
					shiftBox[0] -= lcp;
					if (shiftBox[0] == 0){
						return progress(start + lcp, end, shiftBox);
					}
				}
			}
			return this;
		}

		public State neighbor(byte transition) {
			if (transition < 0){
				return null;
			}
			else {
				return transitions[transition];
			}
		}

		public String toString(){
			if (this == parent) {
				return "";
			}
			else {
				return parent.toString() + getWord(inLabel); 
			}
		}

		private void memoizeFailTransitions(int j){
			if (j == 0){
				failTransitions = new State[inLabel.length][alphabet.size()];
				failShifts = new byte[inLabel.length][alphabet.size()];
			}

			int currShift = inLabel.length - j - 1;
			int suffixShift = inLabel.length-1 - j; 
			State suffixState = this;
			int lastTransition = inLabel[j];

			if (suffixShift >= suffixState.inLabel.length-1){
				suffixShift = suffixState.parent.failShifts[0][lastTransition];
				suffixState = suffixState.parent.failTransitions[0][lastTransition];
			}
			else{
				suffixShift = suffixState.failShifts[currShift+1][lastTransition];
				suffixState = suffixState.failTransitions[currShift+1][lastTransition];
			}

			if (suffixShift == 0){
				// the word of the suffix state is the longest 
				// proper suffix of the parent word + j label edges
				for (int i = alphabet.size()-1; i >=0; --i){
					if (suffixState.transitions[i] != null){
						failTransitions[currShift][i] = suffixState.transitions[i];
						failShifts[currShift][i] = (byte) (suffixState.transitions[i].inLabel.length-1);
					}
					else{
						failTransitions[currShift][i] = suffixState.failTransitions[0][i];
						failShifts[currShift][i] = suffixState.failShifts[0][i];
					}
				}
			}
			else{
				byte transition = suffixState.inLabel[suffixState.inLabel.length - suffixShift];
				for (int i = alphabet.size()-1; i >=0; --i){
					if (i == transition){
						failTransitions[currShift][i] = suffixState;
						failShifts[currShift][i] = (byte) (suffixShift-1);
					}
					else{
						failTransitions[currShift][i] = suffixState.failTransitions[suffixShift][i];
						failShifts[currShift][i] = suffixState.failShifts[suffixShift][i];
					}
				}
			}
		}

		private int longestCommonPrefix(byte[] label, int start, int end, int shift) {
			int lcp = 0;
			int labelStart = inLabel.length-shift;
			int max = Math.min(end-start, shift);
			for (; lcp<max && label[start+lcp] == inLabel[labelStart + lcp]; ++lcp);
			return lcp;
		}

		public void setWords(String[] words) {
			if (wordIx == NO_WORD){
				for (byte transition = (byte) (alphabet.size()-1); transition >= 0; --transition){
					if (transitions[transition] != null){
						transitions[transition].setWords(words);
					}
				}
			}
			else{
				words[wordIx] = this.toString();
			}
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
		Trie3 t = new Trie3();

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

		t.finalize();

		String word = "gtccgacgtccacgttgtcca";

		final List<Integer> ls = new ArrayList<Integer>();

		IntFunction f = new IntFunction() {

			@Override
			public void execute(int x) {
				ls.add(x);
			}

			@Override
			public void execute(int x, int y) {
				ls.add(x);
			}
			
			public void execute(int x, int y, ArrayList<Integer> z) {
				ls.add(x);
			}
		};

		t.forEach(word, 0, word.length(), f);

		System.out.println(ls);

	}

	@Override
	public void forEach(CharSequence word, int start, int end, IntFunction f) {
		forEach(word, start, end, f, false);
	}


}
