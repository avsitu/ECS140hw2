# line 7 does not have a comparison operator for the for loop
# should give a parsing error

@a,b,c
a = 0
b = 2
{ c = 0 c &* 10 c = c + 1}
!c