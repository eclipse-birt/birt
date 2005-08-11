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

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.DataSetRuntime;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.mozilla.javascript.NativeJavaObject;
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
	static private final String COLUMN_MD = "columnDefns";
	static public final String ROW_POSITION = "_rowPosition";
	
	private IResultObject resultObj;
    private IResultIterator resultSet;
    private DataSetRuntime	dataSet;
    private JSColumnMetaData cachedColumnMetaData;
    
    private boolean	allowUpdate = false;
    
	private static Logger logger = Logger.getLogger( JSRowObject.class.getName( ) );

	/**
     * Constructor. Creates an empty row object with no binding.
     */
    public JSRowObject( DataSetRuntime dataSet )
	{
		logger.entering( JSRowObject.class.getName( ), "JSRowObject" );
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
    	// We also have "dataSet", "columnMetadata", "row[0]" and "_rowPosition"
    	int count = 4 + 2*columnCount;		

    	int next = 0;
    	Object[] ids = new Object[count];
    	ids[next++] = DATA_SET;
    	ids[next++] = COLUMN_MD;
    	ids[next++] = new Integer(0);
    	ids[next++] = ROW_POSITION;
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
    				logger.logp( Level.FINER,
							JSColumnDefn.class.getName( ),
							"getIds",
							e.getMessage( ),
							e );
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
		logger.entering( JSRowObject.class.getName( ),
				"has",
				new Integer( index ) );
        // We maintain indexes 0 to columnCount
        // Column 0 is internal row ID; column 1 - columnCount are actual columns
    	IResultObject obj = this.getResultObject( );
	    	
		if ( index >= 0
				&& obj != null
				&& index <= obj.getResultClass( ).getFieldCount( ) )
		{
			logger.exiting( JSRowObject.class.getName( ),
					"has",
					new Boolean( true ) );
			return true;
		}
        
        // Let super handle the rest; caller may have added properties
		if ( logger.isLoggable( Level.FINER ) )
			logger.exiting( JSRowObject.class.getName( ),
				"has",
				new Boolean( super.has( index, start ) ) );
        return super.has( index, start );
    }
    
    /**
     * Checks if named property exists.
     */
    public boolean has( String name, Scriptable start )
    {
		logger.entering( JSRowObject.class.getName( ), "has", name );
    	if ( name.equals( DATA_SET )
				|| name.endsWith( COLUMN_MD ) 
				|| name.equals( ROW_POSITION ) )
		{
			logger.exiting( JSRowObject.class.getName( ),
					"has",
					new Boolean( true ) );
    		return true;
    	}
    	
        // Check if name is a valid column name or alias
       	IResultObject obj = this.getResultObject( );
		if ( obj != null
					&& obj.getResultClass( ).getFieldIndex( name ) >= 0 )
		{
			logger.exiting( JSRowObject.class.getName( ),
					"has",
					new Boolean( true ) );
			return true;
		}
        // Let super handle the rest; caller may have added properties
		if ( logger.isLoggable( Level.FINER ) )
			logger.exiting( JSRowObject.class.getName( ),
				"has",
				new Boolean( super.has( name, start ) ) );
        return super.has( name, start );
    }
    
    /**
     * Gets an indexed property
     */
    public Object get( int index, Scriptable start )
    {
		logger.entering( JSRowObject.class.getName( ),
				"get",
				new Integer( index ) );
       	// Special case: row[0] refers to internal row ID
       	// It has undefined meaning for standalone IResultObject (we will let
       	// IResultObject handle it in such case)
    	try
		{
	       	if ( index == 0 && resultSet != null )
	       	{
	    		logger.exiting( JSRowObject.class.getName( ),
						"get",
						new Integer( resultSet.getCurrentResultIndex( ) ) );
	       		return new Integer( resultSet.getCurrentResultIndex() );
	       	}
	       	else
	       	{
				IResultObject obj = this.getResultObject( );
				if ( obj != null ){
					if ( logger.isLoggable( Level.FINER ) )
		    			logger.exiting( JSRowObject.class.getName( ),
							"get",
							obj.getFieldValue( index ) );
					return obj.getFieldValue( index );
				}
				else{
		    		logger.exiting( JSRowObject.class.getName( ), "get", null );
					return null;
				}
			}
		}
    	catch (DataException e )
		{
    		logger.logp( Level.FINER,
					JSColumnDefn.class.getName( ),
					"get",
					e.getMessage( ),
					e );
    		logger.exiting( JSRowObject.class.getName( ), "get", null );
    		return null;
		}
	}
    
    /**
	 * Gets a named property
	 */
    public Object get( String name, Scriptable start )
    {
		logger.entering( JSRowObject.class.getName( ), "get", name );
    	if ( name.equals(DATA_SET) ){
			if ( logger.isLoggable( Level.FINER ) )
    			logger.exiting( JSRowObject.class.getName( ),
					"get",
					dataSet.getScriptable( ) );
    		return dataSet.getScriptable();
    	}
    	else if ( name.equals(COLUMN_MD) )
    	{
			if ( logger.isLoggable( Level.FINER ) )
    			logger.exiting( JSRowObject.class.getName( ),
					"get",
					getColumnMetadataScriptable( ) );
    		return getColumnMetadataScriptable();
    	}
		else if ( name.equals( ROW_POSITION ) )
		{
			try
			{
				if ( logger.isLoggable( Level.FINER ) )
					logger.exiting( JSRowObject.class.getName( ),
							"get",
							new Integer( resultSet.getCurrentResultIndex( ) ) );
				return new Integer( resultSet.getCurrentResultIndex( ) );
			}
			catch ( DataException e )
			{
				// Fall through and let super return not-found
				logger.logp( Level.FINER,
						JSColumnDefn.class.getName( ),
						"get",
						e.getMessage( ),
						e );
			}
		}
    	
    	// Try column names
        try
		{
			IResultObject obj = this.getResultObject( );
			if ( obj != null ){
				if ( logger.isLoggable( Level.FINER ) )
	    			logger.exiting( JSRowObject.class.getName( ),
						"get",
						obj.getFieldValue( name ) );
				return obj.getFieldValue( name );
			}
		}
		catch ( DataException e )
		{
			// Fall through and let super return not-found
    		logger.logp( Level.FINER,
					JSColumnDefn.class.getName( ),
					"get",
					e.getMessage( ),
					e );
    		
    		// Here we cannot return data exception directly because Rhino will sometimes 
    		// re-handle the return object and then return an Undefined type object to the caller
      		return new DataExceptionMocker( e );
 		}
		
		if ( logger.isLoggable( Level.FINER ) )
			logger.exiting( JSRowObject.class.getName( ),
				"get",
				super.get( name, start ) );
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
		logger.entering( JSRowObject.class.getName( ), "put", name );
    	if ( name.equals(DATA_SET) || name.equals(COLUMN_MD) )
    		// these two are not updatable
    		return;
    	
    	value = getRealValue( value );
        try
		{
        	IResultObject obj = this.getResultObject( );
        	int fieldIndex = -1;
        	if ( obj != null )
        		fieldIndex = obj.getResultClass().getFieldIndex( name );
        	
			if ( fieldIndex >= 0 )
			{
				// Update column value only of allowUpdate; otherwise no-op
				setFieldValue( fieldIndex, value );
			}
			else
			{
			    // Not a column managed by us, let super handle it
			    super.put( name, start, value );
			}
		}
		catch ( BirtException e )
		{
    		logger.logp( Level.FINER,
					JSColumnDefn.class.getName( ),
					"put",
					e.getMessage( ),
					e );
 		}
		logger.exiting( JSRowObject.class.getName( ), "put" );
    }
    
    /**
     * Sets an indexed property
     *
     */
    public void put( int index, Scriptable start, Object value )
    {
		logger.entering( JSRowObject.class.getName( ),
				"put",
				new Integer( index ) );
		
		value = getRealValue( value );
        try
		{
        	IResultObject obj = this.getResultObject( );    	
			if ( index >= 0	&& obj != null
					&& index <= obj.getResultClass( ).getFieldCount( ) )
			{
				setFieldValue( index, value );
			}
			else
			{
			    // Not a column managed by us, let super handle it
			    super.put( index, start, value );
			}
		}
		catch ( BirtException e )
		{
    		logger.logp( Level.FINER,
					JSColumnDefn.class.getName( ),
					"put",
					e.getMessage( ),
					e );
		}
		logger.exiting( JSRowObject.class.getName( ), "put" );
    }
    
    /**
	 * Value may be wrapped by NativeJavaObject, if so, the real value
	 * behind it needs to be unwrapped from it.
	 * 
	 * @param value
	 * @return real value
	 */
    private static Object getRealValue( Object value )
	{
		if ( value instanceof NativeJavaObject )
			return ( (NativeJavaObject) value ).unwrap( );
		else
			return value;
	}
    
    /** 
     * Sets the value of a custom field
     * @param fieldIndex 1-based index of custom field
     */
    private void setFieldValue( int fieldIndex, Object value ) 
    	throws BirtException
    {
    	if ( ! allowUpdate )
    		return;		// updates not allowed; ignore
    	
    	IResultObject obj = this.getResultObject( );
    	
		// Observe the type restriction on the column
		Class fieldClass = obj.getResultClass().getFieldValueClass(fieldIndex);
		if ( fieldClass != DataType.AnyType.class )
		{
			value = DataTypeUtil.convert( value, fieldClass);
		}
		obj.setCustomFieldValue( fieldIndex, value );
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
