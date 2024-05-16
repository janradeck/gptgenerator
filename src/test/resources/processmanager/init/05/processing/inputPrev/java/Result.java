public class Result {
	private int count;

	public Result() {
		count = 0;
	}

	public void incrementCount() {
#I(Result.java.fragment)#
	}

	public void decrementCount() {
		count--;
	}

	public int getCount() {
		return count;
	}

}