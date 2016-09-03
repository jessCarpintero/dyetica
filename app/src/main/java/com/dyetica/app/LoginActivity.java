package com.dyetica.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dyetica.app.persistence.ClientHTTP;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private AttemptLogin mAuthTask = null;
    // UI references.
    private EditText mUserNameView;
    private EditText mPasswordView;
    private TextView mLinkRegisterView;
    private View mProgressView;
    private View mLoginScrollFormView;

    /**
     * Parameters preferences
     */
    private SharedPreferences _prefs;
    private SharedPreferences.Editor _prefsEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        _prefs = getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
        _prefsEditor = _prefs.edit();
        // Set up the login form.
        mUserNameView = (EditText) findViewById(R.id.user_text);

        mPasswordView = (EditText) findViewById(R.id.password);

        mLinkRegisterView = (TextView) findViewById(R.id.link_register);

        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginScrollFormView = findViewById(R.id.login_scroll_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mUserNameView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        Map<String, String> success = new HashMap<>();
        Boolean error = true;

        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUserNameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String userName = mUserNameView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();

        Log.d("LoginActivity", "Valor de userName: " + userName);
        Log.d("LoginActivity", "Valor de contraseña: " + password);


        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            Log.d("LoginActivity", "contraseña vacia");
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(userName)) {
            Log.d("LoginActivity", "nombre de usuario vacio");

            mUserNameView.setError(getString(R.string.error_field_required));
            focusView = mUserNameView;
            cancel = true;
        }

        if (cancel) {
            Log.d("LoginActivity", "Dentro de cancel");

            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            Log.d("LoginActivity", "No deberia ESTA AQUI");

            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new AttemptLogin(userName, password);
            try {
                success = mAuthTask.execute((Void) null).get();
                Log.d("VERBOSE", "Despues de success: " + success.toString());

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }


            error = Boolean.parseBoolean(success.get(getString(R.string.error)));
            Log.d("VERBOSE", "Valor de ERROR: " + error);

            if (!error) {
                Log.d("VERBOSE", "Valor de ERROR: " + error);
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                finish();
                startActivity(i);
            } else {
                Toast.makeText(this, success.get(getString(R.string.message)), Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginScrollFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginScrollFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginScrollFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginScrollFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    class AttemptLogin extends AsyncTask<Void, Void, Map<String, String>> {
        private final String mUserName;
        private final String mPassword;
        private String error, message, statusCode, params;
        private Map<String, String> success = new HashMap<>();

        AttemptLogin(String username, String password) {
            mUserName = username;
            mPassword = password;
        }


        @Override
        protected Map<String, String> doInBackground(Void... args) {
            // Check for success tag
            try {
                // Building Parameters
                Log.v("VERBOSE", "VAlor de username" + mUserName);
                Log.v("VERBOSE", "VAlor de pass" + mPassword);
                Log.v("VERBOSE", "url" + getString(R.string.url_login));
                params = "username=" + mUserName + "&password=" + encode64(mPassword);
                Log.d("request!", "starting, valor de params: " + params);

                // getting product details by making HTTP request
                ClientHTTP clientHTTP = new ClientHTTP();
                Map<String, String> response = clientHTTP.makeHttpRequest(new URL(getString(R.string.url_login)), "POST", "", params);

                // check your log for json response
                Log.d("VERBOSE", response.toString());
                statusCode = response.get(getString(R.string.status));
                success.put(getString(R.string.status), statusCode);
                if (!getString(R.string.status_200).equals(statusCode)){
                    Log.d("VERBOSE", "ERROR distinto de 200");
                    success.put(getString(R.string.error), "true");
                    success.put(getString(R.string.message), getString(R.string.error_connection));
                } else {
                    Log.d("VERBOSE", "Status igual 200");
                    error = response.get("error");
                    message = response.get("message");
                    if (error == "false") {
                        Log.d("VERBOSE", "Valor de mensaje: " + message);
                        JSONObject json = new JSONObject(message);
                        saveUserDBAndPreferences(json);
                       // if (_prefs.getInt("idUserCurrent", 0) == idUser || _prefs.getInt("idUserCurrent", 0) == 0)
                         //   _prefsEditor.putInt("idUserCurrent", idUser);
                        //else {
                          //  _prefsEditor.putInt("idUserOld", _prefs.getInt("idUserCurrent", 0));
                        //     _prefsEditor.putInt("idUserCurrent", idUser);
                        //   }
                        //    if (remember.isChecked()) {
                        //      _prefsEditor.putString("email", mail.getText().toString().trim());
                        //      _prefsEditor.putString("password", pass.getText().toString().trim());
                        //    } else {
                        //     _prefsEditor.putString("email", "");
                        //      _prefsEditor.putString("password", "");
                        //   }
                        //   _prefsEditor.commit();
                        //  if (dbManager.getUser(idUser) == null) {
                        //       dbManager.addUser(new UserVO(idUser, json.getString("email"), json.getString("name"), json.getString("apiKey"), json.getString("createdAt"), "", 0, 0));
                        //    }
                    }
                    Log.d("VERBOSE", "Escribiendo success");

                    success.put(getString(R.string.error), error);
                    success.put(getString(R.string.message), message);
                    Log.d("VERBOSE", "Terminando success");

                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Log.d("VERBOSE", "Enviando success");

            return success;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        @Override
        protected void onPostExecute(final Map<String, String> values) {
            mAuthTask = null;
            showProgress(false);
        }

        private String encode64(String mPassword){
            return Base64.encodeToString(mPassword.getBytes(), Base64.DEFAULT);
        }

        private void saveUserDBAndPreferences(JSONObject jsonObject){

        }


    }


}

