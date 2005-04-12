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

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.birt.report.data.oda.jdbc.ui.model.TableImpl;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.UserPropertyDefnHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.UserPropertyException;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.metadata.StringPropertyType;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;


/**
 * TODO: Please document
 * 
 * @version $Revision: 1.7 $ $Date: 2005/03/30 00:11:22 $
 */
public class Utility
{

//	 The suffix which will be used to create new names 
	// if duplicate objects are selected in the Table Selection Page
	private static final String dupAffix = "_";
	

	
    /**
     * 
     */
    private Utility()
    {
        super();
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
		Tree parent = parentItem.getParent();
		Font dataSourceItemFont = parent.getFont();
	
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
				
			}

	
			item[i].setText(displayName);
			item[i].setData(name);
			
			item[i].setImage(image);
			parent.setTopItem(item[i]);
			item[i].setExpanded(false);
	
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
	

}
