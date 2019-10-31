package com.example.qrscan;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.concurrent.Executor;

import static androidx.biometric.BiometricConstants.ERROR_NEGATIVE_BUTTON;

public class MainActivity extends AppCompatActivity  {

    private static final String TAG = MainActivity.class.getName();;
    private BiometricPrompt biometricPrompt = null;
    private Executor executor = new MainThreadExecutor();

        private BiometricPrompt.AuthenticationCallback callback = new BiometricPrompt.AuthenticationCallback() {
        @Override
        public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
            super.onAuthenticationError(errorCode, errString);
            if (errorCode == ERROR_NEGATIVE_BUTTON && biometricPrompt != null)
                biometricPrompt.cancelAuthentication();
            toast(errString.toString());
        }

        @Override
        public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);
            toast("Authentication succeed");
            Log.d(TAG, "Intent Begin");
            Intent i = new Intent(MainActivity.this, LoadingFirebase.class);
            startActivity(i);

        }

        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
            toast("Application did not recognize the placed finger print. Please try again!");

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (biometricPrompt == null)
            biometricPrompt = new BiometricPrompt(this,executor,callback);

          findViewById(R.id.selectFingerPrintButton).setOnClickListener(view -> {
              BiometricPrompt.PromptInfo promptInfo= buildBiometricPrompt();
              biometricPrompt.authenticate(promptInfo);
          });

    }
    private BiometricPrompt.PromptInfo buildBiometricPrompt(){
        return new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Login")
                .setSubtitle("Login into your account")
                .setDescription("Touch your finger on the finger print sensor to authorise your account.")
                .setNegativeButtonText("Cancel")
                .build();
    }
    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
