package com.polysfactory.levenshteindistancesearchsample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class MainActivity extends Activity {

	private static final String TAG = "LDSS";
	private EditText mNameField;
	private TextView mSearchResult;
	private List<String> mNames;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mNames = loadNames();
		mNameField = (EditText) findViewById(R.id.editText);
		mSearchResult = (TextView) findViewById(R.id.searchResult);
		Button searchButton = (Button) findViewById(R.id.button);
		searchButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String name = mNameField.getText().toString();
				List<Result> result = search(name);
				mSearchResult.setText(result.toString());
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private List<String> loadNames() {
		List<String> result = new ArrayList<String>();
		InputStream inputStream = getResources().openRawResource(R.raw.names);
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		try {
			String s;
			while ((s = bufferedReader.readLine()) != null) {
				result.add(s);
			}
		} catch (IOException e) {
			Log.d(TAG, "io error", e);
		}
		return result;
	}

	private List<Result> search(String input) {
		List<Result> results = new ArrayList<Result>();
		Queue<Result> minHeap = new PriorityQueue<Result>();
		for (String name : mNames) {
			int levenshteinDistance = StringUtils.getLevenshteinDistance(name, input);
			Result r = new Result();
			r.name = name;
			r.distance = levenshteinDistance;
			minHeap.add(r);
		}
		for (int i = 0; i < 5 && !minHeap.isEmpty(); i++) {
			results.add(minHeap.poll());
		}
		return results;
	}

	private class Result implements Comparable<Result> {
		int distance;
		String name;

		@Override
		public int compareTo(Result another) {
			return this.distance - another.distance;
		}

		@Override
		public String toString() {
			return "name=" + name + ", distance=" + distance + "\n";
		}
	}
}
