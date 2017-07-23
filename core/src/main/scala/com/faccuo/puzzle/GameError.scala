package com.faccuo.puzzle

sealed trait GameError

case object MissingRulesError extends GameError

case object EvenNumberOfWeaponsProvided extends GameError

case object RepeatedWeaponsInModularDefinitionError extends GameError

case class InvalidWeaponInPlay[Weapon](weapon: Weapon) extends GameError

case class InvalidRulesForWeaponsError[Weapon](weapons: List[Weapon]) extends GameError
