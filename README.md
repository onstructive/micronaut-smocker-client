# Micronaut Http Client for Smocker

## About Smocker

Please find all information and documentation at https://smocker.dev/

## Run Smocker

```shell
docker run -d --rm -p 8080:8080 -p 8081:8081 thiht/smocker:0.18.5
```

The Smocker Docker image is only availabe for the `amd64` architecture. If you are running on Apple Silicon, you need to install Docker 2 and then start the Docker container like this.

```shell
docker run -d --rm --platform=linux/amd64 -p 8080:8080 -p 8081:8081 thiht/smocker:0.18.5
```

