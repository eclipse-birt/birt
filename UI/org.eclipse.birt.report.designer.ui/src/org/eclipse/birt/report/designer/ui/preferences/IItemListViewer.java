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

package org.eclipse.birt.report.designer.ui.preferences;

/**
 * 
 */

public interface IItemListViewer {

	/**
	 * Update the view to reflect the fact that a task was added to the task list
	 * 
	 * @param content
	 */
	public void addContent(ItemContent content);

	/**
	 * Update the view to reflect the fact that a task was removed from the task
	 * list
	 * 
	 * @param content
	 */
	public void removeContent(ItemContent content);

	/**
	 * Update the view to reflect the fact that one of the tasks was modified
	 * 
	 * @param content
	 */
	public void updateContent(ItemContent content);
}
