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

import org.eclipse.birt.report.designer.internal.ui.dialogs.resource.NewResourceFileDialog;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.JarFileFormProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.JsFileFormProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.TextPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.FormSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SeperatorSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextAndButtonSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.FileFormPropertyDescriptor;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.FormPropertyDescriptor;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 */

public class ResourcesPage extends AttributePage
{

	private TextAndButtonSection includeSourceSection;

	public void buildUI( Composite parent )
	{
		super.buildUI( parent );
		// TODO Auto-generated method stub
		container.setLayout( WidgetUtil.createGridLayout( 5 ,15) );

		TextPropertyDescriptorProvider includeSourceProvider = new TextPropertyDescriptorProvider( ModuleHandle.INCLUDE_RESOURCE_PROP,
				ReportDesignConstants.MODULE_ELEMENT );
		includeSourceSection = new TextAndButtonSection( includeSourceProvider.getDisplayName( ),
				container,
				true );
		includeSourceSection.setProvider( includeSourceProvider );
		includeSourceSection.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				final NewResourceFileDialog dialog = new NewResourceFileDialog( );
				if ( dialog.open( ) == Window.OK )
				{
					String path = dialog.getPath( );
					if ( path.lastIndexOf( "." ) > 0 ) //$NON-NLS-1$
					{
						path = path.substring( 0, path.lastIndexOf( "." ) ); //$NON-NLS-1$
					}
					includeSourceSection.setStringValue( path );
					includeSourceSection.forceFocus( );
				}
			}

		} );
		includeSourceSection.setWidth( 500 );
		includeSourceSection.setButtonText( Messages.getString( "ReportPage.text.Resource.Add" ) ); //$NON-NLS-1$
		includeSourceSection.setButtonWidth( 60 );
		includeSourceSection.setGridPlaceholder( 1, true );
		addSection( PageSectionId.RESOURCE_INCLUDE, includeSourceSection );

		SeperatorSection seperatorSection = new SeperatorSection( container,
				SWT.HORIZONTAL );
		addSection( PageSectionId.RESOURCE_SEPERATOR, seperatorSection );

		JarFileFormProvider jarFileProvider = new JarFileFormProvider( );
		FormSection jarFileSection = new FormSection( jarFileProvider.getDisplayName( ),
				container,
				true );
		jarFileSection.setCustomForm( new FileFormPropertyDescriptor( true ) );
		jarFileSection.setProvider( jarFileProvider );
		jarFileSection.showDisplayLabel( true );
		jarFileSection.setButtonWithDialog( true );
		jarFileSection.setStyle( FormPropertyDescriptor.FULL_FUNCTION );
		jarFileSection.setFillForm( true );
		jarFileSection.setWidth( 500 );
		jarFileSection.setHeight( 120 );
		jarFileSection.setDisplayLabelStyle( SWT.HORIZONTAL );
		jarFileSection.setGridPlaceholder( 1, true );
		addSection( PageSectionId.RESOURCE_JARFILE, jarFileSection );
		
		JsFileFormProvider jsFileProvider = new JsFileFormProvider( );
		FormSection jsFileSection = new FormSection( jarFileProvider.getDisplayName( ),
				container,
				true );
		jsFileSection.setCustomForm( new FileFormPropertyDescriptor( true ) );
		jsFileSection.setProvider( jsFileProvider );
		jsFileSection.showDisplayLabel( true );
		jsFileSection.setButtonWithDialog( true );
		jsFileSection.setStyle( FormPropertyDescriptor.FULL_FUNCTION );
		jsFileSection.setFillForm( true );
		jsFileSection.setWidth( 500 );
		jsFileSection.setHeight( 120 );
		jsFileSection.setDisplayLabelStyle( SWT.HORIZONTAL );
		jsFileSection.setGridPlaceholder( 1, true );
		addSection( PageSectionId.RESOURCE_JAVASCRIPTFILE, jsFileSection );

		createSections( );
		layoutSections( );

	}
}
