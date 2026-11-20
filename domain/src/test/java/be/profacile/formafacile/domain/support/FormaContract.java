package be.profacile.formafacile.domain.support;
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

public final class FormaContract {
 private FormaContract() {}
 private static void composition(Class<?> owner,String nom,String inverse,Class<?> element) {
  var field=champ(owner,nom,List.class);
  var mapping=field.getAnnotation(OneToMany.class);assertNotNull(mapping);
  assertEquals(inverse,mapping.mappedBy());assertTrue(mapping.orphanRemoval());
  assertTrue(Arrays.asList(mapping.cascade()).contains(CascadeType.ALL));
  assertEquals(element,((java.lang.reflect.ParameterizedType)field.getGenericType()).getActualTypeArguments()[0]);
 }
 public static void enumerations() {
 assertArrayEquals(new String[]{"DEBUTANT","INTERMEDIAIRE","AVANCE"},Arrays.stream(Niveau.values()).map(Enum::name).toArray(String[]::new));
assertArrayEquals(new String[]{"EN_LIGNE","PRESENTIEL","HYBRIDE","INTRA_ENTREPRISE","INDIVIDUEL"},Arrays.stream(Format.values()).map(Enum::name).toArray(String[]::new));
assertArrayEquals(new String[]{"BROUILLON","PUBLIEE","DEPUBLIEE"},Arrays.stream(StatutPublication.values()).map(Enum::name).toArray(String[]::new));
assertArrayEquals(new String[]{"NOUVELLE","CONTACTEE","EN_ATTENTE_CONFIRMATION","CONFIRMEE","CLOTUREE","ANNULEE"},Arrays.stream(StatutDemande.values()).map(Enum::name).toArray(String[]::new));
}
public static void verifierCategorie() {
entite(Categorie.class, "categorie");
colonne(Categorie.class,"libelle",String.class,"libelle",false);
assertTrue(champ(Categorie.class,"libelle",String.class).getAnnotation(Column.class).unique());
assertEquals(80, champ(Categorie.class,"libelle",String.class).getAnnotation(Column.class).length());
repository(CategorieRepository.class,Categorie.class);
assertDoesNotThrow(() -> CategorieRepository.class.getMethod("findByLibelleIgnoreCase", String.class));
}
public static void verifierFormateur() {
entite(Formateur.class, "formateur");
colonne(Formateur.class,"nom",String.class,"nom",false);
assertEquals(80, champ(Formateur.class,"nom",String.class).getAnnotation(Column.class).length());
colonne(Formateur.class,"prenom",String.class,"prenom",false);
assertEquals(80, champ(Formateur.class,"prenom",String.class).getAnnotation(Column.class).length());
colonne(Formateur.class,"email",String.class,"email",false);
assertEquals(150, champ(Formateur.class,"email",String.class).getAnnotation(Column.class).length());
colonne(Formateur.class,"bio",String.class,"bio",true);
repository(FormateurRepository.class,Formateur.class);
assertDoesNotThrow(() -> FormateurRepository.class.getMethod("findByEmail", String.class));
}
public static void verifierAdministrateur() {
entite(Administrateur.class, "administrateur");
colonne(Administrateur.class,"email",String.class,"email",false);
assertTrue(champ(Administrateur.class,"email",String.class).getAnnotation(Column.class).unique());
assertEquals(150, champ(Administrateur.class,"email",String.class).getAnnotation(Column.class).length());
colonne(Administrateur.class,"motDePasse",String.class,"mot_de_passe_hash",false);
assertEquals(255, champ(Administrateur.class,"motDePasse",String.class).getAnnotation(Column.class).length());
colonne(Administrateur.class,"nom",String.class,"nom",false);
assertEquals(80, champ(Administrateur.class,"nom",String.class).getAnnotation(Column.class).length());
repository(AdministrateurRepository.class,Administrateur.class);
assertDoesNotThrow(() -> AdministrateurRepository.class.getMethod("findByEmail", String.class));
}
public static void verifierFormation() {
entite(Formation.class, "formation");
colonne(Formation.class,"titre",String.class,"titre",false);
assertEquals(150, champ(Formation.class,"titre",String.class).getAnnotation(Column.class).length());
colonne(Formation.class,"slug",String.class,"slug",false);
assertTrue(champ(Formation.class,"slug",String.class).getAnnotation(Column.class).unique());
assertEquals(120, champ(Formation.class,"slug",String.class).getAnnotation(Column.class).length());
colonne(Formation.class,"descriptionCourte",String.class,"description_courte",false);
assertEquals(300, champ(Formation.class,"descriptionCourte",String.class).getAnnotation(Column.class).length());
colonne(Formation.class,"descriptionDetaillee",String.class,"description_detaillee",false);
colonne(Formation.class,"niveau",Niveau.class,"niveau",false);
assertEquals(20, champ(Formation.class,"niveau",Niveau.class).getAnnotation(Column.class).length());
assertEquals(EnumType.STRING, Objects.requireNonNull(champ(Formation.class,"niveau",Niveau.class).getAnnotation(Enumerated.class), "Ajouter @Enumerated").value());
colonne(Formation.class,"dureeHeures",int.class,"duree_heures",false);
colonne(Formation.class,"format",Format.class,"format",false);
assertEquals(20, champ(Formation.class,"format",Format.class).getAnnotation(Column.class).length());
assertEquals(EnumType.STRING, Objects.requireNonNull(champ(Formation.class,"format",Format.class).getAnnotation(Enumerated.class), "Ajouter @Enumerated").value());
colonne(Formation.class,"prixIndicatif",BigDecimal.class,"prix_indicatif",false);
colonne(Formation.class,"prerequis",String.class,"prerequis",true);
colonne(Formation.class,"objectifs",String.class,"objectifs",false);
colonne(Formation.class,"publicCible",String.class,"public_cible",false);
assertEquals(255, champ(Formation.class,"publicCible",String.class).getAnnotation(Column.class).length());
colonne(Formation.class,"statut",StatutPublication.class,"statut",false);
assertEquals(20, champ(Formation.class,"statut",StatutPublication.class).getAnnotation(Column.class).length());
assertEquals(EnumType.STRING, Objects.requireNonNull(champ(Formation.class,"statut",StatutPublication.class).getAnnotation(Enumerated.class), "Ajouter @Enumerated").value());
relation(Formation.class,"categorie",Categorie.class,"categorie_id");
relation(Formation.class,"formateur",Formateur.class,"formateur_id");
composition(Formation.class,"modules","formation",Module.class);
repository(FormationRepository.class,Formation.class);
assertDoesNotThrow(() -> FormationRepository.class.getMethod("findByStatut", StatutPublication.class));
}
public static void verifierModule() {
entite(Module.class, "module");
colonne(Module.class,"ordre",int.class,"ordre",false);
colonne(Module.class,"titre",String.class,"titre",false);
assertEquals(150, champ(Module.class,"titre",String.class).getAnnotation(Column.class).length());
colonne(Module.class,"description",String.class,"description",false);
colonne(Module.class,"dureeHeures",int.class,"duree_heures",false);
relation(Module.class,"formation",Formation.class,"formation_id");
repository(ModuleRepository.class,Module.class);
assertDoesNotThrow(() -> ModuleRepository.class.getMethod("findByFormationIdOrderByOrdreAsc", Long.class));
}
public static void verifierDemandeInscription() {
entite(DemandeInscription.class, "demande_inscription");
colonne(DemandeInscription.class,"nom",String.class,"nom",false);
assertEquals(80, champ(DemandeInscription.class,"nom",String.class).getAnnotation(Column.class).length());
colonne(DemandeInscription.class,"prenom",String.class,"prenom",false);
assertEquals(80, champ(DemandeInscription.class,"prenom",String.class).getAnnotation(Column.class).length());
colonne(DemandeInscription.class,"email",String.class,"email",false);
assertEquals(150, champ(DemandeInscription.class,"email",String.class).getAnnotation(Column.class).length());
colonne(DemandeInscription.class,"telephone",String.class,"telephone",true);
assertEquals(20, champ(DemandeInscription.class,"telephone",String.class).getAnnotation(Column.class).length());
colonne(DemandeInscription.class,"entreprise",String.class,"entreprise",true);
assertEquals(150, champ(DemandeInscription.class,"entreprise",String.class).getAnnotation(Column.class).length());
colonne(DemandeInscription.class,"nbParticipants",int.class,"nb_participants",false);
colonne(DemandeInscription.class,"formatSouhaite",Format.class,"format_souhaite",false);
assertEquals(20, champ(DemandeInscription.class,"formatSouhaite",Format.class).getAnnotation(Column.class).length());
assertEquals(EnumType.STRING, Objects.requireNonNull(champ(DemandeInscription.class,"formatSouhaite",Format.class).getAnnotation(Enumerated.class), "Ajouter @Enumerated").value());
colonne(DemandeInscription.class,"periodeSouhaitee",String.class,"periode_souhaitee",true);
assertEquals(100, champ(DemandeInscription.class,"periodeSouhaitee",String.class).getAnnotation(Column.class).length());
colonne(DemandeInscription.class,"message",String.class,"message",true);
colonne(DemandeInscription.class,"consentementRgpd",boolean.class,"consentement_rgpd",false);
colonne(DemandeInscription.class,"dateDemande",LocalDateTime.class,"date_demande",false);
colonne(DemandeInscription.class,"statut",StatutDemande.class,"statut",false);
assertEquals(30, champ(DemandeInscription.class,"statut",StatutDemande.class).getAnnotation(Column.class).length());
assertEquals(EnumType.STRING, Objects.requireNonNull(champ(DemandeInscription.class,"statut",StatutDemande.class).getAnnotation(Enumerated.class), "Ajouter @Enumerated").value());
relation(DemandeInscription.class,"formation",Formation.class,"formation_id");
composition(DemandeInscription.class,"notes","demande",NoteInterne.class);
repository(DemandeInscriptionRepository.class,DemandeInscription.class);
assertDoesNotThrow(() -> DemandeInscriptionRepository.class.getMethod("findByStatut", StatutDemande.class));
}
public static void verifierNoteInterne() {
entite(NoteInterne.class, "note_interne");
colonne(NoteInterne.class,"contenu",String.class,"contenu",false);
colonne(NoteInterne.class,"dateCreation",LocalDateTime.class,"date_creation",false);
relation(NoteInterne.class,"demande",DemandeInscription.class,"demande_id");
relation(NoteInterne.class,"administrateur",Administrateur.class,"administrateur_id");
repository(NoteInterneRepository.class,NoteInterne.class);
assertDoesNotThrow(() -> NoteInterneRepository.class.getMethod("findByDemandeIdOrderByDateCreationAsc", Long.class));
}
public static void verifierMessageContact() {
entite(MessageContact.class, "message_contact");
colonne(MessageContact.class,"nom",String.class,"nom",false);
assertEquals(80, champ(MessageContact.class,"nom",String.class).getAnnotation(Column.class).length());
colonne(MessageContact.class,"email",String.class,"email",false);
assertEquals(150, champ(MessageContact.class,"email",String.class).getAnnotation(Column.class).length());
colonne(MessageContact.class,"sujet",String.class,"sujet",false);
assertEquals(150, champ(MessageContact.class,"sujet",String.class).getAnnotation(Column.class).length());
colonne(MessageContact.class,"message",String.class,"message",false);
colonne(MessageContact.class,"dateEnvoi",LocalDateTime.class,"date_envoi",false);
colonne(MessageContact.class,"traite",boolean.class,"traite",false);
var relation = champ(MessageContact.class,"administrateur",Administrateur.class);
assertNotNull(relation.getAnnotation(ManyToOne.class));
assertTrue(relation.getAnnotation(ManyToOne.class).optional());
assertNotNull(relation.getAnnotation(JoinColumn.class));
assertEquals("administrateur_id",relation.getAnnotation(JoinColumn.class).name());
assertTrue(relation.getAnnotation(JoinColumn.class).nullable());
repository(MessageContactRepository.class,MessageContact.class);
assertDoesNotThrow(() -> MessageContactRepository.class.getMethod("findByTraiteFalse"));
}
}
