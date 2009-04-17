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


#define TOTAL_KEYWORDS 58
#define MIN_WORD_LENGTH 5
#define MAX_WORD_LENGTH 21
#define MIN_HASH_VALUE 11
#define MAX_HASH_VALUE 116
/* maximum key range = 106, duplicates = 0 */

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
      117, 117, 117, 117, 117, 117, 117, 117, 117, 117,
      117, 117, 117, 117, 117, 117, 117, 117, 117, 117,
      117, 117, 117, 117, 117, 117, 117, 117, 117, 117,
      117, 117, 117, 117, 117, 117, 117, 117, 117, 117,
      117, 117, 117, 117, 117,  60, 117, 117, 117, 117,
      117, 117, 117, 117, 117, 117, 117, 117, 117, 117,
      117, 117, 117, 117, 117, 117, 117, 117, 117, 117,
      117, 117, 117, 117, 117, 117, 117, 117, 117, 117,
      117, 117, 117, 117, 117, 117, 117, 117, 117, 117,
      117, 117, 117, 117, 117, 117, 117,  22,   0,  15,
        5,  20,  40,   0,   5,  30, 117,   5,   5,   0,
        5,  20,   5, 117,   0,  30,   0, 117,  35,  55,
       30,  45, 117, 117, 117, 117, 117, 117
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
      11, 12, 10, 11, 18,  9, 11, 12, 18, 14,  5, 11, 12, 13,
      16, 17, 13, 10, 17, 18, 19, 11, 17, 16, 19, 10, 16, 14,
      16, 11, 12, 21, 19, 11,  7, 14, 16, 12, 13, 21, 17, 13,
       9, 10, 16, 17, 14, 16, 17, 14, 21,  7, 16, 14,  6, 17,
      11, 11
    };
  static const char * wordlist[] =
    {
      "margin-left" /* hash value = 11, index = 0 */,
      "margin-right" /* hash value = 12, index = 1 */,
      "margin-top" /* hash value = 15, index = 2 */,
      "data-format" /* hash value = 16, index = 3 */,
      "border-right-color" /* hash value = 18, index = 4 */,
      "direction" /* hash value = 19, index = 5 */,
      "line-height" /* hash value = 21, index = 6 */,
      "padding-left" /* hash value = 22, index = 7 */,
      "border-right-width" /* hash value = 23, index = 8 */,
      "padding-bottom" /* hash value = 24, index = 9 */,
      "color" /* hash value = 25, index = 10 */,
      "padding-top" /* hash value = 26, index = 11 */,
      "number-align" /* hash value = 27, index = 12 */,
      "padding-right" /* hash value = 28, index = 13 */,
      "border-top-color" /* hash value = 31, index = 14 */,
      "background-repeat" /* hash value = 32, index = 15 */,
      "margin-bottom" /* hash value = 33, index = 16 */,
      "can-shrink" /* hash value = 35, index = 17 */,
      "background-height" /* hash value = 37, index = 18 */,
      "border-right-style" /* hash value = 38, index = 19 */,
      "border-bottom-color" /* hash value = 39, index = 20 */,
      "text-indent" /* hash value = 41, index = 21 */,
      "page-break-before" /* hash value = 42, index = 22 */,
      "page-break-after" /* hash value = 43, index = 23 */,
      "border-bottom-width" /* hash value = 44, index = 24 */,
      "text-align" /* hash value = 45, index = 25 */,
      "background-color" /* hash value = 46, index = 26 */,
      "letter-spacing" /* hash value = 49, index = 27 */,
      "text-linethrough" /* hash value = 51, index = 28 */,
      "font-weight" /* hash value = 56, index = 29 */,
      "font-variant" /* hash value = 57, index = 30 */,
      "background-attachment" /* hash value = 58, index = 31 */,
      "border-bottom-style" /* hash value = 59, index = 32 */,
      "master-page" /* hash value = 61, index = 33 */,
      "orphans" /* hash value = 62, index = 34 */,
      "text-transform" /* hash value = 64, index = 35 */,
      "border-top-style" /* hash value = 66, index = 36 */,
      "word-spacing" /* hash value = 67, index = 37 */,
      "text-overline" /* hash value = 68, index = 38 */,
      "background-position-x" /* hash value = 71, index = 39 */,
      "page-break-inside" /* hash value = 72, index = 40 */,
      "show-if-blank" /* hash value = 73, index = 41 */,
      "font-size" /* hash value = 74, index = 42 */,
      "font-style" /* hash value = 75, index = 43 */,
      "border-top-width" /* hash value = 76, index = 44 */,
      "border-left-color" /* hash value = 77, index = 45 */,
      "visible-format" /* hash value = 79, index = 46 */,
      "background-image" /* hash value = 81, index = 47 */,
      "border-left-width" /* hash value = 82, index = 48 */,
      "vertical-align" /* hash value = 84, index = 49 */,
      "background-position-y" /* hash value = 86, index = 50 */,
      "display" /* hash value = 87, index = 51 */,
      "background-width" /* hash value = 91, index = 52 */,
      "text-underline" /* hash value = 94, index = 53 */,
      "widows" /* hash value = 96, index = 54 */,
      "border-left-style" /* hash value = 97, index = 55 */,
      "font-family" /* hash value = 101, index = 56 */,
      "white-space" /* hash value = 116, index = 57 */
    };

  static signed char lookup[] =
    {
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0,  1, -1,
      -1,  2,  3, -1,  4,  5, -1,  6,  7,  8,  9, 10, 11, 12,
      13, -1, -1, 14, 15, 16, -1, 17, -1, 18, 19, 20, -1, 21,
      22, 23, 24, 25, 26, -1, -1, 27, -1, 28, -1, -1, -1, -1,
      29, 30, 31, 32, -1, 33, 34, -1, 35, -1, 36, 37, 38, -1,
      -1, 39, 40, 41, 42, 43, 44, 45, -1, 46, -1, 47, 48, -1,
      49, -1, 50, 51, -1, -1, -1, 52, -1, -1, 53, -1, 54, 55,
      -1, -1, -1, 56, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, 57
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
