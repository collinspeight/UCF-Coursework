/*
Team Name: Compiler Builder 9
Team Members:
    Collin Speight
    Matt Kazen
    Khandoker Ahmed
    Joe Hummel
    Eric Andrews
*/

#include<stdio.h>
#include<stdlib.h>

typedef char* string;

void main(int argc, char *argv[]){
    string infile = argv[1];
    string outfile = argv[2];
    
    lexer(infile);      /*call the two functions necessary to complete the compilation process*/
    parser(outfile);
}