package com.pharma.service;

import com.pharma.model.Drug;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DrugService {

    public List<Drug> getAllDrugs() {
        return List.of(
            new Drug(
                "Paracetamol",
                "Used to treat fever and mild pain.",
                "Take one tablet every 6 hours after food.",
                "Avoid overdose. Monitor liver function.",
                "Cipla",
                "2026-05",
                "Painkiller"
            ),
            new Drug(
                "Amoxicillin",
                "Antibiotic for bacterial infections.",
                "500mg twice daily after meals.",
                "Do not take if allergic to penicillin.",
                "Sun Pharma",
                "2025-11",
                "Antibiotic"
            ),
            new Drug(
                "Azithromycin",
                "Used to treat respiratory tract infections and COVID-related infections.",
                "500mg once daily for 3 days.",
                "Use under medical supervision. May cause stomach upset.",
                "Dr. Reddy's",
                "2026-02",
                "Antibiotic"
            ),
            new Drug(
                "Dolo 650",
                "Relieves pain and fever.",
                "1 tablet every 6-8 hours as needed.",
                "Avoid overdose. Liver function should be monitored in long-term use.",
                "Micro Labs",
                "2025-12",
                "Pain Reliever"
            ),
            new Drug(
                "Montair LC",
                "Used to treat allergic rhinitis and asthma symptoms.",
                "Take one tablet at bedtime.",
                "May cause drowsiness. Avoid alcohol.",
                "Cipla",
                "2026-07",
                "Antihistamine"
            ),
            new Drug(
                "Pantoprazole",
                "Treats acid reflux and gastric ulcers.",
                "40mg once daily before breakfast.",
                "Do not crush or chew. Avoid long-term use without doctor’s advice.",
                "Zydus Cadila",
                "2027-01",
                "Antacid"
            ),
            new Drug(
                "Metformin",
                "Used to control blood sugar in type 2 diabetes.",
                "500mg twice daily after meals.",
                "Monitor blood glucose levels regularly.",
                "USV Ltd.",
                "2026-06",
                "Anti-diabetic"
            ),
            new Drug(
                "Amlodipine",
                "Used for high blood pressure and chest pain.",
                "5mg once daily.",
                "Check blood pressure regularly. May cause dizziness.",
                "Lupin",
                "2027-03",
                "Anti-hypertensive"
            ),
            new Drug(
                "Ivermectin",
                "Anti-parasitic; was used off-label for COVID-19.",
                "12mg once daily for 3–5 days (doctor prescribed only).",
                "Not for self-medication. Use under doctor's guidance.",
                "Mankind Pharma",
                "2025-10",
                "Antiparasitic"
            ),
            new Drug(
                "Cetrizine",
                "Relieves allergy symptoms like runny nose, sneezing.",
                "One tablet at night.",
                "May cause drowsiness. Avoid driving.",
                "Alkem Laboratories",
                "2026-08",
                "Antihistamine"
            )
        );
    }

    public Drug getDrugByName(String name) {
        return getAllDrugs().stream()
                .filter(drug -> drug.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
