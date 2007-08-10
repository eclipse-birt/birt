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

package org.eclipse.birt.report.model.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.core.CachedMemberRef;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.ListGroup;
import org.eclipse.birt.report.model.elements.ListingElement;
import org.eclipse.birt.report.model.elements.TableGroup;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.elements.strategy.GroupPropSearchStrategy;
import org.eclipse.birt.report.model.elements.strategy.ReportItemPropSearchStrategy;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ModelMessages;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.StructPropertyDefn;
import org.eclipse.birt.report.model.util.ContentExceptionFactory;
import org.eclipse.birt.report.model.util.ModelUtil;

/**
 * This class adds, deletes and moves group elements. Group elements are treated
 * specially since data groups can be shared among report items.
 * 
 */

public class GroupElementCommand extends ContentCommand
{

	/**
	 * Constructs the content command with container element.
	 * 
	 * @param module
	 *            the module
	 * @param containerInfo
	 *            the container information
	 */

	GroupElementCommand( Module module, ContainerContext containerInfo )
	{
		super( module, containerInfo );
	}

	/**
	 * Constructs the content command with container element.
	 * 
	 * @param module
	 *            the module
	 * @param containerInfo
	 *            the container information
	 * @param flag
	 */

	GroupElementCommand( Module module, ContainerContext containerInfo,
			boolean flag )
	{
		super( module, containerInfo );
		this.flag = flag;
	}

	/**
	 * Sets name of group element.
	 * 
	 * @param content
	 *            group element.
	 * @param stack
	 *            activity stack.
	 * @param name
	 *            new group name.
	 */

	private void addDataGroups( ListingElement tmpContainer, int groupLevel,
			GroupElement content ) throws ContentException, NameException
	{
		List tmpElements = tmpContainer
				.findReferredListingElements( getModule( ) );

		for ( int i = 0; i < tmpElements.size( ); i++ )
		{
			ListingElement tmpElement = (ListingElement) tmpElements.get( i );
			assert tmpElement.getRoot( ) == getModule( );

			GroupElement tmpGroup = createNewGroupElement( content, module );
			GroupElementCommand cmd = new GroupElementCommand( module, focus
					.createContext( tmpElement ), true );
			cmd.add( tmpGroup );
		}
	}

	/**
	 * @param group
	 * @param module
	 * @return
	 */

	private static GroupElement createNewGroupElement( GroupElement group,
			Module module )
	{
		GroupElement retElement = null;

		if ( retElement instanceof ListGroup )
			retElement = new ListGroup( );
		else if ( group instanceof TableGroup )
			retElement = new TableGroup( );
		else
		{
			assert false;
			return null;
		}

		module.makeUniqueName( retElement );

		if ( retElement instanceof ListGroup )
			return retElement;

		return retElement;
	}

	/**
	 * Sets name of group element.
	 * 
	 * @param content
	 *            group element.
	 * @param stack
	 *            activity stack.
	 * @param name
	 *            new group name.
	 */

	private void deleteDataGroups( ListingElement tmpContainer, int groupIndex )
			throws SemanticException
	{
		List tmpElements = tmpContainer
				.findReferredListingElements( getModule( ) );

		for ( int i = 0; i < tmpElements.size( ); i++ )
		{
			ListingElement tmpElement = (ListingElement) tmpElements.get( i );
			assert tmpElement.getRoot( ) == getModule( );

			GroupElement tmpGroup = (GroupElement) tmpElement.getGroups( ).get(
					groupIndex );
			GroupElementCommand cmd = new GroupElementCommand( module, focus
					.createContext( tmpElement ), true );
			cmd.remove( tmpGroup, false, true );
		}
	}

	private void handleColumnBinding( DesignElement content )
	{
		List boundColumns = element.getListProperty( module,
				IReportItemModel.BOUND_DATA_COLUMNS_PROP );

		if ( boundColumns == null || boundColumns.isEmpty( ) )
			return;

		String groupName = (String) content.getProperty( module,
				IGroupElementModel.GROUP_NAME_PROP );
		List toCleared = new ArrayList( );
		for ( int i = 0; i < boundColumns.size( ); i++ )
		{
			ComputedColumn column = (ComputedColumn) boundColumns.get( i );
			String aggregateGroup = column.getAggregateOn( );
			if ( aggregateGroup != null && aggregateGroup.equals( groupName ) )
				toCleared.add( new Integer( i ) );
		}

		StructPropertyDefn structPropDefn = (StructPropertyDefn) MetaDataDictionary
				.getInstance( ).getStructure(
						ComputedColumn.COMPUTED_COLUMN_STRUCT ).getMember(
						ComputedColumn.AGGREGATEON_MEMBER );

		ElementPropertyDefn propDefn = element
				.getPropertyDefn( IReportItemModel.BOUND_DATA_COLUMNS_PROP );
		try
		{
			for ( int i = 0; i < toCleared.size( ); i++ )
			{
				int columnIndex = ( (Integer) toCleared.get( i ) ).intValue( );

				CachedMemberRef memberRef = new CachedMemberRef( propDefn,
						columnIndex, structPropDefn );

				PropertyCommand propCmd = new PropertyCommand( module, element );
				propCmd.setMember( memberRef, null );
			}
		}
		catch ( SemanticException e )
		{
			// should have no exception
		}
	}

	protected void checkBeforeAdd( DesignElement content )
			throws ContentException, NameException
	{
		super.checkBeforeAdd( content );

		if ( !flag && element instanceof ListingElement &&
				content instanceof GroupElement )
		{
			ListingElement tmpContainer = (ListingElement) element;
			if ( tmpContainer.isDataBindingReferring( module ) )
				throw ContentExceptionFactory
						.createContentException(
								focus,
								content,
								ContentException.DESIGN_EXCEPTION_STRUCTURE_CHANGE_FORBIDDEN );
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.command.ContentCommand#checkBeforeMovePosition(org.eclipse.birt.report.model.core.DesignElement,
	 *      int)
	 */
	protected void checkBeforeMovePosition( DesignElement content, int newPosn )
			throws ContentException
	{
		super.checkBeforeMovePosition( content, newPosn );

		if ( !flag && element instanceof ListingElement &&
				content instanceof GroupElement )
		{
			ListingElement tmpContainer = (ListingElement) element;
			if ( tmpContainer.isDataBindingReferring( module ) )
				throw ContentExceptionFactory
						.createContentException(
								focus,
								content,
								ContentException.DESIGN_EXCEPTION_STRUCTURE_CHANGE_FORBIDDEN );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.command.ContentCommand#checkBeforeRemove(org.eclipse.birt.report.model.core.DesignElement)
	 */

	protected void checkBeforeRemove( DesignElement content )
			throws SemanticException
	{
		super.checkBeforeRemove( content );

		if ( !flag && element instanceof ListingElement &&
				content instanceof GroupElement )
		{
			ListingElement tmpContainer = (ListingElement) element;
			if ( tmpContainer.isDataBindingReferring( module ) )
				throw ContentExceptionFactory
						.createContentException(
								focus,
								content,
								ContentException.DESIGN_EXCEPTION_STRUCTURE_CHANGE_FORBIDDEN );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.command.ContentCommand#doAdd(int,
	 *      org.eclipse.birt.report.model.core.DesignElement)
	 */

	protected void doAdd( int newPos, DesignElement content )
			throws ContentException, NameException
	{
		ActivityStack stack = getActivityStack( );

		stack.startTrans( ModelMessages
				.getMessage( MessageConstants.ADD_ELEMENT_MESSAGE ) );

		try
		{

			super.doAdd( newPos, content );

			// special cases for the group name. Group name must be unique in
			// the scope of its container table/list. Do not support undo/redo.

			String name = module.getNameHelper( ).getUniqueName( content );

			// if the flag is true, means current group is shared data group.
			// thus no need to create a unique name for it

			if ( !flag && name != null && !name.equals( content.getName( ) ) )
			{
				PropertyRecord propertyRecord = new PropertyRecord( content,
						IGroupElementModel.GROUP_NAME_PROP, name );
				stack.execute( propertyRecord );
			}

			addDataGroups( (ListingElement) element, ( (GroupElement) content )
					.getGroupLevel( ), (GroupElement) content );
		}
		catch ( NameException e )
		{
			stack.rollback( );
			throw e;
		}
		catch ( ContentException e )
		{
			stack.rollback( );
			throw e;
		}

		stack.commit( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.command.ContentCommand#doDelectAction(org.eclipse.birt.report.model.core.DesignElement)
	 */

	protected void doDelectAction( DesignElement content )
			throws SemanticException
	{
		int groupIndex = -1;
		if ( content instanceof GroupElement )
			groupIndex = ( (GroupElement) content ).getGroupLevel( ) - 1;

		super.doDelectAction( content );

		// special cases for column binding for the Group.

		deleteDataGroups( (ListingElement) element, groupIndex );

		handleColumnBinding( content );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.command.ContentCommand#doMovePosition(org.eclipse.birt.report.model.core.DesignElement,
	 *      int)
	 */

	protected void doMovePosition( DesignElement content, int newPosn )
			throws ContentException
	{

		int oldPosn = focus.indexOf( module, content );

		ActivityStack stack = getActivityStack( );
		stack.startTrans( ModelMessages
				.getMessage( MessageConstants.MOVE_CONTENT_MESSAGE ) );

		try
		{
			super.doMovePosition( content, newPosn );

			ListingElement tmpContainer = (ListingElement) element;
			List tmpElements = tmpContainer
					.findReferredListingElements( getModule( ) );

			for ( int i = 0; i < tmpElements.size( ); i++ )
			{
				ListingElement tmpElement = (ListingElement) tmpElements
						.get( i );
				assert tmpElement.getRoot( ) == getModule( );

				GroupElement tmpContent = (GroupElement) tmpElement.getGroups( )
						.get( oldPosn );

				GroupElementCommand tmpCmd = new GroupElementCommand( module,
						focus.createContext( tmpElement ), true );
				tmpCmd.movePosition( tmpContent, newPosn );
			}
		}
		catch ( ContentException e )
		{
			stack.rollback( );
			throw e;
		}

		stack.commit( );
	}

	/**
	 * Localizes element properties or removes current group elements and adds
	 * new group elements when the data binding reference is set between two
	 * listing elements.
	 * <p>
	 * When calls this method, the data binding reference has been set.
	 * 
	 * @param oldValue
	 * @param newValue
	 * 
	 * @throws SemanticException
	 */

	protected void setupSharedDataGroups( Object oldValue, Object newValue )
			throws SemanticException
	{
		if ( newValue == null || !( (ElementRefValue) newValue ).isResolved( ) )
		{
			if ( oldValue != null &&
					( (ElementRefValue) oldValue ).isResolved( ) )
				localizeProperties( ( (ElementRefValue) oldValue ).getElement( ) );
		}

		if ( newValue != null && ( (ElementRefValue) newValue ).isResolved( ) )
		{
			setupSharedDataGroups( ( (ElementRefValue) newValue ).getElement( ) );
		}

	}

	/**
	 * Removes current group elements and adds new group elements when the data
	 * binding reference is set between two listing elements.
	 * <p>
	 * 
	 * @param targetElement
	 * @throws SemanticException
	 */

	private void setupSharedDataGroups( DesignElement targetElement )
			throws SemanticException
	{
		if ( targetElement.getDefn( ) != element.getDefn( ) )
			return;

		List groupsToRemove = ( (ListingElement) element ).getGroups( );
		for ( int i = 0; i < groupsToRemove.size( ); i++ )
		{
			GroupElementCommand tmpCmd = new GroupElementCommand( module,
					new ContainerContext( element, ListingElement.GROUP_SLOT ),
					true );
			tmpCmd.remove( (GroupElement) groupsToRemove.get( i ), false, true );
		}

		List groupsToAdd = new ArrayList( );
		List targetGroups = ( (ListingElement) targetElement ).getGroups( );
		for ( int i = 0; i < targetGroups.size( ); i++ )
		{
			groupsToAdd.add( createNewGroupElement( (GroupElement) targetGroups
					.get( i ), module ) );
		}

		for ( int i = 0; i < groupsToAdd.size( ); i++ )
		{
			GroupElementCommand tmpCmd = new GroupElementCommand( module,
					new ContainerContext( element, ListingElement.GROUP_SLOT ),
					true );
			tmpCmd.add( (GroupElement) groupsToAdd.get( i ) );
		}
	}

	/**
	 * Localizes element properties including listing elements and its group
	 * properties.
	 * 
	 * @param targetElement
	 * @throws SemanticException
	 */

	private void localizeProperties( DesignElement targetElement )
			throws SemanticException
	{
		ListingElement listing = (ListingElement) element;

		recoverReferredReportItem( listing, targetElement );
		List listingGroups = listing.getGroups( );

		ListingElement targetListing = (ListingElement) targetElement;
		List targetGroups = targetListing.getGroups( );

		int size = Math.min( listingGroups.size( ), targetGroups.size( ) );
		for ( int i = 0; i < size; i++ )
		{
			recoverReferredReportItem( (GroupElement) listingGroups.get( i ),
					(GroupElement) targetGroups.get( i ) );
		}
	}

	/**
	 * Localizes element properties from <code>targetElement</code> to
	 * <code>source</code>.
	 * 
	 */

	private void recoverReferredReportItem( DesignElement source,
			DesignElement targetElement ) throws SemanticException
	{
		Iterator propNames = null;

		if ( targetElement instanceof ListingElement )
		{
			propNames = ReportItemPropSearchStrategy.getDataBindingPropties( )
					.iterator( );
		}
		else if ( targetElement instanceof GroupElement )
		{
			propNames = GroupPropSearchStrategy.getDataBindingPropties( )
					.iterator( );
		}
		else 
		{
			assert false; 
			return;
		}

		while ( propNames.hasNext( ) )
		{
			String propName = (String) propNames.next( );
			ElementPropertyDefn propDefn = (ElementPropertyDefn) targetElement
					.getDefn( ).getProperty( propName );
			Object value = targetElement.getStrategy( )
					.getPropertyExceptRomDefault( module, targetElement,
							propDefn );
			value = ModelUtil.copyValue( propDefn, value );

			// Set the list value on the element itself.

			PropertyRecord propRecord = new PropertyRecord( source, propDefn,
					value );
			getActivityStack( ).execute( propRecord );
		}
	}
}