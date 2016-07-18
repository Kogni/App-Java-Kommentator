package com.pynting.kommentator_e_20160223;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_TextToSpeech extends Activity implements OnInitListener, OnItemSelectedListener {

    private EditText mEditText;
    private Button mBtnSpeak;

    List<Locale> availableLocales;
    // Locale[] locale;
    String langs[];
    private Spinner lang;
    long position = 0;
    Locale chosenLang;

    private TextToSpeech mTts;

    private Class_learner learner;

    protected static final int RESULT_SPEECH = 1;

    private ImageButton btnSpeak;
    private TextView txtText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);
	learner = new Class_learner();

	mEditText = (EditText) findViewById(R.id.wordToSpeak);
	mBtnSpeak = (Button) findViewById(R.id.btnSpeak);

	mTts = new TextToSpeech(this, this);

	lang = (Spinner) findViewById(R.id.spinner1);

	Locale[] locales = Locale.getAvailableLocales();
	availableLocales = new ArrayList<Locale>();
	for (Locale locale : locales) {

	    availableLocales.add(locale);
	}

	// locale = Locale.getAvailableLocales();
	langs = new String[availableLocales.size()];
	for (int i = 0; i < availableLocales.size(); i++) {
	    Locale temp = availableLocales.get(i);
	    if (temp != null) {
		langs[i] = temp.getDisplayLanguage() + " " + temp.getDisplayName();
	    }
	}
	Arrays.sort(langs);
	ArrayAdapter<Object> spinnerArrayAdapter = new ArrayAdapter<Object>(this, android.R.layout.simple_spinner_item, langs);
	lang.setAdapter(spinnerArrayAdapter);

	lang.setOnItemSelectedListener(this);

	mBtnSpeak.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		String input = mEditText.getText().toString();
		String respond = learner.respondToSentence(input);
		mEditText.setText(respond);
		speak(respond);
	    }

	    private void speak(String respond) {
		mTts.setLanguage(chosenLang);
		Log.i("Class_Learn", "speak langLocale=" + chosenLang);
		mTts.speak(respond, TextToSpeech.QUEUE_FLUSH, null);

	    }
	});

	txtText = (TextView) findViewById(R.id.txtText);

	btnSpeak = (ImageButton) findViewById(R.id.btnSpeak2);

	btnSpeak.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View v) {

		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

		try {
		    startActivityForResult(intent, RESULT_SPEECH);
		    txtText.setText("");
		} catch (ActivityNotFoundException a) {
		    Toast t = Toast.makeText(getApplicationContext(), "Ops! Your device doesn't support Speech to Text",
			    Toast.LENGTH_SHORT);
		    t.show();
		}
	    }
	});

    }

    private Locale getLocaleByString(String lang) {
	for (int i = 0; i < availableLocales.size(); i++) {
	    Locale temp = availableLocales.get(i);
	    if ((temp.getDisplayLanguage() + " " + temp.getDisplayName()).equals(lang)) {
		return temp;
	    }
	}
	return null;
    }

    @Override
    public void onInit(int status) {

	if (status == TextToSpeech.SUCCESS) {
	    int result = mTts.setLanguage(Locale.US);

	    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
		Log.e("404", "Chosen language is not available.");
	    }
	} else {
	    Log.e("404", "Could not initialize TextToSpeech.");
	    Intent installIntent = new Intent();
	    installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
	    startActivity(installIntent);
	}

    }

    @Override
    public void onDestroy() {
	if (mTts != null) {
	    mTts.stop();
	    mTts.shutdown();
	}
	super.onDestroy();
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	long selected_position = lang.getSelectedItemId();
	position = (int) selected_position;
	Locale[] locale = Locale.getAvailableLocales();
	int langID = (int) position;
	Log.i("Class_Learn", "onItemSelected langID=" + langID);
	String langString = langs[langID];
	Log.i("Class_Learn", "onItemSelected langString=" + langString);
	chosenLang = getLocaleByString(langString);
	Log.i("Class_Learn", "onItemSelected langLocale=" + chosenLang);

	Toast toast = Toast.makeText(getApplicationContext(), langString, Toast.LENGTH_SHORT);
	toast.show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	Log.i(this.getClass().toString(), "onActivityResult");

	switch (requestCode) {
	case RESULT_SPEECH: {
	    if (resultCode == RESULT_OK && null != data) {

		ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

		txtText.setText("-" + text.get(0) + "-");
		Toast toast = Toast.makeText(getApplicationContext(), "-" + text.get(0) + "-", Toast.LENGTH_SHORT);
		toast.show();
	    }
	    break;
	}

	}
    }
}