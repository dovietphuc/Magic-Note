package phucdv.android.magicnote.sync;

import android.content.Context;
import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import phucdv.android.magicnote.data.Converters;
import phucdv.android.magicnote.data.NoteRoomDatabase;
import phucdv.android.magicnote.data.checkboxitem.CheckboxItem;
import phucdv.android.magicnote.data.checkboxitem.CheckboxItemDao;
import phucdv.android.magicnote.data.imageitem.ImageItem;
import phucdv.android.magicnote.data.imageitem.ImageItemDao;
import phucdv.android.magicnote.data.label.Label;
import phucdv.android.magicnote.data.label.LabelDao;
import phucdv.android.magicnote.data.noteandlabel.NoteLabel;
import phucdv.android.magicnote.data.noteandlabel.NoteLabelDao;
import phucdv.android.magicnote.data.noteitem.Note;
import phucdv.android.magicnote.data.noteitem.NoteDao;
import phucdv.android.magicnote.data.textitem.TextItem;
import phucdv.android.magicnote.data.textitem.TextItemDao;

public class RestoreWorker extends Worker {
    public static final String RESTORE_WORKER_NAME = "magic_note.restore_worker";

    public RestoreWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        restoreNote(firebaseDatabase, firebaseUser);
        restoreTextItem(firebaseDatabase, firebaseUser);
        restoreCheckbox(firebaseDatabase, firebaseUser);
        restoreImage(firebaseDatabase, firebaseUser);
        restoreLabel(firebaseDatabase, firebaseUser);
        restoreNoteLabel(firebaseDatabase, firebaseUser);
        return Result.success();
    }

    public void restoreNote(FirebaseDatabase firebaseDatabase, FirebaseUser user){
        DatabaseReference noteRef = firebaseDatabase.getReference(user.getUid() + "/note");
        noteRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NoteRoomDatabase db = NoteRoomDatabase.getDatabase(getApplicationContext());
                NoteDao noteDao = db.noteDao();
                for(DataSnapshot child : snapshot.getChildren()){
                    BackUpNoteItem backUpNote = child.getValue(BackUpNoteItem.class);
                    Cursor cursor = db.query("SELECT time_last_update FROM note WHERE id=" + backUpNote.getId(), null);
                    if(cursor.moveToFirst()){
                        long lastUpdate = cursor.getLong(0);
                        if(lastUpdate >= backUpNote.getTime_last_update()){
                            continue;
                        }
                    }
                    Note note = new Note(backUpNote.getTitle(), Converters.datestampToCalendar(backUpNote.getTime_create()),
                            Converters.datestampToCalendar(backUpNote.getTime_last_update()), backUpNote.isIs_archive(), backUpNote.isIs_deleted(),
                            backUpNote.getOrder_in_parent(), backUpNote.isIs_pinned(), backUpNote.getColor(),
                            backUpNote.isHas_checkbox(), backUpNote.isHas_image(), backUpNote.getFull_text(), backUpNote.getUid());
                    note.setId(backUpNote.getId());
                    noteDao.insert(note);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void restoreTextItem(FirebaseDatabase firebaseDatabase, FirebaseUser user){
        DatabaseReference noteRef = firebaseDatabase.getReference(user.getUid() + "/text_item");
        noteRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NoteRoomDatabase db = NoteRoomDatabase.getDatabase(getApplicationContext());
                TextItemDao dao = db.textItemDao();
                for(DataSnapshot child : snapshot.getChildren()){
                    TextItem item = child.getValue(TextItem.class);
                    Cursor cursor = db.query("SELECT time_stamp_update FROM text_item WHERE id=" + item.getId(), null);
                    if(cursor.moveToFirst()){
                        long lastUpdate = cursor.getLong(0);
                        if(lastUpdate >= item.getTime_stamp_update()){
                            continue;
                        }
                    }
                    dao.insert(item);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void restoreCheckbox(FirebaseDatabase firebaseDatabase, FirebaseUser user){
        DatabaseReference noteRef = firebaseDatabase.getReference(user.getUid() + "/checkbox_item");
        noteRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NoteRoomDatabase db = NoteRoomDatabase.getDatabase(getApplicationContext());
                CheckboxItemDao dao = db.checkboxItemDao();
                for(DataSnapshot child : snapshot.getChildren()){
                    CheckboxItem item = child.getValue(CheckboxItem.class);
                    Cursor cursor = db.query("SELECT time_stamp_update FROM checkbox_item WHERE id=" + item.getId(), null);
                    if(cursor.moveToFirst()){
                        long lastUpdate = cursor.getLong(0);
                        if(lastUpdate >= item.getTime_stamp_update()){
                            continue;
                        }
                    }
                    dao.insert(item);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void restoreImage(FirebaseDatabase firebaseDatabase, FirebaseUser user){
        DatabaseReference noteRef = firebaseDatabase.getReference(user.getUid() + "/image_item");
        noteRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NoteRoomDatabase db = NoteRoomDatabase.getDatabase(getApplicationContext());
                ImageItemDao dao = db.imageItemDao();
                for(DataSnapshot child : snapshot.getChildren()){
                    ImageItem item = child.getValue(ImageItem.class);
                    Cursor cursor = db.query("SELECT time_stamp_update FROM image_item WHERE id=" + item.getId(), null);
                    if(cursor.moveToFirst()){
                        long lastUpdate = cursor.getLong(0);
                        if(lastUpdate >= item.getTime_stamp_update()){
                            continue;
                        }
                    }
                    dao.insert(item);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void restoreLabel(FirebaseDatabase firebaseDatabase, FirebaseUser user){
        DatabaseReference noteRef = firebaseDatabase.getReference(user.getUid() + "/label");
        noteRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NoteRoomDatabase db = NoteRoomDatabase.getDatabase(getApplicationContext());
                LabelDao dao = db.labelDao();
                for(DataSnapshot child : snapshot.getChildren()){
                    Label item = child.getValue(Label.class);
                    Cursor cursor = db.query("SELECT time_stamp_update FROM label WHERE id=" + item.getId(), null);
                    if(cursor.moveToFirst()){
                        long lastUpdate = cursor.getLong(0);
                        if(lastUpdate >= item.getTime_stamp_update()){
                            continue;
                        }
                    }
                    dao.insert(item);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void restoreNoteLabel(FirebaseDatabase firebaseDatabase, FirebaseUser user){
        DatabaseReference noteRef = firebaseDatabase.getReference(user.getUid() + "/note_label");
        noteRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NoteRoomDatabase db = NoteRoomDatabase.getDatabase(getApplicationContext());
                NoteLabelDao dao = db.noteLabelDao();
                for(DataSnapshot child : snapshot.getChildren()){
                    NoteLabel item = child.getValue(NoteLabel.class);
                    Cursor cursor = db.query("SELECT time_stamp_update FROM note_label WHERE id=" + item.getId(), null);
                    if(cursor.moveToFirst()){
                        long lastUpdate = cursor.getLong(0);
                        if(lastUpdate >= item.getTime_stamp_update()){
                            continue;
                        }
                    }
                    dao.insert(item);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
