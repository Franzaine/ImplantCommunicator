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

        /*
         * Start NFC handling when activity is in foreground
         * Activity The current activity
         * NfcAdapter.ReaderCallback Implemented in the class declaration
         * int flags. Flags indicating poll technologies and other optional parameters
         * Bundle Extra options for longer transactions and such. Not needed in our example.
         */
        nfcAdapter.enableReaderMode(this, this, NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK, Bundle.EMPTY);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop NFC handling when activity is not in foreground any more
        nfcAdapter.disableReaderMode(this);
        super.onPause();
    }

    @Override
    public void onTagDiscovered(Tag tag) {
        //Encode the tag ID from byte array to hex string
        final String tagId = encodeHex(tag.getId());

        //Run on UI thread
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
