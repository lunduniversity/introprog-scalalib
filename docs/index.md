---
---

This is the documentation of the `introprog` Scala Library with beginner-friendly utilities used in computer science teaching at Lund University.
The open source code is hosted at [[https://github.com/lunduniversity/introprog-scalalib]].

## Package contents

- [[introprog.PixelWindow]] for simple, pixel-based drawing.

- [[introprog.PixelWindow.Event]] for event management in a PixelWindow.

- [[introprog.IO]] for file system interaction.

- [[introprog.Dialog]] for user interaction with standard GUI dialogs.

- [[introprog.BlockGame]] an abstract class to be inherited by games using block graphics.

- [[introprog.examples]] with code examples demonstrating how to use this library.

## How to use introprog-scalalib

You need [Scala](https://www.scala-lang.org/download/) installed, Scala 3.8.3 or later recommended.

## Start a repl session with introprog

The Scala repl (read-evaluate-print-loop) allow you to run Scala-lines one at a time. Start the Scala repl with introprog available like so:

`scala repl . --dep "se.lth.cs::introprog:1.5.0" --jvm 21`

Note the dot after `repl`, which means "this dir" making all scala code in the current dir available in the repl.

Now you can open a drawing window by typing this after the `scala>` prompt:

```
val w = introprog.PixelWindow()
w.drawText("Hello introprog.PixelWindow!", x = 100, y = 100)
```

## Use introprog in your Scala programs

Or add these magic comment lines starting with `//>` in the beginning of your Scala 3 file: 

```
//> using scala 3.8.3
//> using dep "se.lth.cs::introprog:1.5.0"
```
You can use the latest stable Scala version, or any version from at least Scala 3.3.7.

You run your code in the current dir with `scala run .` (note the ending dot, meaning "this dir")

If your program looks like this:

```
//> using scala 3.8.3
//> using dep "se.lth.cs::introprog:1.5.0"

@main def MyMain = 
  val w = introprog.PixelWindow()
  w.drawText("Hello introprog.PixelWindow!", x = 100, y = 100)
```
You should see green text in a new window after executing:
```
scala-cli run .
```
See: [api documentation for PixelWindow](https://fileadmin.cs.lth.se/pgk/api/api/introprog/PixelWindow.html)

### Using sbt

If you have [sbt](https://www.scala-sbt.org/) installed at least version 1.12.9 then you can put this text in a file called `build.sbt`

```
scalaVersion := "3.8.3"  // or any Scala version from at least 3.3.7
libraryDependencies += "se.lth.cs" %% "introprog" % "1.5.0"
```

When you run `sbt` in a terminal, with the above in your `build.sbt`, the introprog lib is automatically downloaded and made available on your classpath. Then you can do things like:

```
sbt> console
scala> val w = new introprog.PixelWindow()
scala> w.fill(100,100,100,100,java.awt.Color.red)
```

## Manual download

You can also manually download the latest jar file from here: 

* Lund University: [http://www.cs.lth.se/pgk/lib](http://www.cs.lth.se/pgk/lib) 

* GitHub: [https://github.com/lunduniversity/introprog-scalalib/releases](https://github.com/lunduniversity/introprog-scalalib/releases)

* ScalaDex: [https://index.scala-lang.org/lunduniversity/introprog-scalalib/introprog](https://index.scala-lang.org/lunduniversity/introprog-scalalib/introprog) 

* Maven Central: [https://repo1.maven.org/maven2/se/lth/cs/introprog_3/](https://repo1.maven.org/maven2/se/lth/cs/introprog_3/)