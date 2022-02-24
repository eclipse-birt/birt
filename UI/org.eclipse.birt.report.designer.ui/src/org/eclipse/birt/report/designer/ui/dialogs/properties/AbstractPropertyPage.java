/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.ui.dialogs.properties;

import org.eclipse.swt.graphics.Image;

/**
 * TODO: Please document
 * 
 * @deprecated As of BIRT 2.1, replaced by
 *             {@link org.eclipse.birt.report.designer.data.ui.property.AbstractPropertyPage
 *             org.eclipse.birt.report.designer.data.ui.property.AbstractPropertyPage
 *             }.
 */

public abstract class AbstractPropertyPage implements IPropertyPage {

	private transient IPropertyPageContainer container = null;

	private transient String name = null;

	private transient Image image = null;

	/**
	 *  
	 */
	public AbstractPropertyPage() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.IPropertyPage#setContainer(org.eclipse.
	 * birt.report.designer.ui.IPropertyPageContainer)
	 */
	public void setContainer(IPropertyPageContainer parentContainer) {
		this.container = parentContainer;
	}

	public IPropertyPageContainer getContainer() {
		return container;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.IPropertyPage#getName()
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.IPropertyPage#getImage()
	 */
	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.IPropertyPage#canLeave()
	 */
	public boolean canLeave() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.IPropertyPage#performOk()
	 */
	public boolean performOk() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.IPropertyPage#performCancel()
	 */
	public boolean performCancel() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.IPropertyPage#performHelp()
	 */
	public void performHelp() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPage#
	 * getToolTip()
	 */
	public String getToolTip() {
		return ""; //$NON-NLS-1$
	}
}
