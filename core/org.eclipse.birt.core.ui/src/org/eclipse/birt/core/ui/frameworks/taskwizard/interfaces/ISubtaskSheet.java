/***********************************************************************
 * Copyright (c) 2005, 2007 Actuate Corporation.
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

package org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces;

import org.eclipse.jface.dialogs.IDialogPage;

public interface ISubtaskSheet extends Cloneable, IDialogPage {

	/**
	 * Called just before the UI for this Subtask is shown. Intended for the UI to
	 * prepare itself by processing the context passed in. The context will depend
	 * on the wizard.
	 * 
	 * @param context The context in which the Subtask UI will be shown. Its content
	 *                depend on individual wizards but it WILL be an instance of
	 *                IWizardContext. The Object type has been used to avoid adding
	 *                a dependency on the new UI plug-ins.
	 * @param wizard  The wizard container instance. It WILL be an instance of
	 *                WizardBase. The Object type has been used to avoid adding a
	 *                dependency on the new UI plug-ins.
	 */
	void onShow(Object context, Object wizard);

	/**
	 * Called just before the UI for the subtask is disposed, it will be used by the
	 * wizard. The context returned should contain the complete updated context for
	 * the wizard. This context should be usable as is and should not require any
	 * additional processing before use.
	 * 
	 * @return complete context for the wizard with all updates that result from
	 *         operations performed as part of this task. This MUST be an instanceof
	 *         IWizardContext! The Object type has been used to avoid adding a
	 *         dependency on the new UI plug-ins.
	 */
	Object onHide();

	void setIndex(int index);

	void setParentTask(ITask parentTask);

	void setNodePath(String nodePath);

	/**
	 * Returns the node path of subtask sheet
	 * 
	 * @since 2.3
	 */
	String getNodePath();

	/**
	 * Attaches specified popup. If task-level popup is null or not existent in
	 * current subtask, to open subtask-level popup. If subtask-level popup is still
	 * null, do nothing.
	 * 
	 * @param popupID task-level popup key which is registered in the subtask.
	 * @return whether the popup is attached successfully.
	 * @since 2.1
	 */
	boolean attachPopup(String popupID);

	/**
	 * Forces the popup dialogue detached.
	 * 
	 * @return detach result
	 * @since 2.1
	 */
	boolean detachPopup();

}
