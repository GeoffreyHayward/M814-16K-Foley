-- Create workshops table
CREATE TABLE workshops (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    date_time TIMESTAMP NOT NULL,
    capacity INTEGER NOT NULL,
    current_bookings INTEGER NOT NULL
);

-- Insert demo data for workshops
-- Upcoming workshops with available slots
INSERT INTO workshops (name, description, date_time, capacity, current_bookings)
VALUES (
    'Sustainable Gardening Basics',
    'Learn the fundamentals of sustainable gardening practices, including composting, water conservation, and organic pest control. This workshop is perfect for beginners who want to start their own eco-friendly garden.',
    NOW() + INTERVAL '7 days',
    20,
    5
);

INSERT INTO workshops (name, description, date_time, capacity, current_bookings)
VALUES (
    'Solar Panel Installation',
    'A hands-on workshop where you''ll learn how to install and maintain solar panels for residential use. We''ll cover the basics of solar energy, system components, and installation techniques.',
    NOW() + INTERVAL '14 days',
    15,
    10
);

INSERT INTO workshops (name, description, date_time, capacity, current_bookings)
VALUES (
    'Zero Waste Cooking',
    'Discover how to reduce food waste in your kitchen through proper storage, creative recipes, and composting. You''ll learn practical tips for sustainable cooking and meal planning.',
    NOW() + INTERVAL '21 days',
    25,
    15
);

-- Upcoming workshop that is fully booked
INSERT INTO workshops (name, description, date_time, capacity, current_bookings)
VALUES (
    'Beekeeping for Beginners',
    'An introduction to beekeeping for those interested in starting their own hive. Learn about bee biology, hive management, and honey harvesting in this popular workshop.',
    NOW() + INTERVAL '10 days',
    12,
    12
);

-- Upcoming workshop with just 1 slot left
INSERT INTO workshops (name, description, date_time, capacity, current_bookings)
VALUES (
    'Rainwater Harvesting Systems',
    'Learn how to design and build your own rainwater harvesting system. This workshop covers collection methods, filtration, storage, and usage of rainwater for various household purposes.',
    NOW() + INTERVAL '30 days',
    15,
    14
);

-- Past workshops
INSERT INTO workshops (name, description, date_time, capacity, current_bookings)
VALUES (
    'Composting Techniques',
    'A comprehensive guide to different composting methods suitable for various living situations, from apartment balconies to large gardens.',
    NOW() - INTERVAL '14 days',
    20,
    18
);

INSERT INTO workshops (name, description, date_time, capacity, current_bookings)
VALUES (
    'DIY Natural Cleaning Products',
    'Make your own eco-friendly cleaning products using simple, non-toxic ingredients. Reduce plastic waste and harmful chemicals in your home.',
    NOW() - INTERVAL '30 days',
    25,
    22
);