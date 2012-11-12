SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

DROP SCHEMA IF EXISTS `comercio` ;
CREATE SCHEMA IF NOT EXISTS `comercio` DEFAULT CHARACTER SET latin1 COLLATE latin1_spanish_ci ;
USE `comercio` ;

-- -----------------------------------------------------
-- Table `comercio`.`Oferta`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `comercio`.`Oferta` (
  `idOferta` BIGINT NOT NULL AUTO_INCREMENT ,
  `descuento` DOUBLE NOT NULL ,
  `fechaInicio` DATE NOT NULL ,
  `fechaFin` DATE NOT NULL ,
  PRIMARY KEY (`idOferta`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `comercio`.`Marca`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `comercio`.`Marca` (
  `idMarca` BIGINT NOT NULL AUTO_INCREMENT ,
  `nombre` VARCHAR(45) NOT NULL ,
  `abreviacion` VARCHAR(45) NULL ,
  `Oferta_idOferta` BIGINT NULL ,
  PRIMARY KEY (`idMarca`) ,
  CONSTRAINT `fk_Marca_Oferta1`
    FOREIGN KEY (`Oferta_idOferta` )
    REFERENCES `comercio`.`Oferta` (`idOferta` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_Marca_Oferta` ON `comercio`.`Marca` (`Oferta_idOferta` ASC) ;


-- -----------------------------------------------------
-- Table `comercio`.`Origen`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `comercio`.`Origen` (
  `idOrigen` INT NOT NULL AUTO_INCREMENT ,
  `descripcion` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`idOrigen`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `comercio`.`Unidad`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `comercio`.`Unidad` (
  `idUnidad` INT NOT NULL AUTO_INCREMENT ,
  `descripcion` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`idUnidad`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `comercio`.`Categoria`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `comercio`.`Categoria` (
  `idCategoria` INT NOT NULL AUTO_INCREMENT ,
  `nombre` VARCHAR(45) NOT NULL ,
  `descripcion` VARCHAR(100) NULL ,
  `Oferta_idOferta` BIGINT NULL ,
  PRIMARY KEY (`idCategoria`) ,
  CONSTRAINT `fk_Categoria_Oferta1`
    FOREIGN KEY (`Oferta_idOferta` )
    REFERENCES `comercio`.`Oferta` (`idOferta` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_Categoria_Oferta` ON `comercio`.`Categoria` (`Oferta_idOferta` ASC) ;


-- -----------------------------------------------------
-- Table `comercio`.`Producto`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `comercio`.`Producto` (
  `idProducto` BIGINT NOT NULL AUTO_INCREMENT ,
  `codigo` VARCHAR(45) NOT NULL ,
  `descripcion` VARCHAR(100) NULL ,
  `precioActual` DOUBLE NOT NULL ,
  `Marca_idMarca` BIGINT NOT NULL ,
  `Origen_idOrigen` INT NOT NULL ,
  `Oferta_idOferta` BIGINT NULL ,
  `Unidad_idUnidad` INT NOT NULL ,
  `Categoria_idCategoria` INT NOT NULL ,
  PRIMARY KEY (`idProducto`) ,
  CONSTRAINT `fk_Producto_Marca1`
    FOREIGN KEY (`Marca_idMarca` )
    REFERENCES `comercio`.`Marca` (`idMarca` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Producto_Origen1`
    FOREIGN KEY (`Origen_idOrigen` )
    REFERENCES `comercio`.`Origen` (`idOrigen` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Producto_Oferta1`
    FOREIGN KEY (`Oferta_idOferta` )
    REFERENCES `comercio`.`Oferta` (`idOferta` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Producto_Magnitud1`
    FOREIGN KEY (`Unidad_idUnidad` )
    REFERENCES `comercio`.`Unidad` (`idUnidad` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Producto_Categoria1`
    FOREIGN KEY (`Categoria_idCategoria` )
    REFERENCES `comercio`.`Categoria` (`idCategoria` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_Producto_Marca` ON `comercio`.`Producto` (`Marca_idMarca` ASC) ;

CREATE INDEX `fk_Producto_Origen` ON `comercio`.`Producto` (`Origen_idOrigen` ASC) ;

CREATE INDEX `fk_Producto_Oferta` ON `comercio`.`Producto` (`Oferta_idOferta` ASC) ;

CREATE INDEX `fk_Producto_Magnitud` ON `comercio`.`Producto` (`Unidad_idUnidad` ASC) ;

CREATE INDEX `fk_Producto_Categoria` ON `comercio`.`Producto` (`Categoria_idCategoria` ASC) ;


-- -----------------------------------------------------
-- Table `comercio`.`Lote`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `comercio`.`Lote` (
  `idLote` BIGINT NOT NULL AUTO_INCREMENT ,
  `Producto_idProducto` BIGINT NOT NULL ,
  `codigo` VARCHAR(45) NOT NULL ,
  `fechaProduccion` DATE NOT NULL ,
  `fechaVencimiento` DATE NULL ,
  PRIMARY KEY (`idLote`) ,
  CONSTRAINT `fk_Lote_Producto`
    FOREIGN KEY (`Producto_idProducto` )
    REFERENCES `comercio`.`Producto` (`idProducto` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_Lote_Producto1` ON `comercio`.`Lote` (`Producto_idProducto` ASC) ;


-- -----------------------------------------------------
-- Table `comercio`.`Sucursal`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `comercio`.`Sucursal` (
  `idSucursal` BIGINT NOT NULL AUTO_INCREMENT ,
  `numero` BIGINT NOT NULL ,
  `ciudad` VARCHAR(45) NULL ,
  `domicilio` VARCHAR(100) NULL ,
  `telefono` BIGINT NULL ,
  PRIMARY KEY (`idSucursal`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `comercio`.`Almacen`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `comercio`.`Almacen` (
  `idAlmacen` BIGINT NOT NULL AUTO_INCREMENT ,
  `numero` BIGINT NOT NULL ,
  `Sucursal_idSucursal` BIGINT NOT NULL ,
  PRIMARY KEY (`idAlmacen`) ,
  CONSTRAINT `fk_Almacen_Sucursal1`
    FOREIGN KEY (`Sucursal_idSucursal` )
    REFERENCES `comercio`.`Sucursal` (`idSucursal` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_Almacen_Sucursal` ON `comercio`.`Almacen` (`Sucursal_idSucursal` ASC) ;


-- -----------------------------------------------------
-- Table `comercio`.`LoteAlmacenado`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `comercio`.`LoteAlmacenado` (
  `idLoteAlmacenado` BIGINT NOT NULL AUTO_INCREMENT ,
  `Lote_idLote` BIGINT NOT NULL ,
  `Almacen_idAlmacen` BIGINT NOT NULL ,
  `cantidad` DOUBLE NOT NULL ,
  PRIMARY KEY (`idLoteAlmacenado`) ,
  CONSTRAINT `fk_LoteAlmacenado_Lote1`
    FOREIGN KEY (`Lote_idLote` )
    REFERENCES `comercio`.`Lote` (`idLote` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_LoteAlmacenado_Almacen1`
    FOREIGN KEY (`Almacen_idAlmacen` )
    REFERENCES `comercio`.`Almacen` (`idAlmacen` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_LoteAlmacenado_Lote` ON `comercio`.`LoteAlmacenado` (`Lote_idLote` ASC) ;

CREATE INDEX `fk_LoteAlmacenado_Almacen` ON `comercio`.`LoteAlmacenado` (`Almacen_idAlmacen` ASC) ;


-- -----------------------------------------------------
-- Table `comercio`.`MedioDePago`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `comercio`.`MedioDePago` (
  `idMedioDePago` INT NOT NULL AUTO_INCREMENT ,
  `descripcion` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`idMedioDePago`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `comercio`.`PuntoVenta`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `comercio`.`PuntoVenta` (
  `idPuntoVenta` BIGINT NOT NULL AUTO_INCREMENT ,
  `numero` BIGINT NOT NULL ,
  `Sucursal_idSucursal` BIGINT NOT NULL ,
  PRIMARY KEY (`idPuntoVenta`) ,
  CONSTRAINT `fk_PuntoVenta_Sucursal1`
    FOREIGN KEY (`Sucursal_idSucursal` )
    REFERENCES `comercio`.`Sucursal` (`idSucursal` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_PuntoVenta_Sucursal` ON `comercio`.`PuntoVenta` (`Sucursal_idSucursal` ASC) ;


-- -----------------------------------------------------
-- Table `comercio`.`Venta`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `comercio`.`Venta` (
  `idVenta` BIGINT NOT NULL AUTO_INCREMENT ,
  `codigo` BIGINT NOT NULL ,
  `fecha` DATE NOT NULL ,
  `MedioDePago_idMedioDePago` INT NOT NULL ,
  `PuntoVenta_idPuntoVenta` BIGINT NOT NULL ,
  PRIMARY KEY (`idVenta`) ,
  CONSTRAINT `fk_Venta_MedioDePago1`
    FOREIGN KEY (`MedioDePago_idMedioDePago` )
    REFERENCES `comercio`.`MedioDePago` (`idMedioDePago` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Venta_PuntoVenta1`
    FOREIGN KEY (`PuntoVenta_idPuntoVenta` )
    REFERENCES `comercio`.`PuntoVenta` (`idPuntoVenta` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_Venta_MedioDePago` ON `comercio`.`Venta` (`MedioDePago_idMedioDePago` ASC) ;

CREATE INDEX `fk_Venta_PuntoVenta` ON `comercio`.`Venta` (`PuntoVenta_idPuntoVenta` ASC) ;


-- -----------------------------------------------------
-- Table `comercio`.`ItemVenta`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `comercio`.`ItemVenta` (
  `idItemVenta` BIGINT NOT NULL AUTO_INCREMENT ,
  `Producto_idProducto` BIGINT NOT NULL ,
  `Venta_idVenta` BIGINT NOT NULL ,
  `precio` DOUBLE NOT NULL ,
  `cantidad` DOUBLE NOT NULL ,
  `descuento` DOUBLE NOT NULL ,
  PRIMARY KEY (`idItemVenta`) ,
  CONSTRAINT `fk_ItemVenta_Producto1`
    FOREIGN KEY (`Producto_idProducto` )
    REFERENCES `comercio`.`Producto` (`idProducto` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ItemVenta_Venta1`
    FOREIGN KEY (`Venta_idVenta` )
    REFERENCES `comercio`.`Venta` (`idVenta` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_ItemVenta_Producto` ON `comercio`.`ItemVenta` (`Producto_idProducto` ASC) ;

CREATE INDEX `fk_ItemVenta_Venta` ON `comercio`.`ItemVenta` (`Venta_idVenta` ASC) ;


-- -----------------------------------------------------
-- Table `comercio`.`PrecioAnterior`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `comercio`.`PrecioAnterior` (
  `idPrecioAnterior` BIGINT NOT NULL AUTO_INCREMENT ,
  `valor` DOUBLE NOT NULL ,
  `fecha` DATE NOT NULL ,
  PRIMARY KEY (`idPrecioAnterior`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `comercio`.`Egreso`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `comercio`.`Egreso` (
  `idEgreso` BIGINT NOT NULL AUTO_INCREMENT ,
  `codigo` VARCHAR(45) NOT NULL ,
  `causaEspecial` VARCHAR(45) NOT NULL ,
  `fecha` DATE NOT NULL ,
  `observaciones` VARCHAR(256) NULL ,
  `Almacen_idAlmacen` BIGINT NOT NULL ,
  PRIMARY KEY (`idEgreso`) ,
  CONSTRAINT `fk_Egreso_Almacen1`
    FOREIGN KEY (`Almacen_idAlmacen` )
    REFERENCES `comercio`.`Almacen` (`idAlmacen` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_Egreso_Almacen` ON `comercio`.`Egreso` (`Almacen_idAlmacen` ASC) ;


-- -----------------------------------------------------
-- Table `comercio`.`LoteEgresado`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `comercio`.`LoteEgresado` (
  `idLoteEgresado` BIGINT NOT NULL AUTO_INCREMENT ,
  `Egreso_idEgreso` BIGINT NOT NULL ,
  `Lote_idLote` BIGINT NOT NULL ,
  `cantidad` DOUBLE NOT NULL ,
  PRIMARY KEY (`idLoteEgresado`) ,
  CONSTRAINT `fk_LoteEgresado_Egreso1`
    FOREIGN KEY (`Egreso_idEgreso` )
    REFERENCES `comercio`.`Egreso` (`idEgreso` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_LoteEgresado_Lote1`
    FOREIGN KEY (`Lote_idLote` )
    REFERENCES `comercio`.`Lote` (`idLote` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_LoteEgresado_Egreso` ON `comercio`.`LoteEgresado` (`Egreso_idEgreso` ASC) ;

CREATE INDEX `fk_LoteEgresado_Lote` ON `comercio`.`LoteEgresado` (`Lote_idLote` ASC) ;


-- -----------------------------------------------------
-- Table `comercio`.`PrecioAnteriorProducto`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `comercio`.`PrecioAnteriorProducto` (
  `Producto_idProducto` BIGINT NOT NULL ,
  `PrecioAnterior_idPrecioAnterior` BIGINT NOT NULL ,
  PRIMARY KEY (`Producto_idProducto`, `PrecioAnterior_idPrecioAnterior`) ,
  CONSTRAINT `fk_PrecioAnteriorProducto_Producto`
    FOREIGN KEY (`Producto_idProducto` )
    REFERENCES `comercio`.`Producto` (`idProducto` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_PrecioAnteriorProducto_PrecioAnterior`
    FOREIGN KEY (`PrecioAnterior_idPrecioAnterior` )
    REFERENCES `comercio`.`PrecioAnterior` (`idPrecioAnterior` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_PrecioAnteriorProducto_PrecioAnterior1` ON `comercio`.`PrecioAnteriorProducto` (`PrecioAnterior_idPrecioAnterior` ASC) ;

CREATE INDEX `fk_PrecioAnteriorProducto_Producto1` ON `comercio`.`PrecioAnteriorProducto` (`Producto_idProducto` ASC) ;


-- -----------------------------------------------------
-- Table `comercio`.`Remito`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `comercio`.`Remito` (
  `idRemito` BIGINT NOT NULL AUTO_INCREMENT ,
  `codigo` VARCHAR(45) NOT NULL ,
  `fecha` DATE NOT NULL ,
  PRIMARY KEY (`idRemito`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `comercio`.`LoteRemito`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `comercio`.`LoteRemito` (
  `idLoteRemito` BIGINT NOT NULL AUTO_INCREMENT ,
  `Remito_idRemito` BIGINT NOT NULL ,
  `Lote_idLote` BIGINT NOT NULL ,
  `cantidadIngresada` DOUBLE NOT NULL ,
  PRIMARY KEY (`idLoteRemito`) ,
  CONSTRAINT `fk_LoteRemito_Remito1`
    FOREIGN KEY (`Remito_idRemito` )
    REFERENCES `comercio`.`Remito` (`idRemito` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_LoteRemito_Lote1`
    FOREIGN KEY (`Lote_idLote` )
    REFERENCES `comercio`.`Lote` (`idLote` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_LoteRemito_Remito` ON `comercio`.`LoteRemito` (`Remito_idRemito` ASC) ;

CREATE INDEX `fk_LoteRemito_Lote` ON `comercio`.`LoteRemito` (`Lote_idLote` ASC) ;


-- -----------------------------------------------------
-- Table `comercio`.`ProductoEnVenta`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `comercio`.`ProductoEnVenta` (
  `idProductoEnVenta` BIGINT NOT NULL AUTO_INCREMENT ,
  `PuntoVenta_idPuntoVenta` BIGINT NOT NULL ,
  `Producto_idProducto` BIGINT NOT NULL ,
  `cantidad` DOUBLE NULL ,
  PRIMARY KEY (`idProductoEnVenta`) ,
  CONSTRAINT `fk_ProductoEnVenta_PuntoVenta1`
    FOREIGN KEY (`PuntoVenta_idPuntoVenta` )
    REFERENCES `comercio`.`PuntoVenta` (`idPuntoVenta` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ProductoEnVenta_Producto1`
    FOREIGN KEY (`Producto_idProducto` )
    REFERENCES `comercio`.`Producto` (`idProducto` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_ProductoEnVenta_PuntoVenta` ON `comercio`.`ProductoEnVenta` (`PuntoVenta_idPuntoVenta` ASC) ;

CREATE INDEX `fk_ProductoEnVenta_Producto` ON `comercio`.`ProductoEnVenta` (`Producto_idProducto` ASC) ;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
