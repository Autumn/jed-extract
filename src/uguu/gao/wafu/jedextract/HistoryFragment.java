package uguu.gao.wafu.jedextract;

import android.app.Fragment;
import android.app.ListFragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;

/**
 * Created by aki on 18/05/13.
 */
public class HistoryFragment extends ListFragment {

    View v = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_history_fragment, container, false);
        v = rootView;
        fillHistory();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        fillHistory();
    }

    /* Fills the view with the contents of the database */

    public void fillHistory() {
        ListView list = (ListView) v.findViewById(android.R.id.list);
        SQLiteDatabase db = DatabaseHelper.getInstance(getActivity()).getReadableDatabase();

        Cursor history;

        // Selects the data from the database and creates the Adapter

        try {
            db.beginTransaction();
            history = db.rawQuery("SELECT _id, origin_file, count FROM history ORDER BY _id DESC", null);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        if (history.getCount() > 0) {
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(), R.layout.main_history_row, history, new String[] {"_id", "origin_file", "count"}, new int[] {R.id.idText, R.id.fileText, R.id.countText}, 2);
            list.setAdapter(adapter);
        }

        // Code to handle pressing on a list item : opens the selected content in a ResultsActivity

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                TextView itemId = (TextView) v.findViewById(R.id.idText);
                String dbId = itemId.getText().toString();
                SQLiteDatabase db = DatabaseHelper.getInstance(getActivity()).getReadableDatabase();

                Cursor entry;

                try {
                    db.beginTransaction();
                    entry = db.query("history", new String[]{"content"}, "_id=?", new String[] {dbId}, null, null, null, null);
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                entry.moveToFirst();
                String content = entry.getString(0);

                // build CharSequence array to pass into ResultsActivity
                String[] split = content.split("\n");
                ArrayList<CharSequence> cs_words = new ArrayList();
                for (String word: split) {
                    cs_words.add(word);
                }

                Bundle bundle = new Bundle();
                Intent i = new Intent(getActivity(), ResultsActivity.class);
                bundle.putCharSequenceArrayList("list", cs_words);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

    }



}
