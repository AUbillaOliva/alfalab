package cl.alfa.alfalab.fragments.intro_fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import cl.alfa.alfalab.MainActivity;
import cl.alfa.alfalab.MainApplication;
import cl.alfa.alfalab.R;
import cl.alfa.alfalab.activities.OnBoardingActivity;
import cl.alfa.alfalab.api.ApiClient;
import cl.alfa.alfalab.api.ApiService;
import cl.alfa.alfalab.models.User;
import cl.alfa.alfalab.utils.SharedPreferences;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThirdIntroFragment extends Fragment {

    private Context context = MainApplication.getContext();
    private static TextInputEditText firstnameInputEditText, lastInputEditText, emailInputEditText, passwordInputEditText, confirmPasswordInputEditText;
    private TextInputLayout firstnameInputLayout, lastnameInputLayout, emailInputLayout, passwordInputLayout, confirmPasswordInputLayout;
    private ProgressDialog progressDialog;
    private SharedPreferences mSharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.third_intro_fragment_layout, container, false);

        mSharedPreferences = new SharedPreferences(context);

        final Toolbar mToolbar = view.findViewById(R.id.toolbar);
        final ExtendedFloatingActionButton button = view.findViewById(R.id.button);
        final TextView signInText = view.findViewById(R.id.sign_up_text_link);
        firstnameInputLayout = view.findViewById(R.id.firstname_intro_input_layout);
        lastnameInputLayout = view.findViewById(R.id.password_input_layout);
        firstnameInputEditText = view.findViewById(R.id.firstname_intro_edit_text);
        firstnameInputLayout.setHint(Html.fromHtml(getString(R.string.order_client_firstname_hint)));
        lastInputEditText = view.findViewById(R.id.password_edit_text);
        lastnameInputLayout.setHint(Html.fromHtml(getString(R.string.order_client_lastname_hint)));
        emailInputLayout = view.findViewById(R.id.email_intro_input_layout);
        emailInputLayout.setHint(Html.fromHtml(getString(R.string.login_email_hint)));
        emailInputEditText = emailInputLayout.findViewById(R.id.email_intro_edit_text);
        passwordInputLayout = view.findViewById(R.id.password_intro_input_layout);
        passwordInputLayout.setHint(Html.fromHtml(getString(R.string.login_password_hint)));
        passwordInputEditText = passwordInputLayout.findViewById(R.id.password_intro_edit_text);
        confirmPasswordInputLayout = view.findViewById(R.id.confirm_password_intro_input_layout);
        confirmPasswordInputLayout.setHint(Html.fromHtml(getResources().getString(R.string.confirm_password_hint)));
        confirmPasswordInputEditText = confirmPasswordInputLayout.findViewById(R.id.confirm_password_intro_edit_text);

        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        mToolbar.setNavigationOnClickListener(v -> OnBoardingActivity.setPosition(0));

        signInText.setOnClickListener(v -> OnBoardingActivity.setSignInItemPosition());

        firstnameInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() > 1) {
                    firstnameInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() < 1) {
                    firstnameInputLayout.setError(getResources().getString(R.string.required));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() < 1) {
                    firstnameInputLayout.setError(getResources().getString(R.string.required));
                }
            }
        });
        lastInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() > 1) {
                    lastnameInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() < 1) {
                    lastnameInputLayout.setError(getResources().getString(R.string.required));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() < 1) {
                    lastnameInputLayout.setError(getResources().getString(R.string.required));
                }
            }
        });
        emailInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() > 1) {
                    emailInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() < 1) {
                    emailInputLayout.setError(getResources().getString(R.string.required));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() < 1) {
                    emailInputLayout.setError(getResources().getString(R.string.required));
                }
            }
        });
        passwordInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() > 1) {
                    passwordInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() < 1) {
                    passwordInputLayout.setError(getResources().getString(R.string.required));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() < 1) {
                    passwordInputLayout.setError(getResources().getString(R.string.required));
                }
            }
        });
        confirmPasswordInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() > 1) {
                    confirmPasswordInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() < 1) {
                    confirmPasswordInputLayout.setError(getResources().getString(R.string.required));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() < 1) {
                    confirmPasswordInputLayout.setError(getResources().getString(R.string.required));
                }
            }
        });

        button.setOnClickListener(v -> validateForm());

        return view;
    }

    private void validateForm() {
        boolean isCorrect = true;
        if(firstnameInputEditText.getEditableText().toString().isEmpty()) {
            firstnameInputLayout.setError(getResources().getString(R.string.required));
            isCorrect = false;
        } else {
            firstnameInputLayout.setError(null);
        }
        if(lastInputEditText.getEditableText().toString().isEmpty()) {
            lastnameInputLayout.setError(getResources().getString(R.string.required));
            isCorrect = false;
        } else {
            lastnameInputLayout.setError(null);
        }
        if(emailInputEditText.getEditableText().toString().isEmpty()) {
            emailInputLayout.setError(getResources().getString(R.string.required));
            isCorrect = false;
        } else {
            emailInputLayout.setError(null);
        }
        if(passwordInputEditText.getEditableText().toString().isEmpty()) {
            passwordInputLayout.setError(getResources().getString(R.string.required));
            isCorrect = false;
        } else {
            passwordInputLayout.setError(null);
        }
        if(confirmPasswordInputEditText.getEditableText().toString().isEmpty()) {
            confirmPasswordInputLayout.setError(getResources().getString(R.string.required));
            isCorrect = false;
        } else {
            confirmPasswordInputLayout.setError(null);
        }
        if(isCorrect) {
            signup(String.valueOf(firstnameInputEditText.getText()), String.valueOf(lastInputEditText.getText()), String.valueOf(emailInputEditText.getText()), String.valueOf(passwordInputEditText.getText()));
        }
    }

    private void signup(@NonNull String firstname, @NonNull String lastname, @NonNull String email, @NonNull String password) {
        progressDialog = ProgressDialog.show(getActivity(), "🙌 Creando cuenta...",  "Esto puede tardar un poco.", true);

        final ApiService.SignUpService service = ApiClient.getClient().create(ApiService.SignUpService.class);
        final Call<ResponseBody> responseCall = service.signUp(new User(firstname, lastname, email, password));

        Log.e(MainActivity.API, responseCall.toString());

        responseCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()) {
                    Toast.makeText(context, "Cuenta creada", Toast.LENGTH_SHORT).show();
                    mSharedPreferences.setResponsible(new User(firstnameInputEditText.getEditableText().toString(), lastInputEditText.getEditableText().toString()));
                    OnBoardingActivity.setLastItemPosition();
                } else {
                    Log.e(MainActivity.API, "onResponse (errorBody): " + response.errorBody());
                    Log.e(MainActivity.API, "onResponse (response.raw): " + response.raw().toString());
                    Log.e(MainActivity.API, "onResponse (message): " + response.message());
                    Log.e(MainActivity.API, "onResponse (call): " + call.request().body());
                    Toast.makeText(context, context.getResources().getString(R.string.wrong_credentials), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, context.getResources().getString(R.string.feed_update_error), Toast.LENGTH_SHORT).show();
                Log.d(MainActivity.API, "onFailure: " + t.getMessage());
            }
        });
    }

    public static Fragment newInstance() {
        return new ThirdIntroFragment();
    }

}

