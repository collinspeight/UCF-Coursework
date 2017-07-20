/************************Full PL/0****************************/
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

#define MAX_SYMBOL_TABLE_SIZE 100
#define IDENT_MAX 12 /* Maximum identifier length */
#define MAX_CODE_LENGTH 500


typedef enum{     /* token types */
    nulsym = 1, identsym = 2, numbersym = 3, plussym = 4, minussym = 5, multsym = 6, slashsym = 7, oddsym = 8,
    eqlsym = 9, neqsym = 10, lessym = 11, leqsym = 12,gtrsym = 13, geqsym = 14, lparentsym = 15, rparentsym = 16, 
    commasym = 17, semicolonsym = 18, periodsym = 19, becomessym = 20, beginsym = 21, endsym = 22, ifsym = 23, thensym = 24, 
    whilesym = 25, dosym = 26, callsym = 27, constsym = 28, varsym = 29, procsym = 30, writesym = 31, readsym = 32, elsesym = 33
}token_type;

typedef struct{     /* token struct */
  token_type type;
  char value[12];
}token;

typedef struct symbol{      /* Symbol structure */
    int kind;
    char name[12];
    int val;
    int level;
    int addr;
}symbol;
symbol symbolTable[MAX_SYMBOL_TABLE_SIZE];  /* table for symbols */

typedef struct{     /* struct for op, l and m codes */
    int op;
    int l;
    int m;
}code;

code codeArray[MAX_CODE_LENGTH];
int cx = 0, numsymbols = 0, level = 1, currentLevel = 1;

typedef char* string;

int eFlag = 0;      /* error flag */
token currentTok;

void program(FILE *);
void block(FILE *);
void constant(FILE *);
void variable(FILE *, int*);
void statement(FILE *);
void expression(FILE *);
void term(FILE *);
void factor(FILE *);
void condition(FILE *);
int getTok(FILE *);
int relationM(void);
void putSymbol(int, string, int, int, int);
symbol *getSymbol(string);
void emit(int, int, int);
void error(int);

/*Main function*/
void parser(string outfile){
    FILE *fin = fopen("lexout.txt", "r"), *fout = fopen(outfile, "w");      /* opens the input and output files then checks if valid */
    if(fin == NULL || fout == NULL){
        printf("I/O File Error.\n");
        return;
    }
    
    int i;
    for(i = 0; i < MAX_CODE_LENGTH; i++){   /* initializes codeArray */
        codeArray[i].op = 0;
        codeArray[i].l = 0;
        codeArray[i].m = 0;
    }
    
    program(fin);
    
    if(!eFlag)      /* prints success message if no errors are flagged */
        printf("No errors, program is syntactically correct.\n");
        
    for(i = 0; i < cx; i++) /* outputs codeArray values to output file */
        fprintf(fout, "%d %d %d\n", codeArray[i].op, codeArray[i].l, codeArray[i].m);
    
    fclose(fin);
    fclose(fout);
}

void program(FILE *fin){    /* sends the input file to functions that process it */
    getTok(fin);
    
    block(fin);
    
    if(currentTok.type != periodsym && !eFlag){ /* if the next symbol isn't a period send error */
        error(9);
        return;
    }
    
    emit(9, 0, 2);
}

void block(FILE *fin){      /* processes tokens */
    int mem = 4, procIndex, jAddr = cx;
    emit(7, 0, 0);

    if(currentTok.type == constsym) /* checks if constant token */
        constant(fin);
    if(currentTok.type == varsym)   /* checks if variable token */
        variable(fin, &mem);
        
    while(currentTok.type == procsym){
        getTok(fin);
        if(currentTok.type != identsym){        /* returns error if token isn't identsym  */
            error(4);
            return;
        }
        
        putSymbol(3, currentTok.value, 0, 0, 0);
            
        procIndex = numsymbols - 1;     /*  */
        symbolTable[procIndex].level = level;
        symbolTable[procIndex].addr = jAddr + 1;
        
        getTok(fin);
        if(currentTok.type != semicolonsym){    /* returns error if token isn't semicolonsym  */
            error(17);
            return;
        }
        
        getTok(fin);
        level++;
        block(fin);
        
        getTok(fin);
        
        emit(2, 0, 0);
    }
    
    codeArray[jAddr].m = cx;
    
    emit(6, 0, mem);
    
    statement(fin);
    currentLevel--;
}

void constant(FILE *fin){   /* processes constants */
    do{
        getTok(fin);
        
        if(currentTok.type != identsym){    /* returns error if token isn't identsym  */
            error(4);
            return;
        }
        
        char name[12];
        strcpy(name, currentTok.value);
        
        getTok(fin);
        if(currentTok.type != eqlsym){      /* returns error if token isn't eqlsym  */
            error(3);
            return;
        }
        
        getTok(fin);
        if(currentTok.type != numbersym){   /* returns error if token isn't numbersym  */
            error(2);
            return;
        }
        
        putSymbol(1, name, atoi(currentTok.value), 0, 0);
        
        getTok(fin);
    }while(currentTok.type == commasym);
    
    if(currentTok.type != semicolonsym){    /* returns error if token isn't semicolonsym */
        error(10);
        return;
    }
    
    getTok(fin);
}

void variable(FILE *fin, int *mem){ /* processes variables */
    int numVars = 0;
    
    do{
        getTok(fin);
        if(currentTok.type != identsym){    /* returns error if token isn't identsym  */
            error(4);
            return;
        }
        
        numVars++;
        putSymbol(2, currentTok.value, 0, 0, 3 + numVars);
        symbolTable[numsymbols-1].level = level;
        
        (*mem)++;
        
        getTok(fin);
    }while(currentTok.type == commasym);    /* loops when current token is a comma */
    
    if(currentTok.type != semicolonsym){    /* returns error if token isn't semicolonsym  */
        error(5);
        return;
    }
    
    getTok(fin);
}

void statement(FILE *fin){      /* processes statements, for statement token type, look at sym type*/
    int tableIndex;

    if(currentTok.type == identsym){
        symbol *currentSymbol = getSymbol(currentTok.value);
        if(currentSymbol == NULL){  /* Error: No symbol with that name */
            error(29);
            return;
        }
        getTok(fin);
        if(currentTok.type != becomessym){  /* returns error if token isn't becomesym */
            error(11);
            return;
        }
        
        getTok(fin);
        
        expression(fin);

        emit(4, currentLevel, currentSymbol->addr);    /* Outputs STO values */
    }
    
    else if(currentTok.type == writesym)    {   /* get next token if current token is writesym */
        getTok(fin);
        
        if(currentTok.type != identsym) {   /* returns error if token isn't identsym */
            error(19);
            return;
        }
        
        symbol *currentSymbol = getSymbol(currentTok.value);
        if(currentSymbol == NULL)
            error(29);
        if(currentSymbol->kind == 1)
            emit(1, 0, currentSymbol->val);
        else if(currentSymbol->kind == 2)
            emit(3, 0, currentSymbol->addr);
        
        emit(9, 0, 0);
        
        getTok(fin);
    }
    
    else if(currentTok.type == readsym) {   /* returns error if current token is readsym and next token isn't identsym */
        emit(9, 0, 1);
        getTok(fin);
        
        if(currentTok.type != identsym) {
            error(19);
            return;
        }
        
        symbol *currentSymbol = getSymbol(currentTok.value);
        if(currentSymbol == NULL){  /* Error: No symbol with that name */
            error(29);
            return;
        }
        emit(4, currentLevel, currentSymbol->addr);
        
        getTok(fin);
    }
    
    else if(currentTok.type == callsym){    /* returns error if current token is callsym and next token isn't identsym */
        getTok(fin);
        if(currentTok.type != identsym){
            error(14);
            return;
        }
        
        int checkDeclare = 0, i;        /*Checks if the identifier has been declared*/
        for(i = numsymbols-1; i >= 0; i--){
            if(!strcmp(currentTok.value, symbolTable[i].name)){
                tableIndex = i;
                checkDeclare = 1;
            }
        }
        
        if(checkDeclare != 1)   /*If not declared, throw an error*/
            error(11);
            
        if(symbolTable[tableIndex].kind == 3){
            emit(5, currentLevel, symbolTable[tableIndex].addr);
            currentLevel++;
        }
        else
            error(14);
        
        getTok(fin);
    }
    
    else if(currentTok.type == beginsym){
        getTok(fin);
        statement(fin);
        
        while(currentTok.type == semicolonsym){
            getTok(fin);
            statement(fin);
        }
        
        if(currentTok.type != endsym){
            error(8);
            return;
        }
        
        getTok(fin);
    }
    
    else if(currentTok.type == ifsym){
        getTok(fin);
        condition(fin);
        
        
        if(currentTok.type != thensym){
            error(16);
            return;
        }
        
        getTok(fin);
        
        int ctemp = cx;
        emit(8, 0, 0);
        statement(fin);
        getTok(fin);
        
        if(currentTok.type == elsesym){
            int ctemp2 = cx;
            
            emit(7, 0, 0);
            codeArray[ctemp].m = cx;
            getTok(fin);
            
            statement(fin);
            codeArray[ctemp2].m = cx;
        }
        else
            codeArray[ctemp].m = cx;
    }
    
    else if(currentTok.type == whilesym){
        int c1x = cx;
        getTok(fin);
        condition(fin);
        int c2x = cx;
        emit(8, 0, 0);
        
        if(currentTok.type != dosym){
            error(18);
            return;
        }
        
        getTok(fin);
        statement(fin);
        emit(7, 0, c1x);
        codeArray[c2x].m = cx; 
    }
}

void expression(FILE *fin){ /* processes addition or subtraction */
    
    int addop;
    if(currentTok.type == plussym || currentTok.type == minussym){
        addop = currentTok.type;
        getTok(fin);
        term(fin);
        if(addop == minussym)
            emit(2, 0, 1); /* negate */
    }
    else
        term(fin);
    
    while(currentTok.type == plussym || currentTok.type == minussym){
        addop = currentTok.type;
        getTok(fin);
        term(fin);
        if (addop == plussym)
            emit(2, 0, 2); /* addition */
        else
            emit(2, 0, 3); /* subtraction */
    }
}

void term(FILE *fin){
    int mulop;
    factor(fin);
    
    while(currentTok.type == multsym || currentTok.type == slashsym){
        mulop = currentTok.type;
        getTok(fin);
        factor(fin);
        if(mulop == multsym)
            emit(2, 0, 4); // Mult
        else
            emit(2, 0, 5); // Div
    }
}

void factor(FILE *fin){
    if(currentTok.type == identsym) {
        symbol *currentSymbol = getSymbol(currentTok.value);
        if(currentSymbol == NULL){
            error(29);
            return;
        }
        
        emit(3, 0, currentSymbol->addr);
        getTok(fin);
    }
        
    else if(currentTok.type == numbersym)   {
        emit(1, 0, atoi(currentTok.value));
        getTok(fin);
    }
        
    else if(currentTok.type == lparentsym){
        getTok(fin);
        expression(fin);
        
        if(currentTok.type != rparentsym){
            error(22);
            return;
        }
        
        getTok(fin);
    }
    
    else{
        error(24);
        return;
    }
}

void condition(FILE *fin){  /* processes conditions */

    if(currentTok.type == oddsym){
        getTok(fin);
        expression(fin);
        emit(2, 0, 6);
    }
    
    else{       
        expression(fin);
        
        int rM = relationM();
        
        if(rM == -1){
            error(20);
            return;
        }
        
        getTok(fin);
        expression(fin);
        
        emit(2, 0, rM);
    }
}

int relationM(){    /* returns values if certain token */
    if(currentTok.type == eqlsym)
        return 8;
    else if(currentTok.type == neqsym)
        return 9;
    else if(currentTok.type == lessym)
        return 10;
    else if(currentTok.type == leqsym)
        return 11;
    else if(currentTok.type == gtrsym)
        return 12;
    else if(currentTok.type == geqsym)
        return 13;
    else
        return -1;
}

void putSymbol(int kind, string name, int num, int level, int modifier) {   /* sets symbols */
    
    int i = 0;
    int lastSymbol;
    
    symbol* currentSymbol = getSymbol(name);
    
    if(currentSymbol != NULL){  /* if the current symbol is invalid, output error */
        error(27);
        return;
    }

    else{       /* finds last symbol */
        while(symbolTable[i].kind != 0 && i <= MAX_SYMBOL_TABLE_SIZE)
            i++;
    
        if(i > 500){
            error(28);
            return;
        }
        else    {
            symbolTable[i].kind = kind;
            strcpy(symbolTable[i].name, name);
            symbolTable[i].val = num;
            symbolTable[i].level = level;
            symbolTable[i].addr = modifier;
            
            numsymbols++;
        }
    }
}

symbol *getSymbol(string name)  {   /* generates symbol from list */
    
    int i;
    
    for(i = 0; i < MAX_SYMBOL_TABLE_SIZE; i++)  {       /* gets symbol for symbol array */
        if(strcmp(symbolTable[i].name, name) == 0)
            return &symbolTable[i];
    }
    
    return NULL;
    
}

void emit(int op, int l, int m){    /* loads code into codeArray */
                    
    if(cx > MAX_CODE_LENGTH)    /* generates error if the code is too long */
        error(26);
    else{
        codeArray[cx].op = op;
        codeArray[cx].l = l;
        codeArray[cx].m = m;
        cx++;
    }
}

int getTok(FILE *fin){      /* gets the next token from the input file */
    char temp[3];
    fscanf(fin, "%s", temp);    /* read in a token type and store it in a temporary variable */
    
    if(temp != EOF){        /* if there are still tokens left to be read: */
        currentTok.type = atoi(temp);   /* store the typecast integer temp in the current token's type field */
        
        if(currentTok.type == identsym || currentTok.type == numbersym)    /* if the current token is an identifier or number: */
            fscanf(fin, "%s", currentTok.value);    /* scan the next value in the input stream into the current token's value */
        else
            currentTok.value[0] = '\0'; /* if it is not an identifier or number, the current token's value is not necessary */
            
        return 1;
    }
    
    else{       /* if we're out of tokens to be read: */
        currentTok.value[0] = '\0';     /* the current token's value and type are set to null because there is nothing left */
        currentTok.type = nulsym;
        
        return 0;
    }
}

void error(int error_code)  /* generates error message based on value */
{
    eFlag = 1;
    printf("Error number %d: ", error_code);
    switch (error_code)
    {
       case 1:  
            printf("Use = instead of :=.\n"); 
            break;
        case 2:  
            printf("= must be followed by a number.\n"); 
            break;
        case 3:  
            printf("Identifier must be followed by =.\n"); 
            break;
        case 4:  
            printf("const, var, procedure must be followed by identifier.\n"); 
            break;
        case 5:  
            printf("Semicolon or comma missing.\n"); 
            break;
        case 6:  
            printf("Incorrect symbol after procedure declaration.\n"); 
            break;
        case 7:  
            printf("Statement expected.\n"); 
            break;
        case 8:  
            printf("Incorrect symbol after statement part in block.\n"); 
            break;
        case 9:  
            printf("Period expected.\n"); 
            break;
        case 10: 
            printf("Semicolon between statements missing.\n"); 
            break;
        case 11: 
            printf("Undeclared identifier.\n"); 
            break;
        case 12: 
            printf("Assignment to constant or procedure is not allowed.\n"); 
            break;
        case 13: 
            printf("Assignment operator expected.\n"); 
            break;
        case 14: 
            printf("call must be followed by an identifier.\n"); 
            break;
        case 15: 
            printf("Call of a constant or variable is meaningless.\n"); 
            break;
        case 16: 
            printf("then expected.\n"); 
            break;
        case 17: 
            printf("Semicolon or } expected.\n"); 
            break;
        case 18: 
            printf("do expected.\n"); 
            break;
        case 19: 
            printf("Incorrect symbol following statement.\n");
            break;
        case 20: 
            printf("Relational operator expected.\n"); 
            break;
        case 21: 
            printf("Expression must not contain a procedure identifier.\n"); 
            break;
        case 22: 
            printf("Right parenthesis missing.\n"); 
            break;
        case 23: 
            printf("The preceding factor cannot begin with this symbol.\n"); 
            break;
        case 24: 
            printf("An expression cannot begin with this symbol.\n"); 
            break;
        case 25: 
            printf("LEXER ERROR: This number is too large.\n");
            break;
        case 26:
            printf("Code too long.\n");
            break;
        case 27:
            printf("Already Symbol with same name.\n");
            break;
        case 28:
            printf("Too many symbols.\n");
            break;
        case 29:
            printf("No symbol with that name.\n");
            break;
        case 30:
            printf("LEXER ERROR: Identifier too long\n");
            break;
        case 31:
            printf("LEXER ERROR: Identifier does not start with a letter\n");
            break;
        case 32:
            printf("LEXER ERROR: invalid token\n");
            break;
        default:
            printf("Incorrect error code.\n");
            break;
    }
    
    exit(0);    /*End the program if there's an error*/
}
