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
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.AbstractToolHandleExtends;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IPreferenceConstants;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.ISharedImages;

/**
 * Factory to populate Master Page graphical editor palette root.
 *  
 */
public class MasterPagePaletteFactory extends DesignerPaletteFactory
{

	private static final String AUTOTEXT_TOOLTIP_CONFIDENTIAL_PAGE = Messages.getString( "MasterPagePaletteFactory.AutotextTooltip.ConfidentialPage" ); //$NON-NLS-1$

	private static final String AUTOTEXT_TOOLTIP_TOTAL_PAGE_COUNT = Messages.getString( "MasterPagePaletteFactory.AutotextTooltip.TotalPageCount" ); //$NON-NLS-1$

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

	private static final String AUTOTEXT_TOTAL_PAGE_COUNT = Messages.getString( "MasterPagePaletteFactory.Autotext.TotalPageCount" ); //$NON-NLS-1$

	private static final String AUTOTEXT_LAST_PRINTED = Messages.getString( "MasterPagePaletteFactory.Autotext.LastPrinted" ); //$NON-NLS-1$

	private static final String AUTOTEXT_FILENAME = Messages.getString( "MasterPagePaletteFactory.Autotext.Filename" ); //$NON-NLS-1$

	private static final String AUTOTEXT_CREATED_BY = Messages.getString( "MasterPagePaletteFactory.Autotext.CreatedBy" ); //$NON-NLS-1$

	private static final String AUTOTEXT_CREATED_ON = Messages.getString( "MasterPagePaletteFactory.Autotext.CreatedOn" ); //$NON-NLS-1$

	private static final String AUTOTEXT_DATE = Messages.getString( "MasterPagePaletteFactory.Autotext.Date" ); //$NON-NLS-1$

	private static final String AUTOTEXT_CONFIDENTIAL_PAGE = Messages.getString( "MasterPagePaletteFactory.Autotext.Confidential_Page" ); //$NON-NLS-1$

	private static final String AUTOTEXT_AUTHOR_PAGE_DATE = Messages.getString( "MasterPagePaletteFactory.Autotext.Author_Page_Date" ); //$NON-NLS-1$

	private static final String AUTOTEXT_PAGE = Messages.getString( "MasterPagePaletteFactory.Autotext.Page" ); //$NON-NLS-1$
	
	private static final String AUTOTEXT_VARIABLE = Messages.getString( "MasterPagePaletteFactory.Autotext.Variable" ); //$NON-NLS-1$
	
	private static final String AUTOTEXT_TOOLTIP_VARIABLE = Messages.getString( "MasterPagePaletteFactory.AutotextTooltip.Variable" ); //$NON-NLS-1$

	
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

		IExtension extension = new IExtension.Stub( ) {

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
		PaletteCategory category = new PaletteCategory( IPreferenceConstants.PALETTE_AUTOTEXT,
				AUTOTEXT_LABEL,
				ReportPlatformUIImages.getImageDescriptor( ISharedImages.IMG_OBJ_FOLDER ) );
		List entries = new ArrayList( );

		CombinedTemplateCreationEntry combined = null;

		combined = createAutoText( AUTOTEXT_PAGE,
				AUTOTEXT_TOOLTIP_PAGE,
				IReportElementConstants.AUTOTEXT_PAGE );
		entries.add( combined );
		
		combined = createAutoText( AUTOTEXT_TOTAL_PAGE_COUNT,
				AUTOTEXT_TOOLTIP_TOTAL_PAGE_COUNT,
				IReportElementConstants.AUTOTEXT_TOTAL_PAGE_COUNT );
		entries.add( combined );
		
		combined = createAutoText( AUTOTEXT_PAGE_X_OF_Y,
		AUTOTEXT_TOOLTIP_PAGE_X_OF_Y,
		IReportElementConstants.AUTOTEXT_PAGEXOFY );
		entries.add( combined );

		combined = createAutoText( AUTOTEXT_AUTHOR_PAGE_DATE,
				AUTOTEXT_TOOLTIP_AUTHOR_PAGE_DATE,
				IReportElementConstants.AUTOTEXT_AUTHOR_PAGE_DATE );
		entries.add( combined );

		combined = createAutoText( AUTOTEXT_CONFIDENTIAL_PAGE,
				AUTOTEXT_TOOLTIP_CONFIDENTIAL_PAGE,
				IReportElementConstants.AUTOTEXT_CONFIDENTIAL_PAGE );
		entries.add( combined );

		combined = createAutoText( AUTOTEXT_DATE,
				AUTOTEXT_TOOLTIP_DATE,
				IReportElementConstants.AUTOTEXT_DATE );
		entries.add( combined );

		combined = createAutoText( AUTOTEXT_CREATED_ON,
				AUTOTEXT_TOOLTIP_CREATED_ON,
				IReportElementConstants.AUTOTEXT_CREATEDON );
		entries.add( combined );

		combined = createAutoText( AUTOTEXT_CREATED_BY,
				AUTOTEXT_TOOLTIP_CREATED_BY,
				IReportElementConstants.AUTOTEXT_CREATEDBY );
		entries.add( combined );

		combined = createAutoText( AUTOTEXT_FILENAME,
				AUTOTEXT_TOOLTIP_FILENAME,
				IReportElementConstants.AUTOTEXT_FILENAME );
		entries.add( combined );

		combined = createAutoText( AUTOTEXT_LAST_PRINTED,
				AUTOTEXT_TOOLTIP_LAST_PRINTED,
				IReportElementConstants.AUTOTEXT_LASTPRINTED );
		entries.add( combined );

		combined = createAutoText( AUTOTEXT_VARIABLE,
				AUTOTEXT_TOOLTIP_VARIABLE,
				IReportElementConstants.AUTOTEXT_VARIABLE );
		entries.add( combined );

//	Remove unsupported function			
//		combined = createAutoText( AUTOTEXT_PAGE_X_OF_Y,
//				AUTOTEXT_TOOLTIP_PAGE_X_OF_Y,
//				IReportElementConstants.AUTOTEXT_PAGEXOFY );
//		entries.add( combined );

		category.addAll( entries );
		return category;
	}

	private static CombinedTemplateCreationEntry createAutoText( String label,
			String shortDesc, Object template )
	{
		AbstractToolHandleExtends preHandle = BasePaletteFactory.getAbstractToolHandleExtendsFromPaletteName(template);
		
		ImageDescriptor icon = ReportPlatformUIImages.getImageDescriptor( IReportGraphicConstants.ICON_AUTOTEXT );
		ImageDescriptor largeIcon = ReportPlatformUIImages.getImageDescriptor( IReportGraphicConstants.ICON_AUTOTEXT_LARGE );
		
		CombinedTemplateCreationEntry entry = new ReportCombinedTemplateCreationEntry( label,
				shortDesc,
				template,
				new ReportElementFactory( template ),
				icon,
				largeIcon,
				preHandle );
		return entry;
	}
}