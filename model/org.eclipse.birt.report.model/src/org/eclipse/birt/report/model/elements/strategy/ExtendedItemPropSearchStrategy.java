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

package org.eclipse.birt.report.model.elements.strategy;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.extension.IStyleDeclaration;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.PropertySearchStrategy;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.MultiViews;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.SystemPropertyDefn;

/**
 * Provides the specific property searching route for <code>ExtendedItem</code>.
 */

public class ExtendedItemPropSearchStrategy
		extends
			ReportItemPropSearchStrategy
{

	/**
	 * Data binding properties for the report items.
	 */

	protected final static Set<Integer> hostViewRelatedProps;

	static
	{
		Set<Integer> tmpSet = new HashSet<Integer>( );

		// add all section style properties
		tmpSet.add( new Integer( IStyleModel.DISPLAY_PROP.hashCode( ) ) );
		tmpSet.add( new Integer( IStyleModel.MASTER_PAGE_PROP.hashCode( ) ) );
		tmpSet
				.add( new Integer( IStyleModel.PAGE_BREAK_BEFORE_PROP
						.hashCode( ) ) );
		tmpSet
				.add( new Integer( IStyleModel.PAGE_BREAK_AFTER_PROP.hashCode( ) ) );
		tmpSet
				.add( new Integer( IStyleModel.PAGE_BREAK_INSIDE_PROP
						.hashCode( ) ) );
		tmpSet.add( new Integer( IStyleModel.SHOW_IF_BLANK_PROP.hashCode( ) ) );

		// add: toc, bookmark
		tmpSet.add( new Integer( IReportItemModel.TOC_PROP.hashCode( ) ) );
		tmpSet.add( new Integer( IReportItemModel.BOOKMARK_PROP.hashCode( ) ) );

		// add: visibility rules
		tmpSet
				.add( new Integer( IReportItemModel.VISIBILITY_PROP.hashCode( ) ) );

		// add: allExport
		tmpSet
				.add( new Integer( IReportItemModel.ALLOW_EXPORT_PROP
						.hashCode( ) ) );

		// add: zIndex
		tmpSet.add( new Integer( IReportItemModel.Z_INDEX_PROP.hashCode( ) ) );

		hostViewRelatedProps = Collections.unmodifiableSet( tmpSet );
	}

	private final static ExtendedItemPropSearchStrategy instance = new ExtendedItemPropSearchStrategy( );

	/**
	 * Protected constructor.
	 */

	protected ExtendedItemPropSearchStrategy( )
	{
	}

	/**
	 * Returns the instance of <code>ExtendedItemPropSearchStrategy</code> which
	 * provide the specific property searching route for
	 * <code>ExtendedItem</code>.
	 * 
	 * @return the instance of <code>ExtendedItemPropSearchStrategy</code>
	 */

	public static PropertySearchStrategy getInstance( )
	{
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.core.PropertySearchStrategy#
	 * getPropertyFromSelfSelector(org.eclipse.birt.report.model.core.Module,
	 * org.eclipse.birt.report.model.core.DesignElement,
	 * org.eclipse.birt.report.model.metadata.ElementPropertyDefn)
	 */

	protected Object getPropertyFromSelfSelector( Module module,
			DesignElement element, ElementPropertyDefn prop )
	{
		ExtendedItem extendedItem = (ExtendedItem) element;
		Object value = null;

		// find the selector defined in extension definition
		IElementDefn elementDefn = extendedItem.getExtDefn( );
		if ( elementDefn != null )
		{
			String selector = extendedItem.getExtDefn( ).getSelector( );
			value = getPropertyFromSelector( module, prop, selector );
			if ( value != null )
				return value;
		}

		// find other pre-defined styles, such as selector : x-tab header, x-tab
		// detail, it has the highest priority than other selector

		return getPropertyFromPredefinedStyles( module, extendedItem, prop );

		// "extended-item" selector is not enabled in ROM. There is no need to
		// search this selector.
	}

	/**
	 * Gets the property value from some predefined-styles in this extended
	 * item. Such as x-tab header, x-tab footer.
	 * 
	 * @param module
	 * @param extendedItem
	 * @param prop
	 * @return
	 */

	private Object getPropertyFromPredefinedStyles( Module module,
			ExtendedItem extendedItem, ElementPropertyDefn prop )
	{

		List predefinedStyles = extendedItem
				.getReportItemDefinedSelectors( module );

		if ( predefinedStyles == null || predefinedStyles.isEmpty( ) )
			return null;
		for ( int i = 0; i < predefinedStyles.size( ); i++ )
		{
			Object predefinedStyle = predefinedStyles.get( i );

			// if the item is String, then search the named style in the
			// module and then find property value in it
			if ( predefinedStyle instanceof String )
			{
				String styleName = (String) predefinedStyle;
				Object value = getPropertyFromSelector( module, prop, styleName );
				if ( value != null )
					return value;
			}
			else if ( predefinedStyle instanceof IStyleDeclaration )
			{
				// if the item is a StyleHandle, then read local property
				// value set in this style directly
				IStyleDeclaration style = (IStyleDeclaration) predefinedStyle;
				Object value = style.getProperty( prop.getName( ) );
				if ( value != null )
				{
					// do some validation for the value
					try
					{
						value = prop.validateValue( module, value );
						if ( value != null )
							return value;
					}
					catch ( PropertyValueException e )
					{
						// do nothing
					}
				}
			}
			else
				assert false;
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.elements.strategy.ReportItemPropSearchStrategy
	 * #getPropertyFromSelf(org.eclipse.birt.report.model.core.Module,
	 * org.eclipse.birt.report.model.core.DesignElement,
	 * org.eclipse.birt.report.model.metadata.ElementPropertyDefn)
	 */

	protected Object getPropertyFromSelf( Module module, DesignElement element,
			ElementPropertyDefn prop )
	{
		if ( !( element.getContainer( ) instanceof MultiViews ) )
			return super.getPropertyFromSelf( module, element, prop );

		String propName = prop.getName( );
		if ( !isDataBindingProperty( element, propName )
				&& !isHostViewProperty( element, propName ) )
			return super.getPropertyFromSelf( module, element, prop );

		DesignElement grandContainer = element.getContainer( ).getContainer( );
		if ( grandContainer == null )
			return super.getPropertyFromSelf( module, element, prop );

		return grandContainer.getProperty( module, prop );
	}

	/**
	 * Returns properties that are bound to data related values.
	 * 
	 * @param tmpElement
	 *            the design element
	 * @return a set containing property names in string
	 */

	private static Set<Integer> getHostViewProperties( DesignElement tmpElement )
	{
		if ( tmpElement instanceof ReportItem )
			return hostViewRelatedProps;

		return Collections.EMPTY_SET;

	}

	/**
	 * Checks if the property is the host view property.
	 * 
	 * @param element
	 *            the design element.
	 * @param propName
	 *            the property name.
	 * @return true if the property is the host view property, otherwise return
	 *         false.
	 */
	public static boolean isHostViewProperty( DesignElement element,
			String propName )
	{
		if ( !( element instanceof ExtendedItem )
				|| StringUtil.isBlank( propName ) )
			return false;
		return getHostViewProperties( element ).contains(
				new Integer( propName.hashCode( ) ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.core.PropertySearchStrategy#
	 * getNonIntrinsicPropertyFromElement
	 * (org.eclipse.birt.report.model.core.Module,
	 * org.eclipse.birt.report.model.core.DesignElement,
	 * org.eclipse.birt.report.model.metadata.ElementPropertyDefn)
	 */

	protected Object getNonIntrinsicPropertyFromElement( Module module,
			DesignElement element, ElementPropertyDefn prop )
	{
		ExtendedItem extendedItem = (ExtendedItem) element;

		// find useOwnModel property.

		ElementPropertyDefn propDefn = extendedItem.getPropertyDefn( prop
				.getName( ) );
		if ( propDefn == null )
			return null;

		if ( !propDefn.enableContextSearch( ) )
			return super.getNonIntrinsicPropertyFromElement( module, element,
					prop );

		IReportItem reportItem = extendedItem.getExtendedElement( );
		if ( reportItem != null )
			return reportItem.getProperty( prop.getName( ) );

		return null;
	}

	/**
	 * Returns the factory property value for the overridden property by the
	 * extension.
	 * 
	 * @param module
	 *            the module
	 * @param element
	 *            the element
	 * @param prop
	 *            the property definition
	 * @return the property value
	 */

	public Object getMetaFactoryProperty( Module module, DesignElement element,
			ElementPropertyDefn prop )
	{
		assert ( prop.isSystemProperty( ) && ( (SystemPropertyDefn) prop )
				.enableContextSearch( ) );
		return super.getNonIntrinsicPropertyFromElement( module, element, prop );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.PropertySearchStrategy#getStyleContainer
	 * (org.eclipse.birt.report.model.core.DesignElement)
	 */

	protected DesignElement getStyleContainer( DesignElement designElement )
	{
		DesignElement tmpContainer = designElement.getContainer( );
		if ( tmpContainer == null || !( tmpContainer instanceof MultiViews ) )
			return tmpContainer;

		assert tmpContainer instanceof MultiViews;
		tmpContainer = tmpContainer.getContainer( );

		if ( tmpContainer == null )
			return null;

		return tmpContainer.getContainer( );

	}
}
