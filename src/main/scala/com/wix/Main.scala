package com.wix

object Main {
  def main(args: Array[String]): Unit = {

    val rawInput: Seq[String] = FileReader.readLinesFromFile("input.txt")

    val validatedInputForPrinting: Seq[Seq[String]] = for {
      line <- rawInput
    } yield {
      InputValidator.validateLineForPrinting(line)
    }

    val command = CommandObj.runCommand(args)

    command match {
      case CommandObj.Print() => printValidatedInput(validatedInputForPrinting)
      case CommandObj.Eval(x) => {
        val evaluatedResult = RPNCalc.changeXToValidNumAndEvaluate(InputValidator.validateLineForRPNCalc(validatedInputForPrinting), x)
        printValidatedInput(evaluatedResult)
      }
    }

  }

  private def printValidatedInput(validatedInput: Seq[Seq[String]]): Unit = {
    for (line <- validatedInput) println(line.mkString(" "))
  }

}
