package com.wix

import scala.collection.mutable.ArrayBuffer
import scala.io.Source

object FileReader {

  def readLinesFromFile(filePath: String): Seq[String] = {

    val input: ArrayBuffer[String] = ArrayBuffer.empty[String]

    // Using Source.fromFile to open the file and get an iterator over its lines
    val fileLines: Iterator[String] = Source.fromFile(filePath).getLines()

    // Loop through each line and process it as required
    fileLines.foreach { line =>
      input.append(line) // Replace this with your processing logic
    }
    // Don't forget to close the file after reading
    Source.fromFile(filePath).close()

    input.toSeq
  }

}
