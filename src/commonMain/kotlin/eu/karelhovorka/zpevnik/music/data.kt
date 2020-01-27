package eu.karelhovorka.zpevnik.music

val chords = """
# triads
Major triad;    dur,,M,maj; P1,M3,P5
Minor triad;    m,-,mi,min; P1,m3,P5
Augmented triad;aug,+5,#5;  P1,M3,A5
Diminished triad;dim,-5,b5; P1,m3,d5

# sixths
Major sixth;    6,add6;     P1,M3,P5,M6
Minor sixth;    m6,m(add6); P1,m3,P5,M6

# sevenths
Dominant seventh;7;         P1,M3,P5,m7
Major seventh;maj7,Δ7,Δ,M7; P1,M3,P5,M7
Minor major seventh;mM7,m#7,-M7,-Δ7,-Δ; P1,m3,P5,M7
Minor seventh;m7,m-7; P1,m3,P5,m7
Augmented major seventh;Δ(#5),Δ(+5),M7(+5),M7(#5),maj7(+5),maj7(#5); P1,M3,A5,M7
Augmented seventh;aug7,7(#5),7(+5); P1,M3,A5,m7
Half diminished seventh;ø,ø7,m7(dim5),mi7(dim5),min7(dim5),m7(-5),m7(b5),m7(o5); P1,m3,d5,m7
Diminished seventh;o(7),dim,(dim7); P1,m3,d5,d7
Seventh flat five;7(b5),7(-5),7(dim5); P1,M3,d5,m7

# ninths
Major ninth;    (M9),Δ(9),(maj9);     P1,M3,P5,M7,M9
Dominant ninth;    (9);           P1,M3,P5,m7,M9
Dominant minor ninth; 7(-9),7(b9),(b9);   P1,M3,P5,m7,m9
Minor major ninth;-(maj9),m(maj9),mi(maj9),min(maj9);      P1,m3,P5,M7,M9
Minor ninth;m(9),(-(9),mi(9),min(9); P1,m3,P5,m7,M9
Augmented major ninth;+(M9),aug(M9),+(maj9),aug(maj9);      P1,M3,A5,M7,M9
Augmented dominant ninth;+(9),(9#5),aug(9);      P1,M3,A5,m7,M9
Half diminished ninth;ø(9); P1,m3,d5,m7,M9
Half diminished minor ninth;ø(b9); P1,m3,d5,m7,m9
Diminished ninth;o(9),dim(9); P1,m3,d5,d7,M9
Diminished minor ninth;o(b9),dim(b9); P1,m3,d5,d7,m9

# elevenths
Eleventh;11; P1,M3,P5,m7,M9,P11
Major eleventh;M11,maj11; P1,M3,P5,M7,M9,P11
Minor major eleventh;m(M11),m(maj11);   P1,m3,P5,M7,M9,P11
Minor eleventh;m11,-11,mi11,min11;      P1,m3,P5,m7,M9,P11
Augmented major eleventh;aug(M11),aug(maj11); P1,M3,A5,M7,M9,P11
Augmented eleventh;aug11; P1,M3,A5,m7,M9,P11
Half diminished eleventh;ø11; P1,m3,d5,m7,M9,P11
Diminished eleventh;o11,dim11; P1,m3,d5,d7,M9,P11

# sus
Sus2;sus2;      P1,M2,P5
Sus4;sus4,sus;  P1,P4,P5

# add
Add2;add2,add9; P1,M2,M3,P5
Add4;add4,add11; P1,M3,P4,P5

#other
Open five;5; P1,P5

""".trimIndent()



const val intervals = """
0	Perfect unison	P1	unisonus
1	Minor second	m2	semitonus
1	Augmented unison	A1	unisonus superflua
2	Major second	M2	tonus
2	Diminished third	d3	-
3	Minor third	m3	semiditonus
3	Augmented second	A2	tonus superflua
4	Major third	M3	ditonus
4	Diminished fourth	d4	semidiatessaron
5	Perfect fourth	P4	diatessaron
5	Augmented third	A3	ditonus superflua
6	Diminished fifth	d5	semidiapente, semitritonus
6	Augmented fourth	A4	tritonus
7	Perfect fifth	P5	diapente
7	Diminished sixth	d6	semihexachordum
8	Minor sixth	m6	hexachordum minus, semitonus maius cum diapente, tetratonus
8	Augmented fifth	A5	diapente superflua
9	Major sixth	M6	hexachordum maius, tonus cum diapente
9	Diminished seventh	d7	semiheptachordum
10	Minor seventh	m7	heptachordum minus, semiditonus cum diapente, pentatonus
10	Augmented sixth	A6	hexachordum superflua
11	Major seventh	M7	heptachordum maius, ditonus cum diapente
11	Diminished octave	d8	semidiapason
12	Perfect octave	P8	diapason
12	Augmented seventh	A7	heptachordum superflua
"""

val intervalAliases = """
M9,M2
m9,m2
P11,P4
""".trimIndent()