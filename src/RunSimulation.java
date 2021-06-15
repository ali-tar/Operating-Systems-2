package molecule.src;


public class RunSimulation {

	/**
	 This class is sent the number of 
	 */
	public static void main(String[] args) {
		int no_hydrogens = Integer.parseInt(args[0]);
		int no_carbons = Integer.parseInt(args[1]);
		
		if(no_hydrogens%8 == 0 && no_carbons%3 == 0){
			System.out.println("Starting simulation with "+no_hydrogens+" H and "+no_carbons + " C");
			
			Carbon[] carbons = new Carbon[no_carbons]; //number of carbons to be bonded
			Hydrogen[] hydrogens = new Hydrogen[no_hydrogens]; //number of propanes to be bonded
			Propane sharedPropane = new Propane(); //use this object to acess semaphores
			
			for (int i=0;i<no_carbons;i++) {
				carbons[i]=new Carbon(sharedPropane); // call constructor - initialising Carbons
				//Number of carbons is stored in carbon class
				carbons[i].start(); // start thread
				//This is where the carbons need to meet up and wait (in run of Carbon class)
			}

			//Same as above: Initialise hydrogens, allocate IDs to Hydrogen objects as well as inform
			//Hydrogen class on number of hydrogens we have to work with
			for (int i=0;i<no_hydrogens;i++) {
				hydrogens[i]= new Hydrogen(sharedPropane);
				hydrogens[i].start();
			}
		}
		else{
			System.out.println("Incorrect number of atoms. There will be leftovers. Try again.");	
			System.exit(0);
		}
		


	}

}
