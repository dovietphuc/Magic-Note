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

import java.util.Calendar;

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

public class AutoSyncWorker extends Worker {
    public static final String AUTO_SYNC_WORKER_NAME = "magic_note.auto_sync_worker";
    public static final long SCHEDULE_TIME = 15;

    private int mNumOfChild = 6;
    private int mNumOfChildCpl = 0;
    public AutoSyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference userRef = firebaseDatabase.getReference(firebaseUser.getUid());

        restoreNote(firebaseDatabase, firebaseUser);
        restoreTextItem(firebaseDatabase, firebaseUser);
        restoreCheckbox(firebaseDatabase, firebaseUser);
        restoreImage(firebaseDatabase, firebaseUser);
        restoreLabel(firebaseDatabase, firebaseUser);
        restoreNoteLabel(firebaseDatabase, firebaseUser);

        return Result.success();
    }

    public void backup(FirebaseDatabase firebaseDatabase, FirebaseUser user){
        NoteRoomDatabase noteRoomDatabase = NoteRoomDatabase.getDatabase(getApplicationContext());

        DatabaseReference userRef = firebaseDatabase.getReference(user.getUid());
        userRef.removeValue();

        backupNote(userRef, noteRoomDatabase);
        backupTextItem(userRef, noteRoomDatabase);
        backupCheckbox(userRef, noteRoomDatabase);
        backupImageItem(userRef, noteRoomDatabase);
        backupLabel(userRef, noteRoomDatabase);
        backupNoteLabel(userRef, noteRoomDatabase);
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
                        if(lastUpdate == backUpNote.getTime_last_update()){
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
                mNumOfChildCpl++;
                if(mNumOfChildCpl == mNumOfChild){
                    backup(firebaseDatabase, user);
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
                        if(lastUpdate == item.getTime_stamp_update()){
                            continue;
                        }
                    }
                    dao.insert(item);
                }
                mNumOfChildCpl++;
                if(mNumOfChildCpl == mNumOfChild){
                    backup(firebaseDatabase, user);
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
                        if(lastUpdate == item.getTime_stamp_update()){
                            continue;
                        }
                    }
                    dao.insert(item);
                }
                mNumOfChildCpl++;
                if(mNumOfChildCpl == mNumOfChild){
                    backup(firebaseDatabase, user);
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
                        if(lastUpdate == item.getTime_stamp_update()){
                            continue;
                        }
                    }
                    dao.insert(item);
                }
                mNumOfChildCpl++;
                if(mNumOfChildCpl == mNumOfChild){
                    backup(firebaseDatabase, user);
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
                        if(lastUpdate == item.getTime_stamp_update()){
                            continue;
                        }
                    }
                    dao.insert(item);
                }
                mNumOfChildCpl++;
                if(mNumOfChildCpl == mNumOfChild){
                    backup(firebaseDatabase, user);
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
                        if(lastUpdate == item.getTime_stamp_update()){
                            continue;
                        }
                    }
                    dao.insert(item);
                }
                mNumOfChildCpl++;
                if(mNumOfChildCpl == mNumOfChild){
                    backup(firebaseDatabase, user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void backupNote(DatabaseReference ref,  NoteRoomDatabase db){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        Cursor cursor = db.query("SELECT * FROM note", null);
        while (cursor.moveToNext()){
            BackUpNoteItem note = new BackUpNoteItem(
                    cursor.getString(cursor.getColumnIndex("title")),
                    cursor.getLong(cursor.getColumnIndex("time_create")),
                    cursor.getLong(cursor.getColumnIndex("time_last_update")),
                    cursor.getInt(cursor.getColumnIndex("is_archive")) == 1,
                    cursor.getInt(cursor.getColumnIndex("is_deleted")) == 1,
                    cursor.getLong(cursor.getColumnIndex("order_in_parent")),
                    cursor.getInt(cursor.getColumnIndex("is_pinned")) == 1,
                    cursor.getInt(cursor.getColumnIndex("color")),
                    cursor.getInt(cursor.getColumnIndex("has_checkbox")) == 1,
                    cursor.getInt(cursor.getColumnIndex("has_image")) == 1,
                    cursor.getString(cursor.getColumnIndex("full_text")),
                    firebaseUser.getUid()
            );
            note.setId(cursor.getLong(cursor.getColumnIndex("id")));

            ref.child("note/" + note.getId()).setValue(note);
        }
    }

    public void backupTextItem(DatabaseReference ref,  NoteRoomDatabase db){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        Cursor cursor = db.query("SELECT * FROM text_item", null);
        while (cursor.moveToNext()){
            TextItem item = new TextItem(
                    cursor.getLong(cursor.getColumnIndex("parent_id")),
                    cursor.getLong(cursor.getColumnIndex("order_in_parent")),
                    cursor.getString(cursor.getColumnIndex("content")),
                    firebaseUser.getUid()
            );
            item.setId(cursor.getLong(cursor.getColumnIndex("id")));

            ref.child("text_item/" + item.getId()).setValue(item);
        }
    }

    public void backupCheckbox(DatabaseReference ref,  NoteRoomDatabase db){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        Cursor cursor = db.query("SELECT * FROM checkbox_item", null);
        while (cursor.moveToNext()){
            CheckboxItem item = new CheckboxItem(
                    cursor.getLong(cursor.getColumnIndex("parent_id")),
                    cursor.getLong(cursor.getColumnIndex("order_in_parent")),
                    cursor.getInt(cursor.getColumnIndex("is_checked")) == 1,
                    cursor.getString(cursor.getColumnIndex("content")),
                    firebaseUser.getUid()
            );
            item.setId(cursor.getLong(cursor.getColumnIndex("id")));

            ref.child("checkbox_item/" + item.getId()).setValue(item);
        }
    }

    public void backupImageItem(DatabaseReference ref,  NoteRoomDatabase db){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        Cursor cursor = db.query("SELECT * FROM image_item", null);
        while (cursor.moveToNext()){
            ImageItem item = new ImageItem(
                    cursor.getLong(cursor.getColumnIndex("order_in_parent")),
                    cursor.getLong(cursor.getColumnIndex("parent_id")),
                    cursor.getString(cursor.getColumnIndex("path")),
                    firebaseUser.getUid()
            );
            item.setId(cursor.getLong(cursor.getColumnIndex("id")));

            ref.child("image_item/" + item.getId()).setValue(item);
        }
    }

    public void backupLabel(DatabaseReference ref,  NoteRoomDatabase db){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        Cursor cursor = db.query("SELECT * FROM label", null);
        while (cursor.moveToNext()){
            Label item = new Label(
                    cursor.getString(cursor.getColumnIndex("name")),
                    firebaseUser.getUid()
            );
            item.setId(cursor.getLong(cursor.getColumnIndex("id")));

            ref.child("label/" + item.getId()).setValue(item);
        }
    }

    public void backupNoteLabel(DatabaseReference ref,  NoteRoomDatabase db){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        Cursor cursor = db.query("SELECT * FROM note_label", null);
        while (cursor.moveToNext()){
            NoteLabel item = new NoteLabel(
                    cursor.getLong(cursor.getColumnIndex("note_id")),
                    cursor.getLong(cursor.getColumnIndex("label_id")),
                    firebaseUser.getUid()
            );
            item.setId(cursor.getLong(cursor.getColumnIndex("id")));

            ref.child("note_label/" + item.getId()).setValue(item);
        }
    }
}
