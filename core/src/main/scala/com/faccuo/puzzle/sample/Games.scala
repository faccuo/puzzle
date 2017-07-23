package com.faccuo.puzzle.sample

import com.faccuo.puzzle.Game

trait GameDefinition {
  val weapons: List[String]
  val game: Game[String]
}

object Simple extends GameDefinition {
  val weapons: List[String] = List("Rock", "Paper", "Scissors")

  val game: Game[String] = Game(List(
    "Rock" -> "Scissors",
    "Paper" -> "Rock",
    "Scissors" -> "Paper"
  )).right.get
}

object Complex extends GameDefinition {
  val weapons: List[String] = List("Rock", "Paper", "Scissors", "Spock", "Lizard")

  val game: Game[String] = Game(weapons: _*).right.get
}
