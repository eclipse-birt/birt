/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */ 
package org.eclipse.birt.data.engine.script;

import org.eclipse.birt.data.engine.impl.DataSetRuntime;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.eclipse.birt.data.engine.core.DataException;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * Underlying implementation of the Javascript "row" object. The ROM scripts use this 
 * JS object to access the current data row in a result set.
 * 
 * The JS row object can be bound to either an odi result set (in which case it maps 
 * to the current row object in the result set), or an individual IResultObject. 
 */
public class JSRowObject extends ScriptableObject
{
	static private final String DATA_SET = "dataSet";
	static private final String COLUMN_MD = "columnMetaData";
	
	private IResultObject resultObj;
    private IResultIterator resultSet;
    private DataSetRuntime	dataSet;
    private JSColumnMetaData cachedColumnMetaData;
    
    private boolean	allowUpdate = false;
    
    /**
     * Constructor. Creates an empty row object with no binding.
     */
    public JSRowObject( DataSetRuntime dataSet )
	{
    	this.dataSet = dataSet;
	}
    
    /**
     * Binds the row object to an odi result set. Exisint binding
     * is replaced.
     * @param resultSet Odi result iterator to bind to
     * @param allowUpdate If true, update to current row's column values are allowed
     */
    public void setResultSet( IResultIterator resultSet, boolean allowUpdate )
    {
        assert resultSet != null;
        this.resultSet = resultSet;
        resultObj = null;
        this.allowUpdate = allowUpdate;
    }
    
    /**
     * Binds the row object to a IResultObject. Existing bindings 
     * is replaced
     * @param resultObj Result object to bind to.
     * @param allowUpdate If true, update to current row's column values are allowed
     */
    public void setRowObject( IResultObject resultObj, boolean allowUpdate ) 
    {
    	assert resultObj != null;
		this.resultObj = resultObj;
		resultSet = null;
        this.allowUpdate = allowUpdate;
    }
    
    /* (non-Javadoc)
     * @see org.mozilla.javascript.Scriptable#getClassName()
     */
    public String getClassName()
    {
        return "DataRow";
    }
    
    /**
     * @see org.mozilla.javascript.Scriptable#getIds()
     */
    public Object[] getIds()
    {
    	IResultObject obj = this.getResultObject( );
    	int columnCount = 0;
    	if ( obj != null )
    	{
    		columnCount = obj.getResultClass().getFieldCount(); 
    	}
		// Each field can be accessed via index or name; hence 2 * 
    	// We also have "dataSet", "columnMetadata" and "row[0]"
    	int count = 3 + 2*columnCount;		

    	int next = 0;
    	Object[] ids = new Object[count];
    	ids[next++] = DATA_SET;
    	ids[next++] = COLUMN_MD;
    	ids[next++] = new Integer(0);
    	if ( columnCount > 0 )
    	{
    		for ( int i = 1; i <= columnCount; i++ )
    		{
    			ids[next++] = new Integer(i);
    			try
				{
    				ids[next++] = obj.getResultClass().getFieldName(i);
				}
    			catch (DataException e)
				{
    				// Shouldn't get here really
				}
    		}
    	}
    	
    	return ids;
    }
    
    /**
     * Checks if an indexed property exists
     */
    public boolean has( int index, Scriptable start )
    {
        // We maintain indexes 0 to columnCount
        // Column 0 is internal row ID; column 1 - columnCount are actual columns
    	IResultObject obj = this.getResultObject( );
	    	
		if ( index >= 0
				&& obj != null
				&& index <= obj.getResultClass( ).getFieldCount( ) )
		{
			return true;
		}
        
        // Let super handle the rest; caller may have added properties
        return super.has( index, start );
    }
    
    /**
     * Checks if named property exists.
     */
    public boolean has( String name, Scriptable start )
    {
    	if ( name.equals(DATA_SET) || name.endsWith(COLUMN_MD) )
    		return true;
    	
        // Check if name is a valid column name or alias
       	IResultObject obj = this.getResultObject( );
		if ( obj != null
					&& obj.getResultClass( ).getFieldIndex( name ) >= 0 )
		{
			return true;
		}
        // Let super handle the rest; caller may have added properties
        return super.has( name, start );
    }
    
    /**
     * Gets an indexed property
     */
    public Object get( int index, Scriptable start )
    {
       	// Special case: row[0] refers to internal row ID
       	// It has undefined meaning for standalone IResultObject (we will let
       	// IResultObject handle it in such case)
    	try
		{
	       	if ( index == 0 && resultSet != null )
	       		return new Integer( resultSet.getCurrentResultIndex() );
	       	else
	       	{
				IResultObject obj = this.getResultObject( );
				if ( obj != null )
					return obj.getFieldValue( index );
				else
					return null;
			}
		}
    	catch (DataException e )
		{
    		// TODO: log exception
    		e.printStackTrace();
    		return null;
		}
	}
    
    /**
	 * Gets a named property
	 */
    public Object get( String name, Scriptable start )
    {
    	if ( name.equals(DATA_SET) )
    		return dataSet.getScriptable();
    	else if ( name.equals(COLUMN_MD) )
    	{
    		return getColumnMetadataScriptable();
    	}
    	
    	// Try column names
        try
		{
			IResultObject obj = this.getResultObject( );
			if ( obj != null )
				return obj.getFieldValue( name );
		}
		catch ( DataException e )
		{
			// Fall through and let super return not-found
		}
		
		return super.get( name, start );
    }

    /** Gets a JS object that implements the ColumnDefn[] array */
    Scriptable getColumnMetadataScriptable()
	{
		IResultObject obj = this.getResultObject( );
		if ( obj == null || obj.getResultClass() == null )
			return null;
		
    	// If the result class has not changed since we last created
    	// the JSColumnMetaData object, return the same object
		// Otherwise create a new one
    	if ( cachedColumnMetaData == null || 
    		 cachedColumnMetaData.getResultClass() != obj.getResultClass() )
    	{
    		cachedColumnMetaData = new JSColumnMetaData( obj.getResultClass() );
    	}
    	
    	return cachedColumnMetaData;
	}
    
    /**
     * Sets a named property
     *
     */
    public void put( String name, Scriptable start, Object value )
    {
    	if ( name.equals(DATA_SET) || name.equals(COLUMN_MD) )
    		// these two are not updatable
    		return;
    	
        try
		{
        	IResultObject obj = this.getResultObject( );
			if ( obj != null
					&& obj.getResultClass( ).getFieldIndex( name ) >= 0 )
			{
				// Update column value only of allowUpdate; otherwise no-op
				if ( allowUpdate )
					obj.setCustomFieldValue( name, value );
			}
			else
			{
			    // Not a column managed by us, let super handle it
			    super.put( name, start, value );
			}
		}
		catch ( DataException e )
		{
		}
    }
    
    /**
     * Sets an indexed property
     *
     */
    public void put( int index, Scriptable start, Object value )
    {
        try
		{
        	IResultObject obj = this.getResultObject( );    	
			if ( index >= 0	&& obj != null
					&& index <= obj.getResultClass( ).getFieldCount( ) )
			{
				// Update column value only of allowUpdate; otherwise no-op
				if ( allowUpdate )
					obj.setCustomFieldValue( index, value );
			}
			else
			{
			    // Not a column managed by us, let super handle it
			    super.put( index, start, value );
			}
		}
		catch ( DataException e )
		{
		}
    }
    
    /**
     * Get result object
     * 1> from IResultObject
     * 2> from CachedResultSet
     * @return
     */
    protected IResultObject getResultObject( )
	{
		IResultObject resultObject;
		if ( resultSet != null )
		{
			try
			{
				resultObject = resultSet.getCurrentResult( );
			}
			catch ( DataException e )
			{
				resultObject = null;
			}
		}
		else
		{
			resultObject = resultObj;
		}
		// assert resultObject!=null;
		return resultObject;
	}
    
}
