# Microserviço keycloak-auth-service

Este projeto utiliza o Quarkus, o framework Java supersônico e subatômico.

Para saber mais sobre o Quarkus, visite o site: <https://quarkus.io/>.

## O projeto utiliza a versão Java 25
Para instalar via sdk, use o comando:
```shell script
sdk install java  25.0.3-graal
```

Para confirmar a instalação:
```shell script
java --version
```

## Executando o aplicativo em modo de desenvolvimento

Você pode executar seu aplicativo no modo de desenvolvimento, que permite a codificação em tempo real, usando:

```shell script
./mvnw quarkus:dev
```

> **_NOTA:_** O Quarkus vem com uma Dev UI, que está disponível apenas no modo de desenvolvimento em <http://localhost:8080/q/dev/>.

## Empacotamento e execução da aplicação

A aplicação pode ser empacotada usando:

```shell script
./mvnw package
```

Ela produz o arquivo `quarkus-run.jar` no diretório `target/quarkus-app/`.
Esteja ciente de que não é um _über-jar_, pois as dependências são copiadas para o diretório `target/quarkus-app/lib/`.

A aplicação agora pode ser executada usando `java -jar target/quarkus-app/quarkus-run.jar`.

Se você quiser construir um _über-jar_, execute o seguinte comando:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

A aplicação, empacotada como um _über-jar_, agora pode ser executada usando `java -jar target/*-runner.jar`.

## Criando um executável nativo

Você pode criar um executável nativo usando:

```shell script
./mvnw package -Dnative
```

Ou, se você não tiver o GraalVM instalado, você pode executar a construção do executável nativo em um contêiner usando:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

Você pode então executar seu executável nativo com: `./target/keycloak-auth-service-1.0.0-SNAPSHOT-runner`

Se você quiser aprender mais sobre a construção de executáveis nativos, consulte <https://quarkus.io/guides/maven-tooling>.

## Guias Relacionados

- REST ([guide](https://quarkus.io/guides/rest)): Uma implementação Jakarta REST utilizando processamento em tempo de construção e Vert.x. Esta extensão não é compatível com a extensão quarkus-resteasy, ou qualquer uma das extensões que dependem dela.
- REST Jackson ([guide](https://quarkus.io/guides/rest#json-serialisation)): Suporte à serialização Jackson para Quarkus REST. Esta extensão não é compatível com a extensão quarkus-resteasy, ou qualquer uma das extensões que dependem dela.
- Smallrye Health ([guide](https://quarkus.io/guides/smallrye-health)): Permite que os aplicativos forneçam informações sobre seu estado para visualizadores externos, o que geralmente é útil em ambientes de nuvem, onde os processos automatizados precisam determinar se o aplicativo deve ser descartado ou reiniciado.

