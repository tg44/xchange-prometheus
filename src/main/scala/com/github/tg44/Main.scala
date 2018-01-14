package com.github.tg44

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.github.tg44.api.prometheus.MetricsApi
import com.github.tg44.service.XChangeMetricsActor
import io.prometheus.client.CollectorRegistry
import concurrent.duration._
import scala.concurrent.Future

object Main extends App{
  implicit val system = ActorSystem("prometheusexporter")
  implicit val dispatcher = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val registry = CollectorRegistry.defaultRegistry

  val config = Config
  val krakenMetrics = system.actorOf(XChangeMetricsActor.props(registry,XChangeMetricsActor.createKrakenFromConf(config)))
  val metrics = new MetricsApi(registry)

  system.scheduler.schedule(15.seconds, 15.seconds, krakenMetrics, "")

  val routes = {
    pathPrefix("metrics") {
      metrics.route
    } ~ {
      complete("root")
    }
  }

  val adminApiBindingFuture: Future[ServerBinding] = Http()
    .bindAndHandle(routes, config.SERVER.url, config.SERVER.port)
    .map(binding => {
      println(s"Server started on ${config.SERVER.url}:${config.SERVER.port}")
      binding
    })

}
