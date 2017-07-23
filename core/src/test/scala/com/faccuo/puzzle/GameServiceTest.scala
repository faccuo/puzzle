package com.faccuo.puzzle

import org.scalatest.{FlatSpec, Matchers}

class GameServiceTest extends FlatSpec with Matchers {

  behavior of "GameService.validate(rules)"

  val service = new GameService {}

  it should "return MissingRulesError when empty list is provided" in {
    service.validate(List.empty) shouldBe Left(MissingRulesError)
  }

  it should "return EvenNumberOfWeaponsProvided when rules with even number of weapons are provided" in {
    service.validate(List(
      "A" -> "B"
    )) shouldBe Left(EvenNumberOfWeaponsProvided)

    service.validate(List(
      "A" -> "B",
      "C" -> "D"
    )) shouldBe Left(EvenNumberOfWeaponsProvided)
  }

  it should "return a list of invalid weapons when wrong rules are provided" in {
    service.validate(List(
      "B" -> "A",
      "A" -> "C"
    )) match {
      case Left(InvalidRulesForWeaponsError(failed)) =>
        failed should contain only("B", "C")
      case _ =>
        fail
    }
  }

  it should "return the list of rules when they are valid" in {
    val rules = List(
      "B" -> "A",
      "A" -> "C",
      "C" -> "B"
    )
    service.validate(rules) match {
      case Right(returned) =>
        returned should contain theSameElementsAs rules
      case _ => fail
    }
  }

  behavior of "GameService.translateModularToRules(modular)"

  it should "return MissingRulesError when no modular rules are provided" in {
    service.translateModularToRules(List.empty) shouldBe Left(MissingRulesError)
  }

  it should "return no rules for a single element list" in {
    service.translateModularToRules(List("A")) shouldBe Right(List.empty)
  }

  it should "return RepeatedWeaponsInModularDefinitionError when repeated weapons are provided" in {
    service.translateModularToRules(List("A", "B", "A")) shouldBe Left(RepeatedWeaponsInModularDefinitionError)
  }

  it should "return rules for the list of elements provided" in {
    service.translateModularToRules(List("A", "B", "C")) match {
      case Right(rules) => rules should contain only(
        ("B", "A"),
        ("A", "C"),
        ("C", "B"))
      case _ => fail
    }
  }

}
