# :warning: Repository Archived :warning:

---

## Attention Contributors and Users

This repository is now **archived** and **no longer actively maintained**


# scalaio-speakers

Script to publish to twitter speaker annoucements for ScalaIO

## Dependencies
 
- [twitter4s](https://github.com/DanielaSfregola/twitter4s)
- [Circe](https://circe.github.io/circe/)
- [Sttp](https://github.com/softwaremill/sttp) 
- [ZIO](https://zio.dev) 
- [Lightbend Config](https://github.com/lightbend/config)

## Prerequisites

 * JDK 8
 * Scala 2.11+

### Twitter

- Go to http://apps.twitter.com/, login with your twitter account and register your application to get a consumer key and a consumer secret.
- Once the app has been created, generate a access key and access secret with the desired permission level.

### Papercall.io

- Go to the [ScalaIO event page](https://www.papercall.io/events/2207/apidocs).
- Locate your personal token in the `Authentication` block.

## Usage
Add your consumer and access token as environment variables:

```bash
export TWITTER_CONSUMER_TOKEN_KEY='my-consumer-key'
export TWITTER_CONSUMER_TOKEN_SECRET='my-consumer-secret'
export TWITTER_ACCESS_TOKEN_KEY='my-access-key'
export TWITTER_ACCESS_TOKEN_SECRET='my-access-secret'
export PAPERCALL_TOKEN='my-papercall-token'
export OUTPUT_DIR='/output/dir/for/images'
export OUTPUT_PHOTOS_DIR='/output/dir/for/photos'
export PHOTOS_DIR='/input/dir/for/speaker/photos'
export SUBMISSIONS_FILE='/output/file/for/submissions.json'
```

You can also add them to your configuration file, usually called `application.conf`:
```scala
twitter {
  consumer {
    key = "my-consumer-key"
    secret = "my-consumer-secret"
  }
  access {
    key = "my-access-key"
    secret = "my-access-secret"
  }
}
papercall {
  token = "my-personal-token"
}
files {
  outputImagesDir = "/output/dir/for/images"
  outputPhotosDir = "/output/dir/for/photos"
  speakerPhotosDir = "/input/dir/for/speaker/photos"
  outputSubmissions = "/output/file/for/submissions.json"
}
```

Run the examples with ```sbt run``` and choose the main to run.


