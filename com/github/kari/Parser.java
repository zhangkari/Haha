package com.github.kari;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Parser {
    private DataInputStream mInputStream;
    private int mIdSize;

    public HProfile parse(String path) throws Exception {
        File file = new File(path);
        HProfile profile = new HProfile();
        mInputStream = new DataInputStream(new FileInputStream(file));
        try {
            profile.header = parseHeader();
            mIdSize = profile.header.idSize;
            profile.body = parseBody();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mInputStream.close();
        }
        return profile;
    }

    private HProfile.Body parseBody() throws Exception {
        HProfile.Body body = new HProfile.Body();
        body.itemCount = 0;
        int tag;
        while ((tag = readByte()) > 0) {
            body.itemCount++;
            switch (tag) {
            case HProfile.TAG_STRING:
                if (body.stringTable == null) {
                    body.stringTable = new ArrayList<>();
                }
                body.stringTable.add(parseStringItem());
                break;

            case HProfile.TAG_LOAD_CLASS:
                if (body.classTable == null) {
                    body.classTable = new ArrayList<>();
                }
                body.classTable.add(parseLoadClassItem());
                break;

            case HProfile.TAG_UNLOAD_CLASS:
                Log.d("unload class");
                break;

            case HProfile.TAG_FRAME:
                Log.d("frame");
                break;

            case HProfile.TAG_TRACE:
                if (body.traceTable == null) {
                    body.traceTable = new ArrayList<>();
                }
                body.traceTable.add(parseStackTrace());
                break;

            case HProfile.TAG_ALLOC_SITES:
                Log.d("alloc sites");
                break;

            case HProfile.TAG_HEAP_SUMMARY:
                Log.d("heap summary");
                break;

            case HProfile.TAG_START_THREAD:
                Log.d("start thread");
                break;

            case HProfile.TAG_END_THREAD:
                Log.d("end thread");
                break;

            case HProfile.TAG_DUMP:
                Log.d("dump");
                break;

            case HProfile.TAG_CPU_SAMPLES:
                Log.d("cpu samples");
                break;

            case HProfile.TAG_CONTROL_SETTINGS:
                Log.d("control settings");
                break;

            case HProfile.TAG_HEAP_DUMP_SEGMENT:
                Log.d("dump segment");
                if (body.dumps == null) {
                    body.dumps = new ArrayList<>();
                }
                body.dumps.add(parseHeapDump());
                break;

            default:
                Log.d("default");
                continue;
            }
        }
        return body;
    }

    private HProfile.Header parseHeader() throws Exception {
        HProfile.Header header = new HProfile.Header();
        header.magic = readNullTerminatedString();
        header.idSize = readInt();
        header.stamp = readLong();
        return header;
    }

    private String readNullTerminatedString() throws Exception {
        StringBuilder sb = new StringBuilder();
        for (byte val = (byte) mInputStream.read(); val != 0; val = (byte) mInputStream.read()) {
            sb.append((char) val);
        }
        return sb.toString();
    }

    private HProfile.StringType parseStringItem() throws Exception {
        HProfile.StringType str = new HProfile.StringType();
        str.stamp = readInt();
        str.length = readInt();
        str.id = readId();
        str.value = readString(str.length - mIdSize);
        return str;
    }

    private long readId() throws IOException {
        switch (mIdSize) {
        case 1:
            return mInputStream.readByte();

        case 2:
            return mInputStream.readShort();

        case 4:
            return mInputStream.readInt();

        case 8:
            return mInputStream.readLong();

        default:
            throw new IllegalArgumentException("Invalid idSize");
        }
    }

    private HProfile.LoadClassType parseLoadClassItem() throws Exception {
        HProfile.LoadClassType cls = new HProfile.LoadClassType();
        cls.stamp = readInt();
        cls.length = readInt();
        cls.clsObjId = readInt();
        cls.clsSerialNo = readInt();
        cls.traceSerailNo = readInt();
        cls.clsNameId = readInt();
        return cls;
    }

    private HProfile.StackTrace parseStackTrace() throws IOException {
        HProfile.StackTrace trace = new HProfile.StackTrace();
        trace.stamp = readInt();
        trace.length = readInt();
        trace.serialNo = readInt();
        trace.threadNo = readInt();
        trace.framesNum = readInt();
        if (trace.frames == null) {
            trace.frames = new ArrayList<>();
        }
        for (int i = 0; i < trace.framesNum; i++) {
            trace.frames.add(readId());
        }
        return trace;
    }

    private HProfile.Dump parseHeapDump() throws IOException {
        HProfile.Dump dump = new HProfile.Dump();
        dump.stamp = readInt();
        dump.length = readInt();
        int length = dump.length;
        while (length > 0) {
            int tag = readByte();
            long id = readId();
            switch (tag) {
            case HProfile.GC_ROOT_UNKNOWN:
                Log.d("gc root unknown");
                break;

            case HProfile.GC_ROOT_JNI_GLOBAL:
                Log.d("gc root jni global");
                break;

            case HProfile.GC_ROOT_JNI_LOCAL:
                Log.d("gc root jni local");
                break;

            case HProfile.GC_ROOT_JAVA_FRAME:
                Log.d("gc root java frame");
                break;

            case HProfile.GC_ROOT_NATIVE_STACK:
                Log.d("gc root native stack");
                break;

            case HProfile.GC_ROOT_STICKY_CLASS:
                Log.d("gc root sticky class");
                break;

            case HProfile.GC_ROOT_THREAD_BLOCK:
                Log.d("gc root thread block");
                break;

            case HProfile.GC_ROOT_MONITOR_USED:
                Log.d("gc root jni monitor used");
                break;

            case HProfile.GC_THREAD_OBJ:
                Log.d("gc thread obj");
                break;

            case HProfile.GC_CLASS_DUMP:
                Log.d("gc class dump");
                break;

            case HProfile.GC_INSTANCE_DUMP:
                Log.d("gc instance dump");
                break;

            case HProfile.GC_OBJ_ARRAY_DUMP:
                Log.d("gc obj array dump");
                break;

            case HProfile.GC_PRIM_ARRAY_DUMP:
                Log.d("gc prim array dump");
                break;

            }
        }
        return dump;
    }

    private int readByte() throws IOException {
        return mInputStream.readByte();
    }

    private int readInt() throws IOException {
        return mInputStream.readInt();
    }

    private long readLong() throws IOException {
        return mInputStream.readLong();
    }

    private String readString(int length) throws IOException {
        byte[] buffer = new byte[length];
        mInputStream.read(buffer);
        return new String(buffer);
    }
}