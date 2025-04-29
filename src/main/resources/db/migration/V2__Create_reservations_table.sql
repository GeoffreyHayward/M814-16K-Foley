-- Create reservations table
CREATE TABLE reservations (
    id SERIAL PRIMARY KEY,
    workshop_id INTEGER NOT NULL,
    attendee_name VARCHAR(255) NOT NULL,
    attendee_email VARCHAR(255) NOT NULL,
    reservation_date TIMESTAMP NOT NULL,
    FOREIGN KEY (workshop_id) REFERENCES workshops(id)
);

-- Add index for faster lookups by workshop_id
CREATE INDEX idx_reservations_workshop_id ON reservations(workshop_id);

-- Add index for faster lookups by attendee_email
CREATE INDEX idx_reservations_attendee_email ON reservations(attendee_email);