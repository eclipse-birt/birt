/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.views;

import java.lang.reflect.Proxy;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.expressions.EvaluationContext;
import org.eclipse.core.expressions.EvaluationResult;
import org.eclipse.core.expressions.ExpressionConverter;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

/**
 * ElementAdapterManager
 */
public class ElementAdapterManager
{

	public static final String ADAPTERS_EXTENSION_ID = "org.eclipse.birt.report.designer.ui.elementAdapters"; //$NON-NLS-1$

	protected static final Logger logger = Logger.getLogger( ElementAdapterManager.class.getName( ) );

	private static Map adaptersMap = new HashMap( ) {

		private static final long serialVersionUID = 534728316184090251L;

		public Object get( Object key )
		{
			Object obj = super.get( key );
			if ( obj == null )
			{
				obj = new ElementAdapterSet( );
				// need sync?
				// obj = Collections.synchronizedSortedSet( new
				// ElementAdapterSet( ) );
				put( key, obj );
			}
			return obj;
		}
	};

	static
	{
		// initial adaptersMap
		IExtensionRegistry registry = Platform.getExtensionRegistry( );
		IExtensionPoint extensionPoint = registry.getExtensionPoint( ADAPTERS_EXTENSION_ID );
		if ( extensionPoint != null )
		{
			IConfigurationElement[] elements = extensionPoint.getConfigurationElements( );
			for ( int j = 0; j < elements.length; j++ )
			{
				String adaptableClassName = elements[j].getAttribute( "class" ); //$NON-NLS-1$
				Class adaptableType = null;

				IConfigurationElement[] adapters = elements[j].getChildren( "adapter" ); //$NON-NLS-1$
				for ( int k = 0; k < adapters.length; k++ )
				{
					String adapterClassName = null;
					Class adapterType = null;

					try
					{
						ElementAdapter adapter = new ElementAdapter( );
						adapter.setId( adapters[k].getAttribute( "id" ) ); //$NON-NLS-1$

						adapter.setSingleton( !"false".equals( adapters[k].getAttribute( "singleton" ) ) ); //$NON-NLS-1$ //$NON-NLS-2$

						if ( adapters[k].getAttribute( "class" ) != null //$NON-NLS-1$
								&& !adapters[k].getAttribute( "class" ) //$NON-NLS-1$
										.equals( "" ) ) //$NON-NLS-1$
						{
							adapter.setAdapterInstance( adapters[k].createExecutableExtension( "class" ) ); //$NON-NLS-1$

							if ( !adapter.isSingleton( ) )
							{
								// cache the config element to create new instance
								adapter.setAdapterConfig( adapters[k] );
							}
						}
						else if ( adapters[k].getAttribute( "factory" ) != null //$NON-NLS-1$
								&& !adapters[k].getAttribute( "factory" ) //$NON-NLS-1$
										.equals( "" ) ) //$NON-NLS-1$
						{
							adapter.setFactory( (IAdapterFactory) adapters[k].createExecutableExtension( "factory" ) ); //$NON-NLS-1$
						}

						if ( adaptableType == null )
						{
							adaptableType = classForName( adaptableClassName,
									adapter.getAdapterInstance( ),
									adapter.getFactory( ) );
						}

						adapter.setAdaptableType( adaptableType );

						adapterClassName = adapters[k].getAttribute( "type" ); //$NON-NLS-1$

						adapterType = classForName( adapterClassName,
								adapter.getAdapterInstance( ),
								adapter.getFactory( ) );

						adapter.setAdapterType( adapterType );

						if ( adapters[k].getAttribute( "priority" ) != null //$NON-NLS-1$
								&& !adapters[k].getAttribute( "priority" ) //$NON-NLS-1$
										.equals( "" ) ) //$NON-NLS-1$
						{
							try
							{
								adapter.setPriority( Integer.parseInt( adapters[k].getAttribute( "priority" ) ) ); //$NON-NLS-1$
							}
							catch ( NumberFormatException e )
							{
							}
						}

						if ( adapters[k].getAttribute( "overwrite" ) != null //$NON-NLS-1$
								&& !adapters[k].getAttribute( "overwrite" ) //$NON-NLS-1$
										.equals( "" ) ) //$NON-NLS-1$
						{
							adapter.setOverwrite( adapters[k].getAttribute( "overwrite" ) //$NON-NLS-1$
									.split( ";" ) ); //$NON-NLS-1$
						}
						adapter.setIncludeWorkbenchContribute( "true".equals( adapters[k].getAttribute( "includeWorkbenchContribute" ) ) ); //$NON-NLS-1$ //$NON-NLS-2$

						IConfigurationElement[] enablements = adapters[k].getChildren( "enablement" ); //$NON-NLS-1$
						if ( enablements != null && enablements.length > 0 )
							adapter.setExpression( ExpressionConverter.getDefault( )
									.perform( enablements[0] ) );
						registerAdapter( adaptableType, adapter );
					}
					catch ( ClassNotFoundException ce )
					{
						if ( adaptableType == null )
						{
							System.out.println( MessageFormat.format( "Adaptable Type class '{0}' not found!", //$NON-NLS-1$
									new Object[]{
										adaptableClassName
									} ) );
							logger.log( Level.SEVERE, ce.getMessage( ), ce );
						}
						else
						{
							System.out.println( MessageFormat.format( "Adapter Type class '{0}' not found!", //$NON-NLS-1$
									new Object[]{
										adapterClassName
									} ) );
							logger.log( Level.SEVERE, ce.getMessage( ), ce );
						}
					}
					catch ( Exception e )
					{
						System.out.println( "Register adapter error!" ); //$NON-NLS-1$
						logger.log( Level.SEVERE, e.getMessage( ), e );
					}
				}
			}
		}
	}

	private static Class<?> classForName( String className,
			Object adapterInstance, IAdapterFactory adapterFacotry )
			throws ClassNotFoundException
	{
		Class<?> clazz = null;

		if ( adapterInstance != null )
		{
			try
			{
				clazz = adapterInstance.getClass( )
						.getClassLoader( )
						.loadClass( className );
			}
			catch ( ClassNotFoundException ex )
			{
				// fail over
			}
		}

		if ( clazz == null && adapterFacotry != null )
		{
			try
			{
				clazz = adapterFacotry.getClass( )
						.getClassLoader( )
						.loadClass( className );
			}
			catch ( ClassNotFoundException ex )
			{
				// it is possible that the default bundle classloader is unaware
				// of this class, but the adaptor factory can load it in some
				// other way. See bug 200068.
				Class[] adapterList = adapterFacotry.getAdapterList( );
				if ( adapterList != null && adapterList.length > 0 )
				{
					for ( int i = 0; i < adapterList.length; i++ )
					{
						if ( className.equals( adapterList[i].getName( ) ) )
						{
							clazz = adapterList[i];
							break;
						}
					}
				}
			}
		}

		if ( clazz == null )
		{
			clazz = Class.forName( className );
		}

		return clazz;
	}

	public static void registerAdapter( Class adaptableType,
			ElementAdapter adapter )
	{
		synchronized ( adaptersMap )
		{
			Set adapterSet = (Set) adaptersMap.get( adaptableType );
			adapterSet.add( adapter );
			// if ( adapterSet.add( adapter ) )
			// System.out.println( "Register adapter for "
			// + adaptableType.getName( )
			// + " "
			// + adapter.getId( ) );
			// else
			// System.out.println( "fail Register adapter for "
			// + adaptableType.getName( )
			// + " "
			// + adapter.getId( ) );
		}
	}

	public static Object[] getAdapters( Object adaptableObject,
			Class adatperType )
	{
		List adapterObjects = getAdapterList( adaptableObject, adatperType );

		return ( adapterObjects != null && adapterObjects.size( ) > 0 ) ? adapterObjects.toArray( new Object[adapterObjects.size( )] )
				: null;
	}

	public static Object getAdapter( Object adaptableObject, Class adatperType )
	{
		List adapterObjects = getAdapterList( adaptableObject, adatperType );
		if ( adapterObjects == null || adapterObjects.size( ) == 0 )
			return null;
		else if ( adapterObjects.size( ) == 1 )
			return adapterObjects.get( 0 );
		else
			return Proxy.newProxyInstance( adatperType.getClassLoader( ),
					new Class[]{
						adatperType
					},
					new ElementAdapterInvocationHandler( adapterObjects ) );
	}

	private static List getAdapterList( Object adaptableObject,
			Class adatperType )
	{
		Set adapters = getAdapters( adaptableObject );
		if ( adapters == null )
			return null;

		List adapterObjects = new ArrayList( );
		l: for ( Iterator iter = adapters.iterator( ); iter.hasNext( ); )
		{
			ElementAdapter adapter = (ElementAdapter) iter.next( );
			if ( adapter.getExpression( ) != null )
			{
				EvaluationContext context = new EvaluationContext( null,
						adaptableObject );
				context.setAllowPluginActivation( true );
				try
				{
					if ( adapter.getExpression( ).evaluate( context ) != EvaluationResult.TRUE )
						continue l;
				}
				catch ( CoreException e )
				{
				}
			}
			Object obj = adapter.getAdater( adaptableObject );
			if ( obj != null && adatperType.isAssignableFrom( obj.getClass( ) ) )
			{
				adapterObjects.add( obj );
			}
		}

		return adapterObjects;
	}

	private static Set getAdapters( Object adaptableObject )
	{
		if ( adaptableObject == null )
		{
			return Collections.emptySet( );
		}
		Set keys = adaptersMap.keySet( );
		ElementAdapterSet adapters = null;
		for ( Iterator iter = keys.iterator( ); iter.hasNext( ); )
		{
			Class clazz = (Class) iter.next( );
			// adaptable is the instance of the key class or its subclass.
			if ( clazz.isAssignableFrom( adaptableObject.getClass( ) ) )
			{
				if ( adapters == null )
				{
					adapters = new ElementAdapterSet( );
				}
				Set set = (Set) adaptersMap.get( clazz );
				for ( Iterator iterator = set.iterator( ); iterator.hasNext( ); )
				{
					adapters.add( iterator.next( ) );
				}
			}
		}
		if ( adapters != null )
			adapters.reset( );
		return adapters;
	}

}

/**
 * ElementAdapterSet
 */
class ElementAdapterSet extends TreeSet
{

	private static final long serialVersionUID = -3451274084543012212L;

	private static Comparator comparator = new Comparator( ) {

		public int compare( Object o1, Object o2 )
		{
			if ( o1 instanceof ElementAdapter && o2 instanceof ElementAdapter )
			{
				ElementAdapter adapter1 = (ElementAdapter) o1;
				ElementAdapter adapter2 = (ElementAdapter) o2;
				if ( adapter1.equals( adapter2 ) )
					return 0;
				int value = adapter1.getPriority( ) - adapter2.getPriority( );
				return value == 0 ? 1 : value;
			}
			return 0;
		}
	};

	private List overwriteList;

	private boolean isReset;

	/**
	 * A TreeSet sorted by ElementAdapter.getPriority( ).
	 */
	public ElementAdapterSet( )
	{
		super( comparator );
	}

	public boolean add( Object o )
	{
		if ( o instanceof ElementAdapter )
		{
			// cached overwrited adapters
			ElementAdapter adapter = (ElementAdapter) o;
			String[] overwriteIds = adapter.getOverwrite( );
			if ( overwriteIds != null && overwriteIds.length > 0 )
			{
				if ( this.overwriteList == null )
				{
					this.overwriteList = new ArrayList( );
				}
				for ( int i = 0; i < overwriteIds.length; i++ )
				{
					this.overwriteList.add( overwriteIds[i] );
				}
			}
			return super.add( o );
		}
		return false;
	}

	/**
	 * remove overwrited adapters.
	 */
	public void reset( )
	{
		if ( !isReset && this.overwriteList != null )
		{
			for ( Iterator iterator = this.iterator( ); iterator.hasNext( ); )
			{
				ElementAdapter adapter = (ElementAdapter) iterator.next( );
				if ( this.overwriteList.contains( adapter.getId( ) ) )
				{
					iterator.remove( );
					ElementAdapterManager.logger.log( Level.FINE,
							"<" + adapter.getId( ) + "> is overwritten." ); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
			this.isReset = true;
		}
	}
}
