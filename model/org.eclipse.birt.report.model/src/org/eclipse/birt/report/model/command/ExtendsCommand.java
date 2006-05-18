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

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.birt.report.model.activity.AbstractElementCommand;
import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ExtendsException;
import org.eclipse.birt.report.model.api.command.ExtendsForbiddenException;
import org.eclipse.birt.report.model.api.command.InvalidParentException;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.CachedMemberRef;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.StyledElement;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.util.ContentIterator;
import org.eclipse.birt.report.model.util.ModelUtil;
import org.eclipse.birt.report.model.util.ReferenceValueUtil;

/**
 * Sets the "extends" attribute of an element.
 * 
 */

public class ExtendsCommand extends AbstractElementCommand
{

	/**
	 * Constructor.
	 * 
	 * @param module
	 *            the module
	 * @param obj
	 *            the element to modify.
	 */

	public ExtendsCommand( Module module, DesignElement obj )
	{
		super( module, obj );
	}

	/**
	 * Sets the extends attribute of an element.
	 * 
	 * @param base
	 *            the name of the new parent element, or null to clear the
	 *            extends attribute.
	 * @throws ExtendsException
	 *             if the element can not be extended or the base element is not
	 *             on component slot.
	 */

	public void setExtendsName( String base ) throws ExtendsException
	{
		base = StringUtil.trimString( base );

		// Can not set extends explicitly if the element is a virtual element
		// (inside a child) or the element already extends from another.

		if ( element.isVirtualElement( ) )
			throw new ExtendsForbiddenException(
					element,
					base,
					ExtendsForbiddenException.DESIGN_EXCEPTION_EXTENDS_FORBIDDEN );

		if ( base == null && element.getExtendsName( ) == null )
			return;

		ElementDefn metaData = (ElementDefn) element.getDefn( );

		ElementRefValue retValue = null;
		if ( base == null )
		{
			if ( !metaData.canExtend( ) )
				return;
		}
		else
		{
			// Verify that the symbol exists and is the right type.

			if ( !metaData.canExtend( ) )
				throw new ExtendsForbiddenException( element, base,
						ExtendsForbiddenException.DESIGN_EXCEPTION_CANT_EXTEND );

			ElementPropertyDefn propDefn = element
					.getPropertyDefn( IDesignElementModel.EXTENDS_PROP );

			try
			{
				retValue = (ElementRefValue) propDefn.validateValue( module,
						base );
			}
			catch ( PropertyValueException e )
			{
				assert false;
			}

		}

		// Make the change.

		doSetExtendsRefValue( retValue );
	}

	/**
	 * Does the work to set the new style with the given
	 * <code>newStyleValue</code>.
	 * 
	 * @param newStyleValue
	 *            the validated <code>ElementRefValue</code>
	 */

	private void doSetExtendsRefValue( ElementRefValue newExtendsValue )
			throws ExtendsException
	{
		if ( newExtendsValue != null )
		{
			// the input value is unresovled, try to resolve it again.

			ElementDefn metaData = (ElementDefn) element.getDefn( );
			PropertyDefn propDefn = (PropertyDefn) metaData
					.getProperty( IDesignElementModel.EXTENDS_PROP );
			int ns = metaData.getNameSpaceID( );

			DesignElement resolvedParent = null;
			Module root = module;
			if ( !metaData.canExtend( ) )
				throw new ExtendsForbiddenException( element, newExtendsValue
						.getName( ),
						ExtendsForbiddenException.DESIGN_EXCEPTION_CANT_EXTEND );

			if ( !newExtendsValue.isResolved( ) )
				resolvedParent = root.resolveElement( ReferenceValueUtil
						.needTheNamespacePrefix( newExtendsValue, module ), ns,
						propDefn );
			else
				resolvedParent = root.resolveElement( newExtendsValue
						.getElement( ), ns, propDefn );

			DesignElement parent = newExtendsValue.getElement( );
			if ( parent != null && parent != resolvedParent )
				throw new InvalidParentException(
						element,
						newExtendsValue.getName( ),
						InvalidParentException.DESIGN_EXCEPTION_PARENT_NOT_FOUND );

			if ( parent == null && resolvedParent != null )
			{
				parent = resolvedParent;
				newExtendsValue.resolve( parent );
			}
			element.checkExtends( parent );

			if ( metaData.getNameSpaceID( ) == Module.ELEMENT_NAME_SPACE )
			{
				IElementDefn moduleDefn = MetaDataDictionary.getInstance( )
						.getElement( ReportDesignConstants.MODULE_ELEMENT );

				if ( parent.getContainer( ).getDefn( ).isKindOf( moduleDefn )
						|| parent.getContainerSlot( ) != Module.COMPONENT_SLOT )
				{
					throw new ExtendsForbiddenException(
							element,
							newExtendsValue.getName( ),
							ExtendsForbiddenException.DESIGN_EXCEPTION_PARENT_NOT_IN_COMPONENT );
				}
			}
		}

		if ( newExtendsValue != null && newExtendsValue.isResolved( )
				&& newExtendsValue.getElement( ) == element.getExtendsElement( ) )
			return;

		// Make the change.

		ActivityStack stack = getActivityStack( );
		ExtendsRecord record = new ExtendsRecord( element, newExtendsValue );
		stack.startTrans( record.getLabel( ) );

		adjustUserProperties( element, newExtendsValue == null
				? null
				: newExtendsValue.getElement( ) );

		stack.execute( record );
		stack.commit( );
	}

	/**
	 * Private method to remove values for any user properties that are defined
	 * by ancestors that will no longer be visible after the change of the
	 * parent element.
	 * 
	 * @param element
	 *            the element to adjust.
	 * @param parent
	 *            the new parent element.
	 */

	private void adjustUserProperties( DesignElement element,
			DesignElement parent )
	{
		ActivityStack stack = getActivityStack( );
		DesignElement ancestor = element.getExtendsElement( );
		while ( ancestor != null && ancestor != parent )
		{
			Collection props = ancestor.getUserProperties( );
			if ( props != null )
			{
				Iterator iter = props.iterator( );
				while ( iter.hasNext( ) )
				{
					UserPropertyDefn prop = (UserPropertyDefn) iter.next( );
					if ( element.getLocalProperty( module, prop ) != null )
					{
						PropertyRecord record = new PropertyRecord( element,
								prop.getName( ), null );
						stack.execute( record );
					}
				}
			}
			ancestor = ancestor.getExtendsElement( );
		}

	}

	/**
	 * Sets the extends attribute for an element given the new parent element.
	 * 
	 * @param parent
	 *            the new parent element.
	 * @throws ExtendsException
	 *             if the element can not be extended or the base element is not
	 *             on component slot, or the base element has no name.
	 */

	public void setExtendsElement( DesignElement parent )
			throws ExtendsException
	{
		if ( parent == null )
		{
			setExtendsName( null );
			return;
		}

		String name = parent.getName( );
		if ( StringUtil.isBlank( name ) )
			throw new InvalidParentException( element, "", //$NON-NLS-1$
					InvalidParentException.DESIGN_EXCEPTION_UNNAMED_PARENT );

		Module module = parent.getRoot( );
		if ( module instanceof Library )
		{
			String namespace = ( (Library) module ).getNamespace( );
			name = StringUtil.buildQualifiedReference( namespace, name );
		}
		setExtendsName( name );
	}

	/**
	 * Sets the extends attribute for an element given the new parent element.
	 * 
	 * @param parent
	 *            the new parent element.
	 * @throws ExtendsException
	 *             if the element can not be extended or the base element is not
	 *             on component slot, or the base element has no name.
	 */

	public void setExtendsElement( DesignElementHandle parent )
			throws ExtendsException
	{
		if ( parent == null )
		{
			setExtendsName( null );
			return;
		}

		String name = parent.getName( );
		if ( StringUtil.isBlank( name ) )
			throw new InvalidParentException( element, "", //$NON-NLS-1$
					InvalidParentException.DESIGN_EXCEPTION_UNNAMED_PARENT );

		setExtendsName( ReferenceValueUtil.needTheNamespacePrefix( parent
				.getElement( ), parent.getModule( ), module ) );
	}

	/**
	 * Localize the element, break the parent/child relationship and set all the
	 * extended properties locally.
	 * 
	 * @throws SemanticException
	 * 
	 */

	public void localizeElement( ) throws SemanticException
	{
		// check parent.

		DesignElement parent = element.getExtendsElement( );
		if ( parent == null )
			throw new InvalidParentException( element, parent,
					InvalidParentException.DESIGN_EXCEPTION_NO_PARENT );

		// Sanity check structure. Parent and the child must be in the same
		// structure
		// when doing the localization.

		ContentIterator parentIter = new ContentIterator( parent );
		ContentIterator childIter = new ContentIterator( element );
		while ( parentIter.hasNext( ) )
		{
			assert childIter.hasNext( );
			DesignElement e1 = (DesignElement) parentIter.next( );
			DesignElement e2 = (DesignElement) childIter.next( );

			assert e1.getDefn( ) == e2.getDefn( );
			assert e2.getBaseId( ) == e1.getID( );
		}

		// copy properties from top level parent to the child element.

		// user properties.

		ActivityStack activityStack = getActivityStack( );
		activityStack.startTrans( );

		try
		{
			if ( parent.getDefn( ).allowsUserProperties( ) )
			{
				Iterator iter = parent.getUserProperties( ).iterator( );
				while ( iter.hasNext( ) )
				{
					UserPropertyDefn userPropDefn = (UserPropertyDefn) iter
							.next( );
					UserPropertyCommand command = new UserPropertyCommand(
							module, element );
					command.addUserProperty( userPropDefn );
				}
			}

			// Other properties.

			Iterator iter = parent.getDefn( ).getProperties( ).iterator( );
			while ( iter.hasNext( ) )
			{
				ElementPropertyDefn propDefn = (ElementPropertyDefn) iter
						.next( );
				String propName = propDefn.getName( );

				if ( !propDefn.canInherit( ) )
					continue;

				// Style property and extends property will be removed.
				// The properties inherited from style or parent will be
				// flatten to new element.

				if ( StyledElement.STYLE_PROP.equals( propName )
						|| DesignElement.EXTENDS_PROP.equals( propName )
						|| DesignElement.USER_PROPERTIES_PROP.equals( propName ) )
					continue;

				Object localValue = element.getLocalProperty( module, propDefn );
				Object parentValue = parent.getStrategy( )
						.getPropertyFromElement( module, parent, propDefn );

				if ( localValue == null && parentValue != null )
				{
					PropertyCommand command = new PropertyCommand( module,
							element );

					if ( propDefn.getTypeCode( ) == PropertyType.STRUCT_TYPE )
					{
						command.makeLocalCompositeValue( new CachedMemberRef(
								propDefn ) );
					}
					else
					{
						command.setProperty( propDefn, ModelUtil.copyValue(
								propDefn, parentValue ) );
					}
				}
			}

			// clear the extends, break the parent/child relationship.

			ExtendsCommand command = new ExtendsCommand( module, element );
			command.setExtendsElement( (DesignElement) null );
		}
		catch ( SemanticException ex )
		{
			activityStack.rollback( );
			throw ex;
		}

		// localize the content virtual elements.

		parentIter = new ContentIterator( parent );
		childIter = new ContentIterator( element );

		while ( parentIter.hasNext( ) )
		{
			DesignElement e1 = (DesignElement) parentIter.next( );
			DesignElement e2 = (DesignElement) childIter.next( );

			ElementLocalizeRecord record = new ElementLocalizeRecord( module,
					e2, e1 );
			activityStack.execute( record );
		}

		activityStack.commit( );
	}

	/**
	 * Sets the theme with the given element reference value. Call this method
	 * when the theme name or theme element has been validated.
	 * 
	 * @param refValue
	 *            the validated reference value
	 * @throws ExtendsException
	 *             if the element can not have theme or the theme is not found.
	 */

	protected void setExtendsRefValue( ElementRefValue refValue )
			throws ExtendsException
	{
		if ( refValue == null && element.getExtendsName( ) == null )
			return;

		doSetExtendsRefValue( refValue );
	}
}