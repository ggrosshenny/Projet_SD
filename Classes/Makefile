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
	$(JC) $(JFLAGS) Agent.java Ressource.java ProductionTask.java Producteur*.java
	cp *.class ../Bin
	rm *.class

joueur:
	$(JC) $(JFLAGS) Agent.java Joueur*.java
	cp *.class ../Bin
	rm *.class

clean:
	rm ../Bin/*.class

