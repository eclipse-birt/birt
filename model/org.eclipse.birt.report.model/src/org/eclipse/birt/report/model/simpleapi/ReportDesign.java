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

package org.eclipse.birt.report.model.simpleapi;

import java.io.IOException;

import org.eclipse.birt.core.script.JavascriptEvalUtil;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.simpleapi.IDataItem;
import org.eclipse.birt.report.model.api.simpleapi.IDataSet;
import org.eclipse.birt.report.model.api.simpleapi.IDataSource;
import org.eclipse.birt.report.model.api.simpleapi.IDesignElement;
import org.eclipse.birt.report.model.api.simpleapi.IDynamicText;
import org.eclipse.birt.report.model.api.simpleapi.IGrid;
import org.eclipse.birt.report.model.api.simpleapi.IImage;
import org.eclipse.birt.report.model.api.simpleapi.ILabel;
import org.eclipse.birt.report.model.api.simpleapi.IList;
import org.eclipse.birt.report.model.api.simpleapi.IMasterPage;
import org.eclipse.birt.report.model.api.simpleapi.IReportDesign;
import org.eclipse.birt.report.model.api.simpleapi.IReportElement;
import org.eclipse.birt.report.model.api.simpleapi.IStyle;
import org.eclipse.birt.report.model.api.simpleapi.ITable;
import org.eclipse.birt.report.model.api.simpleapi.ITextItem;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.WrappedException;

/**
 * The Rhino native Report Design object.
 * <p>
 * Implement a native Rhino object as the scope so that the default mapping
 * (e.g., mapping "getTable" method to a "table" property) does not occur. The
 * various getXXX and setXXX methods on IReportDesign can only be invoked as
 * methods, not properties.
 * 
 */

public class ReportDesign extends ScriptableObject implements IReportDesign
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5768246404361271845L;

	/**
	 * The class name in JavaScript.
	 */

	public static final String CLASS_NAME = "ReportDesign"; //$NON-NLS-1$

	private InternalReportDesign report;

	/**
	 * Constructor.
	 * 
	 * @param report
	 */

	public ReportDesign( ReportDesignHandle report )
	{
		this.report = new InternalReportDesign( report );
		initFunctions( );
	}

	/**
	 * Gets master page script instance.
	 * 
	 * @param name
	 * @return master page script instance
	 */

	public IMasterPage getMasterPage( String name )
	{
		return report.getMasterPage( name );
	}

	public IDataSet getDataSet( String name )
	{
		return report.getDataSet( name );
	}

	public IDataSource getDataSource( String name )
	{
		return report.getDataSource( name );
	}

	public IReportElement getReportElement( String name )
	{
		return report.getReportElement( name );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.api.simpleapi.IReportDesign#
	 * getReportElementByID(long)
	 */
	public IReportElement getReportElementByID( long id )
	{
		return report.getReportElementByID( id );
	}

	public IDataItem getDataItem( String name )
	{
		return report.getDataItem( name );
	}

	public IGrid getGrid( String name )
	{
		return report.getGrid( name );
	}

	public IImage getImage( String name )
	{
		return report.getImage( name );
	}

	public ILabel getLabel( String name )
	{
		return report.getLabel( name );
	}

	public IList getList( String name )
	{
		return report.getList( name );
	}

	public ITable getTable( String name )
	{
		return report.getTable( name );
	}

	public IDynamicText getDynamicText( String name )
	{
		return report.getDynamicText( name );
	}

	public ITextItem getTextItem( String name )
	{
		return report.getTextItem( name );
	}

	public void setDisplayNameKey( String displayNameKey )
			throws SemanticException
	{
		report.setProperty( IDesignElementModel.DISPLAY_NAME_ID_PROP,
				displayNameKey );
	}

	public String getDisplayNameKey( )
	{
		return report.getDisplayNameKey( );
	}

	public void setDisplayName( String displayName ) throws SemanticException
	{
		report.setProperty( IDesignElementModel.DISPLAY_NAME_PROP, displayName );

	}

	public String getDisplayName( )
	{
		return report.getDisplayName( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IReportDesign#save()
	 */

	public void save( ) throws IOException
	{
		report.save( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IReportDesign#saveAs(java
	 * .lang.String)
	 */

	public void saveAs( String newName ) throws IOException
	{
		report.saveAs( newName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IReportDesign#getTheme()
	 */
	public String getTheme( )
	{
		return report.getTheme( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IReportDesign#setTheme(java
	 * .lang.String)
	 */

	public void setTheme( String theme ) throws SemanticException
	{
		report.setProperty( IModuleModel.THEME_PROP, theme );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IDesignElement#getParent()
	 */
	public IDesignElement getParent( )
	{
		return report.getParent( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IDesignElement#getNamedExpression
	 * (java.lang.String)
	 */
	public String getNamedExpression( String name )
	{
		return report.getNamedExpression( name );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IDesignElement#getQualifiedName
	 * ()
	 */
	public String getQualifiedName( )
	{
		return report.getQualifiedName( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IDesignElement#getReport()
	 */
	public IReportDesign getReport( )
	{
		return report;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IDesignElement#getStyle()
	 */
	public IStyle getStyle( )
	{
		return report.getStyle( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IDesignElement#getUserProperty
	 * (java.lang.String)
	 */
	public Object getUserProperty( String name )
	{
		return report.getUserProperty( name );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IDesignElement#setNamedExpression
	 * (java.lang.String, java.lang.String)
	 */
	public void setNamedExpression( String name, String exp )
			throws SemanticException
	{
		report.setNamedExpression( name, exp );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IDesignElement#setUserProperty
	 * (java.lang.String, java.lang.Object, java.lang.String)
	 */
	public void setUserProperty( String name, Object value, String type )
			throws SemanticException
	{
		report.setUserProperty( name, value, type );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IDesignElement#setUserProperty
	 * (java.lang.String, java.lang.String)
	 */
	public void setUserProperty( String name, String value )
			throws SemanticException
	{
		report.setUserProperty( name, value );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mozilla.javascript.ScriptableObject#getClassName()
	 */

	public String getClassName( )
	{
		return CLASS_NAME;
	}

	/**
	 * Adds all functions to the native object.
	 */
	
	private void initFunctions( )
	{
		defineProperty( "getNamedExpression", //$NON-NLS-1$
				new Function_getNamedExpression( ), 0 );
		defineProperty( "setNamedExpression", //$NON-NLS-1$
				new Function_setNamedExpression( ), 0 );

		defineProperty( "getDataItem", new Function_getDataItem( ), 0 ); //$NON-NLS-1$
		defineProperty( "getDataSet", new Function_getDataSet( ), 0 ); //$NON-NLS-1$
		defineProperty( "getDataSource", new Function_getDataSource( ), 0 ); //$NON-NLS-1$
		defineProperty( "getDynamicText", new Function_getDynamicText( ), 0 );//$NON-NLS-1$
		defineProperty( "getGrid", new Function_getGrid( ), 0 );//$NON-NLS-1$
		defineProperty( "getImage", new Function_getImage( ), 0 );//$NON-NLS-1$
		defineProperty( "getLabel", new Function_getLabel( ), 0 );//$NON-NLS-1$
		defineProperty( "getList", new Function_getList( ), 0 );//$NON-NLS-1$
		defineProperty( "getMasterPage", new Function_getMasterPage( ), 0 );//$NON-NLS-1$
		defineProperty( "getReportElement", new Function_getReportElement( ), 0 );//$NON-NLS-1$
		defineProperty( "getReportElementByID",//$NON-NLS-1$
				new Function_getReportElementByID( ), 0 );
		defineProperty( "getTable", new Function_getTable( ), 0 );//$NON-NLS-1$
		defineProperty( "getTextItem", new Function_getTextItem( ), 0 );//$NON-NLS-1$

		defineProperty( "getTheme", new Function_getTheme( ), 0 );//$NON-NLS-1$
		defineProperty( "setTheme", new Function_setTheme( ), 0 );//$NON-NLS-1$

		defineProperty( "getUserProperty", new Function_getUserProperty( ), 0 ); //$NON-NLS-1$
		defineProperty( "setUserProperty", new Function_setUserProperty( ), 0 ); //$NON-NLS-1$

		defineProperty( "save", new Function_save( ), 0 );//$NON-NLS-1$
		defineProperty( "saveAs", new Function_saveAs( ), 0 );//$NON-NLS-1$

		defineProperty( "getReport", new Function_getReport( ), 0 );//$NON-NLS-1$
	}

	private class Function_getDataItem extends BaseFunction
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		Function_getDataItem( )
		{
		}

		public Object call( Context cx, Scriptable scope, Scriptable thisObj,
				java.lang.Object[] args )
		{
			Object[] convertedArgs = JavascriptEvalUtil
					.convertToJavaObjects( args );

			return report.getDataItem( (String) convertedArgs[0] );
		}
	}

	private class Function_getDataSet extends BaseFunction
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		Function_getDataSet( )
		{
		}

		public Object call( Context cx, Scriptable scope, Scriptable thisObj,
				java.lang.Object[] args )
		{
			Object[] convertedArgs = JavascriptEvalUtil
					.convertToJavaObjects( args );

			return report.getDataSet( (String) convertedArgs[0] );
		}
	}

	private class Function_getDataSource extends BaseFunction
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		Function_getDataSource( )
		{
		}

		public Object call( Context cx, Scriptable scope, Scriptable thisObj,
				java.lang.Object[] args )
		{
			Object[] convertedArgs = JavascriptEvalUtil
					.convertToJavaObjects( args );

			return report.getDataSource( (String) convertedArgs[0] );
		}
	}

	private class Function_getDynamicText extends BaseFunction
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		Function_getDynamicText( )
		{
		}

		public Object call( Context cx, Scriptable scope, Scriptable thisObj,
				java.lang.Object[] args )
		{
			Object[] convertedArgs = JavascriptEvalUtil
					.convertToJavaObjects( args );

			return report.getDynamicText( (String) convertedArgs[0] );
		}
	}

	private class Function_getGrid extends BaseFunction
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		Function_getGrid( )
		{
		}

		public Object call( Context cx, Scriptable scope, Scriptable thisObj,
				java.lang.Object[] args )
		{
			Object[] convertedArgs = JavascriptEvalUtil
					.convertToJavaObjects( args );

			return report.getGrid( (String) convertedArgs[0] );
		}
	}

	private class Function_getImage extends BaseFunction
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		Function_getImage( )
		{
		}

		public Object call( Context cx, Scriptable scope, Scriptable thisObj,
				java.lang.Object[] args )
		{
			Object[] convertedArgs = JavascriptEvalUtil
					.convertToJavaObjects( args );

			return report.getImage( (String) convertedArgs[0] );
		}
	}

	private class Function_getLabel extends BaseFunction
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		Function_getLabel( )
		{
		}

		public Object call( Context cx, Scriptable scope, Scriptable thisObj,
				java.lang.Object[] args )
		{
			Object[] convertedArgs = JavascriptEvalUtil
					.convertToJavaObjects( args );

			return report.getLabel( (String) convertedArgs[0] );
		}
	}

	private class Function_getList extends BaseFunction
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		Function_getList( )
		{
		}

		public Object call( Context cx, Scriptable scope, Scriptable thisObj,
				java.lang.Object[] args )
		{
			Object[] convertedArgs = JavascriptEvalUtil
					.convertToJavaObjects( args );

			return report.getList( (String) convertedArgs[0] );
		}
	}

	private class Function_getMasterPage extends BaseFunction
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		Function_getMasterPage( )
		{
		}

		public Object call( Context cx, Scriptable scope, Scriptable thisObj,
				java.lang.Object[] args )
		{
			Object[] convertedArgs = JavascriptEvalUtil
					.convertToJavaObjects( args );

			return report.getMasterPage( (String) convertedArgs[0] );
		}
	}

	private class Function_getReport extends BaseFunction
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		Function_getReport( )
		{
		}

		public Object call( Context cx, Scriptable scope, Scriptable thisObj,
				java.lang.Object[] args )
		{
			return report.getReport( );
		}
	}

	private class Function_getReportElement extends BaseFunction
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		Function_getReportElement( )
		{
		}

		public Object call( Context cx, Scriptable scope, Scriptable thisObj,
				java.lang.Object[] args )
		{
			Object[] convertedArgs = JavascriptEvalUtil
					.convertToJavaObjects( args );

			return report.getReportElement( (String) convertedArgs[0] );
		}
	}

	private class Function_getReportElementByID extends BaseFunction
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		Function_getReportElementByID( )
		{
		}

		public Object call( Context cx, Scriptable scope, Scriptable thisObj,
				java.lang.Object[] args )
		{
			Object[] convertedArgs = JavascriptEvalUtil
					.convertToJavaObjects( args );

			return report.getReportElementByID( (Long) convertedArgs[0] );
		}
	}

	private class Function_getTable extends BaseFunction
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		Function_getTable( )
		{
		}

		public Object call( Context cx, Scriptable scope, Scriptable thisObj,
				java.lang.Object[] args )
		{
			Object[] convertedArgs = JavascriptEvalUtil
					.convertToJavaObjects( args );

			return report.getTable( (String) convertedArgs[0] );
		}
	}

	private class Function_getTextItem extends BaseFunction
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		Function_getTextItem( )
		{
		}

		public Object call( Context cx, Scriptable scope, Scriptable thisObj,
				java.lang.Object[] args )
		{
			Object[] convertedArgs = JavascriptEvalUtil
					.convertToJavaObjects( args );

			return report.getTextItem( (String) convertedArgs[0] );
		}
	}

	private class Function_getTheme extends BaseFunction
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		Function_getTheme( )
		{
		}

		public Object call( Context cx, Scriptable scope, Scriptable thisObj,
				java.lang.Object[] args )
		{
			return report.getTheme( );
		}
	}

	private class Function_getNamedExpression extends BaseFunction
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		Function_getNamedExpression( )
		{
		}

		public Object call( Context cx, Scriptable scope, Scriptable thisObj,
				java.lang.Object[] args )
		{
			Object[] convertedArgs = JavascriptEvalUtil
					.convertToJavaObjects( args );

			return report.getNamedExpression( (String) convertedArgs[0] );
		}
	}

	private class Function_setNamedExpression extends BaseFunction
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		Function_setNamedExpression( )
		{
		}

		public Object call( Context cx, Scriptable scope, Scriptable thisObj,
				java.lang.Object[] args )
		{
			Object[] convertedArgs = JavascriptEvalUtil
					.convertToJavaObjects( args );

			try
			{
				report.setNamedExpression( (String) convertedArgs[0],
						(String) convertedArgs[1] );
			}
			catch ( SemanticException e )
			{
				throw new WrappedException( e );
			}

			return null;
		}
	}

	private class Function_getUserProperty extends BaseFunction
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		Function_getUserProperty( )
		{
		}

		public Object call( Context cx, Scriptable scope, Scriptable thisObj,
				java.lang.Object[] args )
		{
			Object[] convertedArgs = JavascriptEvalUtil
					.convertToJavaObjects( args );

			return report.getUserProperty( (String) convertedArgs[0] );
		}
	}

	private class Function_save extends BaseFunction
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		Function_save( )
		{
		}

		public Object call( Context cx, Scriptable scope, Scriptable thisObj,
				java.lang.Object[] args )
		{
			try
			{
				report.save( );
			}
			catch ( IOException e )
			{
				throw new WrappedException( e );
			}

			return null;
		}
	}

	private class Function_saveAs extends BaseFunction
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		Function_saveAs( )
		{
		}

		public Object call( Context cx, Scriptable scope, Scriptable thisObj,
				java.lang.Object[] args )
		{
			Object[] convertedArgs = JavascriptEvalUtil
					.convertToJavaObjects( args );

			try
			{
				report.saveAs( (String) convertedArgs[0] );
			}
			catch ( IOException e )
			{
				throw new WrappedException( e );
			}

			return null;
		}
	}

	private class Function_setTheme extends BaseFunction
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		Function_setTheme( )
		{
		}

		public Object call( Context cx, Scriptable scope, Scriptable thisObj,
				java.lang.Object[] args )
		{
			Object[] convertedArgs = JavascriptEvalUtil
					.convertToJavaObjects( args );

			try
			{
				report.setTheme( (String) convertedArgs[0] );
			}
			catch ( SemanticException e )
			{
				throw new WrappedException( e );
			}

			return null;
		}
	}

	private class Function_setUserProperty extends BaseFunction
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		Function_setUserProperty( )
		{
		}

		public Object call( Context cx, Scriptable scope, Scriptable thisObj,
				java.lang.Object[] args )
		{
			Object[] convertedArgs = JavascriptEvalUtil
					.convertToJavaObjects( args );

			try
			{
				if ( convertedArgs.length == 2 )
					report.setUserProperty( (String) convertedArgs[0],
							(String) convertedArgs[1] );
				else if ( convertedArgs.length == 3 )
					report.setUserProperty( (String) convertedArgs[0],
							convertedArgs[1], (String) convertedArgs[2] );

			}
			catch ( SemanticException e )
			{
				throw new WrappedException( e );
			}

			return null;
		}
	}
}
