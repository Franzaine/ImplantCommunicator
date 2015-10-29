package se.franzaine.nfc.implantcommunicator;

import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements NfcAdapter.ReaderCallback {

    NfcAdapter nfcAdapter;
    final static private String TAG = "MainActivity";
    final private static char[] hexArray = "0123456789ABCDEF".toCharArray();

    TextView mainText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainText = (TextView) findViewById(R.id.main_text);
    }

    @Override
    protected void onResume() {
        super.onResume();
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.enableReaderMode(this, this, NfcAdapter.FLAG_READER_NFC_A, Bundle.EMPTY);
    }

    @Override
    protected void onPause() {
        nfcAdapter.disableReaderMode(this);
        super.onPause();
    }

    @Override
    public void onTagDiscovered(Tag tag) {
        final String tagId = encodeHex(tag.getId());
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                tagIdDiscovered(tagId);
            }
        });
    }

    private void tagIdDiscovered(String tagId) {
        Log.v(TAG, "Tag discovered! ID: " + tagId);
        mainText.setText("Tag discovered! ID: " + tagId);
    }

    /**
     * Encodes a byte array into a hexadecimal string having two characters per byte
     * @param bytes the input byte[]
     * @return the resulting hex string
     */
    public static String encodeHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int i = 0; i < bytes.length; i++ ) {
            int v = bytes[i] & 0xFF;
            hexChars[i * 2] = hexArray[v >>> 4];
            hexChars[i * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
