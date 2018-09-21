# introprog-scalalib

[![Build Status](https://travis-ci.org/lunduniversity/introprog-scalalib.svg?branch=master)](https://travis-ci.org/lunduniversity/introprog-scalalib)

This is a library with Scala utilities for Computer Science teaching. The library is maintained by Björn Regnell at Lund University, Sweden. Contributions are welcome!

The api documentation is available here: http://cs.lth.se/pgk/api/

You can fins example code here: [src/main/scala/introprog/examples](https://github.com/lunduniversity/introprog-scalalib/tree/master/src/main/scala/introprog/examples)

This repo in used in this course (in Swedish): http://cs.lth.se/pgk with course material published as free open source here: https://github.com/lunduniversity/introprog

## How to use this library
### Using sbt

If you have the [Scala Build Tool](https://www.scala-sbt.org/download.html) then you can put this text in a file called `build.sbt`
```
scalaVersion := "2.12.6"
libraryDependencies += "se.lth.cs" %% "introprog" % "0.1.5"
```

When you run `sbt` in terminal the introprog lib is automatically downloaded and made available on your classpath.
You can do things like:
```
> sbt
sbt> console
scala> val w = new introprog.PixelWindow()
scala> w.fill(100,100,100,100,java.awt.Color.red)
```

#### Manual download

Download the latest jar-file from here: https://github.com/lunduniversity/introprog-scalalib/releases

Put the jar-file on your classpath when you run the Scala REPL, for example:
```
> scala -cp introprog_2.12-0.1.5.jar
scala> val w = new introprog.PixelWindow()
scala> w.fill(100,100,100,100,java.awt.Color.red)
scala>
```
Put the jar-file on your classpath when you run your Scala app, for example:
```
> scala -cp "introprog_2.12-0.1.5.jar:." Main
```
If on Windows cmd/powershell use `;` instead of `:` before the period.
