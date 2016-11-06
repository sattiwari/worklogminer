package pl.bka

import pl.bka.model.db.ProjectDay
import pl.bka.model.parse.Workday

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object Miner extends App {
  def parse(fileName: Option[String]): List[Workday] = {
    val input: String = io.Source.fromFile(fileName.getOrElse("input.txt")).mkString
    val res: List[Workday] = LogParser(input)
    println(res.length)
    res.foreach { workday: Workday => println(workday) }
    res
  }

  def importData(elasticDao: ElasticDao, logData: List[Workday]) = {
    val dbData: Seq[ProjectDay] = logData.flatMap(_.toProjectDays)
    Await.result(elasticDao.importData(dbData), Duration.Inf)
  }

  args(0) match {
    case "parse" =>
      parse(None)
    case "import" =>
      val fileName = if(args.length >= 2) Some(args(1)) else None
      val elasticDao = new ElasticDao
      importData(elasticDao, parse(fileName))
    case "clear" =>
      val elasticDao = new ElasticDao
      elasticDao.clearLogData()
  }
}