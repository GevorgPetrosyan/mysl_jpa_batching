# Spring Boot application to test  JPA Batching.

## Usage
1. Run docker-compose up in root directory
2. Run scripts/enable_transaction_logging.sql as root user on DB to enable transaction logging.

## Dockerized database credentials

- url=jdbc:mysql://localhost:3306 
- username=root
- password=root



## MySQL Batching Benchmark

| Rows count  | Single Batch | Separate Batches | JPA Identity| JPA Table Identity| JPA Native Identity|
| ------------- | ------------ | ------------- | ------------- | ------------- | ------------- |
| 1000  | 54ms  | 11651ms  | 9891ms | 12391ms | 3253ms
| 5000  | 98ms  | 56s 722ms  | 59s 246ms | 1m 5s 188ms | 15s 618ms
| 10000  | 110ms  | 1m 58s 117ms  | 1m 48s 168ms | 2m 32s 680ms | 32s 679ms

P.S. benchmark has done on 13" macbook pro(early 2015) with Core i7 processor and 16GB RAM.