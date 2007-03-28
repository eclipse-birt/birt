/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.model.simpleapi;

import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.TextDataHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.simpleapi.IDesignElement;

public class ElementUtil
{
	public static IDesignElement getElement( DesignElementHandle element )
	{
		if ( element == null )
			return null;
		if ( element instanceof ReportDesignHandle )
			return new ReportDesign( ( ReportDesignHandle ) element );

		if ( !( element instanceof ReportElementHandle ) )
			return null;

		if ( element instanceof DataItemHandle )
			return new DataItem( ( DataItemHandle ) element );

		if ( element instanceof GridHandle )
			return new Grid( ( GridHandle ) element );

		if ( element instanceof ImageHandle )
			return new Image( ( ImageHandle ) element );

		if ( element instanceof LabelHandle )
			return new Label( ( LabelHandle ) element );

		if ( element instanceof ListHandle )
			return new List( ( ListHandle ) element );

		if ( element instanceof TableHandle )
			return new Table( ( TableHandle ) element );

		if ( element instanceof TextDataHandle )
			return new DynamicText( ( TextDataHandle ) element );

		if ( element instanceof TextItemHandle )
			return new TextItem( ( TextItemHandle ) element );

		return new ReportElement( ( ReportElementHandle ) element );

	}

}
