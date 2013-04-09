/* C++ code produced by gperf version 3.0.1 */
/* Command-line: gperf -d -7 -l -D -L C++ token.gperf  */
/* Computed positions: -k'1,3,12,$' */

#if !((' ' == 32) && ('!' == 33) && ('"' == 34) && ('#' == 35) \
      && ('%' == 37) && ('&' == 38) && ('\'' == 39) && ('(' == 40) \
      && (')' == 41) && ('*' == 42) && ('+' == 43) && (',' == 44) \
      && ('-' == 45) && ('.' == 46) && ('/' == 47) && ('0' == 48) \
      && ('1' == 49) && ('2' == 50) && ('3' == 51) && ('4' == 52) \
      && ('5' == 53) && ('6' == 54) && ('7' == 55) && ('8' == 56) \
      && ('9' == 57) && (':' == 58) && (';' == 59) && ('<' == 60) \
      && ('=' == 61) && ('>' == 62) && ('?' == 63) && ('A' == 65) \
      && ('B' == 66) && ('C' == 67) && ('D' == 68) && ('E' == 69) \
      && ('F' == 70) && ('G' == 71) && ('H' == 72) && ('I' == 73) \
      && ('J' == 74) && ('K' == 75) && ('L' == 76) && ('M' == 77) \
      && ('N' == 78) && ('O' == 79) && ('P' == 80) && ('Q' == 81) \
      && ('R' == 82) && ('S' == 83) && ('T' == 84) && ('U' == 85) \
      && ('V' == 86) && ('W' == 87) && ('X' == 88) && ('Y' == 89) \
      && ('Z' == 90) && ('[' == 91) && ('\\' == 92) && (']' == 93) \
      && ('^' == 94) && ('_' == 95) && ('a' == 97) && ('b' == 98) \
      && ('c' == 99) && ('d' == 100) && ('e' == 101) && ('f' == 102) \
      && ('g' == 103) && ('h' == 104) && ('i' == 105) && ('j' == 106) \
      && ('k' == 107) && ('l' == 108) && ('m' == 109) && ('n' == 110) \
      && ('o' == 111) && ('p' == 112) && ('q' == 113) && ('r' == 114) \
      && ('s' == 115) && ('t' == 116) && ('u' == 117) && ('v' == 118) \
      && ('w' == 119) && ('x' == 120) && ('y' == 121) && ('z' == 122) \
      && ('{' == 123) && ('|' == 124) && ('}' == 125) && ('~' == 126))
/* The character set is not based on ISO-646.  */
#error "gperf generated tables don't work with this execution character set. Please report a bug to <bug-gnu-gperf@gnu.org>."
#endif


#define TOTAL_KEYWORDS 61
#define MIN_WORD_LENGTH 5
#define MAX_WORD_LENGTH 21
#define MIN_HASH_VALUE 10
#define MAX_HASH_VALUE 111
/* maximum key range = 102, duplicates = 0 */

class Perfect_Hash
{
private:
  static inline unsigned int hash (const char *str, unsigned int len);
public:
  static const char *in_word_set (const char *str, unsigned int len);
};

inline unsigned int
Perfect_Hash::hash (register const char *str, register unsigned int len)
{
  static unsigned char asso_values[] =
    {
      112, 112, 112, 112, 112, 112, 112, 112, 112, 112,
      112, 112, 112, 112, 112, 112, 112, 112, 112, 112,
      112, 112, 112, 112, 112, 112, 112, 112, 112, 112,
      112, 112, 112, 112, 112, 112, 112, 112, 112, 112,
      112, 112, 112, 112, 112,  45, 112, 112, 112, 112,
      112, 112, 112, 112, 112, 112, 112, 112, 112, 112,
      112, 112, 112, 112, 112, 112, 112, 112, 112, 112,
      112, 112, 112, 112, 112, 112, 112, 112, 112, 112,
      112, 112, 112, 112, 112, 112, 112, 112, 112, 112,
      112, 112, 112, 112, 112, 112, 112,  52,   5,   0,
        0,  15,  40,  20,   0,  40, 112,  15,   0,  20,
        5,   0,   5, 112,   5,  40,   0, 112,  15,  45,
       30,  36, 112, 112, 112, 112, 112, 112
    };
  register int hval = len;

  switch (hval)
    {
      default:
        hval += asso_values[(unsigned char)str[11]];
      /*FALLTHROUGH*/
      case 11:
      case 10:
      case 9:
      case 8:
      case 7:
      case 6:
      case 5:
      case 4:
      case 3:
        hval += asso_values[(unsigned char)str[2]];
      /*FALLTHROUGH*/
      case 2:
      case 1:
        hval += asso_values[(unsigned char)str[0]];
        break;
    }
  return hval + asso_values[(unsigned char)str[len - 1]];
}

const char *
Perfect_Hash::in_word_set (register const char *str, register unsigned int len)
{
  static unsigned char lengthtable[] =
    {
       5, 11, 11, 12, 13,  9, 11, 17, 16, 17, 18, 19, 10, 16,
      18, 19, 11, 12, 14, 10, 11, 18, 19, 10,  6, 12,  5, 16,
       7, 11, 12, 13, 21, 17, 13, 14, 16, 21,  8,  9, 10, 16,
      17, 13, 14, 16, 17, 21, 14, 16,  7, 11, 17, 14,  6, 11,
      17, 16, 14, 12, 11
    };
  static const char * wordlist[] =
    {
      "color" /* hash value = 10, index = 0 */,
      "data-format" /* hash value = 11, index = 1 */,
      "line-height" /* hash value = 16, index = 2 */,
      "padding-left" /* hash value = 17, index = 3 */,
      "padding-right" /* hash value = 18, index = 4 */,
      "direction" /* hash value = 19, index = 5 */,
      "padding-top" /* hash value = 21, index = 6 */,
      "background-height" /* hash value = 22, index = 7 */,
      "background-color" /* hash value = 26, index = 8 */,
      "background-repeat" /* hash value = 27, index = 9 */,
      "border-right-width" /* hash value = 28, index = 10 */,
      "border-bottom-width" /* hash value = 29, index = 11 */,
      "can-shrink" /* hash value = 30, index = 12 */,
      "border-top-color" /* hash value = 31, index = 13 */,
      "border-right-color" /* hash value = 33, index = 14 */,
      "border-bottom-color" /* hash value = 34, index = 15 */,
      "margin-left" /* hash value = 36, index = 16 */,
      "margin-right" /* hash value = 37, index = 17 */,
      "padding-bottom" /* hash value = 39, index = 18 */,
      "margin-top" /* hash value = 40, index = 19 */,
      "text-indent" /* hash value = 41, index = 20 */,
      "border-right-style" /* hash value = 43, index = 21 */,
      "border-bottom-style" /* hash value = 44, index = 22 */,
      "text-align" /* hash value = 45, index = 23 */,
      "height" /* hash value = 46, index = 24 */,
      "number-align" /* hash value = 47, index = 25 */,
      "width" /* hash value = 50, index = 26 */,
      "text-linethrough" /* hash value = 51, index = 27 */,
      "orphans" /* hash value = 52, index = 28 */,
      "font-weight" /* hash value = 56, index = 29 */,
      "font-variant" /* hash value = 57, index = 30 */,
      "margin-bottom" /* hash value = 58, index = 31 */,
      "background-position-x" /* hash value = 61, index = 32 */,
      "page-break-before" /* hash value = 62, index = 33 */,
      "text-overline" /* hash value = 63, index = 34 */,
      "text-transform" /* hash value = 64, index = 35 */,
      "background-width" /* hash value = 66, index = 36 */,
      "background-position-y" /* hash value = 67, index = 37 */,
      "overflow" /* hash value = 68, index = 38 */,
      "font-size" /* hash value = 69, index = 39 */,
      "font-style" /* hash value = 70, index = 40 */,
      "border-top-width" /* hash value = 71, index = 41 */,
      "border-left-width" /* hash value = 72, index = 42 */,
      "show-if-blank" /* hash value = 73, index = 43 */,
      "letter-spacing" /* hash value = 74, index = 44 */,
      "background-image" /* hash value = 76, index = 45 */,
      "border-left-color" /* hash value = 77, index = 46 */,
      "background-attachment" /* hash value = 78, index = 47 */,
      "vertical-align" /* hash value = 79, index = 48 */,
      "border-top-style" /* hash value = 81, index = 49 */,
      "display" /* hash value = 83, index = 50 */,
      "master-page" /* hash value = 86, index = 51 */,
      "border-left-style" /* hash value = 87, index = 52 */,
      "visible-format" /* hash value = 89, index = 53 */,
      "widows" /* hash value = 91, index = 54 */,
      "font-family" /* hash value = 92, index = 55 */,
      "page-break-inside" /* hash value = 97, index = 56 */,
      "page-break-after" /* hash value = 98, index = 57 */,
      "text-underline" /* hash value = 99, index = 58 */,
      "word-spacing" /* hash value = 102, index = 59 */,
      "white-space" /* hash value = 111, index = 60 */
    };

  static signed char lookup[] =
    {
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0,  1, -1, -1,
      -1, -1,  2,  3,  4,  5, -1,  6,  7, -1, -1, -1,  8,  9,
      10, 11, 12, 13, -1, 14, 15, -1, 16, 17, -1, 18, 19, 20,
      -1, 21, 22, 23, 24, 25, -1, -1, 26, 27, 28, -1, -1, -1,
      29, 30, 31, -1, -1, 32, 33, 34, 35, -1, 36, 37, 38, 39,
      40, 41, 42, 43, 44, -1, 45, 46, 47, 48, -1, 49, -1, 50,
      -1, -1, 51, 52, -1, 53, -1, 54, 55, -1, -1, -1, -1, 56,
      57, 58, -1, -1, 59, -1, -1, -1, -1, -1, -1, -1, -1, 60
    };

  if (len <= MAX_WORD_LENGTH && len >= MIN_WORD_LENGTH)
    {
      register int key = hash (str, len);

      if (key <= MAX_HASH_VALUE && key >= 0)
        {
          register int index = lookup[key];

          if (index >= 0)
            {
              if (len == lengthtable[index])
                {
                  register const char *s = wordlist[index];

                  if (*str == *s && !memcmp (str + 1, s + 1, len - 1))
                    return s;
                }
            }
        }
    }
  return 0;
}
