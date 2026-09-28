/*

Problem:
  1. If you do not want to call the method of a class directly from client class then use a proxy class in between to handle the 
      same method and that proxy class will call the method of the target class at the end after doing validations.

Architecture:
  1. Target class and its method
  2. Create an interface for the target class having the target method and implement it in the target class
  3. Use another proxy class and implement the same interface and the method doing some validations and finally calling the 
      target method which should have been called from the client class.

Implementation:*/

import java.util.*;
import java.lang.*;
import java.io.*;

class ProxyPattern
{
	public static void main (String[] args) throws java.lang.Exception
	{
		Client client = new Client();
		client.execute();
	}
}


interface InterfaceTarget {
    public void targetMethod();
}

class TargetClass implements InterfaceTarget {
    public TargetClass() {}
    
    public void targetMethod() {
        System.out.println("called targetMethod of TargetClass");
    }
}

class ProxyClass implements InterfaceTarget {
    InterfaceTarget targetClass = new TargetClass();
    
    public ProxyClass() {}
    
    private void validate() {
        System.out.println("Validations done in proxy class");
    }
    
    public void targetMethod() {
        this.validate();
        targetClass.targetMethod();
    }
}

class Client {
    InterfaceTarget targetUsingProxy = new ProxyClass();
    public Client() {}
    
    public void execute() {
        targetUsingProxy.targetMethod();
    }
}
