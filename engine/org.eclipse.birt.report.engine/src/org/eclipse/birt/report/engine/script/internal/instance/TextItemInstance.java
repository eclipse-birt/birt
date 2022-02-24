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

import org.eclipse.birt.report.engine.api.script.instance.ITextItemInstance;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;

/**
 * A class representing the runtime state of a text item
 */
public class TextItemInstance extends ForeignTextInstance implements ITextItemInstance {

	public TextItemInstance(ITextContent content, ExecutionContext context, RunningState runningState) {
		super(context, runningState);
		setContent(content);
	}

	public TextItemInstance(IForeignContent content, ExecutionContext context, RunningState runningState) {
		super(content, context, runningState);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.instance.ITextItemInstance#getText(
	 * )
	 */
	public String getText() {
		if (content instanceof ITextContent)
			return ((ITextContent) content).getText();
		return super.getText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.instance.ITextItemInstance#setText(
	 * java.lang.String)
	 */
	public void setText(String value) {
		if (content instanceof ITextContent)
			((ITextContent) content).setText(value);
		else
			super.setText(value);
	}

}
