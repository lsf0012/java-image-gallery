FROM gradle:jre14

RUN apt-get update
RUN apt-get install -y postgresql-client

COPY . .
COPY createDB /app/createDB
RUN chmod +x /app/createDB

ARG AWS_ACCESS_KEY_ID
ARG AWS_SECRET_ACCESS_KEY

ENV AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID}
ENV AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY}

CMD ["gradle", "run"]

