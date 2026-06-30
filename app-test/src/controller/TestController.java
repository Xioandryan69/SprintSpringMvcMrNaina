package controller;

import framework.annotation.Controller;
import framework.annotation.Get;
import framework.annotation.UrlMapping;

@Controller
public class TestController {
    @Get("/employe-list")
    public void listEmploye() {
        
    }

    @Get("/employe-add")
    public void addEmploye() {
    }

    @Get("/andrana1")
    public void andrana() {
    }
    @UrlMapping(value= "/andrana1",method ="POST")
    public void andrana3() {
    }

}