# FormaFacile — Sprint 2 : modèle JPA et repositories

Service de formations présenté sur profacile.be, avec backend séparé.
Référence : diagrammes UML et ERD fournis le 20 novembre 2026.

## Répartition
- Lauryn / compte Jira Leslie Tchounka : Categorie, Formateur, Formation, Module, Niveau, Format, StatutPublication et leurs quatre repositories.
- David / compte Jira mbope : Administrateur, DemandeInscription, NoteInterne, MessageContact, StatutDemande et leurs quatre repositories.

## Contrat commun
Classes vides à compléter dans domain/model. Interfaces vides à compléter dans domain/repository.
Chaque entité porte @Entity et @Table avec le nom SQL indiqué ; id Long privé, @Id et @GeneratedValue(IDENTITY).
Constructeur sans argument et getters/setters publics (getTraite/getConsentementRgpd dans ce contrat).
Enums stockés avec @Enumerated(STRING). Colonnes @Column explicites, longueurs et nullabilité ci-dessous.
BigDecimal : précision 10, échelle 2. Champs String sans longueur : TEXT.
Le champ Java motDePasse correspond à mot_de_passe_hash ; conserver uniquement un hash fictif dans les tests.

La nullabilité n'est pas détaillée dans les schémas : bio, téléphone, entreprise, période souhaitée,
message de demande et prérequis sont facultatifs dans ce contrat pédagogique ; les autres champs sont requis.
Ce sont des décisions explicites du squelette.

## Dictionnaire et recherches

### Categorie — table categorie

| Champ | Type | Colonne SQL | Facultatif | Longueur | Unique |
|---|---|---|---|---|---|
| libelle | String | libelle | false | 80 | true |

CategorieRepository extends JpaRepository<Categorie, Long> ; recherche : Optional<Categorie> findByLibelleIgnoreCase(String value).

### Formateur — table formateur

| Champ | Type | Colonne SQL | Facultatif | Longueur | Unique |
|---|---|---|---|---|---|
| nom | String | nom | false | 80 | false |
| prenom | String | prenom | false | 80 | false |
| email | String | email | false | 150 | false |
| bio | String | bio | true | TEXT | false |

FormateurRepository extends JpaRepository<Formateur, Long> ; recherche : Optional<Formateur> findByEmail(String value).

### Administrateur — table administrateur

| Champ | Type | Colonne SQL | Facultatif | Longueur | Unique |
|---|---|---|---|---|---|
| email | String | email | false | 150 | true |
| motDePasse | String | mot_de_passe_hash | false | 255 | false |
| nom | String | nom | false | 80 | false |

AdministrateurRepository extends JpaRepository<Administrateur, Long> ; recherche : Optional<Administrateur> findByEmail(String value).

### Formation — table formation

| Champ | Type | Colonne SQL | Facultatif | Longueur | Unique |
|---|---|---|---|---|---|
| titre | String | titre | false | 150 | false |
| slug | String | slug | false | 120 | true |
| descriptionCourte | String | description_courte | false | 300 | false |
| descriptionDetaillee | String | description_detaillee | false | TEXT | false |
| niveau | Niveau | niveau | false | 20 | false |
| dureeHeures | int | duree_heures | false | — | false |
| format | Format | format | false | 20 | false |
| prixIndicatif | BigDecimal | prix_indicatif | false | — | false |
| prerequis | String | prerequis | true | TEXT | false |
| objectifs | String | objectifs | false | TEXT | false |
| publicCible | String | public_cible | false | 255 | false |
| statut | StatutPublication | statut | false | 20 | false |

Relation categorie : @ManyToOne(optional=false) vers Categorie, @JoinColumn(name="categorie_id",nullable=false).

Relation formateur : @ManyToOne(optional=false) vers Formateur, @JoinColumn(name="formateur_id",nullable=false).

List<Module> modules initialisée : @OneToMany(mappedBy="formation",cascade=ALL,orphanRemoval=true).

FormationRepository extends JpaRepository<Formation, Long> ; recherche : List<Formation> findByStatut(StatutPublication value).

### Module — table module

| Champ | Type | Colonne SQL | Facultatif | Longueur | Unique |
|---|---|---|---|---|---|
| ordre | int | ordre | false | — | false |
| titre | String | titre | false | 150 | false |
| description | String | description | false | TEXT | false |
| dureeHeures | int | duree_heures | false | — | false |

Relation formation : @ManyToOne(optional=false) vers Formation, @JoinColumn(name="formation_id",nullable=false).

ModuleRepository extends JpaRepository<Module, Long> ; recherche : List<Module> findByFormationIdOrderByOrdreAsc(Long value).

### DemandeInscription — table demande_inscription

| Champ | Type | Colonne SQL | Facultatif | Longueur | Unique |
|---|---|---|---|---|---|
| nom | String | nom | false | 80 | false |
| prenom | String | prenom | false | 80 | false |
| email | String | email | false | 150 | false |
| telephone | String | telephone | true | 20 | false |
| entreprise | String | entreprise | true | 150 | false |
| nbParticipants | int | nb_participants | false | — | false |
| formatSouhaite | Format | format_souhaite | false | 20 | false |
| periodeSouhaitee | String | periode_souhaitee | true | 100 | false |
| message | String | message | true | TEXT | false |
| consentementRgpd | boolean | consentement_rgpd | false | — | false |
| dateDemande | LocalDateTime | date_demande | false | — | false |
| statut | StatutDemande | statut | false | 30 | false |

Relation formation : @ManyToOne(optional=false) vers Formation, @JoinColumn(name="formation_id",nullable=false).

List<NoteInterne> notes initialisée : @OneToMany(mappedBy="demande",cascade=ALL,orphanRemoval=true).

DemandeInscriptionRepository extends JpaRepository<DemandeInscription, Long> ; recherche : List<DemandeInscription> findByStatut(StatutDemande value).

### NoteInterne — table note_interne

| Champ | Type | Colonne SQL | Facultatif | Longueur | Unique |
|---|---|---|---|---|---|
| contenu | String | contenu | false | TEXT | false |
| dateCreation | LocalDateTime | date_creation | false | — | false |

Relation demande : @ManyToOne(optional=false) vers DemandeInscription, @JoinColumn(name="demande_id",nullable=false).

Relation administrateur : @ManyToOne(optional=false) vers Administrateur, @JoinColumn(name="administrateur_id",nullable=false).

NoteInterneRepository extends JpaRepository<NoteInterne, Long> ; recherche : List<NoteInterne> findByDemandeIdOrderByDateCreationAsc(Long value).

### MessageContact — table message_contact

| Champ | Type | Colonne SQL | Facultatif | Longueur | Unique |
|---|---|---|---|---|---|
| nom | String | nom | false | 80 | false |
| email | String | email | false | 150 | false |
| sujet | String | sujet | false | 150 | false |
| message | String | message | false | TEXT | false |
| dateEnvoi | LocalDateTime | date_envoi | false | — | false |
| traite | boolean | traite | false | — | false |

Relation administrateur : @ManyToOne(optional=true) vers Administrateur, @JoinColumn(name="administrateur_id",nullable=true).

MessageContactRepository extends JpaRepository<MessageContact, Long> ; recherche : List<MessageContact> findByTraiteFalse().

## Énumérations

- Niveau : DEBUTANT, INTERMEDIAIRE, AVANCE.
- Format : EN_LIGNE, PRESENTIEL, HYBRIDE, INTRA_ENTREPRISE, INDIVIDUEL.
- StatutPublication : BROUILLON, PUBLIEE, DEPUBLIEE.
- StatutDemande : NOUVELLE, CONTACTEE, EN_ATTENTE_CONFIRMATION, CONFIRMEE, CLOTUREE, ANNULEE.

## Travail et validation
1. Créer sa branche depuis develop ; inclure la clé Jira dans la branche et la PR.
2. Commencer par Categorie/Formateur côté catalogue et Administrateur/MessageContact côté suivi.
3. Fusionner Formation/Module dans develop pour que David intègre Formation et termine les demandes/notes.
4. Les tests de structure de chaque RepositoryTest sont autonomes. Les tests de persistance utilisent tout le graphe :
   ils nécessitent l'intégration des deux lots. Ne pas coder le lot de l'autre étudiant pour contourner cette dépendance.
5. Lancer Play sur les classes de tests, ou ./mvnw -pl domain test (Windows : .\mvnw.cmd -pl domain test).
6. Après intégration : ./mvnw verify. Attendu : 18 tests domaine et un test de démarrage réussis.
7. PR vers develop, résumé, résultats des tests et démonstration. Ne pas modifier ou désactiver les tests fournis.

Les tests compilent sur les classes vides grâce à la réflexion et sont volontairement rouges au départ.
Ils vérifient le contrat JPA, la sauvegarde/relecture réelle Hibernate/H2, les champs et relations persistés,
les recherches Spring Data et la suppression des modules/notes orphelins.
H2 est réservé aux tests ; migrations et validation PostgreSQL feront l'objet d'un sprint ultérieur.

## Suite du projet
Services et DTO request/response restent vides pour le futur sprint API/services.
Règles à implémenter ensuite : au moins un module avant publication, catalogue limité aux formations publiées,
demande NOUVELLE -> CONTACTEE -> EN_ATTENTE_CONFIRMATION -> CONFIRMEE -> CLOTUREE,
annulation avant clôture avec motif en note interne, demande entreprise au-delà de cinq participants.
Notes internes privées, consentement sur formulaire. Ces règles métier ne sont pas couvertes par les tests JPA.
Pas d'authentification opérationnelle, paiement ou email réel à développer dans ce sprint.
