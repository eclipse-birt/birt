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

import java.io.ByteArrayOutputStream;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.resource.AddImageResourceFileFolderSelectionDialog;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.TextPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextAndButtonSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextAndTwoButtonSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextSection;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.dialogs.ThumbnailBuilder;
import org.eclipse.birt.report.designer.ui.views.attributes.IPropertyDescriptor;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;

/**
 * The general attribute page of Report element.
 */
public class ReportPage extends ModulePage
{

	private TextAndTwoButtonSection prvImageSection;

	public void buildUI( Composite parent  )
	{
		super.buildUI( parent );
		
		TextPropertyDescriptorProvider displayProvider = new TextPropertyDescriptorProvider( ModuleHandle.DISPLAY_NAME_PROP,
				ReportDesignConstants.REPORT_DESIGN_ELEMENT );
		TextSection displaySection = new TextSection( displayProvider.getDisplayName( ),
				container,
				true );
		displaySection.setProvider( displayProvider );
		displaySection.setWidth( 500 );
		displaySection.setGridPlaceholder( 2, true );
		addSection( PageSectionId.REPORT_DISPLAY, displaySection );

		TextPropertyDescriptorProvider prvImageProvider = new TextPropertyDescriptorProvider( ReportDesignHandle.ICON_FILE_PROP,
				ReportDesignConstants.REPORT_DESIGN_ELEMENT );
		prvImageSection = new TextAndTwoButtonSection( prvImageProvider.getDisplayName( ),
						container,
						true );
		prvImageSection.setProvider( prvImageProvider );
		prvImageSection.addFirstSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				String fileName = null;
				AddImageResourceFileFolderSelectionDialog dlg = new AddImageResourceFileFolderSelectionDialog( );
				if ( dlg.open( ) == Window.OK )
				{
					fileName = dlg.getPath( );
				}
				if ( fileName == null || fileName.trim( ).length( ) == 0 )
				{
					return;
				}

				prvImageSection.setStringValue( fileName );
				prvImageSection.forceFocus( );
			}

		} );
		
		prvImageSection.addSecondSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				ThumbnailBuilder dialog = new ThumbnailBuilder( );
				ReportDesignHandle handle = (ReportDesignHandle) SessionHandleAdapter.getInstance( )
						.getReportDesignHandle( );
				dialog.setReportDesignHandle( handle );
				if ( dialog.open( ) != Dialog.OK )
				{
					Image image = dialog.getImage( );
					if ( image != null )
					{
						image.dispose( );
						image = null;
					}
					return;
				}
				if ( dialog.shouldSetThumbnail( ) )
				{
					Image image = dialog.getImage( );
					ImageData imageData = image.getImageData( );
					ImageLoader imageLoader = new ImageLoader( );
					imageLoader.data = new ImageData[1];
					imageLoader.data[0] = imageData;
					ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
					imageLoader.save( outputStream, SWT.IMAGE_BMP );
					try
					{
						handle.setThumbnail( outputStream.toByteArray( ) );
					}
					catch ( SemanticException e1 )
					{
						ExceptionHandler.handle( e1 );
					}
					if ( image != null )
					{
						image.dispose( );
						image = null;
					}

				}
				else
				{
					if ( handle.getThumbnail( ) != null
							&& handle.getThumbnail( ).length != 0 )
					{
						try
						{
							handle.deleteThumbnail( );
						}
						catch ( SemanticException e1 )
						{
							ExceptionHandler.handle( e1 );
						}

					}
				}
			}

		} );
		
		prvImageSection.setWidth( 500 );
		prvImageSection.setFristButtonText(  Messages.getString( "ReportPage.text.Browse" ) );
		prvImageSection.setSecondButtonText( Messages.getString( "ReportPage.text.Thumbnail" ) );

		addSection( PageSectionId.REPORT_PRVIMAGE, prvImageSection );
		
		createSections( );
		layoutSections( );

	}
}