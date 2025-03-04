/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.layout.pdf.font;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Class of character segment
 *
 * @since 3.3
 *
 */
public class CharSegment {

	int start;
	int end;
	String name;

	/**
	 * Constructor 1
	 *
	 * @param start segment start
	 * @param end   segment end
	 * @param name  segment name
	 */
	public CharSegment(int start, int end, String name) {
		this.start = start;
		this.end = end;
		this.name = name;
	}

	/**
	 * Constructor 2
	 *
	 * @param seg character segment
	 */
	public CharSegment(CharSegment seg) {
		this.start = seg.start;
		this.end = seg.end;
		this.name = seg.name;
	}

	/**
	 * Get the segment start
	 *
	 * @return the segment start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * Get the segment end
	 *
	 * @return the segment end
	 */
	public int getEnd() {
		return end;
	}

	/**
	 * Search method
	 *
	 * @param segments character segments
	 * @param ch       character index
	 * @return index of segment
	 */
	public static int search(CharSegment[] segments, int ch) {
		int index = Arrays.binarySearch(segments, Integer.valueOf(ch), new SearchingComparator());
		if (index < 0) {
			return -1;
		}
		return index;
	}

	/**
	 * Merge the two segments, the mergedSegs must be sorted
	 *
	 * @param mergedSegs segments to be merged
	 * @return merged character segments
	 */
	public static CharSegment[] merge(CharSegment[][] mergedSegs) {
		ArrayList<CharSegment> list = new ArrayList<CharSegment>();
		for (int i = 0; i < mergedSegs.length; i++) {
			list = merge(list, mergedSegs[i]);
		}
		return list.toArray(new CharSegment[] {});
	}

	/**
	 * Merge segments
	 *
	 * @param segments segment list
	 * @return get character segment array
	 */
	public static CharSegment[] merge(ArrayList<CharSegment[]> segments) {
		ArrayList<CharSegment> list = new ArrayList<CharSegment>();
		for (int i = 0; i < segments.size(); i++) {
			list = merge(list, segments.get(i));
		}
		return list.toArray(new CharSegment[] {});
	}

	/**
	 * Normalize method
	 *
	 * @param segs character segments
	 * @return character segments array
	 */
	public static CharSegment[] normalize(CharSegment[] segs) {
		CharSegment.sort(segs);
		if (segs.length < 2) {
			return segs;
		}
		ArrayList<CharSegment> list = new ArrayList<CharSegment>();

		CharSegment prev = segs[0];
		for (int i = 1; i < segs.length; i++) {
			CharSegment next = segs[i];
			if (prev.start == next.start) {
				if (prev.end < next.end) {
					prev.end = next.end;
				}
			} else {
				list.add(prev);
				prev = next;
			}
		}
		list.add(prev);
		return list.toArray(new CharSegment[] {});
	}

	protected static ArrayList<CharSegment> merge(ArrayList<CharSegment> src1, CharSegment[] src2) {
		ArrayList<CharSegment> tgt = new ArrayList<CharSegment>(src1.size());

		int index1 = 0;
		int index2 = 0;
		int start1 = -1;
		int end1 = 0;

		for (index1 = 0; index1 < src1.size(); index1++) {
			CharSegment seg1 = src1.get(index1);
			end1 = seg1.start - 1;
			// now we need find segments in the gap [start1, end1]
			while (index2 < src2.length) {
				CharSegment seg2 = src2[index2];
				int start2 = seg2.start;
				int end2 = seg2.end;

				// the segment is before the gap
				// __________S1_____E1___
				// _S2___E2___
				if (end2 < start1) {
					index2++;
					continue;
				}

				// the segment is after the gap
				// __S1____E1______
				// ___________S2___E2__
				if (start2 > end1) {
					break;
				}
				// find a segment in the gap, insert it into the list
				if (start2 >= start1) {
					if (end2 <= end1) {
						// _S1________E1__
						// ___S2___E2_____
						tgt.add(seg2);
						index2++;
						continue;
					}
					// _S1________E1__
					// _____S2_______E2
					tgt.add(new CharSegment(start2, end1, seg2.name));
					break;
				} else if (end2 <= end1) {
					// ____S1____E1__
					// __S2____E2____
					tgt.add(new CharSegment(start1, end2, seg2.name));
					index2++;
					continue;
				} else {
					// _____S1____E1____
					// __S2__________E2_
					tgt.add(new CharSegment(start1, end1, seg2.name));
					break;
				}
			}
			// insert the seg1
			tgt.add(seg1);
			start1 = seg1.end + 1;
		}

		// insert the remain segments after [start1, ]
		for (; index2 < src2.length; index2++) {
			CharSegment seg2 = src2[index2];
			int start2 = seg2.start;
			int end2 = seg2.end;
			if (end2 >= start1) {
				if (start2 >= start1) {
					// _S1___________
					// ____S2___E2___
					tgt.add(seg2);
				} else {
					// ____S1_______
					// S2______E2__
					tgt.add(new CharSegment(start1, end2, seg2.name));
				}
			}
		}
		return tgt;
	}

	/**
	 * Sort character segment
	 *
	 * @param segments character segment
	 */
	public static void sort(CharSegment[] segments) {
		Arrays.sort(segments, new SortingComparator());
	}

	static class SortingComparator implements Comparator<Object> {

		@Override
		public int compare(Object arg0, Object arg1) {
			CharSegment seg0 = (CharSegment) arg0;
			CharSegment seg1 = (CharSegment) arg1;
			return seg0.start - seg1.start;
		}
	}

	static class SearchingComparator implements Comparator<Object> {

		@Override
		public int compare(Object arg0, Object arg1) {
			CharSegment seg = (CharSegment) arg0;
			int c = ((Integer) arg1).intValue();
			if (seg.end < c) {
				return -1;
			}
			if (seg.start > c) {
				return 1;
			}
			return 0;
		}
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		if (start == end) {
			buffer.append(start);
		} else {
			buffer.append(start);
			buffer.append('-');
			buffer.append(end);
		}
		buffer.append('[');
		buffer.append(name);
		buffer.append(']');
		return buffer.toString();
	}

}
