package be.profacile.formafacile.domain.repository;
import be.profacile.formafacile.domain.model.*;
import be.profacile.formafacile.domain.model.Module;
import be.profacile.formafacile.domain.model.Format;
import be.profacile.formafacile.domain.repository.*;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static be.profacile.formafacile.domain.support.JpaExerciseSupport.*;

import be.profacile.formafacile.domain.support.*;
import org.junit.jupiter.api.Test;
class NoteInterneRepositoryTest {
 @Test void respecterLeDiagrammeJpa() {FormaContract.verifierNoteInterne();}
 @Test void sauvegarderRelireEtRechercher() throws Exception {
  FormaContract.verifierNoteInterne();
  FormaContract.enumerations();
  try(var base=new BaseJpa(FormaFixtures.ENTITES)) {
   var objets=FormaFixtures.creer();
   var repository=base.creerRepository(NoteInterneRepository.class);
   base.entityManager.getTransaction().begin();
   for(var entree:objets.entrySet()) {
    if(entree.getKey().equals("NoteInterne")) repository.saveAndFlush(entree.getValue());
    else base.entityManager.persist(entree.getValue());
   }
   base.entityManager.flush();
   Long id=(Long)lire(objets.get("NoteInterne"),"id");assertNotNull(id);
   base.entityManager.clear();
   Object relu=repository.findById(id).orElseThrow();
   assertEquals(lire(objets.get("NoteInterne"),"contenu"),lire(relu,"contenu"),"Valeur persistée de contenu");
assertEquals(lire(objets.get("NoteInterne"),"dateCreation"),lire(relu,"dateCreation"),"Valeur persistée de dateCreation");
assertEquals(lire(objets.get("DemandeInscription"),"id"),lire(lire(relu,"demande"),"id"));
assertEquals(lire(objets.get("Administrateur"),"id"),lire(lire(relu,"administrateur"),"id"));
   Object resultat=rechercher(repository,"findByDemandeIdOrderByDateCreationAsc",Long.class,lire(objets.get("DemandeInscription"),"id"));
   assertEquals(List.of(id), ((List<?>) resultat).stream().map(o -> assertDoesNotThrow(() -> lire(o,"id"))).toList());
   base.entityManager.getTransaction().commit();
  }
 }
}
