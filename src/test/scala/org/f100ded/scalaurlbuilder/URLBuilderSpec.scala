package org.f100ded.scalaurlbuilder

import org.scalatest.FlatSpec

class URLBuilderSpec extends FlatSpec {
  behavior of "URLBuilder"
  val base = URLBuilder("http://localhost/")

  it should "handle bases with and without trailing slash" in {
    assert(base.toString == "http://localhost/")
    assert(URLBuilder("http://api/v1").withPathSegments("segment").toString() == "http://api/v1/segment")
    assert(URLBuilder("http://api/v1/").withPathSegments("segment").toString() == "http://api/v1/segment")
  }

  it should "handle a simple segment" in {
    assert(base.withPathSegments("segment1").toString() == "http://localhost/segment1")
  }

  it should "handle multiple segments" in {
    assert(base.withPathSegments("seg1/seg2/seg3").toString() == "http://localhost/seg1/seg2/seg3")
  }

  it should "handle leading-slash segments" in {
    assert(base.withPathSegments("/").toString() == "http://localhost/")
    assert(base.withPathSegments("/segment1").toString() == "http://localhost/segment1")
  }

  it should "handle trailing-slash segments" in {
    assert(base.withPathSegments("segment1").toString() == "http://localhost/segment1")
    assert(base.withPathSegments("segment1/").toString() == "http://localhost/segment1/")
  }

  it should "handle single-dot and double-dot segments" in {
    assert(base.withPathSegments("segment1/.").toString() == "http://localhost/segment1")
    assert(base.withPathSegments("segment1/./").toString() == "http://localhost/segment1/")
    assert(base.withPathSegments("segment1/./segment2").toString() == "http://localhost/segment1/segment2")
    assert(base.withPathSegments("segment1/segment2/../segment3").toString() == "http://localhost/segment1/segment3")
    assert(base.withPathSegments("segment1/segment2/..").toString() == "http://localhost/segment1")
    assert(base.withPathSegments("segment1/segment2/../").toString() == "http://localhost/segment1/")
  }

  it should "handle double-slash segments" in {
    assert(base.withPathSegments("//segment1").toString() == "http://localhost/segment1")
    assert(base.withPathSegments("///segment1").toString() == "http://localhost/segment1")
    assert(base.withPathSegments("////segment1").toString() == "http://localhost/segment1")
    assert(base.withPathSegments("segment1//segment2").toString() == "http://localhost/segment1/segment2")
    assert(base.withPathSegments("segment1///segment2").toString() == "http://localhost/segment1/segment2")
    assert(base.withPathSegments("segment1////segment2").toString() == "http://localhost/segment1/segment2")
    assert(base.withPathSegments("segment1/////segment2").toString() == "http://localhost/segment1/segment2")
    assert(base.withPathSegments("segment1/segment2//").toString() == "http://localhost/segment1/segment2/")
    assert(base.withPathSegments("segment1/segment2///").toString() == "http://localhost/segment1/segment2/")
    assert(base.withPathSegments("segment1/segment2////").toString() == "http://localhost/segment1/segment2/")
  }

  it should "handle a single query parameter" in {
    assert(base.withQueryParameters("x" -> "1").toString() == "http://localhost/?x=1")
  }

  it should "handle multiple query parameters" in {
    assert(base.withQueryParameters("x" -> "1", "y" -> "2", "z" -> "3").toString() == "http://localhost/?x=1&y=2&z=3")
  }

  it should "handle base with query parameters" in {
    val newBase = URLBuilder("http://localhost/segment0?a=1&b=2&c=3")
    assert(newBase.withQueryParameters("d" -> "4").toString == "http://localhost/segment0?a=1&b=2&c=3&d=4")
    assert(newBase.withPathSegments("segment1").toString == "http://localhost/segment0/segment1?a=1&b=2&c=3")
    assert(newBase.withPathSegments("segment1/").toString == "http://localhost/segment0/segment1/?a=1&b=2&c=3")
    assert(newBase.withPathSegments("seg1/seg2").toString == "http://localhost/segment0/seg1/seg2?a=1&b=2&c=3")
  }

  it should "handle encoded segments" in {
    assert(base.withPathSegments("%7Bhello%7D").toString == "http://localhost/%7Bhello%7D")
    assert(base.withPathSegments("%7Bhello%7D/world/..").toString == "http://localhost/%7Bhello%7D")
  }

  it should "handle url-unsafe segments" in {
    assert(base.withPathSegments("{hello}/world").toString == "http://localhost/%7Bhello%7D/world")
  }

  it should "handle url-unsafe parameters" in {
    assert(base.withQueryParameters("x" -> "http://a/b c/c?x=1").toString ==
      "http://localhost/?x=http%3A%2F%2Fa%2Fb+c%2Fc%3Fx%3D1"
    )
  }

  it should "handle fragments with and without leading #" in {
    assert(URLBuilder("http://api/v1").withFragment("fragment").toString() == "http://api/v1#fragment")
    assert(URLBuilder("http://api/v1").withFragment("#fragment").toString() == "http://api/v1#fragment")
    assert(URLBuilder("http://api/v1").withFragment("##fragment").toString() == "http://api/v1#%23fragment")
  }

  it should "construct an URL" in {
    val expectedString = "http://localhost/segment1/segment2/file1#ref1?q1=http%3A%2F%2Fa%2Fb+c%2Fc%3Fx%3D1"
    val builder = URLBuilder("http://localhost")
      .withPathSegments("segment1/segment2", "file1")
      .withQueryParameters("q1" -> "http://a/b c/c?x=1")
      .withFragment("ref1")

    assert(builder.toURL.toString == expectedString)
  }
}