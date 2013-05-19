package uguu.gao.wafu.jedextract;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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

    private static final int FILE_BROWSER = 0;
    private static final int DIR_BROWSER = 1;
    private static final int SELECTED = 1;
    private static final int NOT_SELECTED = 0;

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

    /*
    Processes the result of BrowserActivity. After a file is selected from BrowserActivity,
    an Intent with the FILENAME and the ABSOLUTE PATH bundled is returned.

     resultCode SELECTED is passed when a selection was made in BrowserActivity
      requestCode represents which Button launched the BrowserActivity.
      0 for FILE_BROWSER, 1 for DIR_BROWSER
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle bundle;
        String fileName;
        TextView tv;
        RadioButton option;
        if (resultCode == SELECTED) {
            if (requestCode == FILE_BROWSER) {
                bundle = data.getExtras();
                fileName = bundle.getString(FILE);
                selectedPath = bundle.getString(PATH);
                selectedFile = fileName;
                tv = (TextView) findViewById(R.id.filePath);
                tv.setText(fileName);
                option = (RadioButton) findViewById(R.id.fileOption);
                option.toggle();
                type = FILE_TYPE;
            } else if (requestCode == DIR_BROWSER) {
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

    /* Functions for the Button presses */

    public void selectFile(View v) {
        Intent i = new Intent(this, BrowserActivity.class);
        i.putExtra(MODE, FILE);
        startActivityForResult(i, FILE_BROWSER);
    }

    public void selectDir(View v) {
        Intent i = new Intent(this, BrowserActivity.class);
        i.putExtra(MODE, DIR);
        startActivityForResult(i, DIR_BROWSER);
    }


    /*
    * Process the files selected and launch ResultsActivity with the parsed results.
    * the ExtractWords class handles file handling and returns an ArrayList<String> with the results.
    * */

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
                // write to history

                StringBuilder sb = new StringBuilder();
                for (CharSequence word: words) {
                    sb.append(word);
                    sb.append("\n");
                }
                sb.deleteCharAt(sb.length() - 1); // delete the final newline
                String content = sb.toString();

                SQLiteDatabase db = DatabaseHelper.getInstance(this).getWritableDatabase();
                try {
                    db.beginTransaction();
                    ContentValues cv = new ContentValues();
                    cv.put("origin_file", selectedPath);
                    cv.put("content", content);
                    cv.put("count", words.size());
                    db.insert("HISTORY", null, cv);
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                    db.close();
                }

                bundle.putCharSequenceArrayList("list", cs_words);
                i.putExtras(bundle);
                startActivity(i);
            }
        } else {
            Toast.makeText(this, "Not a valid .anki file.", Toast.LENGTH_SHORT).show();
        }
    }


}
