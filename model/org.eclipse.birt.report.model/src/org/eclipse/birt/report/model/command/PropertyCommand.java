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
import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.command.PropertyNameException;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.IStructureDefn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.api.validators.GroupNameValidator;
import org.eclipse.birt.report.model.api.validators.StructureListValidator;
import org.eclipse.birt.report.model.core.BackRef;
import org.eclipse.birt.report.model.core.CachedMemberRef;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.MemberRef;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.ReferencableStructure;
import org.eclipse.birt.report.model.core.ReferenceableElement;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.MasterPage;
import org.eclipse.birt.report.model.elements.TemplateParameterDefinition;
import org.eclipse.birt.report.model.elements.interfaces.ICellModel;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IExtendedItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IMasterPageModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyledElementModel;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ModelMessages;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.metadata.ReferenceValue;
import org.eclipse.birt.report.model.metadata.StructPropertyDefn;
import org.eclipse.birt.report.model.util.ModelUtil;
import org.eclipse.birt.report.model.util.ReferenceValueUtil;

/**
 * Sets the value of a property. Works with both system and user properties.
 * Works with normal and intrinsic properties.
 * 
 */

public class PropertyCommand extends AbstractElementCommand
{

	/**
	 * Constructor.
	 * 
	 * @param module
	 *            the root of <code>obj</code>
	 * @param obj
	 *            the element to modify.
	 */

	public PropertyCommand( Module module, DesignElement obj )
	{
		super( module, obj );
	}

	/**
	 * Sets the value of a property.
	 * 
	 * @param propName
	 *            the internal name of the property to set.
	 * @param value
	 *            the new property value.
	 * @throws SemanticException
	 *             if the property is not found.
	 */

	public void setProperty( String propName, Object value )
			throws SemanticException
	{
		propName = StringUtil.trimString( propName );

		// Ensure that the property is defined.

		ElementPropertyDefn prop = element.getPropertyDefn( propName );
		if ( prop == null )
			throw new PropertyNameException( element, propName );

		setProperty( prop, value );
	}

	/**
	 * Sets the value of a property. If the mask of a property is "lock", throws
	 * one exception.
	 * <p>
	 * If the mask of this property has been set to "lock", no value will be
	 * set. To set the value of a property, the mask value must be "hide" or
	 * "change".
	 * 
	 * @param prop
	 *            definition of the property to set
	 * @param value
	 *            the new property value.
	 * @throws SemanticException
	 *             if the value is invalid or the property mask is "lock".
	 */

	public void setProperty( ElementPropertyDefn prop, Object value )
			throws SemanticException
	{
		if ( IExtendedItemModel.EXTENSION_NAME_PROP.equals( prop.getName( ) ) )
		{
			throw new PropertyValueException(
					element,
					IExtendedItemModel.EXTENSION_NAME_PROP,
					value,
					PropertyValueException.DESIGN_EXCEPTION_EXTENSION_SETTING_FORBIDDEN );
		}

		String mask = element.getPropertyMask( module, prop.getName( ) );
		if ( DesignChoiceConstants.PROPERTY_MASK_TYPE_LOCK
				.equalsIgnoreCase( mask ) )
		{
			throw new PropertyValueException( element, prop, value,
					PropertyValueException.DESIGN_EXCEPTION_VALUE_LOCKED );
		}

		// Within child element, properties that can cause structure change are
		// not allowed to set.

		if ( element.isVirtualElement( ) && element instanceof Cell )
		{
			String propName = prop.getName( );
			if ( ICellModel.COL_SPAN_PROP.equalsIgnoreCase( propName )
					|| ICellModel.ROW_SPAN_PROP.equalsIgnoreCase( propName )
					|| ICellModel.DROP_PROP.equalsIgnoreCase( propName )
					|| ICellModel.COLUMN_PROP.equalsIgnoreCase( propName ) )
			{
				throw new PropertyValueException(
						element,
						prop,
						value,
						PropertyValueException.DESIGN_EXCEPTION_PROPERTY_CHANGE_FORBIDDEN );
			}

		}

		if ( element instanceof MasterPage )
		{

			// Height and width are not allowed to be set if masterpage size
			// type is
			// a pre-defined type.

			String propName = prop.getName( );
			if ( !( (MasterPage) element ).isCustomType( module )
					&& ( IMasterPageModel.WIDTH_PROP.equals( propName ) || IMasterPageModel.HEIGHT_PROP
							.equals( propName ) ) )
			{
				throw new SemanticError( element,
						SemanticError.DESIGN_EXCEPTION_CANNOT_SPECIFY_PAGE_SIZE );
			}
		}

		value = validateValue( prop, value );

		if ( element instanceof GroupElement
				&& IGroupElementModel.GROUP_NAME_PROP.equals( prop.getName( ) ) )
		{
			if ( !isGroupNameValidInContext( (String) value ) )
				throw new NameException( element, (String) value,
						NameException.DESIGN_EXCEPTION_DUPLICATE );
		}

		// Set the property.

		if ( prop.isIntrinsic( ) )
		{
			setIntrinsicProperty( prop, value );
		}
		else
		{
			if ( IDesignElementModel.REF_TEMPLATE_PARAMETER_PROP.equals( prop
					.getName( ) )
					&& value == null )
				clearRefTemplateParameterProp( prop, value );
			else
				doSetProperty( prop, value );
		}
	}

	/**
	 * Remove template definition from module if the definition is no longer
	 * refferenced when setting
	 * <code>IDesignElementModel.REF_TEMPLATE_PARAMETER_PROP</code> null.
	 * 
	 * @param prop
	 *            should be
	 *            <code>IDesignElementModel.REF_TEMPLATE_PARAMETER_PROP</code>.
	 * @param value
	 *            should be null;
	 * @throws SemanticException
	 *             if any semantic exception is thrown.
	 */

	private void clearRefTemplateParameterProp( ElementPropertyDefn prop,
			Object value ) throws SemanticException
	{
		assert prop != null;
		assert IDesignElementModel.REF_TEMPLATE_PARAMETER_PROP.equals( prop
				.getName( ) );

		ActivityStack stack = module.getActivityStack( );
		stack.startTrans( );

		try
		{
			ElementRefValue templateParam = (ElementRefValue) element
					.getProperty( module, prop );
			TemplateParameterDefinition definition = (TemplateParameterDefinition) templateParam
					.getElement( );

			doSetProperty( prop, value );

			if ( definition != null && !definition.hasReferences( ) )
			{
				ContentCommand cmd = new ContentCommand( definition.getRoot( ),
						definition.getContainer( ) );
				cmd.remove( definition, definition.getContainerSlot( ) );
			}
		}
		catch ( SemanticException e )
		{
			stack.rollback( );
			throw e;
		}

		stack.commit( );
	}

	/**
	 * Private method to set property.
	 * 
	 * @param prop
	 *            the definition of the property to set.
	 * @param value
	 *            the new property value.
	 * @throws ExtendedElementException
	 *             if the extension property is invalid
	 * @throws PropertyValueException
	 *             if the element is a template element and users try to set the
	 *             value of template definition to "null" or a non-exsiting
	 *             element
	 */

	private void doSetProperty( ElementPropertyDefn prop, Object value )
			throws ExtendedElementException, PropertyValueException
	{
		// Ignore duplicate values, even if the current value is not local.
		// This avoids making local copies if the user enters the existing
		// value, or if the UI gets a bit sloppy.

		Object oldValue = element.getLocalProperty( module, prop );
		if ( oldValue == null && value == null )
			return;
		if ( oldValue != null && value != null && oldValue.equals( value ) )
			return;

		// The values differ. Make the change.

		if ( element instanceof ExtendedItem )
		{
			ExtendedItem extendedItem = ( (ExtendedItem) element );

			if ( extendedItem.isExtensionModelProperty( prop.getName( ) )
					|| extendedItem.isExtensionXMLProperty( prop.getName( ) ) )
			{
				IReportItem extElement = extendedItem.getExtendedElement( );

				assert extElement != null;

				extElement.checkProperty( prop.getName( ), value );
				extElement.setProperty( prop.getName( ), value );

				return;
			}
		}

		PropertyRecord record = new PropertyRecord( element, prop, value );
		getActivityStack( ).execute( record );

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

	private void assertExtendedElement( Module module, DesignElement element,
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
	 * Private method to validate the value of a property.
	 * 
	 * @param prop
	 *            definition of the property to validate
	 * @param value
	 *            the value to validate
	 * @return the value to store for the property
	 * @throws PropertyValueException
	 *             if the value is not valid
	 */

	private Object validateValue( ElementPropertyDefn prop, Object value )
			throws SemanticException
	{
		// clear the property doesn't needs validation.

		if ( value == null )
			return null;

		Object input = value;

		// uses the name to resolve the element name

		if ( value instanceof DesignElementHandle )
		{
			DesignElementHandle elementHandle = (DesignElementHandle) value;
			Module root = elementHandle.getModule( );

			input = ReferenceValueUtil.needTheNamespacePrefix( elementHandle
					.getElement( ), root, module );
		}

		Object retValue = null;

		try
		{
			retValue = prop.validateValue( module, input );
		}
		catch ( PropertyValueException ex )
		{
			ex.setElement( element );
			ex.setPropertyName( prop.getName( ) );
			throw ex;
		}

		if ( !( retValue instanceof ElementRefValue ) )
			return retValue;

		// if the return element and the input element is not same, throws
		// exception

		ElementRefValue refValue = (ElementRefValue) retValue;
		if ( refValue.isResolved( )
				&& value instanceof DesignElementHandle
				&& refValue.getElement( ) != ( (DesignElementHandle) value )
						.getElement( ) )
			throw new SemanticError( element, new String[]{prop.getName( ),
					refValue.getName( )},
					SemanticError.DESIGN_EXCEPTION_INVALID_ELEMENT_REF );

		return retValue;

	}

	/**
	 * Sets the value of an intrinsic property.
	 * 
	 * @param prop
	 *            definition of the property to set
	 * @param value
	 *            the property value to set.
	 * @throws SemanticException
	 *             if failed to set property.
	 */

	private void setIntrinsicProperty( ElementPropertyDefn prop, Object value )
			throws SemanticException
	{
		String propName = prop.getName( );

		if ( IDesignElementModel.NAME_PROP.equals( propName ) )
		{
			String name = (String) value;

			NameCommand cmd = new NameCommand( module, element );
			cmd.setName( name );
		}
		else if ( IDesignElementModel.EXTENDS_PROP.equals( propName ) )
		{
			ExtendsCommand cmd = new ExtendsCommand( module, element );
			cmd.setExtendsRefValue( (ElementRefValue) value );
		}
		else if ( IStyledElementModel.STYLE_PROP.equals( propName ) )
		{
			// the value must be a type of ElementRefValue or null

			StyleCommand cmd = new StyleCommand( module, element );
			cmd.setStyleRefValue( (ElementRefValue) value );
		}
		else if ( IModuleModel.UNITS_PROP.equals( propName ) )
		{
			doSetProperty( prop, value );
		}
		else if ( IExtendedItemModel.EXTENSION_NAME_PROP.equals( propName ) )
		{
			doSetProperty( prop, value );
		}
		else if ( IModuleModel.THEME_PROP.equals( propName ) )
		{
			assert module == element;

			ThemeCommand cmd = new ThemeCommand( (Module) element );
			cmd.setThemeRefValue( (ElementRefValue) value );
		}
		else
		{
			// Other intrinsics properties will be added here.

			assert false;
		}
	}

	/**
	 * Checks whether the name is valid in the context.
	 * 
	 * @param name
	 *            the new name
	 * @return <code>true</code> if the name is valid. Otherwise
	 *         <code>false</code>.
	 */

	private boolean isGroupNameValidInContext( String groupName )
	{
		assert element instanceof GroupElement;

		if ( groupName == null )
			return true;

		if ( element.getContainer( ) != null )
		{
			DesignElement tmpContainer = element.getContainer( );

			List errors = GroupNameValidator.getInstance( )
					.validateForRenamingGroup(
							(ListingHandle) tmpContainer.getHandle( module ),
							(GroupHandle) element.getHandle( module ),
							groupName );

			if ( !errors.isEmpty( ) )
				return false;
		}

		return true;
	}

	/**
	 * Clears the value of a property.
	 * 
	 * @param propName
	 *            the name of the property to clear.
	 * @throws SemanticException
	 *             if failed to clear property.
	 */

	public void clearProperty( String propName ) throws SemanticException
	{
		setProperty( propName, null );
	}

	/**
	 * Sets the value of the member of a structure.
	 * 
	 * @param ref
	 *            reference to the member to set
	 * @param value
	 *            new value of the member
	 * @throws SemanticException
	 *             if the value is not valid
	 */

	public void setMember( MemberRef ref, Object value )
			throws SemanticException
	{
		PropertyDefn memberDefn = ref.getMemberDefn( );
		PropertyDefn propDefn = ref.getPropDefn( );
		assert propDefn != null;
		assertExtendedElement( module, element, propDefn );

		assert memberDefn != null;
		value = memberDefn.validateValue( module, value );

		// if set the value to the name of a structure, must ensure this
		// would not create duplicates.

		if ( memberDefn.getTypeCode( ) == IPropertyType.NAME_TYPE
				|| memberDefn.getTypeCode( ) == IPropertyType.MEMBER_KEY_TYPE )
			checkItemName( ref, (String) value );

		if ( value instanceof ElementRefValue
				&& memberDefn.getTypeCode( ) == IPropertyType.ELEMENT_REF_TYPE )
		{
			checkRecursiveElementReference( memberDefn, (ElementRefValue) value );
		}

		// Ignore duplicate values, even if the current value is not local.
		// This avoids making local copies if the user enters the existing
		// value, or if the UI gets a bit sloppy.

		Object oldValue = ref.getLocalValue( module, element );
		if ( oldValue == null && value == null )
			return;
		if ( oldValue != null && value != null && oldValue.equals( value ) )
			return;

		// The values differ. Make the change.

		ActivityStack stack = getActivityStack( );

		String label = ModelMessages
				.getMessage( MessageConstants.CHANGE_ITEM_MESSAGE );
		stack.startTrans( label );

		makeLocalCompositeValue( ref );

		MemberRecord record = new MemberRecord( module, element, ref, value );
		stack.execute( record );

		Structure structure = ref.getStructure( module, element );
		List semanticList = structure.validate( module, element );
		if ( semanticList.size( ) > 0 )
		{
			stack.rollback( );
			throw (SemanticException) semanticList.get( 0 );
		}

		stack.commit( );

	}

	/**
	 * Adds an item to a structure list.
	 * <ul>
	 * <li>If the property is currently unset anywhere up the inheritance
	 * hierarchy, then a new list is created on this element, and the list
	 * contains the only the new item.</li>
	 * <li>If the property is currently set on this element, then the item is
	 * added to the existing list.</li>
	 * <li>If the list is not set on this element, but is set by an ancestor
	 * element, then the list is <strong>copied </strong> onto this element, and
	 * the new element is then appended to the copy.</li>
	 * </ul>
	 * 
	 * @param ref
	 *            reference to the list into which to add the structure
	 * @param item
	 *            the structure to add to the list
	 * @throws SemanticException
	 *             if the item to add is invalid.
	 */

	public void addItem( MemberRef ref, IStructure item )
			throws SemanticException
	{
		assert ref != null;

		if ( item == null )
			return;

		PropertyDefn propDefn = ref.getPropDefn( );
		assert propDefn != null;
		assertExtendedElement( module, element, propDefn );

		if ( item.isReferencable( ) )
			assert !( (ReferencableStructure) item ).hasReferences( );

		checkListMemberRef( ref );
		checkItem( ref, item );

		List list = ref.getList( module, element );
		element.checkStructureList( module, ref.getPropDefn( ), list, item );

		ActivityStack stack = getActivityStack( );
		stack.startTrans( ModelMessages
				.getMessage( MessageConstants.ADD_ITEM_MESSAGE ) );

		makeLocalCompositeValue( ref );
		list = ref.getList( module, element );
		if ( null == list )
		{
			list = new ArrayList( );
			MemberRecord memberRecord = new MemberRecord( module, element, ref,
					list );
			stack.execute( memberRecord );
		}

		MemberRef insertRef = new CachedMemberRef( ref, list.size( ) );
		PropertyListRecord record = new PropertyListRecord( element, insertRef,
				insertRef.getList( module, element ), item );
		stack.execute( record );
		stack.commit( );

	}

	/**
	 * Inserts an item to a structure list.
	 * <ul>
	 * <li>If the property is currently unset anywhere up the inheritance
	 * hierarchy, then a new list is created on this element, and the list
	 * contains the only the new item.</li>
	 * <li>If the property is currently set on this element, then the item is
	 * inserted into the existing list.</li>
	 * <li>If the list is not set on this element, but is set by an ancestor
	 * element, then the list is <strong>copied </strong> onto this element, and
	 * the new element is then inserted into the copy.</li>
	 * </ul>
	 * 
	 * @param ref
	 *            reference to the list into which to insert the new item
	 * @param item
	 *            the item to insert
	 * @param posn
	 *            the position at which to insert the item
	 * @throws SemanticException
	 *             if the item to add is invalid.
	 * @throws IndexOutOfBoundsException
	 *             if the given posn is out of range
	 *             <code>(index &lt; 0 || index &gt; list.size())</code>.
	 */

	public void insertItem( MemberRef ref, IStructure item, int posn )
			throws SemanticException
	{
		assert ref != null;

		if ( item == null )
			return;

		PropertyDefn propDefn = ref.getPropDefn( );
		assert propDefn != null;
		assertExtendedElement( module, element, propDefn );

		checkListMemberRef( ref );
		checkItem( ref, item );

		List list = ref.getList( module, element );
		element.checkStructureList( module, ref.getPropDefn( ), list, item );

		ActivityStack stack = getActivityStack( );

		stack.startTrans( ModelMessages
				.getMessage( MessageConstants.INSERT_ITEM_MESSAGE ) );

		makeLocalCompositeValue( ref );
		list = ref.getList( module, element );
		if ( null == list )
		{
			list = new ArrayList( );
			MemberRecord memberRecord = new MemberRecord( module, element, ref,
					list );
			stack.execute( memberRecord );
		}

		if ( posn < 0 || posn > list.size( ) )
			throw new IndexOutOfBoundsException(
					"Posn: " + posn + ", List Size: " + list.size( ) ); //$NON-NLS-1$//$NON-NLS-2$

		MemberRef insertRef = new CachedMemberRef( ref, posn );
		PropertyListRecord record = new PropertyListRecord( element, insertRef,
				list, item );

		stack.execute( record );
		stack.commit( );
	}

	/**
	 * Removes an item from a structure list.
	 * <ul>
	 * <li>The element must exist in the current effective value for the list.
	 * This means the list must be set on this element or a ancestor element.
	 * </li>
	 * <li>If the property is set on this element, then the element is simply
	 * removed.</li>
	 * <li>If the property is set on an ancestor element, then the inherited
	 * list is first <strong>copied </strong> into this element. Then, the copy
	 * of the target item is removed from the copy of the list.</li>
	 * </ul>
	 * 
	 * @param ref
	 *            reference to the list in which to remove an item.
	 * @param posn
	 *            position of the item to be removed from the list.
	 * @throws PropertyValueException
	 *             if the item to remove is not found.
	 * @throws IndexOutOfBoundsException
	 *             if the given posn is out of range
	 *             <code>(index &lt; 0 || index &gt;= list.size())</code>.
	 */

	public void removeItem( MemberRef ref, int posn )
			throws PropertyValueException
	{
		assert ref != null;

		PropertyDefn propDefn = ref.getPropDefn( );
		assert propDefn != null;
		assertExtendedElement( module, element, propDefn );

		checkListMemberRef( ref );

		List list = ref.getList( module, element );
		if ( list == null )
			throw new PropertyValueException( element, ref.getPropDefn( ),
					null,
					PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND );

		if ( posn < 0 || posn >= list.size( ) )
			throw new IndexOutOfBoundsException(
					"Posn: " + posn + ", List Size: " + list.size( ) ); //$NON-NLS-1$//$NON-NLS-2$

		doRemoveItem( new CachedMemberRef( ref, posn ) );
	}

	/**
	 * Removes an item from a structure list.
	 * <ul>
	 * <li>The element must exist in the current effective value for the list.
	 * This means the list must be set on this element or a ancestor element.
	 * </li>
	 * <li>If the property is set on this element, then the element is simply
	 * removed.</li>
	 * <li>If the property is set on an ancestor element, then the inherited
	 * list is first <strong>copied </strong> into this element. Then, the copy
	 * of the target item is removed from the copy of the list.</li>
	 * </ul>
	 * 
	 * @param ref
	 *            the structure list reference
	 * @param structure
	 *            the item to remove
	 * @throws PropertyValueException
	 *             if the item to remove is not found.
	 */

	public void removeItem( MemberRef ref, IStructure structure )
			throws PropertyValueException
	{
		PropertyDefn propDefn = ref.getPropDefn( );
		assert propDefn != null;
		assertExtendedElement( module, element, propDefn );

		checkListMemberRef( ref );
		List list = ref.getList( module, element );
		if ( list == null )
			throw new PropertyValueException( element, ref.getPropDefn( ),
					null,
					PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND );

		int posn = list.indexOf( structure );
		if ( posn == -1 )
			throw new PropertyValueException( element, ref.getPropDefn( )
					.getName( ), null,
					PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND );

		doRemoveItem( new CachedMemberRef( ref, posn ) );
	}

	/**
	 * Removes structure from structure list.
	 * 
	 * @param structRef
	 *            reference to the item to remove
	 */

	private void doRemoveItem( MemberRef structRef )
	{
		String label = ModelMessages
				.getMessage( MessageConstants.REMOVE_ITEM_MESSAGE );

		ActivityStack stack = module.getActivityStack( );
		stack.startTrans( label );

		makeLocalCompositeValue( structRef );
		List list = structRef.getList( module, element );
		assert list != null;

		Structure struct = structRef.getStructure( module, element );
		if ( struct.isReferencable( ) )
			adjustReferenceClients( (ReferencableStructure) struct );

		// handle the structure member refers to other elements.

		adjustReferenceClients( struct, structRef );

		PropertyListRecord record = new PropertyListRecord( element, structRef,
				list );
		stack.execute( record );
		stack.commit( );
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

	private void adjustReferenceClients( ReferencableStructure struct )
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

	private void adjustReferenceClients( Structure referred, MemberRef memberRef )
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
	 * Replaces an item from a structure list with the new one.
	 * <ul>
	 * <li>The element must exist in the effective value for the list. This
	 * means the list must be set on this element or a ancestor element.</li>
	 * <li>If the property is set on this element, then the element is simply
	 * replaced</li>
	 * <li>If the property is set on an ancestor element, then the inherited
	 * list is first copied into this element. Then, the copy of the target item
	 * is removed from the copy of the list.</li>
	 * </ul>
	 * 
	 * @param ref
	 *            The structure list reference.
	 * @param oldItem
	 *            the old item to be replaced
	 * @param newItem
	 *            the new item reference.
	 * @throws SemanticException
	 *             if the old item is not found or this property type is not
	 *             structure list.
	 */

	public void replaceItem( MemberRef ref, IStructure oldItem,
			IStructure newItem ) throws SemanticException
	{
		assert ref != null;

		PropertyDefn propDefn = ref.getPropDefn( );
		assert propDefn != null;
		assertExtendedElement( module, element, propDefn );

		checkListMemberRef( ref );

		List list = ref.getList( module, element );
		if ( list == null )
			throw new PropertyValueException( element, ref.getPropDefn( ),
					null,
					PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND );

		if ( newItem != null )
		{
			checkItem( ref, newItem );
			element.checkStructureList( module, ref.getPropDefn( ), list,
					newItem );
		}

		ActivityStack stack = module.getActivityStack( );

		stack.startTrans( ModelMessages
				.getMessage( MessageConstants.REPLACE_ITEM_MESSAGE ) );

		makeLocalCompositeValue( ref );
		list = ref.getList( module, element );
		assert list != null;

		int index = list.indexOf( oldItem );
		if ( index == -1 )
			throw new PropertyValueException( element, ref.getPropDefn( )
					.getName( ), oldItem,
					PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND );

		PropertyReplaceRecord record = new PropertyReplaceRecord( element, ref,
				list, index, newItem );
		stack.execute( record );

		if ( oldItem.isReferencable( ) )
			adjustReferenceClients( (ReferencableStructure) oldItem );

		stack.commit( );
	}

	/**
	 * Removes all contents of the list. This is different from simply clearing
	 * the property. Removing all the contents leaves the property set to an
	 * empty list.
	 * 
	 * @param ref
	 *            reference to the list to clear
	 * @throws SemanticException
	 *             if the property is not a structure list property
	 */

	public void removeAllItems( MemberRef ref ) throws SemanticException
	{
		checkListMemberRef( ref );

		PropertyDefn propDefn = ref.getPropDefn( );
		assert propDefn != null;
		assertExtendedElement( module, element, propDefn );

		if ( ref.refType == MemberRef.PROPERTY )
			setProperty( ref.getPropDefn( ), null );
		else
			setMember( ref, null );
	}

	/**
	 * Moves an item within a list from one position to a new position.
	 * <ul>
	 * <li>The element must exist in the current effective value for the list.
	 * This means the list must be set on this element or a ancestor element.
	 * </li>
	 * <li>If the property is set on this element, then the element is simply
	 * moved.</li>
	 * <li>If the property is set on an ancestor element, then the inherited
	 * list is first <strong>copied </strong> into this element. Then, the copy
	 * of the target item is moved within the copy of the list.</li>
	 * </ul>
	 * 
	 * <p>
	 * For example, if a list has A, B, C structures in order, when move A
	 * strucutre to <code>newPosn</code> with the value 2, the sequence
	 * becomes B, A, C.
	 * 
	 * 
	 * @param ref
	 *            reference to the list in which to do the move the item.
	 * @param oldPosn
	 *            the old position of the item.
	 * @param newPosn
	 *            new position of the item. Note that the range of
	 *            <code>to</code> is from 0 to the number of strucutres in the
	 *            list.
	 * 
	 * @throws PropertyValueException
	 *             if the property is not a structure list property, or the list
	 *             value is not set.
	 * @throws IndexOutOfBoundsException
	 *             if the given from or to index is out of range
	 *             <code>(index &lt; 0 || index &gt;= list.size())</code>.
	 */

	public void moveItem( MemberRef ref, int oldPosn, int newPosn )
			throws PropertyValueException
	{
		assert ref != null;

		PropertyDefn propDefn = ref.getPropDefn( );
		assert propDefn != null;
		checkListMemberRef( ref );

		List list = ref.getList( module, element );
		if ( list == null )
			throw new PropertyValueException( element, ref.getPropDefn( ),
					null,
					PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND );

		ActivityStack stack = getActivityStack( );
		String label = ModelMessages
				.getMessage( MessageConstants.MOVE_ITEM_MESSAGE );

		int adjustedNewPosn = checkAndAdjustPosition( oldPosn, newPosn, list
				.size( ) );
		if ( oldPosn == adjustedNewPosn )
			return;

		stack.startTrans( label );

		makeLocalCompositeValue( ref );
		list = ref.getList( module, element );
		assert list != null;

		MoveListItemRecord record = new MoveListItemRecord( element, ref, list,
				oldPosn, adjustedNewPosn );

		stack.execute( record );
		stack.commit( );
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
	 * Check to see whether the reference points to a list.
	 * 
	 * @param ref
	 *            reference to the list into which to add the structure
	 * @throws PropertyValueException
	 *             if the <code>ref</code> doesn't refer a list property or
	 *             member.
	 */

	private void checkListMemberRef( MemberRef ref )
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

	private void checkItem( MemberRef ref, IStructure item )
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
	 * Adds an item to a property list.
	 * <ul>
	 * <li>If the property is currently unset anywhere up the inheritance
	 * hierarchy, then a new list is created on this element, and the list
	 * contains the only the new item.</li>
	 * <li>If the property is currently set on this element, then the item is
	 * added to the existing list.</li>
	 * <li>If the list is not set on this element, but is set by an ancestor
	 * element, then the list is <strong>copied </strong> onto this element, and
	 * the new element is then appended to the copy.</li>
	 * </ul>
	 * 
	 * @param prop
	 *            the property definition whose type is list
	 * @param item
	 *            the item to add to the list
	 * @throws PropertyValueException
	 *             if the item to add is invalid.
	 */

	public void addItem( ElementPropertyDefn prop, Object item )
			throws PropertyValueException
	{
		assert prop != null;

		if ( item == null )
			return;

		// this method is not called for structure list property

		assert !( item instanceof IStructure );

		assertExtendedElement( module, element, prop );

		// check the property type is list and do some validation about the item

		checkListProperty( prop );

		Object value = checkItem( prop, item );

		// check whether the value in the list is unique when the sub-type is
		// element reference value

		List list = element.getListProperty( module, prop.getName( ) );
		element.checkSimpleList( module, prop, list, value );

		ActivityStack stack = getActivityStack( );
		stack.startTrans( ModelMessages
				.getMessage( MessageConstants.ADD_ITEM_MESSAGE ) );

		makeLocalCompositeValue( prop );
		list = element.getListProperty( module, prop.getName( ) );
		if ( null == list )
		{
			list = new ArrayList( );
			PropertyRecord propRecord = new PropertyRecord( element, prop, list );
			stack.execute( propRecord );
		}

		SimplePropertyListRecord record = new SimplePropertyListRecord(
				element, prop, list, value, list.size( ) );
		stack.execute( record );

		if ( value instanceof ElementRefValue )
		{
			ElementRefValue refValue = (ElementRefValue) value;
			if ( refValue.isResolved( ) )
			{
				ElementRefRecord refRecord = new ElementRefRecord( element,
						refValue.getTargetElement( ), prop.getName( ), true );
				stack.execute( refRecord );
			}
		}

		stack.commit( );
	}

	/**
	 * Check to see whether the reference points to a list.
	 * 
	 * @param prop
	 *            the property definition to check whether it is list type
	 * @throws PropertyValueException
	 *             if the property definition is not a list type
	 */

	private void checkListProperty( ElementPropertyDefn prop )
			throws PropertyValueException
	{
		if ( prop.getTypeCode( ) != IPropertyType.LIST_TYPE )
			throw new PropertyValueException( element, prop, null,
					PropertyValueException.DESIGN_EXCEPTION_NOT_LIST_TYPE );
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

	private Object checkItem( ElementPropertyDefn prop, Object item )
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

	void makeLocalCompositeValue( ElementPropertyDefn prop )
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
	 * Removes a value item from a simple value list.
	 * <ul>
	 * <li>The item must exist in the current effective value for the list.
	 * This means the list must be set on this element or a ancestor element.
	 * </li>
	 * <li>If the property is set on this element, then the item is simply
	 * removed.</li>
	 * <li>If the property is set on an ancestor element, then the inherited
	 * list is first <strong>copied </strong> into this element. Then, the copy
	 * of the target item is removed from the copy of the list.</li>
	 * </ul>
	 * 
	 * @param prop
	 *            definition of the simple value list property
	 * @param posn
	 *            position of the item to be removed from the list.
	 * @throws PropertyValueException
	 *             if the item to remove is not found.
	 * @throws IndexOutOfBoundsException
	 *             if the given posn is out of range
	 *             <code>(index &lt; 0 || index &gt;= list.size())</code>.
	 */

	public void removeItem( ElementPropertyDefn prop, int posn )
			throws PropertyValueException
	{
		assert prop != null;

		// TODO: if the property is "style", jump to the style command

		assertExtendedElement( module, element, prop );

		checkListProperty( prop );

		List list = element.getListProperty( module, prop.getName( ) );
		if ( list == null )
			throw new PropertyValueException( element, prop.getName( ), null,
					PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND );

		if ( posn < 0 || posn >= list.size( ) )
			throw new IndexOutOfBoundsException(
					"Posn: " + posn + ", List Size: " + list.size( ) ); //$NON-NLS-1$//$NON-NLS-2$

		Object obj = list.get( posn );

		doRemoveItem( prop, posn, obj );
	}

	/**
	 * Removes a value item from a simple value list.
	 * <ul>
	 * <li>The item must exist in the current effective value for the list.
	 * This means the list must be set on this element or a ancestor element.
	 * </li>
	 * <li>If the property is set on this element, then the item is simply
	 * removed.</li>
	 * <li>If the property is set on an ancestor element, then the inherited
	 * list is first <strong>copied </strong> into this element. Then, the copy
	 * of the target item is removed from the copy of the list.</li>
	 * </ul>
	 * 
	 * @param prop
	 *            definition of the simple value list property
	 * @param item
	 *            the item to be removed from the list.
	 * @throws SemanticException
	 *             if the item to remove is not found.
	 * @throws IndexOutOfBoundsException
	 *             if the given posn is out of range
	 *             <code>(index &lt; 0 || index &gt;= list.size())</code>.
	 */

	public void removeItem( ElementPropertyDefn prop, Object item )
			throws SemanticException
	{
		assert prop != null;

		if ( item == null )
			return;

		assertExtendedElement( module, element, prop );

		checkListProperty( prop );

		Object value = null;

		if ( prop.getSubTypeCode( ) == IPropertyType.ELEMENT_REF_TYPE )
			value = checkItem( prop, item );
		else
			value = item;

		List list = element.getListProperty( module, prop.getName( ) );
		if ( list == null )
			throw new PropertyValueException( element, prop, null,
					PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND );

		int posn = list.indexOf( value );
		if ( posn == -1 )
			throw new PropertyValueException( element, prop.getName( ), null,
					PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND );

		doRemoveItem( prop, posn, value );
	}

	/**
	 * Removes structure from structure list.
	 * 
	 * @param structRef
	 *            reference to the item to remove
	 */

	private void doRemoveItem( ElementPropertyDefn prop, int posn, Object item )
	{
		String label = ModelMessages
				.getMessage( MessageConstants.REMOVE_ITEM_MESSAGE );

		ActivityStack stack = module.getActivityStack( );
		stack.startTrans( label );

		makeLocalCompositeValue( prop );
		List list = element.getListProperty( module, prop.getName( ) );
		assert list != null;

		SimplePropertyListRecord record = new SimplePropertyListRecord(
				element, prop, list, posn );
		stack.execute( record );

		if ( item instanceof ElementRefValue )
		{
			ElementRefValue refValue = (ElementRefValue) item;
			if ( refValue.isResolved( ) )
			{
				ElementRefRecord refRecord = new ElementRefRecord( element,
						refValue.getTargetElement( ), prop.getName( ), false );
				stack.execute( refRecord );

			}
		}

		stack.commit( );
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

	private void checkItemName( MemberRef memberRef, String newName )
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
	 * Checks whethere recursive element reference occurs.
	 * 
	 * @param memberDefn
	 *            the property/member definition
	 * @param refValue
	 *            the element reference value
	 * @throws SemanticException
	 */

	private void checkRecursiveElementReference( PropertyDefn memberDefn,
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
}