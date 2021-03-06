
package org.whispercomm.shout.ui;

import org.whispercomm.shout.R;
import org.whispercomm.shout.tutorial.TutorialActivity;
import org.whispercomm.shout.tutorial.TutorialManager;

import android.app.AlertDialog;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * The main activity for Shout. The activity displays a list of all received
 * shouts and provides interfaces for shouting, reshouting, and commenting.
 * 
 * @author David R. Bild
 */
public class ShoutActivity extends AbstractShoutViewActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	protected void initialize() {
		super.initialize();
		TutorialManager.showHelp(this);
		setContentView(R.layout.activity_shout);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_shout, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.settings:
				SettingsActivity.show(this);
				break;
			case R.id.compose:
				MessageActivity.shout(this);
				break;
			case R.id.help:
				TutorialActivity.show(this);
				break;
			case R.id.about:
				AlertDialog dialog = DialogFactory.aboutDialog(this);
				dialog.show();
				break;
			default:
				return super.onOptionsItemSelected(item);
		}

		return true;
	}
}
