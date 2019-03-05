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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.ComboPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.SimpleComboPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ComboSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SeperatorSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SimpleComboSection;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * The section attribute page of DE element.
 */
public class SectionPage extends ResetAttributePage
{

	private SimpleComboSection masterSection;
	private SeperatorSection sepSection;

	public void buildUI( Composite parent  )
	{
		super.buildUI( parent );
		container.setLayout( WidgetUtil.createGridLayout( 5 ) );

		// Defines providers.

		ComboPropertyDescriptorProvider beforeProvider = new ComboPropertyDescriptorProvider( StyleHandle.PAGE_BREAK_BEFORE_PROP,
				ReportDesignConstants.STYLE_ELEMENT );
		beforeProvider.enableReset( true );

		SimpleComboPropertyDescriptorProvider masterProvider = new SimpleComboPropertyDescriptorProvider( StyleHandle.MASTER_PAGE_PROP,
				ReportDesignConstants.STYLE_ELEMENT );
		
		masterProvider.enableReset(true);

		ComboPropertyDescriptorProvider afterProvider = new ComboPropertyDescriptorProvider( StyleHandle.PAGE_BREAK_AFTER_PROP,
				ReportDesignConstants.STYLE_ELEMENT );
		afterProvider.enableReset( true );
		
		ComboPropertyDescriptorProvider insideProvider = new ComboPropertyDescriptorProvider( StyleHandle.PAGE_BREAK_INSIDE_PROP,
				ReportDesignConstants.STYLE_ELEMENT );
		insideProvider.enableReset( true );

		// Defines sections.

		ComboSection beforeSection = new ComboSection( beforeProvider.getDisplayName( ),
				container,
				true );
		
		ComboSection insideSection = new ComboSection( insideProvider.getDisplayName( ),
				container,
				true );

		masterSection = new SimpleComboSection( masterProvider.getDisplayName( ),
						container,
						true );
		ComboSection afterSection = new ComboSection( afterProvider.getDisplayName( ),
				container,
				true );
		
		sepSection = new SeperatorSection(container,SWT.HORIZONTAL);
		beforeSection.setProvider( beforeProvider );
		masterSection.setProvider( masterProvider );
		afterSection.setProvider( afterProvider );
		insideSection.setProvider( insideProvider );
		
		// Sets widths.

		beforeSection.setWidth( 200 );
		masterSection.setWidth( 200 );
		afterSection.setWidth( 200 );
		insideSection.setWidth( 200 );

		// Sets layout num.

		beforeSection.setLayoutNum( 2 );
		afterSection.setLayoutNum( 3 );

		// Sets fill grid num.

		masterSection.setGridPlaceholder( 3, true );
		afterSection.setGridPlaceholder( 1, true );
		insideSection.setGridPlaceholder( 3, true );

		// Adds sections into container page.

		addSection( PageSectionId.SECION_PAGE_BREAK_BEFORE, beforeSection ); //$NON-NLS-1$
		addSection( PageSectionId.SECION_PAGE_BREAK_AFTER, afterSection ); //$NON-NLS-1$
		addSection( PageSectionId.SECION_PAGE_BREAK_INSIDE, insideSection ); //$NON-NLS-1$
		addSection( PageSectionId.SECION_SEPERATOR, sepSection ); //$NON-NLS-1$
		addSection( PageSectionId.SECION_MASTER_PAGE, masterSection ); //$NON-NLS-1$

		createSections( );
		layoutSections( );
	}
	
	public void refresh( )
	{
		super.refresh( );
		setVisible( );
		container.layout( true );
		container.redraw( );
	}
	
	protected void setVisible(){
		if ( DEUtil.getInputSize( input ) == 1
				&& DEUtil.getInputFirstElement( input ) instanceof DesignElementHandle
				&&  isElementInMasterPage( (DesignElementHandle)DEUtil.getInputFirstElement( input ) ) )
		{
			masterSection.setVisible( false );
			sepSection.setVisible( false );
		}
		else{
			masterSection.setVisible( true );
			sepSection.setVisible( true );
		}
	}
	
	protected boolean isElementInMasterPage( DesignElementHandle elementHandle )
	{
		ModuleHandle root = elementHandle.getRoot( );
		DesignElementHandle container = elementHandle;
		while ( container != null && container != root )
		{
			if ( container instanceof MasterPageHandle )
			{
				return true;
			}
			container = container.getContainer( );
		}

		return false;
	}
}
