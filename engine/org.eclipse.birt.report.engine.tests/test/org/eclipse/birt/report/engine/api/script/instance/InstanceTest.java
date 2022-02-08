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
package org.eclipse.birt.report.engine.api.script.instance;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.api.script.IDataSetInstanceHandle;
import org.eclipse.birt.data.engine.api.script.IDataSourceInstanceHandle;
import org.eclipse.birt.report.engine.api.script.IColumnMetaData;
import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.impl.CellContent;
import org.eclipse.birt.report.engine.content.impl.DataContent;
import org.eclipse.birt.report.engine.content.impl.ForeignContent;
import org.eclipse.birt.report.engine.content.impl.ImageContent;
import org.eclipse.birt.report.engine.content.impl.LabelContent;
import org.eclipse.birt.report.engine.content.impl.ListContent;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.engine.content.impl.RowContent;
import org.eclipse.birt.report.engine.content.impl.TableContent;
import org.eclipse.birt.report.engine.ir.DataItemDesign;
import org.eclipse.birt.report.engine.ir.ImageItemDesign;
import org.eclipse.birt.report.engine.script.internal.instance.CellInstance;
import org.eclipse.birt.report.engine.script.internal.instance.DataItemInstance;
import org.eclipse.birt.report.engine.script.internal.instance.DataSetInstance;
import org.eclipse.birt.report.engine.script.internal.instance.DataSourceInstance;
import org.eclipse.birt.report.engine.script.internal.instance.DynamicTextInstance;
import org.eclipse.birt.report.engine.script.internal.instance.GridInstance;
import org.eclipse.birt.report.engine.script.internal.instance.ImageInstance;
import org.eclipse.birt.report.engine.script.internal.instance.LabelInstance;
import org.eclipse.birt.report.engine.script.internal.instance.ListInstance;
import org.eclipse.birt.report.engine.script.internal.instance.RowInstance;
import org.eclipse.birt.report.engine.script.internal.instance.RunningState;
import org.eclipse.birt.report.engine.script.internal.instance.StyleInstance;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.mozilla.javascript.Scriptable;

import com.ibm.icu.util.ULocale;

public class InstanceTest extends TestCase {
	private static final String TYPE_NAME_2 = "type2";

	private static final String TYPE_NAME_1 = "type1";

	private static final String NATIVE2 = "native2";

	private static final String NATIVE1 = "native1";

	private static final String NAME2 = "name2";

	private static final String NAME1 = "name1";

	private static final String LABEL2 = "label2";

	private static final String LABEL1 = "label1";

	private static final String ALIAS2 = "alias2";

	private static final String ALIAS1 = "alias1";

	private static final String ALT_TEXT_KEY = "altTextKey";

	private static final String ALT_TEXT = "altText";

	private static final String WORD_SPACING = "30px";

	private static final String TEXT_INDENT = WORD_SPACING;

	private static final String SHOW_IF_BLANK = "true";

	private static final String PADDING_TOP = "23px";

	private static final String PADDING_RIGHT = "22px";

	private static final String PADDING_LEFT = "21px";

	private static final String PADDING_BOTTOM = "20px";

	private static final String MASTER_PAGE = "masterPage";

	private static final String MARGIN_TOP = "13px";

	private static final String MARGIN_RIGHT = "12px";

	private static final String MARGIN_LEFT = "11px";

	private static final String MARGIN_BOTTOM = "10px";

	private static final String LETTER_SPACING = MARGIN_BOTTOM;

	private static final String YY_MM_DD = "YY-MM-DD";

	private static final String WHITE = "white";

	private static final String CAN_SHRINK = SHOW_IF_BLANK;

	private static final String BORDER_TOP_WIDTH = "16cm";

	private static final String GRAY = "gray";

	private static final String BORDER_RIGHT_WIDTH = "15cm";

	private static final String YELLOW = "yellow";

	private static final String BORDER_LEFT_WIDTH = "14cm";

	private static final String GREEN = "green";

	private static final String BORDER_BOTTOM_WIDTH = "13cm";

	private static final String BLUE = "blue";

	private static final String BACKGROUND_POS_Y = "11cm";

	private static final String BACKGROUND_POS_X = "10cm";

	private static final String RED = "red";

	private static final String BOOKMARK = "bookmark";

	private static final String LABEL_TEXT = "labelText";

	private static final String URI = "uri";

	private static final String MIME_TYPE = "img/gif";

	private static final String IMAGE_NAME = "imageName";

	private static final String DYNAMIC_TEXT = "text";

	private static final String QUERY_TEXT = "queryText";

	private static final String EXT_PROP_VALUE1 = "propvalue1";

	private static final String EXT_PROP1 = "prop1";

	private static final String USER_PROP_VALUE = "userPropValue";

	private static final String USER_PROP = "userProp";

	private static final String HORIZONTAL_POSITION = MARGIN_LEFT;

	private static final String HEIGHT = LETTER_SPACING;

	private static final int COL_SPAN = 2;

	private static final String EXPRESSION_VALUE = "expressionValue";

	private static final String EXPRESSION = "expression";

	private static final String EXTENSION_ID = "extensionId";

	private static final String FAKE_NAME = "fakeName";

	private static final byte[] DATA = new byte[] { 123 };

	private static final int TYPE_2 = 2;

	private static final int TYPE_1 = 1;

	private static final String IMAGE_FILE = "imageFile";

	private static final String IMAGE_URL = "imageURL";

	private ReportContent reportContent;

	private ElementFactory factory;

	public void setUp() {
		reportContent = new ReportContent();
		SessionHandle sessionHandle = new DesignEngine(new DesignConfig()).newSessionHandle(ULocale.ENGLISH);
		ReportDesignHandle designHandle = sessionHandle.createDesign();
		factory = new ElementFactory(designHandle.getModule());
	}

	private void doTestReportElementInstance(IReportElementInstance element) throws ScriptException {
		element.setHeight(HEIGHT);
		assertEquals(HEIGHT, element.getHeight());

		element.setHorizontalPosition(HORIZONTAL_POSITION);
		assertEquals(HORIZONTAL_POSITION, element.getHorizontalPosition());

		element.setUserPropertyValue(USER_PROP, USER_PROP_VALUE);
		// TODO: Fix
		// assertEquals(USER_PROP_VALUE,
		// element.getUserPropertyValue(USER_PROP));
	}

	public void testCellInstance() throws ScriptException {
		CellContent cellContent = (CellContent) reportContent.createCellContent();
		cellContent.setColumn(1);
		Map expressionMap = new HashMap();
		expressionMap.put(EXPRESSION, EXPRESSION_VALUE);
		// IRowData rowData = new FakeRowData(expressionMap);
		ICellInstance cell = new CellInstance(cellContent, null, null, false);

		cell.setColSpan(COL_SPAN);
		assertEquals(COL_SPAN, cell.getColSpan());

		// assertEquals(EXPRESSION_VALUE, cell.getRowData());
	}

	public void testDataItemInstance() throws ScriptException {
		DataContent dataItemContent = (DataContent) reportContent.createDataContent();
		DataItemDesign dataItemDesign = new DataItemDesign();
		DataItemHandle dataHandle = factory.newDataItem("DataItem");
		dataItemDesign.setHandle(dataHandle);
		dataItemContent.setGenerateBy(dataItemDesign);
		IDataItemInstance dataItem = new DataItemInstance(dataItemContent, null, null);

		doTestReportElementInstance(dataItem);
	}

	public void testDataSetInstance() throws ScriptException {
		IDataSetInstance dataSetInstance = new DataSetInstance(new FakeDataSetHandle());

		dataSetInstance.setExtensionProperty(EXT_PROP1, EXT_PROP_VALUE1);
		assertEquals(EXT_PROP_VALUE1, dataSetInstance.getExtensionProperty(EXT_PROP1));

		dataSetInstance.setQueryText(QUERY_TEXT);
		assertEquals(QUERY_TEXT, dataSetInstance.getQueryText());

		assertEquals(FAKE_NAME, dataSetInstance.getName());

		assertEquals(EXTENSION_ID, dataSetInstance.getExtensionID());

		IColumnMetaData meta = dataSetInstance.getColumnMetaData();

		assertEquals(ALIAS1, meta.getColumnAlias(1));
		assertEquals(ALIAS2, meta.getColumnAlias(2));

		assertEquals(LABEL1, meta.getColumnLabel(1));
		assertEquals(LABEL2, meta.getColumnLabel(2));

		assertEquals(NAME1, meta.getColumnName(1));
		assertEquals(NAME2, meta.getColumnName(2));

		assertEquals(NATIVE1, meta.getColumnNativeTypeName(1));
		assertEquals(NATIVE2, meta.getColumnNativeTypeName(2));

		assertEquals(TYPE_NAME_1, meta.getColumnTypeName(1));
		assertEquals(TYPE_NAME_2, meta.getColumnTypeName(2));

		assertTrue(meta.isComputedColumn(1));
		assertFalse(meta.isComputedColumn(2));

		assertEquals(2, meta.getColumnCount());

		assertEquals(TYPE_1, meta.getColumnType(1));
		assertEquals(TYPE_2, meta.getColumnType(2));
	}

	public void testDataSourceInstance() throws ScriptException {
		IDataSourceInstance dataSourceInstance = new DataSourceInstance(new FakeDataSourceHandle());
		dataSourceInstance.setExtensionProperty(EXT_PROP1, EXT_PROP_VALUE1);
		assertEquals(EXT_PROP_VALUE1, dataSourceInstance.getExtensionProperty(EXT_PROP1));
		assertEquals(FAKE_NAME, dataSourceInstance.getName());

		assertEquals(EXTENSION_ID, dataSourceInstance.getExtensionID());
	}

	public void testDynamicTextInstance() throws ScriptException {
		ForeignContent foreignContent = (ForeignContent) reportContent.createForeignContent();
		foreignContent.setRawType(IForeignContent.TEXT_TYPE);
		IDynamicTextInstance textInstance = new DynamicTextInstance(foreignContent, null, null);

		textInstance.setText(DYNAMIC_TEXT);
		assertEquals(DYNAMIC_TEXT, textInstance.getText());
	}

	public void testGridInstance() throws ScriptException {
		TableContent content = (TableContent) reportContent.createTableContent();
		new GridInstance(content, null, null);

		// No methods to test....
	}

	public void testImageInstance() throws ScriptException {
		ImageContent imageContent = (ImageContent) reportContent.createImageContent();
		IImageInstance imageInstance = new ImageInstance(imageContent, null, null);

		imageInstance.setAltText(ALT_TEXT);
		assertEquals(ALT_TEXT, imageInstance.getAltText());

		imageInstance.setAltTextKey(ALT_TEXT_KEY);
		assertEquals(ALT_TEXT_KEY, imageInstance.getAltTextKey());

		imageInstance.setData(DATA);
		assertEquals(DATA, imageInstance.getData());

		imageInstance.setImageName(IMAGE_NAME);
		assertEquals(IMAGE_NAME, imageInstance.getImageName());
		assertEquals(ImageItemDesign.IMAGE_NAME, imageInstance.getImageSource());

		imageInstance.setMimeType(MIME_TYPE);
		assertEquals(MIME_TYPE, imageInstance.getMimeType());

		imageInstance.setURL(IMAGE_URL);
		assertEquals(IImageContent.IMAGE_URL, imageInstance.getImageSource());
		assertEquals(IMAGE_URL, imageInstance.getURL());

		imageInstance.setFile(IMAGE_FILE);
		assertEquals(IImageContent.IMAGE_FILE, imageInstance.getImageSource());
		assertEquals(IMAGE_FILE, imageInstance.getFile());
	}

	public void testLabelInstance() throws ScriptException {
		LabelContent labelContent = (LabelContent) reportContent.createLabelContent();
		ILabelInstance labelInstance = new LabelInstance(labelContent, null, null);

		labelInstance.setText(LABEL_TEXT);
		assertEquals(LABEL_TEXT, labelInstance.getText());
	}

	public void testListInstance() throws ScriptException {
		ListContent listContent = (ListContent) reportContent.createListContent();
		new ListInstance(listContent, null, null);

		// no methods to test...
	}

	public void testRowInstance() throws ScriptException {
		RowContent rowContent = (RowContent) reportContent.createRowContent();
		Map expressionMap = new HashMap();
		expressionMap.put(EXPRESSION, EXPRESSION_VALUE);
		IRowInstance rowInstance = new RowInstance(rowContent, null, null);

		rowInstance.setBookmark(BOOKMARK);
		assertEquals(BOOKMARK, rowInstance.getBookmarkValue());

		rowInstance.setHeight(HEIGHT);
		assertEquals(HEIGHT, rowInstance.getHeight());

		// assertEquals(EXPRESSION_VALUE,
		// rowInstance.getRowData().getExpressionValue(EXPRESSION));
	}

	public void testScriptStyle() throws ScriptException {
		IStyle style = reportContent.createStyle();
		IScriptStyle styleInstance = new StyleInstance(style, null);

		styleInstance.setBackgroundAttachment(DesignChoiceConstants.BACKGROUND_ATTACHMENT_SCROLL);
		assertEquals(DesignChoiceConstants.BACKGROUND_ATTACHMENT_SCROLL, styleInstance.getBackgroundAttachment());

		styleInstance.setBackgroundColor(RED);
		assertEquals(RED, styleInstance.getBackgroundColor());

		styleInstance.setBackgroundImage(URI);
		assertEquals(URI, styleInstance.getBackgroundImage());

		styleInstance.setBackgroundPositionX(BACKGROUND_POS_X);
		assertEquals(BACKGROUND_POS_X, styleInstance.getBackgroundPositionX());

		styleInstance.setBackgroundPositionY(BACKGROUND_POS_Y);
		assertEquals(BACKGROUND_POS_Y, styleInstance.getBackgroundPositionY());

		styleInstance.setBackgroundRepeat(DesignChoiceConstants.BACKGROUND_REPEAT_REPEAT_X);
		assertEquals(DesignChoiceConstants.BACKGROUND_REPEAT_REPEAT_X, styleInstance.getBackgroundRepeat());

		styleInstance.setBorderBottomColor(BLUE);
		assertEquals(BLUE, styleInstance.getBorderBottomColor());

		styleInstance.setBorderBottomStyle(DesignChoiceConstants.LINE_STYLE_SOLID);
		assertEquals(DesignChoiceConstants.LINE_STYLE_SOLID, styleInstance.getBorderBottomStyle());

		styleInstance.setBorderBottomWidth(BORDER_BOTTOM_WIDTH);
		assertEquals(BORDER_BOTTOM_WIDTH, styleInstance.getBorderBottomWidth());

		styleInstance.setBorderLeftColor(GREEN);
		assertEquals(GREEN, styleInstance.getBorderLeftColor());

		styleInstance.setBorderLeftStyle(DesignChoiceConstants.LINE_STYLE_DOTTED);
		assertEquals(DesignChoiceConstants.LINE_STYLE_DOTTED, styleInstance.getBorderLeftStyle());

		styleInstance.setBorderLeftWidth(BORDER_LEFT_WIDTH);
		assertEquals(BORDER_LEFT_WIDTH, styleInstance.getBorderLeftWidth());

		styleInstance.setBorderRightColor(YELLOW);
		assertEquals(YELLOW, styleInstance.getBorderRightColor());

		styleInstance.setBorderRightStyle(DesignChoiceConstants.LINE_STYLE_DASHED);
		assertEquals(DesignChoiceConstants.LINE_STYLE_DASHED, styleInstance.getBorderRightStyle());

		styleInstance.setBorderRightWidth(BORDER_RIGHT_WIDTH);
		assertEquals(BORDER_RIGHT_WIDTH, styleInstance.getBorderRightWidth());

		styleInstance.setBorderTopColor(GRAY);
		assertEquals(GRAY, styleInstance.getBorderTopColor());

		styleInstance.setBorderTopStyle(DesignChoiceConstants.LINE_STYLE_DOUBLE);
		assertEquals(DesignChoiceConstants.LINE_STYLE_DOUBLE, styleInstance.getBorderTopStyle());

		styleInstance.setBorderTopWidth(BORDER_TOP_WIDTH);
		assertEquals(BORDER_TOP_WIDTH, styleInstance.getBorderTopWidth());

		styleInstance.setCanShrink(CAN_SHRINK);
		assertEquals(CAN_SHRINK, styleInstance.getCanShrink());

		styleInstance.setColor(WHITE);
		assertEquals(WHITE, styleInstance.getColor());

		styleInstance.setDateFormat(YY_MM_DD);
		assertEquals(YY_MM_DD, styleInstance.getDateFormat());

		styleInstance.setDisplay(DesignChoiceConstants.DISPLAY_INLINE);
		assertEquals(DesignChoiceConstants.DISPLAY_INLINE, styleInstance.getDisplay());

		styleInstance.setFontFamily(DesignChoiceConstants.FONT_FAMILY_SANS_SERIF);
		assertEquals(DesignChoiceConstants.FONT_FAMILY_SANS_SERIF, styleInstance.getFontFamily());

		styleInstance.setFontSize(DesignChoiceConstants.FONT_SIZE_LARGE);
		assertEquals(DesignChoiceConstants.FONT_SIZE_LARGE, styleInstance.getFontSize());

		styleInstance.setFontStyle(DesignChoiceConstants.FONT_STYLE_ITALIC);
		assertEquals(DesignChoiceConstants.FONT_STYLE_ITALIC, styleInstance.getFontStyle());

		styleInstance.setFontVariant(DesignChoiceConstants.FONT_VARIANT_SMALL_CAPS);
		assertEquals(DesignChoiceConstants.FONT_VARIANT_SMALL_CAPS, styleInstance.getFontVariant());

		styleInstance.setFontWeight(DesignChoiceConstants.FONT_WEIGHT_BOLD);
		assertEquals(DesignChoiceConstants.FONT_WEIGHT_BOLD, styleInstance.getFontWeight());

		styleInstance.setLetterSpacing(LETTER_SPACING);
		assertEquals(LETTER_SPACING, styleInstance.getLetterSpacing());

		styleInstance.setLineHeight(HEIGHT);
		assertEquals(HEIGHT, styleInstance.getLineHeight());

		styleInstance.setMarginBottom(MARGIN_BOTTOM);
		assertEquals(MARGIN_BOTTOM, styleInstance.getMarginBottom());

		styleInstance.setMarginLeft(MARGIN_LEFT);
		assertEquals(MARGIN_LEFT, styleInstance.getMarginLeft());

		styleInstance.setMarginRight(MARGIN_RIGHT);
		assertEquals(MARGIN_RIGHT, styleInstance.getMarginRight());

		styleInstance.setMarginTop(MARGIN_TOP);
		assertEquals(MARGIN_TOP, styleInstance.getMarginTop());

		styleInstance.setMasterPage(MASTER_PAGE);
		assertEquals(MASTER_PAGE, styleInstance.getMasterPage());

		styleInstance.setNumberFormat(DesignChoiceConstants.NUMBER_FORMAT_TYPE_CURRENCY);
		assertEquals(DesignChoiceConstants.NUMBER_FORMAT_TYPE_CURRENCY, styleInstance.getNumberFormat());

		styleInstance.setPaddingBottom(PADDING_BOTTOM);
		assertEquals(PADDING_BOTTOM, styleInstance.getPaddingBottom());

		styleInstance.setPaddingLeft(PADDING_LEFT);
		assertEquals(PADDING_LEFT, styleInstance.getPaddingLeft());

		styleInstance.setPaddingRight(PADDING_RIGHT);
		assertEquals(PADDING_RIGHT, styleInstance.getPaddingRight());

		styleInstance.setPaddingTop(PADDING_TOP);
		assertEquals(PADDING_TOP, styleInstance.getPaddingTop());

		styleInstance.setPageBreakAfter(DesignChoiceConstants.PAGE_BREAK_AFTER_ALWAYS);
		assertEquals(DesignChoiceConstants.PAGE_BREAK_AFTER_ALWAYS, styleInstance.getPageBreakAfter());

		styleInstance.setPageBreakBefore(DesignChoiceConstants.PAGE_BREAK_BEFORE_AVOID);
		assertEquals(DesignChoiceConstants.PAGE_BREAK_BEFORE_AVOID, styleInstance.getPageBreakBefore());

		styleInstance.setShowIfBlank(SHOW_IF_BLANK);
		assertEquals(SHOW_IF_BLANK, styleInstance.getShowIfBlank());

		styleInstance.setStringFormat(DesignChoiceConstants.STRING_FORMAT_TYPE_ZIP_CODE);
		assertEquals(DesignChoiceConstants.STRING_FORMAT_TYPE_ZIP_CODE, styleInstance.getStringFormat());

		styleInstance.setTextAlign(DesignChoiceConstants.TEXT_ALIGN_RIGHT);
		assertEquals(DesignChoiceConstants.TEXT_ALIGN_RIGHT, styleInstance.getTextAlign());

		styleInstance.setTextIndent(TEXT_INDENT);
		assertEquals(TEXT_INDENT, styleInstance.getTextIndent());

		styleInstance.setTextLineThrough(DesignChoiceConstants.TEXT_LINE_THROUGH_LINE_THROUGH);
		assertEquals(DesignChoiceConstants.TEXT_LINE_THROUGH_LINE_THROUGH, styleInstance.getTextLineThrough());

		styleInstance.setTextOverline(DesignChoiceConstants.TEXT_OVERLINE_OVERLINE);
		assertEquals(DesignChoiceConstants.TEXT_OVERLINE_OVERLINE, styleInstance.getTextOverline());

		styleInstance.setTextTransform(DesignChoiceConstants.TRANSFORM_UPPERCASE);
		assertEquals(DesignChoiceConstants.TRANSFORM_UPPERCASE, styleInstance.getTextTransform());

		styleInstance.setTextUnderline(DesignChoiceConstants.TEXT_UNDERLINE_UNDERLINE);
		assertEquals(DesignChoiceConstants.TEXT_UNDERLINE_UNDERLINE, styleInstance.getTextUnderline());

		styleInstance.setVerticalAlign(DesignChoiceConstants.VERTICAL_ALIGN_TOP);
		assertEquals(DesignChoiceConstants.VERTICAL_ALIGN_TOP, styleInstance.getVerticalAlign());

		styleInstance.setVisibleFormat(DesignChoiceConstants.FORMAT_TYPE_ALL);
		assertEquals(DesignChoiceConstants.FORMAT_TYPE_ALL, styleInstance.getVisibleFormat());

		styleInstance.setWhiteSpace(DesignChoiceConstants.WHITE_SPACE_NOWRAP);
		assertEquals(DesignChoiceConstants.WHITE_SPACE_NOWRAP, styleInstance.getWhiteSpace());

		styleInstance.setWordSpacing(WORD_SPACING);
		assertEquals(WORD_SPACING, styleInstance.getWordSpacing());
	}

	public void testPageBreak() throws Throwable {
		IStyle style = reportContent.createStyle();
		StyleInstance styleInstance = new StyleInstance(style, RunningState.CREATE);
		styleInstance.setPageBreakAfter("always");
		styleInstance.setPageBreakBefore("always");
		styleInstance.setPageBreakInside("always");

		styleInstance = new StyleInstance(style, RunningState.RENDER);
		testMethod(styleInstance, "setPageBreakAfter");
		testMethod(styleInstance, "setPageBreakBefore");
		testMethod(styleInstance, "setPageBreakInside");

		styleInstance = new StyleInstance(style, RunningState.PAGEBREAK);
		testMethod(styleInstance, "setPageBreakAfter");
		testMethod(styleInstance, "setPageBreakBefore");
		testMethod(styleInstance, "setPageBreakInside");
	}

	private void testMethod(StyleInstance styleInstance, String methodName) throws Throwable {
		try {
			Method method = StyleInstance.class.getMethod(methodName, String.class);
			method.invoke(styleInstance, "always");
			fail();
		} catch (InvocationTargetException expected) {
			Throwable targetException = expected.getTargetException();
			if (targetException instanceof UnsupportedOperationException) {
				assertTrue(true);
				return;
			}
			throw targetException;
		}
	}

	private abstract class FakeBaseData {
		private Map extProps = new HashMap();

		public String getName() {
			return FAKE_NAME;
		}

		public String getExtensionID() {
			return EXTENSION_ID;
		}

		public String getExtensionProperty(String name) {
			return (String) extProps.get(name);
		}

		public void setExtensionProperty(String name, String value) {
			extProps.put(name, value);
		}

		public Map getAllExtensionProperties() {
			return extProps;
		}

		public Scriptable getScriptScope() {
			return null;
		}
	}

	private class FakeDataSourceHandle extends FakeBaseData implements IDataSourceInstanceHandle {
	}

	private class FakeDataSetHandle extends FakeBaseData implements IDataSetInstanceHandle {

		private String queryText;
		private Map inputParamValues = new HashMap();

		public IDataSourceInstanceHandle getDataSource() {
			return null;
		}

		public IResultMetaData getResultMetaData() throws BirtException {
			return new FakeResultMetadata();
		}

		public String getQueryText() {
			return queryText;
		}

		public void setQueryText(String queryText) throws BirtException {
			this.queryText = queryText;
		}

		public Object getInputParameterValue(String paramName) throws BirtException {
			return null;
		}

		public void setInputParameterValue(String paramName, Object paramValue) throws BirtException {
		}

		public Map getInputParameters() {
			return null;
		}

		public Object getOutputParameterValue(String paramName) throws BirtException {
			return null;
		}

		public void setOutputParameterValue(String paramName, Object paramValue) throws BirtException {
		}

		public Map getOutputParameters() {
			return null;
		}
	}

	// This class never been used locally. Comment this class to fix the warning.
//	private class FakeRowData implements IRowData {
//
//		private Map expressionMap;
//
//		public FakeRowData(Map expressionMap) {
//			this.expressionMap = expressionMap;
//		}
//
//		public Object getExpressionValue(String expression)
//				throws ScriptException {
//			return expressionMap.get(expression);
//		}
//
//		public Object getExpressionValue(int i) throws ScriptException {
//			Set keySet = expressionMap.keySet();
//			Object[] expressions = keySet.toArray();
//			Object key = expressions[i - 2];
//			return expressionMap.get(key);
//		}
//
//		public int getExpressionCount() {
//			return expressionMap.size();
//		}
//
//		public Object getColumnValue( String name )
//		{
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		public Object getColumnValue( int index )
//		{
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		public String getColumnName( int index )
//		{
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		public int getColumnCount( )
//		{
//			// TODO Auto-generated method stub
//			return 0;
//		}
//	}

	private class FakeResultMetadata implements IResultMetaData {

		private List columns;

		public FakeResultMetadata() {
			columns = new ArrayList();
			columns.add(new Column(NAME1, ALIAS1, TYPE_1, TYPE_NAME_1, NATIVE1, LABEL1, true));
			columns.add(new Column(NAME2, ALIAS2, TYPE_2, TYPE_NAME_2, NATIVE2, LABEL2, false));
		}

		private class Column {

			String name;

			String alias;

			int type;

			String typeName;

			String nativeTypeName;

			String label;

			boolean computed;

			public Column(String name, String alias, int type, String typeName, String nativeTypeName, String label,
					boolean computed) {
				this.name = name;
				this.alias = alias;
				this.type = type;
				this.typeName = typeName;
				this.nativeTypeName = nativeTypeName;
				this.label = label;
				this.computed = computed;
			}

		}

		public int getColumnCount() {
			return columns.size();
		}

		public String getColumnName(int index) throws BirtException {
			Column c = (Column) columns.get(index - 1);
			return c.name;
		}

		public String getColumnAlias(int index) throws BirtException {
			Column c = (Column) columns.get(index - 1);
			return c.alias;
		}

		public int getColumnType(int index) throws BirtException {
			Column c = (Column) columns.get(index - 1);
			return c.type;
		}

		public String getColumnTypeName(int index) throws BirtException {
			Column c = (Column) columns.get(index - 1);
			return c.typeName;
		}

		public String getColumnNativeTypeName(int index) throws BirtException {
			Column c = (Column) columns.get(index - 1);
			return c.nativeTypeName;
		}

		public String getColumnLabel(int index) throws BirtException {
			Column c = (Column) columns.get(index - 1);
			return c.label;
		}

		public boolean isComputedColumn(int index) throws BirtException {
			Column c = (Column) columns.get(index - 1);
			return c.computed;
		}
	}

}
