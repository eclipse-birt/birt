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

package org.eclipse.birt.report.model.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.ListingElement;
import org.eclipse.birt.report.model.elements.MasterPage;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.TemplateElement;
import org.eclipse.birt.report.model.elements.interfaces.IListingElementModel;
import org.eclipse.birt.report.model.elements.interfaces.ITableItemModel;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.IContainerDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.SemanticTriggerDefnSet;
import org.eclipse.birt.report.model.metadata.SlotDefn;
import org.eclipse.birt.report.model.util.ContentExceptionFactory;
import org.eclipse.birt.report.model.util.ContentIterator;

/**
 * 
 */
public final class ContainerContext
{

	/**
	 * Elements are structured in a hierarchy. The implementation of the
	 * container-to-child relationship must be defined in derived classes. This
	 * base class defines the child-to-container relationship.
	 */

	protected final DesignElement container;

	/**
	 * Slot in the container in which this element resides.
	 */

	protected final int containerSlotID;

	/**
	 * Name of the property in which this element resides.
	 */
	protected final String containerProp;

	/**
	 * 
	 */
	private final boolean isSlot;

	/**
	 * 
	 */

	private final SlotDefn slotDefn;

	/**
	 * 
	 * @param theContainer
	 * @param slotID
	 */
	public ContainerContext( DesignElement theContainer, int slotID )
	{

		if ( theContainer == null )
			throw new IllegalArgumentException(
					"The container of ContainerInfo should not be null" ); //$NON-NLS-1$

		this.container = theContainer;
		this.containerSlotID = slotID;

		IElementDefn tmpDefn = container.getDefn( );
		assert tmpDefn != null;

		this.slotDefn = (SlotDefn) tmpDefn.getSlot( containerSlotID );

		this.isSlot = true;
		this.containerProp = null;
	}

	/**
	 * 
	 * @param theContainer
	 * @param propName
	 */
	public ContainerContext( DesignElement theContainer, String propName )
	{
		if ( theContainer == null || propName == null )
			throw new IllegalArgumentException(
					"The container and property name should not be null" ); //$NON-NLS-1$
		this.container = theContainer;
		this.containerProp = propName;

		if ( container.getPropertyDefn( propName ) == null )
			throw new IllegalArgumentException(
					"The property \"" + propName + "\" is not defined" ); //$NON-NLS-1$//$NON-NLS-2$
		this.isSlot = false;
		this.containerSlotID = DesignElement.NO_SLOT;
		this.slotDefn = null;
	}

	/**
	 * Gets the container identifier. It is either the slot name or the property
	 * name where the content resides.
	 * 
	 * @return container identifier
	 */
	public String getContainerIdentifier( )
	{
		if ( container.getPropertyDefn( containerProp ) != null )
			return containerProp;
		return container.getDefn( ).getSlot( containerSlotID ).getName( );
	}

	/**
	 * Gets the container element of this information.
	 * 
	 * @return the container element
	 */
	public DesignElement getElement( )
	{
		return this.container;
	}

	/**
	 * Gets the slot id of this information.
	 * 
	 * @return the slot id of this information if slot definition is valid,
	 *         otherwise <code>-1</code>
	 */
	public int getSlotID( )
	{
		return this.containerSlotID;
	}

	/**
	 * Gets the property name of this container information.
	 * 
	 * @return the property name of this information if property definition is
	 *         valid, otherwise <code>null</code>>
	 */
	public String getPropertyName( )
	{
		if ( !isSlot )
			return this.containerProp;
		return null;
	}

	/**
	 * Gets the container definition of this information. The returned value can
	 * be either <code>ISlotDen</code>> or <code>IPropertyDefn</code>>.
	 * 
	 * @return the container definition of this information if slot definition
	 *         is valid or property definition is valid, otherwise null
	 */
	public IContainerDefn getContainerDefn( )
	{
		if ( isSlot )
			return container.getDefn( ).getSlot( containerSlotID );
		PropertyDefn defn = container.getPropertyDefn( containerProp );
		if ( defn != null && defn.isElementType( ) )
			return defn;
		return null;
	}

	/**
	 * Finds the position where the specialized content resides in this
	 * container information.
	 * 
	 * @param module
	 * @param content
	 * @return 0-based position index if found, otherwise -1
	 */
	public int indexOf( Module module, DesignElement content )
	{
		if ( isSlot )
			return container.getSlot( containerSlotID ).findPosn( content );
		Object value = container.getProperty( module, containerProp );
		if ( value == content )
			return 0;
		if ( value instanceof List )
			return ( (List<Object>) value ).indexOf( content );
		return -1;
	}

	/**
	 * 
	 * @param content
	 * @return the 0-based position
	 */
	public int indexOf( DesignElement content )
	{
		return indexOf( container.getRoot( ), content );
	}

	/**
	 * Determines whether this element and its contents are managed by
	 * namespace. If this element is a pending node and not in any module, or it
	 * is contained in a slot that is not managed by namespace, then return
	 * false. Otherwise true.
	 * 
	 * @return true if this element and its contents are managed by namespace,
	 *         otherwise false
	 */

	public boolean isManagedByNameSpace( )
	{
		// if this element is a pending node, return false
		if ( container.getRoot( ) == null )
			return false;

		// if this element is variableElement and it does not locate in report
		// design, it will not be managed by name space.
		if ( containerProp != null )
		{
			PropertyDefn propDefn = container.getPropertyDefn( containerProp );
			IElementDefn variableElementDefn = MetaDataDictionary.getInstance( )
					.getElement( ReportDesignConstants.VARIABLE_ELEMENT );
			if ( propDefn.canContain( variableElementDefn )
					&& !( container instanceof ReportDesign ) )
			{
				return false;
			}
		}

		// check the slot
		ContainerContext containerInfo = this;
		while ( containerInfo != null )
		{
			if ( containerInfo.isSlot )
			{
				SlotDefn slotInfo = (SlotDefn) containerInfo.container
						.getDefn( ).getSlot( containerInfo.containerSlotID );
				if ( slotInfo != null && !slotInfo.isManagedByNameSpace( ) )
					return false;
			}
			containerInfo = containerInfo.container.getContainerInfo( );
		}
		return true;
	}

	/**
	 * Gets selector of the given slot of this element. The selector is kind of
	 * predefined style, and its style property value can be applied on contents
	 * of the given slot of this element depending on whether property can be
	 * inherited.
	 * 
	 * @return the selector of the given slot of this element.
	 */

	public String getSelector( )
	{
		if ( slotDefn == null )
			return null;

		String slotSelector = slotDefn.getSelector( );
		if ( StringUtil.isBlank( slotSelector ) )
			return null;

		// specially handle for group
		if ( container instanceof GroupElement )
		{
			int depth = ( (GroupElement) container ).getGroupLevel( );
			if ( depth > 9 )
				depth = 9;
			return slotSelector + "-" + Integer.toString( depth ); //$NON-NLS-1$ 
		}

		return slotSelector;
	}

	/**
	 * Checks the validity whether the specialized element type can be inserted
	 * in this container information in ROM.def.
	 * 
	 * @param defn
	 * @return true if the element type is legal to be held by this container,
	 *         otherwise false.
	 */
	public boolean canContainInRom( IElementDefn defn )
	{
		if ( defn == null )
			return false;
		return getContainerDefn( ) == null ? false : getContainerDefn( )
				.canContain( defn );
	}

	/**
	 * Justifies whether this container is multiple cardinality or not.
	 * 
	 * @return true if the container is multiple cardinality, otherwise false
	 */
	public boolean isContainerMultipleCardinality( )
	{
		IContainerDefn defn = getContainerDefn( );
		if ( defn instanceof PropertyDefn )
			return ( (PropertyDefn) defn ).isList( );
		if ( defn instanceof SlotDefn )
			return ( (SlotDefn) defn ).isMultipleCardinality( );
		return false;
	}

	/**
	 * The list of all the contents that reside in this container information.
	 * If no element is in, returned value is empty list.
	 * 
	 * @param module
	 * @return the list of the contents
	 */
	public List<DesignElement> getContents( Module module )
	{
		if ( getContainerDefn( ) == null )
			return Collections.emptyList( );
		if ( isSlot )
		{
			return container.getSlot( containerSlotID ).getContents( );
		}

		ElementPropertyDefn defn = container.getPropertyDefn( containerProp );
		Object value = container.getLocalProperty( module, defn );
		if ( defn == null || value == null )
			return Collections.emptyList( );
		if ( defn.isList( ) )
		{
			return (List<DesignElement>) value;
		}
		List<DesignElement> result = new ArrayList<DesignElement>( );
		result.add( (DesignElement) value );
		return result;
	}

	/**
	 * Gets the content in the specialized position of this container. If the
	 * given index is out of range, returned value is <code>null</code>.
	 * 
	 * @param module
	 * @param posn
	 *            0-based position index
	 * @return the content if found, otherwise null
	 */
	public DesignElement getContent( Module module, int posn )
	{
		if ( isSlot )
			return container.getSlot( containerSlotID ).getContent( posn );
		return getContent( module, containerProp, posn );
	}

	/**
	 * 
	 * @param module
	 * @param propName
	 * @param posn
	 * @return
	 */
	private DesignElement getContent( Module module, String propName, int posn )
	{
		ElementPropertyDefn defn = container.getPropertyDefn( propName );
		if ( defn == null )
			return null;
		if ( defn.isList( ) )
		{
			List<Object> value = container.getListProperty( module, propName );
			return (DesignElement) ( value == null ? null : value.get( posn ) );
		}
		return (DesignElement) ( posn == 0 ? container.getProperty( module,
				defn ) : null );
	}

	/**
	 * The count of all the contents that reside in this container information.
	 * 
	 * @param module
	 * @return the count of all the contents
	 */
	public int getContentCount( Module module )
	{
		if ( isSlot )
			return container.getSlot( containerSlotID ) == null ? 0 : container
					.getSlot( containerSlotID ).getCount( );
		return getContentCount( module, containerProp );
	}

	/**
	 * 
	 * @param module
	 * @param propName
	 * @return
	 */
	private int getContentCount( Module module, String propName )
	{
		ElementPropertyDefn defn = container.getPropertyDefn( propName );
		if ( defn == null )
			return 0;
		if ( defn.isList( ) )
		{
			List<Object> value = container.getListProperty( module, propName );
			return value == null ? 0 : value.size( );
		}
		return container.getProperty( module, defn ) == null ? 0 : 1;
	}

	/**
	 * 
	 * @param module
	 * @param content
	 * @param posn
	 *            0-based position index
	 */
	public void add( Module module, DesignElement content, int posn )
	{
		if ( isSlot )
			container.add( content, containerSlotID, posn );
		else
			container.add( module, content, containerProp, posn );
	}

	/**
	 * 
	 * @param module
	 * @param content
	 */
	public void add( Module module, DesignElement content )
	{
		if ( isSlot )
			container.add( content, containerSlotID );
		else
			container.add( module, content, containerProp );
	}

	/**
	 * 
	 * @param module
	 * @param content
	 */
	public void remove( Module module, DesignElement content )
	{
		if ( isSlot )
			container.remove( content, containerSlotID );
		else
			container.remove( module, content, containerProp );
	}

	/**
	 * 
	 * @param module
	 * @param content
	 * @return true if the content resides in this container information,
	 *         otherwise false
	 */
	public boolean contains( Module module, DesignElement content )
	{
		return indexOf( module, content ) != -1;
	}

	/**
	 * Gets the semantic-trigger-set for the container definition. It is either
	 * be <code>PropertyDefn</code> or <code>SlotDefn</code>.
	 * 
	 * @return the semantic-trigger-set if container definition is found,
	 *         otherwise null
	 */
	public SemanticTriggerDefnSet getTriggerSetForContainerDefn( )
	{
		IContainerDefn defn = getContainerDefn( );
		if ( defn instanceof PropertyDefn )
		{
			return ( (PropertyDefn) defn ).getTriggerDefnSet( );
		}
		if ( defn instanceof SlotDefn )
		{
			return ( (SlotDefn) defn ).getTriggerDefnSet( );
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals( Object obj )
	{
		if ( !( obj instanceof ContainerContext ) )
			return false;
		ContainerContext infoObj = (ContainerContext) obj;
		if ( container.equals( infoObj.container ) )
		{
			if ( isSlot && infoObj.isSlot
					&& infoObj.containerSlotID == containerSlotID )
				return true;

			else if ( !isSlot && !infoObj.isSlot
					&& containerProp.equals( infoObj.containerProp ) )
				return true;
		}
		return false;
	}

	/*
	 * (non-Jsdoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode( )
	{
		final int prime = 31;
		int hashCode = container.hashCode( ) * prime;
		hashCode += isSlot ? containerSlotID : containerProp.hashCode( );
		return hashCode;
	}

	/**
	 * Determines if the slot can contain a given element.
	 * 
	 * @param module
	 *            the module
	 * @param content
	 *            the element to insert
	 * @return a list containing exceptions.
	 */

	public boolean canContain( Module module, DesignElement content )
	{
		return new ContainerContextProvider( this )
				.canContain( module, content );
	}

	/**
	 * Determines if the slot can contain an element with the type of
	 * <code>type</code>.
	 * 
	 * @param module
	 * 
	 * @param type
	 *            the name of the element type, like "Table", "List", etc.
	 * @return <code>true</code> if the slot can contain the an element with
	 *         <code>type</code> type, otherwise <code>false</code>.
	 * 
	 * @see #canContain(int, DesignElementHandle)
	 */

	public boolean canContain( Module module, String type )
	{
		return new ContainerContextProvider( this ).canContain( module, type );
	}

	/**
	 * Determines if the current element can contain an element with the
	 * definition of <code>elementType</code> on context containment.
	 * 
	 * @param module
	 *            the module
	 * @param defn
	 *            the definition of the element
	 * @return <code>true</code> if the slot can contain the an element,
	 *         otherwise <code>false</code>.
	 */

	public final boolean canContain( Module module, IElementDefn defn )
	{
		return new ContainerContextProvider( this ).canContain( module, defn );
	}

	/**
	 * Determines if the slot can contain a given element.
	 * 
	 * @param module
	 *            the module
	 * @param element
	 *            the element to insert
	 * @return a list containing exceptions.
	 */

	public final List<SemanticException> checkContainmentContext(
			Module module, DesignElement element )
	{
		return new ContainerContextProvider( this ).checkContainmentContext(
				module, element );
	}

	/**
	 * Moves the content in the given position to the new positon.
	 * 
	 * @param module
	 *            the module where the container resides
	 * @param from
	 *            the source position
	 * @param to
	 *            the destination position
	 */
	public void move( Module module, int from, int to )
	{
		if ( isSlot )
			container.getSlot( containerSlotID ).moveContent( from, to );
		else
			move( module, containerProp, from, to );
	}

	/**
	 * 
	 * @param module
	 * @param propName
	 * @param from
	 * @param to
	 */
	private void move( Module module, String propName, int from, int to )
	{
		PropertyDefn defn = container.getPropertyDefn( propName );

		assert defn.isList( )
				&& defn.getTypeCode( ) == IPropertyType.ELEMENT_TYPE;
		List<Object> items = container.getListProperty( module, propName );
		assert items != null;
		assert from >= 0 && from < items.size( );
		assert to >= 0 && to < items.size( );

		if ( from == to )
			return;

		Object obj = items.remove( from );
		items.add( to, obj );
	}

	/**
	 * Clears all the contents in the container.
	 * 
	 */
	public void clearContents( )
	{
		if ( isSlot )
		{
			ContainerSlot slot = container.getSlot( containerSlotID );
			if ( slot != null )
				slot.clear( );
		}
		else
		{
			container.clearProperty( containerProp );
		}
	}

	/**
	 * Returns the context for the given element. The parameter element must be
	 * same as the <code>container</code>.
	 * 
	 * @param newElement
	 *            the element
	 * @return the context for the element
	 */

	public ContainerContext createContext( DesignElement newElement )
	{
		if ( newElement.getDefn( ) != container.getDefn( ) )
			return null;

		ContainerContext newContext = null;
		if ( isSlot )
			newContext = new ContainerContext( newElement, containerSlotID );
		else
			newContext = new ContainerContext( newElement, containerProp );

		return newContext;
	}

	/**
	 * @return the isSlot
	 */

	public boolean isROMSlot( )
	{
		return isSlot;
	}

	/**
	 * 
	 */
	static class ContainerContextProvider
	{

		/**
		 * The container element.
		 */
		protected ContainerContext focus = null;

		/**
		 * @param containerInfo
		 */
		public ContainerContextProvider( ContainerContext containerInfo )
		{
			if ( containerInfo == null )
				throw new IllegalArgumentException(
						"The containerInfo of this context should not be null" ); //$NON-NLS-1$
			this.focus = containerInfo;
		}

		// refactor codes

		/**
		 * Determines if the slot can contain an element with the type of
		 * <code>type</code>.
		 * 
		 * @param module
		 * 
		 * @param propName
		 *            the slot id
		 * @param type
		 *            the name of the element type, like "Table", "List", etc.
		 * @return <code>true</code> if the slot can contain the an element with
		 *         <code>type</code> type, otherwise <code>false</code>.
		 * 
		 * @see #canContain(int, DesignElementHandle)
		 */

		public boolean canContain( Module module, String type )
		{
			if ( type == null )
				return false;

			return canContain( module, MetaDataDictionary.getInstance( )
					.getElement( type ) );
		}

		/**
		 * Determines if the slot can contain a given element.
		 * 
		 * @param module
		 *            the module
		 * @param element
		 *            the element to insert
		 * @return a list containing exceptions.
		 */

		public final boolean canContain( Module module, DesignElement element )
		{
			if ( module != null && module.isReadOnly( ) )
				return false;

			List<SemanticException> errors = checkContainmentContext( module,
					element );
			if ( !errors.isEmpty( ) )
				return false;

			return true;
		}

		/**
		 * Determines if the current element can contain an element with the
		 * definition of <code>elementType</code> on context containment.
		 * 
		 * @param module
		 *            the module
		 * @param defn
		 *            the definition of the element
		 * @return <code>true</code> if the slot can contain the an element,
		 *         otherwise <code>false</code>.
		 */

		public final boolean canContain( Module module, IElementDefn defn )
		{
			if ( defn == null || ( module != null && module.isReadOnly( ) ) )
				return false;

			boolean retValue = canContainInRom( defn );
			if ( !retValue )
				return false;

			// if the root of element is included by report/library. Do not
			// allow
			// drop.

			if ( focus.getElement( ).isRootIncludedByModule( ) )
				return false;

			if ( !canContainTemplateElement( module, defn ) )
				return false;

			// Can not change structure of child element or a virtual element(
			// inside the child ).

			if ( focus.getElement( ).isVirtualElement( )
					|| focus.getElement( ).getExtendsName( ) != null )
				return false;

			// A summany table cannot contains any detail rows
			if ( focus.getElement( ) instanceof TableItem
					&& focus.getElement( ).getBooleanProperty( module,
							ITableItemModel.IS_SUMMARY_TABLE_PROP )
					&& focus.containerSlotID == IListingElementModel.DETAIL_SLOT )
				return false;

			// special cases check table header containment.

			ContainerContext containerInfo = this.focus;
			while ( containerInfo != null )
			{
				DesignElement container = containerInfo.getElement( );
				if ( container instanceof ListingElement
						|| container instanceof MasterPage )
				{
					List<SemanticException> errors = container.checkContent(
							module, this.focus, defn );
					return errors.isEmpty( );
				}

				containerInfo = container.getContainerInfo( );
			}

			return retValue;
		}

		/**
		 * Determines if the slot can contain a given element.
		 * 
		 * @param module
		 *            the module
		 * @param element
		 *            the element to insert
		 * @return a list containing exceptions.
		 */

		public final List<SemanticException> checkContainmentContext(
				Module module, DesignElement element )
		{
			if ( element == null )
				return Collections.emptyList( );

			boolean retValue = canContainInRom( element.getDefn( ) );
			ContentException e = ContentExceptionFactory
					.createContentException(
							focus,
							element,
							ContentException.DESIGN_EXCEPTION_INVALID_CONTEXT_CONTAINMENT );

			List<SemanticException> errors = new ArrayList<SemanticException>( );
			if ( !retValue )
			{
				errors.add( e );
				return errors;
			}

			// if this element can not be contained in the module, return false;
			// such as, template elements can not be contained in the libraries,
			// so either a template table or a real table with a template image
			// in one cell of it can never be contained in a libraries

			if ( !canContainTemplateElement( module, element ) )
			{
				errors.add( e );
				return errors;
			}

			// if the root of element is included by report/library. Do not
			// allow
			// drop.

			if ( focus.getElement( ).isRootIncludedByModule( ) )
			{
				errors.add( e );
				return errors;
			}

			// Can not change the structure of child element or a virtual
			// element(
			// inside the child ).

			if ( focus.getElement( ).isVirtualElement( )
					|| focus.getElement( ).getExtendsName( ) != null )
			{
				errors.add( e );
				return errors;
			}

			// A summany table cannot contains any detail rows
			if ( focus.getElement( ) instanceof TableItem
					&& focus.getElement( ).getBooleanProperty( module,
							ITableItemModel.IS_SUMMARY_TABLE_PROP )
					&& focus.containerSlotID == IListingElementModel.DETAIL_SLOT )
			{
				errors.add( e );
				return errors;
			}

			// special cases check table header containment.

			ContainerContext containerInfor = focus;
			// DesignElement tmpContainer = this.focus;
			while ( containerInfor != null )
			{
				DesignElement container = containerInfor.getElement( );
				if ( container == element )
				{
					errors.add( e );
					return errors;
				}

				if ( container instanceof ListingElement
						|| container instanceof MasterPage )
				{
					errors = container.checkContent( module, this.focus,
							element );

					return errors;
				}
				containerInfor = container.getContainerInfo( );
			}

			return Collections.emptyList( );
		}

		/**
		 * Checks whether a type of elements can reside in the given slot of the
		 * current element. Besides the type check, it also checks the
		 * cardinality of this slot.
		 * 
		 * @param slotId
		 *            the slot id of the current element
		 * @param defn
		 *            the element definition
		 * 
		 * @return <code>true</code> if elements with the definition
		 *         <code>definition</code> can reside in the given slot.
		 *         Otherwise <code>false</code>.
		 */

		private boolean canContainInRom( IElementDefn defn )
		{
			if ( !focus.canContainInRom( defn ) )
				return false;

			// if the canContain is check for create template, then jump the
			// slot
			// count check for the operation won't change the content count, it
			// is a
			// replace operation.

			String name = defn.getName( );
			if ( ReportDesignConstants.TEMPLATE_DATA_SET.equals( name )
					|| ReportDesignConstants.TEMPLATE_REPORT_ITEM.equals( name )
					|| ReportDesignConstants.TEMPLATE_ELEMENT.equals( name ) )
				return true;

			if ( focus.getContentCount( focus.getElement( ).getRoot( ) ) > 0
					&& !focus.isContainerMultipleCardinality( ) )
				return false;

			return true;

		}

		/**
		 * Checks whether the element to insert can reside in the given module.
		 * 
		 * @param module
		 *            the root module of the element to add
		 * @param slotID
		 *            the slot ID to insert
		 * @param element
		 *            the element to insert
		 * @return false if the module is a library and the element to insert is
		 *         a template element or its content is a template element; or
		 *         the container is report design and slot is component slot and
		 *         the element to insert is a template element or its content is
		 *         a template element; otherwise true
		 */

		private boolean canContainTemplateElement( Module module,
				DesignElement element )
		{
			// if this element is a kind of template element or any its content
			// is a
			// kind of template element, return false

			IElementDefn defn = MetaDataDictionary.getInstance( ).getElement(
					ReportDesignConstants.TEMPLATE_ELEMENT );

			if ( element instanceof TemplateElement )
				return canContainTemplateElement( module, defn );

			ContentIterator contents = new ContentIterator( module, element );
			while ( contents.hasNext( ) )
			{
				DesignElement content = contents.next( );
				if ( content instanceof TemplateElement )
					return canContainTemplateElement( module, defn );
			}

			return true;
		}

		/**
		 * Checks whether the element to insert can reside in the given module.
		 * 
		 * @param module
		 *            the root module of the element to add
		 * @param slotID
		 *            the slot ID to insert
		 * @param defn
		 *            the definition of element to insert
		 * @return false if the module is a library and the element to insert is
		 *         a template element or its content is a template element; or
		 *         the container is report design and slot is component slot and
		 *         the element to insert is a template element or its content is
		 *         a template element; otherwise true
		 */

		private boolean canContainTemplateElement( Module module,
				IElementDefn defn )
		{
			// if this element is a kind of template element or any its content
			// is a
			// kind of template element, return false

			if ( defn != null
					&& defn
							.isKindOf( MetaDataDictionary
									.getInstance( )
									.getElement(
											ReportDesignConstants.TEMPLATE_ELEMENT ) ) )
			{
				// components in the design/library cannot contain template
				// elements.

				ContainerContext containerInfo = focus;
				while ( containerInfo != null )
				{
					DesignElement container = containerInfo.getElement( );
					if ( ( container instanceof Module && containerInfo
							.getSlotID( ) == IModuleModel.COMPONENT_SLOT )
							|| container instanceof Library )
						return false;
					containerInfo = container.getContainerInfo( );
				}

				if ( module instanceof Library )
					return false;
			}

			return true;
		}
	}
}