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

package org.eclipse.birt.report.designer.ui.preview.parameter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.model.api.CascadingParameterGroupHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;

/**
 * Abstact of Radio-button, list-box, combo-box.
 * 
 */

public abstract class ListingParam extends ScalarParam {

	/**
	 * Constructor
	 * 
	 * @param handle
	 * @param engineTask
	 */
	public ListingParam(ScalarParameterHandle handle, IEngineTask engineTask) {
		super(handle, engineTask);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.preview.parameter.ScalarParam#
	 * getValueList()
	 * 
	 * each item is <code>IParameterSelectionChoice</code>
	 */

	public List getValueList() {
		List values = new ArrayList();

		boolean isCascading = isCascadingParameter();

		String name = handle.getName();
		IGetParameterDefinitionTask task = engineTask.getEngine()
				.createGetParameterDefinitionTask(engineTask.getReportRunnable());

		if (isCascading) {
			values = getCascadingValues(values, task);
		} else {
			List selectionList = (List) task.getSelectionList(name);
			/*
			 * Iterator iterator = selectionList.iterator( ); while ( iterator.hasNext( ) )
			 * { IParameterSelectionChoice choice = (IParameterSelectionChoice) iterator
			 * .next( ); values.add( choice.getValue( ) ); }
			 */

			// TODO change IParameterSelectionChoice to parameter choice.
			values.addAll(selectionList);
		}

		return values;
	}

	/**
	 * Gets cascading parameter values.
	 * 
	 * @param values
	 * @param task
	 * @return cascading parameter values. each item is
	 *         <code>IParameterSelectionChoice</code>
	 */

	private List getCascadingValues(List values, IGetParameterDefinitionTask task) {
		CascadingParameterGroupHandle container = (CascadingParameterGroupHandle) handle.getContainer();
		List groupList = new ArrayList();
		List children = group.getChildren();

		Iterator iterator = children.iterator();
		while (iterator.hasNext()) {
			IParameter param = (IParameter) iterator.next();

			if (param == this)
				break;
			String value = param.getSelectionValue();
			// groupList.add( value );
			try {
				groupList.add(param.converToDataType(value));
			} catch (BirtException e) {
				// do nothing
			}
		}
		Object[] groupKeys = new Object[groupList.size()];
		for (int i = 0; i < groupList.size(); i++) {
			groupKeys[i] = groupList.get(i);
		}
		List cascading = (List) task.getSelectionListForCascadingGroup(container.getName(), groupKeys);
		/*
		 * iterator = cascading.iterator( ); while ( iterator.hasNext( ) ) {
		 * IParameterSelectionChoice choice = (IParameterSelectionChoice) iterator
		 * .next( ); values.add( choice.getValue( ) ); }
		 */

		// TODO change IParameterSelectionChoice to parameter choice.
		return cascading;
	}

	/**
	 * Check container of parameter handle is cascading parameter group.
	 * 
	 * @return <code>true</code> if is cascading parameter; else return
	 *         <code>false</code>.
	 */

	private boolean isCascadingParameter() {
		DesignElementHandle container = handle.getContainer();
		if (container != null && container instanceof CascadingParameterGroupHandle) {
			return true;
		}
		return false;
	}

}
