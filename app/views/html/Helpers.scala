package views.html

import play.api.templates.{HtmlFormat, Html}
import play.api.Play
import scalax.file.Path
import play.api.mvc.Call

object includeCode {
  def apply(filename: String, segment: String): Html = apply(filename, Option(segment))

  def apply(filename: String, segment: Option[String] = None): Html =  {
    val extension = filename.split("\\.").last
    val file = Path(Play.current.getFile(filename))

    val content = segment match {
      case Some(s) =>
        val sep = "#" + s
        val sepLine :: block = file.lines().toList.dropWhile(!_.contains(sep))
        val spaces = sepLine.takeWhile(_ == " ")
        block.takeWhile(!_.contains(sep)).map(_.replaceFirst(spaces, "")).mkString("\n")
      case None => file.string
    }

    Html(s"""<pre class="prettyprint"><code class="language-$extension">${HtmlFormat.escape(content)}</code></pre>""")
  }
}

object points {
  def apply(ps: Html) = Seq(Html("""<ul class="points">"""), ps, Html("</ul>"))
}

object point {
  def apply(p: Html) =
    Seq(Html("<li class='point'>"), p, Html("</li>"))
  def show(p: Html) = {
    Seq(Html("<li>"), p, Html("</li>"))
  }

  def apply(p: String) =
    Html("<li class='point'>" + HtmlFormat.escape(p) + "</li>")
  def show(p: String) =
    Html("<li>" + HtmlFormat.escape(p) + "</li>")

}

object item {
  def apply(p: Html) =
    Seq(Html("<span class='point'>"), p, Html("</span>"))
}

object image {
  def apply(width: Int, url: Call) = {
    Html("<img style='width:" + width + "px;' src='" + url.url + "'>")
  }
}

object imagePoint {
  def apply(width: Int, url: Call) = {
    Seq(Html("<div class='point' style='width: " + width + "px; margin: 0 auto;'>"), image(width, url), Html("</div>"))
  }
}

object imageCentered {
  def apply(width: Int, url: Call) = {
    Seq(Html("<div style='width: " + width + "px; margin: 0 auto;'>"), image(width, url), Html("</div>"))
  }
}
