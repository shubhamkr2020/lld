import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {
    
    public interface RateLimiter {
        default public boolean allowedRequest(Request req) { 
            return true;
        };
        default public void finishRequest(Request req) {};
        default public void reset() {};
    }
    
    public static class DefaultRateLimiter implements RateLimiter {
        public DefaultRateLimiter(){}
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
    
    public static class TokenBucketRateLimiter implements RateLimiter {
        int tokenMaxSize, currentTokenSize;
        
        public TokenBucketRateLimiter(int tokenSize){
            this.tokenMaxSize = tokenSize;
            currentTokenSize = tokenSize;
        }
        
        @Override
        public boolean allowedRequest(Request req) {
            if(currentTokenSize > 0) {
                currentTokenSize--;
                return true;
            }
            return false;
        }
        
        @Override
        public void finishRequest(Request req) {
            currentTokenSize++;
        }
        
        @Override
        public void reset() {
            currentTokenSize = tokenMaxSize;
        }
        
        public void incrToken() {
            currentTokenSize++;
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
        public void finishRequest(Request req) {
            rateLimiter.finishRequest(req);
            requests.remove(req);
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
        // RateLimiter rateLimiter = new FixedLoadRateLimit(10);
        RateLimiter rateLimiter = new TokenBucketRateLimiter(2);
        // RateLimiter rateLimiter = new DefaultRateLimiter();
        Server server = new Server(1, rateLimiter);
        
        server.addRequest(new Request(1, 20));
        server.addRequest(new Request(2, 0));
        server.addRequest(new Request(3, 50));
        server.finishRequest(new Request(2, 0));
        server.addRequest(new Request(4, 9));
    }
}
