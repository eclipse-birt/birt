/* C++ code produced by gperf version 3.0.1 */
/* Command-line: gperf -d -7 -l -D -L C++ token.gperf  */
/* Computed positions: -k'3,6,12,$' */

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


#define TOTAL_KEYWORDS 60
#define MIN_WORD_LENGTH 5
#define MAX_WORD_LENGTH 21
#define MIN_HASH_VALUE 9
#define MAX_HASH_VALUE 107
/* maximum key range = 99, duplicates = 0 */

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
      108, 108, 108, 108, 108, 108, 108, 108, 108, 108,
      108, 108, 108, 108, 108, 108, 108, 108, 108, 108,
      108, 108, 108, 108, 108, 108, 108, 108, 108, 108,
      108, 108, 108, 108, 108, 108, 108, 108, 108, 108,
      108, 108, 108, 108, 108,  45, 108, 108, 108, 108,
      108, 108, 108, 108, 108, 108, 108, 108, 108, 108,
      108, 108, 108, 108, 108, 108, 108, 108, 108, 108,
      108, 108, 108, 108, 108, 108, 108, 108, 108, 108,
      108, 108, 108, 108, 108, 108, 108, 108, 108, 108,
      108, 108, 108, 108, 108, 108, 108,  20,  20,  10,
       35,   5,  47,  40,  20,  25, 108,   5,  20,  20,
        0,   0,  35, 108,   0,   0,   0,   5,   5,  61,
       10,  25, 108, 108, 108, 108, 108, 108
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
        hval += asso_values[(unsigned char)str[5]];
      /*FALLTHROUGH*/
      case 5:
      case 4:
      case 3:
        hval += asso_values[(unsigned char)str[2]];
        break;
    }
  return hval + asso_values[(unsigned char)str[len - 1]];
}

const char *
Perfect_Hash::in_word_set (register const char *str, register unsigned int len)
{
  static unsigned char lengthtable[] =
    {
       9, 11, 12,  9, 10, 11, 12, 18, 19, 16, 18, 19,  5, 16,
      17, 13, 11, 12, 13, 10, 16, 18, 19, 10,  6,  7, 13, 14,
      10, 11, 12, 14, 21,  7, 13, 14, 15, 16, 11, 14, 15, 17,
      16, 17, 13, 14, 11, 13, 21, 14, 11, 17, 11, 11, 21, 12,
      16, 16, 17, 17
    };
  static const char * wordlist[] =
    {
      "direction" /* hash value = 9, index = 0 */,
      "margin-left" /* hash value = 11, index = 1 */,
      "margin-right" /* hash value = 12, index = 2 */,
      "font-size" /* hash value = 14, index = 3 */,
      "font-style" /* hash value = 15, index = 4 */,
      "master-page" /* hash value = 16, index = 5 */,
      "font-variant" /* hash value = 17, index = 6 */,
      "border-right-color" /* hash value = 18, index = 7 */,
      "border-bottom-color" /* hash value = 19, index = 8 */,
      "border-top-style" /* hash value = 21, index = 9 */,
      "border-right-style" /* hash value = 23, index = 10 */,
      "border-bottom-style" /* hash value = 24, index = 11 */,
      "color" /* hash value = 25, index = 12 */,
      "border-top-color" /* hash value = 26, index = 13 */,
      "background-repeat" /* hash value = 27, index = 14 */,
      "text-overline" /* hash value = 28, index = 15 */,
      "line-height" /* hash value = 31, index = 16 */,
      "number-align" /* hash value = 32, index = 17 */,
      "margin-bottom" /* hash value = 33, index = 18 */,
      "can-shrink" /* hash value = 35, index = 19 */,
      "background-color" /* hash value = 36, index = 20 */,
      "border-right-width" /* hash value = 38, index = 21 */,
      "border-bottom-width" /* hash value = 39, index = 22 */,
      "text-align" /* hash value = 40, index = 23 */,
      "widows" /* hash value = 41, index = 24 */,
      "orphans" /* hash value = 42, index = 25 */,
      "show-if-blank" /* hash value = 43, index = 26 */,
      "text-transform" /* hash value = 44, index = 27 */,
      "margin-top" /* hash value = 45, index = 28 */,
      "text-indent" /* hash value = 46, index = 29 */,
      "padding-left" /* hash value = 47, index = 30 */,
      "vertical-align" /* hash value = 49, index = 31 */,
      "background-attachment" /* hash value = 51, index = 32 */,
      "display" /* hash value = 52, index = 33 */,
      "number-format" /* hash value = 53, index = 34 */,
      "visible-format" /* hash value = 54, index = 35 */,
      "sql-date-format" /* hash value = 55, index = 36 */,
      "background-image" /* hash value = 56, index = 37 */,
      "date-format" /* hash value = 58, index = 38 */,
      "text-underline" /* hash value = 59, index = 39 */,
      "sql-time-format" /* hash value = 60, index = 40 */,
      "border-left-color" /* hash value = 62, index = 41 */,
      "text-linethrough" /* hash value = 66, index = 42 */,
      "border-left-style" /* hash value = 67, index = 43 */,
      "padding-right" /* hash value = 68, index = 44 */,
      "padding-bottom" /* hash value = 69, index = 45 */,
      "font-weight" /* hash value = 72, index = 46 */,
      "string-format" /* hash value = 73, index = 47 */,
      "background-position-x" /* hash value = 76, index = 48 */,
      "letter-spacing" /* hash value = 79, index = 49 */,
      "padding-top" /* hash value = 81, index = 50 */,
      "border-left-width" /* hash value = 82, index = 51 */,
      "font-family" /* hash value = 83, index = 52 */,
      "white-space" /* hash value = 86, index = 53 */,
      "background-position-y" /* hash value = 91, index = 54 */,
      "word-spacing" /* hash value = 92, index = 55 */,
      "page-break-after" /* hash value = 96, index = 56 */,
      "border-top-width" /* hash value = 97, index = 57 */,
      "page-break-before" /* hash value = 102, index = 58 */,
      "page-break-inside" /* hash value = 107, index = 59 */
    };

  static signed char lookup[] =
    {
      -1, -1, -1, -1, -1, -1, -1, -1, -1,  0, -1,  1,  2, -1,
       3,  4,  5,  6,  7,  8, -1,  9, -1, 10, 11, 12, 13, 14,
      15, -1, -1, 16, 17, 18, -1, 19, 20, -1, 21, 22, 23, 24,
      25, 26, 27, 28, 29, 30, -1, 31, -1, 32, 33, 34, 35, 36,
      37, -1, 38, 39, 40, -1, 41, -1, -1, -1, 42, 43, 44, 45,
      -1, -1, 46, 47, -1, -1, 48, -1, -1, 49, -1, 50, 51, 52,
      -1, -1, 53, -1, -1, -1, -1, 54, 55, -1, -1, -1, 56, 57,
      -1, -1, -1, -1, 58, -1, -1, -1, -1, 59
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
