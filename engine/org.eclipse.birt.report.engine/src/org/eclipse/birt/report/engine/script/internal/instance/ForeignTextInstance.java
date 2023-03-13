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

import org.eclipse.birt.report.engine.api.script.instance.IAbstractTextInstance;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.TextItemDesign;

public abstract class ForeignTextInstance extends ReportItemInstance implements IAbstractTextInstance {

	public ForeignTextInstance(IForeignContent content, ExecutionContext context, RunningState runningState) {
		super(content, context, runningState);
	}

	protected ForeignTextInstance(ExecutionContext context, RunningState runningState) {
		super(context, runningState);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.script.IForeignTextInstance#getText()
	 */
	@Override
	public String getText() {
		IForeignContent fc = (IForeignContent) content;
		String type = fc.getRawType();
		if (IForeignContent.TEMPLATE_TYPE.equals(type)) {
			String text = null;
			Object[] rawValue = (Object[]) fc.getRawValue();
			if (rawValue[0] != null) {
				text = (String) rawValue[0];
			}
			if (text == null) {
				if (fc.getGenerateBy() instanceof TextItemDesign) {
					TextItemDesign design = (TextItemDesign) fc.getGenerateBy();
					text = design.getText();
				}
			}
			return text;
		} else if (IForeignContent.HTML_TYPE.equals(type) || IForeignContent.TEXT_TYPE.equals(type)) {
			return (fc.getRawValue() == null ? null : fc.getRawValue().toString());
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
	@Override
	public void setText(String value) {
		IForeignContent foreignContent = (IForeignContent) content;
		if (foreignContent.getRawType().equals(IForeignContent.TEMPLATE_TYPE)) {
			// the row value should be a Object[2], the first object is the
			// template text, the second object is the value map.
			Object[] rawValue = (Object[]) foreignContent.getRawValue();
			rawValue[0] = value;
		} else {
			foreignContent.setRawValue(value);
		}
	}
}
