package uguu.gao.wafu.jedextract;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import java.io.File;
import java.util.ArrayList;
import android.app.ListActivity;
import android.os.Environment;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by aki on 17/05/13.
 */
public class BrowserActivity extends ListActivity {

    private File currentNode = null;
    private File lastNode = null;
    private File rootNode = null;
    private ArrayList<File> files = new ArrayList<File>();
    private FileAdapter adapter = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browser);

        adapter = new FileAdapter(this, R.layout.browser_row, files);
        setListAdapter(adapter);
        if (savedInstanceState != null) {
            rootNode = (File) savedInstanceState.getSerializable("root_node");
            lastNode = (File) savedInstanceState.getSerializable("last_node");
            currentNode = (File) savedInstanceState.getSerializable("current_node");
        }
        refreshFileList();
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


}