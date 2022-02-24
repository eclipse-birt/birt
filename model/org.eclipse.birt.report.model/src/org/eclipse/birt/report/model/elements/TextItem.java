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
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.ITextItemModel;

/**
 * This class represents a text item in the report.
 * 
 */

public class TextItem extends ReportItem implements ITextItemModel {

	/**
	 * Constructs a text item.
	 */

	public TextItem() {
	}

	/**
	 * Constructs a text item with the given name.
	 * 
	 * @param theName the optional name
	 */

	public TextItem(String theName) {
		super(theName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt.
	 * report.model.elements.ElementVisitor)
	 */

	public void apply(ElementVisitor visitor) {
		visitor.visitTextItem(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	public String getElementName() {
		return ReportDesignConstants.TEXT_ITEM;
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
	 * Returns an handle for this text element.
	 * 
	 * @param module the report design
	 * 
	 * @return a handle for this element
	 */

	public TextItemHandle handle(Module module) {
		if (handle == null) {
			handle = new TextItemHandle(module, this);
		}
		return (TextItemHandle) handle;
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
			String text = getStringProperty(module, ITextItemModel.CONTENT_PROP);

			if (!StringUtil.isBlank(text)) {
				text = limitStringLength(text);
				displayLabel += "(\"" + text + "\")"; //$NON-NLS-1$//$NON-NLS-2$
				return displayLabel;
			}

			String resourceKey = getStringProperty(module, ITextItemModel.CONTENT_RESOURCE_KEY_PROP);
			if (!StringUtil.isBlank(resourceKey)) {
				resourceKey = limitStringLength(resourceKey);
				displayLabel += "(\"" + resourceKey + "\")"; //$NON-NLS-1$//$NON-NLS-2$
				return displayLabel;
			}
		}
		return displayLabel;
	}
}
