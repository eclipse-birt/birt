/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.simpleapi;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.api.simpleapi.IAction;
import org.eclipse.birt.report.model.api.simpleapi.ILabel;
import org.eclipse.birt.report.model.elements.interfaces.ILabelModel;

public class Label extends ReportItem implements ILabel {

	public Label(LabelHandle handle) {
		super(handle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.ILabel#getText()
	 */

	public String getText() {
		return ((LabelHandle) handle).getText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.ILabel#getDisplayText()
	 */

	public String getDisplayText() {
		return ((LabelHandle) handle).getDisplayText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.ILabel#setText(java
	 * .lang.String)
	 */

	public void setText(String text) throws SemanticException {

		setProperty(ILabelModel.TEXT_PROP, text);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.ILabel#getTextKey()
	 */

	public String getTextKey() {
		return ((LabelHandle) handle).getTextKey();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.ILabel#setTextKey(java
	 * .lang.String)
	 */

	public void setTextKey(String resourceKey) throws SemanticException {

		setProperty(ILabelModel.TEXT_ID_PROP, resourceKey);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.ILabel#getHelpText()
	 */

	public String getHelpText() {
		return ((LabelHandle) handle).getHelpText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.ILabel#setHelpText(
	 * java.lang.String)
	 */

	public void setHelpText(String text) throws SemanticException {

		setProperty(ILabelModel.HELP_TEXT_PROP, text);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.ILabel#getHelpTextKey()
	 */

	public String getHelpTextKey() {
		return ((LabelHandle) handle).getHelpTextKey();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.ILabel#setHelpTextKey
	 * (java.lang.String)
	 */

	public void setHelpTextKey(String resourceKey) throws SemanticException {
		setProperty(ILabelModel.HELP_TEXT_ID_PROP, resourceKey);
	}

	public IAction getAction() {
		return new ActionImpl(((LabelHandle) handle).getActionHandle(), (LabelHandle) handle);
	}

	public void addAction(IAction action) throws SemanticException {
		if (action == null)
			return;

		ActivityStack cmdStack = handle.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			((LabelHandle) handle).setAction((Action) action.getStructure());
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

}
