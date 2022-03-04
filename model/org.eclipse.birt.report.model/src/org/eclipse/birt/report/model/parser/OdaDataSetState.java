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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.elements.structures.AggregationArgument;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.OdaResultSetColumn;
import org.eclipse.birt.report.model.api.elements.structures.ResultSetColumn;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.OdaDataSet;
import org.eclipse.birt.report.model.elements.TemplateParameterDefinition;
import org.eclipse.birt.report.model.elements.interfaces.IDataSetModel;
import org.eclipse.birt.report.model.elements.interfaces.IOdaExtendableElementModel;
import org.eclipse.birt.report.model.extension.oda.ODAProvider;
import org.eclipse.birt.report.model.extension.oda.OdaDummyProvider;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.ModelUtil;
import org.eclipse.birt.report.model.util.VersionUtil;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class parses an extended data set. Note: this is temporary syntax, the
 * structure of a data set will be defined by a different team later.
 *
 */

public class OdaDataSetState extends SimpleDataSetState {

	/**
	 * Old extension id of flat file in BIRT 1.0 or before.
	 */

	private static final String OBSOLETE_FLAT_FILE_ID = "org.eclipse.birt.report.data.oda.flatfile.dataSet"; //$NON-NLS-1$

	/**
	 * Extension id of flat file in BIRT 2.0.
	 */

	private static final String NEW_FLAT_FILE_ID = "org.eclipse.datatools.connectivity.oda.flatfile.dataSet"; //$NON-NLS-1$

	/**
	 * <code>true</code> if the extension can be found. Otherwise
	 * <code>false</code>.
	 */

	private boolean isValidExtensionId = true;

	/**
	 * The provider of the element.
	 */

	private ODAProvider provider = null;

	/**
	 * Constructs the data set state with the design parser handler, the container
	 * element and the container slot of the data set.
	 *
	 * @param handler      the design file parser handler
	 * @param theContainer the element that contains this one
	 * @param slot         the slot in which this element appears
	 */

	public OdaDataSetState(ModuleParserHandler handler, DesignElement theContainer, int slot) {
		super(handler, theContainer, slot);
		element = new OdaDataSet();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.parser.DesignParseState#getElement()
	 */

	@Override
	public DesignElement getElement() {
		return element;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.
	 * xml.sax.Attributes)
	 */

	@Override
	public void parseAttrs(Attributes attrs) throws XMLParserException {
		parseODADataSetExtensionID(attrs, false);

		initElement(attrs, true);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java
	 * .lang.String)
	 */
	@Override
	public AbstractParseState startElement(String tagName) {
		int tagValue = tagName.toLowerCase().hashCode();
		if (ParserSchemaConstants.PROPERTY_TAG == tagValue) {
			if (handler.isVersion(VersionUtil.VERSION_0) || handler.isVersion(VersionUtil.VERSION_1_0_0)) {
				return new CompatibleOdaDataSetPropertyState(handler, getElement());
			}
		}

		// if the extension id is OK, use normal procedure to parse the design
		// file. Otherwise, use dummy state to parse.

		if (isValidExtensionId) {
			return super.startElement(tagName);
		}

		return ParseStateFactory.getInstance().createParseState(tagName, handler, element,
				((OdaDummyProvider) provider).getContentTree());
	}

	/**
	 * Parse the attribute of "extensionId" for extendable element.
	 *
	 * @param attrs                 the SAX attributes object
	 * @param extensionNameRequired whether extension name is required
	 */

	private void parseODADataSetExtensionID(Attributes attrs, boolean extensionNameRequired) {
		String extensionID = getAttrib(attrs, DesignSchemaConstants.EXTENSION_ID_ATTRIB);

		if (StringUtil.isBlank(extensionID)) {
			if (!extensionNameRequired) {
				return;
			}

			SemanticError e = new SemanticError(element, SemanticError.DESIGN_EXCEPTION_MISSING_EXTENSION);
			RecoverableError.dealMissingInvalidExtension(handler, e);
			return;
		}
		if (handler.versionNumber < VersionUtil.VERSION_3_0_0) {
			if (OBSOLETE_FLAT_FILE_ID.equalsIgnoreCase(extensionID)) {
				extensionID = NEW_FLAT_FILE_ID;
			}
		}

		setProperty(IOdaExtendableElementModel.EXTENSION_ID_PROP, extensionID);

		provider = ((OdaDataSet) element).getProvider();

		if (provider == null) {
			return;
		}

		if (provider instanceof OdaDummyProvider) {
			SemanticError e = new SemanticError(element, new String[] { extensionID },
					SemanticError.DESIGN_EXCEPTION_EXTENSION_NOT_FOUND);
			RecoverableError.dealMissingInvalidExtension(handler, e);
			isValidExtensionId = false;
		} else {
			// After version 3.2.7 , add convert fuction.

			String newExtensionID = provider.convertExtensionID();
			if (!extensionID.equals(newExtensionID)) {
				setProperty(IOdaExtendableElementModel.EXTENSION_ID_PROP, newExtensionID);
			}

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.parser.SimpleDataSetState#end()
	 */

	@Override
	public void end() throws SAXException {
		super.end();

		OdaDataSet tmpElement = (OdaDataSet) getElement();
		doCompatibleDataSetProperty(tmpElement);
		mergeResultSetAndResultSetHints(tmpElement);
		doCompatibleRemoveResultSetProperty(tmpElement);
		doCompatibleRemoveResultSetHitProperty(tmpElement);
		doCompatibleConvertComputedColumnsConcatenateProperites(tmpElement);

		TemplateParameterDefinition refTemplateParam = tmpElement.getTemplateParameterElement(handler.getModule());
		if (refTemplateParam == null) {
			return;
		}

		OdaDataSet refDefaultElement = (OdaDataSet) refTemplateParam.getDefaultElement();
		doCompatibleDataSetProperty(refDefaultElement);
		mergeResultSetAndResultSetHints(refDefaultElement);
		doCompatibleRemoveResultSetProperty(refDefaultElement);
		doCompatibleRemoveResultSetHitProperty(refDefaultElement);

	}

	/**
	 * Removes 'resultSet' property if version is earlier than 3.2.2.
	 *
	 * @param dataSet the data set element
	 */

	private void doCompatibleRemoveResultSetProperty(OdaDataSet dataSet) {
		if (dataSet == null) {
			return;
		}

		if (handler.versionNumber < VersionUtil.VERSION_3_2_2) {
			dataSet.setProperty(IDataSetModel.RESULT_SET_PROP, null);
		}
	}

	/**
	 * Removes 'resultSetHit' property if version is between 3.2.2 and 3.2.6 .
	 *
	 * @param dataSet the data set element
	 */

	private void doCompatibleRemoveResultSetHitProperty(OdaDataSet dataSet) {
		if (dataSet == null) {
			return;
		}

		if ((handler.versionNumber >= VersionUtil.VERSION_3_2_2)
				&& (handler.versionNumber < VersionUtil.VERSION_3_2_6)) {
			dataSet.setProperty(IDataSetModel.RESULT_SET_HINTS_PROP, null);
		}
	}

	/**
	 * Copies the value from resultSet to resultSetHints.
	 *
	 * @param dataSet the data set element
	 */

	private void doCompatibleDataSetProperty(OdaDataSet dataSet) {
		if (dataSet == null) {
			return;
		}

		if (handler.versionNumber < VersionUtil.VERSION_3_2_2) {
			List dataSetColumns = (List) dataSet.getLocalProperty(handler.module, IDataSetModel.RESULT_SET_PROP);
			Object dataSetHints = dataSet.getLocalProperty(handler.module, IDataSetModel.RESULT_SET_HINTS_PROP);
			if (dataSetHints == null && dataSetColumns != null) {
				dataSet.setProperty(IDataSetModel.RESULT_SET_HINTS_PROP, ModelUtil
						.copyValue(dataSet.getPropertyDefn(IDataSetModel.RESULT_SET_HINTS_PROP), dataSetColumns));
			}
		}
	}

	/**
	 * Parses the old resultSets and resultSetHints list to the new resultSets list.
	 * <p>
	 * resultSetsHints maps to new result set name. resultSet maps to new result set
	 * native name.
	 * <p>
	 * The conversion is done from the file version 3.2.5. It is a part of automatic
	 * conversion for BIRT 2.1.1.
	 *
	 * @param resultSets     the result sets
	 * @param resultSetHints the result set hints
	 */

	private void mergeResultSetAndResultSetHints(OdaDataSet dataSet) {
		if (handler.versionNumber >= VersionUtil.VERSION_3_2_6 || handler.versionNumber < VersionUtil.VERSION_3_2_2) {
			return;
		}

		List resultSets = (List) dataSet.getLocalProperty(handler.module, IDataSetModel.RESULT_SET_PROP);
		List resultSetHints = (List) dataSet.getLocalProperty(handler.module, IDataSetModel.RESULT_SET_HINTS_PROP);

		if (resultSetHints == null) {
			return;
		}

		for (int i = 0; i < resultSetHints.size(); i++) {
			ResultSetColumn hint = (ResultSetColumn) resultSetHints.get(i);

			// use both position and name to match, this can avoid position was
			// not matched and the column name existed already.

			OdaResultSetColumn currentColumn = null;

			if (resultSets != null) {
				currentColumn = findResultSet(resultSets, hint.getColumnName(), hint.getPosition());
			}

			if (currentColumn == null) {
				currentColumn = convertResultSetColumnToOdaResultSetColumn(hint);

				if (resultSets == null) {
					resultSets = new ArrayList();
					dataSet.setProperty(IDataSetModel.RESULT_SET_PROP, resultSets);
				}
				resultSets.add(currentColumn);
			} else {
				String nativeName = currentColumn.getColumnName();
				String columnName = hint.getColumnName();

				currentColumn.setColumnName(columnName);
				currentColumn.setNativeName(nativeName);

				// already in the list, do not add again then.

				if (currentColumn.getDataType() == null) {
					currentColumn.setDataType(hint.getDataType());
				}

				if (currentColumn.getNativeDataType() == null) {
					currentColumn.setNativeDataType(hint.getNativeDataType());
				}

				if (currentColumn.getColumnName() == null) {
					currentColumn.setColumnName(currentColumn.getNativeName());
				}
			}

		}
	}

	/**
	 * Converts old properties, that used translated strings (Message.getString()), to constants.
	 *
	 * @param dataSet the OdaDataSet to convert.
	 */
	private void doCompatibleConvertComputedColumnsConcatenateProperites(OdaDataSet dataSet) {

		if (handler.versionNumber >= VersionUtil.VERSION_3_2_24) {
			return;
		}

		/*
		 * The constants that we should convert to/from is defined in
		 * org.eclipse.birt.data.aggregation.impl.Constants and is because of dependency
		 * reasons to available here.
		 *
		 * These are:
		 *
		 * Constants.SEPARATOR_DISPLAY_NAME -> Constants.SEPARATOR_NAME
		 * Constants.MAXLENGTH__DISPLAY_NAME -> Constants.MAXLENGTH_NAME
		 * Constants.SHOWALLVALUES_DISPLAY_NAME -> Constants.SHOWALLVALUES_NAME
		 *
		 * This conversion will only work if the old report was saved with the
		 * Locale.ROOT locale (en) since all translations of the display names are not
		 * reachable because of the same dependency problems stated about.
		 *
		 * Since old reports and versions of BIRT also have this problem I guess that it
		 * is a decent solution.
		 *
		 */
		List<Object> computedColumnProperty = dataSet.getListProperty(null, IDataSetModel.COMPUTED_COLUMNS_PROP);
		if (computedColumnProperty != null) {
			for (Object item : computedColumnProperty) {
				if (item instanceof ComputedColumn) {
					ComputedColumn computedColumn = (ComputedColumn) item;
					String stringProperty = computedColumn.getAggregateFunction();
					if (Objects.equals(stringProperty, "CONCATENATE")) { //$NON-NLS-1$
						Object property = computedColumn.getProperty(null, ComputedColumn.ARGUMENTS_MEMBER);

						if (property instanceof List) {
							List<Object> arguments = (List<Object>) property;
							for (Object argItem : arguments) {
								if (argItem instanceof AggregationArgument) {
									AggregationArgument aggreagationArg = (AggregationArgument) argItem;
									if (aggreagationArg.getName().equals("Separat&or")) { //$NON-NLS-1$
										aggreagationArg.setName("Separator"); //$NON-NLS-1$
									} else if (aggreagationArg.getName().equals("Ma&x length")) { //$NON-NLS-1$
										aggreagationArg.setName("Maxlength"); //$NON-NLS-1$
									} else if (aggreagationArg.getName().equals("Sho&w all values")) { //$NON-NLS-1$
										aggreagationArg.setName("Showallvalues"); //$NON-NLS-1$
									}
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Returns the result set column in the given position.
	 *
	 * @param pos the position
	 * @return the matched result set column
	 */

	private static OdaResultSetColumn findResultSet(List resultSets, String columnName, Integer pos) {
		for (int i = 0; i < resultSets.size(); i++) {
			OdaResultSetColumn setColumn = (OdaResultSetColumn) resultSets.get(i);

			// position is the first preference. column name is the second.

			if ((pos != null && pos.equals(setColumn.getPosition()))
					|| (columnName != null && columnName.equals(setColumn.getColumnName()))) {
				return setColumn;
			}
		}
		return null;
	}

	/**
	 * Returns a OdaResultSetColumn that maps from ResultSetColumn.
	 *
	 * @param oldColumn the result set column to convert
	 * @return the new OdaResultSetColumn
	 */

	private static OdaResultSetColumn convertResultSetColumnToOdaResultSetColumn(ResultSetColumn oldColumn) {
		assert oldColumn != null;

		OdaResultSetColumn newColumn = StructureFactory.createOdaResultSetColumn();
		newColumn.setColumnName(oldColumn.getColumnName());
		newColumn.setDataType(oldColumn.getDataType());
		newColumn.setNativeDataType(oldColumn.getNativeDataType());

		newColumn.setPosition(oldColumn.getPosition());
		return newColumn;
	}

}
