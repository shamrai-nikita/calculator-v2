package com.wix

import scala.collection.mutable
object RPNCalc {

  //works only with valid Seq
  private def evaluate(args: Seq[String]): Double = {
    val stack = mutable.Stack[Double]()
    for(arg <- args; if arg.nonEmpty) arg match {
      case "+" => stack.push(stack.pop() + stack.pop())
      case "*" => stack.push(stack.pop() * stack.pop())
      case "-" =>
        val tmp = stack.pop()
        stack.push(stack.pop() - tmp)
      case "/" =>
        val tmp = stack.pop()
        stack.push(stack.pop() / tmp)
      case "sqrt" => stack.push(Math.sqrt(stack.pop()))
      case "unary_-" => stack.push(stack.pop() - 1)
      case num => stack.push(num.toDouble)
    }
    stack.pop()
  }

  def changeXToValidNumAndEvaluate(args: Seq[Seq[String]], num: String): Seq[Seq[String]] = {
    val withXReplaced: Seq[Seq[String]] = args.map { line =>
      line.map { elem => {
        if (elem.equals("x")) num else elem
      }
      }
    }

    withXReplaced.map(line =>
    Seq(evaluate(line).toString))

  }

}
