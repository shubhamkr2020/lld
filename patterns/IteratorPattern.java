/*

Problem:
  1. If there are tasks one after the another to be done like a linked list fashion we can use iterator pattern and iterate over 
      list of similar objects using the interface having the same type of reference inside it.
  2. Example: ATM machine with multiple denomination notes inside it. Find number of notes required for 1000 denomination
                then do the same for 500 denomination and then do the same for 100 denomination

Architecture:
  1. There will be an interface for performing one iteration with one type of denomination
  2. That interface will have a reference of its own just like a linked list pointing to another node
  3. Point to the another ndoe without any dependency.
  4. Perform the operation using one node of the linked list and then pass the execution to the adjacent node till the full 
      task is complete.

Implementation: */

import java.util.*;
import java.lang.*;
import java.io.*;

class IteratorPattern
{
	public static void main (String[] args) throws java.lang.Exception
	{
		Atm atm = new Atm();
		atm.dispatchNotes(7800);
	}
}

class Atm {
    private IAtm d100 = new D100(null, 100);
    private IAtm d500 = new D500(d100, 500);
    private IAtm d2000 = new D2000(d500, 2000);
    private IAtm firstDenom = d2000;
    
    public Atm() {}
    
    public void dispatchNotes(int amount) {
        firstDenom.notesNeeded(amount);
    }
}

interface IAtm {
    public boolean notesNeeded(int amount);
}

abstract class Denomination implements IAtm {
    private IAtm nextDen;
    private int denom;
    
    public Denomination(IAtm nextDen, int denom) {
        this.nextDen = nextDen;
        this.denom = denom;
    }
    
    public boolean notesNeeded(int amount) {
        int numOfNotesNeeded = amount/denom;
        amount = amount % denom;
        
        if (nextDen == null && amount != 0)
            return false;
            
        if (amount == 0) {
            dispatchNotes(numOfNotesNeeded);
            return true;
        }
        
        if(nextDen.notesNeeded(amount)) {
            dispatchNotes(numOfNotesNeeded);
        }
        
        return true;
    }
    
    private void dispatchNotes(int numOfNotesNeeded) {
        System.out.println("Dispatching " + numOfNotesNeeded + " of " + denom);
    }
}

class D100 extends Denomination implements IAtm {
    public D100(IAtm nextDen, int denom) {
        super(nextDen, denom);
    }
}

class D500 extends Denomination implements IAtm {
    public D500(IAtm nextDen, int denom) {
        super(nextDen, denom);
    }
}

class D1000 extends Denomination implements IAtm {
    public D1000(IAtm nextDen, int denom) {
        super(nextDen, denom);
    }
}

class D2000 extends Denomination implements IAtm {
    public D2000(IAtm nextDen, int denom) {
        super(nextDen, denom);
    }
}


