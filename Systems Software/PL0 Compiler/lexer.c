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
#include<string.h>

#define IDENT_MAX 12 /*Maximum identifier length*/
#define NUM_MAX 65535  /*Minimum number value*/

typedef enum token{     /*token types*/
    nulsym = 1, identsym = 2, numbersym = 3, plussym = 4, minussym = 5, multsym = 6, slashsym = 7, oddsym = 8,
    eqlsym = 9, neqsym = 10, lessym = 11, leqsym = 12,gtrsym = 13, geqsym = 14, lparentsym = 15, rparentsym = 16, 
    commasym = 17, semicolonsym = 18, periodsym = 19, becomessym = 20, beginsym = 21, endsym = 22, ifsym = 23, thensym = 24, 
    whilesym = 25, dosym = 26, callsym = 27, constsym = 28, varsym = 29, procsym = 30, writesym = 31, readsym = 32, elsesym = 33
}token_type;

typedef char* string;

char *reserved_word_name[] = {"const", "var", "procedure", "call", "begin", "end", "if", "then", "else", "while", "do", "read", "write", "odd"}; /*Array of reserved words*/

union{
    char id[IDENT_MAX+1];
    int num;
}value;

int errorFlag = 0, commentFlag = 0;  /*Global error and comment flags*/

token_type lex(FILE*, FILE*);

/*main function*/
void lexer(string infile){
    FILE *fin = fopen(infile, "r"), *fout = fopen("lexout.txt", "w");    /*Opening input file*/
    if(fin == NULL || fout == NULL){
        printf("I/O File Error.\n");
        return;
    }

    
    fseek(fin, 0, SEEK_SET);    /*Changes position in input file to beginning*/
    token_type tok;
    while((tok = lex(fin, fout)) != nulsym){
        if(errorFlag == 0 && commentFlag == 0){         /*If there are no errors, print the int associated with token type*/
            fprintf(fout, "%d ", tok);
            if(value.id != NULL && tok == identsym)
                fprintf(fout, "%s ", value.id);
            else if(value.num != -1 && tok == numbersym)
                fprintf(fout, "%d ", value.num);
        }
        
        else if(errorFlag == 1)
            break;
        
        commentFlag = 0;        /*Reset flags to 0 after each char is read in*/
    }
    
    fclose(fin);    /*Close input and output files after use.*/
    fclose(fout);
}

token_type lex(FILE *fin, FILE *fout){
    char c;
    
    while((c = fgetc(fin)) == ' ' || c == '\t' || c == '\n' || c == '\r');       /*Do nothing if a space, tab, newline, or carriage return is encountered and move onto the next char*/
    
    if(c == EOF)        /*If the end of the file is reached, stop the program*/
        return nulsym;
    
    if(isalpha(c)){         /*If the char is a letter, store all alphanumeric values following it into an array and label the string to its appropriate token type*/
        char tempStr[IDENT_MAX+1], *p = tempStr;
        int n = 0;
        
        do{
            if(n > IDENT_MAX+1){        /*Checks for if the identifier is longer than allowed*/
                error(30);
                errorFlag = 1;
                break;
            }
            *p++ = c;   /*If not too long, populate the array*/
            
            n++;
        }while((c = fgetc(fin)) != EOF && isalnum(c));
        *p = '\0';      /*End the string with the NUL terminator*/
        
        ungetc(c, fin);
        
        strcpy(value.id, tempStr);
        
        /*Determine whether the string fits into one of the reserved words, if not, it is a variable name*/
        if(strcmp(value.id, reserved_word_name[0]) == 0)
            return constsym;
            
        else if(strcmp(value.id, reserved_word_name[1]) == 0)
            return varsym;
        
        else if(strcmp(value.id, reserved_word_name[2]) == 0)
            return procsym;
        
        else if(strcmp(value.id, reserved_word_name[3]) == 0)
            return callsym;
        
        else if(strcmp(value.id, reserved_word_name[4]) == 0)
            return beginsym;
        
        else if(strcmp(value.id, reserved_word_name[5]) == 0)
            return endsym;
        
        else if(strcmp(value.id, reserved_word_name[6]) == 0)
            return ifsym;
        
        else if(strcmp(value.id, reserved_word_name[7]) == 0)
            return thensym;
        
        else if(strcmp(value.id, reserved_word_name[8]) == 0)
            return elsesym;
        
        else if(strcmp(value.id, reserved_word_name[9]) == 0)
            return whilesym;
        
        else if(strcmp(value.id, reserved_word_name[10]) == 0)
            return dosym;
        
        else if(strcmp(value.id, reserved_word_name[11]) == 0)
            return readsym;
        
        else if(strcmp(value.id, reserved_word_name[12]) == 0)
            return writesym;
        
        else if(strcmp(value.id, reserved_word_name[13]) == 0)
            return oddsym;
            
        else{
            if(n <= IDENT_MAX+1)
                return identsym;
        }
    }
    
    else if(isdigit(c)){        /*If the char is a digit, store all digits following it into an array and convert the string into an integer*/
        char tempStr[100], *p = tempStr;
        
        do{
            *p++ = c;
        }while((c = fgetc(fin)) != EOF && isdigit(c));      /*Same method we used for letters*/
        *p = '\0';
        
        if(isalpha(c)){
            error(31);     /*If a digit is followed by a letter with no spaces in between, it must be an incorrectly declared identifier*/
            errorFlag = 1;
                
            do{
                c = fgetc(fin);     /*Cycle through the file until the end of the incorrect identifier name so we can continue*/
            }while(isalpha(c));
        }
        
        ungetc(c,fin);      /*last char assigned to c wasn't part of incorrect identifier so we unget it*/
        
        value.num = atoi(tempStr);      /*Convert string into an int and store it in our union*/
        if(value.num > NUM_MAX){
            error(25);   /*Error for numbers that are too big*/
            errorFlag = 1;
        }
        else
            return numbersym;
    }
    
    switch(c){      /*Switch for operator based tokens*/
        case '+':
            return plussym;
        case '-':
            return minussym;
        case '*':
            return multsym;
        case '/':       /*Yet again filter out comments using the same method as earlier*/
            if((c = fgetc(fin)) == '*'){
                commentFlag = 1;
                while(c != '/')
                    c = fgetc(fin);
                    
                c = fgetc(fin);
                break;
            }
            
            ungetc(c,fin);
            return slashsym;
        /*case '!':
            if((c = fgetc(fin)) == '='){
                return neqsym;
            }
            
            ungetc(c, fin);
            
            break;*/
        case ':':
            if((c = fgetc(fin)) == '='){
                return becomessym;
            }
            
            ungetc(c, fin);
            
            break;
        case '=':
            return eqlsym;
        case '<':
            if((c = fgetc(fin)) == '='){
                return leqsym;
            }
            
            ungetc(c, fin);
            
            if((c = fgetc(fin)) == '>'){
                return neqsym;
            }
            
            ungetc(c, fin);
            
            return lessym;
        case '>':
            if((c = fgetc(fin)) == '=')
                return geqsym;
            
            ungetc(c, fin);
            
            return gtrsym;
        case ',':
            return commasym;
        case ';':
            return semicolonsym;
        case '.':
            return periodsym;
        case '(':
            return lparentsym;
        case ')':
            return rparentsym;
        default:
            error(32);     /*If none of the tokens fit, it must be invalid*/
            errorFlag = 1;
            break;
    }
}