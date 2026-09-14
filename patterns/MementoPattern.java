/*
Also called SNAPSHOT pattern.

Problem:
  1. This pattern is used when we need to save and restore a snapshot for any Product/Originator without breaking encapsulation (i.e.
    without letting other worker classes like snapshotManager see through the actual Product class's contents)
  2. example: textEditor undo redo, DB transactions, canvas for drawing, etc.
  3. Adding the capability to the same Product class violates the SRP, so we should always try to create separate classes for handling these different functionalities.
  4. Snapshot pattern helps you take Immutable snapshot of the Product without exposing the internal detail of the Product to the SnapshotManager. 

Naive:
  1. Leave the Product as it is and add an external snapshot manager.
  2. Expose the internal details of the Product to external manager.
  3. Take Snapshots from external manager instead of the Product, need to expose the detail from the Product to the manager.

Violations:
  1. SRP violated: exposed the internal detail for Product to external manager
  2. Law of demeter "Tell don't Ask" violated: don't ask the internal details from the Product to take the snapshot, 
      just tell him to take snapshot and give imuutable snapshot to the external manager as a black box snapshot implementaiton detail.

Architecture:
  1. A Product/Originator class for which we need to take the Snapshot and restore it.
  2. An internal static Snapshot class of the Product.
  3. An external SnapshotManager class which do not have access to the members or the Snapshot class or Product class internal details.

Advantages:
  1. Removes all the above violations and is extensible for adding many other features and testing independently.
  
Implementation:    */
import java.util.*; // Imports ArrayDeque for O(1) stack push/pop performance, respecting KISS.

class MementoPattern {
    public static void main (String[] args) {
        TextEditor textEditor = new TextEditor(); // Instantiates the Product/Originator.
        SnapshotManager snapshotManager = new SnapshotManager(); // Instantiates the SnapshotManager service (Separation of Concerns).
        
        textEditor.setContent("Hello World"); // Modifies state via public API.
        snapshotManager.takeSnapshot(textEditor); // Tells editor to package its state (Law of Demeter / Tell, Don't Ask).
        
        textEditor.setContent("Hello Goga"); // Modifies state again.
        System.out.println(textEditor.getContent()); // Prints current state: "Hello Goga"
        
        snapshotManager.undo(textEditor); // Rolls back the state safely.
        System.out.println(textEditor.getContent()); // Prints restored state: "Hello World"
    }
}

// RESTRICTED INTERFACE (Zero-Trust Architecture) can remove this interfcae for simplicity
interface Originator {
    // Reason: Exposes ONLY the methods required for history tracking.
    // Violation Protected: Interface Segregation Principle (ISP). Prevents the external manager 
    // from having access to editing methods like setContent() or getContent().
    TextEditor.Snapshot takeSnapshot();
    void restore(TextEditor.Snapshot snapshot);
}

class TextEditor implements Originator {
    private String content; // Internal state field. Encapsulated (Private data hiding).
    
    public String getContent() { return content; } // Public API for client reading.
    public void setContent(String content) { this.content = content; } // Public API for client writing.
    
    @Override
    public TextEditor.Snapshot takeSnapshot() {
        // Reason: Originator packages its own state into an opaque token.
        // Violation Protected: Tight Coupling. SnapshotManager doesn't need to know how text is stored.
        return new TextEditor.Snapshot(this.content);
    }
    
    @Override
    public void restore(TextEditor.Snapshot snapshot) {
        // Reason: Unpacks the snapshot safely within the owner class.
        // Violation Protected: Encapsulation Breach. Only the creator can read its own memento.
        if (snapshot != null) {
            this.content = snapshot.content; // Direct access because snapshot is an inner class.
        }
    }
    
    // THE MEMENTO (Immutable Snapshot Token)
    public static class Snapshot {
        // Reason: final fields and private modifier make the state completely immutable.
        // Violation Protected: State Mutation Violation. Stops external classes from tampering with history data.
        private final String content; 
        
        private Snapshot(String content) { // Private constructor.
            // Reason: Only the TextEditor (Originator) can instantiate this snapshot.
            // Violation Protected: Unauthorized Creation. External classes cannot forge fake states.
            this.content = content;
        }
    }
}

class SnapshotManager { // THE CARETAKER (Pure History Service)
    // Reason: Private collection holding state tokens safely.
    // Violation Protected: Data Hiding Violation. External classes cannot clear or corrupt the stack directly.
    private final Deque<TextEditor.Snapshot> snapshotHistory = new ArrayDeque<>();
    
    // Notice it accepts 'Originator', NOT 'TextEditor'!
    public void takeSnapshot(Originator originator) {
        // Reason: Delegates state capture without knowing internal attributes.
        // Violation Protected: Law of Demeter. The guard (manager) doesn't peek into the vault.
        snapshotHistory.push(originator.takeSnapshot()); 
        // ❌ COMPILER ENFORCED: originator.setContent() cannot be called here because 
        // the Caretaker only sees the restricted Originator interface (Zero-Trust).
    }
    
    public void undo(Originator originator) {
        // Reason: Safely checks stack boundaries before popping.
        // Violation Protected: Robustness Failure. Prevents EmptyStackException crashes.
        if (!snapshotHistory.isEmpty()) {
            TextEditor.Snapshot latestSnapshot = snapshotHistory.pop(); // Retrieves the latest token.
            originator.restore(latestSnapshot); // Tells originator to restore itself.
        }
    }
}



/*
Interface Implementation
  */

// 1. THE SNAPSHOT INTERFACE (Marker Interface - Zero Methods)
// Acts as a universal, opaque token. The Caretaker only knows it as "something to store".
// Actually this should be an internal static class of the Originator when doing concrete implementations.
interface Snapshot {}

// 2. THE ORIGINATOR INTERFACE
interface Originator {
    // Originator with its contents.
    Snapshot takeSnapshot(); // Originator will have its own snapshot logic
    void restore(Snapshot memento); // Originator will have its own restore logic
}

// 3. THE SnapshotManager INTERFACE
interface SnapshotManager {
    void takeSnapshot(Originator originator); // take Snapshot of the Originator and store it in the SnapshotManager
    void undo(Originator originator); // undo the Originator to the previous state stored in the manager already
}

