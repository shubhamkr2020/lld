/*

Problem:
  1. When we want to decorate one object again and again use this pattern

Architecture
	1. A main object class with its interface
	2. Other Decorator classes implmenting same interface with has-a relationship to interface so that same type of can be passed to it.
	3. Decorator constructor will set the main object in the decorator class and then override the method like getPrice() for the decorated object.

Advantage:
  1. It will help in passing the same decorated object again to a new decoration constructor as the interface is common for all the base object and the decoraters.

Implementation: */

import java.util.*;
import java.lang.*;
import java.io.*;

class DecoratorPattern
{
	public static void main (String[] args) throws java.lang.Exception
	{
	    Pizza newPizza = new BasePizza("chickenPizza");
	    
	    ThinCrustPizza thinCrustPizza = new ThinCrustPizza(newPizza);
	    
	    System.out.println(thinCrustPizza.cost());
	    
	    SausedPizza sausedThinCrustPizza = new SausedPizza(thinCrustPizza);
	    
	    System.out.println(sausedThinCrustPizza.cost());
	}
}

interface Pizza {
    public Integer cost();
}

class BasePizza implements Pizza {
    private String name;
    
    public BasePizza (String name) {
        this.name = name;
    }
    
    @Override
    public Integer cost() {
        return 100;
    }
}

class SausedPizza implements Pizza {
    private Pizza basePizza;
    
    public SausedPizza (Pizza basePizza) {
        this.basePizza = basePizza;
    }
    
    @Override
    public Integer cost() {
        return 20 + basePizza.cost();
    }
}

class ThinCrustPizza implements Pizza {
    private Pizza basePizza;
    
    public ThinCrustPizza (Pizza basePizza) {
        this.basePizza = basePizza;
    }
    
    @Override
    public Integer cost() {
        return 50 + basePizza.cost();
    }
}
