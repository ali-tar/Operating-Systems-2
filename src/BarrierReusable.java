package molecule.src;
import java.util.concurrent.Semaphore;

public class BarrierReusable {

	private int n;
	private int count;
	private Semaphore mutex;
	private Semaphore barrier1;
	private Semaphore barrier2;
	
	/**
	 * Initialises a barrier of size n.
	 * @param n Size of barrier.
	 */
	BarrierReusable(int n) {
		this.n = n; //n = 11 or the number of atoms needed for a Propane molecule
		count=0;
		mutex = new Semaphore(1); //mutual exclusion token
		barrier1 = new Semaphore(0); //barrier closed at start
		barrier2 = new Semaphore(0); //barrier closed at start
	}
	
	/**
	 * Allows all threads waiting at the barrier to be allowed through.
	 * @throws InterruptedException
	 */
	public void phase1() throws InterruptedException{
		mutex.acquire();
			count+=1;
			if (count==n) barrier1.release(n); //unlock first barrier for n threads
		mutex.release();
		barrier1.acquire();
	}
	
	/**
	 * Closes the barrier and resets for reuse.
	 * @throws InterruptedException
	 */
	public void phase2() throws InterruptedException{
		mutex.acquire();
			count-=1;
			if (count==0) barrier2.release(n);  //unlock second  barrier for n threads
		mutex.release();
		barrier2.acquire();
		
	}
	public void b_wait() throws InterruptedException{
		phase1(); //all go through barrier
		phase2(); // all reset the barrier for reuse
	}

}
