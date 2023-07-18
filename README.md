# Calculator

The goal of the exercise is
to apply the knowledge you acquired in "Scala & Functional Programming Essentials" course
to write a program for working with arithmetic expressions.
The exercise is split into several tasks.
Your code will be reviewed after each task.

## Tasks

### 0. Setup

Clone this repo.
It's an SBT project with `Main.scala` file and sbt-assembly plugin.
You can build it from terminal using `sbt assembly`,
after that you can run it using `java -jar target/scala-2.13/calculator-assembly-1.0.jar command arg1 arg2 etc`.

You'll need to implement several commands.
First command line argument will determine which command to run.
The rest of command line arguments will vary depending on a command.
Each command needs to read every line of the standard input, do something,
and print the result for each line to the standard output.

### 1. Parse arithmetic expression in reverse Polish notation (RPN)

Expressions we are going to work with will be written in
[reverse Polish notation](https://en.wikipedia.org/wiki/Reverse_Polish_notation).
The goal of this task is to parse RPN expression and to store it in memory.
Later you'll need to perform operations on these expressions,
so you need to create a data type to store expressions as
[trees](https://en.wikipedia.org/wiki/Binary_expression_tree).

Use `Int` type for numbers.
Numbers and operators are separated with one or more whitespace characters.
Program should support
- binary addition, subtraction, multiplication and division;
- unary minus, square root;
- negative number literals;
- a variable called `x`.

| Expression                                                   | Input               |
|--------------------------------------------------------------|---------------------|
| $\left(2 + 2\right) \times 2$                                | `2 2 + 2 *`         |
| $2 + 2 \times 2$                                             | `2 2 2 * +`         |
| $2 + 2 \times 2$                                             | `  2   2  2  * +  ` |
| $7 \div 3 + 4 \times 5 - 6$                                  | `7 3 / 4 5 * + 6 -` |
| $7 \div \left(\left(3+4\right)\times\left(5-6\right)\right)$ | `7 3 4 + 5 6 - * /` |
| $-\left(3 \times x\right)$                                   | `3 x * unary_-`     |
| $\sqrt{3 \times x}$                                          | `3 x * sqrt`        |
| $-3 \times x$                                                | `-3 x *`            |
| $-2147483648$                                                | `-2147483648`       |

Note that minimal and maximal values of type `Int` are $-2147483648$ and $2147483647$.
Parsing number `2147483648` should fail,
so we can't represent number $-2147483648$ as `2147483648 unary_-`.

Implement `print` command.

Read all lines from the standard input,
parse every line as an RPN expression,
and print it back without extra whitespace characters.
If some line is not a correct RPN expression,
print an error message on that line instead,
preferably a helpful one.

Example:

```bash
% cat input.txt
    x 2 2 * +   
7 	   3 4 + 5    6 - * / 		sqrt    unary_-
-2147483648
2147483648 unary_-
2 2 @
2 2
2 2 2 * + -
% cat input.txt | sed 's/ /·/g;s/\t/￫/g'
····x·2·2·*·+···
7·￫···3·4·+·5····6·-·*·/·￫￫sqrt····unary_-
-2147483648
2147483648·unary_-
2·2·@
2·2
2·2·2·*·+·-
% cat input.txt | java -jar target/scala-2.13/calculator-assembly-1.0.jar print
x 2 2 * +
7 3 4 + 5 6 - * / sqrt unary_-
-2147483648
Unknown RPN token 2147483648
Unknown RPN token @
There are 2 elements in stack after parsing all tokens of an RPN expression, should be 1
Not enough arguments for operator -
```

### 2. Evaluate expressions

Implement `eval` command.

Take the value of `x` as an argument,
and evaluate each expression from the input with that `x`.
Print results to the output.
Program must not terminate if some error occurs in the evaluation process.

Example:

```bash
% cat input2.txt                                                                 
x 2 2 * +
3 x /
3 4 + 5 x - * 7 /
% cat input2.txt | java -jar target/scala-2.13/calculator-assembly-1.0.jar eval -12
-8
0
17
% cat input2.txt | java -jar target/scala-2.13/calculator-assembly-1.0.jar eval 0  
4
error
5
% cat input2.txt | java -jar target/scala-2.13/calculator-assembly-1.0.jar eval 3
7
1
2
```

### 3. Handle errors

In this task you need to handle two types of errors which can occur in the evaluation process:
- division by zero;
- square root of a negative number.

Implement three versions of `eval` command:
- `eval-exceptions` must handle those errors using exceptions;
- `eval-option` must handle those errors using `Option` type;
- `eval-either` must handle those errors using `Either` type.

When error occurs, print in instead of the result.

Example:

```bash
% cat input3.txt 
3 4 + 5 x - *
7 x -
3 4 + 5 x - * 7 x - / sqrt
% cat input3.txt | java -jar target/scala-2.13/calculator-assembly-1.0.jar eval-either 5
0
2
0
% cat input3.txt | java -jar target/scala-2.13/calculator-assembly-1.0.jar eval-either 6
-7
1
SqrtOfANegativeNumberError
% cat input3.txt | java -jar target/scala-2.13/calculator-assembly-1.0.jar eval-either 7
-14
0
DivisionByZeroError
% cat input3.txt | java -jar target/scala-2.13/calculator-assembly-1.0.jar eval-either 8
-21
-1
4
```

And likewise for `eval-exceptions` and `eval-option`.

### 4. Support different number types

So far, we used `Int` type for numbers.
In this task you need to implement support for other number types.

Remove all code for `eval` from task 2 if you haven't done so yet,
and rename command from task 3 that you like most to `eval`.

Add `-type` option to commands `print` and `eval`
with these possible values:
- `-type=Int` — use `Int` type for parsing, printing and evaluation;
- `-type=Double` — use `Double`;
- `-type=BigInt` — use `BigInt`.

If it's not specified, default to `Int`.

Example:
```bash
% cat input4.txt 
3 4 + 5 x - *
7 x -
3 4 + 5 x - * 7 x - / sqrt
123456789 123456789 123456789 123456789 123456789 * * * *
% cat input4.txt | java -jar target/scala-2.13/calculator-assembly-1.0.jar eval 8 -type=Int   
-21
-1
4
-962395515
% cat input4.txt | java -jar target/scala-2.13/calculator-assembly-1.0.jar eval 8 -type=Double
-21.0
-1.0
4.58257569495584
2.867971860299718E40
% cat input4.txt | java -jar target/scala-2.13/calculator-assembly-1.0.jar eval 8 -type=BigInt
-21
-1
4
28679718602997181072337614380936720482949
```

## Advanced exercises (if previous ones were not enough)

### Simplify

Implement `simplify` command which evaluates constant branches of the expression.

Example:
```bash
% cat input4.txt 
3 4 + 5 x - *
7 x -
3 4 + 5 x - * 7 x - / sqrt
123456789 123456789 123456789 123456789 123456789 * * * *
% cat input4.txt | java -jar target/scala-2.13/calculator-assembly-1.0.jar simplify -type=BigInt
7 5 x - *
7 x -
7 5 x - * 7 x - / sqrt
28679718602997181072337614380936720482949
```

### Take derivative by x

Implement `differentiate` command which takes derivative of an expression by $x$.

Example:
```bash
% cat input4.txt                                                                                
3 4 + 5 x - *
7 x -
3 4 + 5 x - * 7 x - / sqrt
123456789 123456789 123456789 123456789 123456789 * * * *
% cat input4.txt | java -jar target/scala-2.13/calculator-assembly-1.0.jar differentiate             
-7
-1
1 2 7 5 x - * 7 x - / sqrt * / -7 7 x - * 7 5 x - * -1 * - 7 x - 7 x - * / *
0
% echo "x x *" | java -jar target/scala-2.13/calculator-assembly-1.0.jar differentiate          
x x +
```