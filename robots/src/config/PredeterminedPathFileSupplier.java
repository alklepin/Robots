package config;

import java.io.File;

public class PredeterminedPathFileSupplier implements FileSupplier{
    private File path;

    public PredeterminedPathFileSupplier(String path) {
        this.path = new File(path);
    }

    @Override
    public File getFile() {
        return path;
    }
}
