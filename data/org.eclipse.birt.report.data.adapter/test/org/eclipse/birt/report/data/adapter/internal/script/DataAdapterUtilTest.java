
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.data.adapter.internal.script;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Date;

import junit.framework.TestCase;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;


/**
 * 
 */

public class DataAdapterUtilTest extends TestCase
{
	public void testRegisterJSObject( )
	{
		try
		{
			//TODO:Add test cases.
			Context cx = Context.enter( );
			/*
			Scriptable target = new ImporterTopLevel( cx );
			Scriptable source = new ImporterTopLevel( cx );
			
			assertFalse( target.get( "row", target ) instanceof DummyJSObject);
			
			DummyResultIterator ri = new DummyResultIterator( source );
			
			assertFalse( target.get( "row", target ) instanceof DummyJSObject);
			assertTrue( source.get( "row", source ) instanceof DummyJSObject);
			
			DataAdapterUtil.registerJSObject( target, ri );
			
			assertTrue( source.get( "row", source ) instanceof DummyJSObject);
			assertTrue( target.get( "row", target ) instanceof DummyJSObject);*/
			
			
		}
		finally
		{
			Context.exit( );
		}
	}
	
	private class DummyResultIterator implements IResultIterator
	{
		private Scriptable scope;
		
		DummyResultIterator ( Scriptable scope )
		{
			assert scope!= null;
			this.scope = scope;
			this.scope.put( "row", scope, new DummyJSObject() );
		}
		public void close( ) throws BirtException
		{
			// TODO Auto-generated method stub
			
		}

		public boolean findGroup( Object[] groupKeyValues )
				throws BirtException
		{
			// TODO Auto-generated method stub
			return false;
		}

		public BigDecimal getBigDecimal( String name ) throws BirtException
		{
			// TODO Auto-generated method stub
			return null;
		}

		public Blob getBlob( String name ) throws BirtException
		{
			// TODO Auto-generated method stub
			return null;
		}

		public Boolean getBoolean( String name ) throws BirtException
		{
			// TODO Auto-generated method stub
			return Boolean.TRUE;
		}

		public byte[] getBytes( String name ) throws BirtException
		{
			// TODO Auto-generated method stub
			return null;
		}

		public Date getDate( String name ) throws BirtException
		{
			// TODO Auto-generated method stub
			return null;
		}

		public Double getDouble( String name ) throws BirtException
		{
			// TODO Auto-generated method stub
			return null;
		}

		public int getEndingGroupLevel( ) throws BirtException
		{
			// TODO Auto-generated method stub
			return 0;
		}

		public Integer getInteger( String name ) throws BirtException
		{
			// TODO Auto-generated method stub
			return null;
		}

		public IQueryResults getQueryResults( )
		{
			// TODO Auto-generated method stub
			return null;
		}

		public IResultMetaData getResultMetaData( ) throws BirtException
		{
			// TODO Auto-generated method stub
			return null;
		}

		public int getRowId( ) throws BirtException
		{
			// TODO Auto-generated method stub
			return 0;
		}

		public int getRowIndex( ) throws BirtException
		{
			// TODO Auto-generated method stub
			return 0;
		}

		public Scriptable getScope( )
		{
			return this.scope;
		}

		public IResultIterator getSecondaryIterator( String subQueryName,
				Scriptable scope ) throws BirtException
		{
			// TODO Auto-generated method stub
			return null;
		}

		public IResultIterator getSecondaryIterator( String subQueryName,
				ScriptContext context ) throws BirtException
		{
			// TODO Auto-generated method stub
			return null;
		}
		
		public int getStartingGroupLevel( ) throws BirtException
		{
			// TODO Auto-generated method stub
			return 0;
		}

		public String getString( String name ) throws BirtException
		{
			// TODO Auto-generated method stub
			return null;
		}

		public Object getValue( String name ) throws BirtException
		{
			// TODO Auto-generated method stub
			return null;
		}

		public boolean isEmpty( ) throws BirtException
		{
			// TODO Auto-generated method stub
			return false;
		}

		public void moveTo( int rowIndex ) throws BirtException
		{
			// TODO Auto-generated method stub
			
		}

		public boolean next( ) throws BirtException
		{
			// TODO Auto-generated method stub
			return false;
		}

		public void skipToEnd( int groupLevel ) throws BirtException
		{
			// TODO Auto-generated method stub
			
		}
		public boolean isBeforeFirst( ) throws BirtException
		{
			// TODO Auto-generated method stub
			return false;
		}
		public boolean isFirst( ) throws BirtException
		{
			// TODO Auto-generated method stub
			return false;
		}
		public IResultIterator getSecondaryIterator( ScriptContext context,
				String subQueryName ) throws BirtException
		{
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	private static class DummyJSObject extends ScriptableObject
	{
		public String getClassName( )
		{
			return "DummyJSObject";
		}
	}
}
