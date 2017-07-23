package com.faccuo.puzzle

import scala.annotation.tailrec

/**
  * Service with functions needed for Game validation and creation.
  */
trait GameService {

  /**
    * Validates provided rules.
    * @param rules Rules defining a relationship between weapons.
    *              E.g.: List(Scissors -> Paper, Paper -> Rock, Rock -> Scissors), so Scissors beat
    *              Paper, Paper beats Rock and Rock beats Scissors.
    * @tparam Weapon Could be any type.
    * @return Either a GameError describing validation problem or provided list of rules.
    */
  def validate[Weapon](rules: List[(Weapon, Weapon)]): Either[GameError, List[(Weapon, Weapon)]] = {
    // Map holding a Set of weapons beatable by the weapon key
    val winnerPlays: Map[Weapon, Set[Weapon]] = rules.groupBy(_._1).mapValues(l => l.map(_._2).toSet)

    // Map holding a Set of weapons winners against the weapon key
    val losingPlays: Map[Weapon, Set[Weapon]] = rules.groupBy(_._2).mapValues(l => l.map(_._1).toSet)

    val distinctWeaponsInRules = rules.flatMap(t => List(t._1, t._2)).distinct

    val numberOfWeapons = distinctWeaponsInRules.size

    distinctWeaponsInRules match {
      case _ if rules.isEmpty => Left(MissingRulesError)
      case _ if isEven(numberOfWeapons) => Left(EvenNumberOfWeaponsProvided)
      case _ =>
        distinctWeaponsInRules.filterNot { weapon =>
          val weaponBeatsAgainst = winnerPlays.get(weapon)
          val weaponLosesAgainst = losingPlays.get(weapon)

          validWeaponRulesDefinition(numberOfWeapons, weaponBeatsAgainst, weaponLosesAgainst)
        } match {
          case invalid if invalid.isEmpty => Right(rules)
          case invalid =>
            Left(InvalidRulesForWeaponsError(invalid.toList))
        }
    }
  }

  /**
    * Asserts that provided weapon satisfies rules.
    * "As long as the number of moves is an odd number and each move defeats exactly half of the other moves while
    * being defeated by the other half, any combination of moves will function as a game."
    * https://en.wikipedia.org/wiki/Rock%E2%80%93paper%E2%80%93scissors#Additional_weapons
    * @param numberOfWeapons Total number of weapons to be used in validations.
    * @param weaponBeatsAgainst A set containing those other weapons that could be beaten by provided weapon.
    * @param weaponLosesAgainst A set containing thos other weapons that could beat provided weapon.
    * @tparam Weapon Weapon to validate.
    * @return A Boolean noting if weapon is valid given provided info.
    */
  private def validWeaponRulesDefinition[Weapon](numberOfWeapons: Int, weaponBeatsAgainst: Option[Set[Weapon]], weaponLosesAgainst: Option[Set[Weapon]]) = {
    (weaponBeatsAgainst, weaponLosesAgainst) match {
      case (Some(beats), Some(loses)) =>
        val defeatsHalfOfTheOther = beats.size == loses.size
        val coversRestOfWeapons = (beats.size + loses.size) == numberOfWeapons - 1
        val rulesDoNotIntersect = (beats intersect loses) == Set.empty[Weapon]

        defeatsHalfOfTheOther && rulesDoNotIntersect && coversRestOfWeapons
      case _ =>
        false
    }
  }

  /**
    * A way to define a game using modular arithmetic.
    * "... the rankings in rock-paper-scissors-Spock-lizard may be modeled by a comparison of the parity
    * of the two choices. If it is the same (two odd-numbered moves or two even-numbered ones) then the
    * lower number wins, while if they are different (one odd and one even) the higher wins."
    * https://en.wikipedia.org/wiki/Rock%E2%80%93paper%E2%80%93scissors#Additional_weapons
    * @param modular
    * @tparam Weapon
    * @return
    */
  def translateModularToRules[Weapon](modular: List[Weapon]): Either[GameError, List[(Weapon, Weapon)]] = {
    @tailrec
    def calcAcc(acc: List[(Weapon, Weapon)],
                sameParity: List[Weapon],
                differentParity: List[Weapon],
                modular: List[Weapon]): List[(Weapon, Weapon)] = {
      modular match {
        case Nil =>
          List.empty // Will not happen but, if so, we return a defective invalid set of rules
        case head :: Nil =>
          sameParity.map(w => w -> head) ++ differentParity.map(w => head -> w) ++ acc
        case head :: tail =>
          calcAcc(sameParity.map(w => w -> head) ++ differentParity.map(w => head -> w) ++ acc, differentParity, head :: sameParity, tail)
      }
    }

    modular match {
      case Nil => Left(MissingRulesError)
      case repeated if repeated.distinct.size != repeated.size => Left(RepeatedWeaponsInModularDefinitionError)
      case even if isEven(modular.size) => Left(EvenNumberOfWeaponsProvided)
      case valid => Right(calcAcc(List.empty, List.empty, List.empty, modular))
    }
  }

  def isEven(number: Int): Boolean = number % 2 == 0
}
