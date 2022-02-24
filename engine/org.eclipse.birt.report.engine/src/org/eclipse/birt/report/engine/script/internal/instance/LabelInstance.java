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

import org.eclipse.birt.report.engine.api.script.instance.IActionInstance;
import org.eclipse.birt.report.engine.api.script.instance.ILabelInstance;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.impl.ActionContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;

/**
 * A class representing the runtime state of a label
 */
public class LabelInstance extends ReportItemInstance implements ILabelInstance {

	public LabelInstance(ILabelContent label, ExecutionContext context, RunningState runningState) {
		super(label, context, runningState);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.instance.ILabelInstance#getText()
	 */
	public String getText() {
		return ((ILabelContent) content).getLabelText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.instance.ILabelInstance#setText(
	 * java.lang.String)
	 */
	public void setText(String value) {
		((ILabelContent) content).setLabelText(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.instance.ILabelInstance#getTextKey(
	 * )
	 */
	public String getTextKey() {
		return ((ILabelContent) content).getLabelKey();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.instance.ILabelInstance#setTextKey(
	 * java.lang.String)
	 */
	public void setTextKey(String key) {
		((ILabelContent) content).setLabelKey(key);
	}

	private IActionInstance actionInstance;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IReportInstance#
	 * createHyperlinkActionInstance( )
	 */
	public IActionInstance createAction() {
		IHyperlinkAction hyperlink = new ActionContent();
		return new ActionInstance(hyperlink);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IReportInstance#
	 * getHyperlinkInstance( )
	 */
	public IActionInstance getAction() {
		IHyperlinkAction hyperlink = content.getHyperlinkAction();
		if (hyperlink != null) {
			if (actionInstance == null) {
				actionInstance = new ActionInstance(hyperlink);
			}
		}
		return actionInstance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IReportInstance#
	 * setActionInstance(org.eclipse.birt.report.engine.api.script.instance.
	 * IActionInstance )
	 */
	public void setAction(IActionInstance actionInstance) {
		if (actionInstance == null) {
			content.setHyperlinkAction(null);
		} else if (actionInstance instanceof ActionInstance) {
			content.setHyperlinkAction(((ActionInstance) actionInstance).getHyperlinkAction());
		}
		this.actionInstance = actionInstance;
	}
}
