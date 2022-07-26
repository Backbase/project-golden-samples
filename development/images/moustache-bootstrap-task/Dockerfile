FROM eclipse-temurin:11
RUN mkdir /opt/app
COPY target /opt/app
COPY run.sh /opt/app
WORKDIR /opt/app
VOLUME /tmp
CMD ["/opt/app/run.sh"]
