/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.tests.example.matrix;

import org.eclipse.birt.report.designer.ui.extensions.IPropertyTabUI;
import org.eclipse.birt.report.designer.ui.extensions.IReportItemPropertyEditUI;

/**
 */
public class ReportItemPropertyEditUITest implements IReportItemPropertyEditUI
{

    private IPropertyTabUI propertyTabUITest = new PropertyTabUITest();

    /* (non-Javadoc)
     * @see org.eclipse.birt.report.designer.ui.extensions.IReportItemPropertyEditUI#getCategoryTabs()
     */
    public IPropertyTabUI[] getCategoryTabs( )
    {
        return new IPropertyTabUI[] { propertyTabUITest };
    }

}
