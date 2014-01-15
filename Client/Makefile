target: *.class

*.class: *.java
	javac -Xlint *.java

clean:
	rm -rf *.class TestSpel.jar myKeys
	rm -rf *~

TestSpel.jar: Manifest.txt *.class bilder/* myKeys TestSpel.jnlp
	jar cfmv TestSpel.jar Manifest.txt *.class bilder/
	jarsigner -keystore myKeys TestSpel.jar jdc

myKeys:
	keytool -genkey -keystore myKeys -alias jdc
