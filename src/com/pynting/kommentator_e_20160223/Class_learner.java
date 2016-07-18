/**
 * @author bls
 *
 */

package com.pynting.kommentator_e_20160223;

import java.util.HashMap;

import android.util.Log;

public class Class_learner {

    // kommunikasjon
    // samtale
    Class_Samtale samtale = new Class_Samtale();
    // poeng
    // setning
    String focusword;
    // ord
    HashMap<String, Class_Word> ordkunnskap = new HashMap<String, Class_Word>();

    String path;
    static String Filename = "count";
    String filename;
    int count = 0;

    public String respondToSentence(String textInput) // tolk hva mottaker ønsker og lag et svar
    {
	// sjekk att mottatt string ikke start med space
	while (textInput.substring(0, 1).equals(" ")) {
	    textInput = textInput.substring(1);
	}

	// sjekk om det fortsatt er samme samtale, ved å sammenligne tidspunkt med forrige utsagn

	// putt ord i array for prosessering
	int words = 0;
	String[] parts = textInput.split(" ");
	for (String word : parts) {
	    words++;
	}
	Class_Word[] sentence = new Class_Word[words];
	int count = 0;
	parts = textInput.split(" ");
	for (String word : parts) {
	    sentence[count] = new Class_Word(word);
	    count++;
	}

	// tolk betydning og finn ordklasser
	Class_Word[] interpretedWords = word_interpret(sentence);

	// lagre nye ord
	sentence_SaveNewWords(interpretedWords);

	// lagre statistikk for hvordan ord brukes
	sentence_setStatistics(interpretedWords);

	// find crucial words
	focusword = get_Focus(interpretedWords);

	// lag respons
	String response = respond(interpretedWords);

	// uttal respons
	return response;
    }

    private String get_Focus(Class_Word[] interpretedWords) {
	String focus = "";
	// viktigste verb eller substantiv i setning og samtale

	return focus;
    }

    public Class_Word[] word_interpret(Class_Word[] words_unknown) // tolk ordene som er mottatt, sjekk at de gir mening
    {
	String result = "";

	// Hvis det er mer enn 1 ord, send setningen rekursivt for å sjekke komponentene for valid mening
	// hva skal gjøres om det ikke blir funnet noen mening?
	Class_Word[] words_temp = new Class_Word[words_unknown.length]; // Instansier ny "setning
	words_temp[0] = words_unknown[0];
	Class_Word[] words_checked;
	int index = words_unknown.length;
	index = 0;
	if (words_unknown.length > 1) {
	    // reduser setning med 1 ord
	    words_temp = new Class_Word[words_unknown.length - 1];
	    for (Class_Word word : words_temp) {
		words_temp[index] = words_unknown[index];
	    }
	    // sjekk ordkomboen
	    words_checked = word_interpret(words_temp);
	}

	// undekromponentene er nå sjekket for mening. Sjekk den tilsendte setningen for mening
	Class_Word[] words_assigned = word_matching(words_unknown);

	return words_assigned;
    }

    private Class_Word[] word_matching(Class_Word[] words_unchecked) {
	// sjekk at ordene gir mening sammen

	// finn ut dette ved å tillegge ordklasser som passer sammen. Ordklassene vil avhenge av plassering ift andre ord(klasser)
	Class_Word[] words_assigned = sentence_figureWordClasses(words_unchecked);

	return words_assigned;
    }

    private Class_Word[] sentence_figureWordClasses(Class_Word[] words_unchecked) {

	int count = 0;
	Class_Word[] words_assigned = words_unchecked;
	for (Class_Word a : words_assigned) {
	    if ((a != null) && (a.word != null)) {
		count++;
		// regler for ordklasser avhenger av språket
		// Ordklassene vil avhenge av plassering ift andre ord(klasser)

		// universale, absolutte regler gjøres i class_word

		// sikre regler - sett ordklassen direkte
		// usikre regler
		if (a.word.endsWith("er")) {
		    // løper, høner,
		    // integer
		    a.possibleClass_Number = false;
		} else if (a.word.endsWith("ing")) {
		    // ting, løping
		    // running, string
		    a.possibleClass_Number = false;
		} else if (a.word.endsWith("or")) {
		    // transistor, bor, glor
		    a.possibleClass_Number = false;
		} else if (a.word.endsWith("een")) {
		    // skjeen, tjueen
		    //teen
		    a.possibleClass_Verb = false;
		} else if (a.word.endsWith("en")) {
		    // tingen, en, tretten
		    //often, when
		    a.possibleClass_Verb = false;
		} else if (a.word.endsWith("ion")) {
		    // transition,
		    a.wordClass = "Object";
		    a.possibleClass_Number = false;
		} else if (a.word.endsWith("s")) {
		    // leses
		    // runs, humans
		}

		Log.i("Class_Learn", "sentence_figureWordClasses " + count + " a=" + a.toString());
	    }
	}

	return words_assigned;

    }

    private void sentence_SaveNewWords(Class_Word[] words) {
	Log.i("Class_Learn", "sentence_SaveNewWords " + words[0].word);
	for (Class_Word a : words) {
	    if (a.word.length() > 0) {
		Boolean saved = false;
		Class_Word lagret = ordkunnskap.get(a.word);
		if (lagret != null) {
		    saved = true;
		}

		if (saved == false) {
		    ordkunnskap.put(a.word, a);
		    // Log.i("Class_Learn", "sentence_SaveNewWords Added new word -" + a.word + "-");
		}
	    }
	}
    }

    private void sentence_setStatistics(Class_Word[] words) {
	// lagre statistikk for hvordan hvert ord brukes i setningen som helhet
	// lagre statistikk for hvor ofte et ord brukes i den aktuelle ordklassen som
	// er tillagt

	Log.i("Class_Learn", "sentence_setStatistics " + words[0].word);
	for (Class_Word a : words) {
	    Class_Word lagret = ordkunnskap.get(a.word);
	    if (lagret != null) {
		lagret.first_samples++;
	    }
	}

	Class_Word lagret2 = ordkunnskap.get(words[0].word);
	lagret2.first_count++;

    }

    public String respond(Class_Word[] receivedChat)// lag et svar
    {

	String response = "";
	// use crucial words
	// include focus word
	// add find words related to focus word

	// make a sentence that makes sense
	Class_Word[] words_checked = word_matching(receivedChat);

	for (Class_Word n : words_checked) {
	    response = response + " " + n.word;
	}
	while (response.substring(0, 1).equals(" ")) {
	    response = response.substring(1);
	}
	Log.i("Class_Learn", "respond #4, =" + +receivedChat.length + " " + words_checked.length);

	return response;

    }

}
