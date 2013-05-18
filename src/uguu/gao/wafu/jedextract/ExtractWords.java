package uguu.gao.wafu.jedextract;

import android.util.Log;
import android.widget.ArrayAdapter;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by aki on 18/05/13.
 */
public class ExtractWords {


    private static final int FILE = 0;
    private static final int DIR = 1;
    private static final String ANKI_TYPE = "anki";

    private ArrayList<File> fileList = null;

    private String path;
    private String file;
    private int type;

    public ExtractWords(String path, String filename, int type) {
        this.path = path;
        this.file = filename;
        this.type = type;
        if (type == DIR) {
            // create list of files
            fileList = new ArrayList<File>();
            File dir = new File(path);
            File[] files = dir.listFiles();
            for (File file: files) {
                if (isAnkiType(file.getName())) {
                    fileList.add(file);
                }
            }

        }
    }

    // returns false if the file provided is not a .anki file
    // or if there are no .anki files in the directory supplied

    public boolean isValid() {
        if (type == FILE) {
            if (isAnkiType(file)) {
                return true;
            }
        } else {
            if (fileList.size() == 0) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    // given a filename, returns whether it has a suffix ".anki"
    private boolean isAnkiType(String file) {
        return file.substring(file.lastIndexOf(".") + 1, file.length()).equals(ANKI_TYPE);
    }

    // returns false if extraction was not successful
    // should not occur

    public boolean extract() {

        return false;
    }
}
