
package com.github.kari;

import com.github.kari.Parser;
import com.github.kari.HProfile.StringType;

import java.util.List;

import com.github.kari.HProfile;

/**
 * High available heap analyzer
 */
public class Haha {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            Log.d("Usage: Haha <hprof FILE path>");
            return;
        }
        HProfile profile = new Parser().parse(args[0]);
        Log.d("magic number:" + profile.header.magic);
        Log.d("id size:" + profile.header.idSize);
        Log.d("timestamp:" + Utils.formatTimeStamp(profile.header.stamp));

        Log.d("itemCount:" + profile.body.itemCount);
        Log.d("string table length:" + profile.body.stringTable.size());
        Log.d("class table length:" + profile.body.classTable.size());
        Log.d("stack trace table length:" + profile.body.traceTable.size());
    }
}