package uguu.gao.wafu.jedextract;

import android.app.Activity;
import android.content.Intent;
import android.inputmethodservice.ExtractEditText;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private static final String FILE = "file";
    private static final String PATH = "path";
    private static final String DIR = "dir";
    private static final String MODE = "mode";

    private static final int FILE_TYPE = 0;
    private static final int DIR_TYPE = 1;

    private String selectedPath = "";
    private String selectedFile = "";
    private int type = FILE_TYPE;

    MainPagerAdapter mainPagerAdapter;
    ViewPager viewPager;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fragment_container);

        mainPagerAdapter = new MainPagerAdapter(getFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(mainPagerAdapter);
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
                fileName = bundle.getString(FILE);
                selectedPath = bundle.getString(PATH);
                selectedFile = fileName;
                tv = (TextView) findViewById(R.id.filePath);
                tv.setText(fileName);
                option = (RadioButton) findViewById(R.id.fileOption);
                option.toggle();
                type = FILE_TYPE;
            } else if (requestCode == 1) {
                bundle = data.getExtras();
                fileName = bundle.getString(FILE);
                selectedPath = bundle.getString(PATH);
                selectedFile = fileName;
                tv = (TextView) findViewById(R.id.dirPath);
                tv.setText(fileName);
                option = (RadioButton) findViewById(R.id.dirOption);
                option.toggle();
                type = DIR_TYPE;
            }
        }
    }

    public void extractWords() {
        ExtractWords extractor = new ExtractWords(selectedPath, selectedFile, type);
        if (extractor.isValid()) {
            if (extractor.extract()) {
                ArrayList<String> words = extractor.listWords();
                Intent i = new Intent(MainActivity.this, ResultsActivity.class);
                Bundle bundle = new Bundle();
                ArrayList<CharSequence> cs_words = new ArrayList<CharSequence>();
                for (String word: words) {
                    cs_words.add(word);
                }
                bundle.putCharSequenceArrayList("list", cs_words);
                i.putExtras(bundle);
                startActivity(i);
            }
        } else {
            Toast.makeText(this, "Not a valid .anki file.", Toast.LENGTH_SHORT).show();
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
                extractWords();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void selectFile(View v) {
        Intent i = new Intent(this, BrowserActivity.class);
        i.putExtra(MODE, FILE);
        startActivityForResult(i, 0);
    }

    public void selectDir(View v) {
        Intent i = new Intent(this, BrowserActivity.class);
        i.putExtra(MODE, DIR);
        startActivityForResult(i, 1);
    }

}
