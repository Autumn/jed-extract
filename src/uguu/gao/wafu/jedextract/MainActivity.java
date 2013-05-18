package uguu.gao.wafu.jedextract;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private String selectedPath = "";
    private String selectedFile = "";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle bundle;
        String fileName;
        TextView tv;
        RadioButton option;
        if (resultCode == 1) {
            if (requestCode == 0) {
                bundle = data.getExtras();
                fileName = bundle.getString("file");
                selectedPath = bundle.getString("path");
                selectedFile = fileName;
                tv = (TextView) findViewById(R.id.filePath);
                tv.setText(fileName);
                option = (RadioButton) findViewById(R.id.fileOption);
                option.toggle();
            } else if (requestCode == 1) {
                bundle = data.getExtras();
                fileName = bundle.getString("file");
                selectedPath = bundle.getString("path");
                selectedFile = fileName;
                tv = (TextView) findViewById(R.id.dirPath);
                tv.setText(fileName);
                option = (RadioButton) findViewById(R.id.dirOption);
                option.toggle();

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_action_bar, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.extract_words:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void selectFile(View v) {
        Intent i = new Intent(this, BrowserActivity.class);
        i.putExtra("mode", "file");
        startActivityForResult(i, 0);
    }

    public void selectDir(View v) {
        Intent i = new Intent(this, BrowserActivity.class);
        i.putExtra("mode", "dir");
        startActivityForResult(i, 1);
    }

}
