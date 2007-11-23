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
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SeperatorSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextSection;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public abstract class GeneralPage extends AttributePage
{

	private TextSection librarySection;
	private SeperatorSection seperatorSection;

	public void buildUI( Composite parent )
	{
		super.buildUI( parent );
		container.setLayout( WidgetUtil.createGridLayout( 6, 15 ) );

		LibraryDescriptorProvider provider = new LibraryDescriptorProvider( );
		librarySection = new TextSection( provider.getDisplayName( ),
				container,
				true );
		librarySection.setProvider( provider );
		librarySection.setGridPlaceholder( 2, true );
		addSection( PageSectionId.GENERAL_LIBRARY, librarySection );

		seperatorSection = new SeperatorSection( container, SWT.HORIZONTAL );
		addSection( PageSectionId.GENERAL_SEPERATOR, seperatorSection );

		buildContent( );

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

	/**
	 * Builds UI content of this page.
	 * 
	 * @param content
	 *            parent composite.
	 */
	abstract void buildContent( );

}
