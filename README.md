# JavaCompilerIsStrange
Repository for Stackoverflow Question: https://stackoverflow.com/questions/72737445/system-java-compiler-behaves-different-depending-on-dependencies-defined-in-mave


Recently I've observed a (at least for me) strange behavior of the Java Compiler "ToolProvider.getSystemJavaCompiler()".

* If I try to compile a not-compilable java file in a "bare" maven project, I can obtain the errors as expected.

* If I add certain dependencies (I've first observed this when adding log4j), the compiler does not provide any information regarding compiler errors anymore.

To demonstrate this behavior, I've created an example repository for this: https://github.com/dfuchss/JavaCompilerIsStrange

In this repository I've added a simple main method that tries to parse the AST of an invalid Java File. The main method throws an exception if the diagnostics object contains no errors. This main method will be invoked by a single test.
In my pom.xml I've created a profile "strange" that simply adds a dependency to the project (that is not used but obviously will be added to the classpath after activating the profile). For this example it's the "metainf-services" dependency.
In the `run.sh` file, I simply execute `mvn test` twice. 
First without the profile activated and after that with the activated profile.

If you run the script you get a successful test (because the invalid syntax was detected) and an failed test (because the invalid syntax was not detected after adding the dependency)

```
## Build without activated profile
[INFO] Scanning for projects...
[....]
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running SimpleExecTest
src/main/resources/Example.java:4: error: ';' expected
                System.out.println("Hello World!")
                                                  ^
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.19 s - in SimpleExecTest
[INFO] 
[INFO] Results:
[INFO]
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  6.832 s
[INFO] Finished at: 2022-06-24T00:57:46+02:00
[INFO] ------------------------------------------------------------------------

## Build with activated profile
[INFO] Scanning for projects...
[....]
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running SimpleExecTest
[ERROR] Tests run: 1, Failures: 0, Errors: 1, Skipped: 0, Time elapsed: 0.203 s <<< FAILURE! - in SimpleExecTest
[ERROR] SimpleExecTest.testMain  Time elapsed: 0.171 s  <<< ERROR!
java.lang.Error: Shall not be possible to compile.
        at org.fuchss.Main.main(Main.java:46)
        at SimpleExecTest.testMain(SimpleExecTest.java:7)
[....]
[INFO]
[INFO] Results:
[INFO]
[ERROR] Errors: 
[ERROR]   SimpleExecTest.testMain:7 »  Shall not be possible to compile.
[INFO]
[ERROR] Tests run: 1, Failures: 0, Errors: 1, Skipped: 0
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  6.323 s
[INFO] Finished at: 2022-06-24T00:57:54+02:00
[INFO] ------------------------------------------------------------------------
[....]

```


Does anyone has an idea how to resolve this behavior?
