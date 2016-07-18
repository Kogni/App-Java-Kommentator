/**
 * @author bls
 *
 */

package com.pynting.kommentator_e_20160223;

public class Class_Word {

    public int first_count;
    public int first_samples;
    public String word;
    public String wordClass;

    public boolean possibleClass_Verb = true;
    public boolean possibleClass_Object = true;
    public boolean possibleClass_Number = true;

    public Class_Word(String word) {
	this.word = word;

	// universale, absolutte regler
	try {
	    int tall = Integer.parseInt(word);
	    wordClass = "Number";
	} catch (Exception e) {
	    possibleClass_Verb = false;
	    possibleClass_Object = false;
	}
    }

    @Override
    public String toString() {
	return word + ", " + wordClass + ", " + first_count + ", " + first_samples;
    }

}
