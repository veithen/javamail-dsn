package net.sf.javamaildsn.heuristics;

import java.util.Arrays;

public class BoundaryMatcher {
	private final char[] chars;
	
	public BoundaryMatcher(char[] chars) {
		this.chars = chars.clone();
		Arrays.sort(this.chars);
	}
	
	private boolean matches(char c) {
		return Arrays.binarySearch(chars, c) != -1;
	}
	
	public boolean matches(String s, int startIndex, int endIndex) {
		return (startIndex == 0 || matches(s.charAt(startIndex-1))) && (endIndex == s.length() || matches(s.charAt(endIndex)));
	}
}
