default:
	sudo apt-get update
	sudo apt-get install default-jdk -y
	sudo cp -pf script.sh /usr/bin/chord
