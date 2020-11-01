package cl.alfa.alfalab.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import cl.alfa.alfalab.R;
import cl.alfa.alfalab.api.ApiClient;
import cl.alfa.alfalab.tools.MultipartRequest;
import cl.alfa.alfalab.utils.SharedPreferences;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static cl.alfa.alfalab.MainActivity.REQUEST_CODE;

public class ReportActivity extends AppCompatActivity {

    private final Context context = this;
    public static final int PICK_IMAGE = 0;
    public static final String USER_KEY = "user", PASS_KEY = "pass";
    private String USER_VAL, PASS_VAL;
    private ImageView imageView, delete;
    private TextInputEditText editText;
    private Intent pickIntent, getIntent, chooserIntent;
    private File file;
    private final MediaType MEDIA_TYPE = MediaType.parse("image/*");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        }

        try {
            final JSONObject obj = new JSONObject(Objects.requireNonNull(loadJSONFromAsset()));
            final JSONObject acraSettings = obj.getJSONObject("acra-settings");
            USER_VAL = acraSettings.getString("acra-user");
            PASS_VAL = acraSettings.getString("acra-pass");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final SharedPreferences mSharedPreferences = new SharedPreferences(context);
        if(mSharedPreferences.loadNightModeState()) {
            setTheme(R.style.AppThemeDark);
        } else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.report_activity_layout);

        final Toolbar mToolbar = findViewById(R.id.toolbar);
        final TextView toolbarTitle = mToolbar.findViewById(R.id.toolbar_title);
        editText = findViewById(R.id.edit_text);
        imageView = findViewById(R.id.report_image);
        delete = findViewById(R.id.delete);

        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if(mSharedPreferences.loadNightModeState()) {
            mToolbar.setTitleTextAppearance(context, R.style.ToolbarTypefaceDark);
        } else {
            mToolbar.setTitleTextAppearance(context, R.style.ToolbarTypefaceLight);
        }
        toolbarTitle.setText(R.string.report_error);

        imageView.setOnClickListener(v -> {
            if(ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
            } else {
                if(hasNullOrEmptyDrawable(imageView)) {
                    getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    getIntent.setType("image/*");

                    pickIntent = new Intent(Intent.ACTION_PICK);
                    pickIntent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");

                    chooserIntent = Intent.createChooser(getIntent, context.getResources().getString(R.string.image_select));
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                    //noinspection deprecation
                    startActivityForResult(pickIntent, PICK_IMAGE);
                } else {
                    imageView.setImageBitmap(null);
                    imageView.setPadding(90,90,90,90);
                    imageView.setImageDrawable(getDrawable(R.drawable.ic_add_24dp));
                    delete.setVisibility(View.GONE);
                }
            }
        });

    }

    public static boolean hasNullOrEmptyDrawable(ImageView iv) {
        final BitmapDrawable bitmapDrawable = iv.getDrawable() instanceof BitmapDrawable ? (BitmapDrawable) iv.getDrawable() : null;
        return bitmapDrawable == null || bitmapDrawable.getBitmap() == null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            InputStream inputStream = null;

            try {
                inputStream = context.getContentResolver().openInputStream(Objects.requireNonNull(data.getData()));
            } catch (FileNotFoundException e){
                e.printStackTrace();
            }

            final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            final Uri uri = data.getData();

            try {
                final InputStream is = this.getContentResolver().openInputStream(uri);
                if (is != null) {
                    final Bitmap pictureBitmap = BitmapFactory.decodeStream(is);
                    file = new File(context.getCacheDir(), "image.png");

                    final ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    pictureBitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
                    final byte[] bitmapdata = bos.toByteArray();

                    final FileOutputStream fos = new FileOutputStream(file);
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageView.setPadding(0,0,0,0);
            imageView.setImageBitmap(bitmap);
            delete.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_report, menu);

        final MenuItem send = menu.findItem(R.id.send);
        final SpannableString s = new SpannableString(getResources().getString(R.string.send).toUpperCase());
        s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)), 0, s.length(), 0);
        send.setTitle(s);

        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final ProgressDialog progressDialog;
        switch (item.getItemId()) {
            case android.R.id.home:
                if(Objects.requireNonNull(editText.getText()).length() != 0 || !hasNullOrEmptyDrawable(imageView)) {
                    final AlertDialog dialog = new AlertDialog.Builder(context)
                            .setView(R.layout.confirmation_dialog_layout)
                            .show();
                    Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    final ExtendedFloatingActionButton positiveButton = dialog.findViewById(R.id.confirm_button);
                    assert positiveButton != null;
                    positiveButton.setText(R.string.positive_discard_dialog_button);
                    positiveButton.setOnClickListener(v -> {
                        dialog.dismiss();
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                    });
                    final TextView dialogTitle = dialog.findViewById(R.id.dialog_title);
                    assert dialogTitle != null;
                    dialogTitle.setText(R.string.discard_dialog_report_title);
                    final TextView dialogSubtitle = dialog.findViewById(R.id.dialog_subtitle);
                    assert dialogSubtitle != null;
                    dialogSubtitle.setText(R.string.discard_dialog_report_subtitle);
                    final TextView negative = dialog.findViewById(R.id.negative_button);
                    assert negative != null;
                    negative.setText(R.string.cancel_dialog_buttton);
                    negative.setOnClickListener(v -> dialog.dismiss());
                } else {
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }
                break;
            case R.id.send:
                if(USER_VAL != null && PASS_VAL != null) {
                    if(!hasNullOrEmptyDrawable(imageView) && Objects.requireNonNull(editText.getText()).length() != 0) {
                        progressDialog = ProgressDialog.show(context, context.getResources().getString(R.string.sending), context.getResources().getString(R.string.sending_report), true);
                        final MultipartRequest request = new MultipartRequest(context, progressDialog);
                        request.addString(USER_KEY, USER_VAL);
                        request.addString(PASS_KEY, PASS_VAL);
                        request.addFile("file", RequestBody.create(MEDIA_TYPE, file), file.getName());
                        request.addString("message", Objects.requireNonNull(editText.getText()).toString());
                        request.execute(ApiClient.REPORT_URL);
                        request.onCode(getResources().getString(R.string.report_max_size), 504);
                    } else if(!hasNullOrEmptyDrawable(imageView) && Objects.requireNonNull(editText.getText()).length() == 0) {
                        progressDialog = ProgressDialog.show(context, context.getResources().getString(R.string.sending), context.getResources().getString(R.string.sending_report), true);
                        final MultipartRequest request = new MultipartRequest(context, progressDialog);
                        request.addString(USER_KEY, USER_VAL);
                        request.addString(PASS_KEY, PASS_VAL);
                        request.addFile("file", RequestBody.create(MEDIA_TYPE, file), file.getName());
                        request.execute(ApiClient.REPORT_URL);
                        request.onCode(getResources().getString(R.string.report_max_size), 504);
                    } else if(hasNullOrEmptyDrawable(imageView) && Objects.requireNonNull(editText.getText()).length() != 0) {
                        progressDialog = ProgressDialog.show(context, context.getResources().getString(R.string.sending), context.getResources().getString(R.string.sending_report), true);
                        final MultipartRequest request = new MultipartRequest(context, progressDialog);
                        request.addString(USER_KEY, USER_VAL);
                        request.addString(PASS_KEY, PASS_VAL);
                        request.addString("message", Objects.requireNonNull(editText.getText()).toString());
                        request.execute(ApiClient.REPORT_URL);
                        request.onCode(getResources().getString(R.string.report_max_size), 504);
                    } else {
                        Toast.makeText(context, context.getResources().getString(R.string.report_input_error), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private String loadJSONFromAsset() {
        try {
            final InputStream is = getApplicationContext().getAssets().open("ACRA-SETTINGS.json");
            final int size = is.available();
            final byte[] buffer = new byte[size];
            //noinspection ResultOfMethodCallIgnored
            is.read(buffer);
            final String json = new String(buffer, StandardCharsets.UTF_8);
            is.close();
            return json;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        if(Objects.requireNonNull(editText.getText()).length() != 0 || !hasNullOrEmptyDrawable(imageView)) {
            final AlertDialog dialog = new AlertDialog.Builder(context)
                    .setView(R.layout.confirmation_dialog_layout)
                    .show();
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            final ExtendedFloatingActionButton positiveButton = dialog.findViewById(R.id.confirm_button);
            assert positiveButton != null;
            positiveButton.setText(R.string.positive_discard_dialog_button);
            positiveButton.setOnClickListener(v -> {
                dialog.dismiss();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            });
            final TextView dialogTitle = dialog.findViewById(R.id.dialog_title);
            assert dialogTitle != null;
            dialogTitle.setText(R.string.discard_dialog_report_title);
            final TextView dialogSubtitle = dialog.findViewById(R.id.dialog_subtitle);
            assert dialogSubtitle != null;
            dialogSubtitle.setText(R.string.discard_dialog_report_subtitle);
            final TextView negative = dialog.findViewById(R.id.negative_button);
            assert negative != null;
            negative.setText(R.string.cancel_dialog_buttton);
            negative.setOnClickListener(v -> dialog.dismiss());
        } else {
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        final Configuration override = new Configuration(newBase.getResources().getConfiguration());
        override.fontScale = 1.0f;
        applyOverrideConfiguration(override);
    }

}