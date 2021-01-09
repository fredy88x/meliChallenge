# Mercado libre Mutant-Challenge
Technical Test 'MercadoLibre' - Mutant API REST to identify if a human is a mutant given the DNA and store and make available the statistics of verifications the exposed method (humans, mutants and ratio).

Where you will receive as a parameter an array of Strings that represent each row of a table of (NxN) with the DNA sequence. The letters of the Strings can only be: (A, T, C, G), which represents each nitrogenous base of DNA.

![Secuencia](/docs/explaindna.png)

You will know if a human is a mutant, if you find more than one four-letter sequence equal, obliquely, horizontally or vertically.

## Live Demo

This application is currently running on Heroku cloud.

Here is the [Mutant-Api](https://glacial-basin-45799.herokuapp.com/) running on Heroku.

```
https://glacial-basin-45799.herokuapp.com/
```

## Local Deploy

Redis instance required:

```bash
sudo apt-get install redis-server
redis-server /usr/local/etc/redis.conf
```


To run the default server on the 8080:

```bash
mvn clean install
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

To run with docker

```bash
docker build -t mutant-challenge-api .
docker run  -p 8080:8080 mutant-challenge-api
```

## Demo

From a test environment for HTTP requests

```
POST /mutant HTTP/1.1
Host: https://glacial-basin-45799.herokuapp.com
Content-Type: text/plain; charset=utf-8
Cache-Control: no-cache

{"dna":["ATGCGA","CAGTGT","TTATGT","AGAAGT","CCCCTT","TCACTG"]}
```

```
GET /stats HTTP/1.1
Host: https://glacial-basin-45799.herokuapp.com
Content-Type: text/plain; charset=utf-8
Cache-Control: no-cache
```


## Diagram of Sequence

![Secuencia](/docs/secuenciamutante.png)

## System Architecture

![Secuencia](/docs/despliegue.png)

