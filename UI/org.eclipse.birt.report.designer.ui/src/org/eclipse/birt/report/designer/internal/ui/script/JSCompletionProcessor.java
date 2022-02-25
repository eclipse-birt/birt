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

package org.eclipse.birt.report.designer.internal.ui.script;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.script.JSObjectMetaData.JSField;
import org.eclipse.birt.report.designer.internal.ui.script.JSObjectMetaData.JSMethod;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.swt.graphics.Image;

/**
 *
 */

public class JSCompletionProcessor implements IContentAssistProcessor {

	/**
	 * presentation wrapper class for expression script objects.
	 */
	// private static IExpressionProvider provider = new ExpressionProvider( );
	protected JSSyntaxContext context;

	protected String currentWord = ""; //$NON-NLS-1$
	protected String currentExpressionStr = ""; //$NON-NLS-1$
	private JSExpression currentExpression;

	public JSCompletionProcessor(JSSyntaxContext context) {
		super();
		this.context = context;
	}

	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
		// System.out.println( editor.getModel( ) );
		// try
		// {
		// String word = supposeCurrentWord( viewer, offset - 1 );
		// if ( className == null )
		// lastProposalObjects = JSObjectMetaFactory.getAllEnginJSObjects();
		// else if ( className != lastClassName )
		// lastProposalObjects = JSObjectMetaFactory.getEnginJSObject( className
		// );
		//
		// if ( "".equals( word ) ) //$NON-NLS-1$
		// return converToCompletionProposalArray( viewer,
		// offset,
		// lastProposalObjects );
		// else if ( word.startsWith( "row[" ) ) { //$NON-NLS-1$
		// if ( datasetColumns == null )
		// datasetColumns = getAllDatasetColumns( );
		// lastProposalObjects = datasetColumns;
		// if ( "row[".equals( word ) ) //$NON-NLS-1$
		// return converToCompletionProposalArray( viewer,
		// offset,
		// lastProposalObjects );
		// word = word.substring( 4 );
		// }
		//
		// ArrayList relevantProposals = new ArrayList( 10 );
		//
		// for ( int n = 0; n < lastProposalObjects.length; n++ )
		// {
		// if ( getInsertText( lastProposalObjects[n] ).toLowerCase( )
		// .startsWith( word.toLowerCase( ) ) )
		// {
		// CompletionProposal proposal = new CompletionProposal( getInsertText(
		// lastProposalObjects[n] ),
		// offset - word.length( ),
		// word.length( ),
		// getCursorPosition( lastProposalObjects[n] ),
		// provider.getImage( lastProposalObjects[n] ),
		// getDisplayName( lastProposalObjects[n] ),
		// null,
		// null );
		// relevantProposals.add( proposal );
		// }
		// }
		//
		// if ( relevantProposals.size( ) > 0 )
		// {
		// return (ICompletionProposal[]) relevantProposals.toArray( new
		// ICompletionProposal[relevantProposals.size( )] );
		// }
		//
		// return emptyProposals;
		// }
		// catch ( BadLocationException e )
		// {
		// }
		this.currentWord = null;
		try {
			String expression = supposeCurrentExpression(viewer.getDocument(), viewer.getTopIndexStartOffset(), offset);
			// if ( currentExpression == null
			// || !expression.equals( this.currentExpressionStr ) )
			// {
			// can not cache last expression, because context may be
			// changed.
			this.currentExpressionStr = expression;
			this.currentExpression = createJSExpression();
			// }
			return getCompletionProposals(currentExpression.getReturnType(), offset);
		} catch (BadLocationException e) {
			// ignore
		}
		return null;
	}

	protected JSExpression createJSExpression() {
		return new JSExpression(context, currentExpressionStr);
	}

	/**
	 * Get the JS expression in current position, split the last not complete code
	 * fragment.
	 *
	 * @param document
	 * @param topOffset Document top offset.
	 * @param offset    Current offset.
	 * @return
	 * @throws BadLocationException
	 */
	private String supposeCurrentExpression(IDocument document, int topOffset, int offset) throws BadLocationException {
		if (offset < 0) {
			offset = 0;
		}

		int startOffset = offset, endOffset = offset;
		char currentChar;
		int bracket = 0;
		while (startOffset > topOffset) {
			startOffset--;
			currentChar = document.getChar(startOffset);
			if (currentWord == null && currentChar == '.') {
				// if behind char is '.', ignore.
				while ((currentChar = document.getChar(--startOffset)) == '.') {
					;
				}
				// else reset start offset.
				currentChar = document.getChar(++startOffset);
				currentWord = document.get(startOffset + 1, endOffset - startOffset - 1);// ignore '.', because
																							// replacement don't have'.'
				endOffset = startOffset + 1;// inculde '.' for expression parse
				// use
			}
			if (currentChar == ')' || currentChar == ']') {
				++bracket;
			}
			if (currentChar == '(' || currentChar == '[') {
				--bracket;
			}
			if (bracket == 0 && (currentChar == '\n' || currentChar == ' ' || currentChar == '=' || currentChar == '+'
					|| currentChar == '-' || currentChar == '*' || currentChar == '/' || currentChar == '<'
					|| currentChar == '>' || currentChar == '&' || currentChar == '|' || currentChar == ';')) {
				startOffset++;
				break;
			}
		}
		if (currentWord == null) {
			return currentWord = document.get(startOffset, endOffset - startOffset);
		}
		return document.get(startOffset, endOffset - startOffset);
	}

	private ICompletionProposal[] getCompletionProposals(Object meta, int offset) {
		if (meta instanceof JSObjectMetaData) {
			return getCompletionProposals((JSObjectMetaData) meta, offset);
		} else if (meta instanceof JSObjectMetaData[]) {
			return getCompletionProposals((JSObjectMetaData[]) meta, offset);
		}
		return null;
	}

	protected CompletionProposal[] getCompletionProposals(JSObjectMetaData[] metas, int offset) {
		List<CompletionProposal> proposals = new ArrayList<>();
		int wordLength = currentWord == null ? 0 : currentWord.length();
		for (int i = 0; i < metas.length; i++) {
			if (currentWord == null || currentWord.equals("") //$NON-NLS-1$
					|| metas[i].getName().toLowerCase().startsWith(currentWord.toLowerCase())) {
				proposals.add(new CompletionProposal(metas[i].getName(), offset - wordLength, wordLength,
						metas[i].getName().length(), null, metas[i].getName(), null, metas[i].getDescription()));
			}
		}
		return proposals.toArray(new CompletionProposal[proposals.size()]);
	}

	protected CompletionProposal[] getCompletionProposals(JSObjectMetaData meta, int offset) {
		List<CompletionProposal> proposals = new ArrayList<>();
		int wordLength = currentWord == null ? 0 : currentWord.length();

		JSField[] members = meta.getFields();
		if (members != null) {
			for (int i = 0; i < members.length; i++) {
				if (currentWord == null || currentWord.equals("") //$NON-NLS-1$
						|| members[i].getName().toLowerCase().startsWith(currentWord.toLowerCase())) {
					proposals.add(new CompletionProposal(members[i].getName(), offset - wordLength, wordLength,
							members[i].getName().length(), getMemberImage(members[i].getVisibility()),
							members[i].getDisplayText(), null, members[i].getDescription()));
				}
			}
		}

		JSMethod[] methods = meta.getMethods();
		if (methods != null) {
			for (int i = 0; i < methods.length; i++) {
				if (currentWord == null || currentWord.equals("") //$NON-NLS-1$
						|| methods[i].getName().toLowerCase().startsWith(currentWord.toLowerCase())) {
					JSObjectMetaData[] args = methods[i].getArguments();

					boolean hasArg = args != null && args.length > 0;

					proposals.add(new CompletionProposal("." //$NON-NLS-1$
							+ methods[i].getName() + "()", //$NON-NLS-1$
							offset - wordLength - 1, wordLength + 1, methods[i].getName().length() + (hasArg ? 2 : 3),
							getMethodImage(methods[i].getVisibility()), methods[i].getDisplayText(), null,
							methods[i].getDescription()));
				}
			}
		}
		return proposals.toArray(new CompletionProposal[proposals.size()]);
	}

	protected Image getMemberImage(int visibility) {
		switch (visibility) {
		case JSObjectMetaData.VISIBILITY_PUBLIC:
			return ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_EXPRESSION_MEMBER);
		case JSObjectMetaData.VISIBILITY_PROTECTED:
			return ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_EXPRESSION_MEMBER);
		case JSObjectMetaData.VISIBILITY_PRIVATE:
			return ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_EXPRESSION_MEMBER);
		case JSObjectMetaData.VISIBILITY_STATIC:
			return ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_EXPRESSION_STATIC_MEMBER);
		default:
			break;
		}
		return null;
	}

	protected Image getMethodImage(int visibility) {
		switch (visibility) {
		case JSObjectMetaData.VISIBILITY_PUBLIC:
			return ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_EXPRESSION_METHOD);
		case JSObjectMetaData.VISIBILITY_PROTECTED:
			return ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_EXPRESSION_METHOD);
		case JSObjectMetaData.VISIBILITY_PRIVATE:
			return ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_EXPRESSION_METHOD);
		case JSObjectMetaData.VISIBILITY_STATIC:
			return ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_EXPRESSION_STATIC_MEMBER);
		default:
			break;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#
	 * computeContextInformation(org.eclipse.jface.text.ITextViewer, int)
	 */
	@Override
	public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#
	 * getCompletionProposalAutoActivationCharacters()
	 */
	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return new char[] { '.', '[' };
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#
	 * getContextInformationAutoActivationCharacters()
	 */
	@Override
	public char[] getContextInformationAutoActivationCharacters() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.text.contentassist.IContentAssistProcessor#getErrorMessage
	 * ()
	 */
	@Override
	public String getErrorMessage() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#
	 * getContextInformationValidator()
	 */
	@Override
	public IContextInformationValidator getContextInformationValidator() {
		return null;
	}
}
