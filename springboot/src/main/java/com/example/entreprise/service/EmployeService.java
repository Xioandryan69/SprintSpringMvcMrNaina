package com.example.entreprise.service;

import com.example.entreprise.EntrepriseApplication;
import com.example.entreprise.entity.Employe;
import com.example.entreprise.repository.EmployeRepository;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeService {

    private static volatile ConfigurableApplicationContext context;

    private final EmployeRepository employeRepository;

    public EmployeService(EmployeRepository employeRepository) {
        this.employeRepository = employeRepository;
    }

    public List<Employe> findAll() {
        return employeRepository.findAll();
    }

    public static List<String> listEmployes() {
        return getInstance().findAll().stream()
                .map(employe -> employe.getNom() + " " + employe.getPrenom()
                        + " - " + employe.getPoste())
                .collect(Collectors.toList());
    }

    private static EmployeService getInstance() {
        if (context == null) {
            synchronized (EmployeService.class) {
                if (context == null) {
                    context = new SpringApplicationBuilder(EntrepriseApplication.class)
                            .web(WebApplicationType.NONE)
                            .run();
                }
            }
        }
        return context.getBean(EmployeService.class);
    }
}