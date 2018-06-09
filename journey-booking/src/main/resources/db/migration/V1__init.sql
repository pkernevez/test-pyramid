CREATE SCHEMA IF NOT EXISTS transport;

CREATE TABLE IF NOT EXISTS transport.journey
(
    id BIGSERIAL NOT NULL ,
    station_from VARCHAR(150) NOT NULL,
    station_to VARCHAR(150) NOT NULL,
    departure TIMESTAMP WITH TIME ZONE,
    arrival TIMESTAMP WITH TIME ZONE,
    PRIMARY KEY (id)
);