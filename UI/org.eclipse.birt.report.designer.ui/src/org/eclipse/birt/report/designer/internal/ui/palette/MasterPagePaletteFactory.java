/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.palette;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.core.IReportElementConstants;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.extensions.GuiExtensionManager;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.extensions.IExtension;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IPreferenceConstants;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteRoot;

/**
 * Factory to populate Master Page graphical editor palette root.
 *  
 */
public class MasterPagePaletteFactory extends DesignerPaletteFactory
{

	private static final String AUTOTEXT_TOOLTIP_CONFIDENTIAL_PAGE = Messages.getString( "MasterPagePaletteFactory.AutotextTooltip.ConfidentialPage" ); //$NON-NLS-1$

	private static final String AUTOTEXT_TOOLTIP_AUTHOR_PAGE_DATE = Messages.getString( "MasterPagePaletteFactory.AutotextTooltip.AuthorPageDate" ); //$NON-NLS-1$

	private static final String AUTOTEXT_TOOLTIP_PAGE_X_OF_Y = Messages.getString( "MasterPagePaletteFactory.AutotextTooltip.PageXofY" ); //$NON-NLS-1$

	private static final String AUTOTEXT_TOOLTIP_LAST_PRINTED = Messages.getString( "MasterPagePaletteFactory.AutotextTooltip.LastPrinted" ); //$NON-NLS-1$

	private static final String AUTOTEXT_TOOLTIP_FILENAME = Messages.getString( "MasterPagePaletteFactory.AutoTextTooltip.Filename" ); //$NON-NLS-1$

	private static final String AUTOTEXT_TOOLTIP_CREATED_BY = Messages.getString( "MasterPagePaletteFactory.AutotextTooltip.CreatedBy" ); //$NON-NLS-1$

	private static final String AUTOTEXT_TOOLTIP_CREATED_ON = Messages.getString( "MasterPagePaletteFactory.AutotextTooltip.CreatedOn" ); //$NON-NLS-1$

	private static final String AUTOTEXT_TOOLTIP_DATE = Messages.getString( "MasterPagePaletteFactory.AutotextTooltip.Date" ); //$NON-NLS-1$

	private static final String AUTOTEXT_TOOLTIP_PAGE = Messages.getString( "MasterPagePaletteFactory.AutotextTooltip.Page" ); //$NON-NLS-1$

	private static final String AUTOTEXT_LABEL = Messages.getString( "MasterPagePaletteFactory.Autotext.Label" ); //$NON-NLS-1$

	private static final String AUTOTEXT_PAGE_X_OF_Y = Messages.getString( "MasterPagePaletteFactory.Autotext.PageXofY" ); //$NON-NLS-1$

	private static final String AUTOTEXT_LAST_PRINTED = Messages.getString( "MasterPagePaletteFactory.Autotext.LastPrinted" ); //$NON-NLS-1$

	private static final String AUTOTEXT_FILENAME = Messages.getString( "MasterPagePaletteFactory.Autotext.Filename" ); //$NON-NLS-1$

	private static final String AUTOTEXT_CREATED_BY = Messages.getString( "MasterPagePaletteFactory.Autotext.CreatedBy" ); //$NON-NLS-1$

	private static final String AUTOTEXT_CREATED_ON = Messages.getString( "MasterPagePaletteFactory.Autotext.CreatedOn" ); //$NON-NLS-1$

	private static final String AUTOTEXT_DATE = Messages.getString( "MasterPagePaletteFactory.Autotext.Date" ); //$NON-NLS-1$

	private static final String AUTOTEXT_CONFIDENTIAL_PAGE = Messages.getString( "MasterPagePaletteFactory.Autotext.Confidential_Page" ); //$NON-NLS-1$

	private static final String AUTOTEXT_AUTHOR_PAGE_DATE = Messages.getString( "MasterPagePaletteFactory.Autotext.Author_Page_Date" ); //$NON-NLS-1$

	private static final String AUTOTEXT_PAGE = Messages.getString( "MasterPagePaletteFactory.Autotext.Page" ); //$NON-NLS-1$

	/**
	 * Creates the palette and returns the palette
	 * 
	 * @return the editor palette
	 */
	public static PaletteRoot createPalette( )
	{
		PaletteRoot root = BasePaletteFactory.createPalette( );
		root.addAll( createCategories( ) );
		root.add( createAutoTextDrawer( ) );
		
		IExtension extension = new IExtension.Stub( ) 
		{

			public String getExtendsionIdentify( )
			{
				return GuiExtensionManager.PALETTE_MASTERPAGE;
			}
		};
		GuiExtensionManager.doExtension( extension, root );
		return root;
	}

	/**
	 * Creates BIRT Master Page specified categories and items.
	 * 
	 * @return PaletteContainer containing BIRT Master Page specified categories
	 */
	private static PaletteContainer createAutoTextDrawer( )
	{
		PaletteCategroy category = new PaletteCategroy( IPreferenceConstants.PALETTE_AUTOTEXT,
				AUTOTEXT_LABEL,
				null );		
		List entries = new ArrayList( );

		CombinedTemplateCreationEntry combined = new ReportCombinedTemplateCreationEntry( AUTOTEXT_PAGE,
				AUTOTEXT_TOOLTIP_PAGE,
				IReportElementConstants.AUTOTEXT_PAGE,
				new ReportElementFactory( IReportElementConstants.AUTOTEXT_PAGE ),
				ReportPlatformUIImages.getImageDescriptor( IReportGraphicConstants.ICON_ELEMENT_TEXT ),
				ReportPlatformUIImages.getImageDescriptor( IReportGraphicConstants.ICON_ELEMENT_TEXT ),
				new TextToolExtends( ) );
		entries.add( combined );

		combined = new ReportCombinedTemplateCreationEntry( AUTOTEXT_AUTHOR_PAGE_DATE,
				AUTOTEXT_TOOLTIP_AUTHOR_PAGE_DATE,
				IReportElementConstants.AUTOTEXT_AUTHOR_PAGE_DATE,
				new ReportElementFactory( IReportElementConstants.AUTOTEXT_AUTHOR_PAGE_DATE ),
				ReportPlatformUIImages.getImageDescriptor( IReportGraphicConstants.ICON_ELEMENT_GRID ),
				ReportPlatformUIImages.getImageDescriptor( IReportGraphicConstants.ICON_ELEMENT_GRID ),
				new GridToolExtends( ) );
		entries.add( combined );

		combined = new ReportCombinedTemplateCreationEntry( AUTOTEXT_CONFIDENTIAL_PAGE,
				AUTOTEXT_TOOLTIP_CONFIDENTIAL_PAGE,
				IReportElementConstants.AUTOTEXT_CONFIDENTIAL_PAGE,
				new ReportElementFactory( IReportElementConstants.AUTOTEXT_CONFIDENTIAL_PAGE ),
				ReportPlatformUIImages.getImageDescriptor( IReportGraphicConstants.ICON_ELEMENT_GRID ),
				ReportPlatformUIImages.getImageDescriptor( IReportGraphicConstants.ICON_ELEMENT_GRID ),
				new GridToolExtends( ) );
		entries.add( combined );

		combined = new ReportCombinedTemplateCreationEntry( AUTOTEXT_DATE,
				AUTOTEXT_TOOLTIP_DATE,
				IReportElementConstants.AUTOTEXT_DATE,
				new ReportElementFactory( IReportElementConstants.AUTOTEXT_DATE ),
				ReportPlatformUIImages.getImageDescriptor( IReportGraphicConstants.ICON_ELEMENT_TEXT ),
				ReportPlatformUIImages.getImageDescriptor( IReportGraphicConstants.ICON_ELEMENT_TEXT ),
				new TextToolExtends( ) );
		entries.add( combined );

		combined = new ReportCombinedTemplateCreationEntry( AUTOTEXT_CREATED_ON,
				AUTOTEXT_TOOLTIP_CREATED_ON,
				IReportElementConstants.AUTOTEXT_CREATEDON,
				new ReportElementFactory( IReportElementConstants.AUTOTEXT_CREATEDON ),
				ReportPlatformUIImages.getImageDescriptor( IReportGraphicConstants.ICON_ELEMENT_TEXT ),
				ReportPlatformUIImages.getImageDescriptor( IReportGraphicConstants.ICON_ELEMENT_TEXT ),
				new TextToolExtends( ) );
		entries.add( combined );

		combined = new ReportCombinedTemplateCreationEntry( AUTOTEXT_CREATED_BY,
				AUTOTEXT_TOOLTIP_CREATED_BY,
				IReportElementConstants.AUTOTEXT_CREATEDBY,
				new ReportElementFactory( IReportElementConstants.AUTOTEXT_CREATEDBY ),
				ReportPlatformUIImages.getImageDescriptor( IReportGraphicConstants.ICON_ELEMENT_TEXT ),
				ReportPlatformUIImages.getImageDescriptor( IReportGraphicConstants.ICON_ELEMENT_TEXT ),
				new TextToolExtends( ) );
		entries.add( combined );

		combined = new ReportCombinedTemplateCreationEntry( AUTOTEXT_FILENAME,
				AUTOTEXT_TOOLTIP_FILENAME,
				IReportElementConstants.AUTOTEXT_FILENAME,
				new ReportElementFactory( IReportElementConstants.AUTOTEXT_FILENAME ),
				ReportPlatformUIImages.getImageDescriptor( IReportGraphicConstants.ICON_ELEMENT_TEXT ),
				ReportPlatformUIImages.getImageDescriptor( IReportGraphicConstants.ICON_ELEMENT_TEXT ),
				new TextToolExtends( ) );
		entries.add( combined );

		combined = new ReportCombinedTemplateCreationEntry( AUTOTEXT_LAST_PRINTED,
				AUTOTEXT_TOOLTIP_LAST_PRINTED,
				IReportElementConstants.AUTOTEXT_LASTPRINTED,
				new ReportElementFactory( IReportElementConstants.AUTOTEXT_LASTPRINTED ),
				ReportPlatformUIImages.getImageDescriptor( IReportGraphicConstants.ICON_ELEMENT_TEXT ),
				ReportPlatformUIImages.getImageDescriptor( IReportGraphicConstants.ICON_ELEMENT_TEXT ),
				new TextToolExtends( ) );
		entries.add( combined );

		combined = new ReportCombinedTemplateCreationEntry( AUTOTEXT_PAGE_X_OF_Y,
				AUTOTEXT_TOOLTIP_PAGE_X_OF_Y,
				IReportElementConstants.AUTOTEXT_PAGEXOFY,
				new ReportElementFactory( IReportElementConstants.AUTOTEXT_PAGEXOFY ),
				ReportPlatformUIImages.getImageDescriptor( IReportGraphicConstants.ICON_ELEMENT_TEXT ),
				ReportPlatformUIImages.getImageDescriptor( IReportGraphicConstants.ICON_ELEMENT_TEXT ),
				new TextToolExtends( ) );
		entries.add( combined );

		category .addAll( entries );
		return category ;
	}
}