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


#define TOTAL_KEYWORDS 59
#define MIN_WORD_LENGTH 5
#define MAX_WORD_LENGTH 21
#define MIN_HASH_VALUE 11
#define MAX_HASH_VALUE 121
/* maximum key range = 111, duplicates = 0 */

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
      122, 122, 122, 122, 122, 122, 122, 122, 122, 122,
      122, 122, 122, 122, 122, 122, 122, 122, 122, 122,
      122, 122, 122, 122, 122, 122, 122, 122, 122, 122,
      122, 122, 122, 122, 122, 122, 122, 122, 122, 122,
      122, 122, 122, 122, 122,  35, 122, 122, 122, 122,
      122, 122, 122, 122, 122, 122, 122, 122, 122, 122,
      122, 122, 122, 122, 122, 122, 122, 122, 122, 122,
      122, 122, 122, 122, 122, 122, 122, 122, 122, 122,
      122, 122, 122, 122, 122, 122, 122, 122, 122, 122,
      122, 122, 122, 122, 122, 122, 122,  60,   0,  15,
        5,  20,  45,  40,   5,  35, 122,   0,  27,   0,
        5,  20,   5, 122,   0,  45,   0, 122,  15,   0,
       30,  50, 122, 122, 122, 122, 122, 122
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
      11, 12, 10, 11, 18,  9, 16, 12, 18, 14, 11, 12, 13, 10,
      16, 17, 13, 16, 17, 18, 19, 11, 11, 19, 10, 16,  5,  8,
      16, 17,  6, 17, 19, 11, 12, 14, 11, 13, 14, 21, 17, 14,
      11,  7,  9, 10, 16, 17, 13, 16, 21, 12, 21, 14,  7, 11,
      14, 17, 16
    };
  static const char * wordlist[] =
    {
      "margin-left" /* hash value = 11, index = 0 */,
      "margin-right" /* hash value = 12, index = 1 */,
      "margin-top" /* hash value = 15, index = 2 */,
      "data-format" /* hash value = 16, index = 3 */,
      "border-right-color" /* hash value = 18, index = 4 */,
      "direction" /* hash value = 19, index = 5 */,
      "border-top-width" /* hash value = 21, index = 6 */,
      "padding-left" /* hash value = 22, index = 7 */,
      "border-right-width" /* hash value = 23, index = 8 */,
      "padding-bottom" /* hash value = 24, index = 9 */,
      "padding-top" /* hash value = 26, index = 10 */,
      "number-align" /* hash value = 27, index = 11 */,
      "padding-right" /* hash value = 28, index = 12 */,
      "can-shrink" /* hash value = 30, index = 13 */,
      "border-top-color" /* hash value = 31, index = 14 */,
      "background-repeat" /* hash value = 32, index = 15 */,
      "margin-bottom" /* hash value = 33, index = 16 */,
      "background-width" /* hash value = 36, index = 17 */,
      "background-height" /* hash value = 37, index = 18 */,
      "border-right-style" /* hash value = 38, index = 19 */,
      "border-bottom-color" /* hash value = 39, index = 20 */,
      "text-indent" /* hash value = 41, index = 21 */,
      "line-height" /* hash value = 43, index = 22 */,
      "border-bottom-width" /* hash value = 44, index = 23 */,
      "text-align" /* hash value = 45, index = 24 */,
      "background-color" /* hash value = 46, index = 25 */,
      "color" /* hash value = 47, index = 26 */,
      "overflow" /* hash value = 48, index = 27 */,
      "text-linethrough" /* hash value = 51, index = 28 */,
      "border-left-color" /* hash value = 52, index = 29 */,
      "widows" /* hash value = 56, index = 30 */,
      "border-left-width" /* hash value = 57, index = 31 */,
      "border-bottom-style" /* hash value = 59, index = 32 */,
      "font-weight" /* hash value = 61, index = 33 */,
      "font-variant" /* hash value = 62, index = 34 */,
      "text-transform" /* hash value = 64, index = 35 */,
      "white-space" /* hash value = 66, index = 36 */,
      "text-overline" /* hash value = 68, index = 37 */,
      "vertical-align" /* hash value = 69, index = 38 */,
      "background-position-x" /* hash value = 71, index = 39 */,
      "border-left-style" /* hash value = 72, index = 40 */,
      "visible-format" /* hash value = 74, index = 41 */,
      "master-page" /* hash value = 76, index = 42 */,
      "orphans" /* hash value = 77, index = 43 */,
      "font-size" /* hash value = 79, index = 44 */,
      "font-style" /* hash value = 80, index = 45 */,
      "border-top-style" /* hash value = 81, index = 46 */,
      "page-break-before" /* hash value = 82, index = 47 */,
      "show-if-blank" /* hash value = 83, index = 48 */,
      "background-image" /* hash value = 86, index = 49 */,
      "background-position-y" /* hash value = 91, index = 50 */,
      "word-spacing" /* hash value = 92, index = 51 */,
      "background-attachment" /* hash value = 96, index = 52 */,
      "text-underline" /* hash value = 99, index = 53 */,
      "display" /* hash value = 107, index = 54 */,
      "font-family" /* hash value = 111, index = 55 */,
      "letter-spacing" /* hash value = 116, index = 56 */,
      "page-break-inside" /* hash value = 117, index = 57 */,
      "page-break-after" /* hash value = 121, index = 58 */
    };

  static signed char lookup[] =
    {
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0,  1, -1,
      -1,  2,  3, -1,  4,  5, -1,  6,  7,  8,  9, -1, 10, 11,
      12, -1, 13, 14, 15, 16, -1, -1, 17, 18, 19, 20, -1, 21,
      -1, 22, 23, 24, 25, 26, 27, -1, -1, 28, 29, -1, -1, -1,
      30, 31, -1, 32, -1, 33, 34, -1, 35, -1, 36, -1, 37, 38,
      -1, 39, 40, -1, 41, -1, 42, 43, -1, 44, 45, 46, 47, 48,
      -1, -1, 49, -1, -1, -1, -1, 50, 51, -1, -1, -1, 52, -1,
      -1, 53, -1, -1, -1, -1, -1, -1, -1, 54, -1, -1, -1, 55,
      -1, -1, -1, -1, 56, 57, -1, -1, -1, 58
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
