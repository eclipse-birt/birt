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
import org.eclipse.gef.palette.PaletteRoot;

/**
 * Factory to populate report graphical editor palette root.
 *  
 */
public class DesignerPaletteFactory extends BasePaletteFactory
{

	private static final String ELEMENT_NAME_TEXT = Messages.getString( "DesignerPaletteFactory.elementName.text" ); //$NON-NLS-1$

	private static final String ELEMENT_NAME_LABEL = Messages.getString( "DesignerPaletteFactory.elementName.label" ); //$NON-NLS-1$

	private static final String ELEMENT_NAME_DATA = Messages.getString( "DesignerPaletteFactory.elementName.data" ); //$NON-NLS-1$

	private static final String ELEMENT_NAME_IMAGE = Messages.getString( "DesignerPaletteFactory.elementName.image" ); //$NON-NLS-1$

	private static final String ELEMENT_NAME_GRID = Messages.getString( "DesignerPaletteFactory.elementName.grid" ); //$NON-NLS-1$

	private static final String ELEMENT_NAME_LIST = Messages.getString( "DesignerPaletteFactory.elementName.list" ); //$NON-NLS-1$

	private static final String ELEMENT_NAME_TABLE = Messages.getString( "DesignerPaletteFactory.elementName.table" ); //$NON-NLS-1$

	private static final String TOOL_TIP_TEXT_REPORT_ITEM = Messages.getString( "DesignerPaletteFactory.toolTip.textReportItem" ); //$NON-NLS-1$

	private static final String TOOL_TIP_LABEL_REPORT_ITEM = Messages.getString( "DesignerPaletteFactory.toolTip.labelReportItem" ); //$NON-NLS-1$

	private static final String TOOL_TIP_DATA_REPORT_ITEM = Messages.getString( "DesignerPaletteFactory.toolTip.dataReportItem" ); //$NON-NLS-1$

	private static final String TOOL_TIP_IMAGE_REPORT_ITEM = Messages.getString( "DesignerPaletteFactory.toolTip.imageReportItem" ); //$NON-NLS-1$

	private static final String TOOL_TIP_GRID_REPORT_ITEM = Messages.getString( "DesignerPaletteFactory.toolTip.gridReportItem" ); //$NON-NLS-1$

	private static final String TOOL_TIP_LIST_REPORT_ITEM = Messages.getString( "DesignerPaletteFactory.toolTip.listReportItem" ); //$NON-NLS-1$

	private static final String TOOL_TIP_TABLE_REPORT_ITEM = Messages.getString( "DesignerPaletteFactory.toolTip.tableReportItem" ); //$NON-NLS-1$

	private static final String REPORT_ITEMS_LABEL = Messages.getString( "DesignerPaletteFactory.categoryName.reportItems" ); //$NON-NLS-1$

	/**
	 * Creates the palette and returns the palette
	 * 
	 * @return the editor palette
	 */
	public static PaletteRoot createPalette( )
	{
		PaletteRoot root = BasePaletteFactory.createPalette( );
		root.addAll( createCategories( ) );

		IExtension extension = new IExtension.Stub( ) {

			public String getExtendsionIdentify( )
			{
				return GuiExtensionManager.PALETTE_DESIGNER;
			}
		};
		GuiExtensionManager.doExtension( extension, root );
		return root;
	}

	/**
	 * Creates palette categories and returns the category list
	 * 
	 * @return Returns the categories list
	 */
	static List createCategories( )
	{
		List categories = new ArrayList( );
		categories.add( createContentCategory( ) );
		return categories;
	}

	/**
	 * Creates BIRT Normal specified categories and items.
	 * 
	 * @return Palette Container containing BIRT Normal specified categories
	 */
	private static PaletteContainer createContentCategory( )
	{
		PaletteCategory category = new PaletteCategory( IPreferenceConstants.PALETTE_CONTENT,
				REPORT_ITEMS_LABEL,
				null );		
		List entries = new ArrayList( );

		CombinedTemplateCreationEntry combined = new ReportCombinedTemplateCreationEntry( ELEMENT_NAME_LABEL,
				TOOL_TIP_LABEL_REPORT_ITEM,
				IReportElementConstants.REPORT_ELEMENT_LABEL,
				new ReportElementFactory( IReportElementConstants.REPORT_ELEMENT_LABEL ),
				ReportPlatformUIImages.getImageDescriptor( IReportGraphicConstants.ICON_ELEMENT_LABEL ),
				ReportPlatformUIImages.getImageDescriptor( IReportGraphicConstants.ICON_ELEMENT_LABEL ),
				getAbstractToolHandleExtendsFromPalletName(IReportElementConstants.REPORT_ELEMENT_LABEL) );

		entries.add( combined );
		
		combined = new ReportCombinedTemplateCreationEntry( ELEMENT_NAME_TEXT,
				TOOL_TIP_TEXT_REPORT_ITEM,
				IReportElementConstants.REPORT_ELEMENT_TEXT,
				new ReportElementFactory( IReportElementConstants.REPORT_ELEMENT_TEXT ),
				ReportPlatformUIImages.getImageDescriptor( IReportGraphicConstants.ICON_ELEMENT_TEXT ),
				ReportPlatformUIImages.getImageDescriptor( IReportGraphicConstants.ICON_ELEMENT_TEXT ),
				getAbstractToolHandleExtendsFromPalletName(IReportElementConstants.REPORT_ELEMENT_TEXT) );

		entries.add( combined );

		combined = new ReportCombinedTemplateCreationEntry( ELEMENT_NAME_DATA,
				TOOL_TIP_DATA_REPORT_ITEM,
				IReportElementConstants.REPORT_ELEMENT_DATA,
				new ReportElementFactory( IReportElementConstants.REPORT_ELEMENT_DATA ),
				ReportPlatformUIImages.getImageDescriptor( IReportGraphicConstants.ICON_ELEMENT_DATA ),
				ReportPlatformUIImages.getImageDescriptor( IReportGraphicConstants.ICON_ELEMENT_DATA ),
				getAbstractToolHandleExtendsFromPalletName(IReportElementConstants.REPORT_ELEMENT_DATA) );

		entries.add( combined );

		combined = new ReportCombinedTemplateCreationEntry( ELEMENT_NAME_IMAGE,
				TOOL_TIP_IMAGE_REPORT_ITEM,
				IReportElementConstants.REPORT_ELEMENT_IMAGE,
				new ReportElementFactory( IReportElementConstants.REPORT_ELEMENT_IMAGE ),
				ReportPlatformUIImages.getImageDescriptor( IReportGraphicConstants.ICON_ELEMENT_IMAGE ),
				ReportPlatformUIImages.getImageDescriptor( IReportGraphicConstants.ICON_ELEMENT_IMAGE ),
				getAbstractToolHandleExtendsFromPalletName(IReportElementConstants.REPORT_ELEMENT_IMAGE) );

		entries.add( combined );

		combined = new ReportCombinedTemplateCreationEntry( ELEMENT_NAME_GRID,
				TOOL_TIP_GRID_REPORT_ITEM,
				IReportElementConstants.REPORT_ELEMENT_GRID,
				new ReportElementFactory( IReportElementConstants.REPORT_ELEMENT_GRID ),
				ReportPlatformUIImages.getImageDescriptor( IReportGraphicConstants.ICON_ELEMENT_GRID ),
				ReportPlatformUIImages.getImageDescriptor( IReportGraphicConstants.ICON_ELEMENT_GRID ),
				getAbstractToolHandleExtendsFromPalletName(IReportElementConstants.REPORT_ELEMENT_GRID) );

		entries.add( combined );
		combined = new ReportCombinedTemplateCreationEntry( ELEMENT_NAME_LIST,
				TOOL_TIP_LIST_REPORT_ITEM,
				IReportElementConstants.REPORT_ELEMENT_LIST,
				new ReportElementFactory( IReportElementConstants.REPORT_ELEMENT_LIST ),
				ReportPlatformUIImages.getImageDescriptor( IReportGraphicConstants.ICON_ELEMENT_LIST ),
				ReportPlatformUIImages.getImageDescriptor( IReportGraphicConstants.ICON_ELEMENT_LIST ),
				getAbstractToolHandleExtendsFromPalletName(IReportElementConstants.REPORT_ELEMENT_LIST) );

		entries.add( combined );

		combined = new ReportCombinedTemplateCreationEntry( ELEMENT_NAME_TABLE,
				TOOL_TIP_TABLE_REPORT_ITEM,
				IReportElementConstants.REPORT_ELEMENT_TABLE,
				new ReportElementFactory( IReportElementConstants.REPORT_ELEMENT_TABLE ),
				ReportPlatformUIImages.getImageDescriptor( IReportGraphicConstants.ICON_ELEMENT_TABLE ),
				ReportPlatformUIImages.getImageDescriptor( IReportGraphicConstants.ICON_ELEMENT_TABLE ),
				getAbstractToolHandleExtendsFromPalletName(IReportElementConstants.REPORT_ELEMENT_TABLE) );

		entries.add( combined );

		category.addAll( entries );
		return category;

	}
}