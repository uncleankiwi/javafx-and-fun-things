# javafx-and-fun-things

###Geothmetic Meandian
Original idea: [xkcd](https://xkcd.com/2435/)

- Given a series of numbers:
    - (x1, x2, ..., xn)
- A function F() is defined such that
   - F(x1, x2, ..., xn) = (arithmeticMean, geometricMean, median)
- And another function GMMD() is such that
    - GMMD(x1, x2, ... , xn) = F(F(...F(x1, x2, ..., xn)))
    - until x1, x2, x3 converges.
- e.g. GMMD(1, 1, 2, 3, 5) = 2.089

![GeothmeticMeandian preview](https://github.com/uncleankiwi/javafx-and-fun-things/blob/master/previews/geothmeticmeandian.PNG)