This project provides an implementation of **Schiper-Eggli-Sandoz point-to-point message exchange protocol** with casual order of message delivery. See _"A. Schiper, J. Eggli, A. Sandoz. A New Algorithm to Implement Causal Ordering, In Proceedings of the 3rd International Workshop on Distributed Algorithms, Springer-Verlag London, 1989, ISBN:3-540-51687-5"_ for theoretical details.

The protocol is implemented with Java RMI technology and can be distributed among several physical machines.

To work with the project, one has to adjust network configuration, start servers at all the allocated machines and connect to the server with a client having messages to exchange.

The network configuration should be located in `network.cfg` file in `resources/` directory. Each line in this file provides a unique URL of a server process. Several processes may locate at one physical machine but have to have different names. If file `network.cfg` is not found in the directory, `network.cfg.default` will be used instead, but it is recommended to create `network.cfg` file. For local processes, *"localhost"* and *"127.0.0.1"* values can be used as a host name part of a process URL.

Build process is organised with Maven. To compile source files and make an executable .jar file, one has to run `mvn clean install -DskipTests` command in the terminal from the project root. After the execution of this command, a newly created .jar file will be placed in `target/` directory with its dependencies in `target/lib/`.

To run the server, one has to copy .java.policy file to the home folder and run `java -Djava.security.policy=java.policy -jar target/DA-1.1.0.jar` from the project root. If using the distributed setup, all the instances should be started within a frame of 5 seconds as during this time servers instantiate its local processes and start to look for the remote ones afterwards. If any remote process is not resolved, the server will stop.

Client connection to the server is emulated with JUnit tests. Once the servers are started, one has to run JUnit tests from SimpleTest.java to connect to the servers and run message exchange. The correctness of the protocol can be verified from the server and client logs.