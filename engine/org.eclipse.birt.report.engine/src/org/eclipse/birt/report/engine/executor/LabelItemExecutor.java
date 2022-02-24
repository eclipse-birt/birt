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

package org.eclipse.birt.report.engine.executor;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.ir.LabelItemDesign;

/**
 * the labelItem excutor
 * 
 */
public class LabelItemExecutor extends QueryItemExecutor {

	/**
	 * constructor
	 * 
	 * @param context the excutor context
	 * @param visitor the report executor visitor
	 */
	public LabelItemExecutor(ExecutorManager manager) {
		super(manager, ExecutorManager.LABELITEM);
	}

	/**
	 * execute a label and output an label item content. The execution process is:
	 * <li>create an label
	 * <li>push it into the stack
	 * <li>intialize the content
	 * <li>process the action, bookmark, style ,visibility.
	 * <li>execute the onCreate if necessary
	 * <li>call emitter to start the label
	 * <li>popup the label.
	 * 
	 * @see org.eclipse.birt.report.engine.executor.ReportItemExcutor#execute(IContentEmitter)
	 */
	public IContent execute() {
		LabelItemDesign labelDesign = (LabelItemDesign) getDesign();
		ILabelContent labelContent = report.createLabelContent();
		setContent(labelContent);

		executeQuery();

		initializeContent(labelDesign, labelContent);

		processAction(labelDesign, labelContent);
		processBookmark(labelDesign, labelContent);
		processStyle(labelDesign, labelContent);
		processVisibility(labelDesign, labelContent);
		processUserProperties(labelDesign, labelContent);

		if (context.isInFactory()) {
			handleOnCreate(labelContent);
		}

		startTOCEntry(labelContent);

		return labelContent;
	}

	public void close() throws BirtException {
		finishTOCEntry();
		closeQuery();
		super.close();
	}
}
