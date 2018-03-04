package skot.radpad;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<FragmentSamplePad> fragmentSamplePads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.app.FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        LinearLayout rowOne = (LinearLayout) findViewById(R.id.rowOne);
        LinearLayout rowTwo = (LinearLayout) findViewById(R.id.rowTwo);
        LinearLayout rowThree = (LinearLayout) findViewById(R.id.rowThree);
        LinearLayout rowFour = (LinearLayout) findViewById(R.id.rowFour);

        fragmentSamplePads = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            FragmentSamplePad pad = new FragmentSamplePad();
            fragmentSamplePads.add(pad);
            fragmentTransaction.add(R.id.rowOne, pad);

            pad = new FragmentSamplePad();
            fragmentSamplePads.add(pad);
            fragmentTransaction.add(R.id.rowTwo, pad);

            pad = new FragmentSamplePad();
            fragmentSamplePads.add(pad);
            fragmentTransaction.add(R.id.rowThree, pad);

            pad = new FragmentSamplePad();
            fragmentSamplePads.add(pad);
            fragmentTransaction.add(R.id.rowFour, pad);
        }

        fragmentTransaction.commit();

    }
}
