package raiffeisen.testapp.generic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by Ghita on 05/03/2017.
 */

public class RaiffeisenActivity extends AppCompatActivity implements RaiffeisenView {


    // boolean to check that app is not in background when
    // we change the view after ws response
    public boolean isInForeground;

    @Override
    public void showMessage(String message) {
        if (isInForeground) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isInForeground = true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        isInForeground = false;
    }


}
