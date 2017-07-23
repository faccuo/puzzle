package com.faccuo.puzzle

import com.faccuo.puzzle.sample.Simple
import org.scalatest.{FlatSpec, Matchers}

class GameTest extends FlatSpec with Matchers {

  behavior of "Game()"

  it should "create return an error when invalid rules are provided" in {
    Game() shouldBe Left(MissingRulesError)
    Game("A", "B") shouldBe Left(EvenNumberOfWeaponsProvided)
    Game("A", "B", "A") shouldBe Left(RepeatedWeaponsInModularDefinitionError)
  }

  it should "Create a Game when valid modular rules are provided" in {
    Game("A", "B", "C") match {
      case Right(game) =>
        game.winnerPlays should contain only(
          "A" -> Set("C"),
          "B" -> Set("A"),
          "C" -> Set("B")
        )
      case _ => fail
    }
  }

  behavior of "Game.play(w1, w2)"

  it should "return InvalidWeaponInPlay when an invalid weapon is used in a game" in {
    Game("A", "B", "C") match {
      case Right(game) => game.play("D", "A") shouldBe Left(InvalidWeaponInPlay("D"))
      case _ => fail
    }
  }

  it should "return proper results for a valid game" in {
    Simple.game.play("Paper", "Paper") shouldBe Right(Game.Result.DRAW)
    Simple.game.play("Rock", "Rock") shouldBe Right(Game.Result.DRAW)
    Simple.game.play("Scissors", "Scissors") shouldBe Right(Game.Result.DRAW)

    Simple.game.play("Paper", "Rock") shouldBe Right(Game.Result.PLAYER_1_WINS)
    Simple.game.play("Rock", "Paper") shouldBe Right(Game.Result.PLAYER_2_WINS)

    Simple.game.play("Scissors", "Paper") shouldBe Right(Game.Result.PLAYER_1_WINS)
    Simple.game.play("Paper", "Scissors") shouldBe Right(Game.Result.PLAYER_2_WINS)

    Simple.game.play("Rock", "Scissors") shouldBe Right(Game.Result.PLAYER_1_WINS)
    Simple.game.play("Scissors", "Rock") shouldBe Right(Game.Result.PLAYER_2_WINS)

  }

}