create table LANGUAGE (
  id integer(11) auto_increment not null,
  title varchar(255),
  shortName VARCHAR (2),
  nativeTitle VARCHAR (255),
  prim bit (1),
  UNIQUE (title),
  UNIQUE (shortName),
  UNIQUE (nativeTitle),
  primary key(id)
);

create table MODULE (
  id integer(11) auto_increment not null,
  title varchar(255),
  UNIQUE (title),
  primary key(id)
);
create table WORD(
  id integer(11) auto_increment not null,
  title varchar(255),
  value varchar(255),
  module integer(11) not null,
  language integer(11) not null,
  FOREIGN KEY (module) REFERENCES module(id),
  FOREIGN KEY (language) REFERENCES language(id),
  primary key(id)
);

ALTER TABLE `WORD` DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci;
ALTER TABLE `MODULE` DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci;
ALTER TABLE `LANGUAGE` DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci;

ALTER TABLE `WORD` ADD CONSTRAINT `module_fk` FOREIGN KEY (`module`) REFERENCES `MODULE` (`id`);
ALTER TABLE `WORD` ADD CONSTRAINT `language_fk` FOREIGN KEY (`language`) REFERENCES `LANGUAGE` (`id`);

