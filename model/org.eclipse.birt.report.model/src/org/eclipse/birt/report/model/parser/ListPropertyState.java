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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.data.IColumnBinding;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.IncludeScript;
import org.eclipse.birt.report.model.api.elements.structures.ParamBinding;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.simpleapi.IExpressionType;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.ICssStyleSheetOperation;
import org.eclipse.birt.report.model.elements.OdaDataSource;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.ScalarParameter;
import org.eclipse.birt.report.model.elements.ScriptDataSet;
import org.eclipse.birt.report.model.elements.interfaces.IAbstractThemeModel;
import org.eclipse.birt.report.model.elements.interfaces.IDataSetModel;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.VersionUtil;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Parses the "property-list" tag. We use the "property-list" tag if the element
 * property or structure member is defined as structure list type, the
 * user-defined properties, user-defined property values and the value choices
 * of user-defined property.
 */

public class ListPropertyState extends AbstractPropertyState {

	private static final int USER_PROPERTIES_PROP = DesignElement.USER_PROPERTIES_PROP.toLowerCase().hashCode();
	private static final int INCLUDE_SCRIPTS_PROP = Module.INCLUDE_SCRIPTS_PROP.toLowerCase().hashCode();
	private static final int LIBRARIES_PROP = Module.LIBRARIES_PROP.toLowerCase().hashCode();
	private static final int INCLUDE_LIBRARIES_PROP = "includeLibraries" //$NON-NLS-1$
			.toLowerCase().hashCode();

	private static final int BOUND_DATA_COLUMNS_PROP = IReportItemModel.BOUND_DATA_COLUMNS_PROP.toLowerCase()
			.hashCode();

	private static final int PARAM_BOUND_DATA_COLUMNS_PROP = IReportItemModel.BOUND_DATA_COLUMNS_PROP.toLowerCase()
			.hashCode();

	private static final int PRIVATE_DRIVER_PROPERTIES_PROP = OdaDataSource.PRIVATE_DRIVER_PROPERTIES_PROP.toLowerCase()
			.hashCode();

	private static final int PUBLIC_DRIVER_PROPERTIES_PROP = OdaDataSource.PUBLIC_DRIVER_PROPERTIES_PROP.toLowerCase()
			.hashCode();

	private static final int RESULT_SET_PROP = "resultSet".toLowerCase() //$NON-NLS-1$
			.hashCode();

	/**
	 * The temporary list which holds the structures in one structure list.
	 */

	ArrayList list = new ArrayList();

	/**
	 * The definition of the property of this list property.
	 */

	protected PropertyDefn propDefn = null;

	/**
	 * Constructs the design parse state with the design file parser handler. This
	 * constructor is used when this list property to parse is a property of one
	 * element.
	 *
	 * @param theHandler the design file parser handler
	 * @param element    the element which holds this property
	 */

	protected ListPropertyState(ModuleParserHandler theHandler, DesignElement element) {
		super(theHandler, element);
	}

	/**
	 * Constructs the design parse state with the design file parser handler. This
	 * constructor is used when this list property to parse is a member of one
	 * structure.
	 *
	 * @param theHandler the design parser handler
	 * @param element    the element holding this list property
	 * @param propDefn   the definition of the property which is structure list
	 * @param struct     the structure which holds this list property
	 */

	ListPropertyState(ModuleParserHandler theHandler, DesignElement element, PropertyDefn propDefn, IStructure struct) {
		super(theHandler, element);

		this.propDefn = propDefn;
		this.struct = struct;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.parser.AbstractPropertyState#setName(java
	 * .lang.String)
	 */

	@Override
	protected void setName(String name) {
		super.setName(name);

		if (struct != null) {
			propDefn = (PropertyDefn) struct.getDefn().getMember(name);
		} else {
			propDefn = element.getPropertyDefn(name);
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

		super.parseAttrs(attrs);

		if (StringUtil.isBlank(name)) {
			return;
		}

		// compatible for old Action list property.
		if (this.struct instanceof Action) {
			if ((ActionStructureState.DRILLTHROUGH_PARAM_BINDINGS_MEMBER.equals(name))) {
				name = Action.PARAM_BINDINGS_MEMBER;
			} else if (ActionStructureState.DRILLTHROUGH_SEARCH_MEMBER.equals(name)) {
				name = Action.SEARCH_MEMBER;
			}
		}

		if (struct != null) {
			propDefn = (PropertyDefn) struct.getDefn().getMember(name);
		} else {
			propDefn = element.getPropertyDefn(name);
		}

		// prop maybe is null, for example, user properties.

		if (!IDesignElementModel.USER_PROPERTIES_PROP.equals(name)) {
			if (propDefn == null) {
				// ROM does not contain public driver properties any more.

				

				// compatible for "includeLibrary" in the module

				if ("publicDriverProperties".equals(name) || (element instanceof Module && "includeLibraries".equalsIgnoreCase(name))) { //$NON-NLS-1$
					return;
				}

				if (element instanceof ScriptDataSet && "resultSet".equalsIgnoreCase(name)) { //$NON-NLS-1$
					return;
				}

				// the property has been removed. It must be handled before
				// checking the
				// validation of <code>valid</code>.

				if (handler.versionNumber > VersionUtil.VERSION_3_0_0
						&& handler.versionNumber <= VersionUtil.VERSION_3_2_1 && ("boundDataColumns".equals(name)) //$NON-NLS-1$
						&& (element instanceof GroupElement)) {
					return;
				}

				// If the property name is invalid, no error will be reported.

				DesignParserException e = new DesignParserException(new String[] { name },
						DesignParserException.DESIGN_EXCEPTION_UNDEFINED_PROPERTY);
				RecoverableError.dealUndefinedProperty(handler, e);

				valid = false;
			} else if (IPropertyType.STRUCT_TYPE != propDefn.getTypeCode()) {
				DesignParserException e = new DesignParserException(
						DesignParserException.DESIGN_EXCEPTION_WRONG_STRUCTURE_LIST_TYPE);
				handler.getErrorHandler().semanticError(e);
				valid = false;
			}
		}

		if (valid && propDefn != null) {
			if (struct != null) {

				struct.setProperty(propDefn, new ArrayList());
			} else if (!IDesignElementModel.USER_PROPERTIES_PROP.equals(propDefn.getName())) {
				element.setProperty(propDefn, new ArrayList());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java
	 * .lang.String)
	 */

	@Override
	public AbstractParseState startElement(String tagName) {
		if (tagName.equalsIgnoreCase(DesignSchemaConstants.STRUCTURE_TAG)) {
			if (struct != null) {
				return new StructureState(handler, element, propDefn, (Structure) struct);
			}

			return new StructureState(handler, element, propDefn);
		}

		if (tagName.equalsIgnoreCase(DesignSchemaConstants.EX_PROPERTY_TAG)) {
			return new ExtendedPropertyState(handler, element, propDefn);
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
		// since 3.2.21, parameter name is case-insensitive
		if (handler.versionNumber < VersionUtil.VERSION_3_2_21) {
			// update column binding
			if (element instanceof ReportItem && IReportItemModel.BOUND_DATA_COLUMNS_PROP.equals(name)) {
				List boundColumns = (List) element.getLocalProperty(handler.module,
						IReportItemModel.BOUND_DATA_COLUMNS_PROP);
				if (boundColumns == null || boundColumns.isEmpty()) {
					return;
				}
				for (int i = 0; i < boundColumns.size(); i++) {
					ComputedColumn column = (ComputedColumn) boundColumns.get(i);
					handleBinding(column, ComputedColumn.EXPRESSION_MEMBER);
				}
			}

			// update parameter binding
			if (propDefn != null && propDefn.getStructDefn() == MetaDataDictionary.getInstance()
					.getStructure(ParamBinding.PARAM_BINDING_STRUCT)) {
				List paramBindings = null;
				if (struct != null) {
					paramBindings = (List) struct.getProperty(handler.module, propDefn);
				} else {
					paramBindings = (List) element.getLocalProperty(handler.module, (ElementPropertyDefn) propDefn);
				}

				if (paramBindings != null) {
					for (int i = 0; i < paramBindings.size(); i++) {
						ParamBinding paramBinding = (ParamBinding) paramBindings.get(i);
						handleBinding(paramBinding, ParamBinding.EXPRESSION_MEMBER);
					}
				}
			}
		}
	}

	private void handleBinding(Structure binding, String memberName) {
		assert binding != null;
		PropertyDefn propDefn = (PropertyDefn) binding.getMemberDefn(memberName);
		Object value = binding.getProperty(handler.module, propDefn);
		if (value == null) {
			return;
		}

		List<Expression> expressions = new ArrayList<>();
		boolean isExpressionType = propDefn.getTypeCode() == IPropertyType.EXPRESSION_TYPE;
		if (isExpressionType) {
			expressions.add((Expression) value);
		} else {
			expressions.addAll((List<Expression>) value);
		}

		List<Expression> newExpressions = new ArrayList<>(expressions);
		if (!isExpressionType) {
			binding.setProperty(propDefn, newExpressions);
		}
		for (int index = 0; index < expressions.size(); index++) {
			Expression exprObj = expressions.get(index);
			if (IExpressionType.JAVASCRIPT.equals(exprObj.getType())) {
				String expression = exprObj.getStringExpression();
				if (expression != null) {
					try {
						List columnExprs = ExpressionUtil.extractColumnExpressions(expression,
								ExpressionUtil.PARAMETER_INDICATOR);

						// set to store all the old name that has been done the
						// replacement or not any such a name parameter is
						// renamed in order to do this handling multiple times
						// for the same name
						HashSet<String> handledNames = new HashSet<>();
						if (columnExprs != null) {
							for (int i = 0; i < columnExprs.size(); i++) {
								IColumnBinding columnBinding = (IColumnBinding) columnExprs.get(i);
								String columnName = columnBinding.getResultSetColumnName();
								if (columnName != null && !handledNames.contains(columnName)) {
									handledNames.add(columnName);
									HashMap paramMap = (HashMap) handler.tempValue
											.get(ModuleParserHandler.PARAMETER_NAME_CACHE_KEY);
									if (paramMap != null && paramMap.containsKey(columnName)) {
										String newParamName = (String) paramMap.get(columnName);
										assert newParamName != null;
										String newExpression = ExpressionUtil.replaceParameterName(expression,
												columnName, newParamName);
										Expression newExprObj = new Expression(newExpression,
												IExpressionType.JAVASCRIPT);
										if (isExpressionType) {
											binding.setExpressionProperty(memberName, newExprObj);
										} else {
											newExpressions.set(index, newExprObj);
										}
									}
								}
							}
						}
					} catch (BirtException e) {
						// do nothing
					}
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

		if (supportIsEmpty()) {
			AbstractPropertyState state = new EmptyListState(handler, element, struct);
			state.setName(name);
			return state;
		}

		if (USER_PROPERTIES_PROP == nameValue) {
			AbstractPropertyState state = new UserPropertyListState(handler, element);
			state.setName(name);
			return state;
		}
		if (element instanceof Module) {
			if (INCLUDE_SCRIPTS_PROP == nameValue) {
				SimpleStructureListState state = new SimpleStructureListState(handler, element);
				state.setName(name);
				state.setMemberName(IncludeScript.FILE_NAME_MEMBER);
				return state;
			}

			if (LIBRARIES_PROP == nameValue || INCLUDE_LIBRARIES_PROP == nameValue) {
				AbstractPropertyState state = new IncludedLibrariesStructureListState(handler, element);
				state.setName(IModuleModel.LIBRARIES_PROP);
				return state;
			}

		}

		if ((PARAM_BOUND_DATA_COLUMNS_PROP == nameValue && element instanceof ScalarParameter) || (BOUND_DATA_COLUMNS_PROP == nameValue && element instanceof ReportItem)) {
			CompatibleBoundColumnState state = new CompatibleBoundColumnState(handler, element);
			state.setName(name);
			return state;
		}

		if (element instanceof ICssStyleSheetOperation) {
			if (IReportDesignModel.CSSES_PROP.equalsIgnoreCase(name)
					|| IAbstractThemeModel.CSSES_PROP.equalsIgnoreCase(name)) {
				AbstractPropertyState state = new IncludedCssStyleSheetListState(handler, element);
				state.setName(IReportDesignModel.CSSES_PROP);
				return state;
			}
		}

		return super.generalJumpTo();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.parser.AbstractPropertyState#
	 * versionConditionalJumpTo()
	 */

	@Override
	protected AbstractParseState versionConditionalJumpTo() {
		if (element instanceof OdaDataSource) {
			if (PRIVATE_DRIVER_PROPERTIES_PROP == nameValue || PUBLIC_DRIVER_PROPERTIES_PROP == nameValue) {
				if (handler.isVersion(VersionUtil.VERSION_0)) {
					CompatibleOdaDriverPropertyStructureListState state = new CompatibleOdaDriverPropertyStructureListState(
							handler, element);
					state.setName(name);
					return state;
				}
			}
		} else if (handler.versionNumber < VersionUtil.VERSION_3_2_0 && BOUND_DATA_COLUMNS_PROP == nameValue
				&& element instanceof ReportItem) {
			CompatibleBoundColumnState state = new CompatibleBoundColumnState(handler, element);
			state.setName(name);
			return state;
		}

		// the property has been removed. It must be handled before checking the
		// validation of <code>valid</code>.

		if (handler.versionNumber > VersionUtil.VERSION_3_0_0 && handler.versionNumber <= VersionUtil.VERSION_3_2_1
				&& ("boundDataColumns".equals(name)) //$NON-NLS-1$
				&& (element instanceof GroupElement)) {

			CompatibleGroupBoundColumnsState state = new CompatibleGroupBoundColumnsState(handler,
					element.getContainer(), (GroupElement) element);
			state.setName(name);
			return state;

		}

		// cannot be result sets in the cached meta data

		if (handler.versionNumber < VersionUtil.VERSION_3_2_4 && element instanceof ScriptDataSet
				&& RESULT_SET_PROP == nameValue && struct == null) {
			CompatibleRenameListPropertyState state = new CompatibleRenameListPropertyState(handler, element, name);
			state.setName(IDataSetModel.RESULT_SET_HINTS_PROP);
			return state;
		}

		return super.versionConditionalJumpTo();
	}
}
