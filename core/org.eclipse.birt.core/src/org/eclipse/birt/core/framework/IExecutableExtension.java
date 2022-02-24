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
 * <code>IExecutableExtension</code> interface. Interface for executable
 * extension classes that require access to their configuration element, or
 * implement an extension adapter.
 * <p>
 * Extension adapters are typically required in cases where the extension
 * implementation does not follow the interface rules specified by the provider
 * of the extension point. In these cases, the role of the adapter is to map
 * between the extension point interface, and the actual extension
 * implementation. In general, adapters are used when attempting to plug-in
 * existing Java implementations, or non-Java implementations (e.g., external
 * executables).
 * </p>
 * <p>
 * Clients may implement this interface.
 * </p>
 * 
 * @see IConfigurationElement#createExecutableExtension(String)
 */
public interface IExecutableExtension {
	/**
	 * This method is called by the implementation of the method
	 * <code>IConfigurationElement.createExecutableExtension</code> on a newly
	 * constructed extension, passing it its relevant configuration information.
	 * Most executable extensions only make use of the first two call arguments.
	 * <p>
	 * Regular executable extensions specify their Java implementation class name as
	 * an attribute of the configuration element for the extension. For example
	 * 
	 * <pre>
	 *     &lt;action run="com.example.BaseAction"/&gt;
	 * </pre>
	 * 
	 * In the above example, this method would be called with a reference to the
	 * <code>&lt;action&gt;</code> element (first argument), and <code>"run"</code>
	 * as the name of the attribute that defined this executable extension (second
	 * argument).
	 * </p>
	 * <p>
	 * The last parameter is for the specific use of extension adapters and is
	 * typically not used by regular executable extensions.
	 * </p>
	 * <p>
	 * There are two supported ways of associating additional adapter-specific data
	 * with the configuration in a way that is transparent to the extension point
	 * implementor:
	 * </p>
	 * <p>
	 * (1) by specifying adapter data as part of the implementation class attribute
	 * value. The Java class name can be followed by a ":" separator, followed by
	 * any adapter data in string form. For example, if the extension point
	 * specifies an attribute <code>"run"</code> to contain the name of the
	 * extension implementation, an adapter can be configured as
	 * 
	 * <pre>
	 *     &lt;action run="com.example.ExternalAdapter:./cmds/util.exe -opt 3"/&gt;
	 * </pre>
	 * </p>
	 * <p>
	 * (2) by converting the attribute used to specify the executable extension to a
	 * child element of the original configuration element, and specifying the
	 * adapter data in the form of xml markup. Using this form, the example above
	 * would become
	 * 
	 * <pre>
	 *     &lt;action&gt;
	 *         &lt;<it>run</it> class="com.xyz.ExternalAdapter"&gt;
	 *             &lt;parameter name="exec" value="./cmds/util.exe"/&gt;
	 *             &lt;parameter name="opt"  value="3"/&gt;
	 *         &lt;/<it>run</it>&gt;
	 *     &lt;/action&gt;
	 * </pre>
	 * </p>
	 * <p>
	 * Form (2) will typically only be used for extension points that anticipate the
	 * majority of extensions configured into it will in fact be in the form of
	 * adapters.
	 * </p>
	 * <p>
	 * In either case, the specified adapter class is instantiated using its
	 * 0-argument public constructor. The adapter data is passed as the last
	 * argument of this method. The data argument is defined as Object. It can have
	 * the following values:
	 * <ul>
	 * <li><code>null</code>, if no adapter data was supplied</li>
	 * <li>in case (1), the initialization data string is passed as a
	 * <code>String</code></li>
	 * <li>in case (2), the initialization data is passed as a
	 * <code>Hashtable</code> containing the actual parameter names and values (both
	 * <code>String</code>s)</li>
	 * </ul>
	 * </p>
	 *
	 * @param config       the configuration element used to trigger this execution.
	 *                     It can be queried by the executable extension for
	 *                     specific configuration properties
	 * @param propertyName the name of an attribute of the configuration element
	 *                     used on the
	 *                     <code>createExecutableExtension(String)</code> call. This
	 *                     argument can be used in the cases where a single
	 *                     configuration element is used to define multiple
	 *                     executable extensions.
	 * @param data         adapter data in the form of a <code>String</code>, a
	 *                     <code>Hashtable</code>, or <code>null</code>.
	 * @exception FrameworkException if error(s) detected during initialization
	 *                               processing
	 * @see IConfigurationElement#createExecutableExtension(String)
	 */
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data)
			throws FrameworkException;
}
