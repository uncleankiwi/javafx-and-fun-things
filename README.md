# javafx-and-fun-things

### Jfx Clock
It ticks.

![JfxClock preview](https://github.com/uncleankiwi/javafx-and-fun-things/blob/master/previews/JfxClock.PNG)

### RichTextGradient
A JavaFX rehash of an earlier VB.NET application. Fleshed out a bit more.

![RichTextGradient preview](https://github.com/uncleankiwi/javafx-and-fun-things/blob/master/previews/RTG.PNG)

- Applies a gradient of colours to a given string of text and outputs rich text.

- There are a number of algorithms available: RGB, HSB, and an approximation of CMYK.

![RichTextGradient swatches comparison](https://github.com/uncleankiwi/javafx-and-fun-things/blob/master/previews/RTGswatches.png)

Above: a comparison of results. From top to bottom: RGB, HSB, and CMYK.

- RGB tends to produce more muted colours in between as they are more likely to pass through grays.

- HSB results in more colourful gradients.

- CMYK is in between the other two.

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

### Mastermind
Playable. Easy.

![Mastermind preview](https://github.com/uncleankiwi/javafx-and-fun-things/blob/master/previews/Mastermind.PNG)

### DiceBinomial
Draws the binomial distribution of results from rolling d number of 6-sided dice.

Also gives the probability of rolling a desired result r.

![Mastermind preview](https://github.com/uncleankiwi/javafx-and-fun-things/blob/master/previews/DiceBinomial.PNG)

### Pi Estimator
This contains:
1. a method for generating fractions that are decent estimates of pi, e, or any other number in the 0 < n < 10 range,
2. a 'closeness' grading system that grades how good those estimates are of that number,
3. and an interactive GUI that graphs the estimates by their closeness.

The number of digits refers to the total number of digits used in the fraction estimating the number. For
example:
- 3 uses 1 digit
- 4/1 uses 2 digits
- 22/7 uses 3 digits

The 'closeness' measure shows how many digits of that estimate are correct, and how close the first wrong digit is to being correct.
Negative values are underestimates of the number, and positive values are overestimates.

Screenshots of an older version:

![Pi estimator preview 1](https://github.com/uncleankiwi/javafx-and-fun-things/blob/master/previews/PiEstimator1.PNG)

![Pi estimator preview 2](https://github.com/uncleankiwi/javafx-and-fun-things/blob/master/previews/PiEstimator2.PNG)