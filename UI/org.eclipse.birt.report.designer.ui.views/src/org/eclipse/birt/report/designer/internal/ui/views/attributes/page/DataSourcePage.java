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

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.LibraryDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.TextPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SeperatorSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextSection;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 */

public class DataSourcePage extends AttributePage
{

	private SeperatorSection seperatorSection;
	private TextSection librarySection;
	
	public void buildUI( Composite parent  )
	{
		super.buildUI( parent );
		container.setLayout( WidgetUtil.createGridLayout( 3 ,15) );

		LibraryDescriptorProvider provider = new LibraryDescriptorProvider( );
		librarySection = new TextSection( provider.getDisplayName( ),
				container,
				true );
		librarySection.setProvider( provider );
		librarySection.setGridPlaceholder( 1, true );
		addSection( PageSectionId.DATA_SOURCE_LIBRARY, librarySection );

		seperatorSection = new SeperatorSection( container, SWT.HORIZONTAL );
		addSection( PageSectionId.DATA_SOURCE_SEPERATOR, seperatorSection );
		
		// Defines provider.

		IDescriptorProvider nameProvider = new TextPropertyDescriptorProvider( ReportItemHandle.NAME_PROP,
				ReportDesignConstants.DATA_SOURCE_ELEMENT );

		// Defines section.

		TextSection nameSection = new TextSection( nameProvider.getDisplayName( ),
				container,
				true );

		nameSection.setProvider( nameProvider );
		nameSection.setWidth( 500 );
		nameSection.setLayoutNum( 3 );
		nameSection.setGridPlaceholder( 1, true );

		// Adds section into this page.

		addSection( PageSectionId.DATA_SOURCE_NAME, nameSection ); //$NON-NLS-1$

		createSections( );
		layoutSections( );
	}
	
	public void refresh( )
	{
		if ( input instanceof List
				&& DEUtil.getMultiSelectionHandle( (List) input )
						.isExtendedElements( ) )
		{
			librarySection.setHidden( false );
			seperatorSection.setHidden( false );
			librarySection.load( );
		}
		else
		{
			librarySection.setHidden( true );
			seperatorSection.setHidden( true );
		}
		container.layout( true );
		container.redraw( );
		super.refresh( );
	}
}
