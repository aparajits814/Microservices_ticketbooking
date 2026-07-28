DELETE FROM theatres;
DELETE FROM screens;
DELETE FROM seats;


INSERT INTO theatres (theatre_id, theatre_name, location, theatre_type) VALUES
('TH001', 'PVR Phoenix Mall', 'Bengaluru', 'M'),
('TH002', 'INOX Orion Mall', 'Bengaluru', 'M'),
('TH003', 'Cinepolis Nexus', 'Hyderabad', 'M'),
('TH004', 'Miraj Cinemas', 'Pune', 'S'),
('TH005', 'Carnival Cinemas', 'Nagpur', 'S');


INSERT INTO screens (screen_id, theatre_id, screen_name, screen_status) VALUES
-- TH001
('SC001', 'TH001', 'Screen 1', 'A'),
('SC002', 'TH001', 'Screen 2', 'A'),

-- TH002
('SC003', 'TH002', 'Screen 1', 'A'),
('SC004', 'TH002', 'Screen 2', 'A'),

-- TH003
('SC005', 'TH003', 'Screen 1', 'A'),
('SC006', 'TH003', 'Screen 2', 'A'),

-- TH004
('SC007', 'TH004', 'Screen 1', 'A'),
('SC008', 'TH004', 'Screen 2', 'A'),

-- TH005
('SC009', 'TH005', 'Screen 1', 'A'),
('SC010', 'TH005', 'Screen 2', 'A');


-- ===========================
-- SEATS (5 seats per screen)
-- Seat Types:
-- P = Premium
-- G = Gold
-- S = Silver
-- ===========================

INSERT INTO seats (seat_id, screen_id, seat_type) VALUES

-- SC001
('SC001-A1','SC001','P'),
('SC001-A2','SC001','P'),
('SC001-B1','SC001','G'),
('SC001-B2','SC001','G'),
('SC001-C1','SC001','S'),

-- SC002
('SC002-A1','SC002','P'),
('SC002-A2','SC002','P'),
('SC002-B1','SC002','G'),
('SC002-B2','SC002','G'),
('SC002-C1','SC002','S'),

-- SC003
('SC003-A1','SC003','P'),
('SC003-A2','SC003','P'),
('SC003-B1','SC003','G'),
('SC003-B2','SC003','G'),
('SC003-C1','SC003','S'),

-- SC004
('SC004-A1','SC004','P'),
('SC004-A2','SC004','P'),
('SC004-B1','SC004','G'),
('SC004-B2','SC004','G'),
('SC004-C1','SC004','S'),

-- SC005
('SC005-A1','SC005','P'),
('SC005-A2','SC005','P'),
('SC005-B1','SC005','G'),
('SC005-B2','SC005','G'),
('SC005-C1','SC005','S'),

-- SC006
('SC006-A1','SC006','P'),
('SC006-A2','SC006','P'),
('SC006-B1','SC006','G'),
('SC006-B2','SC006','G'),
('SC006-C1','SC006','S'),

-- SC007
('SC007-A1','SC007','P'),
('SC007-A2','SC007','P'),
('SC007-B1','SC007','G'),
('SC007-B2','SC007','G'),
('SC007-C1','SC007','S'),

-- SC008
('SC008-A1','SC008','P'),
('SC008-A2','SC008','P'),
('SC008-B1','SC008','G'),
('SC008-B2','SC008','G'),
('SC008-C1','SC008','S'),

-- SC009
('SC009-A1','SC009','P'),
('SC009-A2','SC009','P'),
('SC009-B1','SC009','G'),
('SC009-B2','SC009','G'),
('SC009-C1','SC009','S'),

-- SC010
('SC010-A1','SC010','P'),
('SC010-A2','SC010','P'),
('SC010-B1','SC010','G'),
('SC010-B2','SC010','G'),
('SC010-C1','SC010','S');