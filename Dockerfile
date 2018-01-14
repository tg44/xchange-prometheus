FROM hseeberger/scala-sbt:8u141_2.12.4_1.0.2

ADD build.sbt /xchange-prometheus/build.sbt
ADD project /xchange-prometheus/project
ADD src /xchange-prometheus/src

WORKDIR /xchange-prometheus

RUN sbt clean compile

CMD sbt run
