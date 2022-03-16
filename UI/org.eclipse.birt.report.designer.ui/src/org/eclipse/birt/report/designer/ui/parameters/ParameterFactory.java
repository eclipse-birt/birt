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

package org.eclipse.birt.report.designer.ui.parameters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.model.api.AbstractScalarParameterHandle;
import org.eclipse.birt.report.model.api.CascadingParameterGroupHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.core.runtime.Platform;

/**
 * Parameter factory which can create Parameter and Parameter Group see
 * <code>IParameter</code>
 */
public class ParameterFactory {

	private static final String RADIO_BUTTON = DesignChoiceConstants.PARAM_CONTROL_RADIO_BUTTON;

	private static final String TEXT_BOX = DesignChoiceConstants.PARAM_CONTROL_TEXT_BOX;

	private static final String LIST_BOX = DesignChoiceConstants.PARAM_CONTROL_LIST_BOX;

	private static final String CHECK_BOX = DesignChoiceConstants.PARAM_CONTROL_CHECK_BOX;

	private static final String AUTOSUGGEST_BOX = DesignChoiceConstants.PARAM_CONTROL_AUTO_SUGGEST;

	private IGetParameterDefinitionTask task;

	/**
	 * Constructor.
	 *
	 * @param task
	 */
	public ParameterFactory(IGetParameterDefinitionTask task) {
		this.task = task;
	}

	public List getRootChildren() {
		return getRootChildren(true);
	}

	/**
	 * Gets children of root.
	 *
	 * @param task
	 * @return children of root.
	 */
	public List getRootChildren(boolean includeHidden) {
		IReportRunnable runnable = task.getReportRunnable();
		if (runnable == null) {
			return null;
		}

		DesignElementHandle designHandle = runnable.getDesignHandle();
		if (designHandle == null) {
			return null;
		}

		assert designHandle.getRoot() != null;

		List parameters = designHandle.getRoot().getParameters().getContents();
		Iterator iterator = parameters.iterator();

		// The design handle of root is null.

		List childrenList = new ArrayList();
		while (iterator.hasNext()) {
			DesignElementHandle handle = (DesignElementHandle) iterator.next();

			if (handle instanceof ScalarParameterHandle) {
				// build parameter
				ScalarParameterHandle temp = (ScalarParameterHandle) handle;
				if (includeHidden || !temp.isHidden()) {
					IParameter param = createScalarParameter(temp);
					childrenList.add(param);
				}
			} else if (handle instanceof ParameterGroupHandle) {
				// build parameter group
				ParameterGroupHandle groupHandle = (ParameterGroupHandle) handle;
				IParameterGroup group;
				if (handle instanceof CascadingParameterGroupHandle) {
					group = new CascadingParameterGroup(groupHandle);
				} else {
					group = new ParameterGroup(groupHandle);
				}
				childrenList.add(group);

				createParameterGroup(group, groupHandle, includeHidden);
			} else if (handle instanceof AbstractScalarParameterHandle) {
				Object adapter = Platform.getAdapterManager().getAdapter(handle, IParameterAdapter.class);
				if (adapter != null) {
					((IParameterAdapter) adapter).setHandle((AbstractScalarParameterHandle) handle);
					((IParameterAdapter) adapter).setParameterDefinitionTask(task);
					childrenList.add(adapter);
				}
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
	private void createParameterGroup(IParameterGroup group, ParameterGroupHandle groupHandle, boolean includeHidden) {
		assert group != null;
		assert groupHandle != null;

		SlotHandle slotHandle = groupHandle.getSlot(ParameterGroupHandle.PARAMETERS_SLOT);

		// Now parameter group only contains parameter. can't contain parameter
		// group.

		Iterator iterator = slotHandle.iterator();
		while (iterator.hasNext()) {
			ParameterHandle handle = (ParameterHandle) iterator.next();
			if (includeHidden || !handle.isHidden()) {
				createParameter(group, handle);
			}
		}
	}

	/**
	 * Create parameter.
	 *
	 * @param parentGroup
	 * @param paramHandle
	 */
	private IParameter createParameter(IParameterGroup parentGroup, ParameterHandle paramHandle) {
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
	private ScalarParameter createScalarParameter(ScalarParameterHandle paramHandle) {
		ScalarParameter param = null;
		String controlType = paramHandle.getControlType();
		if (controlType.equals(LIST_BOX)) {
			boolean mustMatch = paramHandle.isMustMatch();
			if (mustMatch) {
				// list - box
				param = new ListBoxParameter(paramHandle, task);
			} else {
				// combo-box
				param = new ComboBoxParameter(paramHandle, task);
			}
		} else if (controlType.equals(TEXT_BOX) || controlType.equals(AUTOSUGGEST_BOX)) {
			param = new StaticTextParameter(paramHandle, task);
		} else if (controlType.equals(RADIO_BUTTON)) {
			param = new RadioParameter(paramHandle, task);
		} else if (controlType.equals(CHECK_BOX)) {
			param = new CheckBoxParameter(paramHandle, task);
		}

		return param;
	}

}
