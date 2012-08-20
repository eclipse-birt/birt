
package org.eclipse.birt.data.engine.impl.index;

import java.util.BitSet;

public class BTreeUtil
{

	public static int bytesToInteger( byte[] b )
	{
		return ( ( b[0] & 0xFF ) << 24 )
				+ ( ( b[1] & 0xFF ) << 16 ) + ( ( b[2] & 0xFF ) << 8 )
				+ ( ( b[3] & 0xFF ) << 0 );
	}

	public static byte[] getIncrementBytes( int value, int lastValues )
	{

		String outString = Integer.toBinaryString( value - lastValues );
		int length = outString.length( );
		int outByteArrayLength;
		if ( length % 7 == 0 )
		{
			outByteArrayLength = length / 7;
		}
		else
		{
			outByteArrayLength = ( length / 7 ) + 1;
			String zero = new String( );
			for ( int i = 0; i < 7 - length % 7; i++ )
			{
				zero += "0";
			}
			outString = zero + outString;
		}

		BitSet set = new BitSet( );
		for ( int i = 0; i < outByteArrayLength; i++ )
		{
			if ( i != outByteArrayLength - 1 )
				set.set( i * 8, true );
			for ( int j = 0; j < 7; j++ )
			{
				if ( outString.charAt( ( i * 7 ) + j ) == '1' )
					set.set( i * 8 + j + 1, true );
			}
		}

		byte[] arrays = toByteArray( set, outByteArrayLength );
		return arrays;
	}

	public static byte[] toByteArray( BitSet bits, int len )
	{
		byte[] bytes = new byte[len];
		for ( int i = 0; i < len * 8; i++ )
		{
			if ( bits.get( i ) )
			{
				bytes[i / 8] |= 1 << ( ( 7 - i % 8 ) % 8 );
			}
		}
		return bytes;
	}
}
