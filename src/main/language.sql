create table language (
  id integer(11) auto_increment not null,
  title varchar(255),
  short_name VARCHAR (2),
  native_title VARCHAR (255),
  prim bit (1),
  UNIQUE (title),
  UNIQUE (short_name),
  UNIQUE (native_title),
  primary key(id)
);

create table module (
  id integer(11) auto_increment not null,
  title varchar(255),
  UNIQUE (title),
  primary key(id)
);

create table word(
  id integer(11) auto_increment not null,
  title varchar(255),
  value varchar(255),
  module integer(11) not null,
  language integer(11) not null,
  FOREIGN KEY (module) REFERENCES module(id),
  FOREIGN KEY (language) REFERENCES language(id),
  primary key(id)
);

ALTER TABLE `word` DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci;
ALTER TABLE `module` DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci;
ALTER TABLE `language` DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci;

ALTER TABLE `word` ADD CONSTRAINT `module_fk` FOREIGN KEY (`module`) REFERENCES `module` (`id`);
ALTER TABLE `word` ADD CONSTRAINT `language_fk` FOREIGN KEY (`language`) REFERENCES `language` (`id`);
ALTER TABLE `word` ADD UNIQUE INDEX (`language`, `module`, `title`) USING BTREE ;

ALTER TABLE `module` ADD UNIQUE INDEX (`title`) USING BTREE ;

