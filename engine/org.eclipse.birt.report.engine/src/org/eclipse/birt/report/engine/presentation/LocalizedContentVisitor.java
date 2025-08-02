/*******************************************************************************
 * Copyright (c) 2004, 2009, 2025 Actuate Corporation and others
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

package org.eclipse.birt.report.engine.presentation;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.core.format.StringFormatter;
import org.eclipse.birt.core.template.TextTemplate;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.report.engine.api.CachedImage;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.IHTMLImageHandler;
import org.eclipse.birt.report.engine.api.IImage;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.ImageSize;
import org.eclipse.birt.report.engine.api.impl.Image;
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.DataFormatValue;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSValueConstants;
import org.eclipse.birt.report.engine.data.dte.SingleCubeResultSet;
import org.eclipse.birt.report.engine.data.dte.SingleQueryResultSet;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.executor.template.TemplateExecutor;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.ICubeResultSet;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;
import org.eclipse.birt.report.engine.extension.IReportItemPresentation;
import org.eclipse.birt.report.engine.extension.Size;
import org.eclipse.birt.report.engine.extension.internal.ReportItemPresentationInfo;
import org.eclipse.birt.report.engine.ir.AutoTextItemDesign;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.ir.Expression;
import org.eclipse.birt.report.engine.ir.ExtendedItemDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.TextItemDesign;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.eclipse.birt.report.engine.script.internal.OnRenderScriptVisitor;
import org.eclipse.birt.report.model.api.AutoTextHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.w3c.dom.css.CSSValue;

import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

/**
 * Localized content visitor
 *
 * @since 3.3
 *
 */
public class LocalizedContentVisitor {

	protected static Logger logger = Logger.getLogger(LocalizedContentVisitor.class.getName());

	private ExecutionContext context;
	private Locale locale;
	private TimeZone timeZone;
	private String outputFormat;
	protected HashMap<String, SoftReference<TextTemplate>> templates = new HashMap<String, SoftReference<TextTemplate>>();
	private OnRenderScriptVisitor onRenderVisitor;
	final static char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	final static byte[] dummyImageData = { -119, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13, 73, 72, 68, 82, 0, 0, 0, 1, 0,
			0, 0, 1, 8, 2, 0, 0, 0, -112, 119, 83, -34, 0, 0, 0, 1, 115, 82, 71, 66, 0, -82, -50, 28, -23, 0, 0, 0, 4,
			103, 65, 77, 65, 0, 0, -79, -113, 11, -4, 97, 5, 0, 0, 0, 9, 112, 72, 89, 115, 0, 0, 18, 116, 0, 0, 18, 116,
			1, -34, 102, 31, 120, 0, 0, 0, 12, 73, 68, 65, 84, 24, 87, 99, -8, -1, -1, 63, 0, 5, -2, 2, -2, -89, 53,
			-127, -124, 0, 0, 0, 0, 73, 69, 78, 68, -82, 66, 96, -126 };

	private static final String FILE_PROTOCOL = ":/";
	private static final String URL_DATA_PROTOCOL = "data:";

	/**
	 * Constructor
	 *
	 * @param context execution context
	 */
	public LocalizedContentVisitor(ExecutionContext context) {
		this.context = context;
		this.locale = context.getLocale();
		this.timeZone = context.getTimeZone();
		this.outputFormat = context.getOutputFormat();
		this.onRenderVisitor = new OnRenderScriptVisitor(context);
	}

	IReportContent getReportContent() {
		return context.getReportContent();
	}

	ModuleHandle getReportDesign() {
		return context.getDesign();
	}

	/**
	 * Checks the background image property. If it is given as a relative path, gets
	 * its absolute path and sets it back to the style.
	 *
	 * @param style the style that defines background image related properties
	 */
	protected void processBackgroundImage(IStyle style) {
		if (style == null) {
			return;
		}

		String image = style.getBackgroundImage();
		if (image == null) {
			return;
		}

		ModuleHandle reportDesign = context.getDesign();
		if (reportDesign != null) {
			URL url = reportDesign.findResource(image, IResourceLocator.IMAGE, context.getAppContext());
			if (url != null) {
				style.setBackgroundImage(url.toExternalForm());
			}
		}
	}

	/**
	 * Localize of content objects
	 *
	 * @param content
	 * @return Returns the content object
	 * @throws BirtException
	 */
	public IContent localize(IContent content) throws BirtException {
		IStyle style = content.getInlineStyle();
		processBackgroundImage(style);
		switch (content.getContentType()) {
		case IContent.CELL_CONTENT:
			return localizeCell((ICellContent) content);
		case IContent.DATA_CONTENT:
			return localizeData((IDataContent) content);
		case IContent.FOREIGN_CONTENT:
			return localizeForeign((IForeignContent) content);
		case IContent.IMAGE_CONTENT:
			return localizeImage((IImageContent) content);
		case IContent.LABEL_CONTENT:
			return localizeLabel((ILabelContent) content);
		case IContent.PAGE_CONTENT:
			return localizePage((IPageContent) content);
		case IContent.ROW_CONTENT:
			return localizeRow((IRowContent) content);
		case IContent.TABLE_CONTENT:
			return localizeTable((ITableContent) content);
		case IContent.TEXT_CONTENT:
			return localizeText((ITextContent) content);
		case IContent.AUTOTEXT_CONTENT:
			return localizeAutoText((IAutoTextContent) content);
		case IContent.LIST_CONTENT:
			return localizeList((IListContent) content);
		case IContent.GROUP_CONTENT:
		case IContent.LIST_GROUP_CONTENT:
		case IContent.TABLE_GROUP_CONTENT:
			return localizeGroup((IGroupContent) content);
		default:
			return content;
		}
	}

	/**
	 * Localize the report content
	 *
	 * @param report
	 * @return Returns the report content
	 * @throws BirtException
	 */
	public IReportContent localizeReport(IReportContent report) throws BirtException {
		processReport(report);
		return report;
	}

	protected IContent localizeAllChildren(IContent content) throws BirtException {
		ArrayList<?> children = (ArrayList<?>) content.getChildren();
		if (children != null) {
			for (int i = 0; i < children.size(); i++) {
				IContent child = (IContent) children.get(i);
				localize(child);
				localizeAllChildren(child);
			}
		}
		return content;
	}

	private IPageContent localizePage(IPageContent page) throws BirtException {
		boolean isExecutingMasterPage = context.isExecutingMasterPage();
		context.setExecutingMasterPage(true);
		localizeAllChildren(page);
		context.setExecutingMasterPage(isExecutingMasterPage);
		return page;
	}

	protected TextTemplate parseTemplate(String text) throws BirtException {
		SoftReference<TextTemplate> templateRef = templates.get(text);
		TextTemplate template = null;
		if (templateRef != null) {
			template = templateRef.get();
			if (template != null) {
				return template;
			}
		}
		try {
			template = new org.eclipse.birt.core.template.TemplateParser().parse(text);
			templateRef = new SoftReference<TextTemplate>(template);
			templates.put(text, templateRef);
		} catch (Throwable ex) {
			throw new EngineException(ex.getLocalizedMessage(), ex);
		}
		return template;
	}

	private String executeTemplate(TextTemplate template, HashMap<String, Object> values) {
		return new TemplateExecutor(context).execute(template, values);
	}

	private IListContent localizeList(IListContent list) {
		handleOnRender(list);
		String altText = localize(list, list.getAltTextKey(), list.getAltText());
		list.setAltText(altText);
		return list;
	}

	private ITableContent localizeTable(ITableContent table) {
		handleOnRender(table);

		String captionText = table.getCaption();
		String captionKey = table.getCaptionKey();

		captionText = localize(table, captionKey, captionText);
		table.setCaption(captionText);

		return table;
	}

	private IRowContent localizeRow(IRowContent row) {
		handleOnRender(row);
		return row;
	}

	private ICellContent localizeCell(ICellContent cell) {
		handleOnRender(cell);
		String altText = localize(cell, cell.getAltTextKey(), cell.getAltText());
		cell.setAltText(altText);
		return cell;
	}

	/**
	 * handle the data content.
	 *
	 * @param data data content object
	 */
	private IDataContent localizeData(IDataContent data) {
		handleOnRender(data);
		processData(data);
		return data;
	}

	/**
	 * process the data content
	 *
	 * <li>localize the help text
	 * <li>format the value
	 * <li>handle it as it is an text.
	 *
	 * @param data data object
	 */
	protected void processData(IDataContent data) {
		String altText = localize(data, data.getAltTextKey(), data.getAltText());
		data.setAltText(altText);
		String helpText = localize(data, data.getHelpKey(), data.getHelpText());
		data.setHelpText(helpText);
		String text = ""; //$NON-NLS-1$
		if (data.getLabelKey() != null || data.getLabelText() != null) {
			text = localize(data, data.getLabelKey(), data.getLabelText());
		} else {
			Object value = data.getValue();
			IStyle style = data.getComputedStyle();
			text = format(value, style);
			if (value instanceof Number) {
				CSSValue align = style.getProperty(StyleConstants.STYLE_NUMBER_ALIGN);
				if (align != null && align != CSSValueConstants.NONE_VALUE) {
					data.getStyle().setProperty(StyleConstants.STYLE_TEXT_ALIGN, align);
				}
			}
		}

		// text can be null value after applying format
		data.setText(text == null ? "" : text);
	}

	protected String format(Object value, IStyle style) {
		if (value instanceof Object[]) {
			Object[] values = (Object[]) value;
			StringBuilder sb = new StringBuilder();
			sb.append('[');
			for (Object v : values) {
				sb.append(format(v, style));
				sb.append(", ");
			}
			if (sb.length() > 1) {
				sb.setLength(sb.length() - 2);
			}
			sb.append(']');
			return sb.toString();

		}
		if (value == null) {
			return null;
		}

		DataFormatValue dataFormat = style.getDataFormat();
		String pattern = null;
		String locale = null;
		if (value instanceof Number) {
			if (dataFormat != null) {
				pattern = dataFormat.getNumberPattern();
				locale = dataFormat.getNumberLocale();
			}
			NumberFormatter fmt = context.getNumberFormatter(pattern, locale);
			return fmt.format((Number) value);
		}

		if (value instanceof String) {
			if (dataFormat != null) {
				pattern = dataFormat.getStringPattern();
				locale = dataFormat.getStringLocale();
			}
			StringFormatter fmt = context.getStringFormatter(pattern, locale);
			return fmt.format((String) value);
		}

		if (value instanceof java.util.Date) {
			if (dataFormat != null) {
				if (value instanceof java.sql.Date) {
					pattern = dataFormat.getDatePattern();
					locale = dataFormat.getDateLocale();
				} else if (value instanceof java.sql.Time) {
					pattern = dataFormat.getTimePattern();
					locale = dataFormat.getTimeLocale();
				}
				if (pattern == null && locale == null) {
					pattern = dataFormat.getDateTimePattern();
					locale = dataFormat.getDateTimeLocale();
				}
			}
			DateFormatter fmt = context.getDateFormatter(pattern, locale);
			return fmt.format((java.util.Date) value);
		}

		if (value instanceof byte[]) {
			byte[] bytes = (byte[]) value;
			int length = (bytes.length <= 8 ? bytes.length : 8);

			StringBuilder buffer = new StringBuilder();
			int index = 0;
			while (index < length) {
				byte byteValue = bytes[index];
				int lowValue = byteValue & 0x0F;
				int highValue = (byteValue >> 4) & 0x0F;
				buffer.append(HEX[highValue]).append(HEX[lowValue]).append(' ');
				index++;
			}
			if (length > 0) {
				if (length != bytes.length) {
					buffer.append("...");
				} else {
					buffer.setLength(buffer.length() - 1);
				}
			}

			return buffer.toString();
		}
		return value.toString();
	}

	/**
	 * handle the label.
	 *
	 * @param label label content
	 */
	private ILabelContent localizeLabel(ILabelContent label) {
		handleOnRender(label);
		processLabel(label);
		return label;
	}

	/**
	 * process the label content
	 *
	 * <li>localize the help text
	 * <li>localize the label content
	 * <li>handle it as it is an text
	 *
	 * @param label label object
	 */
	protected void processLabel(ILabelContent label) {
		String altText = localize(label, label.getAltTextKey(), label.getAltText());
		label.setAltText(altText);

		String helpText = localize(label, label.getHelpKey(), label.getHelpText());
		label.setHelpText(helpText);

		if (label.getText() == null) {
			String text = localize(label, label.getLabelKey(), label.getLabelText());
			label.setText(text);
		}
	}

	protected void processReport(IReportContent report) {
		if (report.getDesign() != null) {
			ReportDesignHandle handle = report.getDesign().getReportDesign();
			String title = localize(report, handle.getTitleKey(), handle.getTitle());
			report.setTitle(title);
		}
	}

	private ITextContent localizeText(ITextContent text) {
		handleOnRender(text);
		String altText = localize(text, text.getAltTextKey(), text.getAltText());
		text.setAltText(altText);
		return text;
	}

	private IAutoTextContent localizeAutoText(IAutoTextContent autoText) {
		int type = autoText.getType();
		IStyle style = autoText.getComputedStyle();

		if (type == IAutoTextContent.PAGE_NUMBER) {
			long number = context.getFilteredPageNumber();
			String text = format(number, style);
			autoText.setText(text == null ? "" : text);
		} else if (type == IAutoTextContent.TOTAL_PAGE) {
			long totalPage = context.getFilteredTotalPage();
			if (totalPage <= 0) {
				autoText.setText("---");
			} else {
				String text = format(totalPage, style);
				autoText.setText(text == null ? "" : text);
			}
		} else if (type == IAutoTextContent.UNFILTERED_PAGE_NUMBER) {
			long number = context.getPageNumber();
			String text = format(number, style);
			autoText.setText(text == null ? "" : text);
		} else if (type == IAutoTextContent.UNFILTERED_TOTAL_PAGE) {
			long totalPage = context.getTotalPage();
			if (totalPage <= 0) {
				autoText.setText("---");
			} else {
				String text = format(totalPage, style);
				autoText.setText(text == null ? "" : text);
			}
		} else if (type == IAutoTextContent.PAGE_VARIABLE) {
			AutoTextItemDesign design = (AutoTextItemDesign) autoText.getGenerateBy();
			AutoTextHandle designHandle = (AutoTextHandle) design.getHandle();
			String varName = designHandle.getPageVariable();
			Object result = context.getPageVariable(varName);
			String text = format(result, style);
			autoText.setText(text == null ? "" : text);
		}
		handleOnRender(autoText);

		return autoText;
	}

	/**
	 * handle the foreign content object.
	 *
	 * Foreign content can be created by following design element:
	 * <li>Text(HTML). It will create a TEMPLATE_TYPE foreign object.
	 * <li>MultiLine(HTML). It will create a HTML_TYPE forign object
	 * <li>MultiLine(PlainText).It will create a TEXT_TYPE foreign object
	 * <li>Extended item. It will create a TEXT_TYPE/HTML_TYPE/IMAGE_TYPE/VALUE_TYPE
	 * foreign object.
	 *
	 */
	private IContent localizeForeign(IForeignContent foreignContent) {
		IReportContent reportContent = getReportContent();

		handleOnRender(foreignContent);
		String rawFormat = foreignContent.getRawType();
		Object rawValue = foreignContent.getRawValue();

		if (IForeignContent.TEMPLATE_TYPE.equals(rawFormat)) {
			processTemplateContent(foreignContent);
			return foreignContent;
		}

		if (IForeignContent.EXTERNAL_TYPE.equals(rawFormat)) {
			return processExtendedContent(foreignContent);
		}

		if (IForeignContent.IMAGE_TYPE.equals(rawFormat)) {
			if (rawValue instanceof IImageContent) {
				IImageContent image = (IImageContent) rawValue;
				processImage(image);
				return image;
			}
			if (rawValue instanceof byte[]) {
				IImageContent imageContent = reportContent.createImageContent(foreignContent);
				imageContent.setImageSource(IImageContent.IMAGE_EXPRESSION);
				imageContent.setData((byte[]) rawValue);
				processImage(imageContent);
				return imageContent;
			}
		}

		if (IForeignContent.TEXT_TYPE.equals(rawFormat)) {
			ITextContent textContent = reportContent.createDataContent(foreignContent);
			textContent.setText(rawValue == null ? "" : rawValue.toString()); //$NON-NLS-1$
			return textContent;
		}

		if (IForeignContent.HTML_TYPE.equals(rawFormat)) {
			String key = foreignContent.getRawKey();
			if (key != null) {
				String text = localize(foreignContent, key, null);
				if (text != null) {
					foreignContent.setRawValue(text);
				}
			} else {
				Object value = foreignContent.getRawValue();
				String text = format(value, foreignContent.getComputedStyle());
				if (text != null) {
					foreignContent.setRawValue(text);
				}
			}
			return foreignContent;
		}

		if (IForeignContent.VALUE_TYPE.equals(rawFormat)) {
			IDataContent dataContent = reportContent.createDataContent(foreignContent);
			dataContent.setParent(foreignContent.getParent());
			dataContent.setValue(rawValue);
			processData(dataContent);
			return dataContent;
		}
		return foreignContent;
	}

	/**
	 * localize the text.
	 *
	 * @param key  text key
	 * @param text default text
	 * @return localized text.
	 */
	private String localize(IContent content, String key, String text) {
		assert (content != null);
		if (content.getGenerateBy() != null) {
			DesignElementHandle element = ((ReportItemDesign) content.getGenerateBy()).getHandle();
			if (key != null && element != null) {
				String t = ModuleUtil.getExternalizedValue(element, key, text, ULocale.forLocale(locale));
				if (t != null) {
					return t;
				}
			}
		}
		return text;
	}

	private String localize(IReportContent content, String key, String text) {
		assert (content != null);
		if (content.getDesign() != null) {
			DesignElementHandle element = content.getDesign().getReportDesign();
			if (key != null && element != null) {
				String t = ModuleUtil.getExternalizedValue(element, key, text, ULocale.forLocale(locale));
				if (t != null) {
					return t;
				}
			}
		}
		return text;
	}

	private IImageContent localizeImage(IImageContent image) {
		handleOnRender(image);
		if (image.getImageSource() == IImageContent.IMAGE_FILE)
//				|| image.getImageSource( ) == IImageContent.IMAGE_URL )
		{
			String strUri = image.getURI();

			ModuleHandle reportDesign = context.getDesign();
			URL uri = reportDesign.findResource(strUri, IResourceLocator.IMAGE, context.getAppContext());
			if (uri != null) {
				image.setURI(uri.toExternalForm());
			}
		} else if (image.getImageSource() == IImageContent.IMAGE_URL) {
			String uri = image.getURI();
			if (!uri.contains(FILE_PROTOCOL) && !uri.contains(URL_DATA_PROTOCOL)) {
				IRenderOption option = context.getRenderOption();
				if (option != null) {
					String appBaseUrl = option.getAppBaseURL();
					if (appBaseUrl != null) {
						if (appBaseUrl.endsWith("/")) {
							image.setURI(appBaseUrl + uri);
						} else {
							image.setURI(appBaseUrl + "/" + uri);
						}
					}
				}
			}
		}
		processImage(image);
		return image;
	}

	protected void processImage(IImageContent image) {
		String altText = localize(image, image.getAltTextKey(), image.getAltText());
		image.setAltText(altText);
		String helpText = localize(image, image.getHelpKey(), image.getHelpText());
		image.setHelpText(helpText);
	}

	/**
	 * handle the template result.
	 *
	 * @param foreignContent
	 */

	protected void processTemplateContent(IForeignContent foreignContent) {
		assert IForeignContent.TEMPLATE_TYPE.equals(foreignContent.getRawType());

		if (foreignContent.getGenerateBy() instanceof TextItemDesign) {
			TextItemDesign design = (TextItemDesign) foreignContent.getGenerateBy();

			String text = null;
			HashMap<String, Object> rawValues = null;
			if (foreignContent.getRawValue() instanceof Object[]) {
				Object[] rawValue = (Object[]) foreignContent.getRawValue();
				assert rawValue.length == 2;
				assert rawValue[0] == null || rawValue[0] instanceof String;
				if (rawValue[0] != null) {
					text = (String) rawValue[0];
				}
				if (rawValue[1] instanceof HashMap) {
					rawValues = (HashMap<String, Object>) rawValue[1];
				}
			}

			if (text == null) {
				String textKey = design.getTextKey();
				String textContent = design.getText();
				text = localize(foreignContent, textKey, textContent);
			}
			try {
				TextTemplate template = parseTemplate(text);

				String result = executeTemplate(template, rawValues);

				foreignContent.setRawType(IForeignContent.HTML_TYPE);
				foreignContent.setRawValue(result);
			} catch (BirtException ex) {
				context.addException(design, ex);
			}
		}
	}

	private IGroupContent localizeGroup(IGroupContent group) {
		handleOnRender(group);
		return group;
	}

	protected String getOutputFormat() {
		return outputFormat;
	}

//	private static final String PRINTING_FORMATS = "pdf;postscript;ppt;doc;xls";
//	/**
//	 * @return whether the output format is for printing
//	 */
//	protected boolean isForPrinting( )
//	{
//		String outputFormat = getOutputFormat( );
//		if ( PRINTING_FORMATS.indexOf( outputFormat.toLowerCase( ) ) != -1 )
//			return true;
//		return false;
//	}

	private int getChartResolution(IContent content) {
		int resolution = 0;
		Object chartDpi = context.getRenderOption().getOption(IRenderOption.CHART_DPI);
		if (chartDpi instanceof Number) {
			resolution = ((Number) chartDpi).intValue();
		}
		if (resolution == 0) {
			Map<?, ?> appContext = context.getAppContext();
			if (appContext != null) {
				Object tmp = appContext.get(EngineConstants.APPCONTEXT_CHART_RESOLUTION);
				if (tmp instanceof Number) {
					resolution = ((Number) tmp).intValue();
				}
			}
		}
		if (resolution < 96) {
			Object renderOptionDpi = context.getRenderOption().getOption(IRenderOption.RENDER_DPI);
			int dpi = 0;
			if (renderOptionDpi instanceof Integer) {
				dpi = ((Integer) renderOptionDpi).intValue();
			}
			resolution = PropertyUtil.getRenderDpi(content, dpi);
		}
		return resolution;
	}

	private String getChartFormats() {
		IRenderOption renderOption = context.getRenderOption();
		String formats = renderOption.getSupportedImageFormats();
		if (formats != null) {
			return formats;
		}
		return "PNG;GIF;JPG;BMP;"; //$NON-NLS-1$
	}

	private String getImageCacheID(IContent content) {
		StringBuilder buffer = new StringBuilder();
		buffer.append(content.getInstanceID().toUniqueString());
		buffer.append(getChartResolution(content));
		buffer.append(getChartFormats());
		buffer.append(locale);
		if (timeZone != null) {
			buffer.append(timeZone.getID());
		}
		// Add size to cache ID
		DimensionType heightDimension = content.getHeight();
		String heightUnits = heightDimension == null ? "" : heightDimension.getUnits();
		double heightValue = heightDimension == null ? 0 : heightDimension.getMeasure();
		DimensionType widthDimension = content.getWidth();
		String widthUnits = widthDimension == null ? "" : widthDimension.getUnits();
		double widthValue = widthDimension == null ? 0 : widthDimension.getMeasure();
		buffer.append(heightValue);
		buffer.append(heightUnits);
		buffer.append(widthValue);
		buffer.append(widthUnits);
		return buffer.toString();
	}

	private ImageSize processImageSize(Size size) {
		if (size == null) {
			return null;
		}

		return new ImageSize(size.getUnit(), size.getWidth(), size.getHeight());
	}

	private IContent processFixedSizeChart(IForeignContent content, DimensionType width, DimensionType height) {
		IImageContent dummyContent = getReportContent().createImageContent(content);
		dummyContent.setWidth(width);
		dummyContent.setHeight(height);
		dummyContent.setImageSource(IImageContent.IMAGE_EXPRESSION);
		dummyContent.setData(dummyImageData);
		dummyContent.setParent(content.getParent());
		return dummyContent;
	}

	private IContent processCachedImage(IForeignContent content, CachedImage cachedImage) {
		IImageContent imageObj = getReportContent().createImageContent(content);
		imageObj.setParent(content.getParent());
		// Set image map
		imageObj.setImageSource(IImageContent.IMAGE_FILE);
		imageObj.setURI(cachedImage.getURL());
		imageObj.setMIMEType(cachedImage.getMIMEType());
		imageObj.setImageMap(cachedImage.getImageMap());
		imageObj.setAltText(content.getAltText());
		imageObj.setAltTextKey(content.getAltTextKey());
		ImageSize size = cachedImage.getImageSize();
		if (size != null) {
			DimensionType height = new DimensionType(size.getHeight(), size.getUnit());
			DimensionType width = new DimensionType(size.getWidth(), size.getUnit());
			imageObj.setHeight(height);
			imageObj.setWidth(width);
		}
		processImage(imageObj);
		return imageObj;
	}

	/**
	 * handle an extended item.
	 *
	 * @param content the object.
	 */
	protected IContent processExtendedContent(IForeignContent content) {
		assert IForeignContent.EXTERNAL_TYPE.equals(content.getRawType());
		assert content.getGenerateBy() instanceof ExtendedItemDesign;

		IContent generatedContent = content;

		ExtendedItemDesign design = (ExtendedItemDesign) content.getGenerateBy();
		ExtendedItemHandle handle = (ExtendedItemHandle) design.getHandle();
		String tagName = handle.getExtensionName();
		IReportItemPresentation itemPresentation = context.getExtendedItemManager().createPresentation(handle);
		// call the presentation peer to create the content object
		int resolution;

		IDataQueryDefinition[] queries = design.getQueries();

		ReportItemPresentationInfo info = new ReportItemPresentationInfo();
		info.setModelObject(handle);
		info.setApplicationClassLoader(context.getApplicationClassLoader());
		info.setReportContext(context.getReportContext());
		info.setReportQueries(queries);
		resolution = getChartResolution(content);
		info.setResolution(resolution);
		info.setExtendedItemContent(content);
		info.setSupportedImageFormats(getChartFormats());
		info.setActionHandler(context.getActionHandler());
		info.setOutputFormat(getOutputFormat());

		if (itemPresentation != null) {
			itemPresentation.init(info);
		}

		if ("Chart".equals(tagName)) {
			IHTMLImageHandler imageHandler = context.getImageHandler();
			// get cached image if support the cache
			if (imageHandler != null && itemPresentation != null && itemPresentation.isCacheable()) {
				String imageId = getImageCacheID(content);
				CachedImage cachedImage = imageHandler.getCachedImage(imageId, IImage.CUSTOM_IMAGE,
						context.getReportContext());
				if (cachedImage != null) {
					return processCachedImage(content, cachedImage);
				}
			}

			if (context.getFactoryMode() && context.getTaskType() != IEngineTask.TASK_RUNANDRENDER) {
				IReportItem item = null;

				try {
					item = handle.getReportItem();
				} catch (ExtendedElementException e) {

				}
				if (item != null && item.hasFixedSize()) {
					if (design.getWidth() != null && design.getHeight() != null) {
						return processFixedSizeChart(content, design.getWidth(), design.getHeight());
					}
				}
			}
		}

		if (itemPresentation != null) {
			Object rawValue = content.getRawValue();
			if (rawValue instanceof byte[]) {
				byte[] values = (byte[]) rawValue;
				itemPresentation.deserialize(new ByteArrayInputStream(values));
			}

			if (queries == null) {
				DesignElementHandle elementHandle = design.getHandle();
				if (elementHandle instanceof ReportElementHandle) {
					queries = context.getReport()
							.getQueryByReportHandle((ReportElementHandle) elementHandle);
				}
			}
			IBaseResultSet[] rsets = context.getResultSets();
			IBaseResultSet[] resultSets = null;
			if (queries == null) {
				if (rsets != null) {
					resultSets = new IBaseResultSet[1];
					int type = rsets[0].getType();
					if (IBaseResultSet.QUERY_RESULTSET == type) {
						resultSets[0] = new SingleQueryResultSet((IQueryResultSet) rsets[0]);
					} else if (IBaseResultSet.CUBE_RESULTSET == type) {
						resultSets[0] = new SingleCubeResultSet((ICubeResultSet) rsets[0]);
					} else {
						throw new UnsupportedOperationException(
								"Unknown type of result set is found: " + rsets[0].getClass().getName());
					}
				}
			} else {
				resultSets = rsets;
			}

			try {
				Object output = itemPresentation.onRowSets(resultSets);
				int type = itemPresentation.getOutputType();
				String imageMIMEType = itemPresentation.getImageMIMEType();
				Size size = itemPresentation.getSize();
				if (output != null) {
					generatedContent = processExtendedContent(content, type, output, imageMIMEType, size);
				}
				if (size != null) {
					DimensionType height = new DimensionType(size.getHeight(), size.getUnit());
					DimensionType width = new DimensionType(size.getWidth(), size.getUnit());
					generatedContent.setHeight(height);
					generatedContent.setWidth(width);
				}

				itemPresentation.finish();
			} catch (BirtException ex) {
				context.addException(design.getHandle(), ex);
				logger.log(Level.SEVERE, ex.getMessage(), ex);
			}
		}
		if (generatedContent instanceof IImageContent) {
			IImageContent imageContent = (IImageContent) generatedContent;
			imageContent.setResolution(resolution);
		}
		return generatedContent;
	}

	/**
	 * handle the content created by the IPresentation
	 *
	 * @param content foreign content
	 * @param type    output type
	 * @param output  output
	 */
	protected IContent processExtendedContent(IForeignContent content, int type, Object output, String imageMIMEType,
			Size size) {
		assert IForeignContent.EXTERNAL_TYPE.equals(content.getRawType());
		assert output != null;

		IReportContent reportContent = getReportContent();

		switch (type) {
		case IReportItemPresentation.OUTPUT_NONE:
			break;
		case IReportItemPresentation.OUTPUT_AS_IMAGE:
		case IReportItemPresentation.OUTPUT_AS_IMAGE_WITH_MAP:
			// the output object is a image, so create a image content
			// object
			Object imageMap = null;
			byte[] imageContent = {};

			Object image = output;
			if (type == IReportItemPresentation.OUTPUT_AS_IMAGE_WITH_MAP) {
				// OUTPUT_AS_IMAGE_WITH_MAP
				Object[] imageWithMap = (Object[]) output;
				if (imageWithMap.length > 0) {
					image = imageWithMap[0];
				}
				if (imageWithMap.length > 1) {
					imageMap = imageWithMap[1];
				}
			}

			if (image instanceof InputStream) {
				imageContent = readContent((InputStream) image);
			} else if (output instanceof byte[]) {
				imageContent = (byte[]) image;
			} else {
				assert false;
				logger.log(Level.WARNING, "unsupported image type:{0}", //$NON-NLS-1$
						imageMIMEType != null ? imageMIMEType : "Unknown");

			}

			IImageContent imageObj = reportContent.createImageContent(content);
			imageObj.setParent(content.getParent());
			// Set image map
			imageObj.setImageSource(IImageContent.IMAGE_EXPRESSION);
			imageObj.setData(imageContent);
			imageObj.setImageMap(imageMap);
			imageObj.setMIMEType(imageMIMEType);
			imageObj.setAltText(content.getAltText());
			imageObj.setAltTextKey(content.getAltTextKey());

			// put the cached image into cache
			IHTMLImageHandler imageHandler = context.getImageHandler();
			if (imageHandler != null) {
				ExtendedItemDesign design = (ExtendedItemDesign) content.getGenerateBy();
				ExtendedItemHandle handle = (ExtendedItemHandle) design.getHandle();
				IReportItemPresentation itemPresentation = context.getExtendedItemManager().createPresentation(handle);
				// call the presentation peer to create the content object
				int resolution;

				IDataQueryDefinition[] queries = design.getQueries();

				ReportItemPresentationInfo info = new ReportItemPresentationInfo();
				info.setModelObject(handle);
				info.setApplicationClassLoader(context.getApplicationClassLoader());
				info.setReportContext(context.getReportContext());
				info.setReportQueries(queries);
				resolution = getChartResolution(content);
				info.setResolution(resolution);
				info.setExtendedItemContent(content);
				info.setSupportedImageFormats(getChartFormats());
				info.setActionHandler(context.getActionHandler());
				info.setOutputFormat(getOutputFormat());

				itemPresentation.init(info);

				if (itemPresentation != null && itemPresentation.isCacheable()) {
					Image img = new Image(imageObj);
					img.setRenderOption(context.getRenderOption());
					img.setReportRunnable(context.getRunnable());
					img.setImageSize(processImageSize(size));
					String imageId = getImageCacheID(content);
					CachedImage cachedImage = imageHandler.addCachedImage(imageId, IImage.CUSTOM_IMAGE, img,
							context.getReportContext());
					if (cachedImage != null) {
						return processCachedImage(content, cachedImage);
					}
				}
			}

			// don' have image cache, so handle it as a normal image
			processImage(imageObj);
			return imageObj;

		case IReportItemPresentation.OUTPUT_AS_CUSTOM:

			IDataContent dataObj = reportContent.createDataContent(content);
			dataObj.setValue(output);
			processData(dataObj);
			return dataObj;

		case IReportItemPresentation.OUTPUT_AS_HTML_TEXT:
			content.setRawType(IForeignContent.HTML_TYPE);
			content.setRawValue(output.toString());
			return content;

		case IReportItemPresentation.OUTPUT_AS_TEXT:
			ITextContent textObj = reportContent.createTextContent();
			textObj.setText(output.toString());
			return textObj;

		case IReportItemPresentation.OUTPUT_AS_UNKNOWN:
			content.setRawValue(output);
			return content;

		default:
			assert false;
			logger.log(Level.WARNING, "unsupported output format:{0}", //$NON-NLS-1$
					Integer.valueOf(type));
		}
		return content;

	}

	/**
	 * read the content of input stream.
	 *
	 * @param in input content
	 * @return content in the stream.
	 */
	static protected byte[] readContent(InputStream in) {
		BufferedInputStream bin = in instanceof BufferedInputStream ? (BufferedInputStream) in
				: new BufferedInputStream(in);
		ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
		byte[] buffer = new byte[1024];
		int readSize = 0;
		try {
			readSize = bin.read(buffer);
			while (readSize != -1) {
				out.write(buffer, 0, readSize);
				readSize = bin.read(buffer);
			}
		} catch (IOException ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
		}
		return out.toByteArray();
	}

	protected void handleOnRender(IContent content) {
		Object genBy = content.getGenerateBy();
		if (genBy instanceof ReportItemDesign) {
			ReportItemDesign design = (ReportItemDesign) genBy;
			Expression onRender = design.getOnRender();
			String javaEventHandler = design.getJavaClass();
			if (onRender != null || javaEventHandler != null || design instanceof ExtendedItemDesign) {
				// disable onRender script for run task.
				if (context.getEngineTask().getTaskType() != IEngineTask.TASK_RUN) {
					onRenderVisitor.onRender(content);
				}
			}
		}
	}
}
