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
			boolean inString = false;
			Position commentPosition = null;
			Position methodPosition = null;
			int length = script.length( );
			int leftMark = 0;
			int i = 0;

			do
			{
				if ( !inString && !inComment && isCommentLine( i ) )
				{
					while ( i < length && script.charAt( i++ ) != '\n' )
					{
					}
					continue;
				}

				char ch = script.charAt( i++ );

				switch ( ch )
				{
					case '/' :
						if ( !inString &&
								!inComment &&
								i + 1 < length &&
								script.charAt( i ) == '*' )
						{
							inComment = true;
							commentPosition = new Position( i++ );
						}
						break;

					case '*' :
						if ( !inString &&
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
					case '\'' :
						if ( !inComment )
						{
							if ( i - 2 >= 0 )
							{
								if ( script.charAt( i - 2 ) == '\\' )
								{
									break;
								}
							}
							inString = !inString;
						}
						break;

					case 'f' :
						if ( !inComment && !inString )
						{
							int begin = i - 2 >= 0 ? i - 2 : 0;
							int end = begin + "funciton".length( ) + 2; //$NON-NLS-1$

							if ( end <= script.length( ) &&
									script.substring( begin, end )
											.matches( "\\Wfunction\\W" ) ) //$NON-NLS-1$
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
						if ( !inComment && !inString && methodPosition != null )
						{
							leftMark++;
						}
						break;

					case '}' :
						if ( !inComment && !inString && methodPosition != null )
						{
							if ( --leftMark > 0 )
							{
								break;
							}
							if ( includeMultiLine( script,
									methodPosition.getOffset( ),
									i ) )
							{
								while ( i < length &&
										script.charAt( i ) != '\n' )
								{
									i++;
								}

								methodPosition.setLength( i -
										methodPosition.getOffset( ) +
										1 );

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

		while ( start > 0 && script.charAt( start - 1 ) != '\n' )
		{
			if ( start < script.length( ) )
			{
				if ( script.charAt( start ) == '/' &&
						script.charAt( start - 1 ) == '/' )
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
