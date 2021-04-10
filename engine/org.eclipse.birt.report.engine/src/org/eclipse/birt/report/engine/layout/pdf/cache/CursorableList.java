package org.eclipse.birt.report.engine.layout.pdf.cache;

/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class CursorableList implements List {
	private ArrayList list = new ArrayList();

	private int cursor = 0;

	public void resetCursor() {
		cursor = 0;
	}

	public void setCursor(int index) {
		if (index < 0 || index > list.size()) {
			throw new IndexOutOfBoundsException();
		}
		cursor = index;
	}

	public void removeLast() {
		if ((cursor > 0) && (cursor <= list.size())) {
			list.remove(cursor - 1);
			cursor--;
		}

	}

	public Object getCurrent() {
		if ((cursor > 0) && (cursor <= list.size())) {
			return list.get(cursor - 1);
		}
		return null;
	}

	public void next() {
		if (cursor >= list.size()) {
			throw new IndexOutOfBoundsException();
		}
		cursor++;
	}

	public void next(int n) {
		if (cursor + n > list.size()) {
			throw new IndexOutOfBoundsException();
		}
		cursor += n;
	}

	public boolean add(Object o) {
		list.add(cursor, o);
		cursor++;
		return true;
	}

	public void add(int index, Object element) {
		throw new UnsupportedOperationException("unsupported operation"); //$NON-NLS-1$

	}

	public boolean addAll(Collection c) {
		throw new UnsupportedOperationException("unsupported operation"); //$NON-NLS-1$
	}

	public boolean addAll(int index, Collection c) {
		throw new UnsupportedOperationException("unsupported operation"); //$NON-NLS-1$
	}

	public void clear() {
		list.clear();
		cursor = 0;

	}

	public boolean contains(Object o) {
		return list.contains(o);
	}

	public boolean containsAll(Collection c) {
		return list.containsAll(c);
	}

	public Object get(int index) {
		return list.get(index);
	}

	public int indexOf(Object o) {
		return list.indexOf(o);
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}

	public Iterator iterator() {
		return list.iterator();
	}

	public int lastIndexOf(Object o) {
		return list.indexOf(o);
	}

	public ListIterator listIterator() {
		return list.listIterator();
	}

	public ListIterator listIterator(int index) {
		return list.listIterator(index);
	}

	public Object remove(int index) {
		Object obj = list.remove(index);
		if (cursor > index) {
			cursor--;
		}
		return obj;
	}

	public boolean remove(Object o) {
		int index = list.indexOf(o);
		if (index >= 0) {
			remove(index);
			return true;
		}
		return false;
	}

	public boolean removeAll(Collection c) {
		throw new UnsupportedOperationException("unsupported operation"); //$NON-NLS-1$
	}

	public boolean retainAll(Collection c) {
		throw new UnsupportedOperationException("unsupported operation"); //$NON-NLS-1$
	}

	public Object set(int index, Object element) {
		throw new UnsupportedOperationException("unsupported operation"); //$NON-NLS-1$
	}

	public int size() {
		return list.size();
	}

	public List subList(int fromIndex, int toIndex) {
		throw new UnsupportedOperationException("unsupported operation"); //$NON-NLS-1$
	}

	public Object[] toArray() {
		return list.toArray();
	}

	public Object[] toArray(Object[] a) {
		return list.toArray(a);
	}

}
