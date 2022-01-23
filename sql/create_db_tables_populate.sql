CREATE TABLE manufacturers (
    id SERIAL PRIMARY KEY,
    name VARCHAR,
    nationality VARCHAR
);

CREATE TABLE beers (
    id SERIAL PRIMARY KEY,
    name VARCHAR,
    type VARCHAR,
    description VARCHAR,
    abv NUMERIC,
    manufacturer_id INTEGER REFERENCES manufacturers(id)
);

CREATE TABLE providers (
    id SERIAL PRIMARY KEY,
    email VARCHAR UNIQUE NOT NULL,
    pwd VARCHAR NOT NULL,
    role VARCHAR NOT NULL,
    manufacturer_id INTEGER,
    FOREIGN KEY (manufacturer_id) REFERENCES manufacturers(id)
);

CREATE TABLE authorities (
    id SERIAL PRIMARY KEY,
    provider_id INTEGER NOT NULL,
    name VARCHAR NOT NULL,
    FOREIGN KEY (provider_id) REFERENCES providers(id)
);

INSERT INTO manufacturers (name, nationality)
VALUES 
    ('Anheuser-Busch', 'United States'),
    ('Victory Brewing Company', 'United States'),
    ('Tröegs Brewing Company', 'United States'),
    ('Lagunitas Brewing Company', 'United States'),
    ('Yuengling Brewery', 'United States'),
    ('Paulaner Brauerei', 'Germany'),
    ('Spoetzl Brewery', 'United States'),
    ('Cervecería Cuauhtémoc Moctezuma', 'Mexico'),
    ('Boston Beer Company', 'United States'), 
    ('Capital Brewery', 'United States'),
    ('Brooklyn Brewery', 'United States'),
    ('Heineken Nederland B.V.', 'Netherlands'),
    ('Sharp''s Brewery', 'United Kingdom'),
    ('Black Sheep Brewery', 'United Kingdom');

INSERT INTO beers (name, type, description, abv, manufacturer_id)
VALUES 
    ('Budweiser', 'American Lager', 'Brewed using a blend of imported and classic American aroma hops, and a blend of barley malts and rice. Budweiser is brewed with time-honored methods including “kraeusening” for natural carbonation and Beechwood aging, which results in unparalleled balance and character.', 5.0, 1),
    ('Victory Helles Lager', 'German Helles', 'Perfectly balanced and eminently drinkable, this Helles-style lager has been expertly crafted with all-German noble hops, malts and yeast.', 4.8, 2),
    ('Sunshine Pils', 'German Pilsner', 'Like the rising sun, Sunshine Pils delivers winter, spring, summer and fall. This deceptive complex pilsner is all about the tightrope walk of two-row barley, zesty Saaz hops and lager yeast. It''s a go-to when the sun is shining, and it makes us happy when skies are gray.', 4.5, 3),
    ('Lagunitas PILS', 'Czech Pilsner', 'Our only Lager, brewed with loads of imported Saaz hops and a bottom-fermenting yeast strain that leaves it Light and Crisp and Easy to Slam, yet full of real flavor and all the things you yearn for.', 6.2, 4),
    ('Yuengling Lager', 'Amber American Lager', 'Famous for its rich amber color and medium-bodied flavor with roasted caramel malt for a subtle sweetness and a combination of Cluster and Cascade hops, this true original delivers a well-balanced taste with very distinct character.', 4.5, 5),
    ('Oktoberfest Märzen', 'Oktoberfest', 'An amber beer style that was developed to celebrate the original Oktoberfest over 200 years ago. This full bodied beer with its rich malt flavor, dark toffee note and underlying fruitiness has a masterful hop balance.', 5.8, 6),
    ('Shiner Bohemian Black Lager', 'German Schwarzbier', 'Germans call it a schwarzbier (black beer), but Shiner fans know this one as Shiner 97. Unusually smooth for a dark beer, our Bohemian black lager is brewed with roasted malts and Czech Saaz and Styrian hops. All else pales in comparison.', 4.9, 7),
    ('Dos Equis Ambar', 'Vienna Lager', 'Dos Equis Ambar (Amber) is a vienna style lager that was originally brewed by the German-born Mexican brewer Wilhelm Hasse in 1897.', 4.7, 8),
    ('Samuel Adams Winter Lager', 'Traditional Bock', 'For the first time in 31 years, we’re updating the classic Winter Lager recipe to make it crisper and brighter. It’s the same iconic beer, with a wintery remix. Crisp Bock with citrus and spices, lager brewed with orange peel, cinnamon, and ginger.', 5.6, 9),
    ('Troegenator', 'Doppelbock', 'Monks had fasting figured out. No food? No problem. Just drink a Double Bock. Thick and chewy with intense notes of caramel, chocolate and dried stone fruit, ‘Nator (as we call him) serves as a tribute to this liquid bread style.', 8.2, 3),
    ('Moonglow Weizenbock', 'Weizenbock', 'This richly flavored, dark amber wheat beer features fruity and spicy aromas galore. Significant strength underlies the pleasant citric appeal of this bock beer. Brewed with over 50% malted wheat, this is a traditional Bavarian weizenbock. Full of the flavors of harvest fruit, this is the perfect Autumnal elixir.', 8.7, 2),
    ('Capital Maibock', 'Maibock', 'Our spring seasonal is a deep golden lager with a flavorful but smooth presence. When you see our Maibock hit the shelves you know things are about to get better… including the weather!', 6.2, 10),
    ('Brooklyn Brown Ale', 'American Brown Ale', 'This is the award-winning original American brown ale, first brewed as a holiday specialty, and now one of our most popular beers year-round. Northern English brown ales tend to be strong and dry, while southern English brown ales are milder and sweeter.', 5.6, 11),
    ('Newcastle Brown Ale', 'English Brown Ale', 'Newcastle Brown Ale (4.7% ABV) is full-bodied and smooth, showing restrained caramel and notes of bananas and dried fruit.', 4.7, 12),
    ('Doom Bar', 'English Bitter', 'Doom Bar is inspired by its namesake, the treacherous sandbank at the mouth of the Camel Estuary near Sharps home at Rock. Accomplished and precise, Doom Bar is the epitomy of consistency, balance and moreish appeal and is now the UK''s no.1 selling cask beer.', 4.3, 13),
    ('Black Sheep Ale', 'English Pale Ale', 'Black Sheep Ale is a full flavoured premium bitter with a rich, fruity aroma. Brewed using generous handfuls of choice Golding hops, it has a perfectly balanced bittersweet, malty taste with a long, dry and bitter finish.', 4.4, 14),
    ('Hefe-Weissbier Naturtrüb', 'Hefeweizen', 'Cloudy in appearance, it presents itself in the glass with a brilliant velvety golden color, under a robust crown of foam that truly deserves this name.', 5.5, 6);


INSERT INTO providers (email, pwd, role, manufacturer_id)
VALUES
    ('admin@admin.com', '$2a$12$LvGIZQcdC8puHqoWj2d1Q.f8OeHqYOjYM98e/KtYF9gU/qORCdhWS', 'Admin', null),
    ('victory@provider.com', '$2a$12$tu/q0PZskDF9kk.KRPnys.aQcnqU6JdLPOp4bOjIgfwK07N4nSDRC', 'Manufacturer', 2),
    ('tröegs@provider.com', '$2a$12$tu/q0PZskDF9kk.KRPnys.aQcnqU6JdLPOp4bOjIgfwK07N4nSDRC', 'Manufacturer', 3),
    ('paulaner@provider.com', '$2a$12$tu/q0PZskDF9kk.KRPnys.aQcnqU6JdLPOp4bOjIgfwK07N4nSDRC', 'Manufacturer', 6);


INSERT INTO authorities (provider_id, name)
VALUES 
    (1, 'ROLE_ADMIN'),
    (2, 'ROLE_MANUFACTURER'),
    (3, 'ROLE_MANUFACTURER'),
    (4, 'ROLE_MANUFACTURER');
