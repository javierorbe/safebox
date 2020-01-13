package deusto.safebox.common.util;

public interface ConfigFile {

    int getInt(String path);

    void setInt(String path, int value);

    String getString(String path);

    void setString(String path, String value);

    void save();
}
