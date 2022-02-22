/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.engine.internal.document.v2;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * this class is used to constuct the content tree. The tree is a cache and it
 * will be droped if the entry is not used any.
 *
 * A entry is not used only if all its child has been retrived.
 *
 */
class ContentTreeCache {

	static class TreeEntry {

		public TreeEntry(long offset, long parent, long next, Object value) {
			this.offset = offset;
			this.parent = parent;
			this.next = next;
			this.previous = -1;
			this.value = value;
		}

		long offset;
		long parent;
		long next;
		long previous;
		Object value;
	}

	LinkedList entries = new LinkedList();

	/**
	 *
	 * @param entry
	 * @throws Exception
	 */
	public void addEntry(TreeEntry entry) {
		// search the insert position, as we read the
		// content from begin to end, so the new entry will always
		// at the last, search it form the last to begin.
		ListIterator iter = entries.listIterator(entries.size());
		while (iter.hasPrevious()) {
			TreeEntry treeEntry = (TreeEntry) iter.previous();
			if (treeEntry.offset < entry.offset) {
				if (treeEntry.next == entry.offset) {
					entry.previous = treeEntry.offset;
				}
				// remove the previous siblings which is closed by this entry
				while (treeEntry.offset != entry.parent) {
					iter.remove();
					if (iter.hasPrevious()) {
						treeEntry = (TreeEntry) iter.previous();
						continue;
					}
					break;
				}
				// current position is before the treeEntry, so move it to the next.
				if (iter.hasNext()) {
					iter.next();
				}
				break;
			}
		}

		iter.add(entry);
	}

	/**
	 * as the entry are always load from the last, so also search it from the last
	 * to the first.
	 *
	 * @param offset
	 * @return
	 */
	public TreeEntry getEntry(long offset) {
		ListIterator iter = entries.listIterator(entries.size());
		while (iter.hasPrevious()) {
			TreeEntry entry = (TreeEntry) iter.previous();
			if (entry.offset == offset) {
				return entry;
			}
			if (entry.offset < offset) {
				break;
			}
		}
		return null;
	}
}
