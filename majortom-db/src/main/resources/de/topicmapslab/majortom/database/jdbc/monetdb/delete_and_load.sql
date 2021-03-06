drop TABLE associations CASCADE;
drop TABLE changesets CASCADE;
drop TABLE history CASCADE;
drop TABLE locators CASCADE;
drop TABLE metadata CASCADE;
drop TABLE names CASCADE;
drop TABLE occurrences CASCADE;
drop TABLE rel_instance_of CASCADE;
drop TABLE rel_item_identifiers CASCADE;
drop TABLE rel_kind_of CASCADE;
drop TABLE rel_subject_identifiers CASCADE;
drop TABLE rel_subject_locators CASCADE;
drop TABLE rel_themes CASCADE;
drop TABLE revisions CASCADE;
drop TABLE roles CASCADE;
drop TABLE scopes CASCADE;
drop TABLE tags CASCADE;
drop TABLE topicmaps CASCADE;
drop TABLE topics CASCADE;
drop TABLE variants CASCADE;

CREATE TABLE locators (
    id BIGINT NOT NULL AUTO_INCREMENT,
    reference character varying(1024) NOT NULL,
  CONSTRAINT pk_locators PRIMARY KEY (id)
);

CREATE TABLE topicmaps(
	id bigint NOT NULL AUTO_INCREMENT,
    id_parent bigint,
    id_topicmap bigint,
    id_reifier bigint,
    id_base_locator bigint NOT NULL,
  CONSTRAINT pk_topicmap PRIMARY KEY (id),
  CONSTRAINT fk_baselocator FOREIGN KEY (id_base_locator)
      REFERENCES locators (id)
      ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE topics (
	id bigint NOT NULL AUTO_INCREMENT,
    id_parent bigint,
    id_topicmap bigint,
  CONSTRAINT pk_topics PRIMARY KEY (id),
  CONSTRAINT fk_topic_parent FOREIGN KEY (id_parent)
      REFERENCES topicmaps (id)
      ON UPDATE CASCADE ON DELETE CASCADE
);

ALTER TABLE topicmaps ADD
  CONSTRAINT fk_topicmaps_reifier FOREIGN KEY (id_reifier)
      REFERENCES topics (id) ON UPDATE CASCADE ON DELETE CASCADE;



CREATE TABLE scopes (
    id bigint NOT NULL AUTO_INCREMENT,
	id_topicmap bigint,
  CONSTRAINT pk_scope PRIMARY KEY (id),
  CONSTRAINT fk_scopes_topicmap FOREIGN KEY (id_topicmap)
      REFERENCES topicmaps (id)  
      ON UPDATE CASCADE ON DELETE CASCADE
      
) ;

CREATE TABLE associations (
	id bigint NOT NULL AUTO_INCREMENT,
    id_parent bigint,
    id_topicmap bigint,
    id_reifier bigint,
    id_scope bigint NOT NULL,
    id_type bigint NOT NULL,
  CONSTRAINT pk_associations PRIMARY KEY (id)
) ;

ALTER TABLE associations ADD 
  CONSTRAINT fk_associations_parent FOREIGN KEY (id_parent)
      REFERENCES topicmaps (id) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE associations ADD 
  CONSTRAINT fk_associations_type FOREIGN KEY (id_type)
      REFERENCES topics (id) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE associations ADD 
  CONSTRAINT fk_associations_scope FOREIGN KEY (id_scope)
      REFERENCES scopes (id) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE associations ADD 
  CONSTRAINT fk_associations_reifier FOREIGN KEY (id_reifier)
      REFERENCES topics (id) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE associations ADD 
  CONSTRAINT fk_topicmap FOREIGN KEY (id_topicmap)
      REFERENCES topicmaps (id) ON UPDATE CASCADE ON DELETE CASCADE;



CREATE TABLE revisions
(
  id bigint NOT NULL AUTO_INCREMENT,
  time timestamp NOT NULL,
  id_topicmap bigint NOT NULL,
  type character varying(128) NOT NULL,
  CONSTRAINT pk_revisions PRIMARY KEY (id),
  CONSTRAINT fk_revisions_topicmap FOREIGN KEY (id_topicmap)
      REFERENCES topicmaps (id)
      ON UPDATE CASCADE ON DELETE CASCADE
) ;

CREATE TABLE changesets
(
  id bigint NOT NULL AUTO_INCREMENT,
  id_revision bigint,
  id_notifier bigint NOT NULL,
  type character varying(128) NOT NULL,
  newvalue character varying(1024),
  oldvalue character varying(1024),
  time timestamp,
  CONSTRAINT pk_changeset PRIMARY KEY (id),
  CONSTRAINT fk_changesets_revision FOREIGN KEY (id_revision)
      REFERENCES revisions (id)  
      ON UPDATE CASCADE ON DELETE CASCADE
) ;

CREATE TABLE history
(
  id_history bigint NOT NULL AUTO_INCREMENT,
  id bigint NOT NULL,
  id_topicmap bigint NOT NULL,
  id_revision bigint NOT NULL,
  id_parent bigint NOT NULL,
  names varchar(1024),
  occurrences varchar(1024),
  variants varchar(1024),
  associations varchar(1024),
  id_scope bigint,
  id_reification bigint,
  id_player bigint,
  types varchar(1024),
  supertypes varchar(1024),
  value varchar(1024),
  type varchar(1),
  themes varchar(1024),
  itemidentifiers varchar(1024) NOT NULL,
  subjectidentifiers varchar(1024),
  subjectlocators varchar(1024),
  datatype varchar(1024),
  roles varchar(1024),
  bestlabel varchar(256),
  CONSTRAINT pk_history PRIMARY KEY (id_history),
  CONSTRAINT fk_history_revision FOREIGN KEY (id_revision)
      REFERENCES revisions (id) ON UPDATE CASCADE ON DELETE CASCADE ,
  CONSTRAINT fk_history_topicmap FOREIGN KEY (id_topicmap)
      REFERENCES topicmaps (id) ON UPDATE CASCADE ON DELETE CASCADE 
) ;

CREATE TABLE metadata
(
  id_revision bigint NOT NULL,
  key varchar(1024) NOT NULL,
  value varchar(1024),
  CONSTRAINT fk_md_revision FOREIGN KEY (id_revision)
      REFERENCES revisions (id)
      ON UPDATE CASCADE ON DELETE CASCADE  
) ;

CREATE TABLE names (
	id bigint NOT NULL AUTO_INCREMENT,
    id_parent bigint,
    id_topicmap bigint,
    literal varchar(1024),
    id_reifier bigint,
    id_scope bigint NOT NULL,
    id_type bigint NOT NULL,
    value varchar(1024) NOT NULL,
 CONSTRAINT pk_names PRIMARY KEY (id),
  CONSTRAINT fk_names_type FOREIGN KEY (id_type)
      REFERENCES topics (id) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_names_scope FOREIGN KEY (id_scope)
      REFERENCES scopes (id) ON UPDATE CASCADE ON DELETE CASCADE ,
  CONSTRAINT fk_names_reifier FOREIGN KEY (id_reifier)
      REFERENCES topics (id)  
      ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT fk_names_topicmap FOREIGN KEY (id_topicmap)
      REFERENCES topicmaps (id)
      ON UPDATE CASCADE ON DELETE CASCADE 
) ;

CREATE TABLE occurrences (
	id bigint NOT NULL AUTO_INCREMENT,
    id_parent bigint,
    id_topicmap bigint,
    id_reifier bigint,
    id_scope bigint NOT NULL,
    id_type bigint NOT NULL,
    value varchar(1024) NOT NULL,
    id_datatype bigint NOT NULL,
 CONSTRAINT pk_occurrences PRIMARY KEY (id),
  CONSTRAINT fk_occurrences_type FOREIGN KEY (id_type)
      REFERENCES topics (id) ON UPDATE CASCADE ON DELETE CASCADE ,
  CONSTRAINT fk_occurrences_scope FOREIGN KEY (id_scope)
      REFERENCES scopes (id) ON UPDATE CASCADE ON DELETE CASCADE ,
  CONSTRAINT fk_occurrences_reifier FOREIGN KEY (id_reifier)
      REFERENCES topics (id)  
      ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT fk_occurrences_topicmap FOREIGN KEY (id_topicmap)
      REFERENCES topicmaps (id)
      ON UPDATE CASCADE ON DELETE CASCADE  
) ;

CREATE TABLE rel_instance_of (
    id_instance bigint NOT NULL,
    id_type bigint NOT NULL,
  CONSTRAINT fk_instance FOREIGN KEY (id_instance)
      REFERENCES topics (id) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_topic_type FOREIGN KEY (id_type)
      REFERENCES topics (id)
      ON UPDATE CASCADE ON DELETE CASCADE  
) ;

CREATE TABLE rel_item_identifiers (
    id_construct bigint NOT NULL,
    id_locator bigint NOT NULL,
  CONSTRAINT fk_ii_locator FOREIGN KEY (id_locator)
      REFERENCES locators (id) ON UPDATE CASCADE ON DELETE CASCADE 
) ;

CREATE TABLE rel_kind_of (
    id_subtype bigint,
    id_supertype bigint,
  CONSTRAINT fk_subtype FOREIGN KEY (id_subtype)
      REFERENCES topics (id) ON UPDATE CASCADE ON DELETE CASCADE ,
  CONSTRAINT fk_supertype FOREIGN KEY (id_supertype)
      REFERENCES topics (id) ON UPDATE CASCADE ON DELETE CASCADE 
) ;

CREATE TABLE rel_subject_identifiers (
    id_topic bigint NOT NULL,
    id_locator bigint NOT NULL,
  CONSTRAINT fk_si_locator FOREIGN KEY (id_locator)
      REFERENCES locators (id) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_si_topic FOREIGN KEY (id_topic)
      REFERENCES topics (id) ON UPDATE CASCADE ON DELETE CASCADE
) ;

CREATE TABLE rel_subject_locators (
    id_topic bigint,
    id_locator bigint NOT NULL,
  CONSTRAINT fk_sl_locator FOREIGN KEY (id_locator)
      REFERENCES locators (id) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_sl_topic FOREIGN KEY (id_topic)
      REFERENCES topics (id) ON UPDATE CASCADE ON DELETE CASCADE
) ;

CREATE TABLE rel_themes (
    id_scope bigint,
    id_theme bigint NOT NULL,
  CONSTRAINT fk_themes_scope FOREIGN KEY (id_scope)
      REFERENCES scopes (id) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_theme FOREIGN KEY (id_theme)
      REFERENCES topics (id) ON UPDATE CASCADE ON DELETE CASCADE    
) ;

CREATE TABLE roles (
	id bigint NOT NULL AUTO_INCREMENT,
    id_parent bigint,
    id_topicmap bigint,
    id_type bigint NOT NULL,
    id_reifier bigint,
    id_player bigint NOT NULL,
 CONSTRAINT pk_roles PRIMARY KEY (id),
 CONSTRAINT fk_roles_player FOREIGN KEY (id_player)
      REFERENCES topics (id) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_roles_parent FOREIGN KEY (id_parent)
      REFERENCES associations (id) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_roles_type FOREIGN KEY (id_type)
      REFERENCES topics (id) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_roles_reifier FOREIGN KEY (id_reifier)
      REFERENCES topics (id)  
      ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT fk_roles_topicmap FOREIGN KEY (id_topicmap)
      REFERENCES topicmaps (id) ON UPDATE CASCADE ON DELETE CASCADE
) ;

CREATE TABLE tags
(
  tag varchar(1024) NOT NULL,
  time timestamp NOT NULL) ;

CREATE TABLE variants (
	id bigint NOT NULL AUTO_INCREMENT,
    id_parent bigint,
    id_topicmap bigint,
    id_reifier bigint,
    id_scope bigint NOT NULL,
    value varchar(1024) NOT NULL,	
    id_datatype bigint NOT NULL,
  CONSTRAINT pk_variants PRIMARY KEY (id),
  CONSTRAINT fk_variants_parent FOREIGN KEY (id_parent)
      REFERENCES names (id) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_variants_scope FOREIGN KEY (id_scope)
      REFERENCES scopes (id) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_variants_reifier FOREIGN KEY (id_reifier)
      REFERENCES topics (id)  
      ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT fk_variants_topicmap FOREIGN KEY (id_topicmap)
      REFERENCES topicmaps (id) ON UPDATE CASCADE ON DELETE CASCADE
) ;

