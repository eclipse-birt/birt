/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.sample.reportitem.rotatedtext;

import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.extension.ReportItem;

/**
 * RotatedTextItem
 */
public class RotatedTextItem extends ReportItem {

	public static final String EXTENSION_NAME = "RotatedText"; //$NON-NLS-1$
	public static final String TEXT_PROP = "text"; //$NON-NLS-1$
	public static final String ROTATION_ANGLE_PROP = "rotationAngle"; //$NON-NLS-1$

	private ExtendedItemHandle modelHandle;

	RotatedTextItem(ExtendedItemHandle modelHandle) {
		this.modelHandle = modelHandle;
	}

	public ExtendedItemHandle getModelHandle() {
		return modelHandle;
	}

	public String getText() {
		return modelHandle.getStringProperty(TEXT_PROP);
	}

	public int getRotationAngle() {
		return modelHandle.getIntProperty(ROTATION_ANGLE_PROP);
	}

	public void setText(String value) throws SemanticException {
		modelHandle.setProperty(TEXT_PROP, value);
	}

	public void setRotationAngle(int value) throws SemanticException {
		modelHandle.setProperty(ROTATION_ANGLE_PROP, value);
	}

}
