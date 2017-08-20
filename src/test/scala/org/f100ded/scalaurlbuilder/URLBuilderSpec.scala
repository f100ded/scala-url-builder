package org.f100ded.scalaurlbuilder

import org.scalatest.FlatSpec

class URLBuilderSpec extends FlatSpec {
  behavior of "URLBuilder"
  val base = URLBuilder("http://localhost/")

  it should "handle bases with and without trailing slash" in {
    assert(URLBuilder("http://api/v1").addPathSegments("segment").toString() == "http://api/v1/segment")
    assert(URLBuilder("http://api/v1/").addPathSegments("segment").toString() == "http://api/v1/segment")
  }

  it should "handle a simple segment" in {
    assert(base.addPathSegments("segment1").toString() == "http://localhost/segment1")
  }

  it should "handle multiple segments" in {
    assert(base.addPathSegments("seg1/seg2/seg3").toString() == "http://localhost/seg1/seg2/seg3")
  }

  it should "handle leading-slash segments" in {
    assert(base.addPathSegments("/").toString() == "http://localhost/")
    assert(base.addPathSegments("/segment1").toString() == "http://localhost/segment1")
  }

  it should "handle trailing-slash segments" in {
    assert(base.addPathSegments("segment1").toString() == "http://localhost/segment1")
    assert(base.addPathSegments("segment1/").toString() == "http://localhost/segment1/")
  }

  it should "handle single-dot and double-dot segments" in {
    assert(base.addPathSegments("segment1/.").toString() == "http://localhost/segment1")
    assert(base.addPathSegments("segment1/./").toString() == "http://localhost/segment1/")
    assert(base.addPathSegments("segment1/./segment2").toString() == "http://localhost/segment1/segment2")
    assert(base.addPathSegments("segment1/segment2/../segment3").toString() == "http://localhost/segment1/segment3")
    assert(base.addPathSegments("segment1/segment2/..").toString() == "http://localhost/segment1")
    assert(base.addPathSegments("segment1/segment2/../").toString() == "http://localhost/segment1/")
  }

  it should "handle double-slash segments" in {
    assert(base.addPathSegments("//segment1").toString() == "http://localhost/segment1")
    assert(base.addPathSegments("///segment1").toString() == "http://localhost/segment1")
    assert(base.addPathSegments("////segment1").toString() == "http://localhost/segment1")
    assert(base.addPathSegments("segment1//segment2").toString() == "http://localhost/segment1/segment2")
    assert(base.addPathSegments("segment1///segment2").toString() == "http://localhost/segment1/segment2")
    assert(base.addPathSegments("segment1////segment2").toString() == "http://localhost/segment1/segment2")
    assert(base.addPathSegments("segment1/////segment2").toString() == "http://localhost/segment1/segment2")
    assert(base.addPathSegments("segment1/segment2//").toString() == "http://localhost/segment1/segment2/")
    assert(base.addPathSegments("segment1/segment2///").toString() == "http://localhost/segment1/segment2/")
    assert(base.addPathSegments("segment1/segment2////").toString() == "http://localhost/segment1/segment2/")
  }

  it should "handle a single query parameter" in {
    assert(base.addQueryParameters("x" -> "1").toString() == "http://localhost/?x=1")
  }

  it should "handle multiple query parameters" in {
    assert(base.addQueryParameters("x" -> "1", "y" -> "2", "z" -> "3").toString() == "http://localhost/?x=1&y=2&z=3")
  }

  it should "handle base with query parameters" in {
    val newBase = URLBuilder("http://localhost/segment0?a=1&b=2&c=3")
    assert(newBase.addQueryParameters("d" -> "4").toString == "http://localhost/segment0?a=1&b=2&c=3&d=4")
    assert(newBase.addPathSegments("segment1").toString == "http://localhost/segment0/segment1?a=1&b=2&c=3")
    assert(newBase.addPathSegments("segment1/").toString == "http://localhost/segment0/segment1/?a=1&b=2&c=3")
    assert(newBase.addPathSegments("seg1/seg2").toString == "http://localhost/segment0/seg1/seg2?a=1&b=2&c=3")
  }

  it should "handle encoded segments" in {
    assert(base.addPathSegments("%7Bhello%7D").toString == "http://localhost/%7Bhello%7D")
    assert(base.addPathSegments("%7Bhello%7D/world/..").toString == "http://localhost/%7Bhello%7D")
  }

  it should "handle url-unsafe segments" in {
    assert(base.addPathSegments("{hello}/world").toString == "http://localhost/%7Bhello%7D/world")
  }

  it should "handle url-unsafe parameters" in {
    assert(base.addQueryParameters("x" -> "http://a/b c/c?x=1").toString ==
      "http://localhost/?x=http%3A%2F%2Fa%2Fb+c%2Fc%3Fx%3D1"
    )
  }
}