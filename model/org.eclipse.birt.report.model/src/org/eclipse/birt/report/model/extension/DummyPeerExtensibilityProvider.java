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

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.model.api.ModelException;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IPropertyDefinition;
import org.eclipse.birt.report.model.api.extension.UndefinedPropertyInfo;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.extension.SimplePeerExtensibilityProvider.UndefinedChildInfo;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.parser.treebuild.ContentTree;

/**
 *
 */

public final class DummyPeerExtensibilityProvider extends PeerExtensibilityProvider {

	private ContentTree contentTree = null;

	/**
	 *
	 * @param element
	 * @param extensionName
	 */
	public DummyPeerExtensibilityProvider(DesignElement element, String extensionName) {
		super(element, extensionName);
		initializeContentTree();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.extension.PeerExtensibilityProvider#
	 * handleInvalidPropertyValue(java.lang.String, java.lang.Object)
	 */
	@Override
	public void handleInvalidPropertyValue(String propName, Object value) {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.extension.PeerExtensibilityProvider#
	 * handleUndefinedChildren(java.lang.String,
	 * org.eclipse.birt.report.model.core.DesignElement)
	 */
	@Override
	public void handleIllegalChildren(String propName, DesignElement child) {
		// do nothing

	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.extension.PeerExtensibilityProvider#
	 * handleUndefinedProperty(java.lang.String, java.lang.Object)
	 */
	@Override
	public void handleUndefinedProperty(String propName, Object value) {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.parser.treebuild.IContentHandler#getTree()
	 */

	@Override
	public ContentTree getContentTree() {
		return this.contentTree;
	}

	/**
	 * Initializes the content tree.
	 */

	public void initializeContentTree() {
		if (contentTree == null) {
			contentTree = new ContentTree();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.extension.PeerExtensibilityProvider#copyFrom
	 * (org.eclipse.birt.report.model.extension.PeerExtensibilityProvider)
	 */

	@Override
	public void copyFromWithNonElementType(PeerExtensibilityProvider source) {
		super.copyFromWithNonElementType(source);

		// copy content tree
		try {
			this.contentTree = null;
			this.contentTree = (ContentTree) ((DummyPeerExtensibilityProvider) source).contentTree.clone();
		} catch (CloneNotSupportedException e) {
			assert false;
			// do nothing
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.extension.PeerExtensibilityProvider#
	 * getExtensionProperty(java.lang.String)
	 */
	public Object getExtensionProperty(String propName) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.extension.PeerExtensibilityProvider#
	 * setExtensionProperty
	 * (org.eclipse.birt.report.model.metadata.ElementPropertyDefn,
	 * java.lang.Object)
	 */
	@Override
	public void setExtensionProperty(ElementPropertyDefn prop, Object value) {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.extension.PeerExtensibilityProvider#
	 * getPropertyDefn(java.lang.String)
	 */
	@Override
	public ElementPropertyDefn getPropertyDefn(String propName) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.extension.PeerExtensibilityProvider#
	 * getModelMethodDefns()
	 */
	@Override
	public List<IElementPropertyDefn> getModelMethodDefns() {
		return element.getDefn().getMethods();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.extension.PeerExtensibilityProvider#
	 * getScriptPropertyDefinition()
	 */
	@Override
	public IPropertyDefinition getScriptPropertyDefinition() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.extension.PeerExtensibilityProvider#
	 * getPropertyDefns()
	 */
	@Override
	public List<IElementPropertyDefn> getPropertyDefns() {
		List<IElementPropertyDefn> list = element.getDefn().getProperties();
		List<UserPropertyDefn> userProps = element.getUserProperties();
		if (userProps != null) {
			list.addAll(userProps);
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.extension.PeerExtensibilityProvider#
	 * hasLocalPropertyValues()
	 */
	@Override
	public boolean hasLocalPropertyValues() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.extension.PeerExtensibilityProvider#
	 * hasLocalPropertyValuesOnOwnModel()
	 */
	@Override
	public boolean hasLocalPropertyValuesOnOwnModel() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.extension.PeerExtensibilityProvider#
	 * initializeReportItem(org.eclipse.birt.report.model.core.Module)
	 */
	@Override
	public void initializeReportItem(Module module) throws ExtendedElementException {
		if (extensionName == null) {
			throw new ExtendedElementException(element, ModelException.PLUGIN_ID,
					SemanticError.DESIGN_EXCEPTION_MISSING_EXTENSION, null);
		}
		throw new ExtendedElementException(element, ModelException.PLUGIN_ID,
				SemanticError.DESIGN_EXCEPTION_EXTENSION_NOT_FOUND, null);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.extension.PeerExtensibilityProvider#
	 * isExtensionModelProperty(java.lang.String)
	 */
	@Override
	public boolean isExtensionModelProperty(String propName) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.extension.PeerExtensibilityProvider#
	 * isExtensionXMLProperty(java.lang.String)
	 */
	@Override
	public boolean isExtensionXMLProperty(String propName) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.extension.PeerExtensibilityProvider#
	 * setEncryptionHelper
	 * (org.eclipse.birt.report.model.metadata.ElementPropertyDefn,
	 * java.lang.String)
	 */
	@Override
	public void setEncryptionHelper(ElementPropertyDefn propDefn, String encryptionID) {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.extension.PeerExtensibilityProvider#
	 * getIllegalChildren()
	 */
	@Override
	public Map<String, List<UndefinedChildInfo>> getIllegalContents() {
		return Collections.emptyMap();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.extension.PeerExtensibilityProvider#
	 * getInvalidPropertyValueMap()
	 */
	@Override
	public Map<String, UndefinedPropertyInfo> getInvalidPropertyValueMap() {
		return Collections.emptyMap();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.extension.PeerExtensibilityProvider#
	 * getUndefinedPropertyMap()
	 */
	@Override
	public Map<String, UndefinedPropertyInfo> getUndefinedPropertyMap() {
		return Collections.emptyMap();
	}
}
