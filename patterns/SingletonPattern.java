/*

Problem:
  1. When you need only one instance of a class everywhere.
  2. It should not allow creating a new instance once created and always accesses the same instance.

Naive:
  1. Inside constructor only initialise the variables with a constant value. - this creates new instance eveeeeeeeytime with same values.

Violates:
  1. SRP: The consumer classes are forced to worry about when and how to instantiate resources rather than focusing purely on business logic.
  2. DIP: High-level modules are tightly coupled to concrete instantiation logic.
  3. DRY

Architecture:
  1. A static same object inside the class
  2. private constructor
  3. A static newInstance() method to return the same instance or create it for the first time.
  4. For thread safe implementation, use volatile static instance and double checked synchronised newInstance method;
  5. Java enum can also be used for thread safe implementation

Implementation: */

import java.util.*;
import java.lang.*;
import java.io.*;

class SingletonPattern
{
	public static void main (String[] args) throws java.lang.Exception
	{
	   SingletonObject one = SingletonObject.newInstance();
	   SingletonObject two = SingletonObject.newInstance();
	   SingletonObject three = SingletonObject.newInstance();
	   
	   System.out.println(one.getId());
	   System.out.println(two.getId());
	   System.out.println(three.getId());
	}
}

class SingletonObject {
    private static SingletonObject newInstance;
    
    private String id;
    
    private SingletonObject () {
        Random r = new Random();
        this.id = String.valueOf(r.nextInt());
    }
    
    public String getId() {
        return this.id;
    }
    
    public static SingletonObject newInstance () {
        if (newInstance == null) {
            newInstance = new SingletonObject();
        }
        return newInstance;
    }
}

// Singleton Objects should always be thread safe: below is the thread safe implementation

import java.util.*;
import java.lang.*;
import java.io.*;

class SingletonPattern
{
	public static void main (String[] args) throws java.lang.Exception
	{
	   SingletonObject one = SingletonObject.newInstance();
	   SingletonObject two = SingletonObject.newInstance();
	   SingletonObject three = SingletonObject.newInstance();
	   
	   System.out.println(one.getId());
	   System.out.println(two.getId());
	   System.out.println(three.getId());
	}
}

class SingletonObject {
    // volatile ensures thread visibility and prevents instruction reordering
    private static volatile SingletonObject newInstance;
    
    private String id;
    
    private SingletonObject () {
        Random r = new Random();
        this.id = String.valueOf(r.nextInt());
    }
    
    public String getId() {
        return this.id;
    }
    
    public static SingletonObject newInstance () {
        if (newInstance == null) { // First check (no locking overhead)
            synchronized (SingletonObject.class) {
                if (newInstance == null) { // Second check (with locking)
                    newInstance = new SingletonObject();
                }
            }
        }
        return newInstance;
    }
}



/*
Java Enum implementation:

1. This can be used when you need a straightforward not extendinble singleton object with absolute thread safety
2. This is not good when we need less memory utilisation, lazy initiliazation not possible with this,

*/

enum SingletonObject {
    INSTANCE;
    
    private final String id;
    
    // Enum constructor is implicitly private
    SingletonObject() {
        Random r = new Random();
        this.id = String.valueOf(r.nextInt());
    }
    
    public String getId() {
        return this.id;
    }
    
    // Wrapper method to match your original code's client invocation API
    public static SingletonObject newInstance() {
        return INSTANCE;
    }
}
