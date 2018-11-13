# DataFilterAndRepair

A small application that can be used to clean, filter and repair raw spreadsheet data before it's used in other tools.

## Motivation

Often, when dealing with raw data coming out of a database you will be dealing with artifacts and inconsistencies because
data in the database can contain anything necessary in any format. However, spreadsheets and other applications need
well-formatted regular data files to start from. The tool presented here tries to provide some repair and filtering
capabilities for those instances where direct access to the database is not available. The tool will try to do its best
but is not guaranteed to always come up with perfect results.

## Usage

You can either call the Java application directly as can be seen in the **run.sh** shell script, or you can use the
convenience wrapper **repair.sh**, which takes the same arguments but is pre-configures the Java settings.

```
usage: ./repair.sh [-h <arg>] [-i <arg>] [-if <arg>] [-o <arg>] [-of <arg>] [-p <arg>] [-t <arg>] [-v] [-w]

 -h,--help <arg>        Command line help
 -i,--input <arg>       Load data from input file
 -if,--iformat <arg>    Input delimiter character, use t for tab and c for
                        comma. Default is c (comma). Any other character
                        or string will be used as-is
 -o,--output <arg>      Write data to output file, or if not provided
                        write to stdout
 -of,--oformat <arg>    Output delimiter character, use t for tab and c
                        for comma. Default is c (comma). Any other
                        character or string will be used as-is
 -p,--operation <arg>   The operation to perform, one of: json2xml,
                        xml2json, trim, tolower, toupper, hashcode,
                        removewhitespace. Separate with | to run multiple
                        filters. Filters are executed left to right as
                        they are specified in this argument
 -t,--target <arg>      Target column to modify, numeric index You can
                        specify a single index, a comma separated list of
                        indices, a range such as 1-4 or a combination
 -v,--verbose           Show verbose log output
 -w,--overwrite         Overwrite existing file if it exists
```

## Examples

* Repair a comma delimited file that might have spurious commas in the cells of column 8:

```
./repair.sh -v -w --operation repair --target 8 --iformat c --oformat c --input ./data/person-commaseparator.txt --output ./output/person-commaseparator-repaired.filtered.csv
```

* Repair a tab delimited file where some cells have accidental newlines, the newlines are suspected to occur in column 8:

```
./repair.sh -v -w --operation repair --target 8 --iformat t --oformat t --input ./data/person-tabseparator.txt --output ./output/person-tabseparator-repaired.filtered.tsv
```

## Requirements

In order to use this tool you will need to have Java installed. Currently we're compiling against Java 1.8, but later
versions should work.

If you want to build the tool yourself then you will need to have Java and Maven installed. 

* JDK: https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html
* Maven: https://www.baeldung.com/install-maven-on-windows-linux-mac

## Arguments

* **-if,--iformat <arg>** 
  * Description: Input format, use t for tab and c for comma. Default is c (comma). Any other character or string will be used as-is
  * Example to parse with tabs: **--iformat t**
  * Example to use pipes as the separator: **--iformat |**
  * Example to use a comma, either: **--iformat c** or: **--iformat ,**
  * Required: no
  
* **-of,--oformat <arg>** 
  * Description: Output format, use t for tab and c for comma. Default is c (comma). Any other character or string will be used as-is. If not specified the output delimiter will be set to the input delimiter
  * Example to parse with tabs: **--oformat t**
  * Example to use pipes as the separator: **--oformat |**
  * Example to use a comma, either: **--format c** or: **--oformat ,**
  * Required: no  
    
* **-h,--help <arg>**
  * Description: Get help either on the entire application or on a specific command
  * Required: no

* **-i,--input <arg>**
  * Description: Use this argument to point to a file on disk. Currently we do not yet support stdin (console) or url loading
  * Example: --input ./data/person-tabseparator.txt
  * Example: --input /tmp/data/person-pipeseparator.txt
  * Required: yes

* **-o,--output <arg>**
  * Description: When present this argument has a file path. If not provided output will go to stdout (console)
  * Example: --output ./output/person-tabseparator.filtered.tsv
  * Required: no

* **-p,--operation <arg>** 
  * Description: List one or more operations to be performed on each cell of the chosen column(s). See the sections in the README on each available filter. Separate with | to run multiple filters. Filters are executed left to right as they are specified in this argument
  * Example: --operation tolower|trim|json2xml
  * Example: --operation xml2json
  * Example: --operation trim|hashcode
  * Required: yes

* **-t,--target <arg>**
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
  
* **-v,--verbose**
  * Description: turn on debug output to the console. Use this if you want to see detailed information as to what the application does behind the scenes
  * Required: no
  
* **-w,--overwrite**
  * Description: when provided, the application will overwrite any existing file with the same name and path as the output file.
  * Required: no  

## Currently supported filters/operations are:

* **json2xml**,

* **xml2json**, 

* **trim**, 

* **tolower**, 

* **toupper**, 

* **hashcode**,

* **removewhitespace**, 

* **repair**, Always included and placed as the first filter to be run. This filter will transform characters and strings that might harm the parsability by applications such as Excel. This filter will go through a number of iterations over the input cell data to ensure all characters are spreadsheet safe.
 

## ToDo / Wishlist:

* Allow arguments for the filters
* Add specific help for each filter/operation
* Generate a report in a file that provides details of what was done to each row with any exceptions and failures
* Allow operations to add a column instead of working in place
* Support the different formats of newlines: \r\n or \n
