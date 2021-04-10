/*******************************************************************************
 * Copyright (c) 2004, 2005, 2006, 2007, 2008, 2009, 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.util.copy;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IDesignElement;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.api.elements.structures.PropertyBinding;
import org.eclipse.birt.report.model.api.util.IElementCopy;
import org.eclipse.birt.report.model.api.util.IPasteStatus;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.api.util.XPathUtil;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.DesignSessionImpl;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.strategy.CopyForPastePolicy;
import org.eclipse.birt.report.model.elements.strategy.DummyCopyPolicy;

/**
 * This policy is a copy policy for pasting, which means, after copying, the
 * original object is deeply cloned, and the target object can be pasted to
 * every where.
 */

class ContextCopyPasteBasePolicy {

	/**
	 * Returns the element with context information such as xpath, element id and
	 * the location of the design module.
	 * 
	 * @param source the source element
	 * @param root   the module of the source element
	 * 
	 * @return the instance of <code>ContextCopiedElement</code> with context
	 *         information.
	 */

	public IElementCopy createCopy(DesignElement source, Module root) {
		String xpath = XPathUtil.getXPath(source.getHandle(root));

		String extendsName = source.getExtendsName();

		String libLocation = null;
		long extendsElementID = DesignElement.NO_ID;

		if (!StringUtil.isBlank(extendsName)) {
			String namespace = StringUtil.extractNamespace(extendsName);
			Library lib = root.getLibraryWithNamespace(namespace);
			if (lib != null)
				libLocation = lib.getLocation();

			DesignElement element = source.getExtendsElement();
			if (element != null)
				extendsElementID = element.getID();
		}

		String location = null;
		if (root != null && root.getSystemId() != null)
			location = root.getLocation();

		DesignElement destination = null;

		try {
			destination = (DesignElement) source.doClone(DummyCopyPolicy.getInstance());
		} catch (CloneNotSupportedException e) {
			destination = null;
			assert false;
		}

		DesignElement localized = null;

		try {
			localized = (DesignElement) source.doClone(CopyForPastePolicy.getInstance());
		} catch (CloneNotSupportedException e) {
			localized = null;
			assert false;
		}

		List<PropertyBinding> propertyBindings = null;
		if (root != null)
			propertyBindings = root.getPropertyBindings(source);

		return new ContextCopiedElement(destination, localized, xpath, location, libLocation, extendsElementID,
				propertyBindings);
	}

	/**
	 * Checks whether the given copy is valid for pasting. Following cases are
	 * invalid:
	 * 
	 * <ul>
	 * <li>the instance is <code>null</code>.
	 * <li>the instance does not contain the localized copy.
	 * </ul>
	 * 
	 * @param context the context of container
	 * @param module  the module of the element to paste
	 * @param copy    the given copy
	 * 
	 * @return <code>true</code> is the copy is good for pasting. Otherwise
	 *         <code>false</code>.
	 */

	public IPasteStatus isValidCopy(ContainerContext context, Module module, IElementCopy copy) {
		PasteStatus status = new PasteStatus();
		if (!(copy instanceof ContextCopiedElement)) {
			status.setPaste(false);
			status.setErrors(null);
			return status;
		}

		DesignElement copied = ((ContextCopiedElement) copy).getLocalizedCopy();

		if (copied == null) {
			status.setPaste(false);
			status.setErrors(null);
			return status;
		}

		List<SemanticException> errors = context.checkContainmentContext(module, copied);
		if ((errors == null || errors.isEmpty()) && (module == null || !module.isReadOnly())) {
			status.setPaste(true);
			status.setErrors(null);
		} else {
			status.setPaste(false);
			status.setErrors(errors);
		}

		return status;
	}

	private DesignElement findElement(String extendName, Module module) {
		if (!StringUtil.isBlank(extendName)) {
			String namespace = StringUtil.extractNamespace(extendName);
			if (namespace != null) {
				Library lib = module.getLibraryWithNamespace(namespace);
				String name = StringUtil.extractName(extendName);
				if (lib != null && name != null) {
					return lib.findElement(name);
				}
			}
		}
		return null;
	}

	// handle some special case, the value is defined in report design and
	// definition is defined in library
	private void preWorkForElement(ContextCopiedElement copy, Module module) {
		DesignElement element = copy.getCopy();
		String extName = element.getExtendsName();

		DesignElement ext = findElement(extName, module);

		if (ext != null && ext.hasUserProperties()) {
			Iterator<String> iter = element.propertyWithLocalValueIterator();
			while (iter.hasNext()) {
				String key = iter.next();
				if (element.getLocalUserPropertyDefn(key) == null && ext.getLocalUserPropertyDefn(key) != null) {
					element.addUserPropertyDefn(ext.getLocalUserPropertyDefn(key));
				}
			}
		}

	}

	// handle some special case, the value is defined in report design and
	// definition is defined in library
	private void postWorkForElement(ContextCopiedElement copy, Module module) {
		DesignElement element = copy.getCopy();
		String extName = element.getExtendsName();
		DesignElement ext = findElement(extName, module);

		if (ext != null && ext.hasUserProperties()) {
			Iterator<UserPropertyDefn> iter = ext.getLocalUserProperties().iterator();
			while (iter.hasNext()) {
				UserPropertyDefn propertyDef = iter.next();
				String name = propertyDef.getName();
				if (element.getLocalUserPropertyDefn(name) != null) {
					element.dropUserPropertyDefn(element.getLocalUserPropertyDefn(name));
				}
			}
		}

	}

	/**
	 * Checks whether the <code>content</code> can be pasted. And if localization is
	 * needed, localize property values to <code>content</code>.
	 * 
	 * @param context the place where the content is to pasted
	 * @param content the content
	 * @param module  the root of the context
	 * @return the element copy that should be added into the context
	 * 
	 */

	public IDesignElement preWorkForPaste(ContainerContext context, IElementCopy content, Module module) {

		ContextCopiedElement copy = null;

		try {
			preWorkForElement((ContextCopiedElement) content, module);
			copy = (ContextCopiedElement) ((ContextCopiedElement) content).clone();
			postWorkForElement((ContextCopiedElement) content, module);
			postWorkForElement((ContextCopiedElement) copy, module);
		} catch (CloneNotSupportedException e) {
			assert false;
			return null;
		}

		String location = copy.getRootLocation();
		if (location == null)
			return copy.getLocalizedCopy();

		DesignElement copiedElement = copy.getCopy();

		DesignSessionImpl session = module.getSession();
		Module copiedRoot = session.getOpenedModule(location);
		if (copiedRoot == null)
			return copy.getLocalizedCopy();

		if (copiedRoot == module)
			return copy.getUnlocalizedCopy();

		String nameSpace = StringUtil.extractNamespace(copiedElement.getExtendsName());

		// if the element is extends, element should be validated whether the
		// localize it or not
		if (!StringUtil.isEmpty(nameSpace)) {
			Library lib = module.getLibraryWithNamespace(nameSpace);
			if (lib == null)
				return copy.getLocalizedCopy();

			long extendsElementID = copy.getExtendsElementID();
			if (extendsElementID == DesignElement.NO_ID)
				return copy.getLocalizedCopy();

			// gets the location of the library which contains the copied
			// extends.
			String libLocation = copy.getLibLocation();
			if (libLocation == null)
				return copy.getLocalizedCopy();

			// validates the location of the library which contains the copied
			// extends is the same as the location of the library of the target
			// container
			if (!libLocation.equals(lib.getLocation()))
				return copy.getLocalizedCopy();

			Library copiedLib = copiedRoot.getLibraryWithNamespace(nameSpace);
			if (copiedLib == null)
				return copy.getLocalizedCopy();

			// validates the location of the newly open library is the same as
			// the location of the library which contains the extends element.

			if (!libLocation.equals(copiedLib.getLocation()))
				return copy.getLocalizedCopy();

			DesignElement libElement = lib.getElementByID(extendsElementID);
			if (libElement == null)
				return copy.getLocalizedCopy();

			DesignElement copyLibElement = copiedLib.getElementByID(extendsElementID);
			if (libElement.getDefn() != copyLibElement.getDefn())
				return copy.getLocalizedCopy();
		}

		return copy.getCopy();
	}

	public void copyPropertyBindings(IElementCopy copy, DesignElementHandle target) throws SemanticException {
		if (target == null || target.getRoot() == null
				|| target.getRoot().getPropertyDefn(IModuleModel.PROPERTY_BINDINGS_PROP) == null)
			return;
		for (PropertyBinding propBinding : ((ContextCopiedElement) copy).getPropertyBindings()) {
			target.setPropertyBinding(propBinding.getName(),
					propBinding.getExpressionProperty(PropertyBinding.VALUE_MEMBER));
		}
	}
}
