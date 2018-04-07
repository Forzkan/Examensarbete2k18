package examensarbete.model.utility;

public class WaitHandler {

	
	
	public static void waitForMilliseconds(int t) {
		try {
			Thread.sleep(t);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}
}
