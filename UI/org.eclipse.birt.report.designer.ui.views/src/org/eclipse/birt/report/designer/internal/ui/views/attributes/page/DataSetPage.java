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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.LibraryDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.TextPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SeperatorSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextSection;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 */

public class DataSetPage extends AttributePage
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
		addSection( PageSectionId.DATASET_LIBRARY, librarySection );

		seperatorSection = new SeperatorSection( container, SWT.HORIZONTAL );
		addSection( PageSectionId.DATASET_SEPERATOR, seperatorSection );

		
		TextPropertyDescriptorProvider nameProvider = new TextPropertyDescriptorProvider( ReportItemHandle.NAME_PROP,
				ReportDesignConstants.DATA_SET_ELEMENT );
		TextSection nameSection = new TextSection( nameProvider.getDisplayName( ),
				container,
				true );
		nameSection.setProvider( nameProvider );
		nameSection.setGridPlaceholder( 1, true );
		nameSection.setWidth( 500 );
		addSection( PageSectionId.DATASET_NAME, nameSection );
		
		TextPropertyDescriptorProvider dataSetProvider = new TextPropertyDescriptorProvider( DataSetHandle.DATA_SOURCE_PROP,
				ReportDesignConstants.DATA_SET_ELEMENT ){
			public String getDisplayName(){
				return Messages.getString( "DataSetPageGenerator.DataSourceName" );
			}
		};
		TextSection dataSetSection = new TextSection( dataSetProvider.getDisplayName( ),
				container,
				true );
		dataSetSection.setProvider( dataSetProvider );
		dataSetSection.setGridPlaceholder( 1, true );
		dataSetSection.setWidth( 500 );
		addSection( PageSectionId.DATASET_DATASET, dataSetSection );
		
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
