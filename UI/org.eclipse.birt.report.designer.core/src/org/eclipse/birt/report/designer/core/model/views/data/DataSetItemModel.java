/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.core.model.views.data;

import java.sql.Types;

/**
 * Presents data set item on data explorer view.
 */
public class DataSetItemModel
{
    private transient String dataSetColumnName = null;

	private transient String name = null;

	private transient Object parent = null;

	private transient String dataTypeName = null;

	private transient int dataType = Types.VARCHAR;
    
    private transient int position = -1;
    
    private transient String alias = null;
    
    private transient String helpText = null;
    

	/**
	 * @return the dataType.
	 */
	public final int getDataType( )
	{
		return dataType;
	}

	/**
	 * @param dataType
	 *            The dataType to set.
	 */
	public final void setDataType( int dataType )
	{
		this.dataType = dataType;
	}

	/**
	 * @return The dataTypeName.
	 */
	public final String getDataTypeName( )
	{
		return dataTypeName;
	}

	/**
	 * @param dataTypeName
	 *            The dataTypeName to set.
	 */
	public final void setDataTypeName( String dataTypeName )
	{
		this.dataTypeName = dataTypeName;
	}

	private transient String displayName = null;

	/**
	 * @return Returns the displayName.
	 */
	public String getDisplayName( )
	{
		if ( displayName == null || displayName.trim( ).length( ) == 0 )
		{
			return getName();
		}
		return displayName;
	}

	/**
	 * @param displayName
	 *            The displayName to set.
	 */
	public void setDisplayName( String displayName )
	{
		this.displayName = displayName;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName( )
	{
        if(name == null || name.trim().length() == 0)
        {
            return getDataSetColumnName();
        }
		return name;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName( String name )
	{
		this.name = name;
	}

	/**
	 * @return Returns the parent.
	 */
	public Object getParent( )
	{
		return parent;
	}

	/**
	 * @param parent
	 *            The parent to set.
	 */
	public void setParent( Object parent )
	{
		this.parent = parent;
	}
    /**
     * @return Returns the position.
     */
    public int getPosition()
    {
        return position;
    }
    /**
     * @param position The position to set.
     */
    public void setPosition(int position)
    {
        this.position = position;
    }
    /**
     * @return Returns the alias.
     */
    public String getAlias()
    {
        return alias;
    }
    /**
     * @param alias The alias to set.
     */
    public void setAlias(String alias)
    {
        this.alias = alias;
    }
    /**
     * @return Returns the helpText.
     */
    public String getHelpText()
    {
        return helpText;
    }
    /**
     * @param helpText The helpText to set.
     */
    public void setHelpText(String helpText)
    {
        this.helpText = helpText;
    }
    /**
     * @return Returns the dataSetColumnName.
     */
    public String getDataSetColumnName()
    {
        return dataSetColumnName;
    }
    /**
     * @param dataSetColumnName The dataSetColumnName to set.
     */
    public void setDataSetColumnName(String dataSetColumnName)
    {
        this.dataSetColumnName = dataSetColumnName;
    }
}