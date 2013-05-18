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

public class ResultsAdapter extends ArrayAdapter<CharSequence> {
    private ArrayList<File> items;
    private Context c = null;

    public String currentPath = "";

    public ResultsAdapter(Context context, int textViewResourceId, ArrayList<CharSequence> items) {
        super(context, textViewResourceId, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

}
