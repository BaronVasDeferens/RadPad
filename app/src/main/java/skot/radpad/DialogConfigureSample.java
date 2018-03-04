package skot.radpad;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by skot on 3/3/18.
 */

public class DialogConfigureSample extends DialogFragment implements AdapterView.OnItemClickListener {

    LinearLayout myLayout;

    private File sampleDir;
    ArrayList<String> fileNameArray;

    private String selectedSampleName = "";

    OnSampleSelectedListener listener;

    interface OnSampleSelectedListener {
        void onSampleSelected(String sampleFileName);
    }

    public void setOnSampledSelectedListener(final OnSampleSelectedListener listener) {
        this.listener = listener;
    }


    public void setSampleDir(final File sampleDir) {
        this.sampleDir = sampleDir;

        fileNameArray = new ArrayList<>();
        for (File f : sampleDir.listFiles()) {
            if (!f.isDirectory())
                fileNameArray.add(f.getName());
        }
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


        builder.setMessage("Configure Sample Pad")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onSampleSelected(selectedSampleName);
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
        TextView selectedSampleDisplay = (TextView) myLayout.findViewById(R.id.selectedSample);
        selectedSampleDisplay.setText(selectedSampleName);
    }
}