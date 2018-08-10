package com.AnasKhan.BSEF10M013.HW3;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends ListActivity {

	String pkg = "";
	String [] details;
	ArrayList<com.AnasKhan.BSEF10M013.HW3.InstalledApps> list;
	int index;
	AppAdapter adapter;
    ProgressBar pB1;
	TextView tV1;
    
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = PackageInformationUtil.getApps(this);
        adapter = new AppAdapter(this, 0, list);
        this.setListAdapter(adapter);
        ListView lv = this.getListView();
        registerForContextMenu(lv);
        pB1 = (ProgressBar) findViewById(R.id.internal);
        tV1 = (TextView) findViewById(R.id.disk);
        StatFs diskStat = new StatFs(Environment.getDataDirectory().getPath());
        int diskBlockSize = diskStat.getBlockSize();
        int diskTotalBlock = diskStat.getBlockCount();
        int diskAvailableBlocks = diskStat.getAvailableBlocks();
        String diskAvailableSpace = Formatter.formatShortFileSize(this, diskAvailableBlocks * diskBlockSize);
        tV1.setText("Disk Space: " + diskAvailableSpace + " free");
        pB1.setMax(diskTotalBlock * diskBlockSize);
        pB1.setProgress(diskAvailableBlocks * diskBlockSize);
    }

	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.setHeaderTitle("AppManager");
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.context_menu, menu);
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		index = info.position;
		final InstalledApps iApps = (InstalledApps) getListAdapter().getItem(info.position);
		pkg = iApps.getpName();
		String version = iApps.getVersionName();
		String versionCode = "" + iApps.getVersionCode();
		String date = iApps.getDate();
		details = new String[5];
		details[0] = ("NAME: " + iApps.getAppName());
		details[1] = ("PACKAGE: " + pkg);
		details[2] = ("VERSION: " + version);
		details[3] = ("VERSION CODE: " + versionCode);
		details[4] = ("DATE INSTALLED: " + date);
	}

	public boolean onContextItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case(R.id.launch): {
					PackageManager  pmi = getPackageManager();
				    Intent intent = null;
				    intent = pmi.getLaunchIntentForPackage(pkg);                    
				    if (intent != null)
				    {
				    	startActivity(intent);
				    }
				}
				break;
			case(R.id.uninstall): {
					Intent intent = new Intent(Intent.ACTION_DELETE);
				    intent.setData(Uri.parse("package:" + pkg));
				    startActivity(intent);
				    list.remove(index);
				    adapter.notifyDataSetChanged();
				}
				break;
			case(R.id.details): {
					AlertDialog.Builder detail = new AlertDialog.Builder(MainActivity.this);
			    	detail.setTitle("App Details");
			    	detail.setItems(details, new DialogInterface.OnClickListener() 
			    	{
						public void onClick(DialogInterface arg0, int arg1) {
						}
			    	});
			    	detail.create().show();
				}
				break;
			default: return super.onContextItemSelected(item);
		}
		return true;
	}


    public boolean onCreateOptionsMenu(Menu menu) {
    	getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case(R.id.about): {
					String [] about = new String[2];
					about[0] = "NAME: M. Anas Khan Sherwani";
					about[1] = "ROLL #: BSEF10M013";
					AlertDialog.Builder detail = new AlertDialog.Builder(MainActivity.this);
			    	detail.setTitle("About Me");
			    	detail.setItems(about, new DialogInterface.OnClickListener() 
			    	{
						public void onClick(DialogInterface arg0, int arg1) {
						}
			    	});
			    	detail.create().show();
				}
				break;
			case(R.id.exit): finish();
				break;
			default: 
				return super.onOptionsItemSelected(item);
		}
		return true;
	}

	public class AppAdapter extends ArrayAdapter<InstalledApps>{

		public AppAdapter(Context context, int textViewResourceId, ArrayList<InstalledApps> list) {
			super(context, textViewResourceId, list);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.app_list_item, null);
			com.AnasKhan.BSEF10M013.HW3.InstalledApps iApps = (com.AnasKhan.BSEF10M013.HW3.InstalledApps) getItem(position);

			((ImageView)v.findViewById(R.id.imageView1)).setImageDrawable(iApps.getIcon());
			((TextView)v.findViewById(R.id.textView1)).setText(iApps.getAppName());
			((TextView)v.findViewById(R.id.textView2)).setText(iApps.getVersionName());
			((TextView)v.findViewById(R.id.textView3)).setText(iApps.getDate());
			
			return v;
		}
    }
}