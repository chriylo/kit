package generator;

import java.util.ArrayList;

public interface IntFunction {
	public void execute(int x);
	public void execute(int x, int y);
	public void execute(int x, int y, ArrayList<Integer> z);
}
