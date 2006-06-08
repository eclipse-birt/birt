/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.data.oda.jdbc.ui.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.ParameterMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.birt.report.data.oda.jdbc.ui.JdbcPlugin;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;


/**
 * TODO: Please document
 * 
 * @version $Revision: 1.22 $ $Date: 2006/05/18 03:55:57 $
 */
public class Utility
{
	// flag to indicate whether JarInfo and DriverInfo in preference page have
	// been updated from String[] to JarFile and DriverInfo 
	private static boolean updatedOfJarInfo = false;
	private static boolean updatedOfDriverInfo = false;
    /**
     * 
     */
    private Utility()
    {
    }

	/**
	 * @param composite
	 * @param dataSource
	 * @param image
	 * @return
	 */
	public static TreeItem[] createTreeItems( TreeItem parentItem ,ArrayList dataSource, int style, Image image) 
	{
		if ( dataSource == null )
		{
			return null;
		}
		
		TreeItem item[] = new TreeItem[dataSource.size()];
//		Tree parent = parentItem.getParent();
//		Font dataSourceItemFont = parent.getFont();
		boolean addDummyNode = false;
	
		for (int i = 0; i < dataSource.size(); i++)
		{
			item[i] = new TreeItem(parentItem, style);
			Object source = dataSource.get(i);
			String displayName = "";
			String name = "";
			if(source instanceof String)
			{
				displayName = (String)source;
				name = displayName;
				item[i].setData(name);
			}
			else if (source instanceof TableItem )
			{
				displayName = ((TableItem)source).getText();
				name = (String)((TableItem)source).getData();
				item[i].setData(name);
			}
			else if(source instanceof DbObject)
			{
				DbObject dbObject = (DbObject)source;
				name = dbObject.getName();
				displayName = dbObject.getDisplayName();
				image = dbObject.getImage();
				addDummyNode = true;
				item[i].setData(dbObject);
			}
			else if ( source instanceof Column )
			{
				Column column = (Column)source;
				displayName = column.getName();
				name = column.getTableName() + "." + displayName;
				String type = column.getDbType();
				displayName = displayName + " (" + type + ")";
				 
				if ( column.getSchemaName() != null )
				{
					name = column.getSchemaName() + "." + name;
				}
				item[i].setData(name);
			}
			else if ( source instanceof Procedure )
			{
				Procedure column = (Procedure) source;
				name = column.getProcedureName( );
				displayName = name;
				addDummyNode = true;
				if ( column.getSchema( ) != null )
					name = column.getSchema( ) + "." + displayName;
				item[i].setData( column );
			}
			else if ( source instanceof ProcedureParameter )
			{
				ProcedureParameter column = (ProcedureParameter) source;
				name = column.getName( );
				displayName = name;
				if ( column.getSchema( ) != null )
					name = column.getSchema( ) + "." + displayName;
				int type = column.getModeType( );
				String mode = toModeType( type );
				String dataType = column.getDataTypeName( );
				displayName = displayName + " (" + dataType + ", " + mode + ")";
				item[i].setData( column );
			}
	
			item[i].setText(displayName);
			
			item[i].setImage(image);

			//parent.setTopItem(item[i]);
			item[i].setExpanded(false);
			
			if ( addDummyNode )
			{
				new TreeItem(item[i], style);
			}
	
		}
		return item;
	}

	/**
	 * give the stored procedure's column type name from the type.
	 * @param type
	 * @return
	 */
	public static String toModeType( int type )
	{
		switch ( type )
		{
			case ParameterMetaData.parameterModeUnknown:
				return "Unknown";
			case ParameterMetaData.parameterModeIn:
				return "Input";
			case ParameterMetaData.parameterModeInOut:
				return "Input/Output";
			case ParameterMetaData.parameterModeOut:
				return "Output";
			case 5:
				return "Return Value";
			default:
				return "Unknown";
		}
	}
	
	/**
	 * get the tree item name from the tree item's object
	 * @param selectedItem
	 * @return
	 */
	public static String getTreeItemsName( TreeItem selectedItem ) 
	{
		if ( selectedItem == null )
		{
			return null;
		}
		String name = "";
		Object source = selectedItem.getData();
		    if(source instanceof String)
			{
				name = (String)source;
			}
			else if (source instanceof TableItem )
			{
				name = (String)((TableItem)source).getData();
			}
			else if(source instanceof DbObject)
			{
				DbObject dbObject = (DbObject)source;
				name = dbObject.getName();
			}
			else if ( source instanceof Column )
			{
				Column column = (Column)source;
				String displayName = column.getName();
				name = column.getTableName() + "." + displayName;
				String type = column.getDbType();
				displayName = displayName + " (" + type + ")";
				 
				if ( column.getSchemaName() != null )
				{
					name = column.getSchemaName() + "." + name;
				}
			}
			else if( source instanceof Procedure )
			{
				Procedure column = (Procedure)source;
				name = column.getProcedureName( );
				 
				if ( column.getSchema() != null )
				{
					name = column.getSchema() + "." + name;
				}
			}
			return name;
	}
		
	/**
	 * 
	 * @param tree The Tree whose position needs to be set 
	 */
	public static void setMinScrollPosition(Tree tree)
	{
		if ( tree == null )
		{
			return;
		}
		
		ScrollBar horizontalScrollBar = tree.getHorizontalBar();
		
		if( horizontalScrollBar != null )
		{
			horizontalScrollBar.setThumb(0);
		}
		
		
		
		// position the vertical and horizontal scrollbars accordingly
		ScrollBar verticalScrollBar = tree.getVerticalBar();
		if( verticalScrollBar != null )
		{
			verticalScrollBar.setThumb(0);
		}
		
	}
	
	/**
	 * @param Item The TableItem 
	 * @return If the passed Item represents a Table
	 */
	public static boolean isTableNode( TreeItem item, boolean isSchemaSupported, TreeItem rootNode )
	{
		if( item != null)
		{
			if ( isSchemaSupported)
			{
				if (item.getParentItem().getParentItem().getParentItem() == null)
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			else
			{
				if (item.getParentItem().getParentItem() == null)
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		}
		return false;
	}
	
	/**
	 * @param item A tree Item which has to be tested
	 * @return if the TreeItem represents a Schema node
	 */
	public static boolean isSchemaNode( TreeItem item, boolean isSchemaSupported, TreeItem rootNode )
	{
		if ( item != null && isSchemaSupported )
		{
			if (item.getParentItem().getParentItem() == null)
			{
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * 
	 * @param item The Tree Item selected
	 * @return  True if the selected node represents a Catalog Node ( root Node displayed
	 *          in the Available Table List ) . If the node indicates a Table or column
	 *          false is returned.
	 */
	public static  boolean isCatalogNode( TreeItem item )
	{
		if ( item != null )
		{
			if (item.getParentItem() == null )
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Get Map from PreferenceStore by key
	 * @param mapKey the key of the map
	 * @return Map 
	 */
	public static Map getPreferenceStoredMap( String mapKey )
	{
		String driverMap64 = JdbcPlugin.getDefault( )
				.getPreferenceStore( )
				.getString( mapKey );
		try
		{
			if ( driverMap64 != null )
			{
				byte[] bytes = Base64.decodeBase64( driverMap64.getBytes( ) );

				ByteArrayInputStream bis = new ByteArrayInputStream( bytes );
				Object obj = new ObjectInputStream( bis ).readObject( );

				if ( obj instanceof Map )
				{
					return updatePreferenceMap( (Map) obj, mapKey );
				}
			}
		}
		catch ( IOException e )
		{
			//ignore
		}
		catch ( ClassNotFoundException e )
		{
			ExceptionHandler.showException( PlatformUI.getWorkbench( )
					.getDisplay( )
					.getActiveShell( ),
					JdbcPlugin.getResourceString( "exceptionHandler.title.error" ),
					e.getLocalizedMessage( ),
					e );

		}

		return new HashMap( );
	}
	
	/**
	 * Since the data type stored in this map has been changed,this method is
	 * design to surpport the former and the new preference
	 * @param map
	 * @return 
	 */
	private static Map updatePreferenceMap( Map map, String mapKey )
	{
		if ( JdbcPlugin.DRIVER_MAP_PREFERENCE_KEY.equals( mapKey ) )
		{
			if ( updatedOfDriverInfo )
				return map;

			updatedOfDriverInfo = true;
		}
		else if ( JdbcPlugin.JAR_MAP_PREFERENCE_KEY.equals( mapKey ) )
		{
			if ( updatedOfJarInfo )
				return map;

			updatedOfJarInfo = true;
		}
		else{
			return map;
		}
		
		Set entrySet = map.entrySet( );
		Iterator it = entrySet.iterator( );
		if ( !it.hasNext( ) )
		{
			// it is an empty Map
			return map;
		}
		else
		{
			Map.Entry entry = (Map.Entry) it.next( );
			if ( ( entry.getValue( ) instanceof DriverInfo )
					|| ( entry.getValue( ) instanceof JarFile ) )
			{
				return map;
			}
			else
			{
				it = entrySet.iterator( );
				Map rMap = new HashMap( );
				String[] entryValue;
				if ( JdbcPlugin.DRIVER_MAP_PREFERENCE_KEY.equals( mapKey ) )
				{
					DriverInfo driverInfo;
					while ( it.hasNext( ) )
					{
						entry = (Map.Entry) it.next( );
						entryValue = (String[]) entry.getValue( );
						driverInfo = new DriverInfo( entry.getKey( ).toString( ),
								entryValue[0],
								entryValue[1] );
						rMap.put( entry.getKey( ), driverInfo );
					}
				}
				else if ( JdbcPlugin.JAR_MAP_PREFERENCE_KEY.equals( mapKey ) )
				{
					JarFile jarFile;
					while ( it.hasNext( ) )
					{
						entry = (Map.Entry) it.next( );
						entryValue = (String[]) entry.getValue( );
						jarFile = new JarFile( getFileNameFromFilePath( (String) entryValue[0] ),
								entryValue[0],
								"",
								false );
						rMap.put( entry.getKey( ), jarFile );
					}
				}
				setPreferenceStoredMap( JdbcPlugin.DRIVER_MAP_PREFERENCE_KEY,
						rMap );
				return rMap;
			}
		}
	}
	
	private static String getFileNameFromFilePath( String  filePath )
	{
		String fileName = filePath.substring( filePath.lastIndexOf( File.separator )
				+ File.separator.length( ) );
		return fileName;
	}
	
	/**
	 * Put <tt>value</tt> with key <tt>keyInMap</tt>into the map whose key
	 * is <tt>keyOfPreference</tt>
	 * 
	 * @param keyOfPreference
	 *            key of PreferenceStore Map
	 * @param keyInMap
	 *            key in the Map
	 * @param value
	 *            the value to be set
	 */
	public static void putPreferenceStoredMapValue( String keyOfPreference,
			String keyInMap, Object value )
	{
		Map map = getPreferenceStoredMap( keyOfPreference );
		map.put( keyInMap, value );
		setPreferenceStoredMap( keyOfPreference, map );
	}

	/**
	 * Removes map entry with key <tt>keyInMap</tt>from the map whose key
	 * is <tt>keyOfPreference</tt>
	 * @param keyOfPreference
	 *            key of PreferenceStore Map
	 * @param keyInMap
	 * 			  key in the Map
	 */
	public static void removeMapEntryFromPreferenceStoredMap(
			String keyOfPreference, String keyInMap )
	{
		Map map = getPreferenceStoredMap( keyOfPreference );
		if ( map.containsKey( keyInMap ) )
		{
			map.remove( keyInMap );
		}
		setPreferenceStoredMap( keyOfPreference, map );
	}

	/**
	 * Reset the map in PreferenceStored
	 * @param keyOfPreference key in PreferenceStore
	 * @param map the map to be set 
	 */
	public static void setPreferenceStoredMap( String keyOfPreference, Map map )
	{
		try
		{
			ByteArrayOutputStream bos = new ByteArrayOutputStream( );
			new ObjectOutputStream( bos ).writeObject( map );

			byte[] bytes = bos.toByteArray( );

			bytes = Base64.encodeBase64( bytes );

			JdbcPlugin.getDefault( )
					.getPreferenceStore( )
					.setValue( keyOfPreference, new String( bytes ) );
		}
		catch ( IOException e )
		{
			//ignore
		}
	}
	
	/**
	 * 
	 * @param control
	 * @param contextId
	 */
	public static void setSystemHelp( Control control, String contextId )
	{
		PlatformUI.getWorkbench( )
				.getHelpSystem( )
				.setHelp( control, contextId );
	}
}
