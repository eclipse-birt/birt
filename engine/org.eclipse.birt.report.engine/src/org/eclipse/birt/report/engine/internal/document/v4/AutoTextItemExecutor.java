/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.internal.document.v4;

import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.ir.AutoTextItemDesign;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

/**
 * the AutoTextItem excutor
 *
 */
public class AutoTextItemExecutor extends ReportItemExecutor {

	/**
	 * constructor
	 *
	 * @param manager the excutor manager which create this executor
	 */
	public AutoTextItemExecutor(ExecutorManager manager) {
		super(manager, ExecutorManager.AUTOTEXTITEM);
	}

	@Override
	protected IContent doCreateContent() {
		return report.createAutoTextContent();
	}

	@Override
	public void doExecute() throws Exception {
		AutoTextItemDesign textDesign = (AutoTextItemDesign) design;
		IAutoTextContent textContent = (IAutoTextContent) content;

		String type = textDesign.getType();
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

	}
}
