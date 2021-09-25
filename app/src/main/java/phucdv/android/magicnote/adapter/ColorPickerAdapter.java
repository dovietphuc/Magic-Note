package phucdv.android.magicnote.adapter;

import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import phucdv.android.magicnote.R;

public class ColorPickerAdapter extends RecyclerView.Adapter<ColorPickerAdapter.ColorPickerViewHolder> {

    private int[] mColors;
    private OnColorPickerListener mOnColorPickerListener;
    private int mExitsColor;
    private ColorPickerViewHolder mExitsItem;

    public ColorPickerAdapter(int[] colors){
        mColors = colors;
    }

    public void setExitsColor(int color){
        mExitsColor = color;
    }

    public void setOnColorPickerListener(OnColorPickerListener onColorPickerListener){
        mOnColorPickerListener = onColorPickerListener;
    }

    @NonNull
    @Override
    public ColorPickerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ColorPickerViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.color_picker_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ColorPickerViewHolder holder, int position) {
        holder.bind(mColors[position]);
    }

    @Override
    public int getItemCount() {
        return mColors.length;
    }

    public class ColorPickerViewHolder extends RecyclerView.ViewHolder{
        private View mView;
        private FrameLayout mColorItem;
        private FrameLayout mBackground;

        public ColorPickerViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            mBackground = itemView.findViewById(R.id.background);
            mColorItem = itemView.findViewById(R.id.color_item);
            mColorItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnColorPickerListener != null) {
                        mBackground.setBackgroundColor(Color.BLUE);
                        if (mExitsItem != null){
                            mExitsItem.mBackground.setBackgroundColor(Color.WHITE);
                        }
                        mExitsItem = ColorPickerViewHolder.this;
                        mOnColorPickerListener.onColorPicked(mColors[getLayoutPosition()]);
                    }
                }
            });
        }

        public void bind(int color){
            mColorItem.setBackgroundColor(color);
            if(mExitsColor == color){
                mBackground.setBackgroundColor(Color.BLUE);
                mExitsItem = this;
            } else {
                mBackground.setBackgroundColor(Color.WHITE);
            }
        }
    }

    public interface OnColorPickerListener{
        public void onColorPicked(int color);
        public void onDismiss(DialogInterface dialog);
    }
}
