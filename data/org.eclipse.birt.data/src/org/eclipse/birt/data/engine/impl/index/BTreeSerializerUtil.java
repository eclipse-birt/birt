package org.eclipse.birt.data.engine.impl.index;

import it.uniroma3.mat.extendedset.intset.ConciseSet;
import it.uniroma3.mat.extendedset.intset.IntSet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.io.UTFDataFormatException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

import org.eclipse.birt.core.btree.BTreeSerializer;
import org.eclipse.birt.core.i18n.CoreMessages;
import org.eclipse.birt.core.i18n.ResourceConstants;

public class BTreeSerializerUtil
{
	public static int[] byteArrayToIntArray( byte[] b )
	{
		int[] result = new int[b.length/4];
		for( int i = 0; i < result.length; i++ )
		{
			result[i] = byteArrayToInt( b, i*4 );
		}
		return result;
	}
	
	public static byte[] intArrayToByteArray( int[] array )
	{
		ByteBuffer byteBuffer = ByteBuffer.allocate( array.length * 4 );
		IntBuffer intBuffer = byteBuffer.asIntBuffer( );
		intBuffer.put( array );
		return byteBuffer.array( );
	}
	public static int byteArrayToInt(byte[] b, int offset) {
	    int value = 0;
	    for (int i = 0; i < 4; i++) {
	        int shift = (4 - 1 - i) * 8;
	        value += (b[i + offset] & 0x000000FF) << shift;
	    }
	    return value;
	}
	public static BTreeSerializer createSerializer( Class dataType )
	{
		if( dataType == String.class )
		{
			return new StringSerializer( );
		}
		else if ( dataType == BigDecimal.class )
		{
			return new BigDecimalSerializer( );
		}
		else if ( dataType == Integer.class )
		{
			return new IntegerSerializer( );
		}
		else if ( dataType == Double.class )
		{
			return new DoubleSerializer( );
		}
		else if ( dataType == java.util.Date.class )
		{
			return new DateTimeSerializer( );
		}
		else if ( dataType == java.sql.Date.class )
		{
			return new DateSerializer( );
		}
		else if ( dataType == Time.class )
		{
			return new TimeSerializer( );
		}
		else if ( dataType == Timestamp.class )
		{
			return new DateTimeSerializer( );
		}
		else if ( dataType == Boolean.class )
		{
			return new BooleanSerializer( );
		}
		return new JavaSerializer( );
	}	
}

class ConciseSerializer implements BTreeSerializer<IntSet>
{
	ClassLoader classLoader = IntSet.class.getClassLoader( );
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.core.btree.BTreeSerializer#getBytes(java.lang.Object)
	 */
	
	public byte[] getBytes( IntSet object ) throws IOException
	{
		assert object instanceof ConciseSet;
		
		/*double compressionRatio = ( (ConciseSet) object ).collectionCompressionRatio( );
		if( compressionRatio > 0.25 )
		{
			
			//TODO streaming to delta compression
			//Should add leading byte to indicate it is delta compression. 
			//Should return here.
			
		}*/
		
		int[] array = ( (ConciseSet) object ).getWords( );

		return BTreeSerializerUtil.intArrayToByteArray( array );
	}

	

	/* (non-Javadoc)
	 * @see org.eclipse.birt.core.btree.BTreeSerializer#getObject(byte[])
	 */
	
	public IntSet getObject( byte[] bytes ) throws IOException,
			ClassNotFoundException
	{
		//TODO: If the leading byte indicate it is delta, return the delta compression.
		
		return new ConciseSet( BTreeSerializerUtil.byteArrayToIntArray( bytes ) );
	}
}

class JavaSerializer implements BTreeSerializer<Object>
{
	ClassLoader classLoader = null;
	
	public void setClassLoader( ClassLoader classLoader )
	{
		this.classLoader = classLoader;
	}
	
	public byte[] getBytes( Object object ) throws IOException
	{
		if( ! ( object instanceof Serializable ) )
		{
			throw new NotSerializableException(
					CoreMessages.getString( ResourceConstants.NOT_SERIALIZABLE ) );
		}
		ByteArrayOutputStream buff = new ByteArrayOutputStream( );
		ObjectOutputStream oo = new ObjectOutputStream( buff );
		oo.writeObject( object );
		oo.close( );
		return buff.toByteArray( );
	}

	public Object getObject( byte[] bytes ) throws IOException,
			ClassNotFoundException
	{
		final ClassLoader loader = classLoader;
		ObjectInputStream oo = new ObjectInputStream(
					new ByteArrayInputStream( bytes ) ) {

				protected Class resolveClass( ObjectStreamClass desc )
						throws IOException, ClassNotFoundException
				{
					return Class.forName( desc.getName( ), false,
							loader );
				}
			};
		return oo.readObject( );
	}
}

class BooleanSerializer implements BTreeSerializer<Boolean>
{

	public byte[] getBytes( Boolean object ) throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream( 1024 );
		DataOutput oo = new DataOutputStream( out );
		oo.writeBoolean( object );
		return out.toByteArray( );
	}

	public Boolean getObject( byte[] bytes ) throws IOException,
			ClassNotFoundException
	{
		DataInput input = new DataInputStream( new ByteArrayInputStream(
				bytes ) );
		return input.readBoolean( );
	}
}

class IntegerSerializer implements BTreeSerializer<Integer>
{

	public byte[] getBytes( Integer i ) throws IOException
	{
		byte[] result = new byte[4];

		  result[0] = (byte) (i >> 24);
		  result[1] = (byte) (i >> 16);
		  result[2] = (byte) (i >> 8);
		  result[3] = (byte) ((int)i);
		  return result;
	}

	public Integer getObject( byte[] bytes ) throws IOException,
			ClassNotFoundException
	{
		return ByteBuffer.wrap(bytes).getInt();
	}
}

class DoubleSerializer implements BTreeSerializer<Double>
{

	public byte[] getBytes( Double object ) throws IOException
	{
		byte[] bytes = new byte[8];
	    ByteBuffer.wrap(bytes).putDouble( object );
	    return bytes;
	}

	public Double getObject( byte[] bytes ) throws IOException,
			ClassNotFoundException
	{
		return ByteBuffer.wrap(bytes).getDouble( );
	}
}

class StringSerializer implements BTreeSerializer<String>
{
	public byte[] getBytes( String str ) throws IOException
	{
		int strlen = str.length( );
		int c = 0;
		ByteBuffer dos = ByteBuffer.allocate(getBytesSize( str ));
		dos.position( 0 );
		int i = 0;
		for ( ; i < strlen; i++ )
		{
			c = str.charAt( i );
			if ( !( ( c >= 0x0001 ) && ( c <= 0x007F ) ) )
				break;
			dos.put( (byte) c );
		}

		for ( ; i < strlen; i++ )
		{
			c = str.charAt( i );
			if ( ( c >= 0x0001 ) && ( c <= 0x007F ) )
			{
				dos.put( (byte) c );
			}
			else if ( c > 0x07FF )
			{
				dos.put( (byte) ( 0xE0 | ( ( c >> 12 ) & 0x0F ) ) );
				dos.put(  (byte) ( 0x80 | ( ( c >> 6 ) & 0x3F ) ) );
				dos.put(  (byte) ( 0x80 | ( ( c >> 0 ) & 0x3F ) ) );
			}
			else
			{
				dos.put(  (byte) ( 0xC0 | ( ( c >> 6 ) & 0x1F ) ) );
				dos.put(  (byte) ( 0x80 | ( ( c >> 0 ) & 0x3F ) ) );
			}
		}
		return dos.array( );
	}

	
	private static int getBytesSize( String str )
	{
		int c, utflen = 0;
		for ( int i = 0; i < str.length( ); i++ )
		{
			c = str.charAt( i );
			if ( ( c >= 0x0001 ) && ( c <= 0x007F ) )
			{
				utflen++;
			}
			else if ( c > 0x07FF )
			{
				utflen += 3;
			}
			else
			{
				utflen += 2;
			}
		}
		return utflen;
	}
	public String getObject( byte[] bytearr ) throws IOException,
			ClassNotFoundException
	{
		int utflen = bytearr.length;
		char[] chararr = new char[utflen];
		int c;
		int chararr_count = 0;

		int count = 0;
		while ( count < utflen )
		{
			c = (int) bytearr[count] & 0xff;
			if ( c > 127 )
				break;
			count++;
			chararr[chararr_count++] = (char) c;
		}
		chararr_count = generateCharArray( chararr, bytearr, count,
				chararr_count );

		// The number of chars produced may be less than utflen
		return new String( chararr, 0, chararr_count );
	}
	
	private static int generateCharArray( char[] chararr, byte[] bytearr,
			int count, int chararr_count ) throws UTFDataFormatException
	{
		int c, char2, char3;
		int utflen = bytearr.length;
		while ( count < utflen )
		{
			c = (int) bytearr[count] & 0xff;
			switch ( c >> 4 )
			{
				case 0 :
				case 1 :
				case 2 :
				case 3 :
				case 4 :
				case 5 :
				case 6 :
				case 7 :
					// 0xxxxxxx
					count++;
					chararr[chararr_count++] = (char) c;
					break;
				case 12 :
				case 13 :
					// 110x xxxx 10xx xxxx
					count += 2;
					if ( count > utflen )
						throw new UTFDataFormatException(
								CoreMessages.getString( ResourceConstants.MALFORMED_INPUT_ERROR ) );
					char2 = (int) bytearr[count - 1];
					if ( ( char2 & 0xC0 ) != 0x80 )
						throw new UTFDataFormatException(
								CoreMessages.getFormattedString(
										ResourceConstants.MALFORMED_INPUT_AROUND_BYTE,
										new Object[]{count} ) );
					chararr[chararr_count++] = (char) ( ( ( c & 0x1F ) << 6 ) | ( char2 & 0x3F ) );
					break;
				case 14 :
					// 1110 xxxx 10xx xxxx 10xx xxxx
					count += 3;
					if ( count > utflen )
						throw new UTFDataFormatException(
								CoreMessages.getString( ResourceConstants.MALFORMED_INPUT_ERROR ) );
					char2 = (int) bytearr[count - 2];
					char3 = (int) bytearr[count - 1];
					if ( ( ( char2 & 0xC0 ) != 0x80 )
							|| ( ( char3 & 0xC0 ) != 0x80 ) )
						throw new UTFDataFormatException(
								CoreMessages.getFormattedString(
										ResourceConstants.MALFORMED_INPUT_AROUND_BYTE,
										new Object[]{count - 1} ) );
					chararr[chararr_count++] = (char) ( ( ( c & 0x0F ) << 12 )
							| ( ( char2 & 0x3F ) << 6 ) | ( ( char3 & 0x3F ) << 0 ) );
					break;
				default :
					// 10xx xxxx, 1111 xxxx
					throw new UTFDataFormatException(
							CoreMessages.getFormattedString(
									ResourceConstants.MALFORMED_INPUT_AROUND_BYTE,
									new Object[]{count} ) );
			}
		}
		return chararr_count;
	}
}

class DateTimeSerializer<K> implements BTreeSerializer<java.util.Date>
{

	public byte[] getBytes( java.util.Date object ) throws IOException
	{
		byte[] bytes = new byte[8];
	    ByteBuffer.wrap(bytes).putLong(object.getTime( ));
	    return bytes;
	}

	public java.util.Date getObject( byte[] bytes ) throws IOException,
			ClassNotFoundException
	{
		return new Date( ByteBuffer.wrap(bytes).getLong( ) );
	}
}

class TimeStampSerializer<K> implements BTreeSerializer<Timestamp>
{

	public byte[] getBytes( Timestamp object ) throws IOException
	{
		byte[] bytes = new byte[8];
	    ByteBuffer.wrap(bytes).putLong(object.getTime( ));
	    return bytes;
	}

	public Timestamp getObject( byte[] bytes ) throws IOException,
			ClassNotFoundException
	{
		return new Timestamp( ByteBuffer.wrap(bytes).getLong( ) );
	}
}

class DateSerializer<K> implements BTreeSerializer<java.sql.Date>
{

	public byte[] getBytes( java.sql.Date object ) throws IOException
	{
		byte[] bytes = new byte[8];
	    ByteBuffer.wrap(bytes).putLong(object.getTime( ));
	    return bytes;
	}

	public java.sql.Date getObject( byte[] bytes ) throws IOException,
			ClassNotFoundException
	{
		return new java.sql.Date( ByteBuffer.wrap(bytes).getLong( ) );
	}
}

class TimeSerializer<K> implements BTreeSerializer<java.sql.Time>
{

	public byte[] getBytes( java.sql.Time object ) throws IOException
	{
		long time = object.getTime( );
		byte[] bytes = new byte[8];
	    ByteBuffer.wrap(bytes).putLong(time);
	    return bytes;
	}

	public java.sql.Time getObject( byte[] bytes ) throws IOException,
			ClassNotFoundException
	{
		return new java.sql.Time( ByteBuffer.wrap(bytes).getLong( ) );
	}
}

class BigDecimalSerializer<K> implements BTreeSerializer<BigDecimal>
{
	private StringSerializer ss = new StringSerializer();
	public byte[] getBytes( BigDecimal object ) throws IOException
	{
		return ss.getBytes( object.toString( ) );
	}

	public BigDecimal getObject( byte[] bytes ) throws IOException,
			ClassNotFoundException
	{
		return new BigDecimal( ss.getObject( bytes ) );
	}
}