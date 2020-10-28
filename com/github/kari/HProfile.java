package com.github.kari;

import java.util.List;

public class HProfile {
    // top-level records
    public static final byte TAG_STRING = 0x01;
    public static final byte TAG_LOAD_CLASS = 0x02;
    public static final byte TAG_UNLOAD_CLASS = 0x03;
    public static final byte TAG_FRAME = 0x04;
    public static final byte TAG_TRACE = 0x05;
    public static final byte TAG_ALLOC_SITES = 0x06;
    public static final byte TAG_HEAP_SUMMARY = 0x07;
    public static final byte TAG_START_THREAD = 0x0A;
    public static final byte TAG_END_THREAD = 0x0B;
    public static final byte TAG_DUMP = 0x0C;
    public static final byte TAG_CPU_SAMPLES = 0x0D;
    public static final byte TAG_CONTROL_SETTINGS = 0x0E;

    // 1.0.2 record types
    public static final byte TAG_HEAP_DUMP_SEGMENT = 0x1C;
    public static final byte TAG_HEAP_DUMP_END = 0x2C;

    // field types
    public static final byte TYPE_ARRAY_OBJECT = 0x01;
    public static final byte TYPE_NORMAL_OBJECT = 0x02;
    public static final byte TYPE_BOOLEAN = 0x04;
    public static final byte TYPE_CHAR = 0x05;
    public static final byte TYPE_FLOAT = 0x06;
    public static final byte TYPE_DOUBLE = 0x07;
    public static final byte TYPE_BYTE = 0x08;
    public static final byte TYPE_SHORT = 0x09;
    public static final byte TYPE_INT = 0x0A;
    public static final byte TYPE_LONG = 0x0B;

    // data-dump sub-records
    public static final byte GC_ROOT_UNKNOWN = (byte) 0xFF;
    public static final byte GC_ROOT_JNI_GLOBAL = 0x01;
    public static final byte GC_ROOT_JNI_LOCAL = 0x02;
    public static final byte GC_ROOT_JAVA_FRAME = 0x03;
    public static final byte GC_ROOT_NATIVE_STACK = 0x04;
    public static final byte GC_ROOT_STICKY_CLASS = 0x05;
    public static final byte GC_ROOT_THREAD_BLOCK = 0x06;
    public static final byte GC_ROOT_MONITOR_USED = 0x07;
    public static final byte GC_THREAD_OBJ = 0x08;
    public static final byte GC_CLASS_DUMP = 0x20;
    public static final byte GC_INSTANCE_DUMP = 0x21;
    public static final byte GC_OBJ_ARRAY_DUMP = 0x22;
    public static final byte GC_PRIM_ARRAY_DUMP = 0x23;

    public Header header;
    public Body body;

    public static class Header {
        public static final int MAGIC_LENGTH = 19;
        public String magic; // "JAVA PROFILE 1.0.2" (0-terminated)
        /*
         * size of identifiers. Identifiers are used to represent UTF8 strings, objects,
         * stack traces, etc. They usually have the same size as host pointers. For
         * example, on Solaris and Win32, the size is 4.
         */
        public int idSize;
        public long stamp; // number of milliseconds since 0:00 GMT, 1/1/70
    }

    public static class TypeItem {
        /**
         * number of *microseconds* since the time stamp in the header. (wraps around in
         * a little more than an hour)
         */
        public int stamp;
        public int length;
    }

    public static class StringType extends TypeItem {
        public long id; // actual size specified by idSize
        public String value;
    }

    public static class LoadClassType extends TypeItem {
        public int clsSerialNo;
        public int clsObjId;
        public int traceSerailNo;
        public int clsNameId;
    }

    public static class StackFrame extends TypeItem {
        public int stamp;
        public int length;
        public int serialNo;
        public int threadNo;
        public int framesNum;
    }

    public static class StackTrace extends TypeItem {
        public int stamp;
        public int length;
        public int serialNo;
        public int threadNo;
        public int framesNum;
        public List<Long> frames;
    }

    public static class Dump {
        public int stamp;
        public int length;
    }

    public static class DumpSegment extends Dump {

    }

    public static class Body {
        public int itemCount;
        public List<StringType> stringTable;
        public List<LoadClassType> classTable;
        public List<StackFrame> frameTable;
        public List<StackTrace> traceTable;
        public List<Dump> dumps;
    }
}