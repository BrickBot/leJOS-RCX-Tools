The package 'rcxdirect' can be used to test the functionality
of sensors and motors. You can also write some programs to
control the RCX via PC.

Download the program for direct communication to the RCX 
(Click 'Download Direct-RCX' in RCXDirectMode).

Place RCXDirectTest.java into the same directory as
rcxtools.jar and RCXTools.properties.
Compile with
    javac -classpath rcxtools.jar RCXDirectTest.java
Run with
    java -classpath .;rcxtools.jar RCXDirectTest


P.S.: You can also use the batch files 'make' and 'test'
      instead of going through the steps above.