import com.github.nscala_time.time.Imports._

import scala.util.parsing.combinator.RegexParsers

object Miner extends App {
  val input: String = io.Source.fromFile("input.txt").mkString
  val res: List[Workday] = LogParser(input)
  println(res.length)
  res.foreach {
    case Workday(date, projects) =>
      println(DateTimeFormat.forPattern("dd.MM.yyyy").print(date) + " " +
        projects.map({ case Project(name, _) => name }).reduce(_ + ", " + _) )
  }
}