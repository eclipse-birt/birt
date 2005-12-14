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

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.ReferenceValue;

/**
 * Collection of <code>ReferenceValue</code> utilities.
 */

public class ReferenceValueUtil
{

	/**
	 * Gets the property value with the name prefix. This method is just used
	 * for the element/structure reference type property. If the report design
	 * element is extended from a library element and the retrieved value comes
	 * from the parent, the namespace of the library should be added before the
	 * value. This is used to allocate the referenced element/structure within
	 * the correct scope.
	 * 
	 * @param refValue
	 *            the reference value
	 * @param root
	 *            the module that the element attached.
	 * @param module
	 *            the module that holds the ActivityStack. For the case that
	 *            element is not on the library/design tree.
	 * @return the value of the property. The type of the returned object should
	 *         be strings.
	 * 
	 */

	public static String needTheNamespacePrefix( ReferenceValue refValue,
			Module root, Module module )
	{
		if ( refValue == null )
			return null;

		String namespace = refValue.getLibraryNamespace( );
		String name = refValue.getName( );

		if ( namespace == null )
			return name;
		Module theRoot = module;
		if ( root != null )
			theRoot = root;

		if ( theRoot instanceof Library )
		{
			if ( !namespace.equals( ( (Library) theRoot ).getNamespace( ) ) )
				name = namespace + ReferenceValue.NAMESPACE_DELIMITER + name;
		}
		else
			name = namespace + ReferenceValue.NAMESPACE_DELIMITER + name;

		return name;

	}

	/**
	 * Gets the correct element name for the specified module. If the
	 * <code>module</code> is the root element of <code>element</code>, no
	 * libray namespace is in the return string. If the root element of
	 * <code>element</code> is not <code>module</code> and it is a library
	 * with the namespace, the return value contains the namespace.
	 * 
	 * <p>
	 * This is used to allocate the referenced element/structure within the
	 * correct scope.
	 * 
	 * @param element
	 *            the design element.
	 * @param root
	 * @param module
	 *            the module.
	 * @return the element name. It contains the library namespace if above
	 *         criteria applies.
	 * 
	 */

	public static String needTheNamespacePrefix( DesignElement element,
			Module root, Module module )
	{
		if ( element == null )
			return null;

		String nameSpace = null;
		if ( root != null && root instanceof Library )
			nameSpace = ( (Library) root ).getNamespace( );

		String name = element.getName( );

		if ( root != module )
			name = nameSpace + ReferenceValue.NAMESPACE_DELIMITER + name;
		return name;
	}

	/**
	 * Gets the correct element name for the specified module. If the
	 * <code>module</code> is the root element of <code>element</code>, no
	 * libray namespace is in the return string. If the root element of
	 * <code>element</code> is not <code>module</code> and it is a library
	 * with the namespace, the return value contains the namespace.
	 * 
	 * <p>
	 * This is used to allocate the referenced element/structure within the
	 * correct scope.
	 * 
	 * @param refValue
	 * 
	 * @param root
	 * @return the element name. It contains the library namespace if above
	 *         criteria applies.
	 * 
	 */

	public static String needTheNamespacePrefix( ReferenceValue refValue,
			Module root )
	{
		if ( refValue == null )
			return null;

		String namespace = refValue.getLibraryNamespace( );
		String name = refValue.getName( );

		if ( namespace == null )
			return name;

		Module module = null;

		if ( refValue.isResolved( ) && refValue instanceof ElementRefValue )
			module = ( (ElementRefValue) refValue ).getElement( ).getRoot( );
		else
		{
			module = root.getLibraryWithNamespace( namespace );

			// if it is resolved as structure, the element of strcuture
			// cannot be retrieved.

			if ( root instanceof Library && module == null )
			{
				Library lib = (Library) root;
				if ( namespace.equalsIgnoreCase( lib.getNamespace( ) ) )
					module = root;
			}
		}

		// if reference value is not resolved.

		String retNamespace = null;

		// if cannot find the library with the given namespace, uses the
		// namespace in the reference value.

		if ( module == null )
			retNamespace = namespace;

		// if the root eqauls module and they are all not null , just return the
		// name.

		while ( module != root && module instanceof Library )
		{
			Library lib = (Library) module;
			if ( retNamespace != null )
				retNamespace = lib.getNamespace( )
						+ ReferenceValue.NAMESPACE_DELIMITER + retNamespace;
			else
				retNamespace = lib.getNamespace( );

			module = lib.getHost( );
		}

		if ( retNamespace != null )
			name = retNamespace + ReferenceValue.NAMESPACE_DELIMITER + name;

		return name;
	}
}
