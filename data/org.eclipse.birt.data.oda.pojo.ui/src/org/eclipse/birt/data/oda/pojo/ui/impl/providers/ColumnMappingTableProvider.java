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

import java.util.List;

import org.eclipse.birt.data.oda.pojo.ui.impl.models.ColumnDefinition;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

public class ColumnMappingTableProvider {
	private MappingTableContentProvider contentProvider;

	private MappingTableLabelProvider labelProvider;

	public ColumnMappingTableProvider() {
		contentProvider = new MappingTableContentProvider();
		labelProvider = new MappingTableLabelProvider();
	}

	public MappingTableContentProvider getTableContentProvider() {
		return this.contentProvider;
	}

	public MappingTableLabelProvider getTableLabelProvider() {
		return this.labelProvider;
	}

	private static class MappingTableContentProvider implements IStructuredContentProvider {
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java
		 * .lang.Object)
		 */
		public Object[] getElements(Object arg0) {
			if (arg0 instanceof Object[])
				return (Object[]) arg0;

			if (arg0 instanceof List)
				return ((List) arg0).toArray();

			return new Object[0];
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
		 */
		public void dispose() {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface
		 * .viewers.Viewer, java.lang.Object, java.lang.Object)
		 */
		public void inputChanged(Viewer arg0, Object arg1, Object arg2) {

		}

	}

	private static class MappingTableLabelProvider extends LabelProvider implements ITableLabelProvider {

		public Image getColumnImage(Object arg0, int arg1) {
			// TODO Auto-generated method stub
			return null;
		}

		public String getColumnText(Object row, int index) {
			ColumnDefinition cm = (ColumnDefinition) row;
			switch (index) {
			case 0:
				return cm.getName();
			case 1:
				return cm.getMappingPathText();
			case 2:
				return cm.getType().getDisplayName();
			}
			return ""; //$NON-NLS-1$
		}
	}

}
