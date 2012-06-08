package nl.tudelft.in4150.da3;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Represents the node in the decision tree that is used for storing intermediate decisions
 * and contains the graph operations to modify the tree
 */
public class Node {

//    private static Log LOGGER = LogFactory.getLog(Node.class);

    private int source;
    private Order order;
    private List<Node> children;
    private Node parent = null;
    private boolean ready;
    private static Map<Integer, Node> treeCache = new HashMap<Integer, Node>();

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

    public void setReady(boolean ready){
        this.ready = ready;
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
     * The tree is ready if enough children of the root are ready
     * @param root subtree root node
     * @param root root of the subtree
     * @param numProcesses total number of processes participating in messages exchange
     * @param maxTraitors maximum amount of potentially failing processes at this subtree level
     * @return true if the subtree is ready to initiate decision making process
     */
    protected static boolean isTreeReady(Node root, int numProcesses, int maxTraitors){

        if (root.getChildren().isEmpty()){
            return root.isReady();
        }

        int rootLevel = root.getNodeLevel();
        int numChildren = numProcesses - 2 - rootLevel;
        int numReadyChildren = 0;

        //at bottom level we have nodes with exactly one child
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

        int rootReady = root.isReady() ? 1 : 0;

        //ready condition: number of ready nodes > total number of nodes - number of traitors
        return (numReadyChildren + rootReady >= numChildren + 1 - maxTraitors);
    }

    /**
     * Returns a level of node in the tree given that root level is 0
     * @return level of this node
     */
    private int getNodeLevel(){
        Node root = getRoot();
        return findNodeLevel(this, root, 0);
    }

    /**
     * Returns a level of the node for the subtree with a given root, provided the root level
     * @param node node to look for
     * @param root subtree root
     * @param currentLevel root level
     * @return level of node
     */
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

    /**
     * Returns tree root
     * @return root
     */
    private Node getRoot(){
        Node root = this;
        while (root.parent != null){
            root = root.parent;
        }
        return root;
    }

    /**
     * Makes final decision on the tree recursively applying the majority function
     * @param root tree root
     * @return decision
     */
    protected static Order decide(Node root){
        List<Order> orders = new LinkedList<Order>();
        orders.add(root.getOrder());
        for (Node n : root.getChildren()){
            orders.add(decide(n));
        }
        return Order.getMostFrequentOrder(orders);
    }

    /**
     * Returns a list of source values for the path from the current node to root starting from the root
     * @param currentNode current node
     * @param currentSequence already available sequence
     * @return list of source values
     */
    protected static List<Integer> getSourceSequence(Node currentNode, List<Integer> currentSequence){

        if (currentSequence == null){
            currentSequence = new LinkedList<Integer>();
        }

        if (currentNode.parent == null){
            currentSequence.add(0, 0);
            return currentSequence;
        } else{
            currentSequence.add(0, currentNode.source);
            return getSourceSequence(currentNode.parent, currentSequence);
        }
    }

    public String toString(){
        return source + ", " + order + ", " + parent + ", " + children.size() + ", " + ready;
    }
}
