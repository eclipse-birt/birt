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

package org.eclipse.birt.report.model.core.namespace;

import java.util.List;

import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.ISlotDefn;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.api.validators.GroupNameValidator;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;
import org.eclipse.birt.report.model.i18n.ModelMessages;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.IContainerDefn;
import org.eclipse.birt.report.model.metadata.NameConfig;
import org.eclipse.birt.report.model.metadata.NamePropertyType;
import org.eclipse.birt.report.model.util.LevelContentIterator;

/**
 * 
 */
class NameExecutorImpl {

	/**
	 * The core design element that this executor focus on.
	 */
	protected DesignElement focus;
	/**
	 * the focus will be added to this container
	 */
	protected ContainerContext container;
	/**
	 * use the namespace in nsContainer
	 */
	protected DesignElement nsContainer;
	// protected String propName;
	protected Module module;

	protected INameHelper nameHelper;
	protected String namespaceId;

	/**
	 * 
	 */
	public static final String NAME_SEPARATOR = "/"; //$NON-NLS-1$

	public NameExecutorImpl(Module module, DesignElement container, DesignElement element) {
		if (element == null)
			throw new IllegalArgumentException("The element can not be null"); //$NON-NLS-1$
		this.module = module;
		this.container = null;
		this.nsContainer = container;
		this.focus = element;
		findNameHelper();
	}

	public NameExecutorImpl(Module module, ContainerContext container, DesignElement element) {
		if (element == null)
			throw new IllegalArgumentException("The element can not be null"); //$NON-NLS-1$
		this.module = module;
		this.container = container;
		this.nsContainer = null;
		this.focus = element;
		findNameHelper();
	}

	protected void findNameHelper() {
		nameHelper = null;
		namespaceId = null;

		ElementDefn focusDefn = (ElementDefn) focus.getDefn();
		if (focusDefn.getNameOption() == MetaDataConstants.NO_NAME) {
			return;
		}
		NameConfig nameConfig = focusDefn.getNameConfig();
		namespaceId = nameConfig.getNameSpaceID();
		// namespace is defined as property in a module
		ElementPropertyDefn tgtProp = (ElementPropertyDefn) nameConfig.getNameProperty();
		if (tgtProp != null) {
			Object value = module.getProperty(module, tgtProp);
			if (value == null) {
				return;
			}
			if (value instanceof List) {
				List valueList = (List) value;
				if (valueList.isEmpty()) {
					return;
				}
				value = valueList.get(0);
			}
			if (value instanceof INameContainer) {
				nameHelper = ((INameContainer) value).getNameHelper();
				return;
			}
			return;
		}

		// namespace is defined as a hold element
		IElementDefn holderDefn = nameConfig.getNameContainer();
		if (holderDefn != null) {
			DesignElement nsParent = nsContainer;
			if (nsParent == null) {
				if (container != null) {
					nsParent = container.getElement();
				} else {
					if (module == focus.getRoot() || focus.getRoot() == null) {
						nsParent = focus.getContainer();
					}
				}

			}
			// then try the container
			while (nsParent != null) {
				if (nsParent.getDefn().isKindOf(holderDefn)) {
					if (nsParent instanceof INameContainer) {
						nameHelper = ((INameContainer) nsParent).getNameHelper();
						return;
					}
				}
				nsParent = nsParent.getContainer();
			}

			// try the module
			if (module != null && module.getDefn().isKindOf(holderDefn)) {
				nameHelper = module.getNameHelper();
				return;
			}
			return;
		}

		// namespace is not defined in the element itself, it may defined in the
		// container property
		ContainerContext context = container;
		if (context == null) {
			if (nsContainer != null) {
				context = getContext(nsContainer, focus);
			}
			if (context == null) {
				context = focus.getContainerInfo();
			}
		}

		while (context != null) {
			DesignElement parent = context.getElement();
			IContainerDefn propDefn = context.getContainerDefn();
			nameConfig = propDefn.getNameConfig();
			if (nameConfig != null && parent instanceof INameContainer) {
				List<IElementDefn> allowElements = propDefn.getAllowedElements();
				if (allowElements != null) {
					for (IElementDefn allowElement : allowElements) {
						if (focusDefn.isKindOf(allowElement)) {
							nameHelper = ((INameContainer) parent).getNameHelper();
							namespaceId = nameConfig.getNameSpaceID();
							return;
						}
					}
				}
			}
			context = parent.getContainerInfo();
		}

		return;
	}

	public boolean hasNamespace() {
		return nameHelper != null;
	}

	public INameHelper getNameHelper() {
		return nameHelper;
	}

	/**
	 * Gets the name space where the name of this element resides.
	 * 
	 * @param module
	 * @return the namespace instance for this executor
	 */
	public final NameSpace getNameSpace() {
		if (nameHelper != null) {
			return nameHelper.getNameSpace(namespaceId);
		}
		return null;
	}

	public final String getNameSpaceId() {
		if (nameHelper != null) {
			return namespaceId;
		}
		return null;
	}

	public String getUniqueName() {
		return getUniqueName(null);
	}

	public String getUniqueName(String prefix) {
		if (focus instanceof GroupElement) {
			return getUniqueGroupName((GroupElement) focus, prefix);
		}
		if (nameHelper != null) {
			return nameHelper.getUniqueName(namespaceId, focus, prefix);
		}
		return null;
	}

	public void makeUniqueName() {
		makeUniqueName(null);
	}

	public void makeUniqueName(String prefix) {
		if (focus instanceof GroupElement) {
			makeUniqueGroupName((GroupElement) focus, prefix);
			return;
		}

		if (nameHelper != null) {
			nameHelper.makeUniqueName(namespaceId, focus, prefix);
		}
	}

	/**
	 * Checks the element name in this name container.
	 * 
	 * <ul>
	 * <li>If the element name is required and duplicate name is found in name
	 * space, rename the element with a new unique name.
	 * <li>If the element name is not required, clear the name.
	 * </ul>
	 * 
	 * @param element the element handle whose name is need to check.
	 */
	public void rename() {
		if (focus instanceof GroupElement) {
			makeUniqueGroupName((GroupElement) focus, null);
		} else {
			ElementDefn defn = (ElementDefn) focus.getDefn();
			if (defn.getNameOption() == MetaDataConstants.REQUIRED_NAME || focus.getRoot() instanceof Library
					|| module instanceof Library || focus.getName() != null) {
				makeUniqueName();
			}
		}
		LevelContentIterator iter = new LevelContentIterator(module, focus, 1);
		while (iter.hasNext()) {
			DesignElement innerElement = iter.next();
			new NameExecutor(module, innerElement).rename();
		}
	}

	public DesignElement getElement(String name) {
		if (nameHelper != null) {
			return nameHelper.getNameSpace(this.namespaceId).getElement(name);
		}
		return null;
	}

	public void dropElement() {
		if (nameHelper != null) {
			nameHelper.dropElement(namespaceId, focus);
		}
	}

	private void makeUniqueGroupName(GroupElement group, String prefix) {
		String name = getUniqueGroupName(group, prefix);
		if (name != null) {
			setUniqueGroupName(group, name);
		}
	}

	/**
	 * Returns a unique name for the group element. The name is unique in the scope
	 * of the table.
	 * 
	 * @param element the group element.
	 * @return unique name of group element.
	 * 
	 */
	private String getUniqueGroupName(GroupElement group, String prefix) {
		if (group == null || group.getContainer() == null)
			return null;

		ListingHandle listing = (ListingHandle) group.getContainer().getHandle(module);

		String groupName = (String) group.getLocalProperty(module, IGroupElementModel.GROUP_NAME_PROP);

		// replace all the illegal chars with '_'
		groupName = NamePropertyType.validateName(groupName);

		if (StringUtil.isBlank(groupName)) {
			GroupElement virtualGroup = (GroupElement) group.getVirtualParent();

			while (virtualGroup != null) {
				groupName = (String) virtualGroup.getLocalProperty(virtualGroup.getRoot(),
						IGroupElementModel.GROUP_NAME_PROP);
				if (!StringUtil.isBlank(groupName)) {
					break;
				}
				virtualGroup = (GroupElement) virtualGroup.getVirtualParent();
			}
		}

		String namePrefix = groupName;
		int level = group.getGroupLevel();

		if (StringUtil.isBlank(namePrefix)) {
			namePrefix = ModelMessages.getMessage("New." //$NON-NLS-1$
					+ group.getDefn().getName());
			namePrefix = namePrefix.trim();
			groupName = namePrefix + level;
		}

		while (true) {
			if (GroupNameValidator.getInstance()
					.validateForRenamingGroup(listing, (GroupHandle) group.getHandle(module), groupName).size() == 0) {
				break;
			}

			groupName = namePrefix + level;
			level++;

		}
		return groupName;
	}

	/**
	 * Creates a unique name for the group element. The name is unique in the scope
	 * of the table.
	 * 
	 * @param element   the group element.
	 * @param groupName name of group element.
	 * 
	 */
	private void setUniqueGroupName(GroupElement group, String groupName) {
		assert groupName != null;
		String localGroupName = (String) group.getLocalProperty(module, IGroupElementModel.GROUP_NAME_PROP);
		if (groupName.equals(localGroupName))
			return;

		group.setProperty(IGroupElementModel.GROUP_NAME_PROP, groupName);
	}

	protected ContainerContext getContext(DesignElement container, DesignElement content) {

		if (content.getContainer() == container) {
			return content.getContainerInfo();
		}
		IElementDefn containerDefn = container.getDefn();
		IElementDefn contentDefn = content.getDefn();

		List<IElementPropertyDefn> props = containerDefn.getProperties();
		for (IElementPropertyDefn prop : props) {
			List<IElementDefn> elements = prop.getAllowedElements();
			if (elements != null) {
				for (IElementDefn element : elements) {
					if (contentDefn.isKindOf(element)) {
						return new ContainerContext(container, prop.getName());
					}
				}
			}
		}
		int slotCount = containerDefn.getSlotCount();
		for (int i = 0; i < slotCount; i++) {
			ISlotDefn slotDefn = containerDefn.getSlot(i);

			List<IElementDefn> elements = slotDefn.getAllowedElements();
			if (elements != null) {
				for (IElementDefn element : elements) {
					if (contentDefn.isKindOf(element)) {
						return new ContainerContext(container, i);
					}
				}
			}
		}

		return null;
	}

}
