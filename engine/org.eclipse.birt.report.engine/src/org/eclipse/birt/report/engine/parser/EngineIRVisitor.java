/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.dom.StyleDeclaration;
import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.eclipse.birt.report.engine.css.engine.value.DataFormatValue;
import org.eclipse.birt.report.engine.css.engine.value.birt.BIRTConstants;
import org.eclipse.birt.report.engine.ir.ActionDesign;
import org.eclipse.birt.report.engine.ir.AutoTextItemDesign;
import org.eclipse.birt.report.engine.ir.BandDesign;
import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.ColumnDesign;
import org.eclipse.birt.report.engine.ir.DataItemDesign;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.ir.DrillThroughActionDesign;
import org.eclipse.birt.report.engine.ir.DynamicTextItemDesign;
import org.eclipse.birt.report.engine.ir.EngineIRConstants;
import org.eclipse.birt.report.engine.ir.Expression;
import org.eclipse.birt.report.engine.ir.ExtendedItemDesign;
import org.eclipse.birt.report.engine.ir.FreeFormItemDesign;
import org.eclipse.birt.report.engine.ir.GraphicMasterPageDesign;
import org.eclipse.birt.report.engine.ir.GridItemDesign;
import org.eclipse.birt.report.engine.ir.GroupDesign;
import org.eclipse.birt.report.engine.ir.HighlightDesign;
import org.eclipse.birt.report.engine.ir.HighlightRuleDesign;
import org.eclipse.birt.report.engine.ir.ImageItemDesign;
import org.eclipse.birt.report.engine.ir.LabelItemDesign;
import org.eclipse.birt.report.engine.ir.ListBandDesign;
import org.eclipse.birt.report.engine.ir.ListGroupDesign;
import org.eclipse.birt.report.engine.ir.ListItemDesign;
import org.eclipse.birt.report.engine.ir.ListingDesign;
import org.eclipse.birt.report.engine.ir.MapDesign;
import org.eclipse.birt.report.engine.ir.MapRuleDesign;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;
import org.eclipse.birt.report.engine.ir.PageSetupDesign;
import org.eclipse.birt.report.engine.ir.PageVariableDesign;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportElementDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.RowDesign;
import org.eclipse.birt.report.engine.ir.RuleDesign;
import org.eclipse.birt.report.engine.ir.SimpleMasterPageDesign;
import org.eclipse.birt.report.engine.ir.StyledElementDesign;
import org.eclipse.birt.report.engine.ir.TableBandDesign;
import org.eclipse.birt.report.engine.ir.TableGroupDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;
import org.eclipse.birt.report.engine.ir.TemplateDesign;
import org.eclipse.birt.report.engine.ir.TextItemDesign;
import org.eclipse.birt.report.engine.ir.VisibilityDesign;
import org.eclipse.birt.report.engine.ir.VisibilityRuleDesign;
import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.AutoTextHandle;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ColorHandle;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignVisitor;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.ExpressionHandle;
import org.eclipse.birt.report.model.api.ExpressionListHandle;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.FactoryPropertyHandle;
import org.eclipse.birt.report.model.api.FreeFormHandle;
import org.eclipse.birt.report.model.api.GraphicMasterPageHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.HideRuleHandle;
import org.eclipse.birt.report.model.api.HighlightRuleHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ListGroupHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.MapRuleHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.MemberHandle;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.StyleRuleHandle;
import org.eclipse.birt.report.model.api.TOCHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.TemplateReportItemHandle;
import org.eclipse.birt.report.model.api.TextDataHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.VariableElementHandle;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.api.elements.structures.FormatValue;
import org.eclipse.birt.report.model.api.elements.structures.HideRule;
import org.eclipse.birt.report.model.api.elements.structures.StyleRule;
import org.eclipse.birt.report.model.api.elements.structures.TOC;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.interfaces.ICellModel;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IMasterPageModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.elements.interfaces.ITableRowModel;
import org.eclipse.core.runtime.Assert;

import com.ibm.icu.util.ULocale;

/**
 * Constructs an internal representation of the report design for report
 * generation and presentation, based on the internal representation that design
 * engine creates. The DE IR services both the designer UI and factory, and has
 * certain features that are not quite suitable for FPE use. In particular, this
 * step of the reconstruction is needed for several reasons:
 * <p>
 * <li>Style handling: DE stores all styles in an unflatten version. Factory
 * needs to reference styles where the element hierarchy has been flattened.
 * <li>Faster lookup: DE stores various properties as property name/value pairs.
 * Factory IR might store them as structure. See
 * <code>createHighlightRule()</code> for an example.
 * <li>Merging properties: DE stores custom and default properties separately.
 * In FPE, they are merged.</li>
 * <p>
 *
 * This class visits the Design Engine's IR to create a new IR for FPE. It is
 * usually used in the "Design Adaptation" phase of report generation, which is
 * also the first step in report generation after DE loads the report in.
 *
 * <p>
 * special consideration in styles
 * <p>
 * BIRT uses a simlar style mode with CSS, but not exactly the same. The main
 * differences are:
 * <li>text-decoration is not inheraible which simplify the CSS standard. This
 * rules makes text-decroation are usless for all the containers. As the HTML
 * treat the text-decoration inheritable in block-level element, the ENGINE must
 * remove the text-decoration from the container's styles.
 * <li>BIRT doesn't define the body style, it uses a predefined style "report"
 * as the default style.
 *
 */
public class EngineIRVisitor extends DesignVisitor {
	/**
	 * The prefix of style name
	 */
	protected static final String PREFIX_STYLE_NAME = "style_"; //$NON-NLS-1$

	/**
	 * default master page name.
	 */
	protected static final String DEFAULT_MASTERPAGE_NAME = "NewSimpleMasterPage";

	/**
	 * The default value of masterPage's margin, in inch. See rom.ref in model:
	 * <PropertyGroup displayNameID="Element.MasterPage.margin">
	 */
	protected static final double DEFAULT_MASTERPAGE_TOP_MARGIN = 0.25;
	protected static final double DEFAULT_MASTERPAGE_LEFT_MARGIN = 0.25;
	protected static final double DEFAULT_MASTERPAGE_BOTTOM_MARGIN = 0.25;
	protected static final double DEFAULT_MASTERPAGE_RIGHT_MARGIN = 0.25;
	protected static final double DEFAULT_MASTERPAGE_HEIGHT = 11;
	protected static final double DEFAULT_MASTERPAGE_WIDTH = 8.5;

	/**
	 * logger used to log the error.
	 */
	protected static Logger logger = Logger.getLogger(EngineIRVisitor.class.getName());

	/**
	 * current report element created by visitor
	 */
	protected Object currentElement;

	/**
	 * Factory IR created by this visitor
	 */
	protected Report report;

	/**
	 * default script language
	 */
	protected String defaultScriptLanguage = "javascript";

	/**
	 * report design handle
	 */
	protected ReportDesignHandle handle;

	/**
	 * the CSSEngine
	 */
	protected CSSEngine cssEngine;

	/**
	 * the inheritable report style
	 */
	StyleDeclaration nonInheritableReportStyle;

	/**
	 * the non-inheritable report style
	 */
	StyleDeclaration inheritableReportStyle;

	protected long currentElementId = -1;

	/**
	 * Used to fix half-baked handle, such as: fix the new added empty cell created
	 * in format irregular table or grid. fix default master page.
	 */
	long newCellId = -1;

	/**
	 * constructor
	 *
	 * @param handle the entry point to the DE report design IR
	 *
	 */
	public EngineIRVisitor(ReportDesignHandle handle) {
		super();
		this.handle = handle;
	}

	/**
	 * translate the DE's IR to FPE's IR.
	 *
	 * @return FPE's IR.
	 */
	public Report translate() {
		report = new Report();
		cssEngine = report.getCSSEngine();
		report.setReportDesign(handle);
		handle.cacheValues();
		apply(handle);
		return report;
	}

	public ReportItemDesign translate(ReportElementHandle handle, Report report) {
		this.report = report;
		cssEngine = report.getCSSEngine();
		newCellId = handle.getID() * -100000000;
		apply(handle);
		assert currentElement instanceof ReportItemDesign;
		return (ReportItemDesign) currentElement;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.DesignVisitor#visitReportDesign(org.eclipse
	 * .birt.report.model.api.ReportDesignHandle)
	 */
	@Override
	public void visitReportDesign(ReportDesignHandle handle) {

		Map<String, Expression> userProperties = createUserProperties(handle);
		if (userProperties != null && !userProperties.isEmpty()) {
			report.setUserProperties(userProperties);
		}

		// INCLUDE LIBRARY
		// INCLUDE SCRIPT
		// CODE MODULES

		ULocale locale = handle.getLocale();
		if (locale != null) {
			report.setLocale(locale.toString());
		}

		// Sets the report default style
		createReportDefaultStyles(handle);

		// TODO: add report style
		// report.addStyle( );

		// COLOR-PALETTE
		// METHOD
		// STYLES
		// We needn't handle the style slot, it will be handled for each
		// element.

		// Handle Master Page
		List<VariableElementHandle> varElements = handle.getPageVariables();
		Collection<PageVariableDesign> varDesigns = report.getPageVariables();
		for (VariableElementHandle varElement : varElements) {
			String scope = varElement.getType();
			String name = varElement.getVariableName();
			ExpressionHandle exprHandle = varElement.getExpressionProperty(VariableElementHandle.VALUE_PROP);
			Expression defaultValue = createExpression(exprHandle);
			PageVariableDesign pv = new PageVariableDesign(name, scope);
			pv.setDefaultValue(defaultValue);
			varDesigns.add(pv);
		}

		String onPageEnd = handle.getOnPageEnd();
		Expression.Script onPageEndScript = createScript(onPageEnd);
		if (onPageEndScript != null) {
			String scriptId = ModuleUtil.getScriptUID(handle.getPropertyHandle(IReportDesignModel.ON_PAGE_END_METHOD));
			onPageEndScript.setFileName(scriptId);
			report.setOnPageEnd(onPageEndScript);
		}
		String onPageStart = handle.getOnPageStart();
		Expression.Script onPageStartScript = createScript(onPageStart);
		if (onPageStartScript != null) {
			String scriptId = ModuleUtil
					.getScriptUID(handle.getPropertyHandle(IReportDesignModel.ON_PAGE_START_METHOD));
			onPageStartScript.setFileName(scriptId);
			report.setOnPageStart(onPageStartScript);
		}

		report.setJavaClass(handle.getEventHandlerClass());

		PageSetupDesign pageSetup = new PageSetupDesign();
		SlotHandle pageSlot = handle.getMasterPages();
		for (int i = 0; i < pageSlot.getCount(); i++) {
			apply(pageSlot.get(i));
			if (currentElement != null) {
				pageSetup.addMasterPage((MasterPageDesign) currentElement);
			}
		}

		// If there is no master page, set a default one.
		if (pageSlot.getCount() < 1) {
			MasterPageDesign masterPage = new SimpleMasterPageDesign();
			masterPage.setID(generateUniqueID());
			masterPage.setName(DEFAULT_MASTERPAGE_NAME);
			masterPage.setPageType(DesignChoiceConstants.PAGE_SIZE_US_LETTER);
			masterPage.setOrientation(DesignChoiceConstants.PAGE_ORIENTATION_AUTO);
			DimensionType width = new DimensionType(DEFAULT_MASTERPAGE_WIDTH, DimensionType.UNITS_IN);
			DimensionType height = new DimensionType(DEFAULT_MASTERPAGE_HEIGHT, DimensionType.UNITS_IN);
			masterPage.setPageSize(width, height);
			DimensionType top = new DimensionType(DEFAULT_MASTERPAGE_TOP_MARGIN, DimensionType.UNITS_IN);
			DimensionType left = new DimensionType(DEFAULT_MASTERPAGE_LEFT_MARGIN, DimensionType.UNITS_IN);
			DimensionType bottom = new DimensionType(DEFAULT_MASTERPAGE_BOTTOM_MARGIN, DimensionType.UNITS_IN);
			DimensionType right = new DimensionType(DEFAULT_MASTERPAGE_RIGHT_MARGIN, DimensionType.UNITS_IN);
			masterPage.setMargin(top, left, bottom, right);
			setupElementIDMap(masterPage);
			pageSetup.addMasterPage(masterPage);
		}

		// FIXME: add page sequence support
		// Handle Page Sequence
		// SlotHandle seqSlot = handle.getPageSequences( );
		// for ( int i = 0; i < seqSlot.getCount( ); i++ )
		// {
		// apply( seqSlot.get( i ) );
		// assert ( currentElement != null );
		// pageSetup.addPageSequence( (PageSequenceDesign) currentElement );
		// }

		report.setPageSetup(pageSetup);

		// COMPONENTS

		// Handle Report Body
		SlotHandle bodySlot = handle.getBody();
		for (int i = 0; i < bodySlot.getCount(); i++) {
			apply(bodySlot.get(i));
			if (currentElement != null) {
				report.addContent((ReportItemDesign) currentElement);
			}
		}

		// SCRATCH-PAD
		// CONFIG-VARS
		// TRANSLATIONS
		// IMAGES
		// CUSTOM
	}

	/**
	 * setup the user properties expression map
	 *
	 * @param userProperties   user defined named expressions in design file
	 * @param namedExpressions the data structure that hold named expressions
	 */
	private Map<String, Expression> createUserProperties(DesignElementHandle handle) {
		List propDefns = handle.getUserProperties();
		if (propDefns == null || propDefns.isEmpty()) {
			return null;
		}
		Map<String, Expression> propExprs = new HashMap<>(propDefns.size());
		for (int i = 0; i < propDefns.size(); i++) {
			UserPropertyDefn userDef = (UserPropertyDefn) propDefns.get(i);
			Expression expr = org.eclipse.birt.report.engine.util.ExpressionUtil.createUserProperty(handle, userDef);
			if (expr != null) {
				propExprs.put(userDef.getName(), expr);
			}
		}
		return propExprs;
	}

	/**
	 * setup the master page object from the base master page handle.
	 *
	 * @param page   page object
	 * @param handle page handle
	 */
	private void setupMasterPage(MasterPageDesign page, MasterPageHandle handle) {
		setupStyledElement(page, handle);

		page.setPageType(handle.getPageType());

		// Master page width and height
		DimensionValue effectWidth = handle.getPageWidth();
		DimensionValue effectHeight = handle.getPageHeight();
		DimensionType width = null;
		DimensionType height = null;
		if (effectWidth != null) {
			width = new DimensionType(effectWidth.getMeasure(), effectWidth.getUnits());
		} else {
			width = new DimensionType(DEFAULT_MASTERPAGE_WIDTH, DimensionType.UNITS_IN);
		}
		if (effectHeight != null) {
			height = new DimensionType(effectHeight.getMeasure(), effectHeight.getUnits());
		} else {
			height = new DimensionType(DEFAULT_MASTERPAGE_HEIGHT, DimensionType.UNITS_IN);
		}
		page.setPageSize(width, height);
		page.setOrientation(handle.getOrientation());

		// Master page margins
		DimensionType top = createDimension(handle.getTopMargin(), true);
		DimensionType left = createDimension(handle.getLeftMargin(), true);
		DimensionType bottom = createDimension(handle.getBottomMargin(), true);
		DimensionType right = createDimension(handle.getRightMargin(), true);
		page.setMargin(top, left, bottom, right);

		String onPageEnd = handle.getOnPageEnd();
		Expression.Script onPageEndScript = createScript(onPageEnd);
		if (onPageEndScript != null) {
			String scriptId = ModuleUtil.getScriptUID(handle.getPropertyHandle(IMasterPageModel.ON_PAGE_END_METHOD));
			onPageEndScript.setFileName(scriptId);
			page.setOnPageEnd(onPageEndScript);
		}
		String onPageStart = handle.getOnPageStart();
		Expression.Script onPageStartScript = createScript(onPageStart);
		if (onPageStartScript != null) {
			String scriptId = ModuleUtil.getScriptUID(handle.getPropertyHandle(IMasterPageModel.ON_PAGE_START_METHOD));
			onPageStartScript.setFileName(scriptId);
			page.setOnPageStart(onPageStartScript);
		}
	}

	@Override
	protected void visitDesignElement(DesignElementHandle obj) {
		// any unsupported element
		currentElement = null;
	}

	@Override
	public void visitGraphicMasterPage(GraphicMasterPageHandle handle) {
		GraphicMasterPageDesign page = new GraphicMasterPageDesign();

		setupMasterPage(page, handle);

		// Multi-column properties
		page.setColumns(handle.getColumnCount());
		DimensionType spacing = createDimension(handle.getColumnSpacing(), true);
		page.setColumnSpacing(spacing);

		// Master page content
		SlotHandle contentSlot = handle.getContent();
		for (int i = 0; i < contentSlot.getCount(); i++) {
			apply(contentSlot.get(i));
			if (currentElement != null) {
				page.addContent((ReportItemDesign) currentElement);
			}
		}

		setCurrentElement(page);

		// We do not support graphic master page now.
		Assert.isTrue(false, "Graphic master page is not supported now!");
	}

	private void setCurrentElement(ReportElementDesign element) {
		currentElement = element;
		currentElementId = -1;
	}

	@Override
	public void visitSimpleMasterPage(SimpleMasterPageHandle handle) {
		SimpleMasterPageDesign page = new SimpleMasterPageDesign();

		// setup the base master page property.
		setupMasterPage(page, handle);

		page.setHeaderHeight(createDimension(handle.getHeaderHeight(), true));
		page.setFooterHeight(createDimension(handle.getFooterHeight(), true));
		page.setShowFooterOnLast(handle.showFooterOnLast());
		page.setShowHeaderOnFirst(handle.showHeaderOnFirst());
		page.setFloatingFooter(handle.isFloatingFooter());
		SlotHandle headerSlot = handle.getPageHeader();
		for (int i = 0; i < headerSlot.getCount(); i++) {
			apply(headerSlot.get(i));
			if (currentElement != null) {
				page.addHeader((ReportItemDesign) currentElement);
			}
		}

		SlotHandle footerSlot = handle.getPageFooter();
		for (int i = 0; i < footerSlot.getCount(); i++) {
			apply(footerSlot.get(i));
			if (currentElement != null) {
				page.addFooter((ReportItemDesign) currentElement);
			}
		}

		setCurrentElement(page);
	}

	@Override
	public void visitList(ListHandle handle) {
		// Create ListItem
		ListItemDesign listItem = new ListItemDesign();
		setupListingItem(listItem, handle);

		// Header
		SlotHandle headerSlot = handle.getHeader();
		if (headerSlot.getCount() > 0) {
			ListBandDesign header = createListBand(headerSlot);
			header.setBandType(ListBandDesign.BAND_HEADER);
			listItem.setHeader(header);
		}
		listItem.setRepeatHeader(handle.repeatHeader());

		// Multiple groups
		SlotHandle groupsSlot = handle.getGroups();
		for (int i = 0; i < groupsSlot.getCount(); i++) {
			apply(groupsSlot.get(i));
			if (currentElement != null) {
				GroupDesign group = (GroupDesign) currentElement;
				group.setGroupLevel(i);
				listItem.addGroup(group);
			}
		}

		// List detail
		SlotHandle detailSlot = handle.getDetail();
		if (detailSlot.getCount() > 0) {
			ListBandDesign detail = createListBand(detailSlot);
			detail.setBandType(ListBandDesign.BAND_DETAIL);
			listItem.setDetail(detail);
		}

		// List Footer
		SlotHandle footerSlot = handle.getFooter();
		if (footerSlot.getCount() > 0) {
			ListBandDesign footer = createListBand(footerSlot);
			footer.setBandType(ListBandDesign.BAND_FOOTER);
			listItem.setFooter(footer);
		}

		setCurrentElement(listItem);
	}

	@Override
	public void visitFreeForm(FreeFormHandle handle) {
		// Create Free form element
		FreeFormItemDesign container = new FreeFormItemDesign();
		setupReportItem(container, handle);

		// Set up each individual item in a free form container
		SlotHandle slot = handle.getReportItems();
		for (int i = 0; i < slot.getCount(); i++) {
			apply(slot.get(i));
			if (currentElement != null) {
				container.addItem((ReportItemDesign) currentElement);
			}
		}

		setCurrentElement(container);
	}

	@Override
	public void visitTextDataItem(TextDataHandle handle) {
		DynamicTextItemDesign dynamicTextItem = new DynamicTextItemDesign();

		setupReportItem(dynamicTextItem, handle);

		ExpressionHandle valueExprHandle = handle.getExpressionProperty(TextDataHandle.VALUE_EXPR_PROP);
		Expression valueExpr = createExpression(valueExprHandle);
		String contentType = handle.getContentType();

		dynamicTextItem.setContent(valueExpr);
		dynamicTextItem.setContentType(contentType);
		dynamicTextItem.setJTidy(handle.isJTidy());
		setupHighlight(dynamicTextItem, valueExpr);
		setupMap(dynamicTextItem, valueExpr);

		setCurrentElement(dynamicTextItem);
	}

	@Override
	public void visitLabel(LabelHandle handle) {
		// Create Label Item
		LabelItemDesign labelItem = new LabelItemDesign();
		setupReportItem(labelItem, handle);

		// Text
		String text = handle.getText();
		String textKey = handle.getTextKey();

		labelItem.setText(textKey, text);

		// Handle Action
		ActionHandle action = handle.getActionHandle();
		if (action != null) {
			labelItem.setAction(createAction(action));
		}
		// Fill in help text
		labelItem.setHelpText(handle.getHelpTextKey(), handle.getHelpText());

		setCurrentElement(labelItem);
	}

	@Override
	public void visitAutoText(AutoTextHandle handle) {
		AutoTextItemDesign autoTextItem = new AutoTextItemDesign();
		setupReportItem(autoTextItem, handle);

		autoTextItem.setType(handle.getAutoTextType());

		setCurrentElement(autoTextItem);
	}

	@Override
	public void visitDataItem(DataItemHandle handle) {
		// Create data item
		DataItemDesign data = new DataItemDesign();
		setupReportItem(data, handle);

		// Fill in data expression,
		// String expr = handle.getValueExpr( );
		String expr = handle.getResultSetColumn();
		if (expr != null && expr.trim().length() > 0) {
			data.setBindingColumn(expr);
		}
		// Handle Action
		ActionHandle action = handle.getActionHandle();
		if (action != null) {
			data.setAction(createAction(action));
		}

		// Fill in help text
		data.setHelpText(handle.getHelpTextKey(), handle.getHelpText());

		Expression defaultExpr = org.eclipse.birt.report.engine.util.ExpressionUtil.createExpression(expr);
		setupHighlight(data, defaultExpr);
		setupMap(data, defaultExpr);

		setCurrentElement(data);
	}

	@Override
	public void visitGrid(GridHandle handle) {
		// Create Grid Item
		GridItemDesign grid = new GridItemDesign();
		setupReportItem(grid, handle);

		// Handle grid summary
		String summary = handle.getSummary();
		if (summary != null) {
			grid.setSummary(summary);
		}

		// Handle grid caption
		String caption = handle.getCaption();
		String captionKey = handle.getCaptionKey();
		if (caption != null || captionKey != null) {
			grid.setCaption(captionKey, caption);
		}

		// Handle Columns
		SlotHandle columnSlot = handle.getColumns();
		for (int i = 0; i < columnSlot.getCount(); i++) {
			ColumnHandle columnHandle = (ColumnHandle) columnSlot.get(i);
			apply(columnHandle);
			if (currentElement != null) {
				ColumnDesign columnDesign = (ColumnDesign) currentElement;
				for (int j = 0; j < columnHandle.getRepeatCount(); j++) {
					grid.addColumn(columnDesign);
				}
			}
		}

		// Handle Rows
		SlotHandle rowSlot = handle.getRows();
		for (int i = 0; i < rowSlot.getCount(); i++) {
			apply(rowSlot.get(i));
			if (currentElement != null) {
				grid.addRow((RowDesign) currentElement);
			}
		}

		newCellId = new TableItemDesignLayout().layout(grid, newCellId);
		applyColumnHighlight(grid);

		setCurrentElement(grid);
	}

	@Override
	public void visitImage(ImageHandle handle) {
		// Create Image Item
		ImageItemDesign image = new ImageItemDesign();
		setupReportItem(image, handle);

		// Handle Action
		ActionHandle action = handle.getActionHandle();
		if (action != null) {
			image.setAction(createAction(action));
		}

		// Help text for image
		image.setHelpText(handle.getHelpTextKey(), handle.getHelpText());

		// Fit to Container property
		image.setFitToContainer(handle.fitToContainer());

		// isProportionalScale
		image.setProportionalScale(handle.isProportionalScale());

		// Handle Image Source
		String imageSrc = handle.getSource();

		if (EngineIRConstants.IMAGE_REF_TYPE_URL.equals(imageSrc)) {
			ExpressionHandle urlExpr = handle.getExpressionProperty(ImageHandle.URI_PROP);
			image.setImageUri(createExpression(urlExpr));
		} else if (EngineIRConstants.IMAGE_REF_TYPE_EXPR.equals(imageSrc)) {
			ExpressionHandle valueExpr = handle.getExpressionProperty(ImageHandle.VALUE_EXPR_PROP);
			ExpressionHandle typeExpr = handle.getExpressionProperty(ImageHandle.TYPE_EXPR_PROP);
			image.setImageExpression(createExpression(valueExpr), createExpression(typeExpr));
		} else if (EngineIRConstants.IMAGE_REF_TYPE_EMBED.equals(imageSrc)) {
			String imageName = handle.getImageName();
			// FIXME: MODEL doesn't support the expression image name
			// ExpressionHandle nameExpr = handle
			// .getExpressionProperty( ImageHandle.IMAGE_NAME_PROP );
			image.setImageName(Expression.newConstant(imageName));
		} else if (EngineIRConstants.IMAGE_REF_TYPE_FILE.equals(imageSrc)) {
			ExpressionHandle fileExpr = handle.getExpressionProperty(ImageHandle.URI_PROP);
			image.setImageFile(createExpression(fileExpr));
		} else {
			assert false;
		}

		setCurrentElement(image);
	}

	private void handleAltText(ReportItemHandle handle, ReportItemDesign design) {
		ExpressionHandle altTextExpr = handle.getExpressionProperty(IReportItemModel.ALTTEXT_PROP);
		design.setAltText(createExpression(altTextExpr));
		design.setAltTextKey(handle.getAltTextKey());
	}

	@Override
	public void visitTable(TableHandle handle) {
		// Create Table Item
		TableItemDesign table = new TableItemDesign();
		table.setRepeatHeader(handle.repeatHeader());

		setupListingItem(table, handle);

		// Handle table summary
		String summary = handle.getSummary();
		if (summary != null) {
			table.setSummary(summary);
		}

		// Handle table caption
		String caption = handle.getCaption();
		String captionKey = handle.getCaptionKey();
		if (caption != null || captionKey != null) {
			table.setCaption(captionKey, caption);
		}

		// Handle table Columns
		SlotHandle columnSlot = handle.getColumns();
		for (int i = 0; i < columnSlot.getCount(); i++) {
			ColumnHandle columnHandle = (ColumnHandle) columnSlot.get(i);
			apply(columnHandle);
			if (currentElement != null) {
				ColumnDesign columnDesign = (ColumnDesign) currentElement;
				for (int j = 0; j < columnHandle.getRepeatCount(); j++) {
					table.addColumn(columnDesign);
				}
			}
		}

		// Handle Table Header
		SlotHandle headerSlot = handle.getHeader();
		if (headerSlot.getCount() > 0) {
			TableBandDesign header = createTableBand(headerSlot);
			header.setBandType(TableBandDesign.BAND_HEADER);
			table.setHeader(header);
		}

		// Handle grouping in table
		SlotHandle groupSlot = handle.getGroups();
		for (int i = 0; i < groupSlot.getCount(); i++) {
			apply(groupSlot.get(i));
			if (currentElement != null) {
				TableGroupDesign group = (TableGroupDesign) currentElement;
				group.setGroupLevel(i);
				table.addGroup(group);
			}
		}

		// Handle detail section
		SlotHandle detailSlot = handle.getDetail();
		if (detailSlot.getCount() > 0) {
			TableBandDesign detail = createTableBand(detailSlot);
			detail.setBandType(TableBandDesign.BAND_DETAIL);
			table.setDetail(detail);
		}

		// Handle table footer
		SlotHandle footerSlot = handle.getFooter();
		if (footerSlot.getCount() > 0) {
			TableBandDesign footer = createTableBand(footerSlot);
			footer.setBandType(TableBandDesign.BAND_FOOTER);
			table.setFooter(footer);
		}

		newCellId = new TableItemDesignLayout().layout(table, newCellId);

		int groupCount = table.getGroupCount();
		for (int i = 0; i < groupCount; i++) {
			TableGroupDesign group = (TableGroupDesign) table.getGroup(i);
			// load the group icons except the most inner group in summary table
			boolean isSummaryTable = handle.isSummaryTable();
			if (!isSummaryTable || isSummaryTable && i < groupCount - 1) {
				locateGroupIcon(group);
			}
		}

		applyColumnHighlight(table);
		// setup the supressDuplicate property of the data items
		applySuppressDuplicate(table);

		TableBandDesign detail = (TableBandDesign) table.getDetail();
		if (detail != null) {
			for (int i = 0; i < detail.getRowCount(); i++) {
				RowDesign row = detail.getRow(i);
				for (int j = 0; j < row.getCellCount(); j++) {
					CellDesign cell = row.getCell(j);
					ColumnDesign column = table.getColumn(cell.getColumn());
					if (!column.hasDataItemsInDetail()) {
						for (int k = 0; k < cell.getContentCount(); k++) {
							ReportItemDesign item = cell.getContent(k);
							if (item instanceof DataItemDesign) {
								column.setHasDataItemsInDetail(true);
								break;
							}
						}
					}
				}
			}
		}

		setCurrentElement(table);
	}

	private void applySuppressDuplicate(TableItemDesign table) {
		for (int i = 0; i < table.getGroupCount(); i++) {
			TableGroupDesign group = (TableGroupDesign) table.getGroup(i);
			applySuppressDuplicate(table, (TableBandDesign) group.getHeader());
			applySuppressDuplicate(table, (TableBandDesign) group.getFooter());
		}
		applySuppressDuplicate(table, (TableBandDesign) table.getDetail());
	}

	private void applySuppressDuplicate(TableItemDesign table, TableBandDesign band) {
		if (band == null) {
			return;
		}
		for (int i = 0; i < band.getRowCount(); i++) {
			RowDesign row = band.getRow(i);
			for (int j = 0; j < row.getCellCount(); j++) {
				CellDesign cell = row.getCell(j);
				ColumnDesign column = table.getColumn(cell.getColumn());
				if (column.getSuppressDuplicate()) {
					for (int k = 0; k < cell.getContentCount(); k++) {
						ReportItemDesign item = cell.getContent(k);
						if (item instanceof DataItemDesign) {
							DataItemDesign dataItem = (DataItemDesign) item;
							dataItem.setSuppressDuplicate(true);
						}
					}
				}
			}
		}
	}

	private void locateGroupIcon(TableGroupDesign group) {
		if (group.getHideDetail()) {
			return;
		}
		GroupHandle groupHandle = (GroupHandle) group.getHandle();
		TableHandle tableHandle = (TableHandle) groupHandle.getContainer();

		String keyExpression = groupHandle.getKeyExpr();
		if (keyExpression == null) {
			return;
		}
		keyExpression = keyExpression.trim();
		BandDesign groupHeader = group.getHeader();

		if (group.getHeader() == null) {
			return;
		}

		CellDesign firstStuffedCell = null;
		String columnBindingExpression = getColumnBinding(tableHandle, keyExpression);
		for (int i = 0; i < groupHeader.getContentCount(); i++) {
			RowDesign row = (RowDesign) groupHeader.getContent(i);
			for (int j = 0; j < row.getCellCount(); j++) {
				CellDesign cell = row.getCell(j);
				for (int k = 0; k < cell.getContentCount(); k++) {
					ReportItemDesign item = cell.getContent(k);
					if (hasExpression(tableHandle, item, keyExpression, columnBindingExpression)) {
						cell.setDisplayGroupIcon(true);
						return;
					}
				}
				if (cell.getContentCount() > 0 && null == firstStuffedCell) {
					firstStuffedCell = cell;
				}
			}
		}

		if (null != firstStuffedCell) {
			// if the group icon hasn't been set, set the icon to the first
			// stuffed cell.
			firstStuffedCell.setDisplayGroupIcon(true);
		} else {
			// if the group icon hasn't been set and all the cells are empty,
			// set the icon to the first cell.
			RowDesign row = (RowDesign) groupHeader.getContent(0);
			if (null != row) {
				CellDesign cell = row.getCell(0);
				if (null != cell) {
					cell.setDisplayGroupIcon(true);
				}
			}
		}
	}

	private boolean hasExpression(TableHandle tableHandle, ReportItemDesign item, String keyExpression,
			String columnBindingExpression) {
		assert keyExpression != null;
		if (item instanceof DataItemDesign) {
			DataItemDesign data = (DataItemDesign) item;
			String columnBinding = data.getBindingColumn();
			String value = ExpressionUtil.createJSRowExpression(columnBinding);
			if (value != null && keyExpression.equals(value.trim())) {
				return true;
			}
			columnBinding = getColumnBinding(tableHandle, value);
			if (columnBinding != null && columnBindingExpression != null
					&& columnBindingExpression.equals(columnBinding)) {
				return true;
			}
		}
		if (item instanceof GridItemDesign) {
			GridItemDesign grid = (GridItemDesign) item;
			GridHandle gridHandle = (GridHandle) grid.getHandle();
			PropertyHandle columnBindings = gridHandle.getColumnBindings();
			if (columnBindings != null && columnBindings.iterator().hasNext()) {
				return false;
			}
			for (int i = 0; i < grid.getRowCount(); i++) {
				RowDesign row = grid.getRow(i);
				for (int j = 0; j < row.getCellCount(); j++) {
					CellDesign cell = row.getCell(j);
					for (int k = 0; k < cell.getContentCount(); k++) {
						ReportItemDesign reportItem = cell.getContent(k);
						if (hasExpression(tableHandle, reportItem, keyExpression, columnBindingExpression)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	private String getColumnBinding(TableHandle tableHandle, String keyExpression) {
		String columnBindingName = null;
		try {
			columnBindingName = ExpressionUtil.getColumnBindingName(keyExpression);
		} catch (BirtException e) {
			logger.log(Level.FINE, e.getMessage(), e);
			return null;
		}
		return getColumnBindingByName(tableHandle, columnBindingName);
	}

	private String getColumnBindingByName(TableHandle tableHandle, String columnBindingName) {
		if (columnBindingName == null) {
			return null;
		}
		Iterator iterator = tableHandle.columnBindingsIterator();
		while (iterator.hasNext()) {
			ComputedColumnHandle columnBinding = (ComputedColumnHandle) iterator.next();
			if (columnBindingName.equals(columnBinding.getName())) {
				return columnBinding.getExpression();
			}
		}
		return null;
	}

	private void applyColumnHighlight(TableItemDesign table) {
		applyColumnHighlight(table, table.getHeader());
		applyColumnHighlight(table, table.getDetail());
		applyColumnHighlight(table, table.getFooter());
		for (int i = 0; i < table.getGroupCount(); i++) {
			applyColumnHighlight(table, table.getGroup(i).getHeader());
			applyColumnHighlight(table, table.getGroup(i).getFooter());
		}
	}

	private void applyColumnHighlight(GridItemDesign grid) {
		for (int i = 0; i < grid.getRowCount(); i++) {
			RowDesign row = grid.getRow(i);
			for (int j = 0; j < row.getCellCount(); j++) {
				CellDesign cell = row.getCell(j);
				ColumnDesign column = grid.getColumn(cell.getColumn());
				applyColumnHighlight(column, cell);
			}
		}
	}

	private void applyColumnHighlight(TableItemDesign table, BandDesign band) {
		if (band == null) {
			return;
		}
		for (int i = 0; i < band.getContentCount(); i++) {
			ReportItemDesign content = band.getContent(i);
			if (content instanceof RowDesign) {
				RowDesign row = (RowDesign) content;
				for (int j = 0; j < row.getCellCount(); j++) {
					CellDesign cell = row.getCell(j);
					ColumnDesign column = table.getColumn(cell.getColumn());
					applyColumnHighlight(column, cell);
				}
			}
		}
	}

	private void applyColumnHighlight(ColumnDesign column, CellDesign cell) {
		HighlightDesign columnHighlight = column.getHighlight();
		if (columnHighlight != null && columnHighlight.getRuleCount() > 0) {
			HighlightDesign cellHighlight = cell.getHighlight();
			if (cellHighlight == null) {
				cellHighlight = new HighlightDesign();
				cell.setHighlight(cellHighlight);
			}
			for (int i = 0; i < columnHighlight.getRuleCount(); i++) {
				cellHighlight.addRule(new HighlightRuleDesign(columnHighlight.getRule(i)));
			}
		}
	}

	@Override
	public void visitColumn(ColumnHandle handle) {
		// Create a Column, mostly used in Table or Grid
		ColumnDesign col = new ColumnDesign();
		// we do not procee the style and highlight since model has change them
		// from column to cell
		setupReportElement(col, handle);
		StyleDeclaration style = this.createColumnStyle(handle);
		if (style != null && !style.isEmpty()) {
			col.setStyleName(assignStyleName(style));
		}

		// is column header
		// FIXME: Model team hasn't finish the property "column-header", so the
		// false will be set here. It needs be fixed after Model team finish the
		// work.
		// col.setColumnHeaderState( handle.isColumnHeader( ) );
		col.setColumnHeaderState(false);

		// Column Width
		DimensionType width = createDimension(handle.getWidth(), false);
		col.setWidth(width);

		boolean supress = handle.suppressDuplicates();
		col.setSuppressDuplicate(supress);

		// Visibility
		VisibilityDesign visibility = createVisibility(handle.visibilityRulesIterator());
		col.setVisibility(visibility);

		// setupHighlight( col, null );

		setCurrentElement(col);
	}

	@Override
	public void visitRow(RowHandle handle) {
		// Create a Row, mostly used in Table and Grid Item
		RowDesign row = new RowDesign();
		setupStyledElement(row, handle);

		// Row Height
		DimensionType height = createDimension(handle.getHeight(), false);
		row.setHeight(height);

		// Book mark
		ExpressionHandle bookmarkExpr = handle.getExpressionProperty(RowHandle.BOOKMARK_PROP);
		row.setBookmark(createExpression(bookmarkExpr));

		// Visibility
		VisibilityDesign visibility = createVisibility(handle.visibilityRulesIterator());
		row.setVisibility(visibility);

		// Cells in a row
		SlotHandle cellSlot = handle.getCells();
		for (int i = 0; i < cellSlot.getCount(); i++) {
			apply(cellSlot.get(i));
			if (currentElement != null) {
				row.addCell((CellDesign) currentElement);
			}
		}

		String onCreate = handle.getOnCreate();
		Expression.Script onCreateScript = createScript(onCreate);
		if (onCreateScript != null) {
			String id = ModuleUtil.getScriptUID(handle.getPropertyHandle(ITableRowModel.ON_CREATE_METHOD));
			onCreateScript.setFileName(id);
			row.setOnCreate(onCreateScript);
		}

		String onRender = handle.getOnRender();
		Expression.Script onRenderScript = createScript(onRender);
		if (onRenderScript != null) {
			String id = ModuleUtil.getScriptUID(handle.getPropertyHandle(ITableRowModel.ON_RENDER_METHOD));
			onRenderScript.setFileName(id);
			row.setOnRender(onRenderScript);
		}

		setupHighlight(row, null);
		/*
		 * model hasn't send onPageBreak to us row.setOnPageBreak(
		 * handle.getOnPageBreak( ) );
		 */

		row.setRepeatable(handle.repeatable());

		setCurrentElement(row);
	}

	/**
	 * Sets up cell element's style attribute.
	 *
	 * @param cell   engine's styled cell element.
	 * @param handle DE's styled cell element.
	 */
	protected void setupStyledElement(StyledElementDesign design, ReportElementHandle handle) {
		// Styled element is a report element
		setupReportElement(design, handle);

		StyleDeclaration style = createStyle(handle, design);
		if (style != null && !style.isEmpty()) {
			design.setStyleName(assignStyleName(style));
		}
	}

	@Override
	public void visitCell(CellHandle handle) {
		// Create a Cell
		CellDesign cell = new CellDesign();
		setupStyledElement(cell, handle);
		setupAuralInfomation(cell, handle);
		// Cell contents
		SlotHandle contentSlot = handle.getContent();
		for (int i = 0; i < contentSlot.getCount(); i++) {
			apply(contentSlot.get(i));
			if (currentElement != null) {
				cell.addContent((ReportItemDesign) currentElement);
			}
		}

		// Span, Drop properties of a cell
		// FIXME: change the colspan/rowspan after MODEL fix the bug
		// cell.setColSpan( LayoutUtil.getEffectiveColumnSpan( handle ) );
		cell.setColSpan(handle.getColumnSpan());
		int columnId = handle.getColumn() - 1;
		if (columnId < 0) {
			columnId = -1;
		}
		cell.setColumn(columnId);
		// cell.setRowSpan( LayoutUtil.getEffectiveRowSpan( handle ) );
		cell.setRowSpan(handle.getRowSpan());
		if (isCellInGroupHeader(handle)) {
			cell.setDrop(handle.getDrop());
		}

		String onCreate = handle.getOnCreate();
		Expression.Script onCreateScript = createScript(onCreate);
		if (onCreateScript != null) {
			String id = ModuleUtil.getScriptUID(handle.getPropertyHandle(ICellModel.ON_CREATE_METHOD));
			onCreateScript.setFileName(id);
			cell.setOnCreate(onCreateScript);
		}

		String onRender = handle.getOnRender();
		Expression.Script onRenderScript = createScript(onRender);
		if (onRenderScript != null) {
			String id = ModuleUtil.getScriptUID(handle.getPropertyHandle(ICellModel.ON_RENDER_METHOD));
			onRenderScript.setFileName(id);
			cell.setOnRender(onRenderScript);
		}

		setupHighlight(cell, null);
		/*
		 * model hasn't send onPageBreak to us cell.setOnPageBreak(
		 * handle.getOnPageBreak( ) );
		 */

		int diagonalNumber = handle.getDiagonalNumber();
		if (diagonalNumber > 0) {
			// The default diagonalNumber value from Model is 0.
			cell.setDiagonalNumber(diagonalNumber);
			cell.setDiagonalStyle(handle.getDiagonalStyle());
			cell.setDiagonalWidth(createDimension(handle.getDiagonalThickness(), true));
			ColorHandle colorHandle = handle.getDiagonalColor();
			if (colorHandle != null) {
				cell.setDiagonalColor(colorHandle.getStringValue());
			}
		}
		int antidiagonalNumber = handle.getAntidiagonalNumber();
		if (antidiagonalNumber > 0) {
			// The default antidiagonalNumber value from Model is 0.
			cell.setAntidiagonalNumber(antidiagonalNumber);
			cell.setAntidiagonalStyle(handle.getAntidiagonalStyle());
			cell.setAntidiagonalWidth(createDimension(handle.getAntidiagonalThickness(), true));
			ColorHandle colorHandle = handle.getAntidiagonalColor();
			if (colorHandle != null) {
				cell.setAntidiagonalColor(colorHandle.getStringValue());
			}
		}

		setCurrentElement(cell);
	}

	private void setupAuralInfomation(CellDesign cell, CellHandle handle) {
		ExpressionHandle bookmarkExpr = handle.getExpressionProperty(CellHandle.BOOKMARK_PROP);
		cell.setBookmark(createExpression(bookmarkExpr));
		ExpressionHandle headersExpr = handle.getExpressionProperty(CellHandle.HEADERS_PROP);
		cell.setHeaders(createExpression(headersExpr));
		String scope = handle.getScope();
		if (scope != null) {
			cell.setScope(scope);
		}
	}

	private boolean isCellInGroupHeader(CellHandle cellHandle) {
		DesignElementHandle rowHandle = cellHandle.getContainer();
		if (rowHandle instanceof RowHandle) {
			DesignElementHandle groupHandle = rowHandle.getContainer();
			if (groupHandle instanceof TableGroupHandle) {
				SlotHandle slot = rowHandle.getContainerSlotHandle();
				if (slot != null) {
					if (slot.getSlotID() == GroupHandle.HEADER_SLOT) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * create a list band using the items in slot.
	 *
	 * @param elements items in DE's IR
	 * @return ListBand.
	 */
	private ListBandDesign createListBand(SlotHandle elements) {
		ListBandDesign band = new ListBandDesign();
		band.setID(generateUniqueID());
		setupElementIDMap(band);

		for (int i = 0; i < elements.getCount(); i++) {
			apply(elements.get(i));
			if (currentElement != null) {
				band.addContent((ReportItemDesign) currentElement);
			}
		}

		return band;
	}

	/**
	 * create a list group using the DE's ListGroup.
	 *
	 * @param handle De's list group
	 * @return engine's list group
	 */
	@Override
	public void visitListGroup(ListGroupHandle handle) {
		ListGroupDesign listGroup = new ListGroupDesign();

		setupGroup(listGroup, handle);

		SlotHandle headerSlot = handle.getHeader();
		if (headerSlot.getCount() > 0) {
			ListBandDesign header = createListBand(headerSlot);
			header.setBandType(ListBandDesign.GROUP_HEADER);
			header.setGroup(listGroup);
			listGroup.setHeader(header);
			listGroup.setHeaderRepeat(handle.repeatHeader());

			// flatten TOC on group to the first report item in group header
			TOCHandle toc = handle.getTOC();
			if (toc != null) {
				ExpressionHandle tocExpr = toc.getExpressionProperty(TOC.TOC_EXPRESSION);
				listGroup.setTOC(createExpression(tocExpr));
			}
		}

		SlotHandle footerSlot = handle.getFooter();
		if (footerSlot.getCount() > 0) {
			ListBandDesign footer = createListBand(footerSlot);
			footer.setBandType(ListBandDesign.GROUP_FOOTER);
			footer.setGroup(listGroup);
			listGroup.setFooter(footer);
		}

		listGroup.setHideDetail(handle.hideDetail());

		setCurrentElement(listGroup);
	}

	/**
	 * create a table group using the DE's TableGroup.
	 *
	 * @param handle De's table group
	 * @return engine's table group
	 */
	@Override
	public void visitTableGroup(TableGroupHandle handle) {
		TableGroupDesign tableGroup = new TableGroupDesign();

		setupGroup(tableGroup, handle);

		SlotHandle headerSlot = handle.getHeader();
		if (headerSlot.getCount() > 0) {
			TableBandDesign header = createTableBand(handle.getHeader());
			header.setBandType(TableBandDesign.GROUP_HEADER);
			header.setGroup(tableGroup);
			tableGroup.setHeader(header);
			tableGroup.setHeaderRepeat(handle.repeatHeader());

			// flatten TOC on group to the first report item in group header
			TOCHandle tocHandle = handle.getTOC();
			if (tocHandle != null) {
				ExpressionHandle tocExpr = tocHandle.getExpressionProperty(TOC.TOC_EXPRESSION);
				tableGroup.setTOC(createExpression(tocExpr));
			}
		}

		SlotHandle footerSlot = handle.getFooter();
		if (footerSlot.getCount() > 0) {
			TableBandDesign footer = createTableBand(handle.getFooter());
			footer.setBandType(TableBandDesign.GROUP_FOOTER);
			footer.setGroup(tableGroup);
			tableGroup.setFooter(footer);
		}

		tableGroup.setHideDetail(handle.hideDetail());

		setCurrentElement(tableGroup);
	}

	@Override
	public void visitTextItem(TextItemHandle handle) {
		// Create Text Item
		TextItemDesign textItem = new TextItemDesign();
		setupReportItem(textItem, handle);

		String contentType = handle.getContentType();
		if (contentType != null) {
			textItem.setTextType(contentType);
		}
		textItem.setText(handle.getContentKey(), handle.getContent());

		textItem.setHasExpression(handle.hasExpression());
		textItem.setJTidy(handle.isJTidy());

		currentElement = textItem;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.DesignVisitor#visitExtendedItem(org.eclipse
	 * .birt.report.model.api.ExtendedItemHandle)
	 */
	@Override
	protected void visitExtendedItem(ExtendedItemHandle obj) {
		ExtendedItemDesign extendedItem = new ExtendedItemDesign();
		setupReportItem(extendedItem, obj);

		handleExtendedItemChildren(extendedItem, obj);

		setCurrentElement(extendedItem);
	}

	/**
	 * process extended item's children.
	 *
	 * @param extendedItem
	 * @param extendedHandle
	 */
	private void handleExtendedItemChildren(ExtendedItemDesign extendedItem, ExtendedItemHandle extendedHandle) {
		if (extendedHandle == null) {
			return;
		}

		Iterator propIter = extendedHandle.getPropertyIterator();
		while (propIter.hasNext()) {
			PropertyHandle propHandle = (PropertyHandle) propIter.next();
			IElementPropertyDefn property = propHandle.getPropertyDefn();
			if (property.getTypeCode() == IPropertyType.ELEMENT_TYPE) {
				Object children = propHandle.getValue();
				if (children instanceof List) {
					List tempList = (List) children;
					for (int i = 0; tempList != null && i < tempList.size(); i++) {
						Object tempObj = tempList.get(i);
						if (tempObj instanceof ReportItemHandle) {
							apply((ReportItemHandle) tempObj);
							extendedItem.getChildren().add(currentElement);
						}
					}
				} else if (children instanceof ReportItemHandle) {
					apply((ReportItemHandle) children);
					extendedItem.getChildren().add(currentElement);
				}
			}
		}
	}

	@Override
	public void visitTemplateReportItem(TemplateReportItemHandle obj) {
		TemplateDesign template = new TemplateDesign();
		setupTemplateReportElement(template, obj);
		template.setPromptText(obj.getDescription());
		template.setPromptTextKey(obj.getDescriptionKey());
		template.setAllowedType(obj.getAllowedType());

		setCurrentElement(template);
	}

	protected void setupGroup(GroupDesign group, GroupHandle handle) {
		// name
		group.setID(handle.getID());
		setupElementIDMap(group);
		group.setName(handle.getName());
		String pageBreakBefore = handle.getPageBreakBefore();
		String pageBreakAfter = handle.getPageBreakAfter();
		String pageBreakInside = handle.getPageBreakInside();
		group.setPageBreakBefore(pageBreakBefore);
		group.setPageBreakAfter(pageBreakAfter);
		group.setPageBreakInside(pageBreakInside);

		// setup TOC expression
		TOCHandle tocHandle = handle.getTOC();
		if (tocHandle != null) {
			ExpressionHandle tocExpr = tocHandle.getExpressionProperty(TOC.TOC_EXPRESSION);
			group.setTOC(createExpression(tocExpr));
		}
		// bookmark
		ExpressionHandle bookmarkExpr = handle.getExpressionProperty(GroupHandle.BOOKMARK_PROP);
		group.setBookmark(createExpression(bookmarkExpr));

		// set up OnCreate, OnRender, OnPageBreak
		String onCreate = handle.getOnCreate();
		Expression.Script onCreateScript = createScript(onCreate);
		if (onCreateScript != null) {
			String id = ModuleUtil.getScriptUID(handle.getPropertyHandle(IGroupElementModel.ON_CREATE_METHOD));
			onCreateScript.setFileName(id);
			group.setOnCreate(onCreateScript);
		}

		String onRender = handle.getOnRender();
		Expression.Script onRenderScript = createScript(onRender);
		if (onRenderScript != null) {
			String id = ModuleUtil.getScriptUID(handle.getPropertyHandle(IGroupElementModel.ON_RENDER_METHOD));
			onRenderScript.setFileName(id);
			group.setOnRender(onRenderScript);
		}

		String onPageBreak = handle.getOnPageBreak();
		Expression.Script onPageBreakScript = createScript(onPageBreak);
		if (onPageBreakScript != null) {
			String id = ModuleUtil.getScriptUID(handle.getPropertyHandle(IGroupElementModel.ON_PAGE_BREAK_METHOD));
			onPageBreakScript.setFileName(id);
			group.setOnPageBreak(onPageBreakScript);
		}

		group.setHandle(handle);
		group.setJavaClass(handle.getEventHandlerClass());

	}

	/**
	 * create a table band using the items in slot.
	 *
	 * @param elements items in DE's IR
	 * @return TableBand.
	 */
	private TableBandDesign createTableBand(SlotHandle elements) {
		TableBandDesign band = new TableBandDesign();
		band.setID(generateUniqueID());
		setupElementIDMap(band);

		for (int i = 0; i < elements.getCount(); i++) {
			apply(elements.get(i));
			if (currentElement != null) {
				band.addRow((RowDesign) currentElement);
			}
		}

		return band;
	}

	/**
	 * Creates the property visibility
	 *
	 * @param visibilityRulesIterator the handle's rules iterator
	 * @return null only if the iterator is null or it contains no rules, otherwise
	 *         VisibilityDesign
	 */
	protected VisibilityDesign createVisibility(Iterator visibilityRulesIterator) {
		if (visibilityRulesIterator != null) {
			if (visibilityRulesIterator.hasNext()) {
				VisibilityDesign visibility = new VisibilityDesign();
				do {
					VisibilityRuleDesign hide = createHide((HideRuleHandle) visibilityRulesIterator.next());
					visibility.addRule(hide);
				} while (visibilityRulesIterator.hasNext());
				return visibility;
			}
		}
		return null;
	}

	/**
	 * Creates the visibility rule( i.e. the hide)
	 *
	 * @param handle the DE's handle
	 * @return the created visibility rule
	 */
	protected VisibilityRuleDesign createHide(HideRuleHandle handle) {
		VisibilityRuleDesign rule = new VisibilityRuleDesign();
		ExpressionHandle valueExpr = handle.getExpressionProperty(HideRule.VALUE_EXPR_MEMBER);
		rule.setExpression(createExpression(valueExpr));
		String format = handle.getFormat();
		if ("viewer".equalsIgnoreCase(format)) //$NON-NLS-1$
		{
			format = "html";
		}
		rule.setFormat(format);
		return rule;
	}

	/**
	 * setup the attribute of report item
	 *
	 * @param item   Engine's Report Item
	 * @param handle DE's report item.
	 */
	private void setupReportItem(ReportItemDesign item, ReportItemHandle handle) {
		setupStyledElement(item, handle);

		// x, y, width & height
		DimensionType height = createDimension(handle.getHeight(), false);
		DimensionType width = createDimension(handle.getWidth(), false);
		DimensionType x = createDimension(handle.getX(), false);
		DimensionType y = createDimension(handle.getY(), false);
		item.setHeight(height);
		item.setWidth(width);
		item.setX(x);
		item.setY(y);

		// setup TOC expression
		TOCHandle tocHandle = handle.getTOC();
		if (tocHandle != null) {
			ExpressionHandle tocExpr = tocHandle.getExpressionProperty(TOC.TOC_EXPRESSION);
			item.setTOC(createExpression(tocExpr));
		}

		// setup book mark
		ExpressionHandle bookmarkExpr = handle.getExpressionProperty(ReportItemHandle.BOOKMARK_PROP);
		item.setBookmark(createExpression(bookmarkExpr));

		String onCreate = handle.getOnCreate();
		Expression.Script onCreateScript = createScript(onCreate);
		if (onCreateScript != null) {
			String id = ModuleUtil.getScriptUID(handle.getPropertyHandle(IReportItemModel.ON_CREATE_METHOD));
			onCreateScript.setFileName(id);
			item.setOnCreate(onCreateScript);
		}

		String onRender = handle.getOnRender();
		Expression.Script onRenderScript = createScript(onRender);
		if (onRenderScript != null) {
			String id = ModuleUtil.getScriptUID(handle.getPropertyHandle(IReportItemModel.ON_RENDER_METHOD));
			onRenderScript.setFileName(id);
			item.setOnRender(onRenderScript);
		}

		String onPageBreak = handle.getOnPageBreak();
		Expression.Script onPageBreakScript = createScript(onPageBreak);
		if (onPageBreakScript != null) {
			String id = ModuleUtil.getScriptUID(handle.getPropertyHandle(IReportItemModel.ON_PAGE_BREAK_METHOD));
			onPageBreakScript.setFileName(id);
			item.setOnPageBreak(onPageBreakScript);
		}

		// Sets up the visibility
		Iterator visibilityIter = handle.visibilityRulesIterator();
		VisibilityDesign visibility = createVisibility(visibilityIter);
		item.setVisibility(visibility);

		setupHighlight(item, null);

		// Set use cached result.
		if (handle.getDataBindingReference() != null) {
			item.setUseCachedResult(true);
		}

		handleAltText(handle, item);
	}

	/**
	 * setup report element attribute
	 *
	 * @param elem   engine's report element
	 * @param handle DE's report element
	 */
	private void setupReportElement(ReportElementDesign element, DesignElementHandle handle) {
		element.setHandle(handle);
		element.setName(handle.getName());
		long id = currentElementId == -1 ? handle.getID() : currentElementId;
		element.setID(id);

		// handle the properties
		Map<String, Expression> userProperties = createUserProperties(handle);
		if (userProperties != null && !userProperties.isEmpty()) {
			element.setUserProperties(userProperties);
		}

		setupElementIDMap(element);

		element.setJavaClass(handle.getEventHandlerClass());
	}

	/**
	 * setup template report element attribute
	 *
	 * @param elem   engine's template report element
	 * @param handle DE's report element
	 */
	private void setupTemplateReportElement(ReportItemDesign element, TemplateReportItemHandle handle) {
		setupReportElement(element, handle);

		// Sets up the visibility
		Iterator visibilityIter = handle.visibilityRulesIterator();
		VisibilityDesign visibility = createVisibility(visibilityIter);
		element.setVisibility(visibility);
	}

	/**
	 * create a Action.
	 *
	 * @param handle action in DE
	 * @return action in Engine.
	 */
	protected ActionDesign createAction(ActionHandle handle) {
		ActionDesign action = new ActionDesign();
		String linkType = handle.getLinkType();
		action.setTooltip(handle.getToolTip());

		if (EngineIRConstants.ACTION_LINK_TYPE_HYPERLINK.equals(linkType)) {
			ExpressionHandle urlExpr = handle.getExpressionProperty(Action.URI_MEMBER);
			action.setHyperlink(createExpression(urlExpr));
			action.setTargetWindow(handle.getTargetWindow());
		} else if (EngineIRConstants.ACTION_LINK_TYPE_BOOKMARK_LINK.equals(linkType)) {
			ExpressionHandle bookmarkExpr = handle.getExpressionProperty(Action.TARGET_BOOKMARK_MEMBER);
			action.setBookmark(createExpression(bookmarkExpr));
		} else if (EngineIRConstants.ACTION_LINK_TYPE_DRILL_THROUGH.equals(linkType)) {
			action.setTargetWindow(handle.getTargetWindow());
			DrillThroughActionDesign drillThrough = new DrillThroughActionDesign();
			action.setDrillThrough(drillThrough);

			// FIXME: the report name should support expression
			// ExpressionHandle reportNameExpr = handle
			// .getExpressionProperty( Action.REPORT_NAME_MEMBER );
			String reportNameExpr = handle.getReportName();
			drillThrough.setReportName(Expression.newConstant(reportNameExpr));
			drillThrough.setTargetFileType(handle.getTargetFileType());
			drillThrough.setFormat(handle.getFormatType());

			ExpressionHandle bookmarkExpr = handle.getExpressionProperty(Action.TARGET_BOOKMARK_MEMBER);
			drillThrough.setBookmark(createExpression(bookmarkExpr));

			drillThrough.setBookmarkType(
					!DesignChoiceConstants.ACTION_BOOKMARK_TYPE_TOC.equals(handle.getTargetBookmarkType()));
			Map<String, List<Expression>> params = new HashMap<>();
			Iterator paramIte = handle.paramBindingsIterator();
			while (paramIte.hasNext()) {
				ParamBindingHandle member = (ParamBindingHandle) paramIte.next();
				String name = member.getParamName();
				ExpressionListHandle listHandle = member.getExpressionListHandle();
				List<Expression> exprs = createExpression(listHandle);
				if (exprs != null) {
					params.put(name, exprs);
				}
			}
			drillThrough.setParameters(params);
			// XXX Search criteria is not supported yet.
			// Map search = new HashMap( );
			// Iterator searchIte = handle.searchIterator( );
			// while ( searchIte.hasNext( ) )
			// {
			// SearchKeyHandle member = (SearchKeyHandle) paramIte.next( );
			// params
			// .put( member., member
			// .getValue( ) );
			// }
			// drillThrough.setSearch( search );

		} else {
			assert (false);
		}

		return action;
	}

	/**
	 * create a highlight rule from a structure handle.
	 *
	 * @param ruleHandle rule in the MODEL.
	 * @return rule design, null if exist any error.
	 */
	protected HighlightRuleDesign createHighlightRule(StyledElementDesign design, HighlightRuleHandle ruleHandle,
			Expression defaultExpr) {
		HighlightRuleDesign rule = new HighlightRuleDesign();

		setupRuleDesign(rule, ruleHandle, defaultExpr);

		// all other properties are style properties,
		// copy those properties into a style design.
		StyleDeclaration style = new StyleDeclaration(cssEngine);

		setupStyle(design, ruleHandle, style);

		// this rule is empty, so we can drop it safely.
		if (style.isEmpty()) {
			return null;
		}
		rule.setStyle(style);
		return rule;
	}

	/**
	 * create highlight defined in the handle.
	 *
	 * @param item styled item.
	 */
	protected void setupHighlight(StyledElementDesign item, Expression defaultExpr) {
		StyleHandle handle = item.getHandle().getPrivateStyle();
		if (handle == null) {
			return;
		}
		// highlight Rules
		Iterator iter = handle.highlightRulesIterator();

		if (iter == null) {
			return;
		}

		if (iter.hasNext()) {
			HighlightDesign highlight = new HighlightDesign();

			do {
				HighlightRuleHandle ruleHandle = (HighlightRuleHandle) iter.next();
				HighlightRuleDesign rule = createHighlightRule(item, ruleHandle, defaultExpr);
				if (rule != null) {
					highlight.addRule(rule);
				}
			} while (iter.hasNext());
			item.setHighlight(highlight);

		}
	}

	/**
	 * setup a Map.
	 *
	 * @param item styled item;
	 */
	protected void setupMap(StyledElementDesign item, Expression defaultExpr) {
		StyleHandle handle = item.getHandle().getPrivateStyle();
		if (handle == null) {
			return;
		}
		Iterator iter = handle.mapRulesIterator();
		if (iter == null) {
			return;
		}
		MapDesign map = new MapDesign();

		while (iter.hasNext()) {
			MapRuleHandle ruleHandle = (MapRuleHandle) iter.next();
			MapRuleDesign rule = createMapRule(ruleHandle, defaultExpr);
			if (rule != null) {
				map.addRule(rule);
			}
		}

		if (map.getRuleCount() > 0) {
			item.setMap(map);
		}

	}

	/**
	 * create a map rule.
	 *
	 * @param obj map rule in DE.
	 * @return map rule in ENGINE.
	 */
	protected MapRuleDesign createMapRule(MapRuleHandle handle, Expression defaultExpr) {
		MapRuleDesign rule = new MapRuleDesign();
		setupRuleDesign(rule, handle, defaultExpr);
		String displayText = handle.getDisplay();
		rule.setDisplayText(handle.getDisplayKey(), displayText == null ? "" //$NON-NLS-1$
				: displayText);
		return rule;
	}

	private void setupRuleDesign(RuleDesign rule, StyleRuleHandle handle, Expression defaultExpr) {
		boolean isListStyle = ModuleUtil.isListStyleRuleValue(handle);
		if (isListStyle) {
			ExpressionListHandle exprHandles = handle.getValue1ExpressionList();
			List<Expression> exprs = createExpression(exprHandles);
			rule.setExpression(handle.getOperator(), exprs);
		} else {
			ExpressionListHandle exprHandles = handle.getValue1ExpressionList();
			List<Expression> exprs = createExpression(exprHandles);

			ExpressionHandle value2Expr = handle.getExpressionProperty(StyleRule.VALUE2_MEMBER);

			rule.setExpression(handle.getOperator(), exprs.size() > 0 ? exprs.get(0) : null,
					createExpression(value2Expr));
		}

		Expression testExpr = createExpression(handle.getExpressionProperty(StyleRule.TEST_EXPR_MEMBER));
		if (testExpr != null) {
			rule.setTestExpression(testExpr);
		} else if (defaultExpr != null) {
			rule.setTestExpression(defaultExpr);
		}
	}

	/**
	 * Checks if a given style is in report's style list, if not, assign a unique
	 * name to it and then add it to the style list.
	 *
	 * @param style The <code>StyleDeclaration</code> object.
	 * @return the name of the style.
	 */
	private String assignStyleName(StyleDeclaration style) {
		if (style == null || style.isEmpty()) {
			return null;
		}

		// Check if the style is already in report's style list
		Map styles = report.getStyles();
		Iterator iter = styles.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			// Cast the type mandatorily
			StyleDeclaration cachedStyle = (StyleDeclaration) entry.getValue();
			if (cachedStyle.equals(style)) {
				// There exist a style which has same properties with this
				// one,
				style = cachedStyle;
				return (String) entry.getKey();
			}
		}

		// the style is a new style, we need create a unique name for
		// it, and
		// add it into the report's style list.
		String styleName = PREFIX_STYLE_NAME + styles.size();
		report.addStyle(styleName, style);
		return styleName;
	}

	protected String getStyleProperty(StyleHandle handle, String name) {
		PropertyHandle prop = handle.getPropertyHandle(name);
		if (prop != null && prop.isSet()) {
			return prop.getStringValue();
		}
		return null;
	}

	protected String getElementProperty(ReportElementHandle handle, String name) {
		return getElementProperty(handle, name, false);
	}

	protected String getElementProperty(ReportElementHandle handle, String name, boolean isColorProperty) {
		FactoryPropertyHandle prop = handle.getFactoryPropertyHandle(name);
		if (prop != null && prop.isSet()) {
			if (isColorProperty) {
				return prop.getColorValue();
			}

			return prop.getStringValue();
		}
		return null;
	}

	String getElementColorProperty(ReportElementHandle handle, String name) {
		FactoryPropertyHandle prop = handle.getFactoryPropertyHandle(name);
		if (prop != null && prop.isSet()) {
			return prop.getColorValue();
		}
		return null;
	}

	protected String decodePageBreak(String pageBreak) {
		if (pageBreak == null) {
			return null;
		}
		if (DesignChoiceConstants.PAGE_BREAK_AFTER_ALWAYS.equals(pageBreak) || DesignChoiceConstants.PAGE_BREAK_AFTER_ALWAYS_EXCLUDING_LAST.equals(pageBreak)) {
			return IStyle.CSS_ALWAYS_VALUE;
		}
		if (DesignChoiceConstants.PAGE_BREAK_AFTER_AUTO.equals(pageBreak)) {
			return IStyle.CSS_AUTO_VALUE;
		}
		if (DesignChoiceConstants.PAGE_BREAK_AFTER_AVOID.equals(pageBreak)) {
			return IStyle.CSS_AVOID_VALUE;
		}
		if (DesignChoiceConstants.PAGE_BREAK_BEFORE_ALWAYS.equals(pageBreak)) {
			return IStyle.CSS_ALWAYS_VALUE;
		}
		if (DesignChoiceConstants.PAGE_BREAK_BEFORE_ALWAYS_EXCLUDING_FIRST.equals(pageBreak)) {
			return IStyle.CSS_ALWAYS_VALUE;
		}
		if (DesignChoiceConstants.PAGE_BREAK_BEFORE_AUTO.equals(pageBreak)) {
			return IStyle.CSS_AUTO_VALUE;
		}
		if (DesignChoiceConstants.PAGE_BREAK_BEFORE_AVOID.equals(pageBreak)) {
			return IStyle.CSS_AVOID_VALUE;
		}
		return IStyle.CSS_AUTO_VALUE;
	}

	protected StyleDeclaration createColumnStyle(ReportElementHandle handle) {
		StyleDeclaration style = new StyleDeclaration(cssEngine);

		String pageBreakAfter = getElementProperty(handle, StyleHandle.PAGE_BREAK_AFTER_PROP);
		style.setPageBreakAfter(decodePageBreak(pageBreakAfter));
		String pageBreakBefore = getElementProperty(handle, StyleHandle.PAGE_BREAK_BEFORE_PROP);
		style.setPageBreakBefore(decodePageBreak(pageBreakBefore));

		String display = getElementProperty(handle, StyleHandle.DISPLAY_PROP);
		style.setDisplay(display);
		return style;

	}

	private void createDataFormat(DesignElementHandle handle, StyleDeclaration style) {
		if (handle == null) {
			return;
		}

		Set<String> propertyNames = StyleUtil.customName2Index.keySet();
		for (String propertyName : propertyNames) {
			if (BIRTConstants.BIRT_STYLE_DATA_FORMAT.equalsIgnoreCase(propertyName)) {
				DataFormatValue formatSet = new DataFormatValue();
				boolean formatSetValid = false;
				FormatValue modelValue = (FormatValue) handle.getProperty(StyleHandle.STRING_FORMAT_PROP);
				if (modelValue != null) {
					ULocale locale = modelValue.getLocale();
					formatSet.setStringFormat(modelValue.getPattern(), locale == null ? null : locale.toString());
					formatSetValid = true;
				}
				modelValue = (FormatValue) handle.getProperty(StyleHandle.NUMBER_FORMAT_PROP);
				if (modelValue != null) {
					ULocale locale = modelValue.getLocale();
					formatSet.setNumberFormat(modelValue.getPattern(), locale == null ? null : locale.toString());
					formatSetValid = true;
				}
				modelValue = (FormatValue) handle.getProperty(StyleHandle.DATE_FORMAT_PROP);
				if (modelValue != null) {
					ULocale locale = modelValue.getLocale();
					formatSet.setDateFormat(modelValue.getPattern(), locale == null ? null : locale.toString());
					formatSetValid = true;
				}
				modelValue = (FormatValue) handle.getProperty(StyleHandle.TIME_FORMAT_PROP);
				if (modelValue != null) {
					ULocale locale = modelValue.getLocale();
					formatSet.setTimeFormat(modelValue.getPattern(), locale == null ? null : locale.toString());
					formatSetValid = true;
				}
				modelValue = (FormatValue) handle.getProperty(StyleHandle.DATE_TIME_FORMAT_PROP);
				if (modelValue != null) {
					ULocale locale = modelValue.getLocale();
					formatSet.setDateTimeFormat(modelValue.getPattern(), locale == null ? null : locale.toString());
					formatSetValid = true;
				}

				if (formatSetValid) {
					style.setProperty(StyleUtil.customName2Index.get(propertyName), formatSet);
				}
			}
		}
	}

	protected StyleDeclaration createStyle(ReportElementHandle handle, StyledElementDesign design) {
		// Background
		StyleDeclaration style = new StyleDeclaration(cssEngine);
		Set<String> styles = StyleUtil.styleName2Index.keySet();
		for (String propertyName : styles) {
			populateElementProperty(handle, design, style, propertyName);
		}
		createDataFormat(handle, style);
		return style;
	}

	private void populateElementProperty(ReportElementHandle handle, StyledElementDesign design, StyleDeclaration style,
			String propertyName) {
		boolean isColorProperty = StyleUtil.colorProperties.contains(propertyName);
		String elementProperty = getElementProperty(handle, propertyName, isColorProperty);
		populateStyle(design, style, propertyName, elementProperty);
	}

	private void populateStyle(StyledElementDesign design, IStyle style, String propertyName, String elementProperty) {
		int propertyIndex = StyleUtil.styleName2Index.get(propertyName);
		// TODO need support the expression style
		// if ( elementProperty.isExpression( ) )
		// {
		// design.setExpressionStyle( propertyIndex, createExpression(
		// elementProperty );
		// }
		// else
		// {
		style.setCssText(propertyIndex, elementProperty);
		// }
	}

	String getMemberProperty(Module module, StructureHandle handle, String name) {
		MemberHandle prop = handle.getMember(name);
		if (prop != null) {
			Object value = prop.getContext().getLocalValue(module);
			if (value != null) {
				return prop.getStringValue();
			}

			// for highlight rule, reutrn the referred style local value
			if (handle instanceof HighlightRuleHandle) {
				StyleHandle styleHandle = ((HighlightRuleHandle) handle).getStyle();
				if (styleHandle == null) {
					return null;
				}
				FactoryPropertyHandle propHandle = styleHandle.getFactoryPropertyHandle(name);
				if (propHandle == null) {
					return null;
				}
				return propHandle.getStringValue();
			} else {
				return null;
			}
		}
		return null;
	}

	void setupStyle(StyledElementDesign design, StructureHandle highlight, IStyle style) {
		Set<String> propertyNames = StyleUtil.ruleStyleName2Index.keySet();
		for (String propertyName : propertyNames) {
			populateHighlightStyle(design, highlight, style, propertyName);
		}
		createDataFormat(highlight, style);
	}

	private void createDataFormat(StructureHandle handle, IStyle style) {
		if (handle == null) {
			return;
		}

		Set<String> propertyNames = StyleUtil.customName2Index.keySet();
		for (String propertyName : propertyNames) {
			if (BIRTConstants.BIRT_STYLE_DATA_FORMAT.equalsIgnoreCase(propertyName)) {
				DataFormatValue formatSet = new DataFormatValue();
				boolean formatSetValid = false;
				FormatValue modelValue = (FormatValue) handle.getProperty(StyleHandle.STRING_FORMAT_PROP);
				if (modelValue != null) {
					ULocale locale = modelValue.getLocale();
					formatSet.setStringFormat(modelValue.getPattern(), locale == null ? null : locale.toString());
					formatSetValid = true;
				}
				modelValue = (FormatValue) handle.getProperty(StyleHandle.NUMBER_FORMAT_PROP);
				if (modelValue != null) {
					ULocale locale = modelValue.getLocale();
					formatSet.setNumberFormat(modelValue.getPattern(), locale == null ? null : locale.toString());
					formatSetValid = true;
				}
				modelValue = (FormatValue) handle.getProperty(StyleHandle.DATE_FORMAT_PROP);
				if (modelValue != null) {
					ULocale locale = modelValue.getLocale();
					formatSet.setDateFormat(modelValue.getPattern(), locale == null ? null : locale.toString());
					formatSetValid = true;
				}
				modelValue = (FormatValue) handle.getProperty(StyleHandle.TIME_FORMAT_PROP);
				if (modelValue != null) {
					ULocale locale = modelValue.getLocale();
					formatSet.setTimeFormat(modelValue.getPattern(), locale == null ? null : locale.toString());
					formatSetValid = true;
				}
				modelValue = (FormatValue) handle.getProperty(StyleHandle.DATE_TIME_FORMAT_PROP);
				if (modelValue != null) {
					ULocale locale = modelValue.getLocale();
					formatSet.setDateTimeFormat(modelValue.getPattern(), locale == null ? null : locale.toString());
					formatSetValid = true;
				}

				if (formatSetValid) {
					style.setProperty(StyleUtil.customName2Index.get(propertyName), formatSet);
				}
			}
		}
	}

	private void populateHighlightStyle(StyledElementDesign design, StructureHandle highlight, IStyle style,
			String propertyName) {
		Module module = design.getHandle().getModule();
		String property = getMemberProperty(module, highlight, propertyName);
		populateStyle(design, style, propertyName, property);
	}

	protected DimensionType createDimension(DimensionHandle handle, boolean useDefault) {
		if (handle == null || !useDefault && !handle.isSet()) {
			return null;
		}
		// Extended Choice
		if (handle.isKeyword()) {
			return new DimensionType(handle.getStringValue());
		}
		// set measure and unit
		double measure = handle.getMeasure();
		String unit = handle.getUnits();

		if (unit == null || unit.length() == 0) {
			unit = handle.getDefaultUnit();
		}
		return new DimensionType(measure, unit);
	}

	protected void setupListingItem(ListingDesign listing, ListingHandle handle) {
		// setup related scripts
		setupReportItem(listing, handle);

		int interval = handle.getPageBreakInterval();
		if (interval > 0) {
			listing.setPageBreakInterval(interval);
		}
		// setup scripts
		// listing.setOnStart( handle.getOnStart( ) );
		// listing.setOnRow( handle.getOnRow( ) );
		// listing.setOnFinish( handle.getOnFinish( ) );
	}

	protected void addReportDefaultPropertyValue(String name, StyleHandle handle) {
		addReportDefaultPropertyValue(name, handle, false);
	}

	protected void addReportDefaultPropertyValue(String name, StyleHandle handle, boolean isColorProperty) {
		Object value = null;
		int index = StylePropertyMapping.getPropertyID(name);

		if (StylePropertyMapping.canInherit(name)) {
			if (handle != null) {
				if (isColorProperty) {
					value = handle.getColorProperty(name).getStringValue();
				} else {
					value = handle.getProperty(name);
				}
			}
			if (value == null) {
				// bidi_hcg added arg
				value = StylePropertyMapping.getDefaultValue(name, this.handle);
			}

			inheritableReportStyle.setCssText(index, value == null ? null : value.toString());
		} else {
			// bidi_hcg added arg
			value = StylePropertyMapping.getDefaultValue(name, this.handle);
			nonInheritableReportStyle.setCssText(index, value == null ? null : value.toString());
		}

	}

	/**
	 * Creates Report default styles
	 */
	protected void createReportDefaultStyles(ReportDesignHandle reportDesignHandle) {

		StyleHandle handle = reportDesignHandle.findStyle("report");//$NON-NLS-1$
		nonInheritableReportStyle = new StyleDeclaration(cssEngine);
		inheritableReportStyle = new StyleDeclaration(cssEngine);

		// Background
		addReportDefaultPropertyValue(Style.BACKGROUND_COLOR_PROP, handle, true);
		addReportDefaultPropertyValue(Style.BACKGROUND_IMAGE_PROP, handle);
		addReportDefaultPropertyValue(Style.BACKGROUND_POSITION_X_PROP, handle);
		addReportDefaultPropertyValue(Style.BACKGROUND_POSITION_Y_PROP, handle);
		addReportDefaultPropertyValue(Style.BACKGROUND_REPEAT_PROP, handle);

		// bidi_hcg: Bidi related.
		addReportDefaultPropertyValue(Style.TEXT_DIRECTION_PROP, handle);

		// Text related
		addReportDefaultPropertyValue(Style.TEXT_ALIGN_PROP, handle);
		addReportDefaultPropertyValue(Style.TEXT_INDENT_PROP, handle);
		addReportDefaultPropertyValue(Style.LETTER_SPACING_PROP, handle);
		addReportDefaultPropertyValue(Style.LINE_HEIGHT_PROP, handle);
		addReportDefaultPropertyValue(Style.ORPHANS_PROP, handle);
		addReportDefaultPropertyValue(Style.TEXT_TRANSFORM_PROP, handle);
		addReportDefaultPropertyValue(Style.VERTICAL_ALIGN_PROP, handle);
		addReportDefaultPropertyValue(Style.WHITE_SPACE_PROP, handle);
		addReportDefaultPropertyValue(Style.WIDOWS_PROP, handle);
		addReportDefaultPropertyValue(Style.WORD_SPACING_PROP, handle);

		// Section properties
		addReportDefaultPropertyValue(Style.DISPLAY_PROP, handle);
		addReportDefaultPropertyValue(Style.MASTER_PAGE_PROP, handle);
		addReportDefaultPropertyValue(Style.PAGE_BREAK_AFTER_PROP, handle);
		addReportDefaultPropertyValue(Style.PAGE_BREAK_BEFORE_PROP, handle);
		addReportDefaultPropertyValue(Style.PAGE_BREAK_INSIDE_PROP, handle);

		// Font related
		addReportDefaultPropertyValue(Style.FONT_FAMILY_PROP, handle);
		addReportDefaultPropertyValue(Style.COLOR_PROP, handle, true);
		addReportDefaultPropertyValue(Style.FONT_SIZE_PROP, handle);
		addReportDefaultPropertyValue(Style.FONT_STYLE_PROP, handle);
		addReportDefaultPropertyValue(Style.FONT_WEIGHT_PROP, handle);
		addReportDefaultPropertyValue(Style.FONT_VARIANT_PROP, handle);

		// Text decoration
		addReportDefaultPropertyValue(Style.TEXT_LINE_THROUGH_PROP, handle);
		addReportDefaultPropertyValue(Style.TEXT_OVERLINE_PROP, handle);
		addReportDefaultPropertyValue(Style.TEXT_UNDERLINE_PROP, handle);

		// Border
		addReportDefaultPropertyValue(Style.BORDER_BOTTOM_COLOR_PROP, handle, true);
		addReportDefaultPropertyValue(Style.BORDER_BOTTOM_STYLE_PROP, handle);
		addReportDefaultPropertyValue(Style.BORDER_BOTTOM_WIDTH_PROP, handle);
		addReportDefaultPropertyValue(Style.BORDER_LEFT_COLOR_PROP, handle, true);
		addReportDefaultPropertyValue(Style.BORDER_LEFT_STYLE_PROP, handle);
		addReportDefaultPropertyValue(Style.BORDER_LEFT_WIDTH_PROP, handle);
		addReportDefaultPropertyValue(Style.BORDER_RIGHT_COLOR_PROP, handle, true);
		addReportDefaultPropertyValue(Style.BORDER_RIGHT_STYLE_PROP, handle);
		addReportDefaultPropertyValue(Style.BORDER_RIGHT_WIDTH_PROP, handle);
		addReportDefaultPropertyValue(Style.BORDER_TOP_COLOR_PROP, handle, true);
		addReportDefaultPropertyValue(Style.BORDER_TOP_STYLE_PROP, handle);
		addReportDefaultPropertyValue(Style.BORDER_TOP_WIDTH_PROP, handle);

		// Margin
		addReportDefaultPropertyValue(Style.MARGIN_TOP_PROP, handle);
		addReportDefaultPropertyValue(Style.MARGIN_LEFT_PROP, handle);
		addReportDefaultPropertyValue(Style.MARGIN_BOTTOM_PROP, handle);
		addReportDefaultPropertyValue(Style.MARGIN_RIGHT_PROP, handle);

		// Padding
		addReportDefaultPropertyValue(Style.PADDING_TOP_PROP, handle);
		addReportDefaultPropertyValue(Style.PADDING_LEFT_PROP, handle);
		addReportDefaultPropertyValue(Style.PADDING_BOTTOM_PROP, handle);
		addReportDefaultPropertyValue(Style.PADDING_RIGHT_PROP, handle);

		// Format
		/*
		 * addReportDefaultPropertyValue( Style.STRING_FORMAT_PROP, handle );
		 * addReportDefaultPropertyValue( Style.NUMBER_FORMAT_PROP, handle );
		 * addReportDefaultPropertyValue( Style.DATE_TIME_FORMAT_PROP, handle );
		 */
		createDataFormat(handle, nonInheritableReportStyle);
		createDataFormat(handle, inheritableReportStyle);

		if (!inheritableReportStyle.isEmpty()) {
			report.setRootStyleName(assignStyleName(inheritableReportStyle));
		}
	}

	/**
	 * Creates the body style for master page.
	 *
	 * @param design the master page design
	 * @return the content style
	 */
	protected String setupBodyStyle(MasterPageDesign design) {
		String styleName = design.getStyleName();
		IStyle style = report.findStyle(styleName);
		if (style == null || style.isEmpty()) {
			return null;
		}

		StyleDeclaration contentStyle = new StyleDeclaration(cssEngine);
		contentStyle.setProperty(IStyle.STYLE_BACKGROUND_COLOR, style.getProperty(IStyle.STYLE_BACKGROUND_COLOR));
		contentStyle.setProperty(IStyle.STYLE_BACKGROUND_IMAGE, style.getProperty(IStyle.STYLE_BACKGROUND_IMAGE));
		contentStyle.setProperty(IStyle.STYLE_BACKGROUND_POSITION_Y,
				style.getProperty(IStyle.STYLE_BACKGROUND_POSITION_Y));
		contentStyle.setProperty(IStyle.STYLE_BACKGROUND_POSITION_X,
				style.getProperty(IStyle.STYLE_BACKGROUND_POSITION_X));
		contentStyle.setProperty(IStyle.STYLE_BACKGROUND_REPEAT, style.getProperty(IStyle.STYLE_BACKGROUND_REPEAT));

		String bodyStyleName = assignStyleName(contentStyle);
		return bodyStyleName;
	}

	private void setupElementIDMap(ReportElementDesign rptElement) {
		report.setReportItemInstanceID(rptElement.getID(), rptElement);
	}

	protected long generateUniqueID() {
		newCellId = newCellId - 1;
		return newCellId;
	}

	private List<Expression> createExpression(ExpressionListHandle exprHandles) {
		List<Expression> listExprs = new ArrayList<>();
		List<org.eclipse.birt.report.model.api.Expression> exprs = exprHandles.getListValue();
		if (exprs != null) {
			for (org.eclipse.birt.report.model.api.Expression expr : exprs) {
				Expression expression = createExpression(expr);
				if (expression != null) {
					listExprs.add(createExpression(expr));
				}
			}
		}

		return listExprs;
	}

	private Expression createExpression(org.eclipse.birt.report.model.api.Expression expr) {
		if (expr != null) {
			String type = expr.getType();
			if (ExpressionType.CONSTANT.equals(type)) {
				String text = expr.getStringExpression();
				return Expression.newConstant(-1, text);
			} else {
				String text = expr.getStringExpression();
				if (text != null) {
					text = text.trim();
					if (text.length() > 0) {
						return Expression.newScript(type, text);
					}
				}
			}
		}
		return null;
	}

	private Expression createExpression(ExpressionHandle expressionHandle) {
		if (expressionHandle != null) {
			if (expressionHandle.isSet()) {
				String type = expressionHandle.getType();
				if (ExpressionType.CONSTANT.equals(type)) {
					// String valueType = expressionHandle.getValue( );
					String text = expressionHandle.getStringExpression();
					return Expression.newConstant(-1, text);
				} else {
					String text = expressionHandle.getStringExpression();
					if (text != null) {
						text = text.trim();
						if (text.length() > 0) {
							return Expression.newScript(type, text);
						}
					}
				}
			}
		}
		return null;
	}

	private Expression.Script createScript(String script) {
		if (script != null) {
			script = script.trim();
			if (script.length() > 0) {
				return Expression.newScript(defaultScriptLanguage, script);
			}
		}
		return null;
	}
}
