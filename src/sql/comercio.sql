-- phpMyAdmin SQL Dump
-- version 3.4.5
-- http://www.phpmyadmin.net
--
-- Servidor: localhost
-- Tiempo de generación: 06-01-2013 a las 23:44:20
-- Versión del servidor: 5.5.16
-- Versión de PHP: 5.3.8

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de datos: `comercio`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `almacen`
--

CREATE TABLE IF NOT EXISTS `almacen` (
  `idAlmacen` bigint(20) NOT NULL AUTO_INCREMENT,
  `numero` bigint(20) NOT NULL,
  `Sucursal_idSucursal` bigint(20) NOT NULL,
  PRIMARY KEY (`idAlmacen`),
  KEY `fk_Almacen_Sucursal` (`Sucursal_idSucursal`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=3 ;

--
-- Volcado de datos para la tabla `almacen`
--

INSERT INTO `almacen` (`idAlmacen`, `numero`, `Sucursal_idSucursal`) VALUES
(1, 1, 1),
(2, 1, 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `categoria`
--

CREATE TABLE IF NOT EXISTS `categoria` (
  `idCategoria` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) COLLATE latin1_spanish_ci NOT NULL,
  `descripcion` varchar(100) COLLATE latin1_spanish_ci DEFAULT NULL,
  `Oferta_idOferta` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`idCategoria`),
  KEY `fk_Categoria_Oferta` (`Oferta_idOferta`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=11 ;

--
-- Volcado de datos para la tabla `categoria`
--

INSERT INTO `categoria` (`idCategoria`, `nombre`, `descripcion`, `Oferta_idOferta`) VALUES
(1, 'MEMORIA', NULL, NULL),
(2, 'TARJETA GRAFICA', NULL, NULL),
(3, 'TARJETA DE RED', NULL, NULL),
(4, 'TARJETA DE SONIDO', NULL, NULL),
(5, 'PLACA MADRE', NULL, NULL),
(6, 'E/S', NULL, NULL),
(7, 'DISCO RIGIDO', NULL, NULL),
(8, 'PROCESADOR', NULL, NULL),
(9, 'IMPRESORA', NULL, NULL),
(10, 'MONITOR', NULL, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `egreso`
--

CREATE TABLE IF NOT EXISTS `egreso` (
  `idEgreso` bigint(20) NOT NULL AUTO_INCREMENT,
  `codigo` varchar(45) COLLATE latin1_spanish_ci NOT NULL,
  `causaEspecial` varchar(45) COLLATE latin1_spanish_ci NOT NULL,
  `fecha` date NOT NULL,
  `observaciones` varchar(256) COLLATE latin1_spanish_ci DEFAULT NULL,
  `Almacen_idAlmacen` bigint(20) NOT NULL,
  PRIMARY KEY (`idEgreso`),
  UNIQUE KEY `codigo_UNIQUE` (`codigo`),
  KEY `fk_Egreso_Almacen` (`Almacen_idAlmacen`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `gerente`
--

CREATE TABLE IF NOT EXISTS `gerente` (
  `idGerente` bigint(20) NOT NULL AUTO_INCREMENT,
  `APELLIDO` varchar(45) COLLATE utf8_spanish_ci NOT NULL,
  `NOMBRE` varchar(45) COLLATE utf8_spanish_ci NOT NULL,
  `NOMBREUSUARIO` varchar(100) COLLATE utf8_spanish_ci NOT NULL,
  `CONTRASEÑA` varchar(100) COLLATE utf8_spanish_ci NOT NULL,
  `DNI` bigint(8) NOT NULL,
  PRIMARY KEY (`idGerente`),
  UNIQUE KEY `NOMBREUSUARIO` (`NOMBREUSUARIO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `gestorinventario`
--

CREATE TABLE IF NOT EXISTS `gestorinventario` (
  `idGestorInventario` bigint(20) NOT NULL AUTO_INCREMENT,
  `APELLIDO` varchar(45) COLLATE utf8_spanish_ci NOT NULL,
  `NOMBRE` varchar(45) COLLATE utf8_spanish_ci NOT NULL,
  `NOMBREUSUARIO` varchar(100) COLLATE utf8_spanish_ci NOT NULL,
  `CONTRASEÑA` varchar(100) COLLATE utf8_spanish_ci NOT NULL,
  `DNI` bigint(8) NOT NULL,
  PRIMARY KEY (`idGestorInventario`),
  UNIQUE KEY `NOMBREUSUARIO` (`NOMBREUSUARIO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `itemventa`
--

CREATE TABLE IF NOT EXISTS `itemventa` (
  `idItemVenta` bigint(20) NOT NULL,
  `CANTIDAD` double DEFAULT NULL,
  `DESCUENTO` double DEFAULT NULL,
  `PRECIO` double DEFAULT NULL,
  `PRODUCTO_idProducto` bigint(20) DEFAULT NULL,
  `VENTA_idVenta` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`idItemVenta`),
  KEY `FK_ITEMVENTA_PRODUCTO_idProducto` (`PRODUCTO_idProducto`),
  KEY `FK_ITEMVENTA_VENTA_idVenta` (`VENTA_idVenta`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `lote`
--

CREATE TABLE IF NOT EXISTS `lote` (
  `idLote` bigint(20) NOT NULL AUTO_INCREMENT,
  `Producto_idProducto` bigint(20) NOT NULL,
  `codigo` varchar(45) COLLATE latin1_spanish_ci NOT NULL,
  `fechaProduccion` date NOT NULL,
  `fechaVencimiento` date DEFAULT NULL,
  PRIMARY KEY (`idLote`),
  UNIQUE KEY `codigo_UNIQUE` (`codigo`),
  KEY `fk_Lote_Producto1` (`Producto_idProducto`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=53 ;

--
-- Volcado de datos para la tabla `lote`
--

INSERT INTO `lote` (`idLote`, `Producto_idProducto`, `codigo`, `fechaProduccion`, `fechaVencimiento`) VALUES
(52, 1, 'L6754', '2011-09-21', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `lotealmacenado`
--

CREATE TABLE IF NOT EXISTS `lotealmacenado` (
  `idLoteAlmacenado` bigint(20) NOT NULL,
  `CANTIDAD` double DEFAULT NULL,
  `ALMACEN_idAlmacen` bigint(20) DEFAULT NULL,
  `LOTE_idLote` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`idLoteAlmacenado`),
  KEY `FK_LOTEALMACENADO_ALMACEN_idAlmacen` (`ALMACEN_idAlmacen`),
  KEY `FK_LOTEALMACENADO_LOTE_idLote` (`LOTE_idLote`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `lotealmacenado`
--

INSERT INTO `lotealmacenado` (`idLoteAlmacenado`, `CANTIDAD`, `ALMACEN_idAlmacen`, `LOTE_idLote`) VALUES
(54, 10, NULL, 52);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `loteegresado`
--

CREATE TABLE IF NOT EXISTS `loteegresado` (
  `idLoteEgresado` bigint(20) NOT NULL,
  `CANTIDAD` double DEFAULT NULL,
  `EGRESO_idEgreso` bigint(20) DEFAULT NULL,
  `LOTE_idLote` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`idLoteEgresado`),
  KEY `FK_LOTEEGRESADO_EGRESO_idEgreso` (`EGRESO_idEgreso`),
  KEY `FK_LOTEEGRESADO_LOTE_idLote` (`LOTE_idLote`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `loteremito`
--

CREATE TABLE IF NOT EXISTS `loteremito` (
  `idLoteRemito` bigint(20) NOT NULL,
  `CANTIDADINGRESADA` double DEFAULT NULL,
  `LOTE_idLote` bigint(20) DEFAULT NULL,
  `REMITO_idRemito` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`idLoteRemito`),
  KEY `FK_LOTEREMITO_REMITO_idRemito` (`REMITO_idRemito`),
  KEY `FK_LOTEREMITO_LOTE_idLote` (`LOTE_idLote`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `loteremito`
--

INSERT INTO `loteremito` (`idLoteRemito`, `CANTIDADINGRESADA`, `LOTE_idLote`, `REMITO_idRemito`) VALUES
(53, 10, 52, 51);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `marca`
--

CREATE TABLE IF NOT EXISTS `marca` (
  `idMarca` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) COLLATE latin1_spanish_ci NOT NULL,
  `abreviacion` varchar(45) COLLATE latin1_spanish_ci DEFAULT NULL,
  `Oferta_idOferta` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`idMarca`),
  KEY `fk_Marca_Oferta` (`Oferta_idOferta`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=7 ;

--
-- Volcado de datos para la tabla `marca`
--

INSERT INTO `marca` (`idMarca`, `nombre`, `abreviacion`, `Oferta_idOferta`) VALUES
(1, 'AMD', 'AMD', NULL),
(2, 'HEWLETT PACKARD', 'HP', NULL),
(3, 'KINGSTON', 'KINGSTON', NULL),
(4, 'LG', 'LG', NULL),
(5, 'SONY', 'SONY', NULL),
(6, 'ASUS', 'ASUS', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `mediodepago`
--

CREATE TABLE IF NOT EXISTS `mediodepago` (
  `idMedioDePago` int(11) NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(45) COLLATE latin1_spanish_ci NOT NULL,
  PRIMARY KEY (`idMedioDePago`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=4 ;

--
-- Volcado de datos para la tabla `mediodepago`
--

INSERT INTO `mediodepago` (`idMedioDePago`, `descripcion`) VALUES
(1, 'EFECTIVO'),
(2, 'TARJETA DE CREDITO'),
(3, 'DEBITO');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `mediopago`
--

CREATE TABLE IF NOT EXISTS `mediopago` (
  `idMedioDePago` bigint(20) NOT NULL,
  PRIMARY KEY (`idMedioDePago`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `oferta`
--

CREATE TABLE IF NOT EXISTS `oferta` (
  `idOferta` bigint(20) NOT NULL AUTO_INCREMENT,
  `descuento` double NOT NULL,
  `fechaInicio` date NOT NULL,
  `fechaFin` date NOT NULL,
  PRIMARY KEY (`idOferta`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `origen`
--

CREATE TABLE IF NOT EXISTS `origen` (
  `idOrigen` int(11) NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(45) COLLATE latin1_spanish_ci NOT NULL,
  PRIMARY KEY (`idOrigen`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=6 ;

--
-- Volcado de datos para la tabla `origen`
--

INSERT INTO `origen` (`idOrigen`, `descripcion`) VALUES
(1, 'ARGENTINA'),
(2, 'JAPON'),
(3, 'ESTADOS UNIDOS'),
(4, 'CHINA'),
(5, 'FRANCIA');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `precioanterior`
--

CREATE TABLE IF NOT EXISTS `precioanterior` (
  `idPrecioAnterior` bigint(20) NOT NULL,
  `FECHA` date DEFAULT NULL,
  `VALOR` double DEFAULT NULL,
  `PRODUCTO_idProducto` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`idPrecioAnterior`),
  KEY `FK_PRECIOANTERIOR_PRODUCTO_idProducto` (`PRODUCTO_idProducto`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `producto`
--

CREATE TABLE IF NOT EXISTS `producto` (
  `idProducto` bigint(20) NOT NULL AUTO_INCREMENT,
  `codigo` varchar(45) COLLATE latin1_spanish_ci NOT NULL,
  `descripcion` varchar(100) COLLATE latin1_spanish_ci DEFAULT NULL,
  `precioActual` double NOT NULL,
  `Marca_idMarca` bigint(20) NOT NULL,
  `Origen_idOrigen` int(11) NOT NULL,
  `Oferta_idOferta` bigint(20) DEFAULT NULL,
  `Unidad_idUnidad` int(11) NOT NULL,
  `Categoria_idCategoria` int(11) NOT NULL,
  PRIMARY KEY (`idProducto`),
  UNIQUE KEY `codigo_UNIQUE` (`codigo`),
  KEY `fk_Producto_Marca` (`Marca_idMarca`),
  KEY `fk_Producto_Origen` (`Origen_idOrigen`),
  KEY `fk_Producto_Oferta` (`Oferta_idOferta`),
  KEY `fk_Producto_Magnitud` (`Unidad_idUnidad`),
  KEY `fk_Producto_Categoria` (`Categoria_idCategoria`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=3 ;

--
-- Volcado de datos para la tabla `producto`
--

INSERT INTO `producto` (`idProducto`, `codigo`, `descripcion`, `precioActual`, `Marca_idMarca`, `Origen_idOrigen`, `Oferta_idOferta`, `Unidad_idUnidad`, `Categoria_idCategoria`) VALUES
(1, 'PM001', 'M4N68T-M LE V2', 459.59, 6, 2, NULL, 1, 5),
(2, 'TV001', 'HD 6670', 730, 1, 3, NULL, 1, 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `productoenventa`
--

CREATE TABLE IF NOT EXISTS `productoenventa` (
  `idProductoEnVenta` bigint(20) NOT NULL,
  `CANTIDAD` double DEFAULT NULL,
  `PRODUCTO_idProducto` bigint(20) DEFAULT NULL,
  `PUNTOVENTA_idPuntoVenta` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`idProductoEnVenta`),
  KEY `FK_PRODUCTOENVENTA_PUNTOVENTA_idPuntoVenta` (`PUNTOVENTA_idPuntoVenta`),
  KEY `FK_PRODUCTOENVENTA_PRODUCTO_idProducto` (`PRODUCTO_idProducto`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `puntoventa`
--

CREATE TABLE IF NOT EXISTS `puntoventa` (
  `idPuntoVenta` bigint(20) NOT NULL AUTO_INCREMENT,
  `numero` bigint(20) NOT NULL,
  `Sucursal_idSucursal` bigint(20) NOT NULL,
  PRIMARY KEY (`idPuntoVenta`),
  KEY `fk_PuntoVenta_Sucursal` (`Sucursal_idSucursal`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=3 ;

--
-- Volcado de datos para la tabla `puntoventa`
--

INSERT INTO `puntoventa` (`idPuntoVenta`, `numero`, `Sucursal_idSucursal`) VALUES
(1, 1, 1),
(2, 1, 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `remito`
--

CREATE TABLE IF NOT EXISTS `remito` (
  `idRemito` bigint(20) NOT NULL,
  `CODIGO` varchar(255) COLLATE utf8_spanish_ci DEFAULT NULL,
  `FECHA` date DEFAULT NULL,
  PRIMARY KEY (`idRemito`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `remito`
--

INSERT INTO `remito` (`idRemito`, `CODIGO`, `FECHA`) VALUES
(1, 'R5643', '2012-12-30'),
(51, 'R6543', '2012-12-30');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `sequence`
--

CREATE TABLE IF NOT EXISTS `sequence` (
  `SEQ_NAME` varchar(50) COLLATE utf8_spanish_ci NOT NULL,
  `SEQ_COUNT` decimal(38,0) DEFAULT NULL,
  PRIMARY KEY (`SEQ_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `sequence`
--

INSERT INTO `sequence` (`SEQ_NAME`, `SEQ_COUNT`) VALUES
('SEQ_GEN', 100);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `sucursal`
--

CREATE TABLE IF NOT EXISTS `sucursal` (
  `idSucursal` bigint(20) NOT NULL AUTO_INCREMENT,
  `NUMERO` bigint(20) NOT NULL,
  `CIUDAD` varchar(45) COLLATE latin1_spanish_ci DEFAULT NULL,
  `DOMICILIO` varchar(100) COLLATE latin1_spanish_ci DEFAULT NULL,
  `TELEFONO` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`idSucursal`),
  UNIQUE KEY `numero_UNIQUE` (`NUMERO`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=3 ;

--
-- Volcado de datos para la tabla `sucursal`
--

INSERT INTO `sucursal` (`idSucursal`, `NUMERO`, `CIUDAD`, `DOMICILIO`, `TELEFONO`) VALUES
(1, 1, 'USHUAIA', 'SAN MARTIN 1234', 445698),
(2, 2, 'RIO GRANDE', '9 DE JULIO 995', 441379);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `unidad`
--

CREATE TABLE IF NOT EXISTS `unidad` (
  `idUnidad` int(11) NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(45) COLLATE latin1_spanish_ci NOT NULL,
  PRIMARY KEY (`idUnidad`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=2 ;

--
-- Volcado de datos para la tabla `unidad`
--

INSERT INTO `unidad` (`idUnidad`, `descripcion`) VALUES
(1, 'UNIDAD');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `vendedor`
--

CREATE TABLE IF NOT EXISTS `vendedor` (
  `idVendedor` bigint(20) NOT NULL AUTO_INCREMENT,
  `APELLIDO` varchar(45) COLLATE utf8_spanish_ci NOT NULL,
  `NOMBRE` varchar(45) COLLATE utf8_spanish_ci NOT NULL,
  `NOMBREUSUARIO` varchar(100) COLLATE utf8_spanish_ci NOT NULL,
  `CONTRASEÑA` varchar(100) COLLATE utf8_spanish_ci NOT NULL,
  `DNI` bigint(8) NOT NULL,
  PRIMARY KEY (`idVendedor`),
  UNIQUE KEY `NOMBREUSUARIO` (`NOMBREUSUARIO`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci AUTO_INCREMENT=2 ;

--
-- Volcado de datos para la tabla `vendedor`
--

INSERT INTO `vendedor` (`idVendedor`, `APELLIDO`, `NOMBRE`, `NOMBREUSUARIO`, `CONTRASEÑA`, `DNI`) VALUES
(1, 'SANDOVAL', 'CARLOS', 'CARLOSSANDOVAL', '123456', 28765345);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `venta`
--

CREATE TABLE IF NOT EXISTS `venta` (
  `idVenta` bigint(20) NOT NULL,
  `CODIGO` bigint(20) DEFAULT NULL,
  `FECHA` date DEFAULT NULL,
  `MEDIODEPAGO_idMedioDePago` bigint(20) DEFAULT NULL,
  `PUNTOVENTA_idPuntoVenta` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`idVenta`),
  KEY `FK_VENTA_PUNTOVENTA_idPuntoVenta` (`PUNTOVENTA_idPuntoVenta`),
  KEY `FK_VENTA_MEDIODEPAGO_idMedioDePago` (`MEDIODEPAGO_idMedioDePago`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `almacen`
--
ALTER TABLE `almacen`
  ADD CONSTRAINT `fk_Almacen_Sucursal1` FOREIGN KEY (`Sucursal_idSucursal`) REFERENCES `sucursal` (`idSucursal`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_ALMACEN_SUCURSAL_idSucursal` FOREIGN KEY (`Sucursal_idSucursal`) REFERENCES `sucursal` (`idSucursal`);

--
-- Filtros para la tabla `categoria`
--
ALTER TABLE `categoria`
  ADD CONSTRAINT `fk_Categoria_Oferta1` FOREIGN KEY (`Oferta_idOferta`) REFERENCES `oferta` (`idOferta`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_CATEGORIA_OFERTA_idOferta` FOREIGN KEY (`Oferta_idOferta`) REFERENCES `oferta` (`idOferta`);

--
-- Filtros para la tabla `egreso`
--
ALTER TABLE `egreso`
  ADD CONSTRAINT `fk_Egreso_Almacen1` FOREIGN KEY (`Almacen_idAlmacen`) REFERENCES `almacen` (`idAlmacen`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_EGRESO_ALMACEN_idAlmacen` FOREIGN KEY (`Almacen_idAlmacen`) REFERENCES `almacen` (`idAlmacen`);

--
-- Filtros para la tabla `itemventa`
--
ALTER TABLE `itemventa`
  ADD CONSTRAINT `FK_ITEMVENTA_PRODUCTO_idProducto` FOREIGN KEY (`PRODUCTO_idProducto`) REFERENCES `producto` (`idProducto`),
  ADD CONSTRAINT `FK_ITEMVENTA_VENTA_idVenta` FOREIGN KEY (`VENTA_idVenta`) REFERENCES `venta` (`idVenta`);

--
-- Filtros para la tabla `lote`
--
ALTER TABLE `lote`
  ADD CONSTRAINT `fk_Lote_Producto` FOREIGN KEY (`Producto_idProducto`) REFERENCES `producto` (`idProducto`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_LOTE_PRODUCTO_idProducto` FOREIGN KEY (`Producto_idProducto`) REFERENCES `producto` (`idProducto`);

--
-- Filtros para la tabla `lotealmacenado`
--
ALTER TABLE `lotealmacenado`
  ADD CONSTRAINT `FK_LOTEALMACENADO_ALMACEN_idAlmacen` FOREIGN KEY (`ALMACEN_idAlmacen`) REFERENCES `almacen` (`idAlmacen`),
  ADD CONSTRAINT `FK_LOTEALMACENADO_LOTE_idLote` FOREIGN KEY (`LOTE_idLote`) REFERENCES `lote` (`idLote`);

--
-- Filtros para la tabla `loteegresado`
--
ALTER TABLE `loteegresado`
  ADD CONSTRAINT `FK_LOTEEGRESADO_EGRESO_idEgreso` FOREIGN KEY (`EGRESO_idEgreso`) REFERENCES `egreso` (`idEgreso`),
  ADD CONSTRAINT `FK_LOTEEGRESADO_LOTE_idLote` FOREIGN KEY (`LOTE_idLote`) REFERENCES `lote` (`idLote`);

--
-- Filtros para la tabla `loteremito`
--
ALTER TABLE `loteremito`
  ADD CONSTRAINT `FK_LOTEREMITO_LOTE_idLote` FOREIGN KEY (`LOTE_idLote`) REFERENCES `lote` (`idLote`),
  ADD CONSTRAINT `FK_LOTEREMITO_REMITO_idRemito` FOREIGN KEY (`REMITO_idRemito`) REFERENCES `remito` (`idRemito`);

--
-- Filtros para la tabla `marca`
--
ALTER TABLE `marca`
  ADD CONSTRAINT `fk_Marca_Oferta1` FOREIGN KEY (`Oferta_idOferta`) REFERENCES `oferta` (`idOferta`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_MARCA_OFERTA_idOferta` FOREIGN KEY (`Oferta_idOferta`) REFERENCES `oferta` (`idOferta`);

--
-- Filtros para la tabla `precioanterior`
--
ALTER TABLE `precioanterior`
  ADD CONSTRAINT `FK_PRECIOANTERIOR_PRODUCTO_idProducto` FOREIGN KEY (`PRODUCTO_idProducto`) REFERENCES `producto` (`idProducto`);

--
-- Filtros para la tabla `producto`
--
ALTER TABLE `producto`
  ADD CONSTRAINT `fk_Producto_Categoria1` FOREIGN KEY (`Categoria_idCategoria`) REFERENCES `categoria` (`idCategoria`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_PRODUCTO_CATEGORIA_idCategoria` FOREIGN KEY (`Categoria_idCategoria`) REFERENCES `categoria` (`idCategoria`),
  ADD CONSTRAINT `fk_Producto_Magnitud1` FOREIGN KEY (`Unidad_idUnidad`) REFERENCES `unidad` (`idUnidad`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_Producto_Marca1` FOREIGN KEY (`Marca_idMarca`) REFERENCES `marca` (`idMarca`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_PRODUCTO_MARCA_idMarca` FOREIGN KEY (`Marca_idMarca`) REFERENCES `marca` (`idMarca`),
  ADD CONSTRAINT `fk_Producto_Oferta1` FOREIGN KEY (`Oferta_idOferta`) REFERENCES `oferta` (`idOferta`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_PRODUCTO_OFERTA_idOferta` FOREIGN KEY (`Oferta_idOferta`) REFERENCES `oferta` (`idOferta`),
  ADD CONSTRAINT `fk_Producto_Origen1` FOREIGN KEY (`Origen_idOrigen`) REFERENCES `origen` (`idOrigen`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_PRODUCTO_ORIGEN_idOrigen` FOREIGN KEY (`Origen_idOrigen`) REFERENCES `origen` (`idOrigen`),
  ADD CONSTRAINT `FK_PRODUCTO_UNIDAD_idUnidad` FOREIGN KEY (`Unidad_idUnidad`) REFERENCES `unidad` (`idUnidad`);

--
-- Filtros para la tabla `productoenventa`
--
ALTER TABLE `productoenventa`
  ADD CONSTRAINT `FK_PRODUCTOENVENTA_PRODUCTO_idProducto` FOREIGN KEY (`PRODUCTO_idProducto`) REFERENCES `producto` (`idProducto`),
  ADD CONSTRAINT `FK_PRODUCTOENVENTA_PUNTOVENTA_idPuntoVenta` FOREIGN KEY (`PUNTOVENTA_idPuntoVenta`) REFERENCES `puntoventa` (`idPuntoVenta`);

--
-- Filtros para la tabla `puntoventa`
--
ALTER TABLE `puntoventa`
  ADD CONSTRAINT `fk_PuntoVenta_Sucursal1` FOREIGN KEY (`Sucursal_idSucursal`) REFERENCES `sucursal` (`idSucursal`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_PUNTOVENTA_SUCURSAL_idSucursal` FOREIGN KEY (`Sucursal_idSucursal`) REFERENCES `sucursal` (`idSucursal`);

--
-- Filtros para la tabla `venta`
--
ALTER TABLE `venta`
  ADD CONSTRAINT `FK_VENTA_MEDIODEPAGO_idMedioDePago` FOREIGN KEY (`MEDIODEPAGO_idMedioDePago`) REFERENCES `mediopago` (`idMedioDePago`),
  ADD CONSTRAINT `FK_VENTA_PUNTOVENTA_idPuntoVenta` FOREIGN KEY (`PUNTOVENTA_idPuntoVenta`) REFERENCES `puntoventa` (`idPuntoVenta`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
