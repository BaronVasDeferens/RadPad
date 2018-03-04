package skot.radpad;


import android.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSamplePad extends android.app.Fragment implements View.OnTouchListener, View.OnLongClickListener, DialogConfigureSample.OnSampleSelectedListener {

    private View btnTriggerSample;
    private TextView sampleName;
    private File sampleDir;
    private SoundPlayer soundPlayer;


    public FragmentSamplePad() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sample_pad, container, false);
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        btnTriggerSample = getView().findViewById(R.id.btnTriggerSample);
        btnTriggerSample.setOnTouchListener(this);
        btnTriggerSample.setOnLongClickListener(this);

        sampleName = (TextView) getView().findViewById(R.id.sampleName);

        soundPlayer = new SoundPlayer();


        sampleDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), "drums");

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
            soundPlayer.playSound();
        return false;
    }

    @Override
    public boolean onLongClick(View v) {
        return configureSamplePad();
    }

    private boolean configureSamplePad() {

        System.out.println(">>> configuring sample pad...");

        DialogConfigureSample configureSample = new DialogConfigureSample();
        configureSample.setSampleDir(sampleDir);
        configureSample.setOnSampledSelectedListener(this);
        configureSample.show(getFragmentManager(), "O SHIT");

        return false;
    }


    @Override
    public void onSampleSelected(String sampleFileName) {
        if (sampleFileName != null && sampleFileName.length() > 0) {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "drums/" + sampleFileName);

            if (file.exists()) {
                soundPlayer.setSampleSource(file);
                sampleName.setText(sampleFileName);
            } else
                System.out.println(">>> ILLEGAL SAMPLE NAME!");
        }
    }
}
