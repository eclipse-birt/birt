/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.pdf;

import java.text.Bidi;
import java.util.HashSet;
import java.util.logging.Level;

import org.eclipse.birt.report.engine.content.Dimension;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.css.dom.AreaStyle;
import org.eclipse.birt.report.engine.css.dom.ComputedStyle;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;
import org.eclipse.birt.report.engine.layout.ITextLayoutManager;
import org.eclipse.birt.report.engine.layout.PDFConstants;
import org.eclipse.birt.report.engine.layout.area.IArea;
import org.eclipse.birt.report.engine.layout.area.impl.AbstractArea;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;
import org.eclipse.birt.report.engine.layout.pdf.font.FontInfo;
import org.eclipse.birt.report.engine.layout.pdf.hyphen.DefaultHyphenationManager;
import org.eclipse.birt.report.engine.layout.pdf.hyphen.DefaultWordRecognizer;
import org.eclipse.birt.report.engine.layout.pdf.hyphen.Hyphenation;
import org.eclipse.birt.report.engine.layout.pdf.hyphen.IHyphenationManager;
import org.eclipse.birt.report.engine.layout.pdf.hyphen.IWordRecognizer;
import org.eclipse.birt.report.engine.layout.pdf.hyphen.Word;
import org.eclipse.birt.report.engine.layout.pdf.text.Chunk;
import org.eclipse.birt.report.engine.layout.pdf.text.ChunkGenerator;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;

import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;

/**
 * 
 * This layout mananger implements formatting and locating of text chunk.
 * <p>
 * A text chunk can contain hard line break(such as "\n", "\n\r"). This layout
 * manager splits a text content to many text chunk due to different actual
 * font, soft line break etc.
 */
public class PDFTextLM extends PDFLeafItemLM implements ITextLayoutManager
{

	private PDFLineAreaLM lineLM;

	/**
	 * Checks if the compositor needs to pause.
	 */
	private boolean pause = false;

	private Compositor comp = null;

	private ITextContent textContent = null;
	
	public PDFTextLM( PDFLayoutEngineContext context, PDFStackingLM parent,
			IContent content, IReportItemExecutor executor )
	{
		super( context, parent, content, executor );
		lineLM = (PDFLineAreaLM) parent;

		ITextContent textContent = (ITextContent) content;
		String text = textContent.getText( );
		if ( text != null && text.length( ) != 0 )
		{
			transform( textContent );
			this.textContent = textContent;
			comp = new Compositor();
		}
	}

	protected boolean layoutChildren( )
	{
		if ( null == textContent )
			return false;
		pause = false;
		return comp.compose( );
	}

	public void addSpaceHolder( IArea con )
	{
		lineLM.addArea( con, false, false );
	}

	public boolean needPause( )
	{
		return this.pause;
	}

	public void addTextLine( IArea textLine )
	{
		lineLM.addArea( textLine, false, false );
	}

	public void newLine( )
	{
		if ( lineLM.endLine( ) )
			pause = false;
		else
			pause = true;
	}

	public int getFreeSpace( )
	{
		return lineLM.getCurrentMaxContentWidth( );
	}

	public void transform( ITextContent textContent )
	{
		String transformType = textContent.getComputedStyle( )
				.getTextTransform( );
		if ( transformType.equalsIgnoreCase( "uppercase" ) ) //$NON-NLS-1$
		{
			textContent.setText( textContent.getText( ).toUpperCase( ) );
		}
		else if ( transformType.equalsIgnoreCase( "lowercase" ) ) //$NON-NLS-1$
		{
			textContent.setText( textContent.getText( ).toLowerCase( ) );
		}
		else if ( transformType.equalsIgnoreCase( "capitalize" ) ) //$NON-NLS-1$
		{
			textContent.setText( capitalize( textContent.getText( ) ) );
		}
		
		ArabicShaping shaping = new ArabicShaping(ArabicShaping.LETTERS_SHAPE);
		try
		{
			String shapingText =  shaping.shape( textContent.getText( ));
			textContent.setText(shapingText);
		}
		catch ( ArabicShapingException e )
		{
			logger.log( Level.WARNING, e.getMessage( ), e );
		}
	}

	private String capitalize( String text )
	{
		HashSet splitChar = new HashSet( );
		splitChar.add( new Character( ' ' ) );
		splitChar.add( new Character( (char) 0x0A ) );
		char[] array = text.toCharArray( );
		int index = 0;
		while ( index < array.length )
		{
			Character c = new Character( text.charAt( index ) );
			while ( splitChar.contains( c ) )
			{
				index++;
				if ( index == array.length )
					return new String( array );
				c = new Character( text.charAt( index ) );
			}
			array[index] = Character.toUpperCase( array[index] );
			while ( !splitChar.contains( c ) )
			{
				index++;
				if ( index == array.length )
					break;
				c = new Character( text.charAt( index ) );
			}
		}
		return new String( array );
	}
	
	
	private class Compositor
	{	
		private ChunkGenerator cg = null;
		private Chunk chunk = null;
		private ITextContent content;
		private boolean isInline;
		private boolean isNew = true;
		
		private int leftSpaceHolder = 0; 
		private int rightSpaceHolder = 0;
		
		/** 
		 * The vestige is the word which can not be added into last line,
		 * or the remain clip after hyphenation.
		 * vestigeIndex saves the position of the vestige relative to 
		 * the text in chunk. 
		 */
		private int vestigeIndex = -1;
		private int vestigeLength = 0;
		
		private int currentPos = 0;
		private int areaStartPos = 0;
		
		private int letterSpacing = 0;
		private int wordSpacing = 0;
		
		private int maxLineSpace = 0;
		private IWordRecognizer wr = null;
		
		/** 
		 * The flag to indicate whether the current TextArea needs to be added
		 * into the line by force. 
		 */ 
		private boolean addByForce = false;
		
		/**
		 * The flag to indicate whether we need to split off the first character next time. 
		 */
		private boolean nothingSplitted = false;
		
		private int leftMargin;
		private int leftBorder;
		private int leftPadding;
		private int rightMargin;
		private int rightBorder; 
		private int rightPadding;
		private int topBorder;
		private int topPadding;
		private int bottomBorder;
		private int bottomPadding;
		
		public Compositor()
		{
			this.content = textContent;
			cg = new ChunkGenerator(content);
			this.isInline = PropertyUtil.isInlineElement(content);
			this.maxLineSpace = lineLM.maxAvaWidth;		
			IStyle style = content.getComputedStyle();
			letterSpacing = getDimensionValue(style
					.getProperty(StyleConstants.STYLE_LETTER_SPACING));
			wordSpacing = getDimensionValue(style
					.getProperty(StyleConstants.STYLE_WORD_SPACING));
			
			IStyle boxStyle = new AreaStyle((ComputedStyle)style);
			validateBoxProperty(boxStyle, maxLineSpace, context.getMaxHeight( ));
			leftMargin = getDimensionValue(boxStyle.getProperty(StyleConstants.STYLE_MARGIN_LEFT));
			leftBorder = getDimensionValue(boxStyle.getProperty(StyleConstants.STYLE_BORDER_LEFT_WIDTH));
			leftPadding = getDimensionValue(boxStyle.getProperty(StyleConstants.STYLE_PADDING_LEFT));
			rightMargin = getDimensionValue(boxStyle.getProperty(StyleConstants.STYLE_MARGIN_RIGHT));
			rightBorder = getDimensionValue(boxStyle.getProperty(StyleConstants.STYLE_BORDER_RIGHT_WIDTH));
			rightPadding = getDimensionValue(boxStyle.getProperty(StyleConstants.STYLE_PADDING_RIGHT));
			topBorder = getDimensionValue(boxStyle.getProperty(StyleConstants.STYLE_BORDER_TOP_WIDTH));
			topPadding = getDimensionValue(boxStyle.getProperty(StyleConstants.STYLE_PADDING_TOP));
			bottomBorder = getDimensionValue(boxStyle.getProperty(StyleConstants.STYLE_BORDER_BOTTOM_WIDTH));
			bottomPadding  = getDimensionValue(boxStyle.getProperty(StyleConstants.STYLE_PADDING_BOTTOM));
		}
		
		public boolean compose()
		{
			while ( hasMore() )
			{
				handleNext();
				if(PDFTextLM.this.needPause())
				{
					return true;
				}
			}
			return false;
		}

		private boolean hasMore()
		{
			if (cg.hasMore())
				return true;
			else if ( null == chunk )
				return false;
			else
				if (currentPos < chunk.getText().length())
					return true;
				else
				{
					if (isInline)
					{
						ContainerArea con = (ContainerArea)createInlineContainer(content, false, true);
						con.setWidth(rightBorder+rightPadding);
						if (null == chunk.getFontInfo())
						{
							IStyle style = content.getComputedStyle();
							con.setHeight( getDimensionValue(style.getProperty(StyleConstants.STYLE_FONT_SIZE))
									+ topBorder + topPadding + bottomBorder + bottomPadding);
						}else
						{
							con.setHeight( (int)(chunk.getFontInfo().getWordHeight()*PDFConstants.LAYOUT_TO_PDF_RATIO)
									+ topBorder + topPadding + bottomBorder + bottomPadding);
						}
						PDFTextLM.this.addSpaceHolder(con);
					}
					return false;
				}
		}
		
		private void handleNext()
		{
			int freeSpace = PDFTextLM.this.getFreeSpace();
			// current chunk is over, get the next one.
			if ( isNew || currentPos == chunk.getText().length() )
			{
				if (cg.hasMore())
				{
					chunk = cg.getNext();
					if (chunk == Chunk.HARD_LINE_BREAK)
					{
						currentPos = chunk.getText().length();
						PDFTextLM.this.newLine();
						vestigeIndex = -1;
						return;
					}
					currentPos = 0;
					this.wr = new DefaultWordRecognizer(chunk.getText());
				}
				else
				{
					return;
				}
			}
			if (isNew)
			{
				isNew = false;
				if (isInline)
				{
					AbstractArea con = (AbstractArea)createInlineContainer(content, true, false);
					con.setWidth(leftBorder+leftPadding);
					con.setHeight( (int)(chunk.getFontInfo().getWordHeight()*PDFConstants.LAYOUT_TO_PDF_RATIO)
							+ topBorder + topPadding + bottomBorder + bottomPadding);
					PDFTextLM.this.addSpaceHolder(con);
					leftSpaceHolder = leftMargin + leftBorder + leftPadding;
					freeSpace -= leftSpaceHolder;
				}
			}
			
			String str = null;
			Word currentWord = null;

			if (-1 == vestigeIndex)
			{
				currentWord = wr.getNextWord();
				// The first word of the chunk is empty, so it means this chunk is a blank one. 
				if (null == currentWord)
				{
					Dimension d = new Dimension( 0, 
							(int)(chunk.getFontInfo().getWordHeight() * PDFConstants.LAYOUT_TO_PDF_RATIO ));
					IArea builtArea = buildArea("", content,  //$NON-NLS-1$
							chunk.getFontInfo(), d);
					PDFTextLM.this.addTextLine(builtArea);
					return;
				}
				str = currentWord.getValue();
				areaStartPos = chunk.getOffset() + currentWord.getStart();	
			}
			else
			// This is a vestige.
			{
				str = chunk.getText().substring(vestigeIndex, vestigeIndex+vestigeLength);
				areaStartPos = chunk.getOffset() + vestigeIndex;
			}
				
			int prevAreaWidth = 0;
			int areaWidth = (int)(chunk.getFontInfo().getWordWidth(
					chunk.getText().substring(currentPos, currentPos+str.length()))
					* PDFConstants.LAYOUT_TO_PDF_RATIO)
					+ letterSpacing * str.length() + wordSpacing;
				
			// holds space for inline text to draw the right border, padding etc.
			if (isInline)
			{
				if (isAtLast(chunk.getOffset() + currentPos + str.length()))
				{
					rightSpaceHolder = rightMargin + rightBorder + rightPadding;
					freeSpace -= rightSpaceHolder;
				}
			}
					
				
			while ( freeSpace >= areaWidth )
			{
				currentPos += str.length();
				currentWord = wr.getNextWord();
				if (null == currentWord)
				{
					str = null;
					break;
				}
				str = currentWord.getValue();
				prevAreaWidth = areaWidth;
				areaWidth += (int)(chunk.getFontInfo().getWordWidth(
						chunk.getText().substring(currentPos, currentPos+str.length()))
						* PDFConstants.LAYOUT_TO_PDF_RATIO)
						+ letterSpacing * str.length() + wordSpacing;
					
				// holds space for inline text to draw the border, padding etc.
				if (isAtLast(chunk.getOffset() + currentPos + str.length())) 
				{
					rightSpaceHolder = rightMargin + rightBorder + rightPadding;
					freeSpace -= rightSpaceHolder;
				}
			}
			
			//the chunk ends, build the TextArea.
			int length = chunk.getText().length();
			if (currentPos == length )
			{
				Dimension d = new Dimension( areaWidth, 
						(int)(chunk.getFontInfo().getWordHeight() * PDFConstants.LAYOUT_TO_PDF_RATIO ));
					
				String originalText = chunk.getText().substring(areaStartPos - chunk.getOffset(), 
						chunk.getText().length());
				
				IArea builtArea = buildArea(getReverseText(originalText), content, 
						chunk.getFontInfo(), d);
				PDFTextLM.this.addTextLine(builtArea);
				vestigeIndex = -1;
				vestigeLength = 0;
				return;
			}

			if( maxLineSpace < chunk.getFontInfo().getWordWidth(str)* PDFConstants.LAYOUT_TO_PDF_RATIO 
					+ letterSpacing * str.length()+ wordSpacing )
			{
				if ( 0 == str.length() )
				{
					vestigeIndex = -1;
					vestigeLength = 0;
					return;
				}
				// does hyphenation.
				IHyphenationManager hm = new DefaultHyphenationManager();
				Hyphenation hyph = hm.getHyphenation(str);
				
				int endHyphenIndex = hyphen( 0, freeSpace-prevAreaWidth, hyph, chunk.getFontInfo() );
				// forces to add the first character if the hyphen index is 0 for the second time.
				if (endHyphenIndex == 0)
				{
					if (nothingSplitted)
					{
						str = hyph.getHyphenText( 0, endHyphenIndex + 1 );
						addByForce = true;
						nothingSplitted = false;
					}
					else
					{
						nothingSplitted = true;
						vestigeIndex = currentPos;
						vestigeLength = (null == currentWord) ? vestigeLength : currentWord.getLength();
						return;
					}
				}
				else
				{
					str = hyph.getHyphenText( 0, endHyphenIndex );	
				}
				
				//int startHyphenIndex = (null == currentWord) ? vestigeIndex : currentWord.getStart();
				currentPos += str.length();
				vestigeIndex = currentPos;
				vestigeLength = (null == currentWord) ? vestigeLength - str.length() : currentWord.getLength() - str.length();
				
				Dimension d = null;
				if ( addByForce )
				{
					d = new Dimension( freeSpace,
							(int)(chunk.getFontInfo().getWordHeight() * PDFConstants.LAYOUT_TO_PDF_RATIO ));
					addByForce = false;
				}
				else
				{
					d = new Dimension( prevAreaWidth 
							+ (int)(chunk.getFontInfo().getWordWidth(str) * PDFConstants.LAYOUT_TO_PDF_RATIO) 
							+letterSpacing*str.length(),
							(int)(chunk.getFontInfo().getWordHeight() * PDFConstants.LAYOUT_TO_PDF_RATIO ));	
				}
				
				String originalText = chunk.getText().substring(areaStartPos - chunk.getOffset(),
						vestigeIndex);
				
				IArea builtArea = buildArea(getReverseText(originalText), content, 
						 chunk.getFontInfo(), d);
				PDFTextLM.this.addTextLine(builtArea);
				PDFTextLM.this.newLine();
				return;
			}
			else
			{
				// builds the text area and ends current line.
				Dimension d = new Dimension( prevAreaWidth,
						(int)(chunk.getFontInfo().getWordHeight() * PDFConstants.LAYOUT_TO_PDF_RATIO));	
				
				String originalText = chunk.getText().substring(areaStartPos - chunk.getOffset(),
						currentPos);
				
				IArea builtArea = buildArea(getReverseText(originalText), content, chunk.getFontInfo(), d);
				PDFTextLM.this.addTextLine(builtArea);
				PDFTextLM.this.newLine();
				vestigeIndex = (null == currentWord) ? -1 : currentWord.getStart();
				vestigeLength = (null == currentWord) ? 0 : currentWord.getLength();
				return;
			}

		}
		
		/**
		 * build areas by specified properties for text content. the return area
		 * should be an container area which contains a text chunk or only a text
		 * chunk.
		 * <p>
		 * <ul>
		 * <li>For inline text, the return value should be a container area. The
		 * container area inherit border and margin from text content. The position
		 * of the container area in its container is decided by the margin value of
		 * text content.
		 * <li>For block text, the return value should be a text chunk. The
		 * position of text chunk in its container is decided by the padding value
		 * of text content.
		 * </ul>
		 * 
		 * @param content				the TextContent which the TextArea shares the style with.
		 * @param startOffset			the start offset of the text in the TextArea relative to content.
		 * @param endOffset      		the end offset of the text in the TextArea relative to content.
		 * @param fi					the FontInfo of the text in the TextArea.
		 *   
		 * @return						the built TextArea.
		 */
		private IArea buildArea(String text, ITextContent content, FontInfo fi, Dimension dimension) 
		{        
			if ( isInline )
			{
				return createInlineTextArea( text, content, fi, dimension );
			}
			else
			{
				return createBlockTextArea( text, content, fi, dimension );
			}
		}
		
		/**
		 * Gets the hyphenation index
		 * 
		 * @param startIndex			    the start index
		 * @param width						the width of the free space 
		 * @param hyphenation				the hyphenation
		 * @param fi						the FontInfo object of the text to be hyphened.
		 * @return 							the hyphenation index
		 */
		private int hyphen(int startIndex, int width,
				Hyphenation hyphenation, FontInfo fi) 
		{
			assert (startIndex >= 0);
			if (startIndex > hyphenation.length() - 1) 
			{
				return -1;
			}
			int last = 0;
			int current = 0;
			for (int i = startIndex + 1; i < hyphenation.length(); i++) 
			{
				last = current;
				String pre = hyphenation.getHyphenText(startIndex, i);
				current = (int)(fi.getWordWidth(pre) * PDFConstants.LAYOUT_TO_PDF_RATIO) 
					+letterSpacing * pre.length();
				if (width > last && width <= current) 
				{
					return i-1;
				}
			}
			return hyphenation.length() - 1;
		}
		
		/**
		 * Gets the reverse text if the run direction is RtL,
		 * If the run direction is LtR, the text keeps the same.
		 * @param text						the original text.
		 * @return							the reverse text.
		 */
		private String getReverseText(String text)
		{
			if (chunk.getRunDirection() == Bidi.DIRECTION_LEFT_TO_RIGHT)
			{
				return text;
			}
			else
			{
				return flip(text);
			}
		}
		
		/**
		 * Reverse text
		 * @param text
		 * @return
		 */
	    private String flip(String text) 
	    {
	    	char[] indexChars = text.toCharArray();
	    	int start = 0;
	    	int end = indexChars.length;
	        int mid = (start + end) / 2;
	        --end;
	        for (; start < mid; ++start, --end) 
	        {
	            char temp = indexChars[start];
	            indexChars[start] = indexChars[end];
	            indexChars[end] = temp;
	        }
	        return new String(indexChars);
	    }
		
		private boolean isAtLast(int index)
		{
			return index >= content.getText().length();
		}
		
		/**
		 * create inline text area by text content
		 * @param content the text content
		 * @param text the text string
		 * @param contentDimension the content dimension
		 * @param isFirst if this area is the first area of the content
		 * @param isLast if this area is the last area of the content
		 * @return
		 */
		private IArea createInlineTextArea(String text, ITextContent content, FontInfo fi, Dimension contentDimension)
		{
			ContainerArea con = (ContainerArea)createInlineContainer(content, false, false);
			int textHeight = contentDimension.getHeight();
			int textWidth =  contentDimension.getWidth();
			con.setWidth(Math.min( textWidth, context.getMaxWidth( ))); 
			con.setHeight( Math.min( textHeight + topPadding + topBorder + bottomPadding
					+ bottomBorder, context.getMaxHeight( )) );
			
			AbstractArea textArea = (AbstractArea) AreaFactory.createTextArea(
					content, text, fi );
			con.addChild( textArea );
			textArea.setHeight( textHeight );
			textArea.setWidth( textWidth );
			textArea.setPosition( 0, topPadding + topBorder );
			return con;
		}
		
		
			
	}


}
