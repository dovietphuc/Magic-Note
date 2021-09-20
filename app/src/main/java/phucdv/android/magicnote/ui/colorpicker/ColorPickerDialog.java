package phucdv.android.magicnote.ui.colorpicker;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import phucdv.android.magicnote.R;
import phucdv.android.magicnote.adapter.ColorPickerAdapter;

public class ColorPickerDialog extends DialogFragment {

    private RecyclerView mRecyclerView;
    private ColorPickerAdapter mPickerAdapter;
    private ColorPickerAdapter.OnColorPickerListener mOnColorPickerListener;
    private int mExitsColor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.color_picker_layout, container, false);
        if(view instanceof RecyclerView){
            mRecyclerView = (RecyclerView) view;
            int[] colors = getResources().getIntArray(R.array.background_color);
            mPickerAdapter = new ColorPickerAdapter(colors);
            mPickerAdapter.setExitsColor(mExitsColor);
            mPickerAdapter.setOnColorPickerListener(mOnColorPickerListener);
            mRecyclerView.setAdapter(mPickerAdapter);
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        }
        return view;
    }

    public void setExitsColor(int color){
        mExitsColor = color;
    }

    public void setOnColorPickerListener(ColorPickerAdapter.OnColorPickerListener onColorPickerListener){
        mOnColorPickerListener = onColorPickerListener;
    }

    public void showDialog(AppCompatActivity activity) {
        FragmentManager fm = activity.getSupportFragmentManager();
        show(fm, "color_dialog");
    }
}