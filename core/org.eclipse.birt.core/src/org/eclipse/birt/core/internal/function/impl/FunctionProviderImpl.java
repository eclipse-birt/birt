/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.internal.function.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.IConfigurationElement;
import org.eclipse.birt.core.framework.IExtension;
import org.eclipse.birt.core.framework.IExtensionPoint;
import org.eclipse.birt.core.framework.IExtensionRegistry;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.core.script.functionservice.IScriptFunction;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionArgument;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionCategory;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionFactory;
import org.eclipse.birt.core.script.functionservice.impl.Argument;
import org.eclipse.birt.core.script.functionservice.impl.Category;
import org.eclipse.birt.core.script.functionservice.impl.CategoryWrapper;
import org.eclipse.birt.core.script.functionservice.impl.IFunctionProvider;
import org.eclipse.birt.core.script.functionservice.impl.ScriptFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.osgi.framework.Bundle;

/**
 * 
 */

public class FunctionProviderImpl implements IFunctionProvider
{
	//The extension constants
	private static final String EXTENSION_POINT = "org.eclipse.birt.core.ScriptFunctionService";
	private static final String ELEMENT_CATEGORY = "Category";
	private static final String ELEMENT_FUNCTION = "Function";
	private static final String ELEMENT_ARGUMENT = "Argument";
	private static final String ELEMENT_JSLIB = "JSLib";
	private static final String ELEMENT_DATATYPE = "DataType";

	private static final String ATTRIBUTE_NAME = "name";
	private static final String ATTRIBUTE_DESC = "desc";
	private static final String ATTRIBUTE_FACTORYCLASS = "factoryclass";
	private static final String ATTRIBUTE_VALUE = "value";
	private static final String ATTRIBUTE_ISOPTIONAL = "isOptional";
	private static final String ATTRIBUTE_ALLOWVARARGUMENT = "variableArguments";
	private static final String ATTRIBUTE_ISSTATIC="isStatic";
	private static final String ATTRIBUTE_ISCONSTRUCTOR="isConstructor";
	private static final String ATTRIBUTE_LOCATION = "location";
	private static final String ATTRIBUTE_ISVISIBLE="isVisible";
	
	private static final String DEFAULT_CATEGORYNAME = null;

	private Map<String, Category> categories;
	private List<URL> jsLibs = new ArrayList<URL>( );
	private List<URL> jarLibs = new ArrayList<URL>( );

	/**
	 * Return all the categories defined by extensions.
	 * 
	 * @return
	 * @throws BirtException
	 */
	public IScriptFunctionCategory[] getCategories( )
			throws BirtException
	{
		return getCategoryMap( ).values( )
				.toArray( new IScriptFunctionCategory[]{} );
	}

	/**
	 * Return the functions that defined in a category.
	 * 
	 * @param categoryName
	 * @return
	 * @throws BirtException
	 */
	public IScriptFunction[] getFunctions( String categoryName )
			throws BirtException
	{
		if ( getCategoryMap( ).containsKey( categoryName ) )
		{
			Category category = getCategoryMap( ).get( categoryName );
			return category.getFunctions( );
		}

		return new IScriptFunction[0];
	}

	/**
	 * Register script functions to scope.
	 * 
	 * @param cx
	 * @param scope
	 * @throws BirtException
	 */
	public void registerScriptFunction( Context cx, Scriptable scope )
			throws BirtException
	{
		List<CategoryWrapper> wrapperedCategories = getWrapperedCategories( );
		for ( CategoryWrapper category : wrapperedCategories )
		{
			ScriptableObject.putProperty( scope,
					category.getClassName( ),
					category );
		}

		if( !jarLibs.isEmpty( ) )
		{
			ClassLoader classLoader = cx.getApplicationClassLoader( );
			URLClassLoader scriptClassLoader = createScriptClassLoader( jarLibs,
					classLoader );
			setApplicationClassLoader( scriptClassLoader, cx );			
		}
		for ( URL url : jsLibs )
		{
			Script script;
			try
			{
				script = cx.compileReader( new BufferedReader( new InputStreamReader( url.openStream( ) ) ),
						null,
						0,
						null );
				script.exec( cx, scope );
			}
			catch ( IOException e )
			{
			}
		}
	}

	public void setApplicationClassLoader( final ClassLoader appLoader,
			Context context )
	{
		if ( appLoader == null )
		{
			return;
		}
		ClassLoader loader = appLoader;
		try
		{
			appLoader.loadClass( "org.mozilla.javascript.Context" );
		}
		catch ( ClassNotFoundException e )
		{
			loader = AccessController.doPrivileged( new PrivilegedAction<ClassLoader>( ) {

				public ClassLoader run( )
				{
					return new RhinoClassLoaderDecoration( appLoader,
							FunctionProviderImpl.class.getClassLoader( ) );
				}
			} );
		}
		context.setApplicationClassLoader( loader );
	}

	private synchronized URLClassLoader createScriptClassLoader( List urls,
			ClassLoader parent )
	{
		final URL[] jarUrls = (URL[]) urls.toArray( new URL[]{} );
		final ClassLoader parentClassLoader = parent;
		URLClassLoader scriptClassLoader = AccessController.doPrivileged( new PrivilegedAction<URLClassLoader>( ) {

			public URLClassLoader run( )
			{
				return new URLClassLoader( jarUrls, parentClassLoader );
			}
		} );
		return scriptClassLoader;
	}

	private static class RhinoClassLoaderDecoration extends ClassLoader
	{

		private ClassLoader applicationClassLoader;
		private ClassLoader rhinoClassLoader;

		public RhinoClassLoaderDecoration( ClassLoader applicationClassLoader,
				ClassLoader rhinoClassLoader )
		{
			this.applicationClassLoader = applicationClassLoader;
			this.rhinoClassLoader = rhinoClassLoader;
		}

		public Class<?> loadClass( String name ) throws ClassNotFoundException
		{
			try
			{
				return applicationClassLoader.loadClass( name );
			}
			catch ( ClassNotFoundException e )
			{
				return rhinoClassLoader.loadClass( name );
			}
		}
	}

	/**
	 * Return the category map.
	 * 
	 * @return
	 */
	private synchronized Map<String, Category> getCategoryMap( )
	{
		if ( categories != null )
			return categories;

		categories = new HashMap<String, Category>( );
		
		//Find the extension point.
		IExtensionRegistry extReg = Platform.getExtensionRegistry( );
		IExtensionPoint extPoint = extReg.getExtensionPoint( EXTENSION_POINT );

		if ( extPoint == null )
			return categories;

		//Fetch all extensions
		IExtension[] exts = extPoint.getExtensions( );
		if ( exts == null )
		{
			return categories;
		}

		//populate category map as per extension.
		for ( int e = 0; e < exts.length; e++ )
		{
			try
			{
				IConfigurationElement[] configElems = exts[e].getConfigurationElements( );
				if ( configElems == null )
					continue;

				for ( int i = 0; i < configElems.length; i++ )
				{
					boolean isVisible = extractBoolean( configElems[i].getAttribute( ATTRIBUTE_ISVISIBLE ),
							true );
					// for element Category
					if ( configElems[i].getName( ).equals( ELEMENT_CATEGORY ) )
					{
						Category category = new Category( configElems[i].getAttribute( ATTRIBUTE_NAME ),
								configElems[i].getAttribute( ATTRIBUTE_DESC ),
								isVisible );
						categories.put( category.getName( ), category );

						IScriptFunctionFactory factory = null;
						if ( configElems[i].getAttribute( ATTRIBUTE_FACTORYCLASS ) != null )
							factory = (IScriptFunctionFactory) configElems[i].createExecutableExtension( ATTRIBUTE_FACTORYCLASS );
						IConfigurationElement[] functions = configElems[i].getChildren( ELEMENT_FUNCTION );
						for ( int j = 0; j < functions.length; j++ )
						{
							IScriptFunction function = getScriptFunction( category,
									factory,
									functions[j] );
							if ( function != null )
								category.addFunction( function );
						}

					}
					// For element function that are not under certain category.
					// Usually those functions are
					// defined in .js file
					else if ( configElems[i].getName( )
							.equals( ELEMENT_FUNCTION ) )
					{
						if ( categories.get( DEFAULT_CATEGORYNAME ) == null )
						{
							categories.put( DEFAULT_CATEGORYNAME,
									new Category( DEFAULT_CATEGORYNAME,
											null,
											isVisible ) );
						}
						IScriptFunction function = getScriptFunction( categories.get( DEFAULT_CATEGORYNAME ),
								null,
								configElems[i] );
						if ( function != null )
							categories.get( DEFAULT_CATEGORYNAME )
									.addFunction( function );
					}
					// Populate the .js script library
					else if ( configElems[i].getName( ).equals( ELEMENT_JSLIB ) )
					{
						populateResources( jsLibs, ".js", configElems[i] );
						populateResources( jarLibs, ".jar", configElems[i] );
					}
				}
			}
			catch ( BirtException ex )
			{
				ex.printStackTrace( );
			}
		}
		return categories;
	}

	/**
	 * Populate library resources. The library resources includes .js script lib and .jar java lib.
	 * @param libs
	 * @param suffix
	 * @param confElement
	 */
	private static void populateResources( List<URL> libs, String suffix,
			IConfigurationElement confElement )
	{
		String source = confElement.getAttribute( ATTRIBUTE_LOCATION );
		IExtension extension = confElement.getDeclaringExtension( );
		String namespace = extension.getNamespace( );
		Bundle bundle = org.eclipse.core.runtime.Platform.getBundle( namespace );
		//available on OSGi platform
		if ( bundle != null )
		{
			Enumeration<String> files = bundle.getEntryPaths( source );

			if ( files != null )
			{
				// In this case, the bundle denotes to a directory.
				while ( files.hasMoreElements( ) )
				{
					String filePath = files.nextElement( );
					if ( filePath.toLowerCase( ).endsWith( suffix ) )
					{
						URL url = bundle.getEntry( filePath );
						if ( url != null )
						{
							libs.add( url );
						}
					}
				}
			}
			else
			{
				// the bundle denotes to a file.
				if ( source.toLowerCase( ).endsWith( suffix ) )
				{
					URL url = bundle.getEntry( source );
					if ( url != null )
					{
						libs.add( url );
					}
				}
			}
		}
		//if in non-osgi mode, take it as absolute path
		if( bundle == null || libs.isEmpty( ) )
		{
			File file = new File( source );
			if( file.exists( )&& file.isDirectory( ) )
			{
				libs.addAll( findFileList( file, suffix ) );
			}
			else
			{
				if ( source.toLowerCase( ).endsWith( suffix ) )
					try
					{
						libs.add( new URL( source ) );
					}
					catch ( MalformedURLException e )
					{
						//ignore it
					}				
			}
		}
	}
	
	/**
	 * 
	 * @param file
	 * @param suffix
	 * @return
	 */
	private static List<URL> findFileList( File file, String suffix )
	{
		List<URL> fileList = new ArrayList<URL>( );
		for ( File f : file.listFiles( ) )
		{
			if ( f.isFile( ) && f.getName( ).endsWith( suffix ) )
			{
				try
				{
					fileList.add( f.toURI( ).toURL( ) );
				}
				catch ( MalformedURLException e )
				{
					//ignore it
				}
			}
			else if ( f.isDirectory( ) )
			{
				fileList.addAll( findFileList( f, suffix ) );
			}
		}
		return fileList;
	}

	/**
	 * Create script function out of a function element.
	 * @param category
	 * @param factory
	 * @param function
	 * @return
	 */
	private static IScriptFunction getScriptFunction( Category category,
			IScriptFunctionFactory factory, IConfigurationElement function )
	{
		try
		{
			//Function name
			String name = function.getAttribute( ATTRIBUTE_NAME );
			//Function Desc
			String desc = function.getAttribute( ATTRIBUTE_DESC );
			//Allow var argument
			String varArgs = function.getAttribute( ATTRIBUTE_ALLOWVARARGUMENT );
			boolean allowVarArgs = extractBoolean( varArgs, false );
			boolean isConstructor = extractBoolean( function.getAttribute( ATTRIBUTE_ISCONSTRUCTOR ), false);
			boolean isStatic = extractBoolean( function.getAttribute( ATTRIBUTE_ISSTATIC ), true);
			boolean isVisible = extractBoolean( function.getAttribute( ATTRIBUTE_ISVISIBLE ), true);
			String dataType = null;
			List<IScriptFunctionArgument> arguments = new ArrayList<IScriptFunctionArgument>( );
			//Populate function return data type info.
			if ( hasChildren( ELEMENT_DATATYPE, function ) )
			{
				dataType = function.getChildren( ELEMENT_DATATYPE )[0].getAttribute( ATTRIBUTE_VALUE );
			}
			
			//Popualte function argument info
			if ( hasChildren( ELEMENT_ARGUMENT, function ) )
			{
				for ( int i = 0; i < function.getChildren( ELEMENT_ARGUMENT ).length; i++ )
				{
					arguments.add( getScriptFunctionArgument( function.getChildren( ELEMENT_ARGUMENT )[i] ) );
				}
			}
			return new ScriptFunction( name,
					category,
					arguments.toArray( new IScriptFunctionArgument[0] ),
					dataType,
					desc,
					factory == null ? null : factory.getFunctionExecutor( name ),
					allowVarArgs,
					isStatic,
					isConstructor,
					isVisible );
		}
		catch ( Exception e )
		{
			return null;
		}
	}

	private static boolean extractBoolean( String strValue, boolean ifNull )
			throws BirtException
	{
		boolean booleanValue = strValue == null ? ifNull
				: DataTypeUtil.toBoolean( strValue );
		return booleanValue;
	}

	/**
	 * Populate function argument.
	 * @param argument
	 * @return
	 * @throws BirtException
	 */
	private static IScriptFunctionArgument getScriptFunctionArgument(
			IConfigurationElement argument ) throws BirtException
	{
		//
		String name = argument.getAttribute( ATTRIBUTE_NAME );
		String desc = argument.getAttribute( ATTRIBUTE_DESC );

		//populate whether it is optional argument.
		String optional = argument.getAttribute( ATTRIBUTE_ISOPTIONAL );
		boolean isOptional = extractBoolean( optional, false );

		String dataType = null;

		//Populate data type
		if ( hasChildren( ELEMENT_DATATYPE, argument ) )
		{
			dataType = argument.getChildren( ELEMENT_DATATYPE )[0].getAttribute( ATTRIBUTE_VALUE );
		}

		return new Argument( name,
				dataType,
				desc,
				isOptional );
	}

	/**
	 * 
	 * @param name
	 * @param element
	 * @return
	 */
	private static boolean hasChildren( String name,
			IConfigurationElement element )
	{
		IConfigurationElement[] children = element.getChildren( name );
		return children != null && children.length > 0;
	}

	/**
	 * Create category wrapper.
	 * 
	 * @return
	 * @throws BirtException
	 */
	private List<CategoryWrapper> getWrapperedCategories( )
			throws BirtException
	{
		List<CategoryWrapper> result = new ArrayList<CategoryWrapper>( );

		for ( Category category : getCategoryMap( ).values( ) )
		{
			if ( category.getName( ) != DEFAULT_CATEGORYNAME )
				result.add( new CategoryWrapper( category ) );
		}
		return result;
	}

}
