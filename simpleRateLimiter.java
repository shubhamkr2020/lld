import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {
    
    public interface RateLimiter {
        public boolean allowedRequest(Request req);
    }
    
    public static class FixedLoadRateLimit implements RateLimiter {
        public int maxLoad;
        
        public FixedLoadRateLimit(int maxLoad) {
            this.maxLoad = maxLoad;
        }
        
        @Override
        public boolean allowedRequest(Request req) {
            if(req.load < maxLoad) 
                return true;
            return false;
        }
    }
    
    public static class Server {
        public int id;
        public List<Request> requests;
        RateLimiter rateLimiter;
        
        public Server(int id, RateLimiter rateLimiter) {
            this.id = id;
            requests = new ArrayList<>();
            this.rateLimiter = rateLimiter;
        }
        
        public void addRequest(Request req) {
            if(rateLimiter.allowedRequest(req)) {
                System.out.println("allowed..." + req.id);
                requests.add(req);
            } else {
                System.out.println("throttled..." + req.id);
            }
        }
    }
    
    public static class Request {
        public int id;
        public int load;
        
        public Request(int id, int load) {
            this.id = id;
            this.load = load;
        }
    }

    public static void main(String[] args) {
        RateLimiter rateLimiter = new FixedLoadRateLimit(10);
        Server server = new Server(1, rateLimiter);
        
        server.addRequest(new Request(1, 20));
        server.addRequest(new Request(2, 0));
        server.addRequest(new Request(3, 50));
        server.addRequest(new Request(4, 9));
    }
}
