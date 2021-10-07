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

## How to use this library with `sbt`

If you have [sbt](https://www.scala-sbt.org/) installed then you can put this text in a file called `build.sbt`

```
scalaVersion := "3.0.2"
libraryDependencies += "se.lth.cs" %% "introprog" % "1.3.1"
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