INSERT INTO roles (authority)
VALUES ('ROLE_SUBSCRIBER'),
       ('ROLE_JOURNALIST'),
       ('ROLE_ADMIN');
SELECT setval('roles_id_seq', (SELECT MAX(id) FROM roles));

INSERT INTO users (username, password)
VALUES ('john_doe', '$2a$10$E9d8Cnjks7mmbh3fQHHUMOWiRYTPJ1SyMI4FdpAFgfQDt.qgJx6Hq'),
       ('jane_smith', '$2a$10$E9d8Cnjks7mmbh3fQHHUMOWiRYTPJ1SyMI4FdpAFgfQDt.qgJx6Hq'),
       ('bob_johnson', '$2a$10$E9d8Cnjks7mmbh3fQHHUMOWiRYTPJ1SyMI4FdpAFgfQDt.qgJx6Hq'),
       ('amy_wong', '$2a$10$E9d8Cnjks7mmbh3fQHHUMOWiRYTPJ1SyMI4FdpAFgfQDt.qgJx6Hq'),
       ('david_chen', '$2a$10$E9d8Cnjks7mmbh3fQHHUMOWiRYTPJ1SyMI4FdpAFgfQDt.qgJx6Hq'),
       ('emily_kim', '$2a$10$E9d8Cnjks7mmbh3fQHHUMOWiRYTPJ1SyMI4FdpAFgfQDt.qgJx6Hq'),
       ('george_baker', '$2a$10$E9d8Cnjks7mmbh3fQHHUMOWiRYTPJ1SyMI4FdpAFgfQDt.qgJx6Hq'),
       ('hannah_miller', '$2a$10$E9d8Cnjks7mmbh3fQHHUMOWiRYTPJ1SyMI4FdpAFgfQDt.qgJx6Hq'),
       ('isaac_nguyen', '$2a$10$E9d8Cnjks7mmbh3fQHHUMOWiRYTPJ1SyMI4FdpAFgfQDt.qgJx6Hq'),
       ('jessica_brown', '$2a$10$E9d8Cnjks7mmbh3fQHHUMOWiRYTPJ1SyMI4FdpAFgfQDt.qgJx6Hq'),
       ('kevin_lee', '$2a$10$E9d8Cnjks7mmbh3fQHHUMOWiRYTPJ1SyMI4FdpAFgfQDt.qgJx6Hq'),
       ('lisa_park', '$2a$10$E9d8Cnjks7mmbh3fQHHUMOWiRYTPJ1SyMI4FdpAFgfQDt.qgJx6Hq'),
       ('matt_taylor', '$2a$10$E9d8Cnjks7mmbh3fQHHUMOWiRYTPJ1SyMI4FdpAFgfQDt.qgJx6Hq'),
       ('nancy_cho', '$2a$10$E9d8Cnjks7mmbh3fQHHUMOWiRYTPJ1SyMI4FdpAFgfQDt.qgJx6Hq'),
       ('oliver_hernandez', '$2a$10$E9d8Cnjks7mmbh3fQHHUMOWiRYTPJ1SyMI4FdpAFgfQDt.qgJx6Hq'),
       ('peter_ng', '$2a$10$E9d8Cnjks7mmbh3fQHHUMOWiRYTPJ1SyMI4FdpAFgfQDt.qgJx6Hq'),
       ('quincy_smith', '$2a$10$E9d8Cnjks7mmbh3fQHHUMOWiRYTPJ1SyMI4FdpAFgfQDt.qgJx6Hq'),
       ('rachel_kim', '$2a$10$E9d8Cnjks7mmbh3fQHHUMOWiRYTPJ1SyMI4FdpAFgfQDt.qgJx6Hq'),
       ('steven_chen', '$2a$10$E9d8Cnjks7mmbh3fQHHUMOWiRYTPJ1SyMI4FdpAFgfQDt.qgJx6Hq'),
       ('tiffany_wong', '$2a$10$E9d8Cnjks7mmbh3fQHHUMOWiRYTPJ1SyMI4FdpAFgfQDt.qgJx6Hq');
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));

INSERT INTO users_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
         JOIN roles r ON r.authority IN ('ROLE_SUBSCRIBER', 'ROLE_JOURNALIST', 'ROLE_ADMIN')
WHERE (u.id, r.id) IN
      ((1, 1), (2, 1), (3, 2), (4, 2), (5, 2),
       (6, 3), (7, 3), (8, 3), (9, 1), (10, 1),
       (11, 2), (12, 2), (13, 2),(14, 1), (15, 1),
       (16, 3), (17, 3), (18, 3), (19, 2), (20, 2));

