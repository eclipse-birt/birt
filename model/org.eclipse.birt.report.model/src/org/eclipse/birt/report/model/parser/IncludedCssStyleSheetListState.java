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

package org.eclipse.birt.report.model.parser;

import java.net.URL;

import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.command.CssException;
import org.eclipse.birt.report.model.api.css.StyleSheetException;
import org.eclipse.birt.report.model.api.elements.structures.IncludedCssStyleSheet;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.css.CssStyleSheet;
import org.eclipse.birt.report.model.css.CssStyleSheetAdapter;
import org.eclipse.birt.report.model.elements.ICssStyleSheetOperation;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.ModelUtil;
import org.xml.sax.SAXException;

/**
 * Parses the simple structure list for "includedCssStyleSheet" property.
 */

public class IncludedCssStyleSheetListState extends ListPropertyState {

	IncludedCssStyleSheetListState(ModuleParserHandler theHandler, DesignElement element) {
		super(theHandler, element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java
	 * .lang.String)
	 */
	public AbstractParseState startElement(String tagName) {
		if (tagName.equalsIgnoreCase(DesignSchemaConstants.STRUCTURE_TAG))
			return new IncludedCssStructureState(handler, element, propDefn);

		return super.startElement(tagName);
	}

	static class IncludedCssStructureState extends CompatibleStructureState {

		IncludedCssStructureState(ModuleParserHandler theHandler, DesignElement element, PropertyDefn propDefn) {
			super(theHandler, element, propDefn);
			lineNumber = handler.getCurrentLineNo();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */

		public void end() throws SAXException {
			super.end();

			IncludedCssStyleSheet includeCss = (IncludedCssStyleSheet) struct;

			String fileName = includeCss.getFileName();
			String externalCssURI = includeCss.getExternalCssURI();
			boolean useExternalCss = includeCss.isUseExternalCss();
			// handle compatibility (No useExternalCss property in design)
			if (externalCssURI != null && !useExternalCss) {
				includeCss.setUseExternalCss(true);
				useExternalCss = true;
			}

			if (!(element instanceof ICssStyleSheetOperation))
				return;

			URL url = null;
			ICssStyleSheetOperation sheetOperation = (ICssStyleSheetOperation) element;
			if (fileName != null) {
				url = handler.module.findResource(fileName, IResourceLocator.CASCADING_STYLE_SHEET);
			}

			CssStyleSheet sheet = CssStyleSheetAdapter.getCssStyleSheetByProperties(handler.module,
					sheetOperation.getCsses(), url, externalCssURI, useExternalCss);

			if (sheet != null) {
				CssException ex = new CssException(handler.module, new String[] { fileName },
						CssException.DESIGN_EXCEPTION_DUPLICATE_CSS);
				handler.getErrorHandler().semanticWarning(ex);
				return;
			}

			if (fileName != null) {
				try {
					sheet = handler.module.loadCss(element, url, fileName);
					sheet.setExternalCssURI(externalCssURI);
					sheet.setUseExternalCss(useExternalCss);
					sheetOperation.addCss(sheet);
				}

				catch (StyleSheetException e) {
					CssException ex = ModelUtil.convertSheetExceptionToCssException(handler.module, includeCss,
							fileName, e);
					handler.getErrorHandler().semanticWarning(ex);
				}
			} else {
				sheet = new CssStyleSheet();
				sheet.setExternalCssURI(externalCssURI);
				sheet.setUseExternalCss(useExternalCss);
				sheet.setContainer(handler.module);
				sheetOperation.addCss(sheet);

			}
		}
	}
}
