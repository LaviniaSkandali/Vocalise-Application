package com.example.vocalise;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class songList extends AppCompatActivity {

    String[][] data;
    MyArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);
        RecyclerView songlist = findViewById(R.id.songlist);
        songlist.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyArrayAdapter(new String[0][0]); // Initialize the adapter with an empty array
        songlist.setAdapter(adapter);


        String[] buttonArray = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        for (int j = 0; j < 26; j++) {
            int buttonId = getResources().getIdentifier("button" + buttonArray[j], "id", getPackageName());
            Button button = (Button) findViewById(buttonId);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    readAndCheck(((Button) v).getText().charAt(0), songlist);
                }
            });
        }
        data = readCSVFile(this, "spotify_fifthysongdata.csv", true);
    }

    void readAndCheck(char letter, RecyclerView songlist) {
        String[][] songs = new String[data.length][3];
        for (int i = 0; i < data.length; i++) {
            songs[i][0] = data[i][0]+" "+data[i][1];
            songs[i][1] = String.valueOf( CheckLyrcisForLetter(Character.toLowerCase(letter), letter, data[i][3], 0));
        }

        Arrays.sort(songs, new Comparator<String[]>() {
            @Override
            public int compare(final String[] entry1, final String[] entry2) {
                final String time1 = entry1[1];
                final String time2 = entry2[1];
                return Integer.parseInt(time2) - Integer.parseInt(time1);
            }
        });

        MyArrayAdapter adapter = (MyArrayAdapter) songlist.getAdapter();
        adapter.setSongs(songs);

    }
    @SuppressLint("SuspiciousIndentation")
    static int CheckLyrcisForLetter(char letter, char letterCAP, String song, int index) {
        int count=0;
        if(index >= song.length())
            return 0;
        if(song.charAt(index) == letter || song.charAt(index) == letterCAP)
            count++;
            return count+CheckLyrcisForLetter( letter,letterCAP,song, index+1);
    }

    public static String[][] readCSVFile(@NonNull Context context, @NonNull String filename, boolean hasHeader) {
        AssetManager assetManager = context.getAssets();
        String line;
        String[][] data = null;

        try (InputStream inputStream = assetManager.open(filename);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            List<String[]> rows = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                rows.add(line.split(","));
            }

            if (hasHeader) {
                rows.remove(0);
            }

            int numRows = rows.size();
            int numCols = rows.get(0).length;

            data = new String[numRows][numCols];

            for (int i = 0; i < numRows; i++) {
                for (int j = 0; j < numCols; j++) {
                    data[i][j] = rows.get(i)[j];
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }
}