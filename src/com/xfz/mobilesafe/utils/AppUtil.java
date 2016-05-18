package com.xfz.mobilesafe.utils;

import java.io.File;

import android.os.Environment;
import android.os.StatFs;

public class AppUtil {
	/**
	 * get sd card available storage size
	 */
	public static long getAvailableSD(){
		 File path = Environment.getExternalStorageDirectory();
         StatFs stat = new StatFs(path.getPath());
         long blockSize = stat.getBlockSize();
         long totalBlocks = stat.getBlockCount();
         long availableBlocks = stat.getAvailableBlocks();
         return availableBlocks*blockSize;
	}
	/**
	 *get ROM available storage size
	 * @return
	 */
	public static long getAvailableROM(){
		File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks*blockSize;
	}
	
	
	
	
	
	
	
	
}
