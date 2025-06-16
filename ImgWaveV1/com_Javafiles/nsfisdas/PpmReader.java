package com.nsfisdas;

import java.io.*;
import java.awt.image.*;

// This program is written using the software written by
// Jef Poskanzer < jef@acme.com > 1996

public class PpmReader 
{
 private BufferedReader in;
 public PpmReader( BufferedReader buf )
 {
   in = buf;
 }
    private int type;
    private static final int PBM_ASCII = 1;
    private static final int PGM_ASCII = 2;
    private static final int PPM_ASCII = 3;
    private static final int PBM_RAW = 4;
    private static final int PGM_RAW = 5;
    private static final int PPM_RAW = 6;

    private int width = -1, height = -1;
    private int maxval;

    /// Subclasses implement this to read in enough of the image stream
    // to figure out the width and height.
    void readHeader( ) throws IOException
        {
        char c1, c2;

        c1 = (char) readByte( in );
        c2 = (char) readByte( in );

        if ( c1 != 'P' )
            throw new IOException( "not a PBM/PGM/PPM file" );
        switch ( c2 )
            {
            case '1':
            type = PBM_ASCII;
            break;
            case '2':
            type = PGM_ASCII;
            break;
            case '3':
            type = PPM_ASCII;
            break;
            case '4':
            type = PBM_RAW;
            break;
            case '5':
            type = PGM_RAW;
            break;
            case '6':
            type = PPM_RAW;
            break;
            default:
            throw new IOException( "not a standard PBM/PGM/PPM file" );
            }
        width = readInt( in );
        height = readInt( in );
        if ( type != PBM_ASCII && type != PBM_RAW )
            maxval = readInt( in );
        }

    /// Subclasses implement this to return the width, or -1 if not known.
    int getWidth()
        {
        return width;
        }

    /// Subclasses implement this to return the height, or -1 if not
//known.
    int getHeight()
        {
        return height;
        }

    /// Subclasses implement this to read pixel data into the rgbRow
    // array, an int[width].  One int per pixel, no offsets or padding,
    // RGBdefault (AARRGGBB) color model
    void readRow( int row, int[] red, int[] green, int[] blue  ) throws
IOException
        {
        int col, r, g, b;
        int rgb = 0;
        char c;
	r = g = b = 0;
        for ( col = 0; col < width; ++col )
            {
            switch ( type )
                {
                case PBM_ASCII:
                c = readChar( in );
                if ( c == '1' )
                    rgb = 0xff000000;
                else if ( c == '0' )
                    rgb = 0xffffffff;
                else
                    throw new IOException( "illegal PBM bit" );
                break;
                case PGM_ASCII:
                g = readInt( in );
                r = b = g; 
                rgb = makeRgb( g, g, g );
                break;
                case PPM_ASCII:
                r = readInt( in );
                g = readInt( in );
                b = readInt( in );
                rgb = makeRgb( r, g, b );
                break;
                case PBM_RAW:
                if ( readBit( in ) )
                    rgb = 0xff000000;
                else
                    rgb = 0xffffffff;
                break;
                case PGM_RAW:
                g = readByte( in );
                if ( maxval != 255 )
                    g = fixDepth( g );
                r = b = g; 
                rgb = makeRgb( g, g, g );
                break;
                case PPM_RAW:
                r = readByte( in );
                g = readByte( in );
                b = readByte( in );
                if ( maxval != 255 )
                    {
                    r = fixDepth( r );
                    g = fixDepth( g );
                    b = fixDepth( b );
                    }
                rgb = makeRgb( r, g, b );
                break;
                }
            red[col] =r;
  	    blue[col] = b;
            green[col]= g;
           if ( r > 255 || r < 0) {
        	   System.out.println(" Error "+r);
        	   //System.exit(0);
        	   }
 	   if ( g> 255 || g < 0){
 		   System.out.println(" Error "+g);
 		   //System.exit(0);
 	   }
	   if ( b> 255 || b < 0){
		   System.out.println(" Error "+b);
		   //System.exit(0);
		   }
            }
        }
    
    /// Utility routine to read a byte.  Instead of returning -1 on
    // EOF, it throws an exception.
    private static int readByte( BufferedReader in ) throws IOException
        {
        int b = in.read();
        if ( b == -1 )
            throw new EOFException();
        return b;
        }

    private int bitshift = -1;
    private int bits;

    /// Utility routine to read a bit, packed eight to a byte, big-endian.
    private boolean readBit( BufferedReader in ) throws IOException
        {
        if ( bitshift == -1 )
            {
            bits = readByte( in );
            bitshift = 7;
            }
        boolean bit = ( ( ( bits >> bitshift ) & 1 ) != 0 );
        --bitshift;
        return bit;
        }

    /// Utility routine to read a character, ignoring comments.
    private static char readChar( BufferedReader in ) throws IOException
        {
        char c;

        c = (char) readByte( in );
        if ( c == '#' )
            {
            do
                {
                c = (char) readByte( in );
                }
            while ( c != '\n' && c != '\r' );
            }

        return c;
        }

    /// Utility routine to read the first non-whitespace character.
    private static char readNonwhiteChar( BufferedReader in ) throws
IOException
        {
        char c;

        do
            {
            c = readChar( in );
            }
        while ( c == ' ' || c == '\t' || c == '\n' || c == '\r' );

        return c;
        }

    /// Utility routine to read an ASCII integer, ignoring comments.
    private static int readInt( BufferedReader in ) throws IOException
        {
        char c;
        int i;

        c = readNonwhiteChar( in );
        if ( c < '0' || c > '9' )
            throw new IOException( "junk in file where integer should be"
);

        i = 0;
        do
            {
            i = i * 10 + c - '0';
            c = readChar( in );
            }
        while ( c >= '0' && c <= '9' );

        return i;
        }

    /// Utility routine to rescale a pixel value from a non-eight-bit
//maxval.
    private int fixDepth( int p )
        {
        return ( p * 255 + maxval / 2 ) / maxval;
        }

    /// Utility routine make an RGBdefault pixel from three color values.
    private static int makeRgb( int r, int g, int b )
        {
        return 0xff000000 | ( r << 16 ) | ( g << 8 ) | b;
        }

 }


