# osb12c-xquery-tester

A simple utility that uses the internal OSB XQuery engine for testing XQuery transformations.

- DSL for testing XQuery transformation in OSB 11g/12c
- Extension for JUnit Test Case
- Testing custom (or OSB build in) xpath functions
- Generating HTML report
- Support for XQuery 1.0 (with modules) - comming soon...

# How to build

- Download the lastest release and build it (mvn clean install)

# How to use in SB project

- In your maven (service bus) project add dependency

```xml
<groupId>com.tomecode.oracle.osb12c.osb12c-xquery-tester</groupId>
<artifactId>osb12c-xquery-unit-tester</artifactId>
<version>0.0.1-SNAPSHOT</version>
```

# Using in the (junit) test cases

- Example of junit test case:

- Simple DSL
```java

//parse xquery file
Xquery xq = new XqueryExecutor().loadXquery(new File("<path to xquery file>"));
//map data to input argument
xq.arg("input").asDouble(new Double("2.2"));
//execute transformation and get result
String out = xq.execute().asXmlString();
```

- Example of extenstion for junit test case:

```java

//Custom TestCase with build DSL
public class TestHello extends TestCaseX2004 {

	public final void testFileStr() throws Exception {
		//load xquery
		Xquery xq = xq("src/test/resources/com/tomecode/oracle/osb12c/x2004/paramInt.xq");
		//map data for argument
		xq.arg("input").asInt(1);
		//execute transformaton
		String out = xq.execute().asXmlString();
		//generate report with name: 'hello' to $projectHome/target/xqUnitTestReports/hello_report.html
		report(xq).generate("hello");
	}
}
```

This project is member of [OSB utilities](https://github.com/tomecode/osb-utilities)
