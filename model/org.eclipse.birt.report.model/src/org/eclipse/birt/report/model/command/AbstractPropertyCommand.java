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

import org.eclipse.birt.report.model.activity.AbstractElementCommand;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.IStructureDefn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.validators.StructureListValidator;
import org.eclipse.birt.report.model.core.BackRef;
import org.eclipse.birt.report.model.core.CachedMemberRef;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.MemberRef;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.ReferencableStructure;
import org.eclipse.birt.report.model.core.ReferenceableElement;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.metadata.ReferenceValue;
import org.eclipse.birt.report.model.metadata.StructPropertyDefn;
import org.eclipse.birt.report.model.util.ModelUtil;

/**
 * Abstract property command to do all property value change operations.
 */

abstract public class AbstractPropertyCommand extends AbstractElementCommand
{

	/**
	 * Constructor.
	 * 
	 * @param module
	 *            the root of <code>obj</code>
	 * @param obj
	 *            the element to modify.
	 */

	public AbstractPropertyCommand( Module module, DesignElement obj )
	{
		super( module, obj );
	}

	/**
	 * Justifies whether the extended element is created if the UI invokes some
	 * operations to change the extension properties.
	 * 
	 * Note that <code>PropertyCommand</code> do not support structure
	 * operations like addItem, removeItem, etc. for extension elements. This
	 * method is kept but NERVER call it in <code>doSetProperty</code>.
	 * 
	 * @param module
	 *            the module
	 * @param element
	 *            the extended item that holds the extended element
	 * @param prop
	 *            the extension property definition to change
	 */

	protected void assertExtendedElement( Module module, DesignElement element,
			PropertyDefn prop )
	{
		if ( element instanceof ExtendedItem )
		{
			ExtendedItem extendedItem = (ExtendedItem) element;
			if ( extendedItem.isExtensionModelProperty( prop.getName( ) )
					|| extendedItem.isExtensionXMLProperty( prop.getName( ) ) )
			{
				assert ( (ExtendedItem) element ).getExtendedElement( ) != null;
			}
		}
	}

	/**
	 * Check to see whether the reference points to a list.
	 * 
	 * @param ref
	 *            reference to the list into which to add the structure
	 * @throws PropertyValueException
	 *             if the <code>ref</code> doesn't refer a list property or
	 *             member.
	 */

	protected void checkListMemberRef( MemberRef ref )
			throws PropertyValueException
	{
		if ( !ref.isListRef( ) )
			throw new PropertyValueException( element, ref.getPropDefn( ),
					null, PropertyValueException.DESIGN_EXCEPTION_NOT_LIST_TYPE );
	}

	/**
	 * Validates the values of the item members.
	 * 
	 * @param ref
	 *            reference to a list.
	 * @param item
	 *            the item to check
	 * @throws SemanticException
	 *             if the item has any member with invalid value or if the given
	 *             structure is not of a valid type that can be contained in the
	 *             list.
	 */

	protected void checkItem( MemberRef ref, IStructure item )
			throws SemanticException
	{
		assert item != null;

		PropertyDefn propDefn = null;

		if ( ref.refType == MemberRef.PROPERTY )
		{
			propDefn = ref.getPropDefn( );
			if ( item.getDefn( ) != propDefn.getStructDefn( ) )
			{
				throw new PropertyValueException( element, propDefn, item,
						PropertyValueException.DESIGN_EXCEPTION_WRONG_ITEM_TYPE );
			}
		}
		else
		{
			propDefn = ref.getMemberDefn( );
			if ( item.getDefn( ) != propDefn.getStructDefn( ) )
			{
				throw new PropertyValueException( element, ref.getPropDefn( ),
						propDefn, item,
						PropertyValueException.DESIGN_EXCEPTION_WRONG_ITEM_TYPE );
			}

		}

		for ( Iterator iter = item.getDefn( ).propertiesIterator( ); iter
				.hasNext( ); )
		{
			PropertyDefn memberDefn = (PropertyDefn) iter.next( );
			if ( ReferencableStructure.LIB_REFERENCE_MEMBER.equals( memberDefn
					.getName( ) ) )
				continue;

			Object value = ( (Structure) item ).getLocalProperty( module,
					memberDefn );

			// if the user calls Structure.setProperty(), the string element
			// name will be saved as ElementRefValue. So, need to resolve it as
			// string again since ElementRefPropertyType do not accept element
			// reference value

			if ( value instanceof ElementRefValue
					&& memberDefn.getTypeCode( ) == IPropertyType.ELEMENT_REF_TYPE )
			{
				ElementRefValue refValue = (ElementRefValue) value;
				value = memberDefn.validateValue( module, refValue
						.getQualifiedReference( ) );

				checkRecursiveElementReference( memberDefn,
						(ElementRefValue) value );
			}
			else
				value = memberDefn.validateValue( module, value );

			item.setProperty( memberDefn, value );
		}

		if ( item instanceof Structure )
		{
			List errorList = ( (Structure) item ).validate( module, element );
			if ( errorList.size( ) > 0 )
			{
				throw (SemanticException) errorList.get( 0 );
			}
		}

	}

	/**
	 * Adjusts references to a structure that is to be deleted. The structure to
	 * be deleted is one that has references in the form of structure reference
	 * properties on other elements. These other elements, called "clients",
	 * each contain a property of type structure reference and that property
	 * refers to this structure. Each reference is recorded with a "back
	 * pointer" from the referenced structure to the client. That back pointer
	 * has both a pointer to the client element, and the property within that
	 * element that holds the reference. The reference property is unresolved.
	 * 
	 * @param struct
	 *            the structure to be deleted
	 */

	protected void adjustReferenceClients( ReferencableStructure struct )
	{
		assert struct != null;
		if ( !struct.hasReferences( ) )
			return;

		List clients = new ArrayList( struct.getClientList( ) );

		Iterator iter = clients.iterator( );
		while ( iter.hasNext( ) )
		{
			BackRef ref = (BackRef) iter.next( );
			DesignElement client = ref.element;

			BackRefRecord record = new StructBackRefRecord( module, struct,
					client, ref.propName );
			getActivityStack( ).execute( record );

		}
	}

	/**
	 * Checks whethere recursive element reference occurs.
	 * 
	 * @param memberDefn
	 *            the property/member definition
	 * @param refValue
	 *            the element reference value
	 * @throws SemanticException
	 */

	protected void checkRecursiveElementReference( PropertyDefn memberDefn,
			ElementRefValue refValue ) throws SemanticException
	{
		assert refValue != null;

		if ( refValue.isResolved( ) && element instanceof ReferenceableElement )
		{
			DesignElement reference = refValue.getElement( );
			if ( ModelUtil.isRecursiveReference( reference,
					(ReferenceableElement) element ) )

				throw new SemanticError(
						element,
						new String[]{reference.getIdentifier( )},
						SemanticError.DESIGN_EXCEPTION_CIRCULAR_ELEMENT_REFERNECE );
		}

	}

	/**
	 * Validates the values of the item members.
	 * 
	 * @param ref
	 *            reference to a list.
	 * @param item
	 *            the item to check
	 * @throws SemanticException
	 *             if the item has any member with invalid value or if the given
	 *             structure is not of a valid type that can be contained in the
	 *             list.
	 */

	protected void checkItemName( MemberRef memberRef, String newName )
			throws SemanticException
	{
		PropertyDefn propDefn = memberRef.getPropDefn( );

		Structure structure = memberRef.getStructure( module, element );

		List errors = StructureListValidator.getInstance( )
				.validateForRenaming( element.getHandle( module ), propDefn,
						memberRef.getList( module, element ), structure,
						memberRef.getMemberDefn( ), newName );

		if ( errors.size( ) > 0 )
		{
			throw (PropertyValueException) errors.get( 0 );
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

	protected void adjustReferenceClients( Structure referred,
			MemberRef memberRef )
	{
		IStructureDefn structDefn = referred.getDefn( );
		Iterator memberDefns = structDefn.getPropertyIterator( );

		while ( memberDefns.hasNext( ) )
		{
			StructPropertyDefn memberDefn = (StructPropertyDefn) memberDefns
					.next( );
			if ( memberDefn.getTypeCode( ) != IPropertyType.ELEMENT_REF_TYPE )
				continue;

			ReferenceValue refValue = (ReferenceValue) referred
					.getLocalProperty( module, memberDefn );

			if ( refValue == null || !refValue.isResolved( ) )
				continue;

			ReferenceableElement client = (ReferenceableElement) ( (ElementRefValue) refValue )
					.getElement( );

			DesignElement referenceElement = referred.getContextElement( );
			String propName = referred.getContextPropertyName( );

			BackRefRecord record = new ElementBackRefRecord( module, client,
					referenceElement, propName, new CachedMemberRef( memberRef,
							memberDefn ) );
			getActivityStack( ).execute( record );
		}
	}

	/**
	 * The top level element property referenced by a member reference can be a
	 * list property or a structure property.
	 * <p>
	 * <li>If references a list property, the method will check to see if the
	 * current element has the local list value, if it has, the method returns,
	 * otherwise, a copy of the list value inherited from container or parent
	 * will be set locally on the element itself.
	 * <li>If references a structure property, the method will check to see if
	 * the current element has the local structure value, if it has, the method
	 * returns, otherwise, a copy of the structure value inherited from
	 * container or parent will be set locally on the element itself.
	 * <p>
	 * This method is supposed to be used when we need to change the value of a
	 * composite property( a list property or a structure property ). These kind
	 * of property is inherited as a whole, so when the value changed from a
	 * child element. This method will be called to ensure that a local copy
	 * will be made, so change to the child won't affect the original value in
	 * the parent.
	 * 
	 * @param ref
	 *            a reference to a list property or member.
	 */

	void makeLocalCompositeValue( MemberRef ref )
	{
		assert ref != null;
		ElementPropertyDefn propDefn = ref.getPropDefn( );

		if ( ref.getPropDefn( ).isList( ) )
		{
			// Top level property is a list.

			ArrayList list = (ArrayList) element.getLocalProperty( module,
					propDefn );

			if ( list != null )
				return;
			// Make a local copy of the inherited list value.

			ArrayList inherited = (ArrayList) element.getProperty( module,
					propDefn );

			list = new ArrayList( );
			if ( inherited != null )
			{
				for ( int i = 0; i < inherited.size( ); i++ )
					list.add( ( (IStructure) inherited.get( i ) ).copy( ) );
			}

			// Set the list value on the element itself.

			PropertyRecord propRecord = new PropertyRecord( element, propDefn,
					list );
			getActivityStack( ).execute( propRecord );
			return;
		}

		// Top level property is a structure.

		Structure struct = (Structure) element.getLocalProperty( module,
				propDefn );

		if ( struct != null )
			return;

		// Make a local copy of the inherited list value.

		Structure inherited = (Structure) element
				.getProperty( module, propDefn );

		if ( inherited != null )
		{
			IStructure copy = inherited.copy( );
			PropertyRecord propRecord = new PropertyRecord( element, propDefn,
					copy );
			getActivityStack( ).execute( propRecord );
		}

		return;
	}
	
	/**
	 * The property is a simple value list. If property is a list property, the
	 * method will check to see if the current element has the local list value,
	 * if it has, the method returns, otherwise, a copy of the list value
	 * inherited from container or parent will be set locally on the element
	 * itself.
	 * <p>
	 * This method is supposed to be used when we need to change the value of a
	 * composite property( a simple list property ). These kind of property is
	 * inherited as a whole, so when the value changed from a child element.
	 * This method will be called to ensure that a local copy will be made, so
	 * change to the child won't affect the original value in the parent.
	 * 
	 * @param ref
	 *            a reference to a list property or member.
	 */

	protected void makeLocalCompositeValue( ElementPropertyDefn prop )
	{
		assert prop != null;

		// Top level property is a list.

		ArrayList list = (ArrayList) element.getLocalProperty( module, prop );

		if ( list != null )
			return;

		// Make a local copy of the inherited list value.

		ArrayList inherited = (ArrayList) element.getProperty( module, prop );

		Object value = ModelUtil.copyValue( prop, inherited );

		// Set the list value on the element itself.

		PropertyRecord propRecord = new PropertyRecord( element, prop, value );
		getActivityStack( ).execute( propRecord );
		return;

	}
	
	/**
	 * Validates the values of the item members.
	 * 
	 * @param ref
	 *            reference to a list.
	 * @param item
	 *            the item to check
	 * @throws PropertyValueException
	 *             if the item has any member with invalid value or if the given
	 *             structure is not of a valid type that can be contained in the
	 *             list.
	 */

	protected Object checkItem( PropertyDefn prop, Object item )
			throws PropertyValueException
	{
		assert item != null;
		assert prop.getTypeCode( ) == IPropertyType.LIST_TYPE;
		Object value = item;
		if ( item instanceof DesignElementHandle )
			value = ( (DesignElementHandle) item ).getElement( );

		// make use of the sub-type to get the validated value

		PropertyType type = prop.getSubType( );
		assert type != null;
		Object result = type.validateValue( module, prop, value );
		// if ( result instanceof ElementRefValue
		// && !( (ElementRefValue) result ).isResolved( ) )
		// {
		// throw new SemanticError( element,
		// SemanticError.DESIGN_EXCEPTION_INVALID_ELEMENT_REF );
		// }
		return result;

	}
}
