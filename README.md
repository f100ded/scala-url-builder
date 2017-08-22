[![Build status](https://travis-ci.org/f100ded/scala-url-builder.svg?branch=master)](https://travis-ci.org/f100ded/scala-url-builder) [![Coverage Status](https://coveralls.io/repos/f100ded/scala-url-builder/badge.svg?branch=master&service=github)](https://coveralls.io/github/f100ded/scala-url-builder?branch=master)

# Scala URL Builder 

Scala URL Builder is a minimalist library for constructing URLs. It's native to Scala, immutable and has no external dependencies

## Usage

To get started, add the following dependency into your project:
```scala
libraryDependencies += "org.f100ded.scala-url-builder" % "scala-url-builder" % "1.0.0"
```

And then you can start using the builder
```scala
import java.net.URL
import org.f100ded.scalaurlbuilder.URLBuilder

val base = URLBuilder(base = "https://foo/bar?api_key=foo_bar")
val endpoint1 = base.withPathSegments("segment1/segment2/segment3/")
val endpoint2 = endpoint1.withPathSegments("..", "segment4", "segment5")
val endpoint3 = (id: Int) => endpoint2.withPathSegments(s"$id")

val url1: URL = endpoint1.withQueryParameters("lat" -> "51.509865", "lon" -> "‎-0.118092").toURL
val url2: String = endpoint2.toString
val url3: URL = endpoint3(123).toURL
```
