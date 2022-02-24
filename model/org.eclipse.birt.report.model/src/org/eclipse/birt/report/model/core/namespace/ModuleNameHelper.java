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

package org.eclipse.birt.report.model.core.namespace;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.report.model.api.extension.IMessages;
import org.eclipse.birt.report.model.api.extension.IReportItemFactory;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.olap.Level;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ModelMessages;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ExtensionElementDefn;
import org.eclipse.birt.report.model.metadata.NamePropertyType;
import org.eclipse.birt.report.model.metadata.PeerExtensionElementDefn;

/**
 * 
 */
public class ModuleNameHelper extends AbstractNameHelper {

	protected Module module = null;

	/**
	 * The array of cached content names. These elements have not been added to the
	 * name space in fact. However, name of them are reserved and can not be used
	 * again to avoid the duplicate. This may be used when some extensions is not
	 * well-parsed or other reasons.
	 */
	private HashMap<String, Set<String>> cachedContentNames;

	/**
	 * This map to store all level elements for the backward compatibility after
	 * convert level name to local unique in dimension. It just used in parser.
	 * After parser, we will clear it.
	 */
	private Map<String, DesignElement> cachedLevelNames = new HashMap<String, DesignElement>();

	/**
	 * 
	 * @param module
	 */
	public ModuleNameHelper(Module module) {
		super();
		this.module = module;
		cachedContentNames = new HashMap<String, Set<String>>();
	}

	protected INameContext createNameContext(String id) {
		return NameContextFactory.createModuleNameContext(module, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.namespace.INameHelper#getUniqueName
	 * (org.eclipse.birt.report.model.core.DesignElement, java.lang.String)
	 */
	public String getUniqueName(String namespaceId, DesignElement element, String namePrefix) {
		if (element == null)
			return null;

		ElementDefn eDefn = (ElementDefn) element.getDefn();

		String name = element.getName();
		if (StringUtil.isBlank(name)) {
			// Use the given prefix if the element name is null
			name = namePrefix;
		}

		name = StringUtil.trimString(name);
		// replace all the illegal chars with '_'
		name = NamePropertyType.validateName(name);

		// Some elements can have a blank name.
		if (eDefn.getNameOption() == MetaDataConstants.NO_NAME)
			return null;

		if (eDefn.getNameOption() == MetaDataConstants.OPTIONAL_NAME && name == null && module instanceof ReportDesign)
			return null;

		if (module instanceof Library && element instanceof StyleElement && element.getContainer() == null
				&& name != null) {
			return name;
		}

		// If the element already has a unique name, return it.
		NameSpace nameSpace = getCachedNameSpace(namespaceId);
		Set<String> cachedContentNames = getCachedContentNames(namespaceId);
		NameSpace moduleNameSpace = getNameContext(namespaceId).getNameSpace();

		String validName = name;
		if (element instanceof StyleElement)
			validName = validName == null ? null : validName.toLowerCase();
		if (validName != null && isValidInNameSpace(nameSpace, element, validName)
				&& isValidInNameSpace(moduleNameSpace, element, validName) && !cachedContentNames.contains(validName))
			return name;

		// If the element has no name, create it as "New<new name>" where
		// "<new name>" is the new element display name for the element. Both
		// "New" and the new element display name are localized to the user's
		// locale.

		if (name == null) {
			// When creating a new report element which requires a name, the
			// default name will be "New" followed by the element name, such as
			// "New Label"; also, if "NewLabel" already exists, then a number
			// will be appended, such as "NewLabel1", etc.

			if (element instanceof ExtendedItem) {
				ExtensionElementDefn extDefn = ((ExtendedItem) element).getExtDefn();

				PeerExtensionElementDefn peerDefn = (PeerExtensionElementDefn) extDefn;
				IReportItemFactory peerFactory = peerDefn.getReportItemFactory();

				assert peerFactory != null;

				String extensionDefaultName = null;
				IMessages msgs = peerFactory.getMessages();
				if (msgs != null)
					extensionDefaultName = msgs.getMessage((String) extDefn.getDisplayNameKey(), module.getLocale());

				if (StringUtil.isBlank(extensionDefaultName))
					extensionDefaultName = peerDefn.getName();

				name = ModelMessages.getMessage(MessageConstants.NAME_PREFIX_NEW_MESSAGE);

				name = name + extensionDefaultName;

			} else {
				name = ModelMessages.getMessage("New." //$NON-NLS-1$
						+ element.getDefn().getName());
				name = name.trim();
			}
			name = NamePropertyType.validateName(name);
		}

		// Add a numeric suffix that makes the name unique.

		int index = 0;
		String baseName = name;
		validName = name;
		if (element instanceof StyleElement)
			validName = validName == null ? null : validName.toLowerCase();
		while (nameSpace.contains(validName) || moduleNameSpace.contains(validName)
				|| cachedContentNames.contains(validName)) {
			name = baseName + ++index;
			validName = name;
			if (element instanceof StyleElement)
				validName = validName == null ? null : validName.toLowerCase();
		}

		return name;
	}

	/**
	 * Gets the cached content name list with the given id.
	 * 
	 * @param id the name space id to get
	 * @return the cached content name list with the given id
	 */
	private Set<String> getCachedContentNames(String id) {
		Set<String> cachedNames = cachedContentNames.get(id);
		if (cachedNames == null) {
			cachedNames = new HashSet<String>();
			cachedContentNames.put(id, cachedNames);
		}
		return cachedNames;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.namespace.INameHelper#addContentName
	 * (int, java.lang.String)
	 */
	public void addContentName(String id, String name) {
		Set<String> cachedNames = getCachedContentNames(id);
		if (!cachedNames.contains(name)) {
			cachedNames.add(name);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.namespace.INameHelper#getElement()
	 */
	public DesignElement getElement() {
		return module;
	}

	/**
	 * 
	 * @param level
	 * @return true if level is correctly added, otherwise false
	 */
	public boolean addCachedLevel(DesignElement level) {
		if (!(level instanceof Level))
			return true;
		String name = level.getName();

		if (name == null)
			return true;
		if (cachedLevelNames.get(name) != null && cachedLevelNames.get(name) != level)
			return false;
		this.cachedLevelNames.put(level.getName(), level);
		return true;
	}

	/**
	 * Finds a level by the given qualified name.
	 * 
	 * @param elementName
	 * @return the level if found, otherwise null
	 */
	public Level findCachedLevel(String elementName) {
		if (elementName == null)
			return null;

		String namespace = StringUtil.extractNamespace(elementName);
		String name = StringUtil.extractName(elementName);
		if (namespace == null)
			return (Level) cachedLevelNames.get(name);
		Library lib = module.getLibraryWithNamespace(namespace);
		return lib == null ? null : (Level) ((ModuleNameHelper) lib.getNameHelper()).findCachedLevel(name);
	}

	/**
	 * 
	 */
	public void clearCachedLevels() {
		cachedLevelNames = null;
		List<Library> libs = module.getAllLibraries();
		if (libs == null)
			return;
		for (int i = 0; i < libs.size(); i++) {
			Library lib = libs.get(i);
			((ModuleNameHelper) lib.getNameHelper()).cachedLevelNames = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.namespace.AbstractNameHelper#cacheValues
	 * ()
	 */

	public void cacheValues() {
		// do the cache for all resolved styles.

		AbstractModuleNameContext tmpContext = (AbstractModuleNameContext) getNameContext(Module.STYLE_NAME_SPACE);
		tmpContext.cacheValues();
	}

}
