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

import java.util.List;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.command.PropertyNameException;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.elements.structures.TOC;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.api.validators.GroupNameValidator;
import org.eclipse.birt.report.model.core.CachedMemberRef;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.MemberRef;
import org.eclipse.birt.report.model.core.Module;
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
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyledElementModel;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ModelMessages;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.ReferenceValueUtil;

/**
 * Sets the value of a property. Works with both system and user properties.
 * Works with normal and intrinsic properties.
 * 
 */

public class PropertyCommand extends AbstractPropertyCommand
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
		// Backward for TOC expression.

		String propName = prop.getName( );
		if ( ( IReportItemModel.TOC_PROP.equals( propName ) || IGroupElementModel.TOC_PROP
				.equals( propName ) )
				&& ( value instanceof String ) )
		{
			Object oldValue = element.getLocalProperty( module, prop );
			if ( oldValue != null )
			{
				MemberRef ref = new CachedMemberRef( prop, TOC.TOC_EXPRESSION );
				setMember( ref, value );
				return;
			}

			value = StructureFactory.createTOC( (String) value );
		}

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
			propName = prop.getName( );
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

			propName = prop.getName( );
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
						definition.getContainerInfo( ) );
				cmd.remove( definition );
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
}