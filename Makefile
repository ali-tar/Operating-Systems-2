JAVAC=/usr/bin/javac
.SUFFIXES: .java .class
SRCDIR=src
BINDIR=bin
DOCDIR=doc

$(BINDIR)/%.class:$(SRCDIR)/%.java
	$(JAVAC) -d $(BINDIR)/ -cp $(BINDIR) $<

CLASSES= BarrierReusable.class Propane.class Carbon.class Hydrogen.class RunSimulation.class
CLASS_FILES=$(CLASSES:%.class=$(BINDIR)/%.class)
default: $(CLASS_FILES)
clean:
	rm $(BINDIR)/*.class

run:
	java -cp bin molecule.src.RunSimulation "24" "9"

docs:
	javadoc -d $(DOCDIR) src/*.java
