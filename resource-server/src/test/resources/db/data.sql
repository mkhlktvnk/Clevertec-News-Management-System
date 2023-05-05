INSERT INTO news (username, title, text)
VALUES ('user1', 'New study shows benefits of regular exercise',
        'A new study published in the Journal of Health and Fitness suggests that regular exercise can significantly improve overall health and well-being.');
SELECT setval('news_id_seq', (SELECT MAX(id) FROM news));
INSERT INTO news (username, title, text)
VALUES ('user2', 'Experts predict surge in renewable energy investment',
        'Leading energy experts are predicting a significant increase in investment in renewable energy projects over the next decade.');
SELECT setval('news_id_seq', (SELECT MAX(id) FROM news));
INSERT INTO news (username, title, text)
VALUES ('user3', 'Global pandemic highlights importance of digital connectivity',
        'The COVID-19 pandemic has emphasized the crucial role of digital connectivity in keeping people connected and businesses running.');
SELECT setval('news_id_seq', (SELECT MAX(id) FROM news));
INSERT INTO news (username, title, text)
VALUES ('user4', 'New artificial intelligence technology promises to revolutionize healthcare',
        'A new AI-based diagnostic tool developed by a team of researchers could dramatically improve the accuracy and speed of disease diagnosis.');
SELECT setval('news_id_seq', (SELECT MAX(id) FROM news));
INSERT INTO news (username, title, text)
VALUES ('user5', 'Scientists discover new species of dinosaur',
        'Paleontologists have unearthed fossils of a previously unknown species of dinosaur that roamed the earth millions of years ago.');
SELECT setval('news_id_seq', (SELECT MAX(id) FROM news));
INSERT INTO news (username, title, text)
VALUES ('user6', 'New research reveals potential risks of artificial sweeteners',
        'A recent study has found that consuming large quantities of artificial sweeteners could have negative health effects, including an increased risk of type 2 diabetes.');
SELECT setval('news_id_seq', (SELECT MAX(id) FROM news));
INSERT INTO news (username, title, text)
VALUES ('user7', 'Experts warn of potential food shortages due to climate change',
        'A report by leading climate scientists warns that global warming could have devastating impacts on agriculture, potentially leading to widespread food shortages in the coming decades.');
SELECT setval('news_id_seq', (SELECT MAX(id) FROM news));
INSERT INTO news (username, title, text)
VALUES ('user8', 'New study suggests link between air pollution and cognitive decline',
        'A study conducted by researchers at a leading university has found a correlation between exposure to air pollution and a decline in cognitive function, particularly in older adults.');
SELECT setval('news_id_seq', (SELECT MAX(id) FROM news));
INSERT INTO news (username, title, text)
VALUES ('user9', 'NASA announces plans for manned mission to Mars',
        'NASA has unveiled plans for a manned mission to Mars, with a target date set for the mid-2030s. The mission aims to explore the Red Planet and search for signs of extraterrestrial life.');
SELECT setval('news_id_seq', (SELECT MAX(id) FROM news));
INSERT INTO news (username, title, text)
VALUES ('user10', 'New breakthrough in quantum computing could revolutionize cryptography',
        'Scientists at a leading research institution have made a major breakthrough in quantum computing that could pave the way for new and more secure forms of cryptography.');
SELECT setval('news_id_seq', (SELECT MAX(id) FROM news));
INSERT INTO news (username, title, text)
VALUES ('user11', 'Global efforts to reduce plastic waste gain momentum',
        'Governments and organizations around the world are ramping up efforts to reduce plastic waste and promote recycling, in response to growing concerns about the impact of plastics on the environment.');
SELECT setval('news_id_seq', (SELECT MAX(id) FROM news));
INSERT INTO news (username, title, text)
VALUES ('user12', 'New study sheds light on causes of depression',
        'A new study has uncovered a possible link between inflammation and depression, providing new insights into the biological mechanisms that underlie the condition.');
SELECT setval('news_id_seq', (SELECT MAX(id) FROM news));

INSERT INTO news (username, title, text)
VALUES ('user1', 'New study shows benefits of regular exercise',
        'A new study published in the Journal of Health and Fitness suggests that regular exercise can significantly improve overall health and well-being.');

INSERT INTO comments (username, text, news_id)
VALUES ('user1', 'I totally agree with this study!', 1);
SELECT setval('comments_id_seq', (SELECT MAX(id) FROM comments));

INSERT INTO comments (username, text, news_id)
VALUES ('user2', 'I am hopeful for the future of renewable energy!', 2);
SELECT setval('comments_id_seq', (SELECT MAX(id) FROM comments));

INSERT INTO comments (username, text, news_id)
VALUES ('user3', 'Digital connectivity has been a lifesaver during the pandemic.', 3);
SELECT setval('comments_id_seq', (SELECT MAX(id) FROM comments));

INSERT INTO comments (username, text, news_id)
VALUES ('user4', 'AI technology is fascinating, but I hope it doesnt replace human doctors.', 4);
SELECT setval('comments_id_seq', (SELECT MAX(id) FROM comments));

INSERT INTO comments (username, text, news_id)
VALUES ('user5', 'Its amazing how much we can learn from fossils.', 5);
SELECT setval('comments_id_seq', (SELECT MAX(id) FROM comments));

INSERT INTO comments (username, text, news_id)
VALUES ('user6', 'Ive always been skeptical of artificial sweeteners.', 6);
SELECT setval('comments_id_seq', (SELECT MAX(id) FROM comments));

INSERT INTO comments (username, text, news_id)
VALUES ('user7', 'This study provides important information about the risks of artificial sweeteners.', 6);
SELECT setval('comments_id_seq', (SELECT MAX(id) FROM comments));

INSERT INTO comments (username, text, news_id)
VALUES ('user8', 'I think we need to prioritize mental health as much as physical health.', 7);
SELECT setval('comments_id_seq', (SELECT MAX(id) FROM comments));

INSERT INTO comments (username, text, news_id)
VALUES ('user9', 'This pandemic has shown us just how important it is to have a strong healthcare system.', 8);
SELECT setval('comments_id_seq', (SELECT MAX(id) FROM comments));

INSERT INTO comments (username, text, news_id)
VALUES ('user10', 'I hope this new technology can improve access to healthcare for everyone.', 4);
SELECT setval('comments_id_seq', (SELECT MAX(id) FROM comments));

INSERT INTO comments (username, text, news_id)
VALUES ('user11', 'I am excited to see what new discoveries paleontologists will make in the future.', 5);
SELECT setval('comments_id_seq', (SELECT MAX(id) FROM comments));

INSERT INTO comments (username, text, news_id)
VALUES ('user12', 'We should all do our part to take care of the environment.', 2);
SELECT setval('comments_id_seq', (SELECT MAX(id) FROM comments));
