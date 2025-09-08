# Report on setting up Polling API

What have been done:

Set up basic java classes representing the `model` part of the design, these objects can be made and tested independetly from springboot setups. 
Then springboot specific classes that use these model objects were made such as PollController, UserController and Votecontroller. The controllers annotated with `@RestController` can interact with http requests and handle JSON deserialisation/serialisation via `Jackson`. I was impressed with how easy it was getting to a working API using this functionality. It's straight forward to handle which parts of Java objects should be included in JSON.

We were also adviced to use lombok which turns out to be great advice because it made setting up all the basic data classes really simple. 

Overall this task went pretty smoothly where the biggest problem I had was jumping into implementation too quickly before properly reading the task, this left me with a functional multi-question poll with a working React front-end, the latter took some time to iron out all the minor React/JavaScript-problems (stable object IDs..etc..). Then it took more time to backtrack to the much simpler one question poll. 

## Issues

Initially I had some problems with setup which was fixed by setting classpath variable in `gradlew.bat`, after this was done I didn't have direct technical problems.

## Further improvements?

1. Expand testing suite.

    Right now there's some basic model-level unit tests as well as a few intergration tests using a suggest RestClient setup, this is Okay but coverage is too small. Ideallly different components would be mocked as well so that finding the real problems is easier. 

2. Bad error handling:

    Improve error handling. The errors should propogate up to controllers that should send clear errors to the user, right now the design is slightly amateurish, but I will improve on this. 

3. Github actions:
I have set up automatic testing via github actions, but I think this can be improved in-terms of speed by making a docker image for this which lower setup time for gradle, caching is an alternative. 


There is also numerous front-end improvements that need to be dealt with but this will be detailed in further assignments. 
