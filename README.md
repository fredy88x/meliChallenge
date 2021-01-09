# Mercado libre Mutant-Challenge
Technical Test 'MercadoLibre' - Mutant API REST to identify if a human is a mutant given the DNA and store and make available the statistics of verifications the exposed method (humans, mutants and ratio).

Where you will receive as a parameter an array of Strings that represent each row of a table of (NxN) with the DNA sequence. The letters of the Strings can only be: (A, T, C, G), which represents each nitrogenous base of DNA.

![Secuencia](/docs/explaindna.png)

You will know if a human is a mutant, if you find more than one four-letter sequence equal, obliquely, horizontally or vertically.

## Live Demo

This application is currently running on Heroku cloud.

Here is the [Mutant-Api](https://glacial-basin-45799.herokuapp.com/) running on Heroku.

# Instrucciones de ejecucion

Se requiere una instancia de Redis:

```bash
sudo apt-get install redis-server
redis-server
```

Instalar los requerimientos:

```bash
pip3 install -r requirements.txt
```

Para correr el servidor por defecto en el 8081:

```bash
python3 server_api.py
```
para otro puerto:

```bash
export REDIS=[redis_server]
export REDISPWD=[redis_password]
export PORT=[puerto]
python3 server_api.py
```

## Demo

Desde un entorno de pruebas para requests HTTP 

```
POST /mutant HTTP/1.1
Host: https://possessed-spirit-56339.herokuapp.com
Content-Type: text/plain; charset=utf-8
Cache-Control: no-cache

{"dna":["ATGCGA","CAGTGT","TTATGT","AGAAGT","CCCCTT","TCACTG"]}
```

```
GET /stats HTTP/1.1
Host: https://possessed-spirit-56339.herokuapp.com
Content-Type: text/plain; charset=utf-8
Cache-Control: no-cache
```

## Testing

>python3 test.py

## Diagrama de Secuencia



## Diagrama de Arquitectura del Sistema

![Secuencia](/docs/despliegue.png)

