rd /s /q rcxdirect
mkdir rcxdirect
cd ..\src
javadoc @..\apidoc\javadoc_options.txt rcxdirect\Battery.java rcxdirect\Motor.java rcxdirect\Motors.java rcxdirect\Port.java rcxdirect\Sensor.java rcxdirect\Sound.java
cd ..\apidoc
