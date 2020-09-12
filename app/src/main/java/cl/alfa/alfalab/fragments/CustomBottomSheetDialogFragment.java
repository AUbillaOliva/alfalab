package cl.alfa.alfalab.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Date;
import java.util.Objects;

import cl.alfa.alfalab.MainActivity;
import cl.alfa.alfalab.MainApplication;
import cl.alfa.alfalab.R;
import cl.alfa.alfalab.models.Order;
import cl.alfa.alfalab.utils.SharedPreferences;

public class CustomBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private Context context;
    private AutoCompleteTextView typeAutoCompleteTextView;
    private Order order;
    private TextInputLayout typeInputLayout,
        priceInputLayout,
        levelInputLayout;
    private TextInputEditText priceInputEditText,
        levelInputEditText,
        commentariesInputEditText;
    private MaterialCheckBox digitalizedCheckbox;

    public interface OnCreateOrderListener { void saveOrder(Order order); }
    private OnCreateOrderListener createOrder;

    public interface OnEditOrderListener { void editOrder(Order oldOrder, Order newOrder); }
    private OnEditOrderListener editOrder;

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        try {
            createOrder = (OnCreateOrderListener) activity;
            editOrder = (OnEditOrderListener) activity;
            this.context = activity.getApplicationContext();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement methods");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_layout, container, false);
        v.setClipToOutline(true);

        final SharedPreferences mSharedPreferences = new SharedPreferences(MainApplication.getContext());

        typeAutoCompleteTextView = v.findViewById(R.id.type_input_autocomplete);
        typeInputLayout = v.findViewById(R.id.type_input_layout);
        levelInputLayout = v.findViewById(R.id.level_input_layout);
        priceInputLayout = v.findViewById(R.id.price_input_layout);
        TextInputLayout commentariesInputLayout = v.findViewById(R.id.commentaries_input_layout);
        digitalizedCheckbox = v.findViewById(R.id.digitized_checkbox);

        levelInputEditText = levelInputLayout.findViewById(R.id.level_edit_text);
        priceInputEditText = priceInputLayout.findViewById(R.id.price_edit_text);
        commentariesInputEditText = commentariesInputLayout.findViewById(R.id.commentaries_edit_text);

        priceInputLayout.setHint(Html.fromHtml(getResources().getString(R.string.order_price_hint)));

        if (getArguments() != null && getArguments().getSerializable("order") != null) {
            order = (Order) getArguments().getSerializable("order");
            if(getArguments().get("update") != null){
                Log.d(MainActivity.API, "isUpdating");
                levelInputEditText.setText(String.valueOf(order.getForcedLevel()));
                typeAutoCompleteTextView.setText(order.getOrder_type());
                digitalizedCheckbox.setChecked(order.isdigitized());
                priceInputEditText.setText(String.valueOf(order.getPrice()));
                commentariesInputEditText.setText(order.getCommentaries());
            }
        }

        final ExtendedFloatingActionButton button = v.findViewById(R.id.button);
        final String[] TYPES = new String[] {"Blanco y negro", "Color", "Cine", "Diapo"};
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, TYPES);
        typeAutoCompleteTextView.setAdapter(adapter);

        final java.util.Date date = new Date();
        final long time = date.getTime();
        final java.sql.Timestamp timestamp = new java.sql.Timestamp(time);

        button.setOnClickListener(view -> {
            if(typeAutoCompleteTextView.getText().length() < 1)
                typeInputLayout.setError(getResources().getString(R.string.required));
            else if(Objects.requireNonNull(priceInputEditText.getText()).length() <  1)
                priceInputLayout.setError(getResources().getString(R.string.required));
            else if(Objects.requireNonNull(levelInputEditText.getText()).length() < 1)
                levelInputLayout.setError(getResources().getString(R.string.required));
            else if(typeAutoCompleteTextView.getText().length() < 1 && priceInputEditText.getText().length() <  1) {
                typeInputLayout.setError(getResources().getString(R.string.required));
                priceInputLayout.setError(getResources().getString(R.string.required));
            } else if(typeAutoCompleteTextView.getText().length() < 1 && levelInputEditText.getText().length() <  1) {
                typeInputLayout.setError(getResources().getString(R.string.required));
                levelInputLayout.setError(getResources().getString(R.string.required));
            } else if(typeAutoCompleteTextView.getText().length() < 1 && priceInputEditText.getText().length() <  1 && levelInputEditText.getText().length() <  1){
                typeInputLayout.setError(getResources().getString(R.string.required));
                priceInputLayout.setError(getResources().getString(R.string.required));
                levelInputLayout.setError(getResources().getString(R.string.required));
            } else {
                if(getArguments() != null && getArguments().get("update") != null)
                    editOrder.editOrder(new Order(typeAutoCompleteTextView.getText().toString(), timestamp.toString(), mSharedPreferences.getResponsible(), Objects.requireNonNull(commentariesInputEditText.getText()).toString(), Integer.parseInt(Objects.requireNonNull(priceInputEditText.getText()).toString()), Integer.parseInt(Objects.requireNonNull(levelInputEditText.getText()).toString()),  digitalizedCheckbox.isChecked(), false), order);
                else
                    createOrder.saveOrder(new Order(typeAutoCompleteTextView.getText().toString(), timestamp.toString(), mSharedPreferences.getResponsible(), Objects.requireNonNull(commentariesInputEditText.getText()).toString(), Integer.parseInt(Objects.requireNonNull(priceInputEditText.getText()).toString()), Integer.parseInt(Objects.requireNonNull(levelInputEditText.getText()).toString()),  digitalizedCheckbox.isChecked(), false));
                dismissAllowingStateLoss();
            }
        });

        return v;
    }

}