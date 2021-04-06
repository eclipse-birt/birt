
package org.eclipse.birt.report.engine.layout.pdf.font;

import junit.framework.TestCase;

public class CharSegmentTest extends TestCase {

	public void testMerge() {
		// empty merge
		assertEquals("",
				toString(CharSegment.merge(new CharSegment[][] { new CharSegment[] {}, new CharSegment[] {} })));
		// source only merge
		assertEquals("1[A]", toString(CharSegment.merge(
				new CharSegment[][] { new CharSegment[] { new CharSegment(1, 1, "A") }, new CharSegment[] {} })));
		// target only merge
		assertEquals("1[B]", toString(CharSegment.merge(
				new CharSegment[][] { new CharSegment[] {}, new CharSegment[] { new CharSegment(1, 1, "B") } })));
		// 1:--2345678
		// 2:01-3-56-8901
		assertEquals("0-1[B],2-8[A],9-11[B]",
				toString(CharSegment.merge(new CharSegment[][] { new CharSegment[] { new CharSegment(2, 8, "A") },
						new CharSegment[] { new CharSegment(0, 1, "B"), new CharSegment(3, 3, "B"),
								new CharSegment(5, 6, "B"), new CharSegment(8, 11, "B"), } })));
		// 1:01-3-56-8901
		// 2:--2345678
		assertEquals("0-1[A],2[B],3[A],4[B],5-6[A],7[B],8-11[A]",
				toString(CharSegment.merge(new CharSegment[][] {
						new CharSegment[] { new CharSegment(0, 1, "A"), new CharSegment(3, 3, "A"),
								new CharSegment(5, 6, "A"), new CharSegment(8, 11, "A") },
						new CharSegment[] { new CharSegment(2, 8, "B") } })));
	}

	public void testSearch() {
		CharSegment[] segs = new CharSegment[] { new CharSegment(0, 1, "0-1"), new CharSegment(2, 2, "2"),
				new CharSegment(9, 11, "9-11"), new CharSegment(100, 100, "100") };
		assertEquals(-1, CharSegment.search(segs, -1));
		assertEquals(0, CharSegment.search(segs, 0));
		assertEquals(0, CharSegment.search(segs, 1));
		assertEquals(1, CharSegment.search(segs, 2));
		assertEquals(-1, CharSegment.search(segs, 3));
		assertEquals(2, CharSegment.search(segs, 9));
		assertEquals(2, CharSegment.search(segs, 10));
		assertEquals(2, CharSegment.search(segs, 11));
		assertEquals(-1, CharSegment.search(segs, 99));
		assertEquals(3, CharSegment.search(segs, 100));
		assertEquals(-1, CharSegment.search(segs, 101));
	}

	public void testSort() {
		CharSegment[] segs = new CharSegment[] { new CharSegment(10, 100, "10-100"), new CharSegment(4, 4, "4"),
				new CharSegment(7, 10, "7-10"), new CharSegment(0, 2, "0-2") };
		CharSegment.sort(segs);
		assertEquals("0-2[0-2],4[4],7-10[7-10],10-100[10-100]", toString(segs));

	}

	String toString(CharSegment[] segs) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < segs.length; i++) {
			buffer.append(segs[i].toString());
			buffer.append(",");
		}
		if (buffer.length() > 0) {
			buffer.setLength(buffer.length() - 1);
		}
		return buffer.toString();
	}

}
