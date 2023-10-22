INSERT INTO users(id, login, email, password) VALUES(1, 'Den', 'den_mogilev@yopmail.com', '$2a$10$fAhhMrd4fKSawqNjJGse5ej/.zwAIFnsDv4JVLrchTWtBmPFliuJG'), (2, 'Peter', 'peter_mogilev@yopmail.com', '$2a$10$Jz/fkQ8zhUxIa0ANiDQJzuAwijen4gUPWN72Bv0QjWgP.c8B9c2Ue'), (3, 'Asya', 'asya_mogilev@yopmail.com','$2a$10$9IDpH95MdvX0oxFWM92w1.qRpzoaNNunEqq5jD2ovXcps9IPCYA1G'), (4, 'Jimmy', 'jimmy_mogilev@yopmail.com', '$2a$10$BtSb1vIX7synMgLIKximAeNZlpTAae4kZlZMR9xx7wxXZK2s0B4dC');
SELECT setval('user_id_seq', (SELECT MAX(id) from users));

INSERT INTO goods(id, title, price, user_id) VALUES (1, 'Book', 5.5, 1), (2, 'Phone', 100, 2), (3, 'Juice', 2, 4), (4, 'Phone', 200, 2), (5, 'Beer', 1.5, 1), (6, 'Computer', 500, 2), (7, 'Wisky', 4.2, 2);

INSERT INTO orders(id, user_id, total_price) VALUES (1, 2, 75), (2, 3, 134.56), (3, 1, 54), (4, 1, 175), (5, 2, 25), (6, 3, 63), (7, 1, 88);
SELECT setval('order_id_seq', (SELECT MAX(id) from orders));




