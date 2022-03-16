/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.core.framework;

/**
 * This interface is the same as the Eclipse platform
 * <code>IExtensionPoint</code> interface.
 *
 * An extension point declared in a plug-in. Except for the list of extensions
 * plugged in to it, the information available for an extension point is
 * obtained from the declaring plug-in's manifest (<code>plugin.xml</code>)
 * file.
 * <p>
 * This interface is not intended to be implemented by clients.
 * </p>
 */
public interface IExtensionPoint {
	/**
	 * Returns all configuration elements from all extensions configured into this
	 * extension point. Returns an empty array if this extension point has no
	 * extensions configured, or none of the extensions contain configuration
	 * elements.
	 *
	 * @return the configuration elements for all extension configured into this
	 *         extension point
	 */
	IConfigurationElement[] getConfigurationElements();

	/**
	 * Returns the namespace for this extension point. This value can be used in
	 * various global facilities to discover this extension point's provider.
	 * <p>
	 * <b>Note</b>: This is an early access API to the new OSGI-based Eclipse 3.0
	 * Platform Runtime. Because the APIs for the new runtime have not yet been
	 * fully stabilized, they should only be used by clients needing to take
	 * particular advantage of new OSGI-specific functionality, and only then with
	 * the understanding that these APIs may well change in incompatible ways until
	 * they reach their finished, stable form (post-3.0).
	 * </p>
	 *
	 * @return the namespace for this extension point
	 * @see Platform#getBundle(String)
	 * @see IExtensionRegistry
	 * @since 3.0
	 */
	String getNamespace();

	/**
	 * Returns the extension with the given unique identifier configured into this
	 * extension point, or <code>null</code> if there is no such extension. Since an
	 * extension might not have an identifier, some extensions can only be found via
	 * the <code>getExtensions</code> method.
	 *
	 * @param extensionId the unique identifier of an extension (e.g.
	 *                    <code>"com.example.acme.main"</code>).
	 * @return an extension, or <code>null</code>
	 */
	IExtension getExtension(String extensionId);

	/**
	 * Returns all extensions configured into this extension point. Returns an empty
	 * array if this extension point has no extensions.
	 *
	 * @return the extensions configured into this extension point
	 */
	IExtension[] getExtensions();

	/**
	 * Returns a displayable label for this extension point. Returns the empty
	 * string if no label for this extension point is specified in the plug-in
	 * manifest file.
	 * <p>
	 * Note that any translation specified in the plug-in manifest file is
	 * automatically applied.
	 * </p>
	 *
	 * @return a displayable string label for this extension point, possibly the
	 *         empty string
	 */
	String getLabel();

	/**
	 * Returns reference to the extension point schema. The schema reference is
	 * returned as a URL path relative to the plug-in installation URL. Returns the
	 * empty string if no schema for this extension point is specified in the
	 * plug-in manifest file.
	 *
	 * @return a relative URL path, or an empty string
	 */
	String getSchemaReference();

	/**
	 * Returns the simple identifier of this extension point. This identifier is a
	 * non-empty string containing no period characters (<code>'.'</code>) and is
	 * guaranteed to be unique within the defining plug-in.
	 *
	 * @return the simple identifier of the extension point (e.g.
	 *         <code>"builders"</code>)
	 */
	String getSimpleIdentifier();

	/**
	 * Returns the unique identifier of this extension point. This identifier is
	 * unique within the plug-in registry, and is composed of the namespace for this
	 * extension point and this extension point's simple identifier.
	 *
	 * @return the unique identifier of the extension point (e.g.
	 *         <code>"org.eclipse.core.resources.builders"</code>)
	 */
	String getUniqueIdentifier();

}
