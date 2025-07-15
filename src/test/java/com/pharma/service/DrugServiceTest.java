package com.pharma.service;

import com.pharma.model.Drug;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DrugServiceTest {

    private final DrugService service = new DrugService();

    @Test
    public void testGetAllDrugs() {
    List<Drug> drugs = service.getAllDrugs();

    assertEquals(10, drugs.size()); 
    assertEquals("Paracetamol", drugs.get(0).getName()); 
    
    }

    @Test
    public void testGetDrugByNameFound() {
        Drug drug = service.getDrugByName("Amoxicillin");
        assertNotNull(drug);
        assertEquals("Amoxicillin", drug.getName());
        assertEquals("Antibiotic", drug.getCategory());
    }

    @Test
    public void testGetDrugByNameNotFound() {
        Drug drug = service.getDrugByName("UnknownDrug");
        assertNull(drug);
    }
}
