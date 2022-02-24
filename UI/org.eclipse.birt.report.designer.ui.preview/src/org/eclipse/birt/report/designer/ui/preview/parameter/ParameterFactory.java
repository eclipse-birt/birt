/*******************************************************************************
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
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.preview.parameter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.model.api.CascadingParameterGroupHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.SlotHandle;

/**
 * Parameter factory which can create Parameter and Parameter Group see
 * <code>IParameter</code>
 */

public class ParameterFactory {

	private IGetParameterDefinitionTask task = null;

	/**
	 * Radion box
	 */

	private static final String RADIO_BUTTON = "radio-button"; //$NON-NLS-1$

	/**
	 * static text box
	 */

	private static final String TEXT_BOX = "text-box";//$NON-NLS-1$

	/**
	 * list box
	 */

	private static final String LIST_BOX = "list-box";//$NON-NLS-1$

	/**
	 * Constructor.
	 * 
	 * @param task
	 */

	public ParameterFactory(IGetParameterDefinitionTask task) {
		this.task = task;
	}

	/**
	 * Gets children of root.
	 * 
	 * @param task
	 * @return children of root.
	 */

	public List getRootChildren() {
		IReportRunnable runnable = task.getReportRunnable();
		if (runnable == null)
			return null;
		DesignElementHandle designHandle = runnable.getDesignHandle();
		if (designHandle == null)
			return null;

		assert designHandle.getRoot() != null;

		List parameters = designHandle.getRoot().getParametersAndParameterGroups();
		Iterator iterator = parameters.iterator();

		// The design handle of root is null.

		List childrenList = new ArrayList();
		while (iterator.hasNext()) {
			DesignElementHandle handle = (DesignElementHandle) iterator.next();

			if (handle instanceof ScalarParameterHandle) {
				// build parameter

				IParameter param = createScalarParameter((ScalarParameterHandle) handle);
				childrenList.add(param);
			} else if (handle instanceof ParameterHandle) {
				// Now do nothing.
			} else if (handle instanceof CascadingParameterGroupHandle) {
				// build cascading parameter

				ParameterGroupHandle groupHandle = (ParameterGroupHandle) handle;
				IParamGroup group = new CascadingGroup(groupHandle);
				childrenList.add(group);

				createParameterGroup(group, groupHandle);
			} else if (handle instanceof ParameterGroupHandle) {
				// build parameter group

				ParameterGroupHandle groupHandle = (ParameterGroupHandle) handle;
				IParamGroup group = new ParamGroup(groupHandle);
				childrenList.add(group);

				createParameterGroup(group, groupHandle);
			}
		}

		return childrenList;
	}

	/**
	 * Creates parameter group.
	 * 
	 * @param group
	 * @param task
	 * @param groupHandle
	 */

	private void createParameterGroup(IParamGroup group, ParameterGroupHandle groupHandle) {
		assert group != null;
		assert groupHandle != null;

		SlotHandle slotHandle = groupHandle.getSlot(ParameterGroupHandle.PARAMETERS_SLOT);

		// Now parameter group only contains parameter. can't contain parameter
		// group.

		Iterator iterator = slotHandle.iterator();
		while (iterator.hasNext()) {
			ParameterHandle handle = (ParameterHandle) iterator.next();
			createParameter(group, handle);
		}
	}

	/**
	 * Create parameter.
	 * 
	 * @param parentGroup
	 * @param paramHandle
	 */

	private IParameter createParameter(IParamGroup parentGroup, ParameterHandle paramHandle) {
		assert parentGroup != null;
		assert paramHandle != null;

		IParameter param = null;

		if (paramHandle instanceof ScalarParameterHandle) {
			param = createScalarParameter((ScalarParameterHandle) paramHandle);
		}

		// TODO for other parameter type.

		if (param != null) {
			parentGroup.addParameter(param);
		}
		return param;
	}

	/**
	 * Creates scalar parameter.
	 * 
	 * @param paramHandle
	 * @return scalar parameter.
	 */

	private ScalarParam createScalarParameter(ScalarParameterHandle paramHandle) {
		ScalarParam param = null;
		String controlType = paramHandle.getControlType();
		if (controlType.equals(LIST_BOX)) {
			boolean mustMatch = paramHandle.isMustMatch();
			if (mustMatch) {
				// list - box

				param = new ListBoxParam(paramHandle, task);
			} else {
				// combo-box

				param = new ComboBoxParam(paramHandle, task);
			}
		} else if (controlType.equals(TEXT_BOX)) {
			param = new StaticTextParam(paramHandle, task);
		} else if (controlType.equals(RADIO_BUTTON)) {
			param = new RadioParam(paramHandle, task);
		}

		return param;
	}

}
