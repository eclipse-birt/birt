/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation .
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.core.model.schematic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.birt.report.designer.core.model.IModelAdapterHelper;
import org.eclipse.birt.report.designer.core.model.ReportItemtHandleAdapter;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.MetricUtility;
import org.eclipse.birt.report.model.activity.SemanticException;
import org.eclipse.birt.report.model.api.ListGroupHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.command.ContentException;
import org.eclipse.birt.report.model.command.NameException;
import org.eclipse.birt.report.model.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.ListGroup;
import org.eclipse.birt.report.model.elements.ListItem;
import org.eclipse.birt.report.model.elements.ListingElement;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.metadata.DimensionValue;
import org.eclipse.birt.report.model.util.DimensionUtil;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.util.Assert;

/**
 * List element handle adapter
 *  
 */
public class ListHandleAdapter extends ReportItemtHandleAdapter
{

	private static final String TRANS_LABEL_INSERT_GROUP = Messages.getString( "ListHandleAdapt.transLabel.insertGroup" ); //$NON-NLS-1$

	List children = new ArrayList( );

	public static final int HEADER = ListItem.HEADER_SLOT;

	public static final int DETAIL = ListItem.DETAIL_SLOT;

	public static final int FOOTER = ListItem.FOOTER_SLOT;

	/**
	 * @param handle
	 * @param mark
	 */
	public ListHandleAdapter( ListHandle handle, IModelAdapterHelper mark )
	{
		super( handle, mark );
	}

	/**
	 * Gets the Children iterator. This children relationship is determined by
	 * GUI requirement. This is not the model children relationship.
	 * 
	 * @return Children iterator
	 */
	public List getChildren( )
	{
		ListHandle handle = getListHandle( );
		SlotHandle slotHandle = handle.getSlot( ListingElement.HEADER_SLOT );

		addChild( new ListBandProxy( slotHandle, ListBandProxy.LIST_HEADER ) );

		SlotHandle group = handle.getGroups( );

		int number = 0;
		for ( Iterator it = group.iterator( ); it.hasNext( ); )
		{
			number++;
			ListGroupHandle listGroup = (ListGroupHandle) it.next( );
			SlotHandle groupHeaders = listGroup.getSlot( GroupElement.HEADER_SLOT );
			addChild( new ListBandProxy( groupHeaders,
					ListBandProxy.LIST_GROUP_HEADER + number ) );
		}

		slotHandle = handle.getSlot( ListingElement.DETAIL_SLOT );

		addChild( new ListBandProxy( slotHandle, ListBandProxy.LIST_DETAIL ) );

		//number = 0;
		for ( ListIterator it = convertIteratorToListIterator( group.iterator( ) ); it.hasPrevious( ); )
		{

			ListGroupHandle listGroup = (ListGroupHandle) it.previous( );
			SlotHandle groupFooters = listGroup.getSlot( GroupElement.FOOTER_SLOT );
			addChild( new ListBandProxy( groupFooters,
					ListBandProxy.LIST_GROUP_FOOTER + number ) );
			number--;
		}

		slotHandle = handle.getSlot( ListingElement.FOOTER_SLOT );

		addChild( new ListBandProxy( slotHandle, ListBandProxy.LIST_FOOTER ) );
		Comparator com = new Comparator( ) {

			public int compare( Object o1, Object o2 )
			{
				if ( o1 instanceof ListBandProxy && o2 instanceof ListBandProxy )
				{
					ListBandProxy band1 = (ListBandProxy) o1;
					ListBandProxy band2 = (ListBandProxy) o2;
					if ( band1.getType( ) != band2.getType( ) )
					{
						return band1.getType( ) - band2.getType( );
					}
					else if ( band1.getType( ) == ListBandProxy.LIST_GROUP_FOOTER_TYPE )
					{
						return band2.getDisplayName( )
								.compareToIgnoreCase( band1.getDisplayName( ) );
					}
					else
					{
						return band1.getDisplayName( )
								.compareToIgnoreCase( band2.getDisplayName( ) );
					}
				}
				return 0;
			}

		};
		Collections.sort( children, com );
		return children;
	}

	/**
	 * Get padding.
	 * @param retValue
	 * @return padding value
	 */
	public Insets getPadding( Insets retValue )
	{
		if ( retValue == null )
		{
			retValue = new Insets( );
		}
		else
		{
			retValue = new Insets( retValue );
		}
		//DimensionValue value =
		// (DimensionValue)getTableHandle().getProperty(Style.PADDING_TOP_PROP);
		DimensionValue handle = (DimensionValue) getReportItemHandle( ).getProperty( Style.PADDING_TOP_PROP );
		double px = DimensionUtil.convertTo( handle.getMeasure( ),
				handle.getUnits( ),
				DesignChoiceConstants.UNITS_IN ).getMeasure( );

		handle = (DimensionValue) getReportItemHandle( ).getProperty( Style.PADDING_BOTTOM_PROP );
		double py = DimensionUtil.convertTo( handle.getMeasure( ),
				handle.getUnits( ),
				DesignChoiceConstants.UNITS_IN ).getMeasure( );
		Point p = MetricUtility.inchToPixel( px, py );
		retValue.top = p.x;
		retValue.bottom = p.y;

		handle = (DimensionValue) getReportItemHandle( ).getProperty( Style.PADDING_LEFT_PROP );
		px = DimensionUtil.convertTo( handle.getMeasure( ),
				handle.getUnits( ),
				DesignChoiceConstants.UNITS_IN ).getMeasure( );

		handle = (DimensionValue) getReportItemHandle( ).getProperty( Style.PADDING_RIGHT_PROP );
		py = DimensionUtil.convertTo( handle.getMeasure( ),
				handle.getUnits( ),
				DesignChoiceConstants.UNITS_IN ).getMeasure( );

		p = MetricUtility.inchToPixel( px, py );
		retValue.left = p.x;
		retValue.right = p.y;
		return retValue;
	}

	/**
	 * @param iterator
	 */
	private ListIterator convertIteratorToListIterator( Iterator iterator )
	{
		ArrayList list = new ArrayList( );
		for ( Iterator it = iterator; it.hasNext( ); )
		{
			list.add( it.next( ) );
		}
		return list.listIterator( list.size( ) );
	}

	/**
	 * @return
	 */
	private ListHandle getListHandle( )
	{
		return (ListHandle) getHandle( );
	}

	private void addChild( Object obj )
	{
		if ( !children.contains( obj ) )
		{
			children.add( obj );
		}
		else if ( obj instanceof ListBandProxy )
		{
			int index = children.indexOf( obj );
			ListBandProxy proxy = (ListBandProxy) children.get( index );
			proxy.setDisplayName( ( (ListBandProxy) obj ).getDisplayName( ) );
		}
	}

	/**
	 * Inserts a group into list.
	 * @throws ContentException
	 * @throws NameException
	 */
	public ListGroupHandle insertGroup( ) throws ContentException, NameException
	{

		transStar( TRANS_LABEL_INSERT_GROUP );

		ListGroupHandle groupHandle = getListHandle( ).getElementFactory( )
				.newListGroup( );
		SlotHandle handle = getListHandle( ).getGroups( );
		handle.add( groupHandle );

		transEnd( );
		return groupHandle;
	}

	/**
	 * Provides remove band function.
	 * @param model
	 */
	public void remove( Object model )
	{
		if ( model instanceof ListGroupHandle )
		{
			ListGroupHandle group = (ListGroupHandle) model;
			children.remove( new ListBandProxy( group.getSlot( ListGroup.HEADER_SLOT ) ) );
			children.remove( new ListBandProxy( group.getSlot( ListGroup.FOOTER_SLOT ) ) );
		}
		children.remove( model );
	}

	/**
	 * Provides remove group function.
	 * @param group
	 * @throws SemanticException
	 */
	public void removeGroup( Object group ) throws SemanticException
	{
		Assert.isLegal(group instanceof ListBandProxy );
		( (ListBandProxy) group ).getElemtHandle( ).drop( );
	}

	/**
	 * Gets the Children with index
	 * 
	 * @return children on given position.
	 */	
	public Object getChild( int id )
	{
		int index = children.indexOf( new ListBandProxy( getListHandle( ).getSlot( id ) ) );
		return children.get( index );
	}
}