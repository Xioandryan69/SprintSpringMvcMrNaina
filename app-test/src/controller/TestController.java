// // package controller;

// // import framework.annotation.Controller;
// // import framework.annotation.Get;
// // import framework.annotation.UrlMapping;
// // import framework.util.ModelView;

// // @Controller
// // public class TestController {
// //     @Get("/employe-list")
// //     public ModelView listEmploye() {
// //         ModelView mv = new ModelView("employe-list.jsp");
        
// //         // Ajout de données de test à transmettre à la JSP
// //         mv.addItem("titre", "Liste des Employés");
// //         mv.addItem("message", "Bienvenue sur la page de gestion des employés !");
        
// //         return mv;
// //     }

// //     @Get("/employe-add")
// //     public void addEmploye() {
// //     }

// //     @Get("/andrana1")
// //     public void andrana() {
// //     }
// //     @UrlMapping(value= "/andrana1",method ="POST")
// //     public void andrana3() {
// //     }

// // }

// package controller;

// import framework.annotation.Controller;
// import framework.annotation.Get;
// import framework.annotation.UrlMapping;
// import framework.util.DatabaseConnection;
// import framework.util.ModelView;

// import java.sql.Connection;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;
// import java.util.ArrayList;
// import java.util.List;

// @Controller
// public class TestController { 

//     @Get("/employe-list")
//     public ModelView listEmploye() {
//         ModelView mv = new ModelView("employe-list.jsp");
//         List<String> listeEmployes = new ArrayList<>();

//         try (Connection conn = DatabaseConnection.getConnection();
//              PreparedStatement stmt = conn.prepareStatement("SELECT nom, prenom, poste FROM employe");
//              ResultSet rs = stmt.executeQuery()) {

//             while (rs.next()) {
//                 String emp = rs.getString("nom") + " " + rs.getString("prenom") + " (" + rs.getString("poste") + ")";
//                 listeEmployes.add(emp);
//             }

//             mv.addItem("titre", "Liste des Employés");
//             mv.addItem("message", "Données récupérées directement depuis MySQL !");
//             mv.addItem("employes", listeEmployes);

//         } catch (Exception e) {
//             e.printStackTrace();
//             mv.addItem("message", "Erreur lors de la connexion à la base de données : " + e.getMessage());
//         }

//         return mv;
//     }

//     @Get("/employe-add")
//     public void addEmploye() {
//     }

//     @Get("/andrana1")
//     public void andrana() {
//     }
//     @UrlMapping(value= "/andrana1",method ="POST")
//     public void andrana3() {
//     }
// }

package controller;

import framework.annotation.Controller;
import framework.annotation.Get;
import framework.annotation.WebApi;
import framework.util.ModelView;

import java.util.HashMap;
import java.util.Map;

@Controller
public class TestController { 

    // Exemple 1 : Méthode renvoyant un objet/map en JSON
    @Get("/api/status")
    @WebApi
    public Map<String, Object> getStatusApi() {
        Map<String, Object> status = new HashMap<>();
        status.put("code", 200);
        status.put("message", "API Fonctionnelle");
        return status;
    }

    // Exemple 2 : Méthode renvoyant un ModelView transformé automatiquement en JSON
    @Get("/api/employes")
    @WebApi
    public ModelView getEmployesJson() {
        ModelView mv = new ModelView();
        mv.addItem("titre", "Liste API");
        mv.addItem("total", 2);
        return mv;
    }
}