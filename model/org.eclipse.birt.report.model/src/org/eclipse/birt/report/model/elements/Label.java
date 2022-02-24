/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.model.elements;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.ILabelModel;

/**
 * This class represents a static label in the report. The label text can be
 * defined in the label, or the label can reference an external message file so
 * that the label can be localized.
 * 
 */

public class Label extends ReportItem implements ILabelModel {

	/**
	 * Default constructor.
	 */

	public Label() {
	}

	/**
	 * Constructs the label item with an optional name.
	 * 
	 * @param theName the optional name
	 */

	public Label(String theName) {
		super(theName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt.
	 * report.model.elements.ElementVisitor)
	 */

	public void apply(ElementVisitor visitor) {
		visitor.visitLabel(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	public String getElementName() {
		return ReportDesignConstants.LABEL_ITEM;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.DesignElement#getHandle(org.eclipse.birt.
	 * report.model.elements.ReportDesign)
	 */

	public DesignElementHandle getHandle(Module module) {
		return handle(module);
	}

	/**
	 * Returns an API handle for this element.
	 * 
	 * @param module the report design
	 * @return an API handle for this element
	 */

	public LabelHandle handle(Module module) {
		if (handle == null) {
			handle = new LabelHandle(module, this);
		}
		return (LabelHandle) handle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.DesignElement#getDisplayLabel(org.eclipse.
	 * birt.report.model.elements.ReportDesign, int)
	 */

	public String getDisplayLabel(Module module, int level) {
		String displayLabel = super.getDisplayLabel(module, level);
		if (level == IDesignElementModel.FULL_LABEL) {
			String text = handle(module).getText();
			if (!StringUtil.isBlank(text)) {
				text = limitStringLength(text);
				displayLabel += "(\"" + text + "\")"; //$NON-NLS-1$ //$NON-NLS-2$
				return displayLabel;
			}

			String resourceKey = handle(module).getTextKey();
			if (!StringUtil.isBlank(resourceKey)) {
				resourceKey = limitStringLength(resourceKey);
				displayLabel += "(" + resourceKey + ")"; //$NON-NLS-1$ //$NON-NLS-2$
				return displayLabel;
			}
		}
		return displayLabel;
	}

}
