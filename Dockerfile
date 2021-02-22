## designtalk batch images ###

FROM ktis-bastion01.container.ipc.kt.com:5000/designtalk/rhel75-jdk8-wildfly14:latest

MAINTAINER designtalk <91222332@ktfriend.com>

LABEL VERSION="1.0.0"
LABEL SERVER_MODE="DEV"

# docker build option set (docker build --build-arg env=dev
ARG env
ARG log_level=debug


# Encoding
ENV LC_ALL=en_US.utf-8

# Timezone
ENV TZ=Asia/Seoul

RUN mkdir -p /jboss/logs
RUN chmod 770 /jboss/logs
#JAVA OPTION SET
RUN sed -i 's/-Xms[0-9]*m/-Xms1024m/gi;s/-Xmx[0-9]*m/-Xmx4g/gi;s/-XX:MetaspaceSize=[0-9]*m/-XX:MetaspaceSize=1024m/gi;s/JAVA_OPTS=\"$JAVA_OPTS -XX:MaxMetaspaceSize=[0-9]*m\"//gi' /jboss/domains/instance11/env.sh

COPY ./target/war/dt-batch.war /jboss/domains/instance11/deployments

USER jboss
