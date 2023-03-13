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

package org.eclipse.birt.report.engine.script.internal.instance;

import org.eclipse.birt.report.engine.api.script.instance.IAutoTextInstance;
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;

public class AutoTextInstance extends ReportElementInstance implements IAutoTextInstance {

	IAutoTextContent autoText;

	public AutoTextInstance(IAutoTextContent autoText, ExecutionContext context, RunningState runningState) {
		super(autoText, context, runningState);
		this.autoText = autoText;
	}

	@Override
	public void setText(String text) {
		if (runningState != RunningState.RENDER) {
			throw new UnsupportedOperationException("setText can only be invoked in onRender");
		}
		autoText.setText(text);
	}

	@Override
	public String getText() {
		return autoText.getText();
	}
}
