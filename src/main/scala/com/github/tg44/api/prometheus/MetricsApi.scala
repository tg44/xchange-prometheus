package com.github.tg44.api.prometheus

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import io.prometheus.client.CollectorRegistry

class MetricsApi(registry: CollectorRegistry) {

  val route = {
      complete(MetricFamilySamplesEntity.fromRegistry(registry))
  }
}
