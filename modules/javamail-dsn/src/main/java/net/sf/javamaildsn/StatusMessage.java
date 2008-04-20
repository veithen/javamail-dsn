package net.sf.javamaildsn;

/**
 * Interface representing a status message.
 * This interface defines several methods to retrieve the content of the status
 * message in different forms.
 * 
 * @author Andreas Veithen
 */
public interface StatusMessage {
    /**
     * Get the individual lines of the status message as a string array.
     * The individual strings returned by this method don't contain any newlines.
     * 
     * @return the individual lines of the status message
     */
	String[] getLines();
	
	/**
	 * Get the unfolded status message.
	 * The resulting string contains the status message with all newlines replaced
	 * by spaces.
	 * 
	 * @return the unfolded status message
	 */
	String getUnfolded();
	
	/**
	 * Get a string representation of the status message.
	 * The resulting string contains the original status message with all newlines
	 * preserved.
	 * 
	 * @return the message as a single string
	 */
	String toString();
}
