/*

Problem: 
  1. When creating an object, encapsulate what varies. 
  2. Do not expose the members to the users. 
  3. Immutable objects are always thread safe.

Naive: 
  1. Use multiple constructors or overloaded constructors for creating the objects but this is not much readable.
  2. Also you can use setter methods directly in the Product class while having empty consturtor, this will expose the members and will not be immutable thread safe object

Violations:
  1. Encapsulate what varies violated.
  2. Separation of Concern, SOLID principles and KISS principle violated.

Architecture:
  1. An actual Product class
  2. A public static inner ProductBuilder class
  3. Product has private final members and private constructor with one Builder parameter only
  4. No setters for the Product class but can have getters
  5. ProductBuilder class will have setters such that this is returned so that another setter can be called in the same line while building
  6. ProductBuilder will have a public build method which returns the Product builded using the ProductBuilder 'this' passed in its constructor

Advantages:
  1. Encapsulation & Immutability
  2. KISS and Seperation of Concern followed

Implementation:       */
import java.util.*;
import java.lang.*;
import java.io.*;

class BuilderPattern
{
	public static void main (String[] args) throws java.lang.Exception
	{
		Computer comp = new Computer.ComputerBuilder()
		                    .setName("name")
		                    .setRam("ram")
		                    .setSpeaker("speaker")
		                    .build();
		System.out.println(comp.getName());
		System.out.println(comp.getRam());

	}
}

class Computer {
    private final String name;
    private final String ram;
    private final String processor;
    private final String speaker;
    
    private Computer(ComputerBuilder computer) {
        this.name = computer.name;
        this.ram = computer.ram;
        this.processor = computer.processor;
        this.speaker = computer.speaker;
    }
    
    public String getName() {
        return this.name;
    }
    public String getRam() {
        return this.ram;
    }
    
    public static class ComputerBuilder{
        private String name;
        private String ram;
        private String processor;
        private String speaker;
        
        public ComputerBuilder() {}
        
        public ComputerBuilder setName(String name) {
            this.name = name;
            return this;
        }
        public ComputerBuilder setRam(String ram) {
            this.ram = ram;
            return this;
        }
        public ComputerBuilder setProcessor(String Processor) {
            this.processor = processor;
            return this;
        }
        public ComputerBuilder setSpeaker(String speaker) {
            this.speaker = speaker;
            return this;
        }
        
        public Computer build() {
            return new Computer(this);
        }
    }
}
