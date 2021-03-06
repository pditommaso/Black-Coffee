package org.blackcoffee.report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import org.apache.commons.io.FileUtils;
import org.blackcoffee.Config;
import org.blackcoffee.TestCase;
import org.blackcoffee.TestResult;
import org.blackcoffee.exception.BlackCoffeeException;


/**
 * Generic test report builder 
 * 
 * @author Paolo Di Tommaso
 *
 */
public abstract class ReportBuilder {

	final protected PrintStream out;
	
	final protected Config config;
	
	protected ReportBuilder( ) { out=null; config=null; }
	
	protected ReportBuilder( OutputStream out, Config config ) { 
		this.out = out instanceof PrintStream ? (PrintStream)out : new PrintStream(out);
		this.config = config;
	}
	
	abstract public void begin();

	abstract public void end();
	
	abstract public void group(String header);
	
	abstract public void groupEnd();
	
	abstract public void test( TestCase test );
	
	abstract public void testEnd( TestResult result );
	
	abstract public void print(String string);
	

	/**
	 * Helper method to suppres check exceptions 
	 */
	protected static FileOutputStream newFileStream( File file ) { 
		try { 
			return new FileOutputStream(file);
		}
		catch( IOException e ) { 
			throw new BlackCoffeeException(e, "Cannot write to file: %s", file);
		}
	}
	
	
	/**
	 * Converts an amount of time (specified as millisecond) in a duration string. For example:
	 * 
	 * @param millis the elapsed time in milliseconds 
	 * @return time period as string e.g. 100 ms, 10 secs, 1 hour ..
	 */
	public static String asDuration( long millis ) {
		
		if( millis < 1000 ) {
			return millis + " ms";
		}
		
		double secs = millis / (double)1000;
		if( secs < 60 ) {
			return String.format("%.0f s", secs);
		}
		
		double mins = secs / 60;
		if( mins < 60 ) {
			double i = Math.floor(mins);
			double r = ((mins-i) * 60);
			String result = String.format( "%.0f m", i);
			if( r > 0 ) { 
				result += String.format( " %.0f s", r);
			}
			return result;
		}
		
		double hours = mins / 60;
		if( hours < 24 ) {
			double i = Math.floor(hours);
			double r = ((hours-i) * 60);
			String result = String.format( "%.0f h", i);
			if( r > 0 ) { 
				result += String.format( " %.0f m", r);
			}
			return result;
		}
		
		double days = hours / 24;
		double i = Math.floor(days);
		double r = ((days-i) * 24);
		String result = String.format( "%.0f d", i);
		if( r > 0 ) { 
			result += String.format( " %.0f h", r);
		}
		return result;

	}
	
	final protected String readFileToString(File file) {
		try { 
			return FileUtils.readFileToString(file);
		}
		catch( IOException e ) { 
			return String.format("<<cannot read %s>>", file);
		}
	}

}
