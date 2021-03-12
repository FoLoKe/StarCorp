package com.foloke.starcorp.packer.Packers;

import java.io.File;
import java.io.IOException;

public interface ObjectsPacker {
    void pack(File src, File dst) throws IOException;
}
