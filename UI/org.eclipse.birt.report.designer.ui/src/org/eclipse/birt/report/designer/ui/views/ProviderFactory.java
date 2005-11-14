/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.ui.views;

import org.eclipse.birt.report.designer.core.model.views.data.DataSetItemModel;
import org.eclipse.birt.report.designer.core.model.views.outline.EmbeddedImageNode;
import org.eclipse.birt.report.designer.core.model.views.outline.LibraryNode;
import org.eclipse.birt.report.designer.core.model.views.outline.ReportElementModel;
import org.eclipse.birt.report.designer.internal.ui.extension.ExtensionPointManager;
import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.data.providers.CascadingParameterGroupNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.data.providers.DataSetColumnProvider;
import org.eclipse.birt.report.designer.internal.ui.views.data.providers.DataSetNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.data.providers.DataSetParameterProvider;
import org.eclipse.birt.report.designer.internal.ui.views.data.providers.DataSetsNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.data.providers.DataSourceNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.data.providers.DataSourcesNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.data.providers.ParameterGroupNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.data.providers.ParameterNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.data.providers.ParametersNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.outline.providers.BodyNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.outline.providers.CellProvider;
import org.eclipse.birt.report.designer.internal.ui.views.outline.providers.ComponentsProvider;
import org.eclipse.birt.report.designer.internal.ui.views.outline.providers.DataProvider;
import org.eclipse.birt.report.designer.internal.ui.views.outline.providers.EmbeddedImageNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.outline.providers.EmbeddedImagesNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.outline.providers.GridProvider;
import org.eclipse.birt.report.designer.internal.ui.views.outline.providers.GroupProvider;
import org.eclipse.birt.report.designer.internal.ui.views.outline.providers.LabelProvider;
import org.eclipse.birt.report.designer.internal.ui.views.outline.providers.LibrariesNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.outline.providers.LibraryHandleNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.outline.providers.ListBandProvider;
import org.eclipse.birt.report.designer.internal.ui.views.outline.providers.ListProvider;
import org.eclipse.birt.report.designer.internal.ui.views.outline.providers.MasterPageBandProvider;
import org.eclipse.birt.report.designer.internal.ui.views.outline.providers.MasterPageNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.outline.providers.MasterPagesNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.outline.providers.ReportDesignNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.outline.providers.RowProvider;
import org.eclipse.birt.report.designer.internal.ui.views.outline.providers.StyleNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.outline.providers.StylesNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.outline.providers.TableBandProvider;
import org.eclipse.birt.report.designer.internal.ui.views.outline.providers.TableProvider;
import org.eclipse.birt.report.designer.internal.ui.views.outline.providers.ThemeNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.outline.providers.ThemesNodeProvider;
import org.eclipse.birt.report.designer.ui.extensions.IProviderFactory;
import org.eclipse.birt.report.designer.util.AlphabeticallyComparator;
import org.eclipse.birt.report.model.api.CascadingParameterGroupHandle;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSetParameterHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.EmbeddedImageHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ListGroupHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.elements.interfaces.ILibraryModel;

/**
 * This class represents a factory that implements the process of creating the
 * tree node.
 * 
 * 
 */
public class ProviderFactory
{

	static private DefaultNodeProvider defaultProvider = new DefaultNodeProvider( );

	/**
	 * Gets the default provider
	 * 
	 * @return Returns the default provider
	 */
	public static DefaultNodeProvider getDefaultProvider( )
	{
		return defaultProvider;
	}

	/**
	 * Creates the outline provider and returns it.
	 * 
	 * @param object
	 *            the object
	 * @return the outline provider
	 */

	public static INodeProvider createProvider( Object object )
	{
		// System.out.println("class OutlineProcessFactory" + object);

		if ( object instanceof ReportDesignHandle )
		{
			return new ReportDesignNodeProvider( );
		}
		if ( object instanceof LibraryHandle )
		{
			return new LibraryHandleNodeProvider( );
		}
		else if ( object instanceof ReportElementModel )
		{
			ReportElementModel model = (ReportElementModel) object;
			DesignElementHandle handle = model.getElementHandle( );
			if ( handle instanceof ModuleHandle )
			{
				switch ( model.getSlotId( ) )
				{
					case ReportDesignHandle.BODY_SLOT :
						return new BodyNodeProvider( );
					case ModuleHandle.COMPONENT_SLOT :
						return new ComponentsProvider( );
					// case ModuleHandle.STYLE_SLOT :
					// StylesNodeProvider provider = new StylesNodeProvider( );
					// provider.setSorter( new AlphabeticallyComparator( ) );
					// return provider;
					case ILibraryModel.THEMES_SLOT :
					{
						if ( model.getElementHandle( ) instanceof LibraryHandle )
						{
							ThemesNodeProvider themesProvider = new ThemesNodeProvider( );
							themesProvider.setSorter( new AlphabeticallyComparator( ) );
							return themesProvider;
						}
						StylesNodeProvider provider = new StylesNodeProvider( );
						provider.setSorter( new AlphabeticallyComparator( ) );
						return provider;

					}
					case ModuleHandle.PAGE_SLOT :
						return new MasterPagesNodeProvider( );
					case ModuleHandle.DATA_SOURCE_SLOT :
						return new DataSourcesNodeProvider( );
					case ModuleHandle.PARAMETER_SLOT :
						return new ParametersNodeProvider( );
					case ModuleHandle.DATA_SET_SLOT :
						return new DataSetsNodeProvider( );
				}
			}
			else if ( handle instanceof TableHandle
					|| handle instanceof TableGroupHandle )
			{
				return new TableBandProvider( );
			}
			else if ( handle instanceof ListHandle
					|| handle instanceof ListGroupHandle )
			{
				return new ListBandProvider( );
			}
			else if ( handle instanceof MasterPageHandle )
			{
				return new MasterPageBandProvider( );
			}
			/*
			 * TableItem - TableItem.HEADER_SLOT, TableItem.FOOTER_SLOT,
			 * TableItem.DETAIL_SLOT, TableItem.GROUP_SLOT,
			 * TableItem.COLUMN_SLOT TableRow - TableRow.CONTENT_SLOT
			 * TableColumn TableGroup - TableGroup.HEADER_SLOT,
			 * TableGroup.FOOTER_SLOT Cell - Cell.CONTENT_SLOT ListItem -
			 * ListItem.HEADER_SLOT, ListItem.FOOTER_SLOT, ListItem.DETAIL_SLOT,
			 * ListItem.GROUP_SLOT ListGroup - TableGroup.HEADER_SLOT,
			 * TableGroup.FOOTER_SLOT
			 * 
			 * GridItem - GridItem.ROW_SLOT, GridItem.COLUMN_SLOT
			 */
		}
		else if ( object instanceof DesignElementHandle )
		{
			String elementName = ( (DesignElementHandle) object ).getDefn( )
					.getName( );
			IProviderFactory factory = ExtensionPointManager.getInstance( )
					.getProviderFactory( elementName );
			if ( factory != null )
			{
				return factory.createProvider( object );
			}
			if ( object instanceof CellHandle )
			{
				return new CellProvider( );
			}
			else if ( object instanceof GridHandle )
			{
				return new GridProvider( );
			}
			else if ( object instanceof StyleHandle )
			{
				return new StyleNodeProvider( );
			}
			else if ( object instanceof ListHandle )
			{
				return new ListProvider( );
			}
			else if ( object instanceof TableHandle )
			{
				return new TableProvider( );
			}
			else if ( object instanceof TableGroupHandle
					|| object instanceof ListGroupHandle )
			{
				return new GroupProvider( );
			}
			else if ( object instanceof RowHandle )
			{
				return new RowProvider( );
			}
			else if ( object instanceof DataItemHandle )
			{
				return new DataProvider( );
			}
			else if ( object instanceof LabelHandle )
			{
				return new LabelProvider( );
			}
			else if ( object instanceof MasterPageHandle )
			{
				return new MasterPageNodeProvider( );
			}
			else if ( object instanceof DataSourceHandle )
			{
				return new DataSourceNodeProvider( );
			}
			else if ( object instanceof DataSetHandle )
			{
				return new DataSetNodeProvider( );
			}
			else if ( object instanceof ParameterGroupHandle )
			{
				if ( object instanceof CascadingParameterGroupHandle )
				{
					return new CascadingParameterGroupNodeProvider( );
				}
				return new ParameterGroupNodeProvider( );
			}
			else if ( object instanceof ParameterHandle )
			{
				return new ParameterNodeProvider( );
			}
			else if ( object instanceof ThemeHandle )
			{
				return new ThemeNodeProvider( );
			}
		}
		else if ( object instanceof DataSetItemModel )
		{
			return new DataSetColumnProvider( );
		}
		else if ( object instanceof DataSetParameterHandle )
		{
			return new DataSetParameterProvider( );
		}
		else if ( object instanceof EmbeddedImageNode )
		{
			return new EmbeddedImagesNodeProvider( );
		}
		else if ( object instanceof EmbeddedImageHandle )
		{
			return new EmbeddedImageNodeProvider( );
		}
		else if ( object instanceof LibraryNode )
		{
			return new LibrariesNodeProvider( );
		}

		return getDefaultProvider( );
	}
}