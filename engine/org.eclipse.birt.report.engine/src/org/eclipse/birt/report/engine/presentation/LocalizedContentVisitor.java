package org.eclipse.birt.report.engine.presentation;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.core.format.StringFormatter;
import org.eclipse.birt.core.template.TextTemplate;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.HTMLRenderContext;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.content.ContentVisitorAdapter;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.content.impl.ForeignContent;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSValueConstants;
import org.eclipse.birt.report.engine.data.IResultSet;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.executor.template.TemplateExecutor;
import org.eclipse.birt.report.engine.extension.IReportItemPresentation;
import org.eclipse.birt.report.engine.extension.IRowSet;
import org.eclipse.birt.report.engine.extension.internal.ExtensionManager;
import org.eclipse.birt.report.engine.extension.internal.RowSet;
import org.eclipse.birt.report.engine.ir.ExtendedItemDesign;
import org.eclipse.birt.report.engine.ir.GridItemDesign;
import org.eclipse.birt.report.engine.ir.LabelItemDesign;
import org.eclipse.birt.report.engine.ir.ListItemDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;
import org.eclipse.birt.report.engine.ir.TextItemDesign;
import org.eclipse.birt.report.engine.script.internal.CellScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.DataItemScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.DynamicTextScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.GridScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.ImageScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.LabelScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.ListScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.RowScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.TableScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.TextItemScriptExecutor;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.w3c.dom.css.CSSValue;

import com.ibm.icu.util.ULocale;


public class LocalizedContentVisitor extends ContentVisitorAdapter
{
	
	protected static Logger logger = Logger
			.getLogger( LocalizedContentVisitor.class.getName( ) );
	
	private ExecutionContext context;
	private Locale locale;
	private String outputFormat;
	protected HashMap templates = new HashMap( );
	
	public LocalizedContentVisitor( ExecutionContext context )
	{
		this.context = context;
		this.locale = context.getLocale( );
		this.outputFormat = context.getOutputFormat( );
	}
	
	IReportContent getReportContent( )
	{
		return context.getReportContent( );
	}

	ReportDesignHandle getReportDesign( )
	{
		return context.getDesign( );
	}
	
	
	public IContent localize(IContent content)
	{
		Object value = content.accept( this, content );
		return (IContent) value;
	}
	
	protected void localizeAllChildren( IContent content )
	{
		Collection children = content.getChildren( );
		if ( children != null )
		{
			Iterator iter = children.iterator( );
			while ( iter.hasNext( ) )
			{
				IContent child = (IContent) iter.next( );
				localize( child );
				localizeAllChildren( child );
			}
		}
	}

	public Object visitPage( IPageContent page, Object value )
	{
		// must localize all the contents in the page content.
		localizeAllChildren( page );
		return value;
	}
	
	protected TextTemplate parseTemplate( String text )
	{
		SoftReference templateRef = (SoftReference) templates.get( text );
		TextTemplate template = null;
		if ( templateRef != null )
		{
			template = (TextTemplate) templateRef.get( );
			if ( template != null )
			{
				return template;
			}
		}
		try
		{
			template = new org.eclipse.birt.core.template.TemplateParser( )
					.parse( text );
			templateRef = new SoftReference( template );
			templates.put( text, templateRef );
		}
		catch ( Exception ex )
		{
			ex.printStackTrace( );
		}
		return template;
	}

	String executeTemplate( TextTemplate template, HashMap values )
	{
		return new TemplateExecutor( context ).execute( template, values );
	}

	public Object visitList( IListContent list, Object value)
	{
		if ( list.getGenerateBy( ) instanceof ListItemDesign )
		{
			ListScriptExecutor.handleOnRender( list,
					context );
		}
		return list;
	}

	public Object visitTable( ITableContent table, Object value )
	{
		if ( table.getGenerateBy( ) instanceof TableItemDesign )
			TableScriptExecutor.handleOnRender( table, context );
		else if ( table.getGenerateBy( ) instanceof GridItemDesign )
			GridScriptExecutor.handleOnRender( table, context );
		
		String captionText = table.getCaption( );
		String captionKey = table.getCaptionKey( );

		captionText = localize( table, captionKey, captionText );
		table.setCaption( captionText );
		
		return table;
	}

	public Object visitRow( IRowContent row, Object value )
	{
		RowScriptExecutor.handleOnRender( row, context );
		return row;
	}

	public Object visitCell( ICellContent cell, Object value )
	{
		CellScriptExecutor.handleOnRender( cell, context );
		return cell;
	}

	/**
	 * handle the data content.
	 * 
	 * @param data
	 *            data content object
	 */
	public Object visitData( IDataContent data, Object value )
	{
		DataItemScriptExecutor.handleOnRender( data, context );
		processData( data );
		return data;
	}

	/**
	 * process the data content
	 * 
	 * <li> localize the help text
	 * <li> format the value
	 * <li> handle it as it is an text.
	 * 
	 * @param data
	 *            data object
	 */
	protected void processData( IDataContent data )
	{
		String helpText = localize( data, data.getHelpKey( ), data.getHelpText( ) );
		data.setHelpText( helpText );
		String text = ""; //$NON-NLS-1$
		if ( data.getLabelKey( ) != null || data.getLabelText( ) != null )
		{
			text = localize( data, data.getLabelKey( ), data.getLabelText( ) );
		}
		else
		{
			Object value = data.getValue( );
			if ( value != null )
			{
				IStyle style = data.getComputedStyle( );
				if ( value instanceof Number )
				{
					String format = style.getNumberFormat( );
					NumberFormatter fmt = context.getNumberFormatter( format );
					text = fmt.format( (Number) value );
					CSSValue align = style
							.getProperty( IStyle.STYLE_NUMBER_ALIGN );
					if ( align != null && align != CSSValueConstants.NONE_VALUE )
					{
						data.getStyle( ).setProperty( IStyle.STYLE_TEXT_ALIGN,
								align );
					}
				}
				else if ( value instanceof String )
				{
					StringFormatter fmt = context.getStringFormatter( style
							.getStringFormat( ) );
					text = fmt.format( (String) value );

				}
				else if ( value instanceof Date )
				{
					DateFormatter fmt = context.getDateFormatter( style
							.getDateFormat( ) );
					text = fmt.format( (Date) value );
				}
				else
				{
					text = value.toString( );
				}
			}
		}
		//text can be null value after applying format
		if(text!=null)
		{
			data.setText( text );
		}
		else
		{
			data.setText( "" ); //$NON-NLS-1$
		}
			
			

	}

	/**
	 * handle the label.
	 * 
	 * @param label
	 *            label content
	 */
	public Object visitLabel( ILabelContent label, Object value )
	{
		if ( label.getGenerateBy( ) instanceof LabelItemDesign )
			LabelScriptExecutor.handleOnRender( label, context );
		else if ( label.getGenerateBy( ) instanceof TextItemDesign )
			TextItemScriptExecutor.handleOnRender( label, context );
		processLabel( label );
		return label;
	}

	/**
	 * process the label content
	 * 
	 * <li> localize the help text
	 * <li> localize the label content
	 * <li> handle it as it is an text
	 * 
	 * @param label
	 *            label object
	 */
	protected void processLabel( ILabelContent label )
	{
		String helpText = localize( label, label.getHelpKey( ), label.getHelpText( ) );
		label.setHelpText( helpText );

		String text = localize( label, label.getLabelKey( ), label.getLabelText( ) );
		label.setText( text );
	}

	public Object visitText( ITextContent text, Object value )
	{
		TextItemScriptExecutor.handleOnRender( text, context );
		return value;
	}


	/**
	 * handle the foreign content object.
	 * 
	 * Foreign content can be created by following design element:
	 * <li> Text(HTML). It will create a TEMPLATE_TYPE foreign object.
	 * <li> MultiLine(HTML). It will create a HTML_TYPE forign object
	 * <li> MultiLine(PlainText).It will create a TEXT_TYPE foreign object
	 * <li> Extended item. It will create a
	 * TEXT_TYPE/HTML_TYPE/IMAGE_TYPE/VALUE_TYPE foreign object.
	 * 
	 */
	public Object visitForeign( IForeignContent foreignContent, Object value )
	{
		IReportContent reportContent = getReportContent( );
		
		String rawFormat = foreignContent.getRawType( );
		Object rawValue = foreignContent.getRawValue( );

		if ( IForeignContent.TEMPLATE_TYPE.equals( rawFormat ) )
		{
			TextItemScriptExecutor.handleOnRender( foreignContent, context );
			processTemplateContent( foreignContent );
			return foreignContent;
		}
		
		if ( IForeignContent.EXTERNAL_TYPE.equals( rawFormat ) )
		{
			return processExtendedContent( foreignContent );
		}
		
		if ( IForeignContent.IMAGE_TYPE.equals( rawFormat ) )
		{
			if ( rawValue instanceof IImageContent )
			{
				IImageContent image = (IImageContent) rawValue;
				processImage( image );
				return image;
			}
			if ( rawValue instanceof byte[] )
			{
				IImageContent imageContent = reportContent
						.createImageContent( foreignContent );
				imageContent.setImageSource( IImageContent.IMAGE_EXPRESSION );
				imageContent.setData( (byte[]) rawValue );
				processImage( imageContent );
				return imageContent;
			}
		}
		
		if ( IForeignContent.TEXT_TYPE.equals( rawFormat ) )
		{
			TextItemScriptExecutor.handleOnRender( foreignContent, context );
			ITextContent textContent = reportContent
					.createDataContent( foreignContent );
			textContent.setText( rawValue == null ? "" : rawValue.toString( ) ); //$NON-NLS-1$
			return textContent;
		}
		
		if ( IForeignContent.HTML_TYPE.equals( rawFormat ) )
		{
			TextItemScriptExecutor.handleOnRender( foreignContent, context );
			String htmlText = (String)foreignContent.getRawValue( );
			String text = localize( foreignContent, foreignContent.getRawKey( ), htmlText);
			foreignContent.setRawValue(  text );
			return foreignContent;
		}
		
		if ( IForeignContent.VALUE_TYPE.equals( rawFormat ) )
		{
			DynamicTextScriptExecutor.handleOnRender( foreignContent, context );
			IDataContent dataContent = reportContent
					.createDataContent( foreignContent );
			dataContent.setValue( rawValue );
			processData( dataContent );
			return dataContent;
		}
		return foreignContent;
	}

	/**
	 * localzie the text.
	 * 
	 * @param key
	 *            text key
	 * @param text
	 *            default text
	 * @return localized text.
	 */
	private String localize( IContent content, String key, String text )
	{
		assert ( content != null );
		assert ( content.getGenerateBy( ) != null );
		DesignElementHandle element = ( (ReportItemDesign) content
				.getGenerateBy( ) ).getHandle( );
		if ( key != null && element != null )
		{
			String t = ModuleUtil.getExternalizedValue( element, key, text,
					ULocale.forLocale( locale ) );
			if ( t != null )
			{
				return t;
			}
		}
		return text;
	}

	public Object visitImage( IImageContent image, Object value )
	{
		ImageScriptExecutor.handleOnRender( image, context );
		processImage( image );
		return image;
	}

	protected void processImage( IImageContent image )
	{
		String altText = localize( image, image.getAltTextKey( ), image.getAltText( ) );
		image.setAltText( altText );
		String helpText = localize( image, image.getHelpKey( ), image.getHelpText( ) );
		image.setHelpText( helpText );
	}

	/**
	 * handle the template result.
	 * 
	 * @param foreignContent
	 */

	protected void processTemplateContent( IForeignContent foreignContent )
	{
		assert IForeignContent.TEMPLATE_TYPE.equals( foreignContent
				.getRawType( ) );

		if ( foreignContent.getGenerateBy( ) instanceof TextItemDesign )
		{
			TextItemDesign design = (TextItemDesign) foreignContent
					.getGenerateBy( );

			String text = localize( foreignContent, design.getTextKey( ),
					design.getText( ) );
			String textType = design.getTextType( );

			TextTemplate template = parseTemplate( text );

			HashMap values = new HashMap( );
			if ( foreignContent.getRawValue( ) instanceof HashMap )
			{
				values = (HashMap) foreignContent.getRawValue( );
			}

			String result = executeTemplate( template, values );

			String rawType = ForeignContent.getTextRawType( textType, result );
			assert IForeignContent.HTML_TYPE.equals( rawType );

			foreignContent.setRawType( IForeignContent.HTML_TYPE );
			foreignContent.setRawValue( result );
		}
	}

	protected String getOutputFormat()
	{
		return outputFormat;
	}
	
	/**
	 * @return whether the output format is for printing
	 */
	protected boolean isForPrinting( )
	{
		String outputFormat = getOutputFormat( );
		if ( "FO".equalsIgnoreCase( outputFormat )
				|| "PDF".equalsIgnoreCase( outputFormat ) )
			return true;
		return false;
	}

	/**
	 * handle an extended item.
	 * 
	 * @param content
	 *            the object.
	 */
	protected IContent processExtendedContent( IForeignContent content )
	{
		assert IForeignContent.EXTERNAL_TYPE.equals( content.getRawType( ) );
		assert content.getGenerateBy( ) instanceof ExtendedItemDesign;

		IContent generatedContent = content;
		
		ExtendedItemDesign design = (ExtendedItemDesign) content
				.getGenerateBy( );
		ExtendedItemHandle handle = (ExtendedItemHandle) design.getHandle( );
		String tagName = handle.getExtensionName( );

		// call the presentation peer to create the content object
		IReportItemPresentation itemPresentation = ExtensionManager
				.getInstance( ).createPresentationItem( tagName );
		if ( itemPresentation != null )
		{
			itemPresentation.setModelObject( handle );
			itemPresentation.setScriptContext( context.getReportContext( ) );
			IBaseQueryDefinition[] queries = design.getQueries( );
			if ( queries == null )
			{
				if ( design.getQuery( ) != null )
				{
					queries = new IBaseQueryDefinition[]{design.getQuery( )};
				}
			}
			itemPresentation.setReportQueries( queries );
			itemPresentation.setDynamicStyle( content.getComputedStyle( ) );
			Map appContext = context.getAppContext( );
			int resolution = 96;
			if ( isForPrinting( ) )
			{
				resolution = 192;
				if ( appContext != null )
				{
					Object tmp = appContext
							.get( EngineConstants.APPCONTEXT_CHART_PRINT_RESOLUTION );
					if ( tmp != null && tmp instanceof Number )
					{
						resolution = ( (Number) tmp ).intValue( );
						if ( resolution < 96 )
						{
							resolution = 96;
						}
					}
				}
			}

			itemPresentation.setResolution( resolution );
			itemPresentation.setLocale( locale );

			Object renderContext = null;
			if ( appContext != null )
			{
				renderContext = appContext
						.get( EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT );
			}

			// Handle the old-style render context, follow the same code path as
			// before.
			String supportedImageFormats = "PNG;GIF;JPG;BMP;"; //$NON-NLS-1$
			if ( renderContext != null
					&& renderContext instanceof HTMLRenderContext )
			{
				HTMLRenderContext htmlContext = (HTMLRenderContext) renderContext;
				IRenderOption option = context.getRenderOption( );
				htmlContext.SetRenderOption( option );

				String formats = htmlContext.getSupportedImageFormats( );
				if ( formats != null )
					supportedImageFormats = formats;
			}
			itemPresentation.setSupportedImageFormats( supportedImageFormats ); // Default

			itemPresentation.setActionHandler( context.getActionHandler( ) );
			// value
			String outputFormat = getOutputFormat( );
			itemPresentation.setOutputFormat( outputFormat );

			Object rawValue = content.getRawValue( );
			if ( rawValue instanceof byte[] )
			{
				byte[] values = (byte[]) rawValue;
				itemPresentation
						.deserialize( new ByteArrayInputStream( values ) );
			}

			IResultSet parent = context.getResultSet( );
			if (parent != null)
			{
				parent = parent.getParent( );
			}
			IRowSet[] rowSets = executeQueries( parent, design );

			try
			{
				Object output = itemPresentation.onRowSets( rowSets );
				if ( output != null )
				{
					int type = itemPresentation.getOutputType( );
					String imageMIMEType = itemPresentation.getImageMIMEType( );
					generatedContent = processExtendedContent( content, type, output, imageMIMEType );
				}
				itemPresentation.finish( );
			}
			catch ( BirtException ex )
			{
				context.addException( design.getHandle( ), ex );
				logger.log( Level.SEVERE, ex.getMessage( ), ex );
			}

			closeQueries( rowSets );
		}
		return generatedContent;
	}

	/**
	 * handle the content created by the IPresentation
	 * 
	 * @param item
	 *            extended item design
	 * @param emitter
	 *            emitter used to output the contnet
	 * @param content
	 *            ext content
	 * @param type
	 *            output type
	 * @param output
	 *            output
	 */
	protected IContent processExtendedContent( IForeignContent content, int type,
			Object output, String imageMIMEType )
	{
		assert IForeignContent.EXTERNAL_TYPE.equals( content.getRawType( ) );
		assert output != null;

		IReportContent reportContent = getReportContent( );
		
		switch ( type )
		{
			case IReportItemPresentation.OUTPUT_NONE :
				break;
			case IReportItemPresentation.OUTPUT_AS_IMAGE :
			case IReportItemPresentation.OUTPUT_AS_IMAGE_WITH_MAP :
				// the output object is a image, so create a image content
				// object
				Object imageMap = null;
				byte[] imageContent = new byte[0];

				Object image = output;
				if ( type == IReportItemPresentation.OUTPUT_AS_IMAGE_WITH_MAP )
				{
					// OUTPUT_AS_IMAGE_WITH_MAP
					Object[] imageWithMap = (Object[]) output;
					if ( imageWithMap.length > 0 )
					{
						image = imageWithMap[0];
					}
					if ( imageWithMap.length > 1 )
					{
						imageMap = imageWithMap[1];
					}
				}

				if ( image instanceof InputStream )
				{
					imageContent = readContent( (InputStream) image );
				}
				else if ( output instanceof byte[] )
				{
					imageContent = (byte[]) image;
				}
				else
				{
					assert false;
					logger.log( Level.WARNING,
							"unsupported image type:{0}", output ); //$NON-NLS-1$

				}

				IImageContent imageObj = reportContent.createImageContent( content );
				imageObj.setParent( content.getParent( ) );
				// Set image map
				imageObj.setImageSource( IImageContent.IMAGE_EXPRESSION );
				imageObj.setData( imageContent );
				imageObj.setImageMap( imageMap );
				imageObj.setMIMEType( imageMIMEType );
				imageObj.setAltText( content.getAltText( ) );
				imageObj.setAltTextKey( content.getAltTextKey( ) );
				processImage( imageObj );
				return imageObj;

			case IReportItemPresentation.OUTPUT_AS_CUSTOM :

				IDataContent dataObj = reportContent.createDataContent( content );
				dataObj.setValue( output );
				processData( dataObj );
				return dataObj;

			case IReportItemPresentation.OUTPUT_AS_HTML_TEXT :
				content.setRawType( IForeignContent.HTML_TYPE );
				content.setRawValue( output.toString( ) );
				return content;

			case IReportItemPresentation.OUTPUT_AS_TEXT :
				ITextContent textObj = reportContent.createTextContent( );
				textObj.setText( output.toString( ) );
				return textObj;
				
			default :
				assert false;
				logger.log( Level.WARNING, "unsupported output format:{0}", //$NON-NLS-1$
						new Integer( type ) );
		}
		return content;

	}

	/**
	 * read the content of input stream.
	 * 
	 * @param in
	 *            input content
	 * @return content in the stream.
	 */
	static protected byte[] readContent( InputStream in )
	{
		BufferedInputStream bin = in instanceof BufferedInputStream
				? (BufferedInputStream) in
				: new BufferedInputStream( in );
		ByteArrayOutputStream out = new ByteArrayOutputStream( 1024 );
		byte[] buffer = new byte[1024];
		int readSize = 0;
		try
		{
			readSize = bin.read( buffer );
			while ( readSize != -1 )
			{
				out.write( buffer, 0, readSize );
				readSize = bin.read( buffer );
			}
		}
		catch ( IOException ex )
		{
			logger.log( Level.SEVERE, ex.getMessage( ), ex );
		}
		return out.toByteArray( );
	}
	
	protected IRowSet[] executeQueries( IResultSet parent, ExtendedItemDesign extItem )
	{
		IRowSet[] rowSets = null;
		
		IBaseQueryDefinition[] queries = extItem.getQueries( );
		if ( queries != null )
		{
			rowSets = new IRowSet[queries.length];
			for ( int i = 0; i < rowSets.length; i++ )
			{
				try
				{
					IResultSet rset = context.executeQuery( parent, queries[i] );
					assert rset != null;
					rowSets[i] = new RowSet( rset );
				}
				catch ( BirtException ex )
				{
					// TODO: handle exception
					rowSets[i] = null;
				}
			}
		}
		if ( rowSets == null )
		{
			IBaseQueryDefinition query = extItem.getQuery( );
			if ( query != null )
			{
				try
				{
					IResultSet rset = context.executeQuery( parent, query );
					rowSets = new IRowSet[]{ new RowSet( rset ) };
				}
				catch(BirtException ex)
				{
					//TODO: handle birt exception
				}
			}
		}
		return rowSets;
	}

	protected void closeQueries( IRowSet[] rowSets )
	{
		if ( rowSets != null )
		{
			for ( int i = 0; i < rowSets.length; i++ )
			{
				if ( rowSets[i] != null )
				{
					rowSets[i].close( );
				}
			}
		}
	}
}