package molecule.src;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Hydrogen extends Thread {

	private static int hydrogenCounter =0; //total number of Hydrogens avaliable for bonding
	private static int bondedH = 0; //static variable to keep track of how many Hydrogens we've bonded
	private int id;
	private Propane sharedPropane;
	public Semaphore hydroSem;
	private Semaphore carbonSem;
	private Semaphore mutex;
	private BarrierReusable barrier;
	
	public Hydrogen(Propane propane_obj) {
		Hydrogen.hydrogenCounter+=1;
		id=hydrogenCounter;
		this.sharedPropane = propane_obj;
		
		hydroSem = sharedPropane.hydrogensQ; //assigning reference to local variable for each referncing 	
		carbonSem = sharedPropane.carbonQ;
		mutex = sharedPropane.mutex; //reference to mutext semaphore
		barrier = sharedPropane.barrier;
	}
	
	public void run() {
	    try {
			//if we have leftovers, continue to try to bond
			if((Hydrogen.hydrogenCounter != Hydrogen.bondedH) && (sharedPropane.getCarbon() != 0)){//Check to see if we've used up all avaliable Carbon and Hydrogen
			 // you will need to fix below
				mutex.acquire(); //wait for individual access to Scoreboard
				sharedPropane.addHydrogen();
				System.out.println("Hydrogen added. Hydrogen level: "+sharedPropane.getHydrogen());
				
				//If we have enough atoms to create our molecule, we release the semaphores for the molecules we need
				if(sharedPropane.getHydrogen() >= 8 && sharedPropane.getCarbon() >= 3){//there are enough waiting hydrogens to make a molecule
					System.out.println("---Group ready for bonding---");
					System.out.println("Inside IF STATEMENT of Hydrogen.");//TODO TRACE
					hydroSem.release(8);
					sharedPropane.removeHydrogen(8);
					carbonSem.release(3);
					sharedPropane.removeCarbon(3);
				}
				//we don't have enough to bond, so we release the lock and wait for someone else to do it.
				else{
					mutex.release();//we don't have enough hydrogen. Let the bad times roll.
					
				}
				hydroSem.acquire();//Since there aren't enough molecules, we allow this thread to wait until it can be freed to form propane

							
				sharedPropane.bond("H"+ this.id);
				System.out.println("After bond() was called in Hydrogen.");//TODO TRACE
				barrier.b_wait();
				mutex.release();
				hydroSem.release();
			}
		}
	   catch (InterruptedException ex) { /* not handling this  */}
	    //System.out.println(" ");
	}

}
