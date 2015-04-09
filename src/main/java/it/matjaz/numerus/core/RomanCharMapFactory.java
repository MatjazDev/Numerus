package it.matjaz.numerus.core;

// @author Matjaž <a href="mailto:dev@matjaz.it">dev@matjaz.it</a> <a href="http://matjaz.it">www.matjaz.it</a>
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

public class RomanCharMapFactory {

    public static Map<String, Integer> generateCharMap() {
        Map<String, Integer> charMap = new HashMap();
        charMap.put("M", 1000);
        charMap.put("CM", 900);
        charMap.put("D", 500);
        charMap.put("CD", 400);
        charMap.put("C", 100);
        charMap.put("XC", 90);
        charMap.put("L", 50);
        charMap.put("XL", 40);
        charMap.put("X", 10);
        charMap.put("IX", 9);
        charMap.put("V", 5);
        charMap.put("IV", 4);
        charMap.put("I", 1);
        return charMap;
    }
    
    public static Map generateInverseCharMap() {
        Map<Integer, String> inverseCharMap = new HashMap();
        Map<String, Integer> charMap = generateCharMap();
        charMap.keySet().stream().forEach((key) -> {
            inverseCharMap.put(charMap.get(key), key);
        });
        return inverseCharMap;
    }
    
    public static BidiMap generateBidiCharMap() {
        return new DualHashBidiMap(generateCharMap());
    }
}
