# Rock–paper–scissors
In this repository, you'll find an implementation of the popular game [Rock–paper–scissors](https://en.wikipedia.org/wiki/Rock%E2%80%93paper%E2%80%93scissors)
## How to use it
Clone repository and, from the root checkout directory, do as follows.
### Executing tests
```sh
sbt clean test
```
### Code coverage
This sample is using [sbt plugin for scoverage](https://github.com/scoverage/sbt-scoverage). To check the report do as follows:
 ```sh
sbt clean coverage test coverageReport && open ./core/target/scala-2.11/scoverage-report/index.html
```
### Code style
This sample is using [Scalastyle](http://www.scalastyle.org/) as code style checker. It is using, almost, a default style configuration. To pass the validation do as follows:
```sh
sbt scalastyle
```
### Executing the game
The project is divided into two modules:
* **core**: contains the "business" logic of the game. It allows you to create valid games and play with them. It is fully tested and provides an interface to be used from other modules.
* **console**: a simple interactive console application allowing users to play two different games. It's only purpose is to demonstrate how to use the core module, so it does not contain any tests. It is develop using [text-io](https://github.com/beryx/text-io) and allows you to play two popular implementations: **rock-paper-scissors** and **rock-paper-scissors-Spock-lizard**.

To launch the game (and finally play!) you should do as follows:
```sh
sbt console/run
```
## Pending tasks
 - Create module providing a REST Api so users can create valid games and play with them.
 - Refactor console module and add tests.
 - ...
## Author

[faccuo](https://github.com/faccuo)