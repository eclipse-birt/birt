
package org.eclipse.birt.report.tests.model.api;

import java.io.InputStream;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.model.api.css.StyleSheetParserException;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * TestCases for ExternalCssStyleSheet.
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * 
 * <tr>
 * <td>{@link #testParserForSupportedProperties()}</td>
 * <td>Parse supported style properties in CSS file</td>
 * <td>Return style properties values.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testParserForNoSupportedProperties()}</td>
 * <td>Parse unsupported properties.</td>
 * <td>Handle the errors.</td>
 * </tr>
 * </table>
 * 
 */
public class ExternalCssStyleSheet1Test extends BaseTestCase {

	// private String fileName = null;
	private static String fileName = "ExternalCssStyleSheet1Test.css";

	private CssStyleSheetHandle cssStyleSheetHandle = null;
	private SharedStyleHandle style1 = null;
	private SharedStyleHandle style2 = null;
	private SharedStyleHandle style3 = null;

	public ExternalCssStyleSheet1Test(String name) {
		super(name);
	}

	public static Test suite() {
		return new TestSuite(ExternalCssStyleSheet1Test.class);
	}

	public void setUp() throws Exception {
		super.setUp();
		removeResource();

		copyInputToFile(INPUT_FOLDER + "/" + fileName);

		// Platform.initialize( null );
		SessionHandle designSession = DesignEngine.newSession(ULocale.ENGLISH);
		designHandle = designSession.createDesign();
		// fileName = "ExternalCssStyleSheet1Test.css";
		cssStyleSheetHandle = loadStyleSheet(fileName);
		style1 = cssStyleSheetHandle.findStyle("table1");
		style2 = cssStyleSheetHandle.findStyle("table2");
		style3 = cssStyleSheetHandle.findStyle("table3");
	}

	private CssStyleSheetHandle loadStyleSheet(String fileName) throws Exception {
		fileName = INPUT_FOLDER + "/" + fileName;
		InputStream is = ExternalCssStyleSheet1Test.class.getResourceAsStream(fileName);
		return designHandle.openCssStyleSheet(is);
	}

	/**
	 * Parse supported style properties in CSS file
	 * 
	 * @throws Exception
	 */
	public void testParserForSupportedProperties() throws Exception {
		assertNotNull(style1);
		assertNotNull(style2);
		assertNotNull(style3);

		/* font */

		assertEquals("cursive", style1.getProperty(IStyleModel.FONT_FAMILY_PROP));
		assertEquals("\"arial\"", style2.getProperty(IStyleModel.FONT_FAMILY_PROP).toString());
		assertEquals("small", style1.getStringProperty(IStyleModel.FONT_SIZE_PROP));
		assertEquals("12pt", style2.getStringProperty(IStyleModel.FONT_SIZE_PROP));
		assertEquals("italic", style1.getStringProperty(IStyleModel.FONT_STYLE_PROP));
		assertEquals("bolder", style1.getStringProperty(IStyleModel.FONT_WEIGHT_PROP));
		assertEquals("small-caps", style1.getStringProperty(IStyleModel.FONT_VARIANT_PROP));
		assertEquals("red", style1.getStringProperty(IStyleModel.COLOR_PROP));
		assertEquals("#FF0000", style2.getStringProperty(IStyleModel.COLOR_PROP));
		assertEquals("underline", style1.getStringProperty(IStyleModel.TEXT_UNDERLINE_PROP));
		assertEquals("overline", style1.getStringProperty(IStyleModel.TEXT_OVERLINE_PROP));
		assertEquals("line-through", style1.getStringProperty(IStyleModel.TEXT_LINE_THROUGH_PROP));

		/* background */

		assertEquals("fixed", style1.getStringProperty(IStyleModel.BACKGROUND_ATTACHMENT_PROP));
		assertEquals("green", style1.getStringProperty(IStyleModel.BACKGROUND_COLOR_PROP));
		assertEquals("#00FF00", style2.getStringProperty(IStyleModel.BACKGROUND_COLOR_PROP));
		assertEquals("logo.gif", style1.getStringProperty(IStyleModel.BACKGROUND_IMAGE_PROP));
		assertEquals("center", style1.getStringProperty(IStyleModel.BACKGROUND_POSITION_X_PROP));
		assertEquals("center", style1.getStringProperty(IStyleModel.BACKGROUND_POSITION_Y_PROP));
		assertEquals("25%", style2.getStringProperty(IStyleModel.BACKGROUND_POSITION_X_PROP));
		assertEquals("25%", style2.getStringProperty(IStyleModel.BACKGROUND_POSITION_Y_PROP));
		assertEquals("no-repeat", style1.getStringProperty(IStyleModel.BACKGROUND_REPEAT_PROP));

		/* text */

		assertEquals("normal", style1.getStringProperty(IStyleModel.LETTER_SPACING_PROP));
		assertEquals("12em", style2.getStringProperty(IStyleModel.LETTER_SPACING_PROP));
		assertEquals("normal", style1.getStringProperty(IStyleModel.LINE_HEIGHT_PROP));
		assertEquals("12px", style2.getStringProperty(IStyleModel.LINE_HEIGHT_PROP));
		assertEquals("justify", style1.getStringProperty(IStyleModel.TEXT_ALIGN_PROP));
		assertEquals("12mm", style1.getStringProperty(IStyleModel.TEXT_INDENT_PROP));
		assertEquals("uppercase", style1.getStringProperty(IStyleModel.TEXT_TRANSFORM_PROP));
		assertEquals("middle", style1.getStringProperty(IStyleModel.VERTICAL_ALIGN_PROP));
		assertEquals("align", style2.getStringProperty(IStyleModel.VERTICAL_ALIGN_PROP));
		assertEquals("nowrap", style1.getStringProperty(IStyleModel.WHITE_SPACE_PROP));
		assertEquals("inherit", style1.getStringProperty(IStyleModel.WIDOWS_PROP));
		assertEquals("3", style2.getStringProperty(IStyleModel.WIDOWS_PROP));
		assertEquals("inherit", style1.getStringProperty(IStyleModel.ORPHANS_PROP));
		assertEquals("1", style2.getStringProperty(IStyleModel.ORPHANS_PROP));
		assertEquals("normal", style1.getStringProperty(IStyleModel.WORD_SPACING_PROP));
		assertEquals("12in", style2.getStringProperty(IStyleModel.WORD_SPACING_PROP));

		/* section */
		/**
		 * Because of currently model don't support edit the style from css file
		 */
		// assertEquals( "block", style1.getStringProperty(
		// IStyleModel.DISPLAY_PROP ) );
		// style1.setMasterPage( "MP1" );
		// assertEquals( "MP1", style1.getStringProperty(
		// IStyleModel.MASTER_PAGE_PROP ) );
		// assertEquals( "always", style1.getStringProperty(
		// IStyleModel.PAGE_BREAK_BEFORE_PROP ) );
		// assertEquals( "always", style1.getStringProperty(
		// IStyleModel.PAGE_BREAK_AFTER_PROP ) );
		// assertEquals( "avoid", style1.getStringProperty(
		// IStyleModel.PAGE_BREAK_INSIDE_PROP ) );
		// style1.setShowIfBlank( true );
		// assertEquals( "true", style1.getStringProperty(
		// IStyleModel.SHOW_IF_BLANK_PROP ) );
		/* box */

		assertEquals("solid", style1.getStringProperty(IStyleModel.BORDER_TOP_STYLE_PROP));
		assertEquals("thick", style1.getStringProperty(IStyleModel.BORDER_TOP_WIDTH_PROP));
		assertEquals("1mm", style2.getStringProperty(IStyleModel.BORDER_TOP_WIDTH_PROP));
		assertEquals("blue", style1.getStringProperty(IStyleModel.BORDER_TOP_COLOR_PROP));
		assertEquals("#0000FF", style2.getStringProperty(IStyleModel.BORDER_TOP_COLOR_PROP));
		assertEquals("dotted", style1.getStringProperty(IStyleModel.BORDER_LEFT_STYLE_PROP));
		assertEquals("medium", style1.getStringProperty(IStyleModel.BORDER_LEFT_WIDTH_PROP));
		assertEquals("1in", style2.getStringProperty(IStyleModel.BORDER_LEFT_WIDTH_PROP));
		assertEquals("red", style1.getStringProperty(IStyleModel.BORDER_LEFT_COLOR_PROP));
		assertEquals("#FF0000", style2.getStringProperty(IStyleModel.BORDER_LEFT_COLOR_PROP));
		assertEquals("dashed", style1.getStringProperty(IStyleModel.BORDER_BOTTOM_STYLE_PROP));
		assertEquals("thin", style1.getStringProperty(IStyleModel.BORDER_BOTTOM_WIDTH_PROP));
		assertEquals("1cm", style2.getStringProperty(IStyleModel.BORDER_BOTTOM_WIDTH_PROP));
		assertEquals("green", style1.getStringProperty(IStyleModel.BORDER_BOTTOM_COLOR_PROP));
		assertEquals("#00FF00", style2.getStringProperty(IStyleModel.BORDER_BOTTOM_COLOR_PROP));
		assertEquals("double", style1.getStringProperty(IStyleModel.BORDER_RIGHT_STYLE_PROP));
		assertEquals("thin", style1.getStringProperty(IStyleModel.BORDER_RIGHT_WIDTH_PROP));
		assertEquals("1px", style2.getStringProperty(IStyleModel.BORDER_RIGHT_WIDTH_PROP));
		assertEquals("blue", style1.getStringProperty(IStyleModel.BORDER_RIGHT_COLOR_PROP));
		assertEquals("#0000FF", style2.getStringProperty(IStyleModel.BORDER_RIGHT_COLOR_PROP));
		assertEquals("auto", style1.getStringProperty(IStyleModel.MARGIN_TOP_PROP));
		assertEquals("1em", style2.getStringProperty(IStyleModel.MARGIN_TOP_PROP));
		assertEquals("auto", style1.getStringProperty(IStyleModel.MARGIN_BOTTOM_PROP));
		assertEquals("1em", style2.getStringProperty(IStyleModel.MARGIN_BOTTOM_PROP));
		assertEquals("auto", style1.getStringProperty(IStyleModel.MARGIN_LEFT_PROP));
		assertEquals("1em", style2.getStringProperty(IStyleModel.MARGIN_LEFT_PROP));
		assertEquals("auto", style1.getStringProperty(IStyleModel.MARGIN_RIGHT_PROP));
		assertEquals("1em", style2.getStringProperty(IStyleModel.MARGIN_RIGHT_PROP));
		assertEquals("1pt", style1.getStringProperty(IStyleModel.PADDING_TOP_PROP));
		assertEquals("1pt", style1.getStringProperty(IStyleModel.PADDING_LEFT_PROP));
		assertEquals("1pt", style1.getStringProperty(IStyleModel.PADDING_BOTTOM_PROP));
		assertEquals("1pt", style1.getStringProperty(IStyleModel.PADDING_RIGHT_PROP));

		/* format */
		/**
		 * Because of currently model don't support edit the style from css file
		 */
		// DateTimeFormatValue dtfValue = new DateTimeFormatValue( );
		// dtfValue.setCategory( "Short Date" );
		// dtfValue.setPattern( "MM,DD,YYYY" );
		// style1.setDateTimeFormatCategory( dtfValue.getCategory( ) );
		// style1.setDateTimeFormat( dtfValue.getPattern( ) );
		// assertEquals( dtfValue.getCategory( ), ( (DateTimeFormatValue) style1
		// .getProperty( IStyleModel.DATE_TIME_FORMAT_PROP ) ).getCategory( ) );
		// assertEquals( dtfValue.getPattern( ), ( (DateTimeFormatValue) style1
		// .getProperty( IStyleModel.DATE_TIME_FORMAT_PROP ) ).getPattern( ) );
		// NumberFormatValue nfValue = new NumberFormatValue( );
		// nfValue.setCategory( "Currency" );
		// nfValue.setPattern( "####.####" );
		// style1.setNumberFormatCategory( nfValue.getCategory( ) );
		// style1.setNumberFormat( nfValue.getPattern( ) );
		// assertEquals( nfValue.getCategory( ), ( (NumberFormatValue) style1
		// .getProperty( IStyleModel.NUMBER_FORMAT_PROP ) ).getCategory( ) );
		// assertEquals( nfValue.getPattern( ), ( (NumberFormatValue) style1
		// .getProperty( IStyleModel.NUMBER_FORMAT_PROP ) ).getPattern( ) );
		// style1.setProperty( IStyleModel.NUMBER_ALIGN_PROP, "center" );
		// assertEquals( "center", style1.getStringProperty(
		// IStyleModel.NUMBER_ALIGN_PROP ) );
		// StringFormatValue sfValue = new StringFormatValue( );
		// sfValue.setCategory( "custom" );
		// sfValue.setPattern( "(@@@)@@@-@@@@" );
		// style1.setStringFormatCategory( sfValue.getCategory( ) );
		// style1.setStringFormat( sfValue.getPattern( ) );
		// assertEquals( sfValue.getCategory( ), ( (StringFormatValue) style1
		// .getProperty( IStyleModel.STRING_FORMAT_PROP ) ).getCategory( ) );
		// assertEquals( sfValue.getPattern( ), ( (StringFormatValue) style1
		// .getProperty( IStyleModel.STRING_FORMAT_PROP ) ).getPattern( ) );
		// style1.setCanShrink( false );
		// assertEquals( "false", style1.getStringProperty(
		// IStyleModel.CAN_SHRINK_PROP
		// ) );
		// MapRule mr = new MapRule( );
		// mr.setOperator( "between" );
		// mr.setValue1( "100" );
		// mr.setValue2( "500" );
		// mr.setTestExpression( "25+75" );
		// mr.setDisplay( "correct answer" );
		// List mapList = new ArrayList( );
		// mapList.add( mr );
		// PropertyHandle mapRules = style1.getPropertyHandle(
		// IStyleModel.MAP_RULES_PROP );
		// mapRules.addItem( mr );
		// assertEquals( mr.getOperator( ), ( (MapRule) ( (List) style1
		// .getProperty( IStyleModel.MAP_RULES_PROP ) ).get( 0 ) ).getOperator(
		// ) );
		// assertEquals( mr.getValue1( ), ( (MapRule) ( (List) style1
		// .getProperty( IStyleModel.MAP_RULES_PROP ) ).get( 0 ) ).getValue1( )
		// );
		// assertEquals( mr.getValue2( ), ( (MapRule) ( (List) style1
		// .getProperty( IStyleModel.MAP_RULES_PROP ) ).get( 0 ) ).getValue2( )
		// );
		// assertEquals( mr.getTestExpression( ), ( (MapRule) ( (List) style1
		// .getProperty( IStyleModel.MAP_RULES_PROP ) ).get( 0 )
		// ).getTestExpression( )
		// );
		// assertEquals( mr.getDisplay( ), ( (MapRule) ( (List) style1
		// .getProperty( IStyleModel.MAP_RULES_PROP ) ).get( 0 ) ).getDisplay( )
		// );
		// HighlightRule hlr = new HighlightRule( );
		// hlr.setOperator( "gt" );
		// hlr.setValue1( "Actuate" );
		// hlr.setTestExpression( "Actuate" );
		// hlr.setProperty( IStyleModel.BACKGROUND_COLOR_PROP, "red" );
		// List highlight = new ArrayList( );
		// highlight.add( hlr );
		// PropertyHandle highlightRules = style1.getPropertyHandle(
		// IStyleModel.HIGHLIGHT_RULES_PROP );
		// highlightRules.addItem( hlr );
		// assertEquals( hlr.getOperator( ), ( (HighlightRule) ( (List) style1
		// .getProperty( IStyleModel.HIGHLIGHT_RULES_PROP ) ).get( 0 )
		// ).getOperator( )
		// );
		// assertEquals( hlr.getValue1( ), ( (HighlightRule) ( (List) style1
		// .getProperty( IStyleModel.HIGHLIGHT_RULES_PROP ) ).get( 0 )
		// ).getValue1( ) );
		// assertEquals( hlr.getTestExpression( ), ( (HighlightRule) ( (List)
		// style1
		// .getProperty( IStyleModel.HIGHLIGHT_RULES_PROP ) ).get( 0 )
		// ).getTestExpression( ) );
		// assertEquals(
		// hlr.getProperty( designHandle.getModule( ), "red" ),
		// ( (HighlightRule) ( (List) style1.getProperty(
		// IStyleModel.HIGHLIGHT_RULES_PROP ) )
		// .get( 0 ) ).getProperty( designHandle.getModule( ), "red" ) );
	}

	/**
	 * Parse unsupported style properties in CSS file
	 * 
	 * @throws Exception
	 */
	public void testParserForNoSupportedProperties() throws Exception {
		List warningList = cssStyleSheetHandle.getWarnings(style3.getName());

		assertNotNull(warningList);

		for (int i = 0; i < warningList.size(); i++) {
			StyleSheetParserException error = (StyleSheetParserException) warningList.get(i);
			if (i == 0) {
				assertEquals("border-spacing", error.getCSSPropertyName());
				assertEquals("12pt", error.getCSSValue());
			} else if (i == 1) {
				assertEquals("border-collapse", error.getCSSPropertyName());
				assertEquals("separate", error.getCSSValue());
			} else {
				assertEquals("caption-side", error.getCSSPropertyName());
				assertEquals("bottom", error.getCSSValue());
			}
			error = null;
		}
	}
}