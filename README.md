# FrontEndComiler
A compiler I made to practice some central ideas of compilation. This is my first compiler and features were
added not so much out of necessity for the language, but to sufficiently practice implementing a diverse range of
compiler concepts without becoming redundant. The language itself is called Krisp! A turing complete language for minimalists. 

It contains a wide range of compilation features such as boolean and arithmetic expressions, alongside with
branching statements such as if and when. I will probably add functions in the future when I have a bit more time on my hands.

The compiled code is a MIPS-x86 hybrid register based intermediate code. I chosse this as I believed it would make translating to both ARM or x86-64 easier at the back end.

Some syntax for Krisp is as follows (mind the spacing!):
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
