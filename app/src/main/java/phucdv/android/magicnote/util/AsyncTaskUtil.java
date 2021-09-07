package phucdv.android.magicnote.util;

import android.os.AsyncTask;

import java.util.List;

import phucdv.android.magicnote.data.checkboxitem.CheckboxItem;
import phucdv.android.magicnote.data.checkboxitem.CheckboxItemDao;
import phucdv.android.magicnote.data.noteitem.Note;
import phucdv.android.magicnote.data.noteitem.NoteDao;
import phucdv.android.magicnote.data.textitem.TextItem;
import phucdv.android.magicnote.data.textitem.TextItemDao;
import phucdv.android.magicnote.noteinterface.AsyncResponse;

public class AsyncTaskUtil {
    public static class insertNoteAsyncTask extends AsyncTask<Note, Void, Long> {

        private NoteDao mAsyncTaskDao;
        private AsyncResponse mResponse;

        public insertNoteAsyncTask(NoteDao dao, AsyncResponse response) {
            mAsyncTaskDao = dao;
            mResponse = response;
        }

        @Override
        protected Long doInBackground(final Note... params) {
            return mAsyncTaskDao.insert(params[0]);
        }

        @Override
        protected void onPostExecute(Long aLong) {
            mResponse.processFinish(aLong);
        }
    }

    public static class insertTextItemAsyncTask extends AsyncTask<TextItem, Void, Long> {

        private TextItemDao mAsyncTaskDao;
        private AsyncResponse mResponse;

        public insertTextItemAsyncTask(TextItemDao dao, AsyncResponse response) {
            mAsyncTaskDao = dao;
            mResponse = response;
        }

        @Override
        protected Long doInBackground(final TextItem... params) {
            return mAsyncTaskDao.insert(params[0]);
        }

        @Override
        protected void onPostExecute(Long aLong) {
            mResponse.processFinish(aLong);
        }
    }

    public static class insertAllTextItemsAsyncTask extends AsyncTask<List<TextItem>, Void, Long[]> {

        private TextItemDao mAsyncTaskDao;
        private AsyncResponse mResponse;

        public insertAllTextItemsAsyncTask(TextItemDao dao, AsyncResponse response) {
            mAsyncTaskDao = dao;
            mResponse = response;
        }

        @Override
        protected Long[] doInBackground(final List<TextItem>... params) {
            return mAsyncTaskDao.insertAll(params[0]);
        }

        @Override
        protected void onPostExecute(Long[] longs) {
            mResponse.processFinish(longs);
        }
    }

    public static class insertChecboxItemAsyncTask extends AsyncTask<CheckboxItem, Void, Long> {

        private CheckboxItemDao mAsyncTaskDao;
        private AsyncResponse mResponse;
        public insertChecboxItemAsyncTask(CheckboxItemDao dao, AsyncResponse response) {
            mAsyncTaskDao = dao;
            mResponse = response;
        }

        @Override
        protected Long doInBackground(final CheckboxItem... params) {
            return mAsyncTaskDao.insert(params[0]);
        }

        @Override
        protected void onPostExecute(Long aLong) {
            mResponse.processFinish(aLong);
        }
    }

    public static class insertAllCheckboxItemsAsyncTask extends AsyncTask<List<CheckboxItem>, Void, Long[]> {

        private CheckboxItemDao mAsyncTaskDao;
        private AsyncResponse mResponse;

        public insertAllCheckboxItemsAsyncTask(CheckboxItemDao dao, AsyncResponse response) {
            mAsyncTaskDao = dao;
            mResponse = response;
        }

        @Override
        protected Long[] doInBackground(final List<CheckboxItem>... params) {
            return mAsyncTaskDao.insertAll(params[0]);
        }

        @Override
        protected void onPostExecute(Long[] longs) {
            mResponse.processFinish(longs);
        }
    }

    public static class deleteNoteAsyncTask extends AsyncTask<Long, Void, Void> {

        private NoteDao mAsyncTaskDao;

        public deleteNoteAsyncTask(NoteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Long... params) {
            mAsyncTaskDao.deleteNoteByID(params[0]);
            return null;
        }
    }

    public static class deleteTextItemByParentIdAsyncTask extends AsyncTask<Long, Void, Void> {

        private TextItemDao mAsyncTaskDao;

        public deleteTextItemByParentIdAsyncTask(TextItemDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Long... params) {
            mAsyncTaskDao.deleteByParentId(params[0]);
            return null;
        }
    }

    public static class deleteTextItemByIdAsyncTask extends AsyncTask<Long, Void, Void> {

        private TextItemDao mAsyncTaskDao;

        public deleteTextItemByIdAsyncTask(TextItemDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Long... params) {
            mAsyncTaskDao.deleteById(params[0]);
            return null;
        }
    }

    public static class deleteCheckboxItemByParentIdAsyncTask extends AsyncTask<Long, Void, Void> {

        private CheckboxItemDao mAsyncTaskDao;

        public deleteCheckboxItemByParentIdAsyncTask(CheckboxItemDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Long... params) {
            mAsyncTaskDao.deleteByParentId(params[0]);
            return null;
        }
    }

    public static class deleteCheckboxItemByIdAsyncTask extends AsyncTask<Long, Void, Void> {

        private CheckboxItemDao mAsyncTaskDao;

        public deleteCheckboxItemByIdAsyncTask(CheckboxItemDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Long... params) {
            mAsyncTaskDao.deleteById(params[0]);
            return null;
        }
    }

    public static class updateNoteAsyncTask extends AsyncTask<Note, Void, Void> {

        private NoteDao mAsyncTaskDao;

        public updateNoteAsyncTask(NoteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Note... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }

    public static class updateListNoteAsyncTask extends AsyncTask<List<Note>, Void, Void> {

        private NoteDao mAsyncTaskDao;

        public updateListNoteAsyncTask(NoteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final List<Note>... params) {
            mAsyncTaskDao.updateAll(params[0]);
            return null;
        }
    }

    public static class updateTextItemAsyncTask extends AsyncTask<TextItem, Void, Void> {

        private TextItemDao mAsyncTaskDao;

        public updateTextItemAsyncTask(TextItemDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final TextItem... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }

    public static class updateListTextItemAsyncTask extends AsyncTask<List<TextItem>, Void, Void> {

        private TextItemDao mAsyncTaskDao;

        public updateListTextItemAsyncTask(TextItemDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final List<TextItem>... params) {
            mAsyncTaskDao.updateAll(params[0]);
            return null;
        }
    }

    public static class updateCheckboxItemAsyncTask extends AsyncTask<CheckboxItem, Void, Void> {

        private CheckboxItemDao mAsyncTaskDao;

        public updateCheckboxItemAsyncTask(CheckboxItemDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final CheckboxItem... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }

    public static class updateListCheckboxItemAsyncTask extends AsyncTask<List<CheckboxItem>, Void, Void> {

        private CheckboxItemDao mAsyncTaskDao;

        public updateListCheckboxItemAsyncTask(CheckboxItemDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final List<CheckboxItem>... params) {
            mAsyncTaskDao.updateAll(params[0]);
            return null;
        }
    }
}
