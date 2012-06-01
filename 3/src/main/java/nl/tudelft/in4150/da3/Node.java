package nl.tudelft.in4150.da3;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents the node in the decision tree that is used for storing intermediate decisions
 */
public class Node {

//    private static Log LOGGER = LogFactory.getLog(Node.class);

    private int source;
    private Order order;
    private List<Node> children;
    private boolean ready;

    public Node(int source) {
        this.source = source;
        this.order = Order.getDefaultOrder();
        this.children = new LinkedList<Node>();
        this.ready = false;
    }

    public Node(int source, Order order) {
        this.source = source;
        this.order = order;
        this.children = new LinkedList<Node>();
        this.ready = true;
    }

    public int getSource() {
        return source;
    }

    public Order getOrder() {
        return order;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void addChild(Node child){
        children.add(child);
    }

    public void setOrder(Order order){
        this.order = order;
        this.ready = true;
    }

    public boolean isReady() {
        return ready;
    }

    /**
     * Returns a node in the tree with a root at currentNode by traversing the path of sources
     * If there is an empty space somewhere in the path, a new node with default order is created there
     * @param currentNode root of the search tree
     * @param path list of indices to traverse
     * @return node
     */
    public static Node findNodeBySourcePath(Node currentNode, List<Integer> path){

        if (path.isEmpty()){
            return currentNode;
        }

        int firstElement = path.get(0);

        List<Node> children = currentNode.getChildren();
        for (Node n : children){
            if (n.getSource() == firstElement){
                if (path.size() == 1){
                    return n;
                } else{
                    return findNodeBySourcePath(n, path.subList(1, path.size()));
                }
            }
        }
        Node newNode = new Node(firstElement, Order.getDefaultOrder());
        currentNode.addChild(newNode);
        return findNodeBySourcePath(currentNode, path);
    }

    /**
     * Determines for the subtree whether it is ready to make a decision
     * @param root subtree root node
     * @param n total number of processes participating
     * @param f maximum number of failing processes
     * @param level level of root in the whole
     * @return
     */
    private boolean isTreeReady(Node root, int n, int f, int level){

    }

}
