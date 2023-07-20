package com.wix

object CommandObj {
  sealed abstract class Command {
  }

  case class Print() extends Command {}

  case class Eval(x: String) extends Command {}

  def runCommand(args: Array[String]): Command = {
    args.head match {
      case "print" => Print()
      case "eval" => Eval(args(1))
    }

  }

}
