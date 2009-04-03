/* C++ code produced by gperf version 3.0.3 */
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


#define TOTAL_KEYWORDS 62
#define MIN_WORD_LENGTH 5
#define MAX_WORD_LENGTH 21
#define MIN_HASH_VALUE 9
#define MAX_HASH_VALUE 121
/* maximum key range = 113, duplicates = 0 */

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
      122, 122, 122, 122, 122,  50, 122, 122, 122, 122,
      122, 122, 122, 122, 122, 122, 122, 122, 122, 122,
      122, 122, 122, 122, 122, 122, 122, 122, 122, 122,
      122, 122, 122, 122, 122, 122, 122, 122, 122, 122,
      122, 122, 122, 122, 122, 122, 122, 122, 122, 122,
      122, 122, 122, 122, 122, 122, 122,  10,  30,  10,
       45,   5,  11,  25,  20,  25, 122,  20,  20,  20,
        0,   0,  30, 122,   0,   0,   0,   5,   1,   6,
       10,  60, 122, 122, 122, 122, 122, 122
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
       9, 11, 12, 12,  9, 10, 11, 11, 18, 19, 16, 11, 18, 19,
       5, 16, 17, 13, 10, 11, 12, 13, 16,  7, 18, 19, 10, 21,
      16, 13, 14, 15, 11, 17, 13, 14, 10,  6, 16, 14, 16, 12,
      13, 14, 15, 12, 14, 16, 17, 21, 17,  7, 13, 14, 16, 11,
      11, 17, 11, 17, 17, 21
    };
  static const char * wordlist[] =
    {
      "direction" /* hash value = 9, index = 0 */,
      "margin-left" /* hash value = 11, index = 1 */,
      "margin-right" /* hash value = 12, index = 2 */,
      "font-variant" /* hash value = 13, index = 3 */,
      "font-size" /* hash value = 14, index = 4 */,
      "font-style" /* hash value = 15, index = 5 */,
      "master-page" /* hash value = 16, index = 6 */,
      "font-weight" /* hash value = 17, index = 7 */,
      "border-right-color" /* hash value = 18, index = 8 */,
      "border-bottom-color" /* hash value = 19, index = 9 */,
      "border-top-style" /* hash value = 21, index = 10 */,
      "date-format" /* hash value = 22, index = 11 */,
      "border-right-style" /* hash value = 23, index = 12 */,
      "border-bottom-style" /* hash value = 24, index = 13 */,
      "color" /* hash value = 25, index = 14 */,
      "border-top-color" /* hash value = 26, index = 15 */,
      "background-repeat" /* hash value = 27, index = 16 */,
      "text-overline" /* hash value = 28, index = 17 */,
      "text-align" /* hash value = 30, index = 18 */,
      "line-height" /* hash value = 31, index = 19 */,
      "number-align" /* hash value = 32, index = 20 */,
      "margin-bottom" /* hash value = 33, index = 21 */,
      "background-color" /* hash value = 36, index = 22 */,
      "orphans" /* hash value = 37, index = 23 */,
      "border-right-width" /* hash value = 38, index = 24 */,
      "border-bottom-width" /* hash value = 39, index = 25 */,
      "margin-top" /* hash value = 40, index = 26 */,
      "background-attachment" /* hash value = 41, index = 27 */,
      "border-top-width" /* hash value = 42, index = 28 */,
      "number-format" /* hash value = 43, index = 29 */,
      "text-transform" /* hash value = 44, index = 30 */,
      "sql-date-format" /* hash value = 45, index = 31 */,
      "text-indent" /* hash value = 46, index = 32 */,
      "background-height" /* hash value = 47, index = 33 */,
      "string-format" /* hash value = 48, index = 34 */,
      "vertical-align" /* hash value = 49, index = 35 */,
      "can-shrink" /* hash value = 50, index = 36 */,
      "widows" /* hash value = 51, index = 37 */,
      "background-width" /* hash value = 52, index = 38 */,
      "visible-format" /* hash value = 54, index = 39 */,
      "background-image" /* hash value = 56, index = 40 */,
      "padding-left" /* hash value = 57, index = 41 */,
      "show-if-blank" /* hash value = 58, index = 42 */,
      "text-underline" /* hash value = 59, index = 43 */,
      "sql-time-format" /* hash value = 60, index = 44 */,
      "word-spacing" /* hash value = 62, index = 45 */,
      "letter-spacing" /* hash value = 64, index = 46 */,
      "text-linethrough" /* hash value = 66, index = 47 */,
      "border-left-color" /* hash value = 67, index = 48 */,
      "background-position-x" /* hash value = 71, index = 49 */,
      "border-left-style" /* hash value = 72, index = 50 */,
      "display" /* hash value = 77, index = 51 */,
      "padding-right" /* hash value = 78, index = 52 */,
      "padding-bottom" /* hash value = 79, index = 53 */,
      "page-break-after" /* hash value = 81, index = 54 */,
      "font-family" /* hash value = 82, index = 55 */,
      "padding-top" /* hash value = 86, index = 56 */,
      "border-left-width" /* hash value = 87, index = 57 */,
      "white-space" /* hash value = 91, index = 58 */,
      "page-break-inside" /* hash value = 102, index = 59 */,
      "page-break-before" /* hash value = 107, index = 60 */,
      "background-position-y" /* hash value = 121, index = 61 */
    };

  static signed char lookup[] =
    {
      -1, -1, -1, -1, -1, -1, -1, -1, -1,  0, -1,  1,  2,  3,
       4,  5,  6,  7,  8,  9, -1, 10, 11, 12, 13, 14, 15, 16,
      17, -1, 18, 19, 20, 21, -1, -1, 22, 23, 24, 25, 26, 27,
      28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, -1, 39, -1,
      40, 41, 42, 43, 44, -1, 45, -1, 46, -1, 47, 48, -1, -1,
      -1, 49, 50, -1, -1, -1, -1, 51, 52, 53, -1, 54, 55, -1,
      -1, -1, 56, 57, -1, -1, -1, 58, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, 59, -1, -1, -1, -1, 60, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, 61
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
