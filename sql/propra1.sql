-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Erstellungszeit: 13. Jun 2019 um 02:39
-- Server-Version: 10.1.39-MariaDB
-- PHP-Version: 7.1.29

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;


CREATE TABLE `documents` (
  `objectid` int(11) NOT NULL,
  `type` int(11) NOT NULL,
  `filename` varchar(255) NOT NULL,
  `fileext` varchar(255) NOT NULL,
  `datum` datetime NOT NULL,
  `id` int(11) NOT NULL,
  `userid` int(11) NOT NULL,
  `fromuser` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `documents` (`objectid`, `type`, `filename`, `fileext`, `datum`, `id`, `userid`, `fromuser`) VALUES
(3, 1, 'no-image2.png', 'png', '2019-06-11 20:27:13', 1, 0, 0),
(4, 1, 'no-image2.png', 'png', '2019-06-11 20:37:35', 2, 0, 0),
(5, 1, 'adminmodals.php', 'php', '2019-06-12 00:47:43', 3, 0, 0),
(6, 1, 'immobilien.php', 'php', '2019-06-12 10:34:39', 4, 0, 0),
(7, 1, 'hashtag.png', 'png', '2019-06-12 10:36:45', 5, 0, 0),
(8, 1, 'stopWorking.png', 'png', '2019-06-13 02:34:52', 6, 0, 0);

CREATE TABLE `login_attempts` (
  `time` time NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `login_attempts` (`time`, `user_id`) VALUES
('838:59:59', 0),
('838:59:59', 0),
('00:00:00', 0),
('00:00:00', 0),
('00:00:00', 0),
('00:00:00', 0),
('838:59:59', 1),
('838:59:59', 1),
('838:59:59', 1);


CREATE TABLE `mieterverwaltung` (
  `id` int(11) NOT NULL,
  `mieterid` int(11) NOT NULL,
  `verwaltungseinheitid` int(11) NOT NULL,
  `mietbeginn` date NOT NULL,
  `vertragsid` int(11) NOT NULL,
  `kuendigungsid` int(11) NOT NULL,
  `mietende` date NOT NULL,
  `kuendigungsdatum` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `mieterverwaltung` (`id`, `mieterid`, `verwaltungseinheitid`, `mietbeginn`, `vertragsid`, `kuendigungsid`, `mietende`, `kuendigungsdatum`) VALUES
(1, 1, 0, '0000-00-00', 0, 0, '0000-00-00', '0000-00-00'),
(3, 4, 1, '2019-06-07', 5, 0, '0000-00-00', '0000-00-00'),
(5, 6, 4, '2019-12-31', 7, 0, '0000-00-00', '0000-00-00');


CREATE TABLE `objectpics` (
  `id` int(11) NOT NULL,
  `type` int(11) NOT NULL,
  `ext` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `uploaddate` datetime NOT NULL,
  `objectid` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO `objectpics` (`id`, `type`, `ext`, `name`, `uploaddate`, `objectid`) VALUES
(1, 1, 'png', 'no-image2.png', '2019-06-11 20:27:13', 3),
(2, 1, 'png', 'no-image2.png', '2019-06-11 20:37:35', 4),
(3, 1, 'png', 'no-image.png', '2019-06-12 00:47:43', 5),
(4, 1, 'png', 'community.png', '2019-06-12 10:34:39', 6),
(7, 1, 'png', 'hashtag.png', '2019-06-12 21:54:50', 7),
(8, 1, 'png', 'stopWorking.png', '2019-06-13 02:34:52', 8);


CREATE TABLE `objects` (
  `beschreibung` text NOT NULL,
  `plz` varchar(255) NOT NULL,
  `ort` varchar(255) NOT NULL,
  `kommentar` varchar(255) NOT NULL,
  `lageplanid` int(11) NOT NULL,
  `bauplanid` int(11) NOT NULL,
  `baujahr` int(11) NOT NULL,
  `eigentuemerid` int(11) NOT NULL,
  `strasse` varchar(255) NOT NULL,
  `hausnr` int(11) NOT NULL,
  `gesamtflaeche` int(11) NOT NULL,
  `zusatz` varchar(255) NOT NULL,
  `id` int(11) NOT NULL,
  `verwalter` int(11) NOT NULL,
  `type` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO `objects` (`beschreibung`, `plz`, `ort`, `kommentar`, `lageplanid`, `bauplanid`, `baujahr`, `eigentuemerid`, `strasse`, `hausnr`, `gesamtflaeche`, `zusatz`, `id`, `verwalter`, `type`) VALUES
('qwdqwd qwdq dwq dqwdqwdqwd', '123123', 'qwdqwdqwdq', '', 0, 6, 1111, 1, 'qdwqdqdqwd', 23, 22, '', 8, 1, 'qwdqwdq');


CREATE TABLE `users` (
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `salt` varchar(255) NOT NULL,
  `vorname` varchar(255) NOT NULL,
  `nachname` varchar(255) NOT NULL,
  `adresse` varchar(255) NOT NULL,
  `plz` varchar(255) NOT NULL,
  `ort` varchar(255) NOT NULL,
  `gender` varchar(255) NOT NULL,
  `regdate` datetime NOT NULL,
  `roleid` int(11) NOT NULL,
  `company` varchar(255) NOT NULL,
  `approved` int(11) NOT NULL,
  `addition` varchar(255) NOT NULL,
  `objectinfo` varchar(255) NOT NULL,
  `telefon` varchar(255) NOT NULL,
  `hausnr` varchar(255) NOT NULL,
  `lastupdate` datetime NOT NULL,
  `id` int(11) NOT NULL,
  `einheitid` int(11) NOT NULL,
  `geburtsdatum` date NOT NULL,
  `einzugsdatum` date NOT NULL,
  `gekuendigt` int(11) NOT NULL,
  `kuendigungsdatum` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO `users` (`email`, `password`, `salt`, `vorname`, `nachname`, `adresse`, `plz`, `ort`, `gender`, `regdate`, `roleid`, `company`, `approved`, `addition`, `objectinfo`, `telefon`, `hausnr`, `lastupdate`, `id`, `einheitid`, `geburtsdatum`, `einzugsdatum`, `gekuendigt`, `kuendigungsdatum`) VALUES
('mohamed.sib@hotmail.de', '70b7afac3a10b1d4465693e4235ad43625c4f5fd685c117b8a01297fd145e2487b664bab520d9a8533f3a03fe5241c3331c7d87ceb5fb9f1be09c6757ad083fb', 'b4e7f531149b4ddc34a1eac88f4d60a9cd7bd215e662e2ac8b3a93e6c935c47d273a76f54035e08720a05bca676a585efa5b44b3248eea19ba510bec52bbaca7', 'Admin', 'Admin', 'Hallostrasse', '58512', 'Siegen', '', '2019-06-03 14:31:47', 2, '', 1, '', '', '', '81', '2019-06-03 14:34:27', 1, 0, '0000-00-00', '0000-00-00', 0, '0000-00-00'),
('m.siblani@werbeagentur-deknuydt.com', '4785d129c9b40f465a4dcb5f56aead92a00f51aa80df3bda069a37f046e9b496ddeef5de6866f18e0d374392be016cc6c88a2a5bd5e3957942b68cbd8a4e8007', '82eb5de7016904baeb8ab691bf6e7f79b363f6d00e256f8f805bbfeb5dc6d1b320acd6ea941756b278467cc8088f34daad1f876d95e7620717db8fa1e2560f01', 'dqwdqwd', 'qwdqwdqw', '', '', '', '', '2019-06-13 02:30:44', 4, '', 1, '', '', '123123123', '', '0000-00-00 00:00:00', 4, 0, '2019-06-13', '0000-00-00', 0, '0000-00-00'),
('qwdqwdqwd@wdqwd.de', '563cfd9cdaaf8988b300b0f76f9a90bf6f403025e066bfc83a9c09f0397bdeac1f9566a2e6c6d5da2ffddfc64eee83ec4efc378388c4bd4f728942741d00947d', 'd7e57e0289821cfac8182004b56aeb8d87061bac5b8dad99b571a06c82b44680dba831362f278e4346fc824fe7ee83a791ab6a8af2980bdf5bbfa028d381aa86', 'qdqwdqwd qw', 'qwdqwdq', '', '', '', '', '2019-06-13 02:35:53', 4, '', 1, '', '', '123091', '', '0000-00-00 00:00:00', 5, 0, '2019-06-12', '0000-00-00', 0, '0000-00-00'),
('qwdqwd@dwqwd.de', '1759ba8b051861c6054dd144d773824f634e1873328b25bd62cf7f0837db5a6767b315e88d1896bdb2bf80b39b70ac23f67920b134855e34068cde63e58282c5', 'f120303591132a10515fce72d842e01766ab5843e938a3becc23922635e516c7de5754d16ab680f3be41b6ce30ddcfa1502da9d6598340ecc7bd352f28d51228', 'qwdqwdqwd', 'qwdqwdq', '', '', '', '', '2019-06-13 02:37:18', 4, '', 1, '', '', '0921380123', '', '0000-00-00 00:00:00', 6, 0, '2019-06-20', '0000-00-00', 0, '0000-00-00');


CREATE TABLE `vdocuments` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `ext` varchar(255) NOT NULL,
  `type` int(11) NOT NULL,
  `verwaltungseinheitid` int(11) NOT NULL,
  `uploaddate` date NOT NULL,
  `fromid` int(11) NOT NULL,
  `toid` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `vdocuments` (`id`, `name`, `ext`, `type`, `verwaltungseinheitid`, `uploaddate`, `fromid`, `toid`) VALUES
(1, 'likebutt.png', 'png', 2, 0, '2019-06-13', 0, 0),
(2, 'likebutt.png', 'png', 2, 0, '2019-06-13', 0, 0),
(3, 'likebutt.png', 'png', 2, 1, '2019-06-13', 0, 0),
(4, 'stopWorking.png', 'png', 1, 1, '2019-06-13', 0, 0),
(5, 'stopWorking.png', 'png', 1, 1, '2019-06-13', 0, 0),
(6, 'hashtag.png', 'png', 1, 4, '2019-06-13', 0, 0),
(7, 'bg-main.jpg', 'jpg', 1, 4, '2019-06-13', 0, 0);


CREATE TABLE `verwaltungseinheit` (
  `usermanagement` varchar(255) NOT NULL,
  `beschreibung` text NOT NULL,
  `plz` varchar(255) NOT NULL,
  `ort` varchar(255) NOT NULL,
  `adresse` varchar(255) NOT NULL,
  `kommentar` varchar(255) NOT NULL,
  `lageplanid` int(11) NOT NULL,
  `bauplanid` int(11) NOT NULL,
  `wohnflaechen` varchar(255) NOT NULL,
  `kaltmiete` int(11) NOT NULL,
  `monatlichenebenkosten` int(11) NOT NULL,
  `standvirtuelleskonto` int(11) NOT NULL,
  `standmietkautionskonto` int(11) NOT NULL,
  `rauchmelderwartungspflichtbeimmieter` tinyint(1) NOT NULL,
  `objectid` int(11) NOT NULL,
  `eigentuemerid` int(11) NOT NULL,
  `verwalterid` int(11) NOT NULL,
  `vermieterid` int(11) NOT NULL,
  `type` varchar(255) NOT NULL,
  `id` int(11) NOT NULL,
  `vert_schlüsselA` int(11) NOT NULL,
  `vert_schlüsselB` int(11) NOT NULL,
  `vert_schlüsselC` int(11) NOT NULL,
  `vert_schlüsselD` int(11) NOT NULL

) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `verwaltungseinheit` (`usermanagement`, `beschreibung`, `plz`, `ort`, `adresse`, `kommentar`, `lageplanid`, `bauplanid`, `wohnflaechen`, `kaltmiete`, `monatlichenebenkosten`, `standvirtuelleskonto`, `standmietkautionskonto`, `rauchmelderwartungspflichtbeimmieter`, `objectid`, `eigentuemerid`, `verwalterid`, `vermieterid`, `type`, `id`) VALUES
('', 'qwdqwdqwdqwd', '', '', '', '', 3, 0, '222', 0, 0, 0, 0, 0, 7, 1, 0, 2, 'dqwdqwdqdw', 1),
('', 'qwdqwdqwdqwd', '', '', '', '', 0, 0, '211', 0, 0, 0, 0, 0, 7, 1, 0, 1, 'dqdwqwd', 2),
('', 'qdwqwdqwdqwdqwd', '', '', '', '', 0, 0, '22', 0, 0, 0, 0, 0, 6, 1, 1, 1, 'qdwqwdqwd', 3),
('', 'wqdqwd qdw qwdqwdqwd', '', '', '', '', 0, 0, '222', 0, 0, 0, 0, 0, 8, 1, 1, 1, 'qwdqwdqwdqdw', 4);

ALTER TABLE `documents`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `mieterverwaltung`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `objectpics`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `objects`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `vdocuments`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `verwaltungseinheit`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `documents`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

ALTER TABLE `mieterverwaltung`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

ALTER TABLE `objectpics`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

ALTER TABLE `objects`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

ALTER TABLE `vdocuments`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

ALTER TABLE `verwaltungseinheit`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
