/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.internal.executor.doc;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

import org.eclipse.birt.report.engine.internal.document.v4.CascadingComparator;

public class Fragment {

	Object index;
	Segment segment;
	Fragment next;
	Fragment child;
	Comparator comparator;

	static private class Section {

		Object[] left;
		Object[] right;

		Section(Object[] left, Object[] right) {
			this.left = left;
			this.right = right;
		}

		public String toString() {
			StringBuilder sb = new StringBuilder("[");
			sb.append(left[0]);
			for (int i = 1; i < left.length; i++) {
				sb.append(".");
				sb.append(left[i]);
			}
			sb.append(", ");
			sb.append(right[0]);
			for (int i = 1; i < right.length; i++) {
				sb.append(".");
				sb.append(right[i]);
			}
			sb.append("]");
			return sb.toString();
		}
	}

	private ArrayList<Section> sections = new ArrayList<Section>();

	// private int latestEdgeIndex = -1;

	private CascadingComparator cascadingComparator;

	public Fragment(Comparator comparator) {
		this(comparator, null);
		cascadingComparator = new CascadingComparator(comparator);
	}

	private Fragment(Comparator comparator, Object offset) {
		if (comparator instanceof FragmentComparator) {
			this.comparator = comparator;
		} else {
			this.comparator = new FragmentComparator(comparator);
		}
		this.index = offset;
		this.segment = new Segment(this.comparator);
	}

	/**
	 * get the fragment which start from offset.
	 * 
	 * @param offset
	 * @return
	 */
	public Fragment getFragment(Object offset) {
		Fragment frag = child;
		while (frag != null) {
			if (comparator.compare(frag.index, offset) == 0) {
				return frag;
			}
			frag = frag.next;
		}
		return null;
	}

	/**
	 * get next fragment start from offset.
	 * 
	 * @param offset
	 * @return
	 */
	public Fragment getNextFragment(Object offset) {
		if (offset == Segment.LEFT_MOST_EDGE) {
			return child;
		}
		if (offset == Segment.RIGHT_MOST_EDGE) {
			return null;
		}
		Fragment frag = child;
		while (frag != null) {
			if (comparator.compare(frag.index, offset) > 0) {
				return frag;
			}
			frag = frag.next;
		}
		return null;
	}

	public Fragment getFirstFragment() {
		return child;
	}

	/**
	 * @deprecated backward for 2.1.0 document. This method can only insert a
	 *             fragment of given offset as a child of current fragment.
	 */
	public void insertFragment(Object offset) {
		this.segment.insertSection(offset, offset);
		Fragment frag = addChildFragment(offset);
		frag.segment.insertSection(Segment.LEFT_MOST_EDGE, Segment.RIGHT_MOST_EDGE);
	}

	/**
	 * @deprecated
	 * @param offset
	 */
	private Fragment addChildFragment(Object offset) {
		Fragment prev = null;
		Fragment frag = this.child;
		while (frag != null) {
			int result = comparator.compare(frag.index, offset);
			if (result == 0) {
				// that's it
				return frag;
			}
			if (result == 1) {
				// we found it, insert it before the edge, after the prevEdge
				break;
			}
			// continue to search the insert position
			prev = frag;
			frag = frag.next;
		}

		// we have find the position, just after the prevEdge
		frag = new Fragment(comparator, offset);

		// link it with the previous
		if (prev != null) {
			if (prev.next != null) {
				frag.next = prev.next.next;
			}
			prev.next = frag;
		} else {
			frag.next = this.child;
			this.child = frag;
		}
		return frag;
	}

	/**
	 * add fragment defind by the left/right edges.
	 * 
	 * @param leftEdges
	 * @param rightEdges
	 */
	public void build() {
		for (Section sect : sections) {
			Fragment leftEdge = this;
			for (int i = 0; i < sect.left.length; i++) {
				leftEdge.segment.startSegment(sect.left[i]);
				// search the insert point in the edge tree
				leftEdge = addFragment(leftEdge, sect.left[i]);
			}
			// append "LEFT_MOST_EDGE" as the left edge.
			leftEdge.segment.startSegment(Segment.LEFT_MOST_EDGE);

			Fragment rightEdge = this;
			for (int i = 0; i < sect.right.length; i++) {
				rightEdge.segment.endSegment(sect.right[i]);
				// search the insert point in the edge tree
				rightEdge = addFragment(rightEdge, sect.right[i]);
			}
			// append "LEFT_MOST_EDGE" as the right edge.
			rightEdge.segment.endSegment(Segment.LEFT_MOST_EDGE);
		}
		segment.normalize();
	}

	public void addSection(Object[] left, Object[] right) {
		int index = search(left);
		insert(index, left, right);
	}

	protected int search(Object[] left) {
		if (sections.size() == 0) {
			return 0;
		}
		int result = 0;
		int low = 0;
		int high = sections.size() - 1;

		// add beside the last edge.
		result = checkInsertPoint(left, high);
		if (result > 0) {
			return high + 1;
		} else if (result == 0) {
			return high;
		} else {
			high = high - 1;
		}

		// add by binary search.
		while (low <= high) {
			int index = (low + high) >> 1;
			result = checkInsertPoint(left, index);
			if (0 == result) {
				return index;
			} else if (result > 0) {
				low = index + 1;
			} else {
				high = index - 1;
			}
		}
		// no march
		return low;

	}

	/**
	 * checks if we can add the edge at given index.
	 * 
	 * @param left  the left edge which is going to be inserted.
	 * @param right the right edge which is going to be inserted.
	 * @return 0 the edge is inserted successfully. >0 go forward to insert the
	 *         edge. <0 go back to insert the edge.
	 */
	private int checkInsertPoint(Object[] left, int index) {
		assert index >= 0;
		assert index < sections.size();
		if (sections.size() > 0) {
			Section sect = sections.get(index);
			return cascadingComparator.compare(left, sect.left);
		} else {
			// empty list
			if (index == 0) {
				return 0;
			} else {
				throw new RuntimeException("invalid insert position");
			}
		}
	}

	protected void insert(int index, Object[] left, Object[] right) {
		if (index > 0) {
			Section prev = sections.get(index - 1);
			if (cascadingComparator.compare(left, prev.right) > 0) {
				sections.add(index, new Section(left, right));
				merge(index);
			} else {
				if (cascadingComparator.compare(right, prev.right) <= 0) {
					return;
				} else {
					prev.right = right;
					merge(index - 1);
				}
			}
		} else {
			// index == 0
			sections.add(index, new Section(left, right));
			merge(index);
		}
	}

	private void merge(int index) {
		Object[] right = sections.get(index).right;
		for (int i = index + 1; i < sections.size(); i++) {
			Section current = sections.get(i);
			if (cascadingComparator.compare(right, current.left) < 0) {
				return;
			} else {
				sections.remove(i--);
				if (cascadingComparator.compare(right, current.right) <= 0) {
					sections.get(index).right = current.right;
					return;
				}
			}
		}
	}

	/**
	 * search in the edge list to find a position to update the node. If there is no
	 * proper edge node exits, it will create a edge node and insert it into the
	 * list.
	 * 
	 * @param parent the inserted or founded edge node.
	 * @param node   node to be insert.
	 * @return edge node which contains the node.
	 */
	private Fragment addFragment(Fragment parent, Object offset) {
		assert parent != null;
		Fragment prev = null;
		Fragment frag = parent.child;

		if (frag == null) {
			frag = new Fragment(comparator, offset);
			parent.child = frag;
			return frag;
		} else {
			while (frag.next != null) {
				frag = frag.next;
			}
			int result = comparator.compare(frag.index, offset);
			if (result == 0) {
				// that's it
				return frag;
			} else if (result < 0) {
				Fragment newFrag = new Fragment(comparator, offset);
				frag.next = newFrag;
				return newFrag;
			}
			// the new inserted fragment index should not less than the existing indexes.
			throw new RuntimeException("Wrong offset found while building fragment tree");
		}
	}

	/**
	 * Is the offset in the fragment.
	 * 
	 * @param offset the child offset.
	 * @return
	 */
	public boolean inFragment(Object offset) {
		return segment.inSegment(offset);
	}

	public Object getOffset() {
		return index;
	}

	public Object[][] getSections() {
		if (segment != null) {
			segment.normalize();
			return segment.sections;
		}
		return null;
	}

	public String printEdges() {
		StringBuilder sb = new StringBuilder();
		for (Iterator<Section> i = sections.iterator(); i.hasNext();) {
			Section edge = i.next();
			sb.append(edge.toString());
		}
		return sb.toString();
	}
}
