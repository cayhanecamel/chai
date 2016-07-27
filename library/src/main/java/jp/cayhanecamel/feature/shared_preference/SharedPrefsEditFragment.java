
package jp.cayhanecamel.feature.shared_preference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import jp.cayhanecamel.champaca.R;


public class SharedPrefsEditFragment extends Fragment implements OnClickListener {

    public static final String ARG_NAME = "name";
    public static final String PREF_KEY = "pref_key";
    public static final String PREF_VALUE = "pref_value";
    public static final String PREF_TYPE = "pref_type";

    /**
     * 表示用
     */
    private View mView;
    private TextView mFileName;
    private TextView mPrefKeyTextView;
    private EditText mPrefValueTextView;
    private Button mPrefUpdateBtn;
    private RadioGroup mRadioGroup;
    private RadioButton mRadioButtonTrue;
    private RadioButton mRadioButtonFalse;

    /**
     * 内部用
     */
    private String mArgName;
    private String mPrefKey;
    private Object mPrefValue;
    private String mPrefType;

    private static final String TYPE_STRING = "String";
    private static final String TYPE_FLOAT = "Float";
    private static final String TYPE_INT = "Integer";
    private static final String TYPE_LONG = "Long";
    private static final String TYPE_BOOLEAN = "Boolean";

    public static SharedPrefsEditFragment newInstance(String name, String prefKey, Object prefValue, String prefType) {
        SharedPrefsEditFragment fragment = new SharedPrefsEditFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putString(PREF_KEY, prefKey);
        args.putString(PREF_VALUE, (String) prefValue);
        args.putString(PREF_TYPE, prefType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         * inflate
         */
        mView = inflater.inflate(R.layout.jp_cayhanecamel_champaca_fragment_shared_prefs_edit, container, false);

        // パラメータ受け取り
        Bundle args = getArguments();
        if (args != null && args.containsKey(ARG_NAME)) {
            mArgName = args.getString(ARG_NAME);
        }
        if (args != null && args.containsKey(PREF_KEY)) {
            mPrefKey = args.getString(PREF_KEY);
        }
        if (args != null && args.containsKey(PREF_VALUE)) {
            mPrefValue = args.getString(PREF_VALUE);
        }
        if (args != null && args.containsKey(PREF_TYPE)) {
            mPrefType = args.getString(PREF_TYPE);
        }
        /**
         * UIセットアップ
         */
        setupUi();
        return mView;
    }

    /**
     * UIセットアップ
     */
    private void setupUi() {
        mFileName = (TextView) mView.findViewById(R.id.jp_cayhanecamel_champaca_file_name);
        mPrefKeyTextView = (TextView) mView.findViewById(R.id.jp_cayhanecamel_champaca_pref_key);
        mPrefValueTextView = (EditText) mView.findViewById(R.id.jp_cayhanecamel_champaca_pref_value);
        mPrefUpdateBtn = (Button) mView.findViewById(R.id.jp_cayhanecamel_champaca_pref_update_btn);
        mPrefUpdateBtn.setOnClickListener(this);
        mRadioGroup = (RadioGroup) mView.findViewById(R.id.jp_cayhanecamel_champaca_radiogroup);
        mRadioButtonTrue = (RadioButton) mView.findViewById(R.id.jp_cayhanecamel_champaca_radiobutton_true);
        mRadioButtonFalse = (RadioButton) mView.findViewById(R.id.jp_cayhanecamel_champaca_radiobutton_false);

        if (mPrefType.equals(TYPE_BOOLEAN)) {
            mRadioGroup.setVisibility(View.VISIBLE);
            mPrefValueTextView.setVisibility(View.GONE);
            boolean valueBoolean = Boolean.valueOf(mPrefValue.toString());
            if (valueBoolean) {
                mRadioButtonTrue.setChecked(true);
                mRadioButtonFalse.setChecked(false);
            } else {
                mRadioButtonTrue.setChecked(false);
                mRadioButtonFalse.setChecked(true);
            }
        } else {
            mRadioGroup.setVisibility(View.GONE);
            mPrefValueTextView.setVisibility(View.VISIBLE);
        }

        mFileName.setText(mArgName);
        mPrefKeyTextView.setText(mPrefKey + " [" + mPrefType + "]");
        if (mPrefValue != null) {
            mPrefValueTextView.setText((String) mPrefValue);
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.jp_cayhanecamel_champaca_pref_update_btn) {
            int currentapiVersion = android.os.Build.VERSION.SDK_INT;
            if (currentapiVersion >= android.os.Build.VERSION_CODES.GINGERBREAD) {
                putValue();
            } else {
                Toast.makeText(getActivity(), "API level lower than 9", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @SuppressLint("NewApi")
    private void putValue() {
        if (mPrefType == null) {
            return;
        }

        SharedPreferences prefs = getActivity().getSharedPreferences(mArgName, Context.MODE_PRIVATE);
        String value = mPrefValueTextView.getText().toString();
        if (value != null) {
            value.trim();
        }
        if (mPrefType.equals(TYPE_BOOLEAN)) {
            boolean valueBoolean;

            valueBoolean = mRadioGroup.getCheckedRadioButtonId() == R.id.jp_cayhanecamel_champaca_radiobutton_true ? true : false;
            prefs.edit().putBoolean(mPrefKey, valueBoolean).apply();
        } else if (mPrefType.equals(TYPE_FLOAT)) {
            float valueFloat;
            try {
                valueFloat = Float.valueOf(value);
                prefs.edit().putFloat(mPrefKey, valueFloat).apply();
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Invalid data type", Toast.LENGTH_LONG).show();
                return;
            }

        } else if (mPrefType.equals(TYPE_INT)) {
            int valueInt;
            try {
                valueInt = Integer.valueOf(value);
                prefs.edit().putInt(mPrefKey, valueInt).apply();
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Invalid data type", Toast.LENGTH_LONG).show();
                return;
            }

        } else if (mPrefType.equals(TYPE_LONG)) {
            long valueLong;
            try {
                valueLong = Long.valueOf(value);
                prefs.edit().putLong(mPrefKey, valueLong).apply();
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Invalid data type", Toast.LENGTH_LONG).show();
                return;
            }

        } else if (mPrefType.equals(TYPE_STRING)) {
            String valueString;
            try {
                valueString = value;
                prefs.edit().putString(mPrefKey, valueString).apply();
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Invalid data type", Toast.LENGTH_LONG).show();
                return;
            }
        }
        Toast.makeText(getActivity(), "Updated!", Toast.LENGTH_SHORT).show();
    }
}
