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
 * 
 */

public class ElementAdapterManager
{
	protected static Logger logger = Logger.getLogger( ElementAdapterManager.class.getName( ) );


	private static Map adaptersMap = new HashMap( ) {

		/**
		 * Comment for <code>serialVersionUID</code>
		 */
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
		// ElementAdapter adapter1 = new ElementAdapter( );
		// adapter1.setAdapterableType( ModuleHandle.class );
		// adapter1.setAdapterType( INodeProvider.class );
		// adapter1.setFactory( new AdapterFactory1( ) );
		// adapter1.setPriority( 2 );
		// SortedSet adapters = (SortedSet) adaptersMap.get( ModuleHandle.class
		// );
		// adapters.add( adapter1 );
		//
		// ElementAdapter adapter2 = new ElementAdapter( );
		// adapter2.setAdapterableType( ModuleHandle.class );
		// adapter2.setAdapterType( INodeProvider.class );
		// adapter2.setFactory( new AdapterFactory2( ) );
		// adapter2.setPriority( 1 );
		//
		// adapters.add( adapter2 );
		IExtensionRegistry registry = Platform.getExtensionRegistry( );
		IExtensionPoint extensionPoint = registry.getExtensionPoint( "org.eclipse.birt.report.designer.ui.elementAdapters" );
		if ( extensionPoint != null )
		{
			IConfigurationElement[] elements = extensionPoint.getConfigurationElements( );
			for ( int j = 0; j < elements.length; j++ )
			{
				String adaptable = elements[j].getAttribute( "class" );
				try
				{
					Class adaptableType = Class.forName( adaptable );
					IConfigurationElement[] adapters = elements[j].getChildren( "adapter" );
					for ( int k = 0; k < adapters.length; k++ )
					{
						try
						{
							ElementAdapter adapter = new ElementAdapter( );
							adapter.setId( adapters[k].getAttribute( "id" ) );
							adapter.setAdaptableType( adaptableType );
							adapter.setAdapterType( Class.forName( adapters[k].getAttribute( "type" ) ) );

							if ( adapters[k].getAttribute( "class" ) != null
									&& !adapters[k].getAttribute( "class" )
											.equals( "" ) )
								adapter.setAdapterInstance( adapters[k].createExecutableExtension( "class" ) );
							else if ( adapters[k].getAttribute( "factory" ) != null
									&& !adapters[k].getAttribute( "factory" )
											.equals( "" ) )
								adapter.setFactory( (IAdapterFactory) adapters[k].createExecutableExtension( "factory" ) );

							adapter.setSingleton( !"false".equals( adapters[k].getAttribute( "singleton" ) ) );
							if ( adapters[k].getAttribute( "priority" ) != null
									&& !adapters[k].getAttribute( "priority" )
											.equals( "" ) )
								try
								{
									adapter.setPriority( Integer.parseInt( adapters[k].getAttribute( "priority" ) ) );
								}
								catch ( NumberFormatException e )
								{
								}

							if ( adapters[k].getAttribute( "overwrite" ) != null
									&& !adapters[k].getAttribute( "overwrite" )
											.equals( "" ) )
								adapter.setOverwrite( adapters[k].getAttribute( "overwrite" )
										.split( ";" ) );
							adapter.setIncludeWorkbenchContribute( "true".equals( adapters[k].getAttribute( "includeWorkbenchContribute" ) ) );

							IConfigurationElement[] enablements = adapters[k].getChildren( "enablement" );
							if ( enablements != null && enablements.length > 0 )
								adapter.setExpression( ExpressionConverter.getDefault( )
										.perform( enablements[0] ) );
							registerAdapter( adaptableType, adapter );
						}
						catch ( Exception e )
						{
							System.out.println( "Register adapter error!" );
							logger.log(Level.SEVERE, e.getMessage(),e);
						}
					}
				}
				catch ( ClassNotFoundException e )
				{
					// TODO Auto-generated catch block
					System.out.println( MessageFormat.format( "Adaptable Type {0} not found!",
							new Object[]{
								adaptable
							} ) );
					logger.log(Level.SEVERE, e.getMessage(),e);
				}

			}
		}
	}

	public static void registerAdapter( Class adaptableType,
			ElementAdapter adapter )
	{
		synchronized ( adaptersMap )
		{
			Set adapterSet = (Set) adaptersMap.get( adaptableType );
			adapterSet.add( adapter );
//			if ( adapterSet.add( adapter ) )
//				System.out.println( "Register adapter for "
//						+ adaptableType.getName( )
//						+ " "
//						+ adapter.getId( ) );
//			else
//				System.out.println( "fail Register adapter for "
//						+ adaptableType.getName( )
//						+ " "
//						+ adapter.getId( ) );
		}
	}

	public static Object getAdapter( Object adaptableObject, Class adatperType )
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
		return adapterObjects.size( ) > 0 ? Proxy.newProxyInstance( adatperType.getClassLoader( ),
				new Class[]{
					adatperType
				},
				new ElementAdapterInvocationHandler( adapterObjects ) )
				: null;
	}

	private static Set getAdapters( Object adaptableObject )
	{
		Set keys = adaptersMap.keySet( );
		ElementAdapterSet adapters = null;
		for ( Iterator iter = keys.iterator( ); iter.hasNext( ); )
		{
			Class clazz = (Class) iter.next( );
			// adaptable is the key's superclass.
			if ( adaptableObject.getClass( ).isAssignableFrom( clazz ) )
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

class ElementAdapterSet extends TreeSet
{

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
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
					if ( super.remove( adapter ) )
						System.out.println( adapter.getId( ) + " was filtered." );
				}
			}
			this.isReset = true;
		}
	}
}
