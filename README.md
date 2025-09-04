# muraveri

### db via docker

rode o comando:

```
docker pull mariadb
```

e depois:

```
docker run -d --name db -e MYSQL_ROOT_PASSWORD=ifsp -v mariadb_data:/var/lib/mysql -p 3307:3306 mariadb
```

### acessar o db dentro do container

Execute este comando para entrar no container EM EXECUÇÃO:

```
docker exec -it db mariadb -u root -p
```

A senha é: `ifsp`

Para criar o database no banco de dados execute o comando:

```
CREATE DATABASE app
```

Para sair do banco de dados:

```
quit
```
