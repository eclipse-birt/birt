/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.attributes.page;

import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 */

public class CascadingParameterGroupI18nPage extends I18nPage
{
	public void buildUI( Composite parent  )
	{
		elementName = ReportDesignConstants.CASCADING_PARAMETER_GROUP_ELEMENT;
		propertyName = ParameterGroupHandle.HELP_TEXT_KEY_PROP;
		super.buildUI( parent );
	}
}
