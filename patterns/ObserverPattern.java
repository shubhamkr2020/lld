/*

The Problem:
  1. If there are many Observers who need to be notified for a single change, we need a central mechanism where we don't need to change no matter
      how many subscribers or what type of subscribers are added.
  2. The Subscribers and Channel should not be tightly coupled. It should be independently testable.

Naive Approach:
  1. Inside the Channel class itself add all types of Subscribers
  2. Channel and subscribers are tightly coupled

Violations:
  1. Open-Closed Principle: Adding a new type of subscriber requires modification in the channel itself
  2. DIP: Channel depends on the concrete implementation of the Subscribers
  3. Law of Demeter Tell don't Ask: The channel needs to ask for the internal details of the subscribers
  4. Separation of Concern: The core channel is also responsible for updating the different types of subscribers and also responsible for managing the core data it provides

Architecture:
  1. Subject / Channel
  2. Observers / Subscribers
  3. ConcreteSubject and ConcreteObservers

Advantages:
  1. SRP, OCP and DIP all restored.

Implementation: */

import java.util.*;
import java.lang.*;
import java.io.*;

class ObserverPattern
{
	public static void main (String[] args) throws java.lang.Exception
	{
	  Channel goga = new YoutubeChannel("goga");
		
		goga.addSubscriber(new YoutubeChannelSubscriber("1"));
		goga.addSubscriber(new YoutubeChannelSubscriber("2"));
		goga.addSubscriber(new YoutubeChannelSubscriber("3"));
		goga.addSubscriber(new YoutubeChannelSubscriber("4"));
		
		// Triggering the upload to see the observer notification in action
		goga.uploadVideo("Mastering Observer Pattern in Java");
	}
}

interface Channel {
    public void notifySubscribers();
    public void addSubscriber(Subscriber subscriber);
    public void uploadVideo(String title);
}

interface Subscriber {
    public void notifySubscriber();
}

class YoutubeChannel implements Channel {
    private String name;
    private List<Subscriber> subscribers;
    private List<String> videos;
    
    public YoutubeChannel(String name) {
        this.name = name;
        subscribers = new ArrayList<>();
        videos = new ArrayList<>();
    }
    
    @Override
    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }
    
    @Override
    public void notifySubscribers() {
        for(Subscriber subscriber: subscribers) {
            subscriber.notifySubscriber();
        }
    }
    
    @Override
    public void uploadVideo(String videoTitle) {
        System.out.println("uploaded video " + videoTitle);
        videos.add(videoTitle);
        notifySubscribers();
    }
}

class YoutubeChannelSubscriber implements Subscriber {
    private String subscriberId;
    
    public YoutubeChannelSubscriber(String id) {
        this.subscriberId = id;
    }
    
    @Override
    public void notifySubscriber() {
        System.out.println("subscriber " + subscriberId + " notified");
    }
}



/*
Observer Pattern and the Visitor Pattern together to make it more generic for different types of Channels and its notification ways.
*/

import java.util.*;

class ObserverVisitorPattern {
    public static void main(String[] args) {
        // Create our YouTube channel (Subject)
        YoutubeChannel channel = new YoutubeChannel("TechWithGoga");

        // Create subscribers (Visitors)
        EventVisitor subscriber1 = new StandardSubscriber("Alice");
        EventVisitor subscriber2 = new StandardSubscriber("Bob");

        channel.addSubscriber(subscriber1);
        channel.addSubscriber(subscriber2);

        // 1. Publishing a Video event
        channel.publish(new Video("Mastering Visitor Pattern in Java", 15));

        // 2. Publishing an Email notification event through the same infrastructure
        channel.publish(new Email("Weekly Developer Newsletter: Concurrency Tips", "newsletter@goga.com"));
    }
}

// ==========================================
// 1. VISITOR / SUBSCRIBER CONTRACT
// ==========================================
interface EventVisitor {
    void visit(Video video);
    void visit(Email email);
}

// ==========================================
// 2. VISITABLE PAYLOAD CONTRACT (OBSERVABLE)
// ==========================================
interface Event {
    void accept(EventVisitor visitor); // The heart of Double Dispatch
}

// Concrete Payload 1: Video
class Video implements Event {
    private String title;
    private int durationMinutes;

    public Video(String title, int durationMinutes) {
        this.title = title;
        this.durationMinutes = durationMinutes;
    }

    public String getTitle() { return title; }
    public int getDurationMinutes() { return durationMinutes; }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this); // Safely resolves to visit(Video) at runtime
    }
}

// Concrete Payload 2: Email
class Email implements Event {
    private String subject;
    private String sender;

    public Email(String subject, String sender) {
        this.subject = subject;
        this.sender = sender;
    }

    public String getSubject() { return subject; }
    public String getSender() { return sender; }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this); // Safely resolves to visit(Email) at runtime
    }
}

// ==========================================
// 3. SUBJECT / CHANNEL ARCHITECTURE
// ==========================================
interface Channel {
    void addSubscriber(EventVisitor subscriber);
    void removeSubscriber(EventVisitor subscriber);
    void publish(Event event);
}

class YoutubeChannel implements Channel {
    private String name;
    private List<EventVisitor> subscribers = new ArrayList<>();

    public YoutubeChannel(String name) {
        this.name = name;
    }

    @Override
    public void addSubscriber(EventVisitor subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void removeSubscriber(EventVisitor subscriber) {
        subscribers.remove(subscriber);
    }

    @Override
    public void publish(Event event) {
        System.out.println("\n[" + name + " Channel]: New event broadcasted...");
        for (EventVisitor subscriber : subscribers) {
            // Triggers the visitor mechanism; the event routes itself to the correct overloaded method
            event.accept(subscriber);
        }
    }
}

// ==========================================
// 4. CONCRETE SUBSCRIBER (VISITOR IMPLEMENTATION)
// ==========================================
class StandardSubscriber implements EventVisitor {
    private String subscriberName;

    public StandardSubscriber(String subscriberName) {
        this.subscriberName = subscriberName;
    }

    @Override
    public void visit(Video video) {
        System.out.println("  -> [Subscriber " + subscriberName + "] Watched Video: " 
            + video.getTitle() + " (" + video.getDurationMinutes() + " mins)");
    }

    @Override
    public void visit(Email email) {
        System.out.println("  -> [Subscriber " + subscriberName + "] Read Email: " 
            + email.getSubject() + " [From: " + email.getSender() + "]");
    }
}












