/*

Problem:
  1. If there are tasks one after the another to be done like a linked list fashion we can use chain of responsibility pattern
  	and iterate over linked list of similar objects using the interface having the same type of reference inside it.
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
        	return true;
        }
        return false;
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


// AI code for full DIP and other principles followed:

import java.util.*;

class ChainOfResponsibilityATM {
    public static void main(String[] args) {
        // 1. Build the chain externally (Dependency Injection - OCP/DIP Compliant)
        IAtm d100 = new DenominationHandler(100, null);
        IAtm d500 = new DenominationHandler(500, d100);
        IAtm d2000 = new DenominationHandler(2000, d500);

        // 2. Inject the head of the chain into the ATM
        Atm atm = new Atm(d2000);

        System.out.println("--- Transaction 1: 7800 ---");
        atm.dispatchNotes(7800);

        System.out.println("\n--- Transaction 2: 750 (Invalid remainder) ---");
        atm.dispatchNotes(750);
    }
}

// Context Class (High-Level Module)
class Atm {
    private final IAtm firstDenomination;

    public Atm(IAtm firstDenomination) {
        this.firstDenomination = firstDenomination;
    }

    public void dispatchNotes(int amount) {
        Map<Integer, Integer> dispatchResult = new LinkedHashMap<>();
        boolean success = firstDenomination.handle(amount, dispatchResult);

        if (success) {
            System.out.println("Dispense successful:");
            dispatchResult.forEach((denom, count) -> 
                System.out.println("-> Dispatching " + count + " notes of " + denom)
            );
        } else {
            System.out.println("Transaction Failed: Cannot dispense exact amount with available denominations.");
        }
    }
}

// Handler Interface
interface IAtm {
    boolean handle(int amount, Map<Integer, Integer> result);
}

// Concrete Handler implementing Chain of Responsibility
class DenominationHandler implements IAtm {
    private final int denomination;
    private final IAtm nextHandler;

    public DenominationHandler(int denomination, IAtm nextHandler) {
        this.denomination = denomination;
        this.nextHandler = nextHandler;
    }

    @Override
    public boolean handle(int amount, Map<Integer, Integer> result) {
        int count = amount / denomination;
        int remainder = amount % denomination;

        // Base case: exact division reached at this level
        if (remainder == 0) {
            if (count > 0) {
                result.put(denomination, count);
            }
            return true;
        }

        // If we hit the end of the chain and still have a remainder, fail
        if (nextHandler == null) {
            return false;
        }

        // Pass the remainder down the chain
        boolean canFulfillDownstream = nextHandler.handle(remainder, result);

        // If downstream successfully fulfilled the remainder, record our count and succeed
        if (canFulfillDownstream) {
            if (count > 0) {
                result.put(denomination, count);
            }
            return true;
        }

        // Downstream failed
        return false;
    }
}

