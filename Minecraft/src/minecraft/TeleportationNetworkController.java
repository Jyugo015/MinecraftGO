package minecraft;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeleportationNetworkController {
    private static List<Point> nodes = new ArrayList<>();
    private static List<List<Edge>> adjacencyList = new ArrayList<>();
    private static List<Edge> edges = new ArrayList<>();
    
    
    /**
     * Adds teleportation points from the database into the data structure of the graph.
     *
     * @param nodesFromDB The nodes from the database to be added into the data structure of the graph
     * @return True if the teleportation points are added from the database into the data structure of the graph
     */
    public static boolean restoreNode(ArrayList<Point> nodesFromDB) throws SQLException {
        nodes.clear();
        adjacencyList.clear();
        edges.clear();
        for (Point p : nodesFromDB) {
            boolean inDatabaseDuplicated = false;
            for (Point pHad : nodes) {
                 if (pHad.getNameOfTeleportationPoint().equals(p.getNameOfTeleportationPoint())) {
                     inDatabaseDuplicated = true;
                     break;
                }
            }

            if (! inDatabaseDuplicated) {
                nodes.add(new Point(p.nameOfTeleportationPoint, p.owner, p.x, p.y, null, p.friendRequestsReceived, p.friendWaitingAcceptance));
                System.out.println(nodesFromDB.get(nodesFromDB.size()-1).friendRequestsReceived);
                System.out.println(nodesFromDB.get(nodesFromDB.size()-1).friendWaitingAcceptance);
                adjacencyList.add(new ArrayList<>());
            } 
            else {
                database_item5.removeteleportationPoint(p.getNameOfTeleportationPoint());
            }
           
        }
        for (Point p : nodes) {
            ArrayList<String> neighbour = database_item5.retrieveNeighbour(p.getNameOfTeleportationPoint());
            if (neighbour!=null && ! neighbour.isEmpty()) {
                for (String n : neighbour) {
                    p.addNeighbour(n);
                    System.out.println("Add neighbour: " + p.neighbours);
                }
            }
        }
        return true;
    }
    
    /**
     * Adds new teleportation points into the graph.
     *
     * @param newNode The name of the nodes to be added into graph
     * @param owner The username that create the nodes
     * @param x The x-coordinate of the nodes
     * @param y The y-coordinate of the nodes
     * @param neighbours The neighbors of the nodes
     * @param friendRequestsReceived The friend request received from other nodes
     * @param friendWaitingAcceptance The friend request sent to other nodes
     * @return True if the teleportation points are added from the database into the data structure of the graph
     */
    public static boolean addNewNode(String newNode, String owner, float x, float y, ArrayList<String> neighbours, ArrayList<String> friendRequestsReceived, ArrayList<String> friendWaitingAcceptance) throws SQLException{
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).nameOfTeleportationPoint.equals(newNode)){
                System.out.println("The teleportaton point with same name exist already. Please try a new name");
                return false;
            }
//            else if (nodes.get(i).getX() == newNode.getX() && nodes.get(i).getY() == newNode.getY()) {
//                System.out.println("x: " + newNode.getX());
//                System.out.println("y: " + newNode.getY());
//                System.out.println("The teleportaton point with same location exist already. Please try a new location");
//                return false;
//            }
        }
        System.out.println("newNode: " +newNode);
        nodes.add(new Point(newNode, owner, x, y, neighbours, friendRequestsReceived, friendWaitingAcceptance));
        database_item5.addteleportationPoint(newNode, owner, x, y);
        adjacencyList.add(new ArrayList<>());
        System.out.println(adjacencyList.size());
        return true;
    }
    
    /**
     * Remove teleportation points from the graph.
     *
     * @param nodeName The name of the nodes to be removed from graph
     * @return True if the teleportation points is removed from the graph
     */
    public static boolean removeNode(String nodeName) throws SQLException {
        Point node = getNode(nodeName);
        int indexCurrent = getIndex(node.nameOfTeleportationPoint);
        if (indexCurrent != -1) {
            long startTime = System.currentTimeMillis();
            // remove neigbour's neigbours(current) from neighbour
            for (String n : nodes.get(indexCurrent).getNeighbours()) {
                Point neighbour = getNode(n);
                for (int j = 0; j < neighbour.neighbours.size() ; j++) {
                    if (neighbour.neighbours.get(j).equals(node.nameOfTeleportationPoint)) {
                        neighbour.neighbours.remove(j); // remove from neighbour list
                        break;
                    }
                }
                int indexNeighbour= getIndex(neighbour.nameOfTeleportationPoint); // find neighbour index
                for (int i = 0; i <  adjacencyList.get(indexNeighbour).size() ; i++) {
                    if (adjacencyList.get(indexNeighbour).get(i).n2.nameOfTeleportationPoint.equals(node.getNameOfTeleportationPoint())) {
                        adjacencyList.get(indexNeighbour).remove(i); // remove from adjacency list
                        i--;
                        break;
                    }
                }
            }
            adjacencyList.remove(indexCurrent); // remove the whole adjacency list
            System.out.println("Time taken to remove multiple edges: " + (System.currentTimeMillis() - startTime));
            
            // remove the friend request from the other requested friend
            for (String requstedNode: node.friendWaitingAcceptance) {
                Point n = getNode(requstedNode);
                if (n!=null) {
                    n.friendRequestsReceived.remove(node.getNameOfTeleportationPoint());
                    database_item5.setRequestGet(requstedNode, n.friendRequestsReceived);    
                }
            }
            
            nodes.remove(node);
            node.neighbours.clear();
            database_item5.removeteleportationPoint(nodeName);
            // remove the single edges
            startTime = System.currentTimeMillis();
            for (int j = 0; j < edges.size(); j++) {
                if (edges.get(j).n1.equals(node) || edges.get(j).n2.equals(node) ) {
                    edges.remove(j);
                    j--;
                }
            }
            System.out.println("Time taken to remove single edge: " + (System.currentTimeMillis() - startTime));
            return true;

        }
        return false;
    }
    
    /**
     * Get the edges of the graph.
     *
     * @return List of edges of the graph
     */
    public static List<Edge> getEdges() {
        return edges;
    }
    
    /**
     * Get the neighbors of the teleportation point.
     *
     * @param nodename The name of the teleportation point to find its neighbors
     * @return List of names of neighbors  of the point, null if the node doesn't exist
     */
    public static ArrayList<String> getNeighbours(String nodename) {
        Point node = getNode(nodename);
        if (node != null && ! node.neighbours.isEmpty()) {
            System.out.print("Neighbour of nodes " + node.getNameOfTeleportationPoint() + " are: ");
            int sizeNeighbour = node.getNeighbours().size();
            for (int i = 0; i< sizeNeighbour-1; i++) {
                System.out.print(node.getNeighbours().get(i) + ", ");
            } 
            System.out.println(" and " + node.getNeighbours().get(sizeNeighbour-1));
            return node.neighbours;
            }
        return null;
    }
    
    /**
     * Check if the teleportation point is inside the network graph
     *
     * @param nodeName The name of the teleportation point to search
     * @return True if the teleportation point is inside the network graph
     */
    public static boolean contains(Point nodeName) {
        return getNode(nodeName.getNameOfTeleportationPoint()) != null;
    }
    
    /**
     * Find the index of the node in the list of points in the network
     *
     * @param nodeName The name of the teleportation point to search
     * @return The index if the teleportation point is inside the network graph, -1 if the node is not found
     */
    protected static int getIndex(String nodeName) {
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).nameOfTeleportationPoint.equals(nodeName)) {
//                System.out.println("is equal");
                return i;
            } 
        }
        return -1;
    }
    
    /**
     * @return The sum of the teleportation point inside the network graph
     */
    public static int totalNodes() {
        return nodes.size();
    }

    /**
     * @return The adjacency list of the network graph
     */
    private static List<List<Edge>> getAdjacencyList() {
        return adjacencyList;
    }

    /**
     * @return All the teleportation points in the network graph
     */
    public static List<Point> getNodes() {
        return nodes;
    }
    
    /**
     * Use breadth first search to find the number of connected nodes 
     *
     * @param teleportationPoint The name of the teleportation point as starting point
     * @return A list of the points that can be reached by the teleportationPoint
     */
    public static ArrayList<Point> BFSnodesCanBeReached(Point teleportationPoint) {
        ArrayList<Point> reachableNode = new ArrayList<>();
        reachableNode.add(teleportationPoint);
        for (String pName: teleportationPoint.neighbours) {
            reachableNode.add(getNode(pName));
        }
        for (int i = 1; i < reachableNode.size(); i++) {
            Point node = reachableNode.get(i);
            for (String pName: node.neighbours) {
                Point neighbour = getNode(pName);
                if (!reachableNode.contains(neighbour)) {
                    reachableNode.add(neighbour);
                }
            }
        }
        System.out.println("Total number of reachable node(including the root node): " + reachableNode.size());
        return reachableNode;
    }
    
    /**
     * @param nodeName The name of the node to be searched
     * @return The point in the network graph if the point exist
     */
    public static Point getNode(String nodeName) {
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).getNameOfTeleportationPoint().equals(nodeName)) {
                return nodes.get(i);
            }
        }
        return null;
    }
    
    /**
     * Search the shortest path to go from start to the destination point using Dijkstra's algorithm
     * 
     * @param start The starting point
     * @param dest The destination point to go
     * @return The list of nodes which has the minimum distance to go from start to destination
     */
    public static ArrayList<Point> shortestPath(String start, String dest) {
        Point currentPoint = getNode(start);
        Point destinationPoint = getNode(dest);
        if (! nodes.contains(destinationPoint) || ! nodes.contains(currentPoint)) {
            System.out.println("The starting point / destination doesn't exist.");
            return null;
        }
        ArrayList<Point> reachableNode = BFSnodesCanBeReached(currentPoint);
        System.out.println(reachableNode);
        int size = reachableNode.size();
        int[] parent = new int[size];
        for (int i = 0; i < parent.length; i++) {
            parent[i] = -1;
        }
        double[] distance = new double[size];
        for (int i = 0; i < size; i++) {
            distance[i] = Double.POSITIVE_INFINITY;
        }
        distance[0] = 0;
        ArrayList<Point> visited = new ArrayList<>();
        
        while (! visited.contains(destinationPoint) || visited.size() < size) {            
            int u = -1; // index of the vertex to be determine
            double currentMinValue = Double.POSITIVE_INFINITY;
            for (int i = 0; i < size; i++) {
                if (!visited.contains(reachableNode.get(i)) && distance[i] < currentMinValue) {
                    currentMinValue = distance[i];
                    u = i;
                }
            }
            if (u != -1) {
                visited.add(reachableNode.get(u));
           
                for (Edge edge : adjacencyList.get(getIndex(reachableNode.get(u).getNameOfTeleportationPoint()))) {
                    for (int i = 0; i < size; i++) {
                        if (edge.n2.equals(reachableNode.get(i))) {
                           if (! visited.contains(reachableNode.get(i)) && distance[i] > distance[u] + edge.getDistance()) {
                                distance[i] = distance[u] + edge.getDistance();
                                parent[i] = u;
                            }
                        }
                    } 
                } 
            } else {
                System.out.println("No way to go");
                return null;
            }
            
        }
        System.out.println("Parent: " + Arrays.toString(parent));
        System.out.println("Distance: " + Arrays.toString(distance));
        ArrayList<Point> shortestPath = new ArrayList<>();
//        shortestPath.add(destination);
        // find the index of the destination
        int indexParent = 0;
        for (; indexParent < size; indexParent++)
            if (reachableNode.get(indexParent).equals(destinationPoint))
                break;
        while (indexParent != -1) {            
            shortestPath.add(reachableNode.get(indexParent));
            indexParent = parent[indexParent];
        }
        System.out.print("Shortest path: ");
        for (Point node : shortestPath) {
            System.out.print(node.getNameOfTeleportationPoint() + " ");
        }
        return shortestPath;
    }
    
    /**
     * @param teleportationPoint The starting point
     * @param destination The destination point to go
     * @return The numerical value of the shortest distance between 2 points
     */
    public static double shortestDistance(String teleportationPoint, String destination) {
        ArrayList<Point> shortestPath = shortestPath(teleportationPoint, destination);
        if (shortestPath == null) {
            return -1;
        }
        double distance = 0;
        for (int i = 0; i < shortestPath.size()-1; i++) {
            distance += getDistance(shortestPath.get(i), shortestPath.get(i+1));
        }
        System.out.println("Distance: " + distance);
        return distance;
    }
    
    /**
     * @param teleportationPoint The starting point
     * @param destination The destination point to go
     * @return The numerical value of the distance between 2 connected points
     */
    private static double getDistance(Point teleportationPoint, Point destination) {
        for (Edge edge : adjacencyList.get(getIndex(teleportationPoint.getNameOfTeleportationPoint()))) {
            if (edge.n2.equals(destination)) {
                System.out.println("Edge: " + edge);
                return edge.distance;
            }
        }
        return -1;
    }
    
    /**
     * @param owner The username
     * @return All of the teleportation points created by the user
     */
    public static ArrayList<Point> nodesOfOwner(String owner) {
        ArrayList<Point> belong = new ArrayList<>();
        for (Point node : nodes) {
            if (node.getOwner().equals(owner)) {
                belong.add(node);
            }
        }
        return belong;
    }
    
    
    public static class Point{
        private String nameOfTeleportationPoint;
        private final String owner;
        private ArrayList<String> neighbours = new ArrayList<>();
        private ArrayList<String> friendRequestsReceived;
        private ArrayList<String> friendWaitingAcceptance ;
        private final float x;
        private final float y;

        public Point(String nameOfTeleportationPoint, String owner, float x, float y, ArrayList<String> neighbours, ArrayList<String> friendRequestsReceived, ArrayList<String> friendWaitingAcceptance) {
            this.nameOfTeleportationPoint = nameOfTeleportationPoint;
            this.owner = owner;
            this.x = x;
            this.y = y;
            if (neighbours!=null) {
                for (String n : neighbours) {
                    this.addNeighbour(n);
                }
            }
            this.friendRequestsReceived = friendRequestsReceived == null ? new ArrayList<>() : friendRequestsReceived;
            this.friendWaitingAcceptance = friendWaitingAcceptance == null ? new ArrayList<>() : friendWaitingAcceptance;
        }

        
        /**
         * @return All of the friend request sent by the other teleportation points to become neighbor
         */
        public ArrayList<String> getFriendRequestsReceived() {
            return friendRequestsReceived;
        }

        /**
         * @return All of the friend request sent to the other teleportation points that wait for acceptance to become neighbor
         */
        public ArrayList<String> getFriendWaitingAcceptance() {
            return friendWaitingAcceptance;
        }

        /**
         * Send friend request to other teleportation point
         * 
         * @param requestReciepient The teleportation point that will receive the invitation to become neighbor
         * @return True if the friend request is successfully sent to the other teleportation points
         */
        public boolean sendFriendRequest(String requestReciepient) throws SQLException{
            Point reciepient = getNode(requestReciepient);

            if (reciepient !=null) {
                // check if the request sent before // they are neigbours already
                if (reciepient.friendRequestsReceived.contains(nameOfTeleportationPoint.trim()) || reciepient.neighbours.contains(nameOfTeleportationPoint)) {
                    return false;
                } else {
                    reciepient.friendRequestsReceived.add(this.getNameOfTeleportationPoint());
                    this.friendWaitingAcceptance.add(requestReciepient);
                    System.out.println(reciepient + " accpeted " + reciepient.friendRequestsReceived);
                    System.out.println( this + " sent " + friendWaitingAcceptance);
                    database_item5.addRequestSent(nameOfTeleportationPoint, requestReciepient);
                    database_item5.addRequestGet(requestReciepient, nameOfTeleportationPoint);
                    System.out.println("sent in database" + database_item5.retrieveRequestSent(nameOfTeleportationPoint));
                    System.out.println("get in database" + database_item5.retrieveRequestGet(requestReciepient));
                    return true;
                }
            }
            return false;
        }
    
        /**
         * Accept the friend request sent by other teleportation point and
         * become neighbor, forming edge between them
         * 
         * @param requestSender The teleportation point that has sent the invitation to become neighbor
         * @throws SQLException if the sender cannot be found in the database
         */
        public void acceptFriendRequest(String requestSender) throws SQLException{
            Point sender = getNode(requestSender);

            if (sender != null) {
                // remove the request
                friendRequestsReceived.remove(requestSender);
                sender.friendWaitingAcceptance.remove(this.nameOfTeleportationPoint);
                database_item5.setRequestSent(requestSender, sender.friendWaitingAcceptance);
                database_item5.setRequestGet(nameOfTeleportationPoint, this.friendRequestsReceived);
                database_item5.addNeighbour(requestSender, nameOfTeleportationPoint);
                database_item5.addNeighbour(nameOfTeleportationPoint, requestSender);
                sender.addNeighbour(this.nameOfTeleportationPoint);
            }
        }
        
        public void rejectFriendRequest(String requestSender) throws SQLException{
            Point sender = getNode(requestSender);
            if (sender != null) {
                // remove the request
                friendRequestsReceived.remove(requestSender);
                sender.friendWaitingAcceptance.remove(this.getNameOfTeleportationPoint());
                database_item5.setRequestSent(requestSender, sender.friendWaitingAcceptance);
                database_item5.setRequestGet(nameOfTeleportationPoint, friendRequestsReceived);
            }
            
        }
        
        public String getOwner() {
            return owner;
        }

        public ArrayList<String> getNeighbours() {
            return neighbours;
        }

        public ArrayList<String> getNeighboursInString() {
            ArrayList<String> list = new ArrayList<>();
            for (int i = 0; i < neighbours.size(); i++) {
                list.add(neighbours.get(i));
            }
            return list;
        }
        
        public String getNameOfTeleportationPoint() {
            return nameOfTeleportationPoint;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        private boolean addNeighbour(String neighbourName) {
            Point neighbour = getNode(neighbourName);
            if (neighbour==null) return false;
            if (neighbourName.equals(this)) return false; // the node itself is not its neighbour
            if (neighbours.contains(neighbourName)) {
                System.out.println(nameOfTeleportationPoint + " and " + neighbourName + " are neighbour already");
                return false;
            }
            // both add each other as neighbour
            System.out.println("neighbour of " + nameOfTeleportationPoint + " : " + neighbours);
            System.out.println("neighbour of " + neighbourName + " : " + neighbour.neighbours);
            neighbours.add(neighbourName);
            neighbour.neighbours.add(nameOfTeleportationPoint);
            System.out.println("added");
            // create edge between them
            adjacencyList.get(getIndex(this.nameOfTeleportationPoint)).add(new Edge(this, neighbour));
            adjacencyList.get(getIndex(neighbour.nameOfTeleportationPoint)).add(new Edge(neighbour, this));
            edges.add(new Edge(this, neighbour));
            return true;
        }
        
        public boolean removeNeighbour(String removedNeighbourName) throws SQLException{
            Point neighbour = getNode(removedNeighbourName);
            int indexCurrent = getIndex(this.nameOfTeleportationPoint);
            if (neighbour != null) {
                int indexNeighbour = getIndex(neighbour.nameOfTeleportationPoint);
                // remove neighbour's adjacency list that N2 is this
                for (int i = 0; i < adjacencyList.get(indexNeighbour).size(); i++) {
                    if (adjacencyList.get(indexNeighbour).get(i).getN2().equals(this)) {
                        adjacencyList.get(indexNeighbour).remove(i);
                        nodes.get(indexNeighbour).neighbours.remove(this.getNameOfTeleportationPoint());
                        break;
                    }
                }
                // remove this's adjacency list that N2 is neighbour
                for (int i = 0; i < adjacencyList.get(indexCurrent).size(); i++) {
                    if (adjacencyList.get(indexCurrent).get(i).getN2().equals(neighbour)) {
                        adjacencyList.get(indexCurrent).remove(i);
                        nodes.get(indexCurrent).neighbours.remove(neighbour.getNameOfTeleportationPoint());
                        break;
                    }
                }
                // remove edges
                for (int i = 0; i< edges.size(); i++) {
                    Edge e = edges.get(i);
                    if ((e.getN1().equals(this) && e.getN2().equals(neighbour)) || (e.getN1().equals(neighbour) && e.getN2().equals(this)) ) {
                        edges.remove(e);
                        i--;
                    }
                }
                System.out.println("Is they still neighbour?:" + neighbour.neighbours.contains(nameOfTeleportationPoint));
                database_item5.removeNeighbour(nameOfTeleportationPoint, removedNeighbourName);
                database_item5.removeNeighbour(removedNeighbourName, nameOfTeleportationPoint);
                return true;
            }
            return false;
        }
        
//        public boolean renameTeleportationPoint(String newName) {
//            for (Point n:nodes) {
//                if (n.getNameOfTeleportationPoint().equals(newName)) {
//                    return false;
//                }
//            }
//            this.nameOfTeleportationPoint = newName;
//            return true;
//        }

        @Override
        public String toString() {
            return nameOfTeleportationPoint ;
        }
    }
     
    public static class Edge{
        private double distance;
        private Point n1; // starting point
        private Point n2; // starting point

        public Edge(Point n1, Point n2) {
            this.n1 =n1;
            this.n2 =n2;
            this.distance = Math.pow((n1.x - n2.x)*(n1.x - n2.x) + (n1.y - n2.y)*(n1.y - n2.y), 0.5);
        }

        public double getDistance() {
            return distance;
        }

        public Point getN1() {
            return n1;
        }

        public Point getN2() {
            return n2;
        }

        @Override
        public String toString() {
            return String.format("%s, %s, %.3f%n", n1.nameOfTeleportationPoint, n2.nameOfTeleportationPoint, distance);
        }
    }
}
