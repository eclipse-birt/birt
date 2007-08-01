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

package org.eclipse.birt.report.designer.internal.ui.dnd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionDelta;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IRegistryChangeEvent;
import org.eclipse.core.runtime.IRegistryChangeListener;
import org.eclipse.core.runtime.Platform;

/**
 * 1.register adapters with priority 2.look for capable adapter by priority
 */

public class DNDService implements IRegistryChangeListener
{

	public static int LOGIC_TRUE = 1;
	public static int LOGIC_FALSE = -1;
	public static int LOGIC_UNKNOW = 0;

	private List adapterList = new ArrayList( );
	private Map adapterPriorityMap = new HashMap( );

	private List dragAdapterList = new ArrayList( );
	private List dropAdapterList = new ArrayList( );

	private static class DNDServiceInstance {
		static DNDService instance = new DNDService( );
	}
	
	protected Logger logger = Logger.getLogger( DNDService.class.getName( ) );

	private DNDService( )
	{
		/*
		// DesignElementHandle adapter
		addDNDAdapter( new DesignElementHandleDNDAdapter( ),
				IDNDAdapter.CAPABLE_LOW );
		addDNDAdapter( new CascadingParameterGroupHandleDNDAdapter( ),
				IDNDAdapter.CAPABLE_HIGH );
		//		addDNDAdapter( new ParameterGroupHandleDNDAdapter( ),
		//				IDNDAdapter.CAPABLE_HIGH );
		addDNDAdapter( new ThemeHandleDNDAdapter( ), IDNDAdapter.CAPABLE_HIGH );
		addDNDAdapter( new SlotHandleDNDAdapter( ), IDNDAdapter.CAPABLE_HIGH );
		addDNDAdapter( new EmbeddedImageHandleDNDAdapter( ),
				IDNDAdapter.CAPABLE_HIGH );
		addDNDAdapter( new CellHandleDNDAdapter( ), IDNDAdapter.CAPABLE_HIGH );
		addDNDAdapter( new RowHandleDNDAdapter( ), IDNDAdapter.CAPABLE_HIGH );
		*/
		IExtensionRegistry registry = Platform.getExtensionRegistry( );
		IExtensionPoint extensionPoint = registry.getExtensionPoint( "org.eclipse.birt.report.designer.ui.DNDServices" );
		if ( extensionPoint != null )
		{
			addRegistry( extensionPoint );
		}
	}

	public static DNDService getInstance( )
	{
		return DNDServiceInstance.instance;
	}

	public boolean validDrag( Object object )
	{
		if ( object instanceof Object[] )
		{
//			boolean canDrag = false;
//			for ( int i = 0; i < ( (Object[]) object ).length; i++ )
//			{
//				canDrag = validDrag( ( (Object[]) object )[i] );
//				if ( !canDrag )
//					return false;
//			}
//			return canDrag;
			if (((Object[]) object).length == 1)
			{
				return validDrag(( (Object[]) object )[0]);
			}
		}

		for ( Iterator iterator = this.dragAdapterList.iterator( ); iterator.hasNext( ); )
		{
			IDragAdapter dragAdapter = (IDragAdapter) iterator.next( );
			int result = dragAdapter.canDrag( object );
			if ( result == LOGIC_TRUE )
				return true;
			if ( result == LOGIC_FALSE )
				return false;
		}
		return false;
	}

	public Object getDragTransfer( Object object )
	{
		//TODO maybe can cache dragAdapter in validDrag call.
		for ( Iterator iterator = this.dragAdapterList.iterator( ); iterator.hasNext( ); )
		{
			IDragAdapter dragAdapter = (IDragAdapter) iterator.next( );
			int result = dragAdapter.canDrag( object );
			if ( result == LOGIC_TRUE )
				return dragAdapter.getDragTransfer( object );
		}
		return null;
	}

	public boolean validDrop( Object transfer, Object target, int operation,
			DNDLocation location )
	{
		if ( transfer instanceof Object[] )
		{
//			boolean canDrop = false;
//			for ( int i = 0; i < ( (Object[]) transfer ).length; i++ )
//			{
//				canDrop = validDrop( ( (Object[]) transfer )[i],
//						target,
//						operation,
//						location );
//				if ( !canDrop )
//					return false;
//			}
//			return canDrop;
			if (((Object[]) transfer).length == 1)
			{
				return validDrop(( (Object[]) transfer )[0],target,operation,location );
			}
		}
		for ( Iterator iterator = this.dropAdapterList.iterator( ); iterator.hasNext( ); )
		{
			IDropAdapter dropAdapter = (IDropAdapter) iterator.next( );
			int result = dropAdapter.canDrop( transfer,
					target,
					operation,
					location );
			if ( result == LOGIC_TRUE )
				return true;
			if ( result == LOGIC_FALSE )
				return false;
		}
		return false;
	}

	public boolean performDrop( Object transfer, Object target, int operation,
			DNDLocation location )
	{
		if ( transfer instanceof Object[] )
		{
//			boolean result = false;
//			for ( int i = 0; i < ( (Object[]) transfer ).length; i++ )
//			{
//				result = performDrop( ( (Object[]) transfer )[i],
//						target,
//						operation,
//						location );
//				if ( !result )
//					return false;
//			}
//			return result;
			if (((Object[]) transfer).length == 1)
			{
				return performDrop(( (Object[]) transfer )[0],target,operation,location );
			}
		}
		
		for ( Iterator iterator = this.dropAdapterList.iterator( ); iterator.hasNext( ); )
		{
			IDropAdapter dropAdapter = (IDropAdapter) iterator.next( );
			int result = dropAdapter.canDrop( transfer,
					target,
					operation,
					location );
			if ( result == LOGIC_TRUE )
				return dropAdapter.performDrop( transfer,
						target,
						operation,
						location );
		}
		return false;
	}
/*
	public void addDNDAdapter( IDNDAdapter adapter, int priority )
	{
		this.adapterList.add( getIndex( priority ), adapter );
		this.adapterPriorityMap.put( adapter, "" + priority );
	}

	private int getIndex( int priority )
	{
		int index = 0;
		for ( Iterator iterator = this.adapterPriorityMap.entrySet( )
				.iterator( ); iterator.hasNext( ); )
		{
			Map.Entry entry = (Map.Entry) iterator.next( );
			if ( Integer.parseInt( entry.getValue( ).toString( ) ) > priority )
				index++;
		}
		return index;
	}

	public void removeDNDAdapter( IDNDAdapter adapter )
	{
		this.adapterList.remove( adapter );
	}

	private IDNDAdapter getAdapter( final Object object )
	{
		for ( Iterator iter = this.adapterList.iterator( ); iter.hasNext( ); )
		{
			IDNDAdapter adapter = (IDNDAdapter) iter.next( );
			if ( adapter.capable( object ) )
				return adapter;
		}
		return null;
	}
*/
	public void registryChanged( IRegistryChangeEvent event )
	{
		IExtensionDelta[] deltas = event.getExtensionDeltas( "org.eclipse.birt.report.designer.ui",
				"DNDServices" );
		for ( int i = 0; i < deltas.length; i++ )
		{
			if ( deltas[i].getKind( ) == IExtensionDelta.ADDED )
			{
				addRegistry( deltas[i].getExtensionPoint( ) );
			}
			else if ( deltas[i].getKind( ) == IExtensionDelta.REMOVED )
			{
				removeRegistry( deltas[i].getExtensionPoint( ) );
			}
		}
	}

	private void addRegistry( IExtensionPoint extensionPoint )
	{
		IConfigurationElement[] configElements = extensionPoint.getConfigurationElements( );
		for ( int i = 0; i < configElements.length; i++ )
		{
			if ( configElements[i].getName( ).equals( "dragAdapter" ) )
			{
//				int priority = getPriority( configElements[i].getAttribute( "priority" ) );
				try
				{
					IDragAdapter adapter = (IDragAdapter) configElements[i].createExecutableExtension( "adapter" );
					dragAdapterList.add( adapter );
					if ( Policy.TRACING_DND )
					{
						System.out.println( "[add dragAdapter]"
								+ configElements[i].getAttribute( "adapter" ) );
					}
				}
				catch ( CoreException e )
				{
					logger.log( Level.SEVERE, e.getMessage( ),e);
				}
			}
			else if ( configElements[i].getName( ).equals( "dropAdapter" ) )
			{
//				int priority = getPriority( configElements[i].getAttribute( "priority" ) );
				try
				{
					IDropAdapter adapter = (IDropAdapter) configElements[i].createExecutableExtension( "adapter" );
					dropAdapterList.add( adapter );
					if ( Policy.TRACING_DND )
					{
						System.out.println( "[add dropAdapter]"
								+ configElements[i].getAttribute( "adapter" ) );
					}
				}
				catch ( CoreException e )
				{
					logger.log( Level.SEVERE, e.getMessage( ),e);
				}
			}

		}
	}

	private void removeRegistry( IExtensionPoint extensionPoint )
	{
		// TODO Auto-generated method stub

	}

	private int getPriority( String attribute )
	{
		// TODO Auto-generated method stub
		return 0;
	}

	// private List getAdapters( final Object object )
	// {
	// // TODO cache adapters.
	// List adapters = new ArrayList( );
	// for ( Iterator iter = this.adapterList.iterator( ); iter.hasNext( ); )
	// {
	// IDNDAdapter adapter = (IDNDAdapter) iter.next( );
	// if ( adapter.capable( object ) > IDNDAdapter.CAPABLE_NONE )
	// adapters.add( adapter );
	// }
	// Collections.sort( adapters, new Comparator( ) {
	//
	// public int compare( Object o1, Object o2 )
	// {
	// if ( o1 instanceof IDNDAdapter && o2 instanceof IDNDAdapter )
	// {
	// return ( (IDNDAdapter) o1 ).capable( object )
	// - ( (IDNDAdapter) o2 ).capable( object );
	// }
	// return 0;
	// }
	// } );
	// return adapters;
	// }

}
