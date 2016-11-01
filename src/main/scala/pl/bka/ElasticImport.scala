package pl.bka

import com.sksamuel.elastic4s.{ElasticClient, ElasticsearchClientUri, IndexResult}
import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.source.Indexable
import spray.json._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object ElasticImport extends JsonProtocol {

  implicit object WorkdayIndexable extends Indexable[Workday] {
    override def json(t: Workday): String = t.toJson.toString
  }

  def importLogData(logData: List[Workday]): Future[List[IndexResult]] = {
    val uri = ElasticsearchClientUri("elasticsearch://localhost:9300")
    val client = ElasticClient.transport(uri)
    client.execute { create index "log"  }

    Future.traverse(logData) { workday =>
      client.execute {
        index into "log" / "workdays" source workday
      }
    }
  }
}

