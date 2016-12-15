/**
 * Used by Array.sort().
 *
 * katakana will follow hiragana.
 * What about intermixing the two? Makes indexing and searching
 * more complicated. ...
 */
Edict.sortByReading = function (entryA, entryB)  {

  // Some readings are the empty string "".
  // Empty string is less than non-empty string.
  // "" < "A" -> true
  // BUT!!
  // Some entries are mixed kana and kanji: <kana><kanji> [reading] / ... / ... /
  // These won't be sorted correctly by above simplification.
  // So, if a reading is empty, use its entry value for comparison.

  // There are duplicate readings. In that case sort by entry.


  var a = entryA[1];
  if (a == "")  a = entryA[0];
  var b = entryB[1];
  if (b == "")  b = entryB[0];
  if (a < b)  {
    return -1;
  }
  if (a > b)  {
    return 1;
  }
  // Entry is never null. Use as is.
  if (entryA[0] < entryB[0])  {
    return -1;
  }
  if (entryA[0] > entryB[0])  {
    return 1;
  }
  return 0;
}
