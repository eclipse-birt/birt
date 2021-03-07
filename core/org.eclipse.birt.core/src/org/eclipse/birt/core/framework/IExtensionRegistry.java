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
package org.eclipse.birt.core.framework;

/**
 * This interface is the same as the Eclipse platform
 * <code>IExtensionRegistry</code> interface.
 * <p>
 *
 * The extension registry holds the master list of all discovered namespaces,
 * extension points and extensions.
 * <p>
 * The extension registry can be queried, by name, for extension points and
 * extensions.
 * </p>
 * <p>
 * Extensions and extension points are declared by generic entities called
 * <cite>namespaces</cite>. The only fact known about namespaces is that they
 * have unique string-based identifiers. One example of a namespace is a
 * plug-in, for which the namespace id is the plug-in id.
 * </p>
 * <p>
 * This interface is not intended to be implemented by clients.
 * </p>
 *
 * @since 3.0
 */
public interface IExtensionRegistry {

	/**
	 * Returns all configuration elements from all extensions configured into the
	 * identified extension point. Returns an empty array if the extension point
	 * does not exist, has no extensions configured, or none of the extensions
	 * contain configuration elements.
	 *
	 * @param extensionPointId the unique identifier of the extension point (e.g.
	 *                         <code>"org.eclipse.core.resources.builders"</code>)
	 * @return the configuration elements
	 */
	IConfigurationElement[] getConfigurationElementsFor(String extensionPointId);

	/**
	 * Returns all configuration elements from all extensions configured into the
	 * identified extension point. Returns an empty array if the extension point
	 * does not exist, has no extensions configured, or none of the extensions
	 * contain configuration elements.
	 *
	 * @param namespace          the namespace for the extension point (e.g.
	 *                           <code>"org.eclipse.core.resources"</code>)
	 * @param extensionPointName the simple identifier of the extension point (e.g.
	 *                           <code>"builders"</code>)
	 * @return the configuration elements
	 */
	IConfigurationElement[] getConfigurationElementsFor(String namespace, String extensionPointName);

	/**
	 * Returns all configuration elements from the identified extension. Returns an
	 * empty array if the extension does not exist or contains no configuration
	 * elements.
	 *
	 * @param namespace          the namespace for the extension point (e.g.
	 *                           <code>"org.eclipse.core.resources"</code>)
	 * @param extensionPointName the simple identifier of the extension point (e.g.
	 *                           <code>"builders"</code>)
	 * @param extensionId        the unique identifier of the extension (e.g.
	 *                           <code>"com.example.acme.coolbuilder</code>)
	 * @return the configuration elements
	 */
	IConfigurationElement[] getConfigurationElementsFor(String namespace, String extensionPointName,
			String extensionId);

	/**
	 * Returns the specified extension in this extension registry, or
	 * <code>null</code> if there is no such extension.
	 *
	 * @param extensionId the unique identifier of the extension (e.g.
	 *                    <code>"com.example.acme.coolbuilder"</code>)
	 * @return the extension, or <code>null</code>
	 */
	IExtension getExtension(String extensionId);

	/**
	 * Returns the specified extension in this extension registry, or
	 * <code>null</code> if there is no such extension. The first parameter
	 * identifies the extension point, and the second parameter identifies an
	 * extension plugged in to that extension point.
	 *
	 * @param extensionPointId the unique identifier of the extension point (e.g.
	 *                         <code>"org.eclipse.core.resources.builders"</code>)
	 * @param extensionId      the unique identifier of the extension (e.g.
	 *                         <code>"com.example.acme.coolbuilder"</code>)
	 * @return the extension, or <code>null</code>
	 */
	IExtension getExtension(String extensionPointId, String extensionId);

	/**
	 * Returns the specified extension in this extension registry, or
	 * <code>null</code> if there is no such extension. The first two parameters
	 * identify the extension point, and the third parameter identifies an extension
	 * plugged in to that extension point.
	 *
	 * @param namespace          the namespace for the extension point (e.g.
	 *                           <code>"org.eclipse.core.resources"</code>)
	 * @param extensionPointName the simple identifier of the extension point (e.g.
	 *                           <code>"builders"</code>)
	 * @param extensionId        the unique identifier of the extension (e.g.
	 *                           <code>"com.example.acme.coolbuilder"</code>)
	 * @return the extension, or <code>null</code>
	 */
	IExtension getExtension(String namespace, String extensionPointName, String extensionId);

	/**
	 * Returns the extension point with the given extension point identifier in this
	 * extension registry, or <code>null</code> if there is no such extension point.
	 *
	 * @param extensionPointId the unique identifier of the extension point (e.g.,
	 *                         <code>"org.eclipse.core.resources.builders"</code>)
	 * @return the extension point, or <code>null</code>
	 */
	IExtensionPoint getExtensionPoint(String extensionPointId);

	/**
	 * Returns the extension point in this extension registry with the given
	 * namespace and extension point simple identifier, or <code>null</code> if
	 * there is no such extension point.
	 *
	 * @param namespace          the namespace for the given extension point (e.g.
	 *                           <code>"org.eclipse.core.resources"</code>)
	 * @param extensionPointName the simple identifier of the extension point (e.g.
	 *                           <code>" builders"</code>)
	 * @return the extension point, or <code>null</code>
	 */
	IExtensionPoint getExtensionPoint(String namespace, String extensionPointName);

	/**
	 * Returns all extension points known to this extension registry. Returns an
	 * empty array if there are no extension points.
	 *
	 * @return the extension points known to this extension registry
	 */
	IExtensionPoint[] getExtensionPoints();

	/**
	 * Returns all extension points declared in the given namespace. Returns an
	 * empty array if there are no extension points declared in the namespace.
	 *
	 * @param namespace the namespace for the extension points (e.g.
	 *                  <code>"org.eclipse.core.resources"</code>)
	 * @return the extension points in this registry declared in the given namespace
	 */
	IExtensionPoint[] getExtensionPoints(String namespace);

	/**
	 * Returns all extensions declared in the given namespace. Returns an empty
	 * array if no extensions are declared in the namespace.
	 *
	 * @param namespace the namespace for the extensions (e.g.
	 *                  <code>"org.eclipse.core.resources"</code>)
	 * @return the extensions in this registry declared in the given namespace
	 */
	IExtension[] getExtensions(String namespace);

	/**
	 * Returns all namespaces where extensions and/or extension points. Returns an
	 * empty array if there are no known extensions/extension points in this
	 * registry.
	 *
	 * @return all namespaces known to this registry
	 * @since 3.0
	 */
	String[] getNamespaces();

}