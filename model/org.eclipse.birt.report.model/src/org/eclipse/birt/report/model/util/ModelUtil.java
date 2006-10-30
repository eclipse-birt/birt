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
import java.lang.reflect.Constructor;
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
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.table.LayoutUtil;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;
import org.eclipse.birt.report.model.api.util.ElementExportUtil;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.api.util.UnicodeUtil;
import org.eclipse.birt.report.model.core.BackRef;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.core.ReferenceableElement;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.core.StyledElement;
import org.eclipse.birt.report.model.elements.DataSet;
import org.eclipse.birt.report.model.elements.GridItem;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IExtendedItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IOdaExtendableElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.extension.IExtendableElement;
import org.eclipse.birt.report.model.i18n.ModelMessages;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.metadata.ReferenceValue;
import org.eclipse.birt.report.model.parser.DesignParserException;
import org.eclipse.birt.report.model.parser.DesignSchemaConstants;
import org.xml.sax.SAXException;

import com.ibm.icu.text.CollationKey;
import com.ibm.icu.text.Collator;
import com.ibm.icu.util.ULocale;

/**
 * The utility class which provides many static methods used in Model.
 */

public class ModelUtil
{

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
		Iterator propNames = content.propertyWithLocalValueIterator( );
		IElementDefn defn = content.getDefn( );

		while ( propNames.hasNext( ) )
		{
			String propName = (String) propNames.next( );

			ElementPropertyDefn propDefn = (ElementPropertyDefn) defn
					.getProperty( propName );
			revisePropertyNameSpace( module, content, propDefn, nameSpace );
		}

		for ( int i = 0; i < defn.getSlotCount( ); i++ )
		{
			ContainerSlot slot = content.getSlot( i );
			for ( int pos = 0; pos < slot.getCount( ); pos++ )
				reviseNameSpace( module, slot.getContent( pos ), nameSpace );
		}
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
	 * @param propDefn
	 * @param nameSpace
	 *            the new name space
	 */

	public static void revisePropertyNameSpace( Module module,
			DesignElement content, IElementPropertyDefn propDefn,
			String nameSpace )
	{
		if ( propDefn == null || content == null )
			return;

		if ( propDefn.getTypeCode( ) != IPropertyType.ELEMENT_REF_TYPE
				&& propDefn.getTypeCode( ) != IPropertyType.EXTENDS_TYPE )
			return;

		Object value = content.getLocalProperty( module,
				(ElementPropertyDefn) propDefn );
		if ( value == null )
			return;

		ReferenceValue refValue = (ReferenceValue) value;
		refValue.setLibraryNamespace( nameSpace );
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

				if ( targetDefn.isKindOf( defn ) )
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
	 * @param propIDName
	 *            Name of resource key property
	 * @param locale
	 *            the locale
	 * @return externalized message if found, otherwise <code>null</code>
	 */

	public static String searchForExternalizedValue( DesignElement element,
			String propIDName, ULocale locale )
	{
		if ( element == null )
			return null;

		IElementPropertyDefn defn = element.getPropertyDefn( propIDName );
		if ( defn == null )
			return null;

		String textKey = (String) element.getProperty( element.getRoot( ),
				propIDName );
		if ( StringUtil.isBlank( textKey ) )
			return null;

		DesignElement temp = element;
		while ( temp != null )
		{
			String externalizedText = temp.getRoot( ).getMessage( textKey,
					locale );
			if ( externalizedText != null )
				return externalizedText;

			// if this property can not inherit, return null

			if ( !defn.canInherit( ) )
				return null;

			if ( DesignElement.NO_BASE_ID != temp.getBaseId( ) )
				temp = temp.getVirtualParent( );
			else
				temp = temp.getExtendsElement( );
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
	 * @param locale
	 *            the locale
	 * @return externalized value.
	 */

	public static String getExternalizedValue( DesignElement element,
			String propIDName, String propName, ULocale locale )
	{
		if ( element == null || element.getPropertyDefn( propName ) == null
				|| element.getPropertyDefn( propIDName ) == null )
			return null;
		String textKey = searchForExternalizedValue( element, propIDName,
				locale );
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

	/**
	 * Returns a list whose entry is of <code>IVersionInfo</code> type. Each
	 * kind of automatical conversion information is stored in one instance of
	 * <code>IVersionInfo</code>. If the size of the return list is 0, there
	 * is no conversion information.
	 * 
	 * @param version
	 *            the design file version
	 * @return a list containing <code>IVersionInfo</code>
	 */

	public static List checkVersion( String version )
	{
		List rtnList = new ArrayList( );

		if ( VersionUtil.parseVersion( version ) <= VersionInfo.COLUMN_BINDING_FROM_VERSION
				&& DesignSchemaConstants.REPORT_VERSION_NUMBER > VersionInfo.COLUMN_BINDING_FROM_VERSION )
			rtnList.add( new VersionInfo( version,
					VersionInfo.CONVERT_FOR_COLUMN_BINDING ) );

		return rtnList;
	}

	/**
	 * Justifies whether the given element supports template transform
	 * 
	 * @param element
	 *            the element to check
	 * @return true if Model supports the template element for the given one,
	 *         otherwise false
	 */

	public static boolean isTemplateSupported( DesignElement element )
	{
		// all the data sets support template

		if ( element instanceof DataSet )
			return true;

		// not all the report items support template, eg. auto text does not
		// support template

		if ( element instanceof ReportItem )
		{
			IChoiceSet choiceSet = MetaDataDictionary.getInstance( )
					.getChoiceSet(
							DesignChoiceConstants.CHOICE_TEMPLATE_ELEMENT_TYPE );
			assert choiceSet != null;
			IChoice[] choices = choiceSet.getChoices( );
			for ( int i = 0; i < choices.length; i++ )
			{
				if ( element.getDefn( ).getName( ).equalsIgnoreCase(
						choices[i].getName( ) ) )
					return true;
			}
		}

		return false;
	}

	/**
	 * Checks whether the compound element is valid if the element has no
	 * extends property value or if the current element is compound elements and
	 * extends value is unresovled.
	 * 
	 * @param module
	 *            the root module of the element
	 * @param element
	 *            the element to justify
	 * 
	 * @return <code>true</code> if the compound element is valid. Otherwise
	 *         <code>false</code>.
	 * 
	 * @deprecated
	 */

	public static boolean isValidReferenceForCompoundElement( Module module,
			DesignElement element )
	{
		ElementRefValue refValue = (ElementRefValue) element.getLocalProperty(
				module, IDesignElementModel.EXTENDS_PROP );
		if ( refValue == null )
			return true;

		// TODO resolve the element later. NO such case right now.

		if ( element.getDefn( ).isContainer( ) && !refValue.isResolved( ) )
			return false;

		// if any ancestor of this element loses extended element, return false

		DesignElement parent = element.getExtendsElement( );
		while ( parent != null )
		{
			if ( !isValidReferenceForCompoundElement( parent.getRoot( ), parent ) )
				return false;
			parent = parent.getExtendsElement( );
		}

		return true;
	}

	/**
	 * Checks whether the compound element is valid.
	 * <p>
	 * If the table/grid has no rows and columns, its layout is invalid.
	 * 
	 * @param module
	 *            the root module of the element
	 * @param element
	 *            the element to check
	 * 
	 * @return <code>true</code> if the compound element is valid. Otherwise
	 *         <code>false</code>.
	 */

	public static boolean isValidLayout( Module module, DesignElement element )
	{
		if ( !( element instanceof ReportItem ) )
			return true;

		if ( !element.getDefn( ).isContainer( ) )
			return true;

		if ( element instanceof TableItem )
			return LayoutUtil.isValidLayout( (TableItem) element, module );

		if ( element instanceof GridItem )
		{
			int columnCount = ( (GridItem) element ).getColumnCount( module );
			if ( columnCount == 0 )
				return false;
		}
		return true;
	}

	/**
	 * Creates a design element specified by the element type name. Element type
	 * names are defined in rom.def or extension elements. They are managed by
	 * the meta-data system.
	 * 
	 * @param module
	 *            the module to create an element
	 * @param elementTypeName
	 *            the element type name
	 * @param name
	 *            the optional element name
	 * 
	 * @return design element, <code>null</code> returned if the element
	 *         definition name is not a valid element type name.
	 */

	public static DesignElement newElement( Module module,
			String elementTypeName, String name )
	{

		DesignElement element = newElement( elementTypeName, name );
		if ( element != null && module != null )
			module.makeUniqueName( element );
		return element;
	}

	/**
	 * Creates a design element specified by the element type name. Element type
	 * names are defined in rom.def or extension elements. They are managed by
	 * the meta-data system.
	 * 
	 * @param module
	 *            the module to create an element
	 * @param elementTypeName
	 *            the element type name
	 * @param name
	 *            the optional element name
	 * 
	 * @return design element, <code>null</code> returned if the element
	 *         definition name is not a valid element type name.
	 */

	public static DesignElement newElement( String elementTypeName, String name )
	{

		ElementDefn elemDefn = (ElementDefn) MetaDataDictionary.getInstance( )
				.getElement( elementTypeName );

		String javaClass = elemDefn.getJavaClass( );
		if ( javaClass == null )
			return null;

		try
		{
			Class c = Class.forName( javaClass );
			DesignElement element = null;

			try
			{
				Constructor constructor = c
						.getConstructor( new Class[]{String.class} );
				element = (DesignElement) constructor
						.newInstance( new String[]{name} );
				return element;
			}
			catch ( NoSuchMethodException e1 )
			{
				element = (DesignElement) c.newInstance( );
				return element;
			}

		}
		catch ( Exception e )
		{
			// Impossible.

			assert false;
		}

		return null;
	}

	/**
	 * Adds an element to the name space. If the module is null, or element is
	 * null, or element is not in the tree of module, then do nothing.
	 * 
	 * @param module
	 * @param element
	 */

	public static void addElement2NameSpace( Module module,
			DesignElement element )
	{
		if ( module == null || element == null || element.getRoot( ) != module )
			return;
		module.makeUniqueName( element );
		int ns = ( (ElementDefn) element.getDefn( ) ).getNameSpaceID( );
		if ( element.getName( ) != null
				&& ns != MetaDataConstants.NO_NAME_SPACE
				&& element.getContainer( ).isManagedByNameSpace(
						element.getContainerSlot( ) ) )
			module.getNameSpace( ns ).insert( element );
	}

	/**
	 * Gets the element name with the given xml name.
	 * 
	 * @param xmlName
	 *            the xml name to find
	 * @return the element name with the given xml name if found, otherwise null
	 */

	public static String getElementNameFromXmlName( String xmlName )
	{
		if ( StringUtil.isBlank( xmlName ) )
			return null;
		if ( DesignSchemaConstants.CELL_TAG.equalsIgnoreCase( xmlName ) )
			return ReportDesignConstants.CELL_ELEMENT;
		else if ( DesignSchemaConstants.COLUMN_TAG.equalsIgnoreCase( xmlName ) )
			return ReportDesignConstants.COLUMN_ELEMENT;
		else if ( DesignSchemaConstants.DATA_TAG.equalsIgnoreCase( xmlName ) )
			return ReportDesignConstants.DATA_ITEM;
		else if ( DesignSchemaConstants.ODA_DATA_SET_TAG
				.equalsIgnoreCase( xmlName ) )
			return ReportDesignConstants.ODA_DATA_SET;
		else if ( DesignSchemaConstants.ODA_DATA_SOURCE_TAG
				.equalsIgnoreCase( xmlName ) )
			return ReportDesignConstants.ODA_DATA_SOURCE;
		else if ( DesignSchemaConstants.EXTENDED_ITEM_TAG
				.equalsIgnoreCase( xmlName ) )
			return ReportDesignConstants.EXTENDED_ITEM;
		else if ( DesignSchemaConstants.FREE_FORM_TAG
				.equalsIgnoreCase( xmlName ) )
			return ReportDesignConstants.FREE_FORM_ITEM;
		else if ( DesignSchemaConstants.GRAPHIC_MASTER_PAGE_TAG
				.equalsIgnoreCase( xmlName ) )
			return ReportDesignConstants.GRAPHIC_MASTER_PAGE_ELEMENT;
		else if ( DesignSchemaConstants.GRID_TAG.equalsIgnoreCase( xmlName ) )
			return ReportDesignConstants.GRID_ITEM;
		else if ( DesignSchemaConstants.IMAGE_TAG.equalsIgnoreCase( xmlName ) )
			return ReportDesignConstants.IMAGE_ITEM;
		else if ( DesignSchemaConstants.LABEL_TAG.equalsIgnoreCase( xmlName ) )
			return ReportDesignConstants.LABEL_ITEM;
		else if ( DesignSchemaConstants.LINE_TAG.equalsIgnoreCase( xmlName ) )
			return ReportDesignConstants.LINE_ITEM;
		else if ( DesignSchemaConstants.GROUP_TAG.equalsIgnoreCase( xmlName ) )
			return ReportDesignConstants.GROUP_ELEMENT;
		else if ( DesignSchemaConstants.LIST_TAG.equalsIgnoreCase( xmlName ) )
			return ReportDesignConstants.LIST_ITEM;
		else if ( DesignSchemaConstants.PARAMETER_GROUP_TAG
				.equalsIgnoreCase( xmlName ) )
			return ReportDesignConstants.PARAMETER_GROUP_ELEMENT;
		else if ( DesignSchemaConstants.CASCADING_PARAMETER_GROUP_TAG
				.equalsIgnoreCase( xmlName ) )
			return ReportDesignConstants.CASCADING_PARAMETER_GROUP_ELEMENT;
		else if ( DesignSchemaConstants.RECTANGLE_TAG
				.equalsIgnoreCase( xmlName ) )
			return ReportDesignConstants.RECTANGLE_ITEM;
		else if ( DesignSchemaConstants.ROW_TAG.equalsIgnoreCase( xmlName ) )
			return ReportDesignConstants.ROW_ELEMENT;
		else if ( DesignSchemaConstants.SCALAR_PARAMETER_TAG
				.equalsIgnoreCase( xmlName ) )
			return ReportDesignConstants.SCALAR_PARAMETER_ELEMENT;
		else if ( DesignSchemaConstants.SCRIPT_DATA_SET_TAG
				.equalsIgnoreCase( xmlName ) )
			return ReportDesignConstants.SCRIPT_DATA_SET;
		else if ( DesignSchemaConstants.SCRIPT_DATA_SOURCE_TAG
				.equalsIgnoreCase( xmlName ) )
			return ReportDesignConstants.SCRIPT_DATA_SOURCE;
		else if ( DesignSchemaConstants.SIMPLE_MASTER_PAGE_TAG
				.equalsIgnoreCase( xmlName ) )
			return ReportDesignConstants.SIMPLE_MASTER_PAGE_ELEMENT;
		else if ( DesignSchemaConstants.STYLE_TAG.equalsIgnoreCase( xmlName ) )
			return ReportDesignConstants.STYLE_ELEMENT;
		else if ( DesignSchemaConstants.TABLE_TAG.equalsIgnoreCase( xmlName ) )
			return ReportDesignConstants.TABLE_ITEM;
		else if ( DesignSchemaConstants.TEXT_TAG.equalsIgnoreCase( xmlName ) )
			return ReportDesignConstants.TEXT_ITEM;
		else if ( DesignSchemaConstants.TEXT_DATA_TAG
				.equalsIgnoreCase( xmlName ) )
			return ReportDesignConstants.TEXT_DATA_ITEM;
		else if ( DesignSchemaConstants.THEME_TAG.equalsIgnoreCase( xmlName ) )
			return ReportDesignConstants.THEME_ITEM;
		else if ( DesignSchemaConstants.TEMPLATE_PARAMETER_DEFINITION_TAG
				.equalsIgnoreCase( xmlName ) )
			return ReportDesignConstants.TEMPLATE_PARAMETER_DEFINITION;
		else if ( DesignSchemaConstants.TEMPLATE_REPORT_ITEM_TAG
				.equalsIgnoreCase( xmlName ) )
			return ReportDesignConstants.TEMPLATE_REPORT_ITEM;
		else if ( DesignSchemaConstants.TEMPLATE_DATA_SET_TAG
				.equalsIgnoreCase( xmlName ) )
			return ReportDesignConstants.TEMPLATE_DATA_SET;
		else if ( DesignSchemaConstants.JOINT_DATA_SET_TAG
				.equalsIgnoreCase( xmlName ) )
			return ReportDesignConstants.JOINT_DATA_SET;
		else if ( DesignSchemaConstants.AUTO_TEXT_TAG
				.equalsIgnoreCase( xmlName ) )
			return ReportDesignConstants.AUTOTEXT_ITEM;
		else if ( DesignSchemaConstants.MULTI_LINE_DATA_TAG
				.equalsIgnoreCase( xmlName ) )
			return ReportDesignConstants.TEXT_DATA_ITEM;

		return null;
	}

	/**
	 * Checks whether these is reference between <code>reference</code> and
	 * <code>referred</code>.
	 * 
	 * @param reference
	 * @param referred
	 * 
	 * @return <code>true</code> if there is reference. Otherwise
	 *         <code>false</code>.
	 */

	public static boolean isRecursiveReference( DesignElement reference,
			ReferenceableElement referred )
	{
		if ( reference == referred )
			return true;

		List backRefs = referred.getClientList( );

		List referenceElements = new ArrayList( );
		for ( int i = 0; i < backRefs.size( ); i++ )
		{
			BackRef backRef = (BackRef) backRefs.get( i );
			DesignElement tmpElement = backRef.getElement( );

			if ( tmpElement == reference )
				return true;

			if ( tmpElement instanceof ReferenceableElement )
				referenceElements.add( tmpElement );
		}

		for ( int i = 0; i < referenceElements.size( ); i++ )
		{
			if ( isRecursiveReference( reference,
					(ReferenceableElement) referenceElements.get( i ) ) )
				return true;
		}

		return false;
	}
}
