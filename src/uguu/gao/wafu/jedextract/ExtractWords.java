package uguu.gao.wafu.jedextract;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import SQLite3.Database;
import SQLite3.Stmt;
import SQLite3.Exception;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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

    private ArrayList<String> words = new ArrayList<String>();

    public ExtractWords(String path, String filename, int type) {
        this.path = path;
        this.file = filename;
        this.type = type;
        fileList = new ArrayList<File>();

        if (type == DIR) {
            // create list of files
            fileList = new ArrayList<File>();
            File dir = new File(path);
            File[] files = dir.listFiles();
            for (File file: files) {
                if (isAnkiType(file.getName())) {
                    fileList.add(file);
                    Log.e("file", file.getName());
                }
            }
        } else {
            if (isAnkiType(file)) {
                File f = new File(path);
                fileList.add(f);
                Log.e("file", f.getAbsolutePath());
            }
        }
    }

    // returns false if the file provided is not a .anki file
    // or if there are no .anki files in the directory supplied

    public boolean isValid() {
        if (fileList.size() == 0) {
                return false;
        } else {
            return true;
        }
    }

    // given a filename, returns whether it has a suffix ".anki"
    private boolean isAnkiType(String file) {
        return file.substring(file.lastIndexOf(".") + 1, file.length()).equals(ANKI_TYPE);
    }

    // returns false if extraction was not successful
    // should not occur

    public boolean extract() {
        for (File file: fileList) {
            Database db = new Database();
            try {
                db.open(file.getAbsolutePath(), 0666);
                Stmt stmt = db.prepare("select question from cards where ordinal = 1");
                while (stmt.step()) {
                    String word = extractTextFromTag(stmt.column_string(0));
                    words.add(word);
                }
            } catch (Exception e) {
                // shit
            }
            try {
                db.close();
            } catch (Exception e) {
                // truly screwed
            }
        }
        return true;
    }

    public static String extractTextFromTag(String xml) throws Exception {
        Document parsedXML = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xml));
            parsedXML = builder.parse(is);
        } catch (ParserConfigurationException pce) {}
          catch (SAXException s) {}
          catch (IOException i) {}

        return parsedXML.getFirstChild().getFirstChild().getNodeValue();
    }

    public ArrayList<String> listWords() {
        return words;
    }
}
