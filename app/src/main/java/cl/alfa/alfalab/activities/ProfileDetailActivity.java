package cl.alfa.alfalab.activities;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import cl.alfa.alfalab.MainActivity;
import cl.alfa.alfalab.R;
import cl.alfa.alfalab.api.ApiClient;
import cl.alfa.alfalab.api.ApiService;
import cl.alfa.alfalab.fragments.ProfileBottomSheetDialog;
import cl.alfa.alfalab.models.AuthUser;
import cl.alfa.alfalab.models.User;
import cl.alfa.alfalab.utils.SharedPreferences;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static cl.alfa.alfalab.MainActivity.REQUEST_CODE;

public class ProfileDetailActivity extends AppCompatActivity implements ProfileBottomSheetDialog.BottomSheetListener {

    private final Context context = this;
    private SharedPreferences mSharedPreferences;
    private static final int PICK_IMAGE = 0;
    private static final int PIC_CROP = 1;

    private ImageView profileImage;
    private User user;

    public ProfileDetailActivity() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSharedPreferences = new SharedPreferences(context);
        if (mSharedPreferences.loadNightModeState()) {
            setTheme(R.style.AppThemeDark);
        } else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.profile_detail_activity_layout);

        //TODO: DELETE ACCOUNT ACTION
        final Toolbar mToolbar = findViewById(R.id.toolbar);
        final TextView toolbarTitle = mToolbar.findViewById(R.id.toolbar_title);
        profileImage = findViewById(R.id.profile_image);
        final TextInputLayout nameInputLayout = findViewById(R.id.email_input_layout),
                lastInputLayout = findViewById(R.id.password_input_layout);
        final TextInputEditText nameInputEditText = nameInputLayout.findViewById(R.id.email_edit_text),
                lastInputEditText = lastInputLayout.findViewById(R.id.password_edit_text);

        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (mSharedPreferences.loadNightModeState()) {
            mToolbar.setTitleTextAppearance(context, R.style.ToolbarTypefaceDark);
        } else {
            mToolbar.setTitleTextAppearance(context, R.style.ToolbarTypefaceLight);
        }
        toolbarTitle.setText(R.string.edit_profile_text);

        user = mSharedPreferences.getResponsible();
        if (user.getProfileImage() != null) {
            Glide.with(context).load(ContextCompat.getDrawable(context, R.drawable.ic_baseline_account_circle_24)).into(profileImage);
        } else {
            Glide.with(context).load(user.getProfileImage()).into(profileImage);
        }

        findViewById(R.id.profile_image_container).setOnClickListener(v -> new ProfileBottomSheetDialog().show(getSupportFragmentManager(), "profileBottomSheet"));

        nameInputLayout.setHint(Html.fromHtml(getString(R.string.order_client_firstname_hint)));
        lastInputLayout.setHint(Html.fromHtml(getString(R.string.order_client_lastname_hint)));
        nameInputEditText.setText(capitalizeFirstLetter(mSharedPreferences.getResponsible().getFirstname()));
        lastInputEditText.setText(capitalizeFirstLetter(mSharedPreferences.getResponsible().getLastname()));
        final ExtendedFloatingActionButton button = findViewById(R.id.button);

        nameInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 1) {
                    nameInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() < 1) {
                    nameInputLayout.setError(getResources().getString(R.string.required));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() < 1) {
                    nameInputLayout.setError(getResources().getString(R.string.required));
                }
            }
        });
        lastInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 1) {
                    lastInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() < 1) {
                    lastInputLayout.setError(getResources().getString(R.string.required));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() < 1) {
                    lastInputLayout.setError(getResources().getString(R.string.required));
                }
            }
        });

        button.setOnClickListener(v -> {
            if (nameInputEditText.getEditableText().length() < 1 && lastInputEditText.getEditableText().length() < 1) {
                Toast.makeText(context, "Ingresa tu nombre y apellido", Toast.LENGTH_SHORT).show();
                nameInputLayout.setError(getResources().getString(R.string.required));
                lastInputLayout.setError(getResources().getString(R.string.required));
            } else if (lastInputEditText.getEditableText().length() < 1) {
                lastInputLayout.setError(getResources().getString(R.string.required));
                Toast.makeText(context, "Ingresa tu apellido", Toast.LENGTH_SHORT).show();
            } else if (nameInputEditText.getEditableText().length() < 1) {
                Toast.makeText(context, "Ingresa tu nombre", Toast.LENGTH_SHORT).show();
                nameInputLayout.setError(getResources().getString(R.string.required));
            } else {
                updateUser(new User(nameInputEditText.getEditableText().toString(), lastInputEditText.getEditableText().toString()));
            }
        });

    }

    private String capitalizeFirstLetter(String s) {

        final String[] in = s.split("^/([ ]?\\.?[a-zA-Z])+/$");
        final StringBuilder out = new StringBuilder();
        for (String ss : in) {
            Log.d(MainActivity.API, ss);
            final String upperString = ss.substring(0, 1).toUpperCase() + ss.substring(1).toLowerCase();
            out.append(upperString);
            out.append(" ");
        }
        return out.toString();
    }

    private void updateUser(User newUser) {
        final ApiService.UpdateUserService service = ApiClient.getClient().create(ApiService.UpdateUserService.class);
        final Call<ResponseBody> responseCall = service.updateUser(mSharedPreferences.getToken(), newUser);

        responseCall.enqueue(new Callback<ResponseBody>() {

            JsonElement token = null;
            String refreshToken = null;

            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    final Gson gson = new Gson();
                    final JsonObject jsonObject;
                    try {
                        jsonObject = gson.fromJson(response.body().string(), JsonObject.class);
                        token = jsonObject.get("token");
                        refreshToken = Objects.requireNonNull(response.headers().get("Refresh-Token"));
                        refreshToken(mSharedPreferences.getToken());
                        /*if(profileImage.getDrawable() != ContextCompat.getDrawable(context, R.drawable.ic_baseline_account_circle_24)) {
                            //TODO: 🚧 WORK IN PROGRESS - UPLOAD IMAGE
                        }*/
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT).show();
                    Log.e(MainActivity.API, "getData - onResponse (errorBody): " + response.errorBody());
                    Log.e(MainActivity.API, "getData - onResponse (response.raw): " + response.raw().toString());
                    Log.e(MainActivity.API, "getData - onResponse (response.raw): " + call.request().method());
                    Log.e(MainActivity.API, "getData - onResponse (message):" + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.d(MainActivity.API, "getData - onFailure: " + t.getMessage());
                Log.e(MainActivity.API, "getData - onFailure: " + t.getLocalizedMessage());
                Toast.makeText(context, context.getResources().getString(R.string.feed_update_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void refreshToken(@NonNull String token) {
        final ApiService.RefreshTokenService service = ApiClient.getClient().create(ApiService.RefreshTokenService.class);
        final Call<ResponseBody> responseCall = service.refreshToken(token);

        responseCall.enqueue(new Callback<ResponseBody>() {

            JsonElement token = null;
            String refreshToken = null;

            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                if (response.body() != null) {
                    try {
                        final Gson gson = new Gson();
                        final JsonObject jsonObject = gson.fromJson(response.body().string(), JsonObject.class);
                        token = jsonObject.get("token");
                        refreshToken = Objects.requireNonNull(response.headers().get("Refresh-Token"));

                        mSharedPreferences.setToken(refreshToken.replace("\"", ""));

                        if (response.isSuccessful()) {
                            final ApiService.GetUserService userService = ApiClient.getClient().create(ApiService.GetUserService.class);
                            final Call<AuthUser> userResponseCall = userService.getUser(mSharedPreferences.getToken().replace("\"", ""));

                            userResponseCall.enqueue(new Callback<AuthUser>() {
                                @Override
                                public void onResponse(@NonNull Call<AuthUser> call, @NonNull Response<AuthUser> response) {
                                    if (response.isSuccessful()) {
                                        assert response.body() != null;
                                        mSharedPreferences.setResponsible(response.body());
                                        AuthUser authUser = response.body();
                                        user = new User(authUser.getFirstname(), authUser.getLastname(), authUser.getEmail(), null, authUser.getProfileImage());
                                        startActivity(new Intent(context, SettingsActivity.class));
                                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                        finish();
                                    } else {
                                        Toast.makeText(context, getResources().getString(R.string.wrong_credentials), Toast.LENGTH_SHORT).show();
                                        Log.e(MainActivity.API, "GetUserService - onResponse (errorBody): " + response.errorBody());
                                        Log.e(MainActivity.API, "GetUserService - onResponse (response.raw): " + response.raw().toString());
                                        Log.e(MainActivity.API, "GetUserService - onResponse (response.raw): " + call.request().method());
                                        Log.e(MainActivity.API, "GetUserService - onResponse (message):" + response.message());
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<AuthUser> call, @NonNull Throwable t) {
                                    Toast.makeText(context, getResources().getString(R.string.wrong_credentials), Toast.LENGTH_SHORT).show();
                                    Log.d(MainActivity.API, "GetUserService - onFailure: " + t.getMessage());
                                    Log.e(MainActivity.API, "GetUserService - onFailure: " + t.getLocalizedMessage());
                                }

                            });

                        } else {
                            Log.e(MainActivity.API, "RefreshTokenService - onResponse (response.errorBody): " + response.errorBody());
                            Log.e(MainActivity.API, "RefreshTokenService - onResponse (response.raw): " + response.raw().toString());
                            Log.e(MainActivity.API, "RefreshTokenService - onResponse (response.message): " + response.message());
                            Log.e(MainActivity.API, "RefreshTokenService - onResponse (call.request.body): " + call.request().body());
                            Toast.makeText(context, context.getResources().getString(R.string.wrong_credentials), Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, getResources().getString(R.string.wrong_credentials), Toast.LENGTH_SHORT).show();
                    Log.e(MainActivity.API, "RefreshTokenService - onResponse (errorBody): " + response.errorBody());
                    Log.e(MainActivity.API, "RefreshTokenService - onResponse (response.raw): " + response.raw().toString());
                    Log.e(MainActivity.API, "RefreshTokenService - onResponse (response.raw): " + call.request().method());
                    Log.e(MainActivity.API, "RefreshTokenService - onResponse (message):" + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(context, context.getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT).show();
                Log.d(MainActivity.API, "login - onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(context, SettingsActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(context, SettingsActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onButtonClicked(String text) {
        if (text.equals("edit")) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            } else {

                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK);
                pickIntent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, context.getResources().getString(R.string.image_select));
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                //noinspection deprecation
                startActivityForResult(pickIntent, PICK_IMAGE);
            }
        } else {
            profileImage.setPadding(0, 0, 0, 0);
            profileImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_account_circle_24));
        }
    }

    private void performCrop(Uri picUri) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(picUri, "image/*");
            cropIntent.putExtra("crop", true);
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 128);
            cropIntent.putExtra("outputY", 128);
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
            cropIntent.putExtra("return-data", true);
            cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivityForResult(cropIntent, PIC_CROP);
        } catch (ActivityNotFoundException error) {
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                //TODO: GET IMAGE CROP FROM GOOGLE PHOTOS
                if (imageUri.toString().startsWith("content://com.google.android.apps.photos.content")) {
                    profileImage.setImageBitmap(selectedImage);
                } else {
                    performCrop(imageUri);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        } else if (resultCode == RESULT_OK && requestCode == PIC_CROP) {
            if (data != null) {
                Bundle extras = data.getExtras();
                Bitmap bitmap = extras.getParcelable("data");
                profileImage.setImageBitmap(bitmap);
            }
        }
    }

}