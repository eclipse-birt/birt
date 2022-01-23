/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.api.impl;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedObjectManager<T> implements Iterable<T> {
	private LinkedEntry<T> first;
	private LinkedEntry<T> last;
	private int modCount = 0;

	public Iterator<T> iterator() {
		return new InternalIterator();
	}

	public LinkedEntry<T> add(T object) {
		LinkedEntry<T> entry = new LinkedEntry<T>(this, object);
		add(entry);
		return entry;
	}

	public void add(LinkedEntry<T> entry) {
		if (entry == null) {
			return;
		}
		entry.setNext(null);
		entry.setPrevious(last);
		if (first == null) {
			first = entry;
		}
		if (last != null) {
			last.setNext(entry);
		}
		last = entry;
		modCount++;
	}

	public void remove(LinkedEntry<T> entry) {
		if (entry.getManager() != this) {
			return;
		}
		LinkedEntry<T> previous = entry.getPrevious();
		LinkedEntry<T> next = entry.getNext();
		if (previous != null) {
			previous.setNext(next);
		}
		if (next != null) {
			next.setPrevious(previous);
		}
		if (first == entry) {
			first = next;
		}
		if (last == entry) {
			last = previous;
		}
		entry.setNext(null);
		entry.setPrevious(null);
		modCount--;
	}

	public void clear() {
		first = null;
		last = null;
		modCount = 0;
	}

	private class InternalIterator implements Iterator<T> {
		private LinkedEntry<T> next, current;
		private int expectedModCount = 0;

		public InternalIterator() {
			next = first;
			expectedModCount = modCount;
		}

		public boolean hasNext() {
			return next != null;
		}

		public T next() {
			if (modCount != expectedModCount)
				throw new ConcurrentModificationException();
			LinkedEntry<T> entry = next;
			if (entry == null)
				throw new NoSuchElementException();
			T value = entry.getValue();
			current = entry;
			next = entry.getNext();
			return value;
		}

		public void remove() {
			if (current == null)
				throw new IllegalStateException();
			if (modCount != expectedModCount)
				throw new ConcurrentModificationException();
			LinkedObjectManager.this.remove(current);
			current = null;
			expectedModCount = modCount;
		}
	}

	public static class LinkedEntry<T> {
		private LinkedEntry<T> previous;
		private LinkedEntry<T> next;
		private LinkedObjectManager<T> manager;
		private T value;

		private LinkedEntry(LinkedObjectManager<T> manager, T value) {
			this.manager = manager;
			this.value = value;
		}

		public LinkedEntry<T> getPrevious() {
			return previous;
		}

		public void setPrevious(LinkedEntry<T> previousEntry) {
			this.previous = previousEntry;
		}

		public LinkedEntry<T> getNext() {
			return next;
		}

		public void setNext(LinkedEntry<T> nextEntry) {
			this.next = nextEntry;
		}

		public T getValue() {
			return value;
		}

		public LinkedObjectManager<T> getManager() {
			return manager;
		}
	}
}
