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

import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.AnyElementState;
import org.eclipse.birt.report.model.util.VersionUtil;
import org.xml.sax.SAXException;

/**
 * This class provides parser state for the top-level Report element.
 *
 */

public class ReportState extends ModuleState {

	/**
	 * Constructs the report state with the design file parser handler.
	 *
	 * @param theHandler The design parser handler.
	 */

	public ReportState(ModuleParserHandler theHandler) {
		super(theHandler);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java
	 * .lang.String)
	 */

	@Override
	public AbstractParseState startElement(String tagName) {
		if (handler.isReadOnlyModuleProperties) {
			return super.startElement(tagName);
		}

		if (tagName.equalsIgnoreCase(DesignSchemaConstants.TRANSLATIONS_TAG)) {
			return new TranslationsState();
		}
		if (tagName.equalsIgnoreCase(DesignSchemaConstants.PARAMETERS_TAG)) {
			return new ParametersState(handler, getElement(), IModuleModel.PARAMETER_SLOT);
		}
		if (tagName.equalsIgnoreCase(DesignSchemaConstants.DATA_SOURCES_TAG)) {
			return new DataSourcesState(handler, getElement(), IModuleModel.DATA_SOURCE_SLOT);
		}
		if (tagName.equalsIgnoreCase(DesignSchemaConstants.DATA_SETS_TAG)) {
			return new DataSetsState(handler, getElement(), IModuleModel.DATA_SET_SLOT);
		}
		if (tagName.equalsIgnoreCase(DesignSchemaConstants.STYLES_TAG)) {
			return new StylesState(handler, getElement(), IReportDesignModel.STYLE_SLOT);
		}
		if (tagName.equalsIgnoreCase(DesignSchemaConstants.PAGE_SETUP_TAG)) {
			return new PageSetupState(handler, getElement(), IModuleModel.PAGE_SLOT);
		}
		if (tagName.equalsIgnoreCase(DesignSchemaConstants.COMPONENTS_TAG)) {
			return new ComponentsState(handler, getElement(), IModuleModel.COMPONENT_SLOT);
		}
		if (tagName.equalsIgnoreCase(DesignSchemaConstants.BODY_TAG)) {
			return new BodyState(handler, getElement(), IReportDesignModel.BODY_SLOT);
		}
		if (tagName.equalsIgnoreCase(DesignSchemaConstants.SCRATCH_PAD_TAG)) {
			return new ComponentsState(handler, getElement(), IReportDesignModel.SCRATCH_PAD_SLOT);
		}
		if (tagName.equalsIgnoreCase(DesignSchemaConstants.PROPERTY_TAG)) {
			return new PropertyState(handler, getElement());
		}
		if (tagName.equalsIgnoreCase(DesignSchemaConstants.TEMPLATE_PARAMETER_DEFINITIONS_TAG)) {
			return new TemplateParameterDefinitionsState();
		}
		if (tagName.equalsIgnoreCase(DesignSchemaConstants.CUBES_TAG)) {
			return new CubesState(handler, getElement(), IReportDesignModel.CUBE_SLOT);
		}
		if (tagName.equalsIgnoreCase(DesignSchemaConstants.THEMES_TAG)) {
			return new ThemesState(handler, getElement(), IReportDesignModel.THEMES_SLOT);
		}
		return super.startElement(tagName);
	}

	/**
	 * Parses the contents of the body tag that contains the list of top-level
	 * sections.
	 */

	static class BodyState extends SlotState {

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement
		 * (java.lang.String)
		 */

		protected BodyState(ModuleParserHandler handler, DesignElement container, int slot) {
			super(handler, container, slot);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement
		 * (java.lang.String)
		 */

		@Override
		public AbstractParseState startElement(String tagName) {
			int tagValue = tagName.toLowerCase().hashCode();
			if (ParserSchemaConstants.TEXT_TAG == tagValue) {
				return new TextItemState(handler, container, ReportDesign.BODY_SLOT);
			}
			if (ParserSchemaConstants.GRID_TAG == tagValue) {
				return new GridItemState(handler, container, ReportDesign.BODY_SLOT);
			}
			if (ParserSchemaConstants.FREE_FORM_TAG == tagValue) {
				return new FreeFormState(handler, container, ReportDesign.BODY_SLOT);
			}
			if (ParserSchemaConstants.LIST_TAG == tagValue) {
				return new ListItemState(handler, container, ReportDesign.BODY_SLOT);
			}
			if (ParserSchemaConstants.TABLE_TAG == tagValue) {
				return new TableItemState(handler, container, ReportDesign.BODY_SLOT);
			}
			if (ParserSchemaConstants.LABEL_TAG == tagValue) {
				return new LabelState(handler, container, ReportDesign.BODY_SLOT);
			}
			if (ParserSchemaConstants.IMAGE_TAG == tagValue) {
				return new ImageState(handler, container, ReportDesign.BODY_SLOT);
			}
			if (ParserSchemaConstants.DATA_TAG == tagValue) {
				return new DataItemState(handler, container, ReportDesign.BODY_SLOT);
			}
			if ((ParserSchemaConstants.INCLUDE_TAG == tagValue) || (ParserSchemaConstants.TOC_TAG == tagValue)) {
				return new AnyElementState(handler);
			}
			if (ParserSchemaConstants.EXTENDED_ITEM_TAG == tagValue) {
				return new ExtendedItemState(handler, container, ReportDesign.BODY_SLOT);
			}
			if (ParserSchemaConstants.MULTI_LINE_DATA_TAG == tagValue
					|| ParserSchemaConstants.TEXT_DATA_TAG == tagValue) {
				return new TextDataItemState(handler, container, ReportDesign.BODY_SLOT);
			}
			if (ParserSchemaConstants.TEMPLATE_REPORT_ITEM_TAG == tagValue) {
				return new TemplateReportItemState(handler, container, ReportDesign.BODY_SLOT);
			}
			return super.startElement(tagName);
		}
	}

	/**
	 * Parses the contents of the list of data sources.
	 */

	class TemplateParameterDefinitionsState extends InnerParseState {

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement
		 * (java.lang.String)
		 */

		@Override
		public AbstractParseState startElement(String tagName) {
			int tagValue = tagName.toLowerCase().hashCode();
			if (ParserSchemaConstants.TEMPLATE_PARAMETER_DEFINITION_TAG == tagValue) {
				return new TemplateParameterDefinitionState(handler, module,
						ReportDesign.TEMPLATE_PARAMETER_DEFINITION_SLOT);
			}
			return super.startElement(tagName);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */
	@Override
	public void end() throws SAXException {
		super.end();

		// in version 3.2.20, we change the default value of layoutPreference
		// from auto to fixed, do backward-compatibility about this
		if (handler.versionNumber < VersionUtil.VERSION_3_2_20) {
			ElementPropertyDefn propDefn = module.getPropertyDefn(IReportDesignModel.LAYOUT_PREFERENCE_PROP);
			assert propDefn != null;
			String layoutPreference = (String) module.getLocalProperty(module, propDefn);
			if (layoutPreference == null) {
				module.setProperty(propDefn, DesignChoiceConstants.REPORT_LAYOUT_PREFERENCE_AUTO_LAYOUT);
			}
		}
	}

}
