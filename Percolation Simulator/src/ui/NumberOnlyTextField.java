package ui;

import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;
import javax.swing.text.PlainDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

@SuppressWarnings("serial")
public class NumberOnlyTextField extends JTextField {
	private boolean beepOnInvalidInput;
	private boolean allowNegatives;
	private boolean allowDecimals;
	
	private List<Character> disallowedChars;
	
	private List<NumberChangeListener> listeners;
	
	public NumberOnlyTextField() {
		init();
	}
	
	private void init() {
		beepOnInvalidInput = true;
		allowNegatives = true;
		allowDecimals = true;
		
		listeners = new ArrayList<>();
		
		disallowedChars = new ArrayList<>();
		
		// 1) Selects all text when TextField is first clicked
		// 2) When focus is lost, if the TextField is empty, a number will
		//    automatically be inserted
		addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent arg0) {
				selectAll();
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				resolveSingleIllegalChar(true);
			}
		});
		
		/*
		 * Fire NumberChangeListeners
		 */
		addKeyListener(new KeyAdapter() {
			float prevNum;
			
			@Override
			public void keyPressed(KeyEvent e) {
				prevNum = getNumberFloat();
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// Only fire listeners when the NUMBER represented
				// in the text field has changed, (not when the text
				// itself has changed)
				
				if (getText().isEmpty())
					return;
								
				final float currNum = getNumberFloat();
				
				if (prevNum != currNum)
					fireListeners(prevNum, currNum);
			}
		});
	}
	
	
	private char resolveSingleIllegalChar(boolean replace) {
		// Replace invalid characters/empty strings
		if (
				// Text is empty
				getText().isEmpty()
				
				// Or text is one character long and it is a period
				|| (getText().length() == 1 && (getText().charAt(0) == '.' ||				
				// Or it is a minus sign
				getText().charAt(0) == '-'))
				
				// Or it is this: ".-" (illegal config)
				|| getText().equals(".-")
		) {
			// Find a legal number to put into the text field
			char replacement;
			for (replacement = '0'; replacement <= '9'; replacement++) {
				if (!disallowedChars.contains(replacement)) {
					break;
				}
			}			
			
			if (replace) {
				setText(String.valueOf(replacement));
				// Fire listeners, number value has changed
				fireListeners(Float.NaN, getNumberFloat());
			} else {
				return replacement;
			}	
		}
		return '\0';
	}
		
	public float getNumberFloat() {
		// See if there's an illegal single char (don't replace)
		final char repl = resolveSingleIllegalChar(false);
		
		// If so, return the char
		if (repl != '\0')
			return repl;
		
		// "-." = 0, but Float.parseFloat() doesn't know that
		if (getText().equals("-."))
			return 0f;
		
		return Float.parseFloat(getText());
	}
	
	public int getNumberInt() {
		return (int) getNumberFloat();
	}
	
	/**
	 * Returns the given String without any illegal characters (based on
	 * the insertion point/index).
	 * Illegal characters are non-number characters, but decimal points
	 * and minus signs can also be considered illegal depending on the
	 * values returned by {@link NumberOnlyTextField#isAllowDecimals()}
	 * and {@link NumberOnlyTextField#isAllowNegatives()}.
	 * 
	 * NOTE: there is an unsolved problem in this method: disallowed
	 * characters can still sneakily get through this method. Example:<p>
	 *     If 0 was disallowed, then typing 10 and then deleting the
	 *     one would work (0 would be alone)
	 *     <p>
	 * Another problem is that this method does not allow the insertion
	 * of a disallowed number (problematic if for example 1 is disallowed,
	 * but you wanted to type "11").
	 * @param str the String to examine
	 * @param offs the insertion index in the String
	 * @return the given String, minus any illegal characters
	 */
	public String removeIllegalCharacters(String str, int offs) {
		
		final int indexOfDecimal = getText().indexOf(".");
		final boolean containsDecimal = indexOfDecimal > -1;
		final boolean containsMinus = getText().contains("-");
		StringBuilder modifiedString = new StringBuilder();
		
		for (char c : str.toCharArray()) {
			// c must be between 0-9, cannot be disallowed
			// (while there is no other text)
			final boolean validDigit = c >= '0' && c <= '9';
			// Can't be disallowed (if it's the only char)
			final boolean notDisallowed = !isDisallowedChar(c) 
					|| (isDisallowedChar(c) 
					&& (!getText().isEmpty() || str.length() > 1));
			final boolean decimalsAllowed = c == '.' && allowDecimals;
			final boolean notDuplicateDecimal = !containsDecimal;
			final boolean minusAllowed = c == '-' && allowNegatives;
			final boolean notDuplicateMinus = !containsMinus;
			final boolean minusInFront = offs == 0;
			// A period cannot come before a minus sign
			final boolean isNotIllegalSeq = 
					! (c == '-' && containsDecimal && indexOfDecimal < offs);
			
			// Conditions must be met
			if (
					((validDigit && notDisallowed)
					||
					(decimalsAllowed && notDuplicateDecimal)
					||
					(minusAllowed && notDuplicateMinus && minusInFront))
					&&
					isNotIllegalSeq
			) {
				modifiedString.append(c);
			}
		}
		
		return modifiedString.toString();
		
	}
	
	@Override
	public final void setDocument(Document doc) {
		super.setDocument(new LetterOnlyDocument());
	}
	
	public boolean beepOnInvalidInput() {
		return beepOnInvalidInput;
	}
	
	public void setBeepOnInvalidInput(boolean beepOnInvalidInput) {
		this.beepOnInvalidInput = beepOnInvalidInput;
	}
	
	public boolean isAllowNegatives() {
		return allowNegatives;
	}

	public void setAllowNegatives(boolean allowNegatives) {
		this.allowNegatives = allowNegatives;
	}

	public boolean isAllowDecimals() {
		return allowDecimals;
	}

	public void setAllowDecimals(boolean allowDecimals) {
		this.allowDecimals = allowDecimals;
	}
	
	
	public List<Character> getDisallowedChars() {
		return disallowedChars;
	}
	
	private boolean isDisallowedChar(char c) {
		for (char d : disallowedChars) {
			if (d == c) {
				return true;
			}
		}
		return false;
	}
	
	public List<NumberChangeListener> getNumberChangeListeners() {
		return listeners;
	}
	
	private void fireListeners(float oldNum, float newNum) {
		for (NumberChangeListener list : listeners) {
			list.numberChanged(this, oldNum, newNum);
		}
	}
	
	
	private class LetterOnlyDocument extends PlainDocument {
		@Override
		public void insertString(int offs, String str, AttributeSet a) 
				throws BadLocationException {
			
			String modifiedString = removeIllegalCharacters(str, offs);
			
			// Beep if necessary
			if (beepOnInvalidInput && modifiedString.length() != str.length()) {
				Toolkit.getDefaultToolkit().beep();
			}
			
			super.insertString(offs, modifiedString.toString(), a);
		
		}
	}

}
