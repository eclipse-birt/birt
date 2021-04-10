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

package org.eclipse.birt.report.engine.script.internal.instance;

import org.eclipse.birt.report.engine.api.script.instance.IDynamicTextInstance;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;

public class DynamicTextInstance extends ReportItemInstance implements IDynamicTextInstance {

	protected IForeignContent fc = null;
	protected IDataContent dc = null;

	public DynamicTextInstance(IContent content, ExecutionContext context, RunningState runningState) {
		super(content, context, runningState);
		if (content instanceof IForeignContent) {
			fc = (IForeignContent) content;
		}
		if (content instanceof IDataContent) {
			dc = (IDataContent) content;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.script.IForeignTextInstance#getText()
	 */
	public String getText() {
		if (fc != null) {
			String type = fc.getRawType();
			if (IForeignContent.TEMPLATE_TYPE.equals(type) || IForeignContent.HTML_TYPE.equals(type)
					|| IForeignContent.TEXT_TYPE.equals(type)) {
				return (fc.getRawValue() == null ? null : fc.getRawValue().toString());
			}
		}
		if (dc != null) {
			return dc.getText();
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.script.IForeignTextInstance#setText(java.lang.
	 * String)
	 */
	public void setText(String value) {
		if (fc != null) {
			fc.setRawValue(value);
		}
		if (dc != null) {
			dc.setText(value);
		}
	}

	public Object getValue() {
		if (dc != null) {
			return dc.getValue();
		}
		return null;
	}
}
