FROM gradle:jre14

RUN apt-get update
RUN apt-get install -y postgresql-client

COPY . .
COPY createDB /app/createDB
RUN chmod +x /app/createDB

ARG AWS_ACCESS_KEY_ID
ARG AWS_SECRET_ACCESS_KEY
ARG pg_host
ARG pg_port
ARG ig_database
ARG ig_user
ARG ig_passwd
ARG ig_passwd_file
ARG s3_image_bucket

ENV AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID}
ENV AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY}
ENV PG_HOST=${pg_host}
ENV PG_PORT=${pg_port}
ENV IG_DATABASE=${ig_database}
ENV IG_USER=${ig_user}
ENV IG_PASSWD=${ig_passwd}
ENV IG_PASSWD_FILE=${ig_passwd_file}
ENV S3_IMAGE_BUCKET=${s3_image_bucket}

RUN echo "*:*:*:*:$IG_PASSWD" > $IG_PASSWD_FILE

CMD ["gradle", "run"]

