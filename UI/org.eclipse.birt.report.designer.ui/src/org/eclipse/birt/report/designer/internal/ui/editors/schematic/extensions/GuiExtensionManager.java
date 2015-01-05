/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation .
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.extensions;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.birt.core.preference.IPreferences;
import org.eclipse.birt.report.designer.core.IReportElementConstants;
import org.eclipse.birt.report.designer.internal.ui.extension.ExtendedEditPart;
import org.eclipse.birt.report.designer.internal.ui.extension.ExtendedElementUIPoint;
import org.eclipse.birt.report.designer.internal.ui.extension.ExtensionPointManager;
import org.eclipse.birt.report.designer.internal.ui.extension.experimental.CommandCombinedTemplateCreationEntry;
import org.eclipse.birt.report.designer.internal.ui.extension.experimental.EditpartExtensionManager;
import org.eclipse.birt.report.designer.internal.ui.extension.experimental.PaletteEntryExtension;
import org.eclipse.birt.report.designer.internal.ui.palette.BasePaletteFactory;
import org.eclipse.birt.report.designer.internal.ui.palette.PaletteCategory;
import org.eclipse.birt.report.designer.internal.ui.palette.ReportCombinedTemplateCreationEntry;
import org.eclipse.birt.report.designer.internal.ui.palette.ReportElementFactory;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.extensions.IExtensionConstants;
import org.eclipse.birt.report.designer.ui.extensions.IReportItemFigureProvider;
import org.eclipse.birt.report.designer.ui.preferences.PreferenceFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.ISharedImages;

import com.ibm.icu.text.Collator;

/**
 * Deal with the extension element
 * 
 */
public class GuiExtensionManager
{

	public static final String PALETTE_DESIGNER = "pallet_designer"; //$NON-NLS-1$
	public static final String PALETTE_MASTERPAGE = "pallet_masterpage"; //$NON-NLS-1$

	public static final String DESIGNER_FACTORY = "designer_factory"; //$NON-NLS-1$

	private static final String EXT_MGR_LABEL = Messages.getString( "GuiExtensionManager.label.name" ); //$NON-NLS-1$

	/**
	 * @param extension
	 * @param object
	 * @return
	 */
	public static Object doExtension( IExtension extension, Object object )
	{
		// Bug 183605
		// List list = ExtensionPointManager.getInstance( )
		// .getExtendedElementPoints( );
		// if ( list == null || list.size( ) == 0 )
		// {
		// return null;
		// }
		Object retValue = null;
		if ( PALETTE_DESIGNER.equals( extension.getExtendsionIdentify( ) )
				|| PALETTE_MASTERPAGE.equals( extension.getExtendsionIdentify( ) ) )
		{

			retValue = doPalette( object, extension.getExtendsionIdentify( ) );
		}
		else if ( DESIGNER_FACTORY.equals( extension.getExtendsionIdentify( ) ) )
		{
			retValue = doDesignerFactory( object );
		}

		return retValue;
	}

	private static Object doDesignerFactory( Object object )
	{
		if ( object instanceof ExtendedItemHandle )
		{
			ExtendedItemHandle model = (ExtendedItemHandle) object;
			ExtendedEditPart part = new ExtendedEditPart( model );
			String id = getExtendedElementID( model );

			ExtendedElementUIPoint point = ExtensionPointManager.getInstance( )
					.getExtendedElementPoint( id );
			if ( point == null )
			{
				return null;
			}
			IReportItemFigureProvider UI = ExtensionPointManager.getInstance( )
					.getExtendedElementPoint( id )
					.getReportItemUI( );
			if ( UI == null )
			{
				return null;
			}
			part.setExtendedElementUI( UI );
			return part;
		}
		return null;
	}

	/**
	 * @param model
	 * @return
	 */
	public static String getExtendedElementID( ExtendedItemHandle model )
	{
		return model.getExtensionName( );
	}

	/**
	 * Get display name
	 * 
	 * @param obj
	 * @return
	 */
	public static String getExtensionDisplayName( Object obj )
	{
		String value = EXT_MGR_LABEL;
		if ( obj instanceof ExtendedItemHandle )
		{
			String name = ( (ExtendedItemHandle) obj ).getDefn( )
					.getDisplayName( );
			if ( name != null )
			{
				value = name;
			}
		}

		return value;
	}

	/**
	 * @param object
	 */
	private static Object doPalette( Object object, String type )
	{
		assert ( object instanceof PaletteRoot );
		PaletteRoot root = (PaletteRoot) object;
		List list = root.getChildren( );
		List exts = ExtensionPointManager.getInstance( )
				.getExtendedElementPoints( );

		Comparator<CombinedTemplateCreationEntry> entryComparator = new Comparator<CombinedTemplateCreationEntry>( ) {

			public int compare( CombinedTemplateCreationEntry o1,
					CombinedTemplateCreationEntry o2 )
			{
				return Collator.getInstance( ).compare( o1.getLabel( ),
						o2.getLabel( ) );
			}
		};

		if ( exts == null )
		{
			return root;
		}

		// sort containers and entries
		Map<String, SortedSet<CombinedTemplateCreationEntry>> containerEntriesMap = new HashMap<String, SortedSet<CombinedTemplateCreationEntry>>( );

		for ( Iterator itor = exts.iterator( ); itor.hasNext( ); )
		{
			ExtendedElementUIPoint point = (ExtendedElementUIPoint) itor.next( );
			if ( point == null )
			{
				return root;
			}
			String category = (String) point.getAttribute( IExtensionConstants.ATTRIBUTE_PALETTE_CATEGORY );

			IReportItemFigureProvider UI = point.getReportItemUI( );
			if ( UI == null )
			{
				return root;
			}

			if ( PALETTE_DESIGNER.equals( type ) )
			{
				if ( !UIUtil.isVisibleExtensionElement( point ) )
					continue;
			}
			else if ( PALETTE_MASTERPAGE.equals( type ) )
			{
				Boolean bool = (Boolean) point.getAttribute( IExtensionConstants.ATTRIBUTE_EDITOR_SHOW_IN_MASTERPAGE );
				// if ( !bool.booleanValue( ) )
				{
					continue;
				}
			}

			ImageDescriptor icon = (ImageDescriptor) point.getAttribute( IExtensionConstants.ATTRIBUTE_KEY_PALETTE_ICON );

			ImageDescriptor iconLarge = (ImageDescriptor) point.getAttribute( IExtensionConstants.ATTRIBUTE_KEY_PALETTE_ICON_LARGE );
			if ( iconLarge == null )
			{
				iconLarge = icon;
			}

			String displayName = DEUtil.getMetaDataDictionary( )
					.getExtension( point.getExtensionName( ) )
					.getDisplayName( );
			CombinedTemplateCreationEntry combined = new ReportCombinedTemplateCreationEntry( displayName,
					Messages.getFormattedString( "GuiExtensionManager.tooltip.insert", new Object[]{displayName} ), //$NON-NLS-1$
					getExtendedPalletTemplateName( point ),
					new ReportElementFactory( IReportElementConstants.REPORT_ELEMENT_EXTENDED
							+ point.getExtensionName( ) ),
					icon,
					iconLarge,
					BasePaletteFactory.getAbstractToolHandleExtendsFromPaletteName( getExtendedPalletTemplateName( point ) ) );
			PaletteContainer entry = findCategory( list, category );
			if ( entry == null )
			{
				String categoryLabel = (String) point.getAttribute( IExtensionConstants.ATTRIBUTE_PALETTE_CATEGORY_DISPLAYNAME );
				if ( categoryLabel == null )
				{
					categoryLabel = category;
				}
				entry = new PaletteCategory( category,
						categoryLabel,
						ReportPlatformUIImages.getImageDescriptor( ISharedImages.IMG_OBJ_FOLDER ) );
				root.add( entry );
			}
			// entry.add( combined );
			if ( !containerEntriesMap.containsKey( category ) )
			{
				containerEntriesMap.put( category,
						new TreeSet<CombinedTemplateCreationEntry>( entryComparator ) );
			}
			containerEntriesMap.get( category ).add( combined );
		}

		// experimental
		PaletteEntryExtension[] entries = EditpartExtensionManager.getPaletteEntries( );
		for ( int i = 0; i < entries.length; i++ )
		{
			if ( PALETTE_MASTERPAGE.equals( type ) )
			{
				continue;
			}
			String category = entries[i].getCategory( );
			PaletteContainer entry = findCategory( list, category );
			if ( entry == null )
			{
				String categoryLabel = entries[i].getCategoryDisplayName( );
				if ( categoryLabel == null )
				{
					categoryLabel = category;
				}
				entry = new PaletteCategory( category,
						categoryLabel,
						ReportPlatformUIImages.getImageDescriptor( ISharedImages.IMG_OBJ_FOLDER ) );
				root.add( entry );
			}

			CombinedTemplateCreationEntry combined = new CommandCombinedTemplateCreationEntry( entries[i] );
			// entry.add( combined );
			if ( !containerEntriesMap.containsKey( category ) )
			{
				containerEntriesMap.put( category,
						new TreeSet<CombinedTemplateCreationEntry>( entryComparator ) );
			}
			containerEntriesMap.get( category ).add( combined );
		}

		for ( Map.Entry<String, SortedSet<CombinedTemplateCreationEntry>> entry : containerEntriesMap.entrySet( ) )
		{
			PaletteContainer container = findCategory( list, entry.getKey( ) );
			for ( CombinedTemplateCreationEntry combined : entry.getValue( ) )
			{
				container.add( combined );
			}
		}

		return root;
	}

	public static Object getExtendedPalletTemplateName(
			ExtendedElementUIPoint point )
	{
		return IReportElementConstants.REPORT_ELEMENT_EXTENDED
				+ point.getExtensionName( );
	}

	private static PaletteCategory findCategory( List list, String category )
	{
		for ( Iterator itor = list.iterator( ); itor.hasNext( ); )
		{
			Object entry = itor.next( );
			if ( entry instanceof PaletteCategory )
			{
				if ( ( (PaletteCategory) entry ).getCategoryName( )
						.equals( category ) )
				{
					return (PaletteCategory) entry;
				}
			}
		}
		return null;
	}
}
