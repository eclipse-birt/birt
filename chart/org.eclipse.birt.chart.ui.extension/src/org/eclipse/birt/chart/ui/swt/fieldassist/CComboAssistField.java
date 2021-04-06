/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.fieldassist;

import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;

/**
 * The class wraps CCombo for field assist function.
 * 
 * @since 2.5
 */

public class CComboAssistField extends AssistField {

	/**
	 * Constructor of the class.
	 * 
	 * @param ccombo    the CCombo to be decorated.
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
	public CComboAssistField(CCombo ccombo, Composite composite) {
		this(ccombo, composite, null);
	}

	/**
	 * @param ccombo    the CCombo to be decorated.
	 * @param composite The SWT composite within which the decoration should be
	 *                  rendered. The decoration will be clipped to this composite,
	 *                  but it may be rendered on a child of the composite. The
	 *                  decoration will not be visible if the specified composite or
	 *                  its child composites are not visible in the space relative
	 *                  to the control, where the decoration is to be rendered. If
	 *                  this value is null, then the decoration will be rendered on
	 *                  whichever composite (or composites) are located in the
	 *                  specified position.
	 * @param values    the available contents for CCombo.
	 */
	public CComboAssistField(CCombo ccombo, Composite composite, String[] values) {
		super(ccombo, composite, new CComboContentAdapter(), values);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.fieldassist.AssistField#initModifyListener
	 * ()
	 */
	protected void initModifyListener() {
		((CCombo) control).addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent event) {
				FieldAssistHelper.getInstance().handleFieldModify(CComboAssistField.this);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.fieldassist.AssistField#isValid()
	 */
	@Override
	public boolean isValid() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.fieldassist.AssistField#isWarning()
	 */
	@Override
	public boolean isWarning() {
		return false;
	}
}
