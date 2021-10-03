package phucdv.android.magicnote.widget;

import android.app.Application;

import androidx.annotation.NonNull;

import phucdv.android.magicnote.ui.BaseViewModel;

public class WidgetConfigViewModel extends BaseViewModel {
    public WidgetConfigViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public void initNotes() {
        mNotes = mNoteRepository.getAllNotes();
    }
}
