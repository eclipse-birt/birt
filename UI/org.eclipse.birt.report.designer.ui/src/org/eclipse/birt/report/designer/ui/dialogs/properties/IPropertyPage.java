
/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.ui.dialogs.properties;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * An interface that provides methods of a property page.
 * 
 * @deprecated As of BIRT 2.1, replaced by
 *             {@link org.eclipse.birt.report.designer.data.ui.property.IPropertyPage
 *             org.eclipse.birt.report.designer.data.ui.property.IPropertyPage
 *             }.
 */
public interface IPropertyPage {

	public Control createPageControl(Composite parent);

	public void setContainer(IPropertyPageContainer parentContainer);

	public String getName();

	public Image getImage();

	public boolean canLeave();

	public boolean performOk();

	public boolean performCancel();

	public void performHelp();

	public void pageActivated();

	public String getToolTip();
}
