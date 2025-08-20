# introprog-scalalib

![Build Status](https://github.com/lunduniversity/introprog-scalalib/actions/workflows/main.yml/badge.svg)

[<img src="https://img.shields.io/maven-central/v/se.lth.cs/introprog_3.svg?label=latest%20release%20for%20Scala%203">](http://search.maven.org/#search%7Cga%7C1%7Cg%3Ase.lth.cs%20a%3Aintroprog_3)  [<img src="https://img.shields.io/maven-central/v/se.lth.cs/introprog_2.13.svg?label=latest%20release%20for%20Scala%20%202.13">](http://search.maven.org/#search%7Cga%7C1%7Cg%3Ase.lth.cs%20a%3Aintroprog_2.13)  [<img src="https://img.shields.io/maven-central/v/se.lth.cs/introprog_2.12.svg?label=latest%20release%20for%20Scala%20%202.12">](http://search.maven.org/#search%7Cga%7C1%7Cg%3Ase.lth.cs%20a%3Aintroprog_2.12)

This is a library with Scala utilities for Computer Science teaching. The library is maintained by Björn Regnell at Lund University, Sweden. Contributions are welcome!

* The api **documentation** is available here: http://cs.lth.se/pgk/api/

* You can find code **examples** here: [src/main/scala/introprog/examples](https://github.com/lunduniversity/introprog-scalalib/tree/master/src/main/scala/introprog/examples)

This repo is used in this course *(in Swedish)*: http://cs.lth.se/pgk with course material published as free open source here: https://github.com/lunduniversity/introprog


## How to use introprog-scalalib

### Getting started using scala from the command line

You need to have [Scala installed](https://www.scala-lang.org/download/) using version 3.5.2 or later.

You can start the Scala REPL in the current directory with `introprog` directly available to play with using this command in a terminal window:
```
scala repl . --dep se.lth.cs::introprog:1.4.0
```

You can then open a drawing window like so:
```scala
scala> val w = introprog.PixelWindow()
val w: introprog.PixelWindow = introprog.PixelWindow@34f60be9

scala> w.drawText("Hello introprog.PixelWindow!", x = 100, y = 100)
```

If you want to use `introprog` in your program, add these magic comment lines starting with `//>` in the beginning of your Scala 3 file (update the version number after `//> using scala` to the [latest release](https://www.scala-lang.org/)): 

```
//> using scala 3.7.2
//> using dep se.lth.cs::introprog:1.4.0
```

You can then run your code with `scala run .` (note the ending dot, meaning "current dir")

If your program looks like this:

```
//> using scala 3.7.2
//> using dep se.lth.cs::introprog:1.4.0

@main def run = 
  val w = introprog.PixelWindow()
  w.drawText("Hello introprog.PixelWindow!", x = 100, y = 100)
```
You should see green text in a new window after executing:
```
scala-cli run .
```
See: [api documentation for PixelWindow](https://fileadmin.cs.lth.se/pgk/api/api/introprog/PixelWindow.html) for more things you can do with a PixelWindow.

You can also give the `introprog` dependency directly at the command line, instead of the `using dep` directive:
```
scala-cli run . --dep se.lth.cs::introprog:1.4.0
```

### Getting started using sbt

If you use the [Scala Build Tool, version 1.6 or later](https://www.scala-sbt.org/download.html) then put this text in a file called `build.sbt`
```
scalaVersion := "3.7.2"
libraryDependencies += "se.lth.cs" %% "introprog" % "1.4.0"
```

When you run `sbt` in terminal the `introprog` package is automatically downloaded and made available on your classpath.
You can do things like:
```
> sbt
sbt> console
scala> val w = new introprog.PixelWindow()
scala> w.fill(100,100,100,100,java.awt.Color.red)
```
See: [api documentation for PixelWindow](https://fileadmin.cs.lth.se/pgk/api/api/introprog/PixelWindow.html)

### Older Scala versions

If you want to use Scala 2.13 with 2.13.5 or later then use these special settings in `build.sbt`, esp. note that you should use version 1.1.5 of introprog: 
```
scalaVersion := "2.13.8" //2.13.5 or any later 2.13 version
scalacOptions += "-Ytasty-reader"
libraryDependencies += 
  ("se.lth.cs" %% "introprog" % "1.1.5").cross(CrossVersion.for2_13Use3)
```

For Scala 2.12.x and 2.13.4 and older you need to use version 1.1.4 of introprog or older. 


### Manual download

Download the latest jar-file from here: 
* Github releases: https://github.com/lunduniversity/introprog-scalalib/releases
* Scaladex: https://index.scala-lang.org/lunduniversity/introprog-scalalib
* Search Maven central: https://search.maven.org/search?q=introprog
* Maven central server: https://repo1.maven.org/maven2/se/lth/cs/

Put the latest introprog jar-file in your sbt project in a subfolder called `lib`.  In your `build.sbt` you only need `scalaVersion := "3.0.1"` without a library dependency to introprog, as `sbt` automatically put jars in lib on your classpath.

## How to build introprog-scalalib

With [`sbt`](https://www.scala-sbt.org/download.html) and [`git`](https://git-scm.com/downloads) on your path type in terminal:
```
> git clone git@github.com:lunduniversity/introprog-scalalib.git
> cd introprog-scalalib
> sbt package
```

## How to build and see the doc pages using a local server

Run this in linux bash terminal:
```
sbt doc && cd target/scala-3.3.3/api && python3 -m http.server 8080
```
Open Firefox and type this url in the address field:
```
http://localhost:8080/
``` 

## Intentions and philosophy behind introprog-scalalib

This repo includes utilities to empower learners to advance from basic to intermediate levels of computer science by providing easy-to-use constructs for creating simple desktop apps in terminal and using simple 2D graphics. The utilities are implemented and exposed through an api that follows these guidelines:

* Use as simple constructs as possible.
* Follow Scala idioms with a pragmatic mix of imperative, functional and object-oriented programming.
* Don't use advanced functional programming concepts and magical implicits.
* Prefer a clean api with single-responsibility functions in simple modules.
* Prefer immutability over mutable state, `Vector` for sequences and case classes for data.
* Hide/avoid threading and complicated concurrency.
* Inspiration:
  - Talk by Martin Odersky: [Scala the Simple Parts](https://www.youtube.com/watch?v=ecekSCX3B4Q) with slides [here](https://www.slideshare.net/Odersky/scala-the-simple-parts)
  - [Principle of least power](http://www.lihaoyi.com/post/StrategicScalaStylePrincipleofLeastPower.html) blog post by Li Haoyi

Areas currently in scope of this library:

* Simple pixel-based 2D graphics for single-threaded game programming with explicit game loop.
* Simple blocking IO that hides the underlying complication of releasing resources etc.
* Simple modal GUI dialogs that block while waiting for user response.
