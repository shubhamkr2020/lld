/*

Problem:
  1. Decouple the object creation logic from busniness logic.
  2. It is similar to StrategyPattern (Decouple the task execution logic from business logic)
  3. we use FactoryPattern when there is static behavior after the object is created. The strategy won't change at runtime.
      The Object won't have reference to strategies to change it at runtime.
  4. we use StrategyPattern when there is dynamic behaviour. The strategy of task execution changes at runtime even after the object is created.
      like re trigger notification with a different strategy when one fails without creating a new object in the code just by modifying the strategy,
        this needs a strategy reference insdie the object itself. 
  5. In production systems, we should use both.
  6. Factory Provider is used to provide the factories according to the need from a registry/map.

Architecture:
  1. Item or Abstract Item
  2. Factory or Abstract Factory
  3. terminologies: 
    a. Factory Method - If different notification types like email and push needed but for same service like gmail then factory is same
                    and abstract notification
    b. Factory Provider - If multiple factories like gmail and yahoo both have one email notifications then just one factory interface
                    handling the item creation is enough.
    c. Abstract Factory Provider - If both gmail and yahoo is using both email and push notifications then both abstract
  4. Factory pattern still uses if else if block which violates the OCP, so use Factory Provider pattern always in production systems.

Implementation: (Abstract Factory)*/

// 1. Abstract Products (The Items)
interface EmailNotification {
    void sendEmail();
}

interface PushNotification {
    void sendPush();
}

// 2. Concrete Products for Gmail Family
class GmailEmailNotification implements EmailNotification {
    @Override
    public void sendEmail() {
        System.out.println("Sending Gmail Email...");
    }
}

class GmailPushNotification implements PushNotification {
    @Override
    public void sendPush() {
        System.out.println("Sending Gmail Push notification...");
    }
}

// 2b. Concrete Products for Yahoo Family
class YahooEmailNotification implements EmailNotification {
    @Override
    public void sendEmail() {
        System.out.println("Sending Yahoo Email...");
    }
}

class YahooPushNotification implements PushNotification {
    @Override
    public void sendPush() {
        System.out.println("Sending Yahoo Push notification...");
    }
}

// 3. The Abstract Factory Interface
interface NotificationFactory {
    EmailNotification createEmailNotification();
    PushNotification createPushNotification();
}

// 4. Concrete Factory for Gmail Family
class GmailNotificationFactory implements NotificationFactory {
    @Override
    public EmailNotification createEmailNotification() {
        return new GmailEmailNotification();
    }

    @Override
    public PushNotification createPushNotification() {
        return new GmailPushNotification();
    }
}

// 4b. Concrete Factory for Yahoo Family
class YahooNotificationFactory implements NotificationFactory {
    @Override
    public EmailNotification createEmailNotification() {
        return new YahooEmailNotification();
    }

    @Override
    public PushNotification createPushNotification() {
        return new YahooPushNotification();
    }
}

// 5. Client Code
public class AbstractFactoryDemo {
    public static void main(String[] args) {
        // We choose our vendor family once (e.g., based on user configuration or deployment environment)
        NotificationFactory factory = new GmailNotificationFactory();
        // Or if we switch vendors: NotificationFactory factory = new YahooNotificationFactory();

        // The client creates products through the abstract factory, 
        // ensuring they all belong to the same family/vendor safely.
        EmailNotification email = factory.createEmailNotification();
        PushNotification push = factory.createPushNotification();

        email.sendEmail();
        push.sendPush();
    }
}


// Using Abstract Factory Provider

import java.util.HashMap;
import java.util.Map;

// 1. The Product Interface (Item)
interface Notification {
    void notifyUser(String message);
}

// 2. Concrete Products for Gmail Provider
class GmailEmailNotification implements Notification {
    @Override
    public void notifyUser(String message) {
        System.out.println("Gmail Server sending Email: " + message);
    }
}

// 2b. Concrete Products for Yahoo Provider
class YahooEmailNotification implements Notification {
    @Override
    public void notifyUser(String message) {
        System.out.println("Yahoo Server sending Email: " + message);
    }
}

// 3. The Factory Interface (The Provider Contract)
interface NotificationProviderFactory {
    Notification createNotification();
}

// 4. Concrete Provider 1: Gmail Factory
class GmailProviderFactory implements NotificationProviderFactory {
    @Override
    public Notification createNotification() {
        // Here you could also inject Gmail-specific configurations/credentials
        return new GmailEmailNotification();
    }
}

// 4b. Concrete Provider 2: Yahoo Factory
class YahooProviderFactory implements NotificationProviderFactory {
    @Override
    public Notification createNotification() {
        // Here you could inject Yahoo-specific configurations/credentials
        return new YahooEmailNotification();
    }
}

// 5. A Registry / Service Locator to manage our Factory Providers dynamically
class NotificationProviderRegistry {
    private static final Map<String, NotificationProviderFactory> providers = new HashMap<>();

    static {
        // Register default providers at startup
        providers.put("GMAIL", new GmailProviderFactory());
        providers.put("YAHOO", new YahooProviderFactory());
    }

    public static void registerProvider(String providerName, NotificationProviderFactory factory) {
        providers.put(providerName.toUpperCase(), factory);
    }

    public static NotificationProviderFactory getProvider(String providerName) {
        NotificationProviderFactory factory = providers.get(providerName.toUpperCase());
        if (factory == null) {
            throw new IllegalArgumentException("No provider found for: " + providerName);
        }
        return factory;
    }
}

// 6. Client Execution Demo
public class ProviderPatternDemo {
    public static void main(String[] args) {
        // Step A: Client requests the Gmail provider factory dynamically via configuration/string
        String selectedProvider = "GMAIL"; 
        
        NotificationProviderFactory factory = NotificationProviderRegistry.getProvider(selectedProvider);
        
        // Step B: Use the provider factory to produce the item
        Notification notification = factory.createNotification();
        notification.notifyUser("Hello via Factory Provider pattern!");

        // Step C: Switch provider seamlessly to Yahoo without altering core workflow
        NotificationProviderFactory yahooFactory = NotificationProviderRegistry.getProvider("YAHOO");
        Notification yahooNotification = yahooFactory.createNotification();
        yahooNotification.notifyUser("Hello from Yahoo provider!");
    }
}
