/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.composites;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;

import org.eclipse.birt.chart.model.data.BigNumberDataElement;
import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.model.data.impl.BigNumberDataElementImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataElementComposite;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;

import com.ibm.icu.text.NumberFormat;

/**
 * Composite for inputing NumberDataElement
 */

public class NumberDataElementComposite extends TextEditorComposite implements IDataElementComposite {

	protected EObject eParent;

	public NumberDataElementComposite(Composite parent, DataElement data) {
		super(parent, SWT.BORDER | SWT.SINGLE, TextEditorComposite.TYPE_NUMBERIC);
		GridData gd = new GridData();
		gd.widthHint = 80;
		this.setLayoutData(gd);
		this.setDefaultValue(""); //$NON-NLS-1$

		setDataElement(data);
	}

	public DataElement getDataElement() {
		NumberFormat nf = ChartUIUtil.getDefaultNumberFormatInstance();
		try {
			Number number = nf.parse(getText());
			if (number instanceof BigInteger) {
				BigInteger biNumber = (BigInteger) number;
				return BigNumberDataElementImpl.create(new BigDecimal(biNumber).stripTrailingZeros());
			}
			return NumberDataElementImpl.create(number.doubleValue());
		} catch (ParseException e1) {
			return null;
		}
	}

	public void notifyListeners(int eventType, Event event) {
		// Filter out other events
		if (eventType == DATA_MODIFIED || eventType == FRACTION_CONVERTED) {
			super.notifyListeners(eventType, event);
		}
	}

	public void setDataElement(DataElement data) {
		if (data instanceof BigNumberDataElement) {
			this.setText(((BigNumberDataElement) data).getValue().stripTrailingZeros().toString());
		}

		if (data == null || data instanceof NumberDataElement) {
			this.setText(data == null ? "" //$NON-NLS-1$
					: ChartUIUtil.getDefaultNumberFormatInstance().format(((NumberDataElement) data).getValue()));
		}
	}

	public void setEObjectParent(EObject eParent) {
		this.eParent = eParent;
	}
}
