FROM gradle:jre14

#RUN apt-get update && apt-get install -y nginx

COPY . .
#COPY src/main/resources/public/build /var/www/html

ARG AWS_ACCESS_KEY_ID
ARG AWS_SECRET_ACCESS_KEY

ENV AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID}
ENV AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY}

#ENTRYPOINT sh -c 'service nginx start && gradle run'

