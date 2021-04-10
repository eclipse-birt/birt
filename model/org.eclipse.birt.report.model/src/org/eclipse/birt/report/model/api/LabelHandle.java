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

package org.eclipse.birt.report.model.api;

import java.util.Iterator;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.ILabelModel;
import org.eclipse.birt.report.model.util.impl.ActionHelper;

/**
 * Represents a label report item. A label shows a static piece of text
 * displayed in the report. The label has the following properties:
 * 
 * <ul>
 * <li>An optional hyperlink ( action ) for this label.
 * <li>An help text for the label.
 * <li>An static text message to display.
 * </ul>
 */

public class LabelHandle extends ReportItemHandle implements ILabelModel {

	/**
	 * Constructs a label handle with the given design and the element. The
	 * application generally does not create handles directly. Instead, it uses one
	 * of the navigation methods available on other element handles.
	 * 
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public LabelHandle(Module module, DesignElement element) {
		super(module, element);
	}

	/**
	 * Returns the static text for the label.
	 * 
	 * @return the static text to display
	 */

	public String getText() {
		return getStringProperty(TEXT_PROP);
	}

	/**
	 * Returns the localized text for the label. If the localized text for the text
	 * resource key is found, it will be returned. Otherwise, the static text will
	 * be returned.
	 * 
	 * @return the localized text for the label
	 */

	public String getDisplayText() {
		return getExternalizedValue(TEXT_ID_PROP, TEXT_PROP);
	}

	/**
	 * Sets the text of the label. Sets the static text itself. If the label is to
	 * be externalized, then set the text ID separately.
	 * 
	 * @param text the new text for the label
	 * @throws SemanticException if the property is locked.
	 */

	public void setText(String text) throws SemanticException {
		setStringProperty(TEXT_PROP, text);
	}

	/**
	 * Returns the resource key of the static text of the label.
	 * 
	 * @return the resource key of the static text
	 */

	public String getTextKey() {
		return getStringProperty(TEXT_ID_PROP);
	}

	/**
	 * Sets the resource key of the static text of the label.
	 * 
	 * @param resourceKey the resource key of the static text
	 * 
	 * @throws SemanticException if the resource key property is locked.
	 */

	public void setTextKey(String resourceKey) throws SemanticException {
		setStringProperty(TEXT_ID_PROP, resourceKey);
	}

	/**
	 * Returns a handle to work with the action property, action is a structure that
	 * defines a hyperlink.
	 * 
	 * @return a handle to the action property, return <code>null</code> if the
	 *         action has not been set on the label.
	 * @see ActionHandle
	 */

	public ActionHandle getActionHandle() {
		return new ActionHelper(this, ACTION_PROP).getActionHandle();
	}

	/**
	 * Set an action on the image.
	 * 
	 * @param action new action to be set on the image, it represents a bookmark
	 *               link, hyperlink, and drill through etc.
	 * @return a handle to the action property, return <code>null</code> if the
	 *         action has not been set on the image.
	 * 
	 * @throws SemanticException if member of the action is not valid.
	 */

	public ActionHandle setAction(Action action) throws SemanticException {
		return new ActionHelper(this, ACTION_PROP).setAction(action);
	}

	/**
	 * Returns the iterator for action defined on this label item.
	 * 
	 * @return the iterator for <code>Action</code> structure list defined on this
	 *         label item
	 */

	public Iterator<ActionHandle> actionsIterator() {
		return new ActionHelper(this, ACTION_PROP).actionsIterator();
	}

	/**
	 * Returns the help text of this label item.
	 * 
	 * @return the help text
	 */

	public String getHelpText() {
		return getStringProperty(HELP_TEXT_PROP);
	}

	/**
	 * Sets the help text of this label item.
	 * 
	 * @param text the help text
	 * 
	 * @throws SemanticException if the resource key property is locked.
	 */

	public void setHelpText(String text) throws SemanticException {
		setStringProperty(HELP_TEXT_PROP, text);
	}

	/**
	 * Returns the help text key of this label item.
	 * 
	 * @return the help text key
	 */

	public String getHelpTextKey() {
		return getStringProperty(HELP_TEXT_ID_PROP);
	}

	/**
	 * Sets the help text key of this label item.
	 * 
	 * @param resourceKey the help text key
	 * 
	 * @throws SemanticException if the resource key property of the help text is
	 *                           locked.
	 */

	public void setHelpTextKey(String resourceKey) throws SemanticException {
		setStringProperty(HELP_TEXT_ID_PROP, resourceKey);
	}
}