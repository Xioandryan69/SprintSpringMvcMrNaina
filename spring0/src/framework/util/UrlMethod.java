package framework.util;

import framework.annotation.UrlMapping;
import java.util.Objects;

public class UrlMethod {
    private String url;
    private String method;

    public UrlMethod(String url, String method) {
        this.url = url;
        this.method = method;
    }

    public UrlMethod(UrlMapping urlannotation) {
        this.url = urlannotation.value();
        this.method = urlannotation.method();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof UrlMethod))
            return false;
        UrlMethod other = (UrlMethod) obj;
        return Objects.equals(this.url, other.url) && Objects.equals(this.method, other.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.url, this.method);
    }

    @Override
    public String toString() {
        return "[method : " + method + "] " + url + "[url: " + url + "]";
    }

}
