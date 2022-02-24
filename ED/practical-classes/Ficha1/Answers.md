# Ficha Laboratorial 1 - Complexidade

## 1.

### a)

- `soma++` é executada $n*n$ vezes
- completixadade $O(n^2)$
- se $n$ for aumentado para $4n$, , `soma++` vai ser executado $4n*4n$ vezes 

Tempo de execução aumenta 16 vezes.

### b)

- linear
- `soma++` é executada $n$ vezes
- complexidade $O(n)$
- se $n$ for aumentado para $4n$, `soma++` vai ser executado $4n$ vezes

Tempo de execução aumenta 4 vezes.

### c)

- linear
- `soma++` é executada $n/2$ vezes
- complexidade $O(n/2)$
- se $n$ for aumentado para $4n$, `soma++` vai ser executado $2n$ vezes

Tempo de execução aumenta 4 vezes.

### d)

- linear
- `soma++` é executada $1000*n$ vezes
- complexidade $O(1000*n)$
- se $n$ for aumentado para $4n$, `soma++` vai ser executado $1000*4n$ vezes

Tempo de execução aumenta 4 vezes.

### e)

- linear
- `soma++` é executada $2n$ vezes
- complexidade $O(2n)$
- se $n$ for aumentado para $4n$, `soma++` vai ser executado $8n$ vezes

Tempo de execução aumenta 4 vezes.

### f)

- constante
- `soma++` é executado no máximo 20000*20000 vezes
- complexidade $O(1)$
- se $n$ for aumentado para $4n$, `soma++` continuará a ser executado no máximo $20000*20000$

Tempo de execução não alterado.

### g)

- cúbica
- `soma++` é executada $n*(n*n)$ vezes
- complexidade $O(n*n*n)$
- se $n$ for aumentado para $4n$, `soma++` vai ser executado $4n*4n*4n$ vezes

Tempo de execução aumenta 64 vezes.

### h)

- quadrática
- `soma++` é executado $(1/2)n*(n+1)$
- complexidade $O(n^2)$
- se $n$ for aumentado para $4n$, `soma++` vai ser executado $(4n)^2$ vezes

Tempo de execução aumenta 16 vezes.

### i)

- cúbica
- `soma++` é executado $n*(n+1)*(2n+1)/6$ vezes
- complexidade $O(n^3)$
- se $n$ for aumentado para $4n$, `soma++`, vai ser executado $(4n)^3$ vezes

Tempo de execução aumenta 64 vezes.

## j)

- logarítmico
- `soma++` é executado $\log_2(n)$ vezes
- complexidade $O(\log_2(n))$
- se $n$ for aumentado para $4n$, `soma++` vai ser executado $\log_2(4n)$ vezes

Tempo de execução aumenta 2 vezes.