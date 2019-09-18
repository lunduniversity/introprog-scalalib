# introprog-scalalib

[![Build Status](https://travis-ci.org/lunduniversity/introprog-scalalib.svg?branch=master)](https://travis-ci.org/lunduniversity/introprog-scalalib)

This is a library with Scala utilities for Computer Science teaching. The library is maintained by Björn Regnell at Lund University, Sweden. Contributions are welcome!

* The api **documentation** is available here: http://cs.lth.se/pgk/api/

* You can find code **examples** here: [src/main/scala/introprog/examples](https://github.com/lunduniversity/introprog-scalalib/tree/master/src/main/scala/introprog/examples)

This repo is used in this course *(in Swedish)*: http://cs.lth.se/pgk with course material published as free open source here: https://github.com/lunduniversity/introprog


## How to use introprog-scalalib
### Using sbt

If you have the [Scala Build Tool](https://www.scala-sbt.org/download.html) then you can put this text in a file called `build.sbt`
```
scalaVersion := "2.12.10"
libraryDependencies += "se.lth.cs" %% "introprog" % "1.1.3"
```

When you run `sbt` in terminal the `introprog` package is automatically downloaded and made available on your classpath.
You can do things like:
```
> sbt
sbt> console
scala> val w = new introprog.PixelWindow()
scala> w.fill(100,100,100,100,java.awt.Color.red)
```

### Manual download

Download the latest jar-file from here: https://github.com/lunduniversity/introprog-scalalib/releases

Or from Maven central: https://search.maven.org/search?q=a:introprog_2.12

Put the jar-file on your classpath when you run the Scala REPL, for example:
```
> scala -cp introprog_2.12-1.1.3.jar
scala> val w = new introprog.PixelWindow()
scala> w.fill(100,100,100,100,java.awt.Color.red)
scala>
```
Put the jar-file on your classpath when you run your Scala app, for example:
```
> scala -cp "introprog_2.12-1.1.3.jar:." Main
```
If on Windows cmd/powershell use `;` instead of `:` before the period.

## How to build introprog-scalalib
With [`sbt`](https://www.scala-sbt.org/download.html) and [`git`](https://git-scm.com/downloads) on your path type in terminal:
```
> git clone git@github.com:lunduniversity/introprog-scalalib.git
> cd introprog-scalalib
> sbt package
```

## Intentions and philosophy behind introprog-scalalib

This repo includes utilities to empower learners to advance from basic to intermediate levels of computer science by providing easy-to-use constructs for creating simple desktop apps in terminal and using simple 2D graphics. The utilities are implemented and exposed through an api that follows these guidelines:

* Use as simple constructs as possible.
* Follow Scala idioms with a pragmatic mix of imperative, functional and object-oriented programming.
* Don't use advanced functional programming concepts and magical implicit.
* Prefer a clean api with single-responsibility functions in simple modules.
* Prefer immutability over mutable state, `Vector` for sequences and case classes for data.
* Hide/avoid threading and complicated concurrency.
* Inspiration:
  - Talk by Martin Odersky: [Scala the Simple Parts](https://www.youtube.com/watch?v=ecekSCX3B4Q) with slides [here](https://www.slideshare.net/Odersky/scala-the-simple-parts)
  - [Principle of least power](http://www.lihaoyi.com/post/StrategicScalaStylePrincipleofLeastPower.html) blog post by Li Haoyi

Areas currently in scope of this library:

* Simple pixel-based 2D graphics for single-threaded game programming with explicit game loop.
* Simple blocking IO that hides the underlying complication of releasing resources etc.
* Simple modal GUI dialogs that blocks while waiting for user response.
