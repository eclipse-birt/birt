/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.executor;

import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.ir.AutoTextItemDesign;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

/**
 * the AutoTextItem excutor
 * 
 */
public class AutoTextItemExecutor extends StyledItemExecutor {

	/**
	 * constructor
	 * 
	 * @param manager the excutor manager which create this executor
	 */
	public AutoTextItemExecutor(ExecutorManager manager) {
		super(manager, ExecutorManager.AUTOTEXTITEM);
	}

	/**
	 * execute a AutoText and output an AutoText item content. The execution process
	 * is:
	 * <li>create an AutoText
	 * <li>push it into the stack
	 * <li>intialize the content
	 * <li>process the action, bookmark, style ,visibility.
	 * <li>execute the onCreate if necessary
	 * <li>call emitter to start the AutoText
	 * <li>popup the AutoText.
	 * 
	 * @see org.eclipse.birt.report.engine.executor.ReportItemExcutor#execute(IContentEmitter)
	 */
	public IContent execute() {
		AutoTextItemDesign textDesign = (AutoTextItemDesign) getDesign();
		IAutoTextContent textContent = report.createAutoTextContent();
		setContent(textContent);

		restoreResultSet();

		initializeContent(textDesign, textContent);
		processStyle(design, content);
		processVisibility(design, content);

		String type = ((AutoTextItemDesign) design).getType();
		if (DesignChoiceConstants.AUTO_TEXT_PAGE_NUMBER.equalsIgnoreCase(type)) {
			textContent.setType(IAutoTextContent.PAGE_NUMBER);
		} else if (DesignChoiceConstants.AUTO_TEXT_TOTAL_PAGE.equalsIgnoreCase(type)) {
			textContent.setType(IAutoTextContent.TOTAL_PAGE);
		} else if (DesignChoiceConstants.AUTO_TEXT_PAGE_NUMBER_UNFILTERED.equalsIgnoreCase(type)) {
			textContent.setType(IAutoTextContent.UNFILTERED_PAGE_NUMBER);
		} else if (DesignChoiceConstants.AUTO_TEXT_TOTAL_PAGE_UNFILTERED.equalsIgnoreCase(type)) {
			textContent.setType(IAutoTextContent.UNFILTERED_TOTAL_PAGE);
		} else if (DesignChoiceConstants.AUTO_TEXT_PAGE_VARIABLE.equalsIgnoreCase(type)) {
			textContent.setType(IAutoTextContent.PAGE_VARIABLE);
		}

		if (context.isInFactory()) {
			handleOnCreate(textContent);
		}
		return textContent;
	}
}