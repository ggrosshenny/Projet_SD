########################
#  Makefile du projet  #
########################


# Define compiler and compiler flag variables
# --------

JFLAGS = -g
JC = javac

default: 
	$(JC) $(JFLAGS) *.java
	cp *.class ../Bin
	rm *.class

producteur:
	$(JC) $(JFLAGS) Agent.java Producteur*.java ProductionTask.java
	cp *.class ../Bin
	rm *.class

clean:
	rm ../Bin/*.class

