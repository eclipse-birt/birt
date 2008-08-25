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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.ColorPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.ComboPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.TextPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.UnitPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ColorSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ComboSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SeperatorSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.UnitSection;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.swt.SWT;

/**
 * The general attribute page of MasterPage element.
 */
public class MasterPageGeneralPage extends GeneralPage
{

	private UnitSection heightSection;
	private ComboPropertyDescriptorProvider typeProvider;
	private UnitSection widthSection;

	protected void buildContent( )
	{
		container.setLayout( WidgetUtil.createGridLayout( 6, 15 ) );

		TextPropertyDescriptorProvider nameProvider = new TextPropertyDescriptorProvider( MasterPageHandle.NAME_PROP,
				ReportDesignConstants.MASTER_PAGE_ELEMENT );
		TextSection nameSection = new TextSection( nameProvider.getDisplayName( ),
				container,
				true );
		nameSection.setProvider( nameProvider );
		nameSection.setWidth( 200 );
		nameSection.setGridPlaceholder( 4, true );
		addSection( PageSectionId.MASTER_PAGE_NAME, nameSection );

		SeperatorSection seperatorSection = new SeperatorSection( container,
				SWT.HORIZONTAL );
		addSection( PageSectionId.MASTER_PAGE_SEPERATOR, seperatorSection );

		UnitPropertyDescriptorProvider headHeightProvider = new UnitPropertyDescriptorProvider( SimpleMasterPageHandle.HEADER_HEIGHT_PROP,
				ReportDesignConstants.SIMPLE_MASTER_PAGE_ELEMENT );
		UnitSection headHeightSection = new UnitSection( headHeightProvider.getDisplayName( ),
				container,
				true );
		headHeightSection.setProvider( headHeightProvider );
		headHeightSection.setWidth( 200 );
		headHeightSection.setLayoutNum( 2 );
		addSection( PageSectionId.MASTER_PAGE_HEAD_HEIGHT, headHeightSection );

		ColorPropertyDescriptorProvider colorProvider = new ColorPropertyDescriptorProvider( StyleHandle.BACKGROUND_COLOR_PROP,
				ReportDesignConstants.STYLE_ELEMENT );
		ColorSection colorSection = new ColorSection( colorProvider.getDisplayName( ),
				container,
				true );
		colorSection.setProvider( colorProvider );
		colorSection.setWidth( 200 );
		colorSection.setLayoutNum( 4 );
		colorSection.setGridPlaceholder( 2, true );
		addSection( PageSectionId.MASTER_PAGE_COLOR, colorSection );

		UnitPropertyDescriptorProvider footHeightProvider = new UnitPropertyDescriptorProvider( SimpleMasterPageHandle.FOOTER_HEIGHT_PROP,
				ReportDesignConstants.SIMPLE_MASTER_PAGE_ELEMENT );
		UnitSection footHeightSection = new UnitSection( footHeightProvider.getDisplayName( ),
				container,
				true );
		footHeightSection.setProvider( footHeightProvider );
		footHeightSection.setWidth( 200 );
		footHeightSection.setLayoutNum( 2 );
		addSection( PageSectionId.MASTER_PAGE_FOOT_HEIGHT, footHeightSection );

		ComboPropertyDescriptorProvider orientationProvider = new ComboPropertyDescriptorProvider( MasterPageHandle.ORIENTATION_PROP,
				ReportDesignConstants.MASTER_PAGE_ELEMENT );
		ComboSection orientationSection = new ComboSection( orientationProvider.getDisplayName( ),
				container,
				true );
		orientationSection.setProvider( orientationProvider );
		orientationSection.setLayoutNum( 4 );
		orientationSection.setGridPlaceholder( 2, true );
		orientationSection.setWidth( 200 );
		addSection( PageSectionId.MASTER_PAGE_ORIENTATION, orientationSection );

		SeperatorSection seperatorSection1 = new SeperatorSection( container,
				SWT.HORIZONTAL );
		addSection( PageSectionId.MASTER_PAGE_SEPERATOR_1, seperatorSection1 );

		UnitPropertyDescriptorProvider widthProvider = new UnitPropertyDescriptorProvider( MasterPageHandle.WIDTH_PROP,
				ReportDesignConstants.MASTER_PAGE_ELEMENT );
		widthSection = new UnitSection( widthProvider.getDisplayName( ),
				container,
				true );
		widthSection.setProvider( widthProvider );
		widthSection.setWidth( 200 );
		widthSection.setLayoutNum( 2 );
		addSection( PageSectionId.MASTER_PAGE_WIDTH, widthSection );

		typeProvider = new ComboPropertyDescriptorProvider( MasterPageHandle.TYPE_PROP,
				ReportDesignConstants.MASTER_PAGE_ELEMENT );
		ComboSection typeSection = new ComboSection( typeProvider.getDisplayName( ),
				container,
				true );
		typeSection.setProvider( typeProvider );
		typeSection.setGridPlaceholder( 2, true );
		typeSection.setLayoutNum( 4 );
		typeSection.setWidth( 200 );
		addSection( PageSectionId.MASTER_PAGE_TYPE, typeSection );

		UnitPropertyDescriptorProvider heightProvider = new UnitPropertyDescriptorProvider( MasterPageHandle.HEIGHT_PROP,
				ReportDesignConstants.MASTER_PAGE_ELEMENT );
		heightSection = new UnitSection( heightProvider.getDisplayName( ),
				container,
				true );
		heightSection.setProvider( heightProvider );
		heightSection.setWidth( 200 );
		heightSection.setGridPlaceholder( 4, true );
		addSection( PageSectionId.MASTER_PAGE_HEIGHT, heightSection );

		// WidgetUtil.buildGridControl( container, propertiesMap,
		// ReportDesignConstants.MASTER_PAGE_ELEMENT,
		// MasterPageHandle.NAME_PROP, 1, false );

		// WidgetUtil.buildGridControl( container, propertiesMap,
		// ReportDesignConstants.STYLE_ELEMENT,
		// StyleHandle.BACKGROUND_COLOR_PROP, 1, false );
		//		
		// WidgetUtil.createGridPlaceholder( container, 1, true );

		/*
		 * WidgetUtil.buildGridControl( container, propertiesMap,
		 * ReportDesignConstants.MASTER_PAGE_ELEMENT,
		 * MasterPageHandle.ORIENTATION_PROP, 1, false );
		 * 
		 * Label separator = new Label( container, SWT.SEPARATOR |
		 * SWT.HORIZONTAL ); GridData data = new GridData( );
		 * data.horizontalSpan = 5; data.grabExcessHorizontalSpace = false;
		 * data.horizontalAlignment = GridData.FILL; separator.setLayoutData(
		 * data );
		 * 
		 * WidgetUtil.buildGridControl( container, propertiesMap,
		 * ReportDesignConstants.MASTER_PAGE_ELEMENT,
		 * MasterPageHandle.TYPE_PROP, 1, false ); pageSizeDescriptor =
		 * (IPropertyDescriptor) propertiesMap.get( MasterPageHandle.TYPE_PROP );
		 * 
		 * WidgetUtil.createGridPlaceholder( container, 3, false );
		 * 
		 * widthPane = (Composite) WidgetUtil.buildGridControl( container,
		 * propertiesMap, ReportDesignConstants.MASTER_PAGE_ELEMENT,
		 * MasterPageHandle.WIDTH_PROP, 1, false );
		 * 
		 * heightPane = (Composite) WidgetUtil.buildGridControl( container,
		 * propertiesMap, ReportDesignConstants.MASTER_PAGE_ELEMENT,
		 * MasterPageHandle.HEIGHT_PROP, 1, false );
		 */
	}

	public void refresh( )
	{
		super.refresh( );
		resetCustomStyle( );
	}

	private boolean checkControl( )
	{
		return widthSection != null
				&& widthSection.getUnitComboControl( ) != null
				&& !widthSection.getUnitComboControl( )
						.getControl( )
						.isDisposed( );
	}

	private void resetCustomStyle( )
	{
		if ( checkControl( ) )
		{
			if ( typeProvider.load( )
					.equals( DesignChoiceConstants.PAGE_SIZE_CUSTOM ) )
			{
				widthSection.getUnitComboControl( ).setReadOnly( false );
				heightSection.getUnitComboControl( ).setReadOnly( false );
			}
			else
			{
				widthSection.getUnitComboControl( ).setReadOnly( true );
				heightSection.getUnitComboControl( ).setReadOnly( true );
			}
		}
	}

	public void postElementEvent( )
	{
		super.postElementEvent( );
		resetCustomStyle( );
	}


}