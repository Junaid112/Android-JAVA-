package com.AnasKhan.BSEF10M013.HW3;

import java.text.SimpleDateFormat;
import java.util.*;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;

public class PackageInformationUtil {
	public static ArrayList<InstalledApps> getApps(Context context) {
		ArrayList<InstalledApps> apps = getInstalledApps(context, true);
		return apps;
	}

	@SuppressLint({ "NewApi", "SimpleDateFormat" })
	private static ArrayList<InstalledApps> getInstalledApps(Context context,
			boolean b) {
		ArrayList<InstalledApps> res = new ArrayList<InstalledApps>();
		List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(0);
		for(int i=0;i<packs.size();i++)
		{
			PackageInfo p=packs.get(i);
			if((!b)&&p.versionName==null)
			{
				continue;
			}
			InstalledApps apps = new InstalledApps();
			apps.setAppName(p.applicationInfo.loadLabel(context.getPackageManager()).toString());
			apps.setpName(p.packageName);
			apps.setVersionCode(p.versionCode);
			apps.setVersionName(p.versionName);
			apps.setIcon(p.applicationInfo.loadIcon(context.getPackageManager()));
			Calendar calendar = Calendar.getInstance();
	        calendar.setTimeInMillis(p.firstInstallTime);
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			apps.setDate(format.format(calendar.getTime()).toString());
			res.add(apps);
		}
		return res;
	}
}
