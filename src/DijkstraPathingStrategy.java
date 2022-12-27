import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class DijkstraPathingStrategy
        implements PathingStrategy {


    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {
        List<Point> path = new LinkedList<>();

        Hashtable<Point, Node> openHash = new Hashtable<>();

        Comparator<Node> compF = Comparator.comparing(Node::getF).thenComparing(Node::getG);
        PriorityQueue<Node> openQueue = new PriorityQueue<>(compF);  //create open hash and queue

        Hashtable<Point, Point> closedHash = new Hashtable<>();  //create closed hash

        Node cur = new Node(start, end);//mark start as current node
        cur.setH(0);
        cur.setG(0);

        openHash.put(cur.getP(), cur);
        openQueue.add(cur);     //add start node to open list

        Node pathNode = null;

        while (!openQueue.isEmpty()) {
            cur = openQueue.peek();     //get lowest f value

            if (withinReach.test(cur.getP(),end)) {
                pathNode = cur;        //check if cur is goal
                break;
            }

            List<Point> validNeighbors = potentialNeighbors.apply(cur.getP())
                    .filter(canPassThrough)
                    .filter(pt ->
                            !closedHash.containsKey(pt))
                    .collect(Collectors.toList());   //removes neighbors in closed list


            for (Point po : validNeighbors) {
                if (openHash.containsKey(po)) {
                    Node t = openHash.get(po);
                    if (t.getG() > cur.getG() + 1) {       //replace g value if better one found
                        openQueue.remove(openHash.get(po));
                        openHash.get(po).setG(cur.getG() + 1);
                        openHash.get(po).setPrior(cur);
                        openQueue.add(openHash.get(po));
                    }
                }
                else{     //add neighbor to openlist
                    Node temp = new Node(po, end);
                    temp.setH(0);
                    temp.setPrior(cur);
                    temp.setG(cur.getG() + 1);

                    openHash.put(temp.getP(), temp);
                    openQueue.add(temp);}
            }
            openHash.remove(cur.getP());
            openQueue.remove(cur);                        //remove cur from open list
            closedHash.put(cur.getP(), cur.getP());  //add cur to closed list


        }


        while (pathNode != null && !pathNode.getP().equals(start)) {   //create path list
            path.add(0,pathNode.getP());
            pathNode = pathNode.getPrior();
        }

        return path;

    }
}
