# javafx-and-fun-things

### Geothmetic Meandian
Original idea: [xkcd](https://xkcd.com/2435/)

- Given a series of numbers
    - `(x1, x2, ..., xn)`
- and the following function,
   - `F(x1, x2, ..., xn) = (arithmeticMean, geometricMean, median)`
- this attempts to apply `F()` to the result recursively
    - `GMMD(x1, x2, ... , xn) = F(F(...F(x1, x2, ..., xn))`
- until `x1, x2, x3` converges.
- e.g. `GMMD(1, 1, 2, 3, 5) = 2.089`

![GeothmeticMeandian preview](https://github.com/uncleankiwi/javafx-and-fun-things/blob/master/previews/geothmeticmeandian.PNG)