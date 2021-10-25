package phucdv.android.magicnote.authentic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import phucdv.android.magicnote.R;

public class BackUpActivity extends AppCompatActivity {

    public static final String BACK_UP_RESTORE="back_up_restore";
    public static final String AUTO_BACKUP="auto_backup";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private FirebaseUser firebaseUser;

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_up);

        SwitchCompat switchAutoBackup = findViewById(R.id.switch_auto_backup);

        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);

        boolean autoBackup=sharedPreferences.getBoolean(AUTO_BACKUP, false);

        if (autoBackup){
            switchAutoBackup.setChecked(true);
        }else {
            switchAutoBackup.setChecked(false);
        }

        switchAutoBackup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    editor = sharedPreferences.edit();
                    editor.putBoolean(AUTO_BACKUP,true);
                    editor.apply();
                }else {
                    editor = sharedPreferences.edit();
                    editor.putBoolean(AUTO_BACKUP,false);
                    editor.apply();
                }
            }
        });
    }

    public void backUpNote(View view) {
        editor = sharedPreferences.edit();
        editor.putString(BACK_UP_RESTORE,"Backup");
        editor.apply();

//        BackUpRestoreJobIntentService.enqueueWork(this,new Intent(this,BackUpRestoreJobIntentService.class));
    }

    public void restoreNote(View view) {
        editor = sharedPreferences.edit();
        editor.putString(BACK_UP_RESTORE,"Restore");
        editor.apply();

//        BackUpRestoreJobIntentService.enqueueWork(this,new Intent(this,BackUpRestoreJobIntentService.class));
    }

    public void logout(View view) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(this,LoginActivity.class));
    }
}
