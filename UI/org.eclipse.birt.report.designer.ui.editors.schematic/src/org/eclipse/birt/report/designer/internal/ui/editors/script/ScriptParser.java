/*************************************************************************************
 * Copyright (c) 2007 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.script;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

import org.eclipse.jface.text.Position;

/**
 * The script parser.
 */
public class ScriptParser
{

	/**
	 * The all comment positons in the script, elements are instance of
	 * <code>Position</code>.
	 */
	private final Collection commentPositions = new HashSet( );

	/**
	 * The all method positons in the script, elements are instance of
	 * <code>Position</code>.
	 */
	private final Collection methodPositions = new HashSet( );

	/** The script text to parse. */
	private final String script;

	/**
	 * Constructs a script parser with the specified script text.
	 * 
	 * @param script
	 *            the script to parse.
	 */
	public ScriptParser( String script )
	{
		this.script = script;
		parse( );
	}

	/**
	 * Parses the script.
	 */
	protected void parse( )
	{
		commentPositions.clear( );
		methodPositions.clear( );

		if ( script != null && script.length( ) > 0 )
		{
			boolean inComment = false;
			boolean inDoubleString = false;
			boolean inSingleString = false;
			Position commentPosition = null;
			Position methodPosition = null;
			int length = script.length( );
			int leftMark = 0;
			int i = 0;

			do
			{
				if ( !inDoubleString &&
						!inSingleString &&
						!inComment &&
						isCommentLine( i ) )
				{
					while ( i < length && script.charAt( i++ ) != '\n' )
					{
					}

					if ( i < length )
					{
						continue;
					}
					else
					{
						break;
					}
				}

				char ch = script.charAt( i++ );

				switch ( ch )
				{
					case '/' :
						if ( !inDoubleString &&
								!inSingleString &&
								!inComment &&
								i + 1 < length &&
								script.charAt( i ) == '*' )
						{
							inComment = true;
							commentPosition = new Position( i++ );
						}
						break;

					case '*' :
						if ( !inDoubleString &&
								!inSingleString &&
								inComment &&
								i < length &&
								script.charAt( i ) == '/' &&
								commentPosition != null )
						{
							i++;
							if ( includeMultiLine( script,
									commentPosition.getOffset( ),
									i - 1 ) )
							{
								int end = i;

								while ( end < length &&
										script.charAt( end ) != '\n' )
								{
									end++;
								}

								commentPosition.setLength( end -
										commentPosition.getOffset( ) +
										1 );

								commentPositions.add( commentPosition );
							}
							commentPosition = null;
							inComment = false;
						}
						break;

					case '"' :
						if ( !inComment && !inSingleString )
						{
							if ( i - 2 >= 0 )
							{
								int j = i - 2;
								int n = 0;

								while ( j >= 0 && script.charAt( j ) == '\\' )
								{
									j--;
									n++;
								}

								if ( n % 2 == 1 )
								{
									break;
								}
							}
							inDoubleString = !inDoubleString;
						}
						break;

					case '\'' :
						if ( !inComment && !inDoubleString )
						{
							if ( i - 2 >= 0 )
							{
								int j = i - 2;
								int n = 0;

								while ( j >= 0 && script.charAt( j ) == '\\' )
								{
									j--;
									n++;
								}

								if ( n % 2 == 1 )
								{
									break;
								}
							}
							inSingleString = !inSingleString;
						}
						break;

					case 'f' :
						if ( !inComment && !inDoubleString && !inSingleString )
						{
							// When the value of 'i - 2' is less than 0, 'f' is
							// the first char of the script.
							int begin = i - 2 >= 0 ? i - 2 : 0;
							int end = begin +
									"funciton".length( ) + ( i - 2 >= 0 ? 2 : 1 ); //$NON-NLS-1$

							String keyword = script.substring( begin,
									Math.min( length, end ) );

							keyword = i - 2 >= 0 ? keyword : " " + keyword; //$NON-NLS-1$
							if ( end <= script.length( ) &&
									keyword.matches( "\\Wfunction\\W" ) ) //$NON-NLS-1$
							{
								int start = i;

								while ( start > 0 &&
										script.charAt( start - 1 ) != '\n' )
								{
									start--;
								}
								methodPosition = new Position( start );
								leftMark = 0;
								i += "funciton".length( ); //$NON-NLS-1$
							}
						}
						break;

					case '{' :
						if ( !inComment &&
								!inDoubleString &&
								!inSingleString &&
								methodPosition != null )
						{
							leftMark++;
						}
						break;

					case '}' :
						if ( !inComment &&
								!inDoubleString &&
								!inSingleString &&
								methodPosition != null )
						{
							if ( --leftMark > 0 )
							{
								break;
							}
							if ( includeMultiLine( script,
									methodPosition.getOffset( ),
									i ) )
							{
								int j = i;

								while ( j < length &&
										script.charAt( j ) != '\n' )
								{
									j++;
								}

								methodPosition.setLength( j -
										methodPosition.getOffset( ) +
										( j < length ? 1 : 0 ) );

								methodPositions.add( methodPosition );
							}
							methodPosition = null;
							leftMark = 0;
						}
						break;
				}
			} while ( i < length );
		}
	}

	/**
	 * Returns <code>true</code> if the text{start, end} include multi line,
	 * <code>false</code> otherwise.
	 * 
	 * @param text
	 *            the specified text.
	 * @param start
	 *            the start index.
	 * @param end
	 *            the end index.
	 * @return <code>true</code> if multi line are included,
	 *         <code>false</code> otherwise.
	 */
	private boolean includeMultiLine( String text, int start, int end )
	{
		for ( int i = start; i < Math.min( text.length( ), end ); i++ )
		{
			if ( text.charAt( i ) == '\n' )
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns <code>true</code> if current index is in a comment line,
	 * <code>false</code> otherwise.
	 * 
	 * @param index
	 *            the current index.
	 * @return <code>true</code> if current index is in a comment line,
	 *         <code>false</code> otherwise.
	 */
	private boolean isCommentLine( int index )
	{
		int start = index;

		while ( start >= 0 && script.charAt( start ) != '\n' )
		{
			if ( start + 1 < script.length( ) )
			{
				if ( script.charAt( start ) == '/' &&
						script.charAt( start + 1 ) == '/' )
				{
					return true;
				}
			}
			start--;
		}
		return false;
	}

	/**
	 * Returns a collection of all comment positions, elements are instance of
	 * <code>Position</code>.
	 * 
	 * @return a unmodifiable collection of all comment positions.
	 */
	public Collection getCommentPositions( )
	{
		return Collections.unmodifiableCollection( commentPositions );
	}

	/**
	 * Returns a collection of all method positions, elements are instance of
	 * <code>Position</code>.
	 * 
	 * @return a unmodifiable collection of all method positions.
	 */
	public Collection getMethodPositions( )
	{
		return Collections.unmodifiableCollection( methodPositions );
	}

	/**
	 * Returns a collection of all method info. Elements are instance of
	 * <code>MethodInfo</code>.
	 * 
	 * @return a unmodifiable collection of all method info. Elements are
	 *         instance of <code>IScriptMethodInfo</code>.
	 */
	public Collection getAllMethodInfo( )
	{
		Collection allMethodInfo = new HashSet( );
		Collection positions = getMethodPositions( );

		for ( Iterator iterator = positions.iterator( ); iterator.hasNext( ); )
		{
			Position position = (Position) iterator.next( );
			int offset = position.getOffset( );

			if ( offset < script.length( ) )
			{
				String[] strs = ( " " + script.substring( offset ) ).split( "\\Wfunction\\W", 2 ); //$NON-NLS-1$ //$NON-NLS-2$

				if ( strs.length > 1 )
				{
					String method = strs[1].trim( );

					for ( int i = 0; i < method.length( ); i++ )
					{
						char ch = method.charAt( i );

						if ( ch != ' ' && !Character.isJavaIdentifierPart( ch ) )
						{
							if ( ch == '(' )
							{
								method = method.substring( 0, i ).trim( );
							}
							else
							{
								method = ""; //$NON-NLS-1$
							}
							break;
						}
					}
					allMethodInfo.add( new ScriptMethodInfo( method, position ) );
				}
			}
		}
		return Collections.unmodifiableCollection( allMethodInfo );
	}
}
