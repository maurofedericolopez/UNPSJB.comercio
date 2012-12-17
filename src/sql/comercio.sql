-- phpMyAdmin SQL Dump
-- version 3.4.5
-- http://www.phpmyadmin.net
--
-- Servidor: localhost
-- Tiempo de generación: 17-12-2012 a las 02:46:53
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
-- Estructura de tabla para la tabla `itemventa`
--

CREATE TABLE IF NOT EXISTS `itemventa` (
  `idItemVenta` bigint(20) NOT NULL AUTO_INCREMENT,
  `Producto_idProducto` bigint(20) NOT NULL,
  `Venta_idVenta` bigint(20) NOT NULL,
  `precio` double NOT NULL,
  `cantidad` double NOT NULL,
  `descuento` double NOT NULL,
  PRIMARY KEY (`idItemVenta`),
  KEY `fk_ItemVenta_Producto` (`Producto_idProducto`),
  KEY `fk_ItemVenta_Venta` (`Venta_idVenta`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=1 ;

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=3 ;

--
-- Volcado de datos para la tabla `lote`
--

INSERT INTO `lote` (`idLote`, `Producto_idProducto`, `codigo`, `fechaProduccion`, `fechaVencimiento`) VALUES
(1, 1, 'L321654', '2011-12-08', NULL),
(2, 2, 'L132213', '2011-12-13', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `lotealmacenado`
--

CREATE TABLE IF NOT EXISTS `lotealmacenado` (
  `idLoteAlmacenado` bigint(20) NOT NULL AUTO_INCREMENT,
  `Lote_idLote` bigint(20) NOT NULL,
  `Almacen_idAlmacen` bigint(20) NOT NULL,
  `cantidad` double NOT NULL,
  PRIMARY KEY (`idLoteAlmacenado`),
  KEY `fk_LoteAlmacenado_Lote` (`Lote_idLote`),
  KEY `fk_LoteAlmacenado_Almacen` (`Almacen_idAlmacen`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=3 ;

--
-- Volcado de datos para la tabla `lotealmacenado`
--

INSERT INTO `lotealmacenado` (`idLoteAlmacenado`, `Lote_idLote`, `Almacen_idAlmacen`, `cantidad`) VALUES
(1, 1, 1, 8),
(2, 2, 1, 7);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `loteegresado`
--

CREATE TABLE IF NOT EXISTS `loteegresado` (
  `idLoteEgresado` bigint(20) NOT NULL AUTO_INCREMENT,
  `Egreso_idEgreso` bigint(20) NOT NULL,
  `Lote_idLote` bigint(20) NOT NULL,
  `cantidad` double NOT NULL,
  PRIMARY KEY (`idLoteEgresado`),
  KEY `fk_LoteEgresado_Egreso` (`Egreso_idEgreso`),
  KEY `fk_LoteEgresado_Lote` (`Lote_idLote`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `loteremito`
--

CREATE TABLE IF NOT EXISTS `loteremito` (
  `idLoteRemito` bigint(20) NOT NULL AUTO_INCREMENT,
  `Remito_idRemito` bigint(20) NOT NULL,
  `Lote_idLote` bigint(20) NOT NULL,
  `cantidadIngresada` double NOT NULL,
  PRIMARY KEY (`idLoteRemito`),
  KEY `fk_LoteRemito_Remito` (`Remito_idRemito`),
  KEY `fk_LoteRemito_Lote` (`Lote_idLote`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=3 ;

--
-- Volcado de datos para la tabla `loteremito`
--

INSERT INTO `loteremito` (`idLoteRemito`, `Remito_idRemito`, `Lote_idLote`, `cantidadIngresada`) VALUES
(1, 1, 1, 10),
(2, 1, 2, 10);

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=10 ;

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
  `idPrecioAnterior` bigint(20) NOT NULL AUTO_INCREMENT,
  `valor` double NOT NULL,
  `fecha` date NOT NULL,
  `Producto_idProducto` bigint(20) NOT NULL,
  PRIMARY KEY (`idPrecioAnterior`),
  KEY `fk_PrecioAnterior_Producto` (`Producto_idProducto`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=3 ;

--
-- Volcado de datos para la tabla `precioanterior`
--

INSERT INTO `precioanterior` (`idPrecioAnterior`, `valor`, `fecha`, `Producto_idProducto`) VALUES
(1, 459.59, '2012-01-01', 1),
(2, 730, '2012-01-01', 2);

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
  `idProductoEnVenta` bigint(20) NOT NULL AUTO_INCREMENT,
  `PuntoVenta_idPuntoVenta` bigint(20) NOT NULL,
  `Producto_idProducto` bigint(20) NOT NULL,
  `cantidad` double DEFAULT NULL,
  PRIMARY KEY (`idProductoEnVenta`),
  KEY `fk_ProductoEnVenta_PuntoVenta` (`PuntoVenta_idPuntoVenta`),
  KEY `fk_ProductoEnVenta_Producto` (`Producto_idProducto`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=3 ;

--
-- Volcado de datos para la tabla `productoenventa`
--

INSERT INTO `productoenventa` (`idProductoEnVenta`, `PuntoVenta_idPuntoVenta`, `Producto_idProducto`, `cantidad`) VALUES
(1, 1, 1, 2),
(2, 1, 2, 3);

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
  `idRemito` bigint(20) NOT NULL AUTO_INCREMENT,
  `fecha` date NOT NULL,
  PRIMARY KEY (`idRemito`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=2 ;

--
-- Volcado de datos para la tabla `remito`
--

INSERT INTO `remito` (`idRemito`, `fecha`) VALUES
(1, '2012-01-08');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `sucursal`
--

CREATE TABLE IF NOT EXISTS `sucursal` (
  `idSucursal` bigint(20) NOT NULL AUTO_INCREMENT,
  `numero` bigint(20) NOT NULL,
  `ciudad` varchar(45) COLLATE latin1_spanish_ci DEFAULT NULL,
  `domicilio` varchar(100) COLLATE latin1_spanish_ci DEFAULT NULL,
  `telefono` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`idSucursal`),
  UNIQUE KEY `numero_UNIQUE` (`numero`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=3 ;

--
-- Volcado de datos para la tabla `sucursal`
--

INSERT INTO `sucursal` (`idSucursal`, `numero`, `ciudad`, `domicilio`, `telefono`) VALUES
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
-- Estructura de tabla para la tabla `venta`
--

CREATE TABLE IF NOT EXISTS `venta` (
  `idVenta` bigint(20) NOT NULL AUTO_INCREMENT,
  `codigo` bigint(20) NOT NULL,
  `fecha` date NOT NULL,
  `MedioDePago_idMedioDePago` int(11) NOT NULL,
  `PuntoVenta_idPuntoVenta` bigint(20) NOT NULL,
  PRIMARY KEY (`idVenta`),
  UNIQUE KEY `codigo_UNIQUE` (`codigo`),
  KEY `fk_Venta_MedioDePago` (`MedioDePago_idMedioDePago`),
  KEY `fk_Venta_PuntoVenta` (`PuntoVenta_idPuntoVenta`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci AUTO_INCREMENT=1 ;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `almacen`
--
ALTER TABLE `almacen`
  ADD CONSTRAINT `fk_Almacen_Sucursal1` FOREIGN KEY (`Sucursal_idSucursal`) REFERENCES `sucursal` (`idSucursal`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `categoria`
--
ALTER TABLE `categoria`
  ADD CONSTRAINT `fk_Categoria_Oferta1` FOREIGN KEY (`Oferta_idOferta`) REFERENCES `oferta` (`idOferta`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `egreso`
--
ALTER TABLE `egreso`
  ADD CONSTRAINT `fk_Egreso_Almacen1` FOREIGN KEY (`Almacen_idAlmacen`) REFERENCES `almacen` (`idAlmacen`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `itemventa`
--
ALTER TABLE `itemventa`
  ADD CONSTRAINT `fk_ItemVenta_Producto1` FOREIGN KEY (`Producto_idProducto`) REFERENCES `producto` (`idProducto`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_ItemVenta_Venta1` FOREIGN KEY (`Venta_idVenta`) REFERENCES `venta` (`idVenta`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `lote`
--
ALTER TABLE `lote`
  ADD CONSTRAINT `fk_Lote_Producto` FOREIGN KEY (`Producto_idProducto`) REFERENCES `producto` (`idProducto`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `lotealmacenado`
--
ALTER TABLE `lotealmacenado`
  ADD CONSTRAINT `fk_LoteAlmacenado_Almacen1` FOREIGN KEY (`Almacen_idAlmacen`) REFERENCES `almacen` (`idAlmacen`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_LoteAlmacenado_Lote1` FOREIGN KEY (`Lote_idLote`) REFERENCES `lote` (`idLote`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `loteegresado`
--
ALTER TABLE `loteegresado`
  ADD CONSTRAINT `fk_LoteEgresado_Egreso1` FOREIGN KEY (`Egreso_idEgreso`) REFERENCES `egreso` (`idEgreso`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_LoteEgresado_Lote1` FOREIGN KEY (`Lote_idLote`) REFERENCES `lote` (`idLote`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `loteremito`
--
ALTER TABLE `loteremito`
  ADD CONSTRAINT `fk_LoteRemito_Lote1` FOREIGN KEY (`Lote_idLote`) REFERENCES `lote` (`idLote`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_LoteRemito_Remito1` FOREIGN KEY (`Remito_idRemito`) REFERENCES `remito` (`idRemito`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `marca`
--
ALTER TABLE `marca`
  ADD CONSTRAINT `fk_Marca_Oferta1` FOREIGN KEY (`Oferta_idOferta`) REFERENCES `oferta` (`idOferta`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `precioanterior`
--
ALTER TABLE `precioanterior`
  ADD CONSTRAINT `fk_PrecioAnterior_Producto1` FOREIGN KEY (`Producto_idProducto`) REFERENCES `producto` (`idProducto`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `producto`
--
ALTER TABLE `producto`
  ADD CONSTRAINT `fk_Producto_Categoria1` FOREIGN KEY (`Categoria_idCategoria`) REFERENCES `categoria` (`idCategoria`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_Producto_Magnitud1` FOREIGN KEY (`Unidad_idUnidad`) REFERENCES `unidad` (`idUnidad`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_Producto_Marca1` FOREIGN KEY (`Marca_idMarca`) REFERENCES `marca` (`idMarca`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_Producto_Oferta1` FOREIGN KEY (`Oferta_idOferta`) REFERENCES `oferta` (`idOferta`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_Producto_Origen1` FOREIGN KEY (`Origen_idOrigen`) REFERENCES `origen` (`idOrigen`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `productoenventa`
--
ALTER TABLE `productoenventa`
  ADD CONSTRAINT `fk_ProductoEnVenta_Producto1` FOREIGN KEY (`Producto_idProducto`) REFERENCES `producto` (`idProducto`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_ProductoEnVenta_PuntoVenta1` FOREIGN KEY (`PuntoVenta_idPuntoVenta`) REFERENCES `puntoventa` (`idPuntoVenta`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `puntoventa`
--
ALTER TABLE `puntoventa`
  ADD CONSTRAINT `fk_PuntoVenta_Sucursal1` FOREIGN KEY (`Sucursal_idSucursal`) REFERENCES `sucursal` (`idSucursal`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `venta`
--
ALTER TABLE `venta`
  ADD CONSTRAINT `fk_Venta_MedioDePago1` FOREIGN KEY (`MedioDePago_idMedioDePago`) REFERENCES `mediodepago` (`idMedioDePago`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_Venta_PuntoVenta1` FOREIGN KEY (`PuntoVenta_idPuntoVenta`) REFERENCES `puntoventa` (`idPuntoVenta`) ON DELETE NO ACTION ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
