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

package org.eclipse.birt.report.model.elements;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.IllegalOperationException;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;
import org.eclipse.birt.report.model.api.extension.IMessages;
import org.eclipse.birt.report.model.api.extension.IReportItemFactory;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IPredefinedStyle;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.i18n.ModelMessages;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PeerExtensionElementDefn;

import com.ibm.icu.util.ULocale;

/**
 * This class represents a shared style.
 * 
 */

public class Style extends StyleElement implements IStyleModel {

	/**
	 * Default constructor.
	 */

	public Style() {
	}

	/**
	 * Constructs the style element with an optional name.
	 * 
	 * @param theName the optional name
	 */

	public Style(String theName) {
		super(theName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt
	 * .report.model.elements.ElementVisitor)
	 */

	public void apply(ElementVisitor visitor) {
		visitor.visitStyle(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	public String getElementName() {
		return ReportDesignConstants.STYLE_ELEMENT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getHandle(org.eclipse
	 * .birt.report.model.elements.ReportDesign)
	 */

	public DesignElementHandle getHandle(Module module) {
		return handle(module);
	}

	/**
	 * Returns an API handle for this element.
	 * 
	 * @param module the report design of the style
	 * 
	 * @return an API handle for this element
	 */

	public SharedStyleHandle handle(Module module) {
		if (handle == null) {
			handle = new SharedStyleHandle(module, this);
		}
		return (SharedStyleHandle) handle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#validate(org.eclipse
	 * .birt.report.model.elements.ReportDesign)
	 */

	public List<SemanticException> validate(Module module) {
		List<SemanticException> list = super.validate(module);

		list.addAll(validateStyleProperties(module, this));

		return list;
	}

	/**
	 * Checks the style properties of style elements and styled elements.
	 * 
	 * @param module  the report design of the element
	 * @param element the element to check
	 * @return the list of the errors found in validation, each of which is the
	 *         <code>SemanticException</code> object.
	 */

	public static List<SemanticException> validateStyleProperties(Module module, DesignElement element) {
		List<SemanticException> list = new ArrayList<SemanticException>();

		List<Object> rules = element.getListProperty(module, HIGHLIGHT_RULES_PROP);
		if (rules != null) {
			for (int i = 0; i < rules.size(); i++) {
				list.addAll(((HighlightRule) rules.get(i)).validate(module, element));
			}
		}

		return list;
	}

	public String getDisplayLabel(Module module, int level) {
		MetaDataDictionary meta = MetaDataDictionary.getInstance();
		IPredefinedStyle selector = meta.getPredefinedStyle(name);
		if (selector == null)
			return super.getDisplayLabel(module, level);

		// must scan all extension definition to found the corresponding element
		// definition.

		List<IElementDefn> elementDefns = meta.getExtensions();
		ElementDefn elementDefn = null;
		for (int i = 0; i < elementDefns.size(); i++) {
			ElementDefn tmpElementDefn = (ElementDefn) elementDefns.get(i);
			if (name.equalsIgnoreCase(tmpElementDefn.getSelector())) {
				elementDefn = tmpElementDefn;
				break;
			}
		}

		String displayLabel = null;
		if (elementDefn != null) {
			if (!(elementDefn instanceof PeerExtensionElementDefn))
				throw new IllegalOperationException("Only report item extension can be created through this method."); //$NON-NLS-1$

			PeerExtensionElementDefn extDefn = (PeerExtensionElementDefn) elementDefn;
			IReportItemFactory reportItemFactory = extDefn.getReportItemFactory();
			if (reportItemFactory == null)
				return super.getDisplayLabel(module, level);
			IMessages msgs = reportItemFactory.getMessages();
			if (msgs == null)
				return super.getDisplayLabel(module, level);

			ULocale locale = module == null ? ThreadResources.getLocale() : module.getLocale();
			displayLabel = msgs.getMessage(selector.getDisplayNameKey(), locale);
		} else
			displayLabel = ModelMessages.getMessage(selector.getDisplayNameKey());

		if (StringUtil.isBlank(displayLabel)) {
			displayLabel = super.getDisplayLabel(module, level);
		}

		return displayLabel;
	}
}
