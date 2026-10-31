package framework.util;

import jakarta.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class MySession {
    private HttpSession httpSession;
    private Map<String, Object> sessionMap = new HashMap<>();

    public MySession(HttpSession httpSession) {
        this.httpSession = httpSession;
        if (httpSession != null) {
            Enumeration<String> attributeNames = httpSession.getAttributeNames();
            while (attributeNames.hasMoreElements()) {
                String key = attributeNames.nextElement();
                sessionMap.put(key, httpSession.getAttribute(key));
            }
        }
    }

    public Object get(String key) {
        return sessionMap.get(key);
    }

    public void set(String key, Object value) {
        sessionMap.put(key, value);
    }

    public void remove(String key) {
        sessionMap.remove(key);
    }

    public Map<String, Object> getData() {
        return sessionMap;
    }

    // Fomba fiasa handefasana sy hanavaozana ny HttpSession tena izy rehefa vita ny invoke
    public void updateHttpSession() {
        if (httpSession != null) {
         
            Enumeration<String> attributeNames = httpSession.getAttributeNames();
            while (attributeNames.hasMoreElements()) {
                String key = attributeNames.nextElement();
                if (!sessionMap.containsKey(key)) {
                    httpSession.removeAttribute(key);
                }
            }
        
            for (Map.Entry<String, Object> entry : sessionMap.entrySet()) {
                httpSession.setAttribute(entry.getKey(), entry.getValue());
            }
        }
    }
}