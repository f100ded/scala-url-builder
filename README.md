[![Build status](https://travis-ci.org/f100ded/scala-url-builder.svg?branch=master)](https://travis-ci.org/f100ded/scala-url-builder)
[![Coverage Status](https://coveralls.io/repos/f100ded/scala-url-builder/badge.svg?branch=master&service=github)](https://coveralls.io/github/f100ded/scala-url-builder?branch=master)
[![Maven](https://img.shields.io/maven-central/v/org.f100ded.scala-url-builder/scala-url-builder_2.12.svg)](http://mvnrepository.com/artifact/org.f100ded.scala-url-builder/scala-url-builder_2.12)

# Scala URL Builder 

Scala URL Builder is a minimalist library for constructing URLs. It's native to Scala, immutable and has no external dependencies

## Usage

To get started, add the following dependency into your project:
```scala
libraryDependencies += "org.f100ded.scala-url-builder" %% "scala-url-builder" % "LATEST_VERSION"
```
Replace LATEST_VERSION with the actual release version from [the releases page](https://github.com/f100ded/scala-url-builder/releases).

And then you can start using the builder:
```scala
import org.f100ded.scalaurlbuilder.URLBuilder

val url = URLBuilder(base = "https://foo/bar?api_key=foo_bar")
    .withFragment("#ref1")
    .withPathSegments("segment1/segment2/segment3/", "..", "/segment4", "segment5/")
    .withQueryParameters(
      "lat" -> "51.509865",
      "lon" -> "‎-0.118092"
    ).toString
```
