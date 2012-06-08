package nl.tudelft.in4150.da3;

import nl.tudelft.in4150.da3.fault.AFault;
import nl.tudelft.in4150.da3.fault.NoFault;
import nl.tudelft.in4150.da3.message.AckMessage;
import nl.tudelft.in4150.da3.message.OrderMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * Implementation of randomized Byzantine agreement without authentication algorithm (Lamport-Pease-Shostak).
 * Synchronous communication is modeled by sending each message from node to other nodes each round.
 */
public class DA_Byzantine extends UnicastRemoteObject implements DA_Byzantine_RMI, Runnable {

    private static final long serialVersionUID = 2526720373028386278L;
    private static Log LOGGER = LogFactory.getLog(DA_Byzantine.class);
    private static final int TIME_OUT_MS = 20;

    //Cache to fasten lookup operations in remote registers
    private Map<String, DA_Byzantine_RMI> processCache;

    //Index of a current process
    private int index;

    //Number of processes participating in message exchange
    private int numProcesses;

    //URLs of processes in a system
    private String[] urls;

    //Counter of a messages sent by the process
    private int nextMessageId = 1;

    //final decision
    private Order finalOrder = null;

    //queue of incoming messages needed to simulate synchronous communication
    private Map<List<Integer>, OrderMessage> incomingMessages = new HashMap<List<Integer>, OrderMessage>();

    //queue of outgoing messages needed to simulate synchronous communication
    private List<OrderMessage> outgoingMessages = new LinkedList<OrderMessage>();

    private long lastOutcomingCheck = 0;

    //the root of the decision tree
    private Node root = new Node(0);

    private Waiter waiter = new Waiter(this);

    private AFault fault = new NoFault(-1);

    /**
     * Default constructor following RMI conventions
     *
     * @param urls  URLs of participating processes
     * @param index index of current process
     * @throws RemoteException if RMI mechanisms fail
     */
    protected DA_Byzantine(String[] urls, int index) throws RemoteException {
        super();
        processCache = new HashMap<String, DA_Byzantine_RMI>();

        this.index = index;
        this.urls = urls;
        this.numProcesses = urls.length;
    }

    @Override
    public void run() {
        //intentionally left blank and dedicated to the first page in O'Reilly titles
    }

    @Override
    public boolean isDone() {
        return finalOrder != null;
    }

    @Override
    public void receiveOrder(OrderMessage message) throws RemoteException {
        LOGGER.debug(echoIndex() + "received " + message.getOrder() + " order from " + message.getSender());
        incomingMessages.put(message.getAlreadyProcessed(), message);
        sendAck(message);

        Node messageInTheTree = process(message);

        if (messageInTheTree != null && Node.isTreeReady(root, numProcesses, message.getTotalTraitors()) &&
                !isDone() && !waiter.isStarted()) {
            //wait for the late messages and decide
            new Thread(waiter).start();
        }
    }

    public void decide() {
        finalOrder = Node.decide(root);
        LOGGER.info(echoIndex() + "decided to " + finalOrder.toString().toLowerCase());
        reportMissedMessages(root);
    }

    /**
     * Apply the Lamport-Pease-Shostak distributed algorithm for solving the Byzantine Generals problem.
     *
     * @param message The message to be processed.
     */
    private Node process(OrderMessage message) {

        Node result = null;

        Order order = message.getOrder();
        Integer currentMaxTraitors = message.getCurrentMaxTraitors();
        Integer totalTraitors = message.getTotalTraitors();
        List<Integer> alreadyProcessed = message.getAlreadyProcessed();

        //commander receives the message from the client and redistributes it
        if (message.getSender() == 0 && index == 0) {
            finalOrder = message.getOrder();
            LOGGER.info(echoIndex() + "decided to " + finalOrder.toString().toLowerCase());
            broadcastOrder(currentMaxTraitors, totalTraitors, order, alreadyProcessed);

            //lieutenant receives the first message directly from commander
        } else if (message.getSender() == 0) {
            root.setOrder(message.getOrder());
            root.setReady(true);
            broadcastOrder(currentMaxTraitors - 1, totalTraitors, order, alreadyProcessed);

            //lieutenants exchange messages
        } else {
            result = Node.findNodeBySourcePath(root, alreadyProcessed.subList(1, alreadyProcessed.size()));
            result.setOrder(order);
            if (currentMaxTraitors != 0) {
                broadcastOrder(currentMaxTraitors - 1, totalTraitors, order, alreadyProcessed);
            }
        }

        return result;
    }

    private void sendAck(OrderMessage orderMessage) {
        AckMessage message = getAckMessageTemplate(orderMessage.getSender());
        message.setAckId(message.getId());
        DA_Byzantine_RMI receiver = getProcess(urls[orderMessage.getSender()]);
        try {
            receiver.receiveAck(message);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void receiveAck(AckMessage message) throws RemoteException {
        OrderMessage messageToRemove = null;
        for (OrderMessage om : outgoingMessages) {
            if (om.getId() == message.getAckId()) {
                messageToRemove = om;
                break;
            }
        }
        outgoingMessages.remove(messageToRemove);

        //second attempt to delivery failed messages, if it also fails we give up
        long now = System.currentTimeMillis();
        List<OrderMessage> messagesToRemove = new LinkedList<OrderMessage>();
        if (now - lastOutcomingCheck > TIME_OUT_MS) {
            lastOutcomingCheck = now;
            for (OrderMessage om : outgoingMessages) {
                if (now - om.getTimestamp() > TIME_OUT_MS) {
                    try {
                        DA_Byzantine_RMI dest = getProcess(urls[om.getReceiver()]);
                        dest.receiveOrder(om);
                        messagesToRemove.add(om);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            outgoingMessages.removeAll(messagesToRemove);
        }
    }

    /**
     * Constructs a template of a new message
     *
     * @param receiver index of message receiver
     * @return order message template
     */
    private OrderMessage getOrderMessageTemplate(int receiver) {
        nextMessageId++;
        return new OrderMessage(nextMessageId - 1, index, receiver);
    }

    /**
     * Constructs a template of a new acknowledgment message
     *
     * @param receiver index of a message receiver
     * @return ack message template
     */
    private AckMessage getAckMessageTemplate(int receiver) {
        nextMessageId++;
        return new AckMessage(nextMessageId - 1, index, receiver);
    }

    @Override
    public void reset(int numProcesses) {
        nextMessageId = 1;
        finalOrder = null;
        incomingMessages = new HashMap<List<Integer>, OrderMessage>();
        outgoingMessages = new LinkedList<OrderMessage>();
        lastOutcomingCheck = 0;
        root = new Node(0);
        waiter = new Waiter(this);
        fault = new NoFault(-1);
        this.numProcesses = numProcesses;
        fault = new NoFault(-1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIndex() {
        return index;
    }

    /**
     * Returns a process specified by its URL either from a local cache or RMI lookup.
     *
     * @param url process url
     * @return process
     */
    private DA_Byzantine_RMI getProcess(String url) {
        DA_Byzantine_RMI result = processCache.get(url);
        if (result == null) {
            try {
                result = (DA_Byzantine_RMI) Naming.lookup(url);
            } catch (RemoteException e1) {
                throw new RuntimeException(e1);
            } catch (MalformedURLException e2) {
                throw new RuntimeException(e2);
            } catch (NotBoundException e3) {
                throw new RuntimeException(e3);
            }
            processCache.put(url, result);
        }
        return result;
    }


    private void broadcastOrder(int currentMaxTraitors, int totalTraitors, Order order, List<Integer> alreadyProcessed) {

        for (int i = 0; i < numProcesses; i++) {
            if (index != i && !alreadyProcessed.contains(i)) {
                DA_Byzantine_RMI destination = getProcess(urls[i]);

                // Apply faulty behavior. The iteration is indicated by the sequence of generals
                // that already received the order, .i.e. the depth of the recursion.
                Order orderWithFaultApplied = this.getFault().applyFaultyBehavior(order, alreadyProcessed.size());

                if (!(getFault() instanceof NoFault)){
                    LOGGER.debug(echoIndex() + "is faulty. Sends " + orderWithFaultApplied + " to process " + i +
                    " (Non-faulty order is " + order + ")");
                }

                //does not send anything if the faulty behavior requests so
                if (orderWithFaultApplied == null){
                    continue;
                }

                OrderMessage messageCopy = getOrderMessageTemplate(i);
                messageCopy.setCurrentMaxTraitors(currentMaxTraitors);
                messageCopy.setTotalTraitors(totalTraitors);
                messageCopy.setOrder(orderWithFaultApplied);
                messageCopy.setAlreadyProcessed(new LinkedList<Integer>(alreadyProcessed));
                messageCopy.getAlreadyProcessed().add(index);

                try {
                    destination.receiveOrder(messageCopy);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * Set the type of faulty behavior for this process.
     *
     * @param fault fault
     */
    @Override
    public void setFault(AFault fault) {
        if (fault != null) {
            this.fault = fault;
        }
    }

    /**
     * Get the type of faulty behavior for this process.
     *
     * @return fault
     */
    @Override
    public AFault getFault() {
        return this.fault;
    }

    /**
     * Check if this process has faulty behavior i.e. not NoFault.
     *
     * @return true if the process is faulty, false otherwise
     */
    @Override
    public boolean hasFault() {
        return !(this.getFault() instanceof NoFault);
    }

    private String echoIndex() {
        return "[" + index + "] ";
    }

    /**
     * Logs all the messages that were not received by the process
     *
     * @param root decision tree root
     */
    protected void reportMissedMessages(Node root) {
        if (!root.isReady()) {
            LOGGER.info(echoIndex() + "did not receive message with sequence " + Node.getSourceSequence(root, null));
        }

        for (Node n : root.getChildren()) {
            reportMissedMessages(n);
        }
    }

	@Override
	public Order getFinalOrder() throws RemoteException {
		return finalOrder;		
	}

	@Override
	public int getNumberOfReceivedMessages() throws RemoteException {
		return incomingMessages.size();
	}

}

