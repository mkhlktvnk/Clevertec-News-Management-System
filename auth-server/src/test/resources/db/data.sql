INSERT INTO roles (authority)
VALUES ('ROLE_SUBSCRIBER'),
       ('ROLE_JOURNALIST'),
       ('ROLE_ADMIN');
SELECT setval('roles_id_seq', (SELECT MAX(id) FROM roles));

INSERT INTO users (username, password)
VALUES ('john_doe', 'password123'),
       ('jane_smith', 'password456'),
       ('bob_johnson', 'password789'),
       ('amy_wong', 'password012'),
       ('david_chen', 'password345'),
       ('emily_kim', 'password678'),
       ('george_baker', 'password901'),
       ('hannah_miller', 'password234'),
       ('isaac_nguyen', 'password567'),
       ('jessica_brown', 'password890'),
       ('kevin_lee', 'password123'),
       ('lisa_park', 'password456'),
       ('matt_taylor', 'password789'),
       ('nancy_cho', 'password012'),
       ('oliver_hernandez', 'password345'),
       ('peter_ng', 'password678'),
       ('quincy_smith', 'password901'),
       ('rachel_kim', 'password234'),
       ('steven_chen', 'password567'),
       ('tiffany_wong', 'password890');
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

