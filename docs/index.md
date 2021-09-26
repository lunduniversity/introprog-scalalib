---
---

This is the documentation of the `introprog` Scala Library with beginner-friendly utilities used in computer science teaching at Lund University.
The code repository is hosted at [[https://github.com/lunduniversity/introprog-scalalib]].

## Package contents

- [[introprog.PixelWindow]] for simple, pixel-based drawing.

- [[introprog.PixelWindow.Event]] for event management in a PixelWindow.

- [[introprog.IO]] for file system interaction.

- [[introprog.Dialog]] for user interaction with standard GUI dialogs.

- [[introprog.BlockGame]] an abstract class to be inherited by games using block graphics.

- [[introprog.examples]] with code examples demonstrating how to use this library.

## How to use this library with `sbt`

If you have [sbt](https://www.scala-sbt.org/) version 1.5.2 or later installed then you can put this text in a file called `build.sbt`

```
scalaVersion := "3.0.2"
libraryDependencies += "se.lth.cs" %% "introprog" % "1.2.0"
```

You can find the latest version of introprog-scalalib on [scaladex](https://index.scala-lang.org/lunduniversity/introprog-scalalib/introprog).

When you run `sbt` in a terminal the introprog lib is automatically downloaded and made available on your classpath.
Then you can do things like:

```
> sbt
sbt> console
scala> val w = new introprog.PixelWindow()
scala> w.fill(100,100,100,100,java.awt.Color.red)
```
