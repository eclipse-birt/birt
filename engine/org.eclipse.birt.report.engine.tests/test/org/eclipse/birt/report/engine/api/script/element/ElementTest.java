/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.engine.api.script.element;

import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.script.internal.element.ActionImpl;
import org.eclipse.birt.report.engine.script.internal.element.Cell;
import org.eclipse.birt.report.engine.script.internal.element.DataItem;
import org.eclipse.birt.report.engine.script.internal.element.DataSet;
import org.eclipse.birt.report.engine.script.internal.element.DataSource;
import org.eclipse.birt.report.engine.script.internal.element.DynamicText;
import org.eclipse.birt.report.engine.script.internal.element.Grid;
import org.eclipse.birt.report.engine.script.internal.element.Group;
import org.eclipse.birt.report.engine.script.internal.element.HideRuleImpl;
import org.eclipse.birt.report.engine.script.internal.element.HighlightRuleImpl;
import org.eclipse.birt.report.engine.script.internal.element.Image;
import org.eclipse.birt.report.engine.script.internal.element.Label;
import org.eclipse.birt.report.engine.script.internal.element.List;
import org.eclipse.birt.report.engine.script.internal.element.Listing;
import org.eclipse.birt.report.engine.script.internal.element.ReportItem;
import org.eclipse.birt.report.engine.script.internal.element.Row;
import org.eclipse.birt.report.engine.script.internal.element.StyleDesign;
import org.eclipse.birt.report.engine.script.internal.element.Table;
import org.eclipse.birt.report.engine.script.internal.element.TextItem;
import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.HideRuleHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.TextDataHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.elements.structures.HideRule;
import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;
import org.eclipse.birt.report.model.api.elements.structures.SortKey;

import com.ibm.icu.util.ULocale;

import junit.framework.TestCase;

/**
 * Tests the report element representations in the scripting environment.
 * Basically tests that all getters/setter correspond correctly to each other
 * (setFoo("foo") -> getFoo() should return "foo").
 *
 */
public class ElementTest extends TestCase {

	private static final String TARGET_WINDOW = "targetWindow";

	private static final String REPORT_NAME = "reportName";

	private static final int Y1 = 40;

	private static final int X1 = 30;

	private static final int WIDTH1 = 20;

	private static final String IN = "in";

	private static final int HEIGHT1 = 10;

	private static final String GRID = "Grid";

	private static final String CONTENT_KEY = "contentKey";

	private static final String TEXT_CONTENT = "textContent";

	private static final String TEXT_ITEM = "TextItem";

	private static final String CAPTION_KEY = "captionKey";

	private static final String CAPTION = "caption";

	private static final String TABLE = "Table";

	private static final String WORD_SPACING = "21px";

	private static final String WINDOWS = "6";

	private static final String TEXT_INDENT = "20px";

	private static final String STRINGFORMAT = "stringformat";

	private static final String PADDING_TOP = "19pt";

	private static final String PADDING_RIGHT = "18pt";

	private static final String PADDING_LEFT = "17pt";

	private static final String PADDING_BOTTOM = "16pt";

	private static final String ORPHANS = "5";

	private static final String MASTER_PAGE = "masterPage";

	private static final String MARGIN_TOP = "15pt";

	private static final String MARGIN_RIGHT = "14pt";

	private static final String MARGIN_LEFT = "13pt";

	private static final String MARGIN_BOTTOM = "12pt";

	private static final String LINE_HEIGHT = "11pt";

	private static final String LETTER_SPACING = "10pt";

	private static final String FONT_SIZE = "20pt";

	private static final String YY_MM_DD = "YY-MM-DD";

	private static final String WHITE = "white";

	private static final String BORDER_TOP_WIDTH = "15px";

	private static final String GRAY = "gray";

	private static final String BORDER_RIGHT_WIDTH = "14px";

	private static final String YELLOW = "yellow";

	private static final String BORDER_LEFT_WIDTH = "13px";

	private static final String GREEN = "green";

	private static final String BORDER_BOTTOM_WIDTH = "12px";

	private static final String BLUE = "blue";

	private static final String BACKGROUND_POSITION_Y = "11px";

	private static final String BACKGROUND_POSITION_X = "10px";

	private static final String IMAGE_URL = "imageUrl";

	private static final String RED = "red";

	private static final String STYLE = "Style";

	private static final String LIST = "List";

	private static final String VALUE_EXPRESSION = "valueExpression";

	private static final String URI = "URI";

	private static final String IMAGE_NAME = "imageName";

	private static final String IMAGE = "Image";

	private static final double INTERVAL_RANGE = 11.5;

	private static final String INTERVAL_BASE = "2000";

	private static final String YEAR = "year";

	private static final String TEST = "test";

	private static final String TEXT_DATA = "TextData";

	private static final String DATA_SOURCE = "DataSource";

	private static final String QUERY = "query";

	private static final String VALUE = "value";

	private static final String KEY = "key";

	private static final String TEXT_KEY = "textKey";

	private static final String TEXT = "text";

	private static final String HELP_TEXT_KEY = "helpTextKey";

	private static final String HELP_TEXT = "helpText";

	private static final String LABEL = "Label";

	private static final String Y2 = "40cm";

	private static final String X2 = "30cm";

	private static final String TOC = "TOC";

	private static final String WIDTH2 = "20cm";

	private static final String HEIGHT2 = "10cm";

	private static final String BOOKMARK = "bookmark";

	private static final String USER_PROP = "userProp";

	private static final String USER_PROP2 = "userProp2";

	private static final String INTEGER = "integer";

	private static final String USER_PROP1 = "userProp1";

	private static final String EXPRESSION = "expression";

	private static final String NAMED_EXPRESSION = "namedExpression";

	private static final String NAME = "name";

	private static final String DISPLAY_NAME_KEY = "displayNameKey";

	private static final String DISPLAY_NAME = "displayName";

	private static final String XML = "<xml/>";

	private static final String COMMENTS = "comments";

	private static final String IMAGE_FILE = "imageFile";

	private ElementFactory factory;

	private ReportDesignHandle designHandle;

	@Override
	public void setUp() {
		SessionHandle sessionHandle = new DesignEngine(new DesignConfig()).newSessionHandle(ULocale.ENGLISH);
		designHandle = sessionHandle.createDesign();
		factory = new ElementFactory(designHandle.getModule());
	}

	private void doTestAction(LabelHandle handle) throws SemanticException, ScriptException {
		ActionHandle actionHandle = handle.setAction(new Action());
		IAction action = new ActionImpl(actionHandle, handle);

		action.setFormatType(DesignChoiceConstants.ACTION_FORMAT_TYPE_HTML);
		assertEquals(DesignChoiceConstants.ACTION_FORMAT_TYPE_HTML, action.getFormatType());

		action.setLinkType(DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH);
		assertEquals(DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH, action.getLinkType());

		action.setReportName(REPORT_NAME);
		assertEquals(REPORT_NAME, action.getReportName());

		action.setTargetBookmark(BOOKMARK);
		assertEquals(BOOKMARK, action.getTargetBookmark());

		action.setTargetWindow(TARGET_WINDOW);
		assertEquals(TARGET_WINDOW, action.getTargetWindow());

		// Set to hyperlink so getURI not return null
		action.setLinkType(DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK);
		action.setURI(URI);
		assertEquals(URI, action.getURI());
	}

	private void doTestReportElement(IReportElement element) throws ScriptException {

		element.setComments(COMMENTS);
		assertEquals(COMMENTS, element.getComments());

		element.setCustomXml(XML);
		assertEquals(XML, element.getCustomXml());

		element.setDisplayName(DISPLAY_NAME);
		assertEquals(DISPLAY_NAME, element.getDisplayName());

		element.setDisplayNameKey(DISPLAY_NAME_KEY);
		assertEquals(DISPLAY_NAME_KEY, element.getDisplayNameKey());

		element.setName(NAME);
		assertEquals(NAME, element.getName());

		element.setNamedExpression(NAMED_EXPRESSION, EXPRESSION);
		assertEquals(EXPRESSION, element.getNamedExpression(NAMED_EXPRESSION));

		element.setUserProperty(USER_PROP1, new Integer(1), INTEGER);
		assertEquals(new Integer(1), element.getUserProperty(USER_PROP1));

		element.setUserProperty(USER_PROP2, USER_PROP);
		assertEquals(USER_PROP, element.getUserProperty(USER_PROP2));
	}

	private void doTestReportItem(IReportItem item) throws ScriptException {
		item.setBookmark(BOOKMARK);
		assertEquals(BOOKMARK, item.getBookmark());

		item.setHeight(HEIGHT1);
		assertEquals(HEIGHT1 + IN, item.getHeight());

		item.setHeight(HEIGHT2);
		assertEquals(HEIGHT2, item.getHeight());

		item.setWidth(WIDTH1);
		assertEquals(WIDTH1 + IN, item.getWidth());

		item.setWidth(WIDTH2);
		assertEquals(WIDTH2, item.getWidth());

		item.setTocExpression(TOC);
		assertEquals(TOC, item.getTocExpression());

		item.setX(X1);
		assertEquals(X1 + IN, item.getX());

		item.setX(X2);
		assertEquals(X2, item.getX());

		item.setY(Y1);
		assertEquals(Y1 + IN, item.getY());

		item.setY(Y2);
		assertEquals(Y2, item.getY());
	}

	public void testLabel() throws ScriptException, SemanticException {

		LabelHandle labelHandle = factory.newLabel(LABEL);
		ILabel label = new Label(labelHandle);

		doTestReportElement(label);
		doTestReportItem(label);
		doTestAction(labelHandle);

		label.setHelpText(HELP_TEXT);
		assertEquals(HELP_TEXT, label.getHelpText());

		label.setHelpTextKey(HELP_TEXT_KEY);
		assertEquals(HELP_TEXT_KEY, label.getHelpTextKey());

		label.setText(TEXT);
		assertEquals(TEXT, label.getText());

		label.setTextKey(TEXT_KEY);
		assertEquals(TEXT_KEY, label.getTextKey());
	}

	public void testCell() throws ScriptException {
		CellHandle cellHandle = factory.newCell();
		ICell cell = new Cell(cellHandle);

		cell.setColumn(2);
		assertEquals(2, cell.getColumn());

		assertEquals(1, cell.getColumnSpan());

		cell.setDrop(DesignChoiceConstants.DROP_TYPE_ALL);
		assertEquals(DesignChoiceConstants.DROP_TYPE_ALL, cell.getDrop());

		assertEquals(1, cell.getRowSpan());
	}

	public void testDataItem() throws ScriptException {
		DataItemHandle dataHandle = factory.newDataItem("DataItem");
		IDataItem data = new DataItem(dataHandle);

		data.setHelpText(HELP_TEXT);
		assertEquals(HELP_TEXT, data.getHelpText());

		data.setHelpTextKey(HELP_TEXT_KEY);
		assertEquals(HELP_TEXT_KEY, data.getHelpTextKey());

	}

	/**
	 * Test <code>IDataBinding</code> method.
	 *
	 * @throws ScriptException
	 * @throws SemanticException
	 */

	public void testDataBinding() throws ScriptException, SemanticException {
		TableHandle tableHandle = factory.newTableItem("table", 3);//$NON-NLS-1$
		designHandle.getBody().add(tableHandle);

		ComputedColumn column = StructureFactory.newComputedColumn(tableHandle, "column1");//$NON-NLS-1$
		column.setExpression("row[\"123\"]");//$NON-NLS-1$
		tableHandle.addColumnBinding(column, true);

		ComputedColumn column2 = StructureFactory.newComputedColumn(tableHandle, "column2");//$NON-NLS-1$
		column2.setExpression("row[\"234\"]");//$NON-NLS-1$
		tableHandle.addColumnBinding(column2, true);

		IReportItem item = new ReportItem(tableHandle);
		IDataBinding[] bindings = item.getDataBindings();
		assertEquals(2, bindings.length);

		IDataBinding binding = bindings[0];
		IDataBinding binding2 = bindings[1];

		assertEquals("column1", binding.getName());//$NON-NLS-1$
		assertEquals("column2", binding2.getName());//$NON-NLS-1$

		binding.setAggregateOn("11"); //$NON-NLS-1$
		assertEquals("11", binding.getAggregateOn()); //$NON-NLS-1$

		binding.setDataType("string"); //$NON-NLS-1$
		assertEquals("string", binding.getDataType()); //$NON-NLS-1$

		binding.setExpression("row[\"123\"]"); //$NON-NLS-1$
		assertEquals("row[\"123\"]", binding.getExpression()); //$NON-NLS-1$

		assertEquals("column1", binding.getName()); //$NON-NLS-1$

		assertEquals("row[\"234\"]", item.getDataBinding("column2"));//$NON-NLS-1$//$NON-NLS-2$

	}

	/**
	 * Tests addColumnBinding , addHighLightRule , addHideRule , addFilterCondition
	 * , addSortCondition.
	 *
	 * @throws SemanticException
	 * @throws ScriptException
	 */
	public void testAddFunction() throws SemanticException, ScriptException {
		TableHandle tableHandle = factory.newTableItem("table", 3);//$NON-NLS-1$
		designHandle.getBody().add(tableHandle);

		IDataBinding binding = StructureScriptAPIFactory.createDataBinding();
		binding.setExpression("expression");//$NON-NLS-1$
		binding.setName("name");//$NON-NLS-1$

		IListing item = new Listing(tableHandle);
		item.addDataBinding(binding);

		IDataBinding binding2 = StructureScriptAPIFactory.createDataBinding();

		try {
			item.addDataBinding(binding2);
			fail();
		} catch (ScriptException e) {
			// We want an exception here
		}

		RowHandle rowHandle = (RowHandle) tableHandle.getFooter().getContents().get(0);
		IRow row = new Row(rowHandle);
		IHighlightRule highlight = StructureScriptAPIFactory.createHighLightRule();
		row.addHighlightRule(highlight);

		IHideRule hideRule = StructureScriptAPIFactory.createHideRule();
		item.addHideRule(hideRule);

		IFilterCondition filter = StructureScriptAPIFactory.createFilterCondition();

		try {
			item.addFilterCondition(filter);
			fail();
		} catch (ScriptException e) {
			// We want an exception here
		}

		filter.setExpr("expr");//$NON-NLS-1$
		item.addFilterCondition(filter);

		ISortCondition sort = StructureScriptAPIFactory.createSortCondition();

		try {
			item.addSortCondition(sort);
			fail();
		} catch (ScriptException e) {
			// We want an exception here
		}

		sort.setKey("key");//$NON-NLS-1$
		item.addSortCondition(sort);

	}

	/**
	 * Test <code>IHighLightRule</code>
	 *
	 * @throws SemanticException
	 * @throws ScriptException
	 */

	public void testHighLightRule() throws SemanticException, ScriptException {
		TableHandle tableHandle = factory.newTableItem("table", 3);//$NON-NLS-1$
		designHandle.getBody().add(tableHandle);

		HighlightRule rule = StructureFactory.createHighlightRule();

		SharedStyleHandle style = factory.newStyle("Style");//$NON-NLS-1$
		style.getPropertyHandle("highlightRules").addItem(rule);//$NON-NLS-1$
		designHandle.getStyles().add(style);

		tableHandle.setStyle(style);

		RowHandle rowHandle = (RowHandle) tableHandle.getHeader().get(0);

		IRow item = new Row(rowHandle);
		IHighlightRule iRule = new HighlightRuleImpl(rule);
		item.addHighlightRule(iRule);
		IHighlightRule[] rules = item.getHighlightRules();
		assertEquals(1, rules.length);

		iRule = rules[0];

		iRule.setColor("red"); //$NON-NLS-1$
		assertEquals("red", iRule.getColor()); //$NON-NLS-1$

		iRule.setDateTimeFormat("mm dd, yyyy");//$NON-NLS-1$
		assertEquals("mm dd, yyyy", iRule.getDateTimeFormat()); //$NON-NLS-1$

		iRule.setFontStyle("oblique");//$NON-NLS-1$
		assertEquals("oblique", iRule.getFontStyle()); //$NON-NLS-1$

		iRule.setFontWeight("900");//$NON-NLS-1$
		assertEquals("900", iRule.getFontWeight()); //$NON-NLS-1$

		iRule.setStringFormat("no format");//$NON-NLS-1$
		assertEquals("no format", iRule.getStringFormat()); //$NON-NLS-1$

		iRule.setTestExpression("expression");//$NON-NLS-1$
		assertEquals("expression", iRule.getTestExpression()); //$NON-NLS-1$

		iRule.setColor("#FFFFFF");//$NON-NLS-1$
		assertEquals("#FFFFFF", iRule.getColor());//$NON-NLS-1$

		iRule.setOperator("between");//$NON-NLS-1$
		assertEquals("between", iRule.getOperator());//$NON-NLS-1$

		iRule.setValue1("100");//$NON-NLS-1$
		assertEquals("100", iRule.getValue1());//$NON-NLS-1$

		iRule.setValue2("300");//$NON-NLS-1$
		assertEquals("300", iRule.getValue2());//$NON-NLS-1$

		iRule.setBackGroundColor("#FF0000");//$NON-NLS-1$
		assertEquals("#FF0000", iRule.getBackGroundColor());//$NON-NLS-1$

		item.removeHighlightRules();
		rules = item.getHighlightRules();
		assertEquals(0, rules.length);
	}

	/**
	 * Test <code>IFilterCondition</code>
	 *
	 * @throws SemanticException
	 * @throws ScriptException
	 */

	public void testFilterCondition() throws SemanticException, ScriptException {
		TableHandle tableHandle = factory.newTableItem("table", 3);//$NON-NLS-1$
		designHandle.getBody().add(tableHandle);

		FilterCondition cond = StructureFactory.createFilterCond();
		cond.setExpr("inner join");//$NON-NLS-1$
		tableHandle.getPropertyHandle("filter").addItem(cond);//$NON-NLS-1$

		IListing item = new Listing(tableHandle);
		IFilterCondition[] conds = item.getFilterConditions();
		assertEquals(1, conds.length);
		IFilterCondition iFilter = conds[0];

		assertEquals("eq", iFilter.getOperator());//$NON-NLS-1$

		iFilter.setOperator("between"); //$NON-NLS-1$
		assertEquals("between", iFilter.getOperator()); //$NON-NLS-1$

		iFilter.setValue1("1"); //$NON-NLS-1$
		assertEquals("1", iFilter.getValue1()); //$NON-NLS-1$

		iFilter.setValue2("100"); //$NON-NLS-1$
		assertEquals("100", iFilter.getValue2()); //$NON-NLS-1$

	}

	/**
	 * Test <code>ISortCondition</code>
	 *
	 * @throws SemanticException
	 * @throws ScriptException
	 */

	public void testSortCondition() throws SemanticException, ScriptException {
		TableHandle tableHandle = factory.newTableItem("table", 3);//$NON-NLS-1$
		designHandle.getBody().add(tableHandle);

		SortKey sort = StructureFactory.createSortKey();
		sort.setKey("key");//$NON-NLS-1$
		tableHandle.getPropertyHandle("sort").addItem(sort);//$NON-NLS-1$
		IListing item = new Listing(tableHandle);
		ISortCondition[] sorts = item.getSortConditions();
		assertEquals(1, sorts.length);
		ISortCondition iSort = sorts[0];
		assertEquals("key", iSort.getKey());//$NON-NLS-1$

		iSort.setDirection("desc"); //$NON-NLS-1$
		assertEquals("desc", iSort.getDirection()); //$NON-NLS-1$

		iSort.setKey("1"); //$NON-NLS-1$
		assertEquals("1", iSort.getKey()); //$NON-NLS-1$

	}

	/**
	 * Test <code>IHideRule</code>
	 *
	 * @throws SemanticException
	 * @throws ScriptException
	 */

	public void testHideRule() throws SemanticException, ScriptException {
		TableHandle tableHandle = factory.newTableItem("table", 3);//$NON-NLS-1$
		designHandle.getBody().add(tableHandle);

		HideRule hide = StructureFactory.createHideRule();
		hide.setFormat("format");//$NON-NLS-1$
		HideRuleHandle hideHandle = (HideRuleHandle) tableHandle.getPropertyHandle("visibility").addItem(hide);//$NON-NLS-1$

		HideRule hide2 = StructureFactory.createHideRule();
		hide2.setExpression("expr2");//$NON-NLS-1$
		hide2.setFormat("format");//$NON-NLS-1$
		tableHandle.getPropertyHandle("visibility").addItem(hide2);//$NON-NLS-1$

		IReportItem item = new ReportItem(tableHandle);
		IHideRule[] rules = item.getHideRules();
		assertEquals(2, rules.length);

		IHideRule iHide = new HideRuleImpl(hideHandle);
		assertEquals("format", iHide.getFormat()); //$NON-NLS-1$

		iHide.setValueExpr("valueExpr"); //$NON-NLS-1$
		assertEquals("valueExpr", iHide.getValueExpr()); //$NON-NLS-1$

		item.removeHideRule(iHide);
		rules = item.getHideRules();
		assertEquals(1, rules.length);
	}

	public void testDataSet() throws ScriptException {
		DataSetHandle dataSetHandle = factory.newOdaDataSet("DataSet", null);
		IDataSet dataSet = new DataSet(dataSetHandle);

		dataSet.setPrivateDriverProperty(KEY, VALUE);
		assertEquals(VALUE, dataSet.getPrivateDriverProperty(KEY));

		dataSet.setQueryText(QUERY);
		assertEquals(QUERY, dataSet.getQueryText());
	}

	public void testDataSource() throws ScriptException {
		DataSourceHandle dataSourceHandle = factory.newOdaDataSource(DATA_SOURCE, null);
		IDataSource dataSource = new DataSource(dataSourceHandle);

		dataSource.setPrivateDriverProperty(KEY, VALUE);
		assertEquals(VALUE, dataSource.getPrivateDriverProperty(KEY));
	}

	public void testDynamicText() throws ScriptException {
		TextDataHandle textDataHandle = factory.newTextData(TEXT_DATA);
		IDynamicText dynamicText = new DynamicText(textDataHandle);

		dynamicText.setContentType(DesignChoiceConstants.TEXT_CONTENT_TYPE_HTML);
		assertEquals(DesignChoiceConstants.TEXT_CONTENT_TYPE_HTML, dynamicText.getContentType());

		try {
			dynamicText.setContentType(TEST);
			fail("\"test\" should not be a valid content type");
		} catch (ScriptException e) {
			// We want an exception here, since "test" should not be a valid
			// content type
		}

		dynamicText.setValueExpr(EXPRESSION);
		assertEquals(EXPRESSION, dynamicText.getValueExpr());
	}

	public void testGrid() throws ScriptException {
		GridHandle gridHandle = factory.newGridItem(GRID);
		new Grid(gridHandle);

		// No methods on IGrid to test...
	}

	public void testGroup() throws ScriptException {
		GroupHandle groupHandle = factory.newTableGroup();
		IGroup group = new Group(groupHandle);

		group.setInterval(YEAR);
		assertEquals(YEAR, group.getInterval());

		group.setIntervalBase(INTERVAL_BASE);
		assertEquals(INTERVAL_BASE, group.getIntervalBase());

		group.setIntervalRange(INTERVAL_RANGE);
		assertTrue(INTERVAL_RANGE == group.getIntervalRange());

		group.setKeyExpr(EXPRESSION);
		assertEquals(EXPRESSION, group.getKeyExpr());

		group.setName(NAME);
		assertEquals(NAME, group.getName());

		group.setSortDirection(DesignChoiceConstants.SORT_DIRECTION_DESC);
		assertEquals(DesignChoiceConstants.SORT_DIRECTION_DESC, group.getSortDirection());

		try {
			group.setSortDirection(TEST);
			fail("\"test\" should not be a valid sort direction");
		} catch (ScriptException e) {
			// We want an exception here, since "test" should not be a valid
			// sort direction
		}

		group.setTocExpression(TOC);
		assertEquals(TOC, group.getTocExpression());

		group.setHideDetail(true);

		assertTrue(group.getHideDetail());
	}

	public void testImage() throws ScriptException {
		ImageHandle imageHandle = factory.newImage(IMAGE);
		IImage image = new Image(imageHandle);

		image.setHelpText(HELP_TEXT);
		assertEquals(HELP_TEXT, image.getHelpText());

		image.setHelpTextKey(HELP_TEXT_KEY);
		assertEquals(HELP_TEXT_KEY, image.getHelpTextKey());

		image.setImageName(IMAGE_NAME);
		assertEquals(IMAGE_NAME, image.getImageName());

		image.setScale(50.5);
		assertTrue(50.5 == image.getScale());

		image.setSize(DesignChoiceConstants.IMAGE_SIZE_SCALE_TO_ITEM);
		assertEquals(DesignChoiceConstants.IMAGE_SIZE_SCALE_TO_ITEM, image.getSize());

		try {
			image.setSize(TEST);
			fail("\"test\" should not be a valid sizing method");
		} catch (ScriptException e) {
			// We want an exception here, since "test" should not be a valid
			// sizing method
		}

		image.setSource(DesignChoiceConstants.IMAGE_REF_TYPE_URL);
		assertEquals(DesignChoiceConstants.IMAGE_REF_TYPE_URL, image.getSource());

		try {
			image.setSource(TEST);
			fail("\"test\" should not be a valid image source");
		} catch (ScriptException e) {
			// We want an exception here, since "test" should not be a valid
			// image source
		}

		image.setTypeExpression(EXPRESSION);
		assertEquals(EXPRESSION, image.getTypeExpression());

		image.setValueExpression(VALUE_EXPRESSION);
		assertEquals(VALUE_EXPRESSION, image.getValueExpression());

		image.setURL(IMAGE_URL);
		assertEquals(IMAGE_URL, image.getURL());

		image.setFile(IMAGE_FILE);
		assertEquals(IMAGE_FILE, image.getFile());
	}

	public void testList() throws ScriptException {
		ListHandle listHandle = factory.newList(LIST);
		new List(listHandle);

		// No methods to test on IList
	}

	public void testRow() throws ScriptException {
		RowHandle rowHandle = factory.newTableRow();
		IRow row = new Row(rowHandle);

		row.setBookmark(BOOKMARK);
		assertEquals(BOOKMARK, row.getBookmark());
	}

	public void testStyle() throws ScriptException {
		StyleHandle styleHandle = factory.newStyle(STYLE);
		IScriptStyleDesign style = new StyleDesign(styleHandle);

		style.setBackgroundAttachment(DesignChoiceConstants.BACKGROUND_ATTACHMENT_SCROLL);
		assertEquals(DesignChoiceConstants.BACKGROUND_ATTACHMENT_SCROLL, style.getBackgroundAttachment());

		style.setBackgroundColor(RED);
		assertEquals(RED, style.getBackgroundColor());

		style.setBackgroundImage(IMAGE_URL);
		assertEquals(IMAGE_URL, style.getBackgroundImage());

		style.setBackGroundPositionX(BACKGROUND_POSITION_X);
		assertEquals(BACKGROUND_POSITION_X, style.getBackGroundPositionX());

		style.setBackGroundPositionY(BACKGROUND_POSITION_Y);
		assertEquals(BACKGROUND_POSITION_Y, style.getBackGroundPositionY());

		style.setBackgroundRepeat(DesignChoiceConstants.BACKGROUND_REPEAT_REPEAT);
		assertEquals(DesignChoiceConstants.BACKGROUND_REPEAT_REPEAT, style.getBackgroundRepeat());

		style.setBorderBottomColor(BLUE);
		assertEquals(BLUE, style.getBorderBottomColor());

		style.setBorderBottomStyle(DesignChoiceConstants.LINE_STYLE_SOLID);
		assertEquals(DesignChoiceConstants.LINE_STYLE_SOLID, style.getBorderBottomStyle());

		style.setBorderBottomWidth(BORDER_BOTTOM_WIDTH);
		assertEquals(BORDER_BOTTOM_WIDTH, style.getBorderBottomWidth());

		style.setBorderLeftColor(GREEN);
		assertEquals(GREEN, style.getBorderLeftColor());

		style.setBorderLeftStyle(DesignChoiceConstants.LINE_STYLE_DOTTED);
		assertEquals(DesignChoiceConstants.LINE_STYLE_DOTTED, style.getBorderLeftStyle());

		style.setBorderLeftWidth(BORDER_LEFT_WIDTH);
		assertEquals(BORDER_LEFT_WIDTH, style.getBorderLeftWidth());

		style.setBorderRightColor(YELLOW);
		assertEquals(YELLOW, style.getBorderRightColor());

		style.setBorderRightStyle(DesignChoiceConstants.LINE_STYLE_DASHED);
		assertEquals(DesignChoiceConstants.LINE_STYLE_DASHED, style.getBorderRightStyle());

		style.setBorderRightWidth(BORDER_RIGHT_WIDTH);
		assertEquals(BORDER_RIGHT_WIDTH, style.getBorderRightWidth());

		style.setBorderTopColor(GRAY);
		assertEquals(GRAY, style.getBorderTopColor());

		style.setBorderTopStyle(DesignChoiceConstants.LINE_STYLE_DOUBLE);
		assertEquals(DesignChoiceConstants.LINE_STYLE_DOUBLE, style.getBorderTopStyle());

		style.setBorderTopWidth(BORDER_TOP_WIDTH);
		assertEquals(BORDER_TOP_WIDTH, style.getBorderTopWidth());

		style.setCanShrink(true);
		assertTrue(style.canShrink());

		style.setColor(WHITE);
		assertEquals(WHITE, style.getColor());

		style.setDateTimeFormat(YY_MM_DD);
		assertEquals(YY_MM_DD, style.getDateTimeFormat());

		style.setDateTimeFormatCategory(DesignChoiceConstants.DATETIME_FORMAT_TYPE_MEDIUM_DATE);
		assertEquals(DesignChoiceConstants.DATETIME_FORMAT_TYPE_MEDIUM_DATE, style.getDateTimeFormatCategory());

		style.setDisplay(DesignChoiceConstants.DISPLAY_INLINE);
		assertEquals(DesignChoiceConstants.DISPLAY_INLINE, style.getDisplay());

		style.setFontFamily(DesignChoiceConstants.FONT_FAMILY_SANS_SERIF);
		assertEquals(DesignChoiceConstants.FONT_FAMILY_SANS_SERIF, style.getFontFamily());

		style.setFontSize(FONT_SIZE);
		assertEquals(FONT_SIZE, style.getFontSize());

		style.setFontStyle(DesignChoiceConstants.FONT_STYLE_ITALIC);
		assertEquals(DesignChoiceConstants.FONT_STYLE_ITALIC, style.getFontStyle());

		style.setFontVariant(DesignChoiceConstants.FONT_VARIANT_SMALL_CAPS);
		assertEquals(DesignChoiceConstants.FONT_VARIANT_SMALL_CAPS, style.getFontVariant());

		style.setFontWeight(DesignChoiceConstants.FONT_WEIGHT_BOLD);
		assertEquals(DesignChoiceConstants.FONT_WEIGHT_BOLD, style.getFontWeight());

		style.setLetterSpacing(LETTER_SPACING);
		assertEquals(LETTER_SPACING, style.getLetterSpacing());

		style.setLineHeight(LINE_HEIGHT);
		assertEquals(LINE_HEIGHT, style.getLineHeight());

		style.setMarginBottom(MARGIN_BOTTOM);
		assertEquals(MARGIN_BOTTOM, style.getMarginBottom());

		style.setMarginLeft(MARGIN_LEFT);
		assertEquals(MARGIN_LEFT, style.getMarginLeft());

		style.setMarginRight(MARGIN_RIGHT);
		assertEquals(MARGIN_RIGHT, style.getMarginRight());

		style.setMarginTop(MARGIN_TOP);
		assertEquals(MARGIN_TOP, style.getMarginTop());

		style.setMasterPage(MASTER_PAGE);
		assertEquals(MASTER_PAGE, style.getMasterPage());

		style.setNumberFormat(DesignChoiceConstants.NUMBER_FORMAT_TYPE_CURRENCY);
		assertEquals(DesignChoiceConstants.NUMBER_FORMAT_TYPE_CURRENCY, style.getNumberFormat());

		style.setNumberFormatCategory(DesignChoiceConstants.NUMBER_FORMAT_TYPE_PERCENT);
		assertEquals(DesignChoiceConstants.NUMBER_FORMAT_TYPE_PERCENT, style.getNumberFormatCategory());

		style.setOrphans(ORPHANS);
		assertEquals(ORPHANS, style.getOrphans());

		style.setPaddingBottom(PADDING_BOTTOM);
		assertEquals(PADDING_BOTTOM, style.getPaddingBottom());

		style.setPaddingLeft(PADDING_LEFT);
		assertEquals(PADDING_LEFT, style.getPaddingLeft());

		style.setPaddingRight(PADDING_RIGHT);
		assertEquals(PADDING_RIGHT, style.getPaddingRight());

		style.setPaddingTop(PADDING_TOP);
		assertEquals(PADDING_TOP, style.getPaddingTop());

		style.setPageBreakAfter(DesignChoiceConstants.PAGE_BREAK_AFTER_ALWAYS);
		assertEquals(DesignChoiceConstants.PAGE_BREAK_AFTER_ALWAYS, style.getPageBreakAfter());

		style.setShowIfBlank(true);
		assertTrue(style.getShowIfBlank());

		style.setStringFormat(STRINGFORMAT);
		assertEquals(STRINGFORMAT, style.getStringFormat());

		style.setStringFormatCategory(DesignChoiceConstants.STRING_FORMAT_TYPE_PHONE_NUMBER);
		assertEquals(DesignChoiceConstants.STRING_FORMAT_TYPE_PHONE_NUMBER, style.getStringFormatCategory());

		style.setTextAlign(DesignChoiceConstants.TEXT_ALIGN_RIGHT);
		assertEquals(DesignChoiceConstants.TEXT_ALIGN_RIGHT, style.getTextAlign());

		style.setTextIndent(TEXT_INDENT);
		assertEquals(TEXT_INDENT, style.getTextIndent());

		style.setTextLineThrough(DesignChoiceConstants.TEXT_LINE_THROUGH_LINE_THROUGH);
		assertEquals(DesignChoiceConstants.TEXT_LINE_THROUGH_LINE_THROUGH, style.getTextLineThrough());

		style.setTextOverline(DesignChoiceConstants.TEXT_OVERLINE_OVERLINE);
		assertEquals(DesignChoiceConstants.TEXT_OVERLINE_OVERLINE, style.getTextOverline());

		style.setTextTransform(DesignChoiceConstants.TRANSFORM_LOWERCASE);
		assertEquals(DesignChoiceConstants.TRANSFORM_LOWERCASE, style.getTextTransform());

		style.setTextUnderline(DesignChoiceConstants.TEXT_UNDERLINE_UNDERLINE);
		assertEquals(DesignChoiceConstants.TEXT_UNDERLINE_UNDERLINE, style.getTextUnderline());

		style.setVerticalAlign(DesignChoiceConstants.VERTICAL_ALIGN_TOP);
		assertEquals(DesignChoiceConstants.VERTICAL_ALIGN_TOP, style.getVerticalAlign());

		style.setWhiteSpace(DesignChoiceConstants.WHITE_SPACE_NOWRAP);
		assertEquals(DesignChoiceConstants.WHITE_SPACE_NOWRAP, style.getWhiteSpace());

		style.setWidows(WINDOWS);
		assertEquals(WINDOWS, style.getWidows());

		style.setWordSpacing(WORD_SPACING);
		assertEquals(WORD_SPACING, style.getWordSpacing());
	}

	public void testTable() throws ScriptException {
		TableHandle tableHandle = factory.newTableItem(TABLE);
		ITable table = new Table(tableHandle);

		table.setCaption(CAPTION);
		assertEquals(CAPTION, table.getCaption());

		table.setCaptionKey(CAPTION_KEY);
		assertEquals(CAPTION_KEY, table.getCaptionKey());

		table.setRepeatHeader(true);
		assertTrue(table.repeatHeader());
	}

	public void testTextItem() throws ScriptException {
		TextItemHandle textItemHandle = factory.newTextItem(TEXT_ITEM);
		ITextItem text = new TextItem(textItemHandle);

		text.setContent(TEXT_CONTENT);
		assertEquals(TEXT_CONTENT, text.getContent());

		text.setContentKey(CONTENT_KEY);
		assertEquals(CONTENT_KEY, text.getContentKey());

		text.setContentType(DesignChoiceConstants.TEXT_CONTENT_TYPE_HTML);
		assertEquals(DesignChoiceConstants.TEXT_CONTENT_TYPE_HTML, text.getContentType());
	}

}
