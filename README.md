# DataFilterAndRepair

A small application that can be used to clean, filter and repair raw spreadsheet data before it's used in other tools

```
usage: DataFiltering
 -f,--format <arg>      Input format, use t for tab and c for comma.
                        Default is c
 -h,--help <arg>        Command line help
 -i,--input <arg>       Load data from input file
 -o,--output <arg>      Write data to output file, or if not provided
                        write to stdout
 -p,--operation <arg>   The operation to perform, one of: json2xml,
                        xml2json, clean, tolower, toupper, repair
 -t,--target <arg>      Target column to modify, numeric index
 -v,--verbose           Show verbose log output
```

