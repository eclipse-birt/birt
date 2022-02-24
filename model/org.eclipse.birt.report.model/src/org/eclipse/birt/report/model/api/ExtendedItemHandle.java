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

package org.eclipse.birt.report.model.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.elements.IReportItemMethodContext;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.extension.IllegalContentInfo;
import org.eclipse.birt.report.model.api.extension.UndefinedPropertyInfo;
import org.eclipse.birt.report.model.api.metadata.IMethodInfo;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.namespace.NameExecutor;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.interfaces.IExtendedItemModel;
import org.eclipse.birt.report.model.extension.PeerExtensibilityProvider;
import org.eclipse.birt.report.model.extension.SimplePeerExtensibilityProvider.UndefinedChildInfo;

/**
 * Represents an extended element. An extended item represents a custom element
 * added by the application. Extended items can use user-defined properties, can
 * use scripts, or a combination of the two. Extended items often require
 * user-defined properties.
 * <p>
 * An extended element has a plug-in property that is a name of a Java class
 * that implements the behavior for the element.
 * 
 * @see org.eclipse.birt.report.model.elements.ExtendedItem
 */

public class ExtendedItemHandle extends ReportItemHandle implements IExtendedItemModel, IReportItemMethodContext {

	/**
	 * Logger instance.
	 */

	private static Logger logger = Logger.getLogger(ExtendedItemHandle.class.getName());

	/**
	 * Constructs the handle with the report design and the element it holds. The
	 * application generally does not create handles directly. Instead, it uses one
	 * of the navigation methods available on other element handles.
	 * 
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public ExtendedItemHandle(Module module, DesignElement element) {
		super(module, element);
	}

	/**
	 * Returns the extension name defined by the extended item.
	 * 
	 * @return the extension name as a string
	 */

	public String getExtensionName() {
		return getStringProperty(EXTENSION_NAME_PROP);
	}

	/**
	 * Loads the instance of extended element. When the application invokes UI for
	 * the extended element, such as listing property values in property sheet, set
	 * the value of the extension-defined properties and so other operations, the
	 * application must create an instance of the extension element first. The
	 * created extended element reads its information cached by the handle and
	 * de-serialize the extension model.
	 * 
	 * @throws ExtendedElementException if the serialized model is invalid
	 */

	public void loadExtendedElement() throws ExtendedElementException {
		((ExtendedItem) getElement()).initializeReportItem(module);
	}

	/**
	 * Returns the interface <code>IReportItem</code> for extension.
	 * 
	 * @return the interface <code>IReportItem</code> for extension
	 * 
	 * @throws ExtendedElementException if the serialized model is invalid
	 */

	public IReportItem getReportItem() throws ExtendedElementException {
		IReportItem reportItem = ((ExtendedItem) getElement()).getExtendedElement();

		if (reportItem == null) {
			loadExtendedElement();
			reportItem = ((ExtendedItem) getElement()).getExtendedElement();
		}

		return reportItem;
	}

	/**
	 * Returns the list of extension property definition. All these properties are
	 * just those defined in extension plugin.
	 * 
	 * @return the list of extension property definition.
	 */

	public List getExtensionPropertyDefinitionList() {
		if (((ExtendedItem) getElement()).getExtDefn() != null)

			return ((ExtendedItem) getElement()).getExtDefn().getLocalProperties();

		return Collections.EMPTY_LIST;

	}

	/**
	 * Returns the methods defined on the extension element definition and the
	 * methods defined within the extension model property inside.
	 * 
	 * @return the list of methods
	 */

	public List getMethods() {
		return ((ExtendedItem) getElement()).getMethods();
	}

	/**
	 * Returns an iterator over filter. The iterator returns instances of
	 * <code>FilterConditionHandle</code> that represents filter condition object.
	 * 
	 * @return iterator over filters.
	 * 
	 * @see org.eclipse.birt.report.model.api.elements.structures.FilterCondition
	 */

	public Iterator filtersIterator() {
		PropertyHandle propHandle = getPropertyHandle(FILTER_PROP);
		if (propHandle == null)
			return Collections.EMPTY_LIST.iterator();
		return propHandle.iterator();
	}

	/**
	 * Returns the external script defined in the extended element model.
	 * 
	 * @return the script
	 */

	public String getExternalScript() {
		String propName = ((ExtendedItem) getElement()).getScriptPropertyName();
		if (propName == null)
			return null;
		return getStringProperty(propName);
	}

	/**
	 * Sets the scripts in the extension element model.
	 * 
	 * @param theScript the script to be set
	 * @throws SemanticException if fail to set the scripts
	 */

	public void setExternalScript(String theScript) throws SemanticException {
		String propName = ((ExtendedItem) getElement()).getScriptPropertyName();
		if (propName == null)
			return;
		setStringProperty(propName, theScript);
	}

	/**
	 * Returns the alternate text of this extended item.
	 * 
	 * @return the alternate text of the extended item.
	 */

	public String getAltText() {
		return getStringProperty(ALTTEXT_PROP);
	}

	/**
	 * Returns the resource key of the alternate text of this extended item.
	 * 
	 * @return the resource key of the alternate text
	 */

	public String getAltTextKey() {
		return getStringProperty(ALTTEXT_KEY_PROP);
	}

	/**
	 * Sets the alt text of this extended item.
	 * 
	 * @param altText the alt text
	 * 
	 * @throws SemanticException if the property is locked.
	 */

	public void setAltText(String altText) throws SemanticException {
		setStringProperty(ALTTEXT_PROP, altText);
	}

	/**
	 * Sets the alt text id of this extended item.
	 * 
	 * @param altTextKey the alt text id
	 * 
	 * @throws SemanticException if the property is locked.
	 */

	public void setAltTextKey(String altTextKey) throws SemanticException {
		setStringProperty(ALTTEXT_KEY_PROP, altTextKey);
	}

	/**
	 * Returns functions that can be called in the given method.
	 * 
	 * @param context the method name in string
	 * 
	 * @return a list containing <code>IMethodInfo</code> for functions
	 */

	public List getMethods(String context) {
		if (StringUtil.isBlank(context))
			return null;

		IReportItem extension = null;

		try {
			extension = getReportItem();
		} catch (ExtendedElementException e) {
			return null;
		}

		IMethodInfo[] info = extension.getMethods(context);
		if (info == null || info.length == 0) {
			return null;
		}
		List returnList = new ArrayList();
		for (int i = 0; i < info.length; i++) {
			IMethodInfo tmpInfo = info[i];
			if (tmpInfo == null) {
				logger.log(Level.WARNING, "The method info " + i + " in the methods are null."); //$NON-NLS-1$ //$NON-NLS-2$

				continue;
			}
			String tmpContext = tmpInfo.getName();
			if (StringUtil.isBlank(tmpContext)) {
				logger.log(Level.WARNING, "The name of the method info " + i + " is empty or null."); //$NON-NLS-1$//$NON-NLS-2$
				continue;
			}
			returnList.add(tmpInfo);
		}

		return returnList;
	}

	/**
	 * Gets the map of all name/value pair. The property in the map is either set an
	 * invalid value or the definition is not found. Key is the name of the property
	 * and value is instance of <code>UndefinedPropertyInfo</code>.
	 * 
	 * @return map of invalid property value or undefined property
	 */
	public Map<String, UndefinedPropertyInfo> getUndefinedProperties() {
		PeerExtensibilityProvider provider = ((ExtendedItem) getElement()).getExtensibilityProvider();

		Map<String, UndefinedPropertyInfo> propMap = new HashMap<String, UndefinedPropertyInfo>();
		propMap.putAll(provider.getInvalidPropertyValueMap());
		propMap.putAll(provider.getUndefinedPropertyMap());
		return propMap;
	}

	/**
	 * Gets all the illegal contents. The key is the property name where the
	 * contents reside. The value is the list of item that are illegal to be
	 * inserted. Each item in the list is instance of
	 * <code>IllegalContentInfo</code>.
	 * 
	 * @return
	 */
	public Map<String, List<IllegalContentInfo>> getIllegalContents() {
		PeerExtensibilityProvider provider = ((ExtendedItem) getElement()).getExtensibilityProvider();
		Map<String, List<UndefinedChildInfo>> illegalChildren = provider.getIllegalContents();
		if (illegalChildren == null || illegalChildren.isEmpty())
			return Collections.emptyMap();

		Map<String, List<IllegalContentInfo>> transMap = new HashMap<String, List<IllegalContentInfo>>();
		Iterator<Entry<String, List<UndefinedChildInfo>>> iter = illegalChildren.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, List<UndefinedChildInfo>> entry = iter.next();
			List<UndefinedChildInfo> childList = entry.getValue();
			if (childList != null && !childList.isEmpty()) {
				List<IllegalContentInfo> transChildren = new ArrayList<IllegalContentInfo>();
				for (int i = 0; i < childList.size(); i++) {
					UndefinedChildInfo infor = childList.get(i);
					transChildren.add(new IllegalContentInfo(infor, module));
				}
				transMap.put(entry.getKey(), transChildren);
			}
		}

		return transMap;
	}

	/**
	 * Gets the extension version of this element.
	 * 
	 * @return extension version of this element
	 */
	public String getExtensionVersion() {
		return getStringProperty(EXTENSION_VERSION_PROP);
	}

	/**
	 * Sets the extension version of this element.
	 * 
	 * @param extensionVersion
	 * @throws SemanticException
	 */
	public void setExtensionVersion(String extensionVersion) throws SemanticException {
		setStringProperty(EXTENSION_VERSION_PROP, extensionVersion);
	}

	/**
	 * 
	 * Makes a unique name for this element with the given name prefix.
	 * 
	 * @param namePrefix the name prefix
	 */

	public void makeUniqueName(String namePrefix) throws NameException {
		NameExecutor executor = new NameExecutor(module, element);
		if (executor.hasNamespace()) {
			executor.makeUniqueName(namePrefix);
		}
	}
}
