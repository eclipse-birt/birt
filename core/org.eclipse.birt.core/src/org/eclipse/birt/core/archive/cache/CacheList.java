/*******************************************************************************
 * Copyright (c) 20010 Actuate Corporation.
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

package org.eclipse.birt.core.archive.cache;

class CacheList {

	protected Cacheable firstEntry;
	protected Cacheable lastEntry;

	protected int listSize;

	CacheList() {
		firstEntry = null;
		lastEntry = null;
		listSize = 0;
	}

	void clear() {
		firstEntry = null;
		lastEntry = null;
		listSize = 0;
	}

	int size() {
		return listSize;
	}

	Cacheable first() {
		return firstEntry;
	}

	Cacheable last() {
		return lastEntry;
	}

	/**
	 * the entry may have been removed from the list
	 * 
	 * @param entry
	 */
	void remove(Cacheable entry) {
		Cacheable prev = entry.getPrev();
		Cacheable next = entry.getNext();
		entry.setPrev(null);
		entry.setNext(null);
		if (prev != null) {
			prev.setNext(next);
		} else if (entry == firstEntry) {
			firstEntry = next;
		}
		if (next != null) {
			next.setPrev(prev);
		} else if (entry == lastEntry) {
			lastEntry = prev;
		}
		listSize--;
	}

	Cacheable remove() {
		if (listSize > 0) {
			Cacheable removed = firstEntry;
			remove(removed);
			return removed;
		}
		return null;
	}

	void add(Cacheable entry) {
		entry.setPrev(lastEntry);
		entry.setNext(null);
		if (lastEntry != null) {
			lastEntry.setNext(entry);
		} else {
			firstEntry = entry;
		}
		lastEntry = entry;
		listSize++;
	}
}
