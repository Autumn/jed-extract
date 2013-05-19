package uguu.gao.wafu.jedextract;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.*;
import android.widget.*;

import java.util.ArrayList;

/**
 * Created by aki on 18/05/13.
 */

public class ResultsActivity extends ListActivity {

    private String listShareText = "";
    private String selectedShareText = "";


    /* ActionMode implementation for selecting on a single word - brings up a menu with a Share icon and action */
    private ActionMode actionMode = null;
    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.results_action_mode, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.share_word:
                    shareText(selectedShareText, 1);
                    finish();
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);

        // set up the Up button
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        ArrayList<CharSequence> words = bundle.getCharSequenceArrayList("list");
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, R.layout.results_row, words);
        setListAdapter(adapter);

        // create the shareable text. Simply the set of words, newline delimited.
        StringBuilder sb = new StringBuilder();
        for (CharSequence word: words) {
            sb.append(word);
            sb.append("\n");
        }
        sb.deleteCharAt(sb.length() - 1); // delete the final newline
        listShareText = sb.toString();

        // set the long click listener

        ListView lv = this.getListView();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                v.setSelected(true);
                TextView tv = (TextView) v;
                selectedShareText = tv.getText().toString();

                actionMode = ResultsActivity.this.startActionMode(actionModeCallback);

                actionMode.setTitle("Share Word");
                actionMode.setSubtitle(selectedShareText);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.results_action_bar, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share_results:
                shareText(listShareText, 0);
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void shareText(String shareText, int type) {
        String shareHeading = "Share your word list to another app.";
        if (type == 1) {
            shareHeading = "Share this word to another app.";
        }
        Intent i = new Intent(android.content.Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT, shareText);

        startActivity(Intent.createChooser(i, shareHeading));
    }
}