package com.faccuo.puzzle

import com.faccuo.puzzle.Game.Result
import com.faccuo.puzzle.sample.{Complex, GameDefinition, Simple}
import org.beryx.textio.TextIoFactory

import scala.collection.JavaConverters._
import scala.util.Random

object Main extends App {

  val r = new Random
  val textIO = TextIoFactory.getTextIO()
  val terminal = textIO.getTextTerminal
  val props = terminal.getProperties

  do {
    val definition: GameDefinition = textIO.newStringInputReader()
      .withNumberedPossibleValues(
        "rock-paper-scissors",
        "rock-paper-scissors-lizard-Spock"
      )
      .withDefaultValue("rock-paper-scissors")
      .read("Choose a game") match {
      case "rock-paper-scissors" =>
        Simple
      case _ =>
        Complex
    }

    val (player1, player2) = textIO.newStringInputReader()
      .withNumberedPossibleValues("Player vs Computer", "Computer vs Computer")
      .withDefaultValue("Player vs Computer")
      .read("Game options") match {

      case "Player vs Computer" =>
        val player = textIO.newStringInputReader()
          .withNumberedPossibleValues(definition.weapons.toSeq.map(_.toString).asJava)
          .withDefaultValue(getRandomWeapon(definition))
          .read("Choose a weapon")

        suspense()

        val computer = getRandomWeapon(definition)
        terminal.println(s"PLAYER 1: $player vs PLAYER 2 (Computer): $computer")

        (player, computer)
      case _ =>
        suspense()

        val computer1 = getRandomWeapon(definition)
        val computer2 = getRandomWeapon(definition)
        terminal.println(s"PLAYER 1 (Computer): $computer1 vs PLAYER 2 (Computer): $computer2")

        (computer1, computer2)
    }

    play(definition.game, player1, player2)


  } while (textIO.newBooleanInputReader()
    .withDefaultValue(true)
    .read("Play again?"))

  private def play(game: Game[String], player: String, computer: String) = {
    terminal.println
    game.play(player, computer) match {
      case Right(Result.DRAW) =>
        terminal.println("DRAW!")
      case Right(Result.PLAYER_1_WINS) =>
        terminal.println("PLAYER 1 WINS!")
      case Right(Result.PLAYER_2_WINS) =>
        terminal.println("PLAYER 2 WINS!")
      case _ =>
        terminal.println("An error happened. Please, try again.") // Will not happen but, if so, show error message.
    }
    terminal.println
  }

  private def suspense() = {
    terminal.println(".")
    (1 to 2).foreach { _ =>
      Thread.currentThread().join(500)
      terminal.println(".")
    }
  }

  private def getRandomWeapon(definition: GameDefinition): String = {
    definition.weapons(r.nextInt(definition.weapons.size))
  }
}