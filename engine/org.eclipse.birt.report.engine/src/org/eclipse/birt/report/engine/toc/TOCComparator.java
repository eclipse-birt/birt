
package org.eclipse.birt.report.engine.toc;

import java.util.Comparator;

/**
 * node id has two version:
 * 
 * <li>__TOC_[number]
 * 
 * <li>__TOC_[number]_[number]
 * 
 * 
 */
public class TOCComparator implements Comparator<String> {

	/** length of the "__TOC_", 6 */
	static final int PREFIX_LENGTH = 6;

	public int compare(String o1, String o2) {
		int length1 = o1.length();
		int length2 = o2.length();

		if (length1 <= 6) {
			return -1;
		}

		if (length2 <= 6) {
			return 1;
		}

		int offset = PREFIX_LENGTH;
		while (offset < length1 && offset < length2) {
			char ch1 = o1.charAt(offset);
			char ch2 = o2.charAt(offset);
			if (ch1 == ch2) {
				offset++;
				continue;
			}
			if (ch1 == '_') {
				return -1;
			}
			if (ch2 == '_') {
				return 1;
			}
			// now the ch1 and ch2 are different digit
			int remainDigits1 = getCharsTillEnd(o1, offset + 1);
			int remainDigits2 = getCharsTillEnd(o2, offset + 1);
			if (remainDigits1 < remainDigits2) {
				return -1;
			}
			if (remainDigits1 > remainDigits2) {
				return 1;
			}
			if (ch1 < ch2) {
				return -1;
			}
			return 1;
		}
		// o1 and o2 are same till one of it has been finished
		if (length1 < length2) {
			return -1;
		}
		if (length1 > length2) {
			return 1;
		}
		return 0;
	}

	int getCharsTillEnd(String o, int offset) {
		int length = o.length();
		int chars = offset;
		while (chars < length) {
			char ch = o.charAt(chars);
			if (ch == '_') {
				return chars - offset;
			}
			chars++;
		}
		return length - offset;
	}
}
