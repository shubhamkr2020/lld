/*

Problem:
  1. When there are multiple strategies of doing the same thing.
  2. Examples: 
      Payment strategy, notification strategy, cost calculation, limit calculations, report generation (pdf, doc, email, etc), Hashing strategies.
  3. Use this if there are a lot of if else block in the code.

Naive Approach:
  1. Using if else if block or using switch statement hardcode the strategy for certain scenarios.

Violation:
  1. OCP
  2. SRP
  3. Encapsulate what varies

Architecture:
  1. A common strategy interface.
  2. Concrete multiple strategie is-a Strategy
  3. classes using those strategies called Context class has-a strategy
  4. strategies can be set or intitialised according to the requirements.
  5. No dependencies should be present or no if else statements present.

Advantages:
  1. No if else blocks, clean understandable code.
  2. No dependencies, testable separately
  3. all principles preserved

Implementation: */

import java.util.*;
import java.lang.*;
import java.io.*;

class StrategyPattern
{
	public static void main (String[] args) throws java.lang.Exception
	{
	   Notification notification1 = new DebitNotification(
       new EmailNotificationStrategy(), "1000 ruppee debited");
	   notification1.notifyNotification();
	   
	   Notification notification2 = new DebitNotification(
       new EmailNotificationStrategy(), "2000 ruppee debited");
	   notification2.setNotificationStrategy(new PushNotificationStrategy());
	   notification2.notifyNotification();
	}
}

interface NotificationStrategy {
    public void notifyNotification(String message);
}

class EmailNotificationStrategy implements NotificationStrategy {
    public void notifyNotification(String message) {
        System.out.println("notified via EmailNotificationStrategy: " + message);
    }
}

class PushNotificationStrategy implements NotificationStrategy {
    public void notifyNotification(String message) {
        System.out.println("notified via PushNotificationStrategy: " + message);
    }
}

interface Notification {
    public void setNotificationStrategy(NotificationStrategy strategy);
    public void notifyNotification();
}

class DebitNotification implements Notification {
    private NotificationStrategy notificationStrategy;
    private String message;
    
    public DebitNotification(NotificationStrategy strategy, String message) {
        this.notificationStrategy = strategy;
        this.message = message;
    }
    
    @Override
    public void setNotificationStrategy(NotificationStrategy strategy) {
        this.notificationStrategy = strategy;
    }
    
    @Override
    public void notifyNotification() {
        this.notificationStrategy.notifyNotification(this.message);
    }
}

