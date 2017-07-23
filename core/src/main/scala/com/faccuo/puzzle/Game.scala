package com.faccuo.puzzle

/**
  * A valid game (see smart constructor) able to play using weapons of type Weapon.
  * @tparam Weapon
  */
sealed trait Game[Weapon] {

  /**
    * A Map holding, for each weapon, the set of other weapons that this one can beat.
    */
  val winnerPlays: Map[Weapon, Set[Weapon]]

  /**
    * Evaulates a game so, given two weapons and its own validated rules, it gives a result.
    * @param w1 Player 1 weapon.
    * @param w2 Player 2 weapon.
    * @return Who wins the game.
    */
  def play(w1: Weapon, w2: Weapon): Either[GameError, Game.Result.Value] =
    (winnerPlays.get(w1), winnerPlays.get(w2)) match {
      case (None, _) => Left(InvalidWeaponInPlay(w1))
      case (_, None) => Left(InvalidWeaponInPlay(w2))
      case _ if w1 == w2 => Right(Game.Result.DRAW)
      case (Some(beatableByW1), _) if beatableByW1 contains w2 =>
        Right(Game.Result.PLAYER_1_WINS)
      case _ =>
        Right(Game.Result.PLAYER_2_WINS)

    }
}

/**
  * Companion object with smart constructors.
  */
object Game extends GameService {

  object Result extends Enumeration {
    val DRAW, PLAYER_1_WINS, PLAYER_2_WINS = Value
  }

  /**
    * Creates a Game given explicit rules.
    * @param rules List(Scissors -> Paper, Paper -> Rock, Rock -> Scissors), so
    * Scissors beat Paper, Paper beats Rock and Rock beats Scissors.
    * @tparam Weapon Could be any type used in the list.
    * @return A valid Game.
    */
  def apply[Weapon](rules: List[(Weapon, Weapon)]): Either[GameError, Game[Weapon]] = {
    validate(rules).right.map[Game[Weapon]](rules => new Game[Weapon] {
      override lazy val winnerPlays: Map[Weapon, Set[Weapon]] = rules.groupBy(_._1).mapValues(l => l.map(_._2).toSet)
    })
  }

  /**
    * Creates a Game given a list of weapons following modular aritmethic rules in terms of parity.
    * https://en.wikipedia.org/wiki/Rock%E2%80%93paper%E2%80%93scissors#Additional_weapons
    * @param modular List of weapons. E.g.: "Rock", "Paper", "Scissors", "Spock", "Lizard"
    * @tparam Weapon Could be any type used in the List.
    * @return A valid game.
    */
  def apply[Weapon](modular: Weapon*): Either[GameError, Game[Weapon]] =
    translateModularToRules(modular.toList).right.flatMap(rules => Game(rules))
}
