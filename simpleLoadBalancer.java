import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {
    
    public static class LoadBalancer {
        // private int maxServers;
        Set<Server> servers;
        BalancingStrategy strategy;
        int size;
        
        public LoadBalancer(BalancingStrategy strategy) {
            this.strategy = strategy;
            servers = new HashSet<>();
            size = 0;
        }
        
        public Server getServer() {
            return strategy.getServer(servers);
        }
        public void addServer(Server server) {
            if(servers.contains(server))
                servers.remove(server);
            server.used = false;
            servers.add(server);
        }
    }

    public interface BalancingStrategy {
        public Server getServer(Set<Server> servers);
    }

    public static class RoundRobinBalancingStrategy implements BalancingStrategy {
        public RoundRobinBalancingStrategy() {}
        
        @Override
        public Server getServer(Set<Server> servers) {
            Server selectedServer = new Server(0, 0);
            int minLoad = Integer.MAX_VALUE;
            for(Server server: servers) {
                if(server.used == false && server.getLoad() < minLoad) {
                    selectedServer = server;
                    minLoad = server.getLoad();
                }
            }
            selectedServer.used = true;
            return selectedServer;
        }
    }

    public static class Server {
        private int id;
        private int load;
        private boolean used;
        
        public Server(int id, int load) {
            this.id = id;
            this.load = load;
            this.used = false;
        }
        
        public int getId() {
            return id;
        }
        public int getLoad() {
            return load;
        }
    }

    public static void main(String[] args) {
        BalancingStrategy strategy = new RoundRobinBalancingStrategy();
        LoadBalancer lb = new LoadBalancer(strategy);
        lb.addServer(new Server(1,15));
        lb.addServer(new Server(2,20));
        lb.addServer(new Server(3,10));
        lb.addServer(new Server(4,40));
        
        Server server1 = lb.getServer();
        System.out.println(server1.getId());
        
        Server server2 = lb.getServer();
        System.out.println(server2.getId());
        
        lb.addServer(new Server(3,5));
        System.out.println(lb.getServer().getId());
    }
}
