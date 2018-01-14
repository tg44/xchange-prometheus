package com.github.tg44.service

import akka.actor.{Actor, ActorLogging, Props}
import com.github.tg44.Config
import io.prometheus.client.{CollectorRegistry, Gauge}
import org.knowm.xchange.kraken.service.KrakenAccountServiceRaw
import org.knowm.xchange.Exchange
import org.knowm.xchange.ExchangeFactory
import org.knowm.xchange.kraken.KrakenExchange
import org.knowm.xchange.currency.{Currency, CurrencyPair}

import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}

class XChangeMetricsActor(registry: CollectorRegistry, exchange: => Exchange, baseCurrency: Currency) extends Actor with ActorLogging {

  val gauge = Gauge.build()
    .name("balance_eur")
    .help("Your current account balance in euro.")
    .labelNames("currency")
    .register(registry)

  def doQuerying = {
    val marketDataService = exchange.getMarketDataService
    val accountInfo = exchange.getAccountService.getAccountInfo

    val wallet = accountInfo.getWallet

    val modded = wallet.getBalances.asScala.map{
      case (key, value) =>
        val pair = new CurrencyPair(key, baseCurrency)
        if(pair.base != pair.counter) {
          val ticker = marketDataService.getTicker(pair)
          val eur = ticker.getLast multiply value.getTotal
          gauge.labels(key.getDisplayName).set(eur.doubleValue)
          (key, (eur, value.getTotal))
        } else {
          gauge.labels(key.getDisplayName).set(value.getTotal.doubleValue)
          (key, (value.getTotal, value.getTotal))
        }
    }
  }

  override def receive = {
    case _ =>
      Try(doQuerying) match {
        case Success(_) => log.debug(s"Successfull update")
        case Failure(ex) => log.warning("Error at querying", ex)
      }
  }
}

object XChangeMetricsActor {

  def props(registry: CollectorRegistry, exchange: => Exchange, baseCurrency: Currency = Currency.EUR) = Props(new XChangeMetricsActor(registry, exchange, baseCurrency))

  def createKrakenFromConf(conf: Config): Exchange = {
    val krakenExchange = ExchangeFactory.INSTANCE.createExchange(classOf[KrakenExchange].getName)
    krakenExchange.getExchangeSpecification.setApiKey(conf.EXCHANGE.kraken.apiKey)
    krakenExchange.getExchangeSpecification.setSecretKey(conf.EXCHANGE.kraken.apiSecret)
    krakenExchange.getExchangeSpecification.setUserName("xxx")
    krakenExchange.applySpecification(krakenExchange.getExchangeSpecification)
    krakenExchange
  }
}


