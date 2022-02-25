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

package org.eclipse.birt.report.model.elements;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.structures.IncludeScript;
import org.eclipse.birt.report.model.api.validators.MasterPageRequiredValidator;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.DesignSessionImpl;
import org.eclipse.birt.report.model.core.LayoutModule;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.css.CssStyleSheet;
import org.eclipse.birt.report.model.css.CssStyleSheetAdapter;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.elements.strategy.CopyPolicy;
import org.eclipse.birt.report.model.util.ContentIterator;

/**
 * This class represents the root element in the report design hierarchy.
 * Contains the list of data sets, data sources, master pages, components, body
 * content, scratch pad and more. Code modules in the report gives
 * specifications for global scripts that apply to the report as a whole.Report
 * design is valid if it is opened without error or with semantic error.
 * Otherwise, it's invalid.
 *
 */

abstract class ReportDesignImpl extends LayoutModule implements IReportDesignModel, ICssStyleSheetOperation {

	private ICssStyleSheetOperation operation = null;

	private HashMap<DesignElement, String> sourceMap;
	private HashMap<String, HashMap<String, DesignElement>> namespaceMap;

	/**
	 * Default constructor.
	 *
	 * @deprecated
	 */

	@Deprecated
	public ReportDesignImpl() {
		super(null);
		initSlots();
		onCreate();
	}

	/**
	 * Constructs the report design with the session.
	 *
	 * @param session the session that owns this design
	 */

	public ReportDesignImpl(DesignSessionImpl session) {
		super(session);
		initSlots();
		onCreate();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#validate(org.eclipse
	 * .birt.report.model.elements.ReportDesign)
	 */

	@Override
	public final List<SemanticException> validate(Module module) {
		List<SemanticException> list = super.validate(module);

		// Must there is more than one master page in setup page

		list.addAll(MasterPageRequiredValidator.getInstance().validate(this, this));

		list.addAll(validateStructureList(module, IMAGES_PROP));
		list.addAll(validateStructureList(module, COLOR_PALETTE_PROP));

		list.addAll(validateStructureList(module, INCLUDE_SCRIPTS_PROP));
		list.addAll(validateStructureList(module, LIBRARIES_PROP));
		list.addAll(validateStructureList(module, PROPERTY_BINDINGS_PROP));

		return list;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	@Override
	public final String getElementName() {
		return ReportDesignConstants.REPORT_DESIGN_ELEMENT;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#getHandle(org.eclipse
	 * .birt.report.model.elements.ReportDesign)
	 */

	@Override
	public final DesignElementHandle getHandle(Module module) {
		return handle();
	}

	/**
	 * Returns an API handle for this element.
	 *
	 * @return an API handle for this element
	 */

	public abstract ReportDesignHandle handle();

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#getIntrinsicProperty
	 * (java.lang.String)
	 */

	/**
	 * Finds an include script by the file name.
	 *
	 * @param fileName the script file name
	 * @return the defined include script that matches, or <code>null</code> if the
	 *         file name was not found in the include scripts list.
	 */

	public final IncludeScript findIncludeScript(String fileName) {
		List<Object> list = (ArrayList) getLocalProperty(null, INCLUDE_SCRIPTS_PROP);
		if (list == null) {
			return null;
		}
		for (int i = 0; i < list.size(); i++) {
			IncludeScript script = (IncludeScript) list.get(i);
			if (script.getFileName() != null && script.getFileName().equals(fileName)) {
				return script;
			}
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.module#getSlotCount()
	 */

	@Override
	protected final int getSlotCount() {
		return SLOT_COUNT;
	}

	/**
	 * Gets all TOCs or bookmarks defined in the slot of the module.
	 *
	 * @param slotId   slot id in which the items hold TOCs.
	 * @param propName property name
	 * @return all TOCs or bookmarks defined in the slot of the module.
	 */

	public final List<Object> collectPropValues(int slotId, String propName) {
		List<Object> rtnList = new ArrayList<>();
		ContentIterator contents = new ContentIterator(this, new ContainerContext(this, slotId));

		while (contents.hasNext()) {
			DesignElement ele = contents.next();
			if (ele.getContainer() != null && ele.getContainer() instanceof MultiViews) {
				// Use getLocalProperty to avoid mutli-view element delegating
				Object obj = ele.getLocalProperty(this, propName);
				if (obj != null) {
					rtnList.add(obj);
				}
			} else {
				Object obj = ele.getProperty(this, propName);
				if (obj != null) {
					rtnList.add(obj);
				}
			}

		}

		return rtnList;
	}

	/**
	 * Gets the thumbnail image in Base64 encoding.
	 *
	 * @return the thumbnail image in Base64 encoding
	 */

	public final byte[] getThumbnail() {
		String data = getStringProperty(this, THUMBNAIL_PROP);
		if (data == null) {
			return null;
		}

		try {
			return data.getBytes(CHARSET);
		} catch (UnsupportedEncodingException e) {
			assert false;
		}

		return null;
	}

	/**
	 * Drops the given css from css list.
	 *
	 * @param css the css to drop
	 * @return the position of the css to drop
	 */

	@Override
	public final int dropCss(CssStyleSheet css) {
		if (operation == null) {
			return -1;
		}
		return operation.dropCss(css);
	}

	/**
	 * Adds the given css to css list.
	 *
	 * @param css the css to insert
	 */

	@Override
	public final void addCss(CssStyleSheet css) {
		if (operation == null) {
			operation = new CssStyleSheetAdapter();
		}
		operation.addCss(css);
	}

	/**
	 * Insert the given css to the given position
	 *
	 * @param css
	 * @param index
	 */

	@Override
	public final void insertCss(CssStyleSheet css, int index) {
		if (operation == null) {
			operation = new CssStyleSheetAdapter();
		}
		operation.insertCss(css, index);
	}

	/**
	 * Returns only csses this module includes directly.
	 *
	 * @return list of csses. each item is <code>CssStyleSheet</code>
	 */

	@Override
	public final List<CssStyleSheet> getCsses() {
		if (operation == null) {
			return Collections.emptyList();
		}
		return operation.getCsses();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.core.Module#doClone(org.eclipse.birt.report
	 * .model.elements.strategy.CopyPolicy)
	 */

	@Override
	public Object doClone(CopyPolicy policy) throws CloneNotSupportedException {
		Module module = (Module) super.doClone(policy);

		if (isCached()) {
			module.cacheValues();
		}

		// clone operation
		this.cloneOperation(this, (ReportDesignImpl) module);
		return module;
	}

	private void cloneOperation(ReportDesignImpl oldReportDesign, ReportDesignImpl newReportDesign)
			throws CloneNotSupportedException {
		CssStyleSheetAdapter oldCssStyleSheetAdapter = (CssStyleSheetAdapter) oldReportDesign.operation;
		if (oldCssStyleSheetAdapter == null) {
			return;
		}
		CssStyleSheetAdapter newCssStyleSheetAdapter = new CssStyleSheetAdapter();
		List<CssStyleSheet> oldSheetList = oldCssStyleSheetAdapter.getCsses();
		List<CssStyleSheet> newSheetList = new ArrayList<>();
		// clone CssStyleSheetAdapter
		if (oldSheetList != null && oldSheetList.size() > 0) {
			for (CssStyleSheet sheet : oldSheetList) {
				// clone CssStyleSheet
				CssStyleSheet newSheet = (CssStyleSheet) sheet.clone();
				newSheet.setContainer(newReportDesign);
				newSheetList.add(newSheet);
			}
		}
		if (newSheetList != null && newSheetList.size() > 0) {
			for (CssStyleSheet sheet : newSheetList) {
				newCssStyleSheetAdapter.addCss(sheet);
			}
		}
		newReportDesign.operation = newCssStyleSheetAdapter;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.Module#cacheValues()
	 */
	@Override
	public final void cacheValues() {
		setIsCached(true);

		super.cacheValues();

		// grid or table lies in body and master page slot
		doCacheValues(BODY_SLOT);
		doCacheValues(IModuleModel.PAGE_SLOT);
	}

	private void doCacheValues(int slotID) {
		ContentIterator iter1 = new ContentIterator(this, new ContainerContext(this, slotID));
		while (iter1.hasNext()) {
			DesignElement tmpElement = iter1.next();
			if (!(tmpElement instanceof ReportItem)) {
				continue;
			}

			((ReportItem) tmpElement).cacheValues();
		}
	}

	/**
	 * Finds a variable element in this report design itself.
	 *
	 * @param name the variable name.
	 * @return variable element.
	 */
	public final VariableElement findVariableElement(String name) {
		return (VariableElement) nameHelper.getNameSpace(VARIABLE_ELEMENT_NAME_SPACE).getElement(name);
	}

	/**
	 * Caches the flatten element.
	 *
	 * @param originalElement the original element
	 * @param flattenElement  the flatten element
	 */
	public void cacheFlattenElement(DesignElement originalElement, DesignElement flattenElement) {
		if (sourceMap == null) {
			sourceMap = new LinkedHashMap<>();
			namespaceMap = new LinkedHashMap<>();
		}
		String namespace = originalElement.getRoot().getNamespace();
		sourceMap.put(flattenElement, namespace);

		HashMap<String, DesignElement> nameMapping = namespaceMap.get(namespace);
		if (nameMapping == null) {
			nameMapping = new LinkedHashMap<>();
			namespaceMap.put(namespace, nameMapping);
		}
		nameMapping.put(originalElement.getName(), flattenElement);
	}

	/**
	 * Gets the flatten element by the original name.
	 *
	 * @param element      a flatten element once in the same namespace
	 * @param originalName the original name of the element
	 *
	 * @return the flatten element, or null if not found
	 */
	public DesignElement getFlattenElement(DesignElement element, String originalName) {
		if (element != null && sourceMap != null) {
			String namespace = sourceMap.get(element);
			if (namespace != null) {
				HashMap<String, DesignElement> nameMapping = namespaceMap.get(namespace);
				if (nameMapping != null) {
					DesignElement flattenElement = nameMapping.get(originalName);
					if (flattenElement != null) {
						return flattenElement;
					}
				}
			}
		}
		return null;
	}
}
