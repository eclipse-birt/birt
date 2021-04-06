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
