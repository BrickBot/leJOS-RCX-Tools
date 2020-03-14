cd ..\src\bin
del /s *.class
cd ..
set CLASSPATH=.;C:\Programme\Lego\lejos\lib\pcrcxcomm.jar
rem javac -target 1.1 -deprecation -d bin rcxtools/*.java rcxtools/filebrowser/*.java rcxtools/rcxdirectmode/*.java rcxtools/rcxdownload/*.java rcxtools/share/gui/*.java rcxtools/share/tvm/*.java rcxdirect/*.java
javac -d bin rcxtools/*.java rcxtools/filebrowser/*.java rcxtools/rcxdirectmode/*.java rcxtools/rcxdownload/*.java rcxtools/share/gui/*.java rcxtools/share/tvm/*.java rcxdirect/*.java
rem javac -Xlint:unchecked -deprecation -d bin rcxtools/*.java rcxtools/filebrowser/*.java rcxtools/rcxdirectmode/*.java rcxtools/rcxdownload/*.java rcxtools/share/gui/*.java rcxtools/share/tvm/*.java rcxdirect/*.java
cd ..\RCXTools
