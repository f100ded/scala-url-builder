package org.f100ded.scalaurlbuilder

import java.net.{MalformedURLException, URL, URLDecoder, URLEncoder}

/**
  * Simple way of constructing URLs
  */
class URLBuilder private(base: String,
                         pathSegments: List[String],
                         val queryParameters: Option[String],
                         val fragment: Option[String]) {
  lazy val location: String = (Seq(base) ++ pathSegments.map(encode)).mkString("/") +
    fragment.map("#" + _).getOrElse("")

  /**
    * Adds given segments and returns a new builder with the added segments.
    * The segments can be separated by a slash (/) or represented as separate values in the given sequence of segments.
    *
    * @param segments Segments added to the builder
    * @return A new builder created from the current one with added segments
    */
  def withPathSegments(segments: String*): URLBuilder = {
    val flatSegments = segments.flatMap(_.split("/", Integer.MAX_VALUE))
    flatSegments.foldLeft(this) {
      case (builder, segment) => builder.withSegment(segment)
    }
  }

  /**
    * Adds given query parameters and returns a new builder with the added parameters.
    *
    * @param params Query string parameters added to the builder
    * @return A new builder created from the current one with added query parameters
    */
  def withQueryParameters(params: (String, String)*): URLBuilder = {
    val render = normalize(_: String) + "=" + normalize(_: String)
    val newQueryParameters = Seq(queryParameters.toSeq, params.map(render.tupled))
      .flatten
      .filter(_.length > 1)
      .mkString("&")

    new URLBuilder(base, pathSegments, Some(newQueryParameters), fragment)
  }

  /**
    * A new builder containing updating this builder's URL fragment with the given fragment
    * @param newFragment A new fragment
    * @return A new builder with the fragment
    */
  def withFragment(newFragment: String): URLBuilder = {
    new URLBuilder(base, pathSegments, queryParameters, Some(normalize(newFragment.stripPrefix("#"))))
  }

  /**
    * Builds a string representing the constructed URL
    *
    * @return A string representation of the URL constructed by the current builder
    */
  override def toString: String = queryParameters match {
    case Some(params) => s"$location?$params"
    case None => location
  }

  /**
    * Builds the constructed URL
    *
    * @return The URL constructed by the current builder
    */
  def toURL: URL = new URL(toString)

  private def withSegment(segment: String): URLBuilder = segment match {
    case s if s == "." => this
    case s if s == ".." => new URLBuilder(base, pathSegments.dropRight(1), queryParameters, fragment)
    case s => new URLBuilder(base, pathSegments.filter(_.nonEmpty) :+ decode(s), queryParameters, fragment)
  }

  private def decode(value: String): String = URLDecoder.decode(value, "UTF-8")

  private def encode(value: String): String = URLEncoder.encode(value, "UTF-8")

  private def normalize(s: String): String = encode(decode(s))
}

object URLBuilder {
  /**
    * Returns a new URLBuilder with the given base URL
    *
    * @param base Base URL
    * @return A new URLBuilder instance
    */
  def apply(base: URL): URLBuilder = new URLBuilder(
    base = s"${base.getProtocol}://${base.getAuthority}",
    pathSegments = List(),
    queryParameters = Option(base.getQuery),
    fragment = Option(base.getRef)
  ).withPathSegments(base.getPath)

  /**
    * Returns a new URLBuilder with the given base URL
    *
    * @throws MalformedURLException if the given base is not a valid URL
    * @param base String representation of the base URL
    * @return A new URLBuilder instance
    */
  def apply(base: String): URLBuilder = apply(new URL(base))
}