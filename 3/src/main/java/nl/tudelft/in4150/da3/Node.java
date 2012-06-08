package nl.tudelft.in4150.da3;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents the node in the decision tree that is used for storing intermediate decisions
 * and contains the tree operations
 */
public class Node {

//    private static Log LOGGER = LogFactory.getLog(Node.class);

    private int source;
    private Order order;
    private List<Node> children;
    private Node parent = null;
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
        child.parent = this;
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
    protected static Node findNodeBySourcePath(Node currentNode, List<Integer> path){

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
    protected static boolean isTreeReady(Node root, int numProcesses, int maxTraitors){

        if (root.getChildren().isEmpty()){
            return root.isReady();
        }

        int rootLevel = root.getNodeLevel();
        int numChildren = numProcesses - 2 - rootLevel;
        int numReadyChildren = 0;
        boolean isBottom = (numProcesses - rootLevel == 2);

        if (!isBottom){
            for (Node n : root.getChildren()){
                if (isTreeReady(n, rootLevel++, maxTraitors - 1)){
                    numReadyChildren++;
                }
            }
        } else {
            for (Node n : root.getChildren()){
                if (n.isReady()){
                    numReadyChildren++;
                }
            }
        }

        if (numReadyChildren >= numChildren - maxTraitors){
            return true;
        } else{
            return false;
        }
    }

    private int getNodeLevel(){
        Node root = getRoot();
        return findNodeLevel(this, root, 0);
    }

    private int findNodeLevel(Node node, Node root, int currentLevel){

        int level = 0;

        if (root.getChildren().isEmpty()){
            if (root == node){
                return currentLevel;
            } else{
                return 0;
            }

        } else {
            for (Node n : root.getChildren()){
                int childLevel = findNodeLevel(node, n, currentLevel++);
                if (childLevel > 0){
                    level = childLevel;
                    break;
                }
            }
        }

        return level;
    }

    private Node getRoot(){
        Node root = this;
        while (root.parent != null){
            root = root.parent;
        }
        return root;
    }

    protected static Order decide(Node root){
        List<Order> orders = new LinkedList<Order>();
        orders.add(root.getOrder());
        for (Node n : root.getChildren()){
            orders.add(decide(n));
        }
        return Order.getMostFrequentOrder(orders);
    }

}
