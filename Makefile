NAME = review
ARGS = ""

run all:
	#javac -d . ./*/*.java
	javac -d . ./DLX/*.java
	javac $(NAME).java
	java $(NAME) $(ARGS)

clean:
	rm *.class
