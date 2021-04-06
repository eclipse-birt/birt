/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.oda.pojo.ui.impl.providers;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import org.eclipse.birt.data.oda.pojo.ui.impl.models.TreeData;
import org.eclipse.birt.data.oda.pojo.util.ClassParser;

/**
 * 
 */

public class ClassTreeContentProvider implements ITreeContentProvider {

	private String[] chainItems;
	private ClassParser cp;

	public ClassTreeContentProvider(String[] chainItems, ClassLoader cl) {
		this.chainItems = chainItems;
		this.cp = new ClassParser(cl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	public Object[] getChildren(Object arg0) {
		if (arg0 instanceof TreeData) {
			TreeData td = (TreeData) arg0;
			int level = td.getLevel();
			String filter = getFilterString(level);
			Object obj = td.getWrappedObject();
			level++;

			if (obj instanceof ClassWrapper) {
				Member[] members = ClassParser.getPublicMembers(((ClassWrapper) obj).getWrappedClass(), filter);
				return createTreeDataArray(members, level);
			}
			if (obj instanceof Field) {
				return createTreeDataArray(cp.getPublicMembers((Field) obj, filter), level);
			}
			if (obj instanceof Method) {
				return createTreeDataArray(cp.getPublicMembers((Method) obj, filter), level);
			}
		}
		return new Object[0];
	}

	private TreeData[] createTreeDataArray(Object[] members, int level) {
		TreeData[] items = new TreeData[members.length];
		for (int i = 0; i < items.length; i++) {
			items[i] = new TreeData(members[i], level);
		}
		return items;
	}

	private String getFilterString(int level) {
		return chainItems != null && chainItems.length > level ? chainItems[level].trim() : null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	public Object getParent(Object arg0) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	public boolean hasChildren(Object arg0) {
		if (arg0 instanceof TreeData) {
			Object obj = ((TreeData) arg0).getWrappedObject();

			if (obj instanceof ClassWrapper) {
				return ClassParser.getPublicMembers(((ClassWrapper) obj).getWrappedClass(), null).length > 0;
			}
			if (obj instanceof Field) {
				return cp.getPublicMembers((Field) obj, null).length > 0;
			}
			if (obj instanceof Method) {
				return cp.getPublicMembers((Method) obj, null).length > 0;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.
	 * Object)
	 */
	@SuppressWarnings("unchecked")
	public Object[] getElements(Object arg0) {
		if (arg0 instanceof TreeData) {
			Object obj = ((TreeData) arg0).getWrappedObject();
			if (obj instanceof Class) {
				return new Object[] { new TreeData(new ClassWrapper((Class) obj), 0) };
			} else if (obj instanceof String) {
				return new Object[] { obj };
			}
		}
		return new Object[0];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.
	 * viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {

	}

	@SuppressWarnings("unchecked")
	public static class ClassWrapper {

		public ClassWrapper(Class c) {
			this.c = c;
		}

		private Class c;

		public Class getWrappedClass() {
			return c;
		}
	}

}
