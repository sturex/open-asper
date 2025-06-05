-- Database generated with pgModeler (PostgreSQL Database Modeler).
-- pgModeler  version: 0.9.2_snapshot20190921
-- PostgreSQL version: 11.0
-- Project Site: pgmodeler.io
-- Model Author: ---

-- object: poker_db_owner | type: ROLE --
-- DROP ROLE IF EXISTS poker_db_owner;
CREATE ROLE poker_db_owner WITH
	SUPERUSER
	CREATEDB;
-- ddl-end --


-- Database creation must be done outside a multicommand file.
-- These commands were put in this file only as a convenience.
-- -- object: new_database | type: DATABASE --
-- -- DROP DATABASE IF EXISTS new_database;
-- CREATE DATABASE new_database;
-- -- ddl-end --
-- 

-- object: mlpool | type: SCHEMA --
-- DROP SCHEMA IF EXISTS mlpool CASCADE;
CREATE SCHEMA mlpool;
-- ddl-end --
ALTER SCHEMA mlpool OWNER TO poker_db_owner;
-- ddl-end --

SET search_path TO pg_catalog,public,mlpool;
-- ddl-end --

-- object: mlpool.preflop_chart_body | type: TABLE --
-- DROP TABLE IF EXISTS mlpool.preflop_chart_body CASCADE;
CREATE TABLE mlpool.preflop_chart_body (
	id uuid NOT NULL,
	name text NOT NULL,
	description text NOT NULL,
	creation_timestamp timestamp NOT NULL,
	update_timestamp timestamp NOT NULL,
	spot text NOT NULL,
	chart_type text NOT NULL,
	body bytea,
	CONSTRAINT preflop_chart_body_pk PRIMARY KEY (id),
	CONSTRAINT preflop_chart_body_tbl_name_uq UNIQUE (name)

);
-- ddl-end --
ALTER TABLE mlpool.preflop_chart_body OWNER TO poker_db_owner;
-- ddl-end --

-- object: mlpool.postflop_chart_body | type: TABLE --
-- DROP TABLE IF EXISTS mlpool.postflop_chart_body CASCADE;
CREATE TABLE mlpool.postflop_chart_body (
	id uuid NOT NULL,
	name text NOT NULL,
	description text NOT NULL,
	creation_timestamp timestamp NOT NULL,
	update_timestamp timestamp NOT NULL,
	spot text NOT NULL,
	chart_type text NOT NULL,
	body bytea,
	CONSTRAINT postflop_chart_body_pk PRIMARY KEY (id),
	CONSTRAINT postflop_chart_body_tbl_name_uq UNIQUE (name)

);
-- ddl-end --
ALTER TABLE mlpool.postflop_chart_body OWNER TO poker_db_owner;
-- ddl-end --

-- object: mlpool.solution | type: TABLE --
-- DROP TABLE IF EXISTS mlpool.solution CASCADE;
CREATE TABLE mlpool.solution (
	id uuid NOT NULL,
	name text NOT NULL,
	description text NOT NULL,
	creation_timestamp timestamp NOT NULL,
	update_timestamp timestamp NOT NULL,
	body bytea,
	CONSTRAINT solution_pk PRIMARY KEY (id),
	CONSTRAINT solution_tbl_name_uq UNIQUE (name)

);
-- ddl-end --
ALTER TABLE mlpool.solution OWNER TO poker_db_owner;
-- ddl-end --

-- object: mlpool.model | type: TABLE --
-- DROP TABLE IF EXISTS mlpool.model CASCADE;
CREATE TABLE mlpool.model (
	id uuid NOT NULL,
	name text NOT NULL,
	dataset_query text NOT NULL,
	fallback_query text NOT NULL,
	model_type text NOT NULL,
	description text NOT NULL,
	model_status text NOT NULL,
	max_depth integer NOT NULL,
	dataset_row_count integer NOT NULL,
	min_instances_per_node integer NOT NULL,
	min_info_gain float NOT NULL,
	min_weight_fraction_per_node float NOT NULL,
	num_nodes integer NOT NULL,
	model_size integer NOT NULL,
	depth integer NOT NULL,
	body bytea,
	status_info text NOT NULL,
	feature_info jsonb NOT NULL,
	eval_value_seen_data double precision NOT NULL,
	eval_value_unknown_data double precision NOT NULL,
	feature_schema text NOT NULL,
	creation_timestamp timestamp NOT NULL,
	update_timestamp timestamp NOT NULL,
	spot text NOT NULL,
	poker_situation text NOT NULL,
	CONSTRAINT model_pkl PRIMARY KEY (id),
	CONSTRAINT model_tbl_name_uq UNIQUE (name)

);
-- ddl-end --
ALTER TABLE mlpool.model OWNER TO poker_db_owner;
-- ddl-end --

-- object: preflop_chart_body_tbl_spot_idx | type: INDEX --
-- DROP INDEX IF EXISTS mlpool.preflop_chart_body_tbl_spot_idx CASCADE;
CREATE INDEX preflop_chart_body_tbl_spot_idx ON mlpool.preflop_chart_body
	USING btree
	(
	  spot
	);
-- ddl-end --

-- object: postflop_chart_body_tbl_spot_idx | type: INDEX --
-- DROP INDEX IF EXISTS mlpool.postflop_chart_body_tbl_spot_idx CASCADE;
CREATE INDEX postflop_chart_body_tbl_spot_idx ON mlpool.postflop_chart_body
	USING btree
	(
	  spot
	);
-- ddl-end --

-- object: idx_model_tbl_spot | type: INDEX --
-- DROP INDEX IF EXISTS mlpool.idx_model_tbl_spot CASCADE;
CREATE INDEX idx_model_tbl_spot ON mlpool.model
	USING btree
	(
	  spot
	);
-- ddl-end --

-- object: idx_model_tbl_poker_situation | type: INDEX --
-- DROP INDEX IF EXISTS mlpool.idx_model_tbl_poker_situation CASCADE;
CREATE INDEX idx_model_tbl_poker_situation ON mlpool.model
	USING btree
	(
	  poker_situation
	);
-- ddl-end --

-- object: idx_model_tbl_feature_schema | type: INDEX --
-- DROP INDEX IF EXISTS mlpool.idx_model_tbl_feature_schema CASCADE;
CREATE INDEX idx_model_tbl_feature_schema ON mlpool.model
	USING btree
	(
	  feature_schema
	);
-- ddl-end --

-- object: mlpool.special_spot_tweak | type: TABLE --
-- DROP TABLE IF EXISTS mlpool.special_spot_tweak CASCADE;
CREATE TABLE mlpool.special_spot_tweak (
	id uuid NOT NULL,
	name text NOT NULL,
	description text NOT NULL,
	creation_timestamp timestamp NOT NULL,
	update_timestamp timestamp NOT NULL,
	body bytea,
	CONSTRAINT pk_special_spot_tweak_tbl PRIMARY KEY (id),
	CONSTRAINT uq_special_spot_tweak_tbl_name UNIQUE (name)

);
-- ddl-end --
ALTER TABLE mlpool.special_spot_tweak OWNER TO poker_db_owner;
-- ddl-end --

-- object: mlpool.statistics_body | type: TABLE --
-- DROP TABLE IF EXISTS mlpool.statistics_body CASCADE;
CREATE TABLE mlpool.statistics_body (
	id uuid NOT NULL,
	name text NOT NULL,
	description text NOT NULL,
	creation_timestamp timestamp NOT NULL,
	update_timestamp timestamp NOT NULL,
	body bytea,
	CONSTRAINT pk_statistics_body_tbl PRIMARY KEY (id),
	CONSTRAINT uq_statistics_body_tbl_name UNIQUE (name)

);
-- ddl-end --
ALTER TABLE mlpool.statistics_body OWNER TO poker_db_owner;
-- ddl-end --


