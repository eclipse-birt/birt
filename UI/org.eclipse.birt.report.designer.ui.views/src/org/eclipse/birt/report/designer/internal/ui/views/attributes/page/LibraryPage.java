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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.LibraryNameSpaceDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SeperatorSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextSection;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * The general attribute page of Library element.
 */

public class LibraryPage extends ModulePage {

	public void buildUI(Composite parent) {
		super.buildUI(parent);

		SeperatorSection seperatorSection = new SeperatorSection(container, SWT.HORIZONTAL);
		addSection(PageSectionId.LIBRARY_SEPERATOR, seperatorSection);

		LibraryNameSpaceDescriptorProvider libraryProvider = new LibraryNameSpaceDescriptorProvider();
		TextSection librarySection = new TextSection(libraryProvider.getDisplayName(), container, true);
		librarySection.setProvider(libraryProvider);
		librarySection.setWidth(500);
		librarySection.setGridPlaceholder(2, true);
		addSection(PageSectionId.LIBRARY_LIBRARY, librarySection);

		createSections();
		layoutSections();

//		labels = new Label[3];
//		labels[0]=WidgetUtil.createHorizontalLine( this, 5 );
//
//		labels[1]=new Label( this, SWT.NONE );
//		labels[1].setText( Messages.getString( "LibraryPage.Label.Namespace" ) ); //$NON-NLS-1$
//		namespace = new Text( this, SWT.BORDER | SWT.SINGLE | SWT.READ_ONLY );
//		GridData gd = new GridData( );
//		gd.widthHint = 500;
//		gd.horizontalSpan = 3;
//		namespace.setLayoutData( gd );
//
//		labels[2]=WidgetUtil.createGridPlaceholder( this, 1, true );
	}

	public String getElementType() {
		return ReportDesignConstants.LIBRARY_ELEMENT;
	}

//	protected void refreshValues( Set propertiesSet )
//	{
//		super.refreshValues( propertiesSet );
//		boolean visible = false;
//		if ( input.size( ) == 1 && input.get( 0 ) instanceof LibraryHandle )
//		{
//			LibraryHandle handle = (LibraryHandle) input.get( 0 );
//			if ( DEUtil.isIncluded( handle ) )
//			{
//				namespace.setText( handle.getNamespace( ) );
//				visible = true;
//			}
//		}
//		setControlVisible( visible );
//	}
//
//	private void setControlVisible( boolean visible )
//	{
//		namespace.setVisible( visible );
//		for ( int i = 0; i < labels.length; i++ )
//		{
//			labels[i].setVisible( visible );
//		}
//	}

}
