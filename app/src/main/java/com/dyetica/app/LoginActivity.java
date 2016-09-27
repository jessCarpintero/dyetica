package com.dyetica.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
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
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dyetica.app.model.User;
import com.dyetica.app.persistence.ClientHTTP;
import com.dyetica.app.persistence.DBManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.MalformedURLException;
import java.net.URL;
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
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbManager = DBManager.getInstance(this);

        // Set up the login form.
        mUserNameView = (EditText) findViewById(R.id.user_text);

        mPasswordView = (EditText) findViewById(R.id.password);

        //Link to http://dyetica.com/hazte-socio
        mLinkRegisterView = (TextView) findViewById(R.id.link_register);
        mLinkRegisterView.setText(Html.fromHtml("¿No estás registrado todavía? <a href=\"http://dyetica.com/hazte-socio\">Accede a la web </a>"));
        mLinkRegisterView.setMovementMethod(LinkMovementMethod.getInstance());

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

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        Map<String, String> success = new HashMap<>();
        boolean error = true;

        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUserNameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String userName = mUserNameView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(userName)) {
            mUserNameView.setError(getString(R.string.error_field_required));
            focusView = mUserNameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new AttemptLogin(userName, password);
            try {
                success = mAuthTask.execute((Void) null).get();
            } catch (InterruptedException e) {
                Log.e("LoginActivity", "Attempt login have been interripted: " + e.getMessage());
            } catch (ExecutionException e) {
                Log.e("VERBOSE", "Attempt login have been in execution error: " + e.getMessage());
            }

            error = Boolean.parseBoolean(success.get(getString(R.string.error)));
           if (!error) {
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
        private String error, message, statusCode;
        private StringBuilder params = new StringBuilder();
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
                params.append("username=").append(mUserName).append("&password=").append(encode64(mPassword));

                // getting product details by making HTTP request
                Map<String, String> response = ClientHTTP.makeHttpRequest(new URL(getString(R.string.url_login)), "POST", "", params.toString());

                // check your log for json response
                Log.d("VERBOSE", response.toString());
                statusCode = response.get(getString(R.string.status));
                success.put(getString(R.string.status), statusCode);
                if (!getString(R.string.status_200).equals(statusCode)){
                    success.put(getString(R.string.error), "true");
                    success.put(getString(R.string.message), getString(R.string.error_connection));
                } else {
                    error = response.get("error");
                    message = response.get("message");
                    if (error == "false") {
                        Log.d("VERBOSE", "Valor de mensaje: " + message);
                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                        User user = User.getInstance(gson.fromJson(message, User.class));
                        savePreferences(user);
                        if (dbManager.getUser(user.getId()) == null) {
                            dbManager.addUser(user);
                        } else {
                            dbManager.updateUser(user);
                        }
                    }
                    success.put(getString(R.string.error), error);
                    success.put(getString(R.string.message), message);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
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

        private void savePreferences(User user){
            SharedPreferences prefs = getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = prefs.edit();

            prefsEditor.putInt("idUser", user.getId());
            prefsEditor.putString("apiKey", user.getApiKey());
            prefsEditor.commit();
        }
    }


}

