package uguu.gao.wafu.jedextract;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.*;

import java.io.File;
import java.util.ArrayList;
import android.app.ListActivity;
import android.os.Environment;
import android.widget.*;

/**
 * Created by aki on 17/05/13.
 */
public class BrowserActivity extends ListActivity {

    private static final String FILE = "file";
    private static final String PATH = "path";
    private static final String DIR = "dir";
    private static final String HOME = "home";
    private static final String BACK = "back";
    private static final String MODE = "mode";
    private static final String NO_SELECT = "0";

    private static final int SELECTED = 1;
    private static final int NOT_SELECTED = 0;

    private File currentNode = null;
    private File lastNode = null;
    private File rootNode = null;
    private ArrayList<File> files = new ArrayList<File>();
    private FileAdapter adapter = null;

    private int dirMode = 0;

    private String selectedFile = "";
    private String selectedFullPath = "";

    private ActionMode actionMode = null;
    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.browser_action_mode, menu);
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.select_file:
                    Intent result = new Intent(BrowserActivity.this, MainActivity.class);
                    result.putExtra(PATH, selectedFullPath);
                    result.putExtra(FILE, selectedFile);
                    setResult(SELECTED, result);
                    finish(); // Action picked, so close the CAB
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browser);

        // set up the Up button
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle.getString(MODE).equals(DIR)) {
            dirMode = 1;
        }

        adapter = new FileAdapter(this, R.layout.browser_row, files);
        setListAdapter(adapter);
        if (savedInstanceState != null) {
            rootNode = (File) savedInstanceState.getSerializable("root_node");
            lastNode = (File) savedInstanceState.getSerializable("last_node");
            currentNode = (File) savedInstanceState.getSerializable("current_node");
        }

        ListView lv = this.getListView();
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                v.setSelected(true);
                TableRow selectedItem = (TableRow) v;
                TextView name = (TextView) selectedItem.getChildAt(1);
                TextView row_id = (TextView) selectedItem.getChildAt(2);
                if (row_id.getText().equals(BACK) || dirMode == 0 && (row_id.getText().equals(HOME) || row_id.getText().equals(DIR))) {
                    return false;
                }
                actionMode = BrowserActivity.this.startActionMode(actionModeCallback);

                actionMode.setTitle(name.getText());
                selectedFile = name.getText().toString();

                if (row_id.getText().equals(HOME)) {
                    selectedFullPath = name.getText().toString();
                } else {
                    selectedFullPath = adapter.getCurrentPath() + "/" + name.getText();
                }
                actionMode.setSubtitle(selectedFile);
                return true;
            }
        });

        refreshFileList();
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        state.putSerializable("root_node", rootNode);
        state.putSerializable("last_node", lastNode);
        state.putSerializable("current_node", currentNode);
        super.onSaveInstanceState(state);
    }

    @Override
    public void onListItemClick(ListView parent, View v, int position, long id) {
        File f = (File) parent.getItemAtPosition(position);
        if (position == 1) {
            if (currentNode.compareTo(rootNode) != 0) {
                currentNode = f.getParentFile();
                refreshFileList();
            }
        } else if (f.isDirectory()) {
            currentNode = f;
            refreshFileList();
        } else {
            Toast.makeText(this, f.getName(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.browser_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //case R.id.finish_selection:
            //    setResult(NOT_SELECTED, null);
            //    finish();
            case android.R.id.home:
                setResult(NOT_SELECTED, null);
                finish();
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void refreshFileList() {
        if (rootNode == null) {
            rootNode = new File(Environment.getExternalStorageDirectory().toString());
        }

        if (currentNode == null) {
            currentNode = rootNode;
        }

        lastNode = currentNode;
        File[] _files = currentNode.listFiles();
        files.clear();
        files.add(rootNode);
        files.add(lastNode);
        if (_files != null) {
            for (int i = 0; i < _files.length; i++) {
                files.add(_files[i]);
            }
        }
        adapter.notifyDataSetChanged();
    }

}