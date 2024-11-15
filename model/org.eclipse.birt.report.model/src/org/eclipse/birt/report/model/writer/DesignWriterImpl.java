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

package org.eclipse.birt.report.model.writer;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.elements.structures.OdaDesignerState;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IInternalReportDesignModel;
import org.eclipse.birt.report.model.parser.DesignSchemaConstants;

/**
 * Writes the design to an XML design file that follows the BIRT design schema.
 * Uses a visitor pattern to traverse each element. BIRT elements support
 * inheritance in several forms. Because of this, the design writer writes only
 * those properties "local" to the element being written -- it does not write
 * inherited properties.
 * <p>
 * Because the XML schema was designed for to be understood by humans, the
 * schema is not a literal representation of the model. Instead, properties are
 * named and grouped in a way that is easiest to explain and understand. This
 * means that the writer has to do a bit more work to write the design, the the
 * extra work here is well worth the savings to the many customers who will read
 * the design format.
 *
 */

class DesignWriterImpl extends ModuleWriter {

	/**
	 * The design context used to convert units.
	 */

	private ReportDesign design;

	/**
	 * Constructs a writer with the specified design.
	 *
	 * @param design the internal representation of the design
	 */

	public DesignWriterImpl(ReportDesign design) {
		this.design = design;
	}

	/**
	 * Write the top-level Report tag, and the properties and contents of the report
	 * itself.
	 *
	 * @param obj the object to write
	 */

	@Override
	public final void visitReportDesign(ReportDesign obj) {
		writer.startElement(DesignSchemaConstants.REPORT_TAG);

		super.visitReportDesign(obj);

		writeSimpleProperties(obj);

		writeSlot(obj);

		writeContentProperties(obj);

		writeImages(obj);

		writer.endElement();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.writer.ModuleWriter#getModule()
	 */

	@Override
	public Module getModule() {
		return design;
	}

	protected void writeSimpleProperties(ReportDesign obj) {
		property(obj, IInternalReportDesignModel.REFRESH_RATE_PROP);
		property(obj, IModuleModel.INITIALIZE_METHOD);
		property(obj, IInternalReportDesignModel.ON_PREPARE_METHOD);
		property(obj, IInternalReportDesignModel.BEFORE_FACTORY_METHOD);
		property(obj, IInternalReportDesignModel.AFTER_FACTORY_METHOD);
		property(obj, IInternalReportDesignModel.BEFORE_RENDER_METHOD);
		property(obj, IInternalReportDesignModel.AFTER_RENDER_METHOD);
		property(obj, IInternalReportDesignModel.ON_PAGE_START_METHOD);
		property(obj, IInternalReportDesignModel.ON_PAGE_END_METHOD);
		property(obj, IInternalReportDesignModel.CLIENT_INITIALIZE_METHOD);

		if (markLineNumber) {
			getModule().addLineNo(obj.getPropertyDefn(IModuleModel.THEME_PROP),
					writer.getLineCounter());
		}
		property(obj, IModuleModel.THEME_PROP);
		resourceKey(obj, IDesignElementModel.DISPLAY_NAME_ID_PROP, IDesignElementModel.DISPLAY_NAME_PROP);
		property(obj, IInternalReportDesignModel.ICON_FILE_PROP);
		property(obj, IInternalReportDesignModel.CHEAT_SHEET_PROP);
		property(obj, IDesignElementModel.EVENT_HANDLER_CLASS_PROP);
		property(obj, IDesignElementModel.NEW_HANDLER_ON_EACH_EVENT_PROP);
		property(obj, IInternalReportDesignModel.LAYOUT_PREFERENCE_PROP);

		property(obj, IInternalReportDesignModel.BIDI_ORIENTATION_PROP);

		property(obj, IInternalReportDesignModel.ENABLE_ACL_PROP);
		property(obj, IInternalReportDesignModel.ACL_EXPRESSION_PROP);
		property(obj, IInternalReportDesignModel.CASCADE_ACL_PROP);
		property(obj, IInternalReportDesignModel.IMAGE_DPI_PROP);
		property(obj, IInternalReportDesignModel.LOCALE_PROP);

		property(obj, IInternalReportDesignModel.EXCEL_DISABLE_GROUPING);
		property(obj, IInternalReportDesignModel.EXCEL_FORCE_AUTO_COL_WIDTHS);
		property(obj, IInternalReportDesignModel.EXCEL_SINGLE_SHEET);
		property(obj, IInternalReportDesignModel.EXCEL_DISPLAY_GRIDLINES);
		property(obj, IInternalReportDesignModel.EXCEL_AUTO_FILTER);
		property(obj, IInternalReportDesignModel.EXCEL_FORCE_RECALCULATION);
		property(obj, IInternalReportDesignModel.EXCEL_IMAGE_SCALING_CELL_DIMENSION);
		property(obj, IInternalReportDesignModel.EXCEL_SINGLE_SHEET_WITH_PAGE_BREAK);
		property(obj, IInternalReportDesignModel.EXCEL_STREAMING_XLSX);
		property(obj, IInternalReportDesignModel.EXCEL_STRUCTURED_HEADER);
		property(obj, IInternalReportDesignModel.EXCEL_PRINT_PAGES_HIGH);
		property(obj, IInternalReportDesignModel.EXCEL_PRINT_PAGES_WIDE);
		property(obj, IInternalReportDesignModel.EXCEL_PRINT_SCALE);
		property(obj, IInternalReportDesignModel.EXCEL_TEMPLATE_FILE);
		property(obj, IInternalReportDesignModel.PDF_VERSION);
		property(obj, IInternalReportDesignModel.PDF_CONFORMANCE);
		property(obj, IInternalReportDesignModel.PDF_ICC_COLOR_TYPE);
		property(obj, IInternalReportDesignModel.PDF_ICC_PROFILE_EXTERNAL);
		property(obj, IInternalReportDesignModel.PDF_DOCUMENTS_PREPEND);
		property(obj, IInternalReportDesignModel.PDF_DOCUMENTS_APPEND);
		property(obj, IInternalReportDesignModel.PDFA_FONT_FALLBACK);
		property(obj, IInternalReportDesignModel.PDF_FONT_CID_SET);
		property(obj, IInternalReportDesignModel.PDFA_DOCUMENT_EMBED_TITLE);

		property(obj, IInternalReportDesignModel.WORD_COMBINE_MARGIN_PADDING);
		property(obj, IInternalReportDesignModel.WORD_LIST_CELL_ADD_EMPTY_PARA);
		property(obj, IInternalReportDesignModel.WORD_WRAP_TABLE_FOR_MARGIN_PADDING);
		property(obj, IInternalReportDesignModel.WORD_WRAP_TABLE_FOR_HEADER_FOOTER);

		// include libraries and scripts

		writeStructureList(obj, IModuleModel.LIBRARIES_PROP);
		writeStructureList(obj, IInternalReportDesignModel.CSSES_PROP);

		// config variables

		writeStructureList(obj, IModuleModel.CONFIG_VARS_PROP);
	}

	protected void writeSlot(ReportDesign obj) {
		writeContents(obj, IInternalReportDesignModel.TEMPLATE_PARAMETER_DEFINITION_SLOT,
				DesignSchemaConstants.TEMPLATE_PARAMETER_DEFINITIONS_TAG);
		writeContents(obj, IModuleModel.PARAMETER_SLOT, DesignSchemaConstants.PARAMETERS_TAG);
		writeContents(obj, IModuleModel.DATA_SOURCE_SLOT, DesignSchemaConstants.DATA_SOURCES_TAG);
		writeContents(obj, IModuleModel.DATA_SET_SLOT, DesignSchemaConstants.DATA_SETS_TAG);
		writeContents(obj, IInternalReportDesignModel.CUBE_SLOT, DesignSchemaConstants.CUBES_TAG);

		// ColorPalette tag

		writeCustomColors(obj);

		// Translations. ( Custom-defined messages )

		writeTranslations(obj);

		writeContents(obj, IInternalReportDesignModel.STYLE_SLOT, DesignSchemaConstants.STYLES_TAG);
		writeContents(obj, IInternalReportDesignModel.THEMES_SLOT, DesignSchemaConstants.THEMES_TAG);
		writeArrangedContents(obj, IModuleModel.COMPONENT_SLOT, DesignSchemaConstants.COMPONENTS_TAG);
		writeContents(obj, IModuleModel.PAGE_SLOT, DesignSchemaConstants.PAGE_SETUP_TAG);
		writeContents(obj, IInternalReportDesignModel.BODY_SLOT, DesignSchemaConstants.BODY_TAG);
		writeContents(obj, IInternalReportDesignModel.SCRATCH_PAD_SLOT, DesignSchemaConstants.SCRATCH_PAD_TAG);
	}

	protected void writeContentProperties(ReportDesign obj) {
		writeContents(obj, IInternalReportDesignModel.PAGE_VARIABLES_PROP);
		writeContents(obj, IInternalReportDesignModel.DATA_OBJECTS_PROP);
	}

	protected void writeImages(ReportDesign obj) {
		// write thumbnail

		try {
			byte[] thumbnail = design.getThumbnail();
			if (thumbnail != null) {
				byte[] data = Base64.getEncoder().encode(design.getThumbnail());
				String value = null;
				if (data != null) {
					value = new String(data, OdaDesignerState.CHARSET);
				}

				if (value != null && value.length() < IndentableXMLWriter.MAX_CHARS_PER_LINE) {
					writeEntry(DesignSchemaConstants.PROPERTY_TAG, IInternalReportDesignModel.THUMBNAIL_PROP, null,
							value.trim(), false);
				} else {
					writeBase64Text(DesignSchemaConstants.PROPERTY_TAG, IInternalReportDesignModel.THUMBNAIL_PROP,
							value);
				}
			}
		} catch (UnsupportedEncodingException e) {
			assert false;
		}

		// Embedded images

		writeEmbeddedImages(obj);
	}
}
