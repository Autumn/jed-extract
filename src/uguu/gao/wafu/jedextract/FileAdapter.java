package uguu.gao.wafu.jedextract;

/**
 * Created by aki on 17/05/13.
 */

import java.io.File;
import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FileAdapter extends ArrayAdapter<File> {
    private ArrayList<File> items;
    private Context c = null;

    public String currentPath = "";


    public FileAdapter(Context context, int textViewResourceId, ArrayList<File> items) {
        super(context, textViewResourceId, items);
        this.items = items;
        this.c = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.browser_row, null);
        }
        TextView name = null;
        ImageView icon = null;
        TextView id = null;

        File f = items.get(position);
        currentPath = items.get(1).getAbsolutePath();

        if (f != null) {
            name = (TextView) v.findViewById(R.id.fileName);
            icon = (ImageView) v.findViewById(R.id.fileTypeIcon);
            id = (TextView) v.findViewById(R.id.typeId);

        }
        if (name != null) {
            if (position == 0) {
                name.setText(f.getAbsolutePath());
                id.setText("home");
            } else if (position == 1) {
                name.setText(f.getAbsolutePath());
                id.setText("back");
            } else if (f.isDirectory()) {
                name.setText(f.getName());
                id.setText("dir");
            } else {
                name.setText(f.getName());
                id.setText("file");
            }
        }

        if (icon != null) {
            if (position == 0) {
                icon.setImageResource(R.drawable.ic_menu_home);
            } else if (position == 1) {
                icon.setImageResource(R.drawable.ic_menu_back);
            } else if (f.isDirectory()) {
                icon.setImageResource(R.drawable.ic_menu_archive);
            } else {
                icon.setImageResource(R.drawable.ic_menu_attachment);
            }
        }


        return v;
    }

    public String getCurrentPath() {
        return currentPath;
    }
}
