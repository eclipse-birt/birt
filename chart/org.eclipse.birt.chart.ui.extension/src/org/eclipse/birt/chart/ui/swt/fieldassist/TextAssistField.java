/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.chart.ui.swt.fieldassist;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * The class wraps Text field to support field assist.
 *
 * @since 2.5
 */

public class TextAssistField extends AssistField {

	/**
	 * Constructor.
	 *
	 * @param text      the text to be decorated.
	 * @param composite The SWT composite within which the decoration should be
	 *                  rendered. The decoration will be clipped to this composite,
	 *                  but it may be rendered on a child of the composite. The
	 *                  decoration will not be visible if the specified composite or
	 *                  its child composites are not visible in the space relative
	 *                  to the control, where the decoration is to be rendered. If
	 *                  this value is null, then the decoration will be rendered on
	 *                  whichever composite (or composites) are located in the
	 *                  specified position.
	 */
	public TextAssistField(Text text, Composite composite) {
		this(text, composite, null);
	}

	/**
	 * Constructor.
	 *
	 * @param text      the text to be decorated.
	 * @param composite The SWT composite within which the decoration should be
	 *                  rendered. The decoration will be clipped to this composite,
	 *                  but it may be rendered on a child of the composite. The
	 *                  decoration will not be visible if the specified composite or
	 *                  its child composites are not visible in the space relative
	 *                  to the control, where the decoration is to be rendered. If
	 *                  this value is null, then the decoration will be rendered on
	 *                  whichever composite (or composites) are located in the
	 *                  specified position.
	 * @param values    the available contents.
	 */
	public TextAssistField(Text text, Composite composite, String[] values) {
		super(text, composite, new CTextContentAdapter(), values);
	}

	/**
	 * Initialize modify listener for current field.
	 */
	@Override
	protected void initModifyListener() {
		((Text) control).addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent event) {
				FieldAssistHelper.getInstance().handleFieldModify(TextAssistField.this);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.ui.swt.fieldassist.SmartField#isValid()
	 */
	@Override
	public boolean isValid() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.ui.swt.fieldassist.SmartField#isWarning()
	 */
	@Override
	public boolean isWarning() {
		return false;
	}
}
