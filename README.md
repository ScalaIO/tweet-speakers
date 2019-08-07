# scalaio-speakers

Script to publish to twitter speaker annoucements for ScalaIO

## Dependencies
 
- [twitter4s](https://github.com/DanielaSfregola/twitter4s)
- [Circe](https://circe.github.io/circe/)
- [Sttp](https://github.com/softwaremill/sttp) 
- [ZIO](https://zio.dev) 


## Usage
Add your consumer and access token as environment variables:

```bash
export TWITTER_CONSUMER_TOKEN_KEY='my-consumer-key'
export TWITTER_CONSUMER_TOKEN_SECRET='my-consumer-secret'
export TWITTER_ACCESS_TOKEN_KEY='my-access-key'
export TWITTER_ACCESS_TOKEN_SECRET='my-access-secret'
export PAPERCALL_TOKEN='my-papercall-token'
```

Run the examples with ```sbt run``` and choose the main to run.


