/*
 * Copyright (c) 2015, Matjaž <dev@matjaz.it> matjaz.it
 *
 * This Source Code Form is part of the project Numerus, a roman numerals
 * library for Java. The library and its source code may be found on:
 * https://github.com/MatjazDev/Numerus and http://matjaz.it/numerus
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/
 */
package it.matjaz.numerus.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit test of {@link RomanNumeral} which is a container for syntactically
 * correct roman numerals.
 *
 * @author Matjaž <a href="mailto:dev@matjaz.it">dev@matjaz.it</a>
 * <a href="http://matjaz.it">www.matjaz.it</a>
 */
public class RomanNumeralTest {

    private RomanNumeral roman;

    @Before
    public void defaultRomanConstructor() {
        roman = new RomanNumeral();
    }

    @Test
    public void whenDefaultConstructorIsCalledThenSymbolsAreEmptyString() {
        assertEquals(roman.getSymbols(), "");
    }

    @Test
    public void whenCorrectStringIsGivenThenNoExceptionIsThrown() {
        roman.setSymbols("XLII");
        assertEquals("XLII", roman.getSymbols());
    }

    @Test(expected = NullPointerException.class)
    public void whenNullStringIsGivenThenExceptionIsThrown() {
        roman.setSymbols(null);
    }

    @Test
    public void givenStringGetsStrippedAndUpcased() {
        roman.setSymbols("  \t\n\r   xliI ");
        assertEquals("XLII", roman.getSymbols());
    }

    @Test
    public void givenStringGetsStrippedOfInnerWhiteSpaceChars() {
        roman.setSymbols("  XL I  II");
        assertEquals("XLIII", roman.getSymbols());
    }

    @Test(expected = NumberFormatException.class)
    public void whenEmptyStringIsGivenThenExceptionIsThrown() {
        roman.setSymbols("");
    }

    @Test(expected = NumberFormatException.class)
    public void whenWhitespaceStringIsGivenThenExceptionIsThrown() {
        roman.setSymbols("  \t\n\r  ");
    }

    @Test(expected = NumberFormatException.class)
    public void whenImpossiblyLongStringIsGivenThenExceptionIsThrown() {
        roman.setSymbols("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
    }

    @Test(expected = NumberFormatException.class)
    public void whenStringContainsNonRomanCharactersThenExceptionIsThrown() {
        roman.setSymbols("pFXC-");
    }

    @Test
    public void whenStringContainsNonRomanCharactersThenExceptionMessageShowsThem() {
        try {
            roman.setSymbols("pPFXC-");
        } catch (NumberFormatException ex) {
            assertTrue(ex.getMessage().contains("pPF-".toUpperCase()));
        }
    }

    @Test(expected = NumberFormatException.class)
    public void whenStringWithIncorrectTomanSyntaxIsGivenThenExceptionIsThrown() {
        roman.setSymbols("MMCMIIIX");
    }

    @Test
    public void whenStringContainsMoreThanThreeConsecutiveTenLikeSymbolsThenThenExceptionMessageShowsThem() {
        try {
            roman.setSymbols("CCCC");
        } catch (NumberFormatException ex) {
            assertTrue(ex.getMessage().contains("CCCC"));
        }
    }

    @Test
    public void whenStringContainsMoreThanOneConsecutiveFiveLikeSymbolThenExceptionMessageShowsThem() {
        try {
            roman.setSymbols("DDXII");
        } catch (NumberFormatException ex) {
            assertTrue(ex.getMessage().contains("DD"));
        }
    }

    @Test
    public void whenStringContainsMoreThanOneConsecutiveFiveLikeSymbolThenExceptionMessageShowsThem2() {
        try {
            roman.setSymbols("DXID");
        } catch (NumberFormatException ex) {
            assertTrue(ex.getMessage().contains("DXID"));
        }
    }

    @Test
    public void syntaxCheckCanBePerformedWithoutInstantiation() {
        assertTrue(RomanNumeral.isCorrectRomanSyntax("CLXXII"));
        assertFalse(RomanNumeral.isCorrectRomanSyntax("LINUX RULES!"));
    }

    @Test
    public void toStringDelegatesGetter() {
        roman.setSymbols("MCMLXIV");
        assertEquals(roman.getSymbols(), roman.toString());
    }

    @Test
    public void equalsMethodWorksh() {
        RomanNumeral numeral1 = new RomanNumeral("MCMLXIV");
        RomanNumeral numeral2 = new RomanNumeral("MCMLXIV");
        assertTrue(numeral1.equals(numeral2));
    }

    @Test
    public void defaultConstructorAndSetterIsTheSameAsInitializingConstructor() {
        RomanNumeral numeral1 = new RomanNumeral();
        numeral1.setSymbols("MCMLXIV");
        RomanNumeral numeral2 = new RomanNumeral("MCMLXIV");
        assertTrue(numeral1.equals(numeral2));
    }

    @Test
    public void setterAfterDefaultConstructorReturnsEmptyString() {
        assertEquals(roman.getSymbols(), "");
    }

    @Test
    public void whenDefaultConstructorIsCalledThenAnInitializationTestCanBeCalled() {
        assertFalse(roman.isInitialized());
        roman.setSymbols("C");
        assertTrue(roman.isInitialized());
    }

    @Test
    public void serializabilityWorksBothWays() {
        FileOutputStream outputFile = null;
        try {
            roman.setSymbols("MMXV");
            File tempFile = new File("/tmp/numerals.ser");
            outputFile = new FileOutputStream(tempFile);
            ObjectOutputStream outputStream = new ObjectOutputStream(outputFile);
            outputStream.writeObject(roman);
            outputStream.close();
            outputFile.close();
            FileInputStream inputFile = new FileInputStream(tempFile);
            ObjectInputStream inputStream = new ObjectInputStream(inputFile);
            RomanNumeral deserializedRoman = (RomanNumeral) inputStream.readObject();
            inputStream.close();
            inputFile.close();
            assertEquals(roman, deserializedRoman);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RomanNumeralTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(RomanNumeralTest.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                outputFile.close();
            } catch (IOException ex) {
                Logger.getLogger(RomanNumeralTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @Test
    public void romanNumeralIsCloneable() {
        roman.setSymbols("DXI");
        try {
            RomanNumeral otherRoman = (RomanNumeral) roman.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(RomanNumeralTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }
    
    @Test
    public void clonedRomanNumeralEqualsOriginalOne() {
        roman.setSymbols("DXI");
        RomanNumeral otherRoman;
        try {
            otherRoman = (RomanNumeral) roman.clone();
            assertEquals(roman, otherRoman);
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(RomanNumeralTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

}
