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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.page;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.TextPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextSection;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * The Alter-Text attribute page of Image element.
 */
public class AlterPage extends AttributePage
{

	/**
	 * @param parent
	 *            A widget which will be the parent of the new instance (cannot
	 *            be null)
	 * @param style
	 *            The style of widget to construct
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.attributes.page.AttributePage#buildUI()
	 */
	public void buildUI(Composite parent)
	{
		super.buildUI( parent );
		container.setLayout( WidgetUtil.createGridLayout( 1 ,15) );

		// Defines provider.

		IDescriptorProvider provider = new TextPropertyDescriptorProvider( ImageHandle.ALT_TEXT_PROP,
				ReportDesignConstants.IMAGE_ITEM );

		// Defines section.

		TextSection section = new TextSection( provider.getDisplayName( ),
				container,
				true );

		section.setProvider( provider );
		section.setStyle( SWT.MULTI | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL );
		section.setWidth( 500 );
		section.setFillText( true );

		addSection( PageSectionId.ALTER_ALT_TEXT , section ); //$NON-NLS-1$

		createSections( );
		layoutSections( );
	}
}
