package framework.util;
public class FileUpload 
{
    
    private String name;
    private String path;
    private byte[] bytes;
    private long size;

    public FileUpload(String name, String path, byte[] bytes, long size) {
        this.name = name;
        this.path = path;
        this.bytes = bytes;
        this.size = size;
    }

    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public byte[] getBytes() {
        return bytes;
    }
    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
    public long getSize() {
        return size;
    }
    public void setSize(long size) {
        this.size = size;
    }
}
