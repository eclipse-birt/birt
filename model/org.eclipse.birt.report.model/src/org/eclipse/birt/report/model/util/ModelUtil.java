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

package org.eclipse.birt.report.model.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.report.model.activity.LayoutRecordTask;
import org.eclipse.birt.report.model.activity.NotificationRecordTask;
import org.eclipse.birt.report.model.activity.RecordTask;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.LibraryException;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.api.elements.structures.IncludedLibrary;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.util.ElementExportUtil;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.api.util.UnicodeUtil;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.core.StyledElement;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.elements.interfaces.IExtendedItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IOdaExtendableElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.extension.IExtendableElement;
import org.eclipse.birt.report.model.i18n.ModelMessages;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.metadata.ReferenceValue;
import org.eclipse.birt.report.model.parser.DesignParserException;
import org.xml.sax.SAXException;

import com.ibm.icu.text.CollationKey;
import com.ibm.icu.text.Collator;
import com.ibm.icu.util.SimpleTimeZone;
import com.ibm.icu.util.ULocale;

/**
 * The utility class which provides many static methods used in Model.
 */

public class ModelUtil
{

	/**
	 * Returns the id reference relationship between the parent element and the
	 * child element.
	 * <p>
	 * Notice: the element and its parent should have the same structure when
	 * calling this method. That is the child structure has already been
	 * refreshed from parent.
	 * 
	 * @param element
	 *            the element to setup the id reference
	 * @return a map to store the base id and the corresponding child element.
	 */

	public static Map getIdMap( DesignElement element )
	{
		assert element != null;

		// Parent and the child must have the same structures.

		DesignElement parent = element.getExtendsElement( );
		if ( parent == null )
			return Collections.EMPTY_MAP;

		Map idMap = new HashMap( );

		Iterator parentIter = new ContentIterator( parent );
		Iterator childIter = new ContentIterator( element );
		while ( childIter.hasNext( ) )
		{
			DesignElement virtualParent = (DesignElement) parentIter.next( );
			DesignElement virtualChild = (DesignElement) childIter.next( );

			assert virtualChild.getDefn( ).getName( ) == virtualChild.getDefn( )
					.getName( );
			assert virtualParent.getID( ) > 0;

			idMap.put( new Long( virtualParent.getID( ) ), virtualChild );
		}

		return idMap;
	}

	/**
	 * Break the relationship between the given element to its parent.Set all
	 * properties values of the given element on the element locally. The
	 * following properties will be set:
	 * <ul>
	 * <li>Properties set on element itself
	 * <li>Inherited from style or element's selector style
	 * <li>Inherited from parent
	 * </ul>
	 * 
	 * @param element
	 *            the element to be localized.
	 */

	public static void localizeElement( DesignElement element )
	{
		assert element != null;
		DesignElement parent = element.getExtendsElement( );
		if ( parent == null )
			return;

		duplicateProperties( parent, element );

		ContentIterator iter1 = new ContentIterator( parent );
		ContentIterator iter2 = new ContentIterator( element );

		while ( iter1.hasNext( ) )
		{
			DesignElement virtualParent = (DesignElement) iter1.next( );
			DesignElement virtualChild = (DesignElement) iter2.next( );

			duplicateProperties( virtualParent, virtualChild );
		}
	}

	/**
	 * Duplicates some properties in a design element when to export it.
	 * 
	 * @param from
	 *            the from element to get the property values
	 * @param to
	 *            the to element to duplicate the property values
	 */

	private static void duplicateProperties( DesignElement from,
			DesignElement to )
	{
		if ( from.getDefn( ).allowsUserProperties( ) )
		{
			Iterator iter = from.getUserProperties( ).iterator( );
			while ( iter.hasNext( ) )
			{
				UserPropertyDefn userPropDefn = (UserPropertyDefn) iter.next( );
				to.addUserPropertyDefn( userPropDefn );
			}
		}

		Iterator iter = from.getDefn( ).getProperties( ).iterator( );
		while ( iter.hasNext( ) )
		{
			ElementPropertyDefn propDefn = (ElementPropertyDefn) iter.next( );
			String propName = propDefn.getName( );

			// Style property and extends property will be removed.
			// The properties inherited from style or parent will be
			// flatten to new element.

			if ( StyledElement.STYLE_PROP.equals( propName )
					|| DesignElement.EXTENDS_PROP.equals( propName )
					|| DesignElement.USER_PROPERTIES_PROP.equals( propName )
					|| DesignElement.REF_TEMPLATE_PARAMETER_PROP
							.equals( propName ) )
				continue;

			Object localValue = to.getLocalProperty( from.getRoot( ), propDefn );
			Object parentValue = from.getStrategy( ).getPropertyFromElement(
					from.getRoot( ), from, propDefn );

			if ( localValue == null && parentValue != null )
			{
				Object valueToSet = ModelUtil.copyValue( propDefn, parentValue );
				to.setProperty( propDefn, valueToSet );
			}
		}
	}

	/**
	 * Duplicates the properties from source element to destination element.
	 * Source and the destination element should be of the same type. The
	 * following properties will be duplicated:
	 * <ul>
	 * <li>Properties set on element itself
	 * <li>Inherited from style or element's selector style
	 * <li>Inherited from parent
	 * </ul>
	 * 
	 * @param source
	 *            handle of the source element
	 * @param destination
	 *            handle of the destination element
	 * @param onlyFactoryProperty
	 *            indicate whether only factory property values are duplicated.
	 */

	public static void duplicateProperties( DesignElementHandle source,
			DesignElementHandle destination, boolean onlyFactoryProperty )
	{
		assert source != null;
		assert destination != null;

		if ( !( ( source instanceof ReportDesignHandle ) && ( destination instanceof LibraryHandle ) ) )
			assert destination.getDefn( ).getName( ).equalsIgnoreCase(
					source.getDefn( ).getName( ) );

		if ( source.getDefn( ).allowsUserProperties( ) )
		{
			PropertyHandle propHandle = source
					.getPropertyHandle( DesignElement.USER_PROPERTIES_PROP );

			Object value = source.getElement( ).getUserProperties( );

			Object valueToSet = null;
			if ( propHandle != null )
			{
				valueToSet = ModelUtil.copyValue( propHandle.getDefn( ), value );
			}

			if ( valueToSet != null )
			{
				Iterator iter = ( (List) valueToSet ).iterator( );
				while ( iter.hasNext( ) )
				{
					UserPropertyDefn userPropDefn = (UserPropertyDefn) iter
							.next( );
					destination.getElement( )
							.addUserPropertyDefn( userPropDefn );
				}
			}
		}

		if ( source.getElement( ) instanceof IExtendableElement )
			duplicateExtensionIdentifier( source, destination );

		Iterator iter = source.getPropertyIterator( );

		while ( iter.hasNext( ) )
		{
			PropertyHandle propHandle = (PropertyHandle) iter.next( );

			String propName = propHandle.getDefn( ).getName( );

			// Style property and extends property will be removed.
			// The properties inherited from style or parent will be
			// flatten to new element.

			if ( StyledElement.STYLE_PROP.equals( propName )
					|| DesignElement.EXTENDS_PROP.equals( propName )
					|| DesignElement.USER_PROPERTIES_PROP.equals( propName )
					|| IOdaExtendableElementModel.EXTENSION_ID_PROP
							.equals( propName )
					|| IExtendedItemModel.EXTENSION_NAME_PROP.equals( propName )
					|| DesignElement.REF_TEMPLATE_PARAMETER_PROP
							.equals( propName )
					|| DesignElement.VIEW_ACTION_PROP.equals( propName ) )
				continue;

			ElementPropertyDefn propDefn = destination.getElement( )
					.getPropertyDefn( propName );

			if ( propDefn == null )
				continue;

			Object value = null;

			// the special case for the toc, pageBreakAfter and pageBreakBefore
			// properties on the group element

			// for toc the default value is the group expression.

			if ( propHandle.getElement( ) instanceof GroupElement
					&& ( GroupElement.TOC_PROP.equals( propName )
							|| IStyleModel.PAGE_BREAK_AFTER_PROP
									.equals( propName ) || IStyleModel.PAGE_BREAK_BEFORE_PROP
							.equals( propName ) ) )
				value = propHandle.getElement( ).getLocalProperty(
						propHandle.getModule( ), propDefn );
			else if ( onlyFactoryProperty )
				value = propHandle.getElement( ).getFactoryProperty(
						propHandle.getModule( ), propDefn );
			else if ( Module.IMAGES_PROP.equals( propName ) )
			{
				// Copy the embedded images
				Iterator images = source.getPropertyHandle( Module.IMAGES_PROP )
						.iterator( );
				while ( images.hasNext( ) )
				{
					StructureHandle image = (StructureHandle) images.next( );
					try
					{
						ElementExportUtil.exportStructure( image,
								(LibraryHandle) destination, false );
					}
					catch ( SemanticException e )
					{
						assert false;
					}
				}
				continue;
			}
			else
				value = propHandle.getElement( ).getStrategy( )
						.getPropertyExceptRomDefault( propHandle.getModule( ),
								propHandle.getElement( ), propDefn );

			Object valueToSet = ModelUtil.copyValue( propHandle.getDefn( ),
					value );

			destination.getElement( ).setProperty( propName, valueToSet );

		}
	}

	/**
	 * Duplicates the extension identifier. The extension identifier must be set
	 * before copy other property values. If the identifier is not set first,
	 * extension property definitions cannot be found. Hence, duplicating
	 * property values cannot be right.
	 * <p>
	 * The extension identifier is:
	 * <ul>
	 * <li>EXTENSION_ID_PROP for Oda elements.
	 * <li>EXTENSION_NAME_PROP for extension elements like chart.
	 * </ul>
	 * 
	 * @param source
	 *            handle of the source element
	 * @param destination
	 *            handle of the destination element
	 */

	private static void duplicateExtensionIdentifier(
			DesignElementHandle source, DesignElementHandle destination )
	{

		// for the special oda cases, the extension id must be set before
		// copy properties. Otherwise, destination cannot find its ODA
		// properties.

		if ( source.getElement( ) instanceof IOdaExtendableElementModel )
		{
			String extensionId = (String) source
					.getProperty( IOdaExtendableElementModel.EXTENSION_ID_PROP );

			destination.getElement( ).setProperty(
					IOdaExtendableElementModel.EXTENSION_ID_PROP, extensionId );
		}
		else

		if ( source.getElement( ) instanceof IExtendedItemModel )
		{
			String extensionId = (String) source
					.getProperty( IExtendedItemModel.EXTENSION_NAME_PROP );

			destination.getElement( ).setProperty(
					IExtendedItemModel.EXTENSION_NAME_PROP, extensionId );
		}
		else
		{
			assert false;
		}

	}

	/**
	 * Clone the structure list, a list value contains a list of
	 * <code>IStructure</code>.
	 * 
	 * @param list
	 *            The structure list to be cloned.
	 * @return The cloned structure list.
	 */

	private static ArrayList cloneStructList( List list )
	{
		if ( list == null )
			return null;

		ArrayList returnList = new ArrayList( );
		for ( int i = 0; i < list.size( ); i++ )
		{
			Object item = list.get( i );
			if ( item instanceof IStructure )
			{
				returnList.add( ( (IStructure) item ).copy( ) );
			}
			else
			{
				assert false;
			}
		}
		return returnList;
	}

	/**
	 * Clones the value.
	 * <ul>
	 * <li>If the value is of simple type, like integer, or string, the
	 * original value will be returned.
	 * <li>If the value is strcuture list, the cloned structure list will be
	 * cloned.
	 * <li>If the value is structure, the cloned structure will be cloned.
	 * <li>If the value is element/strucuture reference value, the
	 * element/structure name will be returned.
	 * </ul>
	 * 
	 * @param propDefn
	 *            definition of property
	 * @param value
	 *            value to clone
	 * @return new value
	 */

	public static Object copyValue( IPropertyDefn propDefn, Object value )
	{

		if ( value == null || propDefn == null )
			return null;

		switch ( propDefn.getTypeCode( ) )
		{
			case PropertyType.STRUCT_TYPE :

				if ( propDefn.isList( ) )
					return ModelUtil.cloneStructList( (List) value );

				return ( (Structure) value ).copy( );

			case PropertyType.ELEMENT_REF_TYPE :
			case PropertyType.STRUCT_REF_TYPE :

				ReferenceValue refValue = (ReferenceValue) value;
				return refValue.copy( );

			case PropertyType.LIST_TYPE :
				return ModelUtil.clonePropertyList( (List) value );
		}

		return value;
	}

	/**
	 * Copies a list of simple property values.
	 * 
	 * @param value
	 *            the original value to copy
	 * @return the cloned list of simple property values
	 */

	private static Object clonePropertyList( List value )
	{
		if ( value == null )
			return null;

		ArrayList returnList = new ArrayList( );
		for ( int i = 0; i < value.size( ); i++ )
		{
			Object item = value.get( i );
			if ( item instanceof ElementRefValue )
			{
				returnList.add( ( (ElementRefValue) item ).copy( ) );
			}
			else
			{
				returnList.add( item );
			}
		}
		return returnList;
	}

	/**
	 * Filtrates the notification tasks.
	 * 
	 * @param tasks
	 *            the notification tasks
	 * @return a list contained filtrated notification tasks
	 */

	public static List filterNotificationTasks( List tasks )
	{
		List notifications = new ArrayList( );
		for ( int i = 0; i < tasks.size( ); i++ )
		{
			RecordTask task = (RecordTask) tasks.get( i );
			if ( task instanceof NotificationRecordTask )
				notifications.add( task );
		}

		return EventFilter.getInstance( ).filter( notifications );
	}

	/**
	 * Filtrates the table layout tasks.
	 * 
	 * @param tasks
	 *            the table layout tasks
	 * @return a list contained filtrated table layout tasks
	 */

	public static List filterLayoutTasks( List tasks )
	{
		List retList = new ArrayList( );
		Set elements = new LinkedHashSet( );

		for ( int i = 0; i < tasks.size( ); i++ )
		{
			RecordTask task = (RecordTask) tasks.get( i );

			if ( task instanceof LayoutRecordTask )
			{
				DesignElement compoundElement = (DesignElement) ( (LayoutRecordTask) task )
						.getTarget( );
				if ( !elements.contains( compoundElement ) )
				{
					retList.add( task );
					elements.add( compoundElement );
				}
			}
		}

		return retList;
	}

	/**
	 * Returns the first fatal exception from the given exception list. The
	 * fatal exception means the error should be forwarded to the outer-most
	 * host module and stops opening module.
	 * 
	 * @param list
	 *            the exception list
	 * @return the fatal exception, otherwise, return null.
	 */

	public static Exception getFirstFatalException( List list )
	{
		Iterator iter = list.iterator( );
		while ( iter.hasNext( ) )
		{
			Exception ex = (Exception) iter.next( );
			if ( ex instanceof XMLParserException )
			{
				XMLParserException parserException = (XMLParserException) ex;
				if ( parserException.getException( ) instanceof LibraryException )
				{
					String errorCode = ( (LibraryException) parserException
							.getException( ) ).getErrorCode( );

					if ( errorCode == LibraryException.DESIGN_EXCEPTION_LIBRARY_INCLUDED_RECURSIVELY
							|| errorCode == LibraryException.DESIGN_EXCEPTION_DUPLICATE_LIBRARY_NAMESPACE )
					{
						return parserException.getException( );
					}
				}
			}
		}

		return null;
	}

	/**
	 * Checks whether the input stream has a compatible encoding signature with
	 * BIRT. Currently, BIRT only supports UTF-8 encoding.
	 * 
	 * @param inputStream
	 *            the input stream to check
	 * @param fileName
	 *            the design file name
	 * @return the signature from the UTF files.
	 * @throws IOException
	 *             if errors occur during opening the design file
	 * @throws SAXException
	 *             if the stream has unexpected encoding signature
	 */

	public static String checkUTFSignature( InputStream inputStream,
			String fileName ) throws IOException, SAXException
	{

		// This may fail if there are a lot of space characters before the end
		// of the encoding declaration

		String encoding = UnicodeUtil.checkUTFSignature( inputStream );

		if ( encoding != null && !UnicodeUtil.SIGNATURE_UTF_8.equals( encoding ) )
		{
			Exception cause = new DesignParserException(
					DesignParserException.DESIGN_EXCEPTION_UNSUPPORTED_ENCODING );
			Exception fileException = new DesignFileException( fileName, cause );

			throw new SAXException( fileException );
		}

		return encoding;
	}

	/**
	 * 
	 * Performs property name sorting on a list of properties. Properties
	 * returned are sorted by their (locale-specific) display name. The name for
	 * sorting is assumed to be "groupName.displayName" in which "groupName" is
	 * the localized name of the property group, if any; and "displayName" is
	 * the localized name of the property. That is, properties without groups
	 * sort by their property display names. Properties with groups sort first
	 * by group name within the overall list, then by property name within the
	 * group. Sorting in English ignores case.
	 * <p>
	 * For example, if we have the groups "G" and "R", and the properties
	 * "alpha", "G.beta", "G.sigma", "iota", "R.delta", "R.epsilon" and "theta",
	 * the Properties returned is assumed to be sorted into that order.
	 * 
	 * Sorts a list of <code>PropertyDefn</code> s by there localized name.
	 * Uses <code>Collator</code> to do the comparison, sorting in English
	 * ignores case.
	 * 
	 * @param propDefns
	 *            a list that contains PropertyDefns.
	 * @return the list of <code>PropertyDefn</code> s that is sorted by their
	 *         display name.
	 */

	public static List sortPropertiesByLocalizedName( List propDefns )
	{
		// Use the static factory method, getInstance, to obtain the appropriate
		// Collator object for the current
		// locale.

		// The Collator instance that performs locale-sensitive String
		// comparison.

		ULocale locale = ThreadResources.getLocale( );
		Collator collator = Collator.getInstance( locale );

		// Sorting in English should ignore case.
		if ( Locale.ENGLISH.equals( locale ) )
		{

			// Set Collator strength value as PRIMARY, only PRIMARY differences
			// are considered significant during comparison. The assignment of
			// strengths to language features is locale defendant. A common
			// example is for different base letters ("a" vs "b") to be
			// considered a PRIMARY difference.

			collator.setStrength( Collator.PRIMARY );
		}

		final Map keysMap = new HashMap( );
		for ( int i = 0; i < propDefns.size( ); i++ )
		{
			PropertyDefn propDefn = (PropertyDefn) propDefns.get( i );

			// Transforms the String into a series of bits that can be compared
			// bitwise to other CollationKeys.
			// CollationKeys provide better performance than Collator.

			CollationKey key = collator.getCollationKey( propDefn
					.getDisplayName( ) );
			keysMap.put( propDefn, key );
		}

		Collections.sort( propDefns, new Comparator( ) {

			public int compare( Object o1, Object o2 )
			{
				PropertyDefn p1 = (PropertyDefn) o1;
				PropertyDefn p2 = (PropertyDefn) o2;

				CollationKey key1 = (CollationKey) keysMap.get( p1 );
				CollationKey key2 = (CollationKey) keysMap.get( p2 );

				// Comparing two CollationKeys returns the relative order of the
				// Strings they represent. Using CollationKeys to compare
				// Strings is generally faster than using Collator.compare.

				return key1.compareTo( key2 );
			}
		} );

		return propDefns;
	}

	/**
	 * Sorts a list of element by their internal names.
	 * 
	 * @param elements
	 *            a list of <code>DesignElementHandle</code>
	 * @return a sorted list of element.
	 */

	public static List sortElementsByName( List elements )
	{
		List temp = new ArrayList( elements );
		Collections.sort( temp, new Comparator( ) {

			public int compare( Object o1, Object o2 )
			{
				DesignElementHandle handle1 = (DesignElementHandle) o1;
				DesignElementHandle handle2 = (DesignElementHandle) o2;

				String name1 = handle1.getName( );
				String name2 = handle2.getName( );

				if ( null == name1 )
				{
					if ( null == name2 )
						return 0;

					return -1;
				}

				// name1 != null

				if ( null == name2 )
				{
					return 1;
				}

				return name1.compareTo( name2 );
			}

		} );

		return temp;
	}

	/**
	 * Inserts a default theme to the library slot.
	 * 
	 * @param library
	 *            the target library
	 * @param theme
	 *            the theme to insert
	 */

	public static void insertCompatibleThemeToLibrary( Library library,
			Theme theme )
	{
		assert library != null;
		assert theme != null;

		// The name should not be null if it is required. The parser state
		// should have already caught this case.

		String name = theme.getName( );
		assert !StringUtil.isBlank( name )
				&& ModelMessages.getMessage( Theme.DEFAULT_THEME_NAME ).equals(
						name );

		NameSpace ns = library.getNameSpace( Library.THEME_NAME_SPACE );
		assert library.getModuleNameSpace( Library.THEME_NAME_SPACE )
				.canContain( name );

		ns.insert( theme );

		// Add the item to the container.

		library.getSlot( Library.THEMES_SLOT ).add( theme );

		// Cache the inverse relationship.

		theme.setContainer( library, Library.THEMES_SLOT );
	}

	/**
	 * Uses the new name space of the current module for reference property
	 * values of the given element. This method checks the <code>content</code>
	 * and nested elements in it.
	 * 
	 * @param module
	 *            the module that <code>content</code> attaches.
	 * @param content
	 *            the element to revise
	 * @param nameSpace
	 *            the new name space
	 */

	public static void reviseNameSpace( Module module, DesignElement content,
			String nameSpace )
	{

		List propDefns = content.getPropertyDefns( );
		for ( int i = 0; i < propDefns.size( ); i++ )
		{
			ElementPropertyDefn propDefn = (ElementPropertyDefn) propDefns
					.get( i );
			if ( propDefn.getTypeCode( ) != IPropertyType.ELEMENT_REF_TYPE )
				continue;

			Object value = content.getLocalProperty( module, propDefn );
			if ( value == null )
				continue;

			ReferenceValue refValue = (ReferenceValue) value;
			refValue.setLibraryNamespace( nameSpace );
		}

		IElementDefn defn = content.getDefn( );

		for ( int i = 0; i < defn.getSlotCount( ); i++ )
		{
			ContainerSlot slot = content.getSlot( i );

			if ( slot != null )
				for ( int pos = 0; pos < slot.getCount( ); pos++ )
					reviseNameSpace( module, slot.getContent( pos ), nameSpace );
		}
	}

	/**
	 * Determines whether there is a child in the given element, which is kind
	 * of the given element definition.
	 * 
	 * @param element
	 *            the element to find
	 * @param defn
	 *            the element definition type
	 * @return true if there is a child in the element whose type is the given
	 *         definition, otherwise false
	 */

	public static boolean containElement( DesignElement element,
			IElementDefn defn )
	{
		if ( element == null || defn == null )
			return false;

		// Check contents.

		int count = element.getDefn( ).getSlotCount( );
		for ( int i = 0; i < count; i++ )
		{
			Iterator iter = element.getSlot( i ).iterator( );
			while ( iter.hasNext( ) )
			{
				DesignElement e = (DesignElement) iter.next( );
				IElementDefn targetDefn = e.getDefn( );

				if ( defn.isKindOf( targetDefn ) )
					return true;

				if ( containElement( e, defn ) )
					return true;
			}
		}

		return false;
	}

	/**
	 * Determines whether there is a child in the given element, which is kind
	 * of the given element definition.
	 * 
	 * @param element
	 *            the element to find
	 * @param elementName
	 *            the element definition type
	 * @return true if there is a child in the element whose type is the given
	 *         definition, otherwise false
	 */

	public static boolean containElement( DesignElement element,
			String elementName )
	{
		IElementDefn defn = MetaDataDictionary.getInstance( ).getElement(
				elementName );
		return containElement( element, defn );
	}

	/**
	 * Sets default time zone. Wrote this method becuase of bugs in ICUs.
	 */

	public static void setDefaultTimeZone( )
	{
		java.util.TimeZone defaultTimeZone = java.util.TimeZone.getDefault( );
		SimpleTimeZone pdt = new SimpleTimeZone(
				defaultTimeZone.getRawOffset( ), defaultTimeZone.getID( ) );
		SimpleTimeZone.setDefault( pdt );
	}

	/**
	 * Gets the copy of the given element.
	 * 
	 * @param element
	 *            the element to copy
	 * @return the copy of the element
	 */

	public static DesignElement getCopy( DesignElement element )
	{
		if ( element == null )
			return null;

		try
		{
			DesignElement copy = (DesignElement) element.clone( );
			assert copy != null;
			return copy;
		}
		catch ( CloneNotSupportedException e )
		{
			assert false;
			return null;
		}
	}

	/**
	 * Returns externalized message. If There is no externalized message, return
	 * null.
	 * 
	 * @param element
	 *            Design element.
	 * @param propName
	 *            Name of property
	 * @return externalized message.
	 */

	private static String searchForExternalizedValue( DesignElement element,
			String propIDName )
	{
		while ( element != null )
		{
			Module root = element.getRoot( );
			String textKey = (String) element.getLocalProperty( root,
					propIDName );
			if ( !StringUtil.isBlank( textKey ) )
			{
				String externalizedText = root.getMessage( textKey );
				return externalizedText;
			}
			element = element.getExtendsElement( );
		}
		return null;
	}

	/**
	 * Returns externalized value.
	 * 
	 * @param element
	 *            Design element.
	 * @param propIDName
	 *            ID of property
	 * @param propName
	 *            Name of property
	 * @return externalized value.
	 */

	public static String getExternalizedValue( DesignElement element,
			String propIDName, String propName )
	{
		if ( element == null )
			return null;
		String textKey = searchForExternalizedValue( element, propIDName );
		if ( !StringUtil.isBlank( textKey ) )
			return textKey;

		// use static text.

		return element.getStringProperty( element.getRoot( ), propName );
	}

	/**
	 * Checks if report design contains the same library as the target library
	 * which user wants to export. If no exception throws , that stands for user
	 * can export report design to library. Comparing with the obsolute file
	 * name path, if file is the same , throw <code>SemanticException</code>
	 * which error code is include recursive error.
	 * 
	 * For example , the path of library is "C:\test\lib.xml" .The followings
	 * will throw semantic exception:
	 * 
	 * <ul>
	 * <li> design file and library in the same folder: </li>
	 * <li> <list-property name="libraries"> <structure> <property
	 * name="fileName">lib.xml</property> <property name="namespace">lib</property>
	 * </structure> </list-property> </li>
	 * </ul>
	 * <ul>
	 * <li> folder of design file is "C:\design" </li>
	 * <li> <list-property name="libraries"> <structure> <property
	 * name="fileName">..\test\lib.xml</property> <property
	 * name="namespace">lib</property> </structure> </list-property> </li>
	 * </ul>
	 * 
	 * @param designToExport
	 *            handle of the report design to export
	 * @param targetLibraryHandle
	 *            handle of target library
	 * @return if contains the same absolute file path , return true; else
	 *         return false.
	 * @throws SemanticException
	 *             if absolut file path is the same between library included in
	 *             report design and library.
	 */

	public static boolean hasLibrary( ReportDesignHandle designToExport,
			LibraryHandle targetLibraryHandle )
	{
		String reportLocation = targetLibraryHandle.getModule( ).getLocation( );

		List libList = designToExport.getModule( ).getAllLibraries( );

		for ( Iterator libIter = libList.iterator( ); libIter.hasNext( ); )
		{
			Library library = (Library) libIter.next( );
			String libLocation = library.getRoot( ).getLocation( );

			if ( reportLocation.equals( libLocation ) )
			{
				return true;
			}
		}
		return false;
	}
}
