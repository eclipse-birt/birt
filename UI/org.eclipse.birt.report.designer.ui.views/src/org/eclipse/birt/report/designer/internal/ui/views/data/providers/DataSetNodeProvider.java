/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.data.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.eclipse.birt.report.designer.data.ui.dataset.DataSetUIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.actions.RefreshAction;
import org.eclipse.birt.report.designer.ui.actions.ShowPropertyAction;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CachedMetaDataHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSetParameterHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IWorkbenchActionConstants;

import com.ibm.icu.text.Collator;

/**
 * Deals with dataset node
 * 
 */
public class DataSetNodeProvider extends DefaultNodeProvider {

	private static Comparator<ResultSetColumnHandle> comp = new Comparator<ResultSetColumnHandle>() {

		private Collator collator = Collator.getInstance();

		public int compare(ResultSetColumnHandle r1, ResultSetColumnHandle r2) {
			return collator.compare(r1.getColumnName(), r2.getColumnName());
		}

	};

	/**
	 * Creates the context menu for the given object. Gets the action from the
	 * actionRegistry and adds the action to the menu.
	 * 
	 * @param menu   the menu
	 * @param object the object
	 */
	public void createContextMenu(TreeViewer sourceViewer, Object object, IMenuManager menu) {
		super.createContextMenu(sourceViewer, object, menu);

		menu.insertBefore(IWorkbenchActionConstants.MB_ADDITIONS + "-refresh", //$NON-NLS-1$
				new ShowPropertyAction(object));

		menu.insertAfter(IWorkbenchActionConstants.MB_ADDITIONS + "-refresh", new Separator()); //$NON-NLS-1$
		IAction action = new RefreshAction(sourceViewer);
		if (action.isEnabled()) {
			menu.insertAfter(IWorkbenchActionConstants.MB_ADDITIONS + "-refresh", action); //$NON-NLS-1$
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.INodeProvider#
	 * getNodeDisplayName(java.lang.Object)
	 */
	public String getNodeDisplayName(Object model) {
		return DEUtil.getDisplayLabel(model, false);
	}

	/**
	 * Gets the children element of the given model using visitor.
	 * 
	 * @param object the handle
	 */
	public Object[] getChildren(Object object) {
		DataSetHandle handle = (DataSetHandle) object;

		ArrayList params = new ArrayList(10);

		CachedMetaDataHandle cmdh = null;
		try {
			cmdh = DataSetUIUtil.getCachedMetaDataHandle(handle);
		} catch (SemanticException e) {
		}

		ArrayList columns = new ArrayList(10);

		if (cmdh != null) {
			for (Iterator iterator = cmdh.getResultSet().iterator(); iterator.hasNext();) {
				ResultSetColumnHandle element = (ResultSetColumnHandle) iterator.next();
				columns.add(element);
			}
		}

		Collections.sort(columns, comp);

		PropertyHandle parameters = handle.getPropertyHandle(DataSetHandle.PARAMETERS_PROP);
		Iterator iter = parameters.iterator();

		if (iter != null) {
			while (iter.hasNext()) {
				Object dataSetParameter = iter.next();
				if (((DataSetParameterHandle) dataSetParameter).isOutput() == true) {
					params.add(dataSetParameter);
				}
			}
		}

		Object[] parametersArray = params.toArray();
		Object[] both = new Object[columns.toArray().length + parametersArray.length];
		System.arraycopy(columns.toArray(), 0, both, 0, columns.toArray().length);
		System.arraycopy(parametersArray, 0, both, columns.toArray().length, parametersArray.length);
		return both;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider#
	 * hasChildren(java.lang.Object)
	 */
	public boolean hasChildren(Object object) {
		return true;
	}

}
