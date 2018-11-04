default:
	chmod 777 src/Chord.java
	sudo apt-get update
	sudo apt-get install default-jdk -y
	sudo cp -pf script.sh /usr/bin/chord
	chmod 777 /usr/bin/chord