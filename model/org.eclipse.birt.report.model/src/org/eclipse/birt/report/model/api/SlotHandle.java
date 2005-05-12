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

package org.eclipse.birt.report.model.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.core.IDesignElement;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.command.ContentCommand;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ReportDesign;

/**
 * Represents a "slot" within an element. A slot holds a collection of report
 * items.
 * 
 */

public class SlotHandle extends ElementDetailHandle
{

	/**
	 * The numeric identifier of the slot.
	 */

	protected int slotID;

	/**
	 * Constructs a handle for the slot with the given design element handle and
	 * the id of the slot. The application generally does not create a slot
	 * handle directly. Instead, it calls the <code>getSlot( )</code> method
	 * on an element handle, or one of the specific methods on the handle for an
	 * element that is a container.
	 * 
	 * @param element
	 *            handle to the report element
	 * @param slotID
	 *            the numeric identifier of the slot
	 */

	public SlotHandle( DesignElementHandle element, int slotID )
	{
		super( element );
		this.slotID = slotID;
	}

	/**
	 * Adds a report item to the slot with the given element handle. The report
	 * item must not be newly created and not yet added to the design.
	 * 
	 * @param content
	 *            handle to the newly created element
	 * @throws ContentException
	 *             if the element is not allowed in the slot
	 * @throws NameException
	 *             if the element has a duplicate or illegal name
	 */

	public void add( DesignElementHandle content ) throws ContentException,
			NameException
	{
		add( content.getElement( ) );
	}

	/**
	 * Adds a report item to the slot at the given position. The item must not
	 * be newly created and not yet added to the design.
	 * 
	 * @param content
	 *            handle to the newly created element
	 * @param newPos
	 *            the position index at which the content to be inserted
	 * @throws ContentException
	 *             if the element is not allowed in the slot
	 * @throws NameException
	 *             if the element has a duplicate or illegal name
	 */

	public void add( DesignElementHandle content, int newPos )
			throws ContentException, NameException
	{
		add( content.getElement( ), newPos );
	}

	/**
	 * Adds a report item to the slot. The item must not be newly created and
	 * not yet added to the design.
	 * 
	 * @param content
	 *            the newly created element
	 * @throws ContentException
	 *             if the element is not allowed in the slot
	 * @throws NameException
	 *             if the element has a duplicate or illegal name
	 * 
	 * @deprecated by the method {@link #add(DesignElementHandle)}
	 */

	public void add( DesignElement content ) throws ContentException,
			NameException
	{
		ContentCommand cmd = new ContentCommand( getDesign( ), getElement( ) );
		cmd.add( content, slotID );
	}

	/**
	 * Adds a report item to the slot. The item must not be newly created and
	 * not yet added to the design.
	 * 
	 * @param content
	 *            the newly created element
	 * @param newPos
	 *            the position index at which the content to be inserted.
	 * @throws ContentException
	 *             if the element is not allowed in the slot
	 * @throws NameException
	 *             if the element has a duplicate or illegal name
	 * 
	 * @deprecated by the method {@link #add(DesignElementHandle, int)}
	 */

	public void add( DesignElement content, int newPos )
			throws ContentException, NameException
	{
		ContentCommand cmd = new ContentCommand( getDesign( ), getElement( ) );
		cmd.add( content, slotID, newPos );
	}

	/**
	 * Pastes a report item to the slot. The item must be newly created and not
	 * yet added to the design.
	 * 
	 * @param content
	 *            the newly created element handle
	 * @return a list containing all errors for the pasted element
	 * @throws ContentException
	 *             if the element is not allowed in the slot
	 * @throws NameException
	 *             if the element has a duplicate or illegal name
	 */

	public List paste( DesignElementHandle content ) throws ContentException,
			NameException
	{
		add( content );

		return checkPostPasteErrors( content.getElement( ) );
	}

	/**
	 * Pastes a report item to the slot. The item must be newly created and not
	 * yet added to the design.
	 * 
	 * @param content
	 *            the newly created element
	 * @return a list containing all errors for the pasted element
	 * @throws ContentException
	 *             if the element is not allowed in the slot
	 * @throws NameException
	 *             if the element has a duplicate or illegal name
	 */
	public List paste( IDesignElement content ) throws ContentException,
			NameException
	{
		add( content.getHandle( getDesign( ) ) );

		return checkPostPasteErrors( (DesignElement) content );
	}

	/**
	 * Pastes a report item to the slot. The item must be newly created and not
	 * yet added to the design.
	 * 
	 * @param content
	 *            the newly created element handle
	 * @param newPos
	 *            the position index at which the content to be inserted.
	 * @return a list containing all errors for the pasted element
	 * @throws ContentException
	 *             if the element is not allowed in the slot
	 * @throws NameException
	 *             if the element has a duplicate or illegal name
	 */

	public List paste( DesignElementHandle content, int newPos )
			throws ContentException, NameException
	{
		add( content, newPos );

		return checkPostPasteErrors( content.getElement( ) );
	}

	/**
	 * Pastes a report item to the slot. The item must be newly created and not
	 * yet added to the design.
	 * 
	 * @param content
	 *            the newly created element
	 * @param newPos
	 *            the position index at which the content to be inserted.
	 * @return a list containing all errors for the pasted element
	 * @throws ContentException
	 *             if the element is not allowed in the slot
	 * @throws NameException
	 *             if the element has a duplicate or illegal name
	 */

	public List paste( IDesignElement content, int newPos )
			throws ContentException, NameException
	{
		add( content.getHandle( getDesign( ) ), newPos );

		return checkPostPasteErrors( (DesignElement) content );

	}

	/**
	 * Reset the element Id for the pasted element and its sub elements.
	 * 
	 * @param element
	 *            the element new added in the design.
	 */

	private void resetId( DesignElement element )
	{
		if ( element == null )
			return;

		ReportDesign design = this.getDesign( );
		IElementDefn defn = element.getDefn( );
		int id = design.getNextID( );
		element.setID( id );
		design.addElementID( element );

		for ( int i = 0; i < defn.getSlotCount( ); i++ )
		{
			ContainerSlot slot = element.getSlot( i );

			if ( slot == null )
				continue;

			for ( int pos = 0; pos < slot.getCount( ); pos++ )
			{
				DesignElement innerElement = slot.getContent( pos );
				resetId( innerElement );
			}
		}
	}

	/**
	 * Checks the element after the paste action.
	 * 
	 * @param content
	 *            the pasted element
	 * 
	 * @return a list containing parsing errors. Each element in the list is
	 *         <code>ErrorDetail</code>.
	 */

	private List checkPostPasteErrors( DesignElement content )
	{
		List exceptionList = content.validateWithContents( getDesign( ) );
		List errorDetailList = ErrorDetail.convertExceptionList( exceptionList );

		return errorDetailList;
	}

	/**
	 * Gets an iterator over the items in the slot.
	 * 
	 * @return an iterator over the items in the slot. The iterator is of type
	 *         {@link SlotIterator}and each item returned by the iterator's
	 *         <code>getNext( )</code> method is of type
	 *         {@link DesignElementHandle}.
	 */

	public Iterator iterator( )
	{
		return new SlotIterator( this );
	}

	/**
	 * Returns the a list with slot contents.Items are handles to the contents
	 * and in order by position.
	 * 
	 * @return a list with slot contents, items of the list are handles to the
	 *         contents.
	 */

	public List getContents( )
	{
		List contents = getElement( ).getSlot( slotID ).getContents( );

		if ( contents == null )
			return Collections.EMPTY_LIST;

		ArrayList retList = new ArrayList( );
		for ( Iterator iter = contents.iterator( ); iter.hasNext( ); )
		{
			retList.add( ( (DesignElement) iter.next( ) )
					.getHandle( getDesign( ) ) );
		}
		return retList;
	}

	/**
	 * Returns the number of elements in the slot.
	 * 
	 * @return the count of contents in the slot
	 */

	public int getCount( )
	{
		return getElement( ).getSlot( slotID ).getCount( );
	}

	/**
	 * Returns a handle to the content element at the given position.
	 * 
	 * @param posn
	 *            the position within the slot
	 * @return a handle to the content element
	 */

	public DesignElementHandle get( int posn )
	{
		DesignElement content = getElement( ).getSlot( slotID ).getContent(
				posn );
		if ( content == null )
			return null;
		return content.getHandle( getDesign( ) );
	}

	/**
	 * Moves the position of a content element within the slot.
	 * 
	 * @param content
	 *            handle to the content to move
	 * @param toPosn
	 *            the new position
	 * @throws ContentException
	 *             if the content is not in the slot, or if the to position is
	 *             not valid.
	 */

	public void shift( DesignElementHandle content, int toPosn )
			throws ContentException
	{
		ContentCommand cmd = new ContentCommand( getDesign( ), getElement( ) );
		cmd.movePosition( content.getElement( ), slotID, toPosn );
	}

	/**
	 * Moves a content element into a slot in another container element.
	 * 
	 * @param content
	 *            a handle to the element to move
	 * @param newContainer
	 *            a handle to the new container element
	 * @param toSlot
	 *            the target slot ID where the element will be moved to.
	 * @throws ContentException
	 *             if the content is not in this slot or if the new container is
	 *             not, in fact, a container, or if the content cannot go into
	 *             the target slot.
	 */

	public void move( DesignElementHandle content,
			DesignElementHandle newContainer, int toSlot )
			throws ContentException
	{
		ContentCommand cmd = new ContentCommand( getDesign( ), getElement( ) );
		cmd.move( content.getElement( ), slotID, newContainer.getElement( ),
				toSlot );
	}

	/**
	 * Moves a content element into a slot in another container element at the
	 * specified position.
	 * 
	 * @param content
	 *            a handle to the element to move
	 * @param newContainer
	 *            a handle to the new container element
	 * @param toSlot
	 *            the target slot ID where the element will be moved to.
	 * @param newPos
	 *            the position to which the content will be moved. If it is
	 *            greater than the current size of the target slot, the content
	 *            will be appended at the end of the target slot.
	 * @throws ContentException
	 *             if the content is not in this slot or if the new container is
	 *             not, in fact, a container, or if the content cannot go into
	 *             the target slot.
	 */

	public void move( DesignElementHandle content,
			DesignElementHandle newContainer, int toSlot, int newPos )
			throws ContentException
	{
		ContentCommand cmd = new ContentCommand( getDesign( ), getElement( ) );
		cmd.move( content.getElement( ), slotID, newContainer.getElement( ),
				toSlot, newPos );
	}

	/**
	 * Drops a content element from the slot.
	 * 
	 * @param content
	 *            a handle to the content to drop
	 * @throws SemanticException
	 *             if the content is not within the slot
	 */

	public void drop( DesignElementHandle content ) throws SemanticException
	{
		ContentCommand cmd = new ContentCommand( getDesign( ), getElement( ) );
		cmd.remove( content.getElement( ), slotID );
	}

	/**
	 * Drops a content element at the given position from the slot.
	 * 
	 * @param posn
	 *            the position of the content to drop
	 * @throws SemanticException
	 *             if the position is out of range
	 */

	public void drop( int posn ) throws SemanticException
	{
		DesignElement content = getElement( ).getSlot( slotID ).getContent(
				posn );
		ContentCommand cmd = new ContentCommand( getDesign( ), getElement( ) );
		cmd.remove( content, slotID );
	}

	/**
	 * Returns the internal representation of the slot. Use this object only for
	 * reading: make all changes through this handle.
	 * 
	 * @return the internal representation of the slot
	 */

	public ContainerSlot getSlot( )
	{
		return getElement( ).getSlot( slotID );
	}

	/**
	 * Returns the position of the given content in this slot.
	 * 
	 * @param content
	 *            the content to look up
	 * @return Zero-based index of the element. Returns -1 if the content is not
	 *         found
	 */

	public int findPosn( DesignElementHandle content )
	{
		return getSlot( ).findPosn( content.getElement( ) );
	}

	/**
	 * Returns the numeric identifier of the slot.
	 * 
	 * @return The numeric identifier of the slot.
	 */

	public int getSlotID( )
	{
		return slotID;
	}

}