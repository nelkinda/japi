.PHONY: all
all:
	./mvnw verify

.PHONY: clean
clean::
	./mvnw clean

-include User.mk
-include ~/User.mk
