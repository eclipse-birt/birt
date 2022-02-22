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
import org.eclipse.birt.report.engine.api.script.instance.IDataItemInstance;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.impl.ActionContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;

/**
 * A class representing the runtime state of a data item
 */
public class DataItemInstance extends ReportItemInstance implements IDataItemInstance {

	public DataItemInstance(IDataContent data, ExecutionContext context, RunningState runningState) {
		super(data, context, runningState);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.instance.IDataItemInstance#getValue
	 * ()
	 */
	@Override
	public Object getValue() {
		return ((IDataContent) content).getValue();
	}

	private IActionInstance actionInstance;

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.instance.IReportInstance#
	 * createHyperlinkActionInstance( )
	 */
	@Override
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
	@Override
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
	@Override
	public void setAction(IActionInstance actionInstance) {
		if (actionInstance == null) {
			content.setHyperlinkAction(null);
		} else if (actionInstance instanceof ActionInstance) {
			content.setHyperlinkAction(((ActionInstance) actionInstance).getHyperlinkAction());
		}
		this.actionInstance = actionInstance;
	}

	@Override
	public void setDisplayValue(Object value) {
		((IDataContent) content).setValue(value);
	}
}
