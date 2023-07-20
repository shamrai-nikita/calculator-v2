package com.wix

import scala.collection.mutable.ArrayBuffer
import scala.util.control.Breaks._

object InputValidator {

  private val allowedOperators: Seq[String] = Seq("+", "-", "*", "/", "x", "sqrt", "unary_-")
  private val basicOperators: Seq[String] = Seq("+", "-", "*", "/", "x")
  private val numberRegex = "^-?\\d+(\\.\\d+)?$"
  def validateLineForPrinting(inputLine: String): Seq[String] = {
    // trim and split by spaces (more than one space is also a splitter)
    val line: Array[String] = inputLine.trim.split("\\s+")
    var result: ArrayBuffer[String] = ArrayBuffer.empty[String]

    var countOfNumbers = 0
    var countOfBasicOperators = 0

    breakable {
      for (elem <- line) {
        if (elem.matches(numberRegex)) {
          if (validateNumberIsInteger(elem)) {
            countOfNumbers += 1
            result.append(elem)
          }
          else {
            result = ArrayBuffer.empty[String]
            result.append(s"Unknown RPN token $elem")
            break()
          }
        }
        else if (allowedOperators.contains(elem)) {
          if (elem.equals("x")) {
            countOfNumbers += 1
          }
          else if (basicOperators.contains(elem)){
            countOfBasicOperators += 1
          }
          result.append(elem)
        }
        else {
          result = ArrayBuffer.empty[String]
          result.append(s"Unknown RPN token $elem")
          break()
        }
      }
    }
    checkForOperatorsCountCorrection(result.toSeq, countOfNumbers, countOfBasicOperators)
  }

  private def checkForOperatorsCountCorrection(line: Seq[String],
                                       countOfNumbers: Int,
                                       countOfBasicOperators: Int): Seq[String] = {
    if ((countOfNumbers - 1 != countOfBasicOperators) && (!line.head.startsWith("Unknown")))
       Seq("Unknown - quantity of numbers and operators incorrect")
    else line
  }

  def validateLineForRPNCalc(inputLine: Seq[Seq[String]]): Seq[Seq[String]] = {
    val inputWithoutUnknowns = inputLine.map{
      line => line.filterNot(_.startsWith("Unknown"))
    }
    inputWithoutUnknowns.filterNot(seq => seq.isEmpty)
  }

  private def validateNumberIsInteger(number: String): Boolean = {
    val longValueOfNumber = number.toLong;
    longValueOfNumber <= Integer.MAX_VALUE && longValueOfNumber >= Integer.MIN_VALUE
  }

}
