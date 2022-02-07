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

package org.eclipse.birt.report.model.extension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.birt.report.model.api.extension.UndefinedPropertyInfo;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.interfaces.IExtendedItemModel;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.parser.treebuild.ContentTree;
import org.eclipse.birt.report.model.util.ModelUtil;

/**
 * 
 */
public class SimplePeerExtensibilityProvider extends PeerExtensibilityProvider {

	/**
	 * Map to store the invalid extension name/value pairs. Only invalid extensible
	 * value is recorded not including rom-defined properties.
	 */
	private LinkedHashMap<String, UndefinedPropertyInfo> invalidValueMap = null;

	/**
	 * Map to store the undefined property name/value pairs.
	 */
	private LinkedHashMap<String, UndefinedPropertyInfo> undefinedPropertyMap = null;

	/**
	 * Map to store the undefined children map. The key is the container property
	 * and value is list of all the illegal children in this container property. The
	 * order of the children in the list is the order residing in the xml design
	 * file. The item in the list is instance of <code>UndefinedChildInfo</code>.
	 */
	private Map<String, List<UndefinedChildInfo>> illegalChildrenMap = null;

	/**
	 * 
	 * @param element
	 * @param extensionName
	 */
	public SimplePeerExtensibilityProvider(DesignElement element, String extensionName) {
		super(element, extensionName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.extension.PeerExtensibilityProvider#
	 * handleInvalidPropertyValue(java.lang.String, java.lang.Object)
	 */
	public void handleInvalidPropertyValue(String propName, Object value) {
		assert value != null;
		assert propName != null;

		PropertyDefn defn = element.getPropertyDefn(propName);
		if (defn.isExtended()) {
			if (invalidValueMap == null)
				invalidValueMap = new LinkedHashMap<String, UndefinedPropertyInfo>(ModelUtil.MAP_CAPACITY_LOW);
			String extensionVersion = element.getStringProperty(element.getRoot(),
					IExtendedItemModel.EXTENSION_VERSION_PROP);
			UndefinedPropertyInfo infor = new UndefinedPropertyInfo(propName, value, extensionVersion);
			invalidValueMap.put(propName, infor);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.extension.PeerExtensibilityProvider#
	 * handleUndefinedChildren(java.lang.String,
	 * org.eclipse.birt.report.model.core.DesignElement)
	 */
	public void handleIllegalChildren(String propName, DesignElement child) {
		ElementPropertyDefn propDefn = element.getPropertyDefn(propName);
		assert propDefn != null;
		assert child != null;
		if (illegalChildrenMap == null)
			illegalChildrenMap = new HashMap<String, List<UndefinedChildInfo>>();
		List<UndefinedChildInfo> childList = illegalChildrenMap.get(propName);
		if (childList == null)
			childList = new ArrayList<UndefinedChildInfo>();

		int count = childList.size();
		List<Object> contents = (List) element.getProperty(null, propDefn);
		count += contents == null ? 0 : contents.size();
		UndefinedChildInfo infor = new UndefinedChildInfo(child, count);
		childList.add(infor);

		illegalChildrenMap.put(propName, childList);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.extension.PeerExtensibilityProvider#
	 * handleUndefinedProperty(java.lang.String, java.lang.Object)
	 */
	public void handleUndefinedProperty(String propName, Object value) {
		assert propName != null;
		assert value != null;

		if (undefinedPropertyMap == null)
			undefinedPropertyMap = new LinkedHashMap<String, UndefinedPropertyInfo>(ModelUtil.MAP_CAPACITY_LOW);

		String extensionVersion = element.getStringProperty(element.getRoot(),
				IExtendedItemModel.EXTENSION_VERSION_PROP);
		// now we can only handle simple type, such as int, string, simple value
		// list; other complex types, such as structure, structure list, we can
		// not handle
		UndefinedPropertyInfo infor = new UndefinedPropertyInfo(propName, value, extensionVersion);
		undefinedPropertyMap.put(propName, infor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.parser.treebuild.IContentHandler#getContentTree
	 * ()
	 */
	public ContentTree getContentTree() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.extension.PeerExtensibilityProvider#copyFrom
	 * (org.eclipse.birt.report.model.extension.PeerExtensibilityProvider)
	 */

	public void copyFromWithNonElementType(PeerExtensibilityProvider source) {
		super.copyFromWithNonElementType(source);

		SimplePeerExtensibilityProvider provider = (SimplePeerExtensibilityProvider) source;
		invalidValueMap = null;
		illegalChildrenMap = null;
		undefinedPropertyMap = null;

		// handle invalid value map
		if (provider.invalidValueMap != null && !provider.invalidValueMap.isEmpty()) {
			invalidValueMap = new LinkedHashMap<String, UndefinedPropertyInfo>(ModelUtil.MAP_CAPACITY_LOW);
			Iterator<String> iter = provider.invalidValueMap.keySet().iterator();
			while (iter.hasNext()) {
				UndefinedPropertyInfo infor = provider.invalidValueMap.get(iter.next());
				if (infor != null) {
					UndefinedPropertyInfo clonedInfor = new UndefinedPropertyInfo(infor.getPropName(), infor.getValue(),
							infor.getExtensionVersion());
					invalidValueMap.put(clonedInfor.getPropName(), clonedInfor);
				}
			}
		}

		// handle undefined property
		if (provider.undefinedPropertyMap != null && !provider.undefinedPropertyMap.isEmpty()) {
			undefinedPropertyMap = new LinkedHashMap<String, UndefinedPropertyInfo>(ModelUtil.MAP_CAPACITY_LOW);

			// now the value is simple type, so do this simple handle; otherwise
			// we will handle complex type to do deep clone
			Iterator<String> iter = provider.undefinedPropertyMap.keySet().iterator();
			while (iter.hasNext()) {
				UndefinedPropertyInfo infor = provider.undefinedPropertyMap.get(iter.next());
				if (infor != null) {
					UndefinedPropertyInfo clonedInfor = new UndefinedPropertyInfo(infor.getPropName(), infor.getValue(),
							infor.getExtensionVersion());
					undefinedPropertyMap.put(clonedInfor.getPropName(), clonedInfor);
				}
			}
		}

		// handle undefined children
		if (provider.illegalChildrenMap != null && !provider.illegalChildrenMap.isEmpty()) {
			illegalChildrenMap = getCopiedIllegalContents(provider.illegalChildrenMap);
		}
	}

	/**
	 * Gets a deep cloned map for illegal contents.
	 * 
	 * @param illegalContentsMap
	 * @return cloned map
	 */
	public static Map<String, List<UndefinedChildInfo>> getCopiedIllegalContents(
			Map<String, List<UndefinedChildInfo>> illegalContentsMap) {
		if (illegalContentsMap == null || illegalContentsMap.isEmpty())
			return Collections.emptyMap();

		Map<String, List<UndefinedChildInfo>> ret = new HashMap<String, List<UndefinedChildInfo>>();
		Iterator<Entry<String, List<UndefinedChildInfo>>> iter = illegalContentsMap.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, List<UndefinedChildInfo>> entry = iter.next();
			String propName = entry.getKey();
			List<UndefinedChildInfo> childList = entry.getValue();
			if (childList != null && !childList.isEmpty()) {
				List<UndefinedChildInfo> clonedList = new ArrayList<UndefinedChildInfo>();
				for (int i = 0; i < childList.size(); i++) {
					UndefinedChildInfo infor = childList.get(i);
					UndefinedChildInfo clonedInfor = new UndefinedChildInfo(null, -1);
					clonedInfor.copyFrom(infor);
					clonedList.add(clonedInfor);
				}

				ret.put(propName, clonedList);
			}
		}

		return ret;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.extension.PeerExtensibilityProvider#
	 * getIllegalChildren()
	 */
	public Map<String, List<UndefinedChildInfo>> getIllegalContents() {
		return (Map<String, List<UndefinedChildInfo>>) (illegalChildrenMap == null ? Collections.emptyMap()
				: illegalChildrenMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.extension.PeerExtensibilityProvider#
	 * getInvalidPropertyValueMap()
	 */
	public Map<String, UndefinedPropertyInfo> getInvalidPropertyValueMap() {
		return (Map<String, UndefinedPropertyInfo>) (this.invalidValueMap == null ? Collections.emptyMap()
				: invalidValueMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.extension.PeerExtensibilityProvider#
	 * getUndefinedPropertyMap()
	 */
	public Map<String, UndefinedPropertyInfo> getUndefinedPropertyMap() {
		return (Map<String, UndefinedPropertyInfo>) (this.undefinedPropertyMap == null ? Collections.emptyMap()
				: undefinedPropertyMap);
	}

	/**
	 * 
	 */
	static public class UndefinedChildInfo {

		/**
		 * The child that can not be inserted to container.
		 */
		protected DesignElement child;

		/**
		 * The index where the child resides in the xml source.
		 */
		protected int index;

		/**
		 * Constructs the infor by the child element and the position.
		 * 
		 * @param child
		 * @param index
		 */
		UndefinedChildInfo(DesignElement child, int index) {
			this.child = child;
			this.index = index;
		}

		/**
		 * Copies this from the specified source information.
		 * 
		 * @param source
		 */
		void copyFrom(UndefinedChildInfo source) {
			if (source == null)
				return;
			this.child = source.child;
			this.index = source.index;
			if (child != null) {
				child = ModelUtil.getCopy(child);
			}
		}

		/**
		 * 
		 * @return
		 */
		public DesignElement getChild() {
			return child;
		}

		/**
		 * 
		 * @return
		 */
		public int getIndex() {
			return index;
		}
	}
}
