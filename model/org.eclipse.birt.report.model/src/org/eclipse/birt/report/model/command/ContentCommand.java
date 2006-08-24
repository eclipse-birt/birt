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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.activity.AbstractElementCommand;
import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.ExtendsException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.command.TemplateException;
import org.eclipse.birt.report.model.api.command.UserPropertyException;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.api.elements.structures.PropertyBinding;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.BackRef;
import org.eclipse.birt.report.model.core.CachedMemberRef;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.ReferenceableElement;
import org.eclipse.birt.report.model.core.StyledElement;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.TemplateElement;
import org.eclipse.birt.report.model.elements.TemplateParameterDefinition;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ModelMessages;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.metadata.SlotDefn;
import org.eclipse.birt.report.model.metadata.StructRefValue;

/**
 * This class adds, deletes and moves content elements. Adding a content element
 * to a container is the only way to add a new element to the design. Similarly,
 * removing an element from its container is the only way to delete an element
 * from the design.
 * <p>
 * Note: be sure to use the move operation if your intent is to move an element
 * from one place to another. Do not use a drop followed by an add. The move
 * command verifies that the move can be done before starting the action. If you
 * instead do a drop followed by an add, you'll end up with the element deleted
 * if it cannot be added into its new location.
 * 
 */

public class ContentCommand extends AbstractElementCommand
{

	/**
	 * Constructs the content command with container element.
	 * 
	 * @param module
	 *            the module
	 * @param container
	 *            the container element
	 */

	public ContentCommand( Module module, DesignElement container )
	{
		super( module, container );
	}

	/**
	 * Adds a new element into a container and specifies the position in the
	 * container. Virtually all elements must reside in a container. Containers
	 * are identified by a container ID. The application creates the element
	 * object, then adds it to the container here. The undo of this operation
	 * effectively deletes the element.
	 * 
	 * @param content
	 *            the element to add
	 * @param slotID
	 *            the slot in which to add the component
	 * @param newPos
	 *            the position index at which the content to be inserted. If
	 *            it's -1, the content will be inserted at the end of the slot.
	 * @throws ContentException
	 *             if the content cannot be added into this container.
	 * @throws NameException
	 *             if the name of the content exists in name space.
	 */

	public void add( DesignElement content, int slotID, int newPos )
			throws ContentException, NameException
	{
		assert newPos >= 0 || newPos == -1;
		doAdd( content, slotID, newPos );
	}

	/**
	 * Adds a new element into a container. Virtually all elements must reside
	 * in a container. Containers are identified by a container ID. The
	 * application creates the element object, then adds it to the container
	 * here. The undo of this operation effectively deletes the element.
	 * 
	 * @param content
	 *            the element to add
	 * @param slotID
	 *            the slot in which to add the component
	 * @throws ContentException
	 *             if the content cannot be added into this container.
	 * @throws NameException
	 *             if the name of the content exists in name space.
	 */

	public void add( DesignElement content, int slotID )
			throws ContentException, NameException
	{
		doAdd( content, slotID, -1 );
	}

	/**
	 * Adds a new element into a container. Virtually all elements must reside
	 * in a container. (The term "container" here is generic, it is not the same
	 * as the Container element defined in the XML schema.) Containers are
	 * identified by a container ID. The application creates the element object,
	 * then adds it to the container here. The undo of this operation
	 * effectively deletes the element.
	 * 
	 * @param content
	 *            the element to add
	 * @param slotID
	 *            the slot in which to add the component
	 * @param newPos
	 *            the position index at which the content to be inserted. If
	 *            it's -1, the content will be inserted at the end of the slot.
	 * @throws ContentException
	 *             if the content cannot be added into this container.
	 * @throws NameException
	 *             if the name of the content exists in name space.
	 */

	private void doAdd( DesignElement content, int slotID, int newPos )
			throws ContentException, NameException
	{
		assert content.getContainer( ) == null;

		// Ensure that the content can be put into the container.

		ElementDefn metaData = (ElementDefn) element.getDefn( );
		if ( !metaData.isContainer( ) )
			throw new ContentException( element, slotID,
					ContentException.DESIGN_EXCEPTION_NOT_CONTAINER );
		SlotDefn slotInfo = (SlotDefn) metaData.getSlot( slotID );
		if ( slotInfo == null )
			throw new ContentException( element, slotID,
					ContentException.DESIGN_EXCEPTION_SLOT_NOT_FOUND );
		if ( !slotInfo.canContain( content ) )
			throw new ContentException( element, slotID, content,
					ContentException.DESIGN_EXCEPTION_WRONG_TYPE );

		// Can not change the structure of child element or a virtual element(
		// inside the child ).

		if ( element.isVirtualElement( ) || element.getExtendsName( ) != null )
			throw new ContentException(
					element,
					slotID,
					content,
					ContentException.DESIGN_EXCEPTION_STRUCTURE_CHANGE_FORBIDDEN );

		// This element is already the content of the element to add.

		if ( element.isContentOf( content ) )
			throw new ContentException( element, slotID, content,
					ContentException.DESIGN_EXCEPTION_RECURSIVE );

		// If this is a single-item slot, ensure that the slot is empty.

		if ( !slotInfo.isMultipleCardinality( )
				&& element.getSlot( slotID ).getCount( ) > 0 )
		{
			throw new ContentException( element, slotID,
					ContentException.DESIGN_EXCEPTION_SLOT_IS_FULL );
		}

		if ( slotID == Module.COMPONENT_SLOT )
		{
			if ( StringUtil.isBlank( content.getName( ) ) )
				throw new ContentException( element, slotID, content,
						ContentException.DESIGN_EXCEPTION_CONTENT_NAME_REQUIRED );
		}

		checkContainmentContext( slotID, content );

		// Add the item to the container.

		ContentRecord addRecord;
		if ( newPos == -1 )
		{
			addRecord = new ContentRecord( module, element, slotID, content,
					true );
		}
		else
		{
			addRecord = new ContentRecord( module, element, slotID, content,
					newPos );
		}

		ActivityStack stack = getActivityStack( );
		stack.startTrans( addRecord.getLabel( ) );

		try
		{
			// add the template parameter definition first

			TemplateCommand cmd = new TemplateCommand( module, element );
			cmd.checkAdd( content, slotID );

			// add the element

			stack.execute( addRecord );

			// check the name of the content and all its children and add names
			// of them to the namespace

			addElementNames( content );

			// speical cases for the group name. Group name must be unique in
			// the scope of its container table/list. Do not support undo/redo.

			if ( content instanceof GroupElement )
			{
				String name = module.getUniqueName( content );
				setGroupName( content, stack, name );
			}
		}
		catch ( NameException e )
		{
			stack.rollback( );
			throw e;
		}

		stack.commit( );
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

	private void setGroupName( DesignElement content, ActivityStack stack,
			String name )
	{
		if ( name != null && !name.equals( content.getName( ) ) )
		{
			PropertyRecord propertyRecord = new PropertyRecord( content,
					IGroupElementModel.GROUP_NAME_PROP, name );
			stack.execute( propertyRecord );
		}
	}

	/**
	 * Determines if the slot can contain a given element with considering the
	 * context.
	 * 
	 * @param slotId
	 *            the slot id
	 * @param element
	 *            the element to insert
	 * @throws NameException
	 *             if name duplicate occurs
	 * @throws ContentException
	 *             the slot cannot contain the given element.
	 */

	private void checkContainmentContext( int slotId, DesignElement content )
			throws NameException, ContentException
	{
		List errors = element.checkContainmentContext( module, slotId, content );
		if ( !errors.isEmpty( ) )
		{
			SemanticException e = (SemanticException) errors.get( 0 );
			assert e instanceof NameException || e instanceof ContentException;

			if ( e instanceof NameException )
				throw (NameException) e;

			if ( e instanceof ContentException )
				throw (ContentException) e;
		}
	}

	/**
	 * Adds the element name and names of nested element in it to name spaces.
	 * 
	 * @param content
	 *            the content to add
	 * @throws NameException
	 *             if any element has duplicate name with elements already on
	 *             the design tree.
	 */

	private void addElementNames( DesignElement content ) throws NameException
	{
		// before handle the names for the content and its children, the content
		// is added into the container first

		assert content.getContainer( ) != null;

		// if the content is managed by namespace, then check the name and add
		// it to the namespace, otherwise do nothing

		NameCommand nameCmd = new NameCommand( module, content );
		nameCmd.addElement( );

		// recusively check the contents and add them

		for ( int i = 0; i < content.getDefn( ).getSlotCount( ); i++ )
		{
			ContainerSlot slot = content.getSlot( i );
			for ( int j = 0; j < slot.getCount( ); j++ )
			{
				DesignElement tmpElement = slot.getContent( j );
				addElementNames( tmpElement );
			}
		}
	}

	/**
	 * Removes an item from its container. This is equivalent to deleting the
	 * element from the design. Because the element is being deleted, we must
	 * clean up all references to or from the element. References include:
	 * <p>
	 * <ul>
	 * <li>The elements that this content extends.
	 * <li>The elements that extend this content.
	 * <li>The style that this content uses.
	 * <li>The elements that use this style.
	 * <li>The elements that this content contains.
	 * <li>The name space that contains this content.
	 * </ul>
	 * 
	 * @param content
	 *            the element to remove
	 * @param slotID
	 *            the slot from which to remove the content
	 * @throws SemanticException
	 *             if this content cannot be removed from container.
	 */

	public void remove( DesignElement content, int slotID )
			throws SemanticException
	{
		remove( content, slotID, false );
	}

	/**
	 * Removes an item from its container. This is equivalent to deleting the
	 * element from the design. Because the element is being deleted, we must
	 * clean up all references to or from the element. References include:
	 * <p>
	 * <ul>
	 * <li>The elements that this content extends.
	 * <li>The elements that extend this content.
	 * <li>The style that this content uses.
	 * <li>The elements that use this style.
	 * <li>The elements that this content contains.
	 * <li>The name space that contains this content.
	 * </ul>
	 * 
	 * @param content
	 *            the element to remove
	 * @param slotID
	 *            the slot from which to remove the content
	 * @param unresolveReference
	 *            status whether to un-resolve the references
	 * @throws SemanticException
	 *             if this content cannot be removed from container.
	 */

	public void remove( DesignElement content, int slotID,
			boolean unresolveReference ) throws SemanticException
	{
		doRemove( content, slotID, unresolveReference, false );
	}

	/**
	 * Removes an item from its container. This is equivalent to deleting the
	 * element from the design. Because the element is being deleted, we must
	 * clean up all references to or from the element. References include:
	 * <p>
	 * <ul>
	 * <li>The elements that this content extends.
	 * <li>The elements that extend this content.
	 * <li>The style that this content uses.
	 * <li>The elements that use this style.
	 * <li>The elements that this content contains.
	 * <li>The name space that contains this content.
	 * </ul>
	 * 
	 * @param content
	 *            the element to remove
	 * @param slotID
	 *            the slot from which to remove the content
	 * @param unresolveReference
	 *            status whether to un-resolve the references
	 * @param flag
	 *            <code>true</code> to avoid the exception when the
	 *            <code>content</code> is a virtual element. Otherwise,
	 *            <code>false</code>.
	 * @throws SemanticException
	 *             if this content cannot be removed from container.
	 */

	public void remove( DesignElement content, int slotID,
			boolean unresolveReference, boolean flag ) throws SemanticException
	{
		doRemove( content, slotID, unresolveReference, flag );
	}

	/**
	 * @see #remove(DesignElement, int, boolean)
	 * @param content
	 *            the element to remove
	 * @param slotID
	 *            the slot from which to remove the content
	 * @param unresolveReference
	 *            status whether to un-resolve the references
	 * @param flag
	 *            Use the flag to indicate whether this method is do with the
	 *            element itself or do with its contents.
	 * @throws SemanticException
	 *             if this content cannot be removed from container.
	 */

	private void doRemove( DesignElement content, int slotID,
			boolean unresolveReference, boolean flag ) throws SemanticException
	{
		assert content != null;

		// Ensure that the content can be dropped from the container.

		if ( !element.getDefn( ).isContainer( ) )
			throw new ContentException( element, slotID,
					ContentException.DESIGN_EXCEPTION_NOT_CONTAINER );
		ContainerSlot slot = element.getSlot( slotID );
		if ( slot == null )
			throw new ContentException( element, slotID,
					ContentException.DESIGN_EXCEPTION_SLOT_NOT_FOUND );
		if ( !slot.contains( content ) )
			throw new ContentException( element, slotID, content,
					ContentException.DESIGN_EXCEPTION_CONTENT_NOT_FOUND );
		if ( !slot.canDrop( content ) )
			throw new ContentException( element, slotID, content,
					ContentException.DESIGN_EXCEPTION_DROP_FORBIDDEN );

		// Can not drop a virtual element. However, if it is called when
		// dropping the child element, the check should be ignored.

		if ( !content.canDrop( ) && !flag )
			throw new ContentException(
					element,
					slotID,
					content,
					ContentException.DESIGN_EXCEPTION_STRUCTURE_CHANGE_FORBIDDEN );

		// if the content is in component slot of report design and it has
		// children, then the operation is forbidden.

		if ( hasDescendents( content, slotID ) )
		{
			throw new ContentException( element, slotID,
					ContentException.DESIGN_EXCEPTION_HAS_DESCENDENTS );
		}

		// Prepare the transaction.

		ContentRecord dropRecord = new ContentRecord( module, element, slotID,
				content, false );

		ActivityStack stack = getActivityStack( );

		stack.startFilterEventTrans( dropRecord.getLabel( ) );

		try
		{
			doDelectAction( content, unresolveReference );
		}
		catch ( SemanticException ex )
		{
			stack.rollback( );
			throw ex;
		}

		// Remove the element itself.

		stack.execute( dropRecord );
		stack.commit( );

	}

	/**
	 * Changes derived elements to derive from the parent (if any) instead.
	 * 
	 * @param obj
	 *            the element to clean up
	 * @throws ExtendsException
	 *             if an error occurs, but the operation should not fail under
	 *             normal conditions
	 */

	private void adjustDerived( DesignElement obj ) throws ExtendsException
	{
		// Skip if this element does not have derived elements.

		Collection derived = obj.getDerived( );
		if ( derived.isEmpty( ) )
			return;

		DesignElement parent = obj.getExtendsElement( );
		Iterator iter = derived.iterator( );
		while ( iter.hasNext( ) )
		{
			DesignElement child = (DesignElement) iter.next( );
			ExtendsCommand childCmd = new ExtendsCommand( module, child );
			childCmd.setExtendsElement( parent );
		}
	}

	/**
	 * Adjusts references to an element that is to be deleted. The element to be
	 * deleted is one that has references in the form of element reference
	 * properties on other elements. These other elements, called "clients",
	 * each contain a property of type element reference and that property
	 * refers to this element. Each reference is recorded with a "back pointer"
	 * from the referenced element to the client. That back pointer has both a
	 * pointer to the client element, and the property within that element that
	 * holds the reference. There are two algorithms to handle this reference
	 * property, which can be selected by <code>unresolveReference</code>. If
	 * <code>unresolveReference</code> is <code>true</code>, the reference
	 * property is unresolved. Otherwise, it's cleared.
	 * 
	 * @param referred
	 *            the element to be deleted
	 * @param unresolveReference
	 *            the flag indicating the reference property should be
	 *            unresolved, instead of cleared
	 * @throws SemanticException
	 *             if an error occurs, but the operation should not fail under
	 *             normal conditions
	 * 
	 * @see #adjustReferredClients(DesignElement)
	 */

	private void adjustReferenceClients( ReferenceableElement referred,
			boolean unresolveReference ) throws SemanticException
	{
		List clients = new ArrayList( referred.getClientList( ) );

		Iterator iter = clients.iterator( );
		while ( iter.hasNext( ) )
		{
			BackRef ref = (BackRef) iter.next( );
			DesignElement client = ref.element;

			if ( unresolveReference )
			{
				BackRefRecord record = new ElementBackRefRecord( module,
						referred, client, ref.propName );
				getActivityStack( ).execute( record );
			}
			else
			{
				if ( referred.isStyle( ) )
				{
					StyleCommand clientCmd = new StyleCommand( module, client );
					clientCmd.setStyleElement( (DesignElement) null );
				}
				else
				{
					PropertyCommand cmd = new PropertyCommand( module, client );
					cmd.setProperty( ref.propName, null );
				}
			}
		}
	}

	/**
	 * Clears references of elements that are referred by the to-be-deleted
	 * element, except for extends and style element references. Unlike the
	 * method {@link #adjustReferenceClients(ReferenceableElement,boolean)},
	 * this method removes references from those elements that are referred.
	 * 
	 * @param element
	 *            the element to be deleted
	 * 
	 */

	private void adjustReferredClients( DesignElement element )
	{
		List propDefns = element.getPropertyDefns( );

		for ( Iterator iter = propDefns.iterator( ); iter.hasNext( ); )
		{
			PropertyDefn propDefn = (PropertyDefn) iter.next( );

			// DO NOT consider extends and style property since this has been
			// handled in remove method.

			if ( DesignElement.EXTENDS_PROP.equalsIgnoreCase( propDefn
					.getName( ) )
					|| StyledElement.STYLE_PROP.equalsIgnoreCase( propDefn
							.getName( ) ) )
				continue;

			if ( propDefn.getTypeCode( ) == PropertyType.ELEMENT_REF_TYPE
					|| propDefn.getTypeCode( ) == PropertyType.STRUCT_REF_TYPE )
			{
				Object value = element.getLocalProperty( module,
						(ElementPropertyDefn) propDefn );

				if ( value != null )
				{
					if ( ( value instanceof StructRefValue )
							|| ( (ElementRefValue) value ).isResolved( ) )
					{
						TemplateParameterDefinition definition = null;
						if ( value instanceof ElementRefValue )
						{
							ElementRefValue templateParam = (ElementRefValue) value;
							if ( templateParam.getTargetElement( ) instanceof TemplateParameterDefinition )
								definition = (TemplateParameterDefinition) templateParam
										.getTargetElement( );
						}
						try
						{
							// Clear all element reference property for dropped
							// element

							PropertyCommand cmd = new PropertyCommand( module,
									element );
							cmd.setProperty( propDefn.getName( ), null );

							// drop the useless template definition

							if ( definition != null
									&& !definition.hasReferences( ) )
							{
								assert definition.getRoot( ) == module;
								ContentCommand contentCmd = new ContentCommand(
										module, module );
								contentCmd
										.remove(
												definition,
												ReportDesign.TEMPLATE_PARAMETER_DEFINITION_SLOT );
							}
						}
						catch ( SemanticException e )
						{
							assert false;
						}
					}
				}
			}
			else if ( propDefn.getTypeCode( ) == PropertyType.LIST_TYPE
					&& propDefn.getSubTypeCode( ) == PropertyType.ELEMENT_REF_TYPE )
			{
				List valueList = (List) element.getLocalProperty( module,
						(ElementPropertyDefn) propDefn );
				if ( valueList != null )
				{
					for ( int i = valueList.size( ) - 1; i >= 0; i-- )
					{
						ElementRefValue item = (ElementRefValue) valueList
								.get( i );
						if ( item.isResolved( ) )
						{
							try
							{
								// Clear all element reference property for
								// dropped element

								PropertyCommand cmd = new PropertyCommand(
										module, element );
								cmd.removeItem( (ElementPropertyDefn) propDefn,
										item.getElement( ) );
							}
							catch ( SemanticException e )
							{
								assert false;
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Deletes user properties. This will also remove property values from this
	 * element and derived elements.
	 * 
	 * @param obj
	 *            the element to clean up
	 * @throws UserPropertyException
	 *             if an error occurs, but the operation should not fail under
	 *             normal conditions
	 */

	private void dropUserProperties( DesignElement obj )
			throws UserPropertyException
	{
		Collection props = obj.getLocalUserProperties( );
		if ( props != null )
		{
			UserPropertyCommand propCmd = new UserPropertyCommand( module, obj );
			Iterator iter = props.iterator( );
			while ( iter.hasNext( ) )
			{
				UserPropertyDefn prop = (UserPropertyDefn) iter.next( );
				propCmd.dropUserProperty( prop.getName( ) );
			}
		}
	}

	/**
	 * Removes all the contents of the element. Removes all contents from all
	 * slots. Does the drop recursively.
	 * 
	 * @param obj
	 *            the element to clean up
	 * @throws SemanticException
	 *             if an error occurs, but the operation should not fail under
	 *             normal conditions
	 */

	private void removeContents( DesignElement obj ) throws SemanticException
	{
		// Skip this step if the element is not a container.

		IElementDefn metaData = obj.getDefn( );
		if ( !metaData.isContainer( ) )
			return;

		int slotCount = metaData.getSlotCount( );
		for ( int slotID = 0; slotID < slotCount; slotID++ )
		{
			ContainerSlot slot = obj.getSlot( slotID );
			ContentCommand contentCmd = new ContentCommand( module, obj );
			while ( !slot.isEmpty( ) )
			{
				DesignElement content = slot.getContent( 0 );
				contentCmd.doRemove( content, slotID, false, true );
			}
		}
	}

	/**
	 * Moves an element from one slot to another at the specified position. The
	 * destination slot can be in the same element (unusual) or a different
	 * element (usual case). Use the other form of this method to move an
	 * element within the same slot.
	 * 
	 * @param content
	 *            the element to move.
	 * @param fromSlotID
	 *            the old slot location within the container item associated
	 *            with this command.
	 * @param to
	 *            the new container.
	 * @param toSlotID
	 *            the slot within the new container.
	 * @param newPos
	 *            the position in the target slot to which the content will be
	 *            moved. If it is -1 or greater than the size of the target
	 *            slot, the content will be appended at the end of the slot.
	 * @throws ContentException
	 *             if the content cannot be moved to new container.
	 */

	private void doMove( DesignElement content, int fromSlotID,
			DesignElement to, int toSlotID, int newPos )
			throws ContentException
	{
		assert content != null;
		assert to != null;
		assert newPos >= -1;

		// Do nothing if source and destination are the same.

		if ( element == to && fromSlotID == toSlotID )
			return;

		// Can not change the structure of child element or a virtual element(
		// inside the child ).

		if ( content.isVirtualElement( ) || content.getExtendsName( ) != null )
			throw new ContentException(
					element,
					fromSlotID,
					content,
					ContentException.DESIGN_EXCEPTION_STRUCTURE_CHANGE_FORBIDDEN );

		// Cannot put an element inside itself.

		if ( to.isContentOf( content ) )
			throw new ContentException( to, fromSlotID, content,
					ContentException.DESIGN_EXCEPTION_RECURSIVE );

		// Ensure that the content can be put into the container.

		if ( !element.getDefn( ).isContainer( ) )
			throw new ContentException( element, fromSlotID,
					ContentException.DESIGN_EXCEPTION_NOT_CONTAINER );
		if ( !to.getDefn( ).isContainer( ) )
			throw new ContentException( to, toSlotID,
					ContentException.DESIGN_EXCEPTION_NOT_CONTAINER );
		ContainerSlot fromSlot = element.getSlot( fromSlotID );
		if ( fromSlot == null )
			throw new ContentException( element, fromSlotID,
					ContentException.DESIGN_EXCEPTION_SLOT_NOT_FOUND );
		ContainerSlot toSlot = to.getSlot( toSlotID );
		if ( toSlot == null )
			throw new ContentException( to, toSlotID,
					ContentException.DESIGN_EXCEPTION_SLOT_NOT_FOUND );
		if ( !fromSlot.contains( content ) )
			throw new ContentException( element, fromSlotID, content,
					ContentException.DESIGN_EXCEPTION_CONTENT_NOT_FOUND );
		if ( !fromSlot.canDrop( content ) )
			throw new ContentException( element, fromSlotID, content,
					ContentException.DESIGN_EXCEPTION_DROP_FORBIDDEN );

		// if the content is in component slot of report design and it has
		// children, then the operation is forbidden.

		if ( hasDescendents( content, fromSlotID ) )
		{
			throw new ContentException( element, fromSlotID,
					ContentException.DESIGN_EXCEPTION_HAS_DESCENDENTS );
		}

		SlotDefn slotInfo = (SlotDefn) to.getDefn( ).getSlot( toSlotID );
		if ( !slotInfo.canContain( content ) )
			throw new ContentException( to, toSlotID, content,
					ContentException.DESIGN_EXCEPTION_WRONG_TYPE );
		if ( !slotInfo.isMultipleCardinality( ) && toSlot.getCount( ) > 0 )
			throw new ContentException( to, toSlotID,
					ContentException.DESIGN_EXCEPTION_SLOT_IS_FULL );

		if ( !to.canContain( module, toSlotID, content ) )
			throw new ContentException(
					to,
					toSlotID,
					content,
					ContentException.DESIGN_EXCEPTION_INVALID_CONTEXT_CONTAINMENT );

		ActivityStack stack = getActivityStack( );

		String label = ModelMessages
				.getMessage( MessageConstants.MOVE_ELEMENT_MESSAGE );
		stack.startTrans( label );

		ContentRecord record = new ContentRecord( module, element, fromSlotID,
				content, false );
		stack.execute( record );
		record = new ContentRecord( module, to, toSlotID, content, newPos );
		stack.execute( record );
		stack.commit( );
	}

	/**
	 * Moves an element from one slot to another at the specified position. The
	 * destination slot can be in the same element (unusual) or a different
	 * element (usual case). Use the other form of this method to move an
	 * element within the same slot.
	 * 
	 * @param content
	 *            The element to move.
	 * @param fromSlotID
	 *            Old slot location within the container item associated with
	 *            this command.
	 * @param to
	 *            The new container.
	 * @param toSlotID
	 *            the slot within the new container.
	 * @param newPos
	 *            the position in the target slot to which the content will be
	 *            moved. If it is greater than the size of the target slot, the
	 *            content will be appended at the end of the slot.
	 * @throws ContentException
	 */

	public void move( DesignElement content, int fromSlotID, DesignElement to,
			int toSlotID, int newPos ) throws ContentException
	{
		assert newPos >= 0;
		doMove( content, fromSlotID, to, toSlotID, newPos );
	}

	/**
	 * Moves an element from one slot to another. The destination slot can be in
	 * the same element (unusual) or a different element (usual case.) Use the
	 * other form of this method to move an element within the same slot.
	 * 
	 * @param content
	 *            The element to move.
	 * @param fromSlotID
	 *            Old slot location within the container item associated with
	 *            this command.
	 * @param to
	 *            The new container.
	 * @param toSlotID
	 *            The slot within the new container.
	 * @throws ContentException
	 */

	public void move( DesignElement content, int fromSlotID, DesignElement to,
			int toSlotID ) throws ContentException
	{
		doMove( content, fromSlotID, to, toSlotID, -1 );
	}

	/**
	 * Moves an element from one position to another within the same slot.
	 * <p>
	 * For example, if a slot has A, B, C elements in order, when move A element
	 * to <code>newPosn</code> with the value 2, the sequence becomes B, A, C.
	 * 
	 * 
	 * @param content
	 *            The element to move.
	 * @param slotID
	 *            The slot that contains the element.
	 * @param newPosn
	 *            The new position within the slot. Note that the range of
	 *            <code>newPos</code> is from 0 to the number of element in
	 *            the slot with the ID <code>slotID</code>.
	 * @throws ContentException
	 *             if the content cannot be moved to new container.
	 */

	public void movePosition( DesignElement content, int slotID, int newPosn )
			throws ContentException
	{
		assert content != null;

		// Ensure that the content can be put into the container.

		if ( !element.getDefn( ).isContainer( ) )
			throw new ContentException( element, slotID,
					ContentException.DESIGN_EXCEPTION_NOT_CONTAINER );
		ContainerSlot slot = element.getSlot( slotID );
		if ( slot == null )
			throw new ContentException( element, slotID,
					ContentException.DESIGN_EXCEPTION_SLOT_NOT_FOUND );
		if ( !content.isContentOf( element ) )
			throw new ContentException( element, slotID, content,
					ContentException.DESIGN_EXCEPTION_CONTENT_NOT_FOUND );

		// Skip the step if the slotID has only single content.

		SlotDefn slotInfo = (SlotDefn) element.getDefn( ).getSlot( slotID );
		if ( !slotInfo.isMultipleCardinality( ) )
			return;

		// Can not change the structure of child element or a virtual element(
		// inside the child ).

		if ( content.isVirtualElement( ) )
			throw new ContentException(
					element,
					slotID,
					content,
					ContentException.DESIGN_EXCEPTION_STRUCTURE_CHANGE_FORBIDDEN );

		if ( !canMovePosition( content, slotID, newPosn ) )
			throw new ContentException( element, slotID,
					ContentException.DESIGN_EXCEPTION_MOVE_FORBIDDEN );

		int oldPosn = slot.findPosn( content );
		int adjustedNewPosn = checkAndAdjustPosition( oldPosn, newPosn, slot
				.getCount( ) );
		if ( oldPosn == adjustedNewPosn )
			return;

		// Move the new position so that it is in range.

		// if ( newPosn < 0 )
		// newPosn = 0;
		// if ( newPosn > slot.getCount( ) - 1 )
		// newPosn = slot.getCount( );
		//
		// // If the new position is the same as the old, then skip the
		// operation.
		//
		// if ( ( posn == newPosn ) || ( posn + 1 == newPosn ) )
		// return;
		//
		// // adjust the position when move a item from a position with a small
		// // index to another position with a bigger index.
		//
		// if ( posn < newPosn )
		// newPosn -= 1;

		// Do the move.

		MoveContentRecord record = new MoveContentRecord( element, slotID,
				content, adjustedNewPosn );
		getActivityStack( ).execute( record );
	}

	/**
	 * Returns false if the content has ancestor behind the new position or has
	 * children before the new position.
	 * 
	 * @param content
	 *            the element to move position
	 * @param slotID
	 *            the slot that contains the element
	 * @param newPosn
	 *            the new position within the slot.
	 * @return true if the content can be moved within the slot, otherwise
	 *         false.
	 */

	private boolean canMovePosition( DesignElement content, int slotID,
			int newPosn )
	{
		if ( element instanceof ReportDesign
				&& slotID == ReportDesign.COMPONENT_SLOT )
		{
			List derived = content.getDerived( );
			ContainerSlot slot = element.getSlot( slotID );
			Iterator iter = derived.iterator( );
			while ( iter.hasNext( ) )
			{
				DesignElement child = (DesignElement) iter.next( );
				if ( slot.contains( child ) )
				{
					// if content has child before the new position
					// then the move of new position is forbidden.

					if ( slot.findPosn( child ) <= newPosn )
						return false;
				}
			}
			DesignElement e = content.getExtendsElement( );
			while ( e != null )
			{
				// if the content has ancestor behind the new position
				// then the move of new position is forbidden.

				if ( slot.findPosn( e ) >= newPosn )
					return false;
				e = e.getExtendsElement( );
			}
		}
		return true;
	}

	/**
	 * Returns true if content in component slot of report design has
	 * descendants, otherwise false.
	 * 
	 * @param content
	 *            the content to handle
	 * @param fromSlotID
	 *            the slot that contains the element
	 * @return true if content has descendants, otherwise false.
	 */

	private boolean hasDescendents( DesignElement content, int fromSlotID )
	{
		return element instanceof ReportDesign
				&& fromSlotID == ReportDesign.COMPONENT_SLOT
				&& content.hasDerived( );
	}

	/**
	 * Does some transformation between template elements and report items or
	 * data sets. Virtually all elements must reside in a container. Containers
	 * are identified by a container ID.
	 * 
	 * @param from
	 *            the old element to replace
	 * @param to
	 *            the new element to replace
	 * @param slotID
	 *            the slot from which to replace the old element
	 * @param unresolveReference
	 *            status whether to un-resolve the references or set the
	 *            reference to null
	 * @throws SemanticException
	 *             if the old element cannot be replaced by the new element into
	 *             this container.
	 */

	public void transformTemplate( DesignElement from, DesignElement to,
			int slotID, boolean unresolveReference ) throws SemanticException
	{
		doReplace( from, to, slotID, unresolveReference );
	}

	/**
	 * @see #transformTemplate(DesignElement, DesignElement, int, boolean)
	 * 
	 * @param oldElement
	 *            the old element to replace
	 * @param newElement
	 *            the new element to replace
	 * @param slotID
	 *            the slot from which to replace the old element
	 * @param unresolveReference
	 *            status whether to un-resolve the references or set the
	 *            reference to null
	 * @throws SemanticException
	 *             if the old element cannot be replaced by the new element into
	 *             this container.
	 */

	private void doReplace( DesignElement oldElement, DesignElement newElement,
			int slotID, boolean unresolveReference ) throws SemanticException
	{
		assert newElement.getContainer( ) == null;

		// Ensure that the new element can be put into the container.

		ElementDefn metaData = (ElementDefn) element.getDefn( );
		if ( !metaData.isContainer( ) )
			throw new ContentException( element, slotID,
					ContentException.DESIGN_EXCEPTION_NOT_CONTAINER );
		SlotDefn slotInfo = (SlotDefn) metaData.getSlot( slotID );
		if ( slotInfo == null )
			throw new ContentException( element, slotID,
					ContentException.DESIGN_EXCEPTION_SLOT_NOT_FOUND );
		if ( !slotInfo.canContain( newElement ) )
			throw new ContentException( element, slotID, newElement,
					ContentException.DESIGN_EXCEPTION_WRONG_TYPE );

		// This element is already the content of the element to add.

		if ( element.isContentOf( newElement ) )
			throw new ContentException( element, slotID, newElement,
					ContentException.DESIGN_EXCEPTION_RECURSIVE );

		if ( slotID == Module.COMPONENT_SLOT )
		{
			if ( StringUtil.isBlank( newElement.getName( ) ) )
				throw new ContentException( element, slotID, newElement,
						ContentException.DESIGN_EXCEPTION_CONTENT_NAME_REQUIRED );
		}

		// do some checks about the element to be replaced

		ContainerSlot slot = element.getSlot( slotID );
		if ( !slot.contains( oldElement ) )
			throw new ContentException( element, slotID, oldElement,
					ContentException.DESIGN_EXCEPTION_CONTENT_NOT_FOUND );

		// do all checks about the transformation state

		if ( oldElement instanceof TemplateElement )
		{
			if ( !oldElement.canDrop( )
					|| !oldElement.isTemplateParameterValue( module )
					|| !newElement.getDefn( ).isKindOf(
							( (TemplateElement) oldElement ).getDefaultElement(
									module ).getDefn( ) ) )
				throw new TemplateException(
						oldElement,
						TemplateException.DESIGN_EXCEPTION_REVERT_TO_TEMPLATE_FORBIDDEN );
		}
		else
		{
			if ( !oldElement.canTransformToTemplate( module )
					|| !( newElement instanceof TemplateElement ) )
				throw new ContentException(
						element,
						slotID,
						ContentException.DESIGN_EXCEPTION_TEMPLATE_TRANSFORM_FORBIDDEN );
		}

		// if the old element is in component slot of report design and it has
		// children, then the operation is forbidden.

		if ( hasDescendents( oldElement, slotID ) )
		{
			throw new ContentException( oldElement, slotID,
					ContentException.DESIGN_EXCEPTION_HAS_DESCENDENTS );
		}

		// Prepare the transaction.

		ContentReplaceRecord replaceRecord = new TemplateTransformRecord(
				module, element, slotID, oldElement, newElement );

		ActivityStack stack = getActivityStack( );

		stack.startFilterEventTrans( replaceRecord.getLabel( ) );

		try
		{
			doDelectAction( oldElement, unresolveReference );

			// do some checks about the template issues

			TemplateCommand cmd = new TemplateCommand( module, element );
			cmd.checkAdd( newElement, slotID );

			// Remove the element itself.

			stack.execute( replaceRecord );

			addElementNames( newElement );
		}
		catch ( ContentException e )
		{
			stack.rollback( );
			throw e;
		}
		catch ( NameException e )
		{
			stack.rollback( );
			throw e;
		}
		catch ( SemanticException e )
		{
			stack.rollback( );
			throw e;
		}

		stack.commit( );
	}

	/**
	 * Does some actions when the content is removed from the design tree.
	 * 
	 * @param content
	 *            the content to remove
	 * @param unresolveReference
	 *            status whether to un-resolve the references
	 * @throws SemanticException
	 */

	private void doDelectAction( DesignElement content,
			boolean unresolveReference ) throws SemanticException
	{
		// Remove contents.

		removeContents( content );

		// Clean up references to or from the element.

		dropUserProperties( content );
		if ( content.hasReferences( ) )
			adjustReferenceClients( (ReferenceableElement) content,
					unresolveReference );
		adjustReferredClients( content );
		adjustDerived( content );

		// Drop the style...

		if ( content.getStyle( module ) != null )
		{
			StyleCommand styleCmd = new StyleCommand( module, content );
			styleCmd.setStyle( null );
		}

		// Drop the extends...

		if ( content.getExtendsElement( ) != null )
		{
			ExtendsCommand extendsCmd = new ExtendsCommand( module, content );
			extendsCmd.setExtendsName( null );
		}

		// Remove from name space...

		if ( content.getName( ) != null )
		{
			NameCommand nameCmd = new NameCommand( module, content );
			nameCmd.dropElement( );
		}

		// Drop the property binding

		List propertyBindings = module.getPropertyBindings( content );
		for ( int i = 0; i < propertyBindings.size( ); i++ )
		{
			PropertyBinding propBinding = (PropertyBinding) propertyBindings
					.get( i );
			ElementPropertyDefn propDefn = module
					.getPropertyDefn( Module.PROPERTY_BINDINGS_PROP );
			PropertyCommand propCommand = new PropertyCommand( module, module );
			propCommand.removeItem( new CachedMemberRef( propDefn ),
					propBinding );
		}
	}
}