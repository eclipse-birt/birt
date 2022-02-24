/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.engine.script.internal.instance;

import org.eclipse.birt.report.engine.api.script.instance.IReportItemInstance;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.executor.ExecutionContext;

/**
 * A class representing the runtime state of a report item
 */
public class ReportItemInstance extends ReportElementInstance implements IReportItemInstance {

	public ReportItemInstance(IContent content, ExecutionContext context, RunningState runningState) {
		super(content, context, runningState);
	}

	protected void setContent(IContent content) {
		this.content = content;
	}

	protected ReportItemInstance(ExecutionContext context, RunningState runningState) {
		super(context, runningState);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IReportInstance#
	 * getHyperlink()
	 */
	public String getHyperlink() {
		IHyperlinkAction hyperlinkAction = content.getHyperlinkAction();
		return hyperlinkAction == null ? null : hyperlinkAction.getHyperlink();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.instance.IReportInstance#getName()
	 */
	public String getName() {
		return content.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.instance.IReportInstance#setName(
	 * java.lang.String)
	 */
	public void setName(String name) {
		content.setName(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IReportInstance#
	 * getHelpText()
	 */
	public String getHelpText() {
		return content.getHelpText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IReportInstance#
	 * setHelpText(java.lang.String)
	 */
	public void setHelpText(String helpText) {
		content.setHelpText(helpText);
	}

}
