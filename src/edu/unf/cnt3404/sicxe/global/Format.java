package edu.unf.cnt3404.sicxe.global;

//Represents the grammatical structure of an instruction's arguments. 
//Includes formats more detailed than just formats 1-4.
public enum Format {
	FORMAT1, //Without arguments
	FORMAT2R, //With 1 register argument (0 - 15)
	FORMAT2N, //With 1 numerical argument (1 - 16)
	FORMAT2RR, //With two register arguments (0 - 15)
	FORMAT2RN, //With 1 register (0 - 15) and 1 numerical (1 - 16) argument
	FORMAT34, //Without arguments (RSUB)
	FORMAT34M, //With 1 expression argument
}
