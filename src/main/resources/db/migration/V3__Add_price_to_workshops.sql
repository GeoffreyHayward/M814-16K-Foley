-- Add price column to workshops table
ALTER TABLE workshops ADD COLUMN price DECIMAL(10, 2) NOT NULL DEFAULT 0.00;

-- Update existing workshops with sample prices
UPDATE workshops SET price = 25.00 WHERE name = 'Sustainable Gardening Basics';
UPDATE workshops SET price = 50.00 WHERE name = 'Solar Panel Installation';
UPDATE workshops SET price = 35.00 WHERE name = 'Zero Waste Cooking';
UPDATE workshops SET price = 45.00 WHERE name = 'Beekeeping for Beginners';
UPDATE workshops SET price = 40.00 WHERE name = 'Rainwater Harvesting Systems';
UPDATE workshops SET price = 30.00 WHERE name = 'Composting Techniques';
UPDATE workshops SET price = 20.00 WHERE name = 'DIY Natural Cleaning Products';

-- Remove the default constraint after updating existing records
ALTER TABLE workshops ALTER COLUMN price DROP DEFAULT;