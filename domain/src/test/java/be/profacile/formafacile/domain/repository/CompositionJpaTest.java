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
class CompositionJpaTest {
 @Test void retirerUnModuleOrphelin() throws Exception { verifier("Formation",Formation.class,"modules","Module",Module.class); }
 @Test void retirerUneNoteOrpheline() throws Exception { verifier("DemandeInscription",DemandeInscription.class,"notes","NoteInterne",NoteInterne.class); }
 private void verifier(String nom,Class<?> owner,String collection,String enfant,Class<?> child) throws Exception {
  FormaContract.verifierFormation();FormaContract.verifierDemandeInscription();
  try(var base=new BaseJpa(FormaFixtures.ENTITES)) {
   var objets=FormaFixtures.creer();base.entityManager.getTransaction().begin();
   for(Object objet:objets.values())base.entityManager.persist(objet);
   base.entityManager.flush();
   Long parentId=(Long)lire(objets.get(nom),"id"),childId=(Long)lire(objets.get(enfant),"id");
   Long adminId=(Long)lire(objets.get("Administrateur"),"id");
   base.entityManager.clear();
   Object parent=base.entityManager.find(owner,parentId);
   List<?> enfants=(List<?>)lire(parent,collection);assertEquals(1,enfants.size());
   enfants.clear();base.entityManager.flush();base.entityManager.clear();
   assertNull(base.entityManager.find(child,childId));assertNotNull(base.entityManager.find(owner,parentId));
   assertNotNull(base.entityManager.find(Administrateur.class,adminId));
   base.entityManager.getTransaction().commit();
  }
 }
}
