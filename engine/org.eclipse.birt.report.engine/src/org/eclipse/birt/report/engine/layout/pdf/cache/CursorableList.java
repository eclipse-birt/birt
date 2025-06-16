package org.eclipse.birt.report.engine.layout.pdf.cache;

/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
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

	public Object removeLast() {
		if ((cursor > 0) && (cursor <= list.size())) {
			Object result = list.remove(cursor - 1);
			cursor--;
			return result;
		}
		return null;
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

	@Override
	public boolean add(Object o) {
		list.add(cursor, o);
		cursor++;
		return true;
	}

	@Override
	public void add(int index, Object element) {
		throw new UnsupportedOperationException("unsupported operation"); //$NON-NLS-1$

	}

	@Override
	public boolean addAll(Collection c) {
		throw new UnsupportedOperationException("unsupported operation"); //$NON-NLS-1$
	}

	@Override
	public boolean addAll(int index, Collection c) {
		throw new UnsupportedOperationException("unsupported operation"); //$NON-NLS-1$
	}

	@Override
	public void clear() {
		list.clear();
		cursor = 0;

	}

	@Override
	public boolean contains(Object o) {
		return list.contains(o);
	}

	@Override
	public boolean containsAll(Collection c) {
		return list.containsAll(c);
	}

	@Override
	public Object get(int index) {
		return list.get(index);
	}

	@Override
	public int indexOf(Object o) {
		return list.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public Iterator iterator() {
		return list.iterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		return list.indexOf(o);
	}

	@Override
	public ListIterator listIterator() {
		return list.listIterator();
	}

	@Override
	public ListIterator listIterator(int index) {
		return list.listIterator(index);
	}

	@Override
	public Object remove(int index) {
		Object obj = list.remove(index);
		if (cursor > index) {
			cursor--;
		}
		return obj;
	}

	@Override
	public boolean remove(Object o) {
		int index = list.indexOf(o);
		if (index >= 0) {
			remove(index);
			return true;
		}
		return false;
	}

	@Override
	public boolean removeAll(Collection c) {
		throw new UnsupportedOperationException("unsupported operation"); //$NON-NLS-1$
	}

	@Override
	public boolean retainAll(Collection c) {
		throw new UnsupportedOperationException("unsupported operation"); //$NON-NLS-1$
	}

	@Override
	public Object set(int index, Object element) {
		throw new UnsupportedOperationException("unsupported operation"); //$NON-NLS-1$
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public List subList(int fromIndex, int toIndex) {
		throw new UnsupportedOperationException("unsupported operation"); //$NON-NLS-1$
	}

	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	@Override
	public Object[] toArray(Object[] a) {
		return list.toArray(a);
	}

}
