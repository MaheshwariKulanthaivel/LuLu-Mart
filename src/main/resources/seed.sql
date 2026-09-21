-- ============================================================
-- LULU MART - Seed Data
-- Pet-only multi-seller marketplace
--
-- NOTE: password_hash columns use the marker 'CHANGE_ME'.
-- On first application startup the AppListener replaces these
-- with real BCrypt hashes:
--   ADMIN  -> Admin@123
--   SELLER -> Seller@123
--   BUYER  -> Buyer@123
-- ============================================================

-- ------------------------------------------------------------
-- USERS  (1 admin, 12 sellers, 5 buyers)
-- ------------------------------------------------------------
INSERT INTO users (id, name, email, password_hash, role, created_at) VALUES
 (1,  'Lulu Admin',        'admin@lulumart.com',     'CHANGE_ME', 'ADMIN',  CURRENT_TIMESTAMP),
 (2,  'Lulu Pet Store',    'seller@lulumart.com',    'CHANGE_ME', 'SELLER', CURRENT_TIMESTAMP),
 (3,  'PawPerfect Store',  'seller2@lulumart.com',   'CHANGE_ME', 'SELLER', CURRENT_TIMESTAMP),
 (4,  'Whisker World',     'seller3@lulumart.com',   'CHANGE_ME', 'SELLER', CURRENT_TIMESTAMP),
 (5,  'Feather Friends Co.','seller4@lulumart.com',  'CHANGE_ME', 'SELLER', CURRENT_TIMESTAMP),
 (6,  'Burrow Buddies',    'seller5@lulumart.com',   'CHANGE_ME', 'SELLER', CURRENT_TIMESTAMP),
 (7,  'Cage & Critter Co.', 'seller6@lulumart.com',  'CHANGE_ME', 'SELLER', CURRENT_TIMESTAMP),
 (8,  'Fin & Scale Aquatics','seller7@lulumart.com', 'CHANGE_ME', 'SELLER', CURRENT_TIMESTAMP),
 (9,  'Shell & Slide Pets', 'seller8@lulumart.com',  'CHANGE_ME', 'SELLER', CURRENT_TIMESTAMP),
 (10, 'Pawsitive Vibes',    'seller9@lulumart.com',  'CHANGE_ME', 'SELLER', CURRENT_TIMESTAMP),
 (11, 'Cuddle Crates',      'seller10@lulumart.com', 'CHANGE_ME', 'SELLER', CURRENT_TIMESTAMP),
 (12, 'Nature Nibbles',     'seller11@lulumart.com', 'CHANGE_ME', 'SELLER', CURRENT_TIMESTAMP),
 (13, 'Healthy Harvest Pets','seller12@lulumart.com','CHANGE_ME', 'SELLER', CURRENT_TIMESTAMP),
 (14, 'Riya Sharma',        'buyer@lulumart.com',    'CHANGE_ME', 'BUYER',  CURRENT_TIMESTAMP),
 (15, 'Aarav Mehta',        'buyer2@lulumart.com',   'CHANGE_ME', 'BUYER',  CURRENT_TIMESTAMP),
 (16, 'Sneha Patil',        'buyer3@lulumart.com',   'CHANGE_ME', 'BUYER',  CURRENT_TIMESTAMP),
 (17, 'Kabir Singh',        'buyer4@lulumart.com',   'CHANGE_ME', 'BUYER',  CURRENT_TIMESTAMP),
 (18, 'Ananya Iyer',        'buyer5@lulumart.com',   'CHANGE_ME', 'BUYER',  CURRENT_TIMESTAMP);

-- ------------------------------------------------------------
-- PRODUCTS  (70 pet products)
-- ------------------------------------------------------------
INSERT INTO products (id, seller_id, pet_type, name, description, price, stock_qty, category, image_url, created_at) VALUES
-- DOGS
(1, 2, 'Dogs', 'Premium Adult Dog Food – Chicken & Rice (10kg)', 'Complete and balanced adult dog food with real chicken and wholesome rice. Supports healthy digestion, shiny coat and strong immunity for everyday energy.', 2450.00, 40, 'Food', 'https://loremflickr.com/640/640/dog,food?lock=1', '2026-08-02 10:00:00'),
(2, 13, 'Dogs', 'Puppy Growth Chicken Food (3kg)', 'Specially formulated puppy food rich in DHA, calcium and protein to support healthy growth and brain development in growing puppies.', 899.00, 30, 'Food', 'https://loremflickr.com/640/640/dog,puppy?lock=2', '2026-08-15 10:00:00'),
(3, 2, 'Dogs', 'Dental Dog Treats – Mint Flavor', 'Crunchy dental treats that help reduce plaque and tartar while freshening your dog''s breath. Delicious mint flavor dogs love.', 249.00, 60, 'Treats', 'https://loremflickr.com/640/640/dog,treat?lock=3', '2026-08-05 10:00:00'),
(4, 2, 'Dogs', 'Indestructible Rubber Bone Chew Toy', 'Durable non-toxic rubber bone designed for aggressive chewers. Soothing texture massages gums and cleans teeth while playing.', 199.00, 25, 'Toys', 'https://loremflickr.com/640/640/dog,toy?lock=4', '2026-07-20 10:00:00'),
(5, 2, 'Dogs', 'Heavy-Duty Rope Tug Toy', 'Braided cotton rope tug toy, great for fetch, tug-of-war and dental flossing. Keeps your dog active and entertained.', 229.00, 28, 'Toys', 'https://loremflickr.com/640/640/dog,toy?lock=5', '2026-07-25 10:00:00'),
(6, 3, 'Dogs', 'Nylon Adjustable Dog Collar', 'Lightweight yet strong nylon collar with quick-release buckle and adjustable fit for everyday comfort and control.', 349.00, 35, 'Accessories', 'https://loremflickr.com/640/640/dog,collar?lock=6', '2026-08-10 10:00:00'),
(7, 3, 'Dogs', 'No-Pull Padded Dog Harness', 'Front-clip no-pull harness with soft padded chest panel. Evenly distributes pressure so walks are comfortable and controlled.', 599.00, 20, 'Accessories', 'https://loremflickr.com/640/640/dog,harness?lock=7', '2026-08-12 10:00:00'),
(8, 13, 'Dogs', 'Retractable Dog Leash (5m)', 'Smooth retractable leash with ergonomic grip, one-handed brake and strong tangle-free tape. Great for daily walks.', 449.00, 28, 'Accessories', 'https://loremflickr.com/640/640/dog,leash?lock=8', '2026-08-20 10:00:00'),
(9, 3, 'Dogs', 'Slicker Grooming Brush for Dogs', 'Gentle slicker brush removes loose fur, mats and tangles while distributing natural oils for a shiny, healthy coat.', 279.00, 18, 'Grooming', 'https://loremflickr.com/640/640/dog,grooming?lock=9', '2026-07-28 10:00:00'),
(10, 11, 'Dogs', 'Orthopedic Memory Foam Dog Bed', 'Pressure-relieving memory foam bed with supportive bolsters for dogs who love to curl up. Removable, washable cover.', 1899.00, 12, 'Beds & Comfort', 'https://loremflickr.com/640/640/dog,bed?lock=10', '2026-07-18 10:00:00'),
(11, 3, 'Dogs', 'Self-Cooling Dog Mat', 'Hydrated gel cooling mat keeps your dog cool on hot summer days. Non-toxic, chew-resistant and easy to clean.', 849.00, 15, 'Beds & Comfort', 'https://loremflickr.com/640/640/dog,bed?lock=11', '2026-09-02 10:00:00'),
(12, 13, 'Dogs', 'Waterproof Pet Raincoat', 'Waterproof lined raincoat with hood and reflective strips for rainy walks. Easy velcro and snap closures.', 749.00, 14, 'Clothing / Pet Wear', 'https://loremflickr.com/640/640/dog,rain?lock=12', '2026-09-05 10:00:00'),
-- CATS
(13, 4, 'Cats', 'Adult Cat Food – Salmon & Rice (8kg)', 'Premium adult cat food with ocean salmon and rice. Omega-3 for healthy skin and coat plus taurine for heart health.', 1350.00, 50, 'Food', 'https://loremflickr.com/640/640/cat,food?lock=13', '2026-08-01 10:00:00'),
(14, 13, 'Cats', 'Kitten Growth Food – Chicken (2kg)', 'Nutrient-dense kitten food with chicken, DHA and antioxidants to support rapid growth and a strong immune system.', 950.00, 40, 'Food', 'https://loremflickr.com/640/640/cat,kitten?lock=14', '2026-08-18 10:00:00'),
(15, 4, 'Cats', 'Salmon Cat Treats (60g)', 'Freeze-dried salmon training treats packed with protein. Single-ingredient and grain-free – cats go crazy for them.', 299.00, 55, 'Treats', 'https://loremflickr.com/640/640/cat,treat?lock=15', '2026-08-08 10:00:00'),
(16, 4, 'Cats', 'Feather Wand Cat Toy', 'Interactive feather wand with replaceable attachments. Triggers your cat''s natural hunting instincts for active play.', 199.00, 30, 'Toys', 'https://loremflickr.com/640/640/cat,toy?lock=16', '2026-07-30 10:00:00'),
(17, 4, 'Cats', 'Collapsible Cat Tunnel', 'Crinkle and peek-a-boo tunnel cats love to pounce through. Folds flat for easy storage, with hanging toy.', 549.00, 22, 'Toys', 'https://loremflickr.com/640/640/cat,toy?lock=17', '2026-09-01 10:00:00'),
(18, 13, 'Cats', 'Catnip-Filled Mouse Toy (3-pack)', 'Organic catnip stuffed mice with crinkle texture and rattling sound. The perfect boredom buster for indoor cats.', 149.00, 45, 'Toys', 'https://loremflickr.com/640/640/cat,toy?lock=18', '2026-08-22 10:00:00'),
(19, 4, 'Cats', 'Breakaway Cat Collar with Bell', 'Safe breakaway buckle collar with soft bell so you always know where your cat is. Adjustable and lightweight.', 289.00, 26, 'Accessories', 'https://loremflickr.com/640/640/cat,collar?lock=19', '2026-08-14 10:00:00'),
(20, 11, 'Cats', 'Soft-Sided Cat Carrier', 'Comfortable airline-approved carrier with cozy cushion, mesh windows and shoulder strap for travel and vet visits.', 1499.00, 10, 'Accessories', 'https://loremflickr.com/640/640/cat,carrier?lock=20', '2026-07-22 10:00:00'),
(21, 3, 'Cats', 'Cat Grooming Glove Brush', 'Silicone grooming glove that removes loose hair while your cat gets a soothing massage. Great for bonding.', 349.00, 24, 'Grooming', 'https://loremflickr.com/640/640/cat,grooming?lock=21', '2026-08-26 10:00:00'),
(22, 11, 'Cats', 'Plush Donut Cat Bed', 'Soft plush donut-shaped bed with raised rim for head support and a sense of security. Machine washable.', 999.00, 18, 'Beds & Comfort', 'https://loremflickr.com/640/640/cat,bed?lock=22', '2026-08-16 10:00:00'),
(23, 13, 'Cats', 'Clumping Cat Litter (10L)', 'Fast-clumping, low-dust bentonite litter with odor-lock technology. Easy scooping and 99% dust-free.', 545.00, 60, 'Care', 'https://loremflickr.com/640/640/cat,litter?lock=23', '2026-07-15 10:00:00'),
(58, 4, 'Cats', 'Cat Litter Scoop with Storage', 'Sturdy extra-large litter scoop that rests in its own holding base with slots to let clumps drain.', 159.00, 40, 'Cleaning', 'https://loremflickr.com/640/640/cat,litter?lock=58', '2026-09-04 10:00:00'),
(63, 4, 'Cats', 'Cat Scratch Post with Dangling Toy', 'Tall sisal scratching post with perches and a dangling pom-pom toy. Saves your furniture and gives cats an outlet.', 1299.00, 11, 'Toys', 'https://loremflickr.com/640/640/cat,scratching?lock=63', '2026-09-08 10:00:00'),
(67, 11, 'Cats', 'Cozy Faux-Fur Cat Blanket', 'Ultra-soft faux-fur throw blanket for cats of all ages. Warm, machine-washable and perfect for window naps.', 549.00, 13, 'Beds & Comfort', 'https://loremflickr.com/640/640/cat,blanket?lock=67', '2026-09-12 10:00:00'),
-- BIRDS
(24, 5, 'Birds', 'Premium Bird Seed Mix (1kg)', 'Hand-mixed seed blend of millet, canary seed and oats for budgies and finches. No fillers, full of nutrition.', 320.00, 35, 'Food', 'https://loremflickr.com/640/640/bird,food?lock=24', '2026-07-29 10:00:00'),
(25, 5, 'Birds', 'Millet Spray (Pack of 6)', 'Fresh golden millet sprays that birds love to snack on. A natural treat rich in protein and fiber.', 180.00, 40, 'Treats', 'https://loremflickr.com/640/640/bird,food?lock=25', '2026-08-19 10:00:00'),
(26, 5, 'Birds', 'Wooden Bird Swing', 'Smooth wooden swing with knotted cotton rope for parakeets and small parrots. Encourages climbing and exercise.', 220.00, 16, 'Toys', 'https://loremflickr.com/640/640/bird,toy?lock=26', '2026-08-23 10:00:00'),
(27, 5, 'Birds', 'Bird Mirror Toy with Bell', 'Shiny mirror toy with a jingle bell; gives your bird hours of self-entertainment and vocal practice.', 175.00, 22, 'Toys', 'https://loremflickr.com/640/640/bird,toy?lock=27', '2026-08-28 10:00:00'),
(28, 13, 'Birds', 'Hanging Bird Feeder Cup', 'Easy-mount hanging cup feeder with perch ring, dishwasher safe. Holds seed, fruit or treats.', 260.00, 20, 'Accessories', 'https://loremflickr.com/640/640/bird,feeder?lock=28', '2026-09-07 10:00:00'),
(29, 5, 'Birds', 'Spacious Parrot Bird Cage', 'Large corner bird cage with removable tray, stainless dishes and two perches. Ideal for parrots and budgies.', 4599.00, 8, 'Cages / Habitats', 'https://loremflickr.com/640/640/parrot,cage?lock=29', '2026-07-17 10:00:00'),
(30, 13, 'Birds', 'Ceramic Bird Bath', 'Sturdy ceramic bird bath with textured grip so small birds can bathe safely. Easy to clean.', 399.00, 12, 'Care', 'https://loremflickr.com/640/640/bird,bath?lock=30', '2026-08-25 10:00:00'),
(31, 5, 'Birds', 'Natural Wood Bird Perch', 'Unfinished natural wood perch with adjustable clamp, great for foot health and exercise.', 145.00, 30, 'Accessories', 'https://loremflickr.com/640/640/bird,perch?lock=31', '2026-09-09 10:00:00'),
(61, 5, 'Birds', 'Bird Cuttlebone with Holder', 'Calcium-rich cuttlebone with stainless holder – supports beak health and strong bones.', 99.00, 42, 'Health & Wellness', 'https://loremflickr.com/640/640/bird,health?lock=61', '2026-09-15 10:00:00'),
-- RABBITS
(32, 6, 'Rabbits', 'Rabbit Pellets – Timothy Blend (4kg)', 'High-fiber timothy-based pellets formulated to support dental health and healthy digestion in adult rabbits.', 499.00, 30, 'Food', 'https://loremflickr.com/640/640/rabbit,food?lock=32', '2026-08-04 10:00:00'),
(33, 13, 'Rabbits', 'Premium Timothy Hay (1kg)', 'Sweet, green timothy hay full of fiber essential for dental wear and gut health. Harvested fresh twice a year.', 349.00, 50, 'Food', 'https://loremflickr.com/640/640/rabbit,hay?lock=33', '2026-08-11 10:00:00'),
(34, 6, 'Rabbits', 'Wooden Rabbit Chew Toy Bundle', 'Untreated applewood and willow chews that keep rabbit teeth trim and boredom far away.', 189.00, 25, 'Toys', 'https://loremflickr.com/640/640/rabbit,toy?lock=34', '2026-08-21 10:00:00'),
(35, 6, 'Rabbits', 'Rabbit Tunnel Play Set', 'Expandable pop-up tunnel with chew-safe mesh windows – binkies and zoomies are guaranteed.', 649.00, 15, 'Toys', 'https://loremflickr.com/640/640/rabbit,toy?lock=35', '2026-09-03 10:00:00'),
(36, 6, 'Rabbits', 'Adjustable Rabbit Harness & Leash', 'Secure H-style harness with soft padding, sized for rabbits. Includes 1.5m leash for safe outdoor time.', 379.00, 12, 'Accessories', 'https://loremflickr.com/640/640/rabbit,harness?lock=36', '2026-08-29 10:00:00'),
(37, 3, 'Rabbits', 'Rabbit Grooming Brush', 'Double-sided brush with fine and wide teeth for gentle shedding control on sensitive rabbit coats.', 219.00, 18, 'Grooming', 'https://loremflickr.com/640/640/rabbit,grooming?lock=37', '2026-09-10 10:00:00'),
(65, 6, 'Rabbits', 'Rabbit Water Bottle (500ml)', 'No-drip hanging bottle with ball bearing valve. Leak-resistant and easy to refill.', 159.00, 21, 'Care', 'https://loremflickr.com/640/640/rabbit,bottle?lock=65', '2026-09-16 10:00:00'),
-- HAMSTERS
(38, 6, 'Hamsters', 'Hamster Food Mix (1kg)', 'Seed, grain and vegetable mix that mirrors a hamster''s natural diet. Enriched with vitamins A and E.', 259.00, 40, 'Food', 'https://loremflickr.com/640/640/hamster,food?lock=38', '2026-08-06 10:00:00'),
(39, 6, 'Hamsters', 'Silent Hamster Exercise Wheel', 'Large 21cm silent running wheel with solid running surface – protected paws and quiet night runs.', 449.00, 20, 'Toys', 'https://loremflickr.com/640/640/hamster,wheel?lock=39', '2026-08-24 10:00:00'),
(40, 7, 'Hamsters', 'Hamster Tunnel Tubes (Set of 5)', 'Connecting transparent tunnel tubes with chew-proof rings to expand any hamster habitat.', 279.00, 24, 'Cages / Habitats', 'https://loremflickr.com/640/640/hamster,toy?lock=40', '2026-08-30 10:00:00'),
(41, 6, 'Hamsters', 'Cozy Hamster Hideout House', 'Wooden hideout house with two exits and a soft interior pad for naps and nesting.', 329.00, 19, 'Cages / Habitats', 'https://loremflickr.com/640/640/hamster,house?lock=41', '2026-09-06 10:00:00'),
(42, 7, 'Hamsters', 'Soft Paper Hamster Bedding (5L)', 'Dust-free, highly absorbent paper bedding that is 99% dust-free and safe for burrowing.', 299.00, 30, 'Care', 'https://loremflickr.com/640/640/hamster,bedding?lock=42', '2026-08-27 10:00:00'),
(60, 7, 'Hamsters', 'Hamster Water Bottle (120ml)', 'Small-volume gravity bottle with sipper tube mounted by a secure clamp for hamster cages.', 129.00, 30, 'Care', 'https://loremflickr.com/640/640/hamster,bottle?lock=60', '2026-09-11 10:00:00'),
-- GUINEA PIGS
(43, 6, 'Guinea Pigs', 'Guinea Pig Food Pellets (3kg)', 'Vitamin-C fortified pellets that guinea pigs need daily for good health. No added sugar, naturally tasty.', 399.00, 28, 'Food', 'https://loremflickr.com/640/640/guineapig,food?lock=43', '2026-08-03 10:00:00'),
(44, 13, 'Guinea Pigs', 'Orchard Grass Hay (2kg)', 'Soft, fragrant orchard grass hay packed with fiber – a staple for guinea pig digestive health.', 349.00, 26, 'Food', 'https://loremflickr.com/640/640/guineapig,hay?lock=44', '2026-08-13 10:00:00'),
(45, 6, 'Guinea Pigs', 'Guinea Pig Chew Toy (Wooden Set)', 'Natural wood chews with safe hanging rope to grind those continuously growing teeth.', 175.00, 22, 'Toys', 'https://loremflickr.com/640/640/guineapig,toy?lock=45', '2026-08-31 10:00:00'),
(46, 7, 'Guinea Pigs', 'Guinea Pig Hide House (Wooden)', 'Two-door wooden hide hut for cozy naps and privacy. Chew-safe, non-toxic finish.', 599.00, 14, 'Cages / Habitats', 'https://loremflickr.com/640/640/guineapig,house?lock=46', '2026-09-13 10:00:00'),
(66, 13, 'Guinea Pigs', 'Guinea Pig Vitamin-C Supplement Drops', 'Easy liquid vitamin C supplement that keeps scurvy and deficiencies at bay – add to fresh water daily.', 349.00, 17, 'Health & Wellness', 'https://loremflickr.com/640/640/guineapig,vitamin?lock=66', '2026-09-19 10:00:00'),
-- FISH
(47, 8, 'Fish', 'Tropical Fish Flakes Food (250g)', 'High-protein multi-flake food for tropical fish. Floating and sinking flakes with vitamins and color enhancers.', 275.00, 45, 'Food', 'https://loremflickr.com/640/640/fish,aquarium?lock=47', '2026-08-07 10:00:00'),
(48, 8, 'Fish', 'Natural Aquarium Gravel (2kg)', 'Rounded, pre-washed natural gravel that is safe for plants and fish. Ideal for planted and community tanks.', 325.00, 30, 'Care', 'https://loremflickr.com/640/640/aquarium,gravel?lock=48', '2026-08-17 10:00:00'),
(49, 8, 'Fish', 'Fine Mesh Aquarium Fish Net', 'Soft fine-mesh net with gentle rounded rim for safe handling, feeding and transferring of fish.', 149.00, 35, 'Accessories', 'https://loremflickr.com/640/640/aquarium,fish?lock=49', '2026-08-30 10:00:00'),
(50, 8, 'Fish', 'Silk Aquarium Plant (Tall)', 'Natural-looking silk plant with weighted base – adds lush green hiding spots without real-plant upkeep.', 219.00, 24, 'Accessories', 'https://loremflickr.com/640/640/aquarium,plant?lock=50', '2026-09-01 10:00:00'),
(51, 8, 'Fish', 'Aquarium Glass Cleaner Kit', 'Magnetic algae scraper and sponge cleaner pair for streak-free glass without wet hands.', 379.00, 12, 'Cleaning', 'https://loremflickr.com/640/640/aquarium,cleaning?lock=51', '2026-09-05 10:00:00'),
(64, 8, 'Fish', 'Aquarium Water Test Strips (50 pack)', 'Slim dip-and-read test strips for pH, ammonia, nitrite and hardness – essential for a healthy tank.', 899.00, 16, 'Health & Wellness', 'https://loremflickr.com/640/640/aquarium,water?lock=64', '2026-09-18 10:00:00'),
-- TURTLES
(52, 9, 'Turtles', 'Turtle Feeding Pellets (500g)', 'Complete nutrition stackable pellets with calcium and vitamin D3 for aquatic turtles of all ages.', 349.00, 22, 'Food', 'https://loremflickr.com/640/640/turtle,food?lock=52', '2026-08-09 10:00:00'),
(53, 9, 'Turtles', 'Turtle Basking Platform Dock', 'Sturdy floating basking dock with ramp – lets your turtle climb out, dry off, and soak up the UV light.', 799.00, 10, 'Cages / Habitats', 'https://loremflickr.com/640/640/turtle,aquarium?lock=53', '2026-08-26 10:00:00'),
(54, 9, 'Turtles', 'Turtle Habitat River Rocks', 'Large, smooth river rocks sized for turtles to climb and lounge. Great for basking areas.', 499.00, 14, 'Care', 'https://loremflickr.com/640/640/turtle,rocks?lock=54', '2026-09-07 10:00:00'),
-- REPTILES
(55, 9, 'Reptiles', 'Reptile Food – Gut-Loaded Crickets (50 pc)', 'Live, gut-loaded crickets dusted with calcium – a staple feeder for geckos, bearded dragons and other reptiles.', 429.00, 18, 'Food', 'https://loremflickr.com/640/640/reptile,lizard?lock=55', '2026-08-14 10:00:00'),
(56, 9, 'Reptiles', 'Reptile Hide Cave (Natural)', 'Resin cave hide that mimics rocky terrain – essential for stress-free shedding and daytime naps.', 549.00, 12, 'Cages / Habitats', 'https://loremflickr.com/640/640/reptile,lizard?lock=56', '2026-08-29 10:00:00'),
(57, 9, 'Reptiles', 'Reptile Heat Lamp with Ceramic Bulb', 'Reliable heat lamp with ceramic emitter for consistent basking temperatures in reptile enclosures.', 899.00, 9, 'Health & Wellness', 'https://loremflickr.com/640/640/reptile,lamp?lock=57', '2026-09-06 10:00:00'),
(70, 9, 'Reptiles', 'Reptile Fine-Mist Spray Bottle', 'Adjustable fine-mist bottle for humidity control and daily misting of desert and tropical reptiles.', 249.00, 20, 'Care', 'https://loremflickr.com/640/640/reptile,spray?lock=70', '2026-09-20 10:00:00'),
-- SMALL PETS / OTHER + MISC
(59, 3, 'Dogs', 'Dog Poop Bag Dispenser with Handle', 'Carrying bag dispenser with 15 compostable bags and attached handle for mess-free walks.', 199.00, 38, 'Cleaning', 'https://loremflickr.com/640/640/dog,walk?lock=59', '2026-09-08 10:00:00'),
(62, 13, 'Dogs', 'Dog Training Clicker with Wrist Strap', 'Crisp-sound clicker for positive-reinforcement training with an adjustable wrist strap. Comes with training guide.', 149.00, 33, 'Training', 'https://loremflickr.com/640/640/dog,training?lock=62', '2026-09-14 10:00:00'),
(68, 2, 'Dogs', 'Dog Travel Water Bottle', 'One-hand hydration bottle with integrated drinking bowl – perfect for travel, hikes and park days.', 349.00, 25, 'Accessories', 'https://loremflickr.com/640/640/dog,water?lock=68', '2026-09-17 10:00:00'),
(69, 7, 'Other', 'Small Animal Starter Kit', 'All-in-one starter habitat kit for small pets – bedding, hideout, bowl and bottle included.', 1799.00, 9, 'Cages / Habitats', 'https://loremflickr.com/640/640/hamster,rabbit?lock=69', '2026-09-21 10:00:00');

-- ------------------------------------------------------------
-- ORDERS  (seeded so demo buyers see history & ratings exist)
-- ------------------------------------------------------------
INSERT INTO orders (id, order_ref, buyer_id, status, total_amount, shipping_address, payment_method, payment_status, created_at) VALUES
 (1, 'LM1000001', 14, 'DELIVERED', 6250.00, 'B-41, Green Park Apartments, Chennai, Tamil Nadu 600001', 'CARD', 'PAID', '2026-08-08 14:30:00'),
 (2, 'LM1000002', 14, 'SHIPPED',   1023.00, 'B-41, Green Park Apartments, Chennai, Tamil Nadu 600001', 'UPI', 'PAID', '2026-09-12 18:45:00'),
 (3, 'LM1000003', 15, 'DELIVERED', 4349.00, '12A, Lake View Road, Pune, Maharashtra 411001', 'COD', 'PAID', '2026-08-20 11:10:00');

INSERT INTO order_items (id, order_id, product_id, quantity, unit_price) VALUES
 (1, 1, 1,  2, 2450.00),
 (2, 1, 13, 1, 1350.00),
 (3, 2, 16, 1, 199.00),
 (4, 2, 23, 1, 545.00),
 (5, 2, 9,  1, 279.00),
 (6, 3, 1,  1, 2450.00),
 (7, 3, 10, 1, 1899.00);

-- ------------------------------------------------------------
-- REVIEWS
-- ------------------------------------------------------------
INSERT INTO reviews (id, product_id, user_id, order_item_id, rating, comment, created_at) VALUES
 (1,  1,  14, 1, 5, 'Excellent quality, my Labrador absolutely loves this food. Coat is shinier now.', '2026-08-15 09:00:00'),
 (2,  13, 14, 2, 4, 'Good food, the cats are happy and digestion seems fine.', '2026-08-20 17:20:00'),
 (3,  16, 14, 3, 5, 'Hours of fun for my kitten! Feathers are durable and well attached.', '2026-09-13 10:30:00'),
 (4,  23, 14, 4, 4, 'Great clumping litter, low dust. Value for money.', '2026-09-14 19:00:00'),
 (5,  9,  14, 5, 3, 'Decent brush, works fine but handle could be more comfortable.', '2026-09-15 08:45:00'),
 (6,  1,  15, 6, 4, 'Good value for money, my dog eats it without fuss.', '2026-08-25 12:00:00'),
 (7,  10, 15, 7, 5, 'Super comfy bed, my dog sleeps the whole night in it.', '2026-08-28 21:15:00');

-- ------------------------------------------------------------
-- WISHLIST  (demo buyer)
-- ------------------------------------------------------------
INSERT INTO wishlist (id, user_id, created_at) VALUES (1, 14, '2026-08-10 10:00:00');

INSERT INTO wishlist_items (id, wishlist_id, product_id, created_at) VALUES
 (1, 1, 1,  '2026-08-10 10:00:00'),
 (2, 1, 16, '2026-08-10 10:05:00');
