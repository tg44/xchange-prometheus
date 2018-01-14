package com.github.tg44

import com.typesafe.config.ConfigFactory

import scala.util.Try

trait Config {

  private val config = ConfigFactory.load()

  object SERVER {
    lazy val url = Try(config.getString("server.url")).getOrElse("localhost")
    lazy val port = Try(config.getInt("server.port")).getOrElse(9000)
  }

  object EXCHANGE {
    object kraken {
      lazy val apiKey = Try(config.getString("exchange.kraken.apiKey")).getOrElse("key")
      lazy val apiSecret = Try(config.getString("exchange.kraken.apiSecret")).getOrElse("secret")
    }
  }
}

object Config extends Config