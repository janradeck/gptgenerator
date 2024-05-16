# GPT Generator

Application that automatically sends prompts to ChatGPT and stores the replies as source code.

* Files whose name ends with ".prompt" are sent to ChatGPT. The response is saved with ".prompt" removed from the file name.
* Files whose name ends with ".fragment" are merged into other files

## Running the application

```
java -cp gptgenerator.jar gptgenerator.app.AppMain
```

## Running the application without GUI (headless)
```
java -cp gptgenerator.jar gptgenerator.app.AppMain --nogui
```

You can specify the path to the configuration file with the parameter --config

As a default the application looks for the configuration file in the current directory. 