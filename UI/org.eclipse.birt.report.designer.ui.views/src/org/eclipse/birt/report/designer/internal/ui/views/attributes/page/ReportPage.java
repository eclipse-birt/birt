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
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.ComboPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.TextPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ComboSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextAndTwoButtonSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextSection;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.ThumbnailBuilder;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Composite;

/**
 * The general attribute page of Report element.
 */
public class ReportPage extends ModulePage
{

	private TextAndTwoButtonSection prvImageSection;

	public void buildUI( Composite parent )
	{
		super.buildUI( parent );

		ComboPropertyDescriptorProvider layoutProvider = new ComboPropertyDescriptorProvider( ReportDesignHandle.LAYOUT_PREFERENCE_PROP,
				ReportDesignConstants.REPORT_DESIGN_ELEMENT );
		ComboSection layoutSection = new ComboSection( layoutProvider.getDisplayName( ),
				container,
				true );
		layoutSection.setProvider( layoutProvider );
		layoutSection.setWidth( 500 );
		layoutSection.setGridPlaceholder( 2, true );
		addSection( PageSectionId.REPORT_LAYOUT_PREFERENCE, layoutSection );

		/*
		 * If BiDi support is enabled - BiDi Orientation should be added to
		 * properties view
		 */

		ComboPropertyDescriptorProvider biDiOrientatonProvider = new ComboPropertyDescriptorProvider( ReportDesignHandle.BIDI_ORIENTATION_PROP,
				ReportDesignConstants.REPORT_DESIGN_ELEMENT );
		ComboSection biDiOrientatonSection = new ComboSection( biDiOrientatonProvider.getDisplayName( ),
				container,
				true );
		biDiOrientatonSection.setProvider( biDiOrientatonProvider );
		biDiOrientatonSection.setWidth( 500 );
		biDiOrientatonSection.setGridPlaceholder( 2, true );
		addSection( PageSectionId.REPORT_BIDI_ORIENTATION,
				biDiOrientatonSection );

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
		prvImageSection.addSecondSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				ThumbnailBuilder dialog = new ThumbnailBuilder( );
				dialog.setImageName( prvImageSection.getTextControl( )
						.getText( ) );
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
					imageLoader.save( outputStream, SWT.IMAGE_PNG );
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
					prvImageSection.setStringValue( dialog.getImageName( ) );
					prvImageSection.forceFocus( );

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

					prvImageSection.setStringValue( "" ); //$NON-NLS-1$
					prvImageSection.forceFocus( );
				}

			}

		} );

		prvImageSection.setWidth( 500 );
		// prvImageSection.setFristButtonText( Messages.getString(
		// "ReportPage.text.Browse" ) );
		prvImageSection.setSecondButtonText( "..." ); //$NON-NLS-1$
		prvImageSection.setSecondButtonTooltipText( Messages.getString( "ReportPage.PreviewImage.Button.ToolTip" ) ); //$NON-NLS-1$

		addSection( PageSectionId.REPORT_PRVIMAGE, prvImageSection );

		createSections( );
		layoutSections( );

	}

}