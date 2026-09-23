package framework.util;

public class Mapping {
    private String className;
    private String method;

    public Mapping(String className, String methode) {
        this.className = className;
        this.method = methode;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String methode) {
        this.method = methode;
    }
     
}
