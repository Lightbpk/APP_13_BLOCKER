package bpk.light.app_13_blocker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {
    SharedPreferences sP;
    SharedPreferences.Editor sPe;
    EditText startText , stopText;
    Button btnStartServise;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sP = PreferenceManager.getDefaultSharedPreferences(this);
        sPe = sP.edit();
        startText = findViewById(R.id.startTime);
        stopText = findViewById(R.id.stopTime);
        btnStartServise = findViewById(R.id.btnStartService);
        startText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                sPe.putString("start1",startText.getText().toString());
                sPe.commit();
            }
        });
        stopText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                sPe.putString("stop1",stopText.getText().toString());
                sPe.commit();
            }
        });
        btnStartServise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(new Intent("bpk.light.app_13_blocker.action.service"));
                startActivity(new Intent(SettingsActivity.this,BlockActivity.class));
            }
        });
    }
}
