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
import org.f100ded.scalaurlbuilder.URLBuilder

val builder = URLBuilder(base = "http://foo/bar?city=London")
  .withPathSegments("semgent1/segment2/../segment3", "/segment4", "segment5/")
  .withQueryParameters(
    "lat" -> "51.509865",
    "lon" -> "‎-0.118092"
  )

builder.toString // constructed URL as a string
builder.toURL // an instance of java.net.URL
```
