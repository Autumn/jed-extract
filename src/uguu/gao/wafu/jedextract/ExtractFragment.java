package uguu.gao.wafu.jedextract;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * Created by aki on 18/05/13.
 */
public class ExtractFragment extends Fragment {

    View rootView = null;
    String savedPath = ".";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_extract_fragment, container, false);
        this.rootView = rootView;
        return rootView;
    }

    /* Attempt at persistent state for the selected paths
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if (rootView != null) {
            RadioButton dirOption = (RadioButton) rootView.findViewById(R.id.dirOption);
            if (dirOption.isSelected()) {
                TextView path = (TextView) rootView.findViewById(R.id.dirPath);
                savedInstanceState.putString("selected_dir", path.getText().toString());
            } else {
                TextView path = (TextView) rootView.findViewById(R.id.filePath);
                savedInstanceState.putString("selected_file", path.getText().toString());
            }
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && rootView != null) {
            if (savedInstanceState.containsKey("selected_dir")) {
                String dirPath = savedInstanceState.getString("selected_dir");
                TextView path = (TextView) rootView.findViewById(R.id.dirPath);
                path.setText(dirPath);
            } else if (savedInstanceState.containsKey("selected_path")) {
                String filePath = savedInstanceState.getString("selected_path");
                TextView path = (TextView) rootView.findViewById(R.id.filePath);
                path.setText(filePath);
            }
        }
    }*/
}
