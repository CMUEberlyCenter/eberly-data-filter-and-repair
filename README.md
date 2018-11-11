# DataFilterAndRepair

A small application that can be used to clean, filter and repair raw spreadsheet data before it's used in other tools

```
Usage: DataFiltering [-f <arg>] [-h <arg>] [-i <arg>] [-o <arg>] [-p <arg>] [-t <arg>] [-v]

A small application that can be used to clean, filter and repair raw
spreadsheet data before it's used in other tools

 -f,--format <arg>      Input format, use t for tab and c for comma.
                        Default is c. Any other character or string will
                        be used as-is
 -h,--help <arg>        Command line help
 -i,--input <arg>       Load data from input file
 -o,--output <arg>      Write data to output file, or if not provided
                        write to stdout
 -p,--operation <arg>   The operation to perform, one of: json2xml,
                        xml2json, trim, tolower, toupper, hashcode. Separate with |
                        to run multiple filters. Filters are executed left
                        to right as they are specified in this argument
 -t,--target <arg>      Target column to modify, numeric index
 -v,--verbose           Show verbose log output
```

## Requirements

In order to use this tool you will need to have Java installed. Currently we're compiling against Java 1.8, but later
versions should work.

If you want to build the tool yourself then you will need to have Java and Maven installed. 

* JDK: https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html
* Maven: https://www.baeldung.com/install-maven-on-windows-linux-mac

## Arguments

* **-f,--format <arg>** 
  * Description: Input format, use t for tab and c for comma. Default is c (comma). Any other character or string will be used as-is
  * Example to parse with tabs: **--format t**
  * Example to use pipes as the separator: **--format |**
  * Example to use a comma, either: **--format c** or: **--format**
  * Required: no
    
* **-h,--help <arg>** Command line help
  * Description: Get help either on the entire application or on a specific command
  * Required: no

* **-i,--input <arg>** Load data from input file
  * Description: Use this argument to point to a file on disk. Currently we do not yet support stdin (console) or url loading
  * Example: --input ./data/person-tabseparator.txt
  * Example: --input /tmp/data/person-pipeseparator.txt
  * Required: yes

* **-o,--output <arg>** Write data to output file, or if not provided write to stdout
  * Description: (Optional argument) When present this argument has a file path. If not provided output will go to stdout (console)
  * Example: --output ./output/person-tabseparator.filtered.tsv
  * Required: no

* **-p,--operation <arg>** The operation to perform. See the sections in the README on each available filter. Separate with | to run multiple filters. Filters are executed left to right as they are specified in this argument
  * Description:
  * Required: yes

* **-t,--target <arg>** Target column to modify, numeric index
  * Description: A specification of which columns to apply the requested operation(s) on. You can specify a single column, a range or a combination separated by commas.
  * Example (valid ranges):
    * --target 1
    * --target 1,2,3
    * --target 1-9
    * --target 1-9,10-19,20-199
    * --target 1-8,9,10-18,19,20-199
  * Example (invalid ranges): 
    * --target A
    * --target 1,2,
    * --target 1 - 9
  * Required: yes
  
* **-v,--verbose**  Show verbose log output
  * Description: turn on debug output to the console. Use this if you want to see detailed information as to what the application does behind the scenes
  * Required: no

## Currently supported filters/operations are:

* json2xml,

* xml2json, 

* trim, 

* tolower, 

* toupper, 

* hashcode,

* removewhitespace, 

## ToDo:

* Allow arguments to the filters
* Add specific help for each filter/operation
* Generate a report in a file that provides details of what was done to each row with any exceptions and failures
* Allow operations to add a column instead of working in place
* Support the different formats of newlines
