# Single threaded and multi threaded network server.

Demonstrates a single threaded and a multi threaded network server in Java.


## Network

Old fashioned network sockets are used in this implementation (i.e. no servlets, no EJBs, no application servers...),
just sockets. The single threaded server can only process one client at a time while the concurrent server can handle a
fixed number simultaneously before queueing them.  

## Parsing and payload

All parsing (i.e. from object to network and back) is done using
[Google Protobuf](https://developers.google.com/protocol-buffers/docs/javatutorial). The file
[database.proto](database.proto) contains two messages: Request and Response. They have already been compiled into java
(see [DatabaseProtos](DatabaseProtos.java).)

In order to re-compile install Protobuf in your system:
``` sudo apt install protobuf-compiler ```

and run the compiler:
```
protoc -I=. --java_out=. src/sfsu/database.proto
```

When launching a Java process that depends on Protobufs remember to add the JAR file as a project library in IntelliJ or
add it to the CLASSPATH in the command line:

```
java -classpath lib/*:out/production/Database/ ...
```

## Further work

This example does not actually do much. The concurrent server can be the basis for a larger database, though.
