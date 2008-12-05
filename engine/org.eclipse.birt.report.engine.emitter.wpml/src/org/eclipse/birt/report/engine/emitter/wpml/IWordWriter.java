/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.wpml;

import java.io.IOException;

import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.emitter.wpml.AbstractEmitterImpl.InlineFlag;
import org.eclipse.birt.report.engine.emitter.wpml.AbstractEmitterImpl.TextFlag;

public interface IWordWriter
{

	void start( boolean rtl, String creator, String title, String description )
			throws IOException;

	void drawDocumentBackground( String backgroundColor,
			String backgroundImageUrl ) throws IOException;

	void end( ) throws IOException;

	void startSectionInParagraph( );

	void endSectionInParagraph( );

	void startSection( );

	void endSection( );

	void writePageProperties( int pageHeight, int pageWidth, int headerHeight,
			int footerHeight, int topMargin, int bottomMargin, int leftMargin,
			int rightMargin, String orient );

	void startTable( IStyle style, int tableWidth );

	void endTable( );

	void writeColumn( int[] cols );

	void startTableRow( double height, boolean isHeader, boolean repeatHeader );

	void startTableRow( double height );

	void endTableRow( );

	void startTableCell( int width, IStyle style, SpanInfo info );

	void endTableCell( boolean needEmptyP );

	void writeSpanCell( SpanInfo info );

	void writeEmptyCell( );

	void writeTOC( String toc, int tocLevel );

	void insertHiddenParagraph( );

	void endParagraph( );

	void writeCaption( String txt );

	void writeBookmark( String bm );

	void drawImage( byte[] data, double height, double width,
			HyperlinkInfo hyper, IStyle style, InlineFlag inlineFlag,
			String altText, String uri );

	void startHeader( boolean showHeaderOnFirst ) throws IOException;

	void endHeader( );

	void startFooter( ) throws IOException;

	void endFooter( );

	void writeForeign( IForeignContent foreignContent );

	void writeContent( int type, String txt, IStyle style, IStyle inlineStyle,
			String fontFamily, HyperlinkInfo info, InlineFlag inlineFlag,
			TextFlag flag );

	void startPage( );

	void endPage( );
}
