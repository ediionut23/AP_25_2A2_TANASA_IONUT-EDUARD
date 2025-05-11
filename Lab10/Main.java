package org.example;

import org.example.model.Continent;
import org.example.repository.ContinentRepository;
import org.example.util.PersistenceUtil;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        ContinentRepository repo = new ContinentRepository();

        repo.create(new Continent("Europe"));
        repo.create(new Continent("Asia"));

        Continent c1 = repo.findById(1L);
        System.out.println("Found by ID: " + c1.getName());

        List<Continent> results = repo.findByName("A%");
        System.out.println("Found by name (A%):");
        for (Continent c : results) {
            System.out.println(c.getName());
        }

        PersistenceUtil.close();
    }
}
