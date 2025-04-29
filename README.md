# Walton Sustainability Centre

## Background
Each month my employer gives us a dedicated *free-play / learning* day. With the arrival of **JetBrains Junie**, I’m using 
today's (29 April 2025) free-play to see how well Junie handles a project where the **requirements themselves live under source control 
right alongside the code**.

The testbed is a [set of requirements my MSc tutor group wrote back in 2017](https://github.com/Open-University-PG-SWEng/M814-16K-Foley) (back when I was studying for my Masters): a set of functional 
and non-functional requirements for a fictional **Sustainability Centre**. By resurrecting that spec—and tracking every 
edit in Git—I can judge how Junie copes when the full story (code *and* evolving requirements) sits in one repo.

## About Walton Sustainability Centre

Walton Sustainability Centre is a fictional community-focused education venue in the UK that promotes sustainable living 
through practical, hands-on workshops. The Centre:

- **Delivers scheduled, fee-paying workshops** open to the public.
- **Runs an annual membership scheme** that grants discounted booking rates and other perks.
- **Processes payments in-house**, supporting card transactions and bank transfers while keeping invoicing up-to-date.
- **Maintains a small inventory** of teaching equipment and consumables, raising purchase orders as required.
- **Produces routine and ad-hoc management reports** for staff, trustees and auditors.
- **Ensures full compliance with the UK Data Protection Act**, enforcing strict access controls around personal and payment data.

| Note                                                                     |
|--------------------------------------------------------------------------|
| Chat-GPT managed to glean this description by scanning the requirements. |

## Where to find the requirements

| Scope | Location | Format |
|-------|----------|--------|
| **Functional requirements** | `WaltonSustainabilityCentre-Requirements.txt` (repository root) | Single plain-text file, one requirement per “REQ-ID” entry. |
| **Non-functional requirements** | `NonfunctionalRequirements/` | Folder of plain-text files (`NFR-x-*.txt`), one file per requirement plus a template for new items. |

Both sets are human-readable text so they can be diffed and reviewed like code. Any future updates should follow the same 
naming conventions (see example below) to keep the documentation consistent.

```text
REQ-ID:
Description:
Type:
Rationale:
Fit Criterion:
Originator:
Comments: [M814-ModuleChair] Add comments below, making sure to include your initials in [] at the start.
```

## Timeline for the day:
**9am—begin setting up the project**

- Forced original repo
- Adds SpringBoot via the Initializer.
- Made README with Chat-GPT to have a goal.
- Wrote the .junie/guidelines.md as an attempt to steer Junie's decisions.

**11am—begin coding**

1. Ask Chat-GPT what would be a good starting point.
```text
As you have read the requirements what REQ-ID do you think would be the best one to do first? At this point no code has been written, but Spring Boot has been set up. 
```
It responded with:
```text
Start with REQ-ID `FR-32 – “The product shall check the availability of the workshop.”...
```
I then asked Junie:
```text
Please implement REQ-ID: FR-32. As this project is new, you may add demo data so that workshops that are not in the past are displayed and indicate whether they are available or not, as per the requirement.
```
Junie then wrote the following files in the order given (top-down):
- application.properties
- The Workshop Entiey
- The Workshop Repository
- The Workshop DTO
- The Workshop Service
- The Workshop Controller
- main.html (a base UI layout)
- The Workshops list.html
- The Workshop available.html
- The Workshop details.html
- The Workshop availability.html
- A HomeController
- a home.html (homepage)
- V1_Create_workshops_table (flyway migration script) 
- (and then I didn't note anymore)

The full list of changes can be found [here](https://github.com/GeoffreyHayward/M814-16K-Foley/pull/1)

Here are the tests it added:
Here are the test method names from the image:
- testGetAvailableWorkshops()
- testGetAllWorkshops()
- testIsWorkshopAvailable()
- testGetUpcomingWorkshops()
- testGetAvailableSlots()
- testGetPastWorkshops()
- testGetWorkshopById()
- testGetFullyBookedWorkshops()

And running the application, I got a working website. Here is the homepage:

![screenshot-1.png](images/screenshot-1.png)

Junie also added a developer guide Markdown file and a user manual Markdown file.

**12pm - check the PR I created from Junie's work **
Here are my notes:
1. Oddly it disabled CSRF, I allow it, but keep an eye on this.
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configures the security filter chain.
     * For now, we're disabling CSRF protection and allowing all requests without authentication.
     *
     * @param http The HttpSecurity to configure
     * @return The configured SecurityFilterChain
     * @throws Exception If an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            );

        return http.build();
    }
}
```

2. It added some inline CSS in the main.html, I allow it for now (as only a test).
```html
<!-- Custom CSS -->
    <style>
        .workshop-card {
            height: 100%;
            transition: transform 0.3s;
        }
        .workshop-card:hover {
            transform: translateY(-5px);
        }
        .available {
            border-left: 5px solid #28a745;
        }
        .unavailable {
            border-left: 5px solid #dc3545;
        }
        .past {
            border-left: 5px solid #6c757d;
        }
    </style>
```

3. Happy to approve, as only testing and things look generally good. 

**12:30pm—Lunchtime**

Tasty.

**1pm—Implement Workshop Reservation Feature**
1. I asked Chat-GPT what to implment next.
```text
You can assume REQ-ID: FR-32 is now implemented. What would you say is the next requirement to implement? 
```
And Chat-GPT replayed with:
```text
Tackle REQ-ID FR-2 – “The product shall be able to reserve a chosen workshop.” ...
```
I then asked Junie:
```text
Please implement REQ-ID FR-2 – “The product shall be able to reserve a chosen workshop.” Feel free to add additional mock data to the database as needed.
```
And Junie wrote/updated the following files:
```text
developer-guide/workshop-reservation.md
src/main/java/run/geoffrey/walton/controller/ReservationController.java
src/main/java/run/geoffrey/walton/dto/ReservationDTO.java
src/main/java/run/geoffrey/walton/model/Reservation.java
src/main/java/run/geoffrey/walton/repository/ReservationRepository.java
src/main/java/run/geoffrey/walton/service/ReservationService.java
src/main/resources/db/migration/V2__Create_reservations_table.sql
src/main/resources/templates/reservations/cancel.html
developer-guide/workshop-reservation.md
src/main/java/run/geoffrey/walton/controller/ReservationController.java
src/main/java/run/geoffrey/walton/dto/ReservationDTO.java
src/main/java/run/geoffrey/walton/model/Reservation.java
src/main/java/run/geoffrey/walton/repository/ReservationRepository.java
src/main/java/run/geoffrey/walton/service/ReservationService.java
src/main/resources/db/migration/V2__Create_reservations_table.sql
src/main/resources/templates/reservations/cancel.html
src/main/resources/templates/reservations/confirmation.html
src/main/resources/templates/reservations/create.html
src/main/resources/templates/reservations/list.html
src/main/resources/templates/workshops/availability.html
src/main/resources/templates/workshops/detail.html
src/test/java/run/geoffrey/walton/service/ReservationServiceTest.java
user-manual/workshop-reservation.md

```
It did forget to link on 'book now' button, to which I promoted `Not all book now buttons have been updated` and Junie fixed it.

Junie had also created the test, manual and developer guide. 

**2pm—Check REQ-ID FR-2 PR**

The PR look okay, but I skimmed over it (of course I wouldn't do this for a real application). Looking at the PR, I noticed that there 
could be a gap in the requirements, around canceling a workshop. The user manual says:
```markdown
## Canceling a Reservation

If you need to cancel your reservation:

1. From the confirmation page, click the "Cancel Reservation" button.
2. On the cancellation page, review the details of your reservation.
3. Click "Confirm Cancellation" to cancel your reservation.
4. Your spot will be made available to others.
```
But, this button is only available on the booking conformation page. So I asked Chat-GPT:
```text
Given the reequipments, is there a requirement for viewing my bookings?
```
And Chat-GPT responded:
```text
The current specification stops at FR-32, and none of the listed functional requirements covers “let a customer view all their existing workshop bookings”.
```
I asked Chat-GPT to write the requirement, and it responded with:
```text
REQ-ID: FR-33 
Description: The product shall allow a customer to view a list of their current and past workshop bookings. 
Type: Functional requirement 
Rationale: Customers need to check their reservations, prepare for attendance and request changes without contacting staff. 
Originator: [Your Name] - Software Engineer Comments: Provides a self-service “My Bookings” view; presumes customer authentication is in place.
Comments:
```
This is a good starting place for a new requirement. 

Given the requirement was missing, I approved the PR. 

**2:30pm—REQ-ID FR-16**

I asked Chat-GPT:
```text
Okay, I'll look to add that to the backlog for team input. Given the current requirements, and now assuming FR-2 is done, what would be the next reasonable requirement to implement?  
```
Chat-GPT responded with:
```text
Next logical slice: implement REQ-ID FR-16 – “The product shall provide a booking summary before payment is made.” ...
```
So I asked Junie:
```text
Please  implement REQ-ID FR-16 – “The product shall provide a booking summary before payment is made.”
```
Junie made a small update but missed the need to update the DB table. So I provided Junie the exception message , and 
it continued working on the requirement. Then running it again, I found it had made a type error. Again sending it the 
exception message, Junie attempted to fix it again. But third time lucky, and the update is read for PR. 

**3:00pm—Out of office**
Not working on Walton Sustainability Centre.

**4:00pm—Check FR-16 PR**

I like that, Junie continued to add mock data. This indicates Junie has got a good grasp of what's what within this repo. 
```sql
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
```

The PR looks okay, so I approved it. 

**4:30pm—Fished for now**

See [demo video](https://youtu.be/lUCvoJtqzok).
