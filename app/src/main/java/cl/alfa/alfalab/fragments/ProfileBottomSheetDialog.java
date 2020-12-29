package cl.alfa.alfalab.fragments;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import cl.alfa.alfalab.R;
import cl.alfa.alfalab.utils.SharedPreferences;

public class ProfileBottomSheetDialog extends BottomSheetDialogFragment {

    private BottomSheetListener mBottomSheetListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.profile_bottom_sheet_layout, container, false);
        final LinearLayout edit = v.findViewById(R.id.edit_profile_picture),
                           delete = v.findViewById(R.id.delete_profile_picture);
        edit.setOnClickListener(v1 -> {
            mBottomSheetListener.onButtonClicked("edit");
            dismiss();
        });
        delete.setOnClickListener(v12 -> {
            mBottomSheetListener.onButtonClicked("null");
            dismiss();
        });
        return v;
    }

    public interface BottomSheetListener {
        void onButtonClicked(String text);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mBottomSheetListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }
}
