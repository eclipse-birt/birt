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
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.birt.report.data.oda.jdbc.ui.JdbcPlugin;
import org.eclipse.birt.report.data.oda.jdbc.ui.model.TableImpl;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.UserPropertyDefnHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.UserPropertyException;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.metadata.StringPropertyType;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;


/**
 * TODO: Please document
 * 
 * @version $Revision: 1.13 $ $Date: 2005/08/17 05:07:50 $
 */
public class Utility
{

//	 The suffix which will be used to create new names 
	// if duplicate objects are selected in the Table Selection Page
	private static final String dupAffix = "_";
	private static boolean updatedOfJarInfo = false;
    /**
     * 
     */
    private Utility()
    {
    }

    public static String getUserProperty(DesignElementHandle ds, String propertyName)
    {
        String returnValue = null;
        UserPropertyDefnHandle handle = ds.getUserPropertyDefnHandle(propertyName);
        if(handle != null)
        {
            returnValue = handle.getStringValue();
        }
    
        
        return returnValue;
    }

    public static void setUserProperty(DesignElementHandle ds, String propertyName, String value)
    {
        UserPropertyDefnHandle handle = ds.getUserPropertyDefnHandle(propertyName);
        UserPropertyDefn defn = null;
        //PropertyHandle handle = ds.getPropertyHandle(ExtendedDataSet.USER_PROPERTIES_PROP);
        if(handle != null)
        {
            defn = (UserPropertyDefn) handle.getDefn();
        }
        else
        {
            defn = new UserPropertyDefn();
            defn.setType(new StringPropertyType());
            defn.setName(propertyName);
            //handle.addItem(defn);
            try
            {
                ds.addUserPropertyDefn(defn);
            }
            catch (UserPropertyException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }            
        }
        try
        {
            ds.setStringProperty(propertyName, value);
        }
        catch (SemanticException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
			}
			else if( source instanceof TableImpl)
			{
				TableImpl table = (TableImpl)source;
				displayName = table.getTableAlias();
				name = table.getFullyQualifiedName();
			}
			else if (source instanceof TableItem )
			{
				displayName = ((TableItem)source).getText();
				name = (String)((TableItem)source).getData();
			}
			else if(source instanceof DbObject)
			{
				DbObject dbObject = (DbObject)source;
				name = dbObject.getName();
				displayName = dbObject.getDisplayName();
				image = dbObject.getImage();
				addDummyNode = true;
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
			}

	
			item[i].setText(displayName);
			item[i].setData(name);
			
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
	 * @param tables: A List of existing table names
	 * @param fileName
	 * @return
	 */
	public static String checkDuplicationAndGetDuplicateName(ArrayList tableList, String tableName) 
	{
		int index = 0;
		Iterator tableIterator = tableList.iterator();
		
		// See how many items are there matching the given name
		
		while(tableIterator.hasNext())
		{
			TableImpl table = (TableImpl)tableIterator.next();
			
			String name = table.getFullyQualifiedName();
			if(name.equalsIgnoreCase(tableName))
			{
				index ++;
			}
		}
		
		
		if(index > 0)
		{
			tableName = tableName + dupAffix + index;
		}
		return tableName;
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
					if ( JdbcPlugin.JAR_MAP_PREFERENCE_KEY.equals( mapKey ) )
						return updateJarInfoMap( (Map) obj );
					else
						return (Map) obj;
				}
			}
		}
		catch ( IOException e )
		{
			//ignore
		}
		catch ( ClassNotFoundException e )
		{
			ExceptionHandler.handle( e );
		}

		return new HashMap( );
	}
	
	/**
	 * Since the data type stored in this map has been changed,this method is
	 * design to surpport the former and the new preference
	 * @param map
	 * @return 
	 */
	private static Map updateJarInfoMap( Map map )
	{
		if ( updatedOfJarInfo )
			return map;

		updatedOfJarInfo = true;
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
			if ( entry.getValue( ) instanceof JarFile )
			{
				return map;
			}
			else
			{
				it = entrySet.iterator( );
				Map rMap = new HashMap( );
				String[] entryValue;
				JarFile jarFile;
				while ( it.hasNext( ) )
				{
					entry = (Map.Entry) it.next( );
					entryValue = (String[]) entry.getValue( );
					jarFile = new JarFile( entryValue[0], "", false );
					rMap.put( entry.getKey( ), jarFile );
				}
				setPreferenceStoredMap( JdbcPlugin.JAR_MAP_PREFERENCE_KEY, rMap );
				return rMap;
			}
		}
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
}
