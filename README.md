# FrontEndComiler
A front end compiler I made to practice some central ideas of compilation. This is my first compiler and features were
added not so much out of necessity for the language, but to sufficiently practice implementing a diverse range of
compiler concepts. I did not wish to make this into a fully fledged C compiler with all sorts of fancy bells and
whistles as a lot of concepts are very similar and would have been redundant from an educational point of view.

It however contains a wide range of compilation features such as boolean and arithmetic expressions, alongside with
branching statements such as if and when.

The language compiled is known by my invention as Krisp and the compiled code is a MIPS-like register based intermediate
code.

Some syntax for Krisp is as follows:
Assignment statements:

a assign b
a assign 9
a assign expr

Expression statements are strings of operands and arithmetic operators. They are evaluated left to right as god
intended of course

expr := a*ab+23

Boolean expressions are much the same and use symbols > < and or = nand nor e.x.

bool := a < b

If and while statements are as follows:

if ( a < b ) {
stmnts
}

while ( a = b ) {
stmnts
}

Here is not an example of compiled code. To compile a file run "java KrispCompiler inputfile outputfile".

a assign 4 * 5
while ( 2 = a ) {
    if ( a < 2 ) {
    b assign a + 2 * a
    }
    a assign 4 * a
}
c assign a * b

Compiles to:

movc &%rtr, @5
movc &t0, @4
mulr &%rtr, $t0, $%rtr
movr &v0, $%rtr
movc &t0 ,@2
movr &t1 ,$0
leqr &%rtr ,$t0 , $t1

BRANCH0:
movr &t0 ,$0
movc &t1 ,@2
lltr &%rtr ,$t0 , $t1
brze $%rtr, @BRANCH1
movc &%rtr, @2
movr &%rtr, $v0
movc &t0, @2
mulr &%rtr, $t0, $%rtr
addr &%rtr, $v0, $%rtr
movr &v1, $%rtr

BRANCH1:
movr &%rtr, $v0
movc &t0, @4
mulr &%rtr, $t0, $%rtr
movr &v0, $%rtr
brze $%rtr, @BRANCH0
movr &%rtr, $v1
mulr &%rtr, $v0, $%rtr
movr &v2, $%rtr