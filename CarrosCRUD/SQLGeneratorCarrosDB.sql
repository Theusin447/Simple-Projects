CREATE SCHEMA IF NOT EXISTS `carro` DEFAULT CHARACTER SET utf8mb4 ;
USE `carro` ;

CREATE TABLE IF NOT EXISTS `carro`.`carros` (
  `cod_carro` INT NOT NULL AUTO_INCREMENT,
  `descricao_carro` VARCHAR(50) NOT NULL,
  `potencia_carro` VARCHAR(50) NOT NULL,
  `cor_carro` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`cod_carro`))
ENGINE = InnoDB
AUTO_INCREMENT = 18
DEFAULT CHARACTER SET = utf8mb4;