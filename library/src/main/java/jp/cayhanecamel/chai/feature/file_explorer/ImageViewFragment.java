package jp.cayhanecamel.chai.feature.file_explorer;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import jp.cayhanecamel.chai.util.ChaiUtil;
import jp.cayhanecamel.chai.R;

public class ImageViewFragment extends DialogFragment {

    private File imageFile;

    public ImageViewFragment() {
        super();
        // do nothing. for rotate diplasy etc
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if (!imageFile.exists()) {
            dismissAllowingStateLoss();
            Toast.makeText(ChaiUtil.getApplicationContext(), "file not found!", Toast.LENGTH_LONG).show();
        }

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.jp_cayhanecamel_chai_fragment_image_viewer, null,
                false);

        ImageView image = (ImageView) view.findViewById(R.id.jp_cayhanecamel_champaca_image);
        TextView size = (TextView) view.findViewById(R.id.jp_cayhanecamel_champaca_size);


        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);

        image.setImageBitmap(bitmap);

        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;

        size.setText(size.getText().toString() + imageWidth + "px" + " : " + imageHeight + "px");

        Dialog dialog = new Dialog(getActivity());
        dialog.setTitle(imageFile.getName());
        dialog.setContentView(view);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;

    }

    public void show(FragmentActivity hostActivity, String tag) {
        assert (hostActivity != null);
        FragmentTransaction transaction = hostActivity
                .getSupportFragmentManager().beginTransaction();
        transaction.add(this, tag);
        transaction.show(this);
        transaction.commitAllowingStateLoss();
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

}
