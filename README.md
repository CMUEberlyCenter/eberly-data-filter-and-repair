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

In order to use this you will need to have Java and Maven installed. In future versions we will include a pre-compiled
version directly in the repository.

* JDK: https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html
* Maven: https://www.baeldung.com/install-maven-on-windows-linux-mac

## Currently supported filters are:

* json2xml,

* xml2json, 

* trim, 

* tolower, 

* toupper, 

* hashcode,

* removewhitespace, 

## ToDo:

* Instead of one column to be transformed, allow for multiples and ranges
* Allow arguments to the filters
* Add specific help for each filter/operation
* Generate a report in a file that provides details of what was done to each row with any exceptions and failures
* Allow operations to add a column instead of working in place
* Support the different formats of newlines
