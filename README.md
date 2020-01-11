# Learning Shapeless: Workshop Exercises

This repo contains a collections of [exercises](./src/main/scala) designed to help you learn the 
[Shapeless library](https://github.com/milessabin/shapeless) and Type-level programming. 

[Introductory slides](http://benhutchison.github.io/learningshapeless/slides/#/).

### Workshop setup

Assumes you have Java 8 and [sbt](http://www.scala-sbt.org/) pre-installed.

```
git clone https://github.com/benhutchison/learningshapeless.git
git clone https://github.com/benhutchison/learningshapeless.git -b solutions solutions-learning-shapeless
cd learning-shapeless
sbt   #leave sbt running between commands
> update
> run
#optionally to dig deeper into shapeless:
git clone https://github.com/milessabin/shapeless.git
```

### Exercise and Solution Branches 

On the `master` branch, there are a mix of annotated *code examples* (generally names `eg_<something>`) and *exercises*. 
Study the examples before trying the exercises, they demonstrate  features of Shapeless and relevant techniques.
 Code exercises are often named `ex_<something>`, but some exercises are described within inline docs, and may involve 
 annotating types, uncommenting code, or writing explanations.
 
The `solutions` branch contains solutions and/or explanations for all exercises. You can use this either to check your work 
after having attempted an exercise, or as a learning aid, if you're not able to progress.
 
### Experiment!
 
All exercises are runnable using SBTs `run` task. `println`s are sprinkled in so you can view expressions.
Alternately you can use SBTs `console` task to start a REPL with the projects' classes
 available for import & use. 
 
Be aware that for many exercises, getting them to `compile` can be a hard as getting them to run. 
 
## Suggested Order of Exercises
 
### Foundations
 
- [HLists](./src/main/scala/learnshapeless/HLists.scala)
- [Tagged Types](./src/main/scala/learnshapeless/TaggedTypes.scala)
- [Coproduct](./src/main/scala/learnshapeless/Coproduct.scala)
 
### Techniques for Generic Programming
 
- [Generic](./src/main/scala/learnshapeless/Generic.scala)
- [Singleton Types](./src/main/scala/learnshapeless/SingletonTypes.scala)
- [What is Aux?](./src/main/scala/learnshapeless/WhatIsAux.scala)
- [Labels & Records](./src/main/scala/learnshapeless/Labels.scala)
 
### Advanced

- [Induction](./src/main/scala/learnshapeless/Induction.scala)
- [Lazy](./src/main/scala/learnshapeless/Lazy.scala)
- [A complete use-case: Json Serialization](./src/main/scala/learnshapeless/JsonSerialization.scala)

### Other

- [Type Equality](./src/main/scala/learnshapeless/TypeEquality.scala)

## Known Issues

@ssanj pointed out that Shapeless `illTyped()` macro doesn't always work correctly in the REPL, ie anything you define inside of the REPL is not seen by illTyped ([stack overflow](http://stackoverflow.com/questions/20114298/how-do-i-write-a-scala-unit-test-that-ensures-compliation-fails#comment30067810_20169497))

