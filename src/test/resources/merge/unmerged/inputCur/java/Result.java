public class Result {
	private int count;

	public Result() {
		count = 0;
	}

//#(Result-increment.java.fragment)#

	public void decrementCount() {
		count--;
	}

	public int getCount() {
		return count;
	}

}