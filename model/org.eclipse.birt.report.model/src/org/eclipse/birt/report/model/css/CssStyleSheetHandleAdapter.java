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

package org.eclipse.birt.report.model.css;

import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.IncludedCssStyleSheetHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.CssException;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.model.api.elements.structures.IncludedCssStyleSheet;
import org.eclipse.birt.report.model.command.CssCommand;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.AbstractTheme;
import org.eclipse.birt.report.model.elements.ICssStyleSheetOperation;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.interfaces.IAbstractThemeModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;

/**
 * Adapter of CssStyleSheet operation of ThemeHandle/ReportDesignHandle.
 * 
 */

public class CssStyleSheetHandleAdapter {

	private final Module module;

	// element is report design / theme.

	private final DesignElement element;

	/**
	 * Constructor
	 * 
	 * @param module
	 * @param element
	 */

	public CssStyleSheetHandleAdapter(Module module, DesignElement element) {
		this.module = module;
		this.element = element;
	}

	/**
	 * Includes one css with the given css file name. The new css will be appended
	 * to the css list.
	 * 
	 * @param sheetHandle css style sheet handle
	 * @throws SemanticException if error is encountered when handling
	 *                           <code>CssStyleSheet</code> structure list.
	 */

	public final void addCss(CssStyleSheetHandle sheetHandle) throws SemanticException {
		if (sheetHandle == null)
			return;
		if (sheetHandle.getFileName() != null) {
			URL url = module.findResource(sheetHandle.getFileName(), IResourceLocator.CASCADING_STYLE_SHEET);
			if (url == null) {
				throw new CssException(module, new String[] { sheetHandle.getFileName() },
						CssException.DESIGN_EXCEPTION_CSS_NOT_FOUND);
			}
		}
		if (canAddCssStyleSheet(sheetHandle)) {
			CssCommand command = new CssCommand(module, element);
			command.addCss(sheetHandle.getStyleSheet());
		} else {
			throw new CssException(module, new String[] { sheetHandle.getFileName() },
					CssException.DESIGN_EXCEPTION_DUPLICATE_CSS);
		}
	}

	/**
	 * Includes one css with the given css file name. The new css will be appended
	 * to the css list.
	 * 
	 * @param fileName css file name
	 * @throws SemanticException if error is encountered when handling
	 *                           <code>CssStyleSheet</code> structure list.
	 */

	public final void addCssbyProperties(String fileName, String externalCssURI, boolean isUseExternalCss)
			throws SemanticException {
		if (fileName != null) {
			URL url = module.findResource(fileName, IResourceLocator.CASCADING_STYLE_SHEET);
			if (url == null) {
				throw new CssException(module, new String[] { fileName }, CssException.DESIGN_EXCEPTION_CSS_NOT_FOUND);
			}
		}

		if (canAddCssStyleSheetByProperties(fileName, externalCssURI, isUseExternalCss)) {
			CssCommand command = new CssCommand(module, element);
			command.addCssByProperties(fileName, externalCssURI, isUseExternalCss);
		} else {
			throw new CssException(module, new String[] { fileName }, CssException.DESIGN_EXCEPTION_DUPLICATE_CSS);
		}
	}

	/**
	 * Includes one css with the given css file name. The new css will be appended
	 * to the css list.
	 * 
	 * @deprecated
	 * @param fileName css file name
	 * @throws SemanticException if error is encountered when handling
	 *                           <code>CssStyleSheet</code> structure list.
	 */

	public final void addCss(String fileName) throws SemanticException {
		if (fileName == null)
			return;

		CssCommand command = new CssCommand(module, element);
		command.addCss(fileName);
	}

	/**
	 * Drops the given css style sheet of this design file.
	 * 
	 * @param sheetHandle the css to drop
	 * @throws SemanticException if error is encountered when handling
	 *                           <code>CssStyleSheet</code> structure list. Or it
	 *                           maybe because that the given css is not found in
	 *                           the design. Or that the css has descedents in the
	 *                           current module
	 */

	public final void dropCss(CssStyleSheetHandle sheetHandle) throws SemanticException {
		if (sheetHandle == null)
			return;

		CssCommand command = new CssCommand(module, element);
		command.dropCss(sheetHandle.getStyleSheet());
	}

	/**
	 * Check style sheet can be droped or not.
	 * 
	 * @param sheetHandle
	 * @return <code>true</code> can be dropped.else return <code>false</code>
	 */

	public final boolean canDropCssStyleSheet(CssStyleSheetHandle sheetHandle) {
		// element is read-only
		if (!element.canEdit(module)) {
			return false;
		}

		if (sheetHandle == null)
			return false;

		// css not found.

		int position = CssStyleSheetAdapter.getPositionOfCssStyleSheetByProperties(module,
				((ICssStyleSheetOperation) element).getCsses(), sheetHandle.getFileName(),
				sheetHandle.getExternalCssURI(), sheetHandle.isUseExternalCss());
		if (position == -1) {
			return false;
		}
		return true;
	}

	/**
	 * Check style sheet can be added or not.
	 * 
	 * @deprecated
	 * @param fileName
	 * @return <code>true</code> can be added.else return <code>false</code>
	 */

	public final boolean canAddCssStyleSheet(String fileName) {
		// element is read-only

		if (!element.canEdit(module)) {
			return false;
		}
		if (fileName == null) {
			return false;
		}

		URL url = module.findResource(fileName, IResourceLocator.CASCADING_STYLE_SHEET);
		if (url == null) {
			return false;
		}

		CssStyleSheet sheet = CssStyleSheetAdapter.getCssStyleSheetByLocation(module,
				((ICssStyleSheetOperation) element).getCsses(), url);
		if (sheet != null) {
			return false;
		}

		return true;
	}

	/**
	 * Check style sheet can be added or not.
	 * 
	 * @param fileName
	 * @return <code>true</code> can be added.else return <code>false</code>
	 */

	public final boolean canAddCssStyleSheetByProperties(String fileName, String externalCssURI,
			boolean isUseExternalCss) {
		// element is read-only

		if (!element.canEdit(module)) {
			return false;
		}
		URL url = null;

		if (fileName != null) {
			url = module.findResource(fileName, IResourceLocator.CASCADING_STYLE_SHEET);
		}

		CssStyleSheet sheet = CssStyleSheetAdapter.getCssStyleSheetByProperties(module,
				((ICssStyleSheetOperation) element).getCsses(), url, externalCssURI, isUseExternalCss);
		if (sheet != null) {
			return false;
		}

		sheet = CssStyleSheetAdapter.getCssStyleSheetByLocation(module, ((ICssStyleSheetOperation) element).getCsses(),
				url);
		if (sheet != null) {
			return false;
		}
		return true;
	}

	/**
	 * Check style sheet can be added or not.
	 * 
	 * @param sheetHandle
	 * @return <code>true</code> can be added.else return <code>false</code>
	 */

	public final boolean canAddCssStyleSheet(CssStyleSheetHandle sheetHandle) {
		// element is read-only
		if (!element.canEdit(module)) {
			return false;
		}
		if (sheetHandle == null) {
			return false;
		}
		return canAddCssStyleSheetByProperties(sheetHandle.getFileName(), sheetHandle.getExternalCssURI(),
				sheetHandle.isUseExternalCss());
	}

	/**
	 * Reloads the css with the given css file path. If the css style sheet already
	 * is included directly or indirectly, reload it. If the css is not included,
	 * exception will be thrown.
	 * 
	 * @param sheetHandle css style sheet handle
	 * @throws SemanticException if error is encountered when handling
	 *                           <code>IncludeCssStyleSheet</code> structure list.
	 *                           Or it maybe because that the given css is not found
	 *                           in the design. Or that the css has descedents in
	 *                           the current module
	 */

	public final void reloadCss(CssStyleSheetHandle sheetHandle) throws SemanticException {
		if (sheetHandle == null)
			return;

		CssCommand command = new CssCommand(module, element);
		command.reloadCss(sheetHandle.getStyleSheet());
	}

	/**
	 * Includes one css with the given CSS structure. The new css will be appended
	 * to the css list.
	 * 
	 * @param cssStruct the CSS structure
	 * @throws SemanticException if error is encountered when handling
	 *                           <code>CssStyleSheet</code> structure list.
	 */

	public final void addCss(IncludedCssStyleSheet cssStruct) throws SemanticException {

		if (cssStruct == null)
			return;
		if (cssStruct.getFileName() != null) {
			URL url = module.findResource(cssStruct.getFileName(), IResourceLocator.CASCADING_STYLE_SHEET);
			if (url == null) {
				throw new CssException(module, new String[] { cssStruct.getFileName() },
						CssException.DESIGN_EXCEPTION_CSS_NOT_FOUND);
			}
		}
		if (canAddCssStyleSheetByProperties(cssStruct.getFileName(), cssStruct.getExternalCssURI(),
				cssStruct.isUseExternalCss())) {
			CssCommand command = new CssCommand(module, element);
			command.addCss(cssStruct);
		} else {
			throw new CssException(module, new String[] { cssStruct.getFileName() },
					CssException.DESIGN_EXCEPTION_DUPLICATE_CSS);
		}

	}

	/**
	 * Gets <code>IncludedCssStyleSheetHandle</code> by file name.
	 * 
	 * @deprecated
	 * @param fileName the file name
	 * @return the includedCssStyleSheet handle.
	 */
	public IncludedCssStyleSheetHandle findIncludedCssStyleSheetHandleByFileName(String fileName) {

		if (fileName == null)
			return null;

		String propName = null;
		if (element instanceof ReportDesign) {
			propName = IReportDesignModel.CSSES_PROP;

		} else if (element instanceof AbstractTheme) {

			propName = IAbstractThemeModel.CSSES_PROP;
		}

		assert propName != null;

		PropertyHandle propHandle = element.getHandle(module).getPropertyHandle(propName);

		Iterator<Object> handleIter = propHandle.iterator();
		while (handleIter.hasNext()) {
			IncludedCssStyleSheetHandle handle = (IncludedCssStyleSheetHandle) handleIter.next();
			if (fileName.equals(handle.getFileName())) {
				return handle;
			}
		}

		return null;
	}

	/**
	 * Gets <code>IncludedCssStyleSheetHandle</code> by file name.
	 * 
	 * @param fileName the file name
	 * @return the includedCssStyleSheet handle.
	 */
	public IncludedCssStyleSheetHandle findIncludedCssStyleSheetHandleByProperties(String fileName,
			String externalCssURI, boolean useExternalCss) {

		String propName = null;
		if (element instanceof ReportDesign) {
			propName = IReportDesignModel.CSSES_PROP;

		} else if (element instanceof AbstractTheme) {

			propName = IAbstractThemeModel.CSSES_PROP;
		}

		assert propName != null;

		PropertyHandle propHandle = element.getHandle(module).getPropertyHandle(propName);

		Iterator<Object> handleIter = propHandle.iterator();
		while (handleIter.hasNext()) {
			IncludedCssStyleSheetHandle handle = (IncludedCssStyleSheetHandle) handleIter.next();
			if (equals(fileName, externalCssURI, useExternalCss, handle)) {
				return handle;
			}
		}
		return null;
	}

	private boolean equals(String fileName, String externalCssURI, boolean useExternalCss,
			IncludedCssStyleSheetHandle handle) {
		String name = handle.getFileName();
		String uri = handle.getExternalCssURI();
		boolean useCss = handle.isUseExternalCss();

		return (fileName == null && name == null || fileName != null && fileName.equals(name))
				&& (externalCssURI == null && uri == null || externalCssURI != null && externalCssURI.equals(uri))
				&& (useExternalCss == useCss);
	}

	/**
	 * Gets <code>CssStyleSheetHandle</code> by file name.
	 * 
	 * @deprecated
	 * @param fileName the file name.
	 * 
	 * @return the cssStyleSheet handle.
	 */
	public CssStyleSheetHandle findCssStyleSheetHandleByFileName(String fileName) {
		if (fileName == null)
			return null;

		List<CssStyleSheet> list = ((ICssStyleSheetOperation) element).getCsses();
		for (int i = 0; i < list.size(); i++) {
			CssStyleSheet css = list.get(i);
			if (fileName.equals(css.getFileName())) {
				return css.handle(module);
			}
		}
		return null;
	}

	/**
	 * Gets <code>CssStyleSheetHandle</code> by file name.
	 * 
	 * @param fileName the file name.
	 * 
	 * @return the cssStyleSheet handle.
	 */
	public CssStyleSheetHandle findCssStyleSheetHandleByProperties(String fileName, String externalCssURI,
			boolean useExternalCss) {
		if (fileName != null) {
			List<CssStyleSheet> list = ((ICssStyleSheetOperation) element).getCsses();
			for (int i = 0; i < list.size(); i++) {
				CssStyleSheet css = list.get(i);
				String name = css.getFileName();
				String uri = css.getExternalCssURI();
				boolean useCss = css.isUseExternalCss();

				if ((fileName == null && name == null || fileName != null && fileName.equals(name))
						&& (externalCssURI == null && uri == null
								|| externalCssURI != null && externalCssURI.equals(uri))
						&& (useExternalCss == useCss)) {
					return css.handle(module);
				}
			}
		}
		return null;
	}

	/**
	 * Renames as new file name.
	 * 
	 * @deprecated
	 * @param handle      the includedCssStyleSheet handle
	 * @param newFileName the new file name.
	 * @throws SemanticException
	 */
	public void renameCss(IncludedCssStyleSheetHandle handle, String newFileName) throws SemanticException

	{
		if (newFileName == null || handle == null)
			return;

		CssCommand command = new CssCommand(module, element);

		IncludedCssStyleSheet includedCssStyleSheet = command.getIncludedCssStyleSheetByLocation(handle.getFileName());
		command.renameCss(includedCssStyleSheet, newFileName);
	}

	public void renameCssByProperties(IncludedCssStyleSheetHandle handle, String fileName, String externalCssURI,
			boolean isUseExternalCss) throws SemanticException

	{
		if (handle == null)
			return;

		CssCommand command = new CssCommand(module, element);

		IncludedCssStyleSheet includedCssStyleSheet = command.getIncludedCssStyleSheetByProperties(handle.getFileName(),
				handle.getExternalCssURI(), handle.isUseExternalCss());
		command.renameCssByProperties(includedCssStyleSheet, fileName, externalCssURI, isUseExternalCss);
	}

	/**
	 * Checks css style sheet can be renamed or not.
	 * 
	 * @deprecated
	 * @param sheetHandle the included css style sheet handle
	 * @param newFileName the new file name.
	 * @return <code>true</code> can be renamed.else return <code>false</code>
	 */
	public boolean canRenameCss(IncludedCssStyleSheetHandle sheetHandle, String newFileName) {
		if (newFileName == null || sheetHandle == null)
			return false;

		// check the same file name.

		if (sheetHandle.getFileName().equals(newFileName))
			return false;

		CssCommand command = new CssCommand(module, element);
		IncludedCssStyleSheet includedCssStyleSheet = command
				.getIncludedCssStyleSheetByLocation(sheetHandle.getFileName());

		IncludedCssStyleSheet foundIncludedCssStyleSheet = null;
		try {
			foundIncludedCssStyleSheet = command.checkRenameCss(includedCssStyleSheet, newFileName);
		} catch (CssException e) {
			return false;
		}

		// check the same location

		if (foundIncludedCssStyleSheet == sheetHandle.getStructure())
			return false;

		return true;
	}

	/**
	 * Checks css style sheet can be renamed or not.
	 * 
	 * @param sheetHandle the included css style sheet handle
	 * @param newFileName the new file name.
	 * @return <code>true</code> can be renamed.else return <code>false</code>
	 */
	public boolean canRenameCssByProperties(IncludedCssStyleSheetHandle sheetHandle, String newFileName,
			String externalCssURI, boolean isUseExternalCss) {
		if (sheetHandle == null)
			return false;
		CssCommand command = new CssCommand(module, element);
		IncludedCssStyleSheet includedCssStyleSheet = command.getIncludedCssStyleSheetByProperties(
				sheetHandle.getFileName(), sheetHandle.getExternalCssURI(), sheetHandle.isUseExternalCss());

		IncludedCssStyleSheet foundIncludedCssStyleSheet = null;
		try {
			foundIncludedCssStyleSheet = command.checkRenameCssByProperties(includedCssStyleSheet, newFileName,
					externalCssURI, isUseExternalCss);
		} catch (CssException e) {
			return false;
		}
		// check the same location
		if (foundIncludedCssStyleSheet == sheetHandle.getStructure())
			return false;
		return true;
	}

}
