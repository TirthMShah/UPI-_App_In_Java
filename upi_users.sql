-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Oct 10, 2023 at 09:02 AM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `upi users`
--

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `upiId` varchar(50) NOT NULL,
  `name` varchar(50) NOT NULL,
  `mobileNumber` bigint(20) NOT NULL,
  `balance` double NOT NULL,
  `mail_Id` varchar(100) NOT NULL,
  `bank` varchar(20) NOT NULL,
  `password1` varchar(4) NOT NULL,
  `password2` varchar(6) NOT NULL,
  `Notifications` varchar(1000) DEFAULT NULL,
  `wallet` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`upiId`, `name`, `mobileNumber`, `balance`, `mail_Id`, `bank`, `password1`, `password2`, `Notifications`, `wallet`) VALUES
('broadbandCompany@oksbi', 'Broadband Company', 0, 200, 'broadbandCompany@gmail.com', 'sbi', '1234', '123456', '100.0 Recieved From hetavi@oksbi(Hetavi Shah).', NULL),
('DTHCompany@oksbi', 'DTH Comany', 0, 100, 'DTHCompany@gmail.com', 'sbi', '1234', '123456', '100.0 Recieved From dharmesh.shah79@okSBI(Dharmesh Shah)', NULL),
('dumbo@okhdfc', 'Dumbo', 1212343456, 2876, 'dumbo@gmail.com', 'hdfc', '1234', '123456', '100.0 Recieved From krashna@oksbi(Krashna Mehta)', NULL),
('electricityCompany@oksbi', 'Electricity Company', 0, 100, 'electricityCompany@gmail.com', 'sbi', '1234', '123456', '100.0 Recieved From dharmesh.shah79@okSBI(Dharmesh Shah)', NULL),
('fasTagCompany@oksbi', 'FasTag Company', 0, 100, 'fasTagCompany@gmail.com', 'sbi', '1234', '123456', '100.0 Recieved From dharmesh@okhdfc(Dharmesh Shah)', NULL),
('hetavi.shah05@oksbi', 'Hetavi Shah', 7990717818, 8856, 'hetavi.shah05@gmail.com', 'sbi', '1234', '123456', NULL, 10000),
('krashna@oksbi', 'Krashna Mehta', 8987878785, 9900, 'krashna@gmail.com', 'sbi', '1234', '123456', NULL, 1000),
('krishna@oksbi', 'Krishna', 9865475474, 100000, 'krishna@gmail.com', 'sbi', '1234', '123456', NULL, 10000),
('mobileCompany@oksbi', 'Mobile Recharge Company', 0, 300, 'mobileCompany@gmail.com', 'sbi', '1234', '123456', '100.0 Recieved From dharmesh@okhdfc(Dharmesh Shah)', NULL),
('waterCompany@oksbi', 'Water Company', 0, 100, 'waterCompany@gmail.com', 'sbi', '1234', '123456', '100.0 Recieved From dharmesh.shah79@okSBI(Dharmesh Shah)', NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`upiId`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
