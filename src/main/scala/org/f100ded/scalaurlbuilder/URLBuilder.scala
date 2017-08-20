package org.f100ded.scalaurlbuilder

import java.net.{URL, URLDecoder, URLEncoder}

/**
  * Simple way of constructing URLs
  */
class URLBuilder private(base: String, pathSegments: List[String], queryParams: List[(String, String)]) {
  final def addPathSegments(segments: String): URLBuilder = segments.split("/", 2).toList match {
    case Nil => this
    case head :: tail =>
      lazy val cleanSegments = if (pathSegments.nonEmpty && pathSegments.last.isEmpty) {
        pathSegments.dropRight(1)
      } else {
        pathSegments
      }

      val builder = head match {
        case s if s == "." => this
        case s if s == ".." => new URLBuilder(base, cleanSegments.dropRight(1), queryParams)
        case s => new URLBuilder(base, cleanSegments :+ decode(s), queryParams)
      }

      tail.foldLeft(builder) {
        case (acc, v) => acc.addPathSegments(v)
      }
  }

  def addQueryParameters(params: (String, String)*): URLBuilder = {
    new URLBuilder(base, pathSegments, queryParams ++ params.map { case (p, v) => (decode(p), decode(v)) })
  }

  override def toString: String = {
    val segments = Option(pathSegments).filter(_.nonEmpty).getOrElse(List("")).map(encode)
    val location = (base +: segments).mkString("/")
    val parameters = queryParams.map { case (p, v) => s"${encode(p)}=${encode(v)}" }.mkString("&")
    if (parameters.isEmpty) {
      location
    } else {
      s"$location?$parameters"
    }
  }

  private def decode(value: String): String = URLDecoder.decode(value, "UTF-8")

  private def encode(value: String): String = URLEncoder.encode(value, "UTF-8")
}

object URLBuilder {
  def apply(base: URL): URLBuilder = {
    val parameters = Option(base.getQuery).toSeq.flatMap {
      _.split('&').flatMap(x => x.split("=", 2).toList match {
        case a :: b :: Nil => Some(a -> b)
        case a :: Nil => Some(a -> "")
        case Nil => None
      })
    }
    new URLBuilder(s"${base.getProtocol}://${base.getAuthority}", List(), List())
      .addPathSegments(base.getPath)
      .addQueryParameters(parameters: _*)
  }

  def apply(base: String): URLBuilder = apply(new URL(base))
}