#Spellchecker

Spell checker service composed by server and client.

##Requirements

The server exposes two simple methods:

* check(language,word):boolean
* add(language, word):boolean

The client has only a command line interface. When started you are required to provide the name of file to parse. During the parsing phase if a word is not recognized by the server you have two choices: add the word to the common repository or discard it and carry on with the parsing. When the file has been completely parsed the client shows how many words are not valid and the list of them.

Client and server are going to be deployed in different locations. You can decide which type of communication has to be implemented between client and server, however the data exchanged between them has to be maintained as low as possible.

Spring framework can't be used to implement this solution.

##Solution

* Created three maven projects, httpclient, encapsulating HTTP communication, spellchecker and spellcheckerClient
* The server has been developed using the Spark framework (http://www.sparkjava.com/). It contains a jetty server embedded, this way it's not necessary to package the application and deploy it in a WAS. It can be executed as a simple Java application, and exposes the two methods.
* The methods have been published as REST Web Services. This changes a bit the value they can return, and it's not a boolean anymore. Of course, if everything is OK they return the status 200, but depending on the service, the other return values differ, between 404 (NOT FOUND) for the check service or 409 (CONFLICT) for the add service. It's a conflict because you can't add a word that already exists in the repository. The services also return a JSON in the responseBody with additional information.
* To check the validity of the words, I have used the WordReference API (http://www.wordreference.com/docs/api.aspx). I had to create a personal account and use an API key. The problem with this API is that it's limited to 600 requests per hour, so if you test the application several times in a row it can fail.
* To manage the http communication Client <-> Server <-> WordReference, I have used the Apache HttpClient API
* I assume that there can't be no hyphens at the end of the lines in the file, so every line only contains full words
* If the user decides to add a word that doesn't exist in WordReference to the repository, that word becomes a valid word
* No unit tests, sorry, just playing around :)
* No logging API. To report errors the applications use the standard output.

##Execution

Finally, to execute the applications, you can run them using an IDE, or using maven:

Server: `mvn compile exec:java -Dexec.mainClass="com.raulavila.spellchecker.server.SpellCheckerServer"`
Client: `mvn compile exec:java -Dexec.mainClass="com.raulavila.spchclient.client.SpellCheckerClient"`

Note that httpclient project must be installed in the local repository previous to the execution of the projects.
