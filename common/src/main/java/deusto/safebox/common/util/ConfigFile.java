package deusto.safebox.common.util;

public interface ConfigFile {

    int getInt(String path);

    void setInt(int value, String path);

    String getString(String path);

    void setString(String value, String path);

    void save();
}
