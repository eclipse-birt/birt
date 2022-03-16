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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.api.elements.structures.AggregationArgument;
import org.eclipse.birt.report.model.api.elements.structures.CachedMetaData;
import org.eclipse.birt.report.model.api.elements.structures.CalculationArgument;
import org.eclipse.birt.report.model.api.elements.structures.ColumnHint;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.ConfigVariable;
import org.eclipse.birt.report.model.api.elements.structures.CustomColor;
import org.eclipse.birt.report.model.api.elements.structures.DataSetParameter;
import org.eclipse.birt.report.model.api.elements.structures.DateFormatValue;
import org.eclipse.birt.report.model.api.elements.structures.DateTimeFormatValue;
import org.eclipse.birt.report.model.api.elements.structures.DimensionCondition;
import org.eclipse.birt.report.model.api.elements.structures.DimensionJoinCondition;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.elements.structures.FormatValue;
import org.eclipse.birt.report.model.api.elements.structures.HideRule;
import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;
import org.eclipse.birt.report.model.api.elements.structures.IncludeScript;
import org.eclipse.birt.report.model.api.elements.structures.IncludedCssStyleSheet;
import org.eclipse.birt.report.model.api.elements.structures.IncludedLibrary;
import org.eclipse.birt.report.model.api.elements.structures.JoinCondition;
import org.eclipse.birt.report.model.api.elements.structures.LevelAttribute;
import org.eclipse.birt.report.model.api.elements.structures.MapRule;
import org.eclipse.birt.report.model.api.elements.structures.NumberFormatValue;
import org.eclipse.birt.report.model.api.elements.structures.OdaDataSetParameter;
import org.eclipse.birt.report.model.api.elements.structures.OdaDesignerState;
import org.eclipse.birt.report.model.api.elements.structures.OdaLevelAttribute;
import org.eclipse.birt.report.model.api.elements.structures.OdaResultSetColumn;
import org.eclipse.birt.report.model.api.elements.structures.ParamBinding;
import org.eclipse.birt.report.model.api.elements.structures.ParameterFormatValue;
import org.eclipse.birt.report.model.api.elements.structures.PropertyBinding;
import org.eclipse.birt.report.model.api.elements.structures.PropertyMask;
import org.eclipse.birt.report.model.api.elements.structures.ResultSetColumn;
import org.eclipse.birt.report.model.api.elements.structures.Rule;
import org.eclipse.birt.report.model.api.elements.structures.ScriptLib;
import org.eclipse.birt.report.model.api.elements.structures.SearchKey;
import org.eclipse.birt.report.model.api.elements.structures.SelectionChoice;
import org.eclipse.birt.report.model.api.elements.structures.SortHint;
import org.eclipse.birt.report.model.api.elements.structures.SortKey;
import org.eclipse.birt.report.model.api.elements.structures.StringFormatValue;
import org.eclipse.birt.report.model.api.elements.structures.TOC;
import org.eclipse.birt.report.model.api.elements.structures.TimeFormatValue;
import org.eclipse.birt.report.model.api.elements.structures.TimeInterval;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.core.StructureContext;
import org.eclipse.birt.report.model.elements.DataItem;
import org.eclipse.birt.report.model.elements.DataSet;
import org.eclipse.birt.report.model.elements.ImageItem;
import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.elements.interfaces.IDataItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IDataSetModel;
import org.eclipse.birt.report.model.elements.interfaces.IImageItemModel;
import org.eclipse.birt.report.model.elements.interfaces.ILabelModel;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.StructureDefn;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.LineNumberInfo;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Parses one structure. The structure can be either a top level structure on an
 * element or a structure in a list.
 *
 */

public class StructureState extends AbstractPropertyState {

	/**
	 *
	 */

	protected int lineNumber = 1;

	/**
	 * The list property value if this state is used to parse one structure in list.
	 */

	protected List list = null;

	/**
	 * The definition of the list property which this structure is in.
	 */

	PropertyDefn propDefn = null;

	/**
	 * The dictionary for structure class and name mapping.
	 */

	private static Map structDict = null;

	/**
	 * The structure which holds this property as a member.
	 */

	protected Structure parentStruct = null;

	/**
	 * Constructs the state of the structure which is one property.
	 *
	 * @param theHandler the design parser handler
	 * @param element    the element holding this structure to parse
	 */

	StructureState(ModuleParserHandler theHandler, DesignElement element) {
		super(theHandler, element);
	}

	/**
	 * Constructs the state of the structure which is in one structure list.
	 *
	 * @param theHandler   the design parser handler
	 * @param element      the element holding this structure
	 * @param propDefn     the definition of the property which holds this structure
	 * @param parentStruct the structure that contains the current structure
	 */

	StructureState(ModuleParserHandler theHandler, DesignElement element, PropertyDefn propDefn,
			Structure parentStruct) {
		super(theHandler, element);

		assert propDefn != null;

		this.propDefn = propDefn;
		this.parentStruct = parentStruct;
		this.name = propDefn.getName();
	}

	/**
	 * Constructs the state of the structure which is in another structure.
	 *
	 * @param theHandler   the design parser handler
	 * @param element      the element holding this structure
	 * @param parentStruct the structure that contains the current structure
	 */

	StructureState(ModuleParserHandler theHandler, DesignElement element, Structure parentStruct) {
		super(theHandler, element);

		this.parentStruct = parentStruct;
	}

	/**
	 * Constructs the state of the structure which is in one structure list.
	 *
	 * @param theHandler the design parser handler
	 * @param element    the element holding this structure
	 * @param propDefn   the definition of the property which holds this structure
	 * @param theList    the structure list
	 */

	StructureState(ModuleParserHandler theHandler, DesignElement element, PropertyDefn propDefn) {
		super(theHandler, element);

		assert propDefn != null;

		this.propDefn = propDefn;
		this.name = propDefn.getName();
	}

	@Override
	public void setName(String name) {
		super.setName(name);

		propDefn = element.getPropertyDefn(name);
		createStructure();
	}

	/**
	 * Create structure according to struct defn.
	 *
	 */

	private void createStructure() {
		if (struct == null) {
			assert propDefn != null;

			// If the structure has its specific state, the structure will be
			// created by the specific state.

			struct = createStructure((StructureDefn) propDefn.getStructDefn());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.
	 * xml.sax.Attributes)
	 */

	@Override
	public void parseAttrs(Attributes attrs) throws XMLParserException {
		lineNumber = handler.getCurrentLineNo();

		String tmpName = getAttrib(attrs, DesignSchemaConstants.NAME_ATTRIB);

		// if this is a structure that has name.

		if (!StringUtil.isBlank(tmpName) && propDefn == null) {
			name = tmpName;

			if (parentStruct == null) {
				propDefn = element.getPropertyDefn(name);
			} else {
				propDefn = (PropertyDefn) parentStruct.getMemberDefn(name);
			}

		}

		if (propDefn == null || !propDefn.isList()) {
			// if it is not a structure list, it must have the name.

			if (StringUtil.isBlank(name)) {
				handler.getErrorHandler()
						.semanticError(new DesignParserException(DesignParserException.DESIGN_EXCEPTION_NAME_REQUIRED));
				valid = false;
				return;
			} else if (propDefn == null) {
				if (parentStruct == null) {
					propDefn = element.getPropertyDefn(name);
				} else {
					propDefn = (PropertyDefn) parentStruct.getMemberDefn(name);
				}
			}

		}

		if (propDefn == null) {
			handler.getErrorHandler().semanticError(
					new DesignParserException(DesignParserException.DESIGN_EXCEPTION_INVALID_STRUCTURE_NAME));
			valid = false;
			return;
		}

		createStructure();
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
			return new PropertyState(handler, element, propDefn, struct);
		}

		if (ParserSchemaConstants.ENCRYPTED_PROPERTY_TAG == tagValue) {
			return new EncryptedPropertyState(handler, element, propDefn, struct);
		}

		if (ParserSchemaConstants.EXPRESSION_TAG == tagValue) {
			return new ExpressionState(handler, element, propDefn, struct);
		}

		if (ParserSchemaConstants.XML_PROPERTY_TAG == tagValue) {
			return new XmlPropertyState(handler, element, propDefn, struct);
		}

		if (ParserSchemaConstants.LIST_PROPERTY_TAG == tagValue) {
			return new ListPropertyState(handler, element, propDefn, struct);
		}

		if ((ParserSchemaConstants.TEXT_PROPERTY_TAG == tagValue) || (ParserSchemaConstants.HTML_PROPERTY_TAG == tagValue)) {
			return new TextPropertyState(handler, element, struct);
		}

		if (ParserSchemaConstants.STRUCTURE_TAG == tagValue) {
			return new StructureState(handler, element, (Structure) struct);
		}

		if (ParserSchemaConstants.SIMPLE_PROPERTY_LIST_TAG == tagValue) {
			return new SimplePropertyListState(handler, element, propDefn, struct);
		}

		return super.startElement(tagName);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */

	@Override
	public void end() throws SAXException {
		if (handler.markLineNumber && (LineNumberInfo.isLineNumberSuppoerted(struct))) {
			handler.tempLineNumbers.put(struct, Integer.valueOf(lineNumber));
		}

		if (struct != null) {
			if (parentStruct != null) {
				StructureContext context = new StructureContext(parentStruct, propDefn, (Structure) struct);

				if (propDefn.isList()) {
					// structure in a list property.

					context.add((Structure) struct);
				} else {
					((Structure) struct).setContext(context);
					parentStruct.setProperty(propDefn, struct);
				}

			} else {
				StructureContext context = new StructureContext(element, (ElementPropertyDefn) propDefn,
						(Structure) struct);

				// structure property.

				if (propDefn.isList()) {
					// structure in a list property.

					context.add((Structure) struct);
				} else {
					((Structure) struct).setContext(context);
					element.setProperty(name, struct);
				}
			}
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.parser.AbstractPropertyState#generalJumpTo
	 * ()
	 */

	@Override
	protected AbstractParseState generalJumpTo() {
		if (element instanceof Label && ILabelModel.ACTION_PROP.equalsIgnoreCase(name)
				|| element instanceof ImageItem && IImageItemModel.ACTION_PROP.equalsIgnoreCase(name)
				|| element instanceof DataItem && IDataItemModel.ACTION_PROP.equalsIgnoreCase(name)) {
			ActionStructureState state = new ActionStructureState(handler, element);
			state.setName(name);
			return state;
		}

		String propName = propDefn == null ? null : propDefn.getName();

		if ((element instanceof DataSet)) {
			if (IDataSetModel.COMPUTED_COLUMNS_PROP.equalsIgnoreCase(propName)) {
				CompatibleComputedColumnStructureState state = new CompatibleComputedColumnStructureState(handler,
						element, propDefn);
				state.setName(propName);

				return state;
			}
		}
		return super.generalJumpTo();
	}

	/**
	 * Creates structure instance given the structure name.
	 *
	 * @param structDefn the definition of the structure to create
	 * @return the structure instance created.
	 */

	static IStructure createStructure(StructureDefn structDefn) {
		populateStructDict();

		assert structDefn != null;
		assert structDict != null;

		IStructure struct = null;

		try {
			Class c = (Class) structDict.get(structDefn.getName().toLowerCase());
			if (c == null) {
				// Try to load java class from ROM definition
				String clazzName = structDefn.getJavaClass();
				if (clazzName != null) {
					c = Class.forName(clazzName);
				}
			}
			if (c == null) {
				return null;
			}
			struct = (IStructure) c.newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			assert false;
		}

		return struct;
	}

	/**
	 * Populates the dictionary for the structure class and name mapping.
	 *
	 */

	private synchronized static void populateStructDict() {
		if (structDict != null) {
			return;
		}

		structDict = new HashMap();

		structDict.put(Action.ACTION_STRUCT.toLowerCase(), Action.class);

		structDict.put(ColumnHint.COLUMN_HINT_STRUCT.toLowerCase(), ColumnHint.class);

		structDict.put(ComputedColumn.COMPUTED_COLUMN_STRUCT.toLowerCase(), ComputedColumn.class);

		structDict.put(ConfigVariable.CONFIG_VAR_STRUCT.toLowerCase(), ConfigVariable.class);

		structDict.put(CustomColor.CUSTOM_COLOR_STRUCT.toLowerCase(), CustomColor.class);

		structDict.put(EmbeddedImage.EMBEDDED_IMAGE_STRUCT.toLowerCase(), EmbeddedImage.class);

		structDict.put(FilterCondition.FILTER_COND_STRUCT.toLowerCase(), FilterCondition.class);

		structDict.put(HideRule.STRUCTURE_NAME.toLowerCase(), HideRule.class);

		structDict.put(HighlightRule.STRUCTURE_NAME.toLowerCase(), HighlightRule.class);

		structDict.put(IncludedLibrary.INCLUDED_LIBRARY_STRUCT.toLowerCase(), IncludedLibrary.class);

		structDict.put(IncludeScript.INCLUDE_SCRIPT_STRUCT.toLowerCase(), IncludeScript.class);

		structDict.put(DataSetParameter.STRUCT_NAME.toLowerCase(), DataSetParameter.class);

		structDict.put(OdaDataSetParameter.STRUCT_NAME.toLowerCase(), OdaDataSetParameter.class);

		structDict.put(MapRule.STRUCTURE_NAME.toLowerCase(), MapRule.class);

		structDict.put(ParamBinding.PARAM_BINDING_STRUCT.toLowerCase(), ParamBinding.class);

		structDict.put(PropertyMask.STRUCTURE_NAME.toLowerCase(), PropertyMask.class);

		structDict.put(ResultSetColumn.RESULT_SET_COLUMN_STRUCT.toLowerCase(), ResultSetColumn.class);

		structDict.put(SearchKey.SEARCHKEY_STRUCT.toLowerCase(), SearchKey.class);

		structDict.put(SelectionChoice.STRUCTURE_NAME.toLowerCase(), SelectionChoice.class);

		structDict.put(SortKey.SORT_STRUCT.toLowerCase(), SortKey.class);

		structDict.put(CachedMetaData.CACHED_METADATA_STRUCT.toLowerCase(), CachedMetaData.class);

		structDict.put(StringFormatValue.FORMAT_VALUE_STRUCT.toLowerCase(), StringFormatValue.class);

		structDict.put(NumberFormatValue.FORMAT_VALUE_STRUCT.toLowerCase(), NumberFormatValue.class);

		structDict.put(TimeFormatValue.FORMAT_VALUE_STRUCT.toLowerCase(), TimeFormatValue.class);

		structDict.put(DateFormatValue.FORMAT_VALUE_STRUCT.toLowerCase(), DateFormatValue.class);

		structDict.put(DateTimeFormatValue.FORMAT_VALUE_STRUCT.toLowerCase(), DateTimeFormatValue.class);

		structDict.put(ParameterFormatValue.FORMAT_VALUE_STRUCT.toLowerCase(), ParameterFormatValue.class);

		structDict.put(FormatValue.FORMAT_VALUE_STRUCT.toLowerCase(), FormatValue.class);

		structDict.put(PropertyBinding.PROPERTY_BINDING_STRUCT.toLowerCase(), PropertyBinding.class);

		structDict.put(JoinCondition.STRUCTURE_NAME.toLowerCase(), JoinCondition.class);

		structDict.put(OdaDesignerState.STRUCTURE_NAME.toLowerCase(), OdaDesignerState.class);

		structDict.put(OdaResultSetColumn.STRUCTURE_NAME.toLowerCase(), OdaResultSetColumn.class);

		structDict.put(ScriptLib.STRUCTURE_NAME.toLowerCase(), ScriptLib.class);

		structDict.put(IncludedCssStyleSheet.INCLUDED_CSS_STRUCT.toLowerCase(), IncludedCssStyleSheet.class);

		structDict.put(TOC.TOC_STRUCT.toLowerCase(), TOC.class);
		structDict.put(DimensionCondition.DIMENSION_CONDITION_STRUCT.toLowerCase(), DimensionCondition.class);
		structDict.put(Rule.RULE_STRUCTURE.toLowerCase(), Rule.class);

		structDict.put(LevelAttribute.STRUCTURE_NAME.toLowerCase(), LevelAttribute.class);
		structDict.put(OdaLevelAttribute.STRUCTURE_NAME.toLowerCase(), OdaLevelAttribute.class);
		structDict.put(DimensionJoinCondition.DIMENSION_JOIN_CONDITION_STRUCT.toLowerCase(),
				DimensionJoinCondition.class);
		structDict.put(AggregationArgument.STRUCTURE_NAME.toLowerCase(), AggregationArgument.class);
		structDict.put(SortHint.SORT_HINT_STRUCT.toLowerCase(), SortHint.class);
		structDict.put(TimeInterval.STRUCTURE_NAME.toLowerCase(), TimeInterval.class);
		structDict.put(CalculationArgument.STRUCTURE_NAME.toLowerCase(), CalculationArgument.class);
	}
}
