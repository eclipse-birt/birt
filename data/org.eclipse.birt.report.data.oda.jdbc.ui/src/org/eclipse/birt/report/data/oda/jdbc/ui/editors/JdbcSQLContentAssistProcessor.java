/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.data.oda.jdbc.ui.editors;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import org.eclipse.birt.report.data.oda.jdbc.ui.util.Column;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.ConnectionMetaData;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.ConnectionMetaDataManager;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.Constants;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.Schema;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.Table;
import org.eclipse.birt.report.data.oda.jdbc.utils.ISQLSyntax;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.design.DataSourceDesign;
import org.eclipse.datatools.connectivity.oda.design.ui.designsession.DesignSessionUtil;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

/**
 * This is the content assistant for the sql editor. It provides the list of
 * parameters when the user types the ? keyword. It also shows a list of
 * available columns or tables depending on whether the user has entered a table
 * or a schema before the (.) dot keyword.
 * 
 * If both a schema and a table have the same name the results are
 * unpredictable.
 * 
 * @version $Revision: 1.18 $ $Date: 2009/07/07 06:50:16 $
 */

public class JdbcSQLContentAssistProcessor implements IContentAssistProcessor, ISQLSyntax {

	private transient ConnectionMetaData metaData = null;
	private transient ICompletionProposal[] lastProposals = null;
	private long timeout; // milliseconds

	/**
	 *  
	 */
	public JdbcSQLContentAssistProcessor(long milliseconds) {
		super();
		this.timeout = milliseconds;
	}

	public void setDataSourceHandle(DataSourceDesign dataSourceHandle) {
		if (metaData != null) {
			metaData.clearCache();
			metaData = null;
		}
		String driverClass = dataSourceHandle.getPublicProperties().findProperty(Constants.ODADriverClass).getValue();
		String url = dataSourceHandle.getPublicProperties().findProperty(Constants.ODAURL).getValue();
		String user = dataSourceHandle.getPublicProperties().findProperty(Constants.ODAUser).getValue();
		String password = dataSourceHandle.getPublicProperties().findProperty(Constants.ODAPassword).getValue();

		metaData = ConnectionMetaDataManager.getInstance().getMetaData(driverClass, url, user, password, // $NON-NLS-1$
				getConnectionProperties(dataSourceHandle), timeout);

	}

	private Properties getConnectionProperties(DataSourceDesign dataSourceDesign) {
		try {
			return DesignSessionUtil.getEffectiveDataSourceProperties(dataSourceDesign);
		} catch (OdaException ignore) {
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#
	 * computeCompletionProposals(org.eclipse.jface.text.ITextViewer, int)
	 */
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
		try {
			if (offset > viewer.getTopIndexStartOffset()) {
				// Check the character before the offset
				char ch = viewer.getDocument().getChar(offset - 1);

				if (ch == '.') // $NON-NLS-1$
				{
					lastProposals = getTableOrColumnCompletionProposals(viewer, offset);
					return lastProposals;
				} else {
					return getRelevantProposals(viewer, offset);
				}
			}
		} catch (BadLocationException e) {
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#
	 * computeContextInformation(org.eclipse.jface.text.ITextViewer, int)
	 */
	public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#
	 * getCompletionProposalAutoActivationCharacters()
	 */
	public char[] getCompletionProposalAutoActivationCharacters() {
		return new char[] { '.' }; // $NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#
	 * getContextInformationAutoActivationCharacters()
	 */
	public char[] getContextInformationAutoActivationCharacters() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.text.contentassist.IContentAssistProcessor#getErrorMessage(
	 * )
	 */
	public String getErrorMessage() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#
	 * getContextInformationValidator()
	 */
	public IContextInformationValidator getContextInformationValidator() {
		return null;
	}

	/**
	 * @param viewer
	 * @param offset
	 * @return
	 */
	private ICompletionProposal[] getTableOrColumnCompletionProposals(ITextViewer viewer, int offset) {
		if (offset > viewer.getTopIndexStartOffset() + 2) {
			try {
				// Get the word before the dot
				// This can either be the table name or the schema name
				String tableName = stripQuotes(findWord(viewer, offset - 2));
				String schemaName = null;

				// Check the character before this word
				int startOffset = offset - tableName.length() - 2;
				if (startOffset > viewer.getTopIndexStartOffset()) {
					// If this is a dot then find the schama name
					if (viewer.getDocument().getChar(startOffset) == '.')// $NON-NLS-1$
					{
						schemaName = findWord(viewer, startOffset - 1);
					}
				}

				if (schemaName == null) {
					// If the schema name is null
					// then the table name can either be a schema or a table
					// First check whether it is a schema
					Schema schema = metaData.getSchema(tableName);
					// If this is not null then just return all the tables from
					// it.
					if (schema != null) {
						return convertTablesToCompletionProposals(schema.getTables(), offset);
					} else {
						// Find the first table match in all the schemas and
						// return the columns
						ArrayList schemas = metaData.getSchemas();
						Iterator iter = schemas.iterator();
						while (iter.hasNext()) {
							schema = (Schema) iter.next();
							Table table = schema.getTable(tableName);
							if (table != null) {
								return convertColumnsToCompletionProposals(table.getColumns(), offset);
							}
						}
					}
				} else {
					schemaName = stripQuotes(schemaName);
					// We have both the schema and table name
					// return the column names
					Schema schema = metaData.getSchema(schemaName);
					if (schema != null) {
						Table table = schema.getTable(tableName);
						if (table != null) {
							return convertColumnsToCompletionProposals(table.getColumns(), offset);
						}
					}
				}
			} catch (BadLocationException e) {
			} catch (SQLException e) {
			}
		}
		return null;
	}

	private ICompletionProposal[] getRelevantProposals(ITextViewer viewer, int offset) throws BadLocationException {
		if (lastProposals != null) {
			ArrayList relevantProposals = new ArrayList(10);

			String word = (findWord(viewer, offset - 1)).toLowerCase();
			// Search for this word in the list

			for (int n = 0; n < lastProposals.length; n++) {
				if (stripQuotes(lastProposals[n].getDisplayString().toLowerCase()).startsWith(word)) {
					CompletionProposal proposal = new CompletionProposal(lastProposals[n].getDisplayString(),
							offset - word.length(), word.length(), lastProposals[n].getDisplayString().length());
					relevantProposals.add(proposal);
				}
			}

			if (relevantProposals.size() > 0) {
				return (ICompletionProposal[]) relevantProposals.toArray(new ICompletionProposal[] {});
			}
		}

		return null;
	}

	/**
	 * @param columns
	 * @return
	 */
	private ICompletionProposal[] convertColumnsToCompletionProposals(ArrayList columns, int offset) {
		if (columns.size() > 0) {
			ICompletionProposal[] proposals = new ICompletionProposal[columns.size()];
			Iterator iter = columns.iterator();
			int n = 0;
			while (iter.hasNext()) {
				Column column = (Column) iter.next();
				proposals[n++] = new CompletionProposal(addQuotes(column.getName()), offset, 0,
						column.getName().length());
			}
			return proposals;
		}
		return null;
	}

	/**
	 * @param tables
	 * @return
	 */
	private ICompletionProposal[] convertTablesToCompletionProposals(ArrayList tables, int offset) {
		if (tables.size() > 0) {
			ICompletionProposal[] proposals = new ICompletionProposal[tables.size()];
			Iterator iter = tables.iterator();
			int n = 0;
			while (iter.hasNext()) {
				Table table = (Table) iter.next();
				proposals[n++] = new CompletionProposal(addQuotes(table.getName()), offset, 0,
						table.getName().length());
			}
			return proposals;
		}
		return null;
	}

	private String findWord(ITextViewer viewer, int offset) throws BadLocationException {
		// Check the character at the current position
		char ch = viewer.getDocument().getChar(offset);
		int startOffset = offset;
		if (isClosingQuoteChar(ch))// $NON-NLS-1$
		{
			startOffset--;
			char quoteChar = ch;
			if (quoteChar != '\'' || quoteChar != '"') {
				quoteChar = getOpeningQuoteChar();
			}
			// if the current character is a quote then we have to look till
			// the previous quote
			for (; startOffset > viewer.getTopIndexStartOffset(); startOffset--) {
				ch = viewer.getDocument().getChar(startOffset);
				if (ch == quoteChar) {
					break;
				}
			}
		} else {
			// just raad until we encounter something that is not a character
			while (startOffset >= viewer.getTopIndexStartOffset() && viewer.getDocument().getChar(startOffset) != '.'
					&& viewer.getDocument().getChar(startOffset) != ' ') {
				startOffset--;
			}
			startOffset++;
		}

		return viewer.getDocument().get(startOffset, offset - startOffset + 1);
	}

	private String stripQuotes(String string) {
		if (string.length() > 0) {
			if (isOpeningQuoteChar(string.charAt(0)) && isClosingQuoteChar(string.charAt(string.length() - 1))) {
				return string.substring(1, string.length() - 1);
			}
		}
		return string;
	}

	private String addQuotes(String string) {
		try {
			if (string.indexOf(' ') != -1) {
				if ("ACCESS".equalsIgnoreCase(metaData.getDatabaseProductName())) {
					return "[" + string + "]";//$NON-NLS-1$
				}
				return "\"" + string + "\"";//$NON-NLS-1$
			}
		} catch (Exception ex) {
		}

		return string;
	}

	private boolean isOpeningQuoteChar(char ch) {
		try {
			if ("ACCESS".equalsIgnoreCase(metaData.getDatabaseProductName())) {
				return (ch == '[');
			}
			return (ch == '\'' || ch == '"');
		} catch (Exception ex) {

		}

		return false;
	}

	private boolean isClosingQuoteChar(char ch) {
		try {
			if ("ACCESS".equalsIgnoreCase(metaData.getDatabaseProductName())) {
				return (ch == ']');
			}
			return (ch == '\'' || ch == '"');
		} catch (Exception ex) {

		}

		return false;
	}

	private char getOpeningQuoteChar() {
		try {
			if ("ACCESS".equalsIgnoreCase(metaData.getDatabaseProductName())) {
				return '[';
			}
		} catch (Exception ex) {

		}

		return '"';
	}

//    private char getClosingQuoteChar()
//    {
//        try
//        {
//            if("ACCESS".equalsIgnoreCase(metaData.getDatabaseProductName()))
//            {
//                return ']';
//            }
//        }
//        catch(Exception ex)
//        {
//            
//        }
//        
//        return '"';
//    }

}
