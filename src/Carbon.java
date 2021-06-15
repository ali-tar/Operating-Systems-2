package molecule.src;

import java.util.concurrent.Semaphore;

public class Carbon extends Thread {
	
	private static int carbonCounter =0;//total number of Carbons avaliable for bonding
	private static int bondedC = 0; //keep track of how many Carbons we've successfully bonded
	private int id;
	private Propane sharedPropane;
	private Semaphore carbonSem;
	private Semaphore hydroSem;
	private Semaphore mutex;
	private BarrierReusable barrier;

	public Carbon(Propane propane_obj) {
		Carbon.carbonCounter+=1; //Number of carbons needing to be bonded
		id=carbonCounter; //Which numbereth this object is
		this.sharedPropane = propane_obj; //Access to sephamore co-ordinator

		carbonSem = sharedPropane.carbonQ; //creating reference to semaphore object in Propane for easy referencing 
		hydroSem = sharedPropane.hydrogensQ;
		mutex = sharedPropane.mutex; //reference to mutext semaphore
		barrier = sharedPropane.barrier; //reference to Propane's barrier
	}
	
	public void run() {
	    try {	 
			 // you will need to fix below
			mutex.acquire();
			
			if((Carbon.carbonCounter != Carbon.bondedC) && (sharedPropane.getHydrogen() != 0)){//Check to see if we've used up all avaliable Carbon and Hydrogen
				sharedPropane.addCarbon();
				System.out.println("Carbon added. Carbon level: "+sharedPropane.getCarbon());

				if(sharedPropane.getCarbon() >= 3 && sharedPropane.getHydrogen() >=8){// we have enough carbon to make molecule
					System.out.println("---Group ready for bonding---");
					System.out.println("Inside IF STATEMENT of Carbon.");//TODO TRACE

					carbonSem.release(3); //release 3 threads for bonding
					bondedC += 3; //add 3 carbons to the bonded variable
					sharedPropane.removeCarbon(3);
					hydroSem.release(8);
					sharedPropane.removeHydrogen(8);
				}
				else{ //wait for a different thread to make a molecule
					mutex.release(); //release hold on mutex lock so another thread can pick it up and incriment
								
				}
				
				carbonSem.acquire();//if there aren't enough, decriment and allow this thread to wait to be made into Propane
				System.out.println("acquire called. Carbon "+id+" has the semaphore.");

				//System.out.println("---Group ready for bonding---");	
				sharedPropane.bond("C"+ this.id);  //bond

				System.out.println("After bond() was called in Carbon.");//TODO TRACE

				barrier.b_wait();
				mutex.release();
			}
			else{//we've used up all our carbon
				System.exit(0);
			}

	    }
	    catch (InterruptedException ex) { /* not handling this  */}
	   // System.out.println(" ");
	}

}
