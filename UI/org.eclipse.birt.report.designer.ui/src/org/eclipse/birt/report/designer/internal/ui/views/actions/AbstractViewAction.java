/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.actions;

import java.util.logging.Logger;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * The abstract super class for all the view actions
 */

public abstract class AbstractViewAction extends Action {
	protected static final Logger logger = Logger.getLogger(AbstractViewAction.class.getName());

	private Object selection;

	/**
	 * Creates a new action with given selection and no text
	 * 
	 * @param selectedObject the selected object,which cannot be null
	 */
	public AbstractViewAction(Object selectedObject) {
		// Assert.isNotNull( selectedObject );
		this.selection = selectedObject;
		setId(getId());
	}

	/**
	 * Creates a new action with given selection , id and text
	 * 
	 * @param selectedObject the selected object,which cannot be null
	 * @param text           the text of the action
	 */
	public AbstractViewAction(Object selectedObject, String text) {
		this(selectedObject);
		setText(text);
	}

	/**
	 * Gets the object selected
	 * 
	 * @return Returns the object selected.
	 *         <p>
	 *         If object number is more than one, class type is
	 *         <code>StructuredSelection</code>
	 */
	public Object getSelection() {
		if (selection instanceof Object[]) {
			return new StructuredSelection((Object[]) selection);
		} else if (selection == null) {
			return new StructuredSelection();
		}
		return selection;
	}

	/**
	 * Returns class name as ID.
	 * 
	 * @see org.eclipse.jface.action.IAction#getId()
	 */
	public String getId() {
		return getClass().toString();
	}

}
