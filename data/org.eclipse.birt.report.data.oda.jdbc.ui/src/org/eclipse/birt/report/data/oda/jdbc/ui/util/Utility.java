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

import org.eclipse.birt.report.data.oda.jdbc.ui.JdbcPlugin;
import org.eclipse.birt.report.data.oda.jdbc.ui.model.TableImpl;
import org.eclipse.birt.report.model.activity.SemanticException;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.UserPropertyDefnHandle;
import org.eclipse.birt.report.model.command.UserPropertyException;
import org.eclipse.birt.report.model.core.UserPropertyDefn;
import org.eclipse.birt.report.model.metadata.MetaDataException;
import org.eclipse.birt.report.model.metadata.StringPropertyType;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;


/**
 * TODO: Please document
 * 
 * @version $Revision: #2 $ $Date: 2005/02/05 $
 */
public class Utility
{

//	 The suffix which will be used to create new names 
	// if duplicate objects are selected in the Table Selection Page
	private static final String dupAffix = "_";
	
	private static String TABLE_ICON = "TableIcon";
	private static String VIEW_ICON = "ViewIcon";

	
	static
	{
		try
		{

			ImageRegistry reg = JFaceResources.getImageRegistry( );
			reg.put( TABLE_ICON,
					ImageDescriptor.createFromFile( JdbcPlugin.class,
							"icons/table.gif" ) );//$NON-NLS-1$
			reg.put( VIEW_ICON,
					ImageDescriptor.createFromFile( JdbcPlugin.class,
							"icons/view.gif" ) );//$NON-NLS-1$

		}
		catch ( Exception ex )
		{
		} // TODO Exception handling
	}

	
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
            catch (MetaDataException e)
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
	public static TreeItem[] createTreeItems(Tree parent, ArrayList dataSource, int style, Image image) 
	{
		if ( dataSource == null )
		{
			return null;
		}
		
		TreeItem item[] = new TreeItem[dataSource.size()];
		Font dataSourceItemFont = parent.getFont();
	
		for (int i = 0; i < dataSource.size(); i++)
		{
			item[i] = new TreeItem(parent, style);
			item[i].setText((String)dataSource.get(i));
			item[i].setImage( image );
			parent.setTopItem(item[i]);
		}
		return item;
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
				//displayName = ((TableItem)source).getText();
				//name = (String)((TableItem)source).getData();
				DbObject dbObject = (DbObject)source;
				name = dbObject.getName();
				displayName = name;
				
				if( dbObject.getType() == DbObject.TABLE_TYPE)
				{
					image = JFaceResources.getImage( TABLE_ICON );
				}
				else
				{
					image = JFaceResources.getImage( VIEW_ICON );
				}
			}
			//item[i].setText((String)dataSource.get(i));
	
			item[i].setText(displayName);
			item[i].setData(name);
			item[i].setImage( image );
			parent.setTopItem(item[i]);
	
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

}
