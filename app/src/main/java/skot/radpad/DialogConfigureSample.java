package skot.radpad;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by skot on 3/3/18.
 */

public class DialogConfigureSample extends DialogFragment implements AdapterView.OnItemClickListener, SeekBar.OnSeekBarChangeListener {

    LinearLayout myLayout;
    TextView playbackRateBox;
    SeekBar playbackRateSeekBar;


    ArrayList<String> fileNameArray;
    TextView selectedSampleDisplay;
    private String selectedSampleName = "";

    OnSampleSelectedListener listener;
    private int selectedPlaybackRate;

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        System.out.println(">>> progress : " + progress);
        selectedPlaybackRate = (int)((progress / 100f) * 88200);
        playbackRateBox.setText(Integer.toString(selectedPlaybackRate));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    interface OnSampleSelectedListener {
        void onSampleSelected(String sampleFileName, int playbackRate);
    }

    public void setOnSampledSelectedListener(final OnSampleSelectedListener listener) {
        this.listener = listener;
    }


    public void setSampleDir(final File sampleDir) {

        fileNameArray = new ArrayList<>();
        for (File f : sampleDir.listFiles()) {
            if (!f.isDirectory())
                fileNameArray.add(f.getName());
        }
    }

    public void setSelectedSampleName(final String selectedSampleName) {
        this.selectedSampleName = selectedSampleName;
    }

    public void setSelectedPlaybackRate(final int selectedPlaybackRate) {
        System.out.println(">>> selectedPlaybackRate = " + selectedPlaybackRate);
        this.selectedPlaybackRate = selectedPlaybackRate;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        myLayout = (LinearLayout) inflater.inflate(R.layout.dialog_configure_sample, null);
        builder.setView(myLayout);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.sample_selection, fileNameArray);

        ListView listView = (ListView) myLayout.findViewById(R.id.sampleList);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(this);

        selectedSampleDisplay = (TextView) myLayout.findViewById(R.id.selectedSample);

        playbackRateBox = (TextView) myLayout.findViewById(R.id.playbackRateBox);
        playbackRateBox.setText(Integer.toString(selectedPlaybackRate));

        playbackRateSeekBar = (SeekBar) myLayout.findViewById(R.id.playbackRateSeekBar);
        playbackRateSeekBar.setOnSeekBarChangeListener(this);
        playbackRateSeekBar.setProgress((int)((selectedPlaybackRate / 88200f)*100));
        System.out.println("NEW selectedPlaybackRate = " + selectedPlaybackRate);

        if (!selectedSampleName.contentEquals("")) {
            selectedSampleDisplay.setText(selectedSampleName);
        }

        builder.setMessage("Configure Sample Pad")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onSampleSelected(selectedSampleName, selectedPlaybackRate);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });


        // Create the AlertDialog object and return it
        return builder.create();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectedSampleName = fileNameArray.get(position);
        System.out.println(">>> SELECTION : " + selectedSampleName);
        selectedSampleDisplay.setText("SELECTED SAMPLE : " + selectedSampleName);
    }
}