<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>Sprint 5 - Tarree Hojjettootaa</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        table { border-collapse: collapse; width: 60%; margin-top: 15px; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #4CAF50; color: white; }
        tr:nth-child(even) { background-color: #f2f2f2; }
    </style>
</head>
<body>
    <h1>${titre}</h1>
    <p>${message}</p>
    
    <hr>

    <h3>Tarree Hojjettootaa:</h3>
    
    <%-- Tarree hojjettootaa controllers irraa ergame argarsiisuuf --%>
    <% 
        List<String> employes = (List<String>) request.getAttribute("employes");
        if (employes != null && !employes.isEmpty()) {
    %>
        <table>
            <thead>
                <tr>
                    <th>#</th>
                    <th>Maqaa fi Posti Hojjetaa</th>
                </tr>
            </thead>
            <tbody>
                <% 
                    int index = 1;
                    for (String emp : employes) { 
                %>
                    <tr>
                        <td><%= index++ %></td>
                        <td><%= emp %></td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    <% 
        } else { 
    %>
        <p style="color: red;">Hojjetaan tokkollee maapaa database irraa hin argamne.</p>
    <% 
        } 
    %>

    <br><hr>
    <p style="color: gray; font-size: 0.8em;">Rendu généré via WEB-INF/views/</p>
</body>
</html>